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

import net.cit.monad.Operations;
import net.cit.tetrad.common.DateUtil;
import net.cit.tetrad.model.User;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MakeUser {

	public static void main(String[] args) {
		String[] configLocations = new String[]{"applicationContext_rrd.xml"};
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(configLocations);
		
		Operations operation= (Operations)context.getBean("operations");
		
		User user = new User();
		user.setUid("user1");
		user.setIdx(1);
		user.setReg_date(DateUtil.getCurrentDate("yyyy-MM-dd hh:mm:ss"));
		user.setPasswd("0a041b9462caa4a31bac3567e0b6e6fd9100787db2ab433d96f6d178cabfce90");
		user.setUsername("김씨");
		user.setAuthority(1);
		
		operation.insert(user);
	}
}
