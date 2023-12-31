package org.iu.chess.game.artificial.minimax;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

public class MinimaxCalculation {
  private MoveTree tree;

  private MinimaxCalculation(MoveTree tree) {
    this.tree = tree;
  }

  public MoveNode evaluateBestNode(boolean isMaxPlayer, List<MoveNode> children) {
    Comparator<MoveNode> byScoreComparator = Comparator.comparing(MoveNode::score);
    return children.stream()
      .max(isMaxPlayer ? byScoreComparator : byScoreComparator.reversed())
      .orElseThrow(NoSuchElementException::new);
  }
}
