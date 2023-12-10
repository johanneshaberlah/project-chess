package org.iu.chess.ui;

import org.iu.chess.board.Board;
import org.iu.chess.Square;
import org.iu.chess.piece.Piece;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ChessBoardPanel extends JPanel {
  private final Board board;

  public ChessBoardPanel(Board board) {
    this.board = board;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    int squareSize = Math.min(getWidth() / 8, getHeight() / 8);

    // Draw the chessboard first
    for (int row = 0; row < 8; row++) {
      for (int col = 0; col < 8; col++) {
        Color squareColor = (row + col) % 2 == 0 ? Color.WHITE : Color.GRAY;
        g.setColor(squareColor);
        g.fillRect(col * squareSize, row * squareSize, squareSize, squareSize);
      }
    }

    // Draw the pieces on top of the chessboard
    for (int row = 0; row < 8; row++) {
      for (int col = 0; col < 8; col++) {
        Square square = new Square(col + 1, 8 - row); // Adjust to 1-based indexing for file and rank
        Piece piece = board.pieceAt(square).orElse(null);

        if (piece != null) {
          //image drawing logic
          int centerX = col * squareSize + squareSize / 2;
          int centerY = row * squareSize + squareSize / 2;

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
        }
      }
    }
  }


  private static String createImagePath(String pieceName, String color) {
    return "src/main/resources/pieces/" + color + pieceName + ".png";
  }
}


