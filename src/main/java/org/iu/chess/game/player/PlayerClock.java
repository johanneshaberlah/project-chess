package org.iu.chess.game.player;

import org.iu.chess.game.GameTimingStrategy;

public class PlayerClock {
  private int timeRemaining;
  private long moveBeginTime;

  private PlayerClock(int timeRemaining) {
    this.timeRemaining = timeRemaining;
  }

  public void beginMove() {
    moveBeginTime = System.currentTimeMillis();
  }

  public void finishMove() {
    timeRemaining -= (System.currentTimeMillis() - moveBeginTime) / 1000;
  }

  public int timeRemaining() {
    return timeRemaining;
  }

  public void addIncrement(GameTimingStrategy strategy) {
    timeRemaining += strategy.increment();
  }

  public static PlayerClock fromStrategy(GameTimingStrategy strategy) {
    return new PlayerClock(strategy.initialTime());
  }
}
