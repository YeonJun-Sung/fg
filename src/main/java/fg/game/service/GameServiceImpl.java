package fg.game.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import fg.game.dao.GameDAO;

@Service("gameService")
public class GameServiceImpl implements GameService {
	@Resource(name = "gameDAO")
	private GameDAO gameDAO;

	Logger log = Logger.getLogger(this.getClass());

	@Override
	public List<Map<String, Object>> getOrderUserPlayerInfo(String user_key) throws Exception {
		// TODO Auto-generated method stub
		return gameDAO.getOrderUserPlayerInfo(user_key);
	}

	@Override
	public void makeGame(Map<String, Object> param) throws Exception {
		// TODO Auto-generated method stub
		gameDAO.makeGame(param);
	}

	@Override
	public void updateStatus(Map<String, Object> param) throws Exception {
		// TODO Auto-generated method stub
		gameDAO.updateStatus(param);
	}

	@Override
	public String getGameKey(String user_key) throws Exception {
		// TODO Auto-generated method stub
		return gameDAO.getGameKey(user_key);
	}

	@Override
	public Map<String, Integer> getBasicCoord(String position) throws Exception {
		// TODO Auto-generated method stub
		return gameDAO.getBasicCoord(position);
	}

	@Override
	public void saveGamePosition(List<Map<String, Object>> list) throws Exception {
		// TODO Auto-generated method stub
		gameDAO.saveGamePosition(list);
	}

	@Override
	public void resetGamePosition(String user_key) throws Exception {
		// TODO Auto-generated method stub
		gameDAO.resetGamePosition(user_key);
	}

	@Override
	public List<Map<String, Object>> getGameInfo(String game_key, String user_key) throws Exception {
		// TODO Auto-generated method stub
		List<Map<String, Object>> rtvList = gameDAO.getGameInfo(game_key);

		String val = "";
		for (int i = 0; i < rtvList.size(); i++) {
			val = (String) rtvList.get(i).get("user_key");
			if (val.equals(user_key))
				rtvList.get(i).put("who", "home");
			else
				rtvList.get(i).put("who", "away");
		}

		return rtvList;
	}

	@Override
	public Map<String, Object> checkAwayUser(String user_key) {
		// TODO Auto-generated method stub home_user, away_user, b.id home_id, c.id
		// away_id, home_status, away_status
		Map<String, Object> param = gameDAO.checkAwayUser(user_key);
		Map<String, Object> result = new HashMap<String, Object>();

		String valid = (String) param.get("home_user");
		int status = -1;
		String id = "";
		if (valid.equals(user_key)) {
			status = ((Integer) param.get("away_status")).intValue();
			id = (String) param.get("away_id");
		} else {
			status = ((Integer) param.get("home_status")).intValue();
			id = (String) param.get("home_id");
		}
		result.put("id", id);
		result.put("status", status);

		return result;
	}

	@Override
	public List<Map<String, Object>> getPlayerList(String user_key) throws Exception {
		// TODO Auto-generated method stub
		return gameDAO.getPlayerList(user_key);
	}

	@Override
	public String intoGame(String user_key) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> map = gameDAO.findGame();
		if (map != null) {
			String valid = (String) map.get("home_user");
			String game_key = (String) map.get("game_key");

			Map<String, Object> param = new HashMap<String, Object>();
			if (!valid.equals("") && valid != null) {
				param.put("po", "away");
			} else {
				param.put("po", "home");
			}
			param.put("game_key", game_key);
			param.put("user_key", user_key);
			gameDAO.intoGame(param);
			return game_key;
		} else
			return null;

	}

	@Override
	public int changePosition(Map<String, Object> param) throws Exception {
		// TODO Auto-generated method stub
		return gameDAO.changePosition(param);
	}

	@Override
	public void removePlayer(String player_key) throws Exception {
		// TODO Auto-generated method stub
		gameDAO.removePlayer(player_key);
	}

	@Override
	public void insertPlayer(Map<String, Object> param) throws Exception {
		// TODO Auto-generated method stub
		gameDAO.insertPlayer(param);
	}

	@Override
	public Map<String, Object> makePercent(String section) throws Exception {
		// TODO Auto-generated method stub
		return gameDAO.getMakePercent(section);
	}

	@Override
	public Map<String, Object> makeAction(Map<String, Object> per) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		Random generator = new Random();

		int[] act = new int[3];
		act[0] = (Integer) per.get("shoot");
		act[1] = (Integer) per.get("pass") + act[0];
		act[2] = (Integer) per.get("dribble") + act[1];
		int rand = generator.nextInt(act[2]) + 1;

		if (rand <= act[0]) {
			map.put("action", "S");
		} else if (rand <= act[1]) {
			map.put("action", "P");
		} else if (rand <= act[2]) {
			map.put("action", "D");
		}

		return map;
	}

	@Override
	public int[][] getSection(String gk) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> divide = gameDAO.getSection(gk);
		int min_x, max_x, min_y, max_y, div;
		int[][] section = new int[38][51];
		for (int k = 0; k < divide.size(); k++) {
			min_x = ((Integer) divide.get(k).get("min_x")).intValue();
			min_y = ((Integer) divide.get(k).get("min_y")).intValue();
			max_x = ((Integer) divide.get(k).get("max_x")).intValue();
			max_y = ((Integer) divide.get(k).get("max_y")).intValue();
			div = ((Integer) divide.get(k).get("section")).intValue();
			for (int j = min_y; j <= max_y; j++) {
				for (int i = min_x; i <= max_x; i++) {
					section[j][i] = div;
				}
			}
		}
		return section;
	}

	@Override
	public void removeGameInfo(String game_key) throws Exception {
		// TODO Auto-generated method stub
		gameDAO.removeGameInfo(game_key);
	}

	@Override
	public void removeGameTime(String game_key) throws Exception {
		// TODO Auto-generated method stub
		gameDAO.removeGameTime(game_key);
	}

	@Override
	public void removeGameLog(String game_key) throws Exception {
		// TODO Auto-generated method stub
		gameDAO.removeGameLog(game_key);
	}

	@Override
	public void removeGame(String game_key) throws Exception {
		// TODO Auto-generated method stub
		gameDAO.removeGame(game_key);
	}

	@Override
	public String checkHome(String game_key) throws Exception {
		// TODO Auto-generated method stub
		return gameDAO.checkHome(game_key);
	}

	@Override
	public void createGameTime(String game_key) throws Exception {
		// TODO Auto-generated method stub
		gameDAO.createGameTime(game_key);
	}

	@Override
	public void updateGameTime(String game_key) throws Exception {
		// TODO Auto-generated method stub
		gameDAO.updateGameTime(game_key);
	}

	@Override
	public int getGameTime(String game_key) throws Exception {
		return gameDAO.getGameTime(game_key);
	}

	@Override
	public List<Map<String, Object>> getGameLog(String game_key) throws Exception {
		// TODO Auto-generated method stub
		return gameDAO.getGameLog(game_key);
	}

	@Override
	public String selectNotOwn(String check_home) throws Exception {
		// TODO Auto-generated method stub
		List<String> list = gameDAO.selectNotOwn(check_home);
		int random = (int) (Math.random() * 10);
		String player_key = list.get(random);
		return player_key;
	}

	@Override
	public void updateOwnBall(String player_key, String game_key) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("game_key", game_key);
		map.put("player_key", player_key);
		gameDAO.removeOwnBall(game_key);
		gameDAO.updateOwnBall(map);
	}

	@Override
	public String makeAction(int[][] section, int[][] section_gk, String game_key) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		Random generator = new Random();

		Map<String, Object> own_player = gameDAO.getOwnPlayer(game_key);
		String player_key = (String) own_player.get("player_key");
		int x = ((Integer) own_player.get("coord_x")).intValue();
		int y = ((Integer) own_player.get("coord_y")).intValue();
		String position = (String) own_player.get("select_position");
		map.put("player_key", player_key);

		Map<String, Object> per;
		if (!position.equals("GK"))
			per = gameDAO.getMakePercent(Integer.toString(section[y][x]));
		else
			per = gameDAO.getMakePercent(Integer.toString(section_gk[y][x]));
		int[] act = new int[3];
		act[0] = (Integer) per.get("shoot");
		act[1] = (Integer) per.get("pass") + act[0];
		act[2] = (Integer) per.get("dribble") + act[1];
		int rand = generator.nextInt(act[2]) + 1;

		if (rand <= act[0]) {
			map.put("action", "S");
		} else if (rand <= act[1]) {
			map.put("action", "P");
		} else if (rand <= act[2]) {
			map.put("action", "D");
		}
		return (String) map.get("action");
	}

	@Override
	public String getDetailAction(int[][] section, int[][] section_gk, String game_key, String action)
			throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		Random generator = new Random();

		Map<String, Object> own_player = gameDAO.getOwnPlayer(game_key);
		String player_key = (String) own_player.get("player_key");
		int x = ((Integer) own_player.get("coord_x")).intValue();
		int y = ((Integer) own_player.get("coord_y")).intValue();
		String position = (String) own_player.get("select_position");
		map.put("player_key", player_key);

		Map<String, Object> per;
		if (!position.equals("GK"))
			per = gameDAO.getDetailAction(Integer.toString(section[y][x]), action);
		else
			per = gameDAO.getDetailAction(Integer.toString(section_gk[y][x]), action);

		int act_count = 0;
		String[] p_action = { "short", "long" };
		String[] s_action = { "normal", "heading", "acrobatic" };
		String[] d_action = { "skill", "tal", "walking", "running", "chi_dal" };
		String[] detail_action = null;
		if (action.equals("P")) {
			detail_action = p_action;
			act_count = 2;
		} else if (action.equals("S")) {
			detail_action = s_action;
			act_count = 3;
		} else if (action.equals("D")) {
			detail_action = d_action;
			act_count = 5;
		}

		int total = 100;
		int rand = generator.nextInt(total) + 1;
		for (int i = 0; i < act_count; i++) {
			rand -= (Integer) per.get("act_" + i);
			if (rand <= 0) {
				map.put("action", detail_action[i]);
				break;
			}
		}
		return (String) map.get("action");
	}

	@Override
	public Map<String, Object> getOwnPlayer(String game_key) throws Exception {
		// TODO Auto-generated method stub
		return gameDAO.getOwnPlayer(game_key);
	}

	@Override
	public List<Map<String, Object>> findAroundPlayer(Map<String, Object> coord, String game_key, String camp,
			int distance) throws Exception {
		// TODO Auto-generated method stub
		String user_key = gameDAO.getOwnUserKey(game_key);
		coord.put("distance", distance);
		coord.put("user_key", user_key);
		coord.put("game_key", game_key);
		coord.put("camp", camp);
		return gameDAO.findAroundPlayer(coord);
	}

	@Override
	public void createActionRecord(String game_key) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("game_key", game_key);
		param.put("update", "O");
		List<Map<String, Object>> list = gameDAO.getGamePlayerKey(param);
		if(list.size() != 0) gameDAO.createActionRecord(list);
		param.put("update", "X");
		list = gameDAO.getGamePlayerKey(param);
		if(list.size() != 0) gameDAO.updateActionRecord(list);
	}

	@Override
	public void recordAction(String game_key, String player_key, String act, String detail_action, Boolean success)
			throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> param = new HashMap<String, Object>();
		String action = act + "_" + detail_action + "_try";
		String action_suc = success ? (act + "_" + detail_action + "_suc") : "";
		param.put("game_key", game_key);
		param.put("player_key", player_key);
		param.put("action", action);
		param.put("action_suc", action_suc);
		gameDAO.recordAction(param);
	}

	@Override
	public String getInterceptPlayer(Map<String, Object> start_coord, Map<String, Object> end_coord, String game_key) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> param = new HashMap<String, Object>();
		int start_x = ((Integer) start_coord.get("coord_x")).intValue();
		int start_y = ((Integer) start_coord.get("coord_y")).intValue();
		int end_x = ((Integer) end_coord.get("coord_x")).intValue();
		int end_y = ((Integer) end_coord.get("coord_y")).intValue();
		param.put("start_x", start_x < end_x ? start_x : end_x);
		param.put("start_y", start_y < end_y ? start_y : end_y);
		param.put("end_x", start_x < end_x ? end_x : start_x);
		param.put("end_y", start_y < end_y ? end_y : start_y);
		param.put("game_key", game_key);

		return gameDAO.getInterceptPlayer(param);
	}

	@Override
	public void updateCoord(String player_key, String game_key, Map<String, Object> param) throws Exception {
		// TODO Auto-generated method stub
		param.put("player_key", player_key);
		param.put("game_key", game_key);
		int val = gameDAO.checkOwnKeep(param);
		if (val != 1) {
			int coord_x = 50 - ((Integer) param.get("coord_x")).intValue();
			int coord_y = 37 - ((Integer) param.get("coord_y")).intValue();
			param.put("coord_x", coord_x);
			param.put("coord_y", coord_y);
		}
		gameDAO.updateCoord(param);
	}

	@Override
	public Boolean checkOwnKeep(String player_key, String game_key) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("player_key", player_key);
		param.put("game_key", game_key);

		int val = gameDAO.checkOwnKeep(param);
		return val == 1 ? true : false;
	}

	@Override
	public Map<String, Object> getDribbleSection(int temp_dsection) throws Exception {
		// TODO Auto-generated method stub
		return gameDAO.getDribbleSection(temp_dsection);
	}

	@Override
	public String getGoalKeeperKey(String game_key, String camp) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("game_key", game_key);
		param.put("camp", camp);
		return gameDAO.getGoalKeeperKey(param);
	}

	@Override
	public Map<String, Object> makeLooseBallCoord(String player_key) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> intercept_coord = gameDAO.getPlayerCoord(player_key);
		Map<String, Object> coord = new HashMap<String, Object>();
		Random generator = new Random();

		int rand = generator.nextInt(361 * 2) - 361;
		if (rand < 0) {
			rand = (int) Math.sqrt(rand * (-1)) * (-1);
		} else {
			rand = (int) Math.sqrt(rand);
		}
		int coord_x = (int) intercept_coord.get("coord_x") + rand;

		rand = generator.nextInt(361 * 2) - 361;
		if (rand < 0) {
			rand = (int) Math.sqrt(rand * (-1)) * (-1);
		} else {
			rand = (int) Math.sqrt(rand);
		}
		int coord_y = (int) intercept_coord.get("coord_y") + rand;

		coord.put("coord_x", coord_x);
		coord.put("coord_y", coord_y);

		return coord;
	}

	@Override
	public Map<String, Object> getEnemyGK(String game_key) throws Exception {
		// TODO Auto-generated method stub
		return gameDAO.getEnemyGK(game_key);
	}

	@Override
	public String getKicker(String kind, String camp, String game_key) throws Exception {
		// TODO Auto-generated method stub
		List<String> top_stat_player = new ArrayList<String>();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("camp", camp);
		param.put("game_key", game_key);
		param.put("limit", 3);
		param.put("stat", "kick");
		List<String> temp_player = gameDAO.getTopStat(param);
		top_stat_player.addAll(temp_player);
		if (kind.equals("corner") || kind.equals("indirect")) {
			param.put("stat", "cross_stat");
			temp_player = gameDAO.getTopStat(param);
			top_stat_player.addAll(temp_player);
		} else if (kind.equals("direct")) {
			param.put("stat", "shoot_accuracy");
			temp_player = gameDAO.getTopStat(param);
			top_stat_player.addAll(temp_player);
		}
		Random generator = new Random();
		int idx = generator.nextInt(top_stat_player.size());

		return top_stat_player.get(idx);
	}

	@Override
	public List<Map<String, Object>> getStartCoord(String game_key) throws Exception {
		// TODO Auto-generated method stub
		return gameDAO.getStartCoord(game_key);
	}

	@Override
	public void updateBasicCoord(List<Map<String, Object>> basic_coord) throws Exception {
		// TODO Auto-generated method stub
		gameDAO.updateBasicCoord(basic_coord);
	}

	@Override
	public Map<String, Object> getPlayerCoord(String player_key) throws Exception {
		// TODO Auto-generated method stub
		return gameDAO.getPlayerCoord(player_key);
	}

	@Override
	public void recordGameLog(String game_key, String own_player_key, String player_key, String intercept, String act,
			String detail_action, Boolean own_keep, Boolean goal, Boolean loose_ball, Map<String, Object> end_coord,
			int time) throws Exception {
		// TODO Auto-generated method stub
		String own_player_name = gameDAO.getPlayerName(own_player_key);
		String player_name = gameDAO.getPlayerName(player_key);
		String try_log = "" + own_player_name + "이(가)";
		if (end_coord.get("coord_x") != null && end_coord.get("coord_y") != null)
			try_log += "(" + end_coord.get("coord_x") + ", " + end_coord.get("coord_y") + ")로 ";
		if (act.equals("S"))
			act = "Shoot";
		else if (act.equals("P"))
			act = "Pass";
		else if (act.equals("D"))
			act = "Dribble";
		try_log += act + "(" + detail_action + ")을 시도합니다.";

		String result_log = "";
		if (goal)
			result_log = act + "(" + detail_action + ")에 성공하여 " + player_name + "이(가) 득점에 성공합니다.";
		else if (loose_ball && own_keep)
			result_log = act + "(" + detail_action + ")에 실패하였지만 볼은 같은 팀인 " + player_name + "이(가) 소유합니다.";
		else if (own_keep)
			result_log = act + "(" + detail_action + ")에 성공하여 볼을 " + player_name + "이(가) 소유합니다.";
		else
			result_log = act + "(" + detail_action + ")에 실패하여 볼을 " + player_name + "이(가) 빼앗아 갑니다.";

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("game_time", time);
		param.put("game_key", game_key);

		gameDAO.recordGameLog(param, try_log, result_log);
	}

	@Override
	public List<Map<String, Object>> getNotParticipatePlayerList(String game_key, String own_player_key) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("game_key", game_key);
		param.put("own_player", own_player_key);

		return gameDAO.getNotParticipatePlayerList(param);
	}

	@Override
	public int getLastLineCoord(String state, String game_key, String move_player_key) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("state", state);
		param.put("game_key", game_key);
		param.put("player_key", move_player_key);
		return gameDAO.getLastLineCoord(param);
	}

	@Override
	public Map<String, Object> getMoveSection(String move_player_position, int coord_x_gap, int coord_y_gap,
			int state_label, int datum_coord_x, int ball_coord_x) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("player_position", move_player_position);
		param.put("label", state_label);

		Map<String, Object> result = gameDAO.getMoveSection(param);
		int min_x = ((Integer) result.get("min_x")).intValue();
		int max_x = ((Integer) result.get("max_x")).intValue();

		// -1 상대 최전방 공격수 x좌표 1칸 뒤까지
		// -2 상대 최종 수비라인 1칸 뒤까지
		// -3 공위치 x좌표부터
		// -4 상대 최종 수비라인 5칸 뒤까지
		// -5 상대 최종 수비라인 3칸 뒤까지
		// -6 상대 최종 수비라인과 하프라인 사이까지
		// -7 하프라인과 상대 최종 수비라인 기준 1/4지점부터
		// -8 상대 최종 수비라인 1칸 앞까지
		switch (min_x) {
		case -3:
			min_x = ball_coord_x;
			break;
		case -7:
			min_x = 25 + (datum_coord_x - 25) / 4;
			break;
		default:
			break;
		}
		switch (max_x) {
		case -1:
		case -2:
			max_x = datum_coord_x - 1;
			break;
		case -4:
			max_x = datum_coord_x - 4;
			break;
		case -5:
			max_x = datum_coord_x - 3;
			break;
		case -6:
			max_x = (25 + datum_coord_x) / 2;
			break;
		case -8:
			max_x = datum_coord_x + 1;
			break;
		default:
			break;
		}
		int temp_min_x = (min_x < max_x)?min_x:max_x;
		int temp_max_x = (min_x > max_x)?min_x:max_x;
		min_x = temp_min_x;
		max_x = temp_max_x;

		int x_size = max_x - min_x;

		if (coord_x_gap != 0) {
			min_x = min_x - coord_x_gap;
			max_x = max_x - coord_x_gap;
			if (min_x < 0) {
				min_x = 0;
				max_x = x_size;
			}
			if (max_x > 50) {
				max_x = 50;
				min_x = max_x - x_size;
			}
			result.put("min_x", min_x);
			result.put("max_x", max_x);
		}

		return result;
	}

	@Override
	public Map<String, Object> calculateMoveCoord(String game_key, Map<String, Object> move_section,
			Map<String, Object> move_player) throws Exception {
		// TODO Auto-generated method stub
		int min_x = ((Integer) move_section.get("min_x")).intValue();
		int max_x = ((Integer) move_section.get("max_x")).intValue();
		int min_y = ((Integer) move_section.get("min_y")).intValue();
		int max_y = ((Integer) move_section.get("max_y")).intValue();
		int player_coord_x = ((Integer) move_player.get("coord_x")).intValue();
		int player_coord_y = ((Integer) move_player.get("coord_y")).intValue();
		int result_x = 0;
		int result_y = 0;
		int acc = ((Integer) move_player.get("acc")).intValue();
		int speed = ((Integer) move_player.get("speed")).intValue();
		double distance_ability = Math.sqrt(speed) + Math.sqrt(acc);
		int distance = 0;
		if(distance_ability > 20)
			distance = 10;
		else if(distance_ability > 15)
			distance = 9;
		else if(distance_ability > 10)
			distance = 8;
		else
			distance = 7;
		Random generator = new Random();

		int rand_x = generator.nextInt(distance * 2) - distance;
		int rand_y = generator.nextInt(distance * 2) - distance;
		if (min_x <= player_coord_x && max_x >= player_coord_x && min_y <= player_coord_y && max_y >= player_coord_y) {
			result_x = player_coord_x + rand_x;
			result_y = player_coord_y + rand_y;
		} else {
			rand_x = generator.nextInt(distance);
			rand_y = generator.nextInt(distance);
			result_x = player_coord_x + rand_x;
			result_y = player_coord_y + rand_y;
			if (min_x > player_coord_x || max_x < player_coord_x)
				result_x = player_coord_x + (min_x > player_coord_x ? rand_x : rand_x * (-1));
			if (min_y > player_coord_y || max_y < player_coord_y)
				result_y = player_coord_y + (min_y > player_coord_y ? rand_y : rand_y * (-1));
		}

		if (result_x < 0)
			result_x = 0;
		else if (result_x > 50)
			result_x = 50;
		if (result_y < 0)
			result_y = 0;
		else if (result_y > 37)
			result_y = 37;

		String player_key = (String) move_player.get("player_key");
		int[] coord_x_direction = { 1, 1, 0, -1, -1, -1, 0, 1 }; // 8방위에 대한 x좌표
		int[] coord_y_direction = { 0, 1, 1, 1, 0, -1, -1, -1 }; // 8방위에 대한 y좌표
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("game_key", game_key);
		param.put("coord_x", result_x);
		param.put("coord_y", result_y);
		param.put("player_key", player_key);
		int valid = gameDAO.checkOverlapCoord(param);
		int rand_idx = 0;
		while (valid != 0) {
			rand_idx = generator.nextInt(8);
			result_x += coord_x_direction[rand_idx];
			result_y += coord_y_direction[rand_idx];

			if (result_x < 0)
				result_x = 0;
			else if (result_x > 50)
				result_x = 50;
			if (result_y < 0)
				result_y = 0;
			else if (result_y > 37)
				result_y = 37;

			param.put("coord_y", result_y);
			param.put("coord_x", result_x);
			valid = gameDAO.checkOverlapCoord(param);
		}

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("coord_x", result_x);
		result.put("coord_y", result_y);

		return result;
	}

	@Override
	public void updateNotParticipatePlayerCoord(String game_key, Map<String, Object> param, String move_player_key)
			throws Exception {
		// TODO Auto-generated method stub
		param.put("game_key", game_key);
		param.put("player_key", move_player_key);
		gameDAO.updateNotParticipatePlayerCoord(param);
	}

	@Override
	public void updateGameSettingTime(String game_key, int keyword) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("game_key", game_key);
		param.put("keyword", keyword);
		gameDAO.updateGameSettingTime(param);
	}

	@Override
	public int getGameSettingTime(String game_key) throws Exception {
		// TODO Auto-generated method stub
		int valid = gameDAO.checkGameStartStatus(game_key);
		if(valid == 1)
			return 60;
		else
			return gameDAO.getGameSettingTime(game_key);
	}

	@Override
	public List<String> getUserKey(String game_key) throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> rtv = gameDAO.getUserKey(game_key);
		List<String> list = new ArrayList<String>();
		list.add(rtv.get("home_user"));
		list.add(rtv.get("away_user"));
		return list;
	}

	@Override
	public List<Map<String, Object>> getPlayerAllList(String user_key) throws Exception {
		// TODO Auto-generated method stub
		return gameDAO.getPlayerAllList(user_key);
	}

	@Override
	public void updateCoordPreGame(String player_key, String game_key, Map<String, Integer> own_coord) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> param = new HashMap<String, Object>();
		int coord_x = own_coord.get("coord_x");
		int coord_y = own_coord.get("coord_y");
		param.put("player_key", player_key);
		param.put("game_key", game_key);
		param.put("coord_x", coord_x);
		param.put("coord_y", coord_y);
		
		gameDAO.updateCoord(param);
	}
}