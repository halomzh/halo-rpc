# halo-rpc
## 介绍

halo-rpc是一款应用于spring环境的rpc框架。

注意：玩具！玩具！不要应用于生产！

## 原理

比较简单不画图了，看源码吧

## 开始

### rpc基本配置

```yaml
halo:
  rpc:
    enable: true #是否启动远程服务调用
    port: 7788 #服务监听端口
```

### 服务提供注解@RpcService

如果类上有@RpcService注解，那么改类可以提供远程服务。

### 远程服务应用@RpcReference

address：远程服务地址

remoteBeanName: 远程服务bean名称（应用@RpcService注解的bean名称）

### 案例

#### 编写提供服务的bean

```java
@RpcService
@Component("calculation")
public class CalculationService {

   public int add(int a, int b) {
      return a + b;
   }

}
```



#### 修改服务提供方配置

```yaml
server:
  port: 8898
spring:
  application:
    name: example-b

halo:
  rpc:
    enable: true #是否启动远程服务调用
    port: 3344 #服务监听端口
```

#### 启动服务提供方

![image-20210325221926194](https://raw.githubusercontent.com/halomzh/pic/master/20210325221935.png)

#### 编写服务消费方bean

```java
public interface CalculationService {

   int add(int a, int b);
   
}
```

```java
@Component
public class DemoService {

   @RpcReference(address = "127.0.0.1:3344", remoteBeanName = "calculation")
   private CalculationService calculationService;

   public int calculation(int a, int b) {
      
      return calculationService.add(a, b);
   }

}
```

```java
@SpringBootApplication
@RestController
@RequestMapping("/example")
@Slf4j
public class App {

   @Autowired
   private DemoService demoService;

   public static void main(String[] args) {
      SpringApplication.run(App.class, args);
   }

   @GetMapping("/calculation")
   public int calculation(@RequestParam(value = "a") int a, @RequestParam(value = "b") int b) {

      return demoService.calculation(a, b);
   }

}
```

#### 修改服务消费方配置

```yaml
server:
  port: 8899
spring:
  application:
    name: example-a

halo:
  rpc:
    enable: true #是否启动远程服务调用
    port: 7788 #服务监听端口
```

#### 启动服务消费方

![image-20210325222445314](https://raw.githubusercontent.com/halomzh/pic/master/20210325222446.png)

#### 调用服务消费方接口

![image-20210325222629411](https://raw.githubusercontent.com/halomzh/pic/master/20210325222630.png)![image-20210325222704646](https://raw.githubusercontent.com/halomzh/pic/master/20210325222706.png)