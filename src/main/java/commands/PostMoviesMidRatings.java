package commands;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

public class PostMoviesMidRatings implements ICommand {

	/*
		POST /movies/{mid}/reviews - creates a new review for the movie identified by mid, given the following parameters
		reviewerName - the reviewer name
		reviewSummary - the review summary
		review - the complete review
		rating - the review rating
	*/
	@Override
	public void execute(Collection<String> args, HashMap<String, String> prmts)
			throws SQLException {


	}

}
