package com.loquatic.crucible.cli.actions;

import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

public interface IAction {
	
	/**
	 * Method called when an action is invoked. The Map
	 * passed will contain all the command line args the user
	 * specified.
	 * <p>
	 * Each IAction instance can decide what to do with the args.
	 * 
	 * @return
	 */
	public boolean perform( CommandLine commandLine, Properties props ) ;
	
	/**
	 * A hook so that each option can add it's own options for the 
	 * commandline. Each instance of IAction should cache their options.
	 * 
	 * @param options
	 * @return
	 */
	public boolean addOptions( Options options ) ;
	
	/**
	 * Prints the help for this action.
	 */
	public void printHelp() ;
	
	/**
	 * Returns the string that should be passed to the --action argument
	 * when invoking the tool.
	 * @return
	 */
	public String getActionName() ;

}
