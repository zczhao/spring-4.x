package zzc.spring.beans;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AddressTests {

	@Test
	public void testAddress(){
		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		Address address = (Address) ctx.getBean("address");
		System.out.println(address);
	}
}
