package org.iu.chess.game.player;

import org.iu.chess.piece.PieceColor;

public record PlayerTuple(Player white, Player black) {

  public Player otherPlayer(Player player) {
    return player.equals(white) ? black : white;
  }

  public PieceColor playerColor(Player player) {
    return player.equals(white) ? PieceColor.WHITE : PieceColor.BLACK;
  }
}
