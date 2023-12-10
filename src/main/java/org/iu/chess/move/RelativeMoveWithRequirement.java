package org.iu.chess.move;

public record RelativeMoveWithRequirement(RelativeMove move, MoveRequirement... requirement) {

  public RelativeMoveWithRequirement invert() {
    return new RelativeMoveWithRequirement(move.invert(), requirement);
  }

  public static RelativeMoveWithRequirement withMutualTarget(int fileDifference, int rankDifference, MoveRequirement... requirement) {
    return new RelativeMoveWithRequirement(RelativeMove.of(fileDifference, rankDifference), requirement);
  }

  public static RelativeMoveWithRequirement of(int fileDifference, int rankDifference, MoveRequirement... requirement) {;
    MoveRequirement[] newRequirements = new MoveRequirement[requirement.length + 1];
    System.arraycopy(requirement, 0, newRequirements, 0, requirement.length);
    newRequirements[requirement.length] = MoveRequirement.REQUIRES_NO_MUTUAL_PIECE_AT_TARGET_SQUARE;
    return new RelativeMoveWithRequirement(RelativeMove.of(fileDifference, rankDifference), newRequirements);
  }
}
