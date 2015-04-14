package com.spiritdata.framework.exceptionC;

/**
 * 数据库类型未知异常，一般来说都是关系数据库(jdbc)异常，内部码为0102，基本信息为'数据库类型未知'
 * 请参看:
 * {@linkplain com.spiritdata.framework.core.dao}包
 * @author wh
 */
public class Plat0102CException extends PlatCException {
    private static final long serialVersionUID = 1601725256066924350L;

    private static String myBaseMsg = "数据库类型未知";
    private static int myCode = 102;

    /**
     * 构造没有详细消息内容的——'数据库类型未知'异常
     */
    public Plat0102CException() {
        super(myCode, myBaseMsg);
    }

    /**
     * 构造有详细消息内容的——'数据库类型未知'异常
     * @param message 详细消息
     */
    public Plat0102CException(String msg) {
        super(myCode, myBaseMsg, msg);
    }

    /**
     * 根据指定的原因和(cause==null?null:cause.toString())的详细消息构造新——'数据库类型未知'异常
     * @param cause 异常原因，以后通过Throwable.getCause()方法获取它。允许使用null值，指出原因不存在或者是未知的异常
     */
    public Plat0102CException(Throwable cause) throws Plat0000CException {
        super(myCode, myBaseMsg, cause);
    }

    /**
     * 根据指定的原因和(cause==null?null:cause.toString())的详细消息，以及详细消息构造新——'数据库类型未知'异常
     * @param message 详细消息
     * @param cause 异常原因，以后通过Throwable.getCause()方法获取它。允许使用null值，指出原因不存在或者是未知的异常
     */
    public Plat0102CException(String msg, Throwable cause) {
        super(myCode, myBaseMsg, msg, cause);
    }

    public Plat0102CException(String msg, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(myCode, myBaseMsg, msg, cause, enableSuppression, writableStackTrace);
    }
}