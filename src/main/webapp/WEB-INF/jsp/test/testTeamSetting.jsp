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
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.3.1.min.js" /></script>
	<title>Web Game FG - Test</title>
</head>
<body>
<script type="text/javascript">

$(document).ready(function(){
	var keep_detail = "";
	var change_position = "";
	var selected_position = ["", "", "", "", "", "", "", "", "", "", ""];
	var sub_player = ["", "", "", "", "", "" ,""];

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
	
	$(".position_setting td").each(function(){
		var add = $(this).data("position");
		if(add != null && add != "")
			$(".position_select").append("<option value='" + add + "'>" + add + "</option>");
	});
	
	$(".position_select").on("click", function(){
		change_position = $(this).val();
	});
	
	$(".position_select").on("change", function(){
		this_select = $(this).val();
		select_name = $(this).data("name");
		select_key = $(this).data("send_key");

		for(var i = 0;i < 7;i++){
			if(sub_player[i] == change_position)
				sub_player[i] = "";
		}
		for(var i = 0;i < 11;i++){
			if(selected_position[i] == change_position)
				selected_position[i] = "";
		}
		if(this_select.indexOf("SUB") != -1){
			if($.inArray("", sub_player) == -1){
				alert("More than 7!");
				$(this).val("");
			}
			else if($.inArray(this_select, sub_player) != -1 && this_select != ""){
				alert("Already Selected Position!!");
				$(this).val("");
			}
			else {
				for(var i = 0;i < 7;i++){
					if(sub_player[i] == ""){
						sub_player[i] = this_select;
						break;
					}
				}
				
				$(".position_setting td").each(function(){
					if($(this).data("position") == change_position) {
						$(this).text("");
						$(this).removeAttr("data-key");
					}
					if($(this).data("position") == this_select) {
						$(this).text(select_name);
						$(this).attr("data-key", select_key);
					}
				});
			}
		}
		else {
			if($.inArray("", selected_position) == -1){
				alert("More than 11!");
				$(this).val("");
			}
			else if($.inArray(this_select, selected_position) != -1 && this_select != ""){
				alert("Already Selected Position!!");
				$(this).val("");
			}
			else {
				for(var i = 0;i < 11;i++){
					if(selected_position[i] == ""){
						selected_position[i] = this_select;
						break;
					}
				}
				
				$(".position_setting td").each(function(){
					if($(this).data("position") == change_position) {
						$(this).text("");
						$(this).removeAttr("data-key");
					}
					if($(this).data("position") == this_select) {
						$(this).text(select_name);
						$(this).attr("data-key", select_key);
					}
				});
			}
		}
	});
	
	$("#player_list tr").on("mouseout", function(){
		$("#detail_info").empty();
		$("#detail_info").append(keep_detail);
	});
	
	$("#player_list tr").on("mouseover", function(){
		$("#detail_info").empty();
		$.ajax({
	        type : "POST"
	        , url : "/fg/test/getPlayerInfoDetail.do"
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
	
	$("#save_team_setting").on("click", function(){
		if($.inArray("", selected_position) != -1){
			alert("Please select the position of 11 players.");
			$(this).val("");
		}
		else if($.inArray("", sub_player) != -1){
			alert("Please select the position of 7 sub players.");
			$(this).val("");
		}
		else{
			var idx = 0;
			var idx_sub = 0;
			var player_keys = "";
			$(".position_setting td").each(function(){
				var add = $(this).data("position");
				if(add != null && add != "" && $.inArray(add, selected_position) != -1){
					player_keys += $(this).data("key");
					player_keys += "/";
					idx++;
				}
				else if(add != null && add != "" && $.inArray(add, sub_player) != -1){
					player_keys += $(this).data("key");
					player_keys += "/";
					idx++;
				}
			});
			console.log(player_keys);
			console.log(selected_position.join("/")+sub_player.join("/"));
			alert("do save!!");
		}
	});
});

</script>
<form id="send_form">
	<input name="send_key" id="send_key" type="hidden" value="${key }">
	<input name="position" id="position" type="hidden" value="">
	<input name="define_match_time" id="define_match_time" type="hidden" value="3">
	<input name="define_tactical_time" id="define_tactical_time" type="hidden" value="1">
	<input name="define_break_time" id="define_break_time" type="hidden" value="10"> 
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
					<td style='border-right: 1px solid; width: 30%; vertical-align: top;'>
						<table class="position_setting" style="border: 1px solid; height: 100%; width: 100%; background-color: greenyellow; text-align: center;">
							<colgroup>
								<col width="20%"/>
								<col width="20%"/>
								<col width="20%"/>
								<col width="20%"/>
								<col width="20%"/>
							</colgroup>
							<tbody id="position_map">
							<tr style="border: 1px solid">
								<td style="border: 1px solid" colspan="5">Field Player(11)</td>
							</tr>
							<tr style="border: 1px solid">
								<td rowspan="2" data-position="LW"></td>
								<td data-position="LS"></td>
								<td data-position="ST"></td>
								<td data-position="RS"></td>
								<td rowspan="2" data-position="RW"></td>
							</tr>
							<tr style="border: 1px solid">
								<td data-position="LCF"></td>
								<td data-position="CF"></td>
								<td data-position="RCF"></td>
							</tr>
							<tr style="border: 1px solid">
								<td rowspan="3" data-position="LM"></td>
								<td data-position="LCAM"></td>
								<td data-position="CAM"></td>
								<td data-position="RCAM"></td>
								<td rowspan="3" data-position="RM"></td>
							</tr>
							<tr style="border: 1px solid">
								<td data-position="LCM"></td>
								<td data-position="CM"></td>
								<td data-position="RCM"></td>
							</tr>
							<tr style="border: 1px solid">
								<td data-position="LCDM"></td>
								<td data-position="CDM"></td>
								<td data-position="RCDM"></td>
							</tr>
							<tr style="border: 1px solid">
								<td data-position="LWB"></td>
								<td rowspan="2" data-position="LCB"></td>
								<td data-position="CB"></td>
								<td rowspan="2" data-position="RCB"></td>
								<td data-position="RWB"></td>
							</tr>
							<tr style="border: 1px solid">
								<td data-position="LB"></td>
								<td data-position="SW"></td>
								<td data-position="RB"></td>
							</tr>
							<tr style="border: 1px solid">
								<td colspan="5" data-position="GK"></td>
							</tr>
							<tr style="border: 1px solid">
								<td style="border: 1px solid" colspan="5">SUB(7)</td>
							</tr>
							<tr style="border: 1px solid;">
								<td data-position="SUB1"></td>
								<td data-position="SUB2"></td>
								<td data-position="SUB3"></td>
								<td data-position="SUB4"></td>
								<td data-position="SUB5"></td>
							</tr>
							<tr style="border: 1px solid">
								<td data-position="SUB6"></td>
								<td data-position="SUB7"></td>
							</tr>
							<tr>
								<td style="border: 1px solid;background-color: lightblue;" colspan="5" id="save_team_setting">
									SAVE TEAM SETTING
								</td>
							</tr>
							</tbody>
						</table>
					</td>
					
					<td style='border-left: 1px solid; width: 30%; vertical-align: top;'>
						<table id="player_list_table" style="width: 100%;">
							<colgroup>
								<col width="15%"/>
								<col width="40%"/>
								<col width="15%"/>
								<col width="15%"/>
								<col width="15%"/>
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
							<tbody id="player_list">
							<c:forEach var="player" items="${player_list}" varStatus="i">
							<tr data-key="${player.player_key }" data-valid="0">
								<td class="player_position">
									<select class="position_select" data-name="${player.name }" data-key="${player.player_key }" style="width:100%;">
									  <option value="" selected></option>
									</select>
								</td>
								<td class="player_name">${player.name }</td>
								<td class="player_position">${player.player_position }</td>
								<td class="player_age">${player.age }</td>
								<td class="player_overall">${player.overall }</td>
							</tr>
							</c:forEach>
							</tbody>
						</table>
					</td>
					
					<td style='border-left: 1px solid; width: 30%; vertical-align: top;'>
						<div id="detail_info" style='height: 100%;'></div>
					</td>
				</tr>
				<tr style='height: 30%;'>
					<td colspan='3' style='border-top: 1px solid;'>notice</td>
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