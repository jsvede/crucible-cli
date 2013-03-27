package com.loquatic.crucible.cli.actions;

import org.junit.Test;

import com.loquatic.crucible.rest.api.AddChangesetWrapper;

public class CreateReviewActionTests {

	
	@Test
	public void testCreateChangeSetData() {
		
		CreateReviewAction cra = new CreateReviewAction( new FakeProtocolHandler() ) ;
		
		String revision = "99999,88888,77777" ;
		String repoName = "RepoNamedFoo" ;
		
		AddChangesetWrapper wrapper = cra.createChangesetData(revision, repoName) ;
		
		String data = null ;
		try {
			data = cra.createJsonString( wrapper );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println(data) ;
		
	}
}
