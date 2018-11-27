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
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/sockjs-0.3.4.js" /></script>
	<title>Web Game FG - Game Setting</title>
</head>
<body>
<script type="text/javascript">
var change_position = "";
var timecheck = 0;
$(document).ready(function(){
	$(".position_map td").css("width", "60px");
	
	start();
	
	$("#messi").on("click",function(){
		$(".player_list_table").css("display","table");
	});
	
	var keep_detail = "";
	var selected_position = ["", "", "", "", "", "", "", "", "", "", ""];
	var sub_player = ["", "", "", "", "", "" ,""];
	
	$("#my_position td").each(function(){
		var add = $(this).data("position");
		
		if(add != null && add != "")
			$(".position_select").append("<option value='" + add + "'>" + add + "</option>");
	});

	
	$(".position_select").each(function(){
		var selected = $(this).data("select");
		
		if(selected != null && selected != ""){
			var select_name = $(this).data("name");
			var select_key = $(this).data("key");
			$(this).val(selected).attr("selected", "selected");
			refreshMap();	
		}
	});
	
	$(".position_select").on("click", function(){
		change_position = $(this).val();
	});
	
	$(".position_select").on("change", function(){
		this_select = $(this).val();
		select_name = $(this).data("name");
		select_key = $(this).data("key");
		console.log("change : "+ change_position + " > "+ this_select);
		
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
		
		//선수 포지션 변경시 DB 업데이트
		$.ajax({
	        type : "POST"
	        , url : "/fg/game/changePosition.do"
	        , data : {
	        	player_key : select_key,
	        	position : this_select
	        }
	        , success : function(data) {
	        }
	        , error : function(e) {
	        	console.log(e.result);
	        }
	    });
	});
	
	$("#starting_game_home").on("click", function(){
		checkPosition("");
		refreshMap();
		if($.inArray("", position_list) != -1){
			alert("Please select the position of 11 players.");
		}
		else if($.inArray("", sub_list) != -1){
			alert("Please select the position of 7 sub players.");
		}
		else{
			var txt = $(this).text();
			var _this = $(this);
			if(txt.indexOf("START") != -1){
				$.ajax({
			        type : "POST"
			        , url : "/fg/game/updateStatus.do"
			        , data : {
			        	status : "start"
			        	, game_key : $("#game_key").val()
			        }
			        , success : function(data) {
			        	console.log("start");
						_this.text("CANCEL");
						_this.removeAttr("data-start");
						_this.attr("data-start", "cancel");
			        }
			        , error : function(e) {
			        	console.log(e.result);
			        }
			    });
			}
			else if(txt.indexOf("CANCEL") != -1){
				$.ajax({
			        type : "POST"
			        , url : "/fg/game/updateStatus.do"
			        , data : {
			        	status : "cancel"
			        	, game_key : $("#game_key").val()
			        }
			        , success : function(data) {
						console.log("cancel");
						_this.removeAttr("data-start");
						_this.attr("data-start", "start");
						_this.text("START");
					}
			        , error : function(e) {
			        	console.log(e.result);
			        }
			    });
			}

		}
	});
	//openSocket();
	
	$(".position_setting td, #player_list tr").on("mouseout", function(){
		$("#detail_info").empty();
		$("#detail_info").append(keep_detail);
		timecheck = 0;
	});

	$(".position_setting td, #player_list tr").on("mouseover", function(){
		var player_key = $(this).data("key");
		if(player_key != null){
			timecheck = 1;
			$.ajax({
		        type : "POST"
		        , url : "/fg/manage/getPlayerInfoDetail.do"
		        , data : {
		        	player_key : player_key
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
		}
	});

	$("#my_position td").on("click", function(){
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
});

var inter;
function start() {
	var time = 0;
	inter = setInterval(function() {
		$.ajax({
	        type : "POST"
	        , url : "/fg/game/getGameSettingTime.do"
	        , success : function(data) {
	        	time = data;
	    		if(timecheck == 0)
	    			$("#detail_info").html("<p style='font-size:200%; text-align:center;'>경기 시작까지 남은 시간</p><p style='font-size:300%; text-align:center;'>" + (60 - time) +"</p>");

	    		if(time == 60 || time == -1){
	    			if($.inArray("", position_list) != -1 || $.inArray("", sub_list) != -1){
	    				alert("Please select the position of 11 players.");
	    				$.ajax({
	    			        type : "POST"
	    			        , url : "/fg/game/updateGamePosition.do"
	    			        , success : function(data) {
	    			        	alert("Team Update Complete!!");
	    						var form = $("#send_form");
	    						form.attr('method', 'post');
	    						form.attr('action', "/fg/game/playGame.do");
	    						form.submit();
	    			        }
	    			        , error : function(e) {
	    			        	console.log(e.result);
	    			        }
	    			    });
	    			}
	    			else 
	    				startGame();
	    		}
	        }
	        , error : function(e) {
	        	console.log(e.result);
	        }
	    });
		
		var order_id = "";
		$.ajax({
	        type : "POST"
	        , url : "/fg/game/checkAwayUser.do"
	        , success : function(data) {
	        	order_id = data.id;
	
	        	$("#starting_game_away").removeAttr("data-start");
	        	if(data.status == 0){
	            	$("#starting_game_away").attr("data-start", "start");
	            	$("#starting_game_away").text("START");	
	        	}
	        	else if(data.status == 1){
	            	$("#starting_game_away").attr("data-start", "cancel");
	            	$("#starting_game_away").text("CANCEL");
	        	}
	        	
	        	if(order_id != "" && order_id != null){
	        		$.ajax({
	        	        type : "POST"
	        	        , url : "/fg/game/getOrderUserPlayerInfo.do"
	        	        , data : { }
	        	        , success : function(data) {
	        	        	console.log(data)
	        	        	var order_position_list = [];
	        	        	var order_position_list_key = [];
	        	        	var order_position_list_name = [];
	        	        	var idx = 0;
	        	        	var order_sub_list = [];
	        	        	var order_sub_list_key = [];
	        	        	var order_sub_list_name = [];
	        	        	var sub_idx = 0;
	        		        	
	        	        	for(var i = 0;i < data.length;i++){
	        	        		var select = "" + data[i].select_position;
	        	        		var key = "" + data[i].player_key;
	        	        		var name = "" + data[i].name;
	        	       			if(select.indexOf("SUB") != -1) {
	        	       				order_sub_list[sub_idx] = select;
	        	       				order_sub_list_name[sub_idx] = name;
	        	       				order_sub_list_key[sub_idx] = key;
	        	       				sub_idx++;
	        	       			}
	        	       			else if(select != null){
	        	       				order_position_list[idx] = select;
	        	       				order_position_list_name[idx] = name;
	        	       				order_position_list_key[idx] = key;
	        	       				idx++;
	        	       			}
	        	        	}
	        		        	
	        	    		$("#order_position td").each(function(){
	        		        	var this_position = $(this).data("position");
	        		    		var player_idx = $.inArray(this_position, order_position_list);
	        		    		var sub_player_idx = $.inArray(this_position, order_sub_list);
	        		    		if(player_idx != -1){
	        		    			$(this).attr("data-key", order_position_list_key[player_idx]);
	        		    			$(this).children("b").text(order_position_list_name[player_idx]);
	        		    			$(this).children("b").attr("title", order_position_list_name[player_idx]);
	        		    		}
	        		    		else if(sub_player_idx != -1){
	        		    			$(this).attr("data-key", order_sub_list_key[sub_player_idx]);
	        		    			$(this).children("b").text(order_sub_list_name[sub_player_idx]);
	        		    			$(this).children("b").attr("title", order_sub_list_name[sub_player_idx]);
	        		    		}
	        		    		else if(this_position != null){
	        		    			$(this).children("b").text("");
	        		    			$(this).children("b").removeAttr("title");
	        		    		}
	        	    		})
	        	        }
	        	        , error : function(e) {
	        	        	console.log(e.result);
	        	        }
	        	    });
	        	}
	        }
	        , error : function(e) {
	        	console.log(e.result);
	        }
	    });

		if($("#starting_game_home").text().indexOf("CANCEL") != -1 && $("#starting_game_away").text().indexOf("CANCEL") != -1)
			startGame();
	}, 1000);
}

var position_list = ["","","","","","","","","","",""];
var sub_list = ["","","","","","",""];
var position_list_name = ["","","","","","","","","","",""];
var sub_list_name = ["","","","","","",""];
var position_list_key = ["","","","","","","","","","",""];
var sub_list_key = ["","","","","","",""];


function checkPosition(change_position){
	if($.inArray(change_position, sub_list) != -1 || $.inArray(change_position, position_list) != -1)
		return "already";

	var idx = 0;
	var sub_idx = 0;
	
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

function refreshMap() {
	position_list = ["","","","","","","","","","",""];
	sub_list = ["","","","","","",""];
	position_list_name = ["","","","","","","","","","",""];
	sub_list_name = ["","","","","","",""];
	position_list_key = ["","","","","","","","","","",""];
	sub_list_key = ["","","","","","",""];

	var idx = 0;
	var sub_idx = 0;
	
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
	
	$("#my_position td").each(function(){
		var this_position = $(this).data("position");
		
		var player_idx = $.inArray(this_position, position_list);
		var sub_player_idx = $.inArray(this_position, sub_list);
		if(player_idx != -1){
			$(this).attr("data-key", position_list_key[player_idx]);
			$(this).children("b").text(position_list_name[player_idx]);
			$(this).children("b").attr("title",position_list_name[player_idx]);
		}
		else if(sub_player_idx != -1){
			$(this).attr("data-key", sub_list_key[sub_player_idx]);
			$(this).children("b").text(sub_list_name[sub_player_idx]);
			$(this).children("b").attr("title",sub_list_name[sub_player_idx]);
			
		}
		else if(this_position != null){
			$(this).children("b").text("");
			$(this).children("b").removeAttr("title");
		}
		
	});	
	
	return true;
}

function startGame(){
	clearInterval(inter);
	var form = $("#send_form");
	form.attr('method', 'post');
	form.attr('action', "/fg/game/playGame.do");
	form.submit();
}

/*


var ws;

function openSocket(){
    if(ws != undefined && ws.readyState != WebSocket.CLOSED){
        writeResponse("WebSocket is already opened.");
        return;
    }
    
    //웹소켓 객체 만드는 코드
    ws = new WebSocket("ws://127.0.0.1:8080/fg/echo.do?" + $("#user_id").val());
    
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

function send(text){
    ws.send(text);
}

function closeSocket(){
    ws.close();
}
function writeResponse(text){
	var sp = text.split(",");
	if(sp[0] == "Enter")
		send("EnterRecive,"+ $("#user_id").val());
	if(sp[0] == "Change"){
		$("#order_position td").each(function(){
			var val = 0;
			var po = $(this).data("position");
			if(sp[1] == po) {
				$(this).text("");
				$(this).removeAttr("data-key");
			}
			else if(sp[2] == po) {
				$(this).text(sp[3]);
				$(this).attr("data-key", sp[4]);
				
			}
		})
	}
	else if(sp[0] == "Enter" || sp[0] == "EnterRecive") {
		var order_id = sp[1];
		$.ajax({
	        type : "POST"
	        , url : "/fg/game/getOrderUserPlayerInfo.do"
	        , data : {
	        	order_id : order_id
	        }
	        , success : function(data) {
	        	console.log(data)
   	        	var order_position_list = [];
   	        	var order_position_list_key = [];
   	        	var order_position_list_name = [];
   	        	var idx = 0;
   	        	var order_sub_list = [];
   	        	var order_sub_list_key = [];
   	        	var order_sub_list_name = [];
   	        	var sub_idx = 0;
   	        	
	        	for(var i = 0;i < data.length;i++){
	        		var select = "" + data[i].select_position;
	        		var key = "" + data[i].player_key;
	        		var name = "" + data[i].name;
	       			if(select.indexOf("SUB") != -1) {
	       				order_sub_list[sub_idx] = select;
	       				order_sub_list_name[sub_idx] = name;
	       				order_sub_list_key[sub_idx] = key;
	       				sub_idx++;
	       			}
	       			else if(select != null){
	       				order_position_list[idx] = select;
	       				order_position_list_name[idx] = name;
	       				order_position_list_key[idx] = key;
	       				idx++;
	       			}
	        	}
   	        	
        		$("#order_position td").each(function(){
    	        	var this_position = $(this).data("position");
    	    		var player_idx = $.inArray(this_position, order_position_list);
    	    		var sub_player_idx = $.inArray(this_position, order_sub_list);
    	    		if(player_idx != -1){
    	    			$(this).attr("data-key", order_position_list_key[player_idx]);
    	    			$(this).text(order_position_list_name[player_idx]);
    	    		}
    	    		else if(sub_player_idx != -1){
    	    			$(this).attr("data-key", order_sub_list_key[sub_player_idx]);
    	    			$(this).text(order_sub_list_name[sub_player_idx]);
    	    			
    	    		}
    	    		else if(this_position != null){
    	    			$(this).text("");
    	    		}
        		})
	        }
	        , error : function(e) {
	        	console.log(e.result);
	        }
	    });
	}
	else
		alert(sp[0]);
}
*/
</script>
<form id='send_form'>
</form>
<input type='hidden' id='game_key' value='${game_key }'>
<input type='hidden' id='this_position' value=''>
<div class='main_page_html'>
<table class='main_table'>
	<tr>
		<td rowspan='5' colspan='4' class='content_window' style='height: 750px; width: 1600px;'>
			<table class='content_table' style='height: 750px;'>
				<tr class='content_tr'>
				<!-- content start -->
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
					
					<td class='content_300'>
						<table class='position_setting' id='my_position'>
							<colgroup>
								<col width='20%'/>
								<col width='20%'/>
								<col width='20%'/>
								<col width='20%'/>
								<col width='20%'/>
							</colgroup>
							<tbody class='position_map'>
							<tr>
								<td colspan='5' style='width: 300px;border:1px solid;background-color:lightblue;'>Field Player(11)</td>
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
								<td colspan='5' style='width: 300px;' data-position='GK'><b></b></td>
							</tr>
							<tr>
								<td style='width: 300px;border:1px solid;background-color:lightblue;' colspan='5'>SUB(7)</td>
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
								<td colspan='5' style='width: 300px;border:1px solid;background-color:silver;' id='starting_game_home'>
									START
								</td>
							</tr>
							</tbody>
						</table>
					</td>
					
					<td class='content_500' id='detail_info'>
					</td>
					
					<td class='content_300'>
						<table class='position_setting' id='order_position'>
							<colgroup>
								<col width='20%'/>
								<col width='20%'/>
								<col width='20%'/>
								<col width='20%'/>
								<col width='20%'/>
							</colgroup>
							<tbody class='position_map'>
							<tr>
								<td colspan='5' style='width: 300px;border:1px solid;background-color:lightblue;'>Field Player(11)</td>
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
								<td colspan='5' style='width: 300px;' data-position='GK'><b></b></td>
							</tr>
							<tr>
								<td style='width: 300px;border:1px solid;background-color:lightblue;' colspan='5'>SUB(7)</td>
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
								<td colspan='5' style='width: 300px;border:1px solid;background-color:silver;' id='starting_game_away'>
									START
								</td>
							</tr>
							
							</tbody>
						</table>
					</td>
				<!-- content end -->
				</tr>
				<tr>
					<td class='notice_window' colspan='4'>notice</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</div>
</body>
</html>