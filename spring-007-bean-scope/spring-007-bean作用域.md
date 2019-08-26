# Spring Bean作用域

## 1､singleton

```java
public class Car {
	private String brand;
	private double price;

	public Car() {
		System.out.println("Car's Constructor... ");
	}
    
    // getter/setter...
}
```

```xml
<!--
        使用bean的 scope属性来配置 bean的作用域
        singleton：默认值，容器初始化时创建bean实例，在整个容器的生命周期内只创建这一个bean，单例的
        prototype：原型的，容器初始化时不创建bean实例，而在每次请求时都创建一个新新的bean实例，并返回
    -->
<bean id="car" class="zzc.spring.beans.Car" scope="prototype">
    <property name="brand" value="Audi"/>
    <property name="price" value="300000"/>
</bean>
```

```java
@Test
public void testCar() {
    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    Car car = (Car) ctx.getBean("car");
    Car car2 = (Car) ctx.getBean("car");
    System.out.println(car == car2);
}
```



## 2､prototype

## 3､WEB环境作用域