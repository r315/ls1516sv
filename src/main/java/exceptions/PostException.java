package exceptions;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by hmr on 08/06/2016.
 */
public class PostException extends SQLException {
    private int code;
    public static final int ENTRY_EXISTS = 2627;
    public static final int ENTRY_NOT_FOUND = 547;
    private static HashMap<Integer,String> codeMap = new HashMap<>();

    public PostException(String s) { super(s); }
    public PostException(int e) {
        super();
        code = e;
    }

    public PostException(int e, String message) {
        super();
        code = e;
        codeMap.putIfAbsent(e,message);
    }

    @Override
    public String getMessage(){
        return codeMap.get(code);
    }

    public int getErrorCode(){
        return code;
    }
}
