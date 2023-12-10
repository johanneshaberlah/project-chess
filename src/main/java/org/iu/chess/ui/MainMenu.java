package org.iu.chess.ui;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

  private String selectedMode = "1v1"; // Default selected mode

  public MainMenu() {
    setTitle("Chess Game - Main Menu");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(450, 300);
    setLocationRelativeTo(null);

    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(1, 2)); // Two columns

    // Left column for play options
    JPanel playPanel = new JPanel();
    playPanel.setLayout(new GridLayout(6, 1));
    playPanel.setBackground(new Color(218, 218, 218));

    play1v1Button = createStyledButton("Play 1v1");

    Font customFont = new Font("Arial", Font.BOLD, 16);
    play1v1Button.setFont(customFont);

    time5MinutesButton = createStyledButton("5 minutes");
    time10MinutesButton = createStyledButton("10 minutes");
    time15MinutesButton = createStyledButton("15 minutes");

    loadGameButton = createStyledButton("Load Game");
    loadGameButton.setFont(customFont);


    playPanel.add(play1v1Button);
    playPanel.add(new JLabel(" Select Time:"));
    playPanel.add(time5MinutesButton);
    playPanel.add(time10MinutesButton);
    playPanel.add(time15MinutesButton);
    playPanel.add(loadGameButton);

    panel.add(playPanel);

    // Right column for AI difficulty and start button
    JPanel aiPanel = new JPanel();
    aiPanel.setLayout(new GridLayout(6, 1));
    aiPanel.setBackground(new Color(218, 218, 218));

    playAgainstComputerButton = createStyledButton("Play Against Computer");
    playAgainstComputerButton.setFont(customFont);


    aiEasyButton = createStyledButton("Easy");
    aiMediumButton = createStyledButton("Medium");
    aiHardButton = createStyledButton("Hard");

    startGameButton = createStyledButton("Start Game");

    startGameButton.setFont(customFont);

    aiPanel.add(playAgainstComputerButton);
    aiPanel.add(new JLabel(" Select AI Difficulty:"));
    aiPanel.add(aiEasyButton);
    aiPanel.add(aiMediumButton);
    aiPanel.add(aiHardButton);
    aiPanel.add(startGameButton);

    panel.add(aiPanel);

    add(panel);

    play1v1Button.addActionListener(e -> {
      selectedMode = "Computer";
      updateGamemodeButtonState();
      enableAiButtons(true);
      updateGamemodeButtonBackground();
      JOptionPane.showMessageDialog(MainMenu.this, "1v1 game selected");
    });

    playAgainstComputerButton.addActionListener(e -> {
      selectedMode = "1v1";
      updateGamemodeButtonState();
      enableAiButtons(false);
      updateGamemodeButtonBackground();
      JOptionPane.showMessageDialog(MainMenu.this, "Play against computer selected");
    });

    startGameButton.addActionListener(e -> {
      int selectedTime = getSelectedTime();
      String selectedDifficulty = getSelectedDifficulty();

      JOptionPane.showMessageDialog(MainMenu.this,
        "Selected Time: " + selectedTime/60 + " Minutes, AI Difficulty: " + selectedDifficulty);
    });

    aiEasyButton.addActionListener(e -> {
      selectedMode = "Easy";
      updateAiButtonState();
      updateAiButtonBackground();
      JOptionPane.showMessageDialog(MainMenu.this, "Easy AI selected");
    });

    aiMediumButton.addActionListener(e -> {
      selectedMode = "Medium";
      updateAiButtonState();
      updateAiButtonBackground();
      JOptionPane.showMessageDialog(MainMenu.this, "Medium AI selected");
    });

    aiHardButton.addActionListener(e -> {
      selectedMode = "Hard";
      updateAiButtonState();
      updateAiButtonBackground();
      JOptionPane.showMessageDialog(MainMenu.this, "Hard AI selected");
    });

    time5MinutesButton.addActionListener(e -> {
      selectedMode = "5 minutes";
      updateTimeButtonState();
      updateTimeButtonBackground();
      JOptionPane.showMessageDialog(MainMenu.this, "5 minutes selected");
    });

    time10MinutesButton.addActionListener(e -> {
      selectedMode = "10 minutes";
      updateTimeButtonState();
      updateTimeButtonBackground();
      JOptionPane.showMessageDialog(MainMenu.this, "10 minutes selected");
    });

    time15MinutesButton.addActionListener(e -> {
      selectedMode = "15 minutes";
      updateTimeButtonState();
      updateTimeButtonBackground();
      JOptionPane.showMessageDialog(MainMenu.this, "15 minutes selected");
    });

    // Set default selection
    play1v1Button.setSelected(true);
    updateGamemodeButtonBackground();

    aiEasyButton.setSelected(true);
    updateAiButtonBackground();

    time5MinutesButton.setSelected(true);
    updateTimeButtonBackground();
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
    play1v1Button.setSelected("1v1".equals(selectedMode));
    playAgainstComputerButton.setSelected("Computer".equals(selectedMode));
  }

  private void updateGamemodeButtonBackground() {
    play1v1Button.setContentAreaFilled(play1v1Button.isSelected());
    playAgainstComputerButton.setContentAreaFilled(playAgainstComputerButton.isSelected());
  }

  private void updateAiButtonState() {
    aiEasyButton.setSelected("Easy".equals(selectedMode));
    aiMediumButton.setSelected("Medium".equals(selectedMode));
    aiHardButton.setSelected("Hard".equals(selectedMode));
  }

  private void updateAiButtonBackground() {
    aiEasyButton.setContentAreaFilled(aiEasyButton.isSelected());
    aiMediumButton.setContentAreaFilled(aiMediumButton.isSelected());
    aiHardButton.setContentAreaFilled(aiHardButton.isSelected());
  }

  private void updateTimeButtonState() {
    time5MinutesButton.setSelected("5 minutes".equals(selectedMode));
    time10MinutesButton.setSelected("10 minutes".equals(selectedMode));
    time15MinutesButton.setSelected("15 minutes".equals(selectedMode));
  }

  private void updateTimeButtonBackground() {
    time5MinutesButton.setContentAreaFilled(time5MinutesButton.isSelected());
    time10MinutesButton.setContentAreaFilled(time10MinutesButton.isSelected());
    time15MinutesButton.setContentAreaFilled(time15MinutesButton.isSelected());
  }


  private void enableAiButtons(boolean enable) {
    aiEasyButton.setEnabled(enable);
    aiMediumButton.setEnabled(enable);
    aiHardButton.setEnabled(enable);
  }

  private int getSelectedTime() {
    if (time5MinutesButton.isSelected()) {
      return 300;
    } else if (time10MinutesButton.isSelected()) {
      return 600;
    } else if (time15MinutesButton.isSelected()) {
      return 900;
    } else {
      return -1;
    }
  }

  private String getSelectedDifficulty() {
    if (aiEasyButton.isSelected()) {
      return "Easy";
    } else if (aiMediumButton.isSelected()) {
      return "Medium";
    } else if (aiHardButton.isSelected()) {
      return "Hard";
    } else {
      return "null";
    }
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      MainMenu mainMenu = new MainMenu();
      mainMenu.setVisible(true);
    });
  }
}
