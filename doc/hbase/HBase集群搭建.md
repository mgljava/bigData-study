# HBase 集群搭建
## Standalone模式
1. 配置好JDK
2. 解压hbase安装包 `tar xzvf hbase-2.2.3-bin.tar.gz`
3. 在 conf/hbase-env.sh 下配置 JAVA_HOME
4. 配置 conf/hbase-sit.xml
```xml
<configuration>
  <property>
    <name>hbase.rootdir</name>
    <value>file:///home/testuser/hbase</value>
  </property>
  <property>
    <name>hbase.zookeeper.property.dataDir</name>
    <value>/home/testuser/zookeeper</value>
  </property>
  <property>
    <name>hbase.unsafe.stream.capability.enforce</name>
    <value>false</value>
    <description>
      Controls whether HBase will check for stream capabilities (hflush/hsync).

      Disable this if you intend to run on LocalFileSystem, denoted by a rootdir
      with the 'file://' scheme, but be mindful of the NOTE below.

      WARNING: Setting this to false blinds you to potential data loss and
      inconsistent system state in the event of process and/or node failures. If
      HBase is complaining of an inability to use hsync or hflush it's most
      likely not a false positive.
    </description>
  </property>
</configuration>
```
5. 启动hbase, bin/start-hbase.sh
6. 访问  http://${HOST}:16010 可以看到HBase的WebUI界面
7. 停止集群：stop-hbase.sh
 
## 伪分布式集群
概念：HBase仍然运行在单个节点，但是HBase的后台进程（HMaster、HRegionServer、Zookeeper）运行在单独的JVM中
1. 伪分布式集群通过HDFS来存储HBase的数据
2. 配置 conf/hbase.sit
```xml
<!--  -->
<configuration>
    <!-- 设为true，代表分布式安装 -->
    <property>
        <name>hbase.cluster.distributed</name>
        <value>true</value>
    </property>

    <!-- HBase存储目录，需要启动Hadoop的HDFS -->
    <property>
        <name>hbase.rootdir</name>
        <value>hdfs://localhost:8020/hbase</value>
    </property>
</configuration>
```
3. 启动集群，可以jps命令看下Java进程是否成功启动

## 全分布式集群
#### 节点分配
|Node Name|Master|Zookeeper|RegionServer|
|---- | ---- | ---- | ---- |
|node1| yes  |  yes |  no  |
|node2|  no  |  yes |  yes |
|node3|  no  |  yes |  yes |
|node4|backup|  no  |  yes |

#### 准备工作
1. 网络
2. hosts
3. ssh
  - ssh-keygen
  - ssh-copy-id -i .ssh/id_rsa.pub node1 拷贝公钥到node1节点的knownhosts中
4. 时间：各个节点的时间需要保持一致，通过时间服务器来同步时间
  - date -s '2018-12-24 16:23:11' 手动设置时间（不推荐，会存在误差）
  - `yum install ntpdate`， `ntpdate ntp1.aliyun.com` 通过ntpdate工具来设置时间
5. JDK

#### 解压安装包进行配置
1. hbase-env.sh
  - JAVA_HOME
  - HBASE_MANAGES_ZK=false 告诉HBase不采用默认的ZK，使用外部的ZK来管理
2. hbase-site.xml
```xml
<configuration>
    
        <!-- HBase 存储路径 -->
        <property>
  		  <name>hbase.rootdir</name>
  		  <value>hdfs://localhost:8020/hbase</value>
  		</property>
        
        <!-- 是否分布式，需要配置为true -->
  		<property>
  		  <name>hbase.cluster.distributed</name>
  		  <value>true</value>
  		</property>
        
        <!-- ZK 节点 -->
  		<property>
  		  <name>hbase.zookeeper.quorum</name>
  		  <value>node1,node2,node3</value>
  		</property>
</configuration>
```

3. regionservers 文件，如果没有需要新建，代表regionservice的节点
```shell script
node2
node3
node4
```

4. backup-masters 文件，配置主备的HMaster节点
```shell script
node4
```

5. 因为需要HDFS，所以需要拷贝 hdfs-site.xml 到conf目录
6. 启动集群 `start-hbase.sh`