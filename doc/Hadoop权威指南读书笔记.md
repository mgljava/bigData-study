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
2. 在Pig内部，将这些操作和变换转换为一系列的MapReduce作业
3. grunt: grunt是与pig进行交互的外壳程序

##### 执行类型
1. 本地模式：pig -x local
2. MapReduce模式：pig 直接执行，默认为MapReduce模式

##### 运行Pig程序
1. 脚本: 直接运行pig代码，如 pig script.pig
2. Grunt: 如果没有指明pig要运行的文件，或者没有使用-e选项，pig就会启动grunt，在grunt中，可以通过run和exec命令来运行pig脚本
3. 嵌入式方法：可以通过PigServer类来运行Pig程序。或者通过PigRunner以编程的方式来使用grunt

##### Pig Latin程序设计
1. 结构：一条语句理解为一个操作
2. 语句：Pig Latin程序运行时，每个命令按次序进行解析，如果遇到错误，解释器中止运行。解释器会给每个关系操作建立“逻辑计划”，并不会真正的执行，只有遇到STORE命令或者DUM命令才会编译为物理执行
3. 表达式
  - 常数: 1.0 'a'
  - 字段$0: 第0个位置的元素
  - 投影：c.f records.year
  - Map查找: m#k records.year#'2012'
  - 算术: +-*/% 
  - 条件：x ? y : z
  - 比较：> < = != (x is null)(x is not null)
  - 布尔：(x or y) (x and y) (not x)
  - 函数：isGood(quality)
  - 平面化：FLATTEN(f),从包和元组中去除嵌套, FLATTEN(group)
4. 基本类型
  - int
  - long
  - float
  - double
  - bytearray,对应Java的byte数组
  - chararray,对应Java的String类型
5. 复杂类型
  - tuple：任何类型的字段序列
  - bag：元组的无序的多重集合
  - map：键值对的集合