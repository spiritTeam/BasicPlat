package com.spiritdata.framework;

import com.spiritdata.framework.exceptionC.Plat0000CException;

/**
 * 带码异常：包含异常码的异常，模仿Oracle的报错机制。<br/>
 * 异常码分为两部分：分类码+内部序号。<br/>
 * 本类是一个虚类，在具体使用中再去实际化。<br/>
 * <p>
 * 建议按照如下规则使用CodeException框架。<br/>
 * 在如下情况下，建议设定大分类，对应一个独立的分类码：<br/>
 * ·在一个新项目或系统中，使用一类统一的大分类；<br/>
 * ·项目中有一定规模或独立性的子域(子系统、自功能)；<br/>
 * 为此大类编写一个继承自此类的类，这个类中只设置Category，比如TestCException，并建议为abstract类。<br/>
 * 之后按照这个大类中的业务设计异常，并为每一异常编写一个继承自“带码异常”大类(TestCException)的异常类，在此时设置code。<br/>
 * 在系统编写中使用具体的设置了code的“带码异常类，如Test0001CException.<br/>
 * 请参照:
 * {@linkplain com.spiritdata.framework.exceptionC.PlatCException PlatCException}和
 * {@linkplain com.spiritdata.framework.exceptionC.Plat0000CException Plat0000CException}类
 * </p>
 * @author wh
 */
//记得6、7年前在研究院，曾经写过类似的代码，但后来没有继承下来。感觉这还是一个不错的设计，现把这些重做起来
public abstract class CodeException extends RuntimeException {
    private static final long serialVersionUID = -3792843482724151540L;

    private char[] category = new char[4]; //分类码，用4个字符
    private int code; //内部序号，大小为0000-9999也是4位
    private String baseMsg; //基础信息，可省略

    //以下为本类扩展属性的内容
    /**
     * 设置分类码，若分类码不是4个字符，则抛出异常Plat0000CException。
     * 注意，所有分类码在显示时，都会被转换为大写形式。
     * @param s 分类码
     * @throws Plat0000CException若设置的分类码不符合规范
     */
    public void setCategory(String s) {
        if (s==null||s.length()!=4) throw new Plat0000CException("分类码为4个字符，且不能为空，而所设置的分类码为：'"+s+"'！");
        this.category = s.toUpperCase().toCharArray();
    }

    /**
     * 设置内部序号，内部序号范围从0到999，若超出真个范围则抛出SysCException
     * @param code 内部序号
     * @throws Plat0000CException 若设置的内部码不符合规范
     */
    public void setCode(int code) {
        if (code<0||code>9999) throw new Plat0000CException("合法的序号范围是[0..999]，而所设置的序号为："+code+"！");
        this.code = code;
    }

    /**
     * 设置基础信息，这个信息将出现在具体的异常解释字符串的左侧，并以“::”进行分割
     * @param baseMsg 基础信息
     */
    public void setBaseMsg(String baseMsg) {
        this.baseMsg = baseMsg;
    }

    /**
     * 获得异常码，型如：_SYS-0000
     * @return 异常码
     * @throws Plat0000CException 若设置的分类码或内部码不符合规范
     */
    public String getCategoryCode() {
        String eMsg = checkFields();
        if (eMsg!=null) throw new Plat0000CException(eMsg);

        return (new String(this.category))+"-"+((10000+this.code)+"").substring(1);
    }

    /**
     * 获得干净的异常信息，不包括异常码和基础异常信息
     * @return 干净的异常信息
     * @throws Plat0000CException 若不是合法的“带码异常”
     */
    public String getPureMsg() {
        String ret = this.getMessage();
        String _code = getCategoryCode();
        if (!ret.startsWith("["+_code+"]")) throw new Plat0000CException("无法得到异常码，不是合法的“带码异常”！");
        ret = ret.substring(_code.length());
        if (ret.indexOf("::")==-1) return ret;
        else return ret.substring(ret.indexOf("::")+2);
    }

    //覆盖父类的信息
    @Override
    public String getMessage() {
        String eMsg = checkFields();
        if (eMsg!=null) eMsg="[_SYS-0000] “带码异常”自身异常::"+eMsg;
        else eMsg=getCodeMessage();
        return eMsg;
    }

    @Override
    public String toString() {
        String eMsg = checkFields();
        if (eMsg!=null) return getClass().getName()+": "+"[_SYS-0000] “带码异常”自身异常::"+eMsg;

        String s = getClass().getName();
        String message = getCodeMessage();
        return (message!=null)?(s+": "+message):s;
    }

    /*
     * 检查本类是否符合规则
     * @return 若不符合规则，返回说明]信息
     */
    private String checkFields() {
        String _eMsg = "";
        String s= new String(this.category);
        if (s==null||s.length()!=4) _eMsg = "分类码为4个字符，且不能为空，而所设置的分类码为：'"+s+"'！";
        if (this.code<0||this.code>9999) {
            if (_eMsg.length()>0) _eMsg = _eMsg.substring(0, _eMsg.length()-1)+"；";
            _eMsg += "合法的序号范围是[0..999]，而所设置的序号为："+code+"！";
        }
        if (_eMsg.length()==0) return null;
        return _eMsg;
    }

    /*
     * 得到“带码异常”的异常消息
     * @return “带码异常”的异常消息
     */
    private String getCodeMessage() {
        String msg = super.getMessage();
        return "["+new String(this.category)+"-"+((10000+this.code)+"").substring(1)+"] "+(this.baseMsg==null?msg:this.baseMsg+"::"+msg);
    }
    //以上为本类扩展的内容

    /**
     * 参数初始化
     * @param s 分类码
     * @param c 内部码
     * @param bMsg 基础信息
     * @throws Plat0000CException 若设置的分类码或内部码不符合规范
     */
    protected void init(String s, int c, String bMsg) {
        init(s, c);
        this.setBaseMsg(bMsg);
    }

    /**
     * 参数初始化，省略了基础信息
     * @param s 分类码
     * @param c 内部码
     * @throws Plat0000CException 若设置的分类码或内部码不符合规范
     */
    protected void init(String s, int c) {
        String _msg = "";
        try {
            this.setCategory(s);
        } catch(Plat0000CException sce) {
            _msg = sce.getPureMsg();
        }
        try {
            this.setCode(c);
        } catch(Plat0000CException sce) {
            if (_msg.length()>0) _msg = _msg.substring(0, _msg.length()-1)+"；";
            _msg += sce.getPureMsg();
        }
        if (_msg.length()>0) throw new Plat0000CException(_msg);
    }

    //扩充父类的构造函数
    /**
     * 构造没有详细消息内容的“带码异常”
     * @param s 分类码
     * @param c 内部码
     * @param bMsg 基础信息
     * @throws Plat0000CException 若设置的分类码或内部码不符合规范
     */
    protected CodeException(String s, int c, String bMsg) {
        super();
        init(s, c, bMsg);
    }

    /**
     * 构造有详细消息内容的“带码异常”
     * @param s 分类码
     * @param c 内部码
     * @param bMsg 基础信息
     * @param message 详细消息，这个消息将和分类码、内部码、基础信息共同组成异常信息
     * @throws Plat0000CException 若设置的分类码或内部码不符合规范
     */
    protected CodeException(String s, int c, String bMsg, String message) {
        super(message);
        init(s, c, bMsg);
    }

    /**
     * 根据指定的原因和(cause==null?null:cause.toString())的详细消息构造新“带码异常”
     * @param s 分类码
     * @param c 内部码
     * @param bMsg 基础信息
     * @param cause 异常原因，以后通过Throwable.getCause()方法获取它。允许使用null值，指出原因不存在或者是未知的异常
     * @throws Plat0000CException 若设置的分类码或内部码不符合规范
     */
    protected CodeException(String s, int c, String bMsg, Throwable cause) {
        super(cause);
        init(s, c, bMsg);
    }

    /**
     * 根据指定的原因和(cause==null?null:cause.toString())的详细消息，以及详细消息构造新“带码异常”
     * @param s 分类码
     * @param c 内部码
     * @param bMsg 基础信息
     * @param message 详细消息，这个消息将和分类码、内部码、基础信息共同组成异常信息
     * @param cause 异常原因，以后通过Throwable.getCause()方法获取它。允许使用null值，指出原因不存在或者是未知的异常
     * @throws Plat0000CException 若设置的分类码或内部码不符合规范
     */
    protected CodeException(String s, int c, String bMsg, String message, Throwable cause) {
        super(message, cause);
        init(s, c, bMsg);
    }

    protected CodeException(String s, int c, String bMsg, String message, Throwable cause,
               boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        init(s, c, bMsg);
    }
}