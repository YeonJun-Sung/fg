<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/common.css">
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.3.1.min.js"/></script>
	<title>Web Game FG - Find</title>
</head>
<body>
<script type="text/javascript">
$(document).ready(function(){
	// mode에 따른 ID input 창의 show 여부
	var mode = $("#find_mode").val();
	if(mode == "pw")
		$("#id_input_div").show();
	else
		$("#id_input_div").hide();
	// Find Event Trigger
	$("#email_input, #id_input").on("keyup",function(e){
		if(e.keyCode == 13) $("#find_button").trigger("click");
	})
	
	// Find Event
	// mode에 따라 ID, PW 찾기 결정
	// Send Data > Email, ID, mode
	$("#find_button").on("click",function(){
		var email = $("#email_input").val();
		var id = $("#id_input").val();
		$.ajax({
	        type : "POST",
	        url : "/fg/login/findUserInfo.do",
	        data : {
	        	send_email : email
	        	, send_id : id
	        	, send_mode : mode
        	},
	        success : function(data) {
	        	console.log(data)
	        	var result = data.id?data.id:data.pw;
	        	var error = data.error;
	        	if(error != "" && error != null){
	        		alert(error)
	        	}
	        	else {
		        	$(".dialog").show();
		        	$(".black").show();
					var sp = result.split("");
					result = ""
					// 앞자리 2개빼고 원하는 정보를 알려준다.
					for(var i in sp){
						if(i < 2)	result += sp[i];
						else	result += "*"
					}
		        	$("#result_text").text(""+result);
	        	}
	        }, error : function(e) {
	        	console.log(e.result);
	        }
	    });
	});
	
	// Dialog 종료 Event
	$("body").on("click", ".black, #close_button", function(){
    	$(".dialog").hide();
    	$(".black").hide();
    	$("#result_text").text("");
	});
	
	// 이전 페이지 이동 Event
	$("#back_button").on("click",function(){
		var form = $("#back_form");
		form.attr('action', "/fg/login/loginPage.do");
		form.submit();
	});
})
</script>
<form id="back_form"></form>
<input type="hidden" id="find_mode" value="${mode }">
<div class="html_part">
<div class="div_center">
	<div class="div_text" id="id_input_div" style="display:none;">
		<input type="text" id="id_input" class="login_page_input" placeholder="아이디를 입력하세요." value="">
	</div>
	<div class="div_text"><input type="text" id="email_input" class="login_page_input" placeholder="이메일을 입력하세요." value=""></div>
	<div class="div_button"><input type="button" id="find_button" class="login_page_button" value="FIND"></div>
	<div class="div_button_bottom">
		<span class="div_right"><input type="button" id="back_button" class="none_bg_button" value="back"></span>
	</div>
</div>
</div>

<!-- 찾은 정보 보여주는 Dialog -->
<div class="black"></div>
<div class="dialog">
	<div class="result_div">Find ${mode } result : <span id="result_text"></span></div>
	<div class="div_button_bottom">
		<!-- id나 password 가 보여지는 창 -->
		<span class="div_right"><input type="button" id="close_button" class="none_bg_button" value="close"></span>
	</div>
</div>
</body>
</html>