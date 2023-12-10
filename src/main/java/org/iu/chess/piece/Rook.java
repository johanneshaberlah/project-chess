package org.iu.chess.piece;

import com.google.common.collect.Lists;
import org.iu.chess.move.MoveRequirement;
import org.iu.chess.move.RelativeMoveWithRequirement;

import java.io.File;
import java.util.Collection;
import java.util.stream.Collectors;

public class Rook extends Piece {

  private Rook(PieceColor color) {
    super("Rook", color, "R", new File("piece/rook.png"));
  }

  @Override
  public Collection<RelativeMoveWithRequirement> reachableMoves() {
    Collection<RelativeMoveWithRequirement> legalMoves = Lists.newArrayList();

    for (int index = 1; index < 8; index++) {
      legalMoves.add(RelativeMoveWithRequirement.of(0, index, MoveRequirement.REQUIRES_EMPTY_RANK));
      legalMoves.add(RelativeMoveWithRequirement.of(0, -index, MoveRequirement.REQUIRES_EMPTY_RANK));
      legalMoves.add(RelativeMoveWithRequirement.of(index, 0, MoveRequirement.REQUIRES_EMPTY_FILE));
      legalMoves.add(RelativeMoveWithRequirement.of(-index, 0, MoveRequirement.REQUIRES_EMPTY_FILE));
    }

    if (this.color().equals(PieceColor.WHITE)) {
      return legalMoves;
    }
    return legalMoves.stream().map(RelativeMoveWithRequirement::invert).collect(Collectors.toList());

  }

  public static Rook ofColor(PieceColor color) {
    return new Rook(color);
  }
}
