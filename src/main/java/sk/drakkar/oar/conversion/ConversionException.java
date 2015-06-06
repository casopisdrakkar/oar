package sk.drakkar.oar.conversion;

public class ConversionException extends RuntimeException {

	public ConversionException() {
		super();
	}

	public ConversionException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConversionException(String message) {
		super(message);
	}

	public ConversionException(Throwable cause) {
		super(cause);
	}

}
