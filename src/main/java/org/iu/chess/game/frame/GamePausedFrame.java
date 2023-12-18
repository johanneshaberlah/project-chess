package org.iu.chess.game.frame;

import javax.swing.*;
import java.awt.*;

public class GamePausedFrame extends JFrame {
  //buttons: save, new game, end game
  private JButton saveGameButton;
  private JButton continueGameButton;
  private JButton mainMenuButton;
  private JButton exitGameButton;

  public GamePausedFrame() {
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


    saveGameButton = sb.createStyledButton("Spiel speichern");
    continueGameButton = sb.createStyledButton("Spiel fortsetzen");
    mainMenuButton = sb.createStyledButton("neues Spiel");
    exitGameButton = sb.createStyledButton("Spiel beenden");

    gamePausedPanel.add(gamePausedTitle);
    gamePausedPanel.add(saveGameButton);
    gamePausedPanel.add(continueGameButton);
    gamePausedPanel.add(mainMenuButton);
    gamePausedPanel.add(exitGameButton);

    saveGameButton.addActionListener(e -> {
      System.out.println("save game");
    });
    continueGameButton.addActionListener(e -> {
      System.out.println("continue game");//add 3s delay before game start?
    });
    mainMenuButton.addActionListener(e -> {
      System.out.println("new game");
    });
    exitGameButton.addActionListener(e -> {
      System.out.println("exit game");
    });

    add(gamePausedPanel);

  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      GamePausedFrame gamePausedFrame = new GamePausedFrame();
      gamePausedFrame.setVisible(true);
    });
  }
}
