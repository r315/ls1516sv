package exceptions;

/**
 * Created by Red on 09/04/2016.
 */
public class InvalidCommandVariableException extends InvalidCommandException {
    public InvalidCommandVariableException(){
        super("Invalid variable in command");
    }
}
