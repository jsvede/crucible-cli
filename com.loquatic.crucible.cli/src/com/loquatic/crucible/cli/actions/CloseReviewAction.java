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

import java.util.Properties ;

import org.apache.commons.cli.CommandLine ;
import org.apache.commons.cli.Options ;

import com.loquatic.crucible.cli.Action;
import com.loquatic.crucible.cli.CommandLineOption ;
import com.loquatic.crucible.json.IProtocolHandler ;
import com.loquatic.crucible.json.JsonHandler ;
import com.loquatic.crucible.json.ResponseData ;
import com.loquatic.crucible.util.TargetUrlUtil ;

public class CloseReviewAction extends AbstractAction {

	
	public CloseReviewAction( IProtocolHandler myHandler ) {
		super( myHandler ) ;
		setAction( Action.CLOSE_REVIEW ) ;
	}

	@Override
	public boolean perform( CommandLine commandLine, Properties props ) {
		
		boolean success = false ;
		
		String reviewId = commandLine.getOptionValue( CommandLineOption.REVIEW_ID.getName() ) ;
		
		StringBuilder url = TargetUrlUtil.createReviewUrl(  props ) ;
		
		url.append( reviewId ).append( "/transition?action=action:closeReview" ).append( "&" ).append("FEAUTH=").append(getToken() ) ;
		
		//System.out.println( "URL: " + url.toString() ) ;
		
		try {
			ResponseData response = getHandler().doPost( url.toString() ) ;
			
			if( response.getHttpStatusCode() >= 200 && response.getHttpStatusCode() < 300 ) {
				System.out.println( "Successfully closed review " + reviewId ) ;
				success = true ;
			} else {
				System.out.println( "Unable to close review " + reviewId + ". Info: " + response.getStatusLine() ) ;
			}
		} catch ( Exception e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
		
		return success ;
	}

	@Override
	public boolean addOptions( Options options ) {
		
		options.addOption( CommandLineOption.REVIEW_ID.getName(), true, "The ID of the review you wish to close." ) ;
		
		
		return true ;
	}

	@Override
	public String getHelpOverview() {
		StringBuilder helpOverviewSb = new StringBuilder() ;
		helpOverviewSb.append(  "Close a review. The review may or may not need to be summarized prior to closing the review.\n" ) ;
		return helpOverviewSb.toString() ;
	}

	@Override
	public String getHelpExamples() {
		StringBuilder helpExamplesSb = new StringBuilder() ;
		helpExamplesSb.append( "--action closeReview --reviewId FOO-ID" ) ;
		return helpExamplesSb.toString() ;
	}

	
}
