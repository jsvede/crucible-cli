package com.loquatic.crucible.cli.actions;

import java.util.Properties ;

import org.apache.commons.cli.CommandLine ;
import org.apache.commons.cli.Options ;

import com.loquatic.crucible.cli.CommandLineOption ;
import com.loquatic.crucible.json.IProtocolHandler ;
import com.loquatic.crucible.json.JsonHandler ;
import com.loquatic.crucible.json.ResponseData ;
import com.loquatic.crucible.util.TargetUrlUtil ;

public class CloseReviewAction extends AbstractAction {

	
	public CloseReviewAction( IProtocolHandler myHandler ) {
		super( myHandler ) ;
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
	public void printHelp() {
		// TODO Auto-generated method stub
		
	}
}
