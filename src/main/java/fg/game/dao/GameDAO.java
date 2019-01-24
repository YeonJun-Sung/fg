package fg.game.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import fg.common.dao.AbstractDAO;

@Repository("gameDAO")
public class GameDAO extends AbstractDAO {
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getOrderUserPlayerInfo(String user_key) {
		// TODO Auto-generated method stub
		return (List<Map<String, Object>>) selectList("game.getOrderUserPlayerInfo", user_key);
	}

	public void makeGame(Map<String, Object> param) {
		// TODO Auto-generated method stub
		insert("game.makeGame", param);
	}

	public void updateStatus(Map<String, Object> param) {
		// TODO Auto-generated method stub
		int val = (int) update("game.updateHomeStatus", param);
		if (val == 0)
			update("game.updateAwayStatus", param);
	}

	public String getGameKey(String user_key) {
		// TODO Auto-generated method stub
		return (String) selectOne("game.getGameKey", user_key);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Integer> getBasicCoord(String position) {
		// TODO Auto-generated method stub
		return (Map<String, Integer>) selectOne("game.getBasicCoord", position);
	}

	public void saveGamePosition(List<Map<String, Object>> list) {
		// TODO Auto-generated method stub
		insert("game.saveGamePosition", list);
	}

	@SuppressWarnings("unchecked")
	public void resetGamePosition(String user_key) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> list = (List<Map<String, Object>>) selectList("game.getPlayerKey", user_key);
		delete("game.resetGamePosition", list);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getGameInfo(String game_key) {
		// TODO Auto-generated method stub
		return (List<Map<String, Object>>) selectList("game.getGameInfo", game_key);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> checkAwayUser(String user_key) {
		// TODO Auto-generated method stub
		return (Map<String, Object>) selectOne("game.checkAwayUser", user_key);
	}

	public void intoGame(Map<String, Object> param) {
		// TODO Auto-generated method stub
		update("game.intoGame", param);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPlayerList(String user_key) {
		// TODO Auto-generated method stub
		return (List<Map<String, Object>>) selectList("game.getPlayerList", user_key);
	}

	public int changePosition(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return (int) update("game.changePosition", param);
	}

	public void removePlayer(String player_key) {
		// TODO Auto-generated method stub
		delete("game.removePlayer", player_key);
	}

	public void insertPlayer(Map<String, Object> param) {
		// TODO Auto-generated method stub
		insert("game.insertPlayer", param);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getMakePercent(String section) {
		// TODO Auto-generated method stub
		return (Map<String, Object>) selectOne("game.getMakePercent", section);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getSection(String gk) {
		// TODO Auto-generated method stub
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("gk", gk);
		return (List<Map<String, Object>>) selectList("game.getSection", param);
	}

	public void removeGameInfo(String game_key) {
		// TODO Auto-generated method stub
		delete("game.removeGameInfo", game_key);
	}

	public void removeGameTime(String game_key) {
		// TODO Auto-generated method stub
		delete("game.removeGameTime", game_key);
	}

	public void removeGameLog(String game_key) {
		// TODO Auto-generated method stub
		delete("game.removeGameLog", game_key);
	}

	public void removeGame(String game_key) {
		// TODO Auto-generated method stub
		delete("game.removeGame", game_key);
	}

	public String checkHome(String game_key) {
		// TODO Auto-generated method stub
		return (String) selectOne("game.checkHome", game_key);
	}

	public void createGameTime(String game_key) {
		// TODO Auto-generated method stub
		insert("game.createGameTime", game_key);
	}

	public void updateGameTime(String game_key) {
		// TODO Auto-generated method stub
		update("game.updateGameTime", game_key);
	}

	public int getGameTime(String game_key) {
		// TODO Auto-generated method stub
		Integer time = (Integer) selectOne("game.getGameTime", game_key);
		return (time == null)?-1:time.intValue();
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getGameLog(String game_key) {
		// TODO Auto-generated method stub
		return (List<Map<String, Object>>) selectList("game.getGameLog", game_key);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> findGame() {
		// TODO Auto-generated method stub
		return (Map<String, Object>) selectOne("game.findGame", "");
	}
	
	public void saveAction(Map<String, Object> map) {
		// TODO Auto-generated method stub
		insert("game.saveAction", map);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getOwnPlayer(String game_key) {
		// TODO Auto-generated method stub
		return (Map<String, Object>)selectOne("game.getOwnPlayer", game_key);
	}

	@SuppressWarnings("unchecked")
	public List<String> selectNotOwn(String check_home) {
		// TODO Auto-generated method stub
		return (List<String>)selectList("game.selectNotOwn", check_home);
	}

	public void removeOwnBall(String game_key) {
		// TODO Auto-generated method stub
		update("game.removeOwnBall", game_key);
	}

	public void updateOwnBall(Map<String, Object> map) {
		// TODO Auto-generated method stub
		update("game.updateOwnBall", map);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getDetailAction(String section, String action) {
		// TODO Auto-generated method stub
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("section", section);
		param.put("action", action);
		return (Map<String, Object>) selectOne("game.getDetailAction", param);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findAroundPlayer(Map<String, Object> coord) {
		// TODO Auto-generated method stub
		return (List<Map<String, Object>>) selectList("game.findAroundPlayer", coord);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getGamePlayerKey(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return (List<Map<String, Object>>) selectList("game.getGamePlayerKey", param);
	}

	public void createActionRecord(List<Map<String, Object>> list) {
		// TODO Auto-generated method stub
		insert("game.createActionRecord", list);
	}

	public void updateActionRecord(List<Map<String, Object>> list) {
		// TODO Auto-generated method stub
		update("game.updateActionRecord", list);
	}

	public void recordAction(Map<String, Object> param) {
		// TODO Auto-generated method stub
		update("game.recordAction", param);
	}

	public String getOwnUserKey(String game_key) {
		// TODO Auto-generated method stub
		return (String) selectOne("game.getOwnUserKey", game_key);
	}

	public String getInterceptPlayer(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return (String) selectOne("game.getInterceptPlayer", param);
	}

	public void updateCoord(Map<String, Object> param) {
		// TODO Auto-generated method stub
		update("game.updateCoord", param);
	}

	public int checkOwnKeep(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return (int) selectOne("game.checkOwnKeep", param);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getDribbleSection(int temp_dsection) {
		// TODO Auto-generated method stub
		return (Map<String, Object>) selectOne("game.getDribbleSection", temp_dsection);
	}

	public String getGoalKeeperKey(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return (String) selectOne("game.getGoalKeeperKey", param);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getPlayerCoord(String player_key) {
		// TODO Auto-generated method stub
		return (Map<String, Object>) selectOne("game.getPlayerCoord", player_key);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getEnemyGK(String game_key) {
		// TODO Auto-generated method stub
		return (Map<String, Object>)selectOne("game.getEnemyGK", game_key);
	}

	@SuppressWarnings("unchecked")
	public List<String> getTopStat(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return (List<String>) selectList("game.getTopStat", param);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getStartCoord(String game_key) {
		// TODO Auto-generated method stub
		return (List<Map<String, Object>>) selectList("game.getStartCoord", game_key);
	}

	public void updateBasicCoord(List<Map<String, Object>> basic_coord) {
		// TODO Auto-generated method stub
		update("game.updateBasicCoord", basic_coord);
	}

	public void recordGameLog(Map<String, Object> param, String try_log, String result_log) {
		// TODO Auto-generated method stub
		param.put("game_log", try_log);
		insert("game.recordGameLog", param);
		int game_time = ((Integer) param.get("game_time")).intValue();
		param.put("game_log", result_log);
		param.put("game_time", game_time + 1);
		insert("game.recordGameLog", param);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getNotParticipatePlayerList(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return (List<Map<String, Object>>) selectList("game.getNotParticipatePlayerList", param);
	}

	public String getPlayerName(String player_key) {
		// TODO Auto-generated method stub
		return (String) selectOne("game.getPlayerName", player_key);
	}

	public int getLastLineCoord(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return (int) selectOne("game.getLastLineCoord", param);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getMoveSection(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return (Map<String, Object>) selectOne("game.getMoveSection", param);
	}

	public int checkOverlapCoord(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return (int) selectOne("game.checkOverlapCoord", param);
	}

	public void updateNotParticipatePlayerCoord(Map<String, Object> param) {
		// TODO Auto-generated method stub
		update("game.updateNotParticipatePlayerCoord", param);
	}

	public void updateGameSettingTime(Map<String, Object> param) {
		// TODO Auto-generated method stub
		update("game.updateGameSettingTime", param);
	}

	public int getGameSettingTime(String game_key) {
		// TODO Auto-generated method stub
		Integer time = (Integer) selectOne("game.getGameSettingTime", game_key);
		return (time == null)?-1:time.intValue();
	}

	public int checkGameStartStatus(String game_key) {
		// TODO Auto-generated method stub
		return (int) selectOne("game.checkGameStartStatus", game_key);
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> getUserKey(String game_key) {
		// TODO Auto-generated method stub
		return (Map<String, String>) selectOne("game.getUserKey", game_key);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPlayerAllList(String user_key) {
		// TODO Auto-generated method stub
		return (List<Map<String, Object>>) selectList("game.getPlayerAllList", user_key);
	}

	public void updateCoordPreGame(Map<String, Object> param) {
		// TODO Auto-generated method stub
		update("game.updateCoordPreGame", param);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getRandomPlayerKey(String game_key) {
		// TODO Auto-generated method stub
		return (Map<String, Object>) selectOne("game.getRandomPlayerKey", game_key);
	}

	public void ownNullEmend(Map<String, Object> param) {
		// TODO Auto-generated method stub
		update("game.ownNullEmend", param);
	}
}