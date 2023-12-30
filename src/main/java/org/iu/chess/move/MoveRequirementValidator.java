package org.iu.chess.move;

import org.iu.chess.board.Board;
import org.iu.chess.game.player.PlayerMove;
import org.iu.chess.piece.Pawn;
import org.iu.chess.piece.Piece;
import org.iu.chess.piece.PieceColor;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

public class MoveRequirementValidator {

  public static boolean validateMove(Board board, Move move, MoveRequirement requirement) {
    return switch (requirement) {
      case REQUIRES_NO_MUTUAL_PIECE_AT_TARGET_SQUARE ->
        board.pieceAt(move.to()).isEmpty() || !board.pieceAt(move.to()).get().color().equals(board.pieceAt(move.from()).get().color());
      case REQUIRES_PIECE_AT_TARGET_SQUARE -> board.pieceAt(move.to()).isPresent();
      case REQUIRES_EMPTY_TARGET_SQUARE -> board.pieceAt(move.to()).isEmpty();
      case REQUIRES_EMPTY_RANK -> isRankEmpty(board, move);
      case REQUIRES_EMPTY_FILE -> isFileEmpty(board, move);
      case REQUIRES_EMPTY_DIAGONAL -> isDiagonalEmpty(board, move);
      case PIECE_NEVER_MOVED -> checkNeverMoved(board, move);
      case CASTLING -> isCastlingAllowed(board, move);
      case REQUIRES_EN_PASSANT_OR_PIECE -> checkEnPassantOrPiece(board, move);
      case NONE -> true;
    };
  }

  private static boolean checkEnPassantOrPiece(Board board, Move move) {
    if (board.pieceAt(move.to()).isPresent()) {
      return true;
    }
    if (board.lastMove().isEmpty()) {
      return false;
    }
    Optional<Piece> lastMovedPiece = board.pieceAt(board.lastMove().get().to());
    if (lastMovedPiece.isEmpty()) {
      return false;
    }
    Piece piece = lastMovedPiece.get();
    if (piece.fenName() != 'P') {
      return false;
    }
    if (board.lastMove().get().to().file() != move.to().file()) {
      return false;
    }
    return move.to().rank() - board.lastMove().get().to().rank() == (move.from().rank() < move.to().rank() ? 1 : -1);
  }

  private static boolean isRankEmpty(Board board, Move move) {
    return IntStream.range(move.minimalRank() + 1, move.maximalRank())
      .mapToObj(index -> board.pieceAt(move.from().withRank(index)))
      .filter(Objects::nonNull)
      .noneMatch(Optional::isPresent);
  }

  private static boolean isFileEmpty(Board board, Move move) {
    return IntStream.range(move.minimalFile() + 1, move.maximalFile())
      .mapToObj(index -> board.pieceAt(move.from().withFile(index)))
      .filter(Objects::nonNull)
      .noneMatch(Optional::isPresent);
  }

  private static boolean isDiagonalEmpty(Board board, Move move) {
    return IntStream.range(1, move.rankDistance())
      .mapToObj(index -> {
        var fileDirection = index;
        if (move.from().file() > move.to().file()) {
          fileDirection = -fileDirection;
        }
        var rankDirection = index;
        if (move.from().rank() > move.to().rank()) {
          rankDirection = -rankDirection;
        }
        return board.pieceAt(move.from().withRank(move.from().rank() + rankDirection).withFile(move.from().file() + fileDirection));
      }).filter(Objects::nonNull)
      .noneMatch(Optional::isPresent);
  }

  private static boolean isCastlingAllowed(Board board, Move move) {
    // Or else null to avoid having second variables to store the value of the optional, not ideal, but okay here
    var from = board.pieceAt(move.from()).orElse(null);
    var to = board.pieceAt(move.to()).orElse(null);
    if (from == null || to == null) {
      return false;
    }
    if (from.hasMoved() || to.hasMoved()) {
      return false;
    }
    if (from.fenName() != 'K' || to.fenName() != 'R') {
      return false;
    }
    return isFileEmpty(board, move);
  }

  private static boolean checkNeverMoved(Board board, Move move) {
    Piece piece = board.pieceAt(move.from()).get();
    if (piece instanceof Pawn) {
      return move.to().rank() == (piece.color().equals(PieceColor.WHITE) ? 1 : 6);
    }
    return !piece.hasMoved();
  }
}
