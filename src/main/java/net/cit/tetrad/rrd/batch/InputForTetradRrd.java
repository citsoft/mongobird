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
package net.cit.tetrad.rrd.batch;

import static net.cit.tetrad.common.PropertiesNames.XML_PATH;

import org.apache.log4j.Logger;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class InputForTetradRrd {

	private static Logger logger = Logger.getLogger(InputForTetradRrd.class);
	
	public static void main(String[] args) {
		try {
			logger.debug("InputForTetradRrd Start!!");
			
			// 설정파일로부터 스프링컨테이너 생성
			String[] configLocations = new String[] { XML_PATH + "applicationContext_management.xml", 
													  "applicationContext-mongo.xml", 
													  "applicationContext_rrd.xml" };
			AbstractApplicationContext context = new ClassPathXmlApplicationContext(configLocations);
			
			// TetradRrd에 데이터를 입력하기 위한 인스턴스 생성
			TetradRrdInitializer tetradRrdInitializer = (TetradRrdInitializer)context.getBean("tetradRrdInitializer");
			tetradRrdInitializer.input();
			
			logger.debug("InputForTetradRrd End!!");
		} catch (Exception e) {
			logger.info("----------------------------------------------------------------");
			logger.info(e.getMessage());
			logger.info("----------------------------------------------------------------");
		}
	}

}
