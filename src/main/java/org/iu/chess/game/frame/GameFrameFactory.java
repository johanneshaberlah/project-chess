package org.iu.chess.game.frame;

import org.iu.chess.board.BoardFactory;
import org.iu.chess.game.Game;
import org.iu.chess.game.GameTimingStrategy;
import org.iu.chess.move.LegalMovePreviewListener;
import org.iu.chess.move.MoveExecutionListener;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Optional;

public class GameFrameFactory {

  public static GameFrame of(Game game, Optional<GameTimingStrategy> strategy, Runnable onNewGame) {
    GameFrame gameFrame = GameFrame.of(game.position(), strategy, () -> {
      // On-New-Game
      GamePausedFrame frame = GamePausedFrame.of(() -> {
        Arrays.stream(GamePausedFrame.getFrames()).filter(pauseFrame -> pauseFrame.getClass().getSimpleName().equals(GamePausedFrame.class.getSimpleName()))
          .filter(Component::isVisible)
          .map(pauseFrame -> (GamePausedFrame) pauseFrame)
          .findFirst()
          .ifPresent(pauseFrame -> pauseFrame.saveToFile(BoardFactory.generateFEN(game.position())));
        // TODO
      }, () -> {
        // On-Continue
        game.restartClock();
        Arrays.stream(GamePausedFrame.getFrames()).filter(pauseFrame -> pauseFrame.getClass().getSimpleName().equals(GamePausedFrame.class.getSimpleName()))
          .forEach(Window::dispose);
      }, /* On New Game */ onNewGame);
      SwingUtilities.invokeLater(() -> frame.setVisible(true));
      game.pause();
    });
    gameFrame.panel().addMouseListener(MoveExecutionListener.of(gameFrame.panel(), game));
    gameFrame.panel().addMouseListener(LegalMovePreviewListener.of(gameFrame.panel(), game.position()));
    SwingUtilities.invokeLater(() -> gameFrame.setVisible(true));
    game.players().asSet().forEach(player -> player.lostPieces().addChangeListener(gameFrame::showLostPiece));
    return gameFrame;
  }
}
