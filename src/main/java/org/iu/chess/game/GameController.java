package org.iu.chess.game;

import com.google.common.base.Preconditions;
import org.iu.chess.game.frame.GameEndFrame;
import org.iu.chess.game.frame.GameFrame;
import org.iu.chess.game.frame.GamePausedFrame;
import org.iu.chess.game.frame.GameStartFrame;
import org.iu.chess.move.LegalMovePreviewListener;
import org.iu.chess.move.MoveExecutionListener;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class GameController {
  private final GameFactory gameFactory;

  private GameController(GameFactory gameFactory) {
    this.gameFactory = gameFactory;
  }

  public void gameStartMenu() {
    for (Frame frame : GameFrame.getFrames()) {
      frame.setVisible(false);
      frame.dispose();
    }
    GameStartFrame startFrame = GameStartFrame.of(context -> {
      Optional<GameTimingStrategy> strategy = Optional.ofNullable(context.time() == -1 ? null : GameTimingStrategy.of(context));
      startGame(strategy);
    });
    SwingUtilities.invokeLater(() -> startFrame.setVisible(true));
  }

  public void startGame(Optional<GameTimingStrategy> timing) {
    Game game = gameFactory.withTiming(timing);

    // Initialize GameFrame
    ScheduledFuture<?> moveTimer;
    GameFrame gameFrame = GameFrame.of(game.position(), timing, () -> {
      // On-New-Game
      GamePausedFrame frame = GamePausedFrame.of(() -> {
        // On-Save
        // TODO
      }, () -> {
        // On-Continue
        game.restartClock();
        for (Frame pauseFrame : GamePausedFrame.getFrames()) {
          SwingUtilities.invokeLater(() -> pauseFrame.setVisible(true));
        }
      }, /* On New Game*/ this::gameStartMenu);
      frame.setVisible(true);
      game.pause();
    });
    gameFrame.panel().addMouseListener(MoveExecutionListener.of(gameFrame.panel(), game));
    gameFrame.panel().addMouseListener(LegalMovePreviewListener.of(gameFrame.panel(), game.position()));
    SwingUtilities.invokeLater(() -> gameFrame.setVisible(true));
    game.players().asSet().forEach(player -> player.lostPieces().addChangeListener(gameFrame::showLostPiece));

    if (timing.isPresent()) {
      moveTimer = Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
        SwingUtilities.invokeLater(() -> gameFrame.updateTimersText(game.players().white().clock(), game.players().black().clock()));
        game.checkForTerminalGameState();
      }, 1, 1, TimeUnit.SECONDS);
    } else {
      moveTimer = null;
    }

    game.registerGameEndHandler(gameState -> {
      if (moveTimer != null) {
        moveTimer.cancel(true);
      }
      SwingUtilities.invokeLater(() -> {
        GameEndFrame gameEndFrame = GameEndFrame.of(gameState, this::gameStartMenu);
        gameEndFrame.setVisible(true);
        gameFrame.setEnabled(false);
      });
    });
    game.start();
    for (Frame frame : GameStartFrame.getFrames()) {
      SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }
  }

  public static GameController of(GameFactory gameFactory) {
    Preconditions.checkNotNull(gameFactory);
    return new GameController(gameFactory);
  }
}
