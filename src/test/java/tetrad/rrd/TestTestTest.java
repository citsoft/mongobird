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

import java.util.Random;

import org.rrd4j.core.Util;

import net.cit.tetrad.rrd.utils.TimestampUtil;

public class TestTestTest {
	static final long SEED = 1909752002L;
	  static final Random RANDOM = new Random(SEED);
	static final long START = Util.getTimestamp(2003, 4, 1);
	static final long END = Util.getTimestamp(2003, 5, 1);
	 static final int MAX_STEP = 300;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String str = "1337150280, 1337150290, 1337150300, 1337150310, 1337150320, 1337150330, 1337150340";
		String[] arr = str.split(",");
		
		for (String s : arr) {
			long lo = Long.parseLong(s.trim());
			String stamp =	TimestampUtil.convTimestampToString(lo, "yyyyMMdd hh:mm:ss");
			System.out.println(stamp);
		}
		
		 long t = START;
		 while (t <= END + 86400L) {
			 System.out.println(t);
			 t += RANDOM.nextDouble() * MAX_STEP + 1;
		 }
	}

}
