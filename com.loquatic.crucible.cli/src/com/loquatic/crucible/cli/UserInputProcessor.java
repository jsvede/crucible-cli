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

import java.io.FileNotFoundException ;
import java.io.IOException ;
import java.util.Properties ;

import org.apache.commons.cli.CommandLine ;
import org.apache.commons.cli.CommandLineParser ;
import org.apache.commons.cli.GnuParser ;
import org.apache.commons.cli.HelpFormatter ;
import org.apache.commons.cli.Options ;
import org.apache.commons.cli.ParseException ;

import com.loquatic.crucible.json.IProtocolHandler ;
import com.loquatic.crucible.json.JsonHandler ;
import com.loquatic.crucible.util.PropertiesHandler ;

/**
 * Leverages the Apache Commons CLI API to handle mapping arguments.
 * 
 * @author jon.svede
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
		options.addOption( CommandLineOption.ACTION.getName(), true, "Required; the action to be performed.");
		options.addOption( CommandLineOption.USERNAME.getName(),
						            true,
						           "The Crucible username under which to " +
								       "perform this action; required if the app " +
								       "hasn't stored an authentication token.");
		options.addOption( CommandLineOption.PASSWORD.getName(), 
				               true,
				               "The password associated with the username provided.");
		options.addOption( CommandLineOption.CACHE_AUTH.getName(), 
				               true,
				               "defaults to false; when set to true the app will " +
						           "store the authToken for future. When the authToken is " +
						           "stored, the user won't need to provide username " +
						           "and password.");
		options.addOption( CommandLineOption.CONFIG.getName(),
						            true,
						            "defines the path to the configuration file. A " +
						            "configuration file defines the URL and context for " +
						            "the Crucible instance");
		options.addOption( CommandLineOption.HELP.getName(), 
				               false, 
				               "Print help information.");
		
		ActionDispatcher dispatcher = new ActionDispatcher( handler );
		
		dispatcher.addOptionsForActions( options ) ;
		

		if (args == null || (args != null && args.length == 0)) {
			HelpFormatter helpFormatter = new HelpFormatter();
			helpFormatter.printHelp(name, options);
		} else {
			CommandLineParser parser = new GnuParser();
			CommandLine commandLine = parser.parse( options, args, false ) ;

			if (commandLine.hasOption("help")) {
				HelpFormatter helpFormatter = new HelpFormatter();
				helpFormatter.printHelp(name, options);
			} else if (!commandLine.hasOption("action")) {
				System.out.println("--action is a required argument.");
				System.exit(0);
			} else if (!commandLine.hasOption("config")) {
				System.out.println("--config is a required argument.");
				System.exit(0);
			} else {
				String configFilePath = commandLine.getOptionValue("config");
				Properties configProps = null;
				try {
					configProps = PropertiesHandler.load(configFilePath);
				} catch (FileNotFoundException e) {
					System.out.println("Unable locate the configuration file: "
							+ configFilePath);
					System.exit(0);
				} catch (IOException e) {
					System.out.println("Unable load the configuration file: "
							+ configFilePath);
					e.printStackTrace();
					System.exit(0);
				}
				String action = commandLine.getOptionValue("action");
				Action userAction = Action.findByName(action);
				if (userAction == null) {
					System.out.println("Action: " + action
							+ " is invalid. Please try again.");
					HelpFormatter helpFormatter = new HelpFormatter();
					helpFormatter.printHelp(name, options);
					System.exit(0);
				} else {
					
					dispatcher.dispatchToAction(userAction, commandLine, configProps);
				}
			}
		}

	}

}
