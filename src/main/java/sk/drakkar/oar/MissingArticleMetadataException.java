package sk.drakkar.oar;

public class MissingArticleMetadataException extends RuntimeException {

	public MissingArticleMetadataException() {
		super();
	}

	public MissingArticleMetadataException(String message, Throwable cause) {
		super(message, cause);
	}

	public MissingArticleMetadataException(String message) {
		super(message);
	}

	public MissingArticleMetadataException(Throwable cause) {
		super(cause);
	}
	
}
