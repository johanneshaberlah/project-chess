package org.iu.chess.move;

import org.iu.chess.board.Board;

import java.util.Optional;
import java.util.stream.IntStream;

public class MoveRequirementValidator {

  public static boolean validateMove(Board board, Move move, MoveRequirement requirement) {
    return switch (requirement) {
      case REQUIRES_PIECE_AT_TARGET_SQUARE -> board.pieceAt(move.to()).isPresent();
      case REQUIRES_EMPTY_TARGET_SQUARE -> board.pieceAt(move.to()).isEmpty();
      case REQUIRES_EMPTY_RANK -> isRankEmpty(board, move);
      case REQUIRES_EMPTY_FILE -> isFileEmpty(board, move);
      case REQUIRES_EMPTY_DIAGONAL -> isDiagonalEmpty(board, move);
    };
  }

  private static boolean isRankEmpty(Board board, Move move) {
    return IntStream.range(move.minimalRank() + 1, move.maximalRank())
      .mapToObj(index -> board.pieceAt(move.from().withRank(index)))
      .anyMatch(Optional::isPresent);
  }

  private static boolean isFileEmpty(Board board, Move move) {
    return IntStream.range(move.minimalFile() + 1, move.maximalFile())
      .mapToObj(index -> board.pieceAt(move.from().withFile(index)))
      .anyMatch(Optional::isPresent);
  }

  private static boolean isDiagonalEmpty(Board board, Move move) {
    return IntStream.range(move.minimalRank() + 1, move.maximalRank())
      .mapToObj(index -> board.pieceAt(move.from().addDiagonally(index - move.minimalRank())))
      .anyMatch(Optional::isPresent);
  }
}
