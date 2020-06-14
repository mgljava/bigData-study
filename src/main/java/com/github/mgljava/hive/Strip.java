package com.github.mgljava.hive;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

/**
 * Hive自定义函数，用于剪除字符串尾字符
 */
public class Strip extends UDF {

  private final Text result = new Text();

  public Text evaluate(Text str) {
    if (null == str) {
      return null;
    }
    result.set(StringUtils.strip(str.toString()));
    return result;
  }

  public Text evaluate(Text str, String stripChars) {
    if (null == str) {
      return null;
    }
    result.set(StringUtils.strip(str.toString(), stripChars));
    return result;
  }
}
