// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.ruyicai.agencycenter.domain;

import java.lang.Object;
import java.lang.String;
import javax.persistence.Embeddable;

privileged aspect AgencyPercentPK_Roo_Identifier {
    
    declare @type: AgencyPercentPK: @Embeddable;
    
    public AgencyPercentPK.new(String userno, String lotno) {
        super();
        this.userno = userno;
        this.lotno = lotno;
    }

    private AgencyPercentPK.new() {
        super();
    }

    public String AgencyPercentPK.getUserno() {
        return this.userno;
    }
    
    public String AgencyPercentPK.getLotno() {
        return this.lotno;
    }
    
    public boolean AgencyPercentPK.equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof AgencyPercentPK)) return false;
        AgencyPercentPK other = (AgencyPercentPK) obj;
        if (userno == null) {
            if (other.userno != null) return false;
        } else if (!userno.equals(other.userno)) return false;
        if (lotno == null) {
            if (other.lotno != null) return false;
        } else if (!lotno.equals(other.lotno)) return false;
        return true;
    }
    
    public int AgencyPercentPK.hashCode() {
        final int prime = 31;
        int result = 17;
        result = prime * result + (userno == null ? 0 : userno.hashCode());
        result = prime * result + (lotno == null ? 0 : lotno.hashCode());
        return result;
    }
    
}
