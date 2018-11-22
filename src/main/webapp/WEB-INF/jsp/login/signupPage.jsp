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
	<title>Web Game FG - Signup</title>
</head>
<body>
<script type="text/javascript">
$(document).ready(function(){
	var duplicate_id = 0;		// ID	중복	확인용 변수
	var duplicate_name = 0;		// 닉네임	중복	확인용 변수
	var auth_email = 0;			// 이메일	인증	확인용 변수
	var check_auth = 0;			// 인증번호	확인용 변수
	
	var auth = "";				// 인증번호	저장용 변수

	// E-mail 변경 Event
	// Enter > 이메일 인증번호 전송 Event Trigger
	// 
	$("#email_input").on("keyup",function(e){
		if(e.keyCode == 13) $("#auth_email_button").trigger("click");
		else auth_email = 0;
	});
	
	// 인증번호 변경 Event
	// Enter > 인증번호 확인 Event Trigger
	
	$("#check_auth").on("keyup",function(e){
		if(e.keyCode == 13) $("#auth_button").trigger("click");
		else check_auth = 0;
	});
	
	// PW 일치 확인 Event
	$("#pw_input, #confirm_pw").on("keyup", function(){
		if($("#pw_input").val() != null && $("#pw_input").val() != ""
				&& $("#confirm_pw").val() == $("#pw_input").val()) {
			$("#pw_check").text("비밀번호가 일치합니다.");
		}
		else {
			$("#pw_check").text("비밀번호를 확인해주세요.");
		}
	});
	
	// ID 중복 확인 Event
	// Send Data > ID
	$("#id_input").on("keyup",function(){
		var id_input = $("#id_input").val();
		$.ajax({
           type : "POST"
           , url : "/fg/login/checkId.do"
           , data : { send_id : id_input }
           , success : function(data) {
        	   var key = data.user_key;
        	   var error = data.error;
        	   if (error != null) {
        		   console.log(error);
        		   $("#id_check").text("");
        		   duplicate_id = 0;
       		   }
        	   else if(key == null) {
        		   $("#id_check").text("사용 가능한 아이디 입니다.");
        		   $("#id_check").css("color", "blue");
        		   duplicate_id = 1;
       		   }
        	   else {
        		   $("#id_check").text("이미 사용중인 아이디 입니다.");
        		   $("#id_check").css("color", "red");
        		   duplicate_id = 0;
       		   }
           }, error : function(e) {
              console.log(e.result);
           }
       });
	});
	
	// 닉네임 중복 확인 Event
	// Send Data > name
	$("#name_input").on("keyup",function(){
		var name_input = $("#name_input").val();
		$.ajax({
           type : "POST"
           , url : "/fg/login/checkName.do"
           , data : { send_name : name_input }
           , success : function(data) {
        	   var key = data.user_key;
        	   var error = data.error;
        	   if (error != null) {
        		   console.log(error);
        		   $("#name_check").text("");
        		   duplicate_name = 0;
       		   }
        	   else if(key == null) {
        		   $("#name_check").text("사용 가능한 닉네임 입니다.");
        		   $("#name_check").css("color", "blue");
        		   duplicate_name = 1;
       		   }
        	   else {
        		   $("#name_check").text("이미 사용중인 닉네임 입니다.");
        		   $("#name_check").css("color", "red");
        		   duplicate_name = 0;
       		   }
           }, error : function(e) {
              console.log(e.result);
           }
       });
	});
	
	// 이메일 인증번호 전송 Event
	// Send Data > email
	$("#auth_email_button").on("click",function(){
		var email_input = $("#email_input").val();
		$("#auth_div").show();
		$("#check_auth").val("");
		$.ajax({
           type : "POST"
           , url : "/fg/login/authEmail.do"
           , data : { send_email : email_input }
           , success : function(data) {
        	   auth = data;
        	   if(data == "duplicate"){
        		   $("#email_check").text("이미 사용중인 이메일 입니다.");
        		   $("#email_check").css("color", "red");
        		   $("#auth_div").hide();
        	   }
        	   else {
        		   $("#email_check").text("사용 가능한 이메일 입니다.");
        		   $("#email_check").css("color", "blue");
            	   check_auth = 0;
           		   auth_email = 1;   
        	   }
           }
           , error : function(e) {
              console.log(e.result);
           }
       });
		
	});

	// 이전 페이지 이동
	$("#back_button").on("click",function(){
		var form = $("#back_form");
		form.attr('action', "/fg/login/loginPage.do");
		form.submit();
	});

	// 인증번호 일치 확인
	$("body").on("click","#auth_button",function(){
		if($("#check_auth").val() == auth){
			alert("Correct auth number");
			check_auth = 1;
		}
		else
			alert("Wrong auth number");
	});

	// Signup Event
	
	//모든 회원가입 정보 입력시 정보를 저장
	$("#signup_button").on("click",function(){
		if(duplicate_id == 0)
			alert("Please check duplicate ID");
		else if(duplicate_name == 0)
			alert("Please check duplicate Name");
		else if(auth_email == 0)
			alert("Please Authenticate E-mail");
		else if(check_auth == 0)
			alert("Please check Auth number");
		else if($("#pw_input").val() == null || $("#pw_input").val() == ""
				|| $("#confirm_pw").val() != $("#pw_input").val())
			alert("Please check Password");
		else {
			var id_input = $("#id_input").val();
			var name_input = $("#name_input").val();
			var pw_input = $("#pw_input").val();
			var email_input = $("#email_input").val();
			$.ajax({
	           type : "POST"
	           , url : "/fg/login/signupAction.do"
	           , data : {
	        	   send_id : id_input
	        	   , send_name : name_input
	        	   , send_pw : pw_input
	        	   , send_email : email_input
        	   }
	           , success : function() {
	        	   alert("Success Singup");
	        	   $("#back_button").trigger("click");
	           }
	           , error : function(e) {
	        	   console.log(e.result);
	           }
	       });
		}
	});
})
</script>
<form id="back_form"></form>
<div class="html_part">
<div class="div_center" style="top:40%;">
	<div class="div_text">
		<input type="text" id="id_input" class="login_page_input" placeholder="아이디를 입력하세요.">
	</div>
   	<span id="id_check"></span>
   	
	<div class="div_text">
		<input type="text" id="name_input" class="login_page_input" placeholder="닉네임을 입력하세요.">
    </div>
   	<span id="name_check"></span>
   	
	<div class="div_text">
		<input type="password" id="pw_input" class="login_page_input" placeholder="비밀번호를 입력하세요.">
    </div>
    
    <div class="div_text">
    	<input type="password" id="confirm_pw" class="login_page_input" placeholder="비밀번호를 다시 한번 입력하세요.">	
    </div>
   	<span id="pw_check">비밀번호를 확인해주세요.</span>
   	
	<div class="div_text">
		<input type="text" id="email_input" class="login_page_input_button" placeholder="이메일을 입력하세요.">    
		<input type="button" id="auth_email_button" class="login_page_button_input" value="auth">
	</div>
   	<span id="email_check"></span>
	
    <div class="div_text" style="display:none;" id="auth_div">
		<input type="text" id="check_auth" class="login_page_input_button" placeholder="인증번호를 입력하세요.">    
		<input type="button" id="auth_button" class="login_page_button_input" value="OK">
    </div>
    
	<div class="div_button"><input type="button" id="signup_button" class="login_page_button" value="SIGNUP"></div>
	<div class="div_button_bottom">
		<span class="div_right"><input type="button" id="back_button" class="none_bg_button" value="back"></span>
	</div>
</div>
</div>
</body>
</html>