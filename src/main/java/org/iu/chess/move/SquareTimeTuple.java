package org.iu.chess.move;

import org.iu.chess.Square;

public record SquareTimeTuple(Square square, long time) {
  private static final long TIMEOUT = 10_000;

  public boolean isTimedOut() {
    return System.currentTimeMillis() - time > TIMEOUT;
  }
}
