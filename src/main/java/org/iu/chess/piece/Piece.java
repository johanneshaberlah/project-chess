package org.iu.chess.piece;

import org.iu.chess.Board;
import org.iu.chess.move.Move;
import org.iu.chess.move.MoveRequirementValidator;
import org.iu.chess.move.RelativeMove;
import org.iu.chess.move.RelativeMoveWithRequirement;

import java.io.File;
import java.util.Collection;

public abstract class Piece {
  private final String name;
  private final PieceColor color;
  private final String fenName;
  private final File image;

  public Piece(String name, PieceColor color, String fenName, File image) {
    this.name = name;
    this.color = color;
    this.fenName = fenName;
    this.image = image;
  }

  /**
   * Check if a move is legal based on the given position on the board.
   *
   * @param board
   * @param move
   * @return true if the move is legal, false otherwise
   */
  public boolean isLegalMove(Board board, Move move) {
    var reachableMove = reachableMoves().stream()
      .filter(relativeMove -> relativeMove.move().equals(move.asRelativeMove()))
      .findFirst();
    return reachableMove.isPresent() && MoveRequirementValidator.validateMove(board, move, reachableMove.get().requirement());
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

  public String fenName() {
    return fenName;
  }

  public File image() {
    return image;
  }

  public PieceColor color() {
    return color;
  }
}
