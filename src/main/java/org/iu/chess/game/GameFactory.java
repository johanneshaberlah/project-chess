package org.iu.chess.game;

import org.iu.chess.board.BoardFactory;
import org.iu.chess.game.artificial.ArtificialPlayer;
import org.iu.chess.game.player.Player;
import org.iu.chess.game.player.PlayerClock;
import org.iu.chess.game.player.PlayerTuple;

import java.util.HashSet;
import java.util.Optional;
import java.util.Stack;

public class GameFactory {
  private static final String WHITE_COLOR = "Weiß";
  private static final String BLACK_COLOR = "Schwarz";

  private GameFactory() {
  }

  public Game withTiming(Optional<GameTimingStrategy> timing) {
    return new Game(
      timing,
      new PlayerTuple(
        new Player(
          WHITE_COLOR,
          new HashSet<>(),
          timing.map(PlayerClock::fromStrategy)
        ),
        new Player(
          BLACK_COLOR,
          new HashSet<>(),
          timing.map(PlayerClock::fromStrategy)
        )
      ),
      new Stack<>(),
      BoardFactory.startingPosition()
    );
  }

  public Game withTimingAgainstComputer(Optional<GameTimingStrategy> timing) {
    return new Game(
      timing,
      new PlayerTuple(
        new Player(
          WHITE_COLOR,
          new HashSet<>(),
          timing.map(PlayerClock::fromStrategy)
        ),
        ArtificialPlayer.create(timing.map(PlayerClock::fromStrategy))
      ),
      new Stack<>(),
      BoardFactory.startingPosition()
    );
  }

  public static GameFactory create() {
    return new GameFactory();
  }
}
