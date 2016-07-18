package Strutures.ResponseFormat;

import Strutures.Command.CommandInfo;
import exceptions.InvalidCommandException;

import java.sql.SQLException;

/**
 * Created by Red on 18/05/2016.
 */
public interface IResultFormat {
    String generate(ResultInfo ri, CommandInfo ci) throws SQLException, InvalidCommandException;
}
