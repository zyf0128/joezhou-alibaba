# SpringCloudAlibaba

**心法：** 微服务架构演变历史：
- 单体应用架构：将全部功能代码都部署在一个MVC项目中：
    - 优点：开发，部署和维护的成本很低，适用于小型项目。
    - 缺点：功能之间耦合度极高，单点容错率低，水平扩展性差。
- 垂直应用架构：将单体应用按功能模块拆成多个互不相干的应用（服务）：
    - 优点：高流量时，仅针对某个热点服务进行集群即可，不用整个项目集群。
    - 优点：一个服务出问题不会影响到其他服务，提高容错率。
    - 缺点：服务之间相互独立，无法相互调用，会产生大量重复代码。
- 分布式架构：将垂直应用的重复代码抽取成一个个的公共服务，想用哪个调哪个：
    - 优点：抽离出的公共服务层可以提高代码重用性。
    - 缺点：服务之间调用关系复杂，耦合度变高，维护困难。
- SOA面向服务架构：引入服务注册中心以治理服务：
    - 优点：解决了服务之间的复杂调用问题。
    - 缺点：服务之间存在依赖性，一旦某个服务出错容易雪崩，运维，测试部署困难。
- 微服务架构：将服务拆分的更彻底（微服务），服务之间采用Restful等协议相互调用：
    - 优点：微服务独立打包，部署和升级，每个微服务任务划分清晰，利于扩展。
    - 缺点：技术成本高，如限流，容错，分布式事务等。

## 开发父项目

**武技：** 开发maven-jar父项目 `joezhou-alibaba`：
- 检查maven配置是否正确。
- 父项目不用于开发，可以直接删除掉src目录。
- 根据 `alibaba.sql` 生成数据库实例，用户，表和测试数据。
- 在POM中使用 `<parent>` 配置父依赖：
    - `org.springframework.boot.spring-boot-starter-parent(2.1.3.RELEASE)`
- 在POM中使用 `<properties>` 配置jdk和编码：
    - `<java.version>1.8</java.version>`
    - `<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>`
    - `<project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>`
- 在POM中使用 `<dependencyManagement>` 配置除 `<parent>` 之外的父依赖，解决maven的单继承问题： 
    - `org.springframework.cloud.spring-cloud-dependencies(Greenwich.RELEASE)`
    - `com.alibaba.cloud.spring-cloud-alibaba-dependencies(2.1.0.RELEASE)`
    - `<type>pom</type><scope>import</scope>`：每个额外父依赖都必须配置此二项。

## 开发通用微服务

**武技：** 开发maven-jar通用服务 `al-common`：
- 查看父项目中的 `<modules>` 中是否添加了 `al-common` 子模块。
- 添加依赖：
    - `org.projectlombok.lombok`
    - `org.springframework.boot.spring-boot-starter-test`
    - `com.baomidou.mybatis-plus-boot-starter(3.4.1)`
    - `com.baomidou.mybatis-plus-generator(3.4.1)`
    - `org.apache.velocity.velocity-engine-core(2.3)`
    - `mysql.mysql-connector-java(runtime)`
    - `com.fasterxml.jackson.core.jackson-core(2.9.7)`
    - `com.fasterxml.jackson.core.ackson-annotations(2.9.7)`
    - `com.fasterxml.jackson.core.jackson-databind(2.9.7)`
- 开发工具类 `util.JacksonUtil` 用于创建JSON格式的字符串数据。
- 开发工具类 `util.MyBatisPlusUtil`：用于生成MBP代码：
    - 根据数据库表生成三张表的entity层/Mapper层/Service层/Controller层代码和xml配置文件。
    - 表 `order` 踩了SQL关键字，手动在Order实体类上添加 @TableName("`order`") 设置别名。

## 开发用户微服务

**武技：** 开发maven-jar用户服务 `al-user`：
- 查看父项目中的 `<modules>` 中是否添加了 `al-user` 子模块。
- 添加依赖：
    - `com.joezhou.al-common(1.0-SNAPSHOT)`
    - `org.springframework.boot.spring-boot-starter-web`
- 将MBP生成的和用户相关的三层代码拷贝到src中，xml拷贝到classpath:mapper目录下。
- 开发主配 `application.yml`：
    - `server.port=8010`：端口。
    - `spring.application.name=al-user`：微服务名。
    - `spring.datasource.driver-class-name/url/username/password`：数据源。
    - `mybatis-plus.mapper-locations/type-aliases-package/configuration.log-impl`：控制台打印SQL。
- 开发启动类 `com.joezhou.app.UserApp`：标记 `@SpringBootApplication`：
    - `SpringApplication.run(UserApp.class, args)`
- 启动类使用 `@MapperScan()` 扫描Mapper接口所在的包。
- 控制器中开发控制方法：
    - 使用 `@Autowired` 注入业务层接口。
    - 开发按主键查询一条数据的控制方法，响应JSON字符串。
- cli: `localhost:8010/api/user/select-by-id?id=1`

## 开发商品微服务

**武技：** 开发maven-jar商品服务 `al-product`：
- 查看父项目中的 `<modules>` 中是否添加了 `al-product` 子模块。
- 添加依赖：
    - `com.joezhou.al-common(1.0-SNAPSHOT)`
    - `org.springframework.boot.spring-boot-starter-web`
- 将MBP生成的和商品相关的三层代码拷贝到src中，xml拷贝到classpath:mapper目录下。
- 开发主配 `application.yml`：
    - `server.port=8020`：端口。
    - `spring.application.name=al-user`：微服务名。
    - `spring.datasource.driver-class-name/url/username/password`：数据源。
    - `mybatis-plus.mapper-locations/type-aliases-package/configuration.log-impl`：控制台打印SQL。
- 开发启动类：`com.joezhou.app.ProductApp` 并标记 `@SpringBootApplication`：
    - `SpringApplication.run(ProductApp.class, args)`
- 启动类使用 `@MapperScan()` 扫描Mapper接口。
- 控制器中开发控制方法：
    - 使用 `@Autowired` 注入Service层接口。
    - 开发按主键查询一条数据的控制方法，响应JSON字符串。
- cli: `localhost:8020/api/product/select-by-id?id=1`

## 开发订单微服务

**武技：** 开发maven-jar订单服务 `al-order`：
- 查看父项目中的 `<modules>` 中是否添加了 `al-order` 子模块。
- 添加依赖：
    - `com.joezhou.al-common(1.0-SNAPSHOT)`
    - `org.springframework.boot.spring-boot-starter-web`
- 将MBP生成的和订单相关的三层代码拷贝到src中，xml拷贝到classpath:mapper目录下。
- 开发主配 `application.yml`：
    - `server.port=8030`：端口。
    - `spring.application.name=al-user`：微服务名。
    - `spring.datasource.driver-class-name/url/username/password`：数据源。
    - `mybatis-plus.mapper-locations/type-aliases-package/configuration.log-impl`：控制台打印SQL。
- 开发启动类：`com.joezhou.app.OrderApp` 并标记 `@SpringBootApplication`：
    - `SpringApplication.run(OrderApp.class, args)`
- 启动类使用 `@MapperScan()` 扫描Mapper接口。
- 控制器中开发控制方法：
    - 使用 `@Autowired` 注入Service层接口。
    - 开发按主键查询一条数据的控制方法，响应JSON字符串。
- cli: `localhost:8030/api/order/select-by-id?id=1`

# 注册中心Nacos

**心法：** 注册中心用于实现服务治理，即实现各个微服务的自动化注册与发现：
- 服务注册：每个服务都在注册中心中进行登记，最终形成一张服务列表，注册中心负责心跳监测，不可用的服务会被踢出列表。
- 服务发现：服务调用方去注册中心中查看服务列表，以访问某服务。
- 常见的注册中心：
    - Zookeeper：分布式服务框架，来自Apache，主要用于分布式架构的应用。  
    - Eureka：来自于SpringCloud-Netflix，主要作用就是做服务注册和发现，目前已经闭源。
    - Consul：基于GO语言开发的开源工具，主要面向分布式架构的应用。
    - Nacos：来自于SpringCloud-Alibaba，更易于构建云原生应用的动态服务发现，配置管理和服务管理平台。
    
**武技：** 将用户/商品/订单微服务都注册到Nacos服务端：
- 安装[Nacos](https://github.com/alibaba/nacos/releases)：
    - `资源/nacos-server-1.1.4.zip`：解压缩即可。
    - `%NACOS_HOME%\bin\startup.cmd`：建议使用cmd启动Nacos，观察地址和端口。
    - `localhost:8848/nacos`：访问Nacos管控台，账密都是nacos。
- 分别在三个微服务中添加Nacos依赖：
    - `com.alibaba.cloud.spring-cloud-starter-alibaba-nacos-discovery`
- 分别在三个微服务的启动类上标记 `@EnableDiscoveryClient` 以开启Nacos功能。
- 分别在三个微服务的主配中添加Nacos服务端地址：
    - `spring.cloud.nacos.discovery.server-addr=localhost:8848`
- 依次启动三个微服务，在Nacos管控台的服务列表中观察三个服务是否注册到Nacos服务端。

# 远程调用OpenFeign

**心法：** OpenFeign是SpringCloud提供的一个声明式的伪Http客户端，用于远程调用，自带负载均衡效果，即可以将请求按一定规则分摊到多个微服务上：
- 客户端负载均衡：发送请求之前已经决定调用哪个服务节点，推荐使用。
- 服务端负载均衡：在服务端决定使用哪个服务，如Nginx技术。

**武技：** 在订单微服务中使用OpenFeign远程调用商品微服务进行按主键查询商品信息的业务：
- 在订单微服务中添加依赖：
    - `org.springframework.cloud.spring-cloud-starter-openfeign`
- 在启动类上使用 `@EnableFeignClients` 开启OpenFeign功能。
- 开发商品远程接口 `feign.ProductFeign`：
    - 标记 `@FeignClient("al-product")` 以声明为远程接口，并使用value值指定远程服务名。
- 开发远程接口方法 `String selectById(@RequestParam("id") Integer id)`：
    - 返回值/方法名/形参都必须和远程服务对应的控制方法保持一致，且形参必须标记 `@RequestParam()`。
    - 方法上标记 `@RequestMapping("/api..")` 并指定远程服务对应的控制方法的完整URL。
- 开发订单业务接口 `service.OrderService`：开发下单方法：
    - `int insert(Order order)`：成功返回1，失败返回0。
- 开发订单业务接口实现类 `service.impl.OrderServiceImpl`：
    - 使用 `@Autowired` 注入 `OrderMapper` 接口以调用其添加方法。
    - 使用 `@Autowired` 注入 `ProductFeign` 商品远程接口。
    - 业务方法中调用商品远程接口的查询方法以获取该商品的全部信息，并解析出商品名。
    - 业务方法中设置订单数据，其中商品名从远程商品数据中获取，购买人姓名可以暂时写死。
    - 业务方法中调用订单服务的本地添加方法完成下单操作。
- 开发订单控制器 `controller.OrderController`：
    - 使用 `@Autowired` 注入 `OrderService` 业务接口。
    - 开发下单的控制方法，响应JSON字符串。
- cli: `localhost:8030/api/order/insert?product-id=1`

**武技：** 添加订单微服务下单后，在商品微服务中执行扣减库存业务：
- 在商品微服务中开发业务接口 `service.ProductService`：
    - `int reduceInventory(Integer productId, Integer number)`
- 在商品微服务中开发业务实现类 `service.impl.ProductServiceImpl`：
    - 使用 `@Autowried` 注入 `ProductMapper` 接口。
- 在商品微服务中开发业务方法 `reduceInventory()`：
    - 标记 `@Transactional(rollbackFor = Exception.class)` 以添加本地事务。
    - 根据商品主键查询商品信息。
    - 在内存中扣减商品库存，需要判断当前商品的剩余库存是否充足。
    - 根据商品主键修改商品信息，主要是修改库存。
- 在商品微服务中开发控制方法 `reduceInventory()`：调用对应业务方法即可：
    - cli: `localhost:8020/api/product/reduce-inventory?product-id=1&number=1`
- 在订单微服务中开发远程接口方法：`feign.ProductFeign.reduceInventory()`：
    - 返回值/方法名/形参都必须和远程服务对应的控制方法保持一致，且形参必须标记 `@RequestParam()`。
    - 方法上标记 `@RequestMapping("/api/product/reduce-inventory")` 指定远程服务对应的控制方法的URL。
- 修改订单微服务的下单业务方法 `insert()`：
    - 在订单添加完毕之后，通过Feign调用商品扣减库存的方法，注意不要添加本地事务注解，因为包含远程调用。
- 启动nacos服务端，商品微服务和订单微服务：
- cli: `localhost:8030/api/order/insert?product-id=1`：查看订单表和商品表的库存：
    - 存在事务问题：库存为0时下单，仍会继续添加订单表数据，等后面分布式事务再解决。

# 服务容错Sentinel

**心法：** 由于网络或自身等原因，服务无法保证100%可用，当某个服务挂掉时会上游任务堆积，多条线程阻塞，最终瘫痪整个服务，这就是雪崩效应，而服务容错技术的目的，就是保证服务雪落而不雪崩，常见容错手段如下：
- 隔离：将系统按照一定原则划分成多个互不干扰的服务模块，当故障发生时，能将故障隔离在某模块内部而不扩散：
    - 线程池隔离：为每个服务单独建立一个线程池，超出线程池限制直接fallback返回，优点是可以异步调用下游服务，缺点是线程频繁切换消耗CPU资源，适用于高并发且请求处理耗时较长的情况。
    - 信号量隔离：设定一个信号量，超出信号量限制直接fallback返回，优点是一个线程执行到底，无线程切换开销，缺点是只能同步调用下游服务，当调用链比较长的情况下比较耗时，适用于高并发且请求处理耗时较短的情况。
- 超时：在上游服务调用下游服务的时候，设置一个最大响应时间，如果超过这个时间，下游未作出反应，就断开请求，释放掉线程。
- 限流：限制系统的输入/输出流量以保证系统的稳定运行，一旦达到某设定的阈值，就要开启限流。
- 熔断：当下游服务出现故障时，上游服务可以暂时熔断对下游服务的调用转而调用fallback方法以保全大局，每隔一段时间重新尝试调用。
- 降级：特殊情况下，可以对某些不重要的服务主动降级，以暂时让出资源给其他重要的服务，降级的方法暂时使用fallback提供的兜底数据。
- sentinel：分布式系统的流量防卫兵sentinel是阿里巴巴开源的一款用于处理服务容错的工具，包括流量控制，熔断降级，系统负载等功能，承接了阿里巴巴近10年的双十一大促流量的核心场景。

**武技：** 在用户微服务中整合sentinel：
- 安装sentinel管控台，本质就是一个springboot应用：
    - `资源/sentinel-dashboard-1.7.0.jar`
- 启动sentinel管控台：
    - cmd: `java -Dserver.port=8088 -jar sentinel-dashboard-1.7.0.jar`：默认端口8080。
- 访问sentinel管控台，账密都是sentinel：
    - cli: `localhost:8088`
- 在用户微服务中添加依赖：
    - `com.alibaba.cloud.spring-cloud-starter-alibaba-sentinel`
- 在用户微服务的主配添加：
    - `spring.cloud.sentinel.transport.port=8888`：跟控制台交流的内部端口号，随意指定一个未使用的端口即可。
    - `spring.cloud.sentinel.transport.dashboard=localhost:8080`：配置sentinel管控台。
- 开发一个通用的无参控制方法对应的降级方法 `fallback.SentinelFallback.commNoArgFallBack()`：
    - 形参必须和对应的控制方法一致，可额外使用 `Throwable` 参数接收异常信息。
    - 方法必须被public和static修饰，返回值必须和对应的控制方法保持一致。
    - 使用 `e instanceof FlowException` 判断是否是因为限流而触发的异常。
    - 使用 `e instanceof DegradeException`：判断是否是因为熔断而触发的异常。
    - 使用 `e instanceof AuthorityException`：判断是否是因为黑白名单限制而触发的异常。
    - 使用 `e instanceof SystemBlockException`：判断是否是因为系统限制而触发的异常。
    - 使用 `e instanceof ParamFlowException`：判断是否是因为参数限制而触发的异常。
- 开发控制方法 `controller.SentinelController.execute()`：
    - 标记 `@SentinelResource()` 以声明该方法是一个sentinel资源：
    - 注解中使用 `value` 设置资源名。
    - 注解中使用 `fallbackClass` 设置降级类。
    - 注解中使用 `fallback` 设置降级处理类中的降级处理方法名。
- 依次启动nacos后台，sentinel后台，用户服务。
- cli: `localhost:8010/api/sentinel/execute`：
    - 查看sentinel管控台，点击簇点链路，找到刚才发送的请求。
    - 点击添加流控规则，QPS阈值设置为3。
    - 再次快速发送请求，发现请求的资源（控制方法）被熔断降级。

## 流控降级规则

**心法：** 流控降级是因为资源外部原因而触发的，比如限制请求数量，请求线程数量等：
- 流控类型：sentinel流控类型分QPS和线程数两种：
    - QPS：每秒请求率超过此值时触发限流，比较常用。
    - 线程数：并发线程数超过此值时触发限流。
- 流控模式：sentinel流控模式分直接，关联和链路三种：
    - 直接：对自己设置流控规则，触发规则则自己被限流。
    - 关联：对自己关联的资源设置流控规则，关联的资源触发规则，则自己被限流，即自己优先级比关联资源高。
    - 链路：从调用来源设置流控规则，触发规则则自己被限流，当前sentinel版本不支持。
- 流控效果：
    - 快速失败：直接失败，抛出异常，简单粗暴，默认值。
    - Warm Up：先从阈值的1/3开始缓慢增长到阈值，以缓冲突然而来的大流量。
    - 排队等待：让请求匀速通过，超出阈值的请求排队等待直到超时。

**武技：** 测试QPS类型 + 直接模式 + 快速失败效果的流控规则：
- 在sentinel管控台新增流控规则：
    - 填写资源名，选择QPS，阈值填写3，点击新增：每秒请求量超过3时流控。
    - 针对来源默认为default，表示针对所有来源（服务）进行限制。
- cli: `localhost:8010/api/sentinel/execute`：
    - 浏览器快速刷新，发现当QPS超过3时，该资源被限制访问。

**武技：** 测试线程数类型 + 直接模式 + 快速失败效果的流控规则：
- 在sentinel管控台新增流控规则：
    - 填写资源名，选择线程，阈值填写2，点击新增：当访问该资源的线程并发数超过2时流控。
    - 针对来源默认为default，表示针对所有来源（服务）进行限制。
- 安装Jmeter压测工具：
    - `资源/apache-jmeter-5.2.1.zip`
- 启动Jmeter压测工具：
    - `%JMETER_HOME%\bin\jmeter.bat`：双击启动。
- 调整Jmeter语言环境：
    - `Options -> Choose Language -> Chinese(Simplified)`
- 添加线程组：用于设置线程信息：
    - 右键 `Test Plan`：`添加 -> 线程(用户) -> 线程组`。
    - 配置3个线程永久无限发送请求，3个线程要在0.5s内全部启动。
- 添加HTTP请求：用于设置请求路径：
    - 右键 `线程组`：`添加 -> 取样器 -> HTTP请求`。
    - 填写协议，服务器IP，端口和URL接口。
- 添加结果树：用户观察请求结果：
    - 右键 `线程组`：`添加 -> 监听器 -> 观察结果树`。
- 点击工具条中的启动按钮，开启Jmeter压测，观察IDEA控制台，发现该资源因线程数过多而被限流。

**武技：** 测试QPS类型 + 关联模式 + 快速失败效果的流控规则：
- 开发控制方法 `controller.SentinelController.execute2()`：
    - 使用 `@RequestMapping` 指定接口URL。
    - 使用 `@SentinelResource` 指定资源名，降级类和降级方法。
- 在sentinel管控台对资源 `execute` 新增流控规则：
    - 填写资源名，选择QPS，阈值填写1。
    - 点击高级选项，填写关联资源为 `execute2` 并点击新增。
    - 使用Jmeter配置3个线程永久无限向 `execute2` 发送请求，3个线程要在0.5s内全部启动。
- cli: `localhost:8010/api/sentinel/execute`：资源被限流，成功被其关联的 `execute2` 资源连累。 

## 熔断降级规则

**心法：** 熔断降级是因为资源内部原因而触发的，比如资源运行太耗时，运行有异常等：
- RT：当资源的平均响应时间超过阈值（单位ms）就准备熔断，若接下来1s内持续进入的5个请求的RT均超过阈值，则进行熔断：
    - 时间窗口用于设置熔断多久，单位为秒，熔断时内任何访问该资源的请求都只能得到fallback数据，超时后方可尝试恢复访问。
    - sentinel默认RT上限为4900ms，超出都视为4900ms，可在启动时通过 `-Dcsp.sentinel.statistic.max.rt=` 进行配置。
- 异常比例：当资源的每秒异常数/每秒请求数量的比值超过阈值时，进行熔断降级：
    - 时间窗口用于设置熔断多久，单位为秒，熔断时内任何访问该资源的请求都只能得到fallback数据，超时后方可尝试恢复访问。
    - 异常比率的阈值范围是0到1之间的浮点数，包括两端值。
- 异常数：当资源1分钟内的异常数超过阈值时，进行熔断降级：
    - 该项时间窗口是分钟级的，时间窗口设置建议大于60s，否则容易资源结束熔断降级状态后再次进入熔断降级状态。

**武技：** 测试RT熔断降级规则：
- 开发控制方法 `controller.SentinelController.rt()`：
    - 使用 `@RequestMapping` 指定接口URL。
    - 使用 `@SentinelResource` 指定资源名，降级类和降级方法。
    - 方法内使用 `TimeUnit` 睡眠1s以模拟业务耗时。
- 在sentinel管控台新增降级规则：
    - 填写资源名，降级策略选择RT。
    - RT窗口填写500，表示平均响应时间超过500ms时准备熔断。
    - 时间窗口填写5，表示熔断时间为5s，5s内均返回降级方法提供的数据，5s后尝试恢复访问。
- cli: `localhost:8010/api/sentinel/rt`：发现因平均响应时间太慢而被熔断降级5s。 

**武技：** 测试异常比例熔断降级规则：
- 开发控制方法 `controller.SentinelController.ex()`：
    - 使用 `@RequestMapping` 指定接口URL。
    - 使用 `@SentinelResource` 指定资源名，降级类和降级方法。
    - 开发一个计数器，起始值为0。
    - 在方法内部配合计数器，每访问到第3次就抛出一个异常，模拟0.33的异常率。
- 在sentinel管控台新增降级规则：
    - 填写资源名，降级策略选择异常比例。
    - 异常比例窗口填写0.25，表示每秒异常率超过25%时等待熔断。
    - 时间窗口填写5，表示熔断时间为5s，5s内均返回降级方法提供的数据，5s后尝试恢复访问。
- cli: `localhost:8010/api/sentinel/ex`：发现因异常率太高而被熔断降级5s。

## 授权降级规则

**心法：** sentinel的授权规则用于对调用来源设置黑白名单，但需要调用方使用请求参数/请求头的方式传递一个变量作为标识：
- 配置白名单时：只有白名单内的来源可以访问我，其余都不行。
- 配置黑名单时：黑名单内的来源都不可以访问我，其余都可以。

**武技：** 测试授权降级规则：
- 开发控制方法 `controller.SentinelController.auth()`：
    - 使用 `@RequestMapping` 指定接口URL。
    - 使用 `@SentinelResource` 指定资源名，降级类和降级方法。
- 开发配置类 `config.AuthConfig`：
    - 标记 `@Component` 以加入spring容器。
    - 实现 `c.a.c.s.a.s.c.RequestOriginParser` 接口。
    - 重写 `parseOrigin()`，其参数就是HttpServletRequest请求对象。
    - 分别从请求参数和请求头中获取标识，标识名自定义，方法最终返回该标识。
- 在sentinel管控台新增授权规则：
    - 填写资源名，流控应用填 `a,b`，授权类型选择黑名单，表示携带 `标识名=a/b` 的请求不可以访问我。
- cli: 
    - `localhost:8010/api/sentinel/auth`：不降级。
    - `localhost:8010/api/sentinel/auth?app=c`：不降级。
    - `localhost:8010/api/sentinel/auth?app=a`：直接降级。

## 热点降级规则

**心法：** 热点降级规则粒度更细，可以根据方法的形参对方法进行降级规则控制。

**武技：** 测试热点规则：
- 开发控制方法 `controller.SentinelController.param()`：
    - 使用 `@RequestMapping` 指定接口URL。
    - 使用 `@SentinelResource` 指定资源名，降级类和降级方法。
    - 开发两个参数：`(String name, Integer age)`。
- 在sentinel管控台新增热点规则：
    - 填写资源名，限流模式必须选择QPS模式。
    - 参数索引填0，单机阈值填1，表示对资源方法的0号参数进行流控，QPS超过1则直接限流。
    - 统计窗口时长：默认填写1秒，表示以1秒内的统计结果为准。
- cli: 
    - `localhost:8010/api/sentinel/param`：请求中没携带name参数，不降级。
    - `localhost:8010/api/sentinel/param?Age=18`：请求中没携带name参数，不降级。
    - `localhost:8010/api/sentinel/param?name=admin`：请求中携带name参数，QPS超过1就会被降级。

## 规则持久化

**心法：** sentinel管控台中设置的规则默认存放内存，重启服务就会重置，需要将其持久化到硬盘文件：
- sentinel管控台通过API将规则推送至sentinel客户端并更新到内存。
- sentinel客户端将内存中的规则持久化到本地文件。
- sentinel管控台每次启动时候都读取本地文件中的规则并自动配置。

**武技：** 将al-user中的sentinel配置持久化：
- 开发持久化配置类 `config.FilePersistenceConfig`：
    - 仅需要改动持久化文件的存放位置，其余代码拷贝即可。
- 在resources下开发 `META-INF/services` 目录（固定）：
    - 在目录中开发 `com.alibaba.csp.sentinel.init.InitFunc` 文件（固定）。
    - 在文件中拷贝持久化配置类全路径 `com.joezhou.app.config.FilePersistenceConfig`。
- 启动al-user微服务，进入sentinel管控台配置QPS为1的流控降级规则。
- 重启al-user微服务，流控降级规则依然生效：
    - 在 `D:/coder/java/sentinel/al-user-rules` 目录可以查看到持久化的规则文件。

## 整合OpenFeign

**心法：** sentinel默认无法对远程调用的资源方法进行熔断降级处理，需要额外和OpenFeign进行整合才可以。

**武技：** 将用户微服务中的OpenFeign和sentinel整合并测试熔断降级效果：
- 添加依赖：
    - `org.springframework.cloud.spring-cloud-starter-openfeign`
- 在启动类上标记 `@EnableFeignClients` 以开启OpenFeign功能。
- 开发商品远程接口 `feign.ProductFeign` 并模拟一个按主键查询商品的接口方法：
    - 为防止冲突，建议标记 `@Qualifier("productFeign")` 以备精准注入。
- 在主配中添加：
    - `feign.sentinel.enabled=true`：配置sentinel和feign的整合。
- 开发远程接口的降级类 `fallback.ProductFeignFallBack`：
    - 标记 `@Componet` 以被spring管理。
    - 实现 `feign.ProductFeign` 远程接口并在重写方法中返回兜底数据。
- 在远程接口的 `@FeignClient` 中使用 `fallback` 属性指定降级类。
- 开发控制方法 `controller.SentinelController.openFeign()`：
    - 使用 `@Autowried + @Qualifier` 精准注入 `ProductFeign` 接口（因为降级类也是ProductFeign的实现类）。
    - 调用商品远程接口中的接口方法，并将商品数据直接返回到前端。
- 依次启动Nacos，用户微服务和商品微服务。
- cli: `localhost:8010/api/sentinel/open-feign?product-id=1`：调用成功：
    - 关闭商品微服务，再次发送请求，远程调用失败，执行兜底数据，表示sentinel成功对远程调用熔断降级。

# API网关Gateway

**心法：** API网关是所有请求的公共入口，为客户端提供统一服务，可实现一些与业务本身无关的公共逻辑，如认证，鉴权，监控，路由转发等：
- 架构中加入API网关之后：
    - 微服务不再需要管理和记录其他微服务的地址和端口，统一由API网关进行管理和路由转发。
    - 不需要为每个微服务配置认证和鉴权代码，统一由API网关进行配置。
    - 更容易解决跨域请求问题。
- Spring-Cloud-Gateway：Spring公司为了替换Zuul而开发的网关服务，基于Filter链提供了网关基本的功能如安全，监控和限流等：
    - 性能强劲，是第一代网关Zuul的1.6倍。
    - 功能强大，内置了很多实用功能，如路由转发，监控，限流等。
    - 学习成本高，其实现依赖Netty与WebFlux，而非传统的Servlet编程模型。
    - 依赖springboot2.0+版本，且无法将其部署在web容器中运行，只能打成jar包执行。

**武技：** 搭建API网关并使用网关访问三个微服务：
- 开发maven-jar模块 `api-gateway`。
- 添加依赖：
    - `org.projectlombok.lombok`
    - `org.springframework.cloud.spring-cloud-starter-gateway`
    - `com.alibaba.cloud.spring-cloud-starter-alibaba-nacos-discovery`
- 开发启动类 `ApiGatewayApp`：
    - 标记 `@EnableDiscoveryClient` 以将自己注册到nacos中心。
- 主配添加：
    - `server.port=8000`：设置API网关的端口。
    - `spring.application.name=api-gateway`：设置微服务名。
    - `spring.cloud.nacos.discovery.server-addr=localhost:8848`：将自己注册到nacos中心。
    - `spring.cloud.gateway.discovery.locator.enabled=true`：让gateway可以发现nacos中的服务。
- 依次启动nacos，三个微服务和API网关。
- cli: 按照 `API网关地址:API网关端口/微服务名/URL接口` 的格式测试API网关：
    - `localhost:8000/al-product/api/product/select-by-id?id=1`
    - `localhost:8000/al-order/api/order/select-by-id?id=1`
    - `localhost:8000/al-user/api/user/select-by-id?id=1`

## 配置路由

**心法：** 路由route是gateway中最基本的组件之一，用于当请求满足指定条件时将其转发到指定微服务。

**武技：** 对网关服务进行路由详细配置：
- 主配中使用 `spring.cloud.gateway.routes` 对路由进行配置，其值是一个数组：
    - yml数组类型的每个值，都需要在使用 `- ` 作为前缀。
- `id=user-route`：配置路由唯一标识，名称随意，不重复即可。
- `uri=lb://al-user`：从nacos中获取指定微服务的IP和端口，`lb://` 表示负载均衡。
- `order=1`：设置路由优先级，数字越小级别越高。
- `predicates`：设置断言，即转发前的条件判断，其值是一个数组：
    - `Path=/user-service/**`：URL请求满足此格式时，断言配置才能生效。
- `filters`：设置过滤器，即转发前的一些修改，其值是一个数组：
    - `StripPrefix=1`：转发之前去掉端口之后的1层路径，即服务名。
- cli: 按照 `API网关地址:API网关端口/路由配置的Path值/URL接口` 的格式测试API网关：
    - `localhost:8000/user-service/api/user/select-by-id?id=1`
    - `localhost:8000/order-service/api/order/select-by-id?id=1`
    - `localhost:8000/product-service/api/product/select-by-id?id=1`

## 配置断言

**心法：** Predicate断言用于隔离非法请求，只有断言项全部返回真才会放行请求：
- `- Path=/product-service/**`：URL请求满足此格式时，断言配置才能生效。
- `- After=2020-05-05T00:00:00.000+08:00`：请求时间必须在指定的时间之后。
- `- Before=2020-05-05T00:00:00.000+08:00`：请求时间必须在指定的时间之前。
- `- Between=T1,T2`：请求时间必须在两个指定时间之间，T1和T2都是同上的格式。
- `- Cookie=a, \d+`：请求cookie中必须得有a，且值必须满足指定正则表达式。
- `- Header=X-Request-Id, \d+`：请求头中必须得有X-Request-Id，且值必须满足指定正则表达式。
- `- Method=GET`：请求必须是GET类型。
- `- Query=a, \d+`：请求参数必须携带a，且值必须满足指定正则表达式。
    
**武技：** 开发自定义断言，设置请求若携带了age参数，则其值必须在16到60之间：
- 开发自定义断言工厂类 `predicate.AgeRoutePredicateFactory`：
    - 标记 `@Component` 以加入spring容器。
    - 名字必须是 `自定义断言配置的key值 + RoutePredicateFactory` 的格式。
    - 必须继承 `o.s.c.g.h.p.AbstractRoutePredicateFactory<外部类.内部配置类>` 类。
- 在自定义断言类中开发内部配置类，用于接收和存储主配中的Age值：
    - `static class Config{}`：埋minAge和maxAge两个属性并生成setter/getter。
- 重写无参构造并使用 `super(Config.class)` 将内部配置类传递给父类。
- 重写 `shortcutFieldOrder()`：读取主配中Age值并对位赋值给Config配置类的属性：
    - `return Arrays.asList("minAge", "maxAge")`：第一个值赋给minAge，第二个值赋给maxAge。
- 重写 `apply()`：配置断言逻辑，内部直接使用 `return serverWebExchange -> {}` 返回断言结果：
    - `serverWebExchange.getRequest().getQueryParams().getFirst("age")`：获取指定请求参数。
    - `return false/true`: 阻止请求/放行请求。
- 主配添加：
    - `spring.cloud.gateway.routes[0].predicates[0].Age=16,60`
- cli: `localhost:8000/user-service/api/user/select-by-id`：
    - `?id=1`：没有传递age参数，放行。
    - `?id=1&age=999`：age不满足合理范围，阻止。
    - `?id=1&age=50`：age在合理范围内，放行。

## 配置过滤器

**心法：** 当断言全部成功后，会将请求放行到API网关的过滤器链中，在请求被路由前后执行一些业务代码：
- 局部过滤器GatewayFilter：仅作用在某一个或某一组路由上。
- 全局过滤器GlobalFilter：作用所有路由上。
- 内置过滤器配置：
    - `StripPrefix=1`：从端口之后，删除1层原请求的路径，一般用于去掉项目名。
    - `AddRequestHeader=a,1`：为原请求添加Header键值对a=1。
    - `AddRequestParameter=b,2`：为原请求添加请求参数键值对b=2。
    - `AddResponseHeader=c,3`：为原响应添加Header键值对c=3。
    - `RemoveRequestHeader=d`：为原请求删除Header中的d键。
    - `RemoveResponseHeader=f`：为原响应删除Header中的f键。
    - `SetResponseHeader=g,4`：修改原响应Header中g的值为4。
    - `RequestSize=10`：设置请求包大小，超过10字节时返回413错误，默认5M。
    - `SetStatus=2000`：修改原始响应的状态码。
- cli: `localhost:8000/user-service/api/user/select-by-id?id=1`：
    - 浏览器F12可观察到过滤器对响应相关数据的改动，但来不及观察到请求相关的修改。

**武技：** 开发局部过滤器，读取内置过滤器AddRequestHeader和AddRequestParameter中配置的值：
- 开发局部过滤器类 `filter.ReqGatewayFilterFactory`：
    - 标记 `@Component` 以加入spring容器。
    - 名字必须是 `自定义过滤配置的key值 + GatewayFilterFactory` 格式。
    - 必须继承 `o.s.c.g.f.f.AbstractGatewayFilterFactory<外部类.内部配置类>` 类。
- 在局部过滤器类中开发内部配置类，用于接收和存储主配中的Req值：
    - `static class Config{}`：埋requestHeader和requestParam两个布尔属性并生成setter/getter。
- 重写无参构造并使用 `super(Config.class)` 将内部配置类传递给父类。
- 重写 `shortcutFieldOrder()`：读取主配中Req的属性值并对位赋值给Config配置类的属性：
    - `return Arrays.asList("requestHeader", "requestParam")`
- 重写 `apply()`：配置断言逻辑，内部直接使用 `return (exchange, chain) -> {}` 返回过滤结果：
    - `exchange.getRequest().getHeaders().get("a")`：获取指定请求头中的值。
    - `exchange.getRequest().getQueryParams().get("b")`：获取指定请求参数中的值。
    - `return chain.filter(exchange)`：放行当前过滤器。
- 主配添加：
    - `spring.cloud.gateway.routes[0].filters[0].Req=true,true`
- cli: `localhost:8000/user-service/api/user/select-by-id?id=1`：查看控制台日志。

**武技：** 开发全局过滤器，如果请求不携带token，则直接响应401：
- 开发全局过滤器类 `filter.TokenGlobalFilter`：
    - 标记 `@Component` 以加入spring容器。
    - 必须实现 `o.s.c.g.f.GlobalFilter` 接口。
    - 必须实现 `o.s.c.Ordered` 接口。
- 重写 `filter()`：配置过滤器逻辑：
    - `exchange.getRequest().getQueryParams().getFirst("token")`：从请求参数中获取token。
    - `exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED)`：设置响应码为401认证失败。
    - `return exchange.getResponse().setComplete()`：直接发起响应，相当于阻止程序向下运行。
    - `return chain.filter(exchange)`：程序向下运行。
- 重写 `getOrder()`：配置过滤器优先级：返回值越小，优先级越高。
- cli: 以 `localhost:8000/user-service/api/user/select-by-id` 为前缀：
    - `?id=1`：未传递token，阻止请求。
    - `?id=1&token=123`：传递了token，放行请求。

## 配置限流

**心法：** Gateway网关可以配合Sentinel在网关处使用过滤器对某一组请求进行限流。

**武技：** 配置Gateway网关限流：
- 在网关服务中添加依赖：
    - `com.alibaba.csp.sentinel-spring-cloud-gateway-adapter`
- 开发网关限流配置类 `config.SentinelConfig`：
    - 标记 `@Configuration` 以声明为配置类。
    - 开发视图解析器列表属性：`List<ViewResolver> resolvers`，用于展示限流后的响应内容。
    - 开发请求读写器属性：`ServerCodecConfigurer configurer`，用于操作请求和响应内容。
    - 开发构造器：p1为 `ObjectProvider<List<ViewResolver>> provider`：
        - `provider.getIfAvailable(Collections::emptyList)`：获取视图解析器列表并上升作用域。
    - 开发构造器：p2为 `ServerCodecConfigurer configurer`：请求读写器可直上升作用域。
- 在配置类中管理一个全局限流过滤器的bean：
    - 方法名固定为 `sentinelGatewayFilter`。
    - 返回值固定为 `o.s.c.g.f.GlobalFilter` 接口。
    - 标记 `@Bean`：声明为一条bean配置。
    - 标记 `@Order(Ordered.HIGHEST_PRECEDENCE)`：设置过滤器优先级为最高。
    - 返回 `new SentinelGatewayFilter()`：实例化该bean。
- 在配置类中管理一个限流异常处理器的bean：
    - 方法名固定为 `sentinelGatewayBlockExceptionHandler`。
    - 返回值固定为 `c.a.c.s.a.g.s.e.SentinelGatewayBlockExceptionHandler` 接口。
    - 标记 `@Bean`：声明为一条bean配置。
    - 标记 `@Order(Ordered.HIGHEST_PRECEDENCE)`：设置过滤器优先级为最高。
    - 返回 `new SentinelGatewayBlockExceptionHandler()`：实例化该bean，需要入参视图解析器列表和请求读写器实例。
- 在配置类中开发一个限流时的响应方法 `void initExceptionResponse()`，方法名随意：
    - 标记 `@PostConstruct`，表示该方法在过滤器之前执行。
    - 使用 `GatewayCallbackManager.setBlockHandler()` 直接在限流时进行响应，参数为Lambda表达式。
    - Lambda表达式参数有两个，`exchange/throwable`，分别表示网络数据交换实例和异常实例。
    - Lambda表达式中创建一个Map，并存放响应内容。
    - Lambda表达式中直接返回 `ServerResponse.status(HttpStatus.OK)` 以完成状态码为200的响应：
        - `.contentType(MediaType.APPLICATION_JSON_UTF8)`：设置响应MIME类型为JSON格式。
        - `.body(BodyInserters.fromObject(map))`：设置响应数据。
- 在配置类中开发一个API接口分组方法 `void initApiDefinition()`，方法名随意：
    - 标记 `@PostConstruct`，表示该方法在过滤器之前执行。
    - 实例化一个Set集合，用于存放API分组数据，泛型使用 `ApiDefinition` 类。
    - 使用 `new ApiDefinition("productA")` 创建分组，参数为组名。
    - 使用 `.setPredicateItems(new HashSet<>() {{ add(); }})` 对分组设置路径匹配规则，规则在 `add()` 内添加。
    - 使用 `new ApiPathPredicateItem().setPattern("")`：创建路径精准匹配规则：
        - 若额外配置 `.setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX)` 表示路径前缀模糊匹配。
    - 将设置好的分组信息添加到之前准备的set集合中。
    - 将自定义分组信息加载到API网关的API分组中：
        - `GatewayApiDefinitionManager.loadApiDefinitions(definitions)`        
- 在配置类中开发一个限流规则方法 `void initGatewayRules()`，方法名随意：
    - 标记 `@PostConstruct`，表示该方法在过滤器之前执行。
    - 实例化一个Set集合，用于存放API限流规则。
    - 使用 `new GatewayFlowRule("productA").setCount(1).setIntervalSec(2)` 实例化限流规则：
        - `productA`：组名，该限流规则仅对改组生效。
        - `setCount(2)`：设置QPS阈值，1秒内访问次数超过2次立刻限流。
        - `setIntervalSec(3)`：设置时间窗口为3秒，表示限流3秒后恢复访问，默认1秒。
    - 将每组规则都添加到Set集合中。
    - 使用 `GatewayRuleManager.loadRules(Set集合)` 将Set集合加载到网关规则管理器中使其生效。
- cli: `localhost:8000/user-service/api/user/select-by-id?id=1`：快速访问，发现被网关限流。

# 链路追踪Sleuth

**心法：** 分布式链路追踪用于将一次分布式请求还原成调用链路，进行日志记录，性能监控并集中展示，SpringCloud-Alibaba技术栈中并没有提供自己的链路追踪技术，建议采用Sleuth + Zipkin来进行做链路追踪：
- Trace：当请求到达项目的入口端点时，链路追踪框架会为该请求创建一个唯一标识TraceId，直到整个请求返回，该值均不变。
- Span：在一条Trace中，每次请求都会生成一个唯一标识SpanId，用来标记请求的开始，具体过程和结束，方便统计该Span的调用时间和元信息等。
- Annotation：用于记录一次请求的生命周期时间段，是链路追踪内部使用的重要元素：
    - cs(Client Send): 客户端发出请求，开始一个请求的生命周期。
    - sr(Server Received): 服务端接受到请求开始进行处理，sr - cs = 网络延迟（服务调用的时间）。
    - ss(Server Send): 服务端处理完毕准备发送到客户端，ss - sr = 服务器上的请求处理时间。
    - cr(Client Received): 客户端接受到服务端的响应，请求结束，cr - sr = 请求的总时间。

**武技：** 使用sleuth对下单业务进行链路追踪：
- 在父项目中添加依赖：
    - `org.springframework.cloud.spring-cloud-starter-sleuth`
- 依次启动nacos，商品服务，订单服务，API网关。
- cli: `localhost:8000/order-service/api/order/insert?product-id=1`
    - sleuth控制台日志：`[微服务名, TraceId, SpanId, 是否连入第三方管控台]`

## zipkin管控台

**心法：** Zipkin是Twitter的一个开源项目，可以用于收集各个服务器上请求链路的跟踪数据，从而及时地发现系统中出现的延迟升高问题，Zipkin客户端（微服务）会配置Zipkin服务端的URL地址，一旦发生服务间的调用，就会触发配置在客户端里面的Sleuth监听器，生成相应的Trace和Span信息并发送给Zipkin服务端：
- Collector：Zipkin的收集器组件，用于处理从外部系统发送过来的跟踪信息，并将这些信息转换为Zipkin内部处理的Span格式。
- Storage：Zipkin的存储组件，用于将收集器接收到的跟踪信息存储在内存中，支持持久化操作。
- REST_API：Zipkin的API组件，用于提供外部访问接口，比如给客户端展示跟踪信息，或是外接系统访问以实现监控等。
- Web_UI：Zipkin的UI组件，用于更方便，更直观地展示给用户查询和分析跟踪的信息。

**武技：** 使用sleuth和zipkin对下单业务进行链路追踪：
- 下载最新版 [zipkin](https://search.maven.org/remote_content?g=io.zipkin.java&a=zipkin-server&v=LATEST&c=exec)：
    - `资源\zipkin-server-2.12.9-exec.jar`
- 启动zipkin服务：
    - cmd: `java -jar zipkin-server-2.12.9-exec.jar`
    - cli: `http://localhost:9411`：访问zipkin管控台。
- 在父项目中添加依赖：
    - `org.springframework.cloud.spring-cloud-starter-zipkin`
- 分别在用户/商品/订单/网关微服务的主配中添加：
    - `spring.zipkin.base-url=http://localhost.1:9411`：zipkin服务端地址。
    - `spring.zipkin.discoveryClientEnabled=false`：不向nacos注册此服务。
    - `spring.sleuth.sampler.probability=1.0`：采样的百分比设置为100%采样。
- 依次启动nacos，商品服务，订单服务，API网关。
- 从订单微服务访问下单接口，查看zipkin管控台链路。

## zipkin持久化

**心法：** zipkin的链路追踪的信息默认存储在内存中，即服务一旦重启就会丢失之前的数据，建议持久化。

**武技：** 将zipkin链路追踪信息持久化到mysql数据库：
- 添加zipkin持久化相关的三张表（官方提供）：
    - `z-sql/zipkin.sql`
- 启动zipkin服务，额外指定mysql相关配置：
    - `java -jar zipkin-server-2.12.9-exec.jar` 
    - `--STORAGE_TYPE=mysql`：指定将链路追踪的数据持久化到mysql数据库。
    - `--MYSQL_HOST=127.0.0.1`：指定mysql的IP地址。
    - `--MYSQL_TCP_PORT=3306`：指定mysql的端口。
    - `--MYSQL_DB=zipkin`：指定mysql数据库实例名，需要自己创建。 
    - `--MYSQL_USER=zipkin`：指定mysql的连库账号。
    - `--MYSQL_PASS=zipkin`：指定mysql的连库密码。
- 依次启动nacos，商品服务，订单服务，API网关。
- 从订单微服务访问下单接口，查看zipkin表中是否记录了本次链路信息。

**武技：** 将zipkin链路追踪信息持久化到elasticsearch中：
- elasticsearch6版本后删除了自动设置索引，需要换低版本，如5.6.8版本。
- 启动elasticsearch：
    - 双击 `%ELASTICSEARCH_HOME%\bin\elasticsearch.bat` 文件。
- 启动zipkin服务端，额外指定elasticsearch相关配置：
    - `java -jar zipkin-server-2.12.9-exec.jar`
    - `--STORAGE_TYPE=elasticsearch`：指定将链路追踪的数据持久化到elasticsearch。
    - `--ES-HOST=localhost:9200`：指定elasticsearch的服务地址和端口。

# 消息队列RocketMQ

**心法：** [RocketMQ](http://rocketmq.apache.org/) 是阿里巴巴开源的一款高性能，高吞吐量的分布式MQ，源于jms规范但不遵守jms规范：
- NameSrv：邮局，用于管理broker。
- Broker：快递员，用于接收，存储，投递消息，broker需要提前向NameSrv中注册自己，一个broker中包含多个Topic。
- Topic：地区，用来区分不同类型的消息，发送和接收消息之前都需要先创建Topic，一个Topic中包含1或N个MessageQueue：
    - Topic中可以按照标签tag进行更详细的分类。
- MessageQueue：邮件，用于提高吞吐量，支持并行地向多个MQ发送消息，也支持并行的从多个MQ中获取消息。
- Message：消息载体。
- Producer：生产者，寄件人，从NameSrv中获取Broker，生产消息并投递给Broker。
- Consumer：消费者，收件人，从NameSrv中获取Broker，并消费消息。
- ProducerGroup：生产者组，多个发送同一消息的生产者称为一个生产者组。
- ConsumerGroup：消费组，多个消费同一消息的消费者称为一个消费者组。

**武技：** 搭建RocketMQ环境：
- 安装RMQ：下载RMQ并配置环境变量 `ROCKETMQ_HOME` 和 `path`：
    - `资源/rocketmq-all-4.9.0-bin-release.zip`：解压缩即可。
- 启动邮局后台 `namesrv.cmd`：
    - cmd: `mqnamesrv.cmd -n localhost:9876`：端口默认9876。
- 启动快递员后台 `mqbroker.cmd`：
    - cmd: `mqbroker.cmd -n localhost:9876 autoCreateTopicEnable=true`：自动创建topic/tag。
- 安装RMQ界面：是一个springboot项目 `rocketmq-console`：
    - `资源/rocketmq-externals-rocketmq-console-1.0.0.zip`：解压缩即可。
- 修改RMQ界面的主配：
    - `server.port=12581`：配置RMQ界面端口，默认8080。
    - `rocketmq.config.namesrvAddr=localhost:9876`：配置NameSrv地址。
- 打包，启动并访问RMQ界面：需要进入到项目根目录中：
    - cmd: `mvn clean package -Dmaven.test.skip=true`
    - cmd: `java -jar rocketmq-console-ng-1.0.1.jar`
    - cli: `localhost:12581`

## 生产消费模型

**心法：** 测试RocketMQ的生产消费者模型需要添加RMQ相关依赖，如在用户微服务中添加：
- `org.apache.rocketmq.rocketmq-spring-boot-starter(2.0.3)`

**武技：** 使用junit模拟生产者生产消息到broker中：
- 创建生产者实例，并纳入指定的生产者组中，生产者组自动创建：
    - `new DefaultMQProducer("test-producer-group")`
- 为生产者指定RMQ服务地址并启动：
    - `producer.setNamesrvAddr("localhost:9876")`：集群以分号分隔。
    - `producer.start()`
- 创建一个消息实例：
    - `new Message("主题", "标签", "消息".getBytes(RemotingHelper.DEFAULT_CHARSET))`
- 生产者将消息发送到broker：
    - `producer.send(消息实例, 超时毫秒数)`：返回一个SendResult实例。
- 从SendResult实例中获取消息ID和发送状态：
    - `sendResult.getMsgId()`：每个消息都拥有一个唯一标识ID。
    - `sendResult.getSendStatus()`：发送成功会返回 `SEND_OK` 结果。
- tst: `rmq.ProducerTest`：在broker中查看是否已经接收到了生产者生产的消息。

**武技：** 使用junit模拟消费者消费broker中的消息：
- 创建消费者实例，并纳入指定的消费者组中，消费者组自动创建：
    - `new DefaultMQPushConsumer("test-consumer-group")`
- 为消费者指定RMQ服务地址：
    - `consumer.setNamesrvAddr("localhost:9876")`：集群以分号分隔。
- 为消费者订阅主题和标签：
    - `consumer.subscribe("主题", "标签")`：多标签用 `||` 分隔，`*` 表示订阅所有标签。
- 为消费者设置监听：监听的参数可直接使用Lambda表达式，但需要强转为MessageListenerConcurrently类型：
    - `consumer.registerMessageListener((MessageListenerConcurrently)(msgs, context)->{})`
    - `new String(messageExt.getBody())`：Lambda表达式中获取消息体内容的字符串形式。
    - `ConsumeConcurrentlyStatus.RECONSUME_LATER`：Lambda表达式返回值，表示稍后再试。
    - `ConsumeConcurrentlyStatus.CONSUME_SUCCESS`：Lambda表达式返回值，表示消费成功。
- 启动消费者实例：
    - `consumer.start()`
- tst: `rmq.ConsumerTest`：查看是否可以成功消费broker中待消费的消息。

## 消息类型

**心法：** RMQ的消息分为普通消息和顺序消息两种：
- 普通消息：
    - 同步发送：发送方发出数据后，会在收到响应后才发下一个消息，用于重要通知邮件，报名短信，营销短息等。
    - 异步发送：发送方发出数据后，无需等待响应，直接发送下个消息，用于链路耗时长，对RT时间敏感的业务。
    - 单项发送：发送方只负责发送消息，不需要接收响应，适用于耗时短，不是特别重要的业务，速度最快。
- 顺序消息：一种严格按照顺序来发布和消费的消息类型：
    - 针对API的最后一个字符串参数进行hash取余，以决定该消息落入哪个MessageQueue中。

**武技：** 使用RocketMQTemplate测试三种普通消息和一种顺序消息：
- 用户微服务中添加依赖：
    - `org.apache.rocketmq.rocketmq-spring-boot-starter(2.0.3)`
- 用户微服务主配中添加：
    - `rocketmq.name-server=localhost:9876`：RMQ的NameServer的地址。
    - `rocketmq.producer.group=user-producer-group`：配置生产者组名。
- 测试类使用springboot-test方式启动单元测试：
    - 标记 `@RunWith(SpringRunner.class)`：指定启动方式。
    - 标记 `@SpringBootTest(classes = UserApp.class)`：指定启动类。
- 测试类中使用 `@Autowired` 注入 `o.a.r.s.c.RocketMQTemplate` 类。
- 开发测试方法 `testSyncSend()`，测试同步发送消息：
    - `rmqTemplate.syncSend("主题:标签", "消息", 超时时间)`
- 开发测试方法 `testAsyncSend()`，测试异步发送消息：
    - `rmqTemplate.asyncSend("主题:标签", "消息", new SendCallback())`
    - 当消息发送成功时触发回调函数中的 `onSuccess()`。
    - 当消息发送异常时触发回调函数中的 `onException()`。
- 开发测试方法 `testSendOneWay()`，测试单项发送消息：
    - `rmqTemplate.sendOneWay("主题:标签", "消息")`
- 开发测试方法 `testSendOneWayOrderly()`，测试顺序发送消息：
    - `rmqTemplate.sendOneWayOrderly("主题:标签", "消息", "xx")`：最后一个参数随意起名。
- tst: `rmq.MessageTypeTest`

## 整合springboot

**武技：** 订单微服务下单成功后，向broker中的order:sms投递消息：
- 添加依赖：
    - `org.apache.rocketmq.rocketmq-spring-boot-starter(2.0.3)`
- 添加主配：
    - `rocketmq.name-server=localhost:9876`：RMQ的NameServer的地址。
    - `rocketmq.producer.group=order-producer-group`：配置生产者组名。
- 开发业务类 `service.impl.OrderServiceImpl`：
    - 使用 `@Autowired` 直接在订单业务类中注入 `o.a.r.s.c.RocketMQTemplate` 类。
- 开发业务方法 `insert()`：
    - `rmqTemplate.convertAndSend("order:sms", order)`：下单成功后，同步向broker指定主题标签投递order实体。
- cli: `localhost:8000/order-service/api/order/insert?product-id=1`：
    - 观察rmq管控台，查看是否存在订单微服务投递的消息。

**武技：** 用户微服务消费broker中的order:sms消息：
- 添加依赖：
    - `org.apache.rocketmq.rocketmq-spring-boot-starter(2.0.3)`
- 添加主配：
    - `rocketmq.name-server=localhost:9876`：RMQ的NameServer的地址。
- 开发消息监听类 `listener.OrderSmsListener`：用于接收RMQ的Broker投递的消息：
    - 标记 `@Component` 以加入spring管理。
    - 实现 `o.a.r.s.c.RocketMQListener<Order>`，类泛型为具体消息的类型。
    - 重写 `onMessage()`：该方法在Broker投递消息时触发并执行。
- 为消息监听类标记 `@RocketMQMessageListener()` 以声明为一个RMQ消费监听类：
    - `consumerGroup = "user-consumer-group"`：消费者组名。
    - `topic = "order"`：只消费order主题的消息。
    - `selectorExpression = "sms"`：只消费sms标签的消息，默认为 `*`。
    - `consumeMode = ConsumeMode.CONCURRENTLY`：默认CONCURRENTLY同步发送消息，可设置为ORDERLY顺序发送消息。
    - `messageModel = MessageModel.CLUSTERING`：默认CLUSTERING集群发送消息，可设置为BROADCASTING广播消息。
- 启动用户微服务，查看日志是否消费了order:sms中的消息。

# 配置中心NacosConfig

**心法：** 传统微服务架构的配置文件相对分散，不好统一配置和管理，无法区分环境，无法实时动态更新，而NacosConfig可以将微服务中的各种配置进行统一管理，并提供一套标准的接口。

**武技：** 将用户微服务的主配移动到NacosConfig配置中心：
- 在用户微服务中添加依赖：
    - `com.alibaba.cloud.spring-cloud-starter-alibaba-nacos-config`
- 在用户微服务中添加配置文件 `bootstrap.yml`：用于连接nacos配置中心，优先级比 `application.yml` 高：
    - `spring.application.name=al-user`：设置微服务名。
    - `spring.cloud.nacos.config.server-addr=127.0.0.1:8848`：指定nacos配置中心地址。
    - `spring.cloud.nacos.config.file-extension=yaml`：指定nacos配置文件后缀。
    - `spring.profiles.active=test`：设置当前微服务的环境为测试环境。
- 登录nacos管控台，点击 `配置列表`，点击右侧加号进行添加：
    - `Data ID`：配置文件名的格式固定为 `微服务名-环境名.后缀`，如 `al-user-test.yaml`。
    - `Group`：分组用于将不同的服务归类到同一组，一般将一个项目的配置分到一组，默认即可。
    - `配置格式`：建议选择 `YAML`。
    - `配置内容`：将本地 `application.yml` 的全部内容拷贝过来，点击发布。
- 注释掉本地 `application.yml` 中的全部内容并启动用户微服务和网关。
- cli: `localhost:8000/user-service/api/user/select-by-id?id=1`：若仍可访问，说明nacosConfig配置成功。

## 动态读取配置

**武技：** 在微服务任意位置动态读取NacosConfig配置中心的主配内容：
- 在NacosConfig中的al-user-test.yaml中添加一条自定义配置：
    - `env=test`：表示当前开发环境为测试环境。
- 开发控制器 `controller.NacosConfigController`：
    - 标记 `@RestController` 以加入spring容器。
    - 标记 `@RequestMapping` 以指定URL接口前缀。
    - 标记 `@RefreshScope` 以开启动态刷新nacos配置文件内容的功能。
- 开发env属性并标记 `Value("${env}")` 以注入配置文件中env属性的值。
- 开发控制方法，将env属性返回到页面。
- cli: `localhost:8000/user-servic/api/nacos-config/get-env`：
    - 修改NacosConfig中的al-user-test.yaml中的env的值，再次访问以测试动态刷新效果。

## 跨环境共享配置

**武技：** 将开发环境主配和测试环境主配的相同配置提取到跨环境共享配置文件：
- 在NacosConfig配置中心创建一个用户微服务的开发环境主配 `al-user-dev.yaml`：
    - `env=dev`：表示当前开发环境为开发环境。
- 在NacosConfig配置中心创建一个跨环境共享配置文件 `al-user.yaml`：
    - 文件名格式固定为 `微服务名.后缀`。
    - 将全部环境的主配中的相同配置剪切过来。
- 重启用户微服务，正常启动说明跨环境共享配置成功。

## 跨服务共享配置

**武技：** 将全部微服务的主配中的相同配置提取到跨服务共享配置文件：
- 在NacosConfig配置中心创建一个跨服务共享配置文件 `service-comm.yaml`，名称随意：
    - 将全部微服务的主配中的相同配置剪切过来。
- 依次在每个微服务的 `bootstrap.yml` 中配置：
    - `spring.cloud.nacos.config.shared-dataids: service-comm.yaml`：配置跨服务共享配置文件名。
    - `spring.cloud.nacos.config.refreshable-dataids: service-comm.yaml`：配置动态刷新跨服务共享配置文件。
- 重启用户微服务，正常启动说明跨环境共享配置成功。

# 分布式事务Seata

**心法：** 分布式事务是为了保证当一个事务中的N个操作分布在不同的微服务中时，仍可以保证像本地事务一样的ACID特性，Seata是阿里巴巴架构中的分布式事务解决方案，对业务无侵入，让分布式事务的使用像本地事务的使用一样简单和高效：
- 假设A服务需要远程调用B服务，两个服务需要在一个分布式事务中：
- 下载安装并启动一个Seata的事务协调器 `Transaction Coordinator(TC)`，用于协调全局事务。
- 分别在两个服务中添加资源管理器 `Resource Manager(RM)`，用于发起分支事务。
- 在事务中的最上游服务（即A服务）中添加事务管理器 `Transaction Manager(TM)`，用于向TC申请全局事务。
- A服务的TM向TC申请开启一个全局事务，TC创建一个全局事务并返回一个唯一标识XID。
- A服务的RM向TC注册分支事务，TC返回一个分支ID，并关联到XID对应全局事务。
- A服务执行分支事务，操作数据库，记录undo_log日志，两件事需要在一个本地事务中：
    - 若A服务的分支事务没有问题，向TC汇报一个commit消息，释放资源锁。
    - 若A服务的分支事务出现问题，向TC汇报一个rollback消息，释放资源锁。
- A服务远程调用B服务，并将XID传递给B服务。
- B服务的RM向TC注册分支事务，TC返回一个分支ID，并关联到XID对应全局事务。
- B服务执行分支事务，操作数据库，记录undo_log日志，两件事需要在一个本地事务中：
    - 若B服务的分支事务没有问题，向TC汇报一个commit消息，释放资源锁。
    - 若B服务的分支事务出现问题，向TC汇报一个rollback消息，释放资源锁。
- 若A服务和B服务均汇报commit消息，TC通知两个服务删除undo_log日志信息，全局事务完毕。
- 若A服务和B服务中至少一个rollback消息：
    - TC通知两个服务分别根据undo_log日志反向生成并执行SQL以恢复数据，并删除undo_log日志信息。

**武技：** 下载安装并启动seata服务：
- 下载 [seata](https://github.com/seata/seata/releases/v0.9.0/)：解压缩到硬盘即可。
- 修改 `%SEATA_HOME%\conf\registry.conf` 配置：
    - 将 `registry` 中的 `type` 值替换为 `nacos`，然后删除除nacos外的其他代码块和namespace项。
    - 将 `config` 中的 `type` 值替换为 `nacos`，然后删除除nacos外的其他代码块和namespace项。
    - 将 `registry.conf` 文件分别拷贝到商品微服务和订单微服务的resources下。
- 初始化seata在nacos中的配置，在 `%SEATA_HOME%\conf` 目录中执行命令：
    - cmd: `nacos-config.sh 127.0.0.1`：前提保证nacos服务是启动的。
    - 在nacos管控台配置列表中发现生成了很多Group为SEATA_GROUP配置文件。
- 启动seata服务：在 `%SEATA_HOME%\bin` 目录中执行命令：
    - cmd: `seata-server.bat -p 9000`：默认端口8091。
    - 在nacos管控台服务列表中发现生成 `serverAddr` 服务。

**武技：** 将订单微服务下单和商品微服扣减库存加入到一个分布式事务中：
- 创建seata备份表 `undo_log`：
    - `z-sql/seata.sql`
- 分别在商品微服务和订单微服务中添加依赖：
    - `com.alibaba.cloud.spring-cloud-starter-alibaba-seata`
    - `com.alibaba.cloud.spring-cloud-starter-alibaba-nacos-config`
- 分别在商品微服务和订单微服务中添加 `bootstrap.yml` 主配：
    - `spring.application.name: al-order/al-product`：配置微服务名。
    - `spring.profiles.active=test`：配置环境标识，本案例中，该项可选。
    - `spring.cloud.nacos.config.server-addr: 127.0.0.1:8848`：nacos配置中心地址。
    - `spring.cloud.nacos.config.group=SEATA_GROUP`：seata在nacos中生成的配置文件的GROUP名。
    - `spring.cloud.nacos.config.namespace=public`，命名空间，默认public。
    - `spring.cloud.alibaba.seata.tx-service-group=my_test_tx_group`，配置事务服务名：
        - 必须和 `%SEATA_HOME%\conf\file.conf`：中service模块中的值保持一致。
    - `logging.level.com.alibaba.nacos.client.naming: warn`：阻止nacos无限地心跳检测日志，可选。
- 分别在商品微服务和订单微服务添加数据源代理配置类 `config.DataSourceProxyConfig`：
    - 标记 `@Configuration` 以声明为配置类。
- 在配置类中管理 `c.a.d.p.DruidDataSource` 数据源类：
    - 标记 `@Bean` 以被容器管理。
    - 标记 `@ConfigurationProperties(prefix = "spring.datasource")` 以从主配中读取数据源信息。
    - 返回 `new DruidDataSource()` 即可。
- 在配置类中管理 `i.s.r.d.DataSourceProxy` 数据源代理类：
    - 标记 `@Bean` 以被容器管理。
    - 标记 `@Primary` 表示当存在多个数据源代理时，该bean的优先级最高。
    - 方法形参直接注入 `DruidDataSource druidDataSource` 以使用数据源信息。
    - 返回 `new DataSourceProxy(druidDataSource)` 即可。
- 分别在商品微服务和订单微服务的启动类中排除 `DataSourceAutoConfiguration` 的自动注入以避免循环注入：
    - `@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)`
- 在商品微服务中的扣减库存业务方法 `reduceInventory()` 中添加当库存为0时，回滚seata事务的业务：
    - `GlobalTransactionContext.reload(RootContext.getXID()).rollback()`：回滚seata事务。
- 在订单微服务的下单业务方法 `insert()` 中添加seata事务注解以开启全局事务：
    - `@GlobalTransactional(rollbackFor = Exception.class)`
- 依次启动nacos，seata，商品微服务，订单微服务，API网关，并访问订单微服务的下单接口。