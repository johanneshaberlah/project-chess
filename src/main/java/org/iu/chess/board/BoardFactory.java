package org.iu.chess.board;

import org.iu.chess.Square;
import org.iu.chess.piece.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BoardFactory {

  public static Board startingPosition() {
    return create("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
  }

  public static Board create(String fen) {
    Map<Square, Optional<Piece>> squares = new HashMap<>();
    String[] fenParts = fen.split(" ");

    if (fenParts.length >= 1) {
      String piecePlacement = fenParts[0];
      int rank = 7, file = 0;

      for (char fenChar : piecePlacement.toCharArray()) {
        if (Character.isDigit(fenChar)) {
          int emptySquares = Character.getNumericValue(fenChar);
          for (int i = 0; i < emptySquares; i++) {
            Square square = new Square(file, rank);
            squares.put(square, Optional.empty());
            file++;
          }
        } else if (fenChar == '/') {
          rank--;
          file = 0;
        } else {
          Square square = new Square(file, rank);
          Optional<Piece> piece = createPieceFromFEN(fenChar);
          squares.put(square, piece);
          file++;
        }
      }

      // Fill remaining empty squares with Optional.empty()
      for (int remainingRank = rank; remainingRank >= 1; remainingRank--) {
        for (int remainingFile = file; remainingFile <= 8; remainingFile++) {
          Square square = new Square(remainingFile, remainingRank);
          squares.put(square, Optional.empty());
        }
      }
    }

    return Board.of(squares);
  }



  private static Optional<Piece> createPieceFromFEN(char fenChar) {
    PieceColor color = Character.isUpperCase(fenChar) ? PieceColor.WHITE : PieceColor.BLACK;

      return switch (Character.toUpperCase(fenChar)) {
          case 'P' -> Optional.of(Pawn.ofColor(color)) ;
          case 'N' -> Optional.of(Knight.ofColor(color)) ;
          case 'B' -> Optional.of(Bishop.ofColor(color)) ;
          case 'R' -> Optional.of(Rook.ofColor(color)) ;
          case 'Q' -> Optional.of(Queen.ofColor(color)) ;
          case 'K' -> Optional.of(King.ofColor(color)) ;
          default ->
              // Wenn der FEN-Charakter nicht erkannt wird, gib eine leere Figur zurück.
                  Optional.empty();
      };
  }

  public static String generateFEN(Board board) {
    StringBuilder fen = new StringBuilder();
    int emptyCount = 0;

    for (int rank = 8; rank >= 1; rank--) {
      for (int file = 1; file <= 8; file++) {
        Square square = new Square(file, rank);
        Optional<Piece> pieceOptional = board.pieceAt(square);

        if (pieceOptional.isPresent()) {
          if (emptyCount > 0) {
            fen.append(emptyCount);
            emptyCount = 0;
          }

          Piece currentPiece = pieceOptional.get();
          fen.append(getFENRepresentation(currentPiece));
        } else {
          emptyCount++;
        }
      }

      if (emptyCount > 0) {
        fen.append(emptyCount);
        emptyCount = 0;
      }

      if (rank > 1) {
        fen.append('/');
      }
    }

    return fen.toString();
  }


  private static char getFENRepresentation(Piece piece) {
    return piece.color() == PieceColor.WHITE ? piece.fenName() : String.valueOf(piece.fenName()).toLowerCase().charAt(0);
  }
}
