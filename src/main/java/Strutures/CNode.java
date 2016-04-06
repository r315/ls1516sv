package Strutures;

import commands.ICommand;

import java.util.Iterator;

/**
 * Created by Red on 04/04/2016.
 */
public class CNode {
    private Iterable<String> resources;
    ICommand command;
    private CNode next;

    public CNode(Iterable<String> resources,ICommand command){
        this.resources=resources;
        this.command=command;
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
