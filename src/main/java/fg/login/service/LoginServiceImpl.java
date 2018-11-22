package fg.login.service;

import java.util.Map;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import fg.login.dao.LoginDAO;
import fg.vo.UserVO;

@Service("loginService")
public class LoginServiceImpl implements LoginService{
	@Resource(name="loginDAO")
    private LoginDAO loginDAO;

    Logger log = Logger.getLogger(this.getClass());
    //회원가입 중복 아이디 확인 매서드
	public Boolean checkLoginInfo(Map<String, Object> info) {
		String user_key	 = null;
		//유저가 존재하는지 확인한다.
		user_key = loginDAO.getUserKey(info);
		
		if(user_key != null)
			return true;
		else
			return false;
	}
	
	//로그인시 유저 확인 매서드
	@Override
	public UserVO getUserInfo(Map<String, Object> info) throws Exception {
		UserVO result = loginDAO.getUserInfo(info);
		return result;
	}
	
	//유저 정보 찾기 매서드입니다.
	@Override
	public Map<String, Object> findUserInfo(Map<String, Object> info) throws Exception {
		// TODO Auto-generated method stub
		return loginDAO.findUserInfo(info);
	}
	
	//회원가입 페이지에서 중복아이디 확인 매서드
	@Override
	public Map<String, Object> checkId(String id) throws Exception {
		// TODO Auto-generated method stub
		return loginDAO.checkId(id);
	}

	//회원가입 조건 충족시 인서트 하기 위한 매서드
	@Override
	public void signupAction(Map<String, Object> param) throws Exception {
		// TODO Auto-generated method stub
		loginDAO.signupAction(param);
	}

	//회원가입 페이지에서 중복닉네임 확인 매서드
	@Override
	public Map<String, Object> checkName(String name) throws Exception {
		// TODO Auto-generated method stub
		return loginDAO.checkName(name);
	}

	//이메일 인증 확인 매서드
	@Override
	public int checkEmail(String send_email) throws Exception {
		// TODO Auto-generated method stub
		return loginDAO.checkEmail(send_email);
	}
}