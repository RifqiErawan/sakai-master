/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Asus
 */

package org.sakaiproject.poll.tool.producers;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import org.sakaiproject.poll.logic.ExternalLogic;
import org.sakaiproject.poll.logic.PollListManager;
import org.sakaiproject.poll.logic.PollVoteManager;
import org.sakaiproject.poll.model.Option;
import org.sakaiproject.poll.model.Poll;
import org.sakaiproject.poll.model.Vote;
import org.sakaiproject.poll.model.UserNotVote;
import org.sakaiproject.poll.tool.params.PollViewParameters;

import uk.org.ponder.localeutil.LocaleGetter;
import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.messageutil.TargettedMessage;
import uk.org.ponder.messageutil.TargettedMessageList;
import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UILink;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.UISelect;
import uk.org.ponder.rsf.components.UIVerbatim;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCase;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReporter;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;
import uk.org.ponder.rsf.components.decorators.DecoratorList;
import uk.org.ponder.rsf.components.decorators.UIFreeAttributeDecorator;
import uk.org.ponder.rsf.components.decorators.UITooltipDecorator;

@Slf4j
public class UserNotVoteProducer implements ViewComponentProducer,NavigationCaseReporter,ViewParamsReporter {

	public static final String VIEW_ID = "userNotVote";

	
	private PollListManager pollListManager;
	private PollVoteManager pollVoteManager;
	private MessageLocator messageLocator;
	private LocaleGetter localegetter;

	public String getViewID() {
		// TODO Auto-generated method stub
		return VIEW_ID;
	}

	public void setMessageLocator(MessageLocator messageLocator) {

		this.messageLocator = messageLocator;
	}

	
	public void setPollListManager(PollListManager pollListManager) {
		this.pollListManager = pollListManager;
	}

	
	public void setLocaleGetter(LocaleGetter localegetter) {
		this.localegetter = localegetter;
	}
	public void setPollVoteManager(PollVoteManager pvm){
		this.pollVoteManager = pvm;
	}


        private ExternalLogic externalLogic;    
        public void setExternalLogic(ExternalLogic externalLogic) {
		this.externalLogic = externalLogic;
	}
    
	private TargettedMessageList tml;
	public void setTargettedMessageList(TargettedMessageList tml) {
		this.tml = tml;
	}

	
	public void fillComponents(UIContainer tofill, ViewParameters viewparams,
			ComponentChecker checker) {

		PollViewParameters ecvp = (PollViewParameters) viewparams;

		String strId = ecvp.id;
		log.debug("got id of " + strId);
		Poll poll = pollListManager.getPollById(Long.valueOf(strId));

		if (!pollListManager.isAllowedViewResults(poll, externalLogic.getCurrentUserId())) {
			tml.addMessage(new TargettedMessage("poll.noviewresult", new Object[]{}, TargettedMessage.SEVERITY_ERROR));
			return;
			
		}

		String locale = localegetter.get().toString();
                Map<String, String> langMap = new HashMap<String, String>();
                langMap.put("lang", locale);
                langMap.put("xml:lang", locale);

		UIOutput.make(tofill, "polls-html", null).decorate(new UIFreeAttributeDecorator(langMap));
		
                int totalNotVote = pollVoteManager.getDistinctNotVotersForPoll(poll);
                List<String> userName = pollVoteManager.getUsernameNotVotersForPoll(poll);
//                List<String> vlname = pollVoteManager.getVoterLName(poll);
                List<String> userId = pollVoteManager.getUsernameNotVotersForPoll(poll);
//                List<String> voption = pollVoteManager.getVoterOption(poll);
                List<CollatedUserNotVote> collatedUsersNotVote = new ArrayList<CollatedUserNotVote>();
                                
                                
                
//                List<CollatedUserNotVote> collation = new ArrayList<CollatedUserNotVote>();
                
                for (int i=0; i <totalNotVote; i++ ) {    
                        CollatedUserNotVote collatedUserNotVote = new CollatedUserNotVote();                        
			collatedUserNotVote.setFullName(userName.get(i));
                        collatedUserNotVote.setUserId(userId.get(i));                        
                        
                        collatedUsersNotVote.add(collatedUserNotVote);
		}
                
                UILink vname = UILink.make(tofill,"answers-voter",messageLocator.getMessage("results_answers_voter"), "#");
		vname.decorators = new DecoratorList(new UITooltipDecorator(messageLocator.getMessage("results_answers_voter_tooltip")));                
                
                for (int i=0; i <totalNotVote; i++ ) {
			UIBranchContainer resultVoterRow = UIBranchContainer.make(tofill,"answer-row-voter:",collatedUsersNotVote.get(i).getUserId().toString());

			UIVerbatim.make(resultVoterRow,"answer-option-voter",collatedUsersNotVote.get(i).getFullName());
			UIOutput.make(resultVoterRow,"answer-count-voter", Integer.valueOf(i+1).toString());
//			UIOutput.make(resultVoterRow,"answer-optionVote",collation.get(i).getOptionText());
		}
                
		//the cancel button
		UIForm form = UIForm.make(tofill,"actform");
		UICommand cancel = UICommand.make(form,"back",messageLocator.getMessage("voters_cancel"),"#{pollToolBean.cancel}");
		cancel.decorators = new DecoratorList(new UITooltipDecorator(messageLocator.getMessage("voters_cancel_tooltip"))); 
		
		externalLogic.postEvent("poll.viewResult", "poll/site/" + externalLogic.getCurrentLocationId() +"/poll/" +  poll.getPollId(), false);

	}

	public List<NavigationCase> reportNavigationCases() {
		
		List<NavigationCase> togo = new ArrayList<NavigationCase>(); // Always navigate back to this view.
		togo.add(new NavigationCase(null, new SimpleViewParameters(VIEW_ID)));
		togo.add(new NavigationCase("cancel", new PollViewParameters(ResultsProducer.VIEW_ID)));
		return togo;
	}	
	public ViewParameters getViewParameters() {
		return new PollViewParameters();

	}

	private static class CollatedUserNotVote {
		private String fullName;    
                private String userId;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
		
		public CollatedUserNotVote() {
			this.fullName = "";
		}		

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }
                		

	}
}