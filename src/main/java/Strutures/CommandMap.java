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
                    }else if (!cnode_segment.equals("{v}")) {//The segment is not a variable and thus it is not this CNode we are looking for
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

    public static HashMap<String,HashMap<String,DataNode>> createMap(){

        //Methods
        HashMap<String,HashMap<String,DataNode>> commandsMap=new HashMap<String, HashMap<String,DataNode>>(2);
        //Tables
        HashMap<String, DataNode> postMap=new HashMap<String, DataNode>(1);
        HashMap<String, DataNode> getMap=new HashMap<String, DataNode>(2);

        //Methods
        commandsMap.put("GET",getMap);
        commandsMap.put("POST",postMap);

        //Set GET DataNodes
        DataNode get_movies=new DataNode(Arrays.asList(
                new String[]{"movies","ratings","reviews"}));
        getMap.put("movies",get_movies);

        DataNode get_tops= new DataNode(Arrays.asList(
                new String[]{"tops","ratings","reviews","higher","lower","average","count"}));
        getMap.put("tops",get_tops);

        //Set POST DataNodes
        DataNode post_movies=new DataNode(Arrays.asList(
                new String[]{"movies","ratings","reviews"}));
        postMap.put("movies",post_movies);

        //Set POST movies CNodes
        CNode curr= new CNode(Arrays.asList(new String[]{"movies"}),new PostMovies());
        post_movies.setNext(curr);//sets first CNode

        curr.setNext(new CNode(Arrays.asList(new String[]{"movies","{mid}","ratings"}),new PostMoviesMidRatings()));curr=curr.getNext();
        curr.setNext(new CNode(Arrays.asList(new String[]{"movies","{mid}","reviews"}),new PostMoviesMidReviews()));

        //
        //GET movies CNodes
        //
        curr=new CNode(Arrays.asList(new String[]{"movies"}),new GetMovies());
        get_movies.setNext(curr);

        curr.setNext(new CNode(Arrays.asList(new String[]{"movies","{mid}"}),new GetMoviesMid())); curr=curr.getNext();
        curr.setNext(new CNode(Arrays.asList(new String[]{"movies","{mid}","ratings"}),new GetMoviesMidRatings()));curr=curr.getNext();
        curr.setNext(new CNode(Arrays.asList(new String[]{"movies","{mid}","reviews"}),new GetMoviesMidReviews())); curr=curr.getNext();
        curr.setNext(new CNode(Arrays.asList(new String[]{"movies","{mid}","reviews","{rid}"}),new GetMoviesMidReviewsRid()));

        //
        //GET tops CNodes
        //

        curr=new CNode(Arrays.asList(new String[]{"tops","ratings","higher","average"}),new GetTopsRatingsHigherAverage());
        get_tops.setNext(curr);

        curr.setNext(new CNode(Arrays.asList(new String[]{"tops","{n}","ratings","higher","average"}),new GetTopsNRatingsHigherAverage()));curr=curr.getNext();
        curr.setNext(new CNode(Arrays.asList(new String[]{"tops","ratings","lower","average"}),new GetTopsRatingsLowerAverage()));curr=curr.getNext();
        curr.setNext(new CNode(Arrays.asList(new String[]{"tops","{n}","ratings","lower","average"}),new GetTopsNRatingsLowerAverage()));curr=curr.getNext();
        curr.setNext(new CNode(Arrays.asList(new String[]{"tops","reviews","higher","count"}),new GetTopsReviewsHigherCount()));curr=curr.getNext();
        curr.setNext(new CNode(Arrays.asList(new String[]{"tops","{n}","reviews","higher","count"}),new GetTopsNReviewsHigherCount()));

        return commandsMap;
    }
}
