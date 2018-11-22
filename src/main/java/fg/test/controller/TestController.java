package fg.test.controller;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import fg.login.service.LoginService;
import fg.test.service.TestService;
import fg.vo.UserVO;

@Controller
public class TestController {
    Logger log = Logger.getLogger(this.getClass());

	@Resource(name = "testService")
	private TestService testService;
	@Resource(name = "loginService")
	private LoginService loginService;


	@RequestMapping(value = "/test/testPage.do")
	public ModelAndView testPage(HttpServletRequest req, HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView("test/testPage");
		UserVO user = (UserVO) session.getAttribute("userSession");
		String key = user.getUserKey();
		String define_match_time = req.getParameter("define_match_time");
		String define_tactical_time = req.getParameter("define_tactical_time");
		String define_break_time = req.getParameter("define_break_time");
		
		mv.addObject("key", key);
		mv.addObject("define_match_time", define_match_time);
		mv.addObject("define_tactical_time", define_tactical_time);
		mv.addObject("define_break_time", define_break_time);
		return mv;
	}

	@RequestMapping(value = "/test/moveTest.do")
	public ModelAndView moveTest(HttpServletRequest req, HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView("test/moveTest");
		UserVO user = (UserVO) session.getAttribute("userSession");
		return mv;
	}

	@RequestMapping(value = "/test/socketTest.do")
	public ModelAndView socketTest(HttpServletRequest req, HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView("test/socketTest");
		UserVO user = (UserVO) session.getAttribute("userSession");
		String id = user.getId();
		mv.addObject("userId", id);
		return mv;
	}

	@RequestMapping(value = "/test/testMakeGame.do")
	public ModelAndView testMakeGame(HttpServletRequest req) throws Exception {
		ModelAndView mv = new ModelAndView("test/testMakeGame");
		String key = req.getParameter("send_key");
		mv.addObject("key", key);
		return mv;
	}
	
	@RequestMapping(value = "/test/getPlayerInfoDetail.do")
	public @ResponseBody Map<String, Object> getPlayerInfoDetail(HttpServletRequest req) throws Exception {
		String key = req.getParameter("player_key");

		Map<String, Object> result = testService.getPlayerInfoDetail(key);

		return result;
	}
	
	@RequestMapping(value = "/test/testThread.do")
	public void testThread(HttpServletRequest req, HttpSession sess) throws Exception {
		UserVO user = (UserVO) sess.getAttribute("userSession");
		String id = user.getUserName();
		new Thread(new Runnable() {
	        @Override
	        public void run() {
	        	int time = 0;
	        	while(true) {
	        		log.debug("thread time : " + time + " / id :" + id);
	        		try {
						Thread.sleep(1000);
						time++;
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        	}
	        }            
	    }).start();
	}
}