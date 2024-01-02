package org.iu.chess.piece;

import org.iu.chess.move.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Pawn extends Piece {

  private Pawn(PieceColor color) {
    super("Pawn", color, 'P', 5);
  }

  @Override
  public Collection<RelativeMoveWithRequirement> reachableMoves() {
    var legalWhiteMoves = List.of(
      RelativeMoveWithRequirement.of(0, 1, MoveRequirement.REQUIRES_EMPTY_TARGET_SQUARE), // Ein Feld vor
      RelativeMoveWithRequirement.of(1, 1, MoveRequirement.REQUIRES_EN_PASSANT_OR_PIECE), // Bauer schlägt eine Figur rechts
      RelativeMoveWithRequirement.of(-1, 1, MoveRequirement.REQUIRES_EN_PASSANT_OR_PIECE), // Bauer schlägt eine Figur links
      RelativeMoveWithRequirement.of(
        0,
        2,
        MoveRequirement.PIECE_NEVER_MOVED,  // Nur wenn noch nicht bewegt
        MoveRequirement.REQUIRES_EMPTY_RANK, // Nur wenn keine Figur "im Weg" ist
        MoveRequirement.REQUIRES_EMPTY_TARGET_SQUARE // Nur wenn keine Figur auf dem Ziel-Feld steht
      )
    );
    if (this.color().equals(PieceColor.WHITE)) {
      return legalWhiteMoves;
    }
    return legalWhiteMoves.stream().map(RelativeMoveWithRequirement::invert).collect(Collectors.toList());
  }

  public static Pawn ofColor(PieceColor color) {
    return new Pawn(color);
  }
}
