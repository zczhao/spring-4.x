# jdbcTemplate简介

为了使JDBC更加易于使用，Spring在JDBC API上定义了一个抽象层，以此建立一个JDBC存取框架

作为Spring JDBC框架的核心，JDBC模板的设计目的是为不同类型的JDBC操作提供模板方法，每个模板方法都能控制整个过程，并充许覆盖过程中的特定任务，通过这种方式，可以在尽可能保留灵活性的情况下，将数据库存取的工作量降到最低

## 使用jdbcTemplate更新数据库

**用sql语句和参数更新数据库**：

**update**:

public int update(String sql, Object... args)

**批量更新数据库**：

**batchUpdate**:

public int[] batchUpdate(String sql, List<Object[]> batchArgs)

## 使用jdbcTemplate查询数据库

**查询单行**：

**queryForObject**：

public <T> queryForObject(String sql, ParameterizeRowMapper<T> rm, Object... args)

**便利的BeanPropertyRowMapper实现**

**查询多行**：

**query**：

public <T> List<T> query(String sql, ParameterizeRowMapper<T> rm, Object... args)

**单值查询**：

**queryForObject**：

public <T> T queryForObject(String sql,Class<T> requiredType, Object... args)

```properties
jdbc.user=admin
jdbc.password=richard
jdbc.driverClass=com.mysql.jdbc.Driver
jdbc.jdbcUrl=jdbc:mysql://10.10.10.141/test1

jdbc.initPoolSize=5
jdbc.maxPoolSize=10
```

```xml
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
```

```java
public class Employee {

	private Integer id;
	private String lastName;
	private String email;

	private Integer deptId;
	// getter/setter...
}
```

```java
public class Department {

	private Integer id;
	private String name;
    // getter/setter...
}
```

```java
private ApplicationContext ctx = null;
private JdbcTemplate jdbcTemplate;

{
    ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    jdbcTemplate = (JdbcTemplate) ctx.getBean("jdbcTemplate");
}

@Test
public void testDataSource() throws SQLException {
    DataSource dataSource = ctx.getBean(DataSource.class);
    System.out.println(dataSource.getConnection());

}

/**
	 * 执行INSERT,UPDATE,DELETE
	 */
@Test
public void testUpdate() {
    String sql = "UPDATE employees  SET last_name = ? WHERE id = ? ";
    jdbcTemplate.update(sql, "knight", 1);
}

/**
	 * 执行批量更新：批量的INSERT,UPDATE,DELETE
	 * 最后一个参数是Object[]的List类型：因为修改一条记录需要一个Object的数组，那么多条就需要多个Object的数组
	 */
@Test
public void testBatchUpdate() {
    String sql = "INSERT INTO employees(last_name,email,dept_id) VALUES (?, ?, ?)";
    List<Object[]> batchArgs = new ArrayList<Object[]>();
    batchArgs.add(new Object[]{"AAA", "aaa@126.com", 1});
    batchArgs.add(new Object[]{"BBB", "bbb@126.com", 2});
    batchArgs.add(new Object[]{"CCC", "ccc@126.com", 1});
    batchArgs.add(new Object[]{"DDD", "ddd@126.com", 2});
    jdbcTemplate.batchUpdate(sql, batchArgs);

}

/**
	 * 从数据库中获取一条记录，实际得到对应的一个对象
	 * 注意不是调用queryForObject(String sql, Class<Employee> requiredType, Object... args)方法!
	 * 1.其中RowMapper 指定如何映射结果集的行,常用的实现类为BeanPropertyRowMapper
	 * 2.使用SQL中列的别名和类的属性名的映射
	 * 3.不支持级联属性,jdbcTemplate是一个jdbc的小工具,不是ORM框架
	 */
@Test
public void testQueryForObject(){
    String sql = "SELECT id, last_name lastName, email,dept_id as \"department.id\" FROM employees WHERE id = ?";
    RowMapper<Employee> rowMapper = new BeanPropertyRowMapper<Employee>(Employee.class);
    Employee employee = jdbcTemplate.queryForObject(sql, rowMapper,1);
    System.out.println(employee);
}

/**
	 * 获取单个列的值，或做统计查询
	 */
@Test
public void testQueryForObject2(){
    String sql = "SELECT count(id) FROM employees";
    RowMapper<Employee> rowMapper = new BeanPropertyRowMapper<Employee>(Employee.class);
    long count = jdbcTemplate.queryForObject(sql, Long.class);
    System.out.println(count);
}


/**
	 *查到实体类的集合
	 */
@Test
public void testQueryForList(){
    String sql = "SELECT id, last_name lastName, email FROM employees WHERE dept_id = ?";
    RowMapper<Employee> rowMapper = new BeanPropertyRowMapper<Employee>(Employee.class);
    List<Employee> employees = jdbcTemplate.query(sql, rowMapper, 1);
    for(Employee employee:employees){
        System.out.println(employee);
    }
}
```



## 简化JDBC模板查询

每次使用都创建一个JdbcTemplate的新实例，这种做法效率很低下

JdbcTemplate类被设计成为线程安全的，所以可以在IOC容器中声明它的单个实例，并将这个实例注入到所有的DAO实例中

JdbcTemplate也利用了Java1.5的特定(自动装箱、泛型、可变长度等)来简化开发

Spring JDBC框架还提供了一个JdbcDaoSupport类来简化DAO实现，该类声明了jdbcTemplate属性，它可以从IOC容器中注入，或者自动从数据源中创建

## 在JDBC模板中使用具名参数

在经典的JDBC用法中，SQL参数是用占位符?表示，并且受到位置的限制，定位参数的问题在于，一旦参数的顺序发生变化，就必须改变参数绑定

在Spring JDBC框架中，绑定SQL参数的另一种选择是使用具名参数(named parameter)

具名参数：SQL按名称(以冒号开头)而不是按位置进行指定，具名参数更易于维护，也提升了可读性，具名参数由框架类在运行时用占位符取代

具名参数只在NamedParameterJdbcTemplate中得到支持

```xml
<context:component-scan base-package="zzc.spring.jdbc"/>

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

<!-- 配置namedParameterJdbcTemplate,该对象可以使用具名参数,没有无参数的构造器,所以必须为其构造器指定参数 -->
<bean id="namedParameterJdbcTemplate"
      class="org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate">
    <constructor-arg ref="dataSource"/>
</bean>
```

```java
package zzc.spring.jdbc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class JDBCTemplateSpringTests {
    
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	/**
	 * 可以为参数起名字
	 * 1.好处：若有多个参数,则不再去对应位置,直接对应参数名,便于维护
	 * 2.缺点：较为麻烦
	 */
	@Test
	public void testNamedParameterJdbcTemplate(){
		String sql = "INSERT INTO employees(last_name,email,dept_id) VALUES(:ln,:email,:deptid)";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ln","EEE");
		paramMap.put("email","eee@vip.com");
		paramMap.put("deptid",2);
		namedParameterJdbcTemplate.update(sql, paramMap);
	}

	/**
	 * 使用具名参数时,可以使用update(String sql, SqlParameterSource paramSource)方法进行更新操作
	 * 1.SLQ语句中的参数名和类的属性一致
	 * 2.使用SqlParameterSource的BeanPropertySqlParameterSource实现类作为参数
	 */
	@Test
	public void testNamedParameterJdbcTemplate2(){
		String sql = "INSERT INTO employees(last_name,email,dept_id) VALUES(:lastName,:email,:deptId)";
		Employee employee = new Employee();
		employee.setLastName("FFF");
		employee.setEmail("fff@sina.com");
		employee.setDeptId(2);
		SqlParameterSource paramSource = new BeanPropertySqlParameterSource(employee);
		namedParameterJdbcTemplate.update(sql, paramSource);
	}

}
```

