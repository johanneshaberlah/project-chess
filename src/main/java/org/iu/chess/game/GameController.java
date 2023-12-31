package org.iu.chess.game;

import com.google.common.base.Preconditions;
import org.iu.chess.game.artificial.MinimaxPlayer;
import org.iu.chess.game.frame.GameEndFrame;
import org.iu.chess.game.frame.GameFrame;
import org.iu.chess.game.frame.GamePausedFrame;
import org.iu.chess.game.frame.GameStartFrame;
import org.iu.chess.game.player.PlayerClock;
import org.iu.chess.move.LegalMovePreviewListener;
import org.iu.chess.move.MoveExecutionListener;
import org.iu.chess.piece.PieceColor;

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
      frame.dispose();
    }
    GameStartFrame startFrame = GameStartFrame.of(context -> {
      Optional<GameTimingStrategy> strategy = Optional.ofNullable(context.time() == -1 ? null : GameTimingStrategy.of(context));
      startGame(strategy);
    });
    SwingUtilities.invokeLater(() -> startFrame.setVisible(true));
  }

  public void startGame(Optional<GameTimingStrategy> timing) {
    Game game = gameFactory.withTimingAndPlayer(timing, MinimaxPlayer.of(PieceColor.BLACK, timing.map(PlayerClock::fromStrategy)));

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
        Arrays.stream(GamePausedFrame.getFrames()).filter(pauseFrame -> pauseFrame.getClass().getSimpleName().equals(GamePausedFrame.class.getSimpleName()))
          .forEach(Window::dispose);
      }, /* On New Game */ this::gameStartMenu);
      SwingUtilities.invokeLater(() -> frame.setVisible(true));
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
    Arrays.stream(GameStartFrame.getFrames()).forEach(Window::dispose);
  }

  public static GameController of(GameFactory gameFactory) {
    Preconditions.checkNotNull(gameFactory);
    return new GameController(gameFactory);
  }
}
