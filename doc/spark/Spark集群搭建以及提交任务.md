### Spark 集群搭建(spark-2.4.6)

#### 集群配置
1. 准备集群的节点，我这里准备了三台节点，192.168.56.101(Master)主机名：node1，192.168.56.102(Worker)主机名：node2，192.168.56.101(Worker)主机名：node3
2. 从官网下载spark二进制安装包并解压到 /usr/local目录：`tar -zxvf spark-2.4.6-bin-hadoop2.7.tgz -C /usr/local`
3. 配置
  - 配置slaves,拷贝slaves.template并从命名为slaves, 加入两行 
  ```shell script
    node1
    node2
  ```

  - 配置spark env，拷贝spark-env.sh.template并重命名为spark-env.sh，加入以下配置
  ```shell script
    export SPARK_MASTER_HOST=node1 # spark master host
    export SPARK_MASTER_PORT=7077 # 端口，默认7077
    export SPARK_WORKER_CORES=4  # 工作节点的 核心数
    export SPARK_WORKER_MEMORY=1g # 工作节点的内存
    export SPARK_MASTER_WEBUI_PORT=8888 # webUi界面的端口，默认是8080
  ```
4. 分发包到各个节点: 将配置好的spark包分发到node2和node3节点上
5. 至此，集群就配置完成了

#### 启动集群
1. 进入到master节点，进入到 $SPARK_HOME/sbin
2. 使用命令 ./start-all.sh 命令启动spark集群
3. 检查进程是否启动：jps命令查看master节点的Master进程和worker节点Worker进程
4. 访问Spark WebUI节点：node1:8888

#### 提交任务（SparkPi为例）
1. 任何节点都可以提交任务 
2. 进入到 $SPARK_HOME/bin/目录
3. 执行 ./spark-submit --master spark://node1:7077 --class org.apache.spark.examples.SparkPi ../examples/jars/spark-examples_2.11-2.4.6.jar 100
  - --master 指定spark master节点和端口，spark://node1:7077代表采用spark的资源调度器
  - --class 指定要运行的作业的主类
  - *.jar 运行的jar包
  - 100 代表SparkPi的参数

#### 搭建Spark提交任务的客户端
1. 找一台新机器
2. 将master节点上的spark包拷贝到新机器上（该机器与spark集群没有关系），可以完全删除先前的配置
3. 进入到 $SPARK_HOME，提交任务同上