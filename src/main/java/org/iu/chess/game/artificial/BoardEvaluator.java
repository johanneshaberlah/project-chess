package org.iu.chess.game.artificial;

import org.iu.chess.board.Board;
import org.iu.chess.piece.PieceColor;

import java.util.concurrent.atomic.AtomicInteger;

public class BoardEvaluator {

  public static int evaluateBoard(Board board) {
    return evaluateMaterial(board, PieceColor.WHITE) - evaluateMaterial(board, PieceColor.BLACK);
  }

  private static int evaluateMaterial(Board board, PieceColor pieceColor) {
    AtomicInteger value = new AtomicInteger(0);
    board.piecesWithColor(pieceColor)
      .forEach(square -> board.pieceAt(square).ifPresent(piece -> value.getAndAdd(piece.value())));
    return value.get();
  }
}
