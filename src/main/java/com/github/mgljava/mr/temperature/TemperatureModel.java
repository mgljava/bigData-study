package com.github.mgljava.mr.temperature;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import lombok.Data;
import org.apache.hadoop.io.WritableComparable;

// key
@Data
public class TemperatureModel implements WritableComparable<TemperatureModel> {

  private int year;
  private int month;
  private int day;
  private int temperature;

  // 比较两条数据,按照日期正序
  @Override
  public int compareTo(TemperatureModel model) {

    // 比较年份
    int c1 = Integer.compare(this.year, model.getYear());
    if (c1 == 0) {
      // 比较月份
      int c2 = Integer.compare(this.month, model.getMonth());
      if (c2 == 0) {
        // 比较天
        return Integer.compare(this.day, model.getDay());
      }
      return c2;
    }
    return c1;
  }

  // 序列化
  @Override
  public void write(DataOutput out) throws IOException {
    out.writeInt(year);
    out.writeInt(month);
    out.writeInt(day);
    out.writeInt(temperature);
  }

  // 反序列化
  @Override
  public void readFields(DataInput in) throws IOException {
    this.year = in.readInt();
    this.month = in.readInt();
    this.day = in.readInt();
    this.temperature = in.readInt();
  }
}
