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
