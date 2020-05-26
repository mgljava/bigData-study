package com.github.mgljava.hdfs;

import org.apache.hadoop.io.Text;

/**
 * Text类型可以简单的理解为String类型
 */
public class TextTest {

  public static void main(String[] args) {
    Text text = new Text("hello");
    int i = text.charAt(2);
    System.out.println((char)i);

    System.out.println(text.find("p"));
    System.out.println(text.find("l"));
  }
}
