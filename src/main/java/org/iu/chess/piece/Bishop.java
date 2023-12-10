package org.iu.chess.piece;

import org.iu.chess.move.MoveRequirement;
import org.iu.chess.move.RelativeMoveWithRequirement;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Bishop extends Piece {
  private static final String BISHOP_IMAGE_PATH = "piece/bishop.png";

  private Bishop(PieceColor color) {
    super("Bishop", color, "B");
  }

  @Override
  public Collection<RelativeMoveWithRequirement> reachableMoves() {
    List<RelativeMoveWithRequirement> legalMoves = new ArrayList<>();
    for (int index = 1; index < 8; index++) {
      legalMoves.add(RelativeMoveWithRequirement.of(index, index, MoveRequirement.REQUIRES_EMPTY_DIAGONAL));
      legalMoves.add(RelativeMoveWithRequirement.of(-index, index, MoveRequirement.REQUIRES_EMPTY_DIAGONAL));
      legalMoves.add(RelativeMoveWithRequirement.of(index, -index, MoveRequirement.REQUIRES_EMPTY_DIAGONAL));
      legalMoves.add(RelativeMoveWithRequirement.of(-index, -index, MoveRequirement.REQUIRES_EMPTY_DIAGONAL));
    }

    if (this.color().equals(PieceColor.WHITE)) {
      return legalMoves;
    }
    return legalMoves.stream().map(RelativeMoveWithRequirement::invert).collect(Collectors.toList());

  }

  public static Bishop ofColor(PieceColor color) {
    return new Bishop(color);
  }
}
