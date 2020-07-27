package com.github.mgljava.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptorBuilder;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.TableDescriptor;
import org.apache.hadoop.hbase.client.TableDescriptorBuilder;

public class HBaseExample {

  private static final String TABLE_NAME = "test";
  private static final String CF_DEFAULT = "cf";

  public static void createOrOverwrite(Admin admin, TableDescriptor tableDescriptor) throws Exception {
    if (admin.tableExists(tableDescriptor.getTableName())) {
      admin.disableTable(tableDescriptor.getTableName());
      admin.deleteTable(tableDescriptor.getTableName());
    }
    admin.createTable(tableDescriptor);
  }

  public static void main(String[] args) throws Exception {
    Configuration configuration = HBaseConfiguration.create();
    configuration.addResource(new Path("hbase-site.xml"));
    configuration.addResource(new Path("core-site.xml"));
    Connection connection = ConnectionFactory.createConnection(configuration);
    Admin admin = connection.getAdmin();
    TableDescriptor table = TableDescriptorBuilder.newBuilder(TableName.valueOf(TABLE_NAME))
        .setColumnFamily(ColumnFamilyDescriptorBuilder.newBuilder(CF_DEFAULT.getBytes()).build()).build();
    createOrOverwrite(admin, table);
    connection.close();
  }
}
