package utils;

import Strutures.Command.CommandInfo;
import Strutures.ResponseFormat.ResultInfo;
import console.Manager;
import exceptions.InvalidCommandException;
import exceptions.InvalidCommandParametersException;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    //General Utils

    public static String reconQuery (Map<String,String> map){
        String result="";
        if(map.isEmpty())return result;
        for (Map.Entry s:map.entrySet())
            result += String.format("%s=%s&",s.getKey(),s.getValue());
        return result.substring(0,result.length()-1);
    }

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

    public static HashMap<String, String> paging(String query, String link) throws SQLException, InvalidCommandException{
        HashMap<String, String> paging = new HashMap<>();

        String nextSkip, prevSkip = null;
        String top = "5";
        String skip = null;

        if (query != null) {
            Pattern p = Pattern.compile("(skip=)(\\d+)");
            Matcher m = p.matcher(query);

            if (m.find()) skip = m.group(2);
        }

        int skipI;
        try {
            if (skip == null || (skipI = Integer.parseInt(skip)) == 0) {
                nextSkip = "5";
                paging.put("prev", null);
            } else {
                if (skipI < 5) {
                    prevSkip = "0";
                } else {
                    prevSkip = Integer.toString(skipI - 5);
                }

                nextSkip = Integer.toString(skipI + 5);
                paging.put("prev", String.format("%s?%s", link, pagingFormat(query, prevSkip, top)));
            }
        } catch (NumberFormatException e) { //If not number
            throw new InvalidCommandParametersException("Invalid value for 'Skip'");
        }

            //Next
            CommandInfo command = new CommandInfo("GET", link, String.format("top=%s&skip=%s",top,nextSkip));
            ResultInfo ri = Manager.commandMap.get(command).execute(command.getData());

            if (ri.getValues().isEmpty()) paging.put("next", null);
            //else paging.put("next", String.format("%s?top=%s&skip=%s", link, top, nextSkip));
            else paging.put("next",String.format("%s?%s", link, pagingFormat(query, nextSkip, top)));

        return paging;
    }

    private static String pagingFormat(String query, String skip, String top) {

        Pattern p;
        Matcher m;

        if (query == null) query = "top=" + top;
        else {
            p = Pattern.compile("(top=)(\\d+)");
            m = p.matcher(query);

            if (m.find()) query = m.replaceFirst(m.group(1) + top);
            else query += "&top=" + top;
        }

        p = Pattern.compile("(skip=)(\\d+)");
        m = p.matcher(query);

        if (m.find()) query = m.replaceFirst(m.group(1) + skip);
        else query += "&skip=" + skip;

        return query;
    }

}
