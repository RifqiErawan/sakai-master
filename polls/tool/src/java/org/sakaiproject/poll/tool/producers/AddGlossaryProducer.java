/**********************************************************************************
 * $URL: $
 * $Id:  $
 ***********************************************************************************
 *
 * Copyright (c) 2006, 2007, 2008, 2009 The Sakai Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.opensource.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **********************************************************************************/

package org.sakaiproject.poll.tool.producers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.sakaiproject.poll.logic.ExternalLogic;
import org.sakaiproject.poll.logic.PollListManager;
import org.sakaiproject.poll.logic.PollVoteManager;
import org.sakaiproject.poll.model.Glossary;
import org.sakaiproject.poll.model.Option;
import org.sakaiproject.poll.model.Poll;
import org.sakaiproject.poll.model.Vote;
import org.sakaiproject.poll.tool.params.OptionViewParameters;
import org.sakaiproject.poll.tool.params.PollViewParameters;
import org.sakaiproject.poll.tool.params.VoteBean;
import org.sakaiproject.util.FormattedText;

import uk.org.ponder.localeutil.LocaleGetter;
import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.messageutil.TargettedMessage;
import uk.org.ponder.messageutil.TargettedMessageList;
import uk.org.ponder.rsf.components.UIBoundBoolean;
import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIELBinding;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.UIOutputMany;
import uk.org.ponder.rsf.components.UISelect;
import uk.org.ponder.rsf.components.UISelectChoice;
import uk.org.ponder.rsf.components.UISelectLabel;
import uk.org.ponder.rsf.components.UIVerbatim;
import uk.org.ponder.rsf.components.decorators.DecoratorList;
import uk.org.ponder.rsf.components.decorators.UIFreeAttributeDecorator;
import uk.org.ponder.rsf.components.decorators.UILabelTargetDecorator;
import uk.org.ponder.rsf.components.decorators.UITooltipDecorator;
import uk.org.ponder.rsf.evolvers.FormatAwareDateInputEvolver;
import uk.org.ponder.rsf.evolvers.TextInputEvolver;
import uk.org.ponder.rsf.flow.ARIResult;
import uk.org.ponder.rsf.flow.ActionResultInterceptor;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCase;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReporter;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;

@Slf4j
public class AddGlossaryProducer implements ViewComponentProducer,NavigationCaseReporter, ViewParamsReporter{
	public static final String VIEW_ID = "pollAddGlossary";

	private PollListManager pollListManager;
	private MessageLocator messageLocator;
	private LocaleGetter localeGetter;
        private TargettedMessageList tml;
        private ExternalLogic externalLogic;
        
        private static final String NAVIGATE_GLOSSARY = "actions-glossary";

	public String getViewID() {
		return VIEW_ID;
	}

	public void setMessageLocator(MessageLocator messageLocator) {
		this.messageLocator = messageLocator;
	}

	public void setLocaleGetter(LocaleGetter localeGetter) {
		this.localeGetter = localeGetter;
	}

	public void setPollListManager(PollListManager pollListManager) {
		this.pollListManager = pollListManager;
	}	
	
	public void setTargettedMessageList(TargettedMessageList tml) {
		this.tml = tml;
	}
	
	public void setExternalLogic(ExternalLogic externalLogic) {
		this.externalLogic = externalLogic;
	}	
	
	public void fillComponents(UIContainer tofill, ViewParameters viewparams,
			ComponentChecker checker) {		
            String locale = localeGetter.get().toString();
            Map<String, String> langMap = new HashMap<String, String>();
            langMap.put("lang", locale);
            langMap.put("xml:lang", locale);
            UIOutput.make(tofill, "polls-html", null).decorate(new UIFreeAttributeDecorator(langMap));
            
            UIBranchContainer actions = UIBranchContainer.make(tofill,"actions:",Integer.toString(0));
            UIInternalLink.make(actions, NAVIGATE_GLOSSARY, UIMessage.make("action_glossary"),new SimpleViewParameters(GlossaryProducer.VIEW_ID));
                Glossary glossary = new Glossary();	
            UIForm newGlossaryForm = UIForm.make(tofill, "add-glossary-form");
            UIOutput.make(tofill, "add-glossary-title", messageLocator.getMessage("add_glossary_title"));				
            UIMessage.make(tofill, "glossary-name-label", "glossary_name_label");
            UIMessage.make(tofill, "glossary-description-label", "glossary_description_label");
            UIMessage.make(tofill, "glossary-category-label", "glossary_term_label");
            UIMessage.make(tofill, "new-poll-descr", "new_poll_title");
            UIInput.make(newGlossaryForm, "glossary-name-input", "#{pollToolBean.glossaryName}");
            UIInput.make(newGlossaryForm, "glossary-description-input", "#{pollToolBean.glossaryDescription}");
            UIInput.make(newGlossaryForm, "glossary-category-input", "#{pollToolBean.glossaryCategory}");
            UICommand.make(newGlossaryForm, "submit-new-glossary", "#{pollToolBean.processActionAddGlossaryString}");
//            UICommand cancel = UICommand.make(newGlossaryForm, "cancel",UIMessage.make("new_glossary_cancel"));
//            UICommand glossaryIndex = UICommand.make(newGlossaryForm, "glossaryIndex",UIMessage.make("glossary_index"));               
	}
        //        UIInput.make(newGlossaryForm, "glossary-name-input", "#{glossary.term}",glossary.getTerm());
//        UIInput.make(newGlossaryForm, "glossary-description-input", "#{glossary.description}",glossary.getDescription());
//        UIInput.make(newGlossaryForm, "glossary-category-input", "#{glossary.category}",glossary.getCategory());											
//UICommand.make(newGlossaryForm, "submit-new-glossary", UIMessage.make("new_glossary_submit"),
//			"#{pollToolBean.processActionAddGlossary}");
	
		

		
//                UICommand cancel = UICommand.make(newGlossaryForm, "cancel",UIMessage.make("new_glossary_cancel"));
//		cancel.parameters.add(new UIELBinding("#{voteCollection.submissionStatus}", "cancel"));
//		log.debug("Finished generating view");


	public List<NavigationCase> reportNavigationCases() {
		List<NavigationCase> togo = new ArrayList<NavigationCase>(); // Always navigate back to this view.
		togo.add(new NavigationCase(null, new SimpleViewParameters(VIEW_ID)));
		togo.add(new NavigationCase("added", new SimpleViewParameters(GlossaryProducer.VIEW_ID)));
		togo.add(new NavigationCase("glossaryIndex", new SimpleViewParameters(GlossaryProducer.VIEW_ID)));
		togo.add(new NavigationCase("cancel", new SimpleViewParameters(PollToolProducer.VIEW_ID)));
		return togo;
	}

	public ViewParameters getViewParameters() {
		return new PollViewParameters();
	}

	


}


