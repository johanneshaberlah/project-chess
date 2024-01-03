package org.iu.chess.game.frame;

import com.google.common.base.Preconditions;
import org.iu.chess.game.player.PlayerTuple;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GamePausedFrame extends JFrame {

  private GamePausedFrame(Runnable onSave, Runnable onContinue, Runnable onNewGame) {
    StyledGameButton sb = new StyledGameButton();
    setTitle("Schach - Spiel pausiert");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(265, 330);
    setResizable(false);
    setLocationRelativeTo(null);
    // Ändern des Standard-Schließverhaltens
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


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

  public void saveToFile(String fen, PlayerTuple players) {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Wähle einen Ort um das Spiel zu speichern.");

    int userSelection = fileChooser.showSaveDialog(this);

    if (userSelection == JFileChooser.APPROVE_OPTION) {
      File fileToSave = fileChooser.getSelectedFile();
      if (!fileToSave.getAbsolutePath().endsWith(".txt")) {
        fileToSave = new File(fileToSave.getAbsolutePath() + ".txt");
      }
      try {
        FileWriter fw = new FileWriter(fileToSave);
        fw.write(fen);
        fw.write("\n");
        players.white().clock().ifPresent(clock -> {
          try {
            fw.write(String.valueOf(clock.timeRemaining()));
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        });
        players.black().clock().ifPresent(clock -> {
          try {
            fw.write(";" + clock.timeRemaining());
            fw.write(";" + clock.increment());
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        });
        fw.close();
        JOptionPane.showMessageDialog(this, "Das Spiel wurde gespeichert.");
      } catch (IOException ex) {
        JOptionPane.showMessageDialog(this, "Ein Fehler ist aufgetreten: " + ex.getMessage());
      }
    }
  }

  public static GamePausedFrame of(Runnable onSave, Runnable onContinue, Runnable onNewGame) {
    Preconditions.checkNotNull(onSave);
    Preconditions.checkNotNull(onContinue);
    Preconditions.checkNotNull(onNewGame);
    return new GamePausedFrame(onSave, onContinue, onNewGame);
  }
}
