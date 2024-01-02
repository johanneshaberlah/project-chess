package org.iu.chess.game.artificial;

import com.google.common.base.Preconditions;
import org.iu.chess.game.Game;
import org.iu.chess.game.InvalidGameActionException;
import org.iu.chess.game.player.PlayerClock;
import org.iu.chess.game.player.PlayerMove;
import org.iu.chess.game.termination.TerminalGameStateComponent;
import org.iu.chess.move.IllegalMoveException;
import org.iu.chess.move.Move;
import org.iu.chess.move.MoveAndValue;
import org.iu.chess.move.RelativeMoveWithRequirement;
import org.iu.chess.piece.PieceColor;

import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

public class MinimaxPlayer extends ArtificialPlayer {
  private final boolean PERFORM_AS_SIMULATION = true;

  private MinimaxPlayer(PieceColor pieceColor, Optional<PlayerClock> clock) {
    super("Minimax-Player", pieceColor, clock);
  }

  /**
   * Recommend a move in a given game.
   */
  @Override
  public Move recommendMove(Game game) {
    System.out.println(BoardEvaluator.evaluateBoard(game.position()));
    return minimax(game, null, 3).move();
  }

  private MoveAndValue minimax(Game game, Move base, int depth)  {
    if (depth == 0 || TerminalGameStateComponent.of(game).evaluateTerminalGameState().isPresent()) {
      return new MoveAndValue(base, BoardEvaluator.evaluateBoard(game.position()));
    }
    var moveComparator = Comparator.comparingInt(MoveAndValue::value);
    return legalMoves(game, game.nextMove())
      .map(move -> {
        Game simulation = game.clone();
        try {
          simulation.performMove(new PlayerMove(this, move), PERFORM_AS_SIMULATION);
        } catch (IllegalMoveException | InvalidGameActionException e) {
          throw new RuntimeException(e);
        }
        return minimax(simulation, base == null ? move : base, depth - 1);
      })
      .max(game.nextMove().isMaximizer() ? moveComparator : moveComparator.reversed())
      .get();
  }

  /**
   * Find all legal moves in a certain game for a certain player.
   *
   * @param game The affected game - will be cloned to avoid any mutations of the state.
   * @param player The player whose moves are to be found.
   * @return A list of all legal moves - tested using simulation boards.
   */
  private Stream<Move> legalMoves(Game game, PieceColor player) {
    return game.position()
      .piecesWithColor(player) /* Find all pieces with the requested color */
      .stream()
      .filter(square -> game.position().pieceAt(square).isPresent()) /* Double-Check if the Piece on the Square is present */
      .flatMap(square -> {
        /* Access the piece on the square (previously checked using filtration) */
        var piece = game.position().pieceAt(square).get();
        /* Find all reachable moves for the piece on the square using the vectorial representation without taking the state into account */
        return piece.reachableMoves().stream().map(RelativeMoveWithRequirement::move).map(move -> move.asLocalizedMove(square));
      })
      .filter(move -> testMoveValidity(game, move));  /* Test the validity of the move using a simulation board */
  }

  /**
   * Test whether a move is valid or not in a certain state by performing it on a simulation board.
   *
   * @param game The simulation board - it will be cloned within the body.
   * @param move The move to be tested.
   * @return Whether the move is valid or not.
   */
  private boolean testMoveValidity(Game game, Move move) {
    try {
      /* Clone the game to get a simulation to test moves on. */
      Game simulation = game.clone();
      /* Execute the move on the simulation board - throws Exception if the move is invalid in the state. */
      simulation.performMove(new PlayerMove(this, move), PERFORM_AS_SIMULATION);
      return true;
    } catch (IllegalMoveException | InvalidGameActionException illegalMove) {
      /* The game notifies that the move is invalid in the provided state. */
      return false;
    }
  }

  public static MinimaxPlayer of(PieceColor pieceColor, Optional<PlayerClock> clock) {
    Preconditions.checkNotNull(pieceColor);
    Preconditions.checkNotNull(clock);
    return new MinimaxPlayer(pieceColor, clock);
  }
}
