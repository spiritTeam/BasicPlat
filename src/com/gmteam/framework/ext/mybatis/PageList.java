package com.gmteam.framework.ext.mybatis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class PageList<E> extends ArrayList<E> implements Serializable {
    private static final long serialVersionUID = 1412759446332294208L;
    private Paginator paginator;

    public PageList() {
    }

    public PageList(Collection<? extends E> c) {
        super(c);
    }

    public PageList(Collection<? extends E> c, Paginator p) {
        super(c);
        this.paginator = p;
    }

    public PageList(Paginator p) {
        this.paginator = p;
    }

    public Paginator getPaginator() {
        return this.paginator;
    }
}