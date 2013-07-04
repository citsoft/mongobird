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
import java.util.List;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("DbStatusDatasourceName")
public class DbStatusRule extends BaseRule implements XMLRule {

	private static Logger logger = Logger.getLogger(DbStatusRule.class);	
	private static final long serialVersionUID = 1L;
	private URL url = this.getClass().getResource("/rrd/datasource/db_status_datasource_name.xml");
	
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
	
	public List<StatusDatasourceName> dbStatusDatasourceNameXMLToObject() {
		
		List<StatusDatasourceName> dbStatusDatasourceName = new ArrayList<StatusDatasourceName>();
		
		try {
			DbStatusRule dbStatusRule = (DbStatusRule)BaseRule.fromXML(new File(url.getFile()), DbStatusRule.class);
			dbStatusDatasourceName = dbStatusRule.getDatasourceNames();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return dbStatusDatasourceName;
		
	}
	
}
