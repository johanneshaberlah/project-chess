package org.iu.chess.game;

import org.iu.chess.board.Board;
import org.iu.chess.game.artificial.ArtificialPlayer;
import org.iu.chess.game.player.Player;
import org.iu.chess.game.player.PlayerMove;
import org.iu.chess.game.player.PlayerTuple;
import org.iu.chess.move.IllegalMoveException;
import org.iu.chess.piece.PieceColor;

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

  public void performMove(PlayerMove move) throws IllegalMoveException, InvalidGameActionException {
    // Checks if the same player tries to move twice
    if (move.player().equals(moves.peek().player())) {
      throw InvalidGameActionException.create(move.move(), "It is not the turn of " + move.player());
    }
    position.performMove(move.move());
    moves.push(move);
    if (players.otherPlayer(move.player()) instanceof ArtificialPlayer artificialPlayer) {
      artificialPlayer.makeMove(this);
    }
  }

  public Player playerWithColor(PieceColor color) {
    return color.equals(PieceColor.WHITE) ? players.white() : players.black();
  }
}
