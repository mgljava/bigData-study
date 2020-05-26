package com.github.mgljava.conf;

import org.apache.hadoop.conf.Configuration;

public class ConfigurationTest {

  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.addResource("Configuration_1.xml");
    System.out.println("color: " + configuration.get("color"));;
    System.out.println("size: " + configuration.get("size"));;
    System.out.println("weight: " + configuration.get("weight"));;
    System.out.println("size-weight: " + configuration.get("size-weight"));
    System.out.println("----------------------------");
    Configuration configuration1 = new Configuration();
    System.out.println(configuration1.get("hadoop.tmp.dir"));
  }
}
