package fg.manage.service;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import fg.create.dao.CreateDAO;
import fg.login.dao.LoginDAO;
import fg.manage.dao.ManageDAO;

@Service("manageService")
public class ManageServiceImpl implements ManageService{
	@Resource(name="manageDAO")
    private ManageDAO manageDAO;

	@Resource(name="loginDAO")
    private LoginDAO loginDAO;

	@Resource(name="createDAO")
    private CreateDAO createDAO;

    Logger log = Logger.getLogger(this.getClass());
	
    //오버롤을 가져오기 위해 createDAO 사용
	@Override
	public List<Map<String, Object>> getPlayerOverall(List<Map<String, Object>> list) throws Exception {
		// TODO Auto-generated method stub
		String key = "";
		String position = "";
		Object overall = 0;
		for(int i = 0;i < list.size();i++) {
			key = (String) list.get(i).get("player_key");
			position = (String) list.get(i).get("position_detail");
			overall = (Object) createDAO.getPlayerOverall(position, key).get("average");
			list.get(i).put("overall", overall);
		}
		return list;
	}

	//아쿠아 부분 선수 정보 가져오는 매서드
	@Override
	public Map<String, Object> getPlayerInfoDetail(String player_key) throws Exception {
		// TODO Auto-generated method stub
		return manageDAO.getPlayerInfoDetail(player_key);
	}

	//플레이어에 포지션 정보 넣기
	@Override
	public void saveSelectPosition(List<Map<String, Object>> list) throws Exception {
		// TODO Auto-generated method stub
		manageDAO.saveSelectPosition(list);
	}
	
	//실제는 사용하지 않는 리셋 매서드
	@Override
	public void resetSelectPosition(String user_key) throws Exception {
		// TODO Auto-generated method stub
		manageDAO.resetSelectPosition(user_key);
	}
}