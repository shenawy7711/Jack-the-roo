package exception;

public class StandardActionException extends ActionException {// since ActionException is an abstract class, the subclassStandardActionException is created to make new objects 

	public StandardActionException() {
		super();
	}

	public StandardActionException(String message) {
		super(message);
	}
	
}
