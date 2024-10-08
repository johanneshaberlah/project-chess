package org.iu.chess.game.frame;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.iu.chess.board.Board;
import org.iu.chess.piece.Piece;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class GamePanel extends JPanel {
  // Public to share graphics between the GameFrame and the GamePanel
  public final Map<Piece, ImageIcon> imageCache = Maps.newHashMap();

  private final Board position;

  private GamePanel(Board position) {
    this.position = position;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    int squareSize = Math.min(getWidth() / 8, getHeight() / 8);

    position.squares().forEach(square -> {
      Color squareColor = (square.rank() + square.file()) % 2 == 0 ? Color.LIGHT_GRAY : Color.WHITE;
      g.setColor(squareColor);

      // Inverting the y-axis for the drawing
      int x = square.file() * squareSize;
      int y = (7 - square.rank()) * squareSize; // Flipping the y-axis

      g.fillRect(x, y, squareSize, squareSize);

      position.pieceAt(square).ifPresent(piece -> {
        if (imageCache.containsKey(piece)) {
          imageCache.get(piece).paintIcon(this, g, x, y);
          return;
        }
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
        imageCache.put(piece, pieceImage);
        pieceImage.paintIcon(this, g, imageX, imageY);
      });
    });

  }


  private String createImagePath(String pieceName, String color) {
    return "src/main/resources/pieces/" + color + pieceName + ".png";
  }

  public static GamePanel of(Board position) {
    Preconditions.checkNotNull(position);
    return new GamePanel(position);
  }
}


