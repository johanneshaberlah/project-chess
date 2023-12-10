package org.iu.chess;

import org.iu.chess.move.IllegalMoveException;
import org.iu.chess.move.Move;
import org.iu.chess.piece.Piece;

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

  public void performMove(Move move) throws IllegalMoveException {
    var piece = squares.get(move.from()).orElseThrow(() -> IllegalMoveException.of(move));
    if (!squares.containsKey(move.to()) || !piece.isLegalMove(this, move)) {
      throw IllegalMoveException.of(move);
    }
    squares.put(move.from(), Optional.empty());
    squares.put(move.to(), Optional.of(piece));
  }

  public static Board startingPosition() {
    return new Board(Map.of());
  }
}
