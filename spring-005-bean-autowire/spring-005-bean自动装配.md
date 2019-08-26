# spring自动装配

Spring IOC容器可以自动装配Bean，需要做的仅仅是**在<bean>的autowire属性里指定自动装配的模式**

**byType**(根据类型自动装配)：若IOC容器中有多个与目标Bean类型一致的Bean，在这种情况下，Spring将无法判定哪个Bean最合适该属性，所以不能执行自动装配

**byName**(根据名称自动装配)：必须将目标Bean的名称和属性名设置的完全相同

constructor(通过构造器自动装配)：当Bean中存在多个构造器时，此种自动装配方式将会很复杂，**不推荐使用**

**XML配置里的Bean自动装配的缺点**：

在Bean配置文件里设置autowire属性进行自动装配将会装配Bean的所有属性，然而，若只希望装配个别属性时，autowire属性就不够灵活了

autowire属性要么根据类型自动装配，要么根据名称自动装配，不能两者兼而有之

一般情况下，在实际的项目中很少使用自动装配功能，因为和自动装配功能所带来的好处比起来，明确清晰的配置文档更有说服务力一些

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
    
    // getter/setter...
}
```

```java
public class Person {
	private String name;
	private Address address;
	private Car car;
    
    // getter/setter...
}
```

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:p="http://www.springframework.org/schema/p"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="address" class="zzc.spring.beans.autowire.Address" p:city="BeiJing" p:street="HuiLongGuan"/>

    <bean id="address2" class="zzc.spring.beans.autowire.Address" p:city="ShangHai" p:street="ZhangJiangLu"/>

    <bean id="car" class="zzc.spring.beans.autowire.Car" p:brand="Audi" p:price="300000"/>

    <!--
    <bean id="person" class="zzc.spring.beans.autowire.Person" p:name="ZhaoZhiCheng" p:address-ref="address" p:cat-ref="car"/>
    -->

    <!-- 可以使用autowire属性指定自动装配的方式，
        byName根据bean的名字和当前bean的setter风格的属性名进行自动装配，若有匹配的，则进行自动装配，若没有匹配，则不装配
        byType根据bean的类型和当前bean的属性的类型进行自动装配.若IOC容器中有1个以上相同类型的匹配，则抛异常
     -->
    <bean id="person" class="zzc.spring.beans.autowire.Person" p:name="ZhaoZhiCheng" autowire="byName"/>

</beans>
```

```java
@Test
public void testPerson() {
    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    Person person = (Person) ctx.getBean("person");
    System.out.println(person);
}
```

