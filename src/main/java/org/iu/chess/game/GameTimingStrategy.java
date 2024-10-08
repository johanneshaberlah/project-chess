package org.iu.chess.game;

import com.google.common.base.Preconditions;

import java.util.Optional;

public record GameTimingStrategy(int initialTime, int increment) {

  public static Optional<GameTimingStrategy> of(GameStartContext context) {
    Preconditions.checkNotNull(context);
    return Optional.ofNullable(context.timeWhite() == -1 ? null : new GameTimingStrategy(context.timeWhite(), context.increment()));
  }
}
