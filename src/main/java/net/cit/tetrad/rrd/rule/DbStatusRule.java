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
