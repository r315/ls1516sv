package Strutures.Command;

import Strutures.ResponseFormat.IResultFormat;
import Strutures.ResponseFormat.ResultInfo;
import exceptions.InvalidCommandException;

import java.sql.SQLException;
import java.util.HashMap;

public abstract class CommandBase {

    private HashMap<String, IResultFormat> headermap;

    public String getResult(CommandInfo commandInfo, HeaderInfo headerinfo, ResultInfo resultInfo) {
        return headermap.get(headerinfo.getHeadersMap().get("accept")).generate(resultInfo,commandInfo);
    }

    public CommandBase addResultFormat(String hdr, IResultFormat rf){
        headermap.put(hdr,rf);
        return this;
    }

    public CommandBase(){
        headermap = new HashMap<>();
    }

    public CommandBase(HashMap<String, IResultFormat> hm){
        headermap = hm;
    }

    abstract public ResultInfo execute(HashMap<String,String> prmts) throws SQLException, InvalidCommandException;
    abstract public String getInfo();
}
