package zzc.spring.beans.annotation;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import zzc.spring.beans.annotation.controller.UserController;
import zzc.spring.beans.annotation.repository.UserRepository;

public class AnnotationTests {

	@Test
	public void test01(){
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans-annotation.xml");
		//TestObject to = (TestObject) ctx.getBean("testObject");
		//System.out.println(to);
		//
		//
		//UserController userController = (UserController) ctx.getBean("userController");
		//System.out.println(userController);

		UserRepository userRepository = (UserRepository) ctx.getBean("userRepository");
		System.out.println(userRepository);


		//UserService userService = (UserService) ctx.getBean("userService");
		//System.out.println(userService);
	}

	@Test
	public void test02(){
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans-annotation.xml");
		UserController userController = (UserController) ctx.getBean("userController");
		userController.execute();
	}
}
