package com.gmteam.plugin.UGA.gisSm.pojo;

import com.gmteam.framework.UGA.UgaModule;

public class Function extends UgaModule {
    private static final long serialVersionUID = 1L;
    //private String v_functionid;  t.id
    //private String v_parent_functionid; t.pid
    //private String v_function_name; um.moduleName
    //private String v_function_cnname; t.nodeName=displayName
    //private Long n_sort; //t.order
    //private String v_jumpurl; um.url
    private String v_urltype; //无用
    //private String v_descripation; //m.descn
    //private String v_sfyx; //um.isValidate

    public String getV_urltype() {
        return v_urltype;
    }

    public void setV_urltype(String v_urltype) {
        this.v_urltype = v_urltype;
    }
}