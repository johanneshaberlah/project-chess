package org.iu.chess.game;

import org.iu.chess.game.frame.GameEndFrame;
import org.iu.chess.game.frame.GameFrame;
import org.iu.chess.game.frame.GameFrameFactory;

import javax.swing.*;
import javax.swing.text.View;
import java.util.Optional;
import java.util.concurrent.ScheduledFuture;

public class GameEndListenerFactory {

  public static void registerGameEndListener(Game game, GameFrame view, Optional<ScheduledFuture<?>> moveTimer, Runnable onGameEnd) {
    game.registerGameEndHandler(gameState -> {
      moveTimer.ifPresent(timer -> timer.cancel(true));
      SwingUtilities.invokeLater(() -> {
        GameEndFrame gameEndFrame = GameEndFrame.of(gameState, onGameEnd);
        gameEndFrame.setVisible(true);
        view.setEnabled(false);
      });
    });
  }
}
