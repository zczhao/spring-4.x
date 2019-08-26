package zzc.spring.hibernate.entities;

public class Book {

	private Integer id;
	private String bookName;
	private String isBn;
	private int price;
	private int stock;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public String getIsBn() {
		return isBn;
	}

	public void setIsBn(String isBn) {
		this.isBn = isBn;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}
}
