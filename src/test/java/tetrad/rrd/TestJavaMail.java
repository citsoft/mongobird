/*******************************************************************************
 * "mongobird" is released under a dual license model designed to developers 
 * and commercial deployment.
 * 
 * For using OEMs(Original Equipment Manufacturers), ISVs(Independent Software
 * Vendor), ISPs(Internet Service Provider), VARs(Value Added Resellers) 
 * and another distributors, or for using include changed issue
 * (modify / application), it must have to follow the Commercial License policy.
 * To check the Commercial License Policy, you need to contact Cardinal Info.Tech.Co., Ltd.
 * (http://www.citsoft.net)
 *  *
 * If not using Commercial License (Academic research or personal research),
 * it might to be under AGPL policy. To check the contents of the AGPL terms,
 * please see "http://www.gnu.org/licenses/"
 ******************************************************************************/
package tetrad.rrd;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

public class TestJavaMail {
	private Logger log = Logger.getLogger(this.getClass());
	
	private String type = "text/html; charset=UTF-8";
	private Properties props;
	
	public TestJavaMail (Properties props) {
		this.props = props;
	}
	
	public void sendEmail(String from, String to, String subject, String content) {
		log.info("	Send Email Start " + to);
		EmailAuthenticator authenticator = new EmailAuthenticator();		
		try {			
			Session session = Session.getInstance(props, authenticator);
			
			Message msg = new MimeMessage(session);			
			msg.setFrom(new InternetAddress(from));
			
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			
			msg.setSubject(subject);
			msg.setContent(content, type);
			msg.setSentDate(new Date());
			Transport.send(msg);
		} catch (MessagingException e) {
			e.printStackTrace();
			log.info("메일을 발송하는 중 에러가 발생했습니다.");
		}
		log.info("	End Email Start");
	}
	
	class EmailAuthenticator extends Authenticator {         
		private String id;        
		private String pw;          
		
		public EmailAuthenticator(){            
//			this.id = "odeblossom@gmail.com";            
//			this.pw = "0de1717^^";       
			this.id = "wikinoti";            
			this.pw = "tnqkr@12";       
		}                 
		
		public EmailAuthenticator(String id, String pw) {            
			this.id = id;            
			this.pw = pw;        
		}         
		
		protected PasswordAuthentication getPasswordAuthentication() {            
			return new PasswordAuthentication(id, pw);        
		}     
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Properties gmailProps = new Properties();
		gmailProps.put("mail.smtp.starttls.enable", "true");
		gmailProps.put("mail.smtp.host", "smtp.gmail.com");
		gmailProps.put("mail.smtp.auth", "true");
		gmailProps.put("mail.smtp.port", "587");
		
		Properties jamesProps = new Properties();        
		jamesProps.put("mail.smtp.host", "mail.citsoft.net");
		jamesProps.put("mail.smtp.port", "25");
		jamesProps.put("mail.smtp.auth", "true");
		
		String from = "wikinoti@citsoft.net";
		String to = "tomouki@naver.com,";
		String subject = "이메일 발송 테스트";
		String content = "Java Email 발송 테스트입니다.";
		
		TestJavaMail testmail = new TestJavaMail(jamesProps);
		testmail.sendEmail(from, to, subject, content);
		
		System.out.println("done!!");
		
//		Class.forName(className);
	}

}
