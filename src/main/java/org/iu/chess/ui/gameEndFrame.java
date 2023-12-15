package org.iu.chess.ui;

import javax.swing.*;
import java.awt.*;

  //TODO Wer hat gewonnen
  //TODO Spiel speichern
  //TODO Spiel beenden
  //TODO getWinner() aus main game --> Surrender/Patt etc-
public class gameEndFrame extends JFrame{

  private JButton exitGameButton;
  private JButton continueGameButton;
  private JButton mainMenuButton;

  private JPanel winnerDisplay;



  public gameEndFrame() {
    styledButton sb = new styledButton();
    setTitle("Schach - Ende des Spiels");  //+chessGame.getWinner());
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(530, 330);
    setResizable(false);
    setLocationRelativeTo(null);

    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(1, 2));

    /*JPanel winnerDisplay = new JPanel();
    winnerDisplay.setLayout(new GridLayout(2,1,0,-80));//find fitting layout vgap 10
    winnerDisplay.setBackground(new Color(218, 218, 218));

    JLabel gameEndImageLabel = new JLabel();
    gameEndImageLabel.setIcon(getGameEndIcon());
    gameEndImageLabel.setHorizontalAlignment(SwingConstants.CENTER);

    JLabel gameEndMessageLabel = new JLabel(getGameEndMessage());
    gameEndMessageLabel.setFont(new Font("Arial", Font.BOLD, 16));
    gameEndMessageLabel.setHorizontalAlignment(SwingConstants.CENTER);

    winnerDisplay.add(gameEndImageLabel);
    winnerDisplay.add(gameEndMessageLabel);*/

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
    //////////
    panel.add(winnerDisplay);

    JPanel gameEndButtons =new JPanel();
    gameEndButtons.setLayout(new GridLayout(3,1));
    gameEndButtons.setBackground(new Color(218, 218, 218));

    exitGameButton = sb.createStyledButton("Spiel beenden");
    continueGameButton = sb.createStyledButton("Spiel fortsetzen");
    mainMenuButton = sb.createStyledButton("neues Spiel");

    gameEndButtons.add(exitGameButton);
    gameEndButtons.add(continueGameButton);
    gameEndButtons.add(mainMenuButton);

    panel.add(gameEndButtons);
    add(panel);

    exitGameButton.addActionListener(e -> {
      System.out.println("exitGameButton pressed");
      //TODO exitGame();
    });
    continueGameButton.addActionListener(e -> {
      System.out.println("continueGameButton pressed");
      //TODO continueGame();
    });
    mainMenuButton.addActionListener(e -> {
      System.out.println("mainMenuButton pressed");
      //TODO mainMenu();
    });

  }

  private ImageIcon getGameEndIcon() {
    ImageIcon icon= new ImageIcon("src/main/resources/pieces/whiteking.png");
    Image image = icon.getImage();
    //TODO get image of king/ surrender flag/ patt/tie symbol->(both kings next to eachother maybe)
    System.out.println("winnerDisplayWidth: "+winnerDisplay.getWidth());
    System.out.println("winnerDisplayHeight: "+winnerDisplay.getHeight());

    return (new ImageIcon(image.getScaledInstance((int)(winnerDisplay.getWidth()*0.9),-1,Image.SCALE_SMOOTH)));
  }
  private String getGameEndMessage() {
    //TODO get message
    return "game ended- white wins";
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      gameEndFrame gameEndFrame = new gameEndFrame();
      gameEndFrame.setVisible(true);
    });
  }
}
