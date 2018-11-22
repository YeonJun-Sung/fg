package fg.game.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import fg.create.service.CreateService;
import fg.game.service.GameService;
import fg.manage.service.ManageService;
import fg.room.service.RoomService;
import fg.vo.UserVO;

@Controller
public class GameController {
	Logger log = Logger.getLogger(this.getClass());

	@Resource(name = "gameService")
	private GameService gameService;
	
	@Resource(name = "createService")
	private CreateService createService;
	
	@Resource(name = "manageService")
	private ManageService manageService;
	
	@Resource(name = "roomService")
	private RoomService roomService;

	// 게임 진행상황에 따라 game_room 생성 or 시간에 대한 값이 존재하는 페이지로 이동
	@RequestMapping(value = "/game/playGame.do")
	public ModelAndView playGame(HttpServletRequest req, HttpSession session) throws Exception {
		ModelAndView mv = null;
		UserVO user = (UserVO) session.getAttribute("userSession");
		String user_key = user.getUserKey();
		String game_key = gameService.getGameKey(user_key);
		String room_key = roomService.getRoomKey(user_key);
		int valid = 0;
		
		if(room_key == null || room_key.equals("")) {
			// room이 생성되어 있지 않은 경우
			// 새로운 room을 생성하거나 기존에 있는 room 중 자리가 비어 있는 room으로 이동
			mv = new ModelAndView("/room/roomMake");
			int rating = user.getRating();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("user_key", user_key);
			map.put("rating", rating);
			roomService.enterRoom(map);
			
			int max_user = 6;
			mv.addObject("max_user", max_user);
			return mv;
		}
		else {
			// room time이 진행 중인 경우
			valid = roomService.getWaitingRoomTime(room_key);
			int user_count = roomService.getUserCountInRoom(room_key);
			if(valid != -1 || user_count != 6) {
				mv = new ModelAndView("/room/roomMake");
				int rating = user.getRating();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("user_key", user_key);
				map.put("rating", rating);
				roomService.enterRoom(map);
				
				int max_user = 6;
				mv.addObject("max_user", max_user);
				return mv;
			}
		}
		valid = gameService.getGameSettingTime(game_key);
		if(valid != -1 || valid >= 60) {
			// game setting time이 진행중인 경우
			// gamePage로 이동
			mv = new ModelAndView("/game/gamePage");
			List<Map<String, Object>> list = createService.getPlayerList(user_key);
			list = manageService.getPlayerOverall(list);
			mv.addObject("player_list", list);

			game_key = gameService.getGameKey(user_key);
			mv.addObject("game_key", game_key);
			return mv;
		}
		else {
			// game time이 진행중인 경우
			mv = new ModelAndView("/game/playGame");
			return mv;
		}
	}

	@RequestMapping(value = "/game/getOrderUserPlayerInfo.do")
	public @ResponseBody List<Map<String, Object>> getOrderUserPlayerInfo(HttpServletRequest req, HttpSession session) throws Exception {
		UserVO user = (UserVO) session.getAttribute("userSession");
		String user_key = user.getUserKey();
		List<Map<String, Object>> result = gameService.getOrderUserPlayerInfo(user_key);

		return result;
	}

	@RequestMapping(value = "/game/updateStatus.do")
	public @ResponseBody void updateStatus(HttpServletRequest req, HttpSession session) throws Exception {
		UserVO user = (UserVO) session.getAttribute("userSession");
		String status = req.getParameter("status");
		String game_key = req.getParameter("game_key");
		String user_key = user.getUserKey();

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("user_key", user_key);
		if (status.equals("start"))
			param.put("status", 1);
		else if (status.equals("cancel"))
			param.put("status", 0);
		param.put("game_key", game_key);
		
		gameService.updateStatus(param);
	}

	@SuppressWarnings("unlikely-arg-type")
	@RequestMapping(value = "/game/startGame.do")
	public @ResponseBody void startGame(HttpServletRequest req, HttpSession session) throws Exception {
		UserVO user = (UserVO) session.getAttribute("userSession");
		String user_key = user.getUserKey();
		String game_key = gameService.getGameKey(user_key);
		String check_home = gameService.checkHome(game_key);
		String temp_key = req.getParameter("player_key");
		String temp_position = req.getParameter("select_position");

		String[] player_key = temp_key.split("/");
		String[] position = temp_position.split("/");
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> param;
		Map<String, Integer> coord;
		Map<String, Integer> kickOffCoord = gameService.getBasicCoord("KICKOFF");
		int coord_x;
		int coord_y;
		int coord_x_kick = kickOffCoord.get("coord_x").intValue();
		int coord_y_kick = kickOffCoord.get("coord_y").intValue();
		int temp = -1;
		int priority = -1;
		int idx = 0;
		for (int i = 0; i < player_key.length; i++) {
			if (!player_key.equals("") && player_key != null) {
				param = new HashMap<String, Object>();
				param.put("game_key", game_key);
				param.put("player_key", player_key[i]);
				param.put("position", position[i]);
				if (!position[i].contains("SUB")) {
					coord = gameService.getBasicCoord(position[i]);
					coord_x = coord.get("coord_x").intValue();
					coord_y = coord.get("coord_y").intValue();
					param.put("coord_x", coord_x);
					param.put("coord_y", coord_y);
					if (check_home.equals(user_key)) {
						temp = coord.get("priority").intValue();
						if (priority == -1 || priority > temp) {
							idx = i;
							priority = temp;
						}
					}
				}
				list.add(param);
			}
		}
		if (check_home.equals(user_key)) {
			list.get(idx).put("coord_x", coord_x_kick);
			list.get(idx).put("coord_y", coord_y_kick);
			list.get(idx).put("own_ball", "own");
		}
		gameService.resetGamePosition(user_key);
		gameService.saveGamePosition(list);
	}

	@RequestMapping(value = "/game/checkAwayUser.do")
	public @ResponseBody Map<String, Object> checkAwayUser(HttpServletRequest req, HttpSession session) throws Exception {
		UserVO user = (UserVO) session.getAttribute("userSession");
		String user_key = user.getUserKey();
		Map<String, Object> param = gameService.checkAwayUser(user_key);

		return param;
	}

	@RequestMapping(value = "/game/updateGamePosition.do")
	public @ResponseBody void updateGamePosition(HttpServletRequest req, HttpSession session) throws Exception {
		UserVO user = (UserVO) session.getAttribute("userSession");
		String user_key = user.getUserKey();
		String game_key = gameService.getGameKey(user_key);
		List<Map<String, Object>> list = gameService.getPlayerList(user_key);

		Map<String, Integer> coord;
		int coord_x;
		int coord_y;
		for (int i = 0; i < list.size(); i++) {
			String position = (String) list.get(i).get("position");
			list.get(i).put("game_key", game_key);
			if (!position.contains("SUB")) {
				coord = gameService.getBasicCoord(position);
				coord_x = coord.get("coord_x").intValue();
				coord_y = coord.get("coord_y").intValue();
				list.get(i).put("coord_x", coord_x);
				list.get(i).put("coord_y", coord_y);
			}
		}
		gameService.resetGamePosition(user_key);
		gameService.saveGamePosition(list);
	}

	@RequestMapping(value = "/game/changePosition.do")
	public @ResponseBody void changePosition(HttpServletRequest req, HttpSession session) throws Exception {
		UserVO user = (UserVO) session.getAttribute("userSession");
		String user_key = user.getUserKey();
		String player_key = req.getParameter("player_key");
		String position = req.getParameter("position");
		
		Map<String, Object> param = new HashMap<String, Object>();
		if (!position.contains("SUB") && position != null && !position.equals("none")) {
			Map<String, Integer> coord = gameService.getBasicCoord(position);
			int coord_x = coord.get("coord_x").intValue();
			int coord_y = coord.get("coord_y").intValue();
			param.put("coord_x", coord_x);
			param.put("coord_y", coord_y);
		} else {
			param.put("coord_x", null);
			param.put("coord_y", null);
		}
		if (position.equals("none")) {
			gameService.removePlayer(player_key);
		} else {
			param.put("player_key", player_key);
			param.put("position", position);
			int valid = gameService.changePosition(param);
			if (valid == 0) {
				String game_key = gameService.getGameKey(user_key);
				param.put("game_key", game_key);
				gameService.insertPlayer(param);
			}
		}
	}

	@RequestMapping(value = "/game/getGameTime.do")
	public @ResponseBody int getGameTime(HttpServletRequest req, HttpSession session) throws Exception {
		UserVO user = (UserVO) session.getAttribute("userSession");
		String user_key = user.getUserKey();
		String game_key = gameService.getGameKey(user_key);
		int time = gameService.getGameTime(game_key);

		return time;
	}

	@RequestMapping(value = "/game/getGameLog.do")
	public @ResponseBody List<Map<String, Object>> getGameLog(HttpServletRequest req, HttpSession session) throws Exception {
		UserVO user = (UserVO) session.getAttribute("userSession");
		String user_key = user.getUserKey();
		String game_key = gameService.getGameKey(user_key);
		List<Map<String, Object>> log = gameService.getGameLog(game_key);

		return log;
	}

	@RequestMapping(value = "/game/getGameInfo.do")
	public @ResponseBody List<Map<String, Object>> getGameInfo(HttpServletRequest req, HttpSession session) throws Exception {
		UserVO user = (UserVO) session.getAttribute("userSession");
		String user_key = user.getUserKey();
		String game_key = gameService.getGameKey(user_key);
		List<Map<String, Object>> list = gameService.getGameInfo(game_key, user_key);

		return list;
	}

	@RequestMapping(value = "/game/removeGame.do")
	public @ResponseBody void removeGame(HttpServletRequest req, HttpServletResponse res, HttpSession session)
			throws Exception {
		UserVO user = (UserVO) session.getAttribute("userSession");
		String user_key = user.getUserKey();
		String game_key = gameService.getGameKey(user_key);
		String room_key = roomService.getRoomKey(user_key);
		gameService.removeGameInfo(game_key);
		gameService.removeGameTime(game_key);
		gameService.removeGameLog(game_key);
		roomService.removeRoom(room_key);
		gameService.removeGame(game_key);
	}

	@RequestMapping(value = "/game/gameThread.do")
	public void gameThread(HttpServletRequest req, HttpSession sess) throws Exception {
		UserVO user = (UserVO) sess.getAttribute("userSession");
		String user_key = user.getUserKey();
		String room_key = roomService.getRoomKey(user_key);
		int room_time = roomService.getWaitingRoomTime(room_key);
		if(room_time == 0) {
			roomService.updateRoomTime(room_key, 1);
			roomThread(room_key);
		}
	}
	
	public void roomThread(String room_key) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				int time = 0;
				while(true) {
					// 시간 증가 & 확인
					try {
						roomService.updateRoomTime(room_key, 1);
						time = roomService.getWaitingRoomTime(room_key);
					
						log.debug("\t\t\t While End");
						if (time >= 60) {
							log.debug("\t\t\t Room Thread Stop");
							List<String> game_key = roomService.insertGameKey(room_key);
							Map<String, Object> param = new HashMap<String, Object>();
							for(int i = 0;i < game_key.size();i++) {
								List<String> user_key = roomService.getUserKeyInTempGameKey(game_key.get(i));
								param.put("home_user", user_key.get(0));
								param.put("away_user", user_key.get(1));
								param.put("game_key", game_key.get(i));
								gameService.makeGame(param);
								if(i == 0)
									gameSettingThread(game_key.get(i), room_key);
								else
									gameSettingThread(game_key.get(i), "");
							}
							roomService.updateRoomTime(room_key, 0);
							roomService.updateRoomCount();
							
							Thread.interrupted();
							break;
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						Thread.interrupted();
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	public void gameSettingThread(String game_key, String room_key) {
		try {
			List<String> user_key_list = gameService.getUserKey(game_key);
			for(int j = 0;j < user_key_list.size();j++) {
				String user_key = user_key_list.get(j);
				List<Map<String, Object>> list = gameService.getPlayerList(user_key);
	
				Map<String, Integer> coord;
				int coord_x;
				int coord_y;
				for (int i = 0; i < list.size(); i++) {
					String position = (String) list.get(i).get("position");
					list.get(i).put("game_key", game_key);
					if (!position.contains("SUB")) {
						coord = gameService.getBasicCoord(position);
						coord_x = coord.get("coord_x").intValue();
						coord_y = coord.get("coord_y").intValue();
						list.get(i).put("coord_x", coord_x);
						list.get(i).put("coord_y", coord_y);
					}
				}
				gameService.resetGamePosition(user_key);
				gameService.saveGamePosition(list);
			}
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				int time = 0;
				while(true) {
					// 시간 증가 & 확인
					try {
						gameService.updateGameSettingTime(game_key, 1);
						time = gameService.getGameSettingTime(game_key);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					log.debug("\t\t\t While End");
					if (time >= 60) {
						log.debug("\t\t\t Setting Thread Stop");
						Thread.interrupted();
						try {
							gameService.createActionRecord(game_key);
							gameService.updateGameSettingTime(game_key, 0);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						playGameThread(game_key, room_key);
						break;
					}

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						Thread.interrupted();
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	public void playGameThread(String game_key, String room_key) {
		long one_sec = 100;

		try {
			Map<String, Integer> kickOffCoord = gameService.getBasicCoord("KICKOFF");
			String check_home = gameService.checkHome(game_key);
			List<String> user_key_list = gameService.getUserKey(game_key);
			for(int j = 0;j < user_key_list.size();j++) {
				String user_key = user_key_list.get(j);
				if (check_home.equals(user_key)) {
					List<Map<String, Object>> list = gameService.getGameInfo(game_key, user_key);
					Map<String, Integer> coord;
					String player_key = "";
					int temp = -1;
					int priority = -1;
					for (int i = 0; i < list.size(); i++) {
						String position = (String) list.get(i).get("select_position");
						if (!position.contains("SUB")) {
							coord = gameService.getBasicCoord(position);
							temp = coord.get("priority").intValue();
							if (priority == -1 || priority > temp) {
								player_key = (String) list.get(i).get("player_key");
								priority = temp;
							}
						}
					}
					gameService.updateCoordPreGame(player_key, game_key, kickOffCoord);
					gameService.updateOwnBall(player_key, game_key);
				}
			}
		} catch (Exception e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				int time = 0;
				try {
					// Kick Off 상황에서의 고정위치
					// Priority를 정렬 기준으로 삼아 앞 번호가 Kick Off를 하게 됨
					List<Map<String, Object>> basic_coord = gameService.getStartCoord(game_key);
					Map<String, Integer> kickOffCoord = gameService.getBasicCoord("KICKOFF");

					// 처음 위치가 중앙인 선수의 위치를 원래 포지션에 맞는 위치로 변경
					for(int i = 0;i < basic_coord.size();i++) {
						String temp_player_po = (String) basic_coord.get(i).get("select_position");
						if(basic_coord.get(i).get("coord_x") == kickOffCoord.get("coord_x") && basic_coord.get(i).get("coord_y") == kickOffCoord.get("coord_y")) {
							Map<String, Integer> position_coord = gameService.getBasicCoord(temp_player_po);
							basic_coord.get(i).put("coord_x", position_coord.get("coord_x"));
							basic_coord.get(i).put("coord_y", position_coord.get("coord_y"));
							break;
						}
					}
					int[][] section = gameService.getSection("");
					int[][] section_gk = gameService.getSection("gk");
					
					gameService.createGameTime(game_key);
					
					while (true) {
						log.debug("\t\t\t While Start");
						log.debug("thread time : "
								+ ((time / 60 >= 10) ? "" + (int) (time / 60) : "0" + (int) (time / 60)) + ":"
								+ ((time % 60 >= 10) ? "" + (int) (time % 60) : "0" + (int) (time % 60)));
						try {
							gamePlayAlgorithm(game_key, section, section_gk, basic_coord, kickOffCoord, time);
							// 시간 증가 & 확인
							gameService.updateGameTime(game_key);
							time = gameService.getGameTime(game_key);
							
							log.debug("\t\t\t While End");
							if (time >= 90 * 60) {
								log.debug("\t\t\t Game Thread Stop");
								gameService.removeGameInfo(game_key);
								gameService.removeGameTime(game_key);
								gameService.removeGameLog(game_key);
								gameService.removeGame(game_key);
								if(!room_key.equals(""))	roomThread(room_key);
								Thread.interrupted();
								break;
							}
						} catch (Exception e1) {
							Thread.interrupted();
							e1.printStackTrace();
						}
						try {
							Thread.sleep(one_sec);
						} catch (InterruptedException e) {
							Thread.interrupted();
							e.printStackTrace();
						}
					}
				}
				catch (Exception e2) {
					Thread.interrupted();
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			}
		}).start();
	}
	
	public void gamePlayAlgorithm(String game_key, int[][] section, int[][] section_gk, List<Map<String, Object>> basic_coord, Map<String, Integer> kickOffCoord, int time) {
		Random generator = new Random();

		try {
			Map<String, Object> own_player = gameService.getOwnPlayer(game_key);
			Map<String, Object> enemy_GK = gameService.getEnemyGK(game_key);		// "own" 의 상대 골키퍼의 stat 
			
			String own_player_key = (String) own_player.get("player_key");
			if(own_player_key == null || own_player_key.equals(""))
				log.debug("own_null");
			String act = gameService.makeAction(section, section_gk, game_key);
			// 현재 own_player가 있는 위치에 따른 action 생성
			String detail_action = gameService.getDetailAction(section, section_gk, game_key, act);
			// 현재 own_player가 있는 위치와 action에 따른 detail_action 생성
			String last_touch = "";		// 마지막 터치된 선수 키 > loose ball 용
			String player_key = "";		// 공을 가진 선수
			String intercept = "";		// intercept 선수 키
			String flag = "";			// corner kick, goal kick flag
			Boolean loose_ball = false;	// loose ball이 됬는지 체크
			Boolean own_keep = true;	// 공의 소유권 유지 여부, 성공 or 실패에 대한 판단 기능
			Boolean goal = false;		// goal 성공 여부
			
			List<Map<String, Object>> competition_player = new ArrayList<Map<String, Object>>();
			// 공의 위치에 대한 상대 팀 경쟁 선수 목록
			List<Map<String, Object>> reciving_player_list = new ArrayList<Map<String, Object>>();
			// 공의 위치에 대한 같은 팀 선수 목록
			List<String> participate_player_key = new ArrayList<String>();
			Map<String, Object> select_reciving_player = new HashMap<String, Object>();	// 공을 받도록 선택된 선수
			Map<String, Object> start_coord = new HashMap<String, Object>();	// own_player의 시작 위치
			Map<String, Object> end_coord = new HashMap<String, Object>();		// 모든 action이 이루어진 뒤의 공의 위치
			int coord_x = ((Integer) own_player.get("coord_x")).intValue();
			int coord_y = ((Integer) own_player.get("coord_y")).intValue();
			start_coord.put("coord_x", coord_x);
			start_coord.put("coord_y", coord_y);
			// own_player로부터 공의 시작 위치 좌표를 받아옴
			participate_player_key.add(own_player_key);
			
			if(act.equals("P")) {
				// Pass action
				competition_player = gameService.findAroundPlayer(start_coord, game_key, "enemy", 2);
				// 주변 상대선수 확인
				if(!own_keep) {
					// 공격권이 유지되지 않은 경우
					// 클리어링에 대한 확률 발생
					// own_player의 침착성 & competition_player 수
					// 기본확률(50) - 침착성 / 5 + 주변 2칸 이내 상대 선수 수 ^ 2
					int clearing_factor = 50;
					int clearing_rand = generator.nextInt(100);
					int restlessness = ((Integer) own_player.get("restlessness")).intValue();
					
					clearing_factor = (int) (clearing_factor - restlessness / 5 + Math.pow(competition_player.size(), 2));
					
					if(clearing_rand < clearing_factor)
						detail_action = "clearing";
				}
	
				// 거리 10칸 내에 있는 선수 목록  > 숏 패스 가능여부 위해 미리 탐색
				reciving_player_list = gameService.findAroundPlayer(start_coord, game_key, "team", 10);
				if(reciving_player_list.size() == 0)
					detail_action = "long";
				
				int pass_stat = 0;
				int recive_stat = 0;
				int rand_idx = 0;
					
				if(detail_action.equals("short")) {
					// 숏 패스
					// 인터셉트 가능
					// competition_player와 own_player와의 볼경합
					// short pass stat 활용
					pass_stat = ((Integer) own_player.get("short_pass")).intValue();
				}
				else if(detail_action.equals("long")) {
					// 롱 패스
					// 인터셉트 불가
					// 거리 10칸 밖에 있는 선수 목록
					reciving_player_list = gameService.findAroundPlayer(start_coord, game_key, "team", -10);
	
					// competition_player와 own_player와의 볼경합 
					// long pass, eyesight stat 활용
					pass_stat = (((Integer) own_player.get("long_pass")).intValue()
								+ ((Integer) own_player.get("eyesight")).intValue()) / 2;
				}
	
				// 같은 팀 선수들 목록 중 랜덤 1명 선택
				rand_idx = generator.nextInt(reciving_player_list.size());
	
				// 기본 패스 성공률 70%
				// pass stat 기준 75 미만 -, 초과 +
				// 각각 정한 pass stat의 10%  확률 가감
				int pass_per = 70;
				pass_per += (pass_stat - 75) / 10;
				
				// clearing이 아닌경우 수비 통합 비교
				if(!detail_action.equals("clearing")) {
					int pass_rand = generator.nextInt(100) + 1;
					if(pass_rand <= pass_per || competition_player.size() == 0) {
						// 목록 내 선수들 중 랜덤 1명 선택
						select_reciving_player = reciving_player_list.get(rand_idx);
						coord_x = ((Integer) select_reciving_player.get("coord_x")).intValue();
						coord_y = ((Integer) select_reciving_player.get("coord_y")).intValue();
						end_coord.put("coord_x", coord_x);
						end_coord.put("coord_y", coord_y);
						// 선택된 한 명의 선수의 좌표를 end coord로 설정 
						
						// 인터셉트 확인
						// short pass만 Intercept 가능
						if(detail_action.equals("short")) intercept = gameService.getInterceptPlayer(start_coord, end_coord, game_key);
						
						// intercept 된 경우 intercept가 볼 소유
						if(intercept != null && !intercept.equals("")) {
							player_key = intercept;
							end_coord = gameService.getPlayerCoord(player_key);
						}
						else {
							competition_player = gameService.findAroundPlayer(end_coord, game_key, "enemy", 2);
							// select_reciving_player와 competition_player 볼경합
							// recive_stat > controll, positioning, struggle stat 활용
							// pass stat 기준 75 미만 -, 초과 +
							// 각각 정한 pass stat의 10%  확률 가감
							recive_stat = (((Integer) select_reciving_player.get("controll")).intValue()
										+ ((Integer) select_reciving_player.get("positioning")).intValue()
										+ ((Integer) select_reciving_player.get("struggle")).intValue()) / 3;
							pass_per = 70 + (recive_stat - 75) / 10;
	
							// 70 -> 수비 실패 & 30 -> 수비성공
							pass_rand = generator.nextInt(100) + 1;
							if(pass_rand > pass_per && competition_player.size() != 0) {
								rand_idx = generator.nextInt(competition_player.size());
								player_key = (String) competition_player.get(rand_idx).get("player_key");
							}
							else
								player_key = (String) select_reciving_player.get("player_key");
						}
					}
					else {
						rand_idx = generator.nextInt(competition_player.size());
						player_key = (String) competition_player.get(rand_idx).get("player_key");
					}
				}
				else if(detail_action.equals("clearing")) {
					// clearing인 경우
					// kick stat 활용
					// kick 능력치에 따른 거리 계산
					// 능력치 75 이하 > 10 동일
					// 능력치 75 초과 > kick * 7.5
					pass_stat = ((Integer) own_player.get("kick")).intValue();
					int distance = (int) (10 * (pass_stat <= 75?1:pass_stat / 75));
					
					coord_x += generator.nextInt(distance * 2) - distance;
					coord_y += generator.nextInt(distance * 2) - distance;
					
					loose_ball = true;
					last_touch = own_player_key;
					end_coord.put("coord_x", coord_x);
					end_coord.put("coord_y", coord_y);
				}
			} // Pass 액션 종료
			else if(act.equals("D")) {
				// Dribble action
				competition_player = gameService.findAroundPlayer(start_coord, game_key, "enemy", 2);
				// 주변 상대선수 확인
				int dribble_factor = 50;
				if(competition_player.size() == 0 && (detail_action.equals("skill") || detail_action.equals("tal")))
					detail_action = "walking";
				
				// detail_action에 따라 방위에 더해주는 값이 배가 된다 예를 들면 치달의 경우 3배가 됨 그 기준값 변수는 add_direction이다.
				int add_direction = 1;
	
				int acc = ((Integer) own_player.get("acc")).intValue();
				int speed  = ((Integer) own_player.get("speed")).intValue();
				int skill  = ((Integer) own_player.get("skill")).intValue();
				int reflex = ((Integer) own_player.get("reflex")).intValue();
				int restlessness = ((Integer) own_player.get("restlessness")).intValue();
				int flexibility = ((Integer) own_player.get("flexibility")).intValue();
				int[] weight = {1, 1, 1, 1, 1, 1};
				// acc > 0, speed > 1, skill > 2, reflex > 3, restlessness > 4, flexibility > 5; 
				
				if(detail_action.equals("skill")) {
					weight[2] = 3;
					weight[3] = 2;
					weight[4] = 2;
				}
				else if(detail_action.equals("tal")) {
					weight[3] = 3;
					weight[4] = 2;
					weight[5] = 2;
				}
				else if(detail_action.equals("running")) {
					add_direction = 2;
					weight[0] = 3;
					weight[1] = 3;
					weight[3] = 2;
					weight[4] = 2;
				}
				else if(detail_action.equals("chi_dal")) {
					add_direction = 3;
					weight[0] = 4;
					weight[1] = 4;
					weight[3] = 2;
					weight[4] = 2;
				}
				// 디테일 액션에 따른 가중치를 정해 확률의 범위를 정해주는 값이다
				dribble_factor = (int) ((acc * weight[0] + speed * weight[1] + skill * weight[2] + reflex * weight[3]
								+ restlessness * weight[4] + flexibility * weight[5]) / IntStream.of(weight).sum());
				
				// 드리블을 성공하면 주변사람 상관없이 공 소유 유지
				// 주변사람이 0이면 드리블이 실패해도 공 소유 유지
				int dribble_per = 50 + (dribble_factor - 75) / 10;
				int dribble_sucess = generator.nextInt(100) + 1;
				
				if(dribble_sucess < dribble_per && competition_player.size() != 0) {
					int rand_idx = generator.nextInt(competition_player.size());
					player_key = (String) competition_player.get(rand_idx).get("player_key");
				}
				else {
					// 소유권이 넘어가지 않는 경우 방위를 정해 이동 방향을 정한다
					// 방위를 정하고 위치를 정한다
					player_key = (String) own_player.get("player_key");
					
					int direction = generator.nextInt(8);
					int[] coord_x_direction = {1, 1, 0, -1, -1, -1, 0, 1};	//	8방위에 대한 x좌표
					int[] coord_y_direction = {0, 1, 1, 1, 0, -1, -1, -1};	//	8방위에 대한 y좌표
					int next_coord_x = coord_x + (add_direction * coord_x_direction[direction]);
					int next_coord_y = coord_y + (add_direction * coord_y_direction[direction]);
					
					// x, y좌표에 따라 side or goal line 아웃인 경우
					if(next_coord_y < 0)
						next_coord_y = 0;
					else if(next_coord_y > 37)
						next_coord_y = 37;
					if(next_coord_x < 0)
						next_coord_x = 0;
					else if(next_coord_x > 50)
						next_coord_x = 50; 
	
					end_coord.put("coord_x", next_coord_x);
					end_coord.put("coord_y", next_coord_y);
				}	// 바뀐 방위대로 플레이어의 위치를 변환 시킨다
			} // Drrible 액션 종료
			else if(act.equals("S")) {
				// Shoot action
				competition_player = gameService.findAroundPlayer(start_coord, game_key, "enemy", 2);
				
				// controll, shoot_accuracy, restlessness에 따른 슛 정확도 보정
				int controll = ((Integer) own_player.get("controll")).intValue();
				int shoot_accuracy = ((Integer) own_player.get("shoot_accuracy")).intValue();
				int restlessness = ((Integer) own_player.get("restlessness")).intValue();
	
				// 기본 슛 성공 확률 85%에서 stat과 주변의 상대 선수에 따라 확률 조정
				//  stat이 75 기준 미만 -, 초과 +
				// 기본 85 - (상대 선수 수) ^ 2 + (controll + shoot_accuracy + restlessness - 75 * 3) / 20 
				int shoot_ac = (int) (85 - Math.pow(competition_player.size(), 2) + (controll + shoot_accuracy + restlessness - 75 * 3) / 20);
				
				// 슛이 골대로 가는 위치 랜덤으로 결정
				int shooting = generator.nextInt(100) + 1;
				if(shooting <= shoot_ac) {
					coord_x = 50;
					coord_y = generator.nextInt(5) + 16;
				}
				else {
					coord_x = generator.nextInt(5) + 46;
					coord_y = generator.nextInt(38);
				}
				end_coord.put("coord_x", coord_x);
				end_coord.put("coord_y", coord_y);
					
				// 인터셉트 확인 > 슛 블로킹 확인
				// 같은 팀의 선수의 경우 피한다고 가정
				intercept = gameService.getInterceptPlayer(start_coord, end_coord, game_key);
				
				int shoot_case = generator.nextInt(100) + 1;
				if(intercept != null && shoot_case <= 50) {
					//블로킹 또는  굴절
					//핸드링(패널티킥) 추가하기
					end_coord = gameService.makeLooseBallCoord(intercept);
					last_touch = intercept;
					loose_ball = true;
				}
				else if(coord_x != 50 || coord_y > 20 || coord_y < 16) {
					// 골대를 벗어난 경우
					last_touch = own_player_key;
					loose_ball = true;
				}
	
				// Loose ball이 발생하지 않은 경우 > 공이 골대로 향한 경우
				if(!loose_ball)	{
	
					// 골키퍼 능력에 대한 변수
					int gk_handling = 0;
					double gk_ability = 0;
	
					// 골키퍼가 막을 수 있는 범위에 대한 변수
					int gk_coord_x = -1;
					int gk_coord_y = -1;
					int gk_coord_x_max = -1;
					int gk_coord_x_min = -1;
					int gk_coord_y_max = -1;
					int gk_coord_y_min = -1;
					
					if(enemy_GK != null) {
						// 선방에 필요한골키퍼 능력치
						gk_handling = ((Integer) enemy_GK.get("handling")).intValue();
						int gk_reflex = ((Integer) enemy_GK.get("reflex")).intValue();
						int gk_diving = ((Integer) enemy_GK.get("diving")).intValue();
						int gk_restlessness = ((Integer) enemy_GK.get("restlessness")).intValue();
						int gk_jump = ((Integer) enemy_GK.get("jump")).intValue();
						int gk_positioning = ((Integer) enemy_GK.get("positioning")).intValue();
	
						//상대 키퍼 위치 확인
						gk_coord_x = ((Integer) enemy_GK.get("coord_x")).intValue();
						gk_coord_y = ((Integer) enemy_GK.get("coord_y")).intValue();
						
						//상대 키퍼 능력치에 따른 수비 범위 설정(gk_diving, gk_jump)(각각 루트 계산 후 합으로 비교)
						gk_ability = Math.sqrt(gk_jump) + Math.sqrt(gk_diving);
						gk_coord_x_max = gk_coord_x;
						gk_coord_x_min = gk_coord_x;
						gk_coord_y_max = gk_coord_y;
						gk_coord_y_min = gk_coord_y;
						if(gk_ability > 20) {
							gk_coord_x_max -= 3;
							gk_coord_y_max += 3;
							gk_coord_y_min -= 3;
						}
						else if(gk_ability > 15) {
							gk_coord_x_max -= 2;
							gk_coord_y_max += 2;
							gk_coord_y_min -= 2;
						}
						else if(gk_ability > 10) {
							gk_coord_x_max -= 1;
							gk_coord_y_max += 1;
							gk_coord_y_min -= 1;
						}
						
						//골키퍼 능력치를 등급으로 변환
						gk_ability = Math.sqrt(gk_restlessness) + Math.sqrt(gk_reflex) + Math.sqrt(gk_positioning);
						if(gk_ability > 25) 
							gk_ability = 4;
						else if(gk_ability > 19) 
							gk_ability = 3;
						else if(gk_ability > 13) 
							gk_ability = 2;
						else 
							gk_ability = 1;
						
						//골키퍼 핸드링 능력을 등급으로 변환
						gk_handling = (int) Math.sqrt(gk_handling);
						if(gk_handling > 10.5)
							gk_handling = 4;
						else if(gk_handling > 9)
							gk_handling = 3;
						else if(gk_handling > 7.5)
							gk_handling = 2;
						else
							gk_handling = 1;
					}
						
					// 슛에 필요한 공격수 능력치
					int kick = ((Integer) own_player.get("kick")).intValue();
					int heading = ((Integer) own_player.get("heading")).intValue();
					int jump = ((Integer) own_player.get("jump")).intValue();
					int skill = ((Integer) own_player.get("skill")).intValue();
					int flexibility = ((Integer) own_player.get("flexibility")).intValue();
					double at_ability = 0;
					
					int[] grade = {20, 15, 10};		// 공격 등급의 기준
					if(detail_action.equals("normal")) {
						//공이 날라가는 좌표와 키퍼의 수비범위 비교(범위 밖일 경우 골/범위 안일 경우 수비 가능성 비교)
						//수비 성공시 잡았는지 쳐냈는지 설정
						//잡았을 경우 키퍼 의 공 소유 변경
						at_ability = Math.sqrt(kick) + Math.sqrt(skill);
					}
					else if(detail_action.equals("heading")) {
						//공이 날라가는 좌표와 키퍼의 수비범위 비교(범위 밖일 경우 골/범위 안일 경우 수비 가능성 비교)
						//수비 성공시 잡았는지 쳐냈는지 설정
						//잡았을 경우 키퍼 의 공 소유 변경
						at_ability = Math.sqrt(jump) + Math.sqrt(heading);
					}
					else if(detail_action.equals("acrobatic")) {
						//공이 날라가는 좌표와 키퍼의 수비범위 비교(범위 밖일 경우 골/범위 안일 경우 수비 가능성 비교)
						//수비 성공시 잡았는지 쳐냈는지 설정
						//잡았을 경우 키퍼 의 공 소유 변경
						at_ability = Math.sqrt(kick) + Math.sqrt(skill) + Math.sqrt(flexibility);
						grade[0] = 25;
						grade[1] = 19;
						grade[2] = 13;
					}
					
					//능력치를 계산하여 4개의 등급으로 구분
					if(at_ability > grade[0])
						at_ability = 4;
					else if(at_ability > grade[1])
						at_ability = 3;
					else if(at_ability > grade[2])
						at_ability = 2;
					else
						at_ability = 1;
	
					int goal_rand = generator.nextInt(100) + 1;
					int hand = generator.nextInt((gk_handling * 2) + 1) + 1;
					int goal_factor = (int) ((5 - at_ability + gk_ability) * 10);
	
					if(coord_x >= gk_coord_x_min && coord_x <= gk_coord_x_max
							&& coord_y >= gk_coord_y_min && coord_y <= gk_coord_y_max) {
						// 득점한 경우
						if(goal_rand > goal_factor || enemy_GK == null)
							goal = true;
						else {
							player_key = (String) enemy_GK.get("player_key");
							last_touch = player_key;
							if(hand > 1) {
								// 선방 & 캐치
								end_coord.put("coord_x", gk_coord_x);
								end_coord.put("coord_y", gk_coord_y);
							}
							else {
								// 선방 & 펀칭
								// 쳐냈을 시 공을 기준으로 쳐 내는 범위 설정
								int distance = 5;
								coord_x += generator.nextInt(distance);
								coord_y += generator.nextInt(distance * 2) - distance;
								end_coord.put("coord_x", coord_x);
								end_coord.put("coord_y", coord_y);
								
								loose_ball = true;
							}
						}
					}
					else
						goal = true;									
				}
			} // Shoot 액션 종료
			
			// action 중  loose_ball 발생
			if(loose_ball) {
				int rand_idx = 0;
				coord_x = ((Integer) end_coord.get("coord_x")).intValue();
				coord_y = ((Integer) end_coord.get("coord_y")).intValue();
				Boolean check_camp = gameService.checkOwnKeep(last_touch, game_key);
				// 마지막 터치가 어떤 팀인지 확인
				if(coord_y < 0 || coord_y >= 37) {
					// side out
					if(coord_x < 0)
						end_coord.put("coord_x", 1);
					else if(coord_x >= 50)
						end_coord.put("coord_x", 49);
					end_coord.put("coord_y", coord_y < 0?0:37);
					competition_player = gameService.findAroundPlayer(end_coord, game_key, check_camp?"enemy":"team", 50);
					rand_idx = generator.nextInt(competition_player.size());
					player_key = (String) competition_player.get(rand_idx).get("player_key");
				}
				else if(coord_x < 0 || coord_x >= 50) {
					// goal kick || corner kick
					// coord_x가 0보다 작으면 아군 골대 방향으로 아웃		> 마지막 터치가 같은팀 : 적군 코너킥	/ 상대팀 : 아군 골킥
					// coord_x가 50보다 크거나 같으면 상대 골대 방향으로 아웃	> 마지막 터치가 같은팀 : 적군 골킥	/ 상대팀 : 아군 코너킥
					if(coord_x < 0) flag = check_camp?"corner_kick":"goal_kick";
					else if(coord_x >= 50) flag = check_camp?"goal_kick":"corner_kick";
					String camp = check_camp?"team":"enemy";
						
					if(flag.equals("corner_kick") && coord_y <= 18) {
						// Left Corner Kick
						// Corner kick을 찰 조건이 되는 선수 선택
						player_key = gameService.getKicker("corner", camp, game_key);
						end_coord.put("coord_x", check_camp?50:0);
						end_coord.put("coord_y", check_camp?0:37);
					}
					else if(flag.equals("corner_kick") && coord_y > 18) {
						// Right Corner Kick
						// Corner kick을 찰 조건이 되는 선수 선택
						player_key = gameService.getKicker("corner", camp, game_key);
						end_coord.put("coord_x", check_camp?50:0);
						end_coord.put("coord_y", check_camp?37:0);
					}
					else if(flag.equals("goal_kick")) {
						// Goal Kick
						// Camp에 따라 골킥을 찰 선수 선정
						// 좌표는 y좌표 중간(18)은 동일 enemy일 경우는 x좌표 끝에서 2번째 칸(50 - 2) team인 경우 x좌표 2번째 칸(2)
						player_key = gameService.getGoalKeeperKey(game_key, camp);
						end_coord.put("coord_x", check_camp?(50 - 2):2);
						end_coord.put("coord_y", 18);
						
						// 골키퍼가 존재하지 않는 스쿼드의 경우 해당  camp 중 한 명을 정함
						if(player_key.equals("") || player_key == null) {
							competition_player = gameService.findAroundPlayer(end_coord, game_key, camp, 50);
							rand_idx = generator.nextInt(competition_player.size());
							player_key = (String) competition_player.get(rand_idx).get("player_key");
						}
					}
				}
				else {
					// In Play 상황 > 필드 내에 loose ball 발생
					// 기본 거리 5 내에의 아군 & 적군 선수 탐색
					// 아군 & 적군 모두 존재하지 않을 경우 거리를 1씩 증가하면서 한 명이라도 나올 떄까지 선수 탐색
					int increase = 5;
					do {
						reciving_player_list = gameService.findAroundPlayer(end_coord, game_key, "team", increase);
						competition_player = gameService.findAroundPlayer(end_coord, game_key, "enemy", increase);
						increase++;
					} while(reciving_player_list.size() == 0 && competition_player.size() == 0);
					
					double min_distance_team = -1;	// 아군 최단 거리
					double min_distance_enemy = -1;	// 적군 최단 거리
					double temp_distance_team = 0;	// 아군 최단거리 계산을 위한 변수
					double temp_distance_enemy = 0;	// 적군 최단거리 계산을 위한 변수
					int team_idx = 0;				// 아군 최단 거리 선수의 index
					int enemy_idx = 0;				// 적군 최단 거리 선수의 index
					// 아군 & 적군 수
					int team_size = reciving_player_list.size();
					int enemy_size = competition_player.size();
					
					// 공이 떨어진 지점-아군 & 적군의 최단 거리를 구하는 알고리즘
					for(int i = 0;i < ((team_size > enemy_size)?team_size:enemy_size);i++) {
						if(i < team_size) {
							temp_distance_team = Math.sqrt(Math.pow(coord_x - ((Integer)reciving_player_list.get(i).get("coord_x")).intValue(), 2)
												+ Math.pow(coord_y - ((Integer)reciving_player_list.get(i).get("coord_y")).intValue(), 2));
							
							if(min_distance_team == -1 || min_distance_team > temp_distance_team) {
								team_idx = i;
								min_distance_team = temp_distance_team;
							}
						}
						if(i < enemy_size) {
							temp_distance_enemy = Math.sqrt(Math.pow(coord_x - ((Integer)competition_player.get(i).get("coord_x")).intValue(), 2)
												+ Math.pow(coord_y - ((Integer)competition_player.get(i).get("coord_y")).intValue(), 2));
							
							if(min_distance_enemy == -1 || min_distance_enemy > temp_distance_enemy) {
								enemy_idx = i;
								min_distance_enemy = temp_distance_enemy;
							}
						}
					}
					
					int compete = generator.nextInt(100) + 1; 	// 경쟁 확률
					if(enemy_size == 0 && team_size != 0) 		// 거리 5 이내에 적군만 있는 경우
						player_key = (String) reciving_player_list.get(team_idx).get("player_key");
					else if(enemy_size != 0 && team_size == 0)	// 거리 5 이내에 아군만 있는 경우
						player_key = (String) competition_player.get(enemy_idx).get("player_key");
					else if(min_distance_team > min_distance_enemy) {
						// 적이 더 가까운 위치인 경우 80% 확률로 적팀이 공 획득
						// 공위치로 coord update
						if(compete <= 80 && enemy_size != 0)
							player_key = (String) competition_player.get(enemy_idx).get("player_key");
						else
							player_key = (String) reciving_player_list.get(team_idx).get("player_key");
					}
					else if(min_distance_team < min_distance_enemy) {
						// 아군이 더 가까운 위치인 경우 80% 확률로 아군이 공 획득
						// 공위치로 coord update
						if(compete <= 80 && team_size != 0)
							player_key = (String) reciving_player_list.get(team_idx).get("player_key");
						else
							player_key = (String) competition_player.get(enemy_idx).get("player_key");
					}
					else {
						// 거리 동일 > 경쟁
						// 몸싸움, 위치선정, 컨트롤, 키, 몸무게 > 5가지의 stat을 통해 각자의 범위를 설정
						// 두 명의 stat 합를 기준으로 random value 생성 > 각자의 범위에 해당하는 값을 가진 선수가 승리
						int team_stat = ((Integer) reciving_player_list.get(team_idx).get("struggle")).intValue();
						int enemy_stat = ((Integer) competition_player.get(enemy_idx).get("struggle")).intValue();
						team_stat += ((Integer) reciving_player_list.get(team_idx).get("positioning")).intValue();
						enemy_stat += ((Integer) competition_player.get(enemy_idx).get("positioning")).intValue();
						team_stat += ((Integer) reciving_player_list.get(team_idx).get("controll")).intValue();
						enemy_stat += ((Integer) competition_player.get(enemy_idx).get("controll")).intValue();
						team_stat += ((Integer) reciving_player_list.get(team_idx).get("height")).intValue();
						enemy_stat += ((Integer) competition_player.get(enemy_idx).get("height")).intValue();
						team_stat += ((Integer) reciving_player_list.get(team_idx).get("weight")).intValue();
						enemy_stat += ((Integer) competition_player.get(enemy_idx).get("weight")).intValue();
						
						int winner = generator.nextInt(team_stat + enemy_stat);
						if(winner < team_stat)
							player_key = (String) reciving_player_list.get(team_idx).get("player_key");
						else
							player_key = (String) competition_player.get(enemy_idx).get("player_key");
					}
				}
			}
			
			if(goal) {
				// Goal을 넣은 경우
				own_keep = true;
				// 볼 소유가 바뀌지만 성공한 것을 표현하기 위해 true로 지정 
				
				// Kick Off 상황으로 좌표 초기화
				// 적군의 선수 중 가장 priority가 높은 선수가 Kick Off 자리에 위치
				for(int i = 0;i < basic_coord.size();i++) {
					String temp_player_key = (String) basic_coord.get(i).get("player_key");
					Boolean val = gameService.checkOwnKeep(temp_player_key, game_key);
					if(!val) {
						Object temp_coord_x = basic_coord.get(i).get("coord_x");
						Object temp_coord_y = basic_coord.get(i).get("coord_y");
						basic_coord.get(i).put("coord_x", kickOffCoord.get("coord_x"));
						basic_coord.get(i).put("coord_y", kickOffCoord.get("coord_y"));
						// Kick Off 상황의 위치 저장
						gameService.updateBasicCoord(basic_coord);
						// 중앙에 위치한 선수 다시 포메이션에 맞는 위치로 변경
						basic_coord.get(i).put("coord_x", temp_coord_x);
						basic_coord.get(i).put("coord_y", temp_coord_y);
						player_key = temp_player_key;
						break;
					}
				}
			}
			else {
				// 볼을 소유한 선수를 end_coord 위치로 이동
				if(end_coord.get("coord_x") != null && end_coord.get("coord_y") != null)
					gameService.updateCoord(player_key, game_key, end_coord);
				// own 이 유지되는지 확인
				own_keep = gameService.checkOwnKeep(player_key, game_key);
				if(player_key != null && !player_key.equals(""))	participate_player_key.add(player_key);
				if(last_touch != null && !last_touch.equals(""))	participate_player_key.add(last_touch);
			}
	
			// 공을 소유하던 player own 제거 & 공을 소유한 player에게 own 부여
			gameService.updateOwnBall(player_key, game_key);
			gameService.recordAction(game_key, own_player_key, act, detail_action, own_keep);
			gameService.recordGameLog(game_key, own_player_key, player_key, intercept, act, detail_action, own_keep, goal, loose_ball, end_coord, time);
			
			/*
			 * 공을 소유한 선수와 관계된 선수 들의 action 정의
			 */
			/*
			
			// 공과 상관없는 선수 움직임
			List<Map<String, Object>> move_player = gameService.getNotParticipatePlayerList(game_key, participate_player_key);
			Map<String, Object> ball_coord = gameService.getPlayerCoord(player_key);
			Map<String, Object> move_coord = new HashMap<String, Object>();
			Map<String, Object> move_section = new HashMap<String, Object>();
			//Map<String, Object> datum_point = new HashMap<String, Object>();
			String defense_end_line_valid = "ST / RS / LS / CF / RCF / "
										  + "LCF / RW / LW / CAM / RCAM / "
										  + "LCAM / CM / RCM / LCM / CDM / "
										  + "RCDM / LCDM / RM / LM / RB / "
										  + "RWB / LB / LWB";
			String offense_end_line_valid = "CB / RCB / LCB / SW / RB / "
										  + "RWB / LB / LWB";
			String center_position_valid = "ST / RS / LS / CF / RCF / "
										 + "LCF / CAM / RCAM / LCAM / CM / "
										 + "RCM / LCM / CDM / RCDM / LCDM / "
										 + "CB / RCB / LCB / SW";
			String side_position_valid = "RW / LW / RM / LM / RB / "
									   + "RWB / LB / LWB";
			
			String move_player_position = "";
			String move_player_key = "";
			String move_player_camp = "";
			int move_player_coord_x = 0;
			int move_player_coord_y = 0;
			int move_player_speed = 0;
			int move_player_acc = 0;
			int datum_coord_x = -1;
			int coord_x_gap = 0;
			int coord_y_gap = 0;
			int state_label = 0;
	
			int origin_ball_coord_x = ((Integer) ball_coord.get("coord_x")).intValue();
			int origin_ball_coord_y = ((Integer) ball_coord.get("coord_y")).intValue();
			int camp_validation = -1;
			if(origin_ball_coord_x <= 25)
				camp_validation = 0;
			else
				camp_validation = 1;
			// camp_validation이 0이면 camp가 team일 떄 아군 진영, enemy일 떄 적군 진영
			// camp_validation이 1이면 camp가 team일 때 적군 진영, enemy일 때 아군 진영
			
			for(int i = 0;i < move_player.size();i++) {
				// 선수의 속도, 가속도에 따라 이동 가능한 칸 수 결정
				// 이동할 좌표에 이미 선수가 있는 경우 y좌표를 중심과 가까워지도록 + or -
				
				// 공의 위치(end_coord) / 포지션에 따른 중심점, 이동범위 계산
				// y 좌표		> 사이드	포지션의 경우 해당 사이드의 패널티 에어리어 바깥쪽
				// 			> 중앙 	포지션의 경우 패널티 에어리어 안쪽
	
				// 공의 위치가 아군 진영 or 상대 진영 중심에 위치할 떄 x좌표 범위 설정
				// GK				> 패널티 에어리어 내
				
				// CB, SW, RCB, LCB	> 공격 - 아군 진영에서는 페널티 에어리어 앞부터		하프라인 5칸 뒤까지				, 상대 진영에서는 하프라인 5칸 뒤부터						하프라인 3칸 앞까지
				//					> 수비 - 아군 진영에서는 골라인부터				상대 최전방 공격수 x좌표 1칸 뒤까지	, 상대 진영에서는 페널티 에어리어 앞부터					하프라인 5칸 뒤까지
				
				// RB, LB, RWB, LWB	> 공격 - 아군 진영에서는 페널티 에어리어 앞부터		하프라인 까지					, 상대 진영에서는 하프라인부터							상대 최종 수비라인 1칸 뒤까지
				//					> 수비 - 아군 진영에서는 골라인부터				상대 최전방 공격수 x좌표 1칸 뒤까지	, 상대 진영에서는 페널티 에어리어 앞부터					하프라인까지
				
				// CDM, RCDM, LCDM	> 공격 - 아군 진영에서는 공위치 x좌표부터			하프라인 5칸 앞까지				, 상대 진영에서는 하프라인 3칸 앞부터						상대 최종 수비라인 5칸 뒤까지
				//					> 수비 - 아군 진영에서는 페널티 에어리어 절반부터		하프라인 5칸 뒤까지				, 상대 진영에서는 페널티 에어리어 앞부터					하프라인까지
				
				// CM, RCM, LCM		> 공격 - 아군 진영에서는 경기장 1/4 지점 1칸 앞부터	하프라인 7칸 앞까지				, 상대 진영에서는 하프라인 5칸 앞부터						상대 최종 수비라인 3칸 뒤까지 
				//					> 수비 - 아군 진영에서는 패널티 에어리어 앞부터		경기장 1/4 지점까지				, 상대 진영에서는 하프라인 15칸 뒤부터 					하프라인까지
				
				// CAM, RCAM, LCAM	> 공격 - 아군 진영에서는 하프라인 5칸 뒤부터		상대 최종 수비라인과 하프라인 사이까지	, 상대 진영에서는 하프라인과 상대 최종 수비라인 기준 1/4지점부터 	3/4지점까지
				//					> 수비 - 아군 진영에서는 페널티 에어리어 앞부터		하프라인 뒤 5칸 까지				, 상대 진영에서는 하프라인 뒤 5칸부터 						하프라인 앞 5칸까지
				
				// LM, RM			> 공격 - 아군 진영에서는 경기장 1/4 지점부터 		경기장 3/4지점까지				, 상대 진영에서는 하프라인 기준 1/4지점부터 				상대 최종 수비라인 1칸 뒤까지
				//					> 수비 - 아군 진영에서는 페널티 에어리어 절반부터 	하프라인까지					, 상대 진영에서는 경기장 1/4지점부터 						경기장 3/4지점까지
				
				// ST, RS, LS		> 공격 - 아군 진영에서는 하프라인 1칸 뒤부터 		상대 최종 수비라인 1칸 앞까지		, 상대 진영에서는 패널티 에어리어 2칸 뒤부터 				상대 최종 수비라인 1칸 뒤까지
				//					> 수비 - 아군 진영에서는 하프라인 2칸 뒤부터 		하프라인까지					, 상대 진영에서는 하프라인부터 							경기장 3/4 지점까지
				
				// CF, RCF, LCF		> 공격 - 아군 진영에서는 하프라인 1칸 뒤부터 		상대 최종 수비라인 1칸 앞까지		, 상대 진영에서는 패널티 에어리어 4칸 뒤부터 				상대 최종 수비라인 1칸 뒤까지
				//					> 수비 - 아군 진영에서는 하프라인 4칸 뒤부터 		하프라인까지					, 상대 진영에서는 하프라인부터 							경기장 3/4 지점까지
				
				// RW, LW			> 공격 - 아군 진영에서는 하프라인 4칸 뒤부터		경기장 3/4 지점까지				, 상대 진영에서는 3/4 지점 1칸 앞부터						상대 최종 수비라인 1칸 뒤까지
				//					> 수비 - 아군 진영에서는 패널티 에어리어 앞부터		하프라인까지					, 상대진영에서는 하프라인 10칸 뒤부터 						경기장 3/4 지점까지
	
				// 필요한 함수 	: 	최종 수비라인 / 최전방 공격수 x좌표 가져오는 함수 > parameter = game_key, camp 
				//			:	범위 설정 함수 > parameter = end_coord(공 좌표), game_key, (최종 수비라인 좌표, 최전방 공격수 좌표)
	
				// 최종 수비라인 좌표 필요 : ST(공), CF(공), RW(공), CAM(공), CM(공), CDM(공), LM(공), RB(공) 
				// 최전방 공격수 좌표 필요 : CB(수), RB(수)
	
				int ball_coord_x = origin_ball_coord_x;
				int ball_coord_y = origin_ball_coord_x;
				move_player_position = (String) move_player.get(i).get("select_position");
				move_player_key = (String) move_player.get(i).get("player_key");
				move_player_camp = (String) move_player.get(i).get("camp");
				move_player_speed = ((Integer) move_player.get(i).get("speed")).intValue();
				move_player_acc = ((Integer) move_player.get(i).get("acc")).intValue();
				move_player_coord_x = ((Integer) move_player.get(i).get("coord_x")).intValue();
				move_player_coord_y = ((Integer) move_player.get(i).get("coord_y")).intValue();
				
				int temp_coord_x = 0;
				int temp_coord_y = 18;
	
				// camp_validation이 0이면 camp가 team일 떄 아군 진영, enemy일 떄 적군 진영
				// camp_validation이 1이면 camp가 team일 때 적군 진영, enemy일 때 아군 진영
				if(move_player_camp.equals("team")) {
					temp_coord_x = 13 + camp_validation * 25;
					state_label = 1 + camp_validation;
					if(defense_end_line_valid.contains(move_player_position)) {
						// 최종 수비라인 가져오기
						datum_coord_x = gameService.getLastLineCoord("D", game_key, move_player_key);
					}
				}
				else if(move_player_camp.equals("enemy")) {
					temp_coord_x = 38 - camp_validation * 25;
					ball_coord_x = 50 - ball_coord_x;
					ball_coord_y = 37 - ball_coord_y;
					state_label = 4 - camp_validation;
					if(offense_end_line_valid.contains(move_player_position)) {
						// 최전방 공격수 좌표 가져오기
						datum_coord_x = gameService.getLastLineCoord("A", game_key, move_player_key);
					}
				}
				coord_x_gap = temp_coord_x - ball_coord_x;
				coord_y_gap = temp_coord_y - ball_coord_y;
	
				log.debug("camp		 	 > " + move_player_camp);
				log.debug("coord gap 	 > (" + coord_x_gap + ", " + coord_y_gap + ")");
				log.debug("ball coord 	 > (" + ball_coord_x + ", " + ball_coord_y + ")");
				log.debug("temp coord 	 > (" + temp_coord_x + ", " + temp_coord_y + ")");
				log.debug("datum coord x > " + datum_coord_x);
				log.debug("state label 	 > " + state_label);
				log.debug("move player 	 > " + i);
				
				move_section = gameService.getMoveSection(move_player_position, coord_x_gap, coord_y_gap, state_label, datum_coord_x, ball_coord_x);
				move_coord = gameService.calculateMoveCoord(game_key, move_section, move_player.get(i));
				gameService.updateNotParticipatePlayerCoord(game_key, move_coord, move_player_key);
			}
			*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	// 공을 가진 선수의 stat
	}
}