# Spring表达式语言：spEL

**Spring表达式语言**(简称**spEL**)：是一个**支持运行时查询和操作对象图的强大的表达式语言**

**语法类似EL：spEL使用#{…}作为界定符，所有在大框号中的字符都将被认为是spEL**

**spEL为bean的属性进行动态赋值提供了便利**

通过spEL可以实现:

​	通过bean的id对bean进行引用

​	调用方法以及引用对象中的属性

​	计算表达式的值

​	正则表达式的匹配

## spEL字面量

​	字面量的表示：

整数：

```xml
<property name="count" value="#{5}"/>
```

小数：

```xml
<property name="frequency" value="#{89.7}"/>
```

科学计数法：

```xml
<property name="capacity" value="#{1e4}"/>
```

String可以使用单引号或者双引号作为字符串的定界符号：

```xml
<property name="name" value="#{'Spring'}"/>
<property name='name' value='#{"Spring"}'/>
```

Boolean：

```xml
<property name='enabled' value='#{false}'/>
```

## spEL：引用Bean、属性和方法

引用其他对象：

```xml
<!-- 通过value属性和spEL配置bean之间的应用关系 -->
<property name="prefix" value="#{prefixGenerator}"/>
```

引用其他对象的属性：

```xml
<!-- 通过value属性和spEL配置suffix属性值为另一个bean的suffix属性值 -->
<property name="suffix" value="#{sequenceGenerator2.suffix}"/>
```

调用其他方法，还可以链式操作：

```xml
<!-- 通过value属性和spEL配置suffix属性值为另一个bean的方法的返回值 -->
<property name="suffix" value="#{sequenceGenerator2.toString()}"/>
<!-- 方法链式调用 -->
<property name="suffix" value="#{sequenceGenerator2.toString().toUpperCase()}"/>
```

**调用静态方法或静态属性**：通过**T()**调用一个类的静态方法，它将返回一个Class Object，然后再调用相应的方法或属性：

```xml
<property name="initValue" value="#{T(java.lang.Math).PI}"/>
```

## spEL支持的运算符号

算数运算符：+、-、*、/、%、^:

```xml
<!-- +运算符：两个数字相加 -->
<property name="adjustedAmount" value="#{counter.total + 42}"/>
<!-- +运算符：用于连接字符串 -->
<property name="fullName" value="#{performer.firstName + ' ' + performer.lastName}"/>
<!-- -运算符：两个数字相减 -->
<property name="adjustedAmount" value="#{counter.total - 20}"/>
<!-- *运算符：乘法运算 -->
<property name="circumference" value="#{2 * T(java.lang.Math).PI * circle.radius}"/>
<!-- /运算符：除法运算 -->
<property name="average" value="#{counter.total / counter.count}"/>
<!-- %运算符：求余运算 -->
<property name="remainder" value="#{counter.total % counter.count}"/>
<!-- ^运算符：乘方运算 -->
<property name="area" value="#{T(java.lang.Math).PI * circle.radius ^ 2}"/>
```

比较运算符：<、>、==、<=、>=、lt、gt、eq、le、ge：

```xml
<!-- 假设equal属性为布尔属性 -->
<property name="equal" value="#{counter.total == 100}"/>
```

逻辑运算符：and、or、not、|

```xml
<!-- and 运算符 -->
<property name="largeCircle" value="#{shape.kind == 'circle' and shape.perimeter gt 10000}"/>
<!-- ! 运算符 -->
<property name="outOfStock" value="#{!product.availiable}"/>
<!-- not 运算符 -->
<property name="outOfStock" value="#{not product.availiable}"/>
```

If-else运算符：?:(ternary)、?:(Elvis)

```xml
<!-- ?:三元运算符 -->
<property name="song" value="#{kenny.song != null ? kenny.song : 'Greensleeves'}"/>
<!--如果kenny.song值为空，则赋值kenny.song ，否则赋值’Greensleeves’。这里’Greensleeves’的引用重复两次，可简化表达式如下：-->
<property name="song" value="#{kenny.song != null ? 'Greensleeves'}"/>
```

正则表达式：matches

```xml
<!-- 判断一个字符串是否是有效的邮件地址 -->
<property name="validEmail" value="#{admin.email matches '[a-zA-Z0-9.-%+-]+@[a-zA-Z0-9.-]+\\.com'}"/>
```

例子：

```java
public class Address {
	private String city;
	private String street;
    
    // getter/setter...
}
```

```java
public class Car {
	private String brand;
	private double price;

	// 轮胎的周长
	private double tyrePerimeter;
    
    // getter/setter...
}
```

```java
public class Person {
	private String name;
	private Car car;

	// 引用Address bean的 city属性
	private String city;

	// 根据car的price 确认info: car的 pirce <= 300000:金领 否则为白领
	private String info;
    
    // getter/setter...
}
```

```xml
<bean id="address" class="zzc.spring.beans.Address">
    <!-- 使用 spEL 为属性赋一个字面值 -->
    <property name="city" value="#{'BeiJing'}"/>
    <property name="street" value="WuDaoKou"/>
</bean>

<bean id="car" class="zzc.spring.beans.Car">
    <property name="brand" value="Audi"/>
    <property name="price" value="500000"/>
    <!-- 使用spEl引用类的静态属性 -->
    <property name="tyrePerimeter" value="#{T(java.lang.Math).PI * 80}"/>
</bean>

<bean id="person" class="zzc.spring.beans.Person">
    <!-- 使用spEL来引用其他的bean -->
    <property name="car" value="#{car}"/>
    <!-- 使用spEL来引用其他的bean的属性 -->
    <property name="city" value="#{address.city}"/>
    <!-- 在spEL中使用运算符 -->
    <property name="info" value="#{car.price > 300000 ? '金领' : '白领'}"/>
    <property name="name" value="ZhaoZhiCheng"/>
</bean>
```

```java
@Test
public void testAddress(){
    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    Address address = (Address) ctx.getBean("address");
    System.out.println(address);
}

@Test
public void testCar(){
    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    Car car = (Car) ctx.getBean("car");
    System.out.println(car);
}

@Test
public void testCar(){
    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    Person person = (Person) ctx.getBean("person");
    System.out.println(person);
}
```



