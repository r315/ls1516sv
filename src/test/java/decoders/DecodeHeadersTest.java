package decoders;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class DecodeHeadersTest {
    HashMap<String, String> head;

    @Before
    public void init(){
        head = new HashMap<>();
        head.put("accept","text/plain");
        head.put("accept-language","en-gb");
    }

    @Test
    public void HeadersExecute_Headers() throws Exception {
        HashMap<String, String> headers = DecodeHeaders.decode("POST /movies/1/reviews accept:text/plain|accept-language:en-gb");
        assertEquals(head,headers);
    }

    @Test
    public void HeadersExecute_SingleHeader() throws Exception {
        HashMap<String, String> aux = new HashMap<>(); aux.put("accept","text/plain");
        HashMap<String, String> headers = DecodeHeaders.decode("POST /movies/1/reviews accept:text/plain");
        assertEquals(aux,headers);
    }

    @Test
    public void HeadersExecute_HeaderParam() throws Exception {
        HashMap<String, String> headers = DecodeHeaders.decode("POST /movies/1/reviews accept:text/plain|accept-language:en-gb reviewerName=LS&reviewSummary=FewStuff&review=MoreStuff&rating=5");
        assertEquals(head,headers);
    }

    @Test
    public void HeadersExecute_Array() throws Exception {
        String [] aux = new String[]{"POST","/movies/1/reviews","accept:text/plain|accept-language:en-gb"};
        HashMap<String, String> headers = DecodeHeaders.decode(aux);
        assertEquals(head,headers);
    }

    @Test
    public void HeaderExecute_OnlyParam() throws Exception {
        HashMap<String, String> headers = DecodeHeaders.decode("POST /movies/1/reviews reviewerName=LS&reviewSummary=FewStuff&review=MoreStuff&rating=5");
        assertEquals(new HashMap<String, String>(),headers);
    }

    @Test
    public void HeadersExecute_isEmpty() throws Exception {
        String [] aux = new String[]{"GET","","",""};
        HashMap<String, String> headers = DecodeHeaders.decode(aux);
        assertEquals(new HashMap<String, String>(),headers);
    }

    @Test
    public void HeadersExecute_isNull() throws Exception {
        String [] aux = new String[]{null,null,null,null};
        HashMap<String, String> param = DecodeHeaders.decode(aux);
        assertEquals(new HashMap<String, String>(),param);
    }
}
