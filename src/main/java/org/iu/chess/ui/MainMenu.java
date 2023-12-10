package org.iu.chess.ui;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

enum GameMode {
  ONE_VS_ONE,
  COMPUTER
}

enum AiDifficulty {
  EASY,
  MEDIUM,
  HARD
}

enum Time {
  FIVE_MINUTES,
  TEN_MINUTES,
  FIFTEEN_MINUTES
}

public class MainMenu extends JFrame {

  private JButton play1v1Button;
  private JButton playAgainstComputerButton;
  private JButton time5MinutesButton;
  private JButton time10MinutesButton;
  private JButton time15MinutesButton;
  private JButton aiEasyButton;
  private JButton aiMediumButton;
  private JButton aiHardButton;
  private JButton startGameButton;

  private JButton loadGameButton;

  private String selectedMode = "";

  public MainMenu() {
    setTitle("Chess Game - Main Menu");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(530, 300);
    setLocationRelativeTo(null);

    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(1, 2)); // Two columns

    // Left column for play options
    JPanel playPanel = new JPanel();
    playPanel.setLayout(new GridLayout(6, 1));
    playPanel.setBackground(new Color(218, 218, 218));

    play1v1Button = createStyledButton("gegeneinander Spielen");

    Font customFont = new Font("Arial", Font.BOLD, 16);
    play1v1Button.setFont(customFont);

    time5MinutesButton = createStyledButton("5 minuten");
    time10MinutesButton = createStyledButton("10 minuten");
    time15MinutesButton = createStyledButton("15 minuten");

    loadGameButton = createStyledButton("Spiel laden");
    loadGameButton.setFont(customFont);

    playPanel.add(play1v1Button);
    playPanel.add(new JLabel(" Spielzeit wählen:"));
    playPanel.add(time5MinutesButton);
    playPanel.add(time10MinutesButton);
    playPanel.add(time15MinutesButton);
    playPanel.add(loadGameButton);

    panel.add(playPanel);

    // Right column for AI difficulty and start button
    JPanel aiPanel = new JPanel();
    aiPanel.setLayout(new GridLayout(6, 1));
    aiPanel.setBackground(new Color(218, 218, 218));

    playAgainstComputerButton = createStyledButton("gegen den Computer spielen");
    playAgainstComputerButton.setFont(customFont);

    aiEasyButton = createStyledButton("Anfänger");
    aiMediumButton = createStyledButton("Fortgeschritten");
    aiHardButton = createStyledButton("Experte");

    startGameButton = createStyledButton("Spiel starten");

    startGameButton.setFont(customFont);

    aiPanel.add(playAgainstComputerButton);
    aiPanel.add(new JLabel(" Computer-Schwierigkeitsstufe:"));
    aiPanel.add(aiEasyButton);
    aiPanel.add(aiMediumButton);
    aiPanel.add(aiHardButton);
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

      JOptionPane.showMessageDialog(MainMenu.this,
        "Zeit : " + selectedTime / 60 + " Minuten, Computer-Schwierigkeit: " + selectedDifficulty);
    });

    ActionListener aiButtonListener = e -> handleAIDifficultyButtonClick(e.getActionCommand(), "AI " + e.getActionCommand() + " gewählt", (JButton) e.getSource());
    aiEasyButton.addActionListener(aiButtonListener);
    aiMediumButton.addActionListener(aiButtonListener);
    aiHardButton.addActionListener(aiButtonListener);

    ActionListener timeButtonListener = e -> handleTimeOptionButtonClick(e.getActionCommand(), e.getActionCommand() + " gewählt", (JButton) e.getSource());
    time5MinutesButton.addActionListener(timeButtonListener);
    time10MinutesButton.addActionListener(timeButtonListener);
    time15MinutesButton.addActionListener(timeButtonListener);

    // Set default selection
    play1v1Button.setSelected(true);
    enableAiButtons(false);
    updateButtonBackground(play1v1Button);

    aiEasyButton.setSelected(true);
    updateButtonBackground(aiEasyButton);

    time5MinutesButton.setSelected(true);
    updateButtonBackground(time5MinutesButton);
  }

  private void handleGameModeButtonClick(GameMode gameMode, String message, JButton button) {
    selectedMode = gameMode.name();
    updateGamemodeButtonState();
    enableAiButtons(gameMode == GameMode.COMPUTER);
    updateButtonBackground(button);
    JOptionPane.showMessageDialog(MainMenu.this, message);
  }

  private void handleAIDifficultyButtonClick(String difficulty, String message, JButton button) {
    selectedMode = difficulty;
    updateAiButtonState();
    updateButtonBackground(button);
    JOptionPane.showMessageDialog(MainMenu.this, message);
  }

  private void handleTimeOptionButtonClick(String timeOption, String message, JButton button) {
    selectedMode = timeOption;
    updateTimeButtonState();
    updateButtonBackground(button);
    JOptionPane.showMessageDialog(MainMenu.this, message);
  }

  private JButton createStyledButton(String text) {
    JButton button = new JButton(text);
    button.setUI(new BasicButtonUI() {
      @Override
      protected void paintButtonPressed(Graphics g, AbstractButton b) {
        g.setColor(new Color(124, 124, 124)); // Darkened background color
        g.fillRect(0, 0, b.getWidth(), b.getHeight());
      }
    });
    button.setBorderPainted(false);
    button.setFocusPainted(false);
    button.setContentAreaFilled(false);

    int buttonWidth = 150;
    int buttonHeight = 40;
    button.setPreferredSize(new Dimension(buttonWidth, buttonHeight));

    return button;
  }

  private void updateGamemodeButtonState() {
    playAgainstComputerButton.setSelected(selectedMode.equals(GameMode.COMPUTER.name()));
    play1v1Button.setSelected(selectedMode.equals(GameMode.ONE_VS_ONE.name()));
  }

  private void updateAiButtonState() {
    aiEasyButton.setSelected("Easy".equals(selectedMode));
    aiMediumButton.setSelected("Medium".equals(selectedMode));
    aiHardButton.setSelected("Hard".equals(selectedMode));
  }

  private void updateTimeButtonState() {
    time5MinutesButton.setSelected("5 minutes".equals(selectedMode));
    time10MinutesButton.setSelected("10 minutes".equals(selectedMode));
    time15MinutesButton.setSelected("15 minutes".equals(selectedMode));
  }

  private void updateButtonBackground(JButton clickedButton) {
    List<JButton> allButtons = Arrays.asList(play1v1Button, playAgainstComputerButton, aiEasyButton, aiMediumButton, aiHardButton,
      time5MinutesButton, time10MinutesButton, time15MinutesButton);

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
    if (time5MinutesButton.isSelected()) return 300;
    else if (time10MinutesButton.isSelected()) return 600;
    else if (time15MinutesButton.isSelected()) return 900;
    else return -1;
  }

  private String getSelectedDifficulty() {
    if (aiEasyButton.isSelected()) return "Anfänger";
    else if (aiMediumButton.isSelected()) return "Fortgeschritten";
    else if (aiHardButton.isSelected()) return "Experte";
    else return "null";
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      MainMenu mainMenu = new MainMenu();
      mainMenu.setVisible(true);
    });
  }
}
