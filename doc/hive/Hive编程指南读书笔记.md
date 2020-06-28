# Hive编程指南
### Hive架构图
![Hive架构图](https://github.com/mgljava/bigData-study/blob/master/doc/images/Hive架构.png)
### metastore 元数据
元数据一般采用内建数据库 derby数据库存储，不过在生产环境中一般采用MySQL存储，MySQL配置如下
```xml
<!-- hive-sit.xml --> 
<configuration>
  <property>
    <name>javax.jdo.option.ConnectionURL</name>
    <value>jdbc:mysql://localhost:3306/hive_db?createDatabaseIfNotExists=true</value>
<description>数据库连接地址</description>
  </property>
<property>
    <name>javax.jdo.option.ConnectionDriverName</name>
    <value>com.mysql.jdbc.Driver</value>
    <description>Driver Class</description>
  </property>
  <property>
    <name>javax.jdo.option.ConnectionUserName</name>
    <value>root</value>
    <description>MySQL用户名</description>
  </property>
  <property>
    <name>javax.jdo.option.ConnectionPassword</name>
    <value>123456</value>
<description>密码</description>
  </property>
</configuration>
```
然后将MySQL的JDBC驱动放到类路径下，放到Hive的lib目录下.
### 初始化元数据
schematool -dbType mysql -initSchema

### Hive的服务
1. cli：命令行
2. hiveserver：HiveServer用来监听来自于其他进程的Thrift连接的一个守护进程
3. hwi：Hive WebUI界面 2.0 被废弃
4. jar
5. metastore
6. rcfilecat

### 通过 beeline链接hiveserver2
1. 在hive启动hiveserver2服务, 需要查看启动日志
2. 在其他节点通过beeline去连接，bin/beeline -u jdbc:hive2://vm01:10000 -n root
3. 或者：
  ```shell script
    bin/beeline
    ! connect jdbc:hive2://vm01:10000
  ```

### .hiverc 文件 
当cli启动时，先执行 .hiverc 文件，会自动执行该文件中的命令，如下：  
`set hive.cli.print.current.db=true;`

### 在Hive中执行命令
1. Shell命令：加 ! 和； 结尾，如： !ls /root;
2. 执行Hdfs的命令：dfs ls /;

## 第三章 数据类型和文件格式
### 基本数据类型
1. TINYINT：1 字节有符号整数
2. SMALLINT：2 字节有符号整数
3. INT：4 字节有符号整数
4. BIGINT：8 字节有符号整数
5. BOOLEAN：布尔类型
6. FLOAT：单精度
7. DOUBLE：双精度
8. STRING：字符序列
9. TIMESTAMP：整数
10. BINARY：字节数组

### 集合数据类型
1. STRUCT：struct('john', 'joe')
2. MAP：map('key1','value1','key2','value2')
3. ARRAY：array('test','test2')

### 文本文件数据编码
##### 分隔符
1. \n：换行分隔符
2. ^A：用来分隔字段，八进制 \001表示
3. ^B：用来分隔ARRAY或者STRUCT的字段，八进制 \002表示
4. ^C：用来分隔MAP中的键值，八进制 \003表示
5. 如果需要手动指定分隔符，那么需要采用 ROW FORMAT DELIMITED

### 读时模式
1. 传统数据库采用的是写时模式，就是在写入数据的时候就对结构和模式进行检查
2. 而Hive采用读时模式，就是在查询数据室进行验证
3. Hive会在读取是处理错误，如果不能处理，则将字段的值置为null

## 第四章 HiveQL：数据定义
### 数据库
1. 创建数据库：CREATE DATABASE hive_study;
2. 查看数据库：describe DATABASE hive_study;
3. 删除数据库：DROP DATABASE hive_study; 如果数据库中存在表，那么需要先删除表，也可以在删除的时候加上关键字 CASCADE;
4. 修改数据库：ALTER DATABASE hive_study;

### 数据表
1. 创建表：CREATE TABLE
2. 复制表：create table hive_study.emp2 like hive_study.employees;

### 外部表
1. 采用 EXTERNAL 关键字
2. create external table if not exists stocks();

### 内部表和外部表的区别
1. 外部表创建时需要指定数据存储的目录，而内部表的数据会存储到 warehouse目录下
2. 删除时内部表会将数据和元数据一起删除，而外部表只会删除元数据，不会删除数据

### 分区表
1. 采用Partition By 定义
2. 以目录来区分分区字段的信息
3. 外部分区表