/**
*    Copyright (C) 2012 Cardinal Info.Tech.Co.,Ltd.
*
*    This program is free software: you can redistribute it and/or modify
*    it under the terms of the GNU Affero General Public License, version 3,
*    as published by the Free Software Foundation.
*
*    This program is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU Affero General Public License for more details.
*
*    You should have received a copy of the GNU Affero General Public License
*    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
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
