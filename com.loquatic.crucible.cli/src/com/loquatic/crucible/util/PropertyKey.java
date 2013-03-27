package com.loquatic.crucible.util;

public enum PropertyKey {
	PROTOCOL( "protocol" ),
	HOST( "host" ),
	PORT( "port" ),
	CONTEXT( "context" );
	
	private String key ;
	
	private PropertyKey( String k ) {
		key = k ;
	}
	
	public String getKey() {
		return key ;
	}
}
