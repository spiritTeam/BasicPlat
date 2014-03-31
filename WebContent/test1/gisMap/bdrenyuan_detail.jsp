<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
  String path = request.getContextPath();
  String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
  String bdnm = request.getParameter("bdnm");
  String bdlbnm = request.getParameter("bdlbnm");
  String renyuanid = request.getParameter("renyuanid");
  System.out.println("bdrenyuan_detail bdnm="+bdnm+"  bdlbnm="+bdlbnm+"  renyuanid="+renyuanid);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head> 

    <script type="text/javascript" src="../dwr/engine.js"></script>
    <script type="text/javascript" src="../dwr/util.js"></script>  
    <script type="text/javascript" src="../dwr/interface/BDDetailServlet.js"></script>

    <jsp:include page="<%=path%>/common/sysInclude.jsp" flush="true"/>  

<title>人员详细信息表</title>
<style>
   .trTitleClass {
	    width: 100px;
	    color: #555555;
	    font: 14px Arial, Helvetica, sans-serif, Verdana, Arial, Helvetica, sans-serif;
        text-align: center;    
	    font-size:14px;
		font-weight:600;
		
		widh:100%;
		padding:0px;
		margin:0px;
		font-family:Arial,Tahoma,Verdana,Sans-Serif,宋体；
		border-left:1px solid #ADD8E6;
		border-collaspe:collapse;
	}.trClass {
	    width: 120px;
	    font: 12px Arial, Helvetica, sans-serif, Verdana, Arial, Helvetica, sans-serif;
	    font-weight:bold;
		padding:5px 5px 5px 10px;
		color:#303030;
		word-break:break-all;
		word-wrap:break-word;
		white-space:normal;
		border-bottom:#a6bfd0 dashed 1px; 
        padding-left:8px;
	}.trColorClass {
	    width: 120px;
	    font: 12px Arial, Helvetica, sans-serif, Verdana, Arial, Helvetica, sans-serif;
		background:#ededed;
		padding:5px 5px 5px 10px;
		color:#303030;
		word-break:break-all;
		word-wrap:break-word;
		white-space:normal;
		border-bottom:#a6bfd0 dashed 1px; 
        padding-left:8px;
	}.trCenterClass {
	    width: 200px;
	    text-align: left;
	    font: 12px Arial, Helvetica, sans-serif, Verdana, Arial, Helvetica, sans-serif;
		white-space:normal;
		border-bottom:#a6bfd0 dashed 1px; 
        padding-left:8px;
	}.trRightClass {
	    width: 200px;
	    text-align: right;
	    font: 12px Arial, Helvetica, sans-serif, Verdana, Arial, Helvetica, sans-serif;
		white-space:normal;
		border-bottom:#a6bfd0 dashed 1px; 
        padding-left:8px;
	}.trColorCenterClass {
	    width: 200px;
	    text-align: left;
	    font: 12px Arial, Helvetica, sans-serif, Verdana, Arial, Helvetica, sans-serif;
		white-space:normal;
		background:#ededed;
		border-bottom:#a6bfd0 dashed 1px; 
        padding-left:8px;
	}.searchSpan select { font:'arial', '宋体', 'helvetica', 'clean', 'sans-serif'; font-size:12px; margin-top:-4px;}
</style>
</head>
<body  style='background:#fff'>
  <div id="contentHtml" style='height:100%;overflow:scroll;overFlow-x: hidden;'>
    <table>
      <tr>
        <td class="trClass">姓名:</td><td id="XM" class="trCenterClass">&nbsp;</td>
        <td class="trClass">性别:</td><td id="XB" class="trCenterClass">&nbsp;</td>
        <td id="RYPHOTO" colspan="2" rowspan="5" class="trRightClass" text-align="center">&nbsp;</td>
      </tr>
      <tr>
        <td class="trClass">出生日期:</td><td id="CSRQ" class="trCenterClass">&nbsp;</td>
        <td class="trClass">血型:</td><td id="XX" class="trCenterClass">&nbsp;</td>
      </tr>
      <tr>
        <td class="trClass">入伍日期:</td><td id="RWRQ" class="trCenterClass">&nbsp;</td>
        <td class="trClass">工作日期:</td><td id="GZRQ" class="trCenterClass">&nbsp;</td>
      </tr>
      <tr>
        <td class="trClass">民族:</td><td id="MZ" class="trCenterClass">&nbsp;</td>
        <td class="trClass">身份证号:</td><td id="SFZHM" class="trCenterClass">&nbsp;</td>
      </tr>
      <tr>
        <td class="trClass">人员类别:</td><td id="RYLB" class="trCenterClass">&nbsp;</td>
        <td class="trClass">军种:</td><td id="JZ" class="trCenterClass">&nbsp;</td>
      </tr>
      <tr>
        <td class="trClass">主类别:</td><td id="ZLB" class="trCenterClass">&nbsp;</td>
        <td class="trClass">婚姻状况:</td><td id="HYZK" class="trCenterClass">&nbsp;</td>
        <td class="trClass">籍贯:</td><td id="JG" class="trCenterClass">&nbsp;</td>
      </tr>
      <tr>
        <td class="trClass">文化程度:</td><td id="WHCD" class="trCenterClass">&nbsp;</td>
        <td class="trClass">毕业院校:</td><td id="BYYX" class="trCenterClass">&nbsp;</td>
        <td class="trClass">毕业专业:</td><td id="BYZY" class="trCenterClass">&nbsp;</td>
      </tr>
      <tr>
        <td class="trClass">入学日期:</td><td id="RXRQ" class="trCenterClass">&nbsp;</td>
        <td class="trClass">毕业日期:</td><td id="BYRQ" class="trCenterClass">&nbsp;</td>
        <td class="trClass">学位:</td><td id="XW" class="trCenterClass">&nbsp;</td>
      </tr>
      <tr>
        <td class="trClass">行政职务:</td><td id="XZZW" class="trCenterClass">&nbsp;</td>
        <td class="trClass">行政职务等级:</td><td id="XZZWDJ" class="trCenterClass">&nbsp;</td>
        <td class="trClass">军衔文职级:</td><td id="JXWZJ" class="trCenterClass">&nbsp;</td>
      </tr>
      <tr>
        <td class="trClass">专业技术职务:</td><td id="ZYJSZW" class="trCenterClass">&nbsp;</td>
        <td class="trClass">专业技术等级:</td><td id="ZYJSDJ" class="trCenterClass">&nbsp;</td>
        <td class="trClass">政治面貌:</td><td id="ZZMM" class="trCenterClass">&nbsp;</td>
      </tr>
      <tr>
        <td class="trClass">一类特供人员类别:</td><td id="YILTGRYLB" class="trCenterClass">&nbsp;</td>
        <td class="trClass">二类特供人员类别:</td><td id="ERLTGRYLB" class="trCenterClass">&nbsp;</td>
        <td class="trClass">三类特供人员类别:</td><td id="SANLTGRYLB" class="trCenterClass">&nbsp;</td>
      </tr>
      <tr>
        <td class="trClass">四类特供人员类别:</td><td id="SILTGRYLB" class="trCenterClass">&nbsp;</td>
        <td class="trClass">五类特供人员类别:</td><td id="WULTGRYLB" class="trCenterClass">&nbsp;</td>
        <td class="trClass">&nbsp;</td>        <td id="" class="trCenterClass">&nbsp;</td>
      </tr>
    </table>
    <table>
      <tr>
        <td class="trClass">院校培训情况:</td>
        <td id="" class="trCenterClass"><textarea id="JSYXPXQK" rows=3 cols=200></textarea></td>
      </tr>
      <tr>
        <td class="trClass">任职经历:</td>
        <td id="" class="trCenterClass"><textarea id="RZJL" rows=3 cols=200></textarea></td>
      </tr>
      <tr>
        <td class="trClass">指挥重大任务行动:</td>
        <td id="" class="trCenterClass"><textarea id="ZHZDRWQK" rows=3 cols=200></textarea></td>
      </tr>
    </table>
    
  </div>
<script>
$(function(){
    BDDetailServlet.queryRenyuanDetail('<%=bdnm%>','<%=bdlbnm%>','<%=renyuanid%>',function(strHtml){
		//alert(strHtml);
        //document.getElementById("contentHtml").innerHTML = strHtml;
        var rydetail = string2Object(strHtml);
        $("#XM").text(rydetail.XM==null?"":rydetail.XM);
        $("#XB").text(rydetail.XB);
        $("#MZ").text(rydetail.MZ);
        $("#SFZHM").text(rydetail.SFZHM);
        
        $("#CSRQ").text(rydetail.CSRQ);
        $("#XX").text(rydetail.XX);
        $("#JG").text(rydetail.JG);
        
        $("#RWRQ").text(rydetail.RWRQ);
        $("#GZRQ").text(rydetail.GZRQ);
        $("#RYLB").text(rydetail.RYLB);
        
        $("#ZLB").text(rydetail.ZLB);
        $("#HYZK").text(rydetail.HYZK);
        $("#JZ").text(rydetail.JZ);
        
        $("#WHCD").text(rydetail.WHCD);
        $("#BYYX").text(rydetail.BYYX==null?"":rydetail.BYYX);
        $("#XW").text(rydetail.XW);
        
        $("#RXRQ").text(rydetail.RXRQ);
        $("#BYRQ").text(rydetail.BYRQ);
        $("#BYZY").text(rydetail.BYZY==null?"":rydetail.BYZY);
        
        $("#XZZW").text(rydetail.XZZW);
        $("#XZZWDJ").text(rydetail.XZZWDJ);
        $("#JXWZJ").text(rydetail.JXWZJ);
        
        $("#ZYJSZW").text(rydetail.ZYJSZW);
        $("#ZYJSDJ").text(rydetail.ZYJSDJ);
        $("#ZZMM").text(rydetail.ZZMM);
        
        $("#YILTGRYLB").text(rydetail.YILTGRYLB);
        $("#ERLTGRYLB").text(rydetail.ERLTGRYLB);
        $("#SANLTGRYLB").text(rydetail.SANLTGRYLB);
        
        $("#SILTGRYLB").text(rydetail.SILTGRYLB);
        $("#WULTGRYLB").text(rydetail.WULTGRYLB);
        
        $("#JSYXPXQK").text(rydetail.JSYXPXQK==null?"":rydetail.JSYXPXQK);
        $("#RZJL").text(rydetail.RZJL==null?"":rydetail.RZJL);
        $("#ZHZDRWQK").text(rydetail.ZHZDRWQK==null?"":rydetail.ZHZDRWQK);
        
        //显示人员照片
        //alert(rydetail.RYPHOTO);
        $("#RYPHOTO").html(rydetail.RYPHOTO+"");
        //alert($("#RYPHOTO").find("img").attr("src"));
        //$("#RYPHOTO").find("img").attr("width","50px");
        //$("#RYPHOTO").find("img").attr("height","50px");
        //alert($("#RYPHOTO").find("img").attr("height"));
        $("#RYPHOTO").find("img").css({"width":"130px","height":"160px","align":"right"});
        $("#RYPHOTO").css({"align":"right"});
        $("#RYPHOTO").css({"padding":"5px"});
	});
});
</script>
</body>
</html>