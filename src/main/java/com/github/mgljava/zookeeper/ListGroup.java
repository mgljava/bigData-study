package com.github.mgljava.zookeeper;

import java.util.List;
import org.apache.zookeeper.KeeperException;

public class ListGroup extends ConnectionWatcher {

  public static void main(String[] args) throws Exception {
    ListGroup listGroup = new ListGroup();
    listGroup.connect("192.168.56.10");
    // 列出zookeeper路径下的文件
    listGroup.listGroup("zookeeper");
  }

  public void listGroup(String groupName) throws KeeperException, InterruptedException {
    String path = "/" + groupName;
    List<String> children = zk.getChildren(path, false);
    children.forEach(System.out::println);
  }
}
