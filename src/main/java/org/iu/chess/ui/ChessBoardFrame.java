package org.iu.chess.ui;

import com.google.common.base.Preconditions;
import org.iu.chess.board.Board;
import org.iu.chess.board.BoardFactory;
import org.iu.chess.board.BoardPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChessBoardFrame extends JFrame {

  private final Board board;
  private final JLabel timerLabel;
  private final JLabel aboveBoardTimerLabel;
  private final JLabel playerNameLabel; // Hinzugefügtes JLabel für den Spielername
  private int remainingTimeInSeconds = 5 * 60;
  private int aboveBoardRemainingTimeInSeconds = 5 * 60;

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

    // Initialize the JLabel for the player name
    playerNameLabel = new JLabel("Spielername");
    playerNameLabel.setFont(new Font("Arial", Font.PLAIN, 16)); // Schriftart und Größe anpassen
    playerNameLabel.setHorizontalAlignment(JLabel.CENTER);

    // Initialize the BoardPanel and add it to the container
    BoardPanel chessBoardPanel = new BoardPanel(board);
    boardPanelContainer.add(chessBoardPanel, BorderLayout.CENTER);

    // Create a panel for the timers on the right
    JPanel timersPanel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.PAGE_END;
    gbc.insets = new Insets(0, 0, 120, 20); // Fügt zusätzlichen vertikalen Abstand unterhalb des Player 1 Timers hinzu
    timersPanel.add(timerLabel, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.insets = new Insets(20, 0, 20, 20);
    timersPanel.add(playerNameLabel, gbc);

    gbc.gridx = 0;
    gbc.gridy = 2; // Sie können den GridY-Wert entsprechend anpassen
    gbc.anchor = GridBagConstraints.PAGE_START;
    gbc.insets = new Insets(120, 0, 0, 20);
    timersPanel.add(aboveBoardTimerLabel, gbc);

    // Add some empty space on the right to move the timers apart
    JPanel emptyPanel = new JPanel();
    emptyPanel.setPreferredSize(new Dimension(20, 20));
    timersPanel.add(emptyPanel, gbc);

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

    setMinimumSize(new Dimension(900, 750)); // Adjusted width to accommodate the timers
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
