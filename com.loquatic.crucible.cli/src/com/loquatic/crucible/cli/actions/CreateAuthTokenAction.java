/*
 * Copyright (c) 2013, Loquatic Software, LLC
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *
 *    * Redistributions of source code must retain the above copyright notice, 
 *      this list of conditions and the following disclaimer.
 *    * Redistributions in binary form must reproduce the above copyright 
 *      notice, this list of conditions and the following disclaimer in the 
 *      documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 *
 */
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
		String tokenFileName = "crucible-cli.tkn" ;
		
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
		
		myOptions.addOption( CommandLineOption.USERNAME.getName(),
        true,
       "The Crucible username under which to " +
       "perform this action; required if the app " +
       "hasn't stored an authentication token.");

		myOptions.addOption( CommandLineOption.PASSWORD.getName(), 
        true,
        "The password associated with the username provided.");

		return true;
	}

	@Override
	public String getHelpOverview() {
		StringBuilder helpOverviewSb = new StringBuilder() ;
		helpOverviewSb.append(  "Creates an auth token using Crucible's built in facility for this. \nStores the token to your home dir as 'crucible-cli.tkn'.\n" ) ;
		helpOverviewSb.append( "The auth token is valid as long as you don't log out of Crucible. \n\nSee the docs at: https://developer.atlassian.com/display/FECRUDEV/Authenticating+REST+Requests\n") ;
		return helpOverviewSb.toString() ;
	}

	@Override
	public String getHelpExamples() {
		StringBuilder helpExampleSb = new StringBuilder() ;
		helpExampleSb.append(  "--action createToken --username user1 --password passwordForUser1" ) ;
		return helpExampleSb.toString() ;
	}


}
