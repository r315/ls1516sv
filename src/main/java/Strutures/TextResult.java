package Strutures;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by Red on 24/04/2016.
 */
public class TextResult implements IResult{

    public void display(ResultInfo resultInfo){
        if(resultInfo.getValues().isEmpty()){
            System.out.println("No results found.");
            return;
        }
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

    @Override
    public void writeToFile(String filename) throws Exception {
        throw new UnsupportedOperationException();
    }
}
