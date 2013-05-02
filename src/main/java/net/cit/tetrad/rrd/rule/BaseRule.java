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
package net.cit.tetrad.rrd.rule;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Field;

import com.thoughtworks.xstream.XStream;

public class BaseRule implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * XML 문자열로 변환
	 */
	protected String toXML() {
		XStream xstream = new XStream();
		xstream.processAnnotations(getClass());
		Field[] fields = getClass().getDeclaredFields();
		for (Field f : fields) {
			xstream.processAnnotations(f.getType());
		}
		String xmlString = xstream.toXML(this);
		xstream = null;
		return xmlString;
	}
	
	/**
	 * XML 데이터로부터 Rule Build
	 */
	public static XMLRule fromXML(String xmlString, Class clazz) {
		XStream xstream = new XStream();
		xstream.processAnnotations(clazz);
		Field[] fields = clazz.getDeclaredFields();
		for (Field f : fields) {
			xstream.processAnnotations(f.getType());
		}
		XMLRule rule = (XMLRule)xstream.fromXML(xmlString);
		xstream = null;
		return rule;
	}
	
	/**
	 * XML 데이터로부터 Rule Build
	 */
	public static XMLRule fromXML(File file, Class clazz) {
		XStream xstream = new XStream();
		xstream.processAnnotations(clazz);
		Field[] fields = clazz.getDeclaredFields();
		for (Field f : fields) {
			xstream.processAnnotations(f.getType());
		}
		XMLRule rule = (XMLRule)xstream.fromXML(file);
		xstream = null;
		return rule;
	}
}
