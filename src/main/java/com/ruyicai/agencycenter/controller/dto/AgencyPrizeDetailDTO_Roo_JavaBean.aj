// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.ruyicai.agencycenter.controller.dto;

import com.ruyicai.agencycenter.domain.AgencyPrizeDetail;
import com.ruyicai.lottery.domain.Tuserinfo;

privileged aspect AgencyPrizeDetailDTO_Roo_JavaBean {
    
    public AgencyPrizeDetail AgencyPrizeDetailDTO.getAgencyPrizeDetail() {
        return this.agencyPrizeDetail;
    }
    
    public void AgencyPrizeDetailDTO.setAgencyPrizeDetail(AgencyPrizeDetail agencyPrizeDetail) {
        this.agencyPrizeDetail = agencyPrizeDetail;
    }
    
    public Tuserinfo AgencyPrizeDetailDTO.getTuserinfo() {
        return this.tuserinfo;
    }
    
    public void AgencyPrizeDetailDTO.setTuserinfo(Tuserinfo tuserinfo) {
        this.tuserinfo = tuserinfo;
    }
    
    public Tuserinfo AgencyPrizeDetailDTO.getChildTuserinfo() {
        return this.childTuserinfo;
    }
    
    public void AgencyPrizeDetailDTO.setChildTuserinfo(Tuserinfo childTuserinfo) {
        this.childTuserinfo = childTuserinfo;
    }
    
}
