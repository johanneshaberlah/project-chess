package org.iu.chess.move;

import com.google.common.base.Preconditions;

public class IllegalMoveException extends Exception {
  private IllegalMoveException(Move move) {
    super(String.format("The move %s is not legal.", move.asFenNotation()));
  }

  public static IllegalMoveException of(Move move) {
    Preconditions.checkNotNull(move);
    return new IllegalMoveException(move);
  }
}
