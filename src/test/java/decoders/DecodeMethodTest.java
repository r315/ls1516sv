package decoders;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import exceptions.InvalidCommandMethodException;
import utils.Decoder;

public class DecodeMethodTest {

    @Test
    public void MethodExecute_Method() throws Exception {
        String method = Decoder.decodeMethod("GET");
        assertEquals("GET",method);
    }

    @Test
    public void MethodExecute_MethodPath() throws Exception {
        String method = Decoder.decodeMethod("GET /movies/1/reviews/4");
        assertEquals("GET",method);
    }

    @Test
    public void MethodExecute_Array() throws Exception {
        String [] aux = new String[]{"GET","/movies/1/reviews/4"};
        String method = Decoder.decodeMethod(aux);
        assertEquals("GET",method);
    }

    @Test(expected=InvalidCommandMethodException.class)
    public void MethodExecute_isEmpty() throws Exception {
        String [] aux = new String[]{"",""};
        Decoder.decodeMethod(aux);
    }

    @Test(expected= InvalidCommandMethodException.class)
    public void MethodExecute_isNull() throws Exception {
        String [] aux = new String[]{null,null};
        Decoder.decodeMethod(aux);
    }

    /*@Test
    public void MethodExecute_space() throws Exception {
        String aux = null;
        Decoder.decodeMethod(aux);
    }*/

}
