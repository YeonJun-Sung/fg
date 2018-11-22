package fg.transfer.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import fg.common.dao.AbstractDAO;

@Repository("transferDAO")
public class TransferDAO extends AbstractDAO {

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getTransferInfo(String key) {
		// TODO Auto-generated method stub
		return (List<Map<String, Object>>) selectList("transfer.getTransferInfo", key);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getPlayerInfo(String key) {
		// TODO Auto-generated method stub
		return (Map<String, Object>) selectOne("transfer.getPlayerInfo", key);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getRatingInfo(String key) {
		// TODO Auto-generated method stub
		return (Map<String, Object>) selectOne("transfer.getRatingInfo", key);
	}

	public void makePlayer(Map<String, Object> param) {
		// TODO Auto-generated method stub
		insert("transfer.makePlayer", param);
	}

	public int updateTempPlayer(Map<String, Object> param) {
		return (Integer) update("transfer.updateTempPlayer", param);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Integer> getPlayerOverall(String position, String key) {
		// TODO Auto-generated method stub
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("position", position);
		param.put("player_key", key);
		return (Map<String, Integer>) selectOne("transfer.getPlayerOverall", param);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPlayerList(String user_key) {
		// TODO Auto-generated method stub
		return (List<Map<String, Object>>) selectList("transfer.getPlayerList", user_key);
	}
	
	//3보다 큰 횟수 등장시 드랍
	public void dropTempPlayer(int appear) {
		// TODO Auto-generated method stub
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("appear", appear);
		delete("transfer.dropTempPlayer", param);
	}

	public void removeTempPlayer() {
		// TODO Auto-generated method stub
		update("transfer.removeTempPlayer","");
	}

	//선수 권한 종료 매서드
	public void removeTempUserKey(String user_key) {
		// TODO Auto-generated method stub
		update("transfer.removeTempUserKey", user_key);
	}

	
	@SuppressWarnings("unchecked")
	public Map<String, Object> selectTransfer(String player_key) {
		// TODO Auto-generated method stub
		return (Map<String, Object>) selectOne("transfer.selectTransfer", player_key);
	}

	public void insertTransfer(Map<String, Object> data) {
		// TODO Auto-generated method stub
		insert("transfer.insertTransfer", data);
	}

	public void dropTransfer(String player_key) {
		// TODO Auto-generated method stub
		delete("transfer.dropTransfer", player_key);
	}
}