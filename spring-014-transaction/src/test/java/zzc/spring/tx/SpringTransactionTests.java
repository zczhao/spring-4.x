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

	@Autowired
	private Cashier cashier;

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

	@Test
	public void testCheckout(){
		cashier.checkout("AA", Arrays.asList("1001","1002"));
	}


}
