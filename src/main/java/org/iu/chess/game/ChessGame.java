package org.iu.chess.game;

import org.iu.chess.board.Board;
import org.iu.chess.board.BoardFactory;
import org.iu.chess.game.artificial.ArtificialPlayer;
import org.iu.chess.game.player.Player;
import org.iu.chess.game.player.PlayerClock;
import org.iu.chess.game.player.PlayerMove;
import org.iu.chess.game.player.PlayerTuple;
import org.iu.chess.move.IllegalMoveException;
import org.iu.chess.piece.PieceColor;

import java.util.HashSet;
import java.util.Optional;
import java.util.Stack;

public class ChessGame {
  private final Optional<GameTimingStrategy> timingStrategy; // Empty if the game mode is "correspondence" (without timing)
  private final PlayerTuple players;
  private final Stack<PlayerMove> moves;
  private final Board position;

  public ChessGame(
    Optional<GameTimingStrategy> timingStrategy,
    PlayerTuple players,
    Stack<PlayerMove> moves,
    Board position
  ) {
    this.timingStrategy = timingStrategy;
    this.players = players;
    this.moves = moves;
    this.position = position;

    position.registerGameEndListener(winner -> {

    });
  }

  public void performMove(PlayerMove move) throws IllegalMoveException, InvalidGameActionException {
    // Checks if the same player tries to move twice
    if (!moves.empty() && move.player().equals(moves.peek().player())) {
      throw InvalidGameActionException.create(move.move(), "It is not the turn of " + move.player());
    }
    // Finish the current move (stop clock, perform the move on the board, push it to the history)
    move.player().clock().ifPresent(PlayerClock::finishMove);
    var targetPiece = position.pieceAt(move.move().to());
    position.performMove(move.move());
    moves.push(move);
    targetPiece.ifPresent(piece -> playerWithColor(piece.color()).loose(piece));

    // Prepare move for next player (Start Clock and if it is an artificial player, make the move)
    var otherPlayer = players.otherPlayer(move.player());
    otherPlayer.clock().ifPresent(PlayerClock::beginMove);
    if (otherPlayer instanceof ArtificialPlayer artificialPlayer) {
      artificialPlayer.makeMove(this, players.playerColor(artificialPlayer));
    }
  }
  public Board getPosition() {
    return position;
  }

  public Player playerWithColor(PieceColor color) {
    return color.equals(PieceColor.WHITE) ? players.white() : players.black();
  }

  public static ChessGame startingPosition() {
    return new ChessGame(
      Optional.empty(),
      new PlayerTuple(
        new Player(
          "Weiß",
          new HashSet<>(),
          Optional.empty()
        ),
        new Player(
          "Schwarz",
          new HashSet<>(),
          Optional.empty()
        )
      ),
      new Stack<>(),
      BoardFactory.create("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")
    );
  }

  public static ChessGame startingPositionWithComputer() {
    return new ChessGame(
      Optional.empty(),
      new PlayerTuple(
        new Player(
          "Weiß",
          new HashSet<>(),
          Optional.empty()
        ),
        ArtificialPlayer.create(Optional.empty())
      ),
      new Stack<>(),
      BoardFactory.create("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")
    );
  }
}
