package zzc.spring.beans.factorybean;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CarFactoryBeanTests {

	@Test
	public void testCar() {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("beans-beanfactory.xml");
		Car car = (Car) ctx.getBean("car");
		System.out.println(car);
	}
}
