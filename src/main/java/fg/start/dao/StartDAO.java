package fg.start.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import fg.common.dao.AbstractDAO;

@Repository("startDAO")
public class StartDAO extends AbstractDAO {

	//팀키 가져오는 매서드
	@SuppressWarnings("unchecked")
	public Map<String, Object> getTeamInfo(String user_key) {
		// TODO Auto-generated method stub
		return (Map<String, Object>) selectOne("start.getTeamInfo", user_key);
	}

}