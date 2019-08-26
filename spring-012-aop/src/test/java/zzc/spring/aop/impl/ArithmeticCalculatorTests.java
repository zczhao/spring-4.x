package zzc.spring.aop.impl;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ArithmeticCalculatorTests {

	@Test
	public void test01(){
		// 1、创建Spring的IOC容器
		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		// 2、从IOC容器中获取bean的实例
		ArithmeticCalculator arithmeticCalculator = ctx.getBean(ArithmeticCalculator.class);
		// 3、使用bean
		int result = arithmeticCalculator.add(1, 2);
		System.out.println("-->" + result);

		result = arithmeticCalculator.div(4, 2);
		System.out.println("-->" + result);
	}
}
