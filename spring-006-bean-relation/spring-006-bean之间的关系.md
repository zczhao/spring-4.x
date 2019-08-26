# Spring Bean之间的关系

## 1､继承bean配置

**Spring允许继承bean的配置**，被继承的bean称为父bean，继承这个父Bean的Bean称为子Bean

**子bean从父bean中继承配置，包括bean的属性配置**

子bean也可以**覆盖**从父bean继承过来的配置

```java
public class Address {
	private String city;
	private String street;
    
    // getter/setter...
}
```

```xml
<!--
    <bean id="address" class="zzc.spring.beans.Address" p:city="ShangHai" p:street="WuDaoKou"/>

    <bean id="address2" class="zzc.spring.beans.Address" p:city="ShangHai" p:street="ZhangJiangLu"/>
    -->

<bean id="address" class="zzc.spring.beans.Address" p:city="ShangHai^" p:street="WuDaoKou"/>

<!-- bean配置的继承：使用bean的parent属性指定哪个bean的配置 -->
<bean id="address2"  p:street="ZhangJiangLu" parent="address"/>
```

```java
@Test
public void testAddress(){
    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    Address address = (Address) ctx.getBean("address");
    System.out.println(address);

    address = (Address) ctx.getBean("address2");
    System.out.println(address);
}
```

**父bean可以作为配置模板，可以设置<bean>的abstract属性为true**，这样Spring将不会实例化这个bean

```xml
<!-- 抽象bean：bean的属性abstract属性为true的bean，这样的bean不能被IOC容器实例化，只用来被继承配置 -->
<bean id="address" class="zzc.spring.beans.Address" p:city="ShangHai^" p:street="WuDaoKou" abstract="true"/>

<!-- bean配置的继承：使用bean的parent属性指定哪个bean的配置 -->
<bean id="address2"  p:street="ZhangJiangLu" parent="address"/>
```

```java
@Test
public void testAddress(){
    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    //Address address = (Address) ctx.getBean("address");
    //System.out.println(address);

    Address address = (Address) ctx.getBean("address2");
    System.out.println(address);
}
```

**并不是<bean>元素里的所有元素都会被继承**，比如：autowire、abstract等

也**可以忽略父bean的class属性**，让子bean指定自己的类，而共享相同的属性配置，但此时**==abstract必须为true==**

```xml
 <!-- 抽象bean：bean的属性abstract属性为true的bean，这样的bean不能被IOC容器实例化，只用来被继承配置，若某一个bean的class属性没有指定，则该bean必须是一个抽象bean -->
<bean id="address" p:city="ShangHai^" p:street="WuDaoKou" abstract="true"/>

<!-- bean配置的继承：使用bean的parent属性指定哪个bean的配置 -->
<bean id="address2" class="zzc.spring.beans.Address"  p:street="ZhangJiangLu" parent="address"/>
```

## 2､依赖bean配置

**Spring充许用户通过depends-on属性设定bean前置依赖的bean**，前置依赖的bean会在本bean实例化之前创建好

**如果前置依赖多个bean，则可以通过逗号，空格或的方式配置bean的名称**

```xml
<!-- bean配置的继承：使用bean的parent属性指定哪个bean的配置 -->
<bean id="address2" p:street="ZhangJiangLu" parent="address"/>

<bean id="car" class="zzc.spring.beans.Car" p:brand="Audi" p:price="300000"/>

<!-- 要求在配置Person时必须有一个关联的car! 换句话说person这个bean依赖于Car这个bean -->
<bean id="person" class="zzc.spring.beans.Person" p:name="ZhaoZhiCheng" p:address-ref="address2" depends-on="car"/>
```

