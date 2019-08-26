package zzc.spring.beans;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PersonTests {

	static ApplicationContext ctx = null;

	@BeforeClass
	public static void beforeClass() {
		ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
	}

	@Test
	public void testPerson() {
		Person person = (Person) ctx.getBean("person3");
		System.out.println(person);
	}


	@Test
	public void testNewPerson() {
		NewPerson person = (NewPerson) ctx.getBean("newPerson");
		System.out.println(person);
	}

	@Test
	public void testPerson4() {
		Person person = (Person) ctx.getBean("person4");
		System.out.println(person);
	}

	@Test
	public void testPerson5() {
		Person person = (Person) ctx.getBean("person5");
		System.out.println(person);
	}
}
