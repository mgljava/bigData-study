package com.github.mgljava.zookeeper;

import java.io.IOException;
import org.apache.zookeeper.KeeperException;

public class DeleteGroup extends ConnectionWatcher{

  public void deleteGroup(String groupName) throws KeeperException, InterruptedException {
    zk.delete("/" + groupName, -1);
  }

  public static void main(String[] args) throws KeeperException, InterruptedException, IOException {
    DeleteGroup deleteGroup = new DeleteGroup();
    deleteGroup.connect("192.168.56.10");
    deleteGroup.deleteGroup("testGroup2");
    deleteGroup.close();
  }
}
