package structures;

import Strutures.Command.CommandBase;
import Strutures.Command.CommandInfo;
import Strutures.ResponseFormat.ResultInfo;
import commands.GetMoviesMid;
import commands.PostMovies;
import console.Manager;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

public class CommandMapTest {

	@Before
	public void before() throws Exception{
		Manager.Init();
	}

	@Test
	public void GetCommandsTest() throws Exception{
		int i =0;
		for (CommandBase cmd : Manager.commandMap.getCommands()) ++i;
		Assert.assertEquals(25,i);
	}

	@Test
	public void should_Get_GetMoviesMid_ICommand_Test()throws Exception{
		CommandInfo command=new CommandInfo(new String[]{"GET","/movies/1"});
		CommandBase cmd=Manager.commandMap.get(command);
		Assert.assertTrue(cmd instanceof GetMoviesMid);
	}

	@Test
	public void should_Get_ResultInfo_GetMoviesMid_Test() throws Exception{
		CommandInfo command=new CommandInfo(new String[]{"GET","/movies/1"});
		CommandBase cmd=Manager.commandMap.get(command);
		Assert.assertTrue(cmd instanceof GetMoviesMid);
		ResultInfo result = cmd.execute(command.getData());
		int a=0;
	}

	@Test
	public void OptionsCommandTest() throws Exception{
		CommandInfo command=new CommandInfo(new String[]{"OPTION","/"});
		Manager.commandMap.get(command).execute(command.getData());
	}

	@Test
	public void shouldBeAbleToGetICommandPostMovies()throws Exception{
		CommandInfo cmdInfo = new CommandInfo(new String[]{"POST","/movies","title=filme1&release_year=2014"});
		Assert.assertTrue(Manager.commandMap.get(cmdInfo) instanceof PostMovies);
	}

}
