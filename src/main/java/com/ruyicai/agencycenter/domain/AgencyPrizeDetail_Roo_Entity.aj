// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.ruyicai.agencycenter.domain;

import com.ruyicai.agencycenter.domain.AgencyPrizeDetail;
import java.lang.Long;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import org.springframework.transaction.annotation.Transactional;

privileged aspect AgencyPrizeDetail_Roo_Entity {
    
    declare @type: AgencyPrizeDetail: @Entity;
    
    declare @type: AgencyPrizeDetail: @Table(name = "AGENCYPRIZEDETAIL");
    
    @PersistenceContext
    transient EntityManager AgencyPrizeDetail.entityManager;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long AgencyPrizeDetail.id;
    
    public Long AgencyPrizeDetail.getId() {
        return this.id;
    }
    
    public void AgencyPrizeDetail.setId(Long id) {
        this.id = id;
    }
    
    @Transactional
    public void AgencyPrizeDetail.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void AgencyPrizeDetail.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            AgencyPrizeDetail attached = AgencyPrizeDetail.findAgencyPrizeDetail(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void AgencyPrizeDetail.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void AgencyPrizeDetail.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public AgencyPrizeDetail AgencyPrizeDetail.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        AgencyPrizeDetail merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    public static final EntityManager AgencyPrizeDetail.entityManager() {
        EntityManager em = new AgencyPrizeDetail().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long AgencyPrizeDetail.countAgencyPrizeDetails() {
        return entityManager().createQuery("SELECT COUNT(o) FROM AgencyPrizeDetail o", Long.class).getSingleResult();
    }
    
    public static List<AgencyPrizeDetail> AgencyPrizeDetail.findAllAgencyPrizeDetails() {
        return entityManager().createQuery("SELECT o FROM AgencyPrizeDetail o", AgencyPrizeDetail.class).getResultList();
    }
    
    public static AgencyPrizeDetail AgencyPrizeDetail.findAgencyPrizeDetail(Long id) {
        if (id == null) return null;
        return entityManager().find(AgencyPrizeDetail.class, id);
    }
    
    public static List<AgencyPrizeDetail> AgencyPrizeDetail.findAgencyPrizeDetailEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM AgencyPrizeDetail o", AgencyPrizeDetail.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}
