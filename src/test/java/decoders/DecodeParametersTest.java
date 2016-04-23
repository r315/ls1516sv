package decoders;

import exceptions.InvalidCommandPathException;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

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
    public void ParametersExecute_Parameters() throws Exception {
        HashMap<String, String> param = DecodeParameters.decode("POST /movies/1/reviews reviewerName=LS&reviewSummary=FewStuff&review=MoreStuff&rating=5");
        assertEquals(parameters,param);
    }


    @Test
    public void ParametersExecute_SingleParam() throws Exception {
        HashMap<String, String> aux = new HashMap<>(); aux.put("reviewerName","LS");
        HashMap<String, String> param = DecodeParameters.decode("POST /movies/1/reviews reviewerName=LS");
        assertEquals(aux,param);
    }

    @Test
    public void ParametersExecute_HeaderParam() throws Exception {
        HashMap<String, String> param = DecodeParameters.decode("POST /movies/1/reviews accept:text/plain|accept-language:en-gb reviewerName=LS&reviewSummary=FewStuff&review=MoreStuff&rating=5");
        assertEquals(parameters,param);
    }

    @Test
    public void ParametersExecute_Array() throws Exception {
        String [] aux = new String[]{"POST","/movies/1/reviews","reviewerName=LS&reviewSummary=FewStuff&review=MoreStuff&rating=5"};
        HashMap<String, String> param = DecodeParameters.decode(aux);
        assertEquals(parameters,param);
    }

    @Test
    public void ParametersExecute_OnlyHeader() throws Exception {
        HashMap<String, String> param = DecodeParameters.decode("POST /movies/1/reviews accept:text/plain|accept-language:en-gb");
        assertEquals(null,param);
    }

    @Test
    public void ParametersExecute_isEmpty() throws Exception {
        String [] aux = new String[]{"GET","","",""};
        HashMap<String, String> param = DecodeParameters.decode(aux);
        assertEquals(null,param);
    }

    @Test
    public void ParametersExecute_isNull() throws Exception {
        String [] aux = new String[]{null,null,null,null};
        HashMap<String, String> param = DecodeParameters.decode(aux);
        assertEquals(null,param);
    }
}
