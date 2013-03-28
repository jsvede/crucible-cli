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
package com.loquatic.crucible.cli;

import java.util.HashMap ;
import java.util.Properties ;

import org.apache.commons.cli.CommandLine ;
import org.apache.commons.cli.Options ;

import com.loquatic.crucible.cli.actions.AbandonReviewAction ;
import com.loquatic.crucible.cli.actions.CloseReviewAction ;
import com.loquatic.crucible.cli.actions.CreateAuthTokenAction ;
import com.loquatic.crucible.cli.actions.CreateReviewAction ;
import com.loquatic.crucible.cli.actions.IAction ;
import com.loquatic.crucible.cli.actions.SummarizeAndCloseReviewAction ;
import com.loquatic.crucible.cli.actions.SummarizeReviewAction ;
import com.loquatic.crucible.json.IProtocolHandler ;

/**
 * This dispatcher routes user input to the specified action.
 *  
 * @author jon.svede
 *
 */
public class ActionDispatcher {
	
	private HashMap<String, IAction> actions ;
	
	public ActionDispatcher( IProtocolHandler handler ) {
		actions = new HashMap<String,IAction>() ;
		actions.put(Action.CREATE_REVIEW.getName(), new CreateReviewAction(handler) ) ;
		actions.put( Action.CREATE_AUTH_TOKEN.getName(), new CreateAuthTokenAction(handler) ) ;
		actions.put( Action.ABANDON_REVIEW.getName(), new AbandonReviewAction(handler) ) ; 
		actions.put( Action.CLOSE_REVIEW.getName(), new CloseReviewAction(handler) ) ;
		actions.put( Action.SUMMARIZE_REVIEW.getName(), new SummarizeReviewAction(handler) ) ;
		actions.put( Action.SUMMARIZE_AND_CLOSE_REVIEW.getName(), new SummarizeAndCloseReviewAction(handler) ) ;
	}
	
	/**
	 * Returns the list of actions registered with the dispatcher.
	 * 
	 * @return
	 */
	public HashMap<String, IAction> getRegisteredActions() {
		return actions ;
	}
	 
	public void addOptionsForActions( Options options ) {
		for( IAction action:actions.values() ) {
			action.addOptions( options ) ;
		}
	}
	
	/**
	 * Dispatch 
	 * @param args
	 */
	public boolean dispatchToAction( Action action, CommandLine commandLine, Properties props ) {
		
		boolean success = false ;
		
		if( action != null ) {
			IAction myAction = actions.get( action.getName() ) ;
			success = myAction.perform( commandLine, props ) ;
		}
		
		return success ;
		
	}
	
	public void callHelp( Action action, Options options ) {
		if( action != null ) {
			System.out.println( action.getName() ) ;
			IAction myAction = actions.get( action.getName() ) ;
			myAction.addOptions( options ) ;
			myAction.printHelp() ;
		}
		
	}

}
