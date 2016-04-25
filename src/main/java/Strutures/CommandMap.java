package Strutures;

import commands.*;
import decoders.DecodeMethod;
import decoders.DecodePath;
import exceptions.InvalidCommandMethodException;
import exceptions.InvalidCommandPathException;
import exceptions.InvalidCommandTableException;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Red on 28/03/2016.
 */



public class CommandMap {

    private HashMap<String, HashMap<String,DataNode>> commandsMap;

    public CommandMap(){
        this.commandsMap=new HashMap<String, HashMap<String,DataNode>>();
    }

    public boolean add(String sCommand, ICommand iCommand) throws Exception{
        if(sCommand==null || iCommand==null) return false;

        String method= DecodeMethod.decode(sCommand);
        //HashMap<String,DataNode> methodMap= this.commandsMap.get(method);
        HashMap<String, DataNode> methodMap= this.commandsMap.putIfAbsent(method,new HashMap<String, DataNode>());
        if(methodMap==null)//in case it didn't exist before
            methodMap=this.commandsMap.get(method);
        Collection<String> path= DecodePath.decode(sCommand);
        path.removeIf(s -> s.startsWith("{")&& s.endsWith("}"));
        String table= path.iterator().next();
        DataNode dataNode= methodMap.putIfAbsent(table, new DataNode(path));
        if(dataNode==null)//in case it didn't exist before
            dataNode=methodMap.get(table);
        path=DecodePath.decode(sCommand);
        CNode curr= new CNode(path, iCommand);
        curr.setNext(dataNode.getNext());
        dataNode.setNext(curr);
        return true;
    }

    public ICommand get(CommandInfo cmdInfo)
            throws InvalidCommandMethodException, InvalidCommandTableException, InvalidCommandPathException {

        if (cmdInfo == null) return null;
        if(commandsMap==null)throw new InvalidCommandMethodException();
        HashMap<String, DataNode> tablesMap = commandsMap.get(cmdInfo.getMethod());

        //Iterator<String> commandIterator = cmdInfo.getResources().iterator();
        DataNode dataNode = tablesMap.get(cmdInfo.getTable());
        if(tablesMap==null)throw new InvalidCommandTableException();
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

    private static String trimBraces(String s){
        return s.substring(1,s.length()-1);
    }
}
