package com.loquatic.crucible.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * Utility class for loading properties from a fully qualifed path.
 * @author jon.svede
 *
 */
public class PropertiesHandler {

	private PropertiesHandler() {}
	
	public static Properties load( String fileName ) throws FileNotFoundException, IOException {
		File propertiesFile = new File( fileName ) ;
		
		Properties properties = new Properties() ;
		properties.load( new FileReader( propertiesFile ) ) ;
		
		return properties ;
	}
}
