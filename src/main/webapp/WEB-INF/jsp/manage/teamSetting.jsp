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
	<title>Web Game FG - Team Setting</title>
</head>
<body>
<script type="text/javascript">
var keep_detail = "";		// detail info 유지용 변수
$(document).ready(function(){
	// Load Team Class & Money Event
	
	//메인페이지의 기본정보를 그대로 가져온다.
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
	
	// 포지션 셀렉트 ui 생성하기.
	$(".position_setting td").each(function(){
		var add = $(this).data("position");
		if(add != null && add != "")
			$(".position_select").append("<option value='" + add + "'>" + add + "</option>");
	});
	
	// select 되어있는 포지션 배치
	$(".position_select").each(function(){
		var selected = $(this).data("select");
		
		if(selected != null && selected != ""){
			var select_name = $(this).data("name");
			var select_key = $(this).data("key");
			$(this).val(selected).attr("selected", "selected");
			
			refreshMap();
		}
	});
	
	// Change Select Event
	// player  11명	check
	// sub		7명	check
	// 포지션	      중복	check
	// Check 후 position 배치
	
	//포지션을 다른것으로 바꾸기.
	$(".position_select").on("change", function(){
		this_select = $(this).val();
		select_name = $(this).data("name");
		select_key = $(this).data("key");
		
		var result = checkPosition(this_select);
		if(result == "already"){
			alert("Already Selected Position");
			$(this).val("none");
		}
		else if(result == "7"){
			alert("Sub player is Full");
			$(this).val("none");
		}
		else if(result == "11"){
			alert("Player is Full");
			$(this).val("none");
		}
		refreshMap();
	});
	
	// Player Mouseout Event
	// Click 된 player가 있을 경우 유지시킴
	$("#player_list tr").on("mouseout", function(){
		$("#detail_info").empty();
		$("#detail_info").append(keep_detail);
	});

	// Player Mouseout Event
	// Player detail info Event
	// Send Data > player key
	
	//player_list 에 대한 Event
	$("#player_list tr").on("mouseover", function(){
		$.ajax({
	        type : "POST"
	        , url : "/fg/manage/getPlayerInfoDetail.do"
	        , data : {
	        	player_key : $(this).data("key")
	        }
	        , success : function(data) {
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
				rtvHtml += "</tr>";
	    		rtvHtml += "</tbody>";
	    		rtvHtml += "</table>";

	    		$("#detail_info").empty();
	    		$("#detail_info").append(rtvHtml);
	    		$("#detail_info table td").css("border", "1px solid");
	    		$("#detail_info table td").css("text-align", "center");
	    		$("#detail_info table td:first-child").css("background-color", "aqua");
	    		$("#detail_info table td:nth-child(3)").css("background-color", "aqua");
	        }
	        , error : function(e) {
	        	console.log(e.result);
	        }
	    });
	});
	
	// Click Player Event
	// Click한 player는 mouseout event 발생해도 유지
	// Click 상태이면 유지 제거
	$("#player_list tr").on("click", function(){
		var valid = $(this).data("valid");
		$("#player_list tr").css("background-color", "");
		if(valid == "0"){
			$(this).css("background-color", "lightgray");
			keep_detail = $("#detail_info").html();
			$(this).data("valid", "1");
		}
		else{
			keep_detail = "";
			$(this).data("valid", "0");
		}
	});
	
	// Save Team setting Event
	// position의 유효성 검사
	// 저장 전 position map의 재 배치 및 필요 data 저장
	// list를 "/"의 구분자를 이용하여 String으로 전송
	// Send Data > player key list, select position list
	$("#save_team_setting").on("click", function(){
		checkPosition("");
		refreshMap();
		if($.inArray("", position_list) != -1){
			alert("Please select the position of 11 players.");
		}
		else if($.inArray("", sub_list) != -1){
			alert("Please select the position of 7 sub players.");
		}
		else{
			var select_position = position_list.join("/") + "/" + sub_list.join("/");
			var player_keys = position_list_key.join("/") + "/" + sub_list_key.join("/");

			console.log(select_position);
			console.log(player_keys);
			$.ajax({
		        type : "POST"
		        , url : "/fg/manage/saveSelectPosition.do"
		        , data : {
		        	player_key : player_keys,
		        	select_position : select_position
		        }
		        , success : function(data) {
		        	alert("Team Setup Complete!!");
		        }
		        , error : function(e) {
		        	console.log(e.result);
		        }
		    });
		}
	});
});

var position_list = ["","","","","","","","","","",""];			// Field player position list 
var position_list_name = ["","","","","","","","","","",""];	// Field player name	 list
var position_list_key = ["","","","","","","","","","",""];		// Field player key		 list
// 위 3개의 list는 index가 같으면 같은 선수

var sub_list = ["","","","","","",""];		// Sub player position list
var sub_list_name = ["","","","","","",""];	// Sub player name	 list
var sub_list_key = ["","","","","","",""];	// Sub player key		 list
// 위 3개의 list는 index가 같으면 같은 선수


// Position check Function
// 이미 해당 포지션에 선수가 있는 경우			"already"	리턴
// select된 Sub 선수가 7명 이상인경우		"7"			리턴
// select된 Field 선수가 11명 이상인경우	"11"		리턴
function checkPosition(change_position){
	// 이미 해당 포지션에 선수가 있는 경우 체크
	if($.inArray(change_position, sub_list) != -1 || $.inArray(change_position, position_list) != -1)
		return "already";

	var idx = 0;
	var sub_idx = 0;
	
	// select된 선수 count
	$(".position_select").each(function(){
		var this_select = $(this).val();
		var this_key = $(this).data("key");
		var this_name = $(this).data("name");
		if(this_select.indexOf("SUB") != -1) {
			sub_idx++;
		}
		else if(this_select != "none"){
			idx++;
		}
	});
	if(idx > 11) return "11";
	if(sub_idx > 7) return "7";
}

// Position Map 배치
// select에 저장된 값을 배열에 각각 저장
// 배열에 저장된 값의 위치에 선수 배치
function refreshMap() {
	// 배열 초기화
	position_list = ["","","","","","","","","","",""];
	position_list_name = ["","","","","","","","","","",""];
	position_list_key = ["","","","","","","","","","",""];
	sub_list = ["","","","","","",""];
	sub_list_name = ["","","","","","",""];
	sub_list_key = ["","","","","","",""];

	var idx = 0;
	var sub_idx = 0;
	
	// Select된 선수들 목록 배열에 저장
	$(".position_select").each(function(){
		var this_select = $(this).val();
		var this_key = $(this).data("key");
		var this_name = $(this).data("name");
		if(this_select.indexOf("SUB") != -1) {
			sub_list[sub_idx] = this_select;
			sub_list_name[sub_idx] = this_name;
			sub_list_key[sub_idx] = this_key;
			sub_idx++;
		}
		else if(this_select != "none"){
			position_list[idx] = this_select;
			position_list_name[idx] = this_name;
			position_list_key[idx] = this_key;
			idx++;
		}
	});
	
	// 배열에 저장된 포지션에 선수 배치
	$(".position_setting td").each(function(){
		var this_position = $(this).data("position");
		var player_idx = $.inArray(this_position, position_list);
		var sub_player_idx = $.inArray(this_position, sub_list);
		if(player_idx != -1){
			$(this).children("b").text(position_list_name[player_idx]);
			$(this).children("b").attr("title", position_list_name[player_idx]);
		}
		else if(sub_player_idx != -1){
			$(this).children("b").text(sub_list_name[sub_player_idx]);
			$(this).children("b").attr("title", sub_list_name[sub_player_idx]);
		}
		else if(this_position != null){
			$(this).children("b").text("");
			$(this).children("b").removeAttr("title");
		}
	});
	
	return true;
}

</script>
<form id='send_form'>
	<input name='position' id='position' type='hidden' value=''>
	<input name='define_match_time' id='define_match_time' type='hidden' value='3'>
	<input name='define_tactical_time' id='define_tactical_time' type='hidden' value='1'>
	<input name='define_break_time' id='define_break_time' type='hidden' value='10'> 
</form>
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
				<!-- content start -->
					<td class='content_400'>
						<table class='position_setting'>
							<colgroup>
								<col width='20%'/>
								<col width='20%'/>
								<col width='20%'/>
								<col width='20%'/>
								<col width='20%'/>
							</colgroup>
							<!-- 포지션 위치 지정 -->
							<tbody class='position_map'>
							<tr>
								<td colspan='5' style='width: 400px;border:1px solid;background-color:lightblue;'>Field Player(11)</td>
							</tr>
							<tr>
								<td rowspan='2' style='height: 100px;' data-position='LW'><b></b></td>
								<td data-position='LS'><b></b></td>
								<td data-position='ST'><b></b></td>
								<td data-position='RS'><b></b></td>
								<td rowspan='2' style='height: 100px;' data-position='RW'><b></b></td>
							</tr>
							<tr>
								<td data-position='LCF'><b></b></td>
								<td data-position='CF'><b></b></td>
								<td data-position='RCF'><b></b></td>
							</tr>
							<tr>
								<td rowspan='3' style='height: 150px;' data-position='LM'><b></b></td>
								<td data-position='LCAM'><b></b></td>
								<td data-position='CAM'><b></b></td>
								<td data-position='RCAM'><b></b></td>
								<td rowspan='3' style='height: 150px;' data-position='RM'><b></b></td>
							</tr>
							<tr>
								<td data-position='LCM'><b></b></td>
								<td data-position='CM'><b></b></td>
								<td data-position='RCM'><b></b></td>
							</tr>
							<tr>
								<td data-position='LCDM'><b></b></td>
								<td data-position='CDM'><b></b></td>
								<td data-position='RCDM'><b></b></td>
							</tr>
							<tr>
								<td data-position='LWB'><b></b></td>
								<td rowspan='2' style='height: 100px;' data-position='LCB'><b></b></td>
								<td data-position='CB'><b></b></td>
								<td rowspan='2' style='height: 100px;' data-position='RCB'><b></b></td>
								<td data-position='RWB'><b></b></td>
							</tr>
							<tr>
								<td data-position='LB'><b></b></td>
								<td data-position='SW'><b></b></td>
								<td data-position='RB'><b></b></td>
							</tr>
							<tr>
								<td colspan='5' style='width: 400px;' data-position='GK'><b></b></td>
							</tr>
							<tr>
								<td style='width: 400px;border:1px solid;background-color:lightblue;' colspan='5'>SUB(7)</td>
							</tr>
							<tr>
								<td data-position='SUB1'><b></b></td>
								<td data-position='SUB2'><b></b></td>
								<td data-position='SUB3'><b></b></td>
								<td data-position='SUB4'><b></b></td>
								<td data-position='SUB5'><b></b></td>
							</tr>
							<tr>
								<td data-position='SUB6'><b></b></td>
								<td data-position='SUB7'><b></b></td>
							</tr>
							<tr>
								<td colspan='5' style='width: 400px;border:1px solid;background-color:silver;' id='save_team_setting'>
									SAVE TEAM SETTING
								</td>
							</tr>
							</tbody>
						</table>
					</td>
					
					<td class='content_500'>
						<table class='player_list_table'>
							<colgroup>
								<col width='15%'/>
								<col width='40%'/>
								<col width='15%'/>
								<col width='15%'/>
								<col width='15%'/>
							</colgroup>
							<thead>
							
								<tr>
									<th></th>
									<th>Name</th>
									<th>Position</th>
									<th>Age</th>
									<th>Overall</th>
								</tr>
							</thead>
							
							<tbody id='player_list'>
							<c:forEach var='player' items='${player_list}' varStatus='i'>
							<tr data-key='${player.player_key }' data-valid='0'>
								<td class='player_position'>
									<select class='position_select' data-select='${player.select_position }' data-name='${player.name }' data-key='${player.player_key }'>
									  <option value='none' selected></option>
									</select>
								</td>
								<td class='player_name'>${player.name }</td>
								<td class='player_position'>${player.player_position }</td>
								<td class='player_age'>${player.age }</td>
								<td class='player_overall'>${player.overall }</td>
							</tr>
							</c:forEach>
							</tbody>
						</table>
					</td>
					
					<td class='content_500' id='detail_info'>
					</td>
				<!-- content end -->
				</tr>
				<tr>
					<td class='notice_window' colspan='3'>notice</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
	<!-- 구단관리부터 줄이 바뀌므로 유의 해서 작성 -->
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
				<p> - test1</p>
				<p> - test2</p>
				<p> - test3</p>
				<p> - test4</p>
			</div>
		</td>
	</tr>
	<tr>
		<td class='left_menu'>
			<div class="left_main_menu" id='league_record'>4.리그 기록</div>
			<div class='left_sub_menu'>
				<p> - test1</p>
				<p> - test2</p>
				<p> - test3</p>
				<p> - test4</p>
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