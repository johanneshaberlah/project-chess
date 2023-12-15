package org.iu.chess.ui;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;

public class styledButton {
  public JButton createStyledButton(String textContent) {
    JButton button = new JButton(textContent);
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
}
