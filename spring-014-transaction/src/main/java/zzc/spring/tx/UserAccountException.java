package zzc.spring.tx;

public class UserAccountException extends RuntimeException {

	public UserAccountException() {
		super();
	}

	public UserAccountException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserAccountException(String message) {
		super(message);
	}

	public UserAccountException(Throwable cause) {
		super(cause);
	}

}
