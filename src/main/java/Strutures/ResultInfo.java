package Strutures;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Red on 25/04/2016.
 */
public class ResultInfo {

    private Collection<String> title;
    private Collection<ArrayList<String>> data;

    public ResultInfo(){}

    public ResultInfo(Collection<String> title, Collection<ArrayList<String>> data){
        this.title=title;
        this.data=data;
    }

    public void setTitles(Collection<String> titles){
        this.title= titles;
    }

    public void setValues(AbstractCollection<ArrayList<String>> data){this.data=data;}

    public Collection<String> getTitles(){
        return this.title;
    }

    public Collection<ArrayList<String>> getValues(){return this.data;}

}
