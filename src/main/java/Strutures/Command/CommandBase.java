package Strutures.Command;

import Strutures.ResponseFormat.IResultFormat;
import Strutures.ResponseFormat.Plain.TextResult;
import Strutures.ResponseFormat.ResultInfo;
import exceptions.InvalidCommandException;
import templates.ResultFormat;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.function.Function;

public abstract class CommandBase {

    private HashMap<String, ResultFormat> headermap;

    public String getResult(CommandInfo commandInfo, HeaderInfo headerinfo, ResultInfo resultInfo) {
        return headermap.get(headerinfo.getHeadersMap().get("accept")).generate(commandInfo, resultInfo);
    }

    public CommandBase addResultFormat(String hdr, ResultFormat rf){
        headermap.put(hdr,rf);
        return this;
    }

    public CommandBase(){
        headermap = new HashMap<>();
    }

    public CommandBase(HashMap<String, ResultFormat> hm){
        headermap = hm;
    }

    abstract public ResultInfo execute(HashMap<String,String> prmts) throws SQLException, InvalidCommandException;
    abstract public String getInfo();
}
