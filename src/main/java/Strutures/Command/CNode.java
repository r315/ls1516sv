package Strutures.Command;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by Red on 04/04/2016.
 */
public class CNode {
    private Collection<String> resources;
    CommandBase command;
    private CNode next;

    public CNode(Collection<String> resources,CommandBase command){
        this.resources=resources;
        this.command=command;
    }

    public void setNext(CNode next){
        this.next=next;
    }

    public CNode getNext(){
        return this.next;
    }

    public Iterator<String> iterator(){
        return this.resources.iterator();
    }

    public Collection<String> collection(){
        return this.resources;
    }

    public int getCollectionSize(){
        return resources.size();
    }

    public CommandBase getCommand(){
        return command;
    }
}
