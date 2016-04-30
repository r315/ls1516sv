package Strutures;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Red on 25/04/2016.
 */
public class ResultInfo {

    private String diplayTitle;
    private Collection<String> title;
    private Collection<ArrayList<String>> data;

    public ResultInfo(){}

    public ResultInfo(String displayTitle, Collection<String> title, Collection<ArrayList<String>> data){
        this.diplayTitle = displayTitle;
        this.title=title;
        this.data=data;
    }

    public void setTitles(Collection<String> titles){
        this.title= titles;
    }

    public void setValues(Collection<ArrayList<String>> data){this.data=data;}

    public String getDisplayTitle() {return this.diplayTitle;}

    public Collection<String> getTitles(){
        return this.title;
    }

    public Collection<ArrayList<String>> getValues(){return this.data;}

}
