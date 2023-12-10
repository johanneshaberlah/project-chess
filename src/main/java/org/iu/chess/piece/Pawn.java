package org.iu.chess.piece;

import org.iu.chess.move.*;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Pawn extends Piece {

  private Pawn(PieceColor color) {
    super("Pawn", color, 'P');
  }

  @Override
  public Collection<RelativeMoveWithRequirement> reachableMoves() {
    var legalWhiteMoves = List.of(
      RelativeMoveWithRequirement.of(0, 1, MoveRequirement.REQUIRES_EMPTY_TARGET_SQUARE), // 1 Square forward
      RelativeMoveWithRequirement.of(1, 1, MoveRequirement.REQUIRES_PIECE_AT_TARGET_SQUARE), // 1 Square forward, 1 right
      RelativeMoveWithRequirement.of(-1, 1, MoveRequirement.REQUIRES_PIECE_AT_TARGET_SQUARE) // 1 Square forward, 1 left
    );
    if (this.color().equals(PieceColor.WHITE)) {
      return legalWhiteMoves;
    }
    return legalWhiteMoves.stream().map(RelativeMoveWithRequirement::invert).collect(Collectors.toList());
  }

  public static Pawn ofColor(PieceColor color) {
    return new Pawn(color);
  }
}
