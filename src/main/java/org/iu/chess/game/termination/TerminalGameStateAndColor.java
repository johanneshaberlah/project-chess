package org.iu.chess.game.termination;

import org.iu.chess.piece.PieceColor;

import java.util.Optional;

public record TerminalGameStateAndColor(TerminalGameState state, Optional<PieceColor> color) {
}
