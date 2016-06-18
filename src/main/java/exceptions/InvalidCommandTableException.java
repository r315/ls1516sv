package exceptions;

/**
 * Created by Red on 09/04/2016.
 */
public class InvalidCommandTableException extends InvalidCommandException {
    public InvalidCommandTableException(){super("Invalid table in path of command");}
}
