package com.github.mgljava.mr.mysql;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapred.lib.db.DBWritable;

/**
 * 记录读取器
 */
public class StudentInfoRecord implements Writable, DBWritable {

  Integer id;
  String name;

  public StudentInfoRecord() {
  }

  @Override
  public void write(DataOutput out) throws IOException {
    out.writeInt(this.id);
    Text.writeString(out, this.name);
  }

  @Override
  public void readFields(DataInput in) throws IOException {
    this.id = in.readInt();
    this.name = Text.readString(in);
  }

  @Override
  public void write(PreparedStatement statement) throws SQLException {
    statement.setInt(1, this.id);
    statement.setString(2, this.name);
  }

  @Override
  public void readFields(ResultSet result) throws SQLException {
    this.id = result.getInt(1);
    this.name = result.getString(2);
  }

  @Override
  public String toString() {
    return "StudentInfoRecord{" +
        "id=" + id +
        ", name='" + name + '\'' +
        '}';
  }
}
