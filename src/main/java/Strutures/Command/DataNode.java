package Strutures.Command;

import java.util.Collection;

/**
 * Created by Red on 29/03/2016.
 */
public class DataNode {
    private Collection<String> resources;
    private CNode next;

    public DataNode(Collection<String> resources){
        this.resources=resources;
    }

    public void setNext(CNode next){
        this.next=next;
    }

    public CNode getNext(){
        return this.next;
    }

    public Collection<String> resources(){
        return this.resources;
    }

    public void addToResources(String s){
        resources.add(s);
    }

}
