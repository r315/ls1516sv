package decoders;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import exceptions.InvalidCommandMethodException;

public class DecodeMethodTest {

    @Test
    public void MethodExecute_Method() throws Exception {
        String method = DecodeMethod.decode("GET");
        assertEquals("GET",method);
    }

    @Test
    public void MethodExecute_MethodPath() throws Exception {
        String method = DecodeMethod.decode("GET /movies/1/reviews/4");
        assertEquals("GET",method);
    }

    @Test
    public void MethodExecute_Array() throws Exception {
        String [] aux = new String[]{"GET","/movies/1/reviews/4"};
        String method = DecodeMethod.decode(aux);
        assertEquals("GET",method);
    }

    @Test(expected=InvalidCommandMethodException.class)
    public void MethodExecute_isEmpty() throws Exception {
        String [] aux = new String[]{"",""};
        DecodeMethod.decode(aux);
    }

    @Test(expected= InvalidCommandMethodException.class)
    public void MethodExecute_isNull() throws Exception {
        String [] aux = new String[]{null,null};
        DecodeMethod.decode(aux);
    }

    /*@Test
    public void MethodExecute_space() throws Exception {
        String aux = null;
        DecodeMethod.decode(aux);
    }*/

}
