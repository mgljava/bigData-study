package com.github.mgljava.hive.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class HiveJdbcTest {

  private static final String DRIVER_NAME = "org.apache.hive.jdbc.HiveDriver";

  public static void main(String[] args) throws Exception {
    try {
      Class.forName(DRIVER_NAME);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      System.exit(1);
    }

    Connection connection = DriverManager.getConnection("jdbc:hive2://192.168.56.10:10000/hive_study", "root", "123456");
    Statement statement = connection.createStatement();
    String tableName = "userinfo";
    String sql = "desc " + tableName;
    System.out.println("run sql : " + sql);
    ResultSet resultSet = statement.executeQuery(sql);
    while (resultSet.next()) {
      System.out.println(resultSet.getString(1) + "\t" + resultSet.getString(2));
    }

    sql = "select * from " + tableName + " limit 10";
    resultSet = statement.executeQuery(sql);
    while (resultSet.next()) {
      System.out.println(
          resultSet.getString(1) + "\t" + resultSet.getString(2) + "\t" + resultSet.getString(3) + "\t" + resultSet.getString(4) + "\t" + resultSet
              .getString(5) + "\t" + resultSet.getString(6));
    }
    statement.close();
    connection.close();
  }
}
