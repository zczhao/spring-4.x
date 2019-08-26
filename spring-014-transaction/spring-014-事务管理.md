# 事务简介

事务管理是企业级应用程序开发中必不可少的技术，**用来确保数据的完整性和一致性**

事务就是一系列的动作，它们被当做一个单独的工作单元，这些动作要么全部完成，要么全部不起作用

事务的四个关键属性(**ACID**)：
​	原子性(atomicity)：事务是一个原子操作,由一系列动作组成,事务的原子性确保动作要么全部完成要么完全不起作用
​	一致性(consistency)：一旦所有事务动作完成,事务就提交,数据和资源就处于一种满足业务规则的一致性状态中
​	隔离性(isolation)：可能有许多事务会同时处理相同的数据,因此每个事务都应该与其他事务隔离开来,防止数据损坏
​	持久性(durability)：一旦事务提交，无论发生什么系统错误,它的结果都不应该受影响,通常情况下,事务的结果被写到持久化存储器中

# Spring中事务管理

作为企业级应用程序框架，**Spring在不同的事务管理API之上定义了一个抽象层**，而应用程序开发人员不必了解底层的事务管理API，就可以使用Spring的事务管理机制

Spring即支持编程式事务管理，也支持声明式的事务管理

**编程式事务处理：将事务管理代码嵌入到业务方法中来控制事务的提交和回滚**。在编程式管理事务时，必须在每个事务操作中包含额外的事务管理代码

**声明式事务管理**：大多数情况下比编程式事务管理更好用，它**将事务管理代码从业务方法中分离出来，以声明的方式来实现事务管理**，事务管理作为一种横切关注点，可以通过AOP方法模块化，**Spring通过Spring AOP框架支持声明式事务管理**

# Spring中的事务管理器

Spring从不同的事务管理API中抽象了一整套的事务机制，开发人员不必了解底层的事务API，就可以处理这些事务机制。有了这些事务机制，事务管理代码就能独立于特定的事务技术了

Spring的核心事务管理抽象是org.springframework.transaction.PlatformTransactionManager管理封装了一组独立于技术的方法，无论使用Spring的哪种事务管理策略(编程式或声明式)，事务管理器都是必须的

# Spring中的事务管理器的不同实现

org.springframework.jdbc.datasource.DataSourceTransactionManager：在应用程序中需要处理一个数据源，而且通过JDBC存取

org.springframework.transaction.jta.JtaTransactionManager：在JavaEE应用服务器上用JTA(Java Transaction API)进行事务管理

......

事务管理器以普通的bean形式声明在Spring IOC容器中

```sql
CREATE TABLE `account` (
  `username` varchar(255) DEFAULT NULL,
  `balance` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `book` (
  `isbn` varchar(11) DEFAULT NULL,
  `book_name` varchar(255) DEFAULT NULL,
  `price` int(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `book_stock` (
  `isbn` varchar(11) DEFAULT NULL,
  `stock` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `book`(`isbn`, `book_name`, `price`) VALUES ('1001', 'Java', 100);
INSERT INTO `book`(`isbn`, `book_name`, `price`) VALUES ('1002', 'Oracel', 70);

INSERT INTO `account`(`username`, `balance`) VALUES ('AA', 160);

INSERT INTO `book_stock`(`isbn`, `stock`) VALUES ('1001', 4);
INSERT INTO `book_stock`(`isbn`, `stock`) VALUES ('1002', 8);
```

```java
public interface BookShopDao {

	//根据书号获取书的单价
	int findBookPriceByIsbn(String isbn);

	//更新书的库存,使书号对应的库存-1
	void updateBookStock(String isbn);

	//更新用户的账户余额,使用username 的 balance - price 
	void updateUserAccount(String username, int price);
}
```

```java
@Repository("bookShopDao")
public class BookShopDaoImpl implements BookShopDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public int findBookPriceByIsbn(String isbn) {
		String sql = "SELECT price FROM book WHERE isbn = ?";
		return jdbcTemplate.queryForObject(sql, Integer.class, isbn);
	}

	public void updateBookStock(String isbn) {
		//检查书的库存是否足够
		String sql2 = "SELECT stock FROM book_stock where isbn = ?";
		int stock = jdbcTemplate.queryForObject(sql2, Integer.class, isbn);
		if (stock == 0) {
			throw new BookStockExcetion("库存不足!");
		}
		String sql = "UPDATE book_stock SET stock = stock - 1 WHERE isbn = ?";
		jdbcTemplate.update(sql, isbn);
	}

	public void updateUserAccount(String username, int price) {
		String sql2 = "SELECT balance FROM account where username = ?";
		int balance = jdbcTemplate.queryForObject(sql2, Integer.class, username);
		if (balance < price) {
			throw new UserAccountException("余额不足!");
		}
		String sql = "UPDATE account SET balance = balance - ? WHERE username = ?";
		jdbcTemplate.update(sql, price, username);
	}

}
```

```java
public interface BookShopService {

	void purchase(String username, String isbn);
}
```

```java
@Service("bookShopService")
public class BookShopServiceImpl implements BookShopService {

	@Autowired
	private BookShopDao bookShopDao;

	//添加事务注解
	@Transactional
	public void purchase(String username, String isbn) {
		//获取书单价
		int price = bookShopDao.findBookPriceByIsbn(isbn);
		//更新书的库存
		bookShopDao.updateBookStock(isbn);
		//更新用户余额
		bookShopDao.updateUserAccount(username, price);
	}

}
```

```java
public class BookStockExcetion extends RuntimeException {

	public BookStockExcetion() {
		super();
	}

	public BookStockExcetion(String message, Throwable cause) {
		super(message, cause);
	}

	public BookStockExcetion(String message) {
		super(message);
	}

	public BookStockExcetion(Throwable cause) {
		super(cause);
	}
	
}
```

```java
public class UserAccountException extends RuntimeException {

	public UserAccountException() {
		super();
	}

	public UserAccountException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserAccountException(String message) {
		super(message);
	}

	public UserAccountException(Throwable cause) {
		super(cause);
	}

}
```

```properties
jdbc.user=admin
jdbc.password=richard
jdbc.driverClass=com.mysql.jdbc.Driver
jdbc.jdbcUrl=jdbc:mysql://10.10.10.141/test1

jdbc.initPoolSize=5
jdbc.maxPoolSize=10
```

```xml
<context:component-scan base-package="zzc.spring.tx"/>

<!-- 导入属性文件 -->
<context:property-placeholder location="classpath:db.properties"/>

<!-- 配置c3p0数据源 -->
<bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
    <property name="user" value="${jdbc.user}"/>
    <property name="password" value="${jdbc.password}"/>
    <property name="jdbcUrl" value="${jdbc.jdbcUrl}"/>
    <property name="driverClass" value="${jdbc.driverClass}"/>

    <property name="initialPoolSize" value="${jdbc.initPoolSize}"/>
    <property name="maxPoolSize" value="${jdbc.maxPoolSize}"/>
</bean>

<!-- 配置Spring的JDBCTemplate -->
<bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
    <property name="dataSource" ref="dataSource"/>
</bean>

<!-- 配置事务管理器 -->
<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
    <property name="dataSource" ref="dataSource"/>
</bean>

<!-- 启用事务注解 transaction-manager的值为 transactionManager 可省略 -->
<tx:annotation-driven transaction-manager="transactionManager"/>
```

```java
package zzc.spring.tx;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/spring-tx.xml")
public class SpringTransactionTests {

	@Autowired
	private BookShopDao bookShopDao;

	@Autowired
	private BookShopService bookShopService;
    
	@Test
	public void testFindBookPriceByIsbn(){
		System.out.println(bookShopDao.findBookPriceByIsbn("1001"));
	}

	@Test
	public void testUpdateBookStock(){
		bookShopDao.updateBookStock("1001");
	}

	@Test
	public void testUpdateUserAccount(){
		bookShopDao.updateUserAccount("AA",200);
	}

	@Test
	public void testPurchase(){
		bookShopService.purchase("AA", "1001");
	}
}
```

# 事务传播属性

​	当事务方法被另一个事务方法调用时,必须指定事务应该如何传播(例如：方法可能继续在现有事务中运行,也可能开启一个新事务,并在自己的事务中运行)
​	事务的传播行为可以由传播属性指定,spring定义了7种类传播行为：
​		required(默认)：如果有事务在运行,当前方法就在这个事务内运行,否则,就启动一个新的事务,并在自己的事务内运行
​		required_new：当前的方法必须启动新事务,并在自己的事务内运行,如果有事务正在运行,应该将它挂起
​		supports：如果有事务正在运行,当前的方法就在这个事务内运行,否则它可以不运行在事务中
​		not_supported：当前方法不应该在事务中,如果在运行的事务,将它挂起
​		mandatory：当前方法必须运行在事务内部,如果没有正在运行的事务,将会抛出异常
​		never：当前的方法不应该运行在事务中,如果有运行的事务,将会抛出异常
​		nested：如果有事务在运行,当前的方法就应该在这个事务的嵌套事务内运行,否则,就启动一个新的事务,并在自己的事务内运行

```java
package zzc.spring.tx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("bookShopService")
public class BookShopServiceImpl implements BookShopService {

	@Autowired
	private BookShopDao bookShopDao;

	/**
	 * 添加事务注解
	 *
	 * 1.使用propagation 指定事务的传播行为,即当前的事务方法被另外一个事务方法调用时
	 * 	如何使用事务,默认取值为REQUIRED,即使用调用方法的事务
	 * 	REQUIRES_NEW:使用自己的事务,调用的事务方法的事务被挂起
	 *
	 * 2.使用isolation 指定事务的隔离级别,最常用的取值为READ_COMMITTED
	 *
	 * 3.默认情况下spring的声明式事务对所有的运行时异常进行回滚,可以通过对应的属性进行设置
	 *  noRollbackFor={UserAccountException.class} 对哪些异常不捕获
	 *
	 * 4.使用readOnly 指定事务是否为只读,若真的是一个只读取数据库值的方法,应设置readOnly=true
	 *
	 * 5.使用timeout 指定强制回滚事务之前可以占用的时间(单位:秒)
	 *
	 * 超时和只读属性：
	 * 由于事务可以在行和表上获得锁，因此长事务会占用资源，并对整体性能产生影响
	 * 如果一个事务只读取数据但不做修改，数据库引擎可以对这个事务进行优化
	 * 超时事务属性：事务在强制回滚之前可以保持多久，这样可以防止长期运行的事务占用资源
	 * 只读事务属性：表示这个事务只读取数据但不更新数据，这样可以帮助数据库引擎优化事务
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED, readOnly = false, timeout = 1)
	public void purchase(String username, String isbn) {

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		//获取书单价
		int price = bookShopDao.findBookPriceByIsbn(isbn);
		//更新书的库存
		bookShopDao.updateBookStock(isbn);
		//更新用户余额
		bookShopDao.updateUserAccount(username, price);
	}

}
```

# 使用xml文件的方式配置事务

```java
package zzc.spring.tx.xml;

public interface BookShopDao {

	//根据书号获取书的单价
	int findBookPriceByIsbn(String isbn);

	//更新书的库存,使书号对应的库存-1
	void updateBookStock(String isbn);

	//更新用户的账户余额,使用username 的 balance - price 
	void updateUserAccount(String username, int price);
}
```

```java
package zzc.spring.tx.xml;

import org.springframework.jdbc.core.JdbcTemplate;

public class BookShopDaoImpl implements BookShopDao {
	
	private JdbcTemplate jdbcTemplate;
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public int findBookPriceByIsbn(String isbn) {
		String sql = "SELECT price FROM book WHERE isbn = ?";
		return jdbcTemplate.queryForObject(sql, Integer.class,isbn);
	}

	public void updateBookStock(String isbn) {
		//检查书的库存是否足够
		String sql2 = "SELECT stock FROM book_stock where isbn = ?";
		int stock = jdbcTemplate.queryForObject(sql2, Integer.class,isbn);
		if(stock==0){
			throw new BookStockExcetion("库存不足!");
		}
		String sql = "UPDATE book_stock SET stock = stock - 1 WHERE isbn = ?";
		jdbcTemplate.update(sql,isbn);
	}

	public void updateUserAccount(String username, int price) {
		String sql2 = "SELECT balance FROM account where username = ?";
		int balance = jdbcTemplate.queryForObject(sql2, Integer.class,username);
		if(balance < price){
			throw new UserAccountException("余额不足!");
		}
		String sql = "UPDATE account SET balance = balance - ? WHERE username = ?";
		jdbcTemplate.update(sql,price,username);
	}

}
```

```java
package zzc.spring.tx.xml;

public class BookStockExcetion extends RuntimeException {

	public BookStockExcetion() {
		super();
	}

	public BookStockExcetion(String message, Throwable cause) {
		super(message, cause);
	}

	public BookStockExcetion(String message) {
		super(message);
	}

	public BookStockExcetion(Throwable cause) {
		super(cause);
	}
	
}
```

```java
package zzc.spring.tx.xml;

public class UserAccountException extends RuntimeException {

	public UserAccountException() {
		super();
	}

	public UserAccountException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserAccountException(String message) {
		super(message);
	}

	public UserAccountException(Throwable cause) {
		super(cause);
	}

}
```

```java
package zzc.spring.tx.xml.service;

public interface BookShopService {

	void purchase(String username, String isbn);
}
```

```java
package zzc.spring.tx.xml.service.impl;


import zzc.spring.tx.xml.BookShopDao;
import zzc.spring.tx.xml.service.BookShopService;

public class BookShopServiceImpl implements BookShopService {

	private BookShopDao bookShopDao;
	
	public void setBookShopDao(BookShopDao bookShopDao) {
		this.bookShopDao = bookShopDao;
	}

	public void purchase(String username, String isbn) {
		//获取书单价
		int price = bookShopDao.findBookPriceByIsbn(isbn);
		//更新书的库存
		bookShopDao.updateBookStock(isbn);
		//更新用户余额
		bookShopDao.updateUserAccount(username, price);
	}

}
```

```java
package zzc.spring.tx.xml.service;

import java.util.List;

public interface Cashier {

	public void checkout(String username, List<String> isbns);
	
}
```

```java
package zzc.spring.tx.xml.service.impl;

import zzc.spring.tx.xml.service.BookShopService;
import zzc.spring.tx.xml.service.Cashier;

import java.util.List;


public class CashierImpl implements Cashier {

	private BookShopService bookShopService;
	
	public void setBookShopService(BookShopService bookShopService) {
		this.bookShopService = bookShopService;
	}
	
	public void checkout(String username, List<String> isbns) {
		for(String isbn:isbns){
			bookShopService.purchase(username, isbn);
		}

	}

}
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context" xmlns:tx="http://www.springframework.org/schema/tx"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx.xsd http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd">

    <context:component-scan base-package="zzc.spring.tx"/>

    <!-- 导入属性文件 -->
    <context:property-placeholder location="classpath:db.properties"/>

    <!-- 配置c3p0数据源 -->
    <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
        <property name="user" value="${jdbc.user}"/>
        <property name="password" value="${jdbc.password}"/>
        <property name="jdbcUrl" value="${jdbc.jdbcUrl}"/>
        <property name="driverClass" value="${jdbc.driverClass}"/>

        <property name="initialPoolSize" value="${jdbc.initPoolSize}"/>
        <property name="maxPoolSize" value="${jdbc.maxPoolSize}"/>
    </bean>

    <!-- 配置Spring的JDBCTemplate -->
    <bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
        <property name="dataSource" ref="dataSource"/>
    </bean>

    <bean id="bookShopDao" class="zzc.spring.tx.xml.BookShopDaoImpl">
        <property name="jdbcTemplate" ref="jdbcTemplate"/>
    </bean>

    <bean id="bookShopService" class="zzc.spring.tx.xml.service.impl.BookShopServiceImpl">
        <property name="bookShopDao" ref="bookShopDao"/>
    </bean>

    <bean id="cashier" class="zzc.spring.tx.xml.service.impl.CashierImpl">
        <property name="bookShopService" ref="bookShopService"/>
    </bean>

    <!-- 1.配置事务管理器 -->
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"></property>
    </bean>

    <!-- 2.配置事务属性 -->
    <tx:advice id="txAdvice" transaction-manager="transactionManager">
        <tx:attributes>
            <!-- 根据方法名指定事务的属性 -->
            <tx:method name="purchase" propagation="REQUIRES_NEW"/>
            <tx:method name="get*" read-only="true"/>
            <tx:method name="find*" read-only="true"/>
            <tx:method name="*"/>
        </tx:attributes>
    </tx:advice>

    <!-- 3.配置事务切入点,以及把事务切入点和事务属性关联起来 -->
    <aop:config>
        <aop:pointcut expression="execution(* zzc.spring.tx.xml.service.*.*(..))" id="txPointCut"/>
        <aop:advisor advice-ref="txAdvice" pointcut-ref="txPointCut"/>
    </aop:config>

</beans>
```

```java
package zzc.spring.tx.xml;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import zzc.spring.tx.xml.service.BookShopService;
import zzc.spring.tx.xml.service.Cashier;

import java.util.Arrays;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/spring-tx-xml.xml")
public class SpringTransactionTests {

	@Autowired
	private BookShopService bookShopService;

	@Autowired
	private Cashier cashier;

	@Test
	public void testCheckout() {
		cashier.checkout("AA", Arrays.asList("1001", "1002"));
	}

	@Test
	public void testPurchase() {
		bookShopService.purchase("AA", "1001");
	}

}
```

