package org.iu.chess.move;

import org.iu.chess.Square;
import org.iu.chess.board.Board;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MoveExecutionListener extends MouseAdapter {
  private final Board board;
  private final JPanel parent;

  private SquareTimeTuple lastClick;

  private MoveExecutionListener(Board board, JPanel parent) {
    this.board = board;
    this.parent = parent;
  }

  @Override
  public void mousePressed(MouseEvent event) {
    int squareSize = Math.min(event.getComponent().getWidth() / 8, parent.getHeight() / 8);
    int file = event.getX() / squareSize;
    int rank = 7 - (event.getY() / squareSize);
    Square square = new Square(file, rank);
    lastClick = new SquareTimeTuple(square, System.currentTimeMillis());
  }

  @Override
  public void mouseReleased(MouseEvent event) {
    if (lastClick == null || lastClick.isTimedOut()) {
      if (lastClick != null) {
        lastClick = null;
      }
      super.mouseReleased(event);
      return;
    }
    int squareSize = Math.min(event.getComponent().getWidth() / 8, parent.getHeight() / 8);
    int file = event.getX() / squareSize;
    int rank = 7 - (event.getY() / squareSize);
    Square square = new Square(file, rank);
    if (square.equals(lastClick.square())) {
      super.mouseReleased(event);
      lastClick = null;
      return;
    }
    try {
      board.performMove(new Move(lastClick.square(), square));
      parent.repaint();
    } catch (IllegalMoveException e) {
      JOptionPane.showMessageDialog(parent, "Dieser Move ist nicht erlaubt.");
    }
  }

  public static MoveExecutionListener of(Board board, JPanel parent) {
    return new MoveExecutionListener(board, parent);
  }
}
