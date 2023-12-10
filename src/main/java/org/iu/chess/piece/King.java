package org.iu.chess.piece;

import org.iu.chess.move.MoveRequirement;
import org.iu.chess.move.RelativeMoveWithRequirement;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class King extends Piece {
  private static final String KING_IMAGE_PATH = "piece/king.png";

  private King(PieceColor color) {
    super("King", color, "K", new File(KING_IMAGE_PATH));
  }

  @Override
  public Collection<RelativeMoveWithRequirement> reachableMoves() {
    List<RelativeMoveWithRequirement> legalMoves = new ArrayList<>();
    legalMoves.add(RelativeMoveWithRequirement.of(1, 0, MoveRequirement.REQUIRES_EMPTY_TARGET_SQUARE));
    legalMoves.add(RelativeMoveWithRequirement.of(-1, 0, MoveRequirement.REQUIRES_EMPTY_TARGET_SQUARE));
    legalMoves.add(RelativeMoveWithRequirement.of(0, 1, MoveRequirement.REQUIRES_EMPTY_TARGET_SQUARE));
    legalMoves.add(RelativeMoveWithRequirement.of(0, -1, MoveRequirement.REQUIRES_EMPTY_TARGET_SQUARE));
    legalMoves.add(RelativeMoveWithRequirement.of(1, 1, MoveRequirement.REQUIRES_EMPTY_TARGET_SQUARE));
    legalMoves.add(RelativeMoveWithRequirement.of(1, -1, MoveRequirement.REQUIRES_EMPTY_TARGET_SQUARE));
    legalMoves.add(RelativeMoveWithRequirement.of(-1, 1, MoveRequirement.REQUIRES_EMPTY_TARGET_SQUARE));
    legalMoves.add(RelativeMoveWithRequirement.of(-1, -1, MoveRequirement.REQUIRES_EMPTY_TARGET_SQUARE));

    if (this.color().equals(PieceColor.WHITE)) {
      return legalMoves;
    }
    return legalMoves.stream().map(RelativeMoveWithRequirement::invert).collect(Collectors.toList());

  }

  public static King ofColor(PieceColor color) {
    return new King(color);
  }
}
