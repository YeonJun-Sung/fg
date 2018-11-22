package fg.room.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import fg.room.dao.RoomDAO;
import fg.room.service.RoomService;
import fg.create.dao.CreateDAO;

@Service("roomService")
public class RoomServiceImpl implements RoomService{
	@Resource(name="roomDAO")
    private RoomDAO roomDAO;
	
	@Resource(name="createDAO")
    private CreateDAO createDAO;
	
    Logger log = Logger.getLogger(this.getClass());
	
    //데이터베이스에 저장될 방 생성
    @Override
	public void enterRoom(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		// 나랑 같은 레이팅에 인원이 덜 찬 방 찾기 
		String room_key = roomDAO.findGameRoom(map);
		String user_key = (String) map.get("user_key");
		
		// 내 user_key로 만들어진 room_key 가 있는지 찾기
		String valid = roomDAO.getRoomKey(user_key);
		
		// 이 조건이면 방이 필요한 유저!
		if(valid == null || valid.equals("")) {
			// 있는 방에 유저키 등록
			if (room_key != null && !room_key.equals(""))
				roomDAO.updateInfo(map);
			else {
				// 방이 없는 경우 호스트를 만들어 방키 생성
				map.put("room_key", roomDAO.insertInfo(map));
				for(int i = 0;i < 5;i++) {
					//디비 플레이어 정보를 고정하기 위해 인덱스 부여
					map.put("user_index", i + 1);
					roomDAO.initRoom(map);
				}
			}
		}
	}
	
	//방에 들어갈 정보와 다음 상대방의 정보 전달에 필요한 매서드
	@Override
	public List<Map<String, Object>> getRoomId(String room_key) throws Exception {
		// TODO Auto-generated method stub
		return roomDAO.getRoomId(room_key);
	}
	
	//오버롤을 포함한 방에 필요한 정보 가져오는 매서드
	@Override
	public int getPlayerOverall(List<Map<String, Object>> list) throws Exception {
		// TODO Auto-generated method stub
		String key = "";
		String position = "";
		Object overall = 0;
		int sum=0;
		for(int i = 0;i < list.size();i++) {
			key = (String) list.get(i).get("player_key");
			position = (String) list.get(i).get("position_detail");
			overall = createDAO.getPlayerOverall(position, key).get("average");
			
			sum += Integer.parseInt(overall.toString());
		}
		sum = (int) (sum / list.size());
		return sum;
	}

	//방 카운트 증가 매서드
	@Override
	public void updateRoomCount() throws Exception {
		// TODO Auto-generated method stub
		roomDAO.updateRoomCount();
	}
	
	//라운드 정보 가져오는 매서드
	@Override
	public List<Map<String, Object>> getRoundInfo(String room_key) throws Exception {
		// TODO Auto-generated method stub
		return roomDAO.getRoundInfo(room_key);
	}

	//방키 가져오는 매서드
	@Override
	public String getRoomKey(String user_key) throws Exception {
		// TODO Auto-generated method stub
		return roomDAO.getRoomKey(user_key);
	}

	//방 제거 매서드
	@Override
	public void removeRoom(String room_key) throws Exception {
		// TODO Auto-generated method stub
		roomDAO.removeRoom(room_key);
	}

	@Override
	public int getWaitingRoomTime(String room_key) throws Exception {
		// TODO Auto-generated method stub
		return roomDAO.getWaitingRoomTime(room_key);
	}

	@Override
	public void updateRoomTime(String room_key, int keyword) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("room_key", room_key);
		param.put("keyword", keyword);
		roomDAO.updateRoomTime(param);
	}

	@Override
	public int getUserCountInRoom(String room_key) throws Exception {
		// TODO Auto-generated method stub
		return roomDAO.getUserCountInRoom(room_key);
	}

	@Override
	public List<String> insertGameKey(String room_key) throws Exception {
		// TODO Auto-generated method stub
		//라운드 정보 가져오기
		List<Map<String, Object>> list = roomDAO.getRoundInfo(room_key);
		//게임키 인서트
		return roomDAO.insertGameKey(list);
	}

	@Override
	public void exitRoom(String user_key) throws Exception {
		// TODO Auto-generated method stub
		roomDAO.exitRoom(user_key);
	}

	@Override
	public List<String> getUserKeyInTempGameKey(String game_key) throws Exception {
		// TODO Auto-generated method stub
		return roomDAO.getUserKeyInTempGameKey(game_key);
	}

	@Override
	public String getHostUserKey(String room_key) throws Exception {
		// TODO Auto-generated method stub
		return roomDAO.getHostUserKey(room_key);
	}
	
}