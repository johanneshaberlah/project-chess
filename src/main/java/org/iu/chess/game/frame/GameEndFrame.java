package org.iu.chess.game.frame;

import com.google.common.base.Preconditions;
import org.iu.chess.game.termination.TerminalGameState;
import org.iu.chess.game.termination.TerminalGameStateAndColor;
import org.iu.chess.piece.PieceColor;

import javax.swing.*;
import java.awt.*;

//TODO Wer hat gewonnen
//TODO Spiel beenden
//TODO getWinner() aus main game --> Surrender/Patt etc-
public class GameEndFrame extends JFrame {
  private TerminalGameStateAndColor gameState;

  private JButton exitGameButton;
  private JButton continueGameButton;
  private JButton mainMenuButton;

  private JPanel winnerDisplay;

  private GameEndFrame(TerminalGameStateAndColor gameState, Runnable onMainMenu) {
    this.gameState = gameState;
    StyledGameButton sb = new StyledGameButton();
    setTitle("Schach - Ende des Spiels");  //+chessGame.getWinner());
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(530, 330);
    setResizable(false);
    setLocationRelativeTo(null);

    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(1, 2));


    winnerDisplay = new JPanel();
    winnerDisplay.setLayout(new GridBagLayout());
    winnerDisplay.setBackground(new Color(218, 218, 218));

    JLabel gameEndImageLabel = new JLabel();
    // gameEndImageLabel.setIcon(getGameEndIcon());
    SwingUtilities.invokeLater(() -> {
      gameEndImageLabel.setIcon(getGameEndIcon());
      System.out.println("winnerDisplayWidth: " + winnerDisplay.getWidth());
    });
    gameEndImageLabel.setHorizontalAlignment(SwingConstants.CENTER);

    JLabel gameEndMessageLabel = new JLabel(getGameEndMessage());
    gameEndMessageLabel.setFont(new Font("Arial", Font.BOLD, 16));
    gameEndMessageLabel.setHorizontalAlignment(SwingConstants.CENTER);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.insets = new Insets(0, 0, 1, 0); // Add space below the image
    winnerDisplay.add(gameEndImageLabel, gbc);

    gbc.gridy = 1;
    winnerDisplay.add(gameEndMessageLabel, gbc);
    panel.add(winnerDisplay);

    JPanel gameEndButtons = new JPanel();
    gameEndButtons.setLayout(new GridLayout(3, 1));
    gameEndButtons.setBackground(new Color(218, 218, 218));

    exitGameButton = sb.createStyledButton("Spiel beenden");
    continueGameButton = sb.createStyledButton("Spiel fortsetzen");
    mainMenuButton = sb.createStyledButton("neues Spiel");

    gameEndButtons.add(exitGameButton);
    gameEndButtons.add(continueGameButton);
    gameEndButtons.add(mainMenuButton);

    panel.add(gameEndButtons);
    add(panel);

    exitGameButton.addActionListener(e -> System.exit(0));
    continueGameButton.addActionListener(e -> {
      // ?
    });
    mainMenuButton.addActionListener(e -> {
      this.dispose();
      onMainMenu.run();
    });

  }

  private ImageIcon getGameEndIcon() {
    if (gameState.color().isEmpty()) {
      // TODO: Handle draw
      return null;
    }
    ImageIcon icon = new ImageIcon(String.format("src/main/resources/pieces/%sking.png", gameState.color().get().name().toLowerCase()));
    Image image = icon.getImage();
    return (new ImageIcon(image.getScaledInstance((int) (winnerDisplay.getWidth() * 0.9), -1, Image.SCALE_SMOOTH)));
  }

  private String getGameEndMessage() {
    return gameState.toString();
  }

  public static GameEndFrame of(TerminalGameStateAndColor gameState, Runnable onMainMenu) {
    Preconditions.checkNotNull(gameState);
    Preconditions.checkNotNull(onMainMenu);
    return new GameEndFrame(gameState, onMainMenu);
  }
}
