package exceptions;

import java.sql.SQLException;

/**
 * Created by hmr on 08/06/2016.
 */
public class PostException extends SQLException {
    private int code;
    public static final int ENTRY_EXISTS = 2627;
    public static final int ENTRY_NOT_FOUND = 547;
    public static final int DATE_OR_TIME_CONVERTION_FAILED = 241;
    public static final int STRING_IS_TOO_LONG = 8152;

    public PostException(String s) { super(s); }
    public PostException(int e) {
        super();
        code = e;
    }

    public PostException(int e, String message) {
        super(message);
        code = e;
    }

    public int getErrorCode(){
        return code;
    }
}
