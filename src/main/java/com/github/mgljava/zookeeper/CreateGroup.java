package com.github.mgljava.zookeeper;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.CountDownLatch;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

// 创建组
public class CreateGroup implements Watcher {

  private static final int SESSION_TIMEOUT = 5000;
  private ZooKeeper zk;
  private CountDownLatch countDownLatch = new CountDownLatch(1);

  public void connect(String hosts) throws IOException, InterruptedException {
    zk = new ZooKeeper(hosts, SESSION_TIMEOUT, this);
    countDownLatch.await();
  }

  @Override
  public void process(WatchedEvent event) {
    if (event.getState() == KeeperState.SyncConnected) {
      countDownLatch.countDown();
    }
  }

  public void create(String groupName) throws KeeperException, InterruptedException {
    String path = "/" + groupName;
    String createPath = zk.create(path, "hello".getBytes(Charset.defaultCharset()), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    System.out.println("Created: " + createPath);
  }

  public void close() throws InterruptedException {
    zk.close();
  }

  public static void main(String[] args) throws Exception {
    CreateGroup createGroup = new CreateGroup();
    createGroup.connect("192.168.56.10");
    createGroup.create("testGroup2");
    createGroup.close();
  }
}















