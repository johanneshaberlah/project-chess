package org.iu.chess.piece;

import org.iu.chess.move.MoveRequirement;
import org.iu.chess.move.RelativeMoveWithRequirement;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Queen extends Piece {
  private static final String QUEEN_IMAGE_PATH = "piece/queen.png";

  private Queen(PieceColor color) {
    super("Queen", color, "Q", new File(QUEEN_IMAGE_PATH));
  }

  @Override
  public Collection<RelativeMoveWithRequirement> reachableMoves() {
    List<RelativeMoveWithRequirement> legalMoves = new ArrayList<>();

    int[] offsets = {1, -1, 2, -2, 3, -3, 4, -4, 5, -5, 6, -6, 7, -7};

    for (int i : offsets) {
      legalMoves.add(RelativeMoveWithRequirement.of(0, i, MoveRequirement.REQUIRES_EMPTY_RANK));
      legalMoves.add(RelativeMoveWithRequirement.of(i, 0, MoveRequirement.REQUIRES_EMPTY_FILE));
      legalMoves.add(RelativeMoveWithRequirement.of(i, i, MoveRequirement.REQUIRES_EMPTY_DIAGONAL));
      legalMoves.add(RelativeMoveWithRequirement.of(i, -i, MoveRequirement.REQUIRES_EMPTY_DIAGONAL));
    }

    if (this.color().equals(PieceColor.WHITE)) {
      return legalMoves;
    }
    return legalMoves.stream().map(RelativeMoveWithRequirement::invert).collect(Collectors.toList());

  }

  public static Queen ofColor(PieceColor color) {
    return new Queen(color);
  }
}
