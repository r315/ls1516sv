package Strutures;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Red on 25/04/2016.
 */
public class ResultInfo {

	private String displayTitle;
    private Collection<String> title;
    private Collection<ArrayList<String>> data;

    public ResultInfo(){}

    public ResultInfo(String displayTitle, Collection<String> title, Collection<ArrayList<String>> data){
    	this.displayTitle=displayTitle;
        this.title=title;
        this.data=data;
    }

    public void setTitles(Collection<String> titles){
        this.title= titles;
    }

    public void setValues(Collection<ArrayList<String>> data){this.data=data;}

    public Collection<String> getTitles(){
        return this.title;
    }

    public Collection<ArrayList<String>> getValues(){return this.data;}

}
