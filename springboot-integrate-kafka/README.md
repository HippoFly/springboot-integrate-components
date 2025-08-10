# Spring Boot 集成 Kafka 示例

该模块演示了如何在Spring Boot应用程序中集成Apache Kafka消息中间件。

## 功能特性

- Kafka生产者配置
- Kafka消费者配置
- REST API接口发送消息到Kafka主题
- 自动监听Kafka主题并消费消息

## 主要组件

1. `KafkaProducerService` - 负责发送消息到Kafka主题
2. `KafkaConsumerService` - 监听并消费Kafka主题中的消息
3. `KafkaController` - 提供REST API接口用于测试Kafka功能
4. `KafkaConfig` - Kafka相关配置类

## 配置说明

在 `application.yml` 中配置了以下Kafka参数：
- bootstrap-servers: Kafka服务器地址
- producer key/value serializer: 消息序列化方式
- consumer group-id: 消费者组ID
- consumer key/value deserializer: 消息反序列化方式

## 使用方法

1. 启动Kafka服务
2. 修改 `application.yml` 中的Kafka服务器地址（如需要）
3. 启动Spring Boot应用
4. 调用 `/kafka/send` 接口发送消息到指定主题
5. 观察控制台输出，查看消费者接收到的消息

## API接口

- `POST /kafka/send` - 发送消息到指定主题
  - 参数: topic (主题名称), message (消息内容)
  - 返回: 发送成功提示

## 测试指南

### 单元测试

运行单元测试：
```bash
cd springboot-integrate-kafka
mvn test
```

测试包括以下内容：
1. Spring上下文加载测试
2. Kafka生产者服务加载测试
3. Kafka消费者服务加载测试
4. 消息发送功能测试

### 集成测试

1. **启动本地 Kafka 环境**：
   推荐使用 Docker 启动 Kafka：
   ```bash
   # 创建并启动 Kafka 和 Zookeeper 容器
   docker-compose up -d
   ```

2. **验证 Kafka 服务状态**：
   ```bash
   # 检查 Kafka 容器是否运行
   docker ps | grep kafka
   
   # 检查 Kafka 容器日志
   docker logs kafka
   
   # 测试端口连通性
   nc -zv localhost 9092
   ```

3. **启动应用程序**：
   ```bash
   mvn spring-boot:run
   ```

4. **测试 API 接口**：
   - 通过 Swagger UI: 访问 `http://localhost:8980/swagger-ui.html`
   - 使用 curl 命令:
     ```bash
     curl -X POST "http://localhost:8980/kafka/send?topic=test-topic&message=HelloKafka"
     ```

5. **验证结果**：
   查看应用控制台输出，应该能看到消费者接收到的消息:
   ```
   Received Message: HelloKafka
   ```

## 学习要点

1. Kafka基本概念：主题(Topic)、分区(Partition)、生产者(Producer)、消费者(Consumer)
2. Spring Kafka的自动配置和使用
3. 消息的发送和接收处理
4. Kafka在分布式系统中的应用场景
5. Spring Boot应用与消息中间件的集成方式
6. 异步消息处理机制