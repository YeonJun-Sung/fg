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
	<title>Web Game FG - Transfer</title>
</head>
<body>
<script type="text/javascript">
$(document).ready(function(){
	// 유저 기본정보 가져오기
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

	// Show Left sub menu Event
	$("body").on("mouseover",".left_sub_menu",function(){
		$(this).show();
	});

	// Hide Left sub menu Event
	$("body").on("mouseout",".left_sub_menu",function(){
		$(this).hide();
	});

	// Left sub menu Mouseover Event
	$("body").on("mouseover",".left_sub_menu p",function(){
		$(this).css("color","red");
	});

	// Left sub menu Mouseout Event
	$("body").on("mouseout",".left_sub_menu p",function(){
		$(this).css("color","black");
	});
	
	// Left menu Mouseover Event
	$(".left_menu").on("mouseover",function(){
		var _this = $(this);
		_this.children(".left_main_menu").css("background-color","silver");
		_this.children(".left_main_menu").css("border-bottom","1px solid");
		_this.children(".left_sub_menu").show();
	});
	
	// Left menu Mouseout Event
	$(".left_menu").on("mouseout",function(){
		var _this = $(this);
		_this.children(".left_main_menu").css("background-color","white");
		_this.children(".left_main_menu").css("border-bottom","0px");
		_this.children(".left_sub_menu").hide();
	});
	
	// Main Page 이동 Event
	$(".main_banner").on("click", function(){
		var form = $("#send_form");
		form.attr('method', 'post');
		form.attr('action', "/fg/start/mainPage.do");
		form.submit();
	})
	
	// Game Page 이동 Event
	$("#start_game").on("click",function(){
		var form = $("#send_form");
		form.attr('method', 'post');
		form.attr('action', "/fg/game/playGame.do");
		form.submit();
	});

	// Team Setting Page 이동 Event
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
		form.attr('action', "/fg/test/socketTest.do");
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
	// 모든 페이지 공통 Event

	// Click Player Event
	// Click시 배경 변화
	// Send Data > player_key
	$("body").on("click","#player_list tr",function(){
		var _this = $(this);
		var player_key = _this.data("key");
		$("#player_list tr").css("background-color","white");
		_this.css("background-color","lightgray");
		$.ajax({
			type : "POST"
			, url : "/fg/transfer/getPlayerInfo.do"
			, data : {
				send_key : player_key
			}
			, success : function(data) {
				console.log(data);
				var rtvHtml = "";
				
	    		rtvHtml += "<table style='width:100%; height:100%'>";
	    		rtvHtml += "<colgroup>";
	    		rtvHtml += "<col width='25%'/>";
	    		rtvHtml += "<col width='25%'/>";
	    		rtvHtml += "<col width='25%'/>";
	    		rtvHtml += "<col width='25%'/>";
	    		rtvHtml += "</colgroup>";
	    		rtvHtml += "<tbody>";
	    		rtvHtml += "<tr>";
				rtvHtml += "<td><b>이름</b></td><td colspan='3'>" + data.name + "</td>";
				rtvHtml += "</tr>";
				rtvHtml += "<tr>";
	    		rtvHtml += "<td><b>나이</b></td><td>" + data.age + "</td>";
	    		rtvHtml += "<td><b>주발</b></td><td>" + data.foot + "</td>";
				rtvHtml += "</tr>";
	    		rtvHtml += "<tr>";
				rtvHtml += "<td><b>몸무게</b></td><td>" + data.weight + "</td>";
	    		rtvHtml += "<td><b>키</b></td><td>" + data.height + "</td>";
				rtvHtml += "</tr>";
				rtvHtml += "<tr>";
				rtvHtml += "<td><b>포지션</b></td><td>" + data.player_position + "</td>";
				var detail_position = data.position_detail;
				rtvHtml += "<td><b>추천 포지션</b></td><td>";
				if(detail_position == "0_MF")
					rtvHtml += "" + data.foot + "M";
				else if(detail_position == "1_MF")
					rtvHtml += "CAM";
				else if(detail_position == "2_MF")
					rtvHtml += "CM";
				else if(detail_position == "3_MF")
					rtvHtml += "CDM";
				else if(detail_position == "0_FW")
					rtvHtml += "" + data.foot + "W";
				else if(detail_position == "1_FW")
					rtvHtml += "ST / CF";
				else if(detail_position == "0_DF")
					rtvHtml += "" + data.foot + "B / " + data.foot + "WB";
				else if(detail_position == "1_DF")
					rtvHtml += "CB / SW";
				else if(detail_position == "0_GK")
					rtvHtml += "GK";
				rtvHtml += "" + "</td>";
				rtvHtml += "</tr>";
				rtvHtml += "<tr>";
				rtvHtml += "<td><b>가속도</b></td><td>" + data.acc + "</td>";
				rtvHtml += "<td><b>속도</b></td><td>" + data.speed + "</td>";
				rtvHtml += "</tr>";
				rtvHtml += "<tr>";
				rtvHtml += "<td><b>점프</b></td><td>" + data.jump + "</td>";
				rtvHtml += "<td><b>헤딩</b></td><td>" + data.heading + "</td>";
				rtvHtml += "</tr>";
				rtvHtml += "<tr>";
				rtvHtml += "<td><b>컨트롤</b></td><td>" + data.controll + "</td>";
				rtvHtml += "<td><b>개인기</b></td><td>" + data.skill + "</td>";
				rtvHtml += "</tr>";
				rtvHtml += "<tr>";
				rtvHtml += "<td><b>킥</b></td><td>" + data.kick + "</td>";
				rtvHtml += "<td><b>시야</b></td><td>" + data.eyesight + "</td>";
				rtvHtml += "</tr>";
				rtvHtml += "<tr>";
				rtvHtml += "<td><b>슛 정확도</b></td><td>" + data.shoot_accuracy + "</td>";
				rtvHtml += "<td><b>크로스</b></td><td>" + data.cross_stat + "</td>";
				rtvHtml += "</tr>";
				rtvHtml += "<tr>";
				rtvHtml += "<td><b>짧은 패스</b></td><td>" + data.short_pass + "</td>";
				rtvHtml += "<td><b>롱패스</b></td><td>" + data.long_pass + "</td>";
				rtvHtml += "</tr>";
				rtvHtml += "<tr>";
				rtvHtml += "<td><b>슬라이딩 태클</b></td><td>" + data.sliding_tackle + "</td>";
				rtvHtml += "<td><b>스탠딩 태클</b></td><td>" + data.standing_tackle + "</td>";
				rtvHtml += "</tr>";
				rtvHtml += "<tr>";
				rtvHtml += "<td><b>체력</b></td><td>" + data.health + "</td>";
				rtvHtml += "<td><b>몸싸움</b></td><td>" + data.struggle + "</td>";
				rtvHtml += "</tr>";
				rtvHtml += "<tr>";
				rtvHtml += "<td><b>유연성</b></td><td>" + data.flexibility + "</td>";
				rtvHtml += "<td><b>침착성</b></td><td>" + data.restlessness + "</td>";
				rtvHtml += "</tr>";
				rtvHtml += "<tr>";
				rtvHtml += "<td><b>반사신경</b></td><td>" + data.reflex + "</td>";
				rtvHtml += "<td><b>액션</b></td><td>" + data.action_stat + "</td>";
				rtvHtml += "</tr>";
				rtvHtml += "<tr>";
				rtvHtml += "<td><b>다이빙</b></td><td>" + data.diving + "</td>";
				rtvHtml += "<td><b>핸들링</b></td><td>" + data.handling + "</td>";
				rtvHtml += "</tr>";
				rtvHtml += "<tr>";
				rtvHtml += "<td><b>위치선정</b></td><td>" + data.positioning + "</td>";
				rtvHtml += "<td id='transfer_bt' data-key='"+data.player_key+"' colspan='2'>이적</td>";
				rtvHtml += "</tr>";
	    		rtvHtml += "</tbody>";
	    		rtvHtml += "</table>";

				$("#player_detail").empty();
				$("#player_detail").append(rtvHtml);
				$("#player_detail td").css("border", "1px solid");
				$("#player_detail td").css("text-align", "center");
				$("#player_detail td:first-child").css("background-color", "aqua");
				$("#player_detail td:nth-child(3)").css("background-color", "aqua");
           }
           , error : function(e) {
              console.log(e.result);
           }
		});
	});
	
	// Click 이적 Event
	// Send Data > player_key
	
	//이적시키는 뷰
	$("body").on("click", "#transfer_bt", function(){
		var _this = $(this);
		var player_key = _this.data("key");
		$.ajax({
			type : "POST"
			, url : "/fg/transfer/insertTransfer.do"
			, data : {
				player_key : player_key
			}
			, success : function(data) {
				
			}
			, error : function(e) {
               console.log(e.result);
            }
 		});
	})
});
</script>
<form id="send_form"></form>
<div class='main_page_html'>
<input type='button' value='L O G O U T' id='logoutBt'>
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
		<td rowspan='5' colspan='4' class='content_window' style='height: 750px;'>
			<table class='content_table' style='height: 750px;'>
				<tr class='content_tr'>
					<td class="content_700">
						<table style='width: 100%; height: 100%'>
							<colgroup>
								<col width='40%' />
								<col width='20%' />
								<col width='20%' />
								<col width='20%' />
							</colgroup>
							<thead>
								<tr>
									<th>Name</th>
									<th>Position</th>
									<th>Age</th>
									<th>Overall</th>
								</tr>
							</thead>
							<tbody id="player_list">
							<c:forEach var='player' items='${player_list}' varStatus='i'>
							<tr data-key='${player.player_key }' data-valid='0'>
								<td class='player_name'>${player.name }</td>
								<td class='player_position'>${player.player_position }</td>
								<td class='player_age'>${player.age }</td>
								<td class='player_overall'>${player.overall }</td>
							</tr>
							</c:forEach>
							</tbody>
						</table>
					</td>
					<td class="content_700">
						<table style='width: 100%; height: 100%'>
							<colgroup>
								<col width='25%' />
								<col width='25%' />
								<col width='25%' />
								<col width='25%' />
							</colgroup>
							<tbody id="player_detail">
							</tbody>
						</table>
					</td>
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
		<td class='left_blank'>5</td>
	</tr>
</table>
</div>
</body>
</html>