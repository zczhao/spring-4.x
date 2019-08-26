package zzc.spring.beans;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class HelloWorldTests {

	@Test
	public void testHello(){
		// 创建HelloWorld的一个对象
		HelloWorld helloWorld = new HelloWorld();
		// 为name属性赋值
		helloWorld.setName("Spring");
		// 调用hello方法
		helloWorld.hello();
	}

	@Test
	public void testHelloSpring(){
		// 1、创建Spring的IOC容器对象
		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		// 2、从IOC容器中获取bean实例
		HelloWorld helloWorld = (HelloWorld) ctx.getBean("helloWorld");
		// 3、调用hello方法
		helloWorld.hello();
	}
}
