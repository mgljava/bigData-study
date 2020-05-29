### YARN(Yet Another Resource Negotiator)
##### 角色划分
1. 管理集群上资源使用的资源管理器(Resource Manager)
2. 管理集群上运行任务生命周期的应用管理器(Application Manager)
3. 容器(Container): 为资源隔离提出的框架,每个任务对应一个Containers,且只能在Container中运行
4. 节点管理器(Node Manager): 管理每个节点上的资源和任务

##### Yarn作业运行过程
1. 作业提交: 从RM中获取作业ID,计算输入分片, 将作业资源(作业的JAR,配置和分片信息)提交到HDFS,通过调用RM的submitApplication()方法提交作业
2. 作业初始化: RM收到submitApplication方法的请求后,在节点管理器的管理下在容器中启动应用程序的master(MRAppMaster),还要判断任务是否可以以uber方式运行
3. 任务分配: 将任务首先分配到本地化的节点,如果不能分配,那么优先使用机架本地化分配.默认情况下,map和reduce任务都分配到1024M的内存
4. 任务执行: 一旦资源管理器的调度器为任务分配了容器,AM就通过与节点管理器通信来启动容器.该任务由主类为YarnChild的Java程序执行
5. 进度和状态更新: 在YARN运行时,任务每3秒钟通过umbilical接口向AM汇报进度和状态(包含计数器)
6. 作业完成: 除了向AM查询进度外,客户端每5秒钟通过Job的waitForCompletion来检查作业是否完成
7. 作业清理: 通过OutputCommitter来清理

##### YARN失败
1. 任务运行失败: 如程序错误,或者JVM异常退出造成的失败,会反馈给AM,任务被标记为失败,4次尝试后任务标记失败
2. AM失败: AM向资源管理器发送周期性的心跳,当AM发生故障时,资源管理器检测到故障并在一个新的容器中开始一个新的AM实例
3. 节点管理器失败: 如果节点管理器失败,那么将会被移除可用节点资源管理池
4. 资源管理器失败: 资源管理器失败,作业和任务容器都将无法启动

##### 作业调度器
1. 设置作业调度优先级
2. 公平调度器
3. 容量调度器

##### shuffle和排序
1. shuffle: 系统执行排序的过程(将map的输出作为输入传递给reduce)称为shuffle
2. 每个map任务都有一个环形的内存缓冲区用于存储任务的输出,默认情况下缓冲区的大小为100MB.缓冲内容阈值为80%.即当缓冲区内容达到80%时,后台线程将内容溢出(spill)到磁盘
3. 通过调整map和reduce的参数来获取最佳性能

##### 任务执行
1. 任务推测执行: 如果一个任务执行时间比预期慢,那么Hadoop会启动另一个相同的任务作为备份,这就是任务的推测执行
2. OutputCommitter: 用来确保作业和任务都完全成功或失败,默认为FileOutputCommitter. 包含执行成功的_SUCCESS 文件和临时文件的创建
3. JVM重用

### MapReduce的类型与格式
##### Map和Reduce的输入和输出格式
1. map:(K1,V1) -> list(K2,V2)
2. reduce:(K2, list(V2)) -> list(K3,V3)

##### 输入格式
1. FileInputFormat: 使用文件作为数据源,如HDFS. 还将处理输入的分片
2. CombineFileInputFormat: 将多个小文件打包成一个大文件提供给map作为输入
3. SequenceFile: 将大量小文件合并为一个或多个大文件,减少namenode内存的存储空间
4. 不切分文件的分片:可以设置分片大小为文件的大小或者重写FileInputFormat类的isSplitable()方法返回false即可
5. WholeFileInputFormat: 将整个文件的内容作为一行来处理

#### Hadoop Stream
1. Hadoop Stream是Hadoop的一个工具
2. 用法: `hadoop jar $HADOOP_HOME/share/hadoop/tools/lib/hadoop-streaming-2.10.0.jar -input /test/input/test.txt -output /test/output2 -mapper /bin/cat -reducer /usr/bin/wc`