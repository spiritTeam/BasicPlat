package com.spiritdata.framework.exceptionC;

/**
 * Json串转换为对象异常，内部码为0002，基本信息为'json串转换为对象'
 * 请参看:
 * {@linkplain com.spiritdata.framework.util.JsonUtils JsonUtils}
 * @author wh
 */
//此方法在jsonD包中也有，为了各包间引用的明确，此方法在平台包中重复实现，这样可以保证基础包不出现向上引用。
//今后可能把jsonD作为平台包的一部份，这样可能会更好
//此异常码PLAT-0002对应jsonD中的JSND-0102
public class Plat0002CException extends PlatCException {
    private static final long serialVersionUID = -752763978985267519L;

    private static String myBaseMsg = "json串转换为对象";
    private static int myCode = 2;

    /**
     * 构造没有详细消息内容的——'json串转换为对'异常
     */
    public Plat0002CException() {
        super(myCode, myBaseMsg);
    }

    /**
     * 构造有详细消息内容的——'json串转换为对'异常
     * @param message 详细消息
     */
    public Plat0002CException(String msg) {
        super(myCode, myBaseMsg, msg);
    }

    /**
     * 根据指定的原因和(cause==null?null:cause.toString())的详细消息构造新——'json串转换为对'异常
     * @param cause 异常原因，以后通过Throwable.getCause()方法获取它。允许使用null值，指出原因不存在或者是未知的异常
     */
    public Plat0002CException(Throwable cause) {
        super(myCode, myBaseMsg, cause);
    }

    /**
     * 根据指定的原因和(cause==null?null:cause.toString())的详细消息，以及详细消息构造新——'json串转换为对'异常
     * @param message 详细消息
     * @param cause 异常原因，以后通过Throwable.getCause()方法获取它。允许使用null值，指出原因不存在或者是未知的异常
     */
    public Plat0002CException(String msg, Throwable cause) {
        super(myCode, myBaseMsg, msg, cause);
    }

    public Plat0002CException(String msg, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(myCode, myBaseMsg, msg, cause, enableSuppression, writableStackTrace);
    }
}