package zzc.spring.hibernate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import zzc.spring.hibernate.service.BookService;
import zzc.spring.hibernate.service.CashierService;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class SpringHibernateTests {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private BookService bookService;

	@Autowired
	private CashierService cashierService;

	@Test
	public void testDateSource() throws SQLException {
		System.out.println(dataSource.getConnection());
	}


	@Test
	public void testBookService() {
		bookService.purchase("AA", "1001");
	}


	@Test
	public void testCashierService() {
		cashierService.checkout("AA", Arrays.asList("1001", "1002"));
	}
}
