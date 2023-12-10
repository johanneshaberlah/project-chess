package org.iu.chess.piece;

import org.iu.chess.move.MoveRequirement;
import org.iu.chess.move.RelativeMoveWithRequirement;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Knight extends Piece {
  private static final String KNIGHT_IMAGE_PATH = "piece/knight.png";

  private Knight(PieceColor color) {
    super("Knight", color, "N", new File(KNIGHT_IMAGE_PATH));
  }

  @Override
  public Collection<RelativeMoveWithRequirement> reachableMoves() {
    List<RelativeMoveWithRequirement> legalMoves = new ArrayList<>();
    legalMoves.add(RelativeMoveWithRequirement.of(1, 2, MoveRequirement.REQUIRES_EMPTY_TARGET_SQUARE));
    legalMoves.add(RelativeMoveWithRequirement.of(1, -2, MoveRequirement.REQUIRES_EMPTY_TARGET_SQUARE));
    legalMoves.add(RelativeMoveWithRequirement.of(-1, 2, MoveRequirement.REQUIRES_EMPTY_TARGET_SQUARE));
    legalMoves.add(RelativeMoveWithRequirement.of(-1, -2, MoveRequirement.REQUIRES_EMPTY_TARGET_SQUARE));
    legalMoves.add(RelativeMoveWithRequirement.of(2, 1, MoveRequirement.REQUIRES_EMPTY_TARGET_SQUARE));
    legalMoves.add(RelativeMoveWithRequirement.of(2, -1, MoveRequirement.REQUIRES_EMPTY_TARGET_SQUARE));
    legalMoves.add(RelativeMoveWithRequirement.of(-2, 1, MoveRequirement.REQUIRES_EMPTY_TARGET_SQUARE));
    legalMoves.add(RelativeMoveWithRequirement.of(-2, -1, MoveRequirement.REQUIRES_EMPTY_TARGET_SQUARE));

    if (this.color().equals(PieceColor.WHITE)) {
      return legalMoves;
    }
    return legalMoves.stream().map(RelativeMoveWithRequirement::invert).collect(Collectors.toList());

  }

  public static Knight ofColor(PieceColor color) {
    return new Knight(color);
  }
}
