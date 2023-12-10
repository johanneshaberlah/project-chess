package org.iu.chess;

import com.google.inject.AbstractModule;

public class ChessConfiguration extends AbstractModule {
  private ChessConfiguration() {
  }

  @Override
  protected void configure() {
    super.configure();
  }

  public static ChessConfiguration create() {
    return new ChessConfiguration();
  }
}
