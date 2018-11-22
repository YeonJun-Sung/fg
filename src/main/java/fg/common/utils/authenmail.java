package fg.common.utils;
//이메일을 보내는 메일 주소의 정보에 관한 객체입니다. 
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class authenmail extends Authenticator {
	PasswordAuthentication pa;

	public authenmail() {
		//이메일을 보내는 이메일의 id와 pw를 정확하게 입력하지 않으면 오류가 발생할 수 있습니다.
		String id = "zxcas319@gmail.com";
		String pw = "52749720kdg";
		
		pa = new PasswordAuthentication(id, pw);
	}

	public PasswordAuthentication getPasswordAuthentication() {
		return pa;
	}
}
