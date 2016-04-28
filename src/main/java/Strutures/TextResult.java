package Strutures;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by Red on 24/04/2016.
 */
public class TextResult implements IResult{

    //TODO
    public void display(ResultInfo resultInfo){
        System.out.println(resultInfo.getDisplayTitle()+":");
        resultInfo.getValues().forEach( dataList-> {
            Iterator<String> dataList_it= dataList.iterator();
            if(resultInfo.getTitles()==null) {
                while(dataList_it.hasNext())
                    System.out.println(dataList_it.next());
            }else{
                Iterator<String> title_it =resultInfo.getTitles().iterator();
                while(title_it.hasNext())  //dataList and titleList have the same size
                    System.out.println(title_it.next()+" : "+dataList_it.next());
                System.out.println("---------------------------");
            }
        });
    }
}
