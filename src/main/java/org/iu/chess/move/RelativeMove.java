package org.iu.chess.move;

import org.iu.chess.Square;

public record RelativeMove(int fileDifference, int rankDifference) {

  /**
   * Erzeugt einen lokalisierten Zug ausgehend von einem Startfeld.
   *
   * @param base Das Startfeld der Figur
   * @return Den lokalisierten Zug.
   */
  public Move asLocalizedMove(Square base) {
    return new Move(
      /* from = */ base,
      /* to = */ base
        .withFile(base.file() + fileDifference)   // Addiere file-Komponente des Richtungsvektors
        .withRank(base.rank() + rankDifference)  // Addiere rank-Komponente des Richtungsvektors
    );
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
