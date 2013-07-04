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
package net.cit.tetrad.common;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.cit.tetrad.model.Critical;
import net.cit.tetrad.rrd.rule.BaseRule;
import net.cit.tetrad.rrd.rule.XMLRule;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("CriticalInfo")
public class CriticalInfo extends BaseRule implements XMLRule {
	
	private static final long serialVersionUID = 1L;
	private URL url = this.getClass().getResource("/properties/critical_status.xml");
	
	@XStreamImplicit(itemFieldName = "critical")
	private List<Critical> criticalList;
	
	public String toXML() {
		return super.toXML();
	}
	
	public List<Critical> getCriticalList() {
		return criticalList;
	}

	public void setCriticalList(List<Critical> criticalList) {
		this.criticalList = criticalList;
	}

	public List<Critical> criticalInfoXMLToList() {		
		List<Critical> criticalList = new ArrayList<Critical>();		
		try {
			CriticalInfo criticalInfo = (CriticalInfo)BaseRule.fromXML(new File(url.getFile()), CriticalInfo.class);
			criticalList = criticalInfo.getCriticalList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return criticalList;
	}
	
	public Map<String, Object> criticalInfoXMLToMap() {		
		List<Critical> criticalList = criticalInfoXMLToList();
		Map<String, Object> criticalMap = new HashMap<String, Object>();
		for (Critical critical : criticalList) {
			criticalMap.put(critical.getType(), critical);
		}
		return criticalMap;
	}
}
