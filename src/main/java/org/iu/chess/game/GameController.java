package org.iu.chess.game;

import com.google.common.base.Preconditions;
import org.iu.chess.game.frame.GameFrame;
import org.iu.chess.game.frame.GameFrameFactory;
import org.iu.chess.game.frame.GameStartFrame;
import org.iu.chess.move.MoveTimerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ScheduledFuture;

public class GameController {
  private final GameFactory gameFactory;

  private GameController(GameFactory gameFactory) {
    this.gameFactory = gameFactory;
  }

  public void gameStartMenu() {
    for (Frame frame : GameFrame.getFrames()) {
      frame.dispose();
    }
    GameStartFrame startFrame = GameStartFrame.of(this::startGame);
    SwingUtilities.invokeLater(() -> startFrame.setVisible(true));
  }

  public void startGame(GameStartContext context) {
    // Read the strategy from the context
    Optional<GameTimingStrategy> strategy = Optional.ofNullable(context.time() == -1 ? null : GameTimingStrategy.of(context));
    // Create a new game
    Game game = gameFactory.of(context, strategy);
    // Initialize View
    GameFrame view = GameFrameFactory.of(game, strategy, this::gameStartMenu);
    // Create MoveTimer that updates the view based on the game timing model
    ScheduledFuture<?> moveTimer = MoveTimerFactory.createMoveTimer(game, view, strategy);
    // Register game end listeners so the game notifies the view when the game ends
    GameEndListenerFactory.registerGameEndListener(game, view, Optional.ofNullable(moveTimer), this::gameStartMenu);
    // Start the Game
    game.start();
    // Close all GameStartFrames
    Arrays.stream(GameStartFrame.getFrames()).forEach(Window::dispose);
  }

  public static GameController of(GameFactory gameFactory) {
    Preconditions.checkNotNull(gameFactory);
    return new GameController(gameFactory);
  }
}
