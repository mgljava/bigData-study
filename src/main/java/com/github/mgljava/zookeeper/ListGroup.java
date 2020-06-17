package com.github.mgljava.zookeeper;

import java.util.List;
import org.apache.zookeeper.AsyncCallback.StatCallback;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;

public class ListGroup extends ConnectionWatcher {

  public static void main(String[] args) throws Exception {
    ListGroup listGroup = new ListGroup();
    listGroup.connect("192.168.56.10");
    // 列出zookeeper路径下的文件
    listGroup.listGroup("zookeeper");

    listGroup.exists("zookeeper");

    System.out.println("--------------------");
    listGroup.exists("zookeeper", (rc, path, ctx, stat) -> {
      System.out.println("rc: " + rc);
      System.out.println("path: " + path);
    });
    Thread.currentThread().join();
  }

  // exists
  public void exists(String groupName) throws KeeperException, InterruptedException {
    Stat stat = zk.exists("/" + groupName, false);
    System.out.println(stat.getVersion());
  }

  public void exists(String groupName, StatCallback callback) {
    zk.exists("/" + groupName, false, callback, null);
  }

  public void listGroup(String groupName) throws KeeperException, InterruptedException {
    String path = "/" + groupName;
    List<String> children = zk.getChildren(path, false);
    children.forEach(System.out::println);
  }
}
