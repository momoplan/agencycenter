// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.ruyicai.agencycenter.domain;

import com.ruyicai.agencycenter.domain.TlotctrlPK;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.lang.String;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect TlotctrlPK_Roo_Json {
    
    public String TlotctrlPK.toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }
    
    public static TlotctrlPK TlotctrlPK.fromJsonToTlotctrlPK(String json) {
        return new JSONDeserializer<TlotctrlPK>().use(null, TlotctrlPK.class).deserialize(json);
    }
    
    public static String TlotctrlPK.toJsonArray(Collection<TlotctrlPK> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }
    
    public static Collection<TlotctrlPK> TlotctrlPK.fromJsonArrayToTlotctrlPKs(String json) {
        return new JSONDeserializer<List<TlotctrlPK>>().use(null, ArrayList.class).use("values", TlotctrlPK.class).deserialize(json);
    }
    
}
