package com.spiritdata.framework.exceptionC;

/**
 * 文件处理异常，内部码为0004，基本信息为'文件处理'
 * 请参看:
 * {@linkplain com.spiritdata.framework.util.FileUtils FileUtils}
 * @author wh
 */
public class Plat0004CException extends PlatCException {
    private static final long serialVersionUID = -2797629864404151424L;

    private static String myBaseMsg = "文件处理";
    private static int myCode = 4;

    /**
     * 构造没有详细消息内容的——'文件处理'异常
     */
    public Plat0004CException() {
        super(myCode, myBaseMsg);
    }

    /**
     * 构造有详细消息内容的——'文件处理'异常
     * @param message 详细消息
     */
    public Plat0004CException(String msg) {
        super(myCode, myBaseMsg, msg);
    }

    /**
     * 根据指定的原因和(cause==null?null:cause.toString())的详细消息构造新——'文件处理'异常
     * @param cause 异常原因，以后通过Throwable.getCause()方法获取它。允许使用null值，指出原因不存在或者是未知的异常
     */
    public Plat0004CException(Throwable cause) {
        super(myCode, myBaseMsg, cause);
    }

    /**
     * 根据指定的原因和(cause==null?null:cause.toString())的详细消息，以及详细消息构造新——'文件处理'异常
     * @param message 详细消息
     * @param cause 异常原因，以后通过Throwable.getCause()方法获取它。允许使用null值，指出原因不存在或者是未知的异常
     */
    public Plat0004CException(String msg, Throwable cause) {
        super(myCode, myBaseMsg, msg, cause);
    }

    public Plat0004CException(String msg, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(myCode, myBaseMsg, msg, cause, enableSuppression, writableStackTrace);
    }
}