<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/common.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css">
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.3.1.min.js" /></script>
	<title>Web Game FG - Main</title>
</head>
<body>
<script type="text/javascript">

//메인 페이지 뷰
$(document).ready(function(){
	// Load Team Class & Money Event
	$.ajax({
        type : "POST"
        , url : "/fg/start/getTeamInfo.do"
        , data : {}
        , success : function(data) {
        	//기본적으로 표시되는 사용자 정보
        	$("#team_class").text(data.grade);
        	$("#team_money").text(data.money);
        }
        , error : function(e) {
        	console.log(e.result);
        }
    });

	// 왼쪽 서브메뉴에 마우스를 대면 목록이 나온다.
	$("body").on("mouseover",".left_sub_menu",function(){
		$(this).show();
	});

	// 왼쪽 서브메뉴에 마우스를 치우면 목록이 사라진다.
	$("body").on("mouseout",".left_sub_menu",function(){
		$(this).hide();
	});

	// 왼쪽 서브 메뉴 Mouseover Event
	$("body").on("mouseover",".left_sub_menu p",function(){
		$(this).css("color","red");
	});

	// 왼쪽 서브 메뉴  Mouseout Event
	$("body").on("mouseout",".left_sub_menu p",function(){
		$(this).css("color","black");
	});
	
	// 왼쪽 메인 메뉴  Mouseover Event
	$(".left_menu").on("mouseover",function(){
		var _this = $(this);
		_this.children(".left_main_menu").css("background-color","silver");
		_this.children(".left_main_menu").css("border-bottom","1px solid");
		_this.children(".left_sub_menu").show();
	});
	
	//왼쪽 메인 메뉴  Mouseout Event
	$(".left_menu").on("mouseout",function(){
		var _this = $(this);
		_this.children(".left_main_menu").css("background-color","white");
		_this.children(".left_main_menu").css("border-bottom","0px");
		_this.children(".left_sub_menu").hide();
	});
	
	// 상위 Main 클릭시 main Page로 이동
	$(".main_banner").on("click", function(){
		var form = $("#send_form");
		form.attr('method', 'post');
		form.attr('action', "/fg/start/mainPage.do");
		form.submit();
	})
	
	// Game room Page로 이동 
	$("#start_game").on("click",function(){
		var form = $("#send_form");
		form.attr('method', 'post');
		form.attr('action', "/fg/game/playGame.do");
		form.submit();
	});

	// Team Setting Page로 이동
	$("#team_setting").on("click",function(){
		var form = $("#send_form");
		form.attr('method', 'post');
		form.attr('action', "/fg/manage/teamSetting.do");
		form.submit();
	});

	// Market Page 이동 Event
	$("#move_market").on("click",function(){
		var form = $("#send_form");
		form.attr('method', 'post');
		form.attr('action', "/fg/transfer/transferPage.do");
		form.submit();
	});

	// Record Page 이동 Event
	$("#league_record").on("click",function(){
		alert("league_record");
	});
	
	// Test Page 이동 Event
	$("#test_page").on("click",function(){
		var form = $("#send_form");
		form.attr('method', 'post');
		form.attr('action', "/fg/test/moveTest.do");
		form.submit();
	});
	
	// Logout Event
	$("#logoutBt").on("click", function(){
		$.ajax({
	        type : "POST"
	        , url : "/fg/logout/logoutAction.do"
	        , data : {}
	        , success : function(data) {
	    		var form = $("#send_form");
	    		form.attr('method', 'post');
	    		form.attr('action', "/fg/login/loginPage.do");
	    		form.submit();
	        }
	        , error : function(e) {
	        	console.log(e.result);
	        }
	    });
	});
	
	$("#start_game_sub").on("click",function(){
		var form = $("#send_form");
		form.attr('method', 'post');
		form.attr('action', "/fg/game/gamePage.do");
		form.submit();
	});
	// 모든 페이지 공통 Event
	
	
	$("#remove_game").on("click", function(){
		$.ajax({
	        type : "POST"
	        , url : "/fg/game/removeGame.do"
	        , data : {}
	        , success : function(data) { }
	        , error : function(e) {
	        	console.log(e.result);
	        }
	    });
	});
})
</script>
<form id="send_form">
</form>
<div class='main_page_html'>
<input type='button' value='L O G O U T' id='logoutBt'>
<!--다른 화면에서도 사용하는 페이지  -->
<table class='main_table'>
	<tr>
		<td class='main_banner' rowspan='2' colspan='3'>main</td>
		<td class='top_txt'>가치</td>
		<td class='top_val' id='team_money'>1,000</td>
	</tr>
	<tr>
		<td class='top_txt'>등급</td>
		<td class='top_val' id='team_class'>S</td>
	</tr>
	<tr>
		<td class='left_menu'>
		 	<!--게임 테스트를 위해 바로 게임페이지로 넘어가기 위해 만든 임시 페이지-->
			<div class='left_main_menu' id='start_game'>1.경기시작</div>
			<div class='left_sub_menu' id='start_game_sub'>
				<p>- test1</p>
				<p>- test2</p>
				<p>- test3</p>
				<p>- test4</p>
			</div>
		</td>
		<td rowspan='6' colspan='4' class='content_window' style='height: 650px;'>
			<table class='content_table' style='height: 650px;'>
				<tr class='content_tr'>
				<!-- content start -->
					<td class='content_700'>
						<input type='button' id='remove_game' value='remove game'>
						page1
					</td>
					<td class='content_700'>page2</td>
				<!-- content end -->
				</tr>
				<tr>
					<td class='notice_window' colspan='3'>notice</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td class='left_menu'>
			<div class="left_main_menu" id='team_setting'>2.구단 관리</div>
			<div class='left_sub_menu'>
				<p>- test1</p>
				<p>- test2</p>
				<p>- test3</p>
				<p>- test4</p>
			</div>
		</td>
	</tr>
	<tr>
		<td class='left_menu'>
			<div class="left_main_menu" id='move_market'>3.이적 시장</div>
			<div class='left_sub_menu'>
				<p>- test1</p>
				<p>- test2</p>
				<p>- test3</p>
				<p>- test4</p>
			</div>
		</td>
	</tr>
	<tr>
		<td class='left_menu'>
			<div class="left_main_menu" id='league_record'>4.리그 기록</div>
			<div class='left_sub_menu'>
				<p>- test1</p>
				<p>- test2</p>
				<p>- test3</p>
				<p>- test4</p>
			</div>
		</td>
	</tr>
	<tr>
		<td class='left_menu'>
			<div class='left_main_menu' id='test_page'>*.T E S T</div>
			<div class='left_sub_menu'>
				<p>- test1</p>
				<p>- test2</p>
				<p>- test3</p>
				<p>- test4</p>
			</div>
		</td>
	</tr>
	<tr>
		<td class='left_blank'>5</td>
	</tr>
</table>
</div>
</body>
</html>