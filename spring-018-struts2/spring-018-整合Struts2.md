# 1､整合目标

使IOC容器来管理Struts2的Action

# 2､如何整合

## 1､加入依赖

```xml
<dependency>
    <groupId>org.apache.struts</groupId>
    <artifactId>struts2-core</artifactId>
</dependency>

 <dependency>
     <groupId>org.apache.struts</groupId>
     <artifactId>struts2-spring-plugin</artifactId>
</dependency>
```

web.xml

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

    <!-- 配置Struts2的filter -->
    <filter>
        <filter-name>struts2</filter-name>
        <filter-class>org.apache.struts2.dispatcher.filter.StrutsPrepareAndExecuteFilter</filter-class>
    </filter>

    <filter-mapping>
        <filter-name>struts2</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>

</web-app>
```

struts.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE struts PUBLIC
        "-//Apache Software Foundation//DTD Struts Configuration 2.5//EN"
        "http://struts.apache.org/dtds/struts-2.5.dtd">
<struts>

    <!-- 是否开启动态方法调用 -->
    <constant name="struts.enable.DynamicMethodInvocation" value="true"/>

    <!-- 是否使用struts的开发模式。开发模式会有更多的调试信息。默认值为false(生产环境下使用),开发阶段最好打开 -->
    <constant name="struts.devMode" value="true" />

    <package name="default"  namespace="/" extends="struts-default">


    </package>
</struts>
```

## 2､在Spring的IOC容器中配置Struts2的Action

注意：在IOC容器中配置Struts2中Action进，需要配置scope属性，其值必须为prototype

```java
package zzc.spring.struts2.Actions;

import zzc.spring.struts2.services.PersonService;

public class PersonAction {

	private PersonService personService;

	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	public String execute() {
		System.out.println("execute...");
		personService.save();
		return "success";
	}
}
```

```java
package zzc.spring.struts2.services;

public class PersonService {

	public void save() {
		System.out.println("PersonService.save");
	}
}
```

applicationContext.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="personService" class="zzc.spring.struts2.services.PersonService"/>

    <!-- 注意：在IOC容器中配置Struts2中Action进，需要配置scope属性，其值必须为prototype -->
    <bean id="personAction" class="zzc.spring.struts2.Actions.PersonAction" scope="prototype">
        <property name="personService" ref="personService"/>
    </bean>

</beans>
```

## 3､配置Struts2的配置文件

struts.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE struts PUBLIC
        "-//Apache Software Foundation//DTD Struts Configuration 2.5//EN"
        "http://struts.apache.org/dtds/struts-2.5.dtd">
<struts>

    <!-- 是否开启动态方法调用 -->
    <constant name="struts.enable.DynamicMethodInvocation" value="true"/>

    <!-- 是否使用struts的开发模式。开发模式会有更多的调试信息。默认值为false(生产环境下使用),开发阶段最好打开 -->
    <constant name="struts.devMode" value="true" />

    <package name="default"  namespace="/" extends="struts-default">

        <!-- Spring整合Struts2时，在Struts2中配置的Spring的Action的class需要指向IOC容器中该Bean的id -->
        <action name="person" class="personAction">
            <result>/success.jsp</result>
        </action>
    </package>
</struts>
```

index.jsp

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <a href="person">Person save</a>
</body>
</html>
```

success.jsp

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <h1>Success Page</h1>
</body>
</html>
```

# 3､整合原理

通过struts2-spring-plugin，Struts2会先从IOC容器中获取Action的实例

SpringObjectFactory

```java
public Object buildBean(String beanName, Map<String, Object> extraContext, boolean injectInternal) throws Exception {
    Object o;
    if (this.appContext.containsBean(beanName)) {
        o = this.appContext.getBean(beanName);
    } else {
        Class beanClazz = this.getClassInstance(beanName);
        o = this.buildBean(beanClazz, extraContext);
    }

    if (injectInternal) {
        this.injectInternalBeans(o);
    }

    return o;
}
```

