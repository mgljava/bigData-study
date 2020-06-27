package com.github.mgljava.mr.pagerank;

import com.github.mgljava.mr.pagerank.PageRank.MyCounter;
import java.io.IOException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class PageRankReduce extends Reducer<Text, Text, Text, Text> {

  @Override
  protected void reduce(Text key, Iterable<Text> iterable, Context context)
      throws IOException, InterruptedException {

    //相同的key为一组
    //key：页面名称比如B
    //包含两类数据
    //B:1.0 C  //页面对应关系及老的pr值

    //B:0.5		//投票值
    //B:0.5

    double sum = 0.0;

    Node sourceNode = null;
    for (Text i : iterable) {
      Node node = Node.fromMR(i.toString());
      if (node.containsAdjacentNodes()) {
        sourceNode = node;
      } else {
        sum = sum + node.getPageRank();
      }
    }

    // 4为页面总数
    double newPR = (0.15 / 4.0) + (0.85 * sum);
    System.out.println("*********** new pageRank value is " + newPR);

    // 把新的pr值和计算之前的pr比较
    double d = newPR - sourceNode.getPageRank();

    int j = (int) (d * 1000.0);
    j = Math.abs(j);
    System.out.println(j + "___________");
    context.getCounter(MyCounter.my).increment(j);

    sourceNode.setPageRank(newPR);
    context.write(key, new Text(sourceNode.toString()));
  }
}
