package Logic;

import Strutures.CNode;
import Strutures.CommandMap;
import Strutures.DataNode;
import console.CommandInfo;

import Exceptions.*;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Red on 05/04/2016.
 */
public class MapManager{

    public static CNode getCNode(CommandInfo cmdInfo) throws Exception {
        if (cmdInfo == null) return null;
        HashMap<String, HashMap<String, DataNode>> cmdsMap = CommandMap.createMap();
        if(cmdsMap==null)throw new InvalidCommandMethodException();
        HashMap<String, DataNode> tablesMap = cmdsMap.get(cmdInfo.getMethod());
        if(tablesMap==null)throw new InvalidCommandTableException();
        Iterator<String> commandIterator = cmdInfo.getResources().iterator();
        DataNode dataNode = tablesMap.get(cmdInfo.getMethod());
        CNode curr = dataNode.getNext();

        while (commandIterator.hasNext()) {
            Iterator<String> it = curr.resources();
            if (cmdInfo.getResourcesSize() == curr.getCollectionSize()) {

            } else {
                curr = curr.getNext();
                continue;
            }
        }

        throw new InvalidCommandPathException();
        //return null;
    }
}
