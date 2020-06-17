package com.github.mgljava.zookeeper;

import java.nio.charset.Charset;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;

public class JoinGroup extends ConnectionWatcher {

  public void join(String groupName, String memberName) throws Exception {
    // 构造需要创建的路径，groupName需要提前存在
    String path = "/" + groupName + "/" + memberName;
    // 调用ZK的API创建路径，访问模式为UNSAFE，创建模式为临时的ZNode
    String createPath = zk.create(path, memberName.getBytes(Charset.defaultCharset()),
        Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
    System.out.println("createPath: " + createPath);
  }

  public static void main(String[] args) throws Exception {
    JoinGroup joinGroup = new JoinGroup();
    joinGroup.connect("192.168.56.10");
    joinGroup.join("jonGroupTest", "test1");
    joinGroup.join("jonGroupTest", "test2");
    Thread.sleep(Long.MAX_VALUE);
    joinGroup.close();
  }
}
