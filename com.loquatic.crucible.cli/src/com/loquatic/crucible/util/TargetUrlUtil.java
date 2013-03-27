package com.loquatic.crucible.util;

import java.util.Properties ;

/**
 * Utility class for building the target URL on Crucible based on the properties
 * passed in.
 * <p>
 * The properties file is a required argument to the main class and should contain
 * the following properties:
 * <ul>
 * <li>protocol - http/https - defaults to http</li>
 * <li>host - the hostname or IP address of the crucible server</li>
 * <li>port - the post of the instance.</li>
 * <li>context - the context of the server; defaults to blank</li>
 * </ul>
 * <p>
 * By convention, the methods of this class should NOT append a trailing slash.
 * 
 * @author jon.svede
 *
 */
public class TargetUrlUtil {
	
	private TargetUrlUtil() {}
	
	/**
	 * Takes a Properties object and formats a URL for talking to Crucible. This
	 * URL conforms to the Crucible docs and is formatted as follows:
	 * 
	 * protocol://host:port/context/rest-service/reviews-v1
	 * 
	 * @param props
	 * @return
	 */
	public static StringBuilder createReviewUrl( Properties props ) {
		
		StringBuilder reviewString = createBaseUrl( props ) ;
		
		reviewString.append( "/" ) ;
		
		reviewString.append( "rest-service/reviews-v1/" ) ;
		
		return reviewString ;
	}
	
	/**
	 * Creates the URL for building an AuthToken.
	 * 
	 * http://docs.atlassian.com/fisheye-crucible/latest/wadl/crucible.html#d2e324
	 * 
	 * protocol://host:port/context/rest-service/auth-v1/login
	 * 
	 * @param props
	 * @return
	 */
	public static StringBuilder createAuthTokenUrl( Properties props ) {
		
		StringBuilder authString = createBaseUrl( props ) ;
		
		authString.append(  CrucibleUri.AUTH_URI.getUri() ) ;
		
		return authString ;
	}
	
	/**
	 * Creates the base URL:
	 * 
	 * protocol://host:port/context
	 * 
	 * @param props
	 * @return
	 */
	private static StringBuilder createBaseUrl( Properties props ) {
		String protocol = props.getProperty( PropertyKey.PROTOCOL.getKey(), "http" ) ;
		String host = props.getProperty( PropertyKey.HOST.getKey() ) ;
		String port = props.getProperty( PropertyKey.PORT.getKey() ) ;
		String context = props.getProperty( PropertyKey.CONTEXT.getKey() ) ;
		
		StringBuilder hostString = new StringBuilder() ;
		
		hostString.append(  protocol ).append( "://" ).append( host ) ;
		hostString.append( ":").append( port ) ;
		if( context != null && context.length() > 0 ) {
			hostString.append( "/" ).append( context ) ;
		}
		
		return hostString ;
	}

}
