package org.iu.chess.game;

@FunctionalInterface
public interface GameStartListener {

  void onGameStart(GameStartContext startContext);

}
