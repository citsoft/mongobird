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

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("xstream.datasourceNames")
public class StatusDatasourceName {

	private String dsName;
	private String dsType;
	private String calculateType;
	private String yunit;
	
	public String getDsName() {
		return dsName;
	}
	public void setDsName(String dsName) {
		this.dsName = dsName;
	}
	public String getDsType() {
		return dsType;
	}
	public void setDsType(String dsType) {
		this.dsType = dsType;
	}
	public String getCalculateType() {
		return calculateType;
	}
	public void setCalculateType(String calculateType) {
		this.calculateType = calculateType;
	}
	public String getYunit() {
		return yunit;
	}
	public void setYunit(String yunit) {
		this.yunit = yunit;
	}
	
}
