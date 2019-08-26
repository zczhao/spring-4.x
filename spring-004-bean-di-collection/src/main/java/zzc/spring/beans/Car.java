package zzc.spring.beans;

public class Car {
	private String brand;
	private String corp;
	private double price;
	private int maxSpeed;

	@Override
	public String toString() {
		return "Car{" +
				"brand='" + brand + '\'' +
				", corp='" + corp + '\'' +
				", price=" + price +
				", maxSpeed=" + maxSpeed +
				'}';
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getCorp() {
		return corp;
	}

	public void setCorp(String corp) {
		this.corp = corp;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(int maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
}
