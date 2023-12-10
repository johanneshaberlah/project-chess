package org.iu.chess.game.artificial;

import org.iu.chess.game.ChessGame;
import org.iu.chess.game.player.Player;
import org.iu.chess.game.player.PlayerClock;

import java.util.Optional;

public class ArtificialPlayer extends Player {
  private ArtificialPlayer(Optional<PlayerClock> clock) {
    super("Computer", clock);
  }

  public void makeMove(ChessGame chessGame) {

  }

  public static ArtificialPlayer create(Optional<PlayerClock> clock) {
    return new ArtificialPlayer(clock);
  }
}
