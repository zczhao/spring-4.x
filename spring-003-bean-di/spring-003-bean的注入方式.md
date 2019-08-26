# Spring Bean的注入方式

## 1､属性注入 

属性注入即通过**setter方法**注入bean的属性值或依赖的对象

属性注入使用<property>元素,使用name属性指定Bean的属性名称，value属性或<value>子节点指定属性值

**属性注入是实际应用中最常用的注入方式**

```java
package zzc.spring.beans;

public class HelloWorld {

	public HelloWorld() {
		System.out.println("HelloWorld's Constructor...");
	}

	private String name;

	public void setName(String name) {
		System.out.println("setName： " + name);
		this.name = name;
	}

	public void hello() {
		System.out.println("hello：" + name);
	}
}
```

```xml
 <!--
    通过全类名的方式来配置bean：

    id：Bean的名称
    在IOC容器中必须是唯一的
    若id没有指定，Spring自动将权限定性类名作为bean的名字
    id可以指定多个名字，名字之间可用逗号、分号、或空格分隔

    class：bean的全类名，通过反射的方式在IOC容器中创建Bean,所以要求bean中必须有无参数的构造器
    -->
<bean id="helloWorld" class="zzc.spring.beans.HelloWorld">
    <property name="name" value="Spring"/>
</bean>
```

```java
@Test
public void testHelloWorld(){
    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    HelloWorld helloWorld = (HelloWorld) ctx.getBean("helloWorld");
    helloWorld.hello();
}
```



## 2､构造注入

通过构造方法注入bean的属性值或依赖的对象，它保证了Bean实例在实例化后就可以使用

构造器注入在<constructor-arg>元素里声明属性，

**<constructor-arg>中没有name属性**

```java
public class Car {
	private String brand;
	private String corp;
	private double price;
	private int maxSpeed;

	public Car(String brand, String corp, double price) {
		this.brand = brand;
		this.corp = corp;
		this.price = price;
	}

	public Car(String brand, String corp, int maxSpeed) {
		this.brand = brand;
		this.corp = corp;
		this.maxSpeed = maxSpeed;
	}
}
```

applicationContext.xml

```xml
<!-- 通过构造方法来配置bean的属性 -->
<bean id="car" class="zzc.spring.beans.Car">
    <constructor-arg value="AUdi" ></constructor-arg>
    <constructor-arg value="ShangHai" ></constructor-arg>
    <constructor-arg value="300000" ></constructor-arg>
</bean>
```

```xml
<!-- 通过构造方法来配置bean的属性 -->
<bean id="car" class="zzc.spring.beans.Car">
    <!-- 通过下标指定属性的值 -->
    <constructor-arg value="AUdi" index="0"></constructor-arg>
    <constructor-arg value="ShangHai" index="1"></constructor-arg>
    <constructor-arg value="300000" index="2"></constructor-arg>
</bean>
```

```xml
<!-- 使用构造器注入属性值可以指定参数位置和参数类型，以区分重载的构造器 -->
<bean id="car" class="zzc.spring.beans.Car">
    <constructor-arg value="AUdi" index="0"></constructor-arg>
    <constructor-arg value="ShangHai" index="1"></constructor-arg>
    <constructor-arg value="300000" type="double"></constructor-arg>
</bean>

<bean id="car2" class="zzc.spring.beans.Car">
    <constructor-arg value="Baoma" type="java.lang.String"></constructor-arg>
    <constructor-arg value="ShangHai" type="java.lang.String"></constructor-arg>
    <constructor-arg value="240" type="int"></constructor-arg>
</bean>
```

```xml
<!-- 使用构造器注入属性值可以指定参数名和参数值，以区分重载的构造器 -->
<bean id="car" class="zzc.spring.beans.Car">
    <constructor-arg value="AUdi" name="brand"></constructor-arg>
    <constructor-arg value="ShangHai" name="corp"></constructor-arg>
    <constructor-arg value="300000" name="price"></constructor-arg>
</bean>

<bean id="car2" class="zzc.spring.beans.Car">
    <constructor-arg value="Baoma" name="brand"></constructor-arg>
    <constructor-arg value="ShangHai" name="corp"></constructor-arg>
    <constructor-arg value="240" name="maxSpeed"></constructor-arg>
</bean>
```

```java
@Test
public void testCar(){
    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    Car car = (Car) ctx.getBean("car");
    System.out.println(car);
}

@Test
public void testCar2(){
    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    Car car = (Car) ctx.getBean("car2");
    System.out.println(car);
}
```

## 3､工厂方法注入(很少使用，不推荐)

## 4､注入属性值细节

### 	注入属性值：

​		字面值：可以用字符串表示的值，可以通过<value>元素标签或value属性进行注入	

​		基本数据类型及其封装类、String等类型都可以采用字面值注入的方式

​		若字面值中包含特殊字符，可以使用<![CDATA[]]>把字面值包裹起来

```xml
<bean id="car2" class="zzc.spring.beans.Car">
    <constructor-arg value="Baoma" name="brand"></constructor-arg>
    <!-- 如果字面值包含特殊字符可以使用<![CDATA[]]> 包裹起来 -->
    <!-- 属性值也可以使用value子节点进行配置 -->
    <constructor-arg name="corp">
        <value><![CDATA[<ShangHai>]]></value>
    </constructor-arg>
    <constructor-arg name="maxSpeed">
        <value>250</value>
    </constructor-arg>
</bean>
```

## 5､引用其他Bean

​	组成应用程序的bean经常需要相互协作以完成应用程序的功能，要**使Bean能够相互访问**，就必须在Bean配置文件中指定对Bean的引用

​	在bean的配置文件中，可以**通过<ref>元素或ref属性**为bean的属性或构造器参数指定对bean的引用

```java
public class Person {
	private String name;
	private int age;
	private Car car;
	
    // getter/setter...
}
```

```java
public class Car {
	private String brand;
	private String corp;
	private double price;
	private int maxSpeed;

    // getter/setter...
}
```

```xml
<bean id="car2" class="zzc.spring.beans.Car">
    <constructor-arg value="Baoma" name="brand"></constructor-arg>
    <constructor-arg name="corp">
        <value><![CDATA[<ShangHai>]]></value>
    </constructor-arg>
    <constructor-arg name="maxSpeed">
        <value>250</value>
    </constructor-arg>
</bean>

<bean id="person" class="zzc.spring.beans.Person">
    <property name="name" value="ZhaoZhiCheng"/>
    <property name="age" value="24"/>
    <!-- 可以使用的property的ref属性建立 bean之间的引用关系 -->
    <property name="car" ref="car2"/>
    <!--
    <property name="car">
		<ref bean="car2"/>
    </property>
	-->
</bean>
```

## 6､使用内部Bean

```xml
<bean id="person" class="zzc.spring.beans.Person">
    <property name="name" value="ZhaoZhiCheng"/>
    <property name="age" value="24"/>
    <property name="car">
        <!-- 内部bean，不能被外部引用，只能在内部使用 -->
        <bean class="zzc.spring.beans.Car">
            <constructor-arg value="Ford" name="brand"></constructor-arg>
            <constructor-arg value="ChangAn" name="corp"></constructor-arg>
            <constructor-arg value="300000" name="price"></constructor-arg>
        </bean>
    </property>
</bean>    
```

```java
@Test
public void testPerson() {
    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    Person person = (Person) ctx.getBean("person");
    System.out.println(person);
}
```

## 7､null值和级联属性

​	可以使用专用的<null>元素标签为bean的字符串或其他对象类型的属性注入null值

```xml
<bean id="person2" class="zzc.spring.beans.Person">
    <constructor-arg value="Knight"/>
    <constructor-arg value="18"/>
    <!--<constructor-arg ref="car"/>-->
    <!-- 测试赋值null -->
    <constructor-arg index="2">
        <null/>
    </constructor-arg>
</bean>
```

​	和Struts、Hibernate等框架一样，**Spring支持级联属性的配置

```xml
 <bean id="car" class="zzc.spring.beans.Car">
     <constructor-arg value="AUdi" name="brand"></constructor-arg>
     <constructor-arg value="ShangHai" name="corp"></constructor-arg>
     <constructor-arg value="300000" name="price"></constructor-arg>
</bean>

<bean id="person2" class="zzc.spring.beans.Person">
     <constructor-arg value="Knight"/>
     <constructor-arg value="18"/>
     <constructor-arg ref="car"/>
     <!-- 为级联属性赋值，注意：属性需要先初始化后才可以为级联属性赋值，否则会有异常，和Struts2不同 -->
     <property name="car.maxSpeed" value="200"/>
</bean>
```

