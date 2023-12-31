package org.iu.chess.piece;

public enum PieceColor {
  BLACK("Schwarz", false),
  WHITE("Weiß", true);

  private final String displayName;
  private final boolean isMaximizer;

  PieceColor(String displayName, boolean isMaximizer) {
    this.displayName = displayName;
    this.isMaximizer = isMaximizer;
  }

  public String displayName() {
    return displayName;
  }

  public boolean isMaximizer() {
    return isMaximizer;
  }

  public PieceColor opposite() {
    return switch (this) {
      case BLACK -> WHITE;
      case WHITE -> BLACK;
    };
  }
}
