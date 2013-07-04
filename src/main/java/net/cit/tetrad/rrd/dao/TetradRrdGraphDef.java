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
