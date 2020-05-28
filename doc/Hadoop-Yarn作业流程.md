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
