### Hadoop基准测评程序

##### TestDFSIO
1. 用来测试HDFS的I/O性能，他用一个MapReduce作业并行的读或写作业
2. 命令： hadoop jar hadoop-*-test.jar TestDFSIO -write -nrFiles 10 -fileSize 1000: 写10个文件，各个文件的大小为1000MB

##### Sort程序测试排序性能
1. 命令：hadoop jar hadoop-*-test.jar randomwriter random-data 输出数据供sort使用
2. sort命令：hadoop jar hadoop-*-test.jar sort random-data sorted-data

##### MRBench
会多次运行一个小型作业。

##### NNBench
测试NameNode硬件的加载过程。

##### Gridmix
通过模拟一些真实常见的数据访问模式，Gridmix能逼真的为一个集群的负载建模。