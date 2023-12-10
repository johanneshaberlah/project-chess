package org.iu.chess;

import java.io.File;
import java.util.Collection;

public abstract class Piece {
  private final String name;
  private final String fenName;
  private final File image;

  public Piece(String name, String fenName, File image) {
    this.name = name;
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
  public abstract boolean isLegalMove(Board board, Move move);

  /**
   * Returns all the reacheable moves from the given square without taking other pieces into account.
   *
   * @param square
   * @return a collection of reacheable moves
   */
  public abstract Collection<Move> reachableMoves(Piece piece);

  public String name() {
    return name;
  }

  public String fenName() {
    return fenName;
  }

  public File image() {
    return image;
  }
}
