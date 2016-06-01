
<%
	/**
	 * Copyright(c)2016 Beijing ieforever Co. Ltd.
	 * All right reserved.
	 * 项目名称:四川项目全过程管理
	 * 功能说明:里程碑计划设置详情页面
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
<link rel="stylesheet" href="<%=basePath%>webpgc/css/imgSelect.css" type="text/css">
<script src="<%=basePath%>js/imsgbox.js" type="text/javascript"></script>
<script language="javascript" src="<%=path%>/js/message/showDiv.js"></script>
<script language="javascript" src="<%=path%>/js/My97DatePicker/WdatePicker.js" defer="defer"></script>
<script type="text/javascript" src="<%=basePath%>js/iimgbox.js"></script>
<link type="text/css" rel="stylesheet"
	href="<%=basePath%>/scpmm/style/green/css/mm/jquery.tabletree.css" />

<script type="text/javascript"
	src="<%=basePath%>/scpmm/js/mm/jquery.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/scpmm/js/mm/contextmenu.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/scpmm/js/mm/jquery.tabletree.js"></script>
<script type="text/javascript" src="<%=basePath%>/scpmm/js/mm/zDrag.js"></script>
<script type="text/javascript"
	src="<%=basePath%>/scpmm/js/mm/zDialog.js"></script>

</head>
<body>
	<div class="current">
		<div class="mt5">
			<iepip:navigation menuId="${param.menuId}" />
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
		<input type="button" id="new" onclick="setPlan(this)" value="调整"
			class="butt_bg" onmouseout="this.className='butt_bg'"
			onmouseover="this.className='butt_bg_over'">
		<input type="button" style="display: none;" id="save" onclick="saveSetPlan()" value="确定"
			class="butt_bg" onmouseout="this.className='butt_bg'"
			onmouseover="this.className='butt_bg_over'">
		<%
			}
		%>
		<input type="button" id="back" onclick="back()"
			value="<%=Message.getMessage("back.button")%>" class="butt_bg"
			onmouseout="this.className='butt_bg'"
			onmouseover="this.className='butt_bg_over'" />
	</div>
	<table border="0" cellpadding="0" cellspacing="0" class="tab_form"
		width="98%">
		<tbody>
			<tr>
				<th colspan="4" align="left"><SPAN>项目信息<input
						type="hidden" id="id" value='<s:property value="id"/>'></SPAN></th>
			</tr>
			<tr>
				<td class="l">年度</td>
				<td style="width: 30%"><s:property value="plmLib.year" /></td>
				<td class="l" style="width: 20%">项目/预算名称</td>
				<td style="width: 30%"><s:property value="plmLib.projectName" /></td>
			</tr>
			<tr>
				<td class="l" style="width: 20%">项目编码</td>
				<td><s:property value="plmLib.projectCode" /></td>
				<td class="l">主办部门</td>
				<td><s:property value="%{@com.hhwy.iepip.popedom.helper.UserHelper
		                                 @getOrgNamesByIds(plmLib.requestUser)}" /></td>
			</tr>
			<tr>
				<td class="l">项目负责人</td>
				<td><s:property value="plmLib.projectUser" /></td>
				<td class="l">设置日期</td>
				<td><s:date name="setDate" format="yyyy-MM-dd" /></td>
			</tr>
			<tr>
				
				<td class="l">协办部门</td>
				<td><s:property value="%{@com.hhwy.iepip.popedom.helper.UserHelper
		                                 @getOrgNamesByIds(plmLib.coOrganizer)}" /></td>
			</tr>
			<tr>
				<th colspan="4" align="left"><SPAN>里程碑计划日期</SPAN></th>
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
	<!--
		var curPage = $("#curPage").val();
		var projectTypeId = getQueryString("projectTypeId");//项目类型
		
		$(function() {
			var srr = getPalnSetByProjectId();//判断是否有设置过计划
			if (srr) {
				findPlanSetList(projectTypeId, srr);
			}else{//如果没有任何数据，怎没显示空
				$("#new").hide();
				$("#save").hide();
		   	 	//$("#tableTree1").append("<tr><td colspan='6' style='text-align:center;'>没有任何数据...</td></tr>")
			}
			if(${plmLib.projectStatus}=='9200009585413'){
				$("#new").hide();
			}else{
				$("#new").show();
			}
		});
		//拼接表格树
		function findPlanSetList(projectTypeId, srr) {
			var data;
			$.ajax({
						url : basePath
								+ "PlanSetAction.action?method=findPlanTempletListByType",
						type : "post",
						data : {
							"projectTypeId" : projectTypeId
						},
						dataType : "json",
						async : false,
						success : function(res) {
							var jsonData = res.result;
							if (jsonData.result && jsonData.result == 1) {
								data = jsonData.planTempletDate;
							}
						}
					});
			$("#tableTree1").html("");
			$("#tableTree1").tabletree({
				'treeField' : 'name',
				'data' : data,
				'columns' : [ {
					title : '选择',
					field : '',
					width : '5%'
				}, {
					title : '序号',
					field : 'num',
					width : '5%'
				}, {
					title : '里程碑计划名称',
					field : 'name',
					width : '57%'
				}, {
					title : '里程碑层级',
					field : 'level',
					width : '10%'
				}, {
					title : '里程碑属性',
					field : 'isCompactPlan',
					width : '10%'
				}, {
					title : '里程碑计划日期',
					field : '',
					width : '13%'
				} ],
				'isMulChoice' : false,
				'isHasOutHeader' : true,
				'isFixedTreeField' : false,
				'count' : 1,
				'ondblduble' : function() {
				}
			});
			//显示详细信息里程碑计划设置数据
			$('#tableTree1 tr:gt(0)').each(function(i) {
				//由于表格树的table样式和table.css样式冲突，使得此处的字体颜色为红色，故  调整字体颜色
				 $(this).find("TD SPAN").css("color","#000");
				var arr = srr.split("@_@");
				var idStr = arr[0].split(",");
				var dateStr = arr[1].split(",");
				var setIds = arr[2].split(",");
				var selectDiv = '';
				if($.inArray($(this).attr('nodeid'),idStr) >= 0 ){
					selectDiv = "<input type='hidden' name='setId' setId='"+(setIds[$.inArray($(this).attr('nodeid'),idStr)] || "")+"'><input type='checkbox' name='checkbox' checked='checked' disabled='disabled'  id=\"checkbox"
						+ i
						+ "\" onclick='clickCheck(this)'/>";
				}else{
					selectDiv = "<input type='checkbox' name='checkbox' disabled='disabled' id=\"checkbox"
						+ i
						+ "\" onclick='clickCheck(this)'/>";
				}
				var dateDiv = '<input type="text" value="'+(dateStr[$.inArray($(this).attr('nodeid'),idStr)] || "")+'" readonly="readonly" class="Wdate" style="width:98%;height:100%;" name="planDate" id=\'planDate'+i+'\'/>';
				$(this).children('td:eq(0)').html(selectDiv);
				$(this).children('td:eq(5)').html(dateDiv);
				
			});
			
		}
		/**获取该项目下的所有设置时间**/
		function getPalnSetByProjectId() {
			var str='',dateStr='',s="",setIds="";
			$.ajax({
				url : basePath
						+ "PlanSetAction.action?method=findPlanSetList&id="
						+ $("#id").val(),
				type : "post",
				data : {},
				dataType : "json",
				async : false,
				success : function(res) {
					if (res.result && res.result.result ==1) {
						var json = res.result;
						var jsonData = json.list;
						for ( var i = 0, len = jsonData.length; i < len; i++) {
							str += jsonData[i]['PLANTEMPLET_ID'] + ",";
							dateStr += jsonToString(jsonData[i]['PLAN_DATE'],"yyyyMMdd") + ",";
							setIds += jsonData[i]['PLANTEMPLET_ID'] + ",";
						}
						if(str){
							s = str.substring(0,str.lastIndexOf(",")) + "@_@" + dateStr.substring(0,str.lastIndexOf(",")) + "@_@" + setIds.substring(0,str.lastIndexOf(","));
						}
					} else {
						str = "";
						dateStr = "";
						setIds = "";
					}
				}
			});
			return s;
		}
		/**
		 * 功能：保存设置的里程碑计划
		 */
		function saveSetPlan() {
			$("#save").attr("disabled","disabled");//锁定保存按钮 防止多次点击
			var id = $("#id").val();
			var msg = "";
			var arr = "";
			//获取所有选中的复选框
			$("input[name='checkbox']").each(
					function(i) {
						var isChecked = $(this).attr("checked");
						if (isChecked) {
							var id = $(this).parent().parent().attr("nodeid");
							var time = $(this).parent().parent().children(
									"td:eq(5)").find("input:eq(0)").val();
							var setId = $(this).parent().parent().children(
							"td:eq(0)").find("input[name='setId']").attr("setId");
							if (!time) {
								msg += (i + 1) + "、";
							} else {
								arr += id + "," + time + "," + setId + "@_@";
							}

						}
					});
			if (msg) {
				msg = msg.substring(0, msg.length - 1);
				new iMsgBox("第" + msg + "行里程碑计划日期没设置！", "信息提示").showAlert();
				$("#save").attr("disabled",false);//解锁
				return;
			}
			$.ajax({
				url : basePath + "PlanSetAction.action?method=updatePlanSet",
				type : "post",
				data : {
					"arr" : arr,
					"id" : id
				},
				dataType : "json",
				async : false,
				success : function() {
					$("#save").attr("disabled",false);//锁定保存按钮 防止多次点击
					//location.href = document.referrer;
					//closeSubWin();
					back();
				}
			});

		}
		
		function back() {
			//location.href = document.referrer;
			window.history.back(-1); 
		}
		/*
		 * 功能：实时更改或者取消子节点的选中状态 只适用于有二级孩子的树
		 */
		function clickCheck(obj) {
			var xz = $(obj).attr("checked");//是否选中
			var cengame = $(obj).parent().parent().next().children("td:eq(3)")
					.html();//改行属于第几层级
			if (xz && cengame != "一级") {
				$(obj).parent().parent().next().find("input[type='checkbox']")
						.attr("checked", true);
				clickCheck($(obj.parentNode.nextSibling).find("td:eq(0)").find(
						"input[type='checkbox']"));
			} else if (!xz && cengame != "一级") {
				$(obj).parent().parent().next().find("input[type='checkbox']")
						.attr("checked", false);
				clickCheck($(obj.parentNode.nextSibling).find("td:eq(0)").find(
						"input[type='checkbox']"));
			}
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
		//调整时间,解锁复选框和时间可填
		function setPlan(obj) {
			//更改按钮样式
			$("#new").hide();
			$("#save").show();
			//释放
			$('#tableTree1 tr:gt(0)').each(function(i) {
				var nodeName = $(this).children("td:eq(2)").find(".tree_field").html();//节点名字
				if(nodeName == '项目创建' || nodeName == '采购需求线上提报' || nodeName == '合同签订' || nodeName == '支出入账'){
					$(this).find("input[name='checkbox']").attr("disabled",true);
					$(this).find("input[name='checkbox']").attr("checked",true);
				}else{
					var prevCheck = $(this).prev().children("td:eq(0)").find("input[name='checkbox']").attr("disabled");//改行的前一个节点是否选中
					var cengame = $(this).children("td:eq(3)").html();//改行属于第几层级
					if(cengame != '一级' && prevCheck == 'disabled'){
						$(this).find("input[name='checkbox']").attr("disabled",true);
						$(this).find("input[name='checkbox']").attr("checked",true);
					}else{
						$(this).find("input[name='checkbox']").attr("disabled",false);
					}
					$(this).children('td:eq(5)').focus("onfocus",function(){
			    		WdatePicker({el:this,dateFmt:'yyyy-MM-dd',skin:'whyGreen'});
			    	});
			    	$(this).children('td:eq(5)').click("onclick",function(){
			    	 	WdatePicker({el:$dp.$('planDate'+i),dateFmt:'yyyy-MM-dd',skin:'whyGreen'})
			    	});
				}
				 
			});
			//给日期设置点击事件
			 $('#tableTree1 tr:gt(0)').each(function(i){
				 $(this).children('td:eq(5)').focus("onfocus",function(){
		    		 WdatePicker({el:this,dateFmt:'yyyy-MM-dd',skin:'whyGreen'});
		    	 });
		    	 $(this).children('td:eq(5)').click("onclick",function(){
		    		 WdatePicker({el:$dp.$('planDate'+i),dateFmt:'yyyy-MM-dd',skin:'whyGreen'})
		    	 });
		    	 
		    	 
		     });
		}
		//json日期解析 年月日ADD BY SYF 2015-3-11
		function jsonToString(time,flag){
			 if(time){
				var year = 1900 + parseInt(time['year']);
				var month = (parseInt(time['month'])+1).toString();
				if((parseInt(month)) < 10){
					month="0"+month.toString();
				}
				var day = time['date'];
				if((parseInt(day)) < 10){
					day="0"+day.toString();
				}
				var hour = time['hours'];
				if((parseInt(hour)) < 10){
					hour="0"+hour.toString();
				}
				var min = time['minutes'];
				if((parseInt(min)) < 10){
					min="0"+min.toString();
				}
				if(flag == 'yyyyMMdd'){
					return year.toString() +'-'+ month+'-'+day;
				}else if(flag == 'yyyyMMddhhmm'){
					return year.toString() +'-'+ month+'-'+day+' '+hour+':'+min;
				}else{
					return year.toString() +'年'+ month+'月'+day+'日';
				}
				
			 }
			
		}
	//-->
	</script>
	<jsp:directive.include file="/webframe/jsp/include/bottom.jspf" />