<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/common.css">
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery-3.3.1.min.js" /></script>
<title>Web Game FG - Test</title>
</head>
<body>
<script type="text/javascript">
var start_time = new Date();	// 리그 시작 시간
var match_end = new Date();		// 최근 경기 종료 시간
var match_start = 0;			// 경기 시간 timer 변수
var break_time = 0;				// break time timer 변수
var log_count = 0;				// 경기당 로그 수
var first_half = 3;				// 전반 추가 시간
var second_half = 3;			// 후반 추가 시간
var match_count = 0;			// 진행한 경기 수 
var define_match_time = 3;		// 한 경기당 실제 소요할 시간 설정
var define_tactical_time = 1;	// 경기 시작 전 전략 수정에 소요할 시간 설정 > 실제 분 단위
var define_break_time = 10;		// 경기 중간 쉬는 시간 설정 > 한 경기당 실제 소요할 시간에 비례	ex) 3분 단위 경기 > 쉬는 시간 10분 = 3분 * 10 / 90 = 60초 * 1 / 3 = 20초 

$(document).ready(function(){
	define_match_time = $("#define_match_time").val() == "" || $("#define_match_time").val() == null?3:$("#define_match_time").val();
	define_tactical_time = $("#define_tactical_time").val() == "" || $("#define_tactical_time").val() == null?1:$("#define_tactical_time").val();
	define_break_time = $("#define_break_time").val() == "" || $("#define_break_time").val() == null?10:$("#define_break_time").val();

	$("#first_menu").on("click", function(){
		var form = $("#send_form");
		form.attr('action', "/fg/test/testMakeGame.do");
		form.submit();
	});
	$("#second_menu").on("click", function(){
		var form = $("#send_form");
		form.attr('action', "/fg/test/testPage.do");
		form.submit();
	});
	$("#third_menu").on("click", function(){
		var form = $("#send_form");
		form.attr('action', "/fg/test/testTeamSetting.do");
		form.submit();
	});
	$("#fourth_menu").on("click", function(){
		var form = $("#send_form");
		form.attr('action', "/fg/test/testPage.do");
		form.submit();
	});
	
	$("#match_start").on("click", function(){
		match_start = 0;
		log_count = 1;
		break_time = 0;
		match_count++;
		match_end = new Date();

		$("#match_count").text(""+match_count);
		$("#match_log").empty();
	});
})
setInterval(function() {
	var current_time = new Date();
	var game_time = 0;
	var time_h = 0;
	var time_m = 0;
	var time_s = 0;

	game_time = parseInt((current_time - start_time) / 1000);
	totalTimer(game_time);
	
	game_time = parseInt((current_time - match_end) / 1000);
	if(game_time < define_tactical_time * 60){
		tacticalTimer(game_time);
	}
	else {
		var log = "";
		matchLog();
		matchTimer();		
		intoBreakTime();
			
		if(match_start > (90 + second_half) * 60)
			$("#match_start").trigger("click");
	}

}, ((define_match_time * 60) / 6000) * 1000);

function matchLog() {
	var log = "";
	if(match_start == 0) {
		log = "" + (match_count + 1) + " Round Match Start!!!";
		$("#match_log").prepend("<p>" + log + "</p>");
		log = "First Half Strat!!!";
		$("#match_log").prepend("<p>" + log + "</p>");
	}
	else if(match_start == 44 * 60) {
		log = "First Half Additional Time is " + first_half + "!!!";
		$("#match_log").prepend("<p>" + log + "</p>");
	}
	else if(match_start == 89 * 60) {
		log = "Second Half Additional Time is " + second_half + "!!!";
		$("#match_log").prepend("<p>" + log + "</p>");
	}
	else if(break_time == define_break_time * 60) {
		log = "Second Half Strat!!!";
		$("#match_log").prepend("<p>" + log + "</p>");
	}
}

function intoBreakTime() {
	var match_time = 0;
	var time_h = 0;
	var time_m = 0;
	var time_s = 0;
	if(match_start > (45 + first_half) * 60 && break_time == 0){
		match_start = 45 * 60;
		match_time = match_start;
		time_m = parseInt(match_time / 60);
		time_s = parseInt(match_time % 60);
		$("#match_time").text((time_m < 10?"0" + time_m:time_m) + ":" + (time_s < 10?"0" + time_s:time_s));
		match_time = break_time;
		break_time++;
		time_m = parseInt(match_time / 60);
		time_s = parseInt(match_time % 60);
		$("#break_time").text((time_m < 10?"0" + time_m:time_m) + ":" + (time_s < 10?"0" + time_s:time_s));

		var log = "Break Time!!!";
		$("#match_log").prepend("<p>" + log + "</p>");
	}
}

function matchTimer() {
	var match_time = 0;
	var time_h = 0;
	var time_m = 0;
	var time_s = 0;
	if(break_time < 1 || break_time > define_break_time * 60){
		match_time = match_start;
		match_start++;
		time_m = parseInt(match_time / 60);
		time_s = parseInt(match_time % 60);
		
		$("#match_time").text((time_m < 10?"0" + time_m:time_m) + ":" + (time_s < 10?"0" + time_s:time_s));
	}
	else {
		match_time = break_time;
		break_time++;
		time_m = parseInt(match_time / 60);
		time_s = parseInt(match_time % 60);
		
		$("#break_time").text((time_m < 10?"0" + time_m:time_m) + ":" + (time_s < 10?"0" + time_s:time_s));
	}
}

function tacticalTimer(game_time) {
	var time_h = 0;
	var time_m = 0;
	var time_s = 0;
	time_h = parseInt(game_time / 3600);
	time_m = parseInt((game_time / 60) % 60);
	time_s = parseInt(game_time % 60);
	
	$("#tactical_time").text((time_h == 0?"":time_h + ":") + (time_m < 10?"0" + time_m:time_m) + ":" + (time_s < 10?"0" + time_s:time_s));
}

function totalTimer(game_time) {
	var time_h = parseInt(game_time / 3600);
	var time_m = parseInt((game_time / 60) % 60);
	var time_s = parseInt(game_time % 60);	
	$("#total_time").text((time_h == 0?"":time_h + ":") + (time_m < 10?"0" + time_m:time_m) + ":" + (time_s < 10?"0" + time_s:time_s));
}
</script>
	<form id="send_form">
		<input name="send_key" id="send_key" type="hidden" value="${key }">
		<input name="define_match_time" id="define_match_time" type="hidden" value="${define_match_time }">
		<input name="define_tactical_time" id="define_tactical_time" type="hidden" value="${define_tactical_time }">
		<input name="define_break_time" id="define_break_time" type="hidden" value="${define_break_time }"> 
	</form>
	<table style='width: 100%; height: 100%'>
		<tr border='1px solid'>
			<td rowspan='2' colspan='3' style='width: 70%; border: 1px solid;'>main</td>
			<td style='width: 10%; border: 1px solid;'>가치</td>
			<td style='width: 15%; border: 1px solid;'>1,000</td>
		</tr>
		<tr>
			<td style='border: 1px solid;'>등급</td>
			<td style='border: 1px solid;'>S</td>
		</tr>
		<tr>
			<td style='heigth: 20px; width: 20%; border: 1px solid;' id="first_menu">1</td>
			<td rowspan='5' colspan='4' style='width: 200px; border: 1px solid;'>
				<table style='width: 100%; height: 100%;'>
					<tr style='height: 70%;'>
						<td style='border-right: 1px solid;width:30%;vertical-align: top;'>
							<div>총 시간 <span id="total_time">00:00</span></div>
							<div>전술 수정 <span id="tactical_time">00:00</span></div>
							<div>게임 시간 <span id="match_time">00:00</span></div>
							<div>쉬는 시간 <span id="break_time">00:00</span></div>
							<div>진행 경기 <span id="match_count">1</span></div>
							<div><input type="hidden" value="match_start" id="match_start"></div>
						</td>
						<td style='border-left: 1px solid;vertical-align:top;'>
							<div>매치 로그</div>
							<div id="match_log" style="overflow-y:scroll;height:90%;"></div>
						</td>
					</tr>
					<tr style='height: 30%;'>
						<td colspan='2' style='border-top: 1px solid;'>notice</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td style='heigth: 20px; border: 1px solid;' id="second_menu">2</td>
		</tr>
		<tr>
			<td style='heigth: 20px; border: 1px solid;' id="third_menu">3</td>
		</tr>
		<tr>
			<td style='heigth: 20px; border: 1px solid;' id="fourth_menu">4</td>
		</tr>
		<tr>
			<td style='height: 200px; border: 1px solid;'>5</td>
		</tr>
	</table>
</body>
</html>