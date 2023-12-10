package org.iu.chess.move;

public record RelativeMoveWithRequirement(RelativeMove move, MoveRequirement requirement) {

  public RelativeMoveWithRequirement invert() {
    return new RelativeMoveWithRequirement(move.invert(), requirement);
  }

  public static RelativeMoveWithRequirement of(int fileDifference, int rankDifference, MoveRequirement requirement) {
    return new RelativeMoveWithRequirement(RelativeMove.of(fileDifference, rankDifference), requirement);
  }
}
