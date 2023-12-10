package org.iu.chess.piece;

import org.iu.chess.Board;
import org.iu.chess.move.Move;
import org.iu.chess.move.MoveRequirement;
import org.iu.chess.move.MoveRequirementValidator;
import org.iu.chess.move.RelativeMoveWithRequirement;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Rook extends Piece {

  private Rook(PieceColor color) {
    super("Rook", color, "R", new File("piece/rook.png"));
  }

  @Override
  public boolean isLegalMove(Board board, Move move) {
    var reachableMove = reachableMoves().stream()
      .filter(relativeMove -> relativeMove.move().equals(move.asRelativeMove()))
      .findFirst()
      .orElse(null);

    return reachableMove != null && MoveRequirementValidator.validateMove(board, move, reachableMove.requirement());
  }

  @Override
  public Collection<RelativeMoveWithRequirement> reachableMoves() {
    List<RelativeMoveWithRequirement> legalMoves = List.of(
      // Move vertically
      RelativeMoveWithRequirement.of(0, 1, MoveRequirement.REQUIRES_EMPTY_TARGET_SQUARE),
      RelativeMoveWithRequirement.of(0, -1, MoveRequirement.REQUIRES_EMPTY_TARGET_SQUARE),

      // Move horizontally
      RelativeMoveWithRequirement.of(1, 0, MoveRequirement.REQUIRES_EMPTY_TARGET_SQUARE),
      RelativeMoveWithRequirement.of(-1, 0, MoveRequirement.REQUIRES_EMPTY_TARGET_SQUARE)
    );

    return legalMoves.stream().map(relativeMove -> {
      if (this.color().equals(PieceColor.BLACK)) {
        // Invert the moves for the black rook
        return relativeMove.invert();
      }
      return relativeMove;
    }).collect(Collectors.toList());
  }

  public static Rook ofColor(PieceColor color) {
    return new Rook(color);
  }
}
