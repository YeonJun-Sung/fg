package fg.room.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import fg.common.dao.AbstractDAO;

@Repository("roomDAO")
public class RoomDAO extends AbstractDAO {

	@SuppressWarnings("unchecked")
	public Map<String, Object> getRoomInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return (Map<String, Object>) selectOne("room.getRoomInfo", map);
	}
	//방이 없을 경우 호스트가 방 생성
	public String insertInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		insert("room.insertInfo", map);
		return (String) map.get("room_key");
	}
	//호스트외에 다른 유저의 정보 등록
	public void initRoom(Map<String, Object> map) {
		insert("room.initRoom", map);
	}
	
	public String getHostUserKey(String room_key) {
		// TODO Auto-generated method stub
		return (String) selectOne("room.getHostUserKey", room_key);
	}
	
	public List<String> insertGameKey(List<Map<String, Object>> list) {
		// TODO Auto-generated method stub
		Map<String, Object> param = new HashMap<String, Object>();
		List<String> game_key = new ArrayList<String>();
		for(int i = 0;i < 5;i = i + 2) {
			String temp_game_key = (String) selectOne("room.generateGameKey", "");
			game_key.add(temp_game_key);
			int home_idx = ((Integer) list.get(i).get("round_info")).intValue();
			int away_idx = ((Integer) list.get(i + 1).get("round_info")).intValue();
			//두개씩 라운드 순서에 맞게 플레이어에게 게임키 부여
			param.put("temp_game_key", temp_game_key);
			param.put("home_idx", home_idx);
			param.put("away_idx", away_idx);
			update("room.insertGameKey", param);
		}
		return game_key;
	}
	
	public void updateInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		update("room.updateInfo", map);
	}
	
	public String findGameRoom(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return (String) selectOne("room.findGameRoom",map);
	}
	
	//방 정보에 관한 매서드
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getRoomId(String room_key) {
		// TODO Auto-generated method stub
		return (List<Map<String, Object>>) selectList("room.getRoomId", room_key);
	}
	
	public String getRoomKey(String user_key) {
		// TODO Auto-generated method stub
		return  (String) selectOne("room.getRoomKey", user_key);
	}

	public void updateRoomCount() {
		// TODO Auto-generated method stub
		update("room.updateRoomCount","");
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getRoundInfo(String room_key) {
		// TODO Auto-generated method stub
		return (List<Map<String, Object>>) selectList("room.getRoundInfo", room_key);
	}

	public void removeRoom(String room_key) {
		// TODO Auto-generated method stub
		delete("room.removeRoom", room_key);
	}
	public int getWaitingRoomTime(String room_key) {
		// TODO Auto-generated method stub
		return ((Integer) selectOne("room.getWaitingRoomTime", room_key)).intValue();
	}
	public void updateRoomTime(Map<String, Object> param) {
		// TODO Auto-generated method stub
		update("room.updateRoomTime", param);
	}
	public int getUserCountInRoom(String room_key) {
		// TODO Auto-generated method stub
		return (int) selectOne("room.getUserCountInRoom", room_key);
	}
	public void exitRoom(String user_key) {
		// TODO Auto-generated method stub
		update("room.exitRoom", user_key);
	}
	@SuppressWarnings("unchecked")
	public List<String> getUserKeyInTempGameKey(String game_key) {
		// TODO Auto-generated method stub
		return (List<String>) selectList("room.getUserKeyInTempGameKey", game_key);
	}
	
}