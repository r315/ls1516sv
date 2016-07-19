package decoders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import exceptions.InvalidCommandException;
import exceptions.InvalidCommandHeadersException;
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
        HashMap<String, String> headers = decodeHeaders("POST /movies/1/reviews accept:text/plain|accept-language:en-gb");
        assertEquals(expectedHeader,headers);
    }

    @Test(expected= InvalidCommandHeadersException.class)
    public void shouldGetExceptionOnIncompleteFilenameHeader() throws InvalidCommandException {
        decodeHeaders("POST /movies/1/reviews file-name:");
    }

    @Test(expected= InvalidCommandHeadersException.class)
    public void shouldGetExceptionOnIncompleteAcceptHeader() throws InvalidCommandException {
        decodeHeaders("POST /movies/1/reviews accept:");
    }

    @Test
    public void HeadersExecute_HeaderParam() throws Exception {
        HashMap<String, String> headers = decodeHeaders("POST /movies/1/reviews accept:text/plain|accept-language:en-gb reviewerName=LS&reviewSummary=FewStuff&review=MoreStuff&rating=5");
        assertEquals(expectedHeader,headers);
    }

    @Test
    public void HeadersExecute_Array() throws Exception {
        HashMap<String, String> headers = decodeHeaders("POST /movies/1/reviews accept:text/plain|accept-language:en-gb");
        assertEquals(expectedHeader,headers);
    }

    @Test
    public void HeaderExecute_OnlyParam() throws Exception {
        HashMap<String, String> headers = decodeHeaders("POST /movies/1/reviews reviewerName=LS&reviewSummary=FewStuff&review=MoreStuff&rating=5");
        assertEquals(new HashMap<String, String>(),headers);
    }

    @Test
    public void HeadersExecute_isEmpty() throws Exception {
        HashMap<String, String> headers = decodeHeaders("GET");
        assertEquals(new HashMap<String, String>(),headers);
    }

    @Test
	public void shouldGetHeaderFromCommandLine() throws Exception{
		Map <String,String> map = decodeHeaders("GET /movies accept:text/plain|file-name:teste.txt");
		assertEquals(map.get("accept"),"text/plain");
		assertEquals(map.get("file-name"),"teste.txt");		
	}

    private HashMap<String, String> decodeHeaders(String path) throws InvalidCommandException {
        return Decoder.decodeHeaders(path.split(" "));
    }
}
