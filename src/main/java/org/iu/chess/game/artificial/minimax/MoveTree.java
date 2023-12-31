package org.iu.chess.game.artificial.minimax;

public record MoveTree(MoveNode root) {

  public void printMoveTree() {
    printMoveNode(root, 0);
  }

  private void printMoveNode(MoveNode node, int level) {
    // Print the current node with indentation based on its level in the tree
    printIndentation(level);
    System.out.println("Move: " + node.move() + ", Score: " + node.score() + ", IsMaximizer: " + node.isMaximizer());

    // Recursively print each child
    for (MoveNode child : node.children()) {
      printMoveNode(child, level + 1);
    }
  }

  private void printIndentation(int level) {
    for (int i = 0; i < level; i++) {
      System.out.print("    "); // 4 spaces for each level of indentation
    }
  }

}
