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
	<title>Web Game FG - Room</title>
</head>
<body>
<script type="text/javascript">
$(document).ready(function() {
	waitingUser();
	
	// 사용자 기본정보 가져오기
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
	
	// Main Page 이동 Event
	$(".main_banner").on("click", function() {
		var form = $("#send_form");
		form.attr('method', 'post');
		form.attr('action', "/fg/start/mainPage.do");
		form.submit();
	});
	
	// Logout Event
	$("#logoutBt").on("click", function() {
		$.ajax({
			type : "POST",
			url : "/fg/logout/logoutAction.do",
			data : {},
			success : function(data) {
				var form = $("#send_form");
				form.attr('method', 'post');
				form.attr('action', "/fg/login/loginPage.do");
				form.submit();
			},
			error : function(e) {
				console.log(e.result);
			}
		});
	});
	
	// Room Exit Event
	$("#exitBt").on("click", function() {
    	clearInterval(waitInterval);
		$.ajax({
			type : "POST",
			url : "/fg/room/exitRoom.do",
			data : {},
			success : function(data) {
				var form = $("#send_form");
				form.attr('method', 'post');
				form.attr('action', "/fg/start/mainPage.do");
				form.submit();
			},
			error : function(e) {
				console.log(e.result);
			}
		});
	});

	$.ajax({
		type : "POST"
		, url : "/fg/room/getRoomId.do"
		, data : { }
		, success : function(data) {
			//리그의 인원수
			var room_capcity = $("#max").val();
			var temp = new Array(room_capcity);
			console.log("getRoomId");
			console.log(data);
			
			// user_number > 접속한 순서대로의 플레이어 넘버
			// 해당 게임카운트(game_count)에 알맞은 정보를 가져와 게임을 순서대로 모두 진행
			
			//방정보를 배열에 저장
			for(var i = 0;i < data.length;i++) {
				var idx = data[i].user_number;
				var temp_object = new Object();
				temp_object.game_count = data[i].game_count;
				temp_object.user_key = data[i].user_key;
				temp_object.overall = data[i].overall;
				temp_object.id = data[i].id;
				temp_object.rating = data[i].rating;
				temp[idx] = temp_object;
			}
			
			//각각 저장한 방 정보를 뿌려준다. (방생성)
			$(".user_list").each(function(index){
				var rtvHtml = "";
				$(this).children("div").empty();
				if(temp[index] != null){
					rtvHtml += "<table style='height: 130px; width:730px; margin: 10px;'>";
					rtvHtml += "<tbody>";
					rtvHtml += "<tr>";
					rtvHtml += "<td rowspan='2' style='width:130px; height: 130px; padding: 0px;'>";
					//리그에 알맞는 사진 리그의 정보를 변수로 사용 
					rtvHtml += "<img src='${pageContext.request.contextPath}/img/league_" + temp[index].rating + ".jpg'  style='width: 130px; height: 130px;'>";
					rtvHtml += "</td>";
					rtvHtml += "<td style='border-right: 1px white solid; border-bottom: 1px white solid; text-align:center; vertical-align:middle; width: 300px; height: 65px; padding: 0px;'>I D</td>";
					rtvHtml += "<td style='border-right: 1px white solid; border-bottom: 1px white solid; text-align:center; vertical-align:middle; width: 300px; height: 65px; padding: 0px;'>" + temp[index].id + "</td>";
					rtvHtml += "<td style='border-right: 1px white solid; border-bottom: 1px white solid; text-align:center; vertical-align:middle; width: 300px; height: 65px; padding: 0px;'>승 - 무 - 패</td>";
					rtvHtml += "<td style='border-bottom: 1px white solid; text-align:center; vertical-align:middle; width: 300px; height: 65px; padding: 0px;'>0-0-0</td>";
					rtvHtml += "</tr>";
					rtvHtml += "<tr>";
					rtvHtml += "<td style='border-right: 1px white solid; text-align:center; vertical-align:middle; width: 300px; height: 65px; padding: 0px;'>OVERALL</td>";
					rtvHtml += "<td style='border-right: 1px white solid; text-align:center; vertical-align:middle; width: 300px; height: 65px; padding: 0px;'>" + temp[index].overall + "</td>";
					rtvHtml += "<td style='border-right: 1px white solid; text-align:center; vertical-align:middle; width: 300px; height: 65px; padding: 0px;'>주요 전술</td>";
					rtvHtml += "<td style='text-align:center; vertical-align:middle; width: 300px; height: 65px; padding: 0px;'>4-4-2</td>";
					rtvHtml += "</tr>";
					rtvHtml += "</tbody>";
					rtvHtml += "</table>";
					$(this).children("div").append(rtvHtml);	
				}
			});
		}
		, error : function(e) {
			console.log(e.result);
		}
	});
})

//사용자가 들어올때 마다 화면갱신하여 방 접속 유무 판단
function waitingUser() {
	$.ajax({
		type : "POST"
		, url : "/fg/room/getUserCountInRoom.do"
		, data : { }
		, success : function(data) {
			var room_capcity = $("#max").val();
			if(data != room_capcity){
				waitInterval = setInterval(function(){
					$.ajax({
						type : "POST"
						, url : "/fg/room/getRoomId.do"
						, data : { }
						, success : function(data) {
							//리그의 인원수
							var temp = new Array(room_capcity);
							console.log("getRoomId");
							console.log(data);
							
							// user_number > 접속한 순서대로의 플레이어 넘버
							// 해당 게임카운트(game_count)에 알맞은 정보를 가져와 게임을 순서대로 모두 진행
							
							//방정보를 배열에 저장
							for(var i = 0;i < data.length;i++) {
								var idx = data[i].user_number;
								var temp_object = new Object();
								temp_object.game_count = data[i].game_count;
								temp_object.user_key = data[i].user_key;
								temp_object.overall = data[i].overall;
								temp_object.id = data[i].id;
								temp_object.rating = data[i].rating;
								temp[idx] = temp_object;
							}
							
							//각각 저장한 방 정보를 뿌려준다. (방생성)
							$(".user_list").each(function(index){
								var rtvHtml = "";
								$(this).children("div").empty();
								if(temp[index] != null){
									rtvHtml += "<table style='height: 130px; width:730px; margin: 10px;'>";
									rtvHtml += "<tbody>";
									rtvHtml += "<tr>";
									rtvHtml += "<td rowspan='2' style='width:130px; height: 130px; padding: 0px;'>";
									//리그에 알맞는 사진 리그의 정보를 변수로 사용 
									rtvHtml += "<img src='${pageContext.request.contextPath}/img/league_" + temp[index].rating + ".jpg'  style='width: 130px; height: 130px;'>";
									rtvHtml += "</td>";
									rtvHtml += "<td style='border-right: 1px white solid; border-bottom: 1px white solid; text-align:center; vertical-align:middle; width: 300px; height: 65px; padding: 0px;'>I D</td>";
									rtvHtml += "<td style='border-right: 1px white solid; border-bottom: 1px white solid; text-align:center; vertical-align:middle; width: 300px; height: 65px; padding: 0px;'>" + temp[index].id + "</td>";
									rtvHtml += "<td style='border-right: 1px white solid; border-bottom: 1px white solid; text-align:center; vertical-align:middle; width: 300px; height: 65px; padding: 0px;'>승 - 무 - 패</td>";
									rtvHtml += "<td style='border-bottom: 1px white solid; text-align:center; vertical-align:middle; width: 300px; height: 65px; padding: 0px;'>0-0-0</td>";
									rtvHtml += "</tr>";
									rtvHtml += "<tr>";
									rtvHtml += "<td style='border-right: 1px white solid; text-align:center; vertical-align:middle; width: 300px; height: 65px; padding: 0px;'>OVERALL</td>";
									rtvHtml += "<td style='border-right: 1px white solid; text-align:center; vertical-align:middle; width: 300px; height: 65px; padding: 0px;'>" + temp[index].overall + "</td>";
									rtvHtml += "<td style='border-right: 1px white solid; text-align:center; vertical-align:middle; width: 300px; height: 65px; padding: 0px;'>주요 전술</td>";
									rtvHtml += "<td style='text-align:center; vertical-align:middle; width: 300px; height: 65px; padding: 0px;'>4-4-2</td>";
									rtvHtml += "</tr>";
									rtvHtml += "</tbody>";
									rtvHtml += "</table>";
									$(this).children("div").append(rtvHtml);	
								}
							});
							
							//$("#bottom_html").empty();
							//$("#bottom_html").append(rtvHtml);
							
							//방 인원이 다  차면 게임쓰레드 시작
							if(data.length == room_capcity){
							    //count 증가
					        	checkRoomTime();
					        	clearInterval(waitInterval);
							}
						}
						, error : function(e) {
							console.log(e.result);
						}
					});
				}, 1000);
			}
			else
		    	checkRoomTime();
		}
		, error : function(e) {
			console.log(e.result);
		}
	});
	
}

function checkRoomTime(){
	$.ajax({
        type : "POST"
        , url : "/fg/game/gameThread.do"
        , data : {}
        , success : function(data) {}
        , error : function(e) {
        	console.log(e.result);
        }
 	});
	$("#exitBt").hide();
	setInterval(function(){
		$.ajax({
	        type : "POST"
	        , url : "/fg/room/getWaitingRoomTime.do"
	        , data : {}
	        , success : function(data) {
				if(data == -1){
					var form = $("#send_form");
					form.attr('method', 'post');
					form.attr('action', "/fg/game/playGame.do");
					form.submit();
				}
				else
					$("#room_time").text("게임 시작까지 남은 시간 " + (60 - data));
	        }
	        , error : function(e) {
	        	console.log(e.result);
	        }
	 	});
	}, 1000);
}
</script>
<form id="send_form"></form>
<input type='hidden' id='max' value='${max_user }'>
<div class='main_page_html'>
<input type='button' value='L O G O U T' id='logoutBt'>
<input type='button' value='E X I T' id='exitBt'>
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
		<td colspan='5' class='content_window' style='height: 650px;'>
			<table class='content_table' style='height: 650px;'>
				<tr class='content_tr'>
				<!-- content start -->
					<td class='content_full'>
						<table style='height: 650px; width: inherit; margin: 20px;'>
							<c:forEach begin='0' end='${max_user / 2 - 1 }' var='i'>
							<tr style='height: 150px; width:inherit;'>
								<!--방 생성 관련 뷰 -->
								<td class='user_list' style='height: 150px; width:750px;'>
									<div style='border-radius:10px; background-color: lightgray; border: 1px solid; height: 150px; width:750px;'></div>
								</td>
								<td style='width:100px; text-align:center; vertical-align:middle'>VS</td>
								<!--방 생성 관련 뷰 클래스로 계속 생성하기 때문에 자동으로 들어감  -->
								<td class='user_list' style='width:750px;'>
									<div style='border-radius:10px; background-color: lightgray; border: 1px solid; height: 150px; width:750px;'></div>
								</td>
							</tr>
							<!--방 생성 후 하단 마진 -->
							<tr style='height: 50px; width:inherit;'><td></td></tr>
							</c:forEach>
							<tr style='height: 50px; width:inherit;'>
								<td colspan='3' style='font-size:200%; text-align:center;' id='room_time'></td>
							</tr>
						</table>
					</td>
				<!-- content end -->
				</tr>
			</table>
		</td>
	</tr>
</table>
</div>
</body>
</html>