package structures;

import Strutures.HeaderInfo;
import junit.framework.Assert;
import org.junit.Test;

/**
 * Created by Red on 30/04/2016.
 */

public class HeaderInfoTest {

    @Test
    public void GetResultTextTest() throws Exception{
        HeaderInfo hi= new HeaderInfo(new String[]{"GET","/movies"});
        String accept = hi.getHeadersMap().get("accept");
        Assert.assertEquals("text/plain",accept);
    }

    @Test
    public void GetResultTextTestWithHeaderParams() throws Exception{
        HeaderInfo hi= new HeaderInfo(new String[]{"GET","/movies","accept:text/plain"});
        String accept = hi.getHeadersMap().get("accept");
        Assert.assertEquals("text/plain",accept);
    }

    @Test
    public void GetResultHtmlTest() throws Exception{
        HeaderInfo hi= new HeaderInfo(new String[]{"GET","/movies","accept:text/html"});
        String accept = hi.getHeadersMap().get("accept");
        Assert.assertEquals("text/html",accept);
    }
}
