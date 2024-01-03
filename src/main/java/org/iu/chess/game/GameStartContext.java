package org.iu.chess.game;

import java.util.Optional;

public record GameStartContext(int timeWhite, int timeBlack, int increment, String difficulty, String mode, Optional<String> customFen) {

  public GameStartContext withFen(String fen, int timeWhite, int timeBlack) {
    return new GameStartContext(Math.max(0, timeWhite), Math.max(0, timeBlack), Math.max(0, increment), difficulty, mode, Optional.of(fen));
  }
}
