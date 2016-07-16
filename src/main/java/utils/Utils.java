package utils;

import Strutures.Command.CommandBase;
import Strutures.Command.CommandInfo;
import Strutures.Command.HeaderInfo;
import Strutures.ResponseFormat.ResultInfo;
import console.Manager;
import exceptions.InvalidCommandParametersException;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Luigi Sekuiya on 22/04/2016.
 */
public class Utils {

    //General Utils

    public static int getInt (String value) throws NumberFormatException, NullPointerException{
        return Integer.parseInt(value);
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

    //TODO: Check this method
    public static HashMap<String, String> paging(String query, String link){

        //"(skip=)(\\d+)" StringBuffer result = new StringBuffer();

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
        } else {
            if (skipI < 5) { prevSkip = "0"; nextSkip = "5"; }
            else { prevSkip = Integer.toString(skipI-5); nextSkip = Integer.toString(skipI+5); }
        }

        try {
            //Prev
            HeaderInfo headerInfo = new HeaderInfo();
            CommandInfo command;
            CommandBase commandBase;
            ResultInfo ri;

            if ( prevSkip != null) {
                command = new CommandInfo("GET", link, String.format("top=%s&skip=%s", top, prevSkip));
                //resultFormat = (HtmlResult) Manager.executeCommand(command, headerInfo);
                commandBase = Manager.commandMap.get(command);
                ri=commandBase.execute(command.getData());

                if (ri.getValues().isEmpty()) paging.put("prev", null);
                //else paging.put("prev", String.format("%s?top=%s&skip=%s", link, top, prevSkip));
                else paging.put("prev",String.format("%s?%s", link, pagingFormat(query, prevSkip, top)));
            } else paging.put("prev", null);

            //Next
            command = new CommandInfo("GET", link, String.format("top=%s&skip=%s",top,nextSkip));
            commandBase = Manager.commandMap.get(command);
            ri=commandBase.execute(command.getData());

            if (ri.getValues().isEmpty()) paging.put("next", null);
            //else paging.put("next", String.format("%s?top=%s&skip=%s", link, top, nextSkip));
            else paging.put("next",String.format("%s?%s", link, pagingFormat(query, nextSkip, top)));
        }catch (Exception e){

        }

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
