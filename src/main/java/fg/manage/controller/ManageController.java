package fg.manage.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import fg.create.service.CreateService;
import fg.login.service.LoginService;
import fg.manage.service.ManageService;
import fg.vo.UserVO;

@Controller
public class ManageController {
    Logger log = Logger.getLogger(this.getClass());

	@Resource(name = "manageService")
	private ManageService manageService;
	@Resource(name = "loginService")
	private LoginService loginService;
	@Resource(name = "createService")
	private CreateService createService;
	
	//오버롤이 포함된 선수 정보 받아오기
	@RequestMapping(value = "/manage/teamSetting.do")
	public ModelAndView testTeamSetting(HttpServletRequest req, HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView("manage/teamSetting");
		UserVO user = (UserVO) session.getAttribute("userSession");
		String user_key = user.getUserKey();
		
		List<Map<String, Object>> list = createService.getPlayerList(user_key);
		list = manageService.getPlayerOverall(list);
		
		mv.addObject("player_list", list);
		return mv;
	}
	//전체 선수 정보 가져오기 클릭시 뜨는 아쿠아 화면과 관련
	@RequestMapping(value = "/manage/getPlayerInfoDetail.do")
	public @ResponseBody Map<String, Object> getPlayerInfoDetail(HttpServletRequest req) throws Exception {
		String player_key = req.getParameter("player_key");
		Map<String, Object> result = manageService.getPlayerInfoDetail(player_key);
		return result;
	}
	
	//저장한 포지션을 선수에 추가
	@SuppressWarnings("unlikely-arg-type")
	@RequestMapping(value = "/manage/saveSelectPosition.do")
	public @ResponseBody void saveSelectPosition(HttpServletRequest req, HttpSession session) throws Exception {
		String temp_key = req.getParameter("player_key");
		String temp_position = req.getParameter("select_position");
		UserVO user = (UserVO) session.getAttribute("userSession");
		String user_key = user.getUserKey();

		String[] player_key = temp_key.split("/");
		String[] position = temp_position.split("/");
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> param;
		for(int i = 0;i < player_key.length;i++) {
			if(!player_key.equals("") && player_key != null) {
				param = new HashMap<String, Object>();
				param.put("player_key", player_key[i]);
				param.put("position", position[i]);
				list.add(param);
			}
		}
		manageService.resetSelectPosition(user_key);
		manageService.saveSelectPosition(list);
	}
}