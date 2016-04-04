package Strutures;

import commands.*;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Red on 28/03/2016.
 */



public class CommandMap {

    public static HashMap<String,Object> createMap(){
        //Methods
        HashMap<String,Object> commandsMap=new HashMap<String, Object>(2);
        //Tables
        HashMap<String, DataNode> postMap=new HashMap<String, DataNode>(1);
        HashMap<String, DataNode> getMap=new HashMap<String, DataNode>(2);

        //Methods
        commandsMap.put("GET",getMap);
        commandsMap.put("POST",postMap);

        //Set GET DataNodes
        DataNode get_movies=new DataNode(Arrays.asList(
                new String[]{"ratings","reviews"}));
        getMap.put("movies",get_movies);

        DataNode get_tops= new DataNode(Arrays.asList(
                new String[]{"ratings","reviews","higher","lower","average","count"}));
        getMap.put("tops",get_tops);

        //Set POST DataNodes
        DataNode post_movies=new DataNode(Arrays.asList(
                new String[]{"ratings","reviews"}));
        postMap.put("movies",post_movies);

        //Set POST movies CNodes
        CNode curr= new CNode(Arrays.asList(new String[]{}),new PostMovies());
        post_movies.setNext(curr);//sets first CNode

        curr.setNext(new CNode(Arrays.asList(new String[]{"{mid}","ratings"}),new PostMoviesMidRatings()));curr=curr.getNext();
        curr.setNext(new CNode(Arrays.asList(new String[]{"{mid}","reviews"}),new PostMoviesMidReviews()));

        //
        //GET movies CNodes
        //
        curr=new CNode(Arrays.asList(new String[]{}),new GetMovies());
        get_movies.setNext(curr);

        curr.setNext(new CNode(Arrays.asList(new String[]{"{mid}"}),new GetMoviesMid())); curr=curr.getNext();
        curr.setNext(new CNode(Arrays.asList(new String[]{"{mid}","ratings"}),new GetMoviesMidRatings()));curr=curr.getNext();
        curr.setNext(new CNode(Arrays.asList(new String[]{"{mid}","reviews"}),new GetMoviesMidReviews())); curr=curr.getNext();
        curr.setNext(new CNode(Arrays.asList(new String[]{"{mid}","reviews","{rid}"}),new GetMoviesMidReviewsRid()));

        //
        //GET tops CNodes
        //

        curr=new CNode(Arrays.asList(new String[]{"ratings","higher","average"}),new getTopsRatingsHigherAverage());
        get_tops.setNext(curr);

        curr.setNext(new CNode(Arrays.asList(new String[]{"ratings","higher","average"}),new getTopsNRatingsHigherAverage()));curr=curr.getNext();
        curr.setNext(new CNode(Arrays.asList(new String[]{"ratings","lower","average"}),new getTopsRatingsLowerAverage()));curr=curr.getNext();
        curr.setNext(new CNode(Arrays.asList(new String[]{"ratings","lower","average"}),new getTopsNRatingsLowerAverage()));curr=curr.getNext();
        curr.setNext(new CNode(Arrays.asList(new String[]{"reviews","higher","count"}),new getTopsReviewsHigherCount()));curr=curr.getNext();
        curr.setNext(new CNode(Arrays.asList(new String[]{"reviews","higher","count"}),new getTopsNReviewsHigherCount()));

        return commandsMap;
    }
}
