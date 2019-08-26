package zzc.spring.beans;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Tests {

	@Test
	public void test01(){
		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");

		UserService userService = (UserService) ctx.getBean("userService");
		userService.add();
	}
}
