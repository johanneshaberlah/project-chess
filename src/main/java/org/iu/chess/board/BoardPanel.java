package org.iu.chess.board;

import org.iu.chess.Square;
import org.iu.chess.piece.Piece;

import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JPanel {
  private final Board board;

  public BoardPanel(Board board) {
    this.board = board;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    int squareSize = Math.min(getWidth() / 8, getHeight() / 8);
    board.squares().forEach(square -> {
      Color squareColor = (square.rank() + square.file()) % 2 == 0 ? Color.WHITE : Color.GRAY;
      g.setColor(squareColor);
      g.fillRect(square.file() * squareSize, square.rank() * squareSize, squareSize, squareSize);

      board.pieceAt(square).ifPresent(piece -> {
        // image drawing logic
        int centerX = square.file() * squareSize + squareSize / 2;
        int centerY = square.rank() * squareSize + squareSize / 2;

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


