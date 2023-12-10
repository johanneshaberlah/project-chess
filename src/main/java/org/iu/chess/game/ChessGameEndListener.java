package org.iu.chess.game;

import org.iu.chess.piece.PieceColor;

import java.util.Optional;

@FunctionalInterface
public interface ChessGameEndListener {
  void onGameEnd(Optional<PieceColor> winner);
}
