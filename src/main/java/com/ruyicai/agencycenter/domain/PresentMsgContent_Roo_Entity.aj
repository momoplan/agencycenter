// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.ruyicai.agencycenter.domain;

import com.ruyicai.agencycenter.domain.PresentMsgContent;
import java.lang.Integer;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import org.springframework.transaction.annotation.Transactional;

privileged aspect PresentMsgContent_Roo_Entity {
    
    declare @type: PresentMsgContent: @Entity;
    
    declare @type: PresentMsgContent: @Table(name = "PRESENTMSGCONTENT");
    
    @PersistenceContext
    transient EntityManager PresentMsgContent.entityManager;
    
    @Transactional
    public void PresentMsgContent.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void PresentMsgContent.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            PresentMsgContent attached = PresentMsgContent.findPresentMsgContent(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void PresentMsgContent.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void PresentMsgContent.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public PresentMsgContent PresentMsgContent.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        PresentMsgContent merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    public static final EntityManager PresentMsgContent.entityManager() {
        EntityManager em = new PresentMsgContent().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long PresentMsgContent.countPresentMsgContents() {
        return entityManager().createQuery("SELECT COUNT(o) FROM PresentMsgContent o", Long.class).getSingleResult();
    }
    
    public static List<PresentMsgContent> PresentMsgContent.findAllPresentMsgContents() {
        return entityManager().createQuery("SELECT o FROM PresentMsgContent o", PresentMsgContent.class).getResultList();
    }
    
    public static PresentMsgContent PresentMsgContent.findPresentMsgContent(Integer id) {
        if (id == null) return null;
        return entityManager().find(PresentMsgContent.class, id);
    }
    
    public static List<PresentMsgContent> PresentMsgContent.findPresentMsgContentEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM PresentMsgContent o", PresentMsgContent.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}