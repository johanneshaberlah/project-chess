package org.iu.chess.game;

import org.iu.chess.board.BoardFactory;
import org.iu.chess.game.artificial.ArtificialPlayer;
import org.iu.chess.game.artificial.MinimaxPlayer;
import org.iu.chess.game.player.Player;
import org.iu.chess.game.player.PlayerClock;
import org.iu.chess.game.player.PlayerTuple;
import org.iu.chess.piece.PieceColor;

import java.util.HashSet;
import java.util.Optional;
import java.util.Stack;

public class GameFactory {
  private static final String WHITE_COLOR = "Weiß";
  private static final String BLACK_COLOR = "Schwarz";

  private GameFactory() {
  }

  public Game of(GameStartContext context, Optional<GameTimingStrategy> strategy) {
    return context.mode().equals("AI") ?
      withTimingAndPlayer(context, strategy, MinimaxPlayer.of(PieceColor.BLACK, strategy.map(PlayerClock::fromStrategy)))
      : withTiming(context, strategy);
  }

  public Game withTiming(GameStartContext context, Optional<GameTimingStrategy> timing) {
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
      BoardFactory.startingPosition(context)
    );
  }

  public Game withTimingAndPlayer(GameStartContext context, Optional<GameTimingStrategy> timing, ArtificialPlayer artificialPlayer) {
    return new Game(
      timing,
      new PlayerTuple(
        new Player(
          WHITE_COLOR,
          new HashSet<>(),
          timing.map(PlayerClock::fromStrategy)
        ),
        artificialPlayer
      ),
      new Stack<>(),
      BoardFactory.startingPosition(context)
    );
  }

  public static GameFactory create() {
    return new GameFactory();
  }
}
