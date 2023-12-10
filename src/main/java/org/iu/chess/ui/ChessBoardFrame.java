package org.iu.chess.ui;

import org.iu.chess.Board;
import org.iu.chess.piece.Pawn;
import org.iu.chess.piece.Piece;
import org.iu.chess.piece.PieceColor;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ChessBoardFrame extends JFrame {
  private final Board board;

  public ChessBoardFrame(Board board) {
    this.board = board;

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setTitle("Schach");

    ChessBoardPanel chessBoardPanel = new ChessBoardPanel(board);
    add(chessBoardPanel);

    setMinimumSize(new Dimension(750, 750));
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
