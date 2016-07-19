package decoders;

import static org.junit.Assert.assertEquals;

import exceptions.InvalidCommandException;
import org.junit.Test;

import exceptions.InvalidCommandMethodException;
import utils.Decoder;

public class DecodeMethodTest {

    @Test
    public void MethodExecute_Method() throws InvalidCommandException {
        String method = decodeMethods("GET");
        assertEquals("GET",method);
    }

    @Test
    public void MethodExecute_MethodPath() throws InvalidCommandException {
        String method = decodeMethods("GET /movies/1/reviews/4");
        assertEquals("GET",method);
    }

    @Test
    public void MethodExecute_Array() throws InvalidCommandException {
        String method = decodeMethods("GET /movies/1/reviews/4");
        assertEquals("GET",method);
    }

    @Test(expected=InvalidCommandMethodException.class)
    public void MethodExecute_isEmpty() throws InvalidCommandException {
        decodeMethods("");
    }

    private String decodeMethods(String path) throws InvalidCommandException {
        return Decoder.decodeMethod(path.split(" "));
    }
}
