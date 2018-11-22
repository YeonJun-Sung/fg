package fg.transfer.controller;

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

import fg.login.service.LoginService;
import fg.transfer.service.TransferService;
import fg.vo.UserVO;

@Controller
public class TransferController {
    Logger log = Logger.getLogger(this.getClass());
    
	@Resource(name = "loginService")
	private LoginService loginService;
	
	@Resource(name = "transferService")
	private TransferService transferService;
	
	//이적선수 생성 매서드
	@RequestMapping(value = "/transfer/transferPage.do")
	public ModelAndView mainPage(HttpServletRequest req, HttpServletResponse res, HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView("transfer/transferPage");
		UserVO user = (UserVO) session.getAttribute("userSession");
		String user_key = user.getUserKey();
		int appear = 3;
		//일정시간 지난 선수 드랍
		transferService.removeTempPlayer();
		//3보다 큰 횟수로 등장시 드랍
		transferService.dropTempPlayer(appear);
		//이적시장을 종료할 때(이적시장 버튼을 누르면)선수의 권한 종료
		transferService.removeTempUserKey(user_key);
		//등급 확률표 가져오기
		Map<String, Object> percent = transferService.getRatingInfo(user_key);
		//이적시장 선수 생성
		List<Map<String, Object>> result = transferService.makePlayer(percent, user_key);
		mv.addObject("player_list", result);
		
		return mv;
	}

	@RequestMapping(value = "/transfer/getPlayerInfo.do")
	public @ResponseBody Map<String, Object> getPlayerInfo(HttpServletRequest req) throws Exception {
		String player_key = req.getParameter("send_key");
		Map<String, Object> result = transferService.getPlayerInfo(player_key);
		return result;
	}
	
	//이적시장에서 선택한 선수 영입
	@RequestMapping(value = "/transfer/insertTransfer.do")
	public @ResponseBody void insertTransfer(HttpServletRequest req, HttpSession session) throws Exception {
		UserVO user = (UserVO) session.getAttribute("userSession");
		String player_key = req.getParameter("player_key");
		String team_key = user.getTeamKey();
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("player_key", player_key);
		param.put("team_key", team_key);
		
		transferService.insertTransfer(param);
	}
}
