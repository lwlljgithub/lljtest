
<%
	/**
	 * Copyright(c)2016 Beijing ieforever Co. Ltd.
	 * All right reserved.
	 * 项目名称:四川项目全过程管理
	 * 功能说明:合同里程碑计划设情页面
	 */
%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="iepip" prefix="iepip"%>
<jsp:directive.include file="/webframe/jsp/include/top.jspf" />
<link href="<%=cssPath%>css/button.css" rel="stylesheet" type="text/css">
<link href="<%=cssPath%>css/table.css" rel="stylesheet" type="text/css">
<link href="<%=cssPath%>css/style.css" rel="stylesheet" type="text/css">
<link href="<%=cssPath%>css/msg.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="<%=cssPath%>css/layout.css">
<link rel="stylesheet" href="<%=basePath%>webpgc/css/imgSelect.css"
	type="text/css">
<script src="<%=basePath%>js/imsgbox.js" type="text/javascript"></script>
<script language="javascript" src="<%=path%>/js/message/showDiv.js"></script>
<script language="javascript"
	src="<%=path%>/js/My97DatePicker/WdatePicker.js" defer="defer"></script>
<script type="text/javascript" src="<%=basePath%>js/iimgbox.js"></script>
<link type="text/css" rel="stylesheet"
	href="<%=basePath%>/scpmm/style/green/css/mm/jquery.tabletree.css" />

<script type="text/javascript" src="<%=basePath%>/scpmm/js/mm/jquery.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/scpmm/js/mm/contextmenu.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/scpmm/js/mm/jquery.tabletree.js"></script>
<script type="text/javascript" src="<%=basePath%>/scpmm/js/mm/zDrag.js"></script>
<style type="text/css">
</style>
</head>
<body>
	<div class="current">
		<div class="mt5">
                                  当前位置：项目全过程管理系统 > 项目里程碑管理 > 合同里程碑管理 > 合同里程碑计划执行     
		</div>
	</div>
	<div class="w95_100" id="footDIV">
			 <%
				if (com.hhwy.iepip.popedom.helper.UserHelper
						.hasAccreditByAuthoritySign(
								com.hhwy.iepip.framework.message.Message
										.getMessage("authoritySign.button.save")
								)) {
			%>
               <input type="button" id="standingSaveBtn" onclick="saveSetPlan()" value="<%=Message.getMessage("confirm.button")%>" class="butt_bg" onmouseout="this.className='butt_bg'" onmouseover="this.className='butt_bg_over'">
               
              <%
				}
			%>
		<input type="button" onclick="back();" id="back" value="<%=Message.getMessage("cancel.button")%>" class="butt_bg" onmouseout="this.className='butt_bg'" onmouseover="this.className='butt_bg_over'"/>
     </div>
	<table border="0" cellpadding="0" cellspacing="0" class="tab_form"
		width="98%">
		<tbody>
		     <tr>
		     <td>
		           
					<table border="0" cellspacing="1" class="tableTree" style="width:100%;">
						<tr>
							<td class="l" style="width: 20%">年度</td>
							<td style="width: 30%" id="year"></td>
							<td class="l" style="width: 20%">项目/预算名称</td>
							<td style="width: 30%" id="projectName"></td>
						</tr>
						<tr>
							<td class="l" style="width: 20%">项目编码</td>
							<td style="width: 30%" id="projectCode"></td>
							<td class="l" style="width: 20%">主办部门</td>
							<td style="width: 30%" id="requestUser"></td>
						</tr>
						<tr>
							<td class="l" style="width: 20%">项目负责人</td>
							<td style="width: 30%" id="projectUser"></td>
							<td class="l" style="width: 20%">合同名称</td>
							<td style="width: 30%" id="compactName"></td>
						</tr>
						<tr>
							<td class="l" style="width: 20%">协办部门</td>
							<td style="width: 30%" id="xbbm"></td>
						</tr>
					</table>

				</td>
		   </tr>
			<tr>
				<td colspan="4" align="left">
					<div style="width:100%;" class="tableTree_out_box">
						<div style="width:100%;" class="tabletree_box">
							<table border="0" cellspacing="1" class="tableTree"
								id="tableTree1" style="width:100%;">

							</table>
							 
						</div>
					</div>
				</td>
			</tr>
	</table>
	<br>
	<script type="text/javascript">
		var curPage = $("#curPage").val();
		var id = getQueryString("id");//合同ID
		jQuery.getScript(basePath + "js/imsgbox.js");
		$(function() {
			toMakeMilestoneBox(id);
			if(!$("#standingSaveBtn").attr("id")){//如果没有保存、取消按钮  则说明当前登录人没有改功能的操作权限
				$(":input").attr("disabled",true);
				$("#back").attr("disabled",false);
				$("#back").attr("value","返回");
			}
		});
		//拼接表格树
		function toMakeMilestoneBox(pactId) {
			var data;
			$.ajax({
						url : basePath+ "pactProgressexecudAction.action?method=findAllExecudPact",
						type : "post",
						data : {
							"id" : pactId
						},
						dataType : "json",
						async : false,
						success : function(res) {
							if (res.result && res.result == 1) {
								data = res.executeData;
								var projectName=data[0].projectName;
								var pactName=data[0].pactName;
								var year=data[0].year;
								var projectCode=data[0].projectCode; 
								var requestUser=data[0].requestUser;  
								var projectUser=data[0].projectUser;
								var coOrganizer = data[0].coOrganizer;
								if(data[0].projectStatus=='9200009585413'){
									$("#standingSaveBtn").hide();
								}
								//var projectId=data[0].projectId;
								$("#projectName").html(projectName);
								$("#compactName").html(pactName);
								$("#xbbm").html(coOrganizer);
								$("#year").html(year);
								$("#projectCode").html(projectCode);
								$("#requestUser").html(requestUser);
								$("#projectUser").html(projectUser);
								
								
							}else if(res.result==2){
								alert("没有审核通过的项目");
							}
						}
					});
			$("#tableTree1").tabletree({
				'treeField' : 'name',
				'data' : data,
				'columns' : [{
					title : '序号',
					field : 'num',
					width : '5%'
				}, {
					title : '里程碑计划名称',
					field : 'miLestoneName',
					width : '20%'
				}, {
					title : '层级',
					field : 'level',
					width : '5%'
				},{
					title : '项目里程碑计划日期',
					field : 'setDate',
					width : '12%'
				}, {
					title : '合同进度管控计划日期',
					field : 'planControllDate',
					width : '12%'
				}, {
					title : '实际完成日期',
					field : 'actualDate',
					width : '12%'
				},{
					title : '存在问题',
					field : 'problem',
					width : '12%'
				},{
					title : '下一步要求',
					field : 'nextRequest',
					width : '12%'
				},{
					title : '是否考核',
					field : 'isCheck',
					width : '10%'
				},{
					title : '隐藏列',
					field : 'planId',
					width : '0%'
				},{
					title : '隐藏列',
					field : 'tempId',
					width : '0%'
				}],
				'isMulChoice' : false,
				'isHasOutHeader' : true,
				'isFixedTreeField' : false,
				'count' : 1,
				'ondblduble' : function() {
				}
			});
			//显示详细信息里程碑计划设置数据
		     $('#tableTree1 tr:gt(0)').each(function(i){
		    	 $(this).find("TD SPAN").css("color", "#000");
		    	 $(this).children('td:eq(0)').html(i+1);
		    	 var actualDate=$(this).children('td:eq(5)').html();
		    	 var actualDateDiv = '<input type="text" readonly="readonly" class="Wdate" value="'+actualDate+'"  style="width:98%;height:100%;" name="actualDate" id=\'actualDate'+i+'\'/>';
		    	 $(this).children('td:eq(5)').html(actualDateDiv);
		    	 
		    	 var problem=$(this).children('td:eq(6)').html();
		    	 $(this).children('td:eq(6)').html("<input type='text' value='"+problem+"' name='problem' style='width:98%;height:100%;' />");
		    	 
		    	 var nextRequest=$(this).children('td:eq(7)').html();
		    	 $(this).children('td:eq(7)').html("<input type='text' value='"+nextRequest+"' name='nextRequest' style='width:98%;height:100%;' />");
		     });
		     if($("#standingSaveBtn").attr("id")){//如果没有保存、取消按钮  则说明当前登录人没有改功能的操作权限
		    	 $('#tableTree1 tr:gt(0)').each(function(i){
			    	 $(this).children('td:eq(5)').focus("onfocus",function(){
			    		// WdatePicker({el:this,dateFmt:'yyyy-MM-dd',skin:'whyGreen'});
			    		 WdatePicker({el:this,maxDate:new Date(),dateFmt:'yyyy-MM-dd',skin:'whyGreen'});
			    	 });
			    	 $(this).children('td:eq(5)').click("onclick",function(){
			    		// $(this).attr("preDate",$(this).find("input:eq(0)").val());
			    		// WdatePicker({el:$dp.$('actualDate'+i),dateFmt:'yyyy-MM-dd',skin:'whyGreen'});
			    		 WdatePicker({maxDate:new Date(),dateFmt:'yyyy-MM-dd',skin:'whyGreen'});
			    	 });
			    	 
			     });
		     }
		     
		}
		
		
		
		/*
		 * 功能：实时更改或者取消子节点的选中状态
		 */
		function clickCheck(obj){
			 var xz = $(obj).attr("checked");//是否选中
			 var cengame = $(obj).parent().parent().next().children("td:eq(3)").html();//改行属于第几层级
			 if(xz && cengame != "一级"){
				 $(obj).parent().parent().next().find("input[type='checkbox']").attr("checked",true);
				 clickCheck($(obj.parentNode.nextSibling).find("td:eq(0)").find("input[type='checkbox']"));
			 }else if(!xz && cengame != "一级"){
				 $(obj).parent().parent().next().find("input[type='checkbox']").attr("checked",false);
				 clickCheck($(obj.parentNode.nextSibling).find("td:eq(0)").find("input[type='checkbox']"));
			 }
		}
		/**
		 * 功能：保存设置的里程碑计划
		 */
function saveSetPlan() {
	var msg = "";
	var mess="";
	var messa="";
	var arr = "";
	var arr2="";
	//获取所有选中的复选框
	$('#tableTree1 tr:gt(0)').each(
			function(i) {
					var oExecuteId = $(this).attr("nodeid")||"no";
					var actualDate = $(this).children("td:eq(5)").find("input:eq(0)").val()||"no";
					var problem = $(this).children("td:eq(6)").find("input[name='problem']").val()||"no";
					var nextRequest = $(this).children("td:eq(7)").find("input[name='nextRequest']").val()||"no";
					var planId=$(this).children("td:eq(9)").html();
					var tempId=$(this).children("td:eq(10)").html();
				    arr += oExecuteId + "|" +actualDate+"|"+ problem + "|" + nextRequest +"|"+planId+"|"+tempId+"@_@";
// 				    if(actualDate=="no"){
// 				    	msg+=i+1+"、";
// 				    }
// 				    if(problem=="no"){
// 				    	mess+=i+1+"、";
// 				    }
// 				    if(nextRequest=="no"){
// 				    	messa+=i+1+"、";
// 				    }
			});
// 	if (msg) {
// 		msg = msg.substring(0, msg.length - 1);
// 		new iMsgBox("第" + msg + "行实际完成日期没设置！", "信息提示").showAlert();
// 		return;
// 	}
// 	if (mess) {
// 		mess = mess.substring(0, mess.length - 1);
// 		new iMsgBox("第" + mess + "行存在问题 没设置！", "信息提示").showAlert();
// 		return;
// 	}     
// 	if (messa) {
// 		messa = messa.substring(0, messa.length - 1);
// 		new iMsgBox("第" + messa + "行下一步要求没设置！", "信息提示").showAlert();
// 		return;
// 	}
	$("#standingSaveBtn").attr("disabled", "disabled");//锁定保存按钮 防止多次点击
	$.ajax({
		url : basePath + "pactProgressexecudAction.action?method=saveOrUpdateExecute",
		type : "post",
		data : {
			"arr" : arr,
			"id" : id
		},
		dataType : "json",
		async : false,
		success : function(res) {
			if(res.result==1){
				alert("保存成功！");
				$("#standingSaveBtn").attr("disabled", false);//取消锁定保存按钮
				//location.href = document.referrer;
				back();
			}
		
		}
	});
}

		function getQueryString(name) {
			var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
			var r = window.location.search.substr(1).match(reg);
			if (r != null)
				return unescape(r[2]);
			return null;
		}
				//返回上一页面
				function back() {
					//location.href = document.referrer;
					window.history.back(-1); 
				}
	</script>
	<jsp:directive.include file="/webframe/jsp/include/bottom.jspf" />