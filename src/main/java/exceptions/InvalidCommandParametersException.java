package exceptions;

public class InvalidCommandParametersException extends Exception {
	public InvalidCommandParametersException(){super("Invalid Parameters of command");}

	public InvalidCommandParametersException(String string) {
		super(string);
	}
}
