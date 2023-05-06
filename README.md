### 基础工具包
commons是基础工具包

### 当前版本
0.0.1

### 主要功能
- commons-core：封装API数据格式、常用工具类
- commons-exception-advice：全局异常处理
- commons-snowflake：雪花主键生成工具类
- commons-swagger：集成swagger2
- commons-samples: 简单案例

### 如何使用

#### commons-core
 直接引入jar即可

```xml
    <dependencies>
        <dependency>
           <groupId>com.cutefool.commons</groupId>
           <artifactId>commons-core</artifactId>
           <version>0.0.1</version>
        </dependency>
    </dependencies>

```

#### commons-exception-advice
 直接引入jar即可
 ```xml
   <dependencies>
         <dependency>
            <groupId>com.cutefool.commons</groupId>
            <artifactId>commons-exception-advice</artifactId>
             <version>0.0.1</version>
         </dependency>
     </dependencies>

```

#### commons-snowflake
- 引入jar包
 ```xml
   <dependencies>
         <dependency>
            <groupId>com.cutefool.commons</groupId>
            <artifactId>commons-snowflake</artifactId>
             <version>0.0.1</version>
         </dependency>
     </dependencies>

```
- 配置
```yaml
    commons: 
      snowflake:
        workerId: 1   # 0-31
        dataCenterId: 2  # 0-31
       
```
 #### commons-swagger
 - 引入jar包
  ```xml
     <dependencies>
        <dependency>
           <groupId>com.cutefool.commons</groupId>
           <artifactId>commons-swagger</artifactId>
            <version>0.0.1</version>
        </dependency>
    </dependencies>
```

### 版本管理规范
项目版本采用x.x.x形式

 




