package zzc.spring.beans;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CarTests {

	@Test
	public void testCar() {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		Car car = (Car) ctx.getBean("car");
		System.out.println(car);
		// 关闭IOC容器
		ctx.close();
	}

}
