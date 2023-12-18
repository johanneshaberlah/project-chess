package org.iu.chess.game;

import com.google.common.base.Preconditions;
import org.iu.chess.move.MoveTuple;
import org.iu.chess.piece.PieceColor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Special Game States:
 * - Draw (3 fold repetition)
 * - Draw (stalemate)
 * - Draw (insufficient material)
 * - Checkmate
 */
public class TerminalGameStateComponent {
  private ChessGame chessGame;

  private TerminalGameStateComponent(ChessGame chessGame) {
    this.chessGame = chessGame;
  }

  public Optional<TerminalGameStateAndColor> evaluateTerminalGameState() {
    Optional<PieceColor> checkMateColor = Arrays.stream(PieceColor.values()).filter(this::isCheckMate).findFirst();
    if (checkMateColor.isPresent()) {
      return Optional.of(new TerminalGameStateAndColor(TerminalGameState.CHECKMATE, checkMateColor));
    }
    Optional<PieceColor> staleMateColor = Arrays.stream(PieceColor.values()).filter(this::isStaleMate).findFirst();
    if (staleMateColor.isPresent()) {
      return Optional.of(new TerminalGameStateAndColor(TerminalGameState.DRAW_STALEMATE, checkMateColor));
    }
    if (isInsufficientMaterial()) {
      return Optional.of(new TerminalGameStateAndColor(TerminalGameState.DRAW_INSUFFICIENT_MATERIAL, Optional.empty()));
    }
    if (isThreefoldRepetition()) {
      return Optional.of(new TerminalGameStateAndColor(TerminalGameState.DRAW_THREEFOLD, Optional.empty()));
    }
    return Optional.empty();
  }

  private boolean isCheckMate(PieceColor color) {
    return chessGame.position().isCheckMate(chessGame.position(), color);
  }

  private boolean isStaleMate(PieceColor color) {
    return chessGame.position().isStaleMate(chessGame.position(), color);
  }

  private boolean isInsufficientMaterial() {
    // Insufficient material if there is no piece except both kings
    return Arrays.stream(PieceColor.values())
      .map(chessGame.position()::piecesWithColor)
      .anyMatch(squares -> squares.stream()
        .map(chessGame.position()::pieceAt)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .anyMatch(piece -> piece.fenName() != 'K'));
  }

  private boolean isThreefoldRepetition() {
    if (chessGame.moves().size() < 3 * 2) {
      return false;
    }
    // Check if the last three entry-sets of the stack are identical
    Set<MoveTuple> lastThreeMoves = new HashSet<MoveTuple>();
    for (int index = 0; index < 3; index++) {
      var left = chessGame.moves().pop();
      var right = chessGame.moves().pop();
      lastThreeMoves.add(new MoveTuple(left.move(), right.move()));
    }
    // Since Sets are unique, we just need to count the entries of the set
    return lastThreeMoves.size() == 1;
  }

  public static TerminalGameStateComponent of(ChessGame chessGame) {
    Preconditions.checkNotNull(chessGame);
    return new TerminalGameStateComponent(chessGame);
  }
}
