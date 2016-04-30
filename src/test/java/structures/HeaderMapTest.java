package structures;

import Strutures.*;
import console.MainApp;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

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
    public void GetHtmlResultFromMap(){
        HeaderInfo hi= new HeaderInfo(new String[]{"GET","/movies","accept:text/html"});
        IResult r=map.getResponseMethod(hi);
        Assert.assertTrue(r instanceof HtmlResult);
    }

    @Test
    public void GetTextResultFromMap(){
        HeaderInfo hi= new HeaderInfo(new String[]{"GET","/movies","accept:text/plain"});
        IResult r=map.getResponseMethod(hi);
        Assert.assertTrue(r instanceof TextResult);
    }

    @Test
    public void GetTextResultFromMapWithoutParams(){
        HeaderInfo hi= new HeaderInfo(new String[]{"GET","/movies"});
        IResult r=map.getResponseMethod(hi);
        Assert.assertTrue(r instanceof TextResult);
    }

}
