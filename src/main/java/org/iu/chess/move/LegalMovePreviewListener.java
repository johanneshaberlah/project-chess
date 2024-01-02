package org.iu.chess.move;

import org.iu.chess.Square;
import org.iu.chess.board.Board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Stack;

public class LegalMovePreviewListener extends MouseAdapter {
  private final Stack<Point> previewPoints = new Stack<>();

  private final JPanel parent;
  private final Board position;

  private LegalMovePreviewListener(JPanel parent, Board position) {
    this.parent = parent;
    this.position = position;
  }

  @Override
  public void mouseClicked(MouseEvent event) {
    int squareSize = Math.min(parent.getWidth() / 8, parent.getHeight() / 8);
    int file = event.getX() / squareSize;
    int rank = 7 - (event.getY() / squareSize);
    Square square = new Square(file, rank);
    var graphics = parent.getGraphics();
    graphics.setColor(new Color(107, 110, 70));
    SwingUtilities.invokeLater(() -> {
      position.pieceAt(square).ifPresent(piece -> {
        Stack<Point> previewClone = (Stack<Point>) previewPoints.clone();
        previewPoints.clear();
        piece.reachableMoves().forEach(move -> {
          // Inverting the y-axis for the drawing
          var baseMove = move.move().asLocalizedMove(square);
          var targetPiece = position.pieceAt(move.move().asLocalizedMove(square).to());
          if (piece.isLegalMove(position, baseMove) && !(targetPiece.isPresent() && targetPiece.get().color() == piece.color())) {
            int x = (file + move.move().fileDifference()) * squareSize;
            int y = (7 - (rank + move.move().rankDifference())) * squareSize; // Flipping the y-axis
            graphics.fillOval(x + squareSize / 2 - squareSize / 8, y + squareSize / 2 - squareSize / 8, squareSize / 4, squareSize / 4);
            previewPoints.push(new Point(x, y));
            previewClone.remove(new Point(x, y));
          }
        });
        while (!previewClone.empty()) {
          Point point = previewClone.pop();
          parent.repaint(new Rectangle(point.x, point.y, squareSize, squareSize));
        }
      });
    });

  }

  public static LegalMovePreviewListener of(JPanel parent, Board position) {
    return new LegalMovePreviewListener(parent, position);
  }
}
