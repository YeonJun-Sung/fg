package fg.login.controller;
//로그인에 관련된 객체이다.
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import fg.common.utils.Gmailsend;
import fg.login.service.LoginService;
import fg.vo.UserVO;

@Controller
public class LoginController {
    Logger log = Logger.getLogger(this.getClass());
    
	@Resource(name = "loginService")
	private LoginService loginService;
	//로그인 페이지 매서드
	@RequestMapping(value = "/login/loginPage.do")
	public ModelAndView loginPage() throws Exception {
		ModelAndView mv = new ModelAndView("/login/loginPage");
		return mv;
	}
	//회원가입 페이지 매서드
	@RequestMapping(value = "/login/signupPage.do")
	public ModelAndView signupPage() throws Exception {
		ModelAndView mv = new ModelAndView("/login/signupPage");
		return mv;
	}
	//로그인페이지에서 유저와 팀정보 확인 매서드
	@RequestMapping(value = "/login/loginAction.do")
	public @ResponseBody Map<String, Object> loginAction(HttpServletRequest req, HttpSession session) throws Exception {
		String id = req.getParameter("send_id");
		String pw = req.getParameter("send_pw");

		Map<String, Object> info = new HashMap<String, Object>();
		info.put("id", id);
		info.put("pw", pw);

		Map<String, Object> result = new HashMap<String, Object>();
		if (id.equals("") || id == null) {
			result.put("error", "Please enter ID.");
			log.debug(" Request login Fail \t:  ID is null");

			return result;
		} else if (pw.equals("") || pw == null) {
			result.put("error", "Please enter PW.");
			log.debug(" Request login Fail \t:  PW is null");

			return result;
		}
		//유저가 존재하는지 확인한다.
		Boolean valid = loginService.checkLoginInfo(info);
		if (valid) {
			
			UserVO user = loginService.getUserInfo(info);
			session.setAttribute("userSession", user);
			//생성된 팀이 있는지 여부를 확인한다.
			String user_key = user.getUserKey();
			String created_team = user.getTeamKey();
			result.put("user_key", user_key);
			result.put("team_key", created_team);

			log.debug(" Request id \t:  " + id);
			log.debug(" Request pw \t:  " + pw);
			log.debug(" Response key \t:  " + user_key);
			log.debug(" Response team \t:  " + created_team);
		} else {
			result.put("error", "Please check ID or PW.");
			log.debug(" Request login Fail \t:  No Matching Information.");
		}

		return result;
	}
	//유저 정보 찾기 매서드입니다.
	@RequestMapping(value = "/login/findUserInfo.do")
	public @ResponseBody Map<String, Object> findUserInfo(HttpServletRequest req) throws Exception {
		String email = req.getParameter("send_email");
		String mode = req.getParameter("send_mode");

		Map<String, Object> info = new HashMap<String, Object>();
		info.put("email", email);
		//이메일을 통해 유저의 아이디나 비밀번호를 알려준다.
		Map<String, Object> result = new HashMap<String, Object>();
		if (mode.equals("pw")) {
			String id = req.getParameter("send_id");
			if (id.equals("") || id == null) {
				result.put("error", "Please enter ID");
				return result;
			} else if (email.equals("") || email == null) {
				result.put("error", "Please enter E-mail");
				return result;
			} else {
				info.put("id", id);
				result = loginService.findUserInfo(info);
				if (result == null) {
					result = new HashMap<String, Object>();
					result.put("error", "No match data");
				}
			}
		} else if (!email.equals("") && email != null) {
			//pw 나 id 정보가 저장되는 변수이다.
			result = loginService.findUserInfo(info);
			if (result == null) {
				result = new HashMap<String, Object>();
				result.put("error", "No match E-mail");
			}
		} else
			result.put("error", "Please enter E-mail");

		return result;
	}
	//아이디나 비밀번호 찾기 매서드
	@RequestMapping(value = "/login/findPage.do")
	public ModelAndView findPage(HttpServletRequest req) throws Exception {
		ModelAndView mv = new ModelAndView("login/findPage");
		//모드는 id나 pw 두가지로 구성 되어 있다.
		String mode = req.getParameter("send_mode");
		mv.addObject("mode", mode);
		return mv;
	}
	//회원가입 페이지에서 중복아이디 확인 매서드
	@RequestMapping(value = "/login/checkId.do")
	public @ResponseBody Map<String, Object> checkId(HttpServletRequest req) throws Exception {
		String id = req.getParameter("send_id");

		Map<String, Object> result = new HashMap<String, Object>();
		if (id.equals("") || id == null) {
			result.put("error", "Please enter ID.");
			log.debug(" Request login Fail \t:  ID is null");
			return result;
		}
		//중복된 아이디를 확인 한다.
		result = loginService.checkId(id); // 쿼리문 결과

		return result;
	}
	//회원가입 페이지에서 중복닉네임 확인 매서드
	@RequestMapping(value = "/login/checkName.do")
	public @ResponseBody Map<String, Object> checkName(HttpServletRequest req) throws Exception {
		String name = req.getParameter("send_name");
		
		Map<String, Object> result = new HashMap<String, Object>();
		if (name.equals("") || name == null) {
			result.put("error", "Please enter Name.");
			log.debug(" Request login Fail \t:  Name is null");
			return result;
		}
		//중복된 닉네임을 확인 한다.
		result = loginService.checkName(name); // 쿼리문 결과

		return result;
	}
	//이메일 인증 확인 매서드
	@RequestMapping(value = "/login/authEmail.do")
	public @ResponseBody String authEmail(HttpServletRequest req) throws Exception {
		String auth_num = "";
		String send_email = req.getParameter("send_email");
		//이메일이 존재하지 않으면 인증번호를 생성한다.
		if(loginService.checkEmail(send_email) == 0) {
			Random generator = new Random();
			int string_rand = 0;
			int num_rand = 0;
			//랜덤 8자리 인증번호 생성
			for(int i = 0;i < 4;i++) {
				string_rand = generator.nextInt(26) + 65;
				num_rand = generator.nextInt(10) + 48;
				auth_num += Character.toString((char) string_rand);
				auth_num += Character.toString((char) num_rand);
			}
			log.debug(" Send auth number \t:  " + auth_num + " to " + send_email);
			//이메일 인증을 위해 사용하는 객체이다.
			Gmailsend mail = new Gmailsend();
			mail.GmailSet(send_email, "FG game auth number.", auth_num);
			
			return auth_num;
		}
		else
			return "duplicate"; // 중복 이메일 처리하기
	}
	//회원가입 조건 충족시 인서트 하기 위한 매서드
	@RequestMapping(value = "/login/signupAction.do")
	public @ResponseBody void signupAction(HttpServletRequest req) throws Exception {
		String id = req.getParameter("send_id");
		String name = req.getParameter("send_name");
		String pw = req.getParameter("send_pw");
		String send_email = req.getParameter("send_email");
		
		Map<String, Object> param = new HashMap<String, Object>();
		//모든 회원가입 정보를 담는다.
		param.put("id", id);
		param.put("name", name);
		param.put("pw", pw);
		param.put("email", send_email);
		//회원가입 정보를 통채로 넘긴다.
		loginService.signupAction(param);
	}

	@RequestMapping(value = "/logout/logoutAction.do")
	public @ResponseBody void logoutAction(HttpServletRequest req, HttpSession session) throws Exception {
		session.removeAttribute("userSession");
	}
}