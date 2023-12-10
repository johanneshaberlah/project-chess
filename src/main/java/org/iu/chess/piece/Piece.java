package org.iu.chess.piece;

import org.iu.chess.board.Board;
import org.iu.chess.move.Move;
import org.iu.chess.move.MoveRequirementValidator;
import org.iu.chess.move.RelativeMoveWithRequirement;

import java.io.File;
import java.util.Collection;

public abstract class Piece {
  private final String name;
  private final PieceColor color;
  private final char fenName;

  public Piece(String name, PieceColor color, char fenName) {
    this.name = name;
    this.color = color;
    this.fenName = fenName;
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
    return reachableMove.isPresent() && reachableMove.filter(relativeMoveWithRequirement -> MoveRequirementValidator.validateMove(board, move, relativeMoveWithRequirement.requirement())).isPresent();
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
