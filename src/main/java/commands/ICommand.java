package commands;


import Strutures.Result;

import java.util.HashMap;

public interface ICommand {

    Result execute(HashMap<String, String> data) throws Exception;
}
