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
	private DataSource dataSource;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


	@Test
	public void testDataSource() throws SQLException {
		System.out.println(dataSource.getConnection());
	}

	/**
	 * 可以为参数起名字
	 * 1.好处：若有多个参数,则不再去对应位置,直接对应参数名,便于维护
	 * 2.缺点：较为麻烦
	 */
	@Test
	public void testNamedParameterJdbcTemplate(){
		String sql = "INSERT INTO employees(last_name,email,dept_id) VALUES(:ln,:email,:deptId)";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ln","EEE");
		paramMap.put("email","eee@vip.com");
		paramMap.put("deptId",2);
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
