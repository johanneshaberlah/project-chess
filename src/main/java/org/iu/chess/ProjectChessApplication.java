package org.iu.chess;

import org.iu.chess.game.GameController;
import org.iu.chess.game.GameFactory;

public class ProjectChessApplication {

  public static void main(String[] args) {
    GameController gameController = GameController.of(GameFactory.create());
    gameController.gameStartMenu();
  }
}
