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
		// ApplicationContext 代表IOC容器
		// ClassPathXmlApplicationContext：是ApplicationContext 接口的实现类，该实现类从类路径下加载配置文件
		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		// 2、从IOC容器中获取bean实例
		// 利用id定位到IOC容器中bean
		HelloWorld helloWorld = (HelloWorld) ctx.getBean("helloWorld");
		// 3、调用hello方法
		helloWorld.hello();
	}


	@Test
	public void testHelloSpringType(){
		// 1、创建Spring的IOC容器对象
		// ApplicationContext 代表IOC容器
		// ClassPathXmlApplicationContext：是ApplicationContext 接口的实现类，该实现类从类路径下加载配置文件
		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		// 2、从IOC容器中获取bean实例
		// org.springframework.beans.factory.NoUniqueBeanDefinitionException 定义多个bean会报错
		// 利用类型返回 IOC容器中的bean，但要求IOC容器中必须只能有一个该类型的Bean
		HelloWorld helloWorld = ctx.getBean(HelloWorld.class);
		// 3、调用hello方法
		helloWorld.hello();
	}
}
