package utils;

import Strutures.Command.CommandInfo;
import Strutures.ResponseFormat.ResultInfo;
import console.Manager;
import exceptions.InvalidCommandException;
import exceptions.InvalidCommandParametersException;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Utils {
    //General Utils

    public static String decodeParametersMap (Map<String,String[]> map){
        String result="";
        if(map.isEmpty())return result;
        for (Map.Entry<String,String[]> s:map.entrySet())
            result += String.format("%s=%s&",s.getKey(),s.getValue()[0]);
        return result.substring(0,result.length()-1);
    }

    //Commands Utils

    public static int getSkip (String skipS) throws InvalidCommandParametersException {
        final int DEFAULT_VALUE = 0;
        final boolean IS_SKIP = true;
        return getTS(skipS, DEFAULT_VALUE, IS_SKIP);
    }

    public static int getTop (String topS) throws InvalidCommandParametersException {
        final int DEFAULT_VALUE = -1;
        final boolean IS_SKIP = false;
        return getTS(topS, DEFAULT_VALUE, IS_SKIP);
    }

    private static int getTS(String top_skip, int def, boolean skip) throws InvalidCommandParametersException {
        int ts = def;

        if (top_skip != null) {
            try {
                ts = Integer.parseInt(top_skip);
            } catch (NumberFormatException e) { //If not number
                throw new InvalidCommandParametersException("Invalid value for "+ (skip ? "'Skip'": "'Top'"));
            }
            if (skip) {if (ts < 0) throw new InvalidCommandParametersException("'Skip' parameter is below 0.");}
            else {if (ts <= 0) throw new InvalidCommandParametersException("'Top' parameter is equals or below 0.");}
        }

        return ts;
    }

    private static final int PAG_DEFAULT = 5;
    private static final String topPaging = "top";
    private static final String skipPaging = "skip";

    public static Pair<String, String> paging(HashMap<String,String> param, String link) throws SQLException, InvalidCommandException{
        Pair<String,String> page = new Pair<>();

        String nextSkip, prevSkip = "";
        String top = Integer.toString(PAG_DEFAULT);
        String skip = param.get(skipPaging);

        int skipI;
        try {
            if (skip == null || (skipI = Integer.parseInt(skip)) == 0) {
                nextSkip = top;
                page.value1 = prevSkip;
            } else {
                if (skipI < PAG_DEFAULT) {
                    prevSkip = "0";
                } else {
                    prevSkip = Integer.toString(skipI - PAG_DEFAULT);
                }

                nextSkip = Integer.toString(skipI + PAG_DEFAULT);
                page.value1 = String.format("%s?%s", link, pagingFormat(param, prevSkip, top));
            }
        } catch (NumberFormatException e) { //If not number
            throw new InvalidCommandParametersException(String.format("Invalid value for '%s'", skipPaging));
        }

        //Next
        CommandInfo command = new CommandInfo("GET", link, String.format("%s=%s&%s=%s",topPaging,top,skipPaging,nextSkip));
        ResultInfo ri = Manager.commandMap.get(command).execute(command.getData());

        if (ri.getValues().isEmpty()) page.value2 = "";
            //else paging.put("next", String.format("%s?top=%s&skip=%s", link, top, nextSkip));
        else page.value2 = String.format("%s?%s", link, pagingFormat(param, nextSkip, top));

        return page;
    }

    private static String pagingFormat(HashMap<String, String> param, String skip, String top) {
        param.put(topPaging,top);
        param.put(skipPaging,skip);

        String result="";
        for (Map.Entry s:param.entrySet())
            result += String.format("%s=%s&",s.getKey(),s.getValue());

        return result.substring(0,result.length()-1);
    }

    public static String addPagingToQuery(String query) {
        if (query == null) query = String.format("%s=%d", topPaging, PAG_DEFAULT);
        else if (!query.contains(String.format("%s=", topPaging))) query += String.format("&%s=%d", topPaging, PAG_DEFAULT);
        return query;
    }

    public static void writeToFile(String filename, String s) throws FileNotFoundException {
        if(filename == null){
            System.out.println(s);
        }else{
            try(  PrintWriter file = new PrintWriter(filename)) {
                file.println(s);
            }
        }
    }
}
