package com.github.mgljava.hdfs;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;

public class HdfsTest {

  public static void main(String[] args) throws Exception {
    // fileTest();
    deleteFile(new Path("/data/ncdc/output"));
  }

  private static void fileTest() throws IOException {
    FileSystem fileSystem = FileSystem.get(new Configuration());
    FSDataInputStream dataInputStream = fileSystem.open(new Path("/aa/kms.sh"));
    System.out.println((char) dataInputStream.readByte());
    System.out.println((char) dataInputStream.readByte());
    System.out.println((char) dataInputStream.readByte());
    System.out.println((char) dataInputStream.readByte());
    System.out.println((char) dataInputStream.readByte());

    long defaultBlockSize = fileSystem.getDefaultBlockSize(new Path("/aa/kms.sh"));
    System.out.println(defaultBlockSize);
    System.out.println("---------------");
    RemoteIterator<LocatedFileStatus> locatedFileStatusRemoteIterator = fileSystem.listFiles(new Path("/software"), true);
    while (locatedFileStatusRemoteIterator.hasNext()) {
      LocatedFileStatus next = locatedFileStatusRemoteIterator.next();
      BlockLocation[] blockLocations = next.getBlockLocations();
      for (BlockLocation b : blockLocations) {
        System.out.println(b);
      }
    }
  }

  public static void deleteFile(Path path) throws Exception {
    FileSystem fileSystem = FileSystem.get(new Configuration(true));

    // 第二个参数为true代表递归删除, 如果第一个参数为文件或空目录,那么第二个参数将被忽略
    boolean delete = fileSystem.delete(path, true);

    System.out.println("delete result: " + delete);
  }
}
