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
    public void ParametersExecute_Parameters() throws InvalidCommandException {
        HashMap<String, String> param = decodeParams("POST /movies/1/reviews reviewerName=LS&reviewSummary=FewStuff&review=MoreStuff&rating=5");
        assertEquals(parameters,param);
    }


    @Test
    public void ParametersExecute_SingleParam() throws InvalidCommandException {
        HashMap<String, String> aux = new HashMap<>(); aux.put("reviewerName","LS");
        HashMap<String, String> param = decodeParams("POST /movies/1/reviews reviewerName=LS");
        assertEquals(aux,param);
    }

    @Test
    public void ParametersExecute_HeaderParam() throws InvalidCommandException {
        HashMap<String, String> param = decodeParams("POST /movies/1/reviews accept:text/plain|accept-language:en-gb reviewerName=LS&reviewSummary=FewStuff&review=MoreStuff&rating=5");
        assertEquals(parameters,param);
    }

    @Test
    public void HeaderExecute_OnlyParam() throws InvalidCommandException {
        HashMap<String, String> expected = new HashMap<>();
        expected.put("reviewerName","LS");
        assertEquals(expected, decodeParams("POST /movies/1/reviews reviewerName=LS"));
    }

    @Test
    public void ParametersExecute_Array() throws InvalidCommandException {
        HashMap<String, String> param = decodeParams("POST /movies/1/reviews reviewerName=LS&reviewSummary=FewStuff&review=MoreStuff&rating=5");
        assertEquals(parameters,param);
    }

    @Test
    public void ParametersExecute_isEmpty() throws InvalidCommandException {
        HashMap<String, String> param = decodeParams("GET");
        assertEquals(new HashMap<String, String>(),param);
    }

    @Test(expected=InvalidCommandException.class)
    public void ParametersExecute_isNull() throws InvalidCommandException {
        String [] aux = new String[]{null,null,null,null};
        HashMap<String, String> param = Decoder.decodeParameters(aux);
    }

    private HashMap<String, String> decodeParams(String path) throws InvalidCommandException{
        return Decoder.decodeParameters(path.split(" "));
    }
}
