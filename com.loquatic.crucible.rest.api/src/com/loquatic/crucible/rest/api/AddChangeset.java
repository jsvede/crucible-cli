package com.loquatic.crucible.rest.api;

import java.util.HashSet;
import java.util.Set;

public class AddChangeset {

	
	private Set<ChangesetData> changesets ;
	
	public AddChangeset() {
		changesets = new HashSet<ChangesetData>() ;
	}
	
	public Set<ChangesetData> getChangesetData() {
		return changesets;
	}
	public void setChangesets(Set<ChangesetData> changesets) {
		this.changesets = changesets;
	}
	
	public void addChangesetData(ChangesetData data ) {
		changesets.add( data ) ;
	}

}
