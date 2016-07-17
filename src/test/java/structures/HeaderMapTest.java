package structures;

import Strutures.Command.HeaderInfo;
import Strutures.Command.HeaderMap;
import Strutures.ResponseFormat.Html.HtmlResult;
import Strutures.ResponseFormat.IResultFormat;
import templates.TextResult;
import Strutures.ResponseFormat.ResultInfo;
import console.Manager;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.function.Function;

/**
 * Created by Red on 30/04/2016.
 */
public class HeaderMapTest {

    HeaderMap map;

    @Before
    public void before() throws Exception{
        map= Manager.createHeadersMap();
    }

    @Test
    public void GetNumberOfResultTypesTest() throws Exception{
        int i =0;
        for (Function<ResultInfo,IResultFormat> func : map.getResults()) ++i;
        Assert.assertEquals(2,i);
    }

    @Test
    public void GetHtmlResultFromMap(){
        HeaderInfo hi= new HeaderInfo(new String[]{"GET","/movies","accept:text/html"});
        IResultFormat r=map.getResponseMethod(hi).apply(new ResultInfo());
        Assert.assertTrue(r instanceof HtmlResult);
    }

    @Test
    public void GetTextResultFromMap(){
        HeaderInfo hi= new HeaderInfo(new String[]{"GET","/movies","accept:text/plain"});
        IResultFormat r=map.getResponseMethod(hi).apply(new ResultInfo());
        Assert.assertTrue(r instanceof TextResult);
    }

    @Test
    public void GetDefaultResultFromMapWithoutParams(){
        HeaderInfo hi= new HeaderInfo(new String[]{"GET","/movies"});
        IResultFormat r=map.getResponseMethod(hi).apply(new ResultInfo());
        Assert.assertTrue(r instanceof HtmlResult);
    }

}
