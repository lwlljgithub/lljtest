
<%
	/**
	 * Copyright(c)2016 Beijing ieforever Co. Ltd.
	 * All right reserved.
	 * 项目名称:四川项目全过程管理
	 * 功能说明:里程碑计划执行详情页面
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
		<input type="button" id="save" onclick="savePlan(this)" value="保存"
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
				<td class="l">主办部门</td>
				<td><s:property value="%{@com.hhwy.iepip.popedom.helper.UserHelper
		                                 @getOrgNamesByIds(plmLib.requestUser)}" /></td>
				
			</tr>
			<tr>
				<td class="l" style="width: 20%">项目/预算名称</td>
				<td style="width: 30%"><s:property value="plmLib.projectName" /></td>
				<td class="l" style="width: 20%">项目类型</td>
				<td><s:property value="%{@com.hhwy.iepip.common.TranslateUtil
		                                 @transalteDic(plmLib.projectType)}" /></td>
				
			</tr>
			<tr>
				<td class="l">资金类型</td>
				<td><s:property value="%{@com.hhwy.iepip.common.TranslateUtil
		                                 @transalteDic(plmLib.fundType)}" /></td>
				<td class="l">计划资金（万元）</td>
				<td><s:property value="plmLib.planFund" /></td>
			</tr>
			<tr>
				<td class="l">协办部门</td>
		         <td><s:property value="%{@com.hhwy.iepip.popedom.helper.UserHelper
		                @getOrgNamesByIds(plmLib.coOrganizer)}" /></td>
				
			</tr>
			<tr>
				<th colspan="4" align="left"><SPAN>项目里程碑计划执行</SPAN></th>
			</tr>
			<tr>
				<td colspan="4" align="left" style="padding:0px;">
					<div style="width:100%;" class="tableTree_out_box" style="padding:0px;">
						<div style="width:100%;" class="tabletree_box" style="padding:0px;">
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
		var projectTypeId = "<s:property value='plmLib.projectType' />";//项目类型
		var planId = $("#id").val();//里程碑计划ID
		$(function() {
			if(planId){
				var srr = getPalnSetByProjectId();//判断是否有设置过执行
				findPlanSetList(projectTypeId, srr);
			}
		});
		//拼接表格树
		function findPlanSetList(projectTypeId, srr) {
			var data;
			$.ajax({
				url:basePath + "PlanExecudAction.action?method=findPlanSetByProjectId",
				type:"post",
				data:{"planId":planId},
				dataType:"json",
				async:false,
				success:function(res){
					var jsonData = res.result;
					if(jsonData.result && jsonData.result == 1){
						data = jsonData.PlanSetList;
						if(data[0]['projectId']=='9200009585413'){
							$("#save").hide();
							
						}
						
					}
				}
			});
			$("#tableTree1").html("");
			$("#tableTree1").tabletree({'treeField':'name',
									   'data':data,
									   'columns':[
													  {title:'序号',field:'num',width:'5%'},
													  {title:'里程碑计划名称',field:'name',width:'38%'},
													  {title:'里程碑层级',field:'level', width:'7%'},
													  {title:'里程碑属性',field:'isCompactPlan',width:'8%'},
													  {title:'里程碑计划日期',field:'planDate', width:'10%'},
													  {title:'里程碑实际完成日期',field:'', width:'12%'},
													  {title:'存在问题',field:'', width:'10%'},
													  {title:'下一步计划',field:'', width:'10%'}
												  ],
									   'isMulChoice':false,
									   'isHasOutHeader':true,
									   'isFixedTreeField':false,
									   'ondblduble':function(){}									  
									});
			//显示详细信息里程碑计划设置数据
			$('#tableTree1 tr:gt(0)').each(function(i) {
				//由于表格树的table样式和table.css样式冲突，使得此处的字体颜色为红色，故  调整字体颜色
				$(this).find("td span").css("color","#000");
				var dateDiv = '',problemDiv='',nextDiv='';
				if(srr){
					var arr = srr.split("@_@");
					var templetIds = arr[0].split(",");
					var dateStr = arr[1].split(",");
					var problems = arr[2].split(",");//alert("problems:"+problems);
					var nexts = arr[3].split(",");
					var realDate = dateStr[$.inArray($(this).attr('nodeid'),templetIds)];
					var problem = problems[$.inArray($(this).attr('nodeid'),templetIds)];
					var next = nexts[$.inArray($(this).attr('nodeid'),templetIds)];
					var val = $(this).children('td:eq(3)').html().indexOf("项目") >= 0 ;
					dateDiv = '<input type="text" value="'+((realDate && realDate!="#")?realDate:"")+'" readonly="readonly"  class="'+(val?"Wdate":"")+'" style="width:98%;height:100%;" name="realDate" id=\'realDate'+i+'\'/>';
					problemDiv = '<input type="text" value="'+((problem && problem!="#")?problem:"")+'" readonly="readonly" style="width:100%;height:100%;" name="problem" id=\'problem'+i+'\'>';
					nextDiv = '<input type="text" value="'+((next && next!="#")?next:"")+'" readonly="readonly" style="width:100%;height:100%;" name="next" id=\'next'+i+'\'>';
				}else{
					if($(this).children('td:eq(3)').html().indexOf("项目") >= 0){
						dateDiv = '<input type="text" value="" readonly="readonly" class="Wdate" style="width:98%;height:100%;" name="realDate" id=\'realDate'+i+'\'/>';
			        }else{
			        	dateDiv = '<input type="text" value="" readonly="readonly" style="width:98%;height:100%;" name="realDate" id=\'realDate'+i+'\'/>';
			        }
					problemDiv = '<input type="text" value="" readonly="readonly" style="width:100%;height:100%;" name="problem" id=\'problem'+i+'\'>';
					nextDiv = '<input type="text" value="" readonly="readonly" style="width:100%;height:100%;" name="next" id=\'next'+i+'\'>';
				}
				$(this).children('td:eq(5)').html(dateDiv);
				$(this).children('td:eq(6)').html(problemDiv);
				$(this).children('td:eq(7)').html(nextDiv);
				
			});
			$('#tableTree1 tr:gt(0)').each(function(j) {
				 var isC =  $(this).children('td:eq(3)').html();
		    	 if($("#save").attr("id") && isC.indexOf("项目") >= 0 ){
		    		 $(this).children('td:eq(5)').focus("onfocus",function(){
		        		 WdatePicker({el:this,maxDate:new Date(),dateFmt:'yyyy-MM-dd',skin:'whyGreen'});
		        		 //WdatePicker({maxDate:new Date(),dateFmt:'yyyy-MM-dd',skin:'whyGreen'});
		        	 });
		        	 $(this).children('td:eq(5)').click("onclick",function(){
		        		 WdatePicker({el:$dp.$('planDate'+j),maxDate:new Date(),dateFmt:'yyyy-MM-dd',skin:'whyGreen'})
		        	 });
		        	 $(this).children('td:eq(6)').find("input").attr("readonly",false);
		        	 $(this).children('td:eq(7)').find("input").attr("readonly",false);
		    	 }
			});
			//如果没有任何数据，怎没显示空
		    if(!data){
		   	 $("#tableTree1").append("<tr><td colspan='8' style='text-align:center;'>没有任何数据...</td></tr>")
		    }
			if(!$("#save").attr("id"))
				$(":input").attr("readonly",true);
		}
		/**获取该项目下的所有执行时间**/
		function getPalnSetByProjectId() {
			var templetIds='',dateStr='',s="",problems="",nexts="";
			$.ajax({
				url : basePath
						+ "PlanExecudAction.action?method=findPlanExecud&planId="
						+ planId,
				type : "post",
				data : {},
				dataType : "json",
				async : false,
				success : function(res) {
					if (res.result) {
						var json = res.result;
						var jsonData = json.list;
						for ( var i = 0, len = jsonData.length; i < len; i++) {
							templetIds += (jsonData[i]['PLAN_TEMPLET_ID'] || '#') + ",";
							dateStr += (jsonData[i]['ACTUAL_DATE'] || '#' ) + ",";
							problems += (jsonData[i]['PROBLEM'] || '#' ) + ",";
							nexts += (jsonData[i]['NEXT_REQUEST'] || '#' ) + ",";
						}
						if(templetIds){
							s = templetIds.substring(0,templetIds.lastIndexOf(",")) + "@_@" + dateStr.substring(0,dateStr.lastIndexOf(",")) + "@_@" + problems.substring(0,problems.lastIndexOf(","))  + "@_@" + nexts.substring(0,nexts.lastIndexOf(","));
						}
					} 
				}
			});
			return s;
		}
		/**
		 * 功能：保存设置的里程碑计划
		 */
		function savePlan() {
			$("#save").attr("disabled","disabled");//锁定保存按钮 防止多次点击
			var id = planId;
			var arr = "";
			$('#tableTree1 tr:gt(0)').each(function(i) {
				//获取当前里程碑计划模板ID
				var plantempletId = $(this).attr("nodeid");
				//获取填写的实际完成日期
				var realDate = $(this).children('td:eq(5)').find("input:eq(0)").val();
				realDate = realDate?realDate:"#"
				//获取填写的存在问题 
				var problem = $(this).children('td:eq(6)').find("input:eq(0)").val();
				problem = problem?problem:"#"
				//获取填写的下一步计划
				var next = $(this).children('td:eq(7)').find("input:eq(0)").val();
				next = next?next:"#"
		    	arr += plantempletId + "," + realDate + "," + problem + "," + next + "@_@";
			});
			arr = arr.substring(0,arr.length-3);
			$.ajax({
				url : basePath + "PlanExecudAction.action?method=savePlanExecud",
				type : "post",
				data : {
					"arr" : arr,
					"id" : id
				},
				dataType : "json",
				async : false,
				success : function() {
					$("#save").attr("disabled",false);//解锁保存按钮 防止多次点击
					//location.href = document.referrer;
					//closeSubWin();
					back();
				}
			});

		}
		//返回上一页面
		function back() {
			//location.href = document.referrer;
			window.history.back(-1); 
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