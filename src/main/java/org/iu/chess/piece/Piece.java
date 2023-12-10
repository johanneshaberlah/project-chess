package org.iu.chess.piece;

import org.iu.chess.Square;
import org.iu.chess.board.Board;
import org.iu.chess.move.Move;
import org.iu.chess.move.MoveRequirementValidator;
import org.iu.chess.move.RelativeMoveWithRequirement;

import java.util.Collection;

public abstract class Piece {
  private final String name;
  private final PieceColor color;
  private final char fenName;

  private boolean hasMoved;

  public Piece(String name, PieceColor color, char fenName) {
    this.name = name;
    this.color = color;
    this.fenName = fenName;
  }

  public boolean hasMoved() {
    return hasMoved;
  }

  public void declareMoved() {
    hasMoved = true;
  }

  public Collection<Move> legalMoves(Board board, Square square) {
    return reachableMoves().stream()
      .map(move -> move.move().asMove(square))
      .filter(move -> isLegalMove(board, move))
      .toList();
  }

  /**
   * Check if a move is legal based on the given position on the board.
   *
   * @param board
   * @param move
   * @return true if the move is legal, false otherwise
   */
  public boolean isLegalMove(Board board, Move move) {
    if (board.pieceAt(move.from()) == null || board.pieceAt(move.to()) == null) {
      return false;
    }
    var reachableMove = reachableMoves().stream()
      .filter(relativeMove -> relativeMove.move().equals(move.asRelativeMove()))
      .findFirst();
    return reachableMove.isPresent() && reachableMove.filter(relativeMoveWithRequirement -> {
      for (var requirement : relativeMoveWithRequirement.requirement()) {
        if (!MoveRequirementValidator.validateMove(board, move, requirement)) {
          return false;
        }
      }
      return true;
    }).isPresent();
  }

  public PieceColor otherColor() {
    return color == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE;
  }

  /**
   * Returns all the reacheable moves from the given square without taking other pieces into account.
   *
   * @return a collection of reacheable moves
   */
  public abstract Collection<RelativeMoveWithRequirement> reachableMoves();

  public String name() {
    return name;
  }

  public char fenName() {
    return fenName;
  }

  public PieceColor color() {
    return color;
  }
}
