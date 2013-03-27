package com.loquatic.crucible.json ;

import java.io.BufferedReader ;
import java.io.InputStreamReader ;

import org.apache.http.HttpResponse ;
import org.apache.http.client.HttpClient ;
import org.apache.http.client.methods.HttpGet ;
import org.apache.http.client.methods.HttpPost ;
import org.apache.http.entity.StringEntity ;
import org.apache.http.impl.client.DefaultHttpClient ;

/**
 * Primitive JSON handler. It's designed to send and receive JSON without
 * consideration for the state or status of the request/response.
 * 
 * @author jon.svede
 * 
 */
public class JsonHandler implements IProtocolHandler {

	public JsonHandler() {}
	
	public ResponseData doGet( String url )
			throws Exception {
		HttpClient client = new DefaultHttpClient() ;
		HttpGet request = new HttpGet( url ) ;

		request.addHeader( "accept", "application/json" ) ;

		HttpResponse response = client.execute( request ) ;

		ResponseData responseData = new ResponseData( response.getStatusLine() ) ;
		
		StringBuilder sb = new StringBuilder()
		;
		if ( response != null && response.getEntity() != null ) {
			BufferedReader rd = new BufferedReader( new InputStreamReader( response
					.getEntity().getContent() ) ) ;
			sb = new StringBuilder() ;
			String line = "" ;
			while ( ( line = rd.readLine() ) != null ) {
				sb.append( line ) ;
			}
		}
		responseData.setResponseString( sb.toString() ) ;

		return responseData ;
	}

	public ResponseData doPost(  String url ) throws Exception {
		return doPost( null, url ) ;
	}
	
	public ResponseData doPost( String jsonString, String url ) throws Exception {
		HttpClient client = new DefaultHttpClient() ;

		StringEntity requestEntity = null ;
		
		if ( jsonString != null  ) {
			//System.out.println( "JSON data: " + jsonString ) ;
			requestEntity = new StringEntity( jsonString, "application/json", "UTF-8" ) ;
		}
		
		HttpPost request = new HttpPost( url  ) ;
		request.setEntity( requestEntity ) ;
		request.addHeader( "accept", "application/json" ) ;

		StringBuilder sb = new StringBuilder() ;

		HttpResponse response = client.execute( request ) ;

		ResponseData responseData = new ResponseData( response.getStatusLine() ) ;

		// poorly handled response, but in some cases the server doesn't respond.
		if ( response != null && response.getEntity() != null ) {
			BufferedReader rd = new BufferedReader( new InputStreamReader( response
					.getEntity().getContent() ) ) ;
			String line = "" ;
			while ( ( line = rd.readLine() ) != null ) {
				sb.append( line ) ;
			}
		}

		responseData.setResponseString( sb.toString() ) ;
		
		return responseData;

	}
		
}
