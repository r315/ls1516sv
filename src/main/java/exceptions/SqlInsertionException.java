package exceptions;

import java.sql.SQLException;

public class SqlInsertionException extends SQLException {	
	public SqlInsertionException(){
		super();
	}
	
	public SqlInsertionException(String e){
		super(e);		
	}
}
