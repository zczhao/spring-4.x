package zzc.spring.beans.factory;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class CarTests {

	@Test
	public void testCar() {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("beans-factory.xml");
		Car car = (Car) ctx.getBean("car1");
		System.out.println(car);
	}


	@Test
	public void testCar2() {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("beans-factory.xml");
		Car car = (Car) ctx.getBean("car2");
		System.out.println(car);
	}


}
