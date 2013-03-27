package com.loquatic.crucible.json;

import org.apache.http.StatusLine;

/**
 * Models the response from the request to the end point.
 * 
 * @author jon.svede
 *
 */
public class ResponseData {
	
	private StatusLine statusLine ;
	private String responseString ;
	
	
	public ResponseData(StatusLine statusLine) {
		super();
		this.statusLine = statusLine;
	}

	public int getHttpStatusCode() {
		return statusLine.getStatusCode();
	}
	
	public StatusLine getStatusLine() {
		return statusLine;
	}

	public String getResponseString() {
		return responseString;
	}

	public void setResponseString(String responseString) {
		this.responseString = responseString;
	}

	@Override
	public String toString() {
		return "ResponseData [statusLine=" + statusLine + ", responseString="
				+ responseString + "]" ;
	}
	
	

	
}
