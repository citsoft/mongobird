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

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

import net.cit.tetrad.rrd.bean.ServerStatus;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class ReflectTest {

	/**
	 * @param args
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws InvocationTargetException 
	 */
	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		// TODO Auto-generated method stub	
		ServerStatus serverstatus = new ServerStatus();
		serverstatus.setDeviceCode(12);
		serverstatus.setType("a");
		
		BeanWrapper wrapper = new BeanWrapperImpl(serverstatus);
		
		PropertyDescriptor[] ps = wrapper.getPropertyDescriptors();
		
		for (PropertyDescriptor p : ps) {
			String s = p.getName();
			System.out.println(s + " : " + wrapper.getPropertyValue(s));
			
		}
		
//		Field[] fields = serverstatus.getClass().getDeclaredFields();
//		Method[] methods = serverstatus.getClass().getMethods();
//		
//		int idex = 0;
//		for (Field field : fields) {
//			System.out.println("name : " + field.getName());
//			System.out.println("name : " + field.getType());
//			Method method = methods[idex];
//			if (method.getReturnType() == String.class) {
//				System.out.println(method.invoke(method.getName(), ""));
//			}
					
					
//			Object objValue = field.get(Object.class);
//			if (objValue instanceof java.lang.String) {
//				System.out.println(objValue.toString());
//			} else if (objValue instanceof java.lang.Integer) {
//				System.out.println(((java.lang.Integer) objValue).intValue());
//			} else if (objValue instanceof java.lang.Double) {
//				System.out.println(((java.lang.Double) objValue).doubleValue());
//			} else if (objValue instanceof java.lang.Float) {
//				System.out.println(((java.lang.Float) objValue).floatValue());
//			} else {
//				System.out.println(objValue);
//			}
//			idex++;
//		}
	}

}
