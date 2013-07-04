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
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("TotalDatasourceName")
public class TotalStatusRule extends BaseRule implements XMLRule {

	private static final long serialVersionUID = 1L;
	private URL url = this.getClass().getResource("/rrd/datasource/total_datasource_name.xml");
	
	@XStreamImplicit(itemFieldName = "datasourceNames")
	private List<StatusDatasourceName> datasourceNames;
	
	public String toXML() {
		return super.toXML();
	}

	public List<StatusDatasourceName> getDatasourceNames() {
		return datasourceNames;
	}

	public void setDatasourceNames(List<StatusDatasourceName> datasourceNames) {
		this.datasourceNames = datasourceNames;
	}
	
	public List<StatusDatasourceName> totalStatusDatasourceNameXMLToObject() {
		List<StatusDatasourceName> statusDatasourceName = new ArrayList<StatusDatasourceName>();
		
		try {
			TotalStatusRule serverStatusRule = (TotalStatusRule)BaseRule.fromXML(new File(url.getFile()), TotalStatusRule.class);
			statusDatasourceName = serverStatusRule.getDatasourceNames();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return statusDatasourceName;
	}
	
//	public Map<String, StatusDatasourceName> allStatusDatasourceNameByMap() {
//		List<StatusDatasourceName> statusInfoList = totalStatusDatasourceNameXMLToObject();
//		Map<String, StatusDatasourceName> allStatusMap = new HashMap<String, StatusDatasourceName>();
//		for (StatusDatasourceName statusInfo : statusInfoList) {
//			allStatusMap.put(statusInfo.getDsName(), statusInfo);
//		}
//		
//		return allStatusMap;
//	}
}
