package org.iu.chess.game;

import org.iu.chess.move.Move;

public class InvalidGameActionException extends Exception {
  private Move move;

  private InvalidGameActionException(Move move, String message) {
    super("The move %s was invalid because %s".formatted(move, message));
  }

  public static InvalidGameActionException create(Move move, String message) {
    return new InvalidGameActionException(move, message);
  }
}
