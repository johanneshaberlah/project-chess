package org.iu.chess.move;

import org.iu.chess.Square;

public record RelativeMove(int fileDifference, int rankDifference) {

  public Move asMove(Square base) {
    return new Move(base, base.withFile(base.file() + fileDifference).withRank(base.rank() + rankDifference));
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof RelativeMove move) {
      return move.fileDifference == fileDifference && move.rankDifference == rankDifference;
    }
    return false;
  }

  // Inverts the rank, used to handle the difference between black and white pieces
  public RelativeMove invert() {
    return new RelativeMove(fileDifference, -rankDifference);
  }

  public static RelativeMove of(int fileDifference, int rankDifference) {
    return new RelativeMove(fileDifference, rankDifference);
  }
}
