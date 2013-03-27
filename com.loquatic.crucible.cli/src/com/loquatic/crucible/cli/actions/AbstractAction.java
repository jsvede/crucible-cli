package com.loquatic.crucible.cli.actions;

import java.io.BufferedReader ;
import java.io.File ;
import java.io.FileNotFoundException ;
import java.io.FileReader ;
import java.io.IOException ;

import org.codehaus.jackson.JsonParseException ;
import org.codehaus.jackson.map.JsonMappingException ;
import org.codehaus.jackson.map.ObjectMapper ;
import org.codehaus.jackson.map.SerializationConfig ;

import com.loquatic.crucible.json.IProtocolHandler ;

public abstract class AbstractAction implements IAction {
	
	private String token ;
	private String tokenFileName ;
	private File tokenFile ;
	
	protected IProtocolHandler handler ;
	
	public AbstractAction() {
		String userHomeDir = System.getProperty( "user.home" ) ;
		String tknFileName = "crucible.tkn" ;

		StringBuilder fileNameBuilder = new StringBuilder() ;
		fileNameBuilder.append( userHomeDir ).append( File.separator).append( tknFileName ) ;
		
		tokenFileName = fileNameBuilder.toString() ;
		
		tokenFile = new File( tokenFileName ) ;
		
		if( tokenExists() ) {
			loadToken() ;
		}
	}
	
	public AbstractAction( IProtocolHandler myHandler ) {
		this() ;
		setHandler( myHandler ) ;
	}
	
	public void setHandler( IProtocolHandler myHandler ) {
		handler = myHandler ;
	}
	
	public IProtocolHandler getHandler() {
		return handler ;
	}

	protected boolean tokenExists() {
		if( tokenFile.exists() && tokenFile.canRead() ) {
			return true ;
		}
		return false ;
	}
	
	protected void appendAuthToken( StringBuilder url ) {
		url.append( "?" ).append("FEAUTH=").append( getToken() ) ;
	}
	
	protected void loadToken() {
		
		try {
			BufferedReader bfrdFileReader = new BufferedReader( new FileReader( tokenFile ) ) ;
			
			// the file should only have the one line!
			token = bfrdFileReader.readLine() ;
			
			//System.out.println( "token loaded: " + token + " from file " + tokenFile ) ;
			
			bfrdFileReader.close() ;
		} catch ( FileNotFoundException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch ( IOException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected String getToken() {
		if( token == null ) {
			loadToken() ;
		}
		return token ;
	}
	
	protected String createJsonString(Object objToConvert) throws Exception {
		ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
		mapper.configure(SerializationConfig.Feature.WRITE_NULL_MAP_VALUES, false);
		mapper.configure(SerializationConfig.Feature.WRITE_NULL_PROPERTIES, false);
		String output = mapper.writeValueAsString(objToConvert);

		return output;
	}
	
	protected Object createObjectFromString( String json, Object target ) {
		ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally

		try {
			target = mapper.readValue( json, target.getClass() ) ;
		} catch ( JsonParseException e ) {
			e.printStackTrace();
		} catch ( JsonMappingException e ) {
			e.printStackTrace();
		} catch ( IOException e ) {
			e.printStackTrace();
		}
		
		return target ;
	}

}
