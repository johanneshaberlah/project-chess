package org.iu.chess.ui;

import com.google.common.base.Preconditions;
import org.iu.chess.board.Board;
import org.iu.chess.board.BoardFactory;
import org.iu.chess.game.ChessGame;
import org.iu.chess.game.GamePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ChessBoardFrame extends JFrame {

  private final Board board;
  private final JLabel timerLabel;
  private final JLabel aboveBoardTimerLabel;
  private final JLabel playerNameLabel; // Hinzugefügtes JLabel für den Spielername
  private final JLabel secondPlayerNameLabel; // Neues JLabel für den zweiten Spieler

  private final JPanel iconsPanel;
  private final JPanel iconsPanel2;
  private int remainingTimeInSeconds = 5 * 60;
  private int aboveBoardRemainingTimeInSeconds = 5 * 60;

  private ArrayList<ImageIcon> beatenPiecesWhite = new ArrayList<ImageIcon>();
  private ArrayList<ImageIcon> beatenPiecesBlack = new ArrayList<ImageIcon>();

  private ChessBoardFrame(Board board) {
    this.board = board;

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setTitle("Schach");

    // Embed the BoardPanel in a JPanel with an EmptyBorder
    JPanel boardPanelContainer = new JPanel(new BorderLayout());
    boardPanelContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    // Initialize the timer label for Player 1 (bottom right)
    timerLabel = createStyledTimerLabel();
    timerLabel.setHorizontalAlignment(JLabel.CENTER);

    // Initialize the JLabel for the timer above the board for Player 2 (top right)
    aboveBoardTimerLabel = createStyledTimerLabel();
    aboveBoardTimerLabel.setHorizontalAlignment(JLabel.CENTER);

    //Test
    for (var i = 0; i < 16; i++ ){

      ImageIcon icon = new ImageIcon("src/main/resources/pieces/blackpawn.png");
      addBeatenPiecesWhite(new ImageIcon(icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
    }

    iconsPanel = createIconsLabel(beatenPiecesWhite);

    for (var i = 0; i < 16; i++ ){
      ImageIcon icon = new ImageIcon("src/main/resources/pieces/whitepawn.png");
      addBeatenPieceBlack(new ImageIcon(icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
    }
    iconsPanel2 = createIconsLabel(beatenPiecesBlack);

    // Initialize the JLabel for the player name
    playerNameLabel = new JLabel("Player 2");
    playerNameLabel.setFont(new Font("Arial", Font.PLAIN, 26)); // Schriftart und Größe anpassen
    playerNameLabel.setHorizontalAlignment(JLabel.CENTER);

    // Initialize the JLabel for the second player name
    secondPlayerNameLabel = new JLabel("Player 1");
    secondPlayerNameLabel.setFont(new Font("Arial", Font.PLAIN, 26)); // Schriftart und Größe anpassen
    secondPlayerNameLabel.setHorizontalAlignment(JLabel.CENTER);

    // Initialize the BoardPanel and add it to the container
    GamePanel chessBoardPanel = new GamePanel(ChessGame.startingPosition());
    boardPanelContainer.add(chessBoardPanel, BorderLayout.CENTER);

    // Create a panel for the timers on the right
    JPanel timersPanel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.PAGE_START;
    gbc.insets = new Insets(0, 0, 50, 20);
    timersPanel.add(iconsPanel, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.insets = new Insets(0, 0, 10, 20); // Fügt zusätzlichen vertikalen Abstand unterhalb des Player 1 Timers hinzu
    timersPanel.add(timerLabel, gbc);

    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.insets = new Insets(0, 0, 60, 20);
    timersPanel.add(playerNameLabel, gbc);

    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.insets = new Insets(60, 0, 0, 20);
    timersPanel.add(secondPlayerNameLabel, gbc);

    gbc.gridx = 0;
    gbc.gridy = 4; // Sie können den GridY-Wert entsprechend anpassen
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.insets = new Insets(10, 0, 0, 20);
    timersPanel.add(aboveBoardTimerLabel, gbc);

    gbc.gridx = 0;
    gbc.gridy = 5; // Sie können den GridY-Wert entsprechend anpassen
    gbc.anchor = GridBagConstraints.PAGE_END;
    gbc.insets = new Insets(50, 0, 0, 20);
    timersPanel.add(iconsPanel2, gbc);



    // Add the container to the frame
    add(boardPanelContainer, BorderLayout.CENTER);
    add(timersPanel, BorderLayout.EAST);

    // Use javax.swing.Timer to update the timer label for Player 1 every second
    Timer timer = new Timer(1000, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        updateTimerLabel();
      }
    });
    timer.start();

    // Use javax.swing.Timer to update the timer above the board for Player 2 every second
    Timer aboveBoardTimer = new Timer(1000, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        updateAboveBoardTimerLabel();
      }
    });
    aboveBoardTimer.start();

    setMinimumSize(new Dimension( 1050, 840)); // Adjusted width to accommodate the timers
    setResizable(false);

    pack();
    setLocationRelativeTo(null); // Center the frame
  }

  private JLabel createStyledTimerLabel() {
    JLabel label = new JLabel();
    label.setFont(new Font("Arial", Font.BOLD, 24)); // Increased font size
    label.setForeground(Color.WHITE);
    label.setBackground(Color.BLACK);
    label.setOpaque(true);

    // Add padding to the label
    int padding = 10;
    label.setBorder(BorderFactory.createCompoundBorder(
      BorderFactory.createLineBorder(Color.GRAY, 2),
      BorderFactory.createEmptyBorder(padding, padding, padding, padding)
    ));

    // Load and resize the clock icon
    ImageIcon clockIcon = new ImageIcon("src/main/resources/icons/clock.png"); // Replace with the correct path
    label.setIcon(clockIcon);

    return label;
  }

  private static JPanel createIconsLabel(List<ImageIcon> imageIcons) {

    JPanel panel = new JPanel(new GridLayout(2, 8, 2, 2));

    // Add the scaled icon to all labels in the panel
    imageIcons.forEach(i -> {
      JLabel label = new JLabel(i);
      panel.add(label);
    });

    return panel;
  }

  public  void addBeatenPiecesWhite(ImageIcon icon)
  {
    if(beatenPiecesWhite.size() < 16)
      beatenPiecesWhite.add(icon);
  }


  public  void addBeatenPieceBlack(ImageIcon icon)
  {
    if(beatenPiecesBlack.size() < 16)
      beatenPiecesBlack.add(icon);
  }




  private void updateTimerLabel() {
    int minutes = remainingTimeInSeconds / 60;
    int seconds = remainingTimeInSeconds % 60;

    String timeString = String.format("%02d:%02d", minutes, seconds);
    timerLabel.setText(timeString);

    if (remainingTimeInSeconds > 0) {
      remainingTimeInSeconds--;
    } else {
      // Timer abgelaufen
      timerLabel.setText("Player 1 Time's up!");
    }
  }

  private void updateAboveBoardTimerLabel() {
    int minutes = aboveBoardRemainingTimeInSeconds / 60;
    int seconds = aboveBoardRemainingTimeInSeconds % 60;

    String timeString = String.format("%02d:%02d", minutes, seconds);
    aboveBoardTimerLabel.setText(timeString);

    if (aboveBoardRemainingTimeInSeconds > 0) {
      aboveBoardRemainingTimeInSeconds--;
    } else {
      // Timer above board abgelaufen
      aboveBoardTimerLabel.setText("Player 2 Time's up!");
    }
  }

  public static ChessBoardFrame of(Board board) {
    Preconditions.checkNotNull(board);
    return new ChessBoardFrame(board);
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      Board board = BoardFactory.create("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
      ChessBoardFrame chessBoardFrame = ChessBoardFrame.of(board);
      chessBoardFrame.setVisible(true);
    });
  }
}
