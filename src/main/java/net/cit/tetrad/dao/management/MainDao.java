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
