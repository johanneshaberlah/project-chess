package org.iu.chess.ui;

import com.google.common.base.Preconditions;
import org.iu.chess.board.Board;
import org.iu.chess.board.BoardFactory;
import org.iu.chess.board.BoardPanel;

import javax.swing.*;
import java.awt.*;

public class ChessBoardFrame extends JFrame {
  private final Board board;

  private ChessBoardFrame(Board board) {
    this.board = board;

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setTitle("Schach");

    BoardPanel chessBoardPanel = new BoardPanel(board);
    add(chessBoardPanel);

    setMinimumSize(new Dimension(750, 750));
    setResizable(false);

    pack();
    setLocationRelativeTo(null); // Center the frame
  }

  public static ChessBoardFrame of(Board board) {
    Preconditions.checkNotNull(board);
    return new ChessBoardFrame(board);
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      Board board = BoardFactory.create("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
      ChessBoardFrame chessBoardFrame = new ChessBoardFrame(board);
      chessBoardFrame.setVisible(true);
    });
  }
}
