package com.loquatic.crucible.cli.actions;

import java.io.BufferedWriter ;
import java.io.File ;
import java.io.FileWriter ;
import java.io.IOException ;
import java.util.Properties ;

import org.apache.commons.cli.CommandLine ;
import org.apache.commons.cli.Options ;
import org.codehaus.jackson.JsonParseException ;
import org.codehaus.jackson.map.JsonMappingException ;
import org.codehaus.jackson.map.ObjectMapper ;

import com.loquatic.crucible.cli.Action;
import com.loquatic.crucible.cli.CommandLineOption ;
import com.loquatic.crucible.json.IProtocolHandler ;
import com.loquatic.crucible.json.ResponseData ;
import com.loquatic.crucible.rest.api.AuthToken ;
import com.loquatic.crucible.util.TargetUrlUtil ;

public class CreateAuthTokenAction extends AbstractAction {

	
	public CreateAuthTokenAction( IProtocolHandler myHandler ) {
		super( myHandler ) ;
		setAction( Action.CREATE_AUTH_TOKEN ) ;
	}

	@Override
	public boolean perform(CommandLine commandLine, Properties props) {
		boolean success = false ;
		
		try {
			String response = createAuthToken( commandLine, props ) ;
			
			String token = parseResponse( response ) ;
			success = writeTokenToFile( token ) ;
		} catch ( Exception e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			success = false ;
		}
		
		
		
		return success;
	}
	
	private String createAuthToken(CommandLine commandLine, Properties props ) throws Exception {
		StringBuilder authUrl = TargetUrlUtil.createAuthTokenUrl( props ) ;
		String userName = commandLine.getOptionValue( CommandLineOption.USERNAME.getName() ) ;
		String password = commandLine.getOptionValue( CommandLineOption.PASSWORD.getName() ) ;
		authUrl.append( "?" ).append("userName=").append( userName ) ;
		authUrl.append( "&" ).append("password=").append( password ) ;
		
		System.out.println( "sending: " + authUrl.toString() ) ;
		ResponseData response = getHandler().doGet( authUrl.toString() ) ;
		
		if( response.getHttpStatusCode() != 200 ) {
			throw new Exception( "Unable to create auth token: " + response ) ;
		}
		
		return response.getResponseString() ;
	}
	
	
	private String parseResponse( String jsonString ) throws JsonParseException, JsonMappingException, IOException {
		
		ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
		
		AuthToken token = mapper.readValue(jsonString, AuthToken.class ) ;
		
	  return token.getToken() ;
	}
	
	private boolean writeTokenToFile( String tokenValue )  {
		
		boolean success = false ;
		String userHomeDir = System.getProperty( "user.home" ) ;
		String tokenFileName = "crucible.tkn" ;
		
		File tokenFile = new File( userHomeDir + File.separator + tokenFileName ) ;
		
		if( tokenFile.exists() ) {
			tokenFile.delete() ;
		}
		
		try {
			BufferedWriter wb = new BufferedWriter( new FileWriter( tokenFile ) ) ;
			
			wb.write( tokenValue ) ;
			
			wb.flush() ;
			
			wb.close() ;
			
			success = true ;
			
 			System.out.println( "wrote token to " + tokenFileName ) ;
 			
		} catch ( IOException e ) {
			e.printStackTrace() ;
		}
		
		return success ;
	}

	@Override
	public boolean addOptions(Options options) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void printHelp() {
		// TODO Auto-generated method stub

	}

}
