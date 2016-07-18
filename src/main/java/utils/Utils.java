package utils;

import Strutures.Command.CommandBase;
import Strutures.Command.CommandInfo;
import Strutures.Command.HeaderInfo;
import Strutures.ResponseFormat.ResultInfo;
import Strutures.Server.Servlet;
import console.Manager;
import exceptions.InvalidCommandException;
import exceptions.InvalidCommandParametersException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Luigi Sekuiya on 22/04/2016.
 */
public class Utils {

    private static final Logger _logger = LoggerFactory.getLogger(Utils.class);

    //General Utils

    public static int getInt (String value) throws NumberFormatException, NullPointerException{
        return Integer.parseInt(value);
    }

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

    public static Pair<Integer, Integer> getSkipTop (String skip, String top) throws InvalidCommandParametersException {
        int skipI = 0,topI = 1;

        try {
            if (skip != null) skipI = getInt(skip);

            if (top != null) topI = getInt(top);
        } catch (NumberFormatException e) { //If not number
            throw new InvalidCommandParametersException();
        }

        if (skipI < 0 || topI <= 0) throw new InvalidCommandParametersException();

        return new Pair<>(skipI,topI);
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

        if (skip == null || (skipI = getInt(skip)) == 0) {
            nextSkip = "5";
            paging.put("prev", null);
        } else {
            if (skipI < 5) { prevSkip = "0"; nextSkip = "5"; }
            else { prevSkip = Integer.toString(skipI-5); }

            nextSkip = Integer.toString(skipI+5);
            paging.put("prev",String.format("%s?%s", link, pagingFormat(query, prevSkip, top)));
        }
            /*if ( prevSkip != null) {
                command = new CommandInfo("GET", link, String.format("top=%s&skip=%s", top, prevSkip));
                commandBase = Manager.commandMap.get(command);
                ri=commandBase.execute(command.getData());

                if (ri.getValues().isEmpty()) paging.put("prev", null);
                //else paging.put("prev", String.format("%s?top=%s&skip=%s", link, top, prevSkip));
                else paging.put("prev",String.format("%s?%s", link, pagingFormat(query, prevSkip, top)));
            } else paging.put("prev", null);*/

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
