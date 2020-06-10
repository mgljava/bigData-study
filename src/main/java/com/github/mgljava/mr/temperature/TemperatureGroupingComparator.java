package com.github.mgljava.mr.temperature;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

// 分组比较器
public class TemperatureGroupingComparator extends WritableComparator {

  public TemperatureGroupingComparator() {
    super(TemperatureModel.class, true);
  }

  // 比较日期和温度
  @Override
  public int compare(WritableComparable a, WritableComparable b) {
    TemperatureModel t1 = (TemperatureModel) a;
    TemperatureModel t2 = (TemperatureModel) b;

    int c1 = Integer.compare(t1.getYear(), t2.getYear());
    if (0 == c1) {
      return Integer.compare(t1.getMonth(), t2.getMonth());
    }
    return c1;
  }
}
