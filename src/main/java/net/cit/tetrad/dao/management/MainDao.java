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
package net.cit.tetrad.dao.management;

import java.util.List;
import net.cit.tetrad.model.Global;
import net.cit.tetrad.rrd.bean.GraphDefInfo;

public interface MainDao {

	public String[] setFilters(String type, String period, String dsType);
	long getSubGraphSearchTime(String period, String date, String hour, String min);
	String[] setGraphLegend(String type);
	public Global globalValue(int i);
	public String graphGubun();
	public GraphDefInfo setGraphDefInfo(GraphDefInfo gdInfo, String grphPeriod, String[] graphLegend, String consolFun, long step);
	public int mainRefreshPeriodMinute();
	public String getGlobalMongoVersion();
	public String getDsType(String dsType);
	public String[] setDbFilters(String type, String period);
	public String[] setDbGraphLegend(String type);
	
	public void checkExsitEssentialGlobalVariable();
	
	List<String> getExsitGlobalVariable(List<String> essentialGlobalVariableList);
	
	List<String> getEssentialGlobalVariable();
	
	public void registerEssentialGlobalVariable(List<String> essentialList, List<String> exsitList);
	
	Global setEssentialGlobal(String variable, String value);
	public String getGlobalHostname();
	public int getLogRetentionPeriod();
}
