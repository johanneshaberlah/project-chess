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

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Color squareColor = (row + col) % 2 == 0 ? Color.WHITE : Color.GRAY;
                g.setColor(squareColor);
                g.fillRect(col * squareSize, row * squareSize, squareSize, squareSize);

                Square square = new Square(col + 1, 8 - row); // Adjust to 1-based indexing for file and rank
                Piece piece = board.pieceAt(square).orElse(null);

                if (piece != null) {
                    //image drawing logic
                    String imagePath = createImagePath(piece.name().toLowerCase(), piece.color().name().toLowerCase());
                    ImageIcon pieceImage = new ImageIcon(imagePath);
                    pieceImage.paintIcon(this, g, col * squareSize, row * squareSize);
                }
            }
        }
    }
    private static String createImagePath(String pieceName, String color) {
        return "src/main/resources/pieces/" + color + pieceName + ".png";
    }
}


