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
    public void shouldGetTopMoviesHtml() throws SQLException, InvalidCommandException {
        System.out.println(executeCommand("GET /movies accept:text/hmtl top=5"));
    }

    @Test
    public void shouldGetMoviesSortedText() throws SQLException, InvalidCommandException {
        System.out.println(executeCommand("GET /movies accept:text/plain sortBy=title"));
    }

    @Test
    public void shouldPostMovie() throws SQLException, InvalidCommandException {
        System.out.println(executeCommand("POST /movies title=TestMovie&releaseYear=2016"));
    }

    @Test
    public void shouldPostColection() throws SQLException, InvalidCommandException {
        executeCommand("POST /collections name=Testcollection1&description=collection_for_testing");
    }

    @Test
    public void shouldPostMovieOnColection() throws SQLException, InvalidCommandException {
        executeCommand("POST /collections/1/movies mid=1");
    }

    @Test
    public void shouldPostRating() throws SQLException, InvalidCommandException {
        executeCommand("POST /movies/1/ratings rating=5");
    }

    @Test
    public void shouldPostReview() throws SQLException, InvalidCommandException {
        executeCommand("POST /movies/1/reviews reviewerName=isel&reviewSummary=thisisasummary&review=thefullreview&rating=4");
    }

    @BeforeClass
    public static void setUp() throws InvalidCommandException, SQLException {
        Manager.Init();
        DataBase.clear();

        DataBase.createCollection(false);
    }

    @AfterClass
    public static void cleanUp() throws SQLException {
        DataBase.clear();
    }

    private String executeCommand(String cmd) throws InvalidCommandException, SQLException {
        String[] args = cmd.split(" ");
        HeaderInfo headerInfo = new HeaderInfo(args);
        CommandInfo command = new CommandInfo(args);
        return Manager.executeCommand(command, headerInfo);
    }
}
