### YARN(Yet Another Resource Negotiator)
##### 角色划分
1. 管理集群上资源使用的资源管理器(Resource Manager)
2. 管理集群上运行任务生命周期的应用管理器(Application Manager)
3. 容器(Container): 为资源隔离提出的框架,每个任务对应一个Containers,且只能在Container中运行
4. 节点管理器(Node Manager): 管理每个节点上的资源和任务

##### Yarn作业运行过程
1. 作业提交: 作业提交到HDFS,从RM中获取作业ID
2. 作业初始化:
3. 任务分配
