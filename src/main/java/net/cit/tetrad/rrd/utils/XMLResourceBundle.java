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
package net.cit.tetrad.rrd.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

public class XMLResourceBundle extends ResourceBundle {
	
	private Properties props;

	XMLResourceBundle(InputStream stream) throws IOException {
		props = new Properties();
		props.loadFromXML(stream);
	}

	protected Object handleGetObject(String key) {
		return props.getProperty(key);
	}

	public Enumeration<String> getKeys() {
		Set<String> handleKeys = props.stringPropertyNames();
		return Collections.enumeration(handleKeys);
	}

}
