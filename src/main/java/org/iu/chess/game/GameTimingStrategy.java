package org.iu.chess.game;

import com.google.common.base.Preconditions;

public record GameTimingStrategy(int initialTime, int increment) {

  public static GameTimingStrategy of(GameStartContext context) {
    Preconditions.checkNotNull(context);
    return new GameTimingStrategy(context.time(), context.increment());
  }
}
