package com.github.mgljava.zookeeper.example;

import com.github.mgljava.zookeeper.ConnectionWatcher;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

/**
 * 在线配置服务
 * 允许分布式服务更新和检索配置文件
 */
public class ActiveKeyValueStore extends ConnectionWatcher {

  // 最大重试次数
  private static final int MAX_RETRIES = 10;

  // 重试时间间隔
  private static final long RETRY_PERIOD_SECONDS = 3;

  // 将value的值写到zookeeper的path上，该方法是一个幂等操作
  public void write(String path, String value) throws KeeperException, InterruptedException {
    Stat stat = zk.exists(path, false);
    if (null == stat) {
      zk.create(path, value.getBytes(Charset.defaultCharset()), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    } else {
      zk.setData(path, value.getBytes(Charset.defaultCharset()), stat.getVersion());
    }
  }

  // 重载的write方法，能够执行重试
  public void write(String path, String value, int time) throws InterruptedException, KeeperException {
    int retries = 0;
    while (true) {
      try {
        Stat stat = zk.exists(path, false);
        if (null == stat) {
          zk.create(path, value.getBytes(Charset.defaultCharset()), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        } else {
          zk.setData(path, value.getBytes(Charset.defaultCharset()), stat.getVersion());
        }
        return;
      } catch (KeeperException.SessionExpiredException e) {
        throw e;
      } catch (KeeperException e) {
        if (retries++ == MAX_RETRIES) {
          throw e;
        }
        TimeUnit.SECONDS.sleep(RETRY_PERIOD_SECONDS);
      }
    }
  }

  // 从Path路径下读取数据
  public String read(String path, Watcher watcher) throws KeeperException, InterruptedException {
    byte[] data = zk.getData(path, watcher, null);
    return new String(data, Charset.defaultCharset());
  }
}
