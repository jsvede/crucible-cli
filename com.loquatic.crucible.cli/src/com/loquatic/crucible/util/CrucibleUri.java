package com.loquatic.crucible.util;

public enum CrucibleUri {
	REVIEWS_URI( "/rest-service/reviews-v1/" ),
	AUTH_URI( "/rest-service/auth-v1/login" ) ;
	
	private String uri ;
	
	private CrucibleUri( String u ) {
		uri = u ;
	}
	
	public String getUri() {
		return uri ;
	}
}
