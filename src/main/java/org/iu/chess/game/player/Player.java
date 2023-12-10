package org.iu.chess.game.player;

public class Player {
  private final String name;
  private final PlayerClock clock;

  public Player(String name, PlayerClock clock) {
    this.name = name;
    this.clock = clock;
  }

  public String name() {
    return name;
  }

  public PlayerClock clock() {
    return clock;
  }
}
