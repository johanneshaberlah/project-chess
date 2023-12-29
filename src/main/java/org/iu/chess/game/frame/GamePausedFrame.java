package org.iu.chess.game.frame;

import com.google.common.base.Preconditions;

import javax.swing.*;
import java.awt.*;

public class GamePausedFrame extends JFrame {

  private GamePausedFrame(Runnable onSave, Runnable onContinue, Runnable onNewGame) {
    StyledGameButton sb = new StyledGameButton();
    setTitle("Schach - Spiel pausiert");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(265, 330);
    setResizable(false);
    setLocationRelativeTo(null);

    JPanel gamePausedPanel = new JPanel();
    gamePausedPanel.setLayout(new GridLayout(5, 1));
    gamePausedPanel.setBackground(new Color(218, 218, 218));

    JLabel gamePausedTitle = new JLabel("Spiel Pausiert");
    gamePausedTitle.setFont(new Font("Arial", Font.BOLD, 16));
    gamePausedTitle.setHorizontalAlignment(SwingConstants.CENTER);

    JButton saveGameButton = sb.createStyledButton("Spiel speichern");
    JButton continueGameButton = sb.createStyledButton("Spiel fortsetzen");
    JButton mainMenuButton = sb.createStyledButton("Neues Spiel");
    JButton exitGameButton = sb.createStyledButton("Spiel beenden");

    gamePausedPanel.add(gamePausedTitle);
    gamePausedPanel.add(saveGameButton);
    gamePausedPanel.add(continueGameButton);
    gamePausedPanel.add(mainMenuButton);
    gamePausedPanel.add(exitGameButton);

    saveGameButton.addActionListener(e -> onSave.run());
    continueGameButton.addActionListener(e -> onContinue.run());
    mainMenuButton.addActionListener(e -> onNewGame.run());
    exitGameButton.addActionListener(e -> System.exit(0));
    add(gamePausedPanel);
  }

  public static GamePausedFrame of(Runnable onSave, Runnable onContinue, Runnable onNewGame) {
    Preconditions.checkNotNull(onSave);
    Preconditions.checkNotNull(onContinue);
    Preconditions.checkNotNull(onNewGame);
    return new GamePausedFrame(onSave, onContinue, onNewGame);
  }
}
