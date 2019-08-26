# 使用外部配置文件

在配置文件里配置Bean时，有时需要在Bean的配置里混入**系统部署的细节信息**(例如：文件路径、数据源配置信息等)，而这些部署细节实际上需要和bean配置相分离

Spring提供了一个PropertyPlaceholderConfigurer的**Befactory后置处理器**，这个上处理器允许用户将bean配置的部分内容外移到**属性文件**中，可以在Bean配置文件里使用形式为**${var}**的变量，PropertyPlaceholderConfigurer从属性文件里加载属性，并使用这些属性来替换变量

Spirng还允许在属性文件中使用${propName}，以实现属性之间的相互引用

## 注册PropertyPlaceholderConfigurer：

Spring2.0:

```xml
<bean class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
    <property name="location" value="classpath:db.properties"/>
</bean>
```

Spring2.5之后：可通过<context:property-placeholder>元素简化:

<beans>中添加context Schema定义

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-4.0.xsd">
</beans>
```

在配置文件中加入如下配置：

```xml
<context:property-placeholder location="classpath:db.properties"/>
```

配置数据源

```xml
<dependency>
    <groupId>com.mchange</groupId>
    <artifactId>c3p0</artifactId>
</dependency>

<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
</dependency>
```

db.properties

```properties
username=admin
password=richard
driverClass=com.mysql.jdbc.Driver
jdbcUrl=jdbc:mysql://10.10.10.137:3306/mspubsys_scsjb_demo
```

```xml
<!--
    <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
        <property name="user" value="admin"/>
        <property name="password" value="richard"/>
        <property name="driverClass" value="com.mysql.jdbc.Driver"/>
        <property name="jdbcUrl" value="jdbc:mysql://10.10.10.137:3306/mspubsys_scsjb_demo"/>
    </bean>
    -->


<!-- 导入属性文件 -->
<context:property-placeholder location="classpath:db.properties"/>

<bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
    <!-- 使用外部化属性文件的属性 -->
    <property name="user" value="${username}"/>
    <property name="password" value="${password}"/>
    <property name="driverClass" value="${driverClass}"/>
    <property name="jdbcUrl" value="${jdbcUrl}"/>
</bean>
```

```java
@Test
public void testProperties() throws SQLException {
    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    DataSource dataSource = (DataSource) ctx.getBean("dataSource");
    System.out.println(dataSource.getConnection());
}
```

