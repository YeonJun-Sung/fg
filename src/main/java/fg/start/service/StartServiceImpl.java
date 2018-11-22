package fg.start.service;

import java.util.Map;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import fg.start.dao.StartDAO;

@Service("startService")


public class StartServiceImpl implements StartService{
	@Resource(name="startDAO")
    private StartDAO startDAO;
    Logger log = Logger.getLogger(this.getClass());
	
    //사용자키로 팀키 가져오기
    @Override
	public Map<String, Object> getTeamInfo(String user_key) throws Exception {
		// TODO Auto-generated method stub
		return startDAO.getTeamInfo(user_key);
	}

	
}