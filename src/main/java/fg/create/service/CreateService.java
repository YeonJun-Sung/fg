package fg.create.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import fg.vo.UserVO;

@Service
public interface CreateService {
	void makePlayer(int position, int rating, String user_key, Map<String, Object> per) throws Exception;
	Map<String, Object> makePercent(int rating) throws Exception;
	List<Map<String, Object>> getPlayerList(String user_key) throws Exception;
	List<Map<String, Object>> getPlayerOverall(List<Map<String, Object>> list) throws Exception;
	void removePlayerList(String user_key) throws Exception;
	void editPlayerName(Map<String, Object> param) throws Exception;
	void saveTeam(Map<String, Object> param) throws Exception;
	UserVO getUserInfo(String user_key) throws Exception;
	void editPlayerBackNum(Map<String, Object> param) throws Exception;
}