# spring-boot-sleuth-rabbit-demo

This demo shows a problem when using `spring-cloud-sleuth-amqp-starter` togather with `Jackson2JsonMessageConverter`.

1. strart RabbitMQ
2. start the app
3. call http://localhost:8080/ 
4. this will trigger a rabbit message which will be handled by `MyMessageHandler.onMessage(MyMessage)`

`SpringContainerFactoryJacksonConverterConfiguration.rabbitListenerContainerFactory(ConnectionFactory, MessageConverter, AmqpMessagingSpanManager)` configures the `SimpleRabbitListenerContainerFactory` to use a `Jackson2JsonMessageConverter`.


If the `Jackson2JsonMessageConverter` is wrapped in `AmqpMessagingSpanSleuthExtractorMessageConverterWrapper`, then the log output looks like this:

```
2018-01-25 20:56:51.315  INFO [-,ed29372b2c21829e,ed29372b2c21829e,false] 86658 --- [nio-8080-exec-1] com.example.demo.DemoApplication         : Handling home
2018-01-25 20:56:51.315  INFO [-,ed29372b2c21829e,ed29372b2c21829e,false] 86658 --- [nio-8080-exec-1] com.example.demo.MyMessageSender         : just about to send
2018-01-25 20:56:51.373  INFO [-,ed29372b2c21829e,983bd6104c995efc,false] 86658 --- [cTaskExecutor-1] com.example.demo.MyMessageHandler        : got a message: Hello World!
```

If the `Jackson2JsonMessageConverter` is used directly, then the log output looks like this:

```
2018-01-25 20:55:27.373  INFO [-,be187184eaf0305f,be187184eaf0305f,false] 86491 --- [nio-8080-exec-1] com.example.demo.DemoApplication         : Handling home
2018-01-25 20:55:27.373  INFO [-,be187184eaf0305f,be187184eaf0305f,false] 86491 --- [nio-8080-exec-1] com.example.demo.MyMessageSender         : just about to send
2018-01-25 20:55:27.432  INFO [-,,,] 86491 --- [cTaskExecutor-1] com.example.demo.MyMessageHandler        : got a message: Hello World!
```

Please note the missing trace information in the very last log line.


