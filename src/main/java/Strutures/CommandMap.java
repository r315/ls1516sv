package Strutures;

import commands.*;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Red on 28/03/2016.
 */



public class CommandMap {

    public static HashMap<String,HashMap<String,DataNode>> createMap(){
        final String var="{v}";
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

        curr.setNext(new CNode(Arrays.asList(new String[]{"movies",var,"ratings"}),new PostMoviesMidRatings()));curr=curr.getNext();
        curr.setNext(new CNode(Arrays.asList(new String[]{"movies",var,"reviews"}),new PostMoviesMidReviews()));

        //
        //GET movies CNodes
        //
        curr=new CNode(Arrays.asList(new String[]{"movies"}),new GetMovies());
        get_movies.setNext(curr);

        curr.setNext(new CNode(Arrays.asList(new String[]{"movies",var}),new GetMoviesMid())); curr=curr.getNext();
        curr.setNext(new CNode(Arrays.asList(new String[]{"movies",var,"ratings"}),new GetMoviesMidRatings()));curr=curr.getNext();
        curr.setNext(new CNode(Arrays.asList(new String[]{"movies",var,"reviews"}),new GetMoviesMidReviews())); curr=curr.getNext();
        curr.setNext(new CNode(Arrays.asList(new String[]{"movies",var,"reviews",var}),new GetMoviesMidReviewsRid()));

        //
        //GET tops CNodes
        //

        curr=new CNode(Arrays.asList(new String[]{"tops","ratings","higher","average"}),new getTopsRatingsHigherAverage());
        get_tops.setNext(curr);

        curr.setNext(new CNode(Arrays.asList(new String[]{"tops",var,"ratings","higher","average"}),new GetTopsNRatingsHigherAverage()));curr=curr.getNext();
        curr.setNext(new CNode(Arrays.asList(new String[]{"tops","ratings","lower","average"}),new GetTopsRatingsLowerAverage()));curr=curr.getNext();
        curr.setNext(new CNode(Arrays.asList(new String[]{"tops",var,"ratings","lower","average"}),new GetTopsNRatingsLowerAverage()));curr=curr.getNext();
        curr.setNext(new CNode(Arrays.asList(new String[]{"tops","reviews","higher","count"}),new GetTopsReviewsHigherCount()));curr=curr.getNext();
        curr.setNext(new CNode(Arrays.asList(new String[]{"tops",var,"reviews","higher","count"}),new GetTopsNReviewsHigherCount()));

        return commandsMap;
    }
}
