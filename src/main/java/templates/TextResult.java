package templates;

import Strutures.Command.CommandInfo;
import Strutures.ResponseFormat.IResultFormat;
import Strutures.ResponseFormat.ResultInfo;
import exceptions.InvalidCommandException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Red on 24/04/2016.
 */
public class TextResult implements IResultFormat {

    private String response;

    public String generate(ResultInfo resultInfo, CommandInfo commandInfo) throws SQLException, InvalidCommandException {
        if(resultInfo.getValues().isEmpty()){
            //System.out.println("No results found.");
            response="No results found.";
            return response;
        }
        String result=null;
        result= resultInfo.getDisplayTitle()+":\n\n";
        //System.out.println(resultInfo.getDisplayTitle()+":");
        for (ArrayList<String> dataList : resultInfo.getValues()) {
            Iterator<String> dataList_it= dataList.iterator();
            if(resultInfo.getTitles().isEmpty()) {
                while(dataList_it.hasNext())
                    result+=dataList_it.next()+"\n";
                //System.out.println(dataList_it.next());
            }else{
                Iterator<String> title_it =resultInfo.getTitles().iterator();
                while(title_it.hasNext())  //dataList and titleList have the same size
                        result+=title_it.next()+" : "+dataList_it.next()+"\n";
                    //System.out.println(title_it.next()+" : "+dataList_it.next());
                result+="---------------------------\n";
                //System.out.println("---------------------------");
            }
        }
        return result;
    }

    /*
    public void display(Map<String,String> headers) {
        System.out.println(response);
    }
    */
}
