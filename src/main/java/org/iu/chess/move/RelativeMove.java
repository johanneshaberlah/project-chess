package org.iu.chess.move;

public record RelativeMove(int fileDifference, int rankDifference) {

  // Inverts the rank, used to handle the difference between black and white pieces
  public RelativeMove invert() {
    return new RelativeMove(fileDifference, -rankDifference);
  }

  public static RelativeMove of(int fileDifference, int rankDifference) {
    return new RelativeMove(fileDifference, rankDifference);
  }
}
