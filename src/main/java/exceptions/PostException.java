package exceptions;

/**
 * Created by hmr on 08/06/2016.
 */
public class PostException extends Exception {
    private int code;
    public static final int ENTRY_EXISTS = 2627;
    public static final int ENTRY_NOT_FOUND = 547;

    public PostException(String s) { super(s); }
    public PostException(int e) { super(); code = e;}

    public int getErrorCode(){
        return code;
    }
}
