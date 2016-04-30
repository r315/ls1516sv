package exceptions;

public class InvalidCommandParametersException extends Exception {
	public InvalidCommandParametersException(){super("Invalid Parameters");}

	public InvalidCommandParametersException(String string) {
		super(string);
	}
}
