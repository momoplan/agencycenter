// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.ruyicai.agencycenter.domain;

import java.lang.String;

privileged aspect AgencyBuyDetail_Roo_ToString {
    
    public String AgencyBuyDetail.toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BusinessId: ").append(getBusinessId()).append(", ");
        sb.append("BusinessType: ").append(getBusinessType()).append(", ");
        sb.append("BuyAmt: ").append(getBuyAmt()).append(", ");
        sb.append("CreateTime: ").append(getCreateTime()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Lotno: ").append(getLotno()).append(", ");
        sb.append("Percent: ").append(getPercent()).append(", ");
        sb.append("Userno: ").append(getUserno());
        return sb.toString();
    }
    
}
