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

## 4､在WEB环境下使用Spring

### 1､添加依赖

```xml
 <dependency>
     <groupId>org.springframework</groupId>
     <artifactId>spring-web</artifactId>
</dependency>
```

### 2､Spring的配置文件，和非WEB环境没有什么不同

### 3､需要在web.xml文件中加入如下配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://java.sun.com/xml/ns/javaee"
         xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd"
         version="2.5">

    <!-- 配置Spring配置文件的名称和位置 -->
    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath:applicationContext.xml</param-value>
    </context-param>

    <!-- 启动IOC容器的ServletContextListener -->
    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>

</web-app>
```

```java
package zzc.spring.struts2.beans;

public class Person {
	private String username;

	public void setUsername(String username) {
		this.username = username;
	}

	public void hello() {
		System.out.println("My name is " + username);
	}
}
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="person" class="Person">
        <property name="username" value="ZhaoZhiCheng"/>
    </bean>
</beans>
```

```jsp
<%@ page import="org.springframework.context.ApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="Person" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <%
        // 1、从application域对象中得到IOC容器的实例
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(application);
        // 2、从IOC容器中得到bean
        Person person = ctx.getBean(Person.class);
        // 3、使用bean
        person.hello();
    %>
</body>
</html>
```

