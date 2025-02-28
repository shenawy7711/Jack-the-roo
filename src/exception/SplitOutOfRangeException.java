package exception;

public class SplitOutOfRangeException extends InvalidCardException{
	public SplitOutOfRangeException() {
		super();
	}
	SplitOutOfRangeException(String message){
		super(message);
	}
}
