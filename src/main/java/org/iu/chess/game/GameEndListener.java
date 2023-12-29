package org.iu.chess.game;

import org.iu.chess.game.termination.TerminalGameStateAndColor;

@FunctionalInterface
public interface GameEndListener {
  void onGameEnd(TerminalGameStateAndColor gameState);
}
