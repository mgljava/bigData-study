package com.github.mgljava.zookeeper.example;

import java.io.IOException;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;

public class ConfigWatcher implements Watcher {

  private ActiveKeyValueStore store;

  public ConfigWatcher(String hosts) throws IOException, InterruptedException {
    store = new ActiveKeyValueStore();
    store.connect(hosts);
  }

  public void displayConfig() throws KeeperException, InterruptedException {
    // 将新的Watcher对象告知ZK，即当前的this参数对象
    String value = store.read(ConfigUpdater.PATH, this);
    System.out.printf("Read %s as %s\n", ConfigUpdater.PATH, value);
  }

  @Override
  public void process(WatchedEvent event) {
    if (event.getType() == EventType.NodeDataChanged) {
      try {
        displayConfig();
      } catch (InterruptedException e) {
        System.err.println("InterruptedException");
        Thread.currentThread().interrupt();
      } catch (KeeperException e) {
        System.err.printf("KeeperException: %s, Exists.%n", e);
      }
    }
  }

  public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
    ConfigWatcher configWatcher = new ConfigWatcher("192.168.56.10");
    configWatcher.displayConfig();

    Thread.sleep(Long.MAX_VALUE);
  }
}
