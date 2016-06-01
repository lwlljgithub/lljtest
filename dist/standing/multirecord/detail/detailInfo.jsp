<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<%@page import="com.hhwy.iepip.framework.message.Message"%>
<jsp:directive.include file="/webframe/jsp/include/top.jspf" />
<%@ taglib uri="iepip" prefix="iepip" %>

<link href="<%=cssPath%>css/button.css" rel="stylesheet" type="text/css" />
<link href="<%=cssPath%>css/table.css" rel="stylesheet" type="text/css" />
<link href="<%=cssPath%>css/style.css" rel="stylesheet" type="text/css" />
<link href="<%=cssPath%>css/style_page.css" rel="stylesheet"
	type="text/css" />
<link rel="stylesheet" type="text/css" href="<%=cssPath%>css/msg.css" />	
<link href="<%=cssPath%>css/list.css" rel="stylesheet" type="text/css" />
<link href="<%=cssPath%>css/layout.css" rel="stylesheet" type="text/css">
<link href="<%=cssPath%>css/new.css" rel="stylesheet" type="text/css">
<%@ taglib prefix="s" uri="/struts-tags"%>
<script defer="true" src="<%=basePath%>webpdc/js/selects.js"></script>
<script defer="true" src="<%=basePath%>js/My97DatePicker/WdatePicker.js"></script>
<script defer="true" src="<%=basePath%>js/verify.js"></script>
<script defer="true" src="<%=basePath%>js/imsgbox.js"></script>
<script defer="true" src="<%=basePath%>js/message/showDiv.js"></script>
<script type="text/javascript" src="<%=basePath%>js/standing-pageParams.js"></script>
<script defer="true" src="<%=basePath%>controls/js/fileManager.js"></script>

<script type="text/javascript">
//台账统一标识变量
var standing_page = "standing";
/*
function killErrors(){   
  return true;   
  }   
window.onerror = killErrors;
*/
//信息提示框:1:删除的提示信息 2:是 3:否
var rec='<%=Message.getMessage("delete.record")%>';
var y='<%=Message.getMessage("yes")%>';
var n='<%=Message.getMessage("no")%>';

var basePath = '<%=basePath%>';

//当前用户ID
var currentUserId='<s:property value="#request.currentUserId"/>';
var currentid='${param.currentid}';
var funID='<s:property value="#parameters.funID[0]" />';
//页调用的业务配置的函数名
var listFunction=function(){ new iMsgBox("没有配置您的脚本,请检查!", "填写提示").showAlert();}
//导航条参数
var menuId= '${param.menuId}';
var functionId= '${param.functionId}';
var navigationParam='';
	if(functionId != '')
		navigationParam='&functionId='+functionId;
	if(menuId != '')
		navigationParam+='&menuId='+menuId;
//链接中所有参数
var paramStr="${pageContext.request.queryString}";
//在配置中勾为参数的请求参数
var pageParams=getPageParams(paramStr)+navigationParam;
//详细页面的链接类型
var link_type='${param.link_type}';
//字段外参数，用于传递
var notFieldParams = "<%=request.getAttribute("notFieldParams")%>";
//过滤参数
var filtrateParams="<s:property value='#request.filtrateParams'/>"+notFieldParams;
//当前记录所有数据
var pObj=<%=request.getAttribute("paramData")%>;

	//收起或者显示列表内容
	function setTab(obj)
	{
		var table = obj;
		var bd = document.getElementById(obj.id+'0');
		if(bd.style.display == 'none')
		{
			//display
			bd.style.display = '';
			obj.all(0).src = obj.all(0).src.replace(/i_jt2/g, "i_jt1");
		}
		else
		{
			//hidden
			bd.style.display = 'none';
			obj.all(0).src = obj.all(0).src.replace(/i_jt1/g, "i_jt2");
		
		}		
	}
</script>
<style>
<!--
body{overflow:auto;}
-->
</style>
<title> <%=Message.getMessage("page.title.info") %></title>
<body>
<input type=hidden value="info" id="curPage"/>
   <% 
	if(menuId!=null&&!"null".equals(menuId)){ %>
	<div style="margin-top: 5px; "></div>
	<div id="navigation" class="current" style="display:none">
	  <div class="mt5">
		<iepip:navigation  menuId="${param.menuId}"/>
		</div>
		
	</div>	
      <%} %>
	

<%=request.getAttribute("html")%>
<script language="javascript" src="<%=basePath %>js/standing-detail.js"></script>
<script type="text/javascript">
	//显示按钮
	setButtonDisplay();	
//控制返回按钮在页面跳转时出现
	function displayReturnBut(){
	if(link_type=='location')
		document.getElementById('retu').style.display="";
	}
	displayReturnBut();
	function returnList(){
		//window.location.href=basePath+"standing/standingAction.action?method=list&funID="+funID+navigationParam+filtrateParams;
		 window.history.back(-1); 
		 /* if(funID=="TScpmmPmPurchasereq1"){
			 window.location.href=basePath+"standing/standingAction.action?method=list&funID="+funID+navigationParam+filtrateParams;
		 }else{
			 window.history.back(-1);  
		 } */
	}
/*	function setIframe(){
		document.getElementById("childFun").style.height=50%;
	}*/
	//标记是否显示导航条
	var is_show_navigation = '<s:property value="#request.navigation" />';
	var navigetion = document.getElementById("navigation");
	if(navigetion){
		if(is_show_navigation == 'checked' && menuId != '' && functionId != '') {
			navigetion.style.display='';
		}
	}
	//流程引用页面标识,有此标识流程才能正确调整页面大小
	var isReady = false;
	function init(){
		isReady = true;
	}
	
	var subtabVar="";
	//下拉框/单选按钮事件
	function onChangeEvent(subNo,subId){
		if(subNo != null && subNo !="" && subId != null && subId != ""){
			subtabVar=subNo+"="+subId;
			initFrame();
		}
	}
	
//如果子表只有一条记录，并在父详细页面中传递了参数：subtab_subfunID=oneRowID
//则跳转到子表该记录的详细页面
//替换链接
function oneRowDisplayDetailPage(src){
	var s=src.split('&');
	var fID;
	for(var i=0;i<s.length;i++){
		var n=s[i].split('=');
		if(n.length > 1 && n[0] == 'funID'){
			fID = n[1];
			break;
		}
	}
	src = subtabParam(fID,src);
	return src;
}
//判断是否传来显示详细页面的参数
var temp1 = false;
//保存iframe的ID
var temp2 ;
function subtabParam(fID,src){
	if(subtabVar != ""){
		var subfunID = subtabVar.split('=')[0];
			if(fID == subfunID){
				var suboneID = subtabVar.split('=')[1];
				src=src.replace('/standing/standingAction.action?method=list','standing/detailAction.action?method=infoDetail&currentid='+suboneID);
				temp1 = true;
				return src;
			}
	}
	var pps=pageParams.split('&');
	for(var i=0;i<pps.length;i++){
		var p = pps[i].split("_");
		if(p.length >1 && p[0] == 'subtab'){
			var subfunID = p[1].split('=')[0];
			if(fID != subfunID)
				continue;
			var suboneID ;
			if(p[1].split('=').length > 1)
				suboneID = p[1].split('=')[1];
			src=src.replace('/standing/standingAction.action?method=list','standing/detailAction.action?method=infoDetail&currentid='+suboneID);
			temp1 = true;
			return src;
		}
	}
	return src;
}
var srcStr='<s:property value="#request.iframesSrc" />';
var sArr=srcStr.split("#,");
//加载引用功能，即子表
function initFrame(){
	
	if(sArr.length == 0 && sArr == '')
		return;
	for(var ind=0;ind<sArr.length;ind++){
		var id=sArr[ind].split("=")[0];
		var src=sArr[ind].substring(id.length+1);
		src=src.replace(/&amp;/g,'&');
		src = oneRowDisplayDetailPage(src);
		var iframeObj=document.getElementById(id);
		if(iframeObj!=null){
			iframeObj.src=basePath+src;
			if(temp1)
				temp2 = id;
		}
	}
}
initFrame();
function setPageHeight(height,iframeID){
	parent.document.getElementById(iframeID).style.height=height;
}

window.onload = function(){
	init();
	for(var i=0;i<sArr.length;i++){
		var id=sArr[i].split("=")[0];
		if(!window.frames[id])
			continue;
		var hei=window.frames[id].subHeight;
		if(hei != "" && typeof(hei) != "undefined"){
			document.getElementById(id).style.height=hei+25;
			window.frames[id].document.getElementById("gridbox").style.height = hei;
		}
	}
	pageOnload();
}
function pageOnload(){};
//得到页面配置脚本
<%=request.getAttribute("formScript")%>
</script>
<jsp:directive.include file="/webframe/jsp/include/bottom.jspf" />