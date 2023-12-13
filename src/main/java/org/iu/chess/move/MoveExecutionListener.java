package org.iu.chess.move;

import org.iu.chess.Square;
import org.iu.chess.game.ChessGame;
import org.iu.chess.game.InvalidGameActionException;
import org.iu.chess.game.player.PlayerMove;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MoveExecutionListener extends MouseAdapter {
  private final ChessGame game;
  private final JPanel parent;

  private SquareTimeTuple lastClick;

  private MoveExecutionListener(JPanel parent, ChessGame game) {
    this.parent = parent;
    this.game = game;
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
    game.getPosition().pieceAt(lastClick.square()).ifPresent(piece -> {
      try {
        game.performMove(
          new PlayerMove(game.playerWithColor(piece.color()), new Move(lastClick.square(), square))
        );
        parent.repaint();
      } catch (IllegalMoveException e) {
        JOptionPane.showMessageDialog(parent, "Dieser Move ist nicht erlaubt.");
      } catch (InvalidGameActionException e) {
        JOptionPane.showMessageDialog(parent, "Dieser Spieler ist nicht an der Reihe.");
      } finally {
        lastClick = null;
      }
    });
  }

  public static MoveExecutionListener of(JPanel parent, ChessGame game) {
    return new MoveExecutionListener(parent, game);
  }
}
