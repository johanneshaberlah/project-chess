package org.iu.chess.piece;

import org.iu.chess.move.MoveRequirement;
import org.iu.chess.move.RelativeMoveWithRequirement;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class King extends Piece {

  private King(PieceColor color) {
    super("King", color, 'K');
  }

  @Override
  public Collection<RelativeMoveWithRequirement> reachableMoves() {
    List<RelativeMoveWithRequirement> legalMoves = new ArrayList<>();

    int[] offsets = {1, -1, 0};

    for (int i : offsets) {
      for (int j : offsets) {
        if (i != 0 || j != 0) {
          legalMoves.add(RelativeMoveWithRequirement.of(i, j));
        }
      }
    }
    legalMoves.add(RelativeMoveWithRequirement.withMutualTarget(3,0, MoveRequirement.CASTLING));
    legalMoves.add(RelativeMoveWithRequirement.withMutualTarget(-4,0, MoveRequirement.CASTLING));

    if (this.color().equals(PieceColor.WHITE)) {
      return legalMoves;
    }
    return legalMoves.stream().map(RelativeMoveWithRequirement::invert).collect(Collectors.toList());

  }

  public static King ofColor(PieceColor color) {
    return new King(color);
  }
}
