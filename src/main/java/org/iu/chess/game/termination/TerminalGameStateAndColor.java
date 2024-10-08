package org.iu.chess.game.termination;

import org.iu.chess.piece.PieceColor;

import java.util.Optional;

public record TerminalGameStateAndColor(TerminalGameState state, Optional<PieceColor> color) {

  @Override
  public String toString() {
    if (color.isEmpty()) {
      return state.displayName();
    }
    return state.displayName() + " - Gewinner: " + color.map(PieceColor::displayName).orElse("-");
  }
}
