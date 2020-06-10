package com.github.mgljava.mr.temperature;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

// 排序比较器
public class TemperatureComparator extends WritableComparator {

  public TemperatureComparator() {
    super(TemperatureModel.class, true);
  }

  // 比较日期和温度
  @Override
  public int compare(WritableComparable a, WritableComparable b) {
    TemperatureModel t1 = (TemperatureModel) a;
    TemperatureModel t2 = (TemperatureModel) b;

    int c1 = Integer.compare(t1.getYear(), t2.getYear());
    if (0 == c1) {
      int c2 = Integer.compare(t1.getMonth(), t2.getMonth());
      if (0 == c2) {
        return -Integer.compare(t1.getTemperature(), t2.getTemperature());
      }
      return c2;
    }
    return c1;
  }
}
