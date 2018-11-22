package fg.create.controller;

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
import fg.vo.UserVO;

@Controller
public class CreateController {
    Logger log = Logger.getLogger(this.getClass());

	@Resource(name = "createService")
	private CreateService createService;
	
	//팀 생성 매서드
 	@RequestMapping(value = "/create/createTeam.do")
	public ModelAndView createTeam(HttpServletRequest req, HttpServletResponse res) throws Exception {
		ModelAndView mv = new ModelAndView("create/createTeam");
		return mv;
	}
 	//팀 생성 매서드
	@RequestMapping(value = "/create/createPlayer.do")
	public @ResponseBody List<Map<String, Object>> createPlayer(HttpServletRequest req, HttpServletResponse res, HttpSession session) throws Exception {
		UserVO user = (UserVO) session.getAttribute("userSession");
		String user_key = user.getUserKey();
		createService.removePlayerList(user_key);
		int position = 0;
		int rating = 0;
		//선수의 등급의 확률을 가져온다.
		Map<String, Object> per = createService.makePercent(rating);
		for (int i = 0; i < 23; i++) {
			if (i == 3 || i == 11 || i == 19)
				position++;
			//23엔트리 생성
			createService.makePlayer(position, rating, user_key, per);
		}
		
		List<Map<String, Object>> list = createService.getPlayerList(user_key);
		//스텟 overall 가져오기
		list = createService.getPlayerOverall(list);
		return list;
		
	}
	
	//선수 이름 변경 매서드
	@RequestMapping(value = "/create/editPlayerName.do")
	public @ResponseBody void editPlayerName(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String player_key = req.getParameter("player_key");
		String name = req.getParameter("name");
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("player_key", player_key);
		param.put("name", name);
		createService.editPlayerName(param);
	}

	//팀 기본 정보 저장 매서드
	@RequestMapping(value = "/create/saveTeam.do")
	public @ResponseBody void saveTeam(HttpServletRequest req, HttpServletResponse res, HttpSession session) throws Exception {
		UserVO user = (UserVO) session.getAttribute("userSession");
		String user_key = user.getUserKey();
		String team_name = req.getParameter("team_name");
		String tendency = req.getParameter("tendency");
		String rating = req.getParameter("rating");

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("user_key", user_key);
		param.put("team_name", team_name);
		param.put("tendency", Integer.parseInt(tendency));
		param.put("rating", Integer.parseInt(rating));

		createService.saveTeam(param);
		user = createService.getUserInfo(user_key);
		session.removeAttribute("userSession");
		session.setAttribute("userSession", user);
	}

	//선수 등 번호 변경 매서드
	@RequestMapping(value = "/create/editPlayerBackNum.do")
	public @ResponseBody void editPlayerBackNum(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String player_key = req.getParameter("player_key");
		String num = req.getParameter("num");
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("player_key", player_key);
		param.put("num", num);
		createService.editPlayerBackNum(param);
	}
}