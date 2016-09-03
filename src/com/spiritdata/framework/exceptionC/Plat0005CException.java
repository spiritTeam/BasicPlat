package com.spiritdata.framework.exceptionC;

/**
 * Model转换为Po异常，内部码为0005，基本信息为'Model转换为Po'
 * 请参看:
 * {@linkplain com.spiritdata.framework.core.model.ModelSwapPo ModelSwapPo}<br/>
 * 凡实现ModelSwapPo的接口，在转换为po过程中出现问题，应抛出次异常
 * @author wh
 */
public class Plat0005CException extends PlatCException {
    private static final long serialVersionUID = -898705530884638686L;

    private static String myBaseMsg = "Model转换为Po";
    private static int myCode = 5;

    /**
     * 构造没有详细消息内容的——'Model转换为Po'异常
     */
    public Plat0005CException() {
        super(myCode, myBaseMsg);
    }

    /**
     * 构造有详细消息内容的——'Model转换为Po'异常
     * @param message 详细消息
     */
    public Plat0005CException(String msg) {
        super(myCode, myBaseMsg, msg);
    }

    /**
     * 根据指定的原因和(cause==null?null:cause.toString())的详细消息构造新——'Model转换为Po'异常
     * @param cause 异常原因，以后通过Throwable.getCause()方法获取它。允许使用null值，指出原因不存在或者是未知的异常
     */
    public Plat0005CException(Throwable cause) {
        super(myCode, myBaseMsg, cause);
    }

    /**
     * 根据指定的原因和(cause==null?null:cause.toString())的详细消息，以及详细消息构造新——'Model转换为Po'异常
     * @param message 详细消息
     * @param cause 异常原因，以后通过Throwable.getCause()方法获取它。允许使用null值，指出原因不存在或者是未知的异常
     */
    public Plat0005CException(String msg, Throwable cause) {
        super(myCode, myBaseMsg, msg, cause);
    }

    public Plat0005CException(String msg, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(myCode, myBaseMsg, msg, cause, enableSuppression, writableStackTrace);
    }
}