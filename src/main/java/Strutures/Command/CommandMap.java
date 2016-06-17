package Strutures.Command;

import decoders.DecodeMethod;
import decoders.DecodePath;
import exceptions.InvalidCommandMethodException;
import exceptions.InvalidCommandPathException;
import exceptions.InvalidCommandTableException;
import java.util.*;


/**
 * Created by Red on 28/03/2016.
 */

public class CommandMap {

    private HashMap<String, HashMap<String,DataNode>> commandsMap;

    public CommandMap(){
        this.commandsMap=new HashMap<String, HashMap<String,DataNode>>();
    }

    public boolean add(String sCommand, ICommand iCommand)
            throws InvalidCommandPathException, InvalidCommandMethodException{
        //TODO make optional iCommand
        if(sCommand==null || iCommand==null) return false;

        String table=null;
        String method= DecodeMethod.decode(sCommand);
        //HashMap<String,DataNode> methodMap= this.commandsMap.get(method);
        HashMap<String, DataNode> methodMap= this.commandsMap.putIfAbsent(method,new HashMap<String, DataNode>());
        if(methodMap==null)//in case it didn't exist before
            methodMap=this.commandsMap.get(method);
        Collection<String> path= DecodePath.decode(sCommand);
        if(path.size()!=0){
            path.removeIf(s -> s.startsWith("{")&& s.endsWith("}"));
            table= path.iterator().next();
        }

        DataNode dataNode= methodMap.putIfAbsent(table, new DataNode(path));
        if(dataNode==null)//in case it didn't exist before
            dataNode=methodMap.get(table);
        else{//in case it exists, add more
            Collection<String> col=dataNode.resources();
            for (String s:path)
                if(!col.contains(s))dataNode.addToResources(s);
        }
        //path.iterator().forEachRemaining(s->dataNode.);
        path=DecodePath.decode(sCommand);
        CNode curr= new CNode(path, iCommand);
        curr.setNext(dataNode.getNext());
        dataNode.setNext(curr);
        return true;
    }

    public ICommand get(CommandInfo cmdInfo)
            throws InvalidCommandMethodException, InvalidCommandTableException, InvalidCommandPathException {

        if (cmdInfo == null) return null;
        HashMap<String, DataNode> tablesMap = commandsMap.get(cmdInfo.getMethod());

        //Iterator<String> commandIterator = cmdInfo.getResources().iterator();
        if(tablesMap==null)throw new InvalidCommandMethodException();
        DataNode dataNode = tablesMap.get(cmdInfo.getTable());
        if(dataNode==null)throw new InvalidCommandTableException();

        Collection<String> dataNodeSegmentList= dataNode.resources();
        CNode curr = dataNode.getNext();

        while (curr!=null){
            Iterator<String> cnodeIterator = curr.iterator();
            Iterator<String> commandIterator = cmdInfo.getResources().iterator();
            boolean invalidCmd=false;
            if (cmdInfo.getResourcesSize() == curr.getCollectionSize()) {
                while(commandIterator.hasNext()) {
                    String cnode_segment = cnodeIterator.next();
                    String cmdInfo_segment = commandIterator.next();

                    if (dataNodeSegmentList.contains(cmdInfo_segment)) {//It's a non-variable segment.
                        if (cmdInfo_segment.equals(cnode_segment))
                            continue;   //goto next segment
                        else{
                            invalidCmd=true;
                            break; //goto next CNode
                        }
                    }else if(cnode_segment.startsWith("{")&& cnode_segment.endsWith("}")) {//var
                        cmdInfo.addToMapData(trimBraces(cnode_segment),cmdInfo_segment);
                    }else{////The segment is not a variable and thus it is not this CNode we are looking for
                        invalidCmd=true;
                        break;  //goto next CNode
                    }
                }
                if(!invalidCmd)return curr.getCommand();
            }
            curr=curr.getNext();
        }

        throw new InvalidCommandPathException();
    }

    public Set<ICommand> getCommands() {
        return new AbstractSet<ICommand>(){
            @Override
            public Iterator<ICommand> iterator(){
                return new Iterator<ICommand>(){
                    Iterator<HashMap<String,DataNode>> method_it=commandsMap.values().iterator();
                    Iterator<DataNode> dataNode_it= null;
                    CNode curr=null;
                    boolean checked=false;

                    @Override
                    public boolean hasNext() {
                        if(checked) return  true;
                        else {//check if hasNext
                            if(dataNode_it==null && method_it.hasNext()) {//first time case
                                dataNode_it = method_it.next().values().iterator();
                                curr=dataNode_it.next().getNext();
                            }else if(curr.getNext()!=null){//has another CNode
                                curr=curr.getNext();
                            }else{//get next dataNode
                                if(dataNode_it.hasNext()){
                                    curr= dataNode_it.next().getNext();
                                }else{//get next method_it
                                    if(method_it.hasNext()){
                                        dataNode_it=method_it.next().values().iterator();
                                        curr=dataNode_it.next().getNext();
                                    }else return false;
                                }
                            }
                        }
                        checked=true;
                        return true;
                    }

                    @Override
                    public ICommand next(){
                        if (!hasNext()) throw new NoSuchElementException();
                        checked=false;
                        return curr.getCommand();
                    }
                };
            }

            @Override
            public int size() throws UnsupportedOperationException{
                throw new UnsupportedOperationException();
            }
        };
    }

    private static String trimBraces(String s){
        return s.substring(1,s.length()-1);
    }
}
