/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sakaiproject.poll.logic;

import java.util.List;
import org.sakaiproject.poll.model.Glossary;

/**
 *
 * @author wahyu mau
 */
public interface GlossaryManager {
    public List<Glossary> getGlossaries();
    public Glossary getGlossaryById(Long glossaryId);
    public boolean saveGlossary(Glossary t) throws SecurityException, IllegalArgumentException;
    
}
