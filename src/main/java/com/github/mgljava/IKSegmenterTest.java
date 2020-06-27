package com.github.mgljava;

import java.io.StringReader;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

public class IKSegmenterTest {

  public static void main(String[] args) throws Exception {
    StringReader stringReader = new StringReader("今天我和一班好朋友约好了一起吃大餐");
    IKSegmenter ikSegmenter = new IKSegmenter(stringReader, false);
    Lexeme word;
    while ((word = ikSegmenter.next()) != null) {
      String s = word.getLexemeText();
      System.out.println(s);
    }
  }
}
