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
package net.cit.tetrad.rrd.dao;

import net.cit.tetrad.rrd.bean.GraphDefInfo;

import org.rrd4j.graph.RrdGraphDef;

public interface TetradRrdGraphDef {
	
	public RrdGraphDef createRrdDbGraphDef(GraphDefInfo graphDefInfo, String databaseName, String sortItem) throws Exception;
	
	RrdGraphDef createGraphPerRrdDb(String rrdDb, GraphDefInfo graphDefInfo) throws Exception;
	RrdGraphDef createMultiDeviceGraphPerRrd(String rrdDb, GraphDefInfo graphDefInfo) throws Exception;

	RrdGraphDef detailedCreateGraphPerRrdDb(GraphDefInfo graphDefInfo) throws Exception;
	RrdGraphDef detailedCreateMultiDeviceGraphPerRrd(String dsName, GraphDefInfo graphDefInfo, String title) throws Exception;
	public RrdGraphDef createSubMultiDeviceGraphPerRrd(GraphDefInfo graphDefInfo, String sortItem) throws Exception;
	public RrdGraphDef createLockMultiGraphPerRrd(GraphDefInfo graphDefInfo, String sortItem) throws Exception;
	public RrdGraphDef createSoloLockMultiGraphPerRrd(GraphDefInfo graphDefInfo, String sortItem) throws Exception;
	public RrdGraphDef createTotalMultiGraphPerRrd(GraphDefInfo graphDefInfo) throws Exception ;
}
