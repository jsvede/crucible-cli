package com.loquatic.crucible.rest.api;

public class AddChangesetWrapper {
	
	private String repository ;
	private AddChangeset addChangeset ;
	
	
	
	public AddChangesetWrapper() {}
	
	
	public String getRepository() {
		return repository;
	}
	public void setRepository(String repositoryData) {
		this.repository = repositoryData;
	}
	public AddChangeset getChangesets() {
		return addChangeset;
	}
	public void setAddChangeset(AddChangeset addChangeset) {
		this.addChangeset = addChangeset;
	}
	
}
