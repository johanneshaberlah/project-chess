package org.iu.chess.move;

import org.iu.chess.Square;
import org.iu.chess.board.Board;

public record Move(Square from, Square to) {
  public int minimalRank() {
    return Math.min(from.rank(), to.rank());
  }

  public int minimalFile() {
    return Math.min(from.file(), to.file());
  }

  public int maximalRank() {
    return Math.max(from.rank(), to.rank());
  }

  public int maximalFile() {
    return Math.max(from.file(), to.file());
  }

  public int fileDistance() {
    return Math.abs(from.file() - to.file());
  }

  public int rankDistance() {
    return Math.abs(from.rank() - to.rank());
  }

  public String asFenNotation() {
    return from.algebraicNotation() + to.algebraicNotation();
  }

  public Square moveWithMinimalRank() {
    return from.rank() < to.rank() ? from : to;
  }

  public RelativeMove asRelativeMove() {
    return RelativeMove.of(to.file() - from.file(), to.rank() - from.rank());
  }
}
