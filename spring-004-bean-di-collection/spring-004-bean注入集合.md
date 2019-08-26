# Spring Bean集合注入

## 1､List注入

在Spring中可以通过一组内置的xml标签(如：<list>、<set>、<map>)来配置集合属性

配置java.util.List类型的属性，需要指定<list>标签，在标签里包含一些元素，这样标签可以通过<value>指定简单的常量值，通过<ref>指定对其他Bean的引用，通过<bean>指定内置Bean定义，通过<null/>指定空元素，甚至可以内嵌其他集合

```java
public class Car {
	private String brand;
	private String corp;
	private double price;
	private int maxSpeed;
	
    // getter/setter...
}
```

```java
public class Person {
	private String name;
	private int age;
	private List<Car> cars;
	
    // getter/setter...
}
```

```xml
<!-- 测试如何配置集合属性-->
<bean id="car3" class="zzc.spring.beans.collections.Car">
    <property value="AUdi" name="brand"/>
    <property value="ShangHai" name="corp"/>
    <property value="300000" name="price"/>
</bean>

<bean id="car4" class="zzc.spring.beans.collections.Car">
    <property value="Baoma" name="brand"/>
    <property value="ShangHai" name="corp"/>
    <property value="500000" name="price"/>
</bean>

<bean id="person3" class="zzc.spring.beans.collections.Person">
    <property name="name" value="ZhaoZhiCheng"/>
    <property name="age" value="18"/>
    <property name="cars">
        <!-- 使用list节点为 list类型的属性赋值 -->
        <list>
            <ref bean="car3"/>
            <ref bean="car4"/>
        </list>
    </property>
</bean>
```

```java
@Test
public void testPerson() {
    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    Person person = (Person) ctx.getBean("person3");
    System.out.println(person);
}
```

## 2､数组注入

数组的定义和list一样，都使用<list>

## 3､Set注入

配置java.util.set需要使用<set>标签，定义元素的方法与List一样

## 4､Map注入

java.util.Map通过<map>标签里可以使用多个<entry>作为子标签，每个条目包含一个键和一个值

必须在<key>标签里定义键

因为键和值的类型没有限制，所心可以自由地为它们指定<value>、<ref>、<bean>、<null>元素

可以将Map的键和值作为<entry>的属性定义：简单常量使用key和value来定义，Bean引用通过key-ref和value-ref属性定义

```java
public class NewPerson {
	private String name;
	private int age;
	private Map<String, Car> cars;
	
    // getter/setter...
}
```

```xml
<!-- 配置Map属性值 -->
<bean id="newPerson" class="zzc.spring.beans.collections.NewPerson">
    <property name="name" value="ZhaoZhiCheng"/>
    <property name="age" value="18"/>
    <property name="cars">
        <!-- 使用map节点及map的entry子节点配置 map类型的成员变量 -->
        <map>
            <entry key="AA" value-ref="car3"/>
            <entry key="BB" value-ref="car4"/>
        </map>
    </property>
</bean>
```

```java
@Test
public void testNewPerson() {
    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    NewPerson person = (NewPerson) ctx.getBean("newPerson");
    System.out.println(person);
}
```

## 5､Properties注入

使用<props>定义java.util.Properties，该标签使用多个<prop>作为子标签，每个<prop>标签必须定义key属性

```java
public class DataSource {
	private Properties properties;
    
    // getter/setter
}
```

```xml
<!-- 配置Properties 属性值 -->
<bean id="dataSource" class="zzc.spring.beans.collections.DataSource">
    <property name="properties">
        <!-- 使用props和prop子节点来为Properties属性赋值 -->
        <props>
            <prop key="user">root</prop>
            <prop key="password">1234</prop>
            <prop key="jdbcUrl">jdbc:mysql:///test</prop>
            <prop key="driverClass">com.mysql.jdbc.Driver</prop>
        </props>
    </property>
</bean>
```

```java
@Test
public void testPerson() {
    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    DataSource dataSource = (DataSource) ctx.getBean("dataSource");
    System.out.println(dataSource);
}
```

## 6､使用util命名空间定义集合

**使用utility scheme定义集合**：

使用基本的标签定义集合时，**不能将集合作为独立的Bean定义，导致其他Bean无法引用该集合，所以无法在不同Bean之间共享集合**

可以使用util schema里的集合标签定义独立的集合Bean，需要注意的是，必须在<beans>根元素j里添加 util schema定义 

```java
public class Person {
	private String name;
	private int age;
	private List<Car> cars;
	
    // getter/setter...
}
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:util="http://www.springframework.org/schema/util"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
      http://www.springframework.org/schema/util http://www.springframework.org/schema/util/spring-util-4.0.xsd">
    <bean id="car3" class="zzc.spring.beans.collections.Car">
        <property value="AUdi" name="brand"/>
        <property value="ShangHai" name="corp"/>
        <property value="300000" name="price"/>
    </bean>

    <bean id="car4" class="zzc.spring.beans.collections.Car">
        <property value="Baoma" name="brand"/>
        <property value="ShangHai" name="corp"/>
        <property value="500000" name="price"/>
    </bean>
   
    <!-- 配置单例的集合bean，以供多个bean 进行引用,需要导入util命名空间 -->
    <util:list id="cars">
        <ref bean="car3"/>
        <ref bean="car4"/>
    </util:list>

    <bean id="person4" class="zzc.spring.beans.collections.Person">
        <property name="name" value="ZhaoZhiCheng"/>
        <property name="age" value="18"/>
        <property name="cars" ref="cars"/>
    </bean>
</beans> 
```

```java
@Test
public void testPerson4() {
    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    Person person = (Person) ctx.getBean("person4");
    System.out.println(person);
}
```

## 7､使用p命名空间

为了简化XML文件的配置，越来越多的XML文件采用属性而非子元素配置信息

Spring从2.5版本开始引入了一个新的p命名空间，可以通过<bean>元素属性的方式配置bean的属性

使用p命名空间后，基于XML的配置方式将进一步简化

```java
public class Person {
	private String name;
	private int age;
	private List<Car> cars;
	
    // getter/setter...
}
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:util="http://www.springframework.org/schema/util"
       xmlns:p="http://www.springframework.org/schema/p"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
      http://www.springframework.org/schema/util http://www.springframework.org/schema/util/spring-util-4.0.xsd">
    
    <bean id="car3" class="zzc.spring.beans.collections.Car">
        <property value="AUdi" name="brand"/>
        <property value="ShangHai" name="corp"/>
        <property value="300000" name="price"/>
    </bean>

    <bean id="car4" class="zzc.spring.beans.collections.Car">
        <property value="Baoma" name="brand"/>
        <property value="ShangHai" name="corp"/>
        <property value="500000" name="price"/>
    </bean>
    
    <util:list id="cars">
        <ref bean="car3"/>
        <ref bean="car4"/>
    </util:list>
    
     <!-- 通过p命名空间为bean的属性赋值，需要先导入p命名空间,相对于传统的方式更加的简洁 -->
    <bean id="person5" class="zzc.spring.beans.collections.Person" p:name="ZhaoZhiCheng" p:age="18" p:cars-ref="cars"/>
</beans>
```

```java
@Test
public void testPerson4() {
    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    Person person = (Person) ctx.getBean("person5");
    System.out.println(person);
}
```

