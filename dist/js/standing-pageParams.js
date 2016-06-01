function getPageParams(paramStr){
		var params=paramStr.split('&');
		var pageParams='';
		for(var i=0;i<params.length;i++){
			if(params[i]==null || params[i].length==0 
				|| params[i].indexOf('method')!=-1 || params[i].indexOf('funID')!=-1
				|| params[i].indexOf('currentid')!=-1 || params[i].indexOf('linktype')!=-1
				|| params[i].indexOf('menuId')!=-1 ||params[i].indexOf('functionId')!=-1 
				|| params[i].indexOf('onlyread')!=-1)
				continue;
			var kV=params[i];
			var kVArr=kV.split('=');
			//有键有值的继续传递
			if(kVArr.length==2 && kVArr[1]!='')
				pageParams += '&' + kV;
		}
		return pageParams;
}
//提供根据key取页面请求中参数的方法
function getPageParamValueByKey(key){
	var searchP=window.location.search.substring(1);
	var arr=searchP.split('&');
	for(var i=0;i<arr.length;i++){
		var kV=arr[i].split('=');
		if(kV.length > 1){
			var k = kV[0];
			if(k==key)
				return kV[1];
		}
	}
	return null;
}
//默认按钮生成为隐藏的，设置所有按钮为显示，便于其它程序控制显示与否
function setButtonDisplay(){
	var obj=document.getElementsByTagName('input');
	for(var i=0;i<obj.length;i++){
		//添加时不显示删除按钮
		if( obj[i].id.indexOf('btn_delete') > -1 && window.location.search.substring(1).indexOf('method=save') > -1)
			continue;
		//子表不再显示保存按钮(添加时)
		if(typeof(parent.currentid_insert_id) != 'undefined'){
			if(obj[i].id.indexOf('btn_saveMultiRecord') > -1){
				obj[i].style.display='none';
				continue;
			}
		}
		if(obj[i].id.indexOf('btn_') != -1)		
			obj[i].style.display='';
	}
}

//添加业务数据的Id
function setInsertId(updateFormObj){
	var input = document.createElement("INPUT");
	input.setAttribute("type", "hidden");
	input.setAttribute("name", "currentid_insert_id");
	input.setAttribute("value", currentid_insert_id);
	updateFormObj.appendChild(input);
}
//保存
function saveStanding_target(){
	 //获取页面上file的个数
	 var inputs = document.getElementsByTagName("input");
	 var count = 0;
	 //附件的效验
	 for(var i=0;i<inputs.length;i++){
		var obj = inputs[i];
		if(obj.type=="file") count = count + 1; 
	 }
	for(var i=0;i<inputs.length;i++){
		if(inputs[i].type=="file"){
			var name=inputs[i].getAttribute("name");
			//var count=document.getElementsByTagName("iframe").length;
			if(validFileUploads("updateForm",count,name,NoFileTypes)==false){
	 			return false;
			}
		}
	}
	//自定义脚本验证的调用
	for(var i=0;i<inputs.length;i++){
		var obj=inputs[i];
		var val=obj.textvalidate;
		if(obj.type=="text" && typeof(val) != "undefined" ){
			if(!eval(val).test(obj.value)){
				new iMsgBox("［"+obj.desc+"］的输入格式不正确！", "填写提示").showAlert();
				obj.select();
				return false;
			}
		}
	}
	document.updateForm.action=basePath+
		"standing/updateAction.action?method=saveData&funID="
		+funID.value+"&linktype="+linktype+allParams;
	if(currentid){ document.updateForm.action=document.updateForm.action+"&currentid="+currentid.value; }
	
	if(!checkBeforeSave())
		return ;
	if(typeof(standing_MainUpdate) != 'undefined')
		setInsertId(document.updateForm);
	
	document.getElementById("standingSaveBtn").disabled=true;
	
	document.updateForm.submit();
}
function cancel(){
		if(linktype=='open')
			currentCancel();	
		else
			parentCancel();
}

function currentCancel(){
	var test = new DivWin.DivWin("confirm", "260px",  "");
	DivWin.clickYes = function (){
		window.close();
	}
	DivWin.clickNo = function (){
		test.close();
	}
	test.confirm(msgCancelEdit, msgYes , msgNo);		
}

function parentCancel(){
	if(cancel_link == null || cancel_link == "" || currentid.value == "")
		cancel_link = "standing/standingAction.action?method=list"+navigationParam+filtrateParams;
	else
		cancel_link +="&currentid="+currentid.value+allParams;
	
	if(cancel_link.indexOf("funID") == -1)
		cancel_link += "&funID="+funID.value;
	
	var win = getRootWindow();
	var test = new win.DivWin.DivWin("confirm", "260px",  "");
	test.clickYes = function (){
		win.closeDisabledWindow();
		//window.location.href=basePath+cancel_link;
		 window.history.back(-1);
		test.close();
	}
	test.clickNo = function(){
		win.closeDisabledWindow();
		test.close();
	}
	test.confirm(msgCancelEdit, msgYes , msgNo);
}
//调用权限的方法
//showUserByOrg('',1,0,'',1,'selectResult')//id也需要保存,后期处理
//showUserByRole('',0,'','',0,'selectResult')
//showUserByPos('',1,'',0,'selectResult')
//showUserByUser('',1,'',0,'selectResult')

//处理页面返回值
function selectResult(ids,names){
	document.getElementById(document.getElementById("sResult").value).value = ids;
	document.getElementById("name_"+document.getElementById("sResult").value).value = names;
}

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
//保存所有需要联动字段的类
var saveLinkageArray = new Array();
//下拉框联动	
function unionField(col,value){
	var linkageName=col;
	var val=value;
	//下拉框联动
	this.selectLinkage=function(){
		var params="&linkageName="+col+"&currentValue="+value+"&funID="+funID.value;
		$.ajax({
			url :'standing/updateAction.action?method=linkageData',
			type:'post', 
			dataType:'json', 
			data:params, 
			success:this.setUserSelect
		});
	}
	
	this.setUserSelect = function (json){
		var select_L = document.getElementById(linkageName);
		var linkageV = document.getElementById('linkageV');
		if(!select_L || !linkageV){
			return ;
		}
		select_L.options.length = 0;
		if(val == ""){
			var newoption = document.createElement("OPTION");
	        newoption.text = "---";
	        newoption.value = "";
	        select_L.add(newoption);
	        return;
		}
		if(json.linkageList != null){
			if(linkageV != null && linkageV.value != ''){
				for(var i=0;i<json.linkageList.length;i++){
					var linkageMap = json.linkageList[i];
					if(linkageV.value == linkageMap.ID){
						var newoption = document.createElement("OPTION");
		       			newoption.text = linkageMap.NAME;
		        		newoption.value = linkageMap.ID;
		        		if(linkageMap.ID != null && linkageMap.ID != ''){ 
		        			select_L.add(newoption); 
		       		 	}
		       		 	break;
					}
				}
			}
			
			if(selectNullOption == '0'){
				var newoption = document.createElement("OPTION");
		        newoption.text = "--请选择--";
		        newoption.value = "";
		        select_L.add(newoption); 
		    }
			for(var i=0;i<json.linkageList.length;i++){
				var linkageMap = json.linkageList[i];
				if(linkageV.value == linkageMap.ID)
					continue;
			 	var newoption = document.createElement("OPTION");
		        newoption.text = linkageMap.NAME;
		        newoption.value = linkageMap.ID;
		        if(linkageMap.ID != null && linkageMap.ID != ''){ 
		        	select_L.add(newoption); 
		        }
			}
		}
	}
}

function checkKeyCode(){   
    if(event.keyCode==13){
		if(event.srcElement && event.srcElement.id == 'perPageCount'){ 
		    //此段代码为解决:页面右下方设置每页显示记录条数不生效问题
            event.keyCode=13;
    	}else if(event.srcElement.tagName!="TEXTAREA"){
		    event.keyCode=9;
		}
			
    }
}
