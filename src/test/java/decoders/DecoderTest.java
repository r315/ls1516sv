package decoders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import exceptions.InvalidCommandException;
import org.junit.Before;
import org.junit.Test;
import utils.Decoder;

public class DecoderTest {
    HashMap<String, String> expectedHeader;

    @Before
    public void initHeader(){
        expectedHeader = new HashMap<>();
        expectedHeader.put("accept", "text/plain");
        expectedHeader.put("accept-language", "en-gb");
    }

    @Test
    public void shouldBeAblleToGetHeaders() throws Exception{
        HashMap<String, String> headers = Decoder.decodeHeaders(("POST /movies/1/reviews accept:text/plain|accept-language:en-gb").split(" "));
        assertEquals(expectedHeader,headers);
    }

    @Test(expected= InvalidCommandException.class)
    public void HeadersExecute_SingleHeader() throws InvalidCommandException {
        HashMap<String, String> aux = new HashMap<>(); aux.put("accept","text/plain");
        HashMap<String, String> headers = Decoder.decodeHeaders("POST /movies/1/reviews accept:".split(" "));
    }

    @Test
    public void HeadersExecute_HeaderParam() throws Exception {
        HashMap<String, String> headers = Decoder.decodeHeaders("POST /movies/1/reviews accept:text/plain|accept-language:en-gb reviewerName=LS&reviewSummary=FewStuff&review=MoreStuff&rating=5".split(" "));
        assertEquals(expectedHeader,headers);
    }

    @Test
    public void HeadersExecute_Array() throws Exception {
        String [] aux = new String[]{"POST","/movies/1/reviews","accept:text/plain|accept-language:en-gb"};
        HashMap<String, String> headers = Decoder.decodeHeaders(aux);
        assertEquals(expectedHeader,headers);
    }

    @Test
    public void HeaderExecute_OnlyParam() throws Exception {
        HashMap<String, String> headers = Decoder.decodeHeaders("POST /movies/1/reviews reviewerName=LS&reviewSummary=FewStuff&review=MoreStuff&rating=5".split(" "));
        assertEquals(new HashMap<String, String>(),headers);
    }

    @Test
    public void HeadersExecute_isEmpty() throws Exception {
        String [] aux = new String[]{"GET","","",""};
        HashMap<String, String> headers = Decoder.decodeHeaders(aux);
        assertEquals(new HashMap<String, String>(),headers);
    }

    @Test
	public void shouldGetHeaderFromCommandLine() throws Exception{
		String cmdline = "GET /movies accept:text/plain|file-name:teste.txt";
		Map <String,String> map = Decoder.decodeHeaders(cmdline.split(" "));
		assertEquals(map.get("accept"),"text/plain");
		assertEquals(map.get("file-name"),"teste.txt");		
	}	
}
