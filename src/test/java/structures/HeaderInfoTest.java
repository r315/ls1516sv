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
        Assert.assertEquals("text/html",hi.getHeaderValue("accept"));
    }

    @Test
    public void GetResultTextTestWithHeaderParams() throws Exception{
        HeaderInfo hi= new HeaderInfo(new String[]{"GET","/movies","accept:text/plain"});
        Assert.assertEquals("text/plain",hi.getHeaderValue("accept"));
    }

    @Test
    public void GetResultHtmlTest() throws Exception{
        HeaderInfo hi= new HeaderInfo(new String[]{"GET","/movies","accept:text/html"});
        Assert.assertEquals("text/html",hi.getHeaderValue("accept"));
    }
}
