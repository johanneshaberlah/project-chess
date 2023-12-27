package org.iu.chess.game.frame;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.iu.chess.board.Board;
import org.iu.chess.game.Game;
import org.iu.chess.game.GameFactory;
import org.iu.chess.game.GameTimingStrategy;
import org.iu.chess.game.player.PlayerClock;
import org.iu.chess.move.LegalMovePreviewListener;
import org.iu.chess.move.MoveExecutionListener;
import org.iu.chess.piece.Piece;
import org.iu.chess.piece.PieceColor;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.Optional;

public class GameFrame extends JFrame {
  private Map<Piece, ImageIcon> imageCache = Maps.newHashMap();

  private GamePanel gamePanel;
  private Optional<GameTimingStrategy> timingStrategy;

  private final JLabel timerLabel;
  private final JLabel aboveBoardTimerLabel;
  private final JLabel playerNameLabel; // Hinzugefügtes JLabel für den Spielername
  private final JLabel secondPlayerNameLabel; // Neues JLabel für den zweiten Spieler

  private final JPanel iconsPanel;
  private final JPanel iconsPanel2;

  private GameFrame(
    Board position,
    Optional<GameTimingStrategy> timingStrategy
  ) {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setTitle("Schach");
    this.timingStrategy = timingStrategy;
    // Embed the BoardPanel in a JPanel with an EmptyBorder
    JPanel boardPanelContainer = new JPanel(new BorderLayout());
    boardPanelContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    // Initialize the timer label for Player 1 (bottom right)
    timerLabel = createStyledTimerLabel();
    timerLabel.setHorizontalAlignment(JLabel.CENTER);

    // Initialize the JLabel for the timer above the board for Player 2 (top right)
    aboveBoardTimerLabel = createStyledTimerLabel();
    aboveBoardTimerLabel.setHorizontalAlignment(JLabel.CENTER);

    iconsPanel = createIconsLabel();
    iconsPanel2 = createIconsLabel();

    // Initialize the JLabel for the player name
    playerNameLabel = new JLabel("Player 2");
    playerNameLabel.setFont(new Font("Arial", Font.PLAIN, 26)); // Schriftart und Größe anpassen
    playerNameLabel.setHorizontalAlignment(JLabel.CENTER);

    // Initialize the JLabel for the second player name
    secondPlayerNameLabel = new JLabel("Player 1");
    secondPlayerNameLabel.setFont(new Font("Arial", Font.PLAIN, 26)); // Schriftart und Größe anpassen
    secondPlayerNameLabel.setHorizontalAlignment(JLabel.CENTER);

    // Initialize the BoardPanel and add it to the container
    this.gamePanel = GamePanel.of(position);

    boardPanelContainer.add(this.gamePanel, BorderLayout.CENTER);
    add(boardPanelContainer, BorderLayout.CENTER);
    this.imageCache = this.gamePanel.imageCache;
    addTimers();
    setMinimumSize(new Dimension(1050, 840)); // Adjusted width to accommodate the timers
    setResizable(false);

    pack();
    setLocationRelativeTo(null); // Center the frame
  }

  public GamePanel panel() {
    return this.gamePanel;
  }

  private void addTimers() {
    timingStrategy.ifPresent(timing -> {
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
      add(timersPanel, BorderLayout.EAST);
    });
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

  private static JPanel createIconsLabel() {

    JPanel iconsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    iconsPanel.setPreferredSize(new Dimension(150, 100));
    return  iconsPanel;
  }

  public void showLostPiece(Piece piece) {
    JLabel label = new JLabel(new ImageIcon(imageCache.get(piece).getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH)));

    if (piece.color().equals(PieceColor.BLACK)) {
      iconsPanel2.add(label);
    } else {
      iconsPanel.add(label);
    }
  }

  public void updateTimersText(Optional<PlayerClock> whitePlayer, Optional<PlayerClock> blackPlayer) {
    whitePlayer.ifPresent(clock -> {
      long minutes = clock.currentTimeRemaining() / 60;
      long seconds = clock.currentTimeRemaining() % 60;
      String timeString = String.format("%02d:%02d", minutes, seconds);
      aboveBoardTimerLabel.setText(timeString);
    });
    blackPlayer.ifPresent(clock -> {
      long minutes = clock.currentTimeRemaining() / 60;
      long seconds = clock.currentTimeRemaining() % 60;
      String timeString = String.format("%02d:%02d", minutes, seconds);
      timerLabel.setText(timeString);
    });
  }

  public static GameFrame of(
    Board position,
    Optional<GameTimingStrategy> timingStrategy
  ) {
    Preconditions.checkNotNull(position);
    return new GameFrame(position, timingStrategy);
  }
}
