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
package tetrad.rrd.install;
import net.cit.tetrad.rrd.batch.TetradRrdInitializer;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestInstallTotal {

	public static void main(String[] args) throws Exception {
		String[] configLocations = new String[]{"applicationContext_rrd.xml"};
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(configLocations);

		TetradRrdInitializer tetradInitial = (TetradRrdInitializer)context.getBean("tetradRrdInitializer");
		tetradInitial.installTotalRrdDb();
		
		System.out.println("done!!");
	}
}
