// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.ruyicai.agencycenter.domain;

import com.ruyicai.agencycenter.domain.AgencyPercent;
import java.lang.String;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import org.springframework.transaction.annotation.Transactional;

privileged aspect AgencyPercent_Roo_Entity {
    
    declare @type: AgencyPercent: @Entity;
    
    declare @type: AgencyPercent: @Table(name = "AGENCYPERCENT");
    
    @PersistenceContext
    transient EntityManager AgencyPercent.entityManager;
    
    @Transactional
    public void AgencyPercent.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void AgencyPercent.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            AgencyPercent attached = AgencyPercent.findAgencyPercent(this.userno);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void AgencyPercent.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void AgencyPercent.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public AgencyPercent AgencyPercent.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        AgencyPercent merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    public static final EntityManager AgencyPercent.entityManager() {
        EntityManager em = new AgencyPercent().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long AgencyPercent.countAgencyPercents() {
        return entityManager().createQuery("SELECT COUNT(o) FROM AgencyPercent o", Long.class).getSingleResult();
    }
    
    public static List<AgencyPercent> AgencyPercent.findAllAgencyPercents() {
        return entityManager().createQuery("SELECT o FROM AgencyPercent o", AgencyPercent.class).getResultList();
    }
    
    public static AgencyPercent AgencyPercent.findAgencyPercent(String userno) {
        if (userno == null || userno.length() == 0) return null;
        return entityManager().find(AgencyPercent.class, userno);
    }
    
    public static List<AgencyPercent> AgencyPercent.findAgencyPercentEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM AgencyPercent o", AgencyPercent.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}
