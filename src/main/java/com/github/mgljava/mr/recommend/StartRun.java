package com.github.mgljava.mr.recommend;

import java.util.HashMap;
import java.util.Map;
import org.apache.hadoop.conf.Configuration;

public class StartRun {

  public static void main(String[] args) {
    Configuration conf = new Configuration(true);

    conf.set("mapreduce.app-submission.corss-paltform", "true");
    conf.set("mapreduce.framework.name", "yarn");

    //所有mr的输入和输出目录定义在map集合中
    Map<String, String> paths = new HashMap<>();
    paths.put("Step1Input", "/data/mr/itemcf/input/");
    paths.put("Step1Output", "/data/mr/itemcf/output/step1");
    paths.put("Step2Input", paths.get("Step1Output"));
    paths.put("Step2Output", "/data/mr/itemcf/output/step2");
    paths.put("Step3Input", paths.get("Step2Output"));
    paths.put("Step3Output", "/data/mr/itemcf/output/step3");
    paths.put("Step4Input1", paths.get("Step2Output"));
    paths.put("Step4Input2", paths.get("Step3Output"));
    paths.put("Step4Output", "/data/mr/itemcf/output/step4");
    paths.put("Step5Input", paths.get("Step4Output"));
    paths.put("Step5Output", "/data/mr/itemcf/output/step5");
    paths.put("Step6Input", paths.get("Step5Output"));
    paths.put("Step6Output", "/data/mr/itemcf/output/step6");

    Step1.run(conf, paths);
    Step2.run(conf, paths);
    Step3.run(conf, paths);
    Step4.run(conf, paths);
    Step5.run(conf, paths);
    Step6.run(conf, paths);
  }

  public static Map<String, Integer> R = new HashMap<>();

  static {
    R.put("click", 1);
    R.put("collect", 2);
    R.put("cart", 3);
    R.put("alipay", 4);
  }
}
