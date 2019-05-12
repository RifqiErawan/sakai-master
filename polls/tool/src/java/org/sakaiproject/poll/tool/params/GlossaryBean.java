/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sakaiproject.poll.tool.params;

import org.sakaiproject.poll.logic.GlossaryManager;
import org.sakaiproject.poll.model.Glossary;

/**
 *
 * @author wahyu mau
 */
public class GlossaryBean {
    public String glossaryName;
    public String glossaryCategory;
    public String glossaryDescription;
    private GlossaryManager glossaryManager;

    public void setGlossaryManager(GlossaryManager glossaryManager) {
        this.glossaryManager = glossaryManager;
    }

    public String getGlossaryName() {
        return glossaryName;
    }

    public void setGlossaryName(String glossaryName) {
        this.glossaryName = glossaryName;
    }

    public String getGlossaryCategory() {
        return glossaryCategory;
    }

    public void setGlossaryCategory(String glossaryCategory) {
        this.glossaryCategory = glossaryCategory;
    }

    public String getGlossaryDescription() {
        return glossaryDescription;
    }

    public void setGlossaryDescription(String glossaryDescription) {
        this.glossaryDescription = glossaryDescription;
    }
    
    public String processActionAddGlossaryString(){
            if (glossaryCategory != null && glossaryDescription != null && glossaryDescription != null) {
                Glossary glossaryy = new Glossary();
                boolean status;
                
                glossaryy.setCategory(glossaryCategory);
                glossaryy.setDescription(glossaryDescription);
                glossaryy.setTerm(glossaryName);
                status = glossaryManager.saveGlossary(glossaryy);
                if(status){
                    return "success";
                }
                
            }else{
                return "failure";
            }
            return "failure";
        }
    
}
