package structures;

import Strutures.*;
import com.sun.xml.internal.ws.client.sei.ResponseBuilder;
import console.MainApp;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Red on 30/04/2016.
 */
public class HeaderMapTest {

    HeaderMap map;

    @Before
    public void before() throws Exception{
        map= MainApp.createHeadersMap();
    }

    @Test
    public void GetNumberOfResultTypesTest() throws Exception{
        int i =0;
        for (IResult cmd : map.getResults()) ++i;
        Assert.assertEquals(2,i);
    }

    @Test
    public void GetResultTextTest() throws Exception{
        HeaderInfo hi= new HeaderInfo(new String[]{"GET","/movies"});
        String accept = hi.getHeadersMap().get("accept");
        Assert.assertEquals("text/plain",accept);
    }

    @Test
    public void GetResultTextTestWithHeaderParams() throws Exception{
        HeaderInfo hi= new HeaderInfo(new String[]{"GET","/movies accept:text/plain"});
        String accept = hi.getHeadersMap().get("accept");
        Assert.assertEquals("text/plain",accept);
    }

    @Test
    public void GetResultHtmlTest() throws Exception{
        HeaderInfo hi= new HeaderInfo(new String[]{"GET","/movies accept:text/html"});
        String accept = hi.getHeadersMap().get("accept");
        Assert.assertEquals("text/html",accept);
    }

}
