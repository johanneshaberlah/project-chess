package org.iu.chess.board;

import org.iu.chess.Square;
import org.iu.chess.piece.Pawn;
import org.iu.chess.piece.Piece;
import org.iu.chess.piece.PieceColor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BoardFactory {

  public static Board create(String fen) {
    Map<Square, Optional<Piece>> squares = new HashMap<>();
    String[] fenParts = fen.split(" ");

    if (fenParts.length >= 1) {
      String piecePlacement = fenParts[0];
      int rank = 8, file = 1;

      for (char fenChar : piecePlacement.toCharArray()) {
        if (Character.isDigit(fenChar)) {
          file += Character.getNumericValue(fenChar);
        } else if (fenChar == '/') {
          rank--;
          file = 1;
        } else {
          Square square = new Square(file, rank);
          Piece piece = createPieceFromFEN(fenChar);
          squares.put(square, Optional.of(piece));
          file++;
        }
      }
    }

    return new Board(squares);
  }

  private static Piece createPieceFromFEN(char fenChar) {
    // Implementiere die Logik, um ein Piece-Objekt basierend auf dem FEN-Charakter zu erstellen.
    // Beispiel:
    // 'P' oder 'p' für Pawn
    // 'R' oder 'r' für Rook
    // ...

    return new Pawn(PieceColor.WHITE); // Dummy-Rückgabewert, ersetze dies entsprechend
  }
}
