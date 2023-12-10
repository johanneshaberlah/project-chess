package org.iu.chess.move;

import org.iu.chess.Board;

public class MoveRequirementValidator {

  public static boolean validateMove(Board board, Move move, MoveRequirement requirement) {
    return switch (requirement) {
      case REQUIRES_PIECE_AT_TARGET_SQUARE -> board.pieceAt(move.to()).isPresent();
      case REQUIRES_EMPTY_TARGET_SQUARE -> board.pieceAt(move.to()).isEmpty();
      case REQUIRES_EMPTY_RANK -> isRankEmpty(board, move);
      case REQUIRES_EMPTY_FILE -> isFileEmpty(board, move);
      case REQUIRES_EMPTY_DIAGONAL -> true;
    };
  }

  private static boolean isRankEmpty(Board board, Move move) {
    var startingRank = move.from().rank();
    var endingRank = move.to().rank();
    for (int i = Math.min(startingRank, endingRank) + 1; i < Math.max(startingRank, endingRank); i++) {
      if (board.pieceAt(move.from().withRank(i)).isEmpty()) {
        continue;
      }
      return false;
    }
    return true;
  }

  private static boolean isFileEmpty(Board board, Move move) {
    var startingFile = move.from().file();
    var endingFile = move.to().file();
    for (int i = Math.min(startingFile, endingFile) + 1; i < Math.max(startingFile, endingFile); i++) {
      if (board.pieceAt(move.from().withFile(i)).isEmpty()) {
        continue;
      }
      return false;
    }
    return true;
  }
}
