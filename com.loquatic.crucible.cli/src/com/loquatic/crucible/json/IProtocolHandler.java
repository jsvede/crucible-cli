package com.loquatic.crucible.json ;

public interface IProtocolHandler {
	
	public ResponseData doGet( String url ) throws Exception ;
	
	public ResponseData doPost(  String url ) throws Exception ;
	
	public ResponseData doPost( String jsonString, String url ) throws Exception ;

}