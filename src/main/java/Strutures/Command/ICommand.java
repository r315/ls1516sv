package Strutures.Command;

import Strutures.ResponseFormat.ResultInfo;
import exceptions.InvalidCommandException;

import java.sql.SQLException;
import java.util.HashMap;

public interface ICommand {
    ResultInfo execute (HashMap<String,String> prmts) throws SQLException, InvalidCommandException;
    String getInfo();
}
