# HBase
#### HBase概念
分布式、高可用、高性能、面向列、底层采用HDFS存储数据

#### HBase一些术语
1. 列族：一组列的集合
2. 逻辑表
3. 时间戳
4. RowKey
  - 决定一行数据
  - 按照字典顺序排序
  - Row key只能存储64k的字节数据
5. Cell 单元格，具有版本
6. HLog(WAL log: write ahead log)
7. 命名空间

#### HBase 数据模型
1. 列族：代表列的集合
2. 逻辑表

#### HBase角色
1. Client
2. Zookeeper：分布式协作服务,存储元数据
3. HMaster（主从架构）
4. HRegionServer：承担CRUD操作
  - HRegion：等同于表，包含两部分，1.HLog 2.Store
  - Store又包含 MemStore和StoreFile
  - StoreFile等于HFile，StoreFile是HFile的封装，StoreFile是HBase层面，HFile是Hadoop层面

#### HBase架构设计

--- 知识点
1. 搭建HBase集群