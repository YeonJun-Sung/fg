package fg.test.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import fg.common.dao.AbstractDAO;

@Repository("testDAO")
public class TestDAO extends AbstractDAO {

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPlayerList(String key) {
		// TODO Auto-generated method stub
		return (List<Map<String, Object>>)selectList("test.getPlayerList", key);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getPlayerInfoDetail(String key) {
		// TODO Auto-generated method stub
		return (Map<String, Object>)selectOne("test.getPlayerInfoDetail", key);
	}

}