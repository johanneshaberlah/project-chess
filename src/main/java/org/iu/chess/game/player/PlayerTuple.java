package org.iu.chess.game.player;

public record PlayerTuple(Player white, Player black) {

  public Player otherPlayer(Player player) {
    return player.equals(white) ? black : white;
  }
}
