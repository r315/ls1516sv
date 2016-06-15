package utils;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Strutures.Command.CommandInfo;
import Strutures.Command.HeaderInfo;
import Strutures.ResponseFormat.Html.HtmlResult;
import console.Manager;
import exceptions.InvalidCommandParametersException;

/**
 * Created by Luigi Sekuiya on 22/04/2016.
 */
public class Utils {

    public static int getInt (Object value) throws NumberFormatException, NullPointerException{
        return Integer.parseInt(getString(value));
    }

    public static String getString (Object value) throws NullPointerException{
        return value.toString();
    }

    public static HashMap<String, Integer> getSkipTop (String skip, String top) throws InvalidCommandParametersException {
        HashMap<String, Integer> map = new HashMap<>();
        int skipI = 0,topI = 1;

        try {
            skipI = Utils.getInt(skip);
        } catch (NumberFormatException | NullPointerException e) { //If not number OR null
            if (skip != null) throw new InvalidCommandParametersException();
        }

        try {
            topI = Utils.getInt(top);
        } catch (NumberFormatException | NullPointerException e) { //If not number OR null
            if (top != null) throw new InvalidCommandParametersException();
        }

        if (skipI < 0 || topI <= 0) throw new InvalidCommandParametersException();

        map.put("skip",skipI);
        map.put("top",topI);

        return map;
    }

    public static HashMap<String, String> paging (String query, String link) {

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
            HtmlResult resultFormat;

            if ( prevSkip != null) {

                command = new CommandInfo("GET", link, String.format("top=%s&skip=%s", top, prevSkip));
                resultFormat = (HtmlResult) Manager.executeCommand(command, headerInfo);

                if (resultFormat.resultInfo.getValues().isEmpty()) paging.put("prev", null);
                //else paging.put("prev", String.format("%s?top=%s&skip=%s", link, top, prevSkip));
                else paging.put("prev",String.format("%s?%s", link, pagingFormat(query, prevSkip, top)));
            } else paging.put("prev", null);
            //Next

            command = new CommandInfo("GET", link, String.format("top=%s&skip=%s",top,nextSkip));
            resultFormat = (HtmlResult) Manager.executeCommand(command, headerInfo);

            if (resultFormat.resultInfo.getValues().isEmpty()) paging.put("next", null);
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

    public static HashMap<String, String> specialPaging (String query, String link) {
        HashMap<String, String> special = paging (query, link);
        removeTop(special, "prev");
        removeTop(special, "next");

        return special;
    }

    private static void removeTop(HashMap<String, String> special, String key) {
        if (special.get(key) != null) {
            Pattern p = Pattern.compile("(top=)(\\d+)(&)");
            Matcher m = p.matcher(special.get(key));

            special.replace(key,m.replaceFirst(""));
        }
    }

}
