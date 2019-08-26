package zzc.spring.jdbc;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JDBCTemplateTests {

	private ApplicationContext ctx = null;
	private JdbcTemplate jdbcTemplate;
	private EmployeeDao employeeDao;
	private DepartmentDao departmentDao;

	{
		ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		jdbcTemplate = (JdbcTemplate) ctx.getBean("jdbcTemplate");
		employeeDao = (EmployeeDao) ctx.getBean("employeeDao");
		departmentDao = (DepartmentDao) ctx.getBean("departmentDao");
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

	@Test
	public void testEmployeeDao(){
		System.out.println(employeeDao.get(1));
	}

	@Test
	public void testDepartmentDao(){
		System.out.println(departmentDao.get(1));
	}
}
