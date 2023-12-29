package org.iu.chess.game.frame;

import com.google.common.base.Preconditions;
import org.iu.chess.game.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

public class GameStartFrame extends JFrame {
  private final JButton play1v1Button;
  private final JButton playAgainstComputerButton;
  private final JButton time5MinutesButton;
  private final JButton time10MinutesButton;
  private final JButton time15MinutesButton;
  private final JButton aiEasyButton;
  private final JButton aiMediumButton;
  private final JButton aiHardButton;
  private final JButton startGameButton;
  private final JButton timeInfinityButton;
  private final JButton loadGameButton;

  private String selectedMode = "";

  private GameStartFrame(GameStartListener gameStartListener) {
    StyledGameButton sb = new StyledGameButton();
    setTitle("Chess Game - Main Menu");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(530, 330);
    setResizable(false);
    setLocationRelativeTo(null);

    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(1, 2)); // Two columns

    // Left column for play options
    JPanel playPanel = new JPanel();
    playPanel.setLayout(new GridLayout(7, 1));
    playPanel.setBackground(new Color(218, 218, 218));

    play1v1Button = sb.createStyledButton("gegeneinander Spielen");

    Font customFont = new Font("Arial", Font.BOLD, 16);
    play1v1Button.setFont(customFont);

    time5MinutesButton = sb.createStyledButton("5 Minuten (+ 0)");
    time10MinutesButton = sb.createStyledButton("10 Minuten (+ 0)");
    time15MinutesButton = sb.createStyledButton("15 Minuten (+ 0)");
    timeInfinityButton = sb.createStyledButton("Correspondence / Unbegrenzt");

    loadGameButton = sb.createStyledButton("Spiel laden");
    loadGameButton.setFont(customFont);

    playPanel.add(play1v1Button);
    playPanel.add(new JLabel(" Spielzeit wählen:"));
    playPanel.add(timeInfinityButton);
    playPanel.add(time5MinutesButton);
    playPanel.add(time10MinutesButton);
    playPanel.add(time15MinutesButton);
    playPanel.add(loadGameButton);

    panel.add(playPanel);

    // Right column for AI difficulty and start button
    JPanel aiPanel = new JPanel();
    aiPanel.setLayout(new GridLayout(7, 1));
    aiPanel.setBackground(new Color(218, 218, 218));

    playAgainstComputerButton = sb.createStyledButton("gegen den Computer spielen");
    playAgainstComputerButton.setFont(customFont);

    aiEasyButton = sb.createStyledButton("Anfänger");
    aiMediumButton = sb.createStyledButton("Fortgeschritten");
    aiHardButton = sb.createStyledButton("Experte");

    startGameButton = sb.createStyledButton("Spiel starten");

    startGameButton.setFont(customFont);

    aiPanel.add(playAgainstComputerButton);
    aiPanel.add(new JLabel(" Computer-Schwierigkeitsstufe:"));
    aiPanel.add(aiEasyButton);
    aiPanel.add(aiMediumButton);
    aiPanel.add(aiHardButton);
    aiPanel.add(new JLabel(" "));
    aiPanel.add(startGameButton);

    panel.add(aiPanel);

    add(panel);

    play1v1Button.addActionListener(e ->
      handleGameModeButtonClick(GameMode.ONE_VS_ONE, "1v1 Spiel gewählt.", play1v1Button));
    playAgainstComputerButton.addActionListener(e ->
      handleGameModeButtonClick(GameMode.COMPUTER, "Spiel gegen Computer gewählt.", playAgainstComputerButton));

    startGameButton.addActionListener(e -> {
      int selectedTime = getSelectedTime();
      String selectedDifficulty = getSelectedDifficulty();
      System.out.println(selectedTime);
      gameStartListener.onGameStart(new GameStartContext(selectedTime, selectedDifficulty, getSelectedMode()));
    });

    ActionListener aiButtonListener = e -> handleAIDifficultyButtonClick(e.getActionCommand(), "AI " + e.getActionCommand() + " gewählt", (JButton) e.getSource());
    aiEasyButton.addActionListener(aiButtonListener);
    aiMediumButton.addActionListener(aiButtonListener);
    aiHardButton.addActionListener(aiButtonListener);

    ActionListener timeButtonListener = e -> handleTimeOptionButtonClick(e.getActionCommand(), e.getActionCommand() + " gewählt", (JButton) e.getSource());
    time5MinutesButton.addActionListener(timeButtonListener);
    time10MinutesButton.addActionListener(timeButtonListener);
    time15MinutesButton.addActionListener(timeButtonListener);
    timeInfinityButton.addActionListener(timeButtonListener);

    // Set default selection
    play1v1Button.setSelected(true);
    enableAiButtons(false);
    updateButtonBackground(play1v1Button);

    aiEasyButton.setSelected(true);
    updateButtonBackground(aiEasyButton);

    timeInfinityButton.setSelected(true);
    updateButtonBackground(time5MinutesButton);
  }

  private void handleGameModeButtonClick(GameMode gameMode, String message, JButton button) {
    selectedMode = gameMode.name();
    updateGamemodeButtonState();
    enableAiButtons(gameMode == GameMode.COMPUTER);
    updateButtonBackground(button);
    // JOptionPane.showMessageDialog(GameStartFrame.this, message);
  }

  private void handleAIDifficultyButtonClick(String difficulty, String message, JButton button) {
    selectedMode = difficulty;
    updateAiButtonState();
    updateButtonBackground(button);
  }

  private void handleTimeOptionButtonClick(String timeOption, String message, JButton button) {
    selectedMode = timeOption;
    updateTimeButtonState();
    updateButtonBackground(button);
  }

  private void updateGamemodeButtonState() {
    playAgainstComputerButton.setSelected(selectedMode.equals(GameMode.COMPUTER.name()));
    play1v1Button.setSelected(selectedMode.equals(GameMode.ONE_VS_ONE.name()));
  }

  private void updateAiButtonState() {
    aiEasyButton.setSelected("Anfänger".equals(selectedMode));
    aiMediumButton.setSelected("Fortgeschritten".equals(selectedMode));
    aiHardButton.setSelected("Experte".equals(selectedMode));
  }

  private void updateTimeButtonState() {
    System.out.println(selectedMode);
    time5MinutesButton.setSelected("5 Minuten (+ 0)".equals(selectedMode));
    time10MinutesButton.setSelected("10 Minuten (+ 0)".equals(selectedMode));
    time15MinutesButton.setSelected("15 Minuten (+ 0)".equals(selectedMode));
    timeInfinityButton.setSelected("Correspondence / Unbegrenzt".equals(selectedMode));
  }

  private void updateButtonBackground(JButton clickedButton) {
    List<JButton> allButtons = Arrays.asList(play1v1Button, playAgainstComputerButton, aiEasyButton, aiMediumButton, aiHardButton,
      time5MinutesButton, time10MinutesButton, time15MinutesButton, timeInfinityButton);

    for (JButton button : allButtons) {
      button.setContentAreaFilled(button.isSelected());
    }
  }

  private void enableAiButtons(boolean enable) {
    aiEasyButton.setEnabled(enable);
    aiMediumButton.setEnabled(enable);
    aiHardButton.setEnabled(enable);
  }

  private int getSelectedTime() {
    if (time5MinutesButton.isSelected()) return 5;
    else if (time10MinutesButton.isSelected()) return 10;
    else if (time15MinutesButton.isSelected()) return 15;
    else return -1;
  }

  private String getSelectedDifficulty() {
    if (aiEasyButton.isSelected()) return "Anfänger";
    else if (aiMediumButton.isSelected()) return "Fortgeschritten";
    else if (aiHardButton.isSelected()) return "Experte";
    else return "null";
  }

  private String getSelectedMode() {
    if (play1v1Button.isSelected()) return "1v1";
    else if (playAgainstComputerButton.isSelected()) return "AI";
    else return "null";
  }

  public static GameStartFrame of(GameStartListener gameStartListener) {
    Preconditions.checkNotNull(gameStartListener);
    return new GameStartFrame(gameStartListener);
  }
}
