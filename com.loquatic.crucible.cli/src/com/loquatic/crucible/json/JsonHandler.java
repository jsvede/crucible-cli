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
