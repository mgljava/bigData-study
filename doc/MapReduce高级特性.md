# MapReduce高级特性
### 计数器
计数器是收集作业统计信息的有效手段

#### 内置计数器
1. 任务计数器
2. 文件系统计数器
3. FileInputFormat计数器：BYTES_READ(读的字节数)
4. FileOutputFormat计数器： BYTES_WRITETEN(写的字节数)
5. 作业计数器: 由jobtracker或YARN的宿主维护，无需再网络之间传输数据

### 排序
1. 部分排序：（输出文件中有序，多个文件中无序）基于分区的MapFile查找技术
2. 全排序：最简单的生成一个全排序的文件的方式就是使用一个分区. 可以采用TotalOrderPartitioner
3. 采样器：Sampler，输入采样器：InputSampler, RandomSampler
4. 辅助排序：MapReduce框架再记录到达reducer之前按键对记录进行排序
5. 对记录按值进行排序的总结
  - 定义包括自然键和自然值得组合键
  - 根据组合键对记录进行排序
  - 针对组合键进行分区和分组时均只考虑自然键

### 连接
1. Map端连接: CompositeInputFormat
2. Reduce端连接
  - 多输入
  - 辅助排序

### MapReduce库类
1. ChainMapper、ChainReducer


