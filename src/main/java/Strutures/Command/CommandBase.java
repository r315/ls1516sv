package Strutures.Command;

import Strutures.ResponseFormat.IResultFormat;
import Strutures.ResponseFormat.ResultInfo;
import exceptions.InvalidCommandException;

import java.sql.SQLException;
import java.util.HashMap;

public abstract class CommandBase {

    private HashMap<String, IResultFormat> headermap;

    public CommandBase(){
        headermap = new HashMap<>();
    }

    public CommandBase(HashMap<String, IResultFormat> hm){
        headermap = hm;
    }

    public String getResult(CommandInfo commandInfo, HeaderInfo headerinfo, ResultInfo resultInfo) throws SQLException, InvalidCommandException  {
        return headermap.get(headerinfo.getHeaderValue(HeaderInfo.ACCEPT_TOKEN)).generate(resultInfo,commandInfo);
    }

    public CommandBase addResultFormat(String hdr, IResultFormat rf){
        headermap.put(hdr,rf);
        return this;
    }

    abstract public ResultInfo execute(HashMap<String,String> prmts) throws SQLException, InvalidCommandException;

    abstract public String getInfo();
}
