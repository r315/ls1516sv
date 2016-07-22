package decoders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;

import exceptions.InvalidCommandException;
import exceptions.InvalidCommandParametersException;
import org.junit.Before;
import org.junit.Test;
import utils.Decoder;

/**
 * Created by Luigi Sekuiya on 23/04/2016.
 */
public class DecodeParametersTest {
    HashMap<String, String> testParameters;

    @Before
    public void init(){
        testParameters = new HashMap<>();
        testParameters.put("reviewerName", "LS");
        testParameters.put("reviewSummary", "FewStuff");
        testParameters.put("review", "MoreStuff");
        testParameters.put("rating", "5");
    }

    @Test
    public void spaceBetweenParameterValues(){
        String test="Even+More+Stuff+Than+More+Stuff";
        assertEquals("Even More Stuff Than More Stuff",test.replace('+',' '));
    }

    @Test
    public void ParametersExecute_Parameters() throws InvalidCommandException {
        HashMap<String, String> param = decodeParams("POST /movies/1/reviews reviewerName=LS&reviewSummary=FewStuff&review=MoreStuff&rating=5");
        assertEquals(testParameters,param);
    }

    @Test
    public void ParametersExecute_SingleParam() throws InvalidCommandException {
        HashMap<String, String> aux = new HashMap<>(); aux.put("reviewerName","LS");
        assertEquals(aux,decodeParams("POST /movies/1/reviews reviewerName=LS"));
    }

    @Test
    public void ParametersExecute_HeaderAndParam() throws InvalidCommandException {
        HashMap<String, String> param = decodeParams("POST /movies/1/reviews accept:text/plain|accept-language:en-gb reviewerName=LS&reviewSummary=FewStuff&review=MoreStuff&rating=5");
        assertEquals(testParameters,param);
    }

    @Test
    public void ParametersExecute_OnlyParam() throws InvalidCommandException {
        HashMap<String, String> expected = new HashMap<>();
        expected.put("reviewerName","LS");
        assertEquals(expected, decodeParams("POST /movies/1/reviews reviewerName=LS"));
    }

    @Test
    public void ParametersExecute_MultipleParam() throws InvalidCommandException {
        HashMap<String, String> param = decodeParams("POST /movies/1/reviews reviewerName=LS&reviewSummary=FewStuff&review=MoreStuff&rating=5");
        assertEquals(testParameters,param);
    }

    @Test
    public void ParametersExecute_isEmpty() throws InvalidCommandException {
        HashMap<String, String> param = decodeParams("GET");
        assertEquals(new HashMap<String, String>(),param);
    }

    @Test(expected = InvalidCommandParametersException.class)
    public void shouldGetExceptionOnIncompleteRightParameter()throws InvalidCommandException{
        decodeParams("POST /movies/1/reviews reviewerName=");
    }

    @Test(expected = InvalidCommandParametersException.class)
    public void shouldGetExceptionOnIncompleteLeftParameter()throws InvalidCommandException{
        decodeParams("POST /movies/1/reviews =MovieName");
    }

    @Test(expected = InvalidCommandException.class)
     public void shouldGetExceptionOnExcessOfArguments()throws InvalidCommandException{
        decodeParams("POST /movies accept:text/plain name=movie invalid");
    }

    @Test(expected = InvalidCommandException.class)
    public void shouldGetExceptionOnWrongOrder()throws InvalidCommandException{
        decodeParams("POST /movies name=movie accept:text/plain");
    }

    private HashMap<String, String> decodeParams(String path) throws InvalidCommandException{
        return Decoder.decodeParameters(path.split(" "));
    }
}
