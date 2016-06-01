package Strutures.ResponseFormat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by Red on 25/04/2016.
 */
//HR: null fields now return empty collections insted null 
public class ResultInfo {

    public  boolean generateresult;
    private String displayTitle;
    private Collection<String> title;
    private Collection<ArrayList<String>> data;

    public ResultInfo(){}

    public ResultInfo(String displayTitle, Collection<String> title, Collection<ArrayList<String>> data){
        this.displayTitle = displayTitle;
        this.title=title;
        this.data=data;
        this.generateresult = true;
    }

    public ResultInfo(boolean b) {
        this.generateresult = b;
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

    public List<String> removeColumn(String col) throws NoSuchElementException{
        List<String> removedValues = new ArrayList<String>();
        if(title.size() == 0)
            return removedValues;
        List<String> newtitletable = (List<String>)title;
        try {
            int index = newtitletable.indexOf(col);

            ((List<String>) title).remove(index);

            for (List<String> line : data) {
                removedValues.add(line.get(index));
                line.remove(index);
            }
        }catch (Exception e){
            throw new NoSuchElementException();
        }
        return removedValues;
    }

    private String getValueFromData(int col, int line){
           return data.stream()
                   .skip(line)
                   .limit(1)
                   .collect(Collectors.toList()).get(0).get(col);
    }

    public String getGeneratedId(){
       return  getValueFromData(0,0);
    }
}
