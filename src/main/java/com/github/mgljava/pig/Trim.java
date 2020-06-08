package com.github.mgljava.pig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.pig.EvalFunc;
import org.apache.pig.FuncSpec;
import org.apache.pig.data.DataType;
import org.apache.pig.data.Tuple;
import org.apache.pig.impl.logicalLayer.FrontendException;
import org.apache.pig.impl.logicalLayer.schema.Schema;
import org.apache.pig.impl.logicalLayer.schema.Schema.FieldSchema;

/**
 * 自定义计算函数，从chararray值中去掉开头和结尾的空白符
 */
public class Trim extends EvalFunc<String> {

  @Override
  public String exec(Tuple input) throws IOException {
    if (null == input || input.size() == 0) {
      return null;
    }
    try {
      Object object = input.get(0);
      if (null == object) {
        return null;
      }
      return ((String) object).trim();
    } catch (Exception e) {
      throw new IOException(e);
    }
  }

  @Override
  public List<FuncSpec> getArgToFuncMapping() throws FrontendException {
    List<FuncSpec> funcSpecs = new ArrayList<>();
    funcSpecs.add(new FuncSpec(this.getClass().getName(), new Schema(new FieldSchema(null, DataType.CHARARRAY))));
    return funcSpecs;
  }
}
