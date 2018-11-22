package fg.create.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import fg.create.dao.CreateDAO;
import fg.vo.UserVO;

@Service("createService")
public class CreateServiceImpl implements CreateService{
	@Resource(name="createDAO")
    private CreateDAO createDAO;

    Logger log = Logger.getLogger(this.getClass());
    
	@Override
	public Map<String, Object> makePercent(int rating) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rating", rating);
		Map<String, Object> per = createDAO.getMakePercent(map);
		
		return per;
	}

	@Override
	public void makePlayer(int position, int rating, String user_key, Map<String, Object> per) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rating", rating);
		//rating의 이름을 가져온다.
		String rating_name = (String) per.get("name");
		//나이,키,몸무게,주로 쓰는 발 랜덤 생성
		Random generator = new Random();
		int age = generator.nextInt(15) + 19;
		int height = generator.nextInt(35) + 165;
		int foot = generator.nextInt(10);
		int weight = (int) ((height - 100) * (generator.nextInt(50) + 75) / 100);
		map.put("age", age);
		map.put("height", height);
		map.put("weight", weight);
		
		if(foot <= 6)
			map.put("foot", "R");
		else
			map.put("foot", "L");
		
		int tend = 0;
		if(position == 0) {
			map.put("position","GK");
		}
		else if(position == 1) {
			map.put("position","DF");
			tend = generator.nextInt(2);
		}
		else if(position == 2) {
			map.put("position","MF");
			tend = generator.nextInt(4);
		}
		else if(position == 3) {
			map.put("position","FW");
			tend = generator.nextInt(2);
		}
		
		//기본으로 설정되는 선수 이름
		String po = (String) map.get("position");
		map.put("name", rating_name + " " + po + " player");
		//성향을 문자로 바꾸어 넣어준다.
		po = String.valueOf(tend) + "_" + po;
		map.put("position_detail",po);
		
		//상세 포지션에 맞는 선수 스텟 가져오기
		Map<String, Integer> stat_tend = createDAO.getPlayerTendency(po);
		
		//선수 등급을 확률에 맞게 생성
		int[] grade = new int[4];
		grade[0] = (Integer) per.get("S_grade");
		grade[1] = (Integer) per.get("A_grade") + grade[0];
		grade[2] = (Integer) per.get("B_grade") + grade[1];
		grade[3] = (Integer) per.get("C_grade") + grade[2];
		map.put("user_key", user_key);
		int rand = generator.nextInt(grade[3]) + 1;
		int[] stat = new int[23];
		int base_stat = 0;
		if(rand <= grade[0]) {
			base_stat = 60;
			map.put("grade", "S");
		}
		else if(rand <= grade[1]) {
			base_stat = 40;
			map.put("grade", "A");
		}
		else if(rand <= grade[2]) {
			base_stat = 30;
			map.put("grade", "B");
		}
		else if(rand <= grade[3]) {
			base_stat = 20;
			map.put("grade", "C");
		}
		
		//기본 선수 stat 랜덤 설정
		int stat_tend_tmp = 0;
		for(int i = 0;i < stat.length;i++) {
			stat_tend_tmp = (int) stat_tend.get("stat" + String.valueOf(i + 1));
			if(stat_tend_tmp >= 150)
				stat[i] = (int) ((base_stat + generator.nextInt(15)) * stat_tend_tmp / 100);
			else if(stat_tend_tmp >= 130)
				stat[i] = (int) ((base_stat + generator.nextInt(10)) * stat_tend_tmp / 100);
			else if(stat_tend_tmp >= 100)
				stat[i] = (int) ((base_stat + generator.nextInt(10) - 10) * stat_tend_tmp / 100);
			else
				stat[i] = (int) ((base_stat + generator.nextInt(10) - 20) * stat_tend_tmp / 100);
		}
		map.put("stat", stat);
		
		createDAO.makePlayer(map);
	}

	//선수 정보 모두 가져오기
	@Override
	public List<Map<String, Object>> getPlayerList(String user_key) throws Exception {
		// TODO Auto-generated method stub
		return createDAO.getPlayerList(user_key);
	}
	
	//선수 overall 가져오기
	@Override
	public List<Map<String, Object>> getPlayerOverall(List<Map<String, Object>> list) throws Exception {
		// TODO Auto-generated method stub
		String player_key = "";
		for(int i = 0;i < list.size();i++) {
			player_key = (String) list.get(i).get("player_key");
			Object[] overall_fw = new Object[2];
			Object[] overall_mf = new Object[4];
			Object[] overall_df = new Object[2];
			Object[] overall_gk = new Object[1];
			//기존 선수에 계산된 overall을 저장 
			overall_fw[0] = createDAO.getPlayerOverall("0_FW", player_key).get("average");
			overall_fw[1] = createDAO.getPlayerOverall("1_FW", player_key).get("average");
			overall_mf[0] = createDAO.getPlayerOverall("0_MF", player_key).get("average");
			overall_mf[1] = createDAO.getPlayerOverall("1_MF", player_key).get("average");
			overall_mf[2] = createDAO.getPlayerOverall("2_MF", player_key).get("average");
			overall_mf[3] = createDAO.getPlayerOverall("3_MF", player_key).get("average");
			overall_df[0] = createDAO.getPlayerOverall("0_DF", player_key).get("average");
			overall_df[1] = createDAO.getPlayerOverall("1_DF", player_key).get("average");
			overall_gk[0] = createDAO.getPlayerOverall("0_GK", player_key).get("average");
			
			list.get(i).put("stat_fw", overall_fw);
			list.get(i).put("stat_mf", overall_mf);
			list.get(i).put("stat_df", overall_df);
			list.get(i).put("stat_gk", overall_gk);
		}
		return list;
	}

	@Override
	public void removePlayerList(String user_key) throws Exception {
		// TODO Auto-generated method stub
		createDAO.removePlayerList(user_key);
	}

	//팀 정보 저장 매서드
	@Override
	public void saveTeam(Map<String, Object> param) throws Exception {
		// TODO Auto-generated method stub
		String team_key = createDAO.makeTeam(param);
		param.put("team_key", team_key);
		createDAO.updatePlayers(param);
		createDAO.updateUserInfo(param);
	}

	//선수 이름 변경 매서드
	@Override
	public void editPlayerName(Map<String, Object> param) throws Exception {
		// TODO Auto-generated method stub
		createDAO.editPlayerName(param);
	}
	//유저 정보에 팀키 넣기
	@Override
	public UserVO getUserInfo(String user_key) throws Exception {
		// TODO Auto-generated method stub
		return createDAO.getUserInfo(user_key);
	}

	//등번호 업데이트 매서드
	@Override
	public void editPlayerBackNum(Map<String, Object> param) throws Exception {
		// TODO Auto-generated method stub
		createDAO.editPlayerBackNum(param);
	}
}