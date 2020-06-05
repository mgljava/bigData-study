## 第10章 管理Hadoop
#### HDFS
1. namenode和datanode数据存储目录
2. 安全模式

#### 工具使用
1. dfsadmin: 在HDFS上执行管理操作
2. fsck: 检查HDFS中文件地健康状况
3. datanode 块扫描器
4. 均衡器： start-balancer.sh 启动均衡器

### 监控
##### 日志
##### 度量
1. JVM度量
2. DFS
3. RPC
4. mapred
##### JMX

### 维护
1. 元数据备份（namenode）
2. 数据备份（datanode）
3. 文件系统检查
4. 文件系统均衡器

### 添加和删除节点
1. 添加节点
  - 配置datanode，并更新到namenode，执行命令：hadoop dfsadmin -refreshNodes
  - 配置nodemanager，并更新到RM中，执行命令：hadoop mradmin -refreshNodes
  - 配置slaves文件，以便后续管理
  - 启动datanode和nodemanager
  - 执行均衡器

2. 删除节点，需要配置include和exclude文件
  - 将需要被删除地节点地地址添加到exclude文件中，不更新include文件
  - 更新namenode：hadoop dfsadmin -refreshNodes
  - 更新RM：hadoop mradmin -refreshNodes
  - 此时namenode会把节点的信息复制到其他datanode节点
  - 在include文件中移除这些节点，并执行hadoop dfsadmin -refreshNodes和hadoop mradmin -refreshNodes命令
  - 从slaves文件中移除节点
 
### 升级
1. 升级前执行fsck
2. 升级过程
  - 关闭MapReduce，中止NodeManager
  - 关闭HDFS，备份namenode的目录
  - 安装新版的HDFS和MapReduce
  - 使用-upgrade选项启动HDFS
  - 等待升级完成
  - 检查HDFS是否运行正常
  - 启动MapReduce
  - 回滚或升级成功
3. 回滚：$HADOOP_HOME/sbin/start-dfs.sh -rollback
4. 定妥升级：$HADOOP_HOME/bin/hadoop dfsadmin -finalizeUpgrade

## 第11章 Pig
### Pig概念
1. 用于描述数据流的语言，称为Pig Latin
2. 用于运行Pig Latin程序的执行环境，当前有两个环境，单JVM的本地环境和Hadoop集群上的分布式环境

##### Pig Latin
1. Pig Latin由一系列的“操作”和“变换”组成
2. 