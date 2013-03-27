package com.loquatic.crucible.cli.actions;

import java.util.Properties ;

import org.apache.commons.cli.CommandLine ;
import org.apache.commons.cli.HelpFormatter ;
import org.apache.commons.cli.Options ;

import com.loquatic.crucible.cli.CommandLineOption ;
import com.loquatic.crucible.json.IProtocolHandler ;
import com.loquatic.crucible.json.JsonHandler ;
import com.loquatic.crucible.json.ResponseData ;
import com.loquatic.crucible.rest.api.AddChangeset ;
import com.loquatic.crucible.rest.api.AddChangesetWrapper ;
import com.loquatic.crucible.rest.api.ChangesetData ;
import com.loquatic.crucible.rest.api.ReviewData ;
import com.loquatic.crucible.rest.api.State ;
import com.loquatic.crucible.rest.api.UserData ;
import com.loquatic.crucible.util.TargetUrlUtil ;




/**
 * Creates a review.
 * <p>
 * Arguments:
 * <ul>
 * <li>changeset - the commit # from the SCM</li>
 * <li>projectKey - the key as specified in Crucible</li>
 * <li>reviewName - the name/title of the review; defaults 
 *                                       review of changeSetNumber</li>
 * <li>description - required, a description of the review</li>
 * <li>allowOthersToJoin - optional, defaults to true</li>
 * </ul>
 * <p>
 * Requires an existing authToken to have been created.
 * 
 * 
 * @author jon.svede
 *
 */
public class CreateReviewAction extends AbstractAction {
	
	private Options myOptions ;
	
	

	public CreateReviewAction( IProtocolHandler myHandler ) {
		super( myHandler ) ;
	}

	@Override
	public boolean perform(CommandLine commandLine, Properties props ) {
		
		boolean success = false ;
		
		ReviewData reviewData = createReviewData( commandLine ) ;
		
		String reviewId = createReview( props, reviewData ) ;
		
		if( reviewId == null ) {
			System.out.println( "Although it looks like the review was created, not able to get the reviewId; exiting." ) ;
			return false ;
		} else {
			success = true ;
		}
		
		String reviewers = commandLine.getOptionValue( CommandLineOption.REVIEWERS.getName() ) ;
		
		String changeSet = commandLine.getOptionValue( CommandLineOption.CHANGESET.getName() ) ;
		
		String repository = commandLine.getOptionValue( CommandLineOption.REPOSITORY.getName() ) ;
		
		if( success ) {
			success = addReviewersToReview( props, reviewId, reviewers ) ;
		}
		
		if( success ) {
			success = addChangeSetToReview( props, reviewId, changeSet, repository ) ;
		}
		
		return success ;
	}
	
	public boolean addChangeSetToReview( Properties props, String reviewId, String changeSet, String repoName ) {
		AddChangesetWrapper changeSetWrapper = createChangesetData( reviewId, repoName ) ;
		boolean success = false ;
		
		StringBuilder url = TargetUrlUtil.createReviewUrl(  props ) ;
		
		//TODO: fix this, it's horrible
		url.append(  "/" ).append( reviewId ).append("/addChangeset").append( "?" ).append("FEAUTH=").append( getToken() ) ;
		
		try {
			String jsonChangeset = createJsonString( changeSetWrapper ) ;
			
			ResponseData response  = getHandler().doPost( jsonChangeset, url.toString() ) ;
			
			if( response.getHttpStatusCode() >= 200 && response.getHttpStatusCode() > 300 ) {
				System.out.println( "Successfully added changeset " + changeSet + 
						                " to review " + reviewId + " in repo " + repoName  ) ;
				
				success = true ;
			}
		} catch ( Exception e ) {
			e.printStackTrace();
		}

		
		return false ;
	}
	
	public  AddChangesetWrapper createChangesetData( String revision, String repoName ) {
		
		AddChangeset addChangeset = new AddChangeset() ;
		
		AddChangesetWrapper wrapper = new AddChangesetWrapper() ;

		String[] changeSets = revision.split(",") ;
		
		for ( String aRevision : changeSets ) {
			ChangesetData changesetData = new ChangesetData();
			changesetData.setId(aRevision);
			addChangeset.addChangesetData(changesetData);
		}
		wrapper.setAddChangeset( addChangeset ) ;
		
		wrapper.setRepository( repoName ) ;
		
		return wrapper ;
	}

	
	private boolean addReviewersToReview( Properties props, String reviewId, String reviewers ) {
		
		boolean success = false ;
		
		StringBuilder url = TargetUrlUtil.createReviewUrl(  props ) ;
		
		url.append( reviewId ).append( "/reviewers" ).append( "?" ).append("FEAUTH=").append(getToken() ) ;
		
		try {
			ResponseData response = getHandler().doPost( reviewers, url.toString() ) ;
			if( response.getHttpStatusCode() >= 200 && response.getHttpStatusCode() > 300 ) {
				System.out.println( "successfully add the reviewers " + reviewers + " to review " + reviewId ) ;
			}
			success = true ;
		} catch ( Exception e ) {
			e.printStackTrace();
		}

		return success ;
	}
	
	private String createReview( Properties props, ReviewData reviewData ) {
		
		String jsonString = null ;
		
		String reviewId = null ;
		try {
			jsonString = createJsonString( reviewData ) ;
		} catch ( Exception e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		jsonString = "{\"reviewData\":"+  jsonString + "}" ;
		
		StringBuilder url = TargetUrlUtil.createReviewUrl(  props ) ;
		
		appendAuthToken( url ) ;
		
		System.out.println( "targetUrl: " + url.toString() ) ;
		
		try {
			ResponseData response = getHandler().doPost( jsonString, url.toString() ) ;
			
			if( response.getHttpStatusCode() >= 200 && response.getHttpStatusCode() < 300 ) {
				// anything in the 200 range is a success.
				ReviewData review = ( ReviewData ) createObjectFromString( response.getResponseString(), new ReviewData() ) ;
				System.out.println( "Review created with ID: " + review.getPermaId().getId() ) ;
				reviewId = review.getPermaId().getId() ;
			}else {
				System.out.println( "there was an issue creating the review: " + response.getStatusLine() ) ;
			}
		} catch ( Exception e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
		
		return reviewId ;
		
	}
	

	@Override
	public boolean addOptions(Options options) {
		myOptions = options ;
		
		myOptions.addOption( CommandLineOption.CHANGESET.getName(), 
				                 true, 
				                 "Required. The id from the SCM for this " +
				                 "Crucible project that this review will reference." ) ;
		myOptions.addOption( "projectKey", 
				                 true, 
				                 "Required. The project key from Crucible " +
				                 "for this change." ) ;
		myOptions.addOption( CommandLineOption.REVIEW_NAME.getName(), 
				                 true, 
				                 "Optional The name of this " +
				                 "review that will be shown in Crucible. Defaults " +
				                 "to 'Review for commit xxxx' where xxxx is the " +
				                 "changeset specfied.") ;
		myOptions.addOption( CommandLineOption.DESCRIPTION.getName(), 
				                 true, 
				                 "Required. A description of the changeset being " +
				                 "reviewed." ) ;
		myOptions.addOption( CommandLineOption.ALLOW_OTHERS_TO_JOIN.getName(), 
				                 true, 
				                 "Optional. Defaults to true." ) ;
		myOptions.addOption( CommandLineOption.REVIEWERS.getName(), true, "Comma " +
				                 "separated list of Crucible usernames to which " +
				                 "to assign this review." ) ;
		myOptions.addOption( CommandLineOption.REPOSITORY.getName(), true, "The " +
				                 "name of the repository as defined in Crucible." ) ;
		return true;
	}

	@Override
	public void printHelp() {
		HelpFormatter helpFormatter = new HelpFormatter();
		helpFormatter.printHelp("createReview", myOptions);
	} 
	
	private ReviewData createReviewData( CommandLine commandLine ) {
		String author = commandLine.getOptionValue( 
				                 CommandLineOption.USERNAME.getName() ) ;
		String reviewTitle = commandLine.getOptionValue( 
				                 CommandLineOption.REVIEW_NAME.getName() ) ;
		String description = commandLine.getOptionValue( 
				                 CommandLineOption.DESCRIPTION.getName() ) ;
		String project = commandLine.getOptionValue( 
				                 CommandLineOption.PROJECT_KEY.getName() ) ;
		String allowJoinersStr = commandLine.getOptionValue( 
				                 CommandLineOption.ALLOW_OTHERS_TO_JOIN.getName() ) ;
		boolean allowJoiners = Boolean.parseBoolean( allowJoinersStr ) ;

		
		UserData user = createUserData( author )  ;
		ReviewData reviewData = new ReviewData() ;
		reviewData.setAuthor( user ) ;
		reviewData.setAllowReviewersToJoin( allowJoiners ) ;
		reviewData.setModerator( user ) ;
		reviewData.setDescription( description ) ;
		reviewData.setName( reviewTitle ) ;
		reviewData.setProjectKey( project ) ;
		// turns the review on, otherwise it's just a draft
		reviewData.setState( State.Review ) ;
		
		return reviewData ;
	}
	
	private UserData createUserData( String name ) {
		UserData userData = new UserData() ;
		userData.setUserName( name ) ;
		
		return userData ;
	}

 
	

}
