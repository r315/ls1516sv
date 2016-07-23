package console;

import Strutures.Command.CommandInfo;
import Strutures.Command.HeaderInfo;
import exceptions.InvalidCommandException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.DataBase;

import java.sql.SQLException;

/**
 * Created by hmr on 23/07/2016.
 */
public class ConsoleTest {



    @Test
    public void shouldGetMoviesDefaultHtml() throws SQLException, InvalidCommandException {
        System.out.println(executeCommand("GET /movies"));
    }

    @Test
    public void shouldGetMoviesText() throws SQLException, InvalidCommandException {
        System.out.println(executeCommand("GET /movies accept:text/plain"));
    }

    @Test
    public void shouldGetMoviesHtml() throws SQLException, InvalidCommandException {
        System.out.println(executeCommand("GET /movies accept:text/hmtl"));
    }

    @Test
    public void shouldPostMovie() throws SQLException, InvalidCommandException {
        System.out.println(executeCommand("Post /movies title=TestMovie&releaseYear=2016"));
    }

    @BeforeClass
    public static void setUp() throws InvalidCommandException {
        Manager.Init();
    }

    @AfterClass
    public static void cleanUp(){
        DataBase.removeTestMovie();
    }

    private String executeCommand(String cmd) throws InvalidCommandException, SQLException {
        String[] args = cmd.split(" ");
        HeaderInfo headerInfo = new HeaderInfo(args);
        CommandInfo command = new CommandInfo(args);
        return Manager.executeCommand(command, headerInfo);
    }
}
