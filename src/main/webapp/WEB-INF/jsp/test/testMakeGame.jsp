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
<title>Web Game FG - Make Game</title>
</head>
<body>
<script type="text/javascript">
var define_match_time = 3;
var define_tactical_time = 1;
var define_break_time = 10;

$(document).ready(function(){
	$("#first_menu").on("click", function(){
		var form = $("#send_form");
		form.attr('action', "/fg/test/testMakeGame.do");
		form.submit();
	});
	$("#second_menu").on("click", function(){
		var form = $("#send_form");
		$("#define_match_time").val(define_match_time);
		$("#define_tactical_time").val(define_tactical_time);
		$("#define_break_time").val(define_break_time);
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
		$("#define_match_time").val(define_match_time);
		$("#define_tactical_time").val(define_tactical_time);
		$("#define_break_time").val(define_break_time);
		form.attr('action', "/fg/test/testPage.do");
		form.submit();
	});
})
</script>
	<form id="send_form">
		<input name="send_key" id="send_key" type="hidden" value="${key }">
		<input name="define_match_time" id="define_match_time" type="hidden" value="">
		<input name="define_tactical_time" id="define_tactical_time" type="hidden" value="">
		<input name="define_break_time" id="define_break_time" type="hidden" value=""> 
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
						<td style='border-right: 1px solid; width:50%; vertical-align: top;'>
							
						</td>
						<td style='border-left: 1px solid; vertical-align:top;'>

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