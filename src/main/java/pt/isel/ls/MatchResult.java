package pt.isel.ls;

import java.util.Map;

/**
 * Created by pedro on 06/04/16.
 */
public abstract class MatchResult {

    private final boolean _success;

    protected MatchResult(boolean success){
        _success = success;
    }

    public final boolean isMatch(){
        return _success;
    }

    public abstract void addTo(Map<String, String> vars);
}
