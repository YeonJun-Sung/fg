<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/sockjs-0.3.4.js" /></script>
<title>Web Game FG - Main</title>
</head>
<body>
<script type="text/javascript">
$(document).ready(function(){
	$.ajax({
        type : "POST"
        , url : "/fg/start/getTeamInfo.do"
        , data : {}
        , success : function(data) {
        	$("#team_class").text(data.grade);
        	$("#team_money").text(data.money);
        }
        , error : function(e) {
        	console.log(e.result);
        }
    });

	$("body").on("mouseover",".left_sub_menu",function(){
		$(this).show();
	});

	$("body").on("mouseout",".left_sub_menu",function(){
		$(this).hide();
	});

	$("body").on("mouseover",".left_sub_menu p",function(){
		$(this).css("color","red");
	});

	$("body").on("mouseout",".left_sub_menu p",function(){
		$(this).css("color","black");
	});
	
	$(".left_menu").on("mouseover",function(){
		var _this = $(this);
		_this.children(".left_main_menu").css("background-color","silver");
		_this.children(".left_main_menu").css("border-bottom","1px solid");
		_this.children(".left_sub_menu").show();
	});
	
	$(".left_menu").on("mouseout",function(){
		var _this = $(this);
		_this.children(".left_main_menu").css("background-color","white");
		_this.children(".left_main_menu").css("border-bottom","0px");
		_this.children(".left_sub_menu").hide();
	});
	
	$(".main_banner").on("click", function(){
		var form = $("#send_form");
		form.attr('method', 'post');
		form.attr('action', "/fg/start/mainPage.do");
		form.submit();
	});
	
	$(".main_banner").on("click", function(){
		var form = $("#send_form");
		form.attr('method', 'post');
		form.attr('action', "/fg/start/mainPage.do");
		form.submit();
	})
	
	
	$("#start_game").on("click",function(){
		alert("start_game");
	});
	
	$("#team_setting").on("click",function(){
		var form = $("#send_form");
		form.attr('method', 'post');
		form.attr('action', "/fg/manage/teamSetting.do");
		form.submit();
	});
	
	$("#move_market").on("click",function(){
		var form = $("#send_form");
		form.attr('method', 'post');
		form.attr('action', "/fg/transfer/transferPage.do");
		form.submit();
	});
	
	$("#league_record").on("click",function(){
		alert("league_record");
	});
	
	$("#test_page").on("click",function(){
		var form = $("#send_form");
		form.attr('method', 'post');
		form.attr('action', "/fg/test/socketTest.do");
		form.submit();
	});
	
    $("#sendBtn").click(function() {
    	send();
    });
    
    $(".openBtn").click(function() {
    	openSocket($(this).data("num"));
    });
    
    $("#closeBtn").click(function() {
    	closeSocket();
    });
})

var ws;
var messages = $("#messages");

function openSocket(room_num){
    if(ws != undefined && ws.readyState != WebSocket.CLOSED){
        writeResponse("WebSocket is already opened.");
        return;
    }
    
    //웹소켓 객체 만드는 코드
    ws = new WebSocket("ws://192.168.219.100:8080/fg/echo.do?room_num="+room_num);
    
    ws.onopen = function(event) {
        if(event.data == undefined) return;
        
        writeResponse(event.data);
    };
    ws.onmessage = function(event) {
        writeResponse(event.data);
    };
    ws.onclose = function(event) {
        writeResponse("Connection closed");
    }
}

function send(){
    var text = $("#message").val() + "," + $("#uesr_id").val();
    ws.send(text);
    text="";
}

function closeSocket(){
    ws.close();
}
function writeResponse(text){
	$("#output").append("<p>" + text + "</p>");
}
</script>
<form id="send_form">
</form>
<input type='hidden' id='uesr_id' value='${userId }'>
<div class='main_page_html'>
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
				<div class="left_main_menu" id='start_game'>1.경기시작</div>
				<div class='left_sub_menu'>
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
						<td class='content_500'>page1</td>
						<td class='content_400'>
							<form action="">
								<input id="sendBtn" value="Send" type="button">
								<input class="openBtn" value="Open" type="button" data-num="1">
								<input class="openBtn" value="Open" type="button" data-num="2">
								<input id="closeBtn" value="Close" type="button">
								<input id="message" name="message" value="Hello WebSocket!" type="text"><br>
				            </form>
						    <div id="output"></div>
						</td>
						<td class='content_500'>page2</td>
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
				<div class="left_main_menu" id='test_page'>*.T E S T</div>
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