package fg.room.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import fg.create.service.CreateService;
import fg.manage.service.ManageService;
import fg.room.service.RoomService;
import fg.vo.UserVO;
//리그의 방과 관련된 객체 
@Controller
public class RoomController {
    Logger log = Logger.getLogger(this.getClass());
	
	@Resource(name = "roomService")
	private RoomService roomService;

	@Resource(name = "manageService")
	private ManageService manageService;
	
	@Resource(name = "createService")
	private CreateService createService;
	
	//유저의 뷰에 보여질 화면에 데이터와 관련된 매서드
	@RequestMapping(value = "/room/getRoomId.do")
	public @ResponseBody List<Map<String, Object>> getRoomId(HttpServletRequest req, HttpSession session) throws Exception {
		UserVO user = (UserVO) session.getAttribute("userSession");
		String user_key = user.getUserKey();
		int rating = user.getRating();
		// 방 키 가져오기.
		String room_key = roomService.getRoomKey(user_key);
		
		// id,user_key,game_count(게임 순서를 정하는데 필요한 변수) 가져오기
		List<Map<String, Object>> result = roomService.getRoomId(room_key);
		int overall = 0;
		List<Map<String, Object>> temp;
		
		// round(현재 게임을 진행할 사람을 정하는 변수)
		List<Map<String,Object>> round = roomService.getRoundInfo(room_key);
		for(int i = 0;i < result.size();i++) {
			temp = new ArrayList<Map<String, Object>>();
			user_key = (String) result.get(i).get("user_key");
			// overall을 방 정보중 하나로 쓰기 위해 필요한 과정
			temp = createService.getPlayerList(user_key);
			overall = roomService.getPlayerOverall(temp);
			result.get(i).put("overall", overall);
			result.get(i).put("rating", rating);
			// test_column은 진행할 다음 상대방의 정보이다.
			result.get(i).put("user_number", ((Integer) (round.get(i).get("round_info"))).intValue());
		}
		
		return result;
	}
	
	@RequestMapping(value = "/room/getAllOverall.do")
	public @ResponseBody List<Map<String, Object>> getAllOverall(HttpServletRequest req, HttpSession session) throws Exception {
		UserVO user = (UserVO) session.getAttribute("userSession");
		String user_key = user.getUserKey();
		
		List<Map<String, Object>> list = createService.getPlayerList(user_key);
		list = manageService.getPlayerOverall(list);
		
		return list;
	}
	
	@RequestMapping(value = "/room/getWaitingRoomTime.do")
	public @ResponseBody int getWaitingRoomTime(HttpServletRequest req, HttpSession session) throws Exception {
		UserVO user = (UserVO) session.getAttribute("userSession");
		String user_key = user.getUserKey();
		String room_key = roomService.getRoomKey(user_key);
		
		return roomService.getWaitingRoomTime(room_key);
	}
	
	@RequestMapping(value = "/room/getUserCountInRoom.do")
	public @ResponseBody int getUserCountInRoom(HttpServletRequest req, HttpSession session) throws Exception {
		UserVO user = (UserVO) session.getAttribute("userSession");
		String user_key = user.getUserKey();
		String room_key = roomService.getRoomKey(user_key);
		
		return roomService.getUserCountInRoom(room_key);
	}

	@RequestMapping(value = "/room/exitRoom.do")
	public void exitRoom(HttpServletRequest req, HttpSession session) throws Exception {
		UserVO user = (UserVO) session.getAttribute("userSession");
		String user_key = user.getUserKey();
		roomService.exitRoom(user_key);
	}
}
