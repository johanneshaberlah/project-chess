package org.iu.chess.piece;

import org.iu.chess.move.MoveRequirement;
import org.iu.chess.move.RelativeMoveWithRequirement;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Knight extends Piece {


  private Knight(PieceColor color) {
    super("Knight", color, 'N', 10);
  }

  @Override
  public Collection<RelativeMoveWithRequirement> reachableMoves() {
    List<RelativeMoveWithRequirement> legalMoves = new ArrayList<>();

    int[] offsets = {1, -1, 2, -2};

    for (int i : offsets) {
      for (int j : offsets) {
        if (Math.abs(i) != Math.abs(j)) {
          legalMoves.add(RelativeMoveWithRequirement.of(i, j, MoveRequirement.NONE));
        }
      }
    }

    if (this.color().equals(PieceColor.WHITE)) {
      return legalMoves;
    }
    return legalMoves.stream().map(RelativeMoveWithRequirement::invert).collect(Collectors.toList());

  }

  public static Knight ofColor(PieceColor color) {
    return new Knight(color);
  }
}
