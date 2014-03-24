package com.gmteam.framework.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
/**
 * 字符串printWriter，用在exception的处理中。
 * @author wh
 */
public class StringPrintWriter extends PrintWriter{  
    public StringPrintWriter(){
        super(new StringWriter());
    }

    public StringPrintWriter(int initialSize) {
          super(new StringWriter(initialSize));
    }

    public String getString() {
          flush();
          return ((StringWriter) this.out).toString();
    }

    @Override
    public String toString() {
        return getString();
    }
}