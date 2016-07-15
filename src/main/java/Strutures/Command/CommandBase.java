package Strutures.Command;

import Strutures.ResponseFormat.IResultFormat;
import Strutures.ResponseFormat.ResultInfo;
import exceptions.InvalidCommandException;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.function.Function;

public abstract class CommandBase {
    abstract public ResultInfo execute (HashMap<String,String> prmts) throws SQLException, InvalidCommandException;
    abstract public String getInfo();
    private HashMap<String, IResultFormat> headermap;
    private IResultFormat resultFormat;

    public ResultInfo getResultInfo() {
        return resultInfo;
    }

    protected ResultInfo resultInfo;

    public String getResult(String header) throws Exception {
        return headermap.get(header).generate(resultInfo);
    }

    public CommandBase(){

    }


    public CommandBase(HashMap<String,IResultFormat> hdrmap){
        headermap = hdrmap;
    }
}
