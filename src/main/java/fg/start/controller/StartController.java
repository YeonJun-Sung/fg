package fg.start.controller;

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

import fg.start.service.StartService;
import fg.vo.UserVO;

@Controller
public class StartController {
    Logger log = Logger.getLogger(this.getClass());
	
	@Resource(name = "startService")
	private StartService startService;

	//Main Page 매서드 
	@RequestMapping(value = "/start/mainPage.do")
	public ModelAndView mainPage(HttpServletRequest req, HttpServletResponse res, HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView("start/mainPage");
		return mv;
	}
	//기본 사용자 정보 불러오기
	@RequestMapping(value = "/start/getTeamInfo.do")
	public @ResponseBody Map<String, Object> getTeamInfo(HttpServletRequest req, HttpSession session) throws Exception {
		UserVO user = (UserVO) session.getAttribute("userSession");
		String user_key = user.getUserKey();
		Map<String, Object> result = startService.getTeamInfo(user_key);

		return result;
	}

}
