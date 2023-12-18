package org.iu.chess;

import org.iu.chess.game.frame.GameStartFrame;

import javax.swing.*;

public class ProjectChessApplication {

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      GameStartFrame startFrame = new GameStartFrame();
      startFrame.setVisible(true);
    });
  }
}
