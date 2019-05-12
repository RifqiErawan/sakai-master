/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sakaiproject.poll.service.impl;

import java.util.List;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.sakaiproject.genericdao.api.search.Restriction;
import org.sakaiproject.genericdao.api.search.Search;
import org.sakaiproject.poll.dao.PollDao;
import org.sakaiproject.poll.logic.GlossaryManager;
import org.sakaiproject.poll.model.Glossary;
import org.springframework.dao.DataAccessException;


/**
 *
 * @author wahyu mau
 */
@Slf4j
@Data
public class GlossaryManagerImpl implements GlossaryManager{    
    private PollDao dao;

    @Override
    public List<Glossary> getGlossaries() {
        List<Glossary> glossaries = dao.findAll(Glossary.class);
        return glossaries;
    }    
    

    @Override
    public Glossary getGlossaryById(Long glossaryId) throws SecurityException{
        Search search = new Search();
        search.addRestriction(new Restriction("glossaryId", glossaryId));
        Glossary glossary = dao.findOneBySearch(Glossary.class, search);
        if (glossary == null) return null;
        return glossary;
    }
    
    

    @Override
    public boolean saveGlossary(Glossary t) throws SecurityException, IllegalArgumentException {                
        if (t == null || t.getCategory()== null || t.getTerm()== null || t.getDescription()== null) {
        	throw new IllegalArgumentException("you must supply category, term, and description");
        }                               
        try {           
            dao.save(t);

        } catch (DataAccessException e) {
            log.error("Hibernate could not save: " + e.toString(), e);
            return false;
        }
        log.debug(" Glossary  " + t.toString() + "successfuly saved");
        return true;
    }
    
}
