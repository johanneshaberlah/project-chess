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

    int[] offsets = {1, -1, 0};

    for (int i : offsets) {
      for (int j : offsets) {
        if (i != 0 || j != 0) {
          legalMoves.add(RelativeMoveWithRequirement.of(i, j, MoveRequirement.REQUIRES_EMPTY_TARGET_SQUARE));
        }
      }
    }

    if (this.color().equals(PieceColor.WHITE)) {
      return legalMoves;
    }
    return legalMoves.stream().map(RelativeMoveWithRequirement::invert).collect(Collectors.toList());

  }

  public static King ofColor(PieceColor color) {
    return new King(color);
  }
}
