package fg.transfer.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import fg.create.dao.CreateDAO;
import fg.transfer.dao.TransferDAO;

@Service("transferService")
public class TransferServiceImpl implements TransferService {

	@Resource(name = "transferDAO")
	private TransferDAO transferDAO;

	@Resource(name = "createDAO")
	private CreateDAO createDAO;

    Logger log = Logger.getLogger(this.getClass());

	@Override
	public Map<String, Object> getPlayerInfo(String player_key) throws Exception {
		// TODO Auto-generated method stub
		return transferDAO.getPlayerInfo(player_key);
	}

	@Override
	public Map<String, Object> getRatingInfo(String user_key) throws Exception {
		// TODO Auto-generated method stub
		return transferDAO.getRatingInfo(user_key);
	}

	//등급 확률로 선수 생성 매서드
	@Override
	public List<Map<String, Object>> makePlayer(Map<String, Object> percent, String user_key) throws Exception {
		// TODO Auto-generated method stub
		Random generator = new Random();
		int rand;
		int S = (Integer) percent.get("S_grade");
		int A = (Integer) percent.get("A_grade") + S;
		int B = (Integer) percent.get("B_grade") + A;
		int C = (Integer) percent.get("C_grade") + B;
		int position = 0;
		String rating_name = (String) percent.get("name");

		
		for (int i = 0; i < 10; i++) {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("user_key", user_key);
			if (i == 1 || i == 5 || i == 9)
				position++;

			int tend = 0;
			if (position == 0) {
				param.put("position", "GK");
			} else if (position == 1) {
				param.put("position", "DF");
				tend = generator.nextInt(2);
			} else if (position == 2) {
				param.put("position", "MF");
				tend = generator.nextInt(4);
			} else if (position == 3) {
				param.put("position", "FW");
				tend = generator.nextInt(2);
			}

			rand = generator.nextInt(C) + 1;
			int[] stat = new int[23];
			int base_stat = 0;
			if (rand <= S) {
				base_stat = 60;
				param.put("grade", "S");
			} else if (rand <= A) {
				base_stat = 40;
				param.put("grade", "A");
			} else if (rand <= B) {
				base_stat = 30;
				param.put("grade", "B");
			} else if (rand <= C) {
				base_stat = 20;
				param.put("grade", "C");
			}
			
			//조건에 맞는 선수가 있는지 확인.
			int update_valid = transferDAO.updateTempPlayer(param);
			if (update_valid < 1) {
				String po = (String) param.get("position");
				param.put("name", rating_name + " " + po + " player");
				po = String.valueOf(tend) + "_" + po;
				param.put("position_detail", po);
				Map<String, Integer> stat_tend = createDAO.getPlayerTendency(po);

				int stat_tend_tmp = 0;
				for (int j = 0; j < stat.length; j++) {
					stat_tend_tmp = (int) stat_tend.get("stat" + String.valueOf(j + 1));
					if (stat_tend_tmp >= 150)
						stat[j] = (int) ((base_stat + generator.nextInt(15)) * stat_tend_tmp / 100);
					else if (stat_tend_tmp >= 130)
						stat[j] = (int) ((base_stat + generator.nextInt(10)) * stat_tend_tmp / 100);
					else if (stat_tend_tmp >= 100)
						stat[j] = (int) ((base_stat + generator.nextInt(10) - 10) * stat_tend_tmp / 100);
					else
						stat[j] = (int) ((base_stat + generator.nextInt(10) - 20) * stat_tend_tmp / 100);
				}
				param.put("stat", stat);

				int age = generator.nextInt(15) + 19;
				int height = generator.nextInt(35) + 165;
				int foot = generator.nextInt(10);
				int weight = (int) ((height - 100) * (generator.nextInt(50) + 75) / 100);

				param.put("age", age);
				param.put("height", height);
				param.put("weight", weight);

				if (foot <= 6)
					param.put("foot", "R");
				else
					param.put("foot", "L");
				//한명의 선수를 생성
				transferDAO.makePlayer(param);
			}
		}
		//이적시장에 존재하는 선수들의 정보를 모두 가져온다.
		List<Map<String, Object>> result = transferDAO.getPlayerList(user_key);
		
		String player_key = "";	
		String position_detail = "";
		Object overall = 0;
		for (int i = 0; i < result.size(); i++) {
			//이적시장의 선수들의 오버롤을 구하기 위한 과정
			player_key = (String) result.get(i).get("player_key");
			position_detail = (String) result.get(i).get("position_detail");
			overall = (Object) transferDAO.getPlayerOverall(position_detail, player_key).get("average");

			result.get(i).put("overall", overall);
		}

		return result;
	}

	//3보다 큰 횟수 등장시 드랍
	@Override
	public void dropTempPlayer(int appear) throws Exception {
		// TODO Auto-generated method stub
		transferDAO.dropTempPlayer(appear);
	}
	//이적시장 재 진입시 이전 선수 권한 취소
	@Override
	public void removeTempPlayer() throws Exception {
		// TODO Auto-generated method stub
		transferDAO.removeTempPlayer();
	}
	
	//선수 권한 종료 매서드
	@Override
	public void removeTempUserKey(String user_key) throws Exception {
		// TODO Auto-generated method stub
		transferDAO.removeTempUserKey(user_key);
	}

	@Override
	public void insertTransfer(Map<String, Object> param) throws Exception {
		// TODO Auto-generated method stub
		String player_key = (String) param.get("player_key");
		String team_key = (String) param.get("team_key");
		Map<String, Object> data = transferDAO.selectTransfer(player_key);
		data.put("team_key", team_key);

		transferDAO.insertTransfer(data);
		transferDAO.dropTransfer(player_key);
	}
}
