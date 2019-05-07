/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sakaiproject.poll.tool.params;

import java.util.Date;
import org.sakaiproject.poll.logic.PollListManager;
import org.sakaiproject.poll.logic.PollVoteManager;
import org.sakaiproject.poll.model.OtherOption;

/**
 *
 * @author wahyu mau
 */
public class OtherOptionBean {
    private String optionText;
    
    private PollListManager manager;
	public void setPollListManager(PollListManager manager) {
		this.manager = manager;
	}
        
        private PollVoteManager pollVoteManager;
        public void setPollVoteManager(PollVoteManager pvm){
		this.pollVoteManager = pvm;
	}

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }
    
    public String processActionSubmit(){
        if(optionText != null && optionText.length() > 0){
            OtherOption otherOption = new OtherOption();
            
            boolean status;
            
            otherOption.setOptionText(optionText);
            otherOption.setVoteDate(new Date());
            
            status = manager.saveOtherOption(otherOption);
            if(status){
                return "success";
            }else{
                return "failure";
            }
        }
        return "failure";
    }
    
}
