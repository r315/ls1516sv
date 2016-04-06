package Strutures;

import java.util.Iterator;

/**
 * Created by Red on 29/03/2016.
 */
public class DataNode {
    private Iterable<String> resources;
    private CNode next;

    public DataNode(Iterable<String> resources){
        this.resources=resources;
    }

    public void setNext(CNode next){
        this.next=next;
    }

    public CNode getNext(){
        return this.next;
    }

    public Iterator<String> resources(){
        return this.resources.iterator();
    }


}
