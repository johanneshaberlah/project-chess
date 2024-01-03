package org.iu.chess.common;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class NumericDocumentFilter extends DocumentFilter {
  @Override
  public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr) throws BadLocationException {
    if (text.matches("\\d*")) {
      super.insertString(fb, offset, text, attr);
    }
  }

  @Override
  public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
    if (text.matches("\\d*")) {
      super.replace(fb, offset, length, text, attrs);
    }
  }
}
