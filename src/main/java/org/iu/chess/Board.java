package org.iu.chess;

import com.google.common.collect.Maps;
import org.iu.chess.move.IllegalMoveException;
import org.iu.chess.move.Move;
import org.iu.chess.piece.Pawn;
import org.iu.chess.piece.Piece;
import org.iu.chess.piece.PieceColor;

import java.util.Map;
import java.util.Optional;

public class Board {
  private final Map<Square, Optional<Piece>> squares;

  private Board(Map<Square, Optional<Piece>> squares) {
    this.squares = squares;
  }

  public Optional<Piece> pieceAt(Square square) {
    return squares.get(square);
  }

  public Iterable<Square> squares() {
    return squares.keySet();
  }

  public void performMove(Move move) throws IllegalMoveException {
    var piece = squares.get(move.from()).orElseThrow(() -> IllegalMoveException.of(move));
    if (!squares.containsKey(move.to()) || !piece.isLegalMove(this, move)) {
      throw IllegalMoveException.of(move);
    }
    squares.put(move.from(), Optional.empty());
    squares.put(move.to(), Optional.of(piece));
  }

  public static Board examplePosition() {
    Map<Square, Optional<Piece>> squares = Maps.newHashMap();
    for (int rank = 0; rank <= 8; rank++) {
      for (int file = 0; file <= 8; file++) {
        if (rank == 0 || rank == 8) {
          squares.put(new Square(file, rank), Optional.of(Pawn.ofColor(rank == 0 ? PieceColor.WHITE : PieceColor.BLACK)));
        } else {
          squares.put(new Square(file, rank), Optional.empty());
        }
      }
    }
    return new Board(squares);
  }

  public static Board startingPosition() {
    return new Board(Map.of());
  }
}
