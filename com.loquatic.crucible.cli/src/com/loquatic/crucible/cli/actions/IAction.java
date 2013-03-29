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
	
	/**
	 * Each IAction should provide a human friendly description of 
	 * how to use this IAction returned as a String.
	 * @return
	 */
	public String getHelpOverview() ;
	
	/**
	 * Each IAction should provide a human friendly set of examples
	 * that show how someone might use this action; bonus points 
	 * if that output shows all the permutations of itself.
	 * @return
	 */
	public String getHelpExamples() ;

}
