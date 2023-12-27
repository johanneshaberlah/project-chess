package org.iu.chess.game;

import com.google.common.base.Preconditions;
import org.iu.chess.game.frame.GameFrame;
import org.iu.chess.game.frame.GameStartFrame;
import org.iu.chess.move.LegalMovePreviewListener;
import org.iu.chess.move.MoveExecutionListener;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GameController {
  private final GameFactory gameFactory;

  private GameController(GameFactory gameFactory) {
    this.gameFactory = gameFactory;
  }

  public void gameStartMenu() {
    GameStartFrame startFrame = GameStartFrame.of(context -> {
      Optional<GameTimingStrategy> strategy = Optional.of(GameTimingStrategy.of(context));
      startGame(strategy);
    });
    SwingUtilities.invokeLater(() -> startFrame.setVisible(true));
  }

  public void startGame(Optional<GameTimingStrategy> timing) {
    Game game = gameFactory.withTiming(timing);

    // Initiliaze GameFrame
    GameFrame gameFrame = GameFrame.of(game.position(), timing);
    gameFrame.panel().addMouseListener(MoveExecutionListener.of(gameFrame.panel(), game));
    gameFrame.panel().addMouseListener(LegalMovePreviewListener.of(gameFrame.panel(), game.position()));
    SwingUtilities.invokeLater(() -> gameFrame.setVisible(true));
    game.players().asSet().forEach(player -> player.lostPieces().addChangeListener(gameFrame::showLostPiece));

    // Start Game
    game.start();

    // Close all GameStartFrames
    for (Frame frame : GameStartFrame.getFrames()) {
      SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }

    // Start move timer
    timing.ifPresent(strategy -> Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
      SwingUtilities.invokeLater(() -> gameFrame.updateTimersText(game.players().white().clock(), game.players().black().clock()));
    }, 1, 1, TimeUnit.SECONDS));
  }

  public static GameController of(GameFactory gameFactory) {
    Preconditions.checkNotNull(gameFactory);
    return new GameController(gameFactory);
  }
}
