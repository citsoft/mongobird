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
