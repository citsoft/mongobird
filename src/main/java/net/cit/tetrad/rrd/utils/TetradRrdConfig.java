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

import java.util.Locale;
import java.util.ResourceBundle;

public class TetradRrdConfig {
	
	static ResourceBundle resourceBundle = ResourceBundle.getBundle("tetrad_rrd", Locale.getDefault());
	static ResourceBundle monadResourceBundle = ResourceBundle.getBundle("monad", Locale.getDefault());
	
	public static String getTetradRrdConfig(String value) {
		return resourceBundle.getString(value);
	}
	
	public static String getMonadConfig(String value) {
		return monadResourceBundle.getString(value);
	}
	
}
