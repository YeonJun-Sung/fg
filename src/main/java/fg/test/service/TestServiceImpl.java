package fg.test.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import fg.login.dao.LoginDAO;
import fg.test.dao.TestDAO;

@Service("testService")
public class TestServiceImpl implements TestService{
	@Resource(name="testDAO")
    private TestDAO testDAO;

	@Resource(name="loginDAO")
    private LoginDAO loginDAO;

    Logger log = Logger.getLogger(this.getClass());
	
	@Override
	public List<Map<String, Object>> getPlayerList(String key) throws Exception {
		// TODO Auto-generated method stub
		return testDAO.getPlayerList(key);
	}

	@Override
	public Map<String, Object> getPlayerInfoDetail(String key) throws Exception {
		// TODO Auto-generated method stub
		return testDAO.getPlayerInfoDetail(key);
	}
}