package org.iu.chess;

public record Square(int file, int rank) {
  public Square addDiagonally(int offset) {
    System.out.println("Relevant diagonal square: " + withRank(rank + offset).withFile(file + offset));
    return withRank(rank + offset).withFile(file + offset);
  }

  public String algebraicNotation() {
    char fileCharacter = (char) ('a' + file - 1);
    return String.format("%c%d", fileCharacter, rank);
  }

  public Square withRank(int customRank) {
    return new Square(file, customRank);
  }

  public Square withFile(int customFile) {
    return new Square(customFile, rank);
  }
}
