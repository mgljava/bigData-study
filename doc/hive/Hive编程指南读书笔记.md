# Hive编程指南
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

#### Hive的服务
1. cli：命令行
2. hiveserver：HiveServer用来监听来自于其他进程的Thrift连接的一个守护进程
3. hwi：Hive WebUI界面 2.0 被废弃
4. jar
5. metastore
6. rcfilecat