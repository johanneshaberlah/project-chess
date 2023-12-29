package org.iu.chess.game.player;

import com.google.common.base.Stopwatch;
import org.iu.chess.game.GameTimingStrategy;

import java.util.concurrent.TimeUnit;

public class PlayerClock {
  private final int increment;
  private int timeRemaining;
  private Stopwatch moveStopwatch;

  private PlayerClock(int timeRemaining, int increment) {
    this.timeRemaining = timeRemaining;
    this.increment = increment;
  }

  public void beginMove() {
    moveStopwatch = Stopwatch.createStarted();
  }

  public long currentTimeRemaining() {
    return timeRemaining - (moveStopwatch == null ? 0 : moveStopwatch.elapsed(TimeUnit.SECONDS));
  }

  public void pause() {
    if (moveStopwatch != null) {
      timeRemaining -= moveStopwatch.elapsed(TimeUnit.SECONDS);
      moveStopwatch = null;
    }
  }

  public void finishMove() {
    if (moveStopwatch != null) {
      timeRemaining -= moveStopwatch.elapsed(TimeUnit.SECONDS);
      moveStopwatch = null;
      this.addIncrement();
    }
  }

  public int timeRemaining() {
    return timeRemaining;
  }

  public int increment() {
    return increment;
  }

  private void addIncrement() {
    timeRemaining += increment;
  }

  public static PlayerClock fromStrategy(GameTimingStrategy strategy) {
    return new PlayerClock(strategy.initialTime() * 60, strategy.increment());
  }
}
