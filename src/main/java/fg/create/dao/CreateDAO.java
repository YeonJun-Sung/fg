package fg.create.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import fg.common.dao.AbstractDAO;
import fg.vo.UserVO;

@Repository("createDAO")
public class CreateDAO extends AbstractDAO {
	
	//랜덤으로 생성된 선수 정보 insert
	public void makePlayer(Map<String, Object> map) {
		// TODO Auto-generated method stub
		insert("create.makePlayer", map);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getMakePercent(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return (Map<String, Object>)selectOne("create.getMakePercent", map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPlayerList(String user_key) {
		// TODO Auto-generated method stub
		return (List<Map<String, Object>>)selectList("create.getPlayerList", user_key);
	}

	public void removePlayerList(String user_key) {
		// TODO Auto-generated method stub
		delete("create.removePlayerList", user_key);
	}

	//팀 생성 매서드
	public String makeTeam(Map<String, Object> map) {
		// TODO Auto-generated method stub
		insert("create.makeTeam", map);
		return (String)map.get("team_key");
	}

	public void updatePlayers(Map<String, Object> param) {
		// TODO Auto-generated method stub
		update("create.updatePlayers", param);
	}

	public void editPlayerName(Map<String, Object> param) {
		// TODO Auto-generated method stub
		update("create.editPlayerName", param);
	}

	//유저 정보에 팀키 넣기
	public void updateUserInfo(Map<String, Object> param) {
		// TODO Auto-generated method stub
		update("create.updateUserInfo", param);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Integer> getPlayerTendency(String position) {
		// TODO Auto-generated method stub
		return (Map<String, Integer>)selectOne("create.getPlayerTendency", position);
	}

	//overall 가져오기
	@SuppressWarnings("unchecked")
	public Map<String, Integer> getPlayerOverall(String position, String player_key) {
		// TODO Auto-generated method stub
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("position", position);
		param.put("player_key", player_key);
		return (Map<String, Integer>)selectOne("create.getPlayerOverall", param);
	}

	public UserVO getUserInfo(String user_key) {
		// TODO Auto-generated method stub
		return (UserVO)selectOne("create.getUserInfo", user_key);
	}

	public void editPlayerBackNum(Map<String, Object> param) {
		// TODO Auto-generated method stub
		update("create.editPlayerBackNum", param);
	}
}