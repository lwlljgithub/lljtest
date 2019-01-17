<%@page import="java.util.UUID"%>
<%@page import="java.util.Random"%>
<%@page language="java"
	import="com.hhwy.iepip.popedom.helper.UserHelper" pageEncoding="UTF-8"%>
<%@page import="com.zhuozhengsoft.pageoffice.excelwriter.*"%>
<jsp:directive.include file="/webframe/jsp/include/top.jspf" />
<%@ page language="java" import="com.zhuozhengsoft.pageoffice.*"%>
<%@ taglib uri="http://java.pageoffice.cn" prefix="po"%>
<%@ taglib uri="iepip" prefix="iepip"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="com.hhwy.iepip.cqdky.detm.service.impl.TCqdkyDetmWtrecordServiceImpl"%>
<link href="<%=cssPath%>css/style.css" rel="stylesheet" type="text/css" />
<link href="<%=cssPath%>css/button.css" rel="stylesheet" type="text/css" />
<link href="<%=cssPath%>css/table.css" rel="stylesheet" type="text/css" />
<link href="<%=cssPath%>css/font.css" rel="stylesheet" type="text/css" />
<link href="<%=cssPath%>css/list.css" rel="stylesheet" type="text/css" />
<link href="<%=cssPath%>css/page.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="<%=cssPath%>css/msg.css" />
<link href="<%=cssPath%>css/standing-Grid.css" rel="STYLESHEET"
	type="text/css" />
<link rel="stylesheet" type="text/css" href="<%=cssPath%>css/layout.css">
<link href="<%=cssPath%>css/dhtmlxcalendar.css" rel="stylesheet"
	type="text/css" />
<script src="<%=basePath%>js/dhtmlxTreeGrid/dhtmlxcommon.js"></script>
<script src="<%=basePath%>js/dhtmlxTreeGrid/dhtmlxgrid.js"></script>
<script src="<%=basePath%>js/dhtmlxTreeGrid/dhtmlxgridcell.js"></script>
<script src="<%=basePath%>js/dhtmlxTreeGrid/dhtmlxtreegrid.js"></script>
<script src="<%=basePath%>js/dhtmlxcalendar.js"></script>
<script defer="defer" language="javascript"
	src="<%=path%>/js/My97DatePicker/WdatePicker.js">
	</script>
<script src="<%=basePath%>js/lanCalender.js"></script>
<script
	src="<%=basePath%>js/dhtmlxGrid/excells/dhtmlxgrid_excell_dhxcalendar.js"></script>
<script language="javascript" src="<%=path%>/webpdc/js/select.js"></script>
<script language="javascript"
	src="<%=path%>/office/message/js/message.js"></script>
<script src="<%=basePath%>js/dhtmlxTreeGrid/dhtmlxgrid_splt.js"></script>
<script src="<%=basePath%>/cqdky/common/js/pmCommon.js"></script>
<script src="<%=basePath%>/cqdky/common/js/pmSelect.js"></script>
<script src="<%=basePath%>webpdc/js/selects.js"></script>

<%
	PageOfficeCtrl poCtrl1 = new PageOfficeCtrl(request);
	poCtrl1.setServerPage(request.getContextPath() + "/poserver.zz"); //此行必须
	
	//隐藏菜单栏
	poCtrl1.setMenubar(false);
	poCtrl1.setTitlebar(false); //隐藏标题栏
	poCtrl1.setOfficeToolbars(false);//隐藏Office工具条
	//添加按钮
	poCtrl1.addCustomToolButton("打开word", "docNormalEdit", 3);
	//poCtrl1.addCustomToolButton("打开excel", "open", 2);
	poCtrl1.addCustomToolButton("最大化", "SwitchFullScreen()", 4);
	//设置标题
	poCtrl1.setCaption("委托业务报告出具");
	String uid=UUID.randomUUID().toString()+".doc";
	poCtrl1.setSaveFilePage(request.getContextPath()
			+ "/cqdky/detm/jsp/SaveCjrecord.jsp?uid="+uid);//如要保存文件，此行必须
	//打开文件
	//poCtrl1.webOpen("", OpenModeType.xlsNormalEdit, "张三");
	poCtrl1.setTagId("office"); //此行必须	 
%>
<script lang="javascript">
   var uid="";

   $(function(){
	  var laboratoryId=${wtRecord.laboratoryId};
	  if(laboratoryId!="9200009916206"){//如果不是状态检测室（高压）实验室的隐藏签章2
		  $("#qztwo").hide();
	  };
   });
	
	/**
	*保存附件
	*/
	function saveOffice(){
		var fileName = document.getElementById("office").DocumentFileName;
		var experimentDate=$("#experimentDate").val();
		var jdDate=$("#jdDate").val();
        if(fileName==""||fileName==null){
        	var msgBox = new iMsgBox("先打开一个word文档", "系统提示","e");
			msgBox.showAlert();
			return "";
        }else{
        	document.getElementById("office").WebSave();
        	//var  uid=$("#fileName").val();
        	var uid= "<%=uid%>";
        	
        	$.ajax({
    			method : 'post',
    			url : basePath + 'detm/WtrecordAction.action?method=saveWtrecordZxOrZd&experimentDate='+experimentDate+"&jdDate="+jdDate+"&uid="+uid,
    			dataType : 'json',
    			data : $("#form").serialize(),
    			success:function(json) {
    				if(json.message=="noflag"){
    					new iMsgBox("报告里请填写审核员名称命名!\n批准人用   {pzUser}\n核验员用   {hzUser}\n检定员用   {jdUser}", "提示", "e").showAlert();
    					//cancel();
    				}else if(json.message=="noQz"){
    					new iMsgBox("报告里找不到签章，可能是章与word上面不相符，或者选了两个签章而上传的报告里面只有一个章", "提示", "e").showAlert();
    				
    				}else if(json.message=="selectone"){
    					new iMsgBox("请先在试品库里面选择签章", "提示", "e").showAlert();
    				
    				}else{
    					if (json.message && json.message != ""){
        					new iMsgBox("保存失败！", "提示", "e").showAlert();
        					cancel();
        				}else {
        					new iMsgBox("保存成功！", "提示").showAlert();
        					cancel();
        				}
    				}
    				
    			}
    		});
        }
		
	}
	
	function docNormalEdit(){
		//$("#savebutton").hide();
		document.getElementById("office").WordImportDialog();
		var fileName = document.getElementById("office").DocumentFileName;
		//findText(fileName);
			var arr = fileName.split(".");
		var type = arr[arr.length - 1];
		//$("#fileName").val(uid+"pageoffice."+type);
		//saveOffice();
	}
	function SwitchFullScreen() {
		document.getElementById("office").FullScreen = !document
				.getElementById("office").FullScreen;
	}
	
	function findText(fileName){
		var path="C:\\Users\\Administrator\\Desktop\\";
		$.ajax({
			url : basePath + 'detm/WtrecordAction.action?method=findText&path='+path+"&fileName="+fileName,
			type:"post",
			acync:false,
			data:{},
			dataType:"json",
			success:function(data){
				data = eval(data);
				if(data){
					if(data.message=="noflag"){
						var msgBox = new iMsgBox("报告里请填写审核员名称命名!", "系统提示","e");
						msgBox.showAlert();
						return;
					}else{
						$("#savebutton").show();
					}			
				}else{
				}
				
			}
		});
	}
	
	function uuid() {
		  var s = [];
		  var hexDigits = "0123456789abcdef";
		  for (var i = 0; i < 36; i++) {
		    s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
		  }
		  s[14] = "4"; // bits 12-15 of the time_hi_and_version field to 0010
		  s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1); // bits 6-7 of the clock_seq_hi_and_reserved to 01
		  s[8] = s[13] = s[18] = s[23] = "-";
		 
		  var uuid = s.join("");
		  return uuid;
		}
	
	/**
	*打开附件
	*/
	function open(){
		document.getElementById("office").ExcelImportDialog();
		var fileName = document.getElementById("office").DocumentFileName;
		var arr = fileName.split(".");
		var type = arr[arr.length - 1];
		$("#fileName").val("pageoffice."+type);
		var jieLunId = $("#jieLunId").val();
		if (typeof fileName != "undefined" || fileName != null
				|| fileName != "") {
			var arr = fileName.split(".");
			var type = arr[arr.length - 1];
			if (type == "xls" || type == "xlsx"||type=="doc"||type=="docx") {
				//隐藏某一个页签
				if(jieLunId=="9200009876248"){
					setSheetVisible("原始记录", false);
					setSheetVisible("证书", true);
					setSheetVisible("结果通知书", false);
				}else{
					setSheetVisible("原始记录", false);
					setSheetVisible("证书", false);
					setSheetVisible("结果通知书", true);
				}
			}
		}
		//saveOffice();
	}
	
	function myTrim(str) {
	    return str.replace(/^\s+|\s+$/gm,'');
	}


	function savaForm(){	
	 	var fileName = document.getElementById("office").DocumentFileName; 
		var arr = fileName.split(".");
		var type = arr[arr.length - 1];
		var jdDate=$("#jdDate").val();
		if(jdDate==""){
			var msgBox = new iMsgBox("实验日期必填!", "系统提示","e");
			msgBox.showAlert();
			return;
		}
		//$("#fileName").val(uid+"pageoffice."+type); 
		var recordCode=$("#recordCode").val();
		recordCode=myTrim(recordCode);
		$("#recordCode").val(recordCode);
		if(recordCode==""){
			var msgBox = new iMsgBox("请复制word里面的报告编号", "系统提示","e");
			msgBox.showAlert();
			return;
		}
		var qz = $("#qz").val();
			
		if(qz=="0"){
			var msgBox = new iMsgBox("请选择签章", "系统提示","e");
			msgBox.showAlert();
			return;
		}else{
			saveOffice();
			/* $.ajax({
			method : 'post',
			url : basePath + 'detm/WtrecordAction.action?method=saveWtrecordZxOrZd',
			dataType : 'json',
			data : $("#form").serialize(),
			success:function(json) {
				if (json.message && json.message != ""){
					new iMsgBox("保存失败！", "提示", "e").showAlert();
					cancel();
				}else {
					new iMsgBox("保存成功！", "提示").showAlert();
					cancel();
				}
			}
		}); */
		}
		
		
	}
	
	/*
	 *取消
	 */
	function cancel() {
		var url = basePath + "detm/WtrecordAction?method=getWtrecordList&functionId=9200009877929&menuId=9200009869351";
		window.location.href = url;
	}
	
	$(function(){
		document.getElementById("office").JsFunction_AfterDocumentOpened = "setVisible()";
	});
	
	function setVisible(){
	var fileType = $("#fileType").val();
	var jieLunId = $("#jieLunId").val();
	if(fileType=="xls"||fileType=="xlsx"){
		//隐藏某一个页签
		if(jieLunId=="9200009876248"){
			setSheetVisible("原始记录", false);
			setSheetVisible("证书", true);
			setSheetVisible("结果通知书", false);
		}else{
			setSheetVisible("原始记录", false);
			setSheetVisible("证书", false);
			setSheetVisible("结果通知书", true);
		}
	}
}


//控制sheel是否显示
function setSheetVisible(sheet, visible) {
	var sMac = "function myfunc()" + "\r\n"
	+ " For i = 1 To Application.Sheets.Count " + "\r\n"
	+ "  If Application.Sheets(i).Name = \"" + sheet + "\" Then " + "\r\n"
	+ "    Application.Sheets(i).Visible=" + visible + "\r\n"
	+ "  End If" + "\r\n"
	+ " Next" + "\r\n"
	+ " End function";
	return document.getElementById("office").RunMacro("myfunc", sMac);
}
	
	
</script>
</head>
<body>
	<div title="导航条" id="navigation">
		<div class="mt5">
			<iepip:navigation menuId="${param.menuId}" />
		</div>
	</div>
	<!-- 数据表格 -->
	<div class="w95_100">
			<%-- <s:if
				test="@com.hhwy.iepip.popedom.helper.UserHelper@hasAccreditByAuthoritySign(@com.hhwy.iepip.framework.message.Message@getMessage('authoritySign.button.save'),#session.CURRENTLY_ID,@com.hhwy.iepip.framework.message.Message@getMessage('popedom.domain_project'))">
				<input type="button" style="margin-right: 0px"
					value="<s:property value="@com.hhwy.iepip.framework.message.Message@getMessage('save.button')" />"
					class="butt_bg" onmouseout="this.className='butt_bg'"
					onmouseover="this.className='butt_bg_over'" onclick="savaForm();" />
			</s:if> --%>
			<input id="savebutton" type="button" style="margin-right: 0px"
					value="<s:property value="@com.hhwy.iepip.framework.message.Message@getMessage('save.button')" />"
					class="butt_bg" onmouseout="this.className='butt_bg'"
					onmouseover="this.className='butt_bg_over'" onclick="savaForm();" />

			<input type="button" style="margin-right: 0px"
				value="<s:property value="@com.hhwy.iepip.framework.message.Message@getMessage('cancel.button')" />"
				class="butt_bg" onmouseout="this.className='butt_bg'"
				onmouseover="this.className='butt_bg_over'" onclick="cancel();" />
		</div>
<form name="form" id="form" action="detm/WtrecordAction.action?method=saveWtrecordZxOrZd" method="post"
		enctype="multipart/form-data">
	<table border="0" align="center" cellpadding="0" cellspacing="0"
			class="tab_form" align="center">
			<input type="hidden" name="wtRecord.clientBillId" value="${wtRecord.clientBillId}"/>
			<input type="hidden" name="wtRecord.wtorgRecordId" value="${wtRecord.wtorgRecordId}"/>
			<input type="hidden" name="wtRecord.singleUser" value="${wtRecord.singleUser}"/>
			<input type="hidden" name="wtRecord.sampleId"  value="${wtRecord.sampleId}"/>
			<input type="hidden" name="wtRecord.singleDate" value="<s:date
						name="wtRecord.singleDate" format="yyyy-MM-dd" />"/>
			<input type="hidden" name="wtRecord.wtrecordState" value="${wtRecord.wtrecordState}"/>
			<%-- <input type="hidden" name="wtRecord.fileName" id="fileName" vale="${wtRecord.fileName}"> --%>
			 <input type="hidden" name="wtRecord.fileName" id="fileName" vale="<%=uid %>">
			<%-- <input type="hidden" name="fileName" id="fileName" vale="<%=uid %>>"> --%>
			<input type="hidden" name="wtRecord.jieLunId" id="jieLunId" value="${wtRecord.jieLunId}" />
			<tr>
				<th colspan="4"><span>添加报告</span></th>
			</tr>
			<tr>
				<td class="l4">制单人</td>
				<td class="r4" >${wtRecord.singleUserName}
				</td>
				<td class="l4">制单时间</td>
				<td class="r4"><s:date
						name="wtRecord.singleDate" format="yyyy-MM-dd" /></td>
			</tr>
			<tr>
				<td class="l4">试品名称</td>
				<td class="r4" >${manage.sampleName}</td>
				<td class="l4">出厂编号</td>
				<td class="r4" >${clientBillDel.factoryNum}</td>
			</tr>
			<tr>
				<td class="l4">委托单位</td>
				<td class="r4">${clientBillInfo.clientName}</td>
				<td class="l4">委托单编号</td>
				<td class="r4">${clientBillInfo.client_bill_num}</td>
			</tr>
			<tr>
				<td class="l4">报告编号</td>
				<%-- <td class="r4">${wtRecord.recordCode}</td> --%>
				<td class="r4">
				<input type="text" name="wtRecord.recordCode" id="recordCode" value="${wtRecord.recordCode}"></input>
				</td>
				  <td class="l4">签章</td>
				<td class="r4"  id="qzone">&nbsp;
					<% 
				
					
				   TCqdkyDetmWtrecordServiceImpl tCqdkyDetmWtrecordServiceImpl=new TCqdkyDetmWtrecordServiceImpl();
				   List<Map<String,Object>> list=tCqdkyDetmWtrecordServiceImpl.getTsigninfo();
					
					Map map = new HashMap();
					for(int i= 0;i< list.size();i++){
						 map.put(list.get(i).get("ID"),list.get(i).get("SIGNNAME_SHOW"));
					}
					 request.setAttribute("map",map);
				
					%>
					<s:select id="qz" list="#request.map" style="width:150px;"  theme="simple" listKey="key" headerKey="0"   headerValue="--请选择签章--"  listValue="value" name="wtRecord.signInfoId" value="wtRecord.signInfoId" >
					</s:select>
				</td>  
			</tr>
			<%-- <tr id ="qztwo">
			 <td class="l4">签章2</td>
				<td class="r4">&nbsp;
					<% 
				
					
				   TCqdkyDetmWtrecordServiceImpl tCqdkyDetmWtrecordServiceImpl1=new TCqdkyDetmWtrecordServiceImpl();
				   List<Map<String,Object>> list1=tCqdkyDetmWtrecordServiceImpl1.getTsigninfo();
					
					Map map1 = new HashMap();
					for(int i= 0;i< list1.size();i++){
						 map1.put(list1.get(i).get("ID"),list1.get(i).get("SIGNNAME_SHOW"));
					}
					 request.setAttribute("map1",map1);
				
					%>
					<s:select id="qztwo" list="#request.map1" style="width:150px;" theme="simple" listKey="key" headerKey=""  headerValue="--请选择签章--"  listValue="value" name="wtRecord.signInfoTwoId" value="" >
					</s:select>
				</td>
			</tr> --%>
			<tr>
			    <td class="l4">实验日期<span style="color:red;">&nbsp;*</span></td>
				<td class="r4">
				<input type="text"  name="jdDate"  onFocus="WdatePicker({lang:'zh-cn',dateFmt:'yyyy-MM-dd'})" id="jdDate" value=""></input>
				</td>
				<td class="l4">有效期</td>
				<td class="r4">
				<input type="text"  name="experimentDate"  onFocus="WdatePicker({lang:'zh-cn',dateFmt:'yyyy-MM-dd'})" id="experimentDate" value=""></input>
				</td>	
			</tr>
			<tr>
				<td class="l4">备注</td>
				<td class="r4" colspan="3">
				<textarea name="wtRecord.remark" cols="100" rows="4" class="tare">${wtRecord.remark}
				</textarea>
				</td>
			</tr>
		</table>
</form>		
	<div wresizeable1="true" style="height:600px">
		<po:PageOfficeCtrl id="office" />
	</div>
	<jsp:directive.include file="/webframe/jsp/include/bottom.jspf" /><br>
</body>
</html>