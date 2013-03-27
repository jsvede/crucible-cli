package com.loquatic.crucible.cli;

import java.util.LinkedList;

import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

/**
 * This class controls all aspects of the application's execution
 */
public class Application implements IApplication {

	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.IApplicationContext)
	 */
	public Object start(IApplicationContext context) throws Exception {
		
		String[] args = Platform.getApplicationArgs() ;//Platform.getCommandLineArgs();
		
		String[] newArgs = enumerate( args  ) ;
		
		UserInputProcessor processor = new UserInputProcessor( "crucible" ) ;
		
		processor.process( newArgs ) ;
		
		return IApplication.EXIT_OK;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#stop()
	 */
	public void stop() {
		// nothing to do
	}

	/**
	 * Utility method for cleaning up the args passed in, since sometimes
	 * there is a '-showlocation' arg which Commons CLI doesn't particularly
	 * like.
	 * 
	 * @param args
	 * @return
	 */
	private String[] enumerate( String[] args ) {
		LinkedList<String> arguments = new LinkedList<String>() ;
		for( String s: args ) {
			if( s.equals("-showlocation") ) continue ;
			arguments.add(s) ;
		}
		
		String[] returnArgs = new String[arguments.size() ] ;
		
		if( arguments.size() == 0 ) {
			return null ;
		}
		
		return arguments.toArray( returnArgs ) ;
	}
}
