// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.ruyicai.agencycenter.domain;

import com.ruyicai.agencycenter.domain.UserAgency;
import java.lang.String;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import org.springframework.transaction.annotation.Transactional;

privileged aspect UserAgency_Roo_Entity {
    
    declare @type: UserAgency: @Entity;
    
    declare @type: UserAgency: @Table(name = "USERAGENCY");
    
    @PersistenceContext
    transient EntityManager UserAgency.entityManager;
    
    @Transactional
    public void UserAgency.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void UserAgency.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            UserAgency attached = UserAgency.findUserAgency(this.userno);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void UserAgency.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void UserAgency.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public UserAgency UserAgency.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        UserAgency merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    public static final EntityManager UserAgency.entityManager() {
        EntityManager em = new UserAgency().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long UserAgency.countUserAgencys() {
        return entityManager().createQuery("SELECT COUNT(o) FROM UserAgency o", Long.class).getSingleResult();
    }
    
    public static List<UserAgency> UserAgency.findAllUserAgencys() {
        return entityManager().createQuery("SELECT o FROM UserAgency o", UserAgency.class).getResultList();
    }
    
    public static UserAgency UserAgency.findUserAgency(String userno) {
        if (userno == null || userno.length() == 0) return null;
        return entityManager().find(UserAgency.class, userno);
    }
    
    public static List<UserAgency> UserAgency.findUserAgencyEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM UserAgency o", UserAgency.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}
