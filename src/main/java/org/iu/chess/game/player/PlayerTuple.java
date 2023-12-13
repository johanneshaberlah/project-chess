package org.iu.chess.game.player;

import org.iu.chess.piece.PieceColor;

import java.util.Set;

public record PlayerTuple(Player white, Player black) {

  public Player otherPlayer(Player player) {
    return player.equals(white) ? black : white;
  }

  public PieceColor playerColor(Player player) {
    return player.equals(white) ? PieceColor.WHITE : PieceColor.BLACK;
  }

  public Set<Player> asSet() {
    return Set.of(white, black);
  }
}
