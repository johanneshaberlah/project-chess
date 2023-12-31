package org.iu.chess.game.artificial.minimax;

import org.iu.chess.move.Move;

import java.util.List;

public class MoveNode {
  private final Move move;
  private int score;
  private final boolean isMaximizer;
  private final List<MoveNode> children;

  public MoveNode(Move move, int score, boolean isMaximizer, List<MoveNode> children) {
    this.move = move;
    this.score = score;
    this.isMaximizer = isMaximizer;
    this.children = children;
  }

  public Move move() {
    return move;
  }

  public int score() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }

  public boolean isMaximizer() {
    return isMaximizer;
  }

  public List<MoveNode> children() {
    return children;
  }
}
