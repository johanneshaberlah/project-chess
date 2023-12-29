package org.iu.chess.game.termination;

import com.google.common.base.Preconditions;
import org.iu.chess.game.Game;
import org.iu.chess.piece.PieceColor;

import java.util.Arrays;
import java.util.Optional;

/**
 * Special Game States:
 * - Draw (3 fold repetition)
 * - Draw (stalemate)
 * - Draw (insufficient material)
 * - Checkmate
 */
public class TerminalGameStateComponent {
  private Game game;

  private TerminalGameStateComponent(Game game) {
    this.game = game;
  }

  public Optional<TerminalGameStateAndColor> evaluateTerminalGameState() {
    Optional<PieceColor> timeOverColor = Arrays.stream(PieceColor.values()).filter(this::isTimeOver).findFirst();
    if (timeOverColor.isPresent()) {
      return Optional.of(new TerminalGameStateAndColor(TerminalGameState.OUT_OF_TIME, timeOverColor));
    }
    Optional<PieceColor> checkMateColor = Arrays.stream(PieceColor.values()).filter(this::isCheckMate).findFirst();
    if (checkMateColor.isPresent()) {
      return Optional.of(new TerminalGameStateAndColor(TerminalGameState.CHECKMATE, checkMateColor));
    }
    Optional<PieceColor> staleMateColor = Arrays.stream(PieceColor.values()).filter(this::isStaleMate).findFirst();
    if (staleMateColor.isPresent()) {
      return Optional.of(new TerminalGameStateAndColor(TerminalGameState.DRAW_STALEMATE, staleMateColor));
    }
    if (isInsufficientMaterial()) {
      return Optional.of(new TerminalGameStateAndColor(TerminalGameState.DRAW_INSUFFICIENT_MATERIAL, Optional.empty()));
    }
    if (isThreefoldRepetition()) {
      return Optional.of(new TerminalGameStateAndColor(TerminalGameState.DRAW_THREEFOLD, Optional.empty()));
    }
    return Optional.empty();
  }

  private boolean isTimeOver(PieceColor color) {
    return game.playerWithColor(color.opposite()).clock().filter(clock -> clock.currentTimeRemaining() <= 0).isPresent();
  }

  private boolean isCheckMate(PieceColor color) {
    return game.position().isCheckMate(game.position(), color);
  }

  private boolean isStaleMate(PieceColor color) {
    return game.position().isStaleMate(game.position(), color);
  }

  private boolean isInsufficientMaterial() {
    // Insufficient material if there is no piece except both kings
    return Arrays.stream(PieceColor.values())
      .map(game.position()::piecesWithColor)
      .noneMatch(squares -> squares.stream()
        .map(game.position()::pieceAt)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .anyMatch(piece -> piece.fenName() != 'K'));
  }

  private boolean isThreefoldRepetition() {
    // TODO
    return false;
  }

  public static TerminalGameStateComponent of(Game game) {
    Preconditions.checkNotNull(game);
    return new TerminalGameStateComponent(game);
  }
}
