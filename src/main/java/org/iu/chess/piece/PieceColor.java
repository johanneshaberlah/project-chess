package org.iu.chess.piece;

public enum PieceColor {
  BLACK("Schwarz"),
  WHITE("Weiß");

  private final String displayName;

  PieceColor(String displayName) {
    this.displayName = displayName;
  }

  public String displayName() {
    return displayName;
  }

  public PieceColor opposite() {
    return switch (this) {
      case BLACK -> WHITE;
      case WHITE -> BLACK;
    };
  }
}
