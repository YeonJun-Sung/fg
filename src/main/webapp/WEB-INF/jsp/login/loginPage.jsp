<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<!--로그인이 되는 페이지-->
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/common.css">
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.3.1.min.js"/></script>
	<title>Web Game FG - Login</title>
</head>
<body>
<script type="text/javascript">
$(document).ready(function(){
	
	$.ajax({
	    type : "POST"
        , url : "/fg/login/logoutAction.do"
        , data : {}
        , success : function(data) { }
        , error : function(e) {
        	console.log(e.result);
        }
    });
	
	// Login Event Trigger
	$("#id_input, #pw_input").on("keyup",function(e){
		if(e.keyCode == 13) $("#login_button").trigger("click");
	})
	
	// Login Event
	// team 없는 유저 > CreateTeam 이동
	// team 있는 유저 > mainPage	이동
	$("#login_button").on("click",function(){
		var id_input = $("#id_input").val();
		var pw_input = $("#pw_input").val();
		$.ajax({
	        type : "POST"
	        , url : "/fg/login/loginAction.do"
	        , data : {
	        	send_id : id_input
	        	, send_pw : pw_input
	        }
	        , success : function(data) {
	    		var key = data.user_key;
	        	if(key != null){
		    		var	team = data.team_key;
		    		var form = $("#send_form");
		    		if(team == null){
			    		form.attr('method', 'post');
			    		form.attr('action', "/fg/create/createTeam.do");
		    			form.submit();
		    		}
		    		else {
		    			var form = $("#send_form");
		    			form.attr('method', "POST");
		    			form.attr('action', "/fg/start/mainPage.do");
		    			form.submit();
		    		}
	        	}
	        	else {
	        		var msg = data.error;
	        		alert("Login Fail!!\n" + msg + "!!");
	        	}
	        }
	        , error : function(e) {
	        	console.log(e.result);
	        }
	    });
	});
	
	//id를 찾기 위해 findpage로 이동한다.
	$("#find_id_button").on("click",function(){
		var form = $("#send_mode");
		$("#mode").attr('value', 'id');
		form.attr('action', "/fg/login/findPage.do");
		form.submit();
	});

	// password를 찾기 위해 findpage로 이동한다.
	$("#find_pw_button").on("click",function(){
		var form = $("#send_mode");
		$("#mode").attr('value', 'pw');
		form.attr('action', "/fg/login/findPage.do");
		form.submit();
	});
})
</script>
<form id="send_form">
</form>
<form id="send_mode">
	<input name="send_mode" id="mode" type="hidden" value="">
</form>
<div class="html_part">
<div class="div_center">
	<div class="div_text"><input type="text" id="id_input" class="login_page_input" placeholder="아이디를 입력하세요." value=""></div>
	<div class="div_text"><input type="password" id="pw_input" class="login_page_input" placeholder="비밀번호를 입력하세요." value=""></div>
	<div class="div_button"><input type="button" id="login_button" class="login_page_button" value="LOGIN"></div>
	<div class="div_button_bottom">
		<input type="button" id="find_id_button" class="none_bg_button" value="Find ID">
		<input type="button" id="find_pw_button" class="none_bg_button" value="Find PW">
		
		<!--sign_up page 띄우기는 링크로 한다.-->
		
		<span class="div_right"><a href="/fg/login/signupPage.do"><input type="button" id="signup_button" class="none_bg_button" value="signup"></a></span>
	</div>
</div>
</div>
</body>
</html>