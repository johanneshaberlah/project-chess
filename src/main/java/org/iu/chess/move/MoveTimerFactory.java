package org.iu.chess.move;

import org.iu.chess.game.Game;
import org.iu.chess.game.GameTimingStrategy;
import org.iu.chess.game.frame.GameFrame;
import org.iu.chess.game.frame.GameFrameFactory;

import javax.swing.*;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class MoveTimerFactory {

  public static ScheduledFuture<?> createMoveTimer(Game game, GameFrame view, Optional<GameTimingStrategy> strategy) {
    if (!strategy.isPresent()) {
      return null;
    }
    return Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
      SwingUtilities.invokeLater(() -> view.updateTimersText(game.players().white().clock(), game.players().black().clock()));
      game.checkForTerminalGameState();
    }, 1, 1, TimeUnit.SECONDS);
  }
}
