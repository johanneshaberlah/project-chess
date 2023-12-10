package org.iu.chess.game.artificial;

import org.iu.chess.game.ChessGame;
import org.iu.chess.game.player.Player;

public class ArtificialPlayer extends Player {
  private ArtificialPlayer() {
    super("Computer", clock);
  }

  public void makeMove(ChessGame chessGame) {

  }

  public static ArtificialPlayer create() {
    return new ArtificialPlayer();
  }
}
