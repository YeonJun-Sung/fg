package fg.login.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import fg.vo.UserVO;

@Service
public interface LoginService {
	Boolean checkLoginInfo(Map<String, Object> info) throws Exception;
	UserVO getUserInfo(Map<String, Object> info) throws Exception;
	Map<String, Object> findUserInfo(Map<String, Object> info) throws Exception;
	Map<String, Object> checkId(String id) throws Exception;
	void signupAction(Map<String, Object> param) throws Exception;
	Map<String, Object> checkName(String name) throws Exception;
	int checkEmail(String send_email) throws Exception;
}