package org.iu.chess.game.player;

import org.iu.chess.game.GameTimingStrategy;

public class PlayerClock {
  private int timeRemaining;

  private PlayerClock(int timeRemaining) {
    this.timeRemaining = timeRemaining;
  }

  public void addIncrement(GameTimingStrategy strategy) {
    timeRemaining += strategy.increment();
  }

  public static PlayerClock fromStrategy(GameTimingStrategy strategy) {
    return new PlayerClock(strategy.initialTime());
  }
}
