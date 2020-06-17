package com.github.mgljava.zookeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

public class ConnectionWatcher implements Watcher {

  private static final int SESSION_TIMEOUT = 5000;
  protected ZooKeeper zk;
  private final CountDownLatch countDownLatch = new CountDownLatch(1);

  // 链接ZK，该方法立即返回，不管成功与否，所以加入了CountDownLatch来等待process方法执行的结果
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

  // 关闭 ZK
  public void close() throws InterruptedException {
    zk.close();
  }
}
