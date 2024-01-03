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
    var game = context.mode().equals("AI") ?
      withTimingAndPlayer(context, strategy, MinimaxPlayer.of(PieceColor.BLACK, strategy.map(PlayerClock::fromStrategy)))
      : withTiming(context, strategy);
    // Only for game load actions: Load the time of the previous game
    if (context.timeWhite() != context.timeBlack()) {
      game.players().white().clock().ifPresent(clock -> clock.customTimeRemaining(context.timeWhite()));
      game.players().black().clock().ifPresent(clock -> clock.customTimeRemaining(context.timeBlack()));
    }
    return game;
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
