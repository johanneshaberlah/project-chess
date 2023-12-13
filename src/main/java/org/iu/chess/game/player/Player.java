package org.iu.chess.game.player;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import org.iu.chess.common.ObservableSet;
import org.iu.chess.piece.Piece;

import java.util.Optional;
import java.util.Set;

public class Player {
  private final String name;
  private final ObservableSet<Piece> lostPieces;
  private final Optional<PlayerClock> clock;

  public Player(String name, Set<Piece> lostPieces, Optional<PlayerClock> clock) {
    this.name = name;
    this.lostPieces = ObservableSet.of(lostPieces);
    this.clock = clock;
  }

  public void loose(Piece piece) {
    Preconditions.checkNotNull(piece);
    lostPieces.add(piece);
  }

  public ObservableSet<Piece> lostPieces() {
    return lostPieces;
  }

  public String name() {
    return name;
  }

  public Optional<PlayerClock> clock() {
    return clock;
  }
}
