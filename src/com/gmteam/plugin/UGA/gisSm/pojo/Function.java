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
    private String v_sfyx; //um.validate
    private String v_systype; //um.style

    public String getV_urltype() {
        return v_urltype;
    }

    public void setV_urltype(String v_urltype) {
        this.v_urltype = v_urltype;
    }

    public String getV_sfyx() {
        String r = this.v_sfyx;
        if (this.isValid()) r="Y";
        return r;
    }

    public void setV_sfyx(String v_sfyx) {
        this.v_sfyx = v_sfyx;
        if (v_sfyx.equals("Y")) this.setValidate(1);
    }

    public String getV_systype() {
        return v_systype;
    }

    public void setV_systype(String v_systype) {
        this.v_systype = v_systype;
        Integer i = Integer.parseInt("1");
        try {
            i = Integer.parseInt(v_systype);
        } catch(Exception e) {
            
        }
        this.setStyle(i);
    }
}