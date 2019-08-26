package zzc.spring.beans;

public class HelloWorld {

	public HelloWorld() {
		System.out.println("HelloWorld's Constructor...");
	}

	private String name;

	public void setName(String name) {
		System.out.println("setName： " + name);
		this.name = name;
	}

	public void hello() {
		System.out.println("hello：" + name);
	}
}
