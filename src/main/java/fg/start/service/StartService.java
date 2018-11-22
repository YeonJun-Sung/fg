package fg.start.service;

import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface StartService {
	Map<String, Object> getTeamInfo(String user_key) throws Exception;
}