// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.ruyicai.agencycenter.domain;

import java.lang.String;

privileged aspect AgencyPrizeDetail_Roo_ToString {
    
    public String AgencyPrizeDetail.toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BusinessId: ").append(getBusinessId()).append(", ");
        sb.append("BusinessType: ").append(getBusinessType()).append(", ");
        sb.append("ChildUserno: ").append(getChildUserno()).append(", ");
        sb.append("CreateTime: ").append(getCreateTime()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("PrizeAmt: ").append(getPrizeAmt()).append(", ");
        sb.append("State: ").append(getState()).append(", ");
        sb.append("TotalAmt: ").append(getTotalAmt()).append(", ");
        sb.append("Userno: ").append(getUserno());
        return sb.toString();
    }
    
}
