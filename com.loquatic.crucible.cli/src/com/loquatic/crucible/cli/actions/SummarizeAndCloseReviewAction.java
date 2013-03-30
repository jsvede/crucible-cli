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

import java.util.LinkedList ;
import java.util.Properties ;

import org.apache.commons.cli.CommandLine ;
import org.apache.commons.cli.Options ;

import com.loquatic.crucible.cli.Action;
import com.loquatic.crucible.json.IProtocolHandler ;

public class SummarizeAndCloseReviewAction extends AbstractAction {
	
	private LinkedList<IAction> actions ;
	
	
	
	public SummarizeAndCloseReviewAction( IProtocolHandler handler ) {
		actions = new LinkedList<IAction>() ;
		actions.add( new SummarizeReviewAction( handler ) ) ;
		actions.add( new CloseReviewAction( handler ) ) ;
		
		setAction( Action.SUMMARIZE_AND_CLOSE_REVIEW ) ;
	}

	@Override
	public boolean perform( CommandLine commandLine, Properties props ) {
		
		boolean success = false ;
		
		for( IAction action : actions ) {
			success = action.perform( commandLine, props ) ;
			if( !success ) {
				break ;
			}
		}
		return success ;
	}

	@Override
	public boolean addOptions( Options options ) {
		for( IAction action : actions ) {
			action.addOptions( options ) ;
		}
		return true ;
	}

	@Override
	public void printHelp() {
		// TODO Auto-generated method stub

	}

}
