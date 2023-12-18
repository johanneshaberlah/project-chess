package org.iu.chess.game;

import org.iu.chess.board.Board;
import org.iu.chess.board.BoardFactory;
import org.iu.chess.game.artificial.ArtificialPlayer;
import org.iu.chess.game.player.Player;
import org.iu.chess.game.player.PlayerClock;
import org.iu.chess.game.player.PlayerMove;
import org.iu.chess.game.player.PlayerTuple;
import org.iu.chess.move.IllegalMoveException;
import org.iu.chess.move.Move;
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
  }

  public void start() {
    playerWithColor(PieceColor.WHITE).clock().ifPresent(PlayerClock::beginMove);
  }

  public void performMove(PlayerMove move) throws IllegalMoveException, InvalidGameActionException {
    // Checks if the same player tries to move twice
    if (!nextMove().equals(players.playerColor(move.player()))) {
      throw InvalidGameActionException.create(move.move(), "It is not the turn of " + move.player());
    }
    // Finish the current move (stop clock, perform the move on the board, push it to the history)
    var targetPiece = position.pieceAt(move.move().to());
    position.performMove(move.move());
    moves.push(move);
    targetPiece.ifPresent(piece -> playerWithColor(piece.color()).loose(piece));

    // Prepare move for next player (Start Clock and if it is an artificial player, make the move)
    var otherPlayer = players.otherPlayer(move.player());
    move.player().clock().ifPresent(PlayerClock::finishMove);
    otherPlayer.clock().ifPresent(PlayerClock::beginMove);
    if (otherPlayer instanceof ArtificialPlayer artificialPlayer) {
      artificialPlayer.makeMove(this, players.playerColor(artificialPlayer));
    }
  }

  public PieceColor nextMove() {
    if (moves.isEmpty()) {
      return PieceColor.WHITE;
    }
    return players.playerColor(players.otherPlayer(moves.peek().player()));
  }

  public Stack<PlayerMove> moves() {
    // Since stacks are mutable we don't want to expose them publicly to avoid #clear calls
    var movesCopy = new Stack<PlayerMove>();
    moves.forEach(movesCopy::push);
    return movesCopy;
  }

  public Board position() {
    return position;
  }

  public Player playerWithColor(PieceColor color) {
    return color.equals(PieceColor.WHITE) ? players.white() : players.black();
  }

  public PlayerTuple players() {
    return players;
  }

  public Optional<GameTimingStrategy> timingStrategy() {
    return timingStrategy;
  }

  public static ChessGame withSelectedTime(int minutes, int increment) {
    return new ChessGame(
      Optional.of(new GameTimingStrategy(minutes, increment)),
      new PlayerTuple(
        new Player(
          "Weiß",
          new HashSet<>(),
          Optional.of(PlayerClock.fromStrategy(new GameTimingStrategy(minutes, increment)))
        ),
        new Player(
          "Schwarz",
          new HashSet<>(),
          Optional.of(PlayerClock.fromStrategy(new GameTimingStrategy(minutes, increment)))
        )
      ),
      new Stack<>(),
      BoardFactory.create("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")
    );
  }

  public static ChessGame startingPosition() {
    return new ChessGame(
      Optional.of(new GameTimingStrategy(3, 0)),
      new PlayerTuple(
        new Player(
          "Weiß",
          new HashSet<>(),
          Optional.of(PlayerClock.fromStrategy(new GameTimingStrategy(3, 0)))
        ),
        new Player(
          "Schwarz",
          new HashSet<>(),
          Optional.of(PlayerClock.fromStrategy(new GameTimingStrategy(3, 0)))
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
