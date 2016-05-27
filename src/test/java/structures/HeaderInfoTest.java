package structures;

import junit.framework.Assert;

import org.junit.Test;

import Strutures.Command.HeaderInfo;

/**
 * Created by Red on 30/04/2016.
 */

public class HeaderInfoTest {

    @Test
    public void GetDefaultFormat() throws Exception{
        HeaderInfo hi= new HeaderInfo(new String[]{"GET","/movies"});
        String accept = hi.getHeadersMap().get("accept");
        Assert.assertEquals("text/html",accept);
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
