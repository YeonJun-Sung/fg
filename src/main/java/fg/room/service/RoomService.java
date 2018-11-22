package fg.room.service;

import java.util.Map;
import java.util.List;
public interface RoomService {
	void enterRoom(Map<String, Object> map) throws Exception;
	List<Map<String, Object>> getRoomId(String room_key) throws Exception;
	int getPlayerOverall(List<Map<String, Object>> list) throws Exception;
	void updateRoomCount() throws Exception;
	List<Map<String, Object>> getRoundInfo(String room_key) throws Exception;
	String getRoomKey(String user_key) throws Exception;
	void removeRoom(String room_key) throws Exception;
	int getWaitingRoomTime(String room_key) throws Exception;
	void updateRoomTime(String room_key, int keyword) throws Exception;
	int getUserCountInRoom(String room_key) throws Exception;
	List<String> insertGameKey(String room_key) throws Exception;
	void exitRoom(String user_key) throws Exception;
	List<String> getUserKeyInTempGameKey(String game_key) throws Exception;
	String getHostUserKey(String room_key) throws Exception;
}
