package com.spiritdata.framework.exceptionC;

/**
 * “带码异常”自身的异常，内部码为0000，基本信息为'“带码异常”自身'
 * @author wh
 */
public class Plat0000CException extends PlatCException {
    private static final long serialVersionUID = -374758674042637507L;
    private static String myBaseMsg = "“带码异常”自身"; //基本信息
    private static int myCode = 0; //内部编码

    /**
     * 构造没有详细消息内容的——平台“带码异常”自身异常
     * @throws Plat0000CException 若设置的分类码或内部码不符合规范
     */
    public Plat0000CException() {
        super(myCode, myBaseMsg);
    }

    /**
     * 构造有详细消息内容的——平台“带码异常”自身异常
     * @param message 详细消息
     */
    public Plat0000CException(String msg) {
        super(myCode, myBaseMsg, msg);
    }

    /**
     * 根据指定的原因和(cause==null?null:cause.toString())的详细消息构造新——平台“带码异常”自身异常
     * @param cause 异常原因，以后通过Throwable.getCause()方法获取它。允许使用null值，指出原因不存在或者是未知的异常
     */
    public Plat0000CException(Throwable cause) {
        super(myCode, myBaseMsg, cause);
    }

    /**
     * 根据指定的原因和(cause==null?null:cause.toString())的详细消息，以及详细消息构造新——平台“带码异常”自身异常
     * @param message 详细消息
     * @param cause 异常原因，以后通过Throwable.getCause()方法获取它。允许使用null值，指出原因不存在或者是未知的异常
     */
    public Plat0000CException(String msg, Throwable cause) {
        super(myCode, myBaseMsg, msg, cause);
    }

    public Plat0000CException(String msg, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(myCode, myBaseMsg, msg, cause, enableSuppression, writableStackTrace);
    }
}