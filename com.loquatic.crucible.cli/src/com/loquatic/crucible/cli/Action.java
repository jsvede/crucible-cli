package com.loquatic.crucible.cli;

import java.util.HashMap;

/**
 * Valid actions to used with the --action argument.
 * 
 * @author jon.svede
 *
 */
public enum Action {
	
	CREATE_REVIEW( "createReview" ),
	SUMMARIZE_REVIEW( "summarizeReview" ) ,
	CLOSE_REVIEW( "closeReview" ),
	CREATE_AUTH_TOKEN( "createToken" ),
	ABANDON_REVIEW( "abandonReview" ),
	SUMMARIZE_AND_CLOSE_REVIEW("summarizeAndCloseReview" ),
	ADD_REVIEWERS_TO_REVIEW( "addReviewers" ),
	ADD_CHANGESETS_TO_REVIEW( "addChangesets" );
	
	private String name ;
	
	private static HashMap<String, Action> actionMap= new HashMap<String, Action>() ;
	
	static {
		Action[] myActions = Action.values() ;
		for( Action a: myActions ) {
			actionMap.put( a.getName(), a ) ;
		}
	}
	private Action( String n ) {
		name = n ;
	}
	
	public String getName() {
		return name ;
	}
	
	public static Action findByName( String name ) {
		return actionMap.get( name ) ;
	}

}
