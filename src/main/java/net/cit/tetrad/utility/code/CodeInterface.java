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
package net.cit.tetrad.utility.code;

import java.util.Locale;
import java.util.Map;

import net.sf.json.JSONObject;

public interface CodeInterface {

	public void updateCode();
	public void updateCode(Locale locale);
	public <K, V> Map<K, V> getCodeMap();
	public JSONObject getCodeJson();
	public String getName(String code);
	public String getName(int code);
	public int getCode(String name);
	
}
