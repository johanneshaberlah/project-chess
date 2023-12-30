package org.iu.chess.game.artificial;

import org.iu.chess.game.Game;
import org.iu.chess.game.player.Player;
import org.iu.chess.game.player.PlayerClock;
import org.iu.chess.move.Move;
import org.iu.chess.piece.PieceColor;

import java.util.HashSet;
import java.util.Optional;

public abstract class ArtificialPlayer extends Player {
  private final PieceColor pieceColor;

  public ArtificialPlayer(String name, PieceColor pieceColor, Optional<PlayerClock> clock) {
    super(name, new HashSet<>(), clock);
    this.pieceColor = pieceColor;
  }

  public abstract Move recommendMove(Game game);
}
