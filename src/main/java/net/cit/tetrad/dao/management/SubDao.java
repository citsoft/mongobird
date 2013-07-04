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
