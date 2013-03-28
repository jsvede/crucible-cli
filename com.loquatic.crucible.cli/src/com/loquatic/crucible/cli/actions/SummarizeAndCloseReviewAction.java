package com.loquatic.crucible.cli.actions;

import java.util.LinkedList ;
import java.util.Properties ;

import org.apache.commons.cli.CommandLine ;
import org.apache.commons.cli.Options ;

import com.loquatic.crucible.cli.Action;
import com.loquatic.crucible.json.IProtocolHandler ;

public class SummarizeAndCloseReviewAction extends AbstractAction {
	
	private LinkedList<IAction> actions ;
	
	
	
	public SummarizeAndCloseReviewAction(IProtocolHandler handler ) {
		actions = new LinkedList<IAction>() ;
		actions.add( new SummarizeReviewAction( handler ) ) ;
		actions.add( new CloseReviewAction( handler ) ) ;
		
		setAction( Action.SUMMARIZE_AND_CLOSE_REVIEW ) ;
	}

	@Override
	public boolean perform( CommandLine commandLine, Properties props ) {
		
		boolean success = false ;
		
		for( IAction action : actions ) {
			success = action.perform( commandLine, props ) ;
			if( !success ) {
				break ;
			}
		}
		return success ;
	}

	@Override
	public boolean addOptions( Options options ) {
		for( IAction action : actions ) {
			action.addOptions( options ) ;
		}
		return true ;
	}

	@Override
	public void printHelp() {
		// TODO Auto-generated method stub

	}

}
