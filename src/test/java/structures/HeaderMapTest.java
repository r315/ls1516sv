package structures;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import Strutures.HeaderInfo;
import Strutures.HeaderMap;
import Strutures.HtmlResult;
import Strutures.IResultFormat;
import Strutures.TextResult;
import console.MainApp;

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
        for (IResultFormat cmd : map.getResults()) ++i;
        Assert.assertEquals(2,i);
    }

    @Test
    public void GetHtmlResultFromMap(){
        HeaderInfo hi= new HeaderInfo(new String[]{"GET","/movies","accept:text/html"});
        IResultFormat r=map.getResponseMethod(hi);
        Assert.assertTrue(r instanceof HtmlResult);
    }

    @Test
    public void GetTextResultFromMap(){
        HeaderInfo hi= new HeaderInfo(new String[]{"GET","/movies","accept:text/plain"});
        IResultFormat r=map.getResponseMethod(hi);
        Assert.assertTrue(r instanceof TextResult);
    }

    @Test
    public void GetDefaultResultFromMapWithoutParams(){
        HeaderInfo hi= new HeaderInfo(new String[]{"GET","/movies"});
        IResultFormat r=map.getResponseMethod(hi);
        Assert.assertTrue(r instanceof HtmlResult);
    }

}
