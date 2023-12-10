package org.iu.chess.game.player;

import java.util.Optional;

public class Player {
  private final String name;
  private final Optional<PlayerClock> clock;

  public Player(String name, Optional<PlayerClock> clock) {
    this.name = name;
    this.clock = clock;
  }

  public String name() {
    return name;
  }

  public Optional<PlayerClock> clock() {
    return clock;
  }
}
