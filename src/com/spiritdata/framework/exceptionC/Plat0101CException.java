package com.spiritdata.framework.exceptionC;

/**
 * dao中操作数据异常，一般来说都是关系数据库(jdbc)异常，内部码为0101，基本信息为'Dao数据操作'
 * 请参看:
 * {@linkplain com.spiritdata.framework.core.dao}包
 * @author wh
 */
public class Plat0101CException extends PlatCException {
    private static final long serialVersionUID = 1405389252544185628L;

    private static String myBaseMsg = "Dao数据操作";
    private static int myCode = 101;

    /**
     * 构造没有详细消息内容的——'Dao数据操作'异常
     */
    public Plat0101CException() {
        super(myCode, myBaseMsg);
    }

    /**
     * 构造有详细消息内容的——'Dao数据操作'异常
     * @param message 详细消息
     */
    public Plat0101CException(String msg) {
        super(myCode, myBaseMsg, msg);
    }

    /**
     * 根据指定的原因和(cause==null?null:cause.toString())的详细消息构造新——'Dao数据操作'异常
     * @param cause 异常原因，以后通过Throwable.getCause()方法获取它。允许使用null值，指出原因不存在或者是未知的异常
     */
    public Plat0101CException(Throwable cause) throws Plat0000CException {
        super(myCode, myBaseMsg, cause);
    }

    /**
     * 根据指定的原因和(cause==null?null:cause.toString())的详细消息，以及详细消息构造新——'Dao数据操作'异常
     * @param message 详细消息
     * @param cause 异常原因，以后通过Throwable.getCause()方法获取它。允许使用null值，指出原因不存在或者是未知的异常
     */
    public Plat0101CException(String msg, Throwable cause) {
        super(myCode, myBaseMsg, msg, cause);
    }

    public Plat0101CException(String msg, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(myCode, myBaseMsg, msg, cause, enableSuppression, writableStackTrace);
    }
}