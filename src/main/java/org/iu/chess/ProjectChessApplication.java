package org.iu.chess;

import java.io.File;

public class ProjectChessApplication {

  public static void main(String[] args) {
    System.out.println(new File("src/main/resources").listFiles().length);
  }
}
