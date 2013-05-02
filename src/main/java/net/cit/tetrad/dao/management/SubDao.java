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

import net.cit.tetrad.model.CommonDto;
import net.cit.tetrad.model.GraphDto;
import net.cit.tetrad.rrd.bean.DbStatus;
import net.cit.tetrad.rrd.bean.GraphDefInfo;

public interface SubDao {
	public void convertValue(GraphDto graphDto);
	public GraphDefInfo getGraphDefInfoForSubGraph(CommonDto dto);
	public GraphDefInfo getNoDeviceGraphDefInfoForSubGraph(CommonDto dto);
	Object getIncludeOptionResult(CommonDto dto);
	Object getAccumResult(CommonDto dto);
	Object getCurrentResult(CommonDto dto);
	public long setStep(String stepStr);
	public GraphDefInfo getLockGraphDefInfoForSubGraph(CommonDto dto);
	public GraphDefInfo getDbGraphDefInfoForSubGraph(CommonDto dto);
	public List<DbStatus> dbLstNan(int deviceCode);
}
