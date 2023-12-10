package org.iu.chess.game.artificial;

import org.iu.chess.game.ChessGame;
import org.iu.chess.game.player.Player;
import org.iu.chess.game.player.PlayerClock;
import org.iu.chess.piece.PieceColor;

import java.util.HashSet;
import java.util.Optional;

public class ArtificialPlayer extends Player {
  private ArtificialPlayer(Optional<PlayerClock> clock) {
    super("Computer", new HashSet<>(), clock);
  }

  public void makeMove(ChessGame chessGame, PieceColor pieceColor) {

  }

  public static ArtificialPlayer create(Optional<PlayerClock> clock) {
    return new ArtificialPlayer(clock);
  }
}
