# 1､Spring如何在WEB应用中使用

## 1､依赖

```xml
 <dependency>
     <groupId>org.springframework</groupId>
     <artifactId>spring-web</artifactId>
</dependency>
```

## 2､Spring的配置文件，没有什么不同

## 3､如何创建IOC容器

### 1､非WEB应用在main()方法中直接创建

### 2､应该在WEB应用被服务器加载时就创建IOC容器：

在ServletContextListener#contextInitialized(ServletContextEvent sce);方法创建IOC容器

### 3､在WEB应用的其他组件中如何来访问IOC容器呢？

在ServletContextListener#contextInitialized(ServletContextEvent sce);方法创建IOC容器后，可以把其放在ServletContext(即application域)的一个属性中

### 4､实际上，Spring配置文件的名字和位置应该也是可配置的

将其配置到当前WEB应用的初始化参数中较为合适







