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

public class SummarizeReviewAction extends AbstractAction {

	
	
	public SummarizeReviewAction( IProtocolHandler myHandler ) {
		super( myHandler ) ;
		setAction( Action.SUMMARIZE_REVIEW ) ;
	}

	@Override
	public boolean perform( CommandLine commandLine, Properties props ) {
		
		boolean success = false ;
		
		String reviewId = commandLine.getOptionValue( CommandLineOption.REVIEW_ID.getName() ) ;
		
		StringBuilder url = TargetUrlUtil.createReviewUrl(  props ) ;
		
		url.append( reviewId ).append( "/transition?action=action:summarizeReview" ).append( "&" ).append("FEAUTH=").append(getToken() ) ;
		
		//System.out.println( "URL: " + url.toString() ) ;
		
		try {
			ResponseData response = getHandler().doPost( url.toString() ) ;
			
			if( response.getHttpStatusCode() >= 200 && response.getHttpStatusCode() < 300 ) {
				System.out.println( "Successfully summarized review " + reviewId ) ;
				success = true ;
			} else {
				System.out.println( "Unable to summarize review " + reviewId + ". Info: " + response.getStatusLine() ) ;
				success = false ;
			}
		} catch ( Exception e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
		
		return success ;
	}

	@Override
	public boolean addOptions( Options options ) {
		
		options.addOption( CommandLineOption.REVIEW_ID.getName(), true, "The ID of the review you with to summarize." ) ;
		
		
		return true ;
	}

	@Override
	public void printHelp() {
		// TODO Auto-generated method stub
		
	}
}
