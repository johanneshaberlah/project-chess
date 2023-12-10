package org.iu.chess.move;

import org.iu.chess.Square;
import org.iu.chess.board.Board;
import org.iu.chess.game.ChessGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class LegalMovePreviewListener extends MouseAdapter {
  private final Stack<Point> previewPoints = new Stack<>();

  private final JPanel parent;
  private final ChessGame game;

  private LegalMovePreviewListener(JPanel parent, ChessGame game) {
    this.parent = parent;
    this.game = game;
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
      game.getPosition().pieceAt(square).ifPresent(piece -> {
        while (!previewPoints.empty()) {
          Point point = previewPoints.pop();
          parent.repaint(new Rectangle(point.x, point.y, squareSize, squareSize));
        }
        previewPoints.clear();
        piece.reachableMoves().forEach(move -> {
          // Inverting the y-axis for the drawing
          var baseMove = move.move().asMove(square);
          var targetPiece = game.getPosition().pieceAt(move.move().asMove(square).to());
          if (piece.isLegalMove(game.getPosition(), baseMove) && !(targetPiece.isPresent() && targetPiece.get().color() == piece.color())) {
            int x = (file + move.move().fileDifference()) * squareSize;
            int y = (7 - (rank + move.move().rankDifference())) * squareSize; // Flipping the y-axis
            graphics.fillOval(x + squareSize / 2 - squareSize / 8, y + squareSize / 2 - squareSize / 8, squareSize / 4, squareSize / 4);
            previewPoints.push(new Point(x, y));
          }
        });
      });
    });

  }

  public static LegalMovePreviewListener of(JPanel parent, ChessGame game) {
    return new LegalMovePreviewListener(parent, game);
  }
}
