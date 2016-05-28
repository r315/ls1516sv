package Strutures.ResponseFormat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Red on 25/04/2016.
 */
//HR: null fields now return empty collections insted null 
public class ResultInfo {

    private String displayTitle;
    private Collection<String> title;
    private Collection<ArrayList<String>> data;

    public ResultInfo(){}

    public ResultInfo(String displayTitle, Collection<String> title, Collection<ArrayList<String>> data){
        this.displayTitle = displayTitle;
        this.title=title;
        this.data=data;
    }

    public void setTitles(Collection<String> titles){
        this.title= titles;
    }

    public void setValues(Collection<ArrayList<String>> data){this.data=data;}

    public String getDisplayTitle() {return this.displayTitle == null ? "": this.displayTitle;}

    public Collection<String> getTitles(){ 
    	return this.title == null ? new ArrayList<String>():this.title;
    } 

    public Collection<ArrayList<String>> getValues(){
    	return this.data == null ? new ArrayList<ArrayList<String>>(): this.data;}

    public List<String> removeColumn(String col){
        List<String> removedValues = new ArrayList<String>();
        if(title.size() == 0)
            return removedValues;
        List<String> newtitletable = (List<String>)title;

        int index = newtitletable.indexOf(col);

        ((List<String>) title).remove(index);

        for(List<String> line : data){
            removedValues.add(line.get(index));
            line.remove(index);
        }
        return removedValues;
    }
}
