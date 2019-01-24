package fg.game.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface GameService {
	List<Map<String, Object>> getOrderUserPlayerInfo(String user_key) throws Exception;
	void makeGame(Map<String, Object> param) throws Exception;
	void updateStatus(Map<String, Object> param) throws Exception;
	String getGameKey(String user_key) throws Exception;
	Map<String, Integer> getBasicCoord(String position) throws Exception;
	void saveGamePosition(List<Map<String, Object>> list) throws Exception;
	void resetGamePosition(String user_key) throws Exception;
	List<Map<String, Object>> getGameInfo(String game_key, String user_key) throws Exception;
	Map<String, Object> checkAwayUser(String user_key);
	String intoGame(String user_key) throws Exception;
	List<Map<String, Object>> getPlayerList(String user_key) throws Exception;
	int changePosition(Map<String, Object> param) throws Exception;
	void removePlayer(String player_key) throws Exception;
	void insertPlayer(Map<String, Object> param) throws Exception;
	Map<String, Object> makePercent(String section) throws Exception;
	Map<String, Object> makeAction(Map<String, Object> per) throws Exception;
	int[][] getSection(String gk) throws Exception;
	void removeGameInfo(String game_key) throws Exception;
	void removeGameTime(String game_key) throws Exception;
	void removeGameLog(String game_key) throws Exception;
	void removeGame(String game_key) throws Exception;
	String checkHome(String game_key) throws Exception;
	void createGameTime(String game_key) throws Exception;
	void updateGameTime(String game_key) throws Exception;
	int getGameTime(String game_key) throws Exception;
	List<Map<String, Object>> getGameLog(String game_key) throws Exception;
	String makeAction(int[][] section, int[][] section_gk, String game_key) throws Exception;
	String selectNotOwn(String check_home) throws Exception;
	void updateOwnBall(String player_key, String game_key) throws Exception;
	String getDetailAction(int[][] section, int[][] section_gk, String game_key, String action) throws Exception;
	Map<String, Object> getOwnPlayer(String game_key) throws Exception;
	List<Map<String, Object>> findAroundPlayer(Map<String, Object> coord, String game_key, String camp, int distance) throws Exception;
	void createActionRecord(String game_key) throws Exception;
	void recordAction(String game_key, String player_key, String act, String detail_action, Boolean success) throws Exception;
	String getInterceptPlayer(Map<String, Object> start_coord, Map<String, Object> end_coord, String game_key) throws Exception;
	void updateCoord(String player_key, String game_key, Map<String, Object> param) throws Exception;
	Boolean checkOwnKeep(String player_key, String game_key) throws Exception;
	Map<String, Object> getDribbleSection(int temp_dsection) throws Exception;
	String getGoalKeeperKey(String game_key, String camp) throws Exception;
	Map<String, Object> makeLooseBallCoord(String player_key) throws Exception;
	Map<String, Object> getEnemyGK(String game_key) throws Exception;
	String getKicker(String string, String camp, String game_key) throws Exception;
	List<Map<String, Object>> getStartCoord(String game_key) throws Exception;
	void updateBasicCoord(List<Map<String, Object>> basic_coord) throws Exception;
	Map<String, Object> getPlayerCoord(String player_key) throws Exception;
	void recordGameLog(String game_key, String own_player_key, String player_key, String intercept, String act, String detail_action, Boolean own_keep, Boolean goal, Boolean loose_ball, Map<String, Object> end_coord, int time) throws Exception;
	List<Map<String, Object>> getNotParticipatePlayerList(String game_key, String own_player_key) throws Exception;
	int getLastLineCoord(String state, String game_key, String move_player_key) throws Exception;
	Map<String, Object> getMoveSection(String move_player_position, int coord_x_gap, int coord_y_gap, int state_label, int datum_coord_x, int ball_coord_x) throws Exception;
	Map<String, Object> calculateMoveCoord(String game_key, Map<String, Object> move_section, Map<String, Object> move_player) throws Exception;
	void updateNotParticipatePlayerCoord(String game_key, Map<String, Object> param, String move_player_key) throws Exception;
	void updateGameSettingTime(String game_key, int keyword) throws Exception;
	int getGameSettingTime(String game_key) throws Exception;
	List<String> getUserKey(String game_key) throws Exception;
	List<Map<String, Object>> getPlayerAllList(String user_key) throws Exception;
	void updateCoordPreGame(String player_key, String game_key, Map<String, Integer> own_coord) throws Exception;
	void ownNullEmend(String game_key) throws Exception;
}