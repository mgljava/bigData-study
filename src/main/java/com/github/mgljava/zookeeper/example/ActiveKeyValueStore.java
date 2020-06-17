package com.github.mgljava.zookeeper.example;

import com.github.mgljava.zookeeper.ConnectionWatcher;
import java.nio.charset.Charset;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

/**
 * 在线配置服务 允许分布式服务更新和检索配置文件
 */
public class ActiveKeyValueStore extends ConnectionWatcher {

  // 将value的值写到zookeeper的path上
  public void write(String path, String value) throws KeeperException, InterruptedException {
    Stat stat = zk.exists(path, false);
    if (null == stat) {
      zk.create(path, value.getBytes(Charset.defaultCharset()), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    } else {
      zk.setData(path, value.getBytes(Charset.defaultCharset()), stat.getVersion());
    }
  }

  // 从Path路径下读取数据
  public String read(String path, Watcher watcher) throws KeeperException, InterruptedException {
    byte[] data = zk.getData(path, watcher, null);
    return new String(data, Charset.defaultCharset());
  }
}
