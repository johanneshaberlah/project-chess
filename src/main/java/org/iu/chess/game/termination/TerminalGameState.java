package org.iu.chess.game.termination;

public enum TerminalGameState {
  CHECKMATE("Schachmatt"),
  DRAW_THREEFOLD("Zugwiederholung"),
  DRAW_STALEMATE("Patt - Stalemate"),
  DRAW_INSUFFICIENT_MATERIAL("Patt"),
  OUT_OF_TIME("Zeit abgelaufen");

  private final String displayName;

  TerminalGameState(String name) {
    this.displayName = name;
  }

  public String displayName() {
    return displayName;
  }
}
