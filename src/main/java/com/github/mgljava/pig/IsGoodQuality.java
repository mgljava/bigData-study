package com.github.mgljava.pig;

import java.io.IOException;
import org.apache.pig.FilterFunc;
import org.apache.pig.data.Tuple;

/**
 * 自定义过滤函数，用于过滤到不满足质量的气温数据
 */
public class IsGoodQuality extends FilterFunc {

  @Override
  public Boolean exec(Tuple tuple) throws IOException {
    if (null == tuple || tuple.size() == 0) {
      return false;
    }
    try {
      Object object = tuple.get(0);
      if (null == object) {
        return false;
      }
      int i = (Integer) object;
      return i == 0 || i == 1 || i == 4 || i == 5 || i == 9;
    } catch (Exception e) {
      throw new IOException(e);
    }
  }
}
