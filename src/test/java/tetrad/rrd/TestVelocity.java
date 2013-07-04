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

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;

import net.cit.tetrad.resource.MailResource;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.ui.velocity.VelocityEngineUtils;

public class TestVelocity {

	private VelocityEngine velocityEngine;
//	private VelocityEngineFactory velocityEngineFactory;
	
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

//	public void setVelocityEngineFactory(VelocityEngineFactory velocityEngineFactory) {
//		this.velocityEngineFactory = velocityEngineFactory;
//	}

	public String getText() {
		String text = null;
		try {
			text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "test/activate-account.vm", "UTF-8", new HashMap());
		} catch (VelocityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return text;
	}

	public String getText2() {
		Velocity.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, "D:\\Java\\workspace\\tetrad\\src\\main\\webapp\\test");
		Velocity.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
		Velocity.init();
		
		StringWriter sw = new StringWriter();
		VelocityContext context = new VelocityContext();
		context.put("number", 1);
		context.put("math", 2);
		
		Template template = Velocity.getTemplate("activate-account.vm", "UTF-8");
		
		template.merge(context, sw);
		String html = sw.toString();
		
		return html;
	}
	
	public String getText3() {
		String text = null;
		try {
//			text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "mongofail.vm", "UTF-8", new HashMap());
			text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "net/cit/tetrad/template/mongofail.vm", "UTF-8", new HashMap());
		} catch (VelocityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return text;
	}
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		String[] configLocations = new String[]{"applicationContext_rrd.xml"};
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(configLocations);
//		
//		String from = "master@tetrad.citsoft.net";
//		String[] to = {"tomouki@naver.com", "tomouki@hanmail.net", "tomouki@citsoft.net"};
//		String subject = "이메일 발송 테스트";
//		
//		MailResource mailResource = (MailResource)context.getBean("mailResource");
//		mailResource.sendMail(from, to, subject);
		
		TestVelocity testVelocity= (TestVelocity)context.getBean("testVelocity");
		System.out.println(testVelocity.getText3());
	}

}
