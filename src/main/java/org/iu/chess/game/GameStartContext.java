package org.iu.chess.game;

import java.util.Optional;

public record GameStartContext(int time, int increment, String difficulty, String mode, Optional<String> customFen) {

  public GameStartContext withFen(String fen) {
    return new GameStartContext(time, increment, difficulty, mode, Optional.of(fen));
  }
}
