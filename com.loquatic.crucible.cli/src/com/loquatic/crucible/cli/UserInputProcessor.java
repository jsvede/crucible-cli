/*
 * Copyright (c) 2013, Loquatic Software, LLC
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *
 *    * Redistributions of source code must retain the above copyright notice, 
 *      this list of conditions and the following disclaimer.
 *    * Redistributions in binary form must reproduce the above copyright 
 *      notice, this list of conditions and the following disclaimer in the 
 *      documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 *
 */
package com.loquatic.crucible.cli;

import java.io.File ;
import java.io.FileNotFoundException ;
import java.io.IOException ;
import java.util.LinkedList ;
import java.util.Map ;
import java.util.Properties ;

import org.apache.commons.cli.CommandLine ;
import org.apache.commons.cli.CommandLineParser ;
import org.apache.commons.cli.GnuParser ;
import org.apache.commons.cli.HelpFormatter ;
import org.apache.commons.cli.Options ;
import org.apache.commons.cli.ParseException ;

import com.loquatic.crucible.cli.actions.IAction ;
import com.loquatic.crucible.json.IProtocolHandler ;
import com.loquatic.crucible.json.JsonHandler ;
import com.loquatic.crucible.util.PropertiesHandler ;

/**
 * Leverages the Apache Commons CLI API to handle mapping arguments.
 * <p>
 * If a later date the decision is made to use this outside of RCP or 
 * a third party wishes to integrate this code into another tool, 
 * this class would be the best entry point.
 * 
 * @author jsvede@yahoo.com
 * 
 */
public class UserInputProcessor {

	private String name;
	private IProtocolHandler handler ;

	/**
	 * Pass in the name of this app instance.
	 * 
	 * @param appName
	 */
	public UserInputProcessor(String appName) {
		name = appName;
		setProtocolHandler( new JsonHandler() ) ;
	}
	
	public void setProtocolHandler( IProtocolHandler myHandler ) {
		handler = myHandler ;
	}

	public void process(String[] args) throws ParseException {
		Options options = new Options();

		// Global options - available to all IAction instances.
		options.addOption( CommandLineOption.CONFIG.getName(), 
				           true, 
				           "Optional; if you have a ~/crucible-cli.properties, otherwise required." ) ;
		options.addOption( CommandLineOption.ACTION.getName(), 
				           true, 
				           "Required; the action to be performed.");
		options.addOption( CommandLineOption.HELP.getName(), 
				           false, 
				           "Print help information.");
		
		ActionDispatcher dispatcher = new ActionDispatcher( handler );
		
		
		if (args == null || (args != null && args.length == 0)) {
			printHelp( options, dispatcher ) ;
		} else {
			CommandLineParser parser = new GnuParser();
			
			String[] bareMinArgs = getHelpAndActionArgs( args ) ;
			
			CommandLine commandLine = parser.parse( options, bareMinArgs, false ) ;

			if (commandLine.hasOption("help") && !commandLine.hasOption( "action" ) ) {
				printHelp( options, dispatcher ) ;
			} else if (commandLine.hasOption("help") && commandLine.hasOption( "action" ) ) {
				String option = commandLine.getOptionValue( "action" ) ;
				printHelp( options, dispatcher, option ) ;
			} else if (!commandLine.hasOption("action")) {
				System.out.println("--action is a required argument.");
				System.exit(0);
			} else {
				String configFilePath = commandLine.getOptionValue("config");
				if( configFilePath == null ) {
					// assume a default of ~/crucible-cli.properties
					String homeDir = System.getProperty( "user.home" ) ;
					String fileName = "crucible-cli.properties" ;
					StringBuilder sb = new StringBuilder() ;
					sb.append( homeDir ).append( File.separator ).append( fileName ) ;
					configFilePath = sb.toString() ;
				}
				Properties configProps = null;
				try {
					configProps = PropertiesHandler.load(configFilePath);
				} catch (FileNotFoundException e) {
					
					System.out.println("The default configuration file: "
						+ configFilePath + " is not present and you " +
						"did not specify an alternate location using the " +
						"--config option.");
					System.exit(0);
				} catch (IOException e) {
					System.out.println("Unable to load the configuration file: "
							+ configFilePath);
					e.printStackTrace();
					System.exit(0);
				}

//				dispatcher.addOptionsForActions( options ) ;

				String action = commandLine.getOptionValue("action");
				Action userAction = Action.findByName(action);
				if (userAction == null) {
					System.out.println("Action: " + action
							+ " is invalid. Please try again.");
					HelpFormatter helpFormatter = new HelpFormatter();
					helpFormatter.printHelp(name, options);
					System.exit(0);
				} else {
					
					dispatcher.dispatchToAction(userAction, args, configProps, options );
				}
			}
		}

	}
	
	/**
	 * Given the full list of args, simply extract the --action and --help
	 * arguments (if help exists) to establish the basic Options list.
	 * 
	 * @param args
	 * @return
	 */
	private String[] getHelpAndActionArgs( String [] args ) {
		
		
		LinkedList<String>bareMinList = new LinkedList<String>() ;
		
		for( int x=0; x<args.length; x++ ) {
			String element = args[x] ;
			if( element != null ) {
				if( element.toLowerCase().contains(  "action" ) ) {
					bareMinList.add( element ) ;
					bareMinList.add( args[x+1] ) ;
					continue ;
				}
			}
			if( element.toLowerCase().contains(  "help" ) ) {
				bareMinList.add( element ) ;
			}
		}
		
		String[] bareMinimum = new String[bareMinList.size()] ;
		
		return bareMinList.toArray( bareMinimum ) ;
		
	}
	
	private void printHelp( Options options, ActionDispatcher dispatcher )  {
		printHelp( options, dispatcher, null ) ;
	}
	
	/**
	 * Prints the main help from the Commons CLI and then prints each
	 * options customized help.
	 * 
	 * @param options
	 * @param dispatcher
	 */
	private void printHelp( Options options, ActionDispatcher dispatcher, String actionName )  {
		
		Map<Action, IAction> actionsMap = dispatcher.getRegisteredActions() ;
		
		if( actionName == null ) {
			HelpFormatter helpFormatter = new HelpFormatter();
			helpFormatter.printHelp(name, options);
			System.out.println( "-------------------------------------------\n") ;
			System.out.println( "Examples of available Actions:" ) ;
			for( Action action : actionsMap.keySet() ) {
				System.out.println( "--action " + action.getName() ) ;
			}
			System.out.println( "\ntype --action validAction --help for more information" ) ;
		} else {
			IAction myAction = actionsMap.get( Action.findByName(actionName) ) ;
			myAction.printHelp() ;
			System.out.println( "\n" ) ;
			
		}
	}

}
