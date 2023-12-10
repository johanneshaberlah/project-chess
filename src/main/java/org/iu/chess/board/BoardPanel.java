package org.iu.chess.board;

import org.iu.chess.Square;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BoardPanel extends JPanel {
  private final Board board;

  public BoardPanel(Board board) {
    this.board = board;
    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent event) {
        int squareSize = Math.min(getWidth() / 8, getHeight() / 8);
        int file = event.getX() / squareSize;
        int rank = 7 - (event.getY() / squareSize);
        Square square = new Square(file, rank);
        System.out.println("Clicked on square " + rank + ", " + file);
        board.pieceAt(square).ifPresent(piece -> {
          System.out.println("Clicked on " + piece.name() + " at square " + rank + ", " + file);
          piece.reachableMoves().forEach(move -> {
            // Inverting the y-axis for the drawing
            if (piece.isLegalMove(board, move.move().asMove(square))) {
              int x = (file + move.move().fileDifference()) * squareSize;
              int y = (7 - (rank + move.move().rankDifference())) * squareSize; // Flipping the y-axis
              event.getComponent().getGraphics().setColor(Color.RED);
              event.getComponent().getGraphics().fillRect(x, y, squareSize, squareSize);
              System.out.println("Reachable move: " + move.move());
            }
          });
          // Handle the click event for the piece
          // Add further action here
        });
      }
    });
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    int squareSize = Math.min(getWidth() / 8, getHeight() / 8);

    board.squares().forEach(square -> {
      Color squareColor = (square.rank() + square.file()) % 2 == 0 ? Color.WHITE : Color.GRAY;
      g.setColor(squareColor);

      // Inverting the y-axis for the drawing
      int x = square.file() * squareSize;
      int y = (7 - square.rank()) * squareSize; // Flipping the y-axis

      g.fillRect(x, y, squareSize, squareSize);

      board.pieceAt(square).ifPresent(piece -> {
        // image drawing logic, adjusted for the inverted y-axis
        int centerX = x + squareSize / 2;
        int centerY = y + squareSize / 2;

        String imagePath = createImagePath(piece.name().toLowerCase(), piece.color().name().toLowerCase());
        ImageIcon defaultSizeIcon = new ImageIcon(imagePath);

        double scale = Math.min((double) squareSize / defaultSizeIcon.getIconWidth(),
          (double) squareSize / defaultSizeIcon.getIconHeight());

        Image scaledImage = defaultSizeIcon.getImage().getScaledInstance(
          (int) (defaultSizeIcon.getIconWidth() * scale),
          (int) (defaultSizeIcon.getIconHeight() * scale), Image.SCALE_SMOOTH);
        ImageIcon pieceImage = new ImageIcon(scaledImage);
        int imageX = centerX - pieceImage.getIconWidth() / 2;
        int imageY = centerY - pieceImage.getIconHeight() / 2;

        System.out.println("Drawing " + piece.name() + " at " + imageX + ", " + imageY + " with size " +
          pieceImage.getIconWidth() + "x" + pieceImage.getIconHeight());

        pieceImage.paintIcon(this, g, imageX, imageY);
      });
    });
  }


  private static String createImagePath(String pieceName, String color) {
    return "src/main/resources/pieces/" + color + pieceName + ".png";
  }
}


