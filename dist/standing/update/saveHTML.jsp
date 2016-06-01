<%@page import="com.hhwy.iepip.standing.util.StandingTools"%>
<%@page import="com.hhwy.iepip.standing.config.cache.ConfigCache"%>
<%@page import="com.hhwy.iepip.standing.model.TStandingField"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.hhwy.iepip.framework.message.Message"%>
<%@page import="com.hhwy.iepip.standing.util.StringTools"%>
<%@page import="bsh.util.BshCanvas"%>
<%@page import="com.hhwy.iepip.standing.util.BshTools"%>
<jsp:directive.include file="/webframe/jsp/include/top.jspf" />
<%@ taglib prefix="s" uri="/struts-tags"%>
<script type="text/javascript" src="<%=basePath%>js/standing-pageParams.js"></script>
<%
	//是否执行保存后调用的脚本
	String afterSaveFunction = Message.getMessage("afterSaveFunction");
	String afterSaveFunctionValue = (String) request.getAttribute(afterSaveFunction);
	String linkPath = StringTools.nvl(request.getAttribute("return_link"));
	String linkIsExpression = "0";
	if(BshTools.isExpression(linkPath,"Expression:")){
		linkPath = BshTools.checkExpression(linkPath,request);
		linkIsExpression = "1";
	}
%>
<script type="text/javascript" language="javascript">
	var funID="<s:property value='#parameters.funID[0]'/>";
	var linkIsExpression = "<%=linkIsExpression%>";
	var currentid="<s:property value='#parameters.currentid[0]'/>";
	var currentInsertId="<s:property value='#parameters.currentid_insert_id[0]'/>";
	if(currentid=='')
		currentid="<s:property value='#request.sId' />";
	//编辑页面的链接类型
	var linktype="<s:property value='#parameters.linktype[0]' />";
	//详细页面的链接类型
	var link_type='${param.link_type}';
	//配置中配入的返回链接
	var linkPath="<%=linkPath%>";
	//导航条参数
	var menuId= '${param.menuId}';
	var functionId= '${param.functionId}';
	//参数
	var params = "";
	//过滤参数
	var filtrateParams="<s:property value='#request.filtrateParams'/>";
	
	var basePath='<%=basePath%>';

	
	if( linkPath == null || linkPath == '')
		linkPath="standing/standingAction.action?method=list&funID="+
				funID+'&functionId='+functionId+'&menuId='+menuId+filtrateParams;
	else{ 
		linkPath=basePath+linkPath+'<%=StandingTools.getParams(request)%>'+'&'+filtrateParams;
		if(linkPath.indexOf("currentid=") == -1 )
			linkPath = linkPath+"&currentid="+currentInsertId;
	}
	//添加页面
	if(currentid==""){
		//弹出窗口时:需关闭窗口,刷新父页面
		if(linktype=="open"){
			window.opener.location.href=linkPath;
			window.close();
		}else{
			window.location.href=linkPath;
		}
	}else{
		//修改页面
		if(link_type=="" && linktype=="open"){
			if(window.opener)
				window.opener.location.href=linkPath;
			else
				window.location.href=linkPath;
			window.close();
		}
		//详细页面是页面跳转,修改页面是open时,需关闭子窗口,刷新父页面
		//详细页面是弹出,并是非模式窗口,修改页面只处理location的,需关闭子窗口,刷新父页面
		else if((link_type=='location' && linktype=="open")
			||(link_type=='open' && linktype=="location")){
			if(window.opener)
				window.opener.location.href=linkPath;
			else
				window.location.href=linkPath;
			window.close();
		}else{
			 window.history.go(-3); 
			//window.location.href=linkPath;
		}
	}
</script>