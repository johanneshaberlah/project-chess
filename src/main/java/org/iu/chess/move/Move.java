package org.iu.chess;

public record Move(Square from, Square to) {

  public int fileDistance() {
    return Math.abs(from.file() - to.file());
  }

  public int rankDistance() {
    return Math.abs(from.rank() - to.rank());
  }

  public String asFenNotation() {
    return from.algebraicNotation() + to.algebraicNotation();
  }
}
