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
package net.cit.tetrad.resource;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import net.cit.tetrad.dao.management.MainDao;
import net.cit.tetrad.model.User;

import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.velocity.VelocityEngineUtils;

public class MailResource {
	private Logger log = Logger.getLogger(this.getClass());
	
	private JavaMailSender mailSender; 
	private VelocityEngine velocityEngine;
	
	private MainDao mainDao;
	private String globalHostName="";
	public void setMainDao(MainDao mainDao) {
		this.mainDao = mainDao;
	}
 		
    public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
    
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}
	
	public void sendMail(final String from, final List<User> toList, final String subject, final Map<String, Object> map) throws Exception {
		String[] to = new String[toList.size()]; 
		
		int index = 0;
		for (User user : toList) {
			to[index] = user.getEmail();
			index++;
		}
		 sendMail(from, to, subject, map);
	}

	public void sendMail(final String from, final String to, final String subject, final Map<String, Object> map) throws Exception {
		 sendMail(from, new String[] {to}, subject, map);
	}
	
	public void sendMail(final String from, final String[] to, final String subject, final Map<String, Object> map) throws Exception {
    	log.debug("	Send Email Start " + to);
    	if(globalHostName.isEmpty())globalHostName = mainDao.getGlobalHostname();
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {

        					Locale locale = Locale.getDefault();
        					String vmFileName = "net/cit/tetrad/template/mongofail.vm";
        					String subjectStr = subject;
        					if(locale.toString().equals("ko_KR")||locale.toString().equals("ko")){
	        						vmFileName = "net/cit/tetrad/template/mongofail_ko_KR.vm";
	        						subjectStr = "장애 발생 공지";
			        					}
        					
                String mailBody = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, vmFileName, "UTF-8", map);
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                message.setTo(to);
                message.setFrom(from);
                message.setSubject("[" + globalHostName + "] " + subjectStr);
                message.setText(mailBody, true);
            }
        };
        mailSender.send(preparator);
        log.debug("	End Email Start " + to);
    }

}
