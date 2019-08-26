package zzc.spring.beans;

public class Car {
	private String brand;
	private String corp;
	private double price;
	private int maxSpeed;

	public Car(String brand, String corp, double price) {
		this.brand = brand;
		this.corp = corp;
		this.price = price;
	}

	public Car(String brand, String corp, int maxSpeed) {
		this.brand = brand;
		this.corp = corp;
		this.maxSpeed = maxSpeed;
	}
}
