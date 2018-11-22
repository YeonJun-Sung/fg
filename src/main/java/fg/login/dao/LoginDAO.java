package fg.login.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import fg.common.dao.AbstractDAO;
import fg.vo.UserVO;

@Repository("loginDAO")
public class LoginDAO extends AbstractDAO {
	//유저키 정보 가져오는 매서드
	public String getUserKey(Map<String, Object> info) {
		// TODO Auto-generated method stub
		return (String)selectOne("login.getUserKey", info);
	}
	
	//유저 정보 찾기 매서드
	@SuppressWarnings("unchecked")
	public Map<String, Object> findUserInfo(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return (Map<String, Object>) selectOne("login.findUserInfo", param);
	}
	
	//회원가입 페이지에서 중복아이디 확인 매서드
	@SuppressWarnings("unchecked")
	public Map<String, Object> checkId(String id) {
		// TODO Auto-generated method stub
		return (Map<String, Object>) selectOne("login.checkId", id);
	}

	//회원가입 조건 충족시 인서트 하기 위한 매서드
	public void signupAction(Map<String, Object> param) {
		// TODO Auto-generated method stub
		insert("login.signupAction", param);
	}
	
	//회원가입 페이지에서 중복닉네임 확인 매서드
	@SuppressWarnings("unchecked")
	public Map<String, Object> checkName(String name) {
		// TODO Auto-generated method stub
		return (Map<String, Object>) selectOne("login.checkName", name);
	}
	
	//로그인시 유저 확인 매서드
	public UserVO getUserInfo(Map<String, Object> info) {
		// TODO Auto-generated method stub
		return (UserVO) selectOne("login.getUserInfo", info);
	}

	//이메일 인증 확인 매서드
	public int checkEmail(String send_email) {
		// TODO Auto-generated method stub
		return (Integer) selectOne("login.checkEmail", send_email);
	}
}