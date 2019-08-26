# 1､Spring整合Hibernate

## 1､由IOC容器来管理Hibernate的SessionFactory

## 2､让Hibernate使用上Spring的声明式事务

# 2、整合步骤

## 1､加入Hibernate

### 1､添加依赖

```xml
<dependency>
    <groupId>org.hibernate</groupId>
    <artifactId>hibernate-core</artifactId>
</dependency>

<!-- 连接数据库依赖 -->
<dependency>
    <groupId>com.mchange</groupId>
    <artifactId>c3p0</artifactId>
</dependency>

<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
</dependency>
```

### 2､添另Hibernate的配置文件hibernate.cfg.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE hibernate-configuration PUBLIC
        "-//Hibernate/Hibernate Configuration DTD 3.0//EN"
        "http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">
<hibernate-configuration>
    <session-factory>
        <!-- 配置Hibernate的基本属性 -->
        <!-- 1、数据源需配置到IOC容器中，所以此处不再需要配置数据源 -->
        <!-- 2、关联的.hbm.xml也在IOC容器配置SessionFactory实例时再进行配置-->
        <!-- 3、配置Hibernate的基本属性：方言、SQL显示及格式化、生成数据表的策略以及二级缓存 -->

        <!-- hibernate使用的数据库方言 -->
        <property name="hibernate.dialect">org.hibernate.dialect.MySQL55Dialect</property>
        <!-- 执行操作时是否在控制台打印SQL -->
        <property name="hibernate.show_sql">true</property>
        <!-- 是否对SQL进行格式化 -->
        <property name="hibernate.format_sql">true</property>
        <!-- 指定自动生成数据表的策略 -->
        <!--
            hbm2ddl.auto: create|update|create-drop|validate
            create:会根据 .hbm.xml 文件来生成数据表,但是每次运行都会删除上一次表,重新生成表,哪怕两次没有任何改变
            create-drop:会根据  .hbm.xml 文件生成表,但是SessionFactory一关,表就会自动删除
            update:最常用的属性值,会根据.hbm.xml文件生成表,但若.hbm.xml文件和数据库中对应的数据表的结构不同,hibernate将会更新表结构,但不会删除已有的行和列
            validate:会和数据库中的表进行比较,若.hbm.xml文件中的列在数据表中不存在,则会抛出异常
        -->
        <property name="hibernate.hbm2ddl.auto">update</property>

        <!-- 配置Hibernate二级缓存相关的属性 -->
    </session-factory>
</hibernate-configuration>
```

### 3､编写了持久化类对应的.hbm.xml文件

## 2､加入Spring

### 1､添加依赖

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-aspects</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-orm</artifactId>
</dependency>
```

IntelliJ IDEA需要在pom.xml下加入(默认src/main/java下的xml不会编译到classes下)

```xml
<build>
    <resources>
        <resource>
            <directory>src/main/java</directory>
            <includes>
                <include>**/*.hbm.xml</include>
            </includes>
        </resource>
        <resource>
            <directory>src/main/resources</directory>
            <includes>
                <include>**/*</include>
                <include>**/*.xml</include>
                <include>**/*.properties</include>
            </includes>
        </resource>
    </resources>
</build>
```

### 2､加入Spring的配置文件

db.properties

```properties
jdbc.user=admin
jdbc.password=richard
jdbc.driverClass=com.mysql.jdbc.Driver
jdbc.jdbcUrl=jdbc:mysql://10.10.10.141/test1

jdbc.initPoolSize=5
jdbc.maxPoolSize=10
```

applicationContext.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context" xmlns:tx="http://www.springframework.org/schema/tx"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx.xsd http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd">

    <!-- 配置自动扫描的包 -->
    <context:component-scan base-package="zzc.spring.hibernate"/>
    
    <!-- 配置数据源 -->
    <!-- 导入资源文件 -->
    <context:property-placeholder location="classpath:db.properties"/>

    <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
        <property name="user" value="${jdbc.user}"/>
        <property name="password" value="${jdbc.password}"/>
        <property name="jdbcUrl" value="${jdbc.jdbcUrl}"/>
        <property name="driverClass" value="${jdbc.driverClass}"/>

        <property name="initialPoolSize" value="${jdbc.initPoolSize}"/>
        <property name="maxPoolSize" value="${jdbc.maxPoolSize}"/>
    </bean>

    <!-- 配置Hibernate的SessionFactory实例：通过Spring提供的LocalSessionFactoryBean进行配置 -->
    <bean id="sessionFactory" class="org.springframework.orm.hibernate5.LocalSessionFactoryBean">
        <!-- 配置数据源属性 -->
        <property name="dataSource" ref="dataSource"/>
        <!-- 配置Hibernate配置文件的位置及名称 -->
        <property name="configLocation" value="classpath:hibernate.cfg.xml"/>
        <!-- 配置Hibernate映射文件的位置及名称，可以使用通配符 -->
        <property name="mappingLocations" value="zzc/spring/hibernate/entities/*.hbm.xml"/>
    </bean>

    <!-- 配置Spring的声明式事务 -->
    <!-- 1、配置事务管理器 -->
    <bean id="transactionManager" class="org.springframework.orm.hibernate5.HibernateTransactionManager">
        <property name="sessionFactory" ref="sessionFactory"/>
    </bean>

    <!-- 2、配置事务属性 -->
    <tx:advice id="txAdvice" transaction-manager="transactionManager">
        <tx:attributes>
            <tx:method name="get*" read-only="true"/>
            <tx:method name="*"/>
        </tx:attributes>
    </tx:advice>

    <!-- 3、配置事务切点，并把切点和事务属性关联起来-->
    <aop:config>
        <aop:pointcut id="txPointcut" expression="execution(* zzc.spring.hibernate.service.*.*(..))"/>
        <aop:advisor advice-ref="txAdvice" pointcut-ref="txPointcut"/>
    </aop:config>
</beans>
```

## 3､整合