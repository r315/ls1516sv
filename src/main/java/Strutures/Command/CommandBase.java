package Strutures.Command;

import Strutures.ResponseFormat.IResultFormat;
import Strutures.ResponseFormat.Plain.TextResult;
import Strutures.ResponseFormat.ResultInfo;
import exceptions.InvalidCommandException;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.function.Function;

public abstract class CommandBase {

    private HashMap<String, IResultFormat> headermap;

    public String getResult(HeaderInfo headerinfo, ResultInfo resultInfo) {
        return headermap.get(headerinfo.getHeadersMap().get("accept")).generate(resultInfo);
    }

    public CommandBase addResultFormat(String hdr, IResultFormat rf){
        headermap.put(hdr,rf);
        return this;
    }

    public CommandBase(){
        headermap = new HashMap<>();
        headermap.put("text/html",new TextResult());
    }

    abstract public ResultInfo execute(HashMap<String,String> prmts) throws SQLException, InvalidCommandException;
    abstract public String getInfo();
}
