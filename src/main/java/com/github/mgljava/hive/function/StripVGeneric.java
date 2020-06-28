package com.github.mgljava.hive.function;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;

/**
 * Hive自定义函数，用于剪除字符串尾字符
 */
public class StripVGeneric extends GenericUDF {

  @Override
  public ObjectInspector initialize(ObjectInspector[] arguments) throws UDFArgumentException {
    return null;
  }

  @Override
  public Object evaluate(DeferredObject[] arguments) throws HiveException {
    return null;
  }

  @Override
  public String getDisplayString(String[] children) {
    return null;
  }
}
