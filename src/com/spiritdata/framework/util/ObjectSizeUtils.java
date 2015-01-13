package com.spiritdata.framework.util;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public abstract class ObjectSizeUtils {
    /**
     * 计算可序列化对象的内存大小
     * @param o
     * @return
     */
    public static int calcSize(java.io.Serializable o) {
        int ret = 0;

        class DumbOutputStream extends OutputStream {
            int count = 0;
            public void write(int b) throws IOException {
               count++; // 只计数，不产生字节转移
            }
        }

        DumbOutputStream buf = new DumbOutputStream();
        ObjectOutputStream os = null;
        try {
            os = new ObjectOutputStream(buf);
            os.writeObject(o);
            ret = buf.count;
        } catch (IOException e) {
            // No need handle this exception
            e.printStackTrace();
            ret = -1;
        } finally {
            try {os.close();} catch (Exception e) {}
        }
        return ret;  
    }
}