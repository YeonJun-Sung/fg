package fg.manage.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import fg.common.dao.AbstractDAO;

@Repository("manageDAO")
public class ManageDAO extends AbstractDAO {

	//플레이어 정보를 가져온다.
	@SuppressWarnings("unchecked")
	public Map<String, Object> getPlayerInfoDetail(String player_key) {
		// TODO Auto-generated method stub
		return (Map<String, Object>)selectOne("manage.getPlayerInfoDetail", player_key);
	}
	//포지션정보를 업데이트 한다.
	public void saveSelectPosition(List<Map<String, Object>> list) {
		// TODO Auto-generated method stub
		update("manage.saveSelectPosition", list);
	}
	//지우는 건데 아직 구현은 안함.
	public void resetSelectPosition(String user_key) {
		// TODO Auto-generated method stub
		update("manage.resetSelectPosition", user_key);
	}

}