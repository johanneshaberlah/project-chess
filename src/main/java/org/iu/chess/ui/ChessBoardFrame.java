package org.iu.chess.ui;

import org.iu.chess.board.Board;
import org.iu.chess.board.BoardPanel;

import javax.swing.*;
import java.awt.*;

public class ChessBoardFrame extends JFrame {
  private final Board board;

  public ChessBoardFrame(Board board) {
    this.board = board;

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setTitle("Schach");

    BoardPanel chessBoardPanel = new BoardPanel(board);
    add(chessBoardPanel);

    setMinimumSize(new Dimension(700, 700));
    setResizable(false);

    pack();
    setLocationRelativeTo(null); // Center the frame
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      Board board = Board.examplePosition();   // Board.startingPosition()
            ChessBoardFrame chessBoardFrame = new ChessBoardFrame(board);
            chessBoardFrame.setVisible(true);
        });
    }
}
