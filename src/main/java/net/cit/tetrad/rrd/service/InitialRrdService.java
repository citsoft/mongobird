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
package net.cit.tetrad.rrd.service;

import static net.cit.tetrad.common.ColumnConstent.RRD_STEP_30MINUTE;
import static net.cit.tetrad.common.ColumnConstent.RRD_STEP_5MINUTE;
import static net.cit.tetrad.common.ColumnConstent.RRD_STEP_HOUR;
import static net.cit.tetrad.common.ColumnConstent.RRD_STEP_MINUTE;
import static net.cit.tetrad.common.ColumnConstent.RRD_STEP_SECOND;
import static net.cit.tetrad.common.ColumnConstent.SERVERSTATUS_REGTIME;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.cit.tetrad.common.DateUtil;
import net.cit.tetrad.model.Device;
import net.cit.tetrad.rrd.rule.DbStatusRule;
import net.cit.tetrad.rrd.rule.ServerStatusRule;
import net.cit.tetrad.rrd.rule.StatusDatasourceName;
import net.cit.tetrad.rrd.rule.TotalStatusRule;
import net.cit.tetrad.rrd.utils.RrdUtil;
import net.cit.tetrad.rrd.utils.StringUtil;
import net.cit.tetrad.rrd.utils.TetradRrdDbPool;
import net.cit.tetrad.utility.CommonUtils;

import org.apache.log4j.Logger;
import org.rrd4j.ConsolFun;
import org.rrd4j.DsType;
import org.rrd4j.core.RrdDb;
import org.rrd4j.core.RrdDef;
import org.rrd4j.core.Sample;
import org.rrd4j.core.Util;

public class InitialRrdService {
	private Logger logger = Logger.getLogger("process.rrd");
	
	private String rrdPath;
	private Map<String, Object> dataValues;
	private long inputTimeStamp = 0;
		
	InitialRrdService() {
	}
	
	InitialRrdService(String rrdPath) {
		this.rrdPath = rrdPath;
	}

	public Map<String, Object> getDataValues() {
		return dataValues;
	}

	public void setDataValues(Map<String, Object> dataValues) {
		this.dataValues = dataValues;

		try {
			String regtime = (String) dataValues.get(SERVERSTATUS_REGTIME);
			if (regtime == null) {
				inputTimeStamp = Util.getTimestamp();
			} else {
				Date date = DateUtil.dateformat(regtime, "yyyyMMddHHmmssSSS");
				inputTimeStamp = Util.getTimestamp(date);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	public void initialRrd(String dsName, String dstype) throws IOException {
		// 시작 시각 취득
		long startTime = Util.getTimestamp();

		// 생성할 row 갯수 취득
		int intervalTime = RrdUtil.readLogGenerationInterval();

		RrdDef rrdDef = new RrdDef(rrdPath, startTime - 1, intervalTime);

		DsType dsType = DsType.valueOf(dstype);
		rrdDef.addDatasource(StringUtil.rightSubstring(dsName, 20), dsType, intervalTime + 2, 0, Double.NaN);
		rrdDef.addArchive(ConsolFun.LAST, 0.5, RRD_STEP_SECOND, RrdUtil.calculateCreateRowCnt(360));
		rrdDef.addArchive(ConsolFun.LAST, 0.5, RRD_STEP_MINUTE, RrdUtil.calculateCreateRowCnt(60));
		rrdDef.addArchive(ConsolFun.LAST, 0.5, RRD_STEP_5MINUTE, RrdUtil.calculateCreateRowCnt(12));
		rrdDef.addArchive(ConsolFun.LAST, 0.5, RRD_STEP_30MINUTE, RrdUtil.calculateCreateRowCnt(2));
		rrdDef.addArchive(ConsolFun.LAST, 0.5, RRD_STEP_HOUR, RrdUtil.calculateCreateRowCnt(1));
		
		rrdDef.addArchive(ConsolFun.AVERAGE, 0.5, RRD_STEP_SECOND, RrdUtil.calculateCreateRowCnt(360));
		rrdDef.addArchive(ConsolFun.AVERAGE, 0.5, RRD_STEP_MINUTE, RrdUtil.calculateCreateRowCnt(60));
		rrdDef.addArchive(ConsolFun.AVERAGE, 0.5, RRD_STEP_5MINUTE, RrdUtil.calculateCreateRowCnt(12));
		rrdDef.addArchive(ConsolFun.AVERAGE, 0.5, RRD_STEP_30MINUTE, RrdUtil.calculateCreateRowCnt(2));
		rrdDef.addArchive(ConsolFun.AVERAGE, 0.5, RRD_STEP_HOUR, RrdUtil.calculateCreateRowCnt(1));
		
		rrdDef.addArchive(ConsolFun.TOTAL, 0.5, RRD_STEP_SECOND, RrdUtil.calculateCreateRowCnt(360));
		rrdDef.addArchive(ConsolFun.TOTAL, 0.5, RRD_STEP_MINUTE, RrdUtil.calculateCreateRowCnt(60));
		rrdDef.addArchive(ConsolFun.TOTAL, 0.5, RRD_STEP_5MINUTE, RrdUtil.calculateCreateRowCnt(12));
		rrdDef.addArchive(ConsolFun.TOTAL, 0.5, RRD_STEP_30MINUTE, RrdUtil.calculateCreateRowCnt(2));
		rrdDef.addArchive(ConsolFun.TOTAL, 0.5, RRD_STEP_HOUR, RrdUtil.calculateCreateRowCnt(1));

		// RrdDb를 생성한다.
		RrdDb rrdDb = new RrdDb(rrdDef);
		rrdDb.close();
		logger.info(rrdPath + " Tetrad Rrd Db created!!");
	}

	public void executeDbStatus(Device device, String database) throws Exception {
		try {
			List<StatusDatasourceName> dbStatusDataSource 
												= new DbStatusRule().dbStatusDatasourceNameXMLToObject();

			for (StatusDatasourceName name : dbStatusDataSource) {
				String key = name.getDsName();
				String value = "0";

				if (dataValues.containsKey(key)) {
					value = dataValues.get(key).toString();
				}

				this.rrdPath = CommonUtils.getRrdDbPath(device, database, key);
				String dsName = StringUtil.rightSubstring(key, 20);
				execute(dsName, value);
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	public void executeServerStatus(Device device) {
		try {
			List<StatusDatasourceName> serverStausDataSource 
			= new ServerStatusRule().serverStatusDatasourceNameXMLToObject();

			for (StatusDatasourceName name : serverStausDataSource) {
				String key = name.getDsName();
				String value = "0";

				if (dataValues.containsKey(key)) {
					value = dataValues.get(key).toString();
				}

				this.rrdPath = CommonUtils.getRrdDbPath(device, key);
				String dsName = StringUtil.rightSubstring(key, 20);
				execute(dsName, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void executeTotal() {
		try {
			List<StatusDatasourceName> totalDatasourceNames 
			= new TotalStatusRule().totalStatusDatasourceNameXMLToObject();
			
			for (StatusDatasourceName dsInfo : totalDatasourceNames) {
				String key = dsInfo.getDsName();
				
				String value = dataValues.get(key).toString();
				this.rrdPath = CommonUtils.getRrdDbPath(key);
				String dsName = StringUtil.rightSubstring(key, 20);
				execute(dsName, value);				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void execute (String dsName, String value) throws Exception {
		RrdDb rrdDb = TetradRrdDbPool.getRrdDb(rrdPath);
		if (rrdDb.containsDs(dsName)) {
//			long t = Util.getTimestamp();

			Sample sample = rrdDb.createSample();
			sample.setTime(inputTimeStamp);
			sample.setValue(dsName, Double.parseDouble(value));
			sample.update();			
		} else {
			logger.info("FAIL!!!");
			logger.info("\t execute rrd : " + rrdPath);
			logger.info("\t dsName : " + dsName);
		}
	}	

}
