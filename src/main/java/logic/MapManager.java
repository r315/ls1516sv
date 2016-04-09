package logic;

import Strutures.CNode;
import Strutures.CommandMap;
import Strutures.DataNode;
import commands.CommandInfo;

import exceptions.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Red on 05/04/2016.
 */
public class MapManager{

    public static CNode getCNode(CommandInfo cmdInfo)
            throws InvalidCommandMethodException,InvalidCommandTableException,InvalidCommandPathException {

        if (cmdInfo == null) return null;
        HashMap<String, HashMap<String, DataNode>> cmdsMap = CommandMap.createMap();
        if(cmdsMap==null)throw new InvalidCommandMethodException();
        HashMap<String, DataNode> tablesMap = cmdsMap.get(cmdInfo.getMethod());

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
                    }else if (!cnode_segment.equals("{v}")) {//The segment is not a variable and thus it is not this CNode we are looking for
                        invalidCmd=true;
                        break;  //goto next CNode
                    }
                }
                if(!invalidCmd)return curr;
            }
            curr=curr.getNext();
        }

        throw new InvalidCommandPathException();
    }
}
