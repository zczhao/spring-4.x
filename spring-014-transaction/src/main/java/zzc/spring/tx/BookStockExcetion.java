package zzc.spring.tx;

public class BookStockExcetion extends RuntimeException {

	public BookStockExcetion() {
		super();
	}

	public BookStockExcetion(String message, Throwable cause) {
		super(message, cause);
	}

	public BookStockExcetion(String message) {
		super(message);
	}

	public BookStockExcetion(Throwable cause) {
		super(cause);
	}

	
	
}
