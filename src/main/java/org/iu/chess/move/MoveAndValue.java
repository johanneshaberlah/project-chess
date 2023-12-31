package org.iu.chess.move;

public record MoveAndValue(Move move, int value, boolean error) {

  public boolean isLegal() {
    return !error;
  }
}
