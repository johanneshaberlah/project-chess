package org.iu.chess;

import com.google.inject.AbstractModule;

public class ChessModule extends AbstractModule {
  private ChessModule() {
  }

  @Override
  protected void configure() {
    super.configure();
  }

  public static ChessModule create() {
    return new ChessModule();
  }
}
