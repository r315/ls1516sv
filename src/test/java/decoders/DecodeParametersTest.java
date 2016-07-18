package decoders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;

import exceptions.InvalidCommandException;
import org.junit.Before;
import org.junit.Test;
import utils.Decoder;

/**
 * Created by Luigi Sekuiya on 23/04/2016.
 */
public class DecodeParametersTest {
    HashMap<String, String> parameters;

    @Before
    public void init(){
        parameters = new HashMap<>();
        parameters.put("reviewerName","LS");
        parameters.put("reviewSummary","FewStuff");
        parameters.put("review","MoreStuff");
        parameters.put("rating","5");
    }

    @Test
    public void spaceBetweenParameterValues(){
        String test="Even+More+Stuff+Than+More+Stuff";
        assertEquals("Even More Stuff Than More Stuff",test.replace('+',' '));
    }

    @Test
    public void ParametersExecute_Parameters() throws Exception {
        HashMap<String, String> param = Decoder.decodeParameters("POST /movies/1/reviews reviewerName=LS&reviewSummary=FewStuff&review=MoreStuff&rating=5".split(" "));
        assertEquals(parameters,param);
    }


    @Test
    public void ParametersExecute_SingleParam() throws Exception {
        HashMap<String, String> aux = new HashMap<>(); aux.put("reviewerName","LS");
        HashMap<String, String> param = Decoder.decodeParameters("POST /movies/1/reviews reviewerName=LS".split(" "));
        assertEquals(aux,param);
    }

    @Test
    public void ParametersExecute_HeaderParam() throws Exception {
        HashMap<String, String> param = Decoder.decodeParameters("POST /movies/1/reviews accept:text/plain|accept-language:en-gb reviewerName=LS&reviewSummary=FewStuff&review=MoreStuff&rating=5".split(" "));
        assertEquals(parameters,param);
    }

    @Test
    public void ParametersExecute_Array() throws Exception {
        String [] aux = new String[]{"POST","/movies/1/reviews","reviewerName=LS&reviewSummary=FewStuff&review=MoreStuff&rating=5"};
        HashMap<String, String> param = Decoder.decodeParameters(aux);
        assertEquals(parameters,param);
    }

    @Test
    public void ParametersExecute_OnlyHeader() throws Exception {
        HashMap<String, String> param = Decoder.decodeParameters("POST /movies/1/reviews accept:text/plain|accept-language:en-gb".split(" "));
        assertEquals(new HashMap<String, String>(),param);
    }

    @Test
    public void ParametersExecute_isEmpty() throws Exception {
        String [] aux = new String[]{"GET","","",""};
        HashMap<String, String> param = Decoder.decodeParameters(aux);
        assertEquals(new HashMap<String, String>(),param);
    }

    @Test(expected=InvalidCommandException.class)
    public void ParametersExecute_isNull() throws InvalidCommandException {
        String [] aux = new String[]{null,null,null,null};
        HashMap<String, String> param = Decoder.decodeParameters(aux);
    }

    @Test(expected=InvalidCommandException.class)
    public void ParametersExecute_NullParam()throws InvalidCommandException{
        String [] aux = new String[]{"POST","/movies/1/reviews","accept:text/plain|accept-language:en-gb",null};
        HashMap<String, String> param = Decoder.decodeParameters(aux);
    }
}
