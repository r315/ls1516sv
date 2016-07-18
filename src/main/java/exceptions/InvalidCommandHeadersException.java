package exceptions;

/**
 * Created by hmr on 18/07/2016.
 */
public class InvalidCommandHeadersException extends InvalidCommandException {
    public InvalidCommandHeadersException(){super("Invalid Headers of command");}
    public InvalidCommandHeadersException(String s) {
        super(s);
    }
}
