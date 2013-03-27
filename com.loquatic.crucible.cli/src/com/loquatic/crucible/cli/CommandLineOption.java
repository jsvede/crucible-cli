package com.loquatic.crucible.cli;

public enum CommandLineOption {
	
	ACTION("action" ),
	USERNAME( "username" ),
	PASSWORD( "password" ),
	CONFIG( "config" ),
	CHANGESET( "changeset" ),
	CACHE_AUTH( "cacheAuth" ),
	HELP( "help" ),
	PROJECT_KEY( "projectKey" ),
	REVIEW_NAME( "reviewName" ),
	DESCRIPTION( "description" ),
	ALLOW_OTHERS_TO_JOIN( "allowOthersToJoin" ),
	REVIEWERS( "reviewers"),
	REPOSITORY( "repository" ),
	REVIEW_ID( "reviewId" )
	;
	
	
	private String name ;
	
	
	private CommandLineOption( String n ) {
		name = n ;
	}
	
	public String getName() {
		return name ;
	}
	
	

}
