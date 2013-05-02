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

import java.awt.Font;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.cit.tetrad.utility.code.Code;
import net.cit.tetrad.common.Utility;
import net.cit.tetrad.model.Device;
import net.cit.tetrad.rrd.bean.GraphDefInfo;
import net.cit.tetrad.rrd.rule.ServerStatusRule;
import net.cit.tetrad.rrd.rule.StatusDatasourceName;
import net.cit.tetrad.rrd.rule.TotalStatusRule;
import net.cit.tetrad.rrd.utils.StringUtil;
import net.cit.tetrad.rrd.utils.TetradRrdConfig;
import net.cit.tetrad.utility.CommonUtils;
import static net.cit.tetrad.common.ColumnConstent.DBLOCKFILEARR;

import org.rrd4j.ConsolFun;
import org.rrd4j.graph.RrdGraphConstants;
import org.rrd4j.graph.RrdGraphDef;

public class TetradRrdGraphDefImpl implements TetradRrdGraphDef {

	private List<StatusDatasourceName> serverDsList = new ServerStatusRule().serverStatusDatasourceNameXMLToObject();
	private Map<String, StatusDatasourceName> serverMapInfo = convertArrayToMap(serverDsList);
	private List<StatusDatasourceName> totalFieldList = new TotalStatusRule().totalStatusDatasourceNameXMLToObject();
	private Map<String, StatusDatasourceName> totalFieldMapInfo = convertArrayToMap(totalFieldList);
	public static final String DBLOCKFILE = "locks_timeLockedMicros_w";
	
	public RrdGraphDef createTotalMultiGraphPerRrd(GraphDefInfo graphDefInfo) throws Exception {

		RrdGraphDef rrdGraphDef = new RrdGraphDef();
		String resultFilename = null;

		try {			
			String strLineWidth = TetradRrdConfig.getTetradRrdConfig("graph_line_width");
			float lineWidth = Float.parseFloat(strLineWidth);
			String[] filters = graphDefInfo.getFilters();
			String[] graphLegend = graphDefInfo.getGraphLegend();
			
			TetradGraphTemplate template = new TetradGraphTemplate(graphDefInfo.getAxisTimeUnitDiv());
			template.setFont(rrdGraphDef);
			
			String datasource = "";
			int size = 0;
			for (int index = 0 ; index < filters.length ; index++){
				String totalFieldStr = filters[index];
				
				datasource = StringUtil.rightSubstring(totalFieldStr, 20);
				String rrdPath = CommonUtils.getDefaultRrdPath() + totalFieldStr + ".rrd";
				String dateName = totalFieldStr+"_"+size;
				
				String cdefDataSource = "";
				rrdGraphDef.datasource(dateName, rrdPath, datasource,  ConsolFun.valueOf(graphDefInfo.getConsolFun()));
				
				StatusDatasourceName totalFiledInfo = totalFieldMapInfo.get(totalFieldStr);
				String totalFiledType = totalFiledInfo.getDsType();
				
				cdefDataSource = template.getDatasourceSettingCDef(rrdGraphDef, dateName, totalFiledType, "globalLock_lockTime");
				
				
				if(graphLegend != null){
					String label = "";
					String formatLabel = "";
					label = template.setTotalLegend(graphLegend[index]);
					formatLabel = Utility.stringFormat(label, 25, 0);

					rrdGraphDef.line(cdefDataSource, TetradGraphTemplate.getColor(index, size), formatLabel, lineWidth);
					
					rrdGraphDef.comment("Max:");
					rrdGraphDef.gprint(cdefDataSource, ConsolFun.MAX, "%5.2f%S");
					rrdGraphDef.comment("Min:");
					rrdGraphDef.gprint(cdefDataSource, ConsolFun.MIN, "%5.2f%S");
					rrdGraphDef.comment("Avg:");
					rrdGraphDef.gprint(cdefDataSource, ConsolFun.AVERAGE, "%5.2f%S");		
					rrdGraphDef.comment("\\l");
					if(filters.length == 2){
						rrdGraphDef.setTitle("Total Locks sum");
					}else {
						String titleStr = "";
						if(totalFieldStr.equals("totalDbLocksTimeLockedMicros_r_sum")){
							titleStr = "Read";
						}else if(totalFieldStr.equals("totalDbLocksTimeLockedMicros_w_sum")){
							titleStr = "Write";
						}
						rrdGraphDef.setTitle("DB Locks Sum" + " - " + titleStr);
					}
				}else{
					rrdGraphDef.line(cdefDataSource, TetradGraphTemplate.getColor(index, size), datasource, lineWidth);
					rrdGraphDef.setNoLegend(true);
				}
				size++;
			}
			
			// 시간 범위 선택
			rrdGraphDef.setStep(graphDefInfo.getStep());
			rrdGraphDef.setTimeSpan(graphDefInfo.getStartTime(), graphDefInfo.getEndTime());

			rrdGraphDef.setFilename(resultFilename);
			rrdGraphDef.setWidth(graphDefInfo.getWidth());
			rrdGraphDef.setHeight(graphDefInfo.getHeight());

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		return rrdGraphDef;
	}
	
	public RrdGraphDef createRrdDbGraphDef(GraphDefInfo graphDefInfo, String databaseName, String sortItem) throws Exception {
		
		RrdGraphDef rrdGraphDef = new RrdGraphDef();
		String rrdPath = null;

		try {			
			String imagePath = CommonUtils.getDefaultRrdImgPath();
			String strLineWidth = TetradRrdConfig.getTetradRrdConfig("graph_line_width");
			float lineWidth = Float.parseFloat(strLineWidth);
			
			File file = new File(imagePath);
			if (!file.exists()) file.mkdirs();

			String[] filters = graphDefInfo.getFilters();
			String[] graphLegend = graphDefInfo.getGraphLegend();
			String dsName = "";
			String datasource = "";
			String cdefDataSource = "";
			String type = graphDefInfo.getFileName();
			String gubun = graphDefInfo.getGubun();
			
			TetradGraphTemplate template = new TetradGraphTemplate(graphDefInfo.getAxisTimeUnitDiv());
			template.setFont(rrdGraphDef);
			template.setdbYlabel(rrdGraphDef);
			 if(gubun.equals("dataSize")) {
					rrdGraphDef.setTitle(databaseName + " DB Data size");
				} else if(gubun.equals("indexSize")) {
					rrdGraphDef.setTitle(databaseName + " DB Index size");
				} 
			
			for (int i = 0 ; i < filters.length ; i++) {
				dsName = filters[i];
//				if (!type.equals("db")) template.setLimitValue(rrdGraphDef, dsName, graphDefInfo);
				
				datasource = StringUtil.rightSubstring(dsName, 20);
				rrdPath =  CommonUtils.getRrdDbPath(graphDefInfo.getDevice(), databaseName, filters[i]);

				rrdGraphDef.datasource(datasource, rrdPath, datasource, ConsolFun.valueOf(graphDefInfo.getConsolFun()));			
				rrdGraphDef.line(datasource, TetradGraphTemplate.getColor(i, 0), graphLegend[i], lineWidth);
				
				rrdGraphDef.comment("Max:");
				rrdGraphDef.gprint(datasource, ConsolFun.MAX, "%5.2f%S");
				rrdGraphDef.comment("Min:");
				rrdGraphDef.gprint(datasource, ConsolFun.MIN, "%5.2f%S");
				rrdGraphDef.comment("Avg:");
				rrdGraphDef.gprint(datasource, ConsolFun.AVERAGE, "%5.2f%S");				
				rrdGraphDef.comment("\\l");
			}
			
			template.setYaxisValue(rrdGraphDef, type);			

			long start = graphDefInfo.getStartTime();
			long end = graphDefInfo.getEndTime();

			rrdGraphDef.setStep(graphDefInfo.getStep());
			rrdGraphDef.setStartTime(start);
			rrdGraphDef.setEndTime(end);
			rrdGraphDef.setWidth(graphDefInfo.getWidth());
			rrdGraphDef.setHeight(graphDefInfo.getHeight());
									
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return rrdGraphDef;
	}

	public RrdGraphDef createGraphPerRrdDb(String dsName, GraphDefInfo graphDefInfo) throws Exception {

		RrdGraphDef rrdGraphDef = new RrdGraphDef();

		try {
			String strLineWidth = TetradRrdConfig.getTetradRrdConfig("graph_line_width");
			float lineWidth = Float.parseFloat(strLineWidth);
			
			TetradGraphTemplate template = new TetradGraphTemplate(graphDefInfo.getAxisTimeUnitDiv());
//			template.mainSetRrdGraphDefTimeUnit(rrdGraphDef);
			template.setFont(rrdGraphDef);
			
			String cdefDataSource = "";
			String datasource = StringUtil.rightSubstring(dsName, 20);
			String rrdPath = CommonUtils.getDefaultRrdPath() + dsName + ".rrd";
			rrdGraphDef.datasource(datasource, rrdPath, datasource, ConsolFun.valueOf(graphDefInfo.getConsolFun()));
			if(dsName.equals("totalGlobalLockTime")){
				cdefDataSource = template.getDatasourceSettingCDef(rrdGraphDef, datasource, "COUNTER", "globalLock_lockTime");
				rrdGraphDef.line(cdefDataSource, TetradGraphTemplate.getColor(), datasource, lineWidth);
			}else{
				template.setdbYlabel(rrdGraphDef);
				rrdGraphDef.line(datasource, TetradGraphTemplate.getColor(), datasource, lineWidth);
			}
			
			// 임계치 표시
			rrdGraphDef.setStep(graphDefInfo.getStep());
			rrdGraphDef.setTimeSpan(graphDefInfo.getStartTime(), graphDefInfo.getEndTime());
			rrdGraphDef.setWidth(graphDefInfo.getWidth());
			rrdGraphDef.setHeight(graphDefInfo.getHeight());
			rrdGraphDef.setNoLegend(true);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		return rrdGraphDef;
	}
	
	public RrdGraphDef createMultiDeviceGraphPerRrd(String dsName, GraphDefInfo graphDefInfo) throws Exception {

		RrdGraphDef rrdGraphDef = new RrdGraphDef();
		String resultFilename = null;

		try {			
			String strLineWidth = TetradRrdConfig.getTetradRrdConfig("graph_line_width");
			float lineWidth = Float.parseFloat(strLineWidth);
			
			TetradGraphTemplate template = new TetradGraphTemplate(graphDefInfo.getAxisTimeUnitDiv());
//			template.mainSetRrdGraphDefTimeUnit(rrdGraphDef);
			template.setFont(rrdGraphDef);
			
			List<Device> deviceGroup = graphDefInfo.getDeviceGroup();
			String datasource = "";
			int index = 0;
			String devcode = "";
			for (Device device : deviceGroup) {
				devcode = device.getIdx()+"_";
				datasource = StringUtil.rightSubstring(dsName, 20);
				String rrdPath = CommonUtils.getRrdDbPath(device, "", dsName);
				
				String cdefDataSource = "";
				String dataName = devcode+dsName;
//				String originalDsname = dsName;
				rrdGraphDef.datasource(dataName, rrdPath, datasource,  ConsolFun.valueOf(graphDefInfo.getConsolFun()));
				
				StatusDatasourceName serverDsInfo = serverMapInfo.get(dsName);
				String serverDsType = serverDsInfo.getDsType();
				
				cdefDataSource = template.getDatasourceSettingCDef(rrdGraphDef, dataName, serverDsType, dsName);
				
				rrdGraphDef.line(cdefDataSource, TetradGraphTemplate.getColor(index, 0), datasource, lineWidth);
				index++;
			}
			
			// 시간 범위 선택
			rrdGraphDef.setStep(graphDefInfo.getStep());
			rrdGraphDef.setTimeSpan(graphDefInfo.getStartTime(), graphDefInfo.getEndTime());

			rrdGraphDef.setFilename(resultFilename);
			rrdGraphDef.setWidth(graphDefInfo.getWidth());
			rrdGraphDef.setHeight(graphDefInfo.getHeight());
			rrdGraphDef.setNoLegend(true);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		return rrdGraphDef;
	}
	
	public RrdGraphDef detailedCreateGraphPerRrdDb(GraphDefInfo graphDefInfo) throws Exception {

		RrdGraphDef rrdGraphDef = new RrdGraphDef();

		try {
			String strLineWidth = TetradRrdConfig.getTetradRrdConfig("graph_line_width");
			float lineWidth = Float.parseFloat(strLineWidth);
			String[] filters = graphDefInfo.getFilters();
			String[] graphLegend = graphDefInfo.getGraphLegend();
			String type = graphDefInfo.getFileName();
			
			TetradGraphTemplate template = new TetradGraphTemplate(graphDefInfo.getAxisTimeUnitDiv());
			template.setTitle(rrdGraphDef, type);
			template.setFont(rrdGraphDef);
			
			String cdefDataSource = "";
			String dsName = "";
			for (int index = 0 ; index < filters.length ; index++){
				dsName = filters[index];
				String datasource = StringUtil.rightSubstring(dsName, 20);
				String rrdPath = CommonUtils.getDefaultRrdPath() + dsName + ".rrd";
				rrdGraphDef.datasource(datasource, rrdPath, datasource,  ConsolFun.valueOf(graphDefInfo.getConsolFun()));
				if(dsName.equals("totalGlobalLockTime")){
					cdefDataSource = template.getDatasourceSettingCDef(rrdGraphDef, datasource, "COUNTER", "globalLock_lockTime");
					rrdGraphDef.line(cdefDataSource, TetradGraphTemplate.getColor(), datasource, lineWidth);
				}else{
					template.setdbYlabel(rrdGraphDef);
					rrdGraphDef.line(datasource, TetradGraphTemplate.getColor(), graphLegend[index], lineWidth);
				}
	
				rrdGraphDef.comment("Max:");
				rrdGraphDef.gprint(datasource, ConsolFun.MAX, "%5.2f%S");
				rrdGraphDef.comment("Min:");
				rrdGraphDef.gprint(datasource, ConsolFun.MIN, "%5.2f%S");
				rrdGraphDef.comment("Avg:");
				rrdGraphDef.gprint(datasource, ConsolFun.AVERAGE, "%5.2f%S");				
				rrdGraphDef.comment("\\l");
			}
			
			// 임계치 표시
			rrdGraphDef.setStep(graphDefInfo.getStep());
			rrdGraphDef.setTimeSpan(graphDefInfo.getStartTime(), graphDefInfo.getEndTime());
			rrdGraphDef.setWidth(graphDefInfo.getWidth());
			rrdGraphDef.setHeight(graphDefInfo.getHeight());
			rrdGraphDef.setAltYMrtg(false);
			rrdGraphDef.setAltYGrid(true);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		return rrdGraphDef;
	}
	
	public RrdGraphDef detailedCreateMultiDeviceGraphPerRrd(String dsName, GraphDefInfo graphDefInfo, String title) throws Exception {

		RrdGraphDef rrdGraphDef = new RrdGraphDef();
		String resultFilename = null;

		try {			
			String strLineWidth = TetradRrdConfig.getTetradRrdConfig("graph_line_width");
			float lineWidth = Float.parseFloat(strLineWidth);
			String[] graphLegend = graphDefInfo.getGraphLegend();
			
			TetradGraphTemplate template = new TetradGraphTemplate(graphDefInfo.getAxisTimeUnitDiv());
			rrdGraphDef.setTitle(title);
			template.setFont(rrdGraphDef);
			
			List<Device> deviceGroup = graphDefInfo.getDeviceGroup();
			String datasource = "";
			int index = 0;
			String devcode = "";
			for (Device device : deviceGroup) {
				devcode = device.getIdx()+"_";
				String rrdPath = CommonUtils.getRrdDbPath(device, "", dsName);

				String cdefDataSource = "";
				String dataName = devcode+dsName;
//				String originalDsname = dsName;
				datasource = StringUtil.rightSubstring(dsName, 20);
				
				rrdGraphDef.datasource(dataName, rrdPath, datasource,  ConsolFun.valueOf(graphDefInfo.getConsolFun()));

				StatusDatasourceName serverDsInfo = serverMapInfo.get(dsName);
				String serverDsType = serverDsInfo.getDsType();
				cdefDataSource = template.getDatasourceSettingCDef(rrdGraphDef, dataName, serverDsType, dsName);
				
				rrdGraphDef.line(cdefDataSource, TetradGraphTemplate.getColor(index, 0), graphLegend[index], lineWidth);
				
				rrdGraphDef.comment("Max:");
				rrdGraphDef.gprint(cdefDataSource, ConsolFun.MAX, "%5.2f%S");
				rrdGraphDef.comment("Min:");
				rrdGraphDef.gprint(cdefDataSource, ConsolFun.MIN, "%5.2f%S");
				rrdGraphDef.comment("Avg:");
				rrdGraphDef.gprint(cdefDataSource, ConsolFun.AVERAGE, "%5.2f%S");				
				rrdGraphDef.comment("\\l");
				
				index++;
			}
			
			// 시간 범위 선택
			rrdGraphDef.setTimeSpan(graphDefInfo.getStartTime(), graphDefInfo.getEndTime());
			rrdGraphDef.setStep(graphDefInfo.getStep());
			rrdGraphDef.setFilename(resultFilename);
			rrdGraphDef.setWidth(graphDefInfo.getWidth());
			rrdGraphDef.setHeight(graphDefInfo.getHeight());
			rrdGraphDef.setAltYMrtg(false);
			rrdGraphDef.setAltYGrid(true);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		return rrdGraphDef;
	}
	
	public RrdGraphDef createSubMultiDeviceGraphPerRrd(GraphDefInfo graphDefInfo, String sortItem) throws Exception {

		RrdGraphDef rrdGraphDef = new RrdGraphDef();
		String resultFilename = null;

		try {			
			String strLineWidth = TetradRrdConfig.getTetradRrdConfig("graph_line_width");
			float lineWidth = Float.parseFloat(strLineWidth);
			String[] filters = graphDefInfo.getFilters();
			String[] graphLegend = graphDefInfo.getGraphLegend();
			String type = graphDefInfo.getFileName();
			String dsName = "";
			String graphGubun = graphDefInfo.getAxisTimeUnitDiv();
			
			TetradGraphTemplate template = new TetradGraphTemplate(graphGubun);
			template.setTitle(rrdGraphDef, type);
			template.setFont(rrdGraphDef);
			
			List<Device> deviceGroup = graphDefInfo.getDeviceGroup();
			String datasource = "";
			int size = 0;
			for (Device device : deviceGroup) {
				String devCode = device.getIdx()+"_";
				String deviceName = Code.device.getCode().getName(device.getIdx());
				String cdefDataSource = "";
				
				for (int index = 0 ; index < filters.length ; index++){
					dsName = filters[index];
					
					datasource = StringUtil.rightSubstring(dsName, 20);
					String rrdPath = CommonUtils.getRrdDbPath(device, "", dsName);
					
					String dataName = devCode+dsName;
					rrdGraphDef.datasource(dataName, rrdPath, datasource, ConsolFun.valueOf(graphDefInfo.getConsolFun()));

					StatusDatasourceName serverDsInfo = serverMapInfo.get(dsName);
					String serverDsType = serverDsInfo.getDsType();
					
					cdefDataSource = template.getDatasourceSettingCDef(rrdGraphDef, dataName, serverDsType, dsName);
					
					String label = "";
					String formatLabel = "";
					if(sortItem.equals("big")){
						label = deviceName + " " + graphLegend[index];
						formatLabel = Utility.stringFormat(label, 25, 0);
					}else{
						label = deviceName;
						formatLabel = Utility.stringFormat(label, 15, 0);
					}

					rrdGraphDef.line(cdefDataSource, TetradGraphTemplate.getColor(index, size), formatLabel, lineWidth);
					
					rrdGraphDef.comment("Max:");
					rrdGraphDef.gprint(cdefDataSource, ConsolFun.MAX, "%5.2f%S");
					rrdGraphDef.comment("Min:");
					rrdGraphDef.gprint(cdefDataSource, ConsolFun.MIN, "%5.2f%S");
					rrdGraphDef.comment("Avg:");
					rrdGraphDef.gprint(cdefDataSource, ConsolFun.AVERAGE, "%5.2f%S");		
					rrdGraphDef.comment("\\l");
					size++;
				}
			}
			
			// 시간 범위 선택
			rrdGraphDef.setTimeSpan(graphDefInfo.getStartTime(), graphDefInfo.getEndTime());
			rrdGraphDef.setStep(graphDefInfo.getStep());

			rrdGraphDef.setFilename(resultFilename);
			rrdGraphDef.setWidth(graphDefInfo.getWidth());
			rrdGraphDef.setHeight(graphDefInfo.getHeight());
			rrdGraphDef.setAltYMrtg(false);
			rrdGraphDef.setAltYGrid(true);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		return rrdGraphDef;
	}
	
	private String dbLockFileGubun(String fileName){
		String gubun = "";
		if(fileName.equals(DBLOCKFILEARR[0])){
			gubun = "write";
		}else if(fileName.equals(DBLOCKFILEARR[1])){
			gubun = "read";
		}
		return gubun;
	}
	public RrdGraphDef createLockMultiGraphPerRrd(GraphDefInfo graphDefInfo, String sortItem) throws Exception {

		RrdGraphDef rrdGraphDef = new RrdGraphDef();
		String resultFilename = null;

		try {			
			String strLineWidth = TetradRrdConfig.getTetradRrdConfig("graph_line_width");
			float lineWidth = Float.parseFloat(strLineWidth);
			String[] filters = graphDefInfo.getFilters();
			String[] graphLegend = graphDefInfo.getGraphLegend();
			String dbName = "";
			String graphGubun = graphDefInfo.getAxisTimeUnitDiv();
			String fileName = graphDefInfo.getGubun();
			String gubun = dbLockFileGubun(fileName);
			
			TetradGraphTemplate template = new TetradGraphTemplate(graphGubun);
			template.setFont(rrdGraphDef);
			
			List<Device> deviceGroup = graphDefInfo.getDeviceGroup();
			String datasource = "";
			int size = 0;
			for (Device device : deviceGroup) {
				String devCode = device.getIdx()+"_";
				String deviceName = Code.device.getCode().getName(device.getIdx());
				String cdefDataSource = "";
				
				for (int index = 0 ; index < filters.length ; index++){
					dbName = filters[index];
					rrdGraphDef.setTitle(dbName +" DB Lock Time" + " - " +gubun);
					
					datasource = StringUtil.rightSubstring(fileName, 20);
					String rrdPath = CommonUtils.getRrdDbPath(device, dbName, fileName);
					
					File fileExist = new File(rrdPath);

				    // 파일 존재 여부 판단
				 if (fileExist.isFile()) {
						String dataName = devCode+dbName;
						rrdGraphDef.datasource(dataName, rrdPath, datasource, ConsolFun.valueOf(graphDefInfo.getConsolFun()));
						
						cdefDataSource = template.getDatasourceSettingCDef(rrdGraphDef, dataName, "COUNTER", "globalLock_lockTime");
						
						String label = "";
						String formatLabel = "";
						if(sortItem.equals("big")){
							label = deviceName + " " + graphLegend[index] +" - "+ gubun;
							formatLabel = Utility.stringFormat(label, 25, 0);
						}else{
							label = deviceName +" - "+ gubun;
							formatLabel = Utility.stringFormat(label, 12, 0);
						}
	
						rrdGraphDef.line(cdefDataSource, TetradGraphTemplate.getColor(index, size), formatLabel, lineWidth);
						
						rrdGraphDef.comment("Max:");
						rrdGraphDef.gprint(cdefDataSource, ConsolFun.MAX, "%5.2f%S");
						rrdGraphDef.comment("Min:");
						rrdGraphDef.gprint(cdefDataSource, ConsolFun.MIN, "%5.2f%S");
						rrdGraphDef.comment("Avg:");
						rrdGraphDef.gprint(cdefDataSource, ConsolFun.AVERAGE, "%5.2f%S");		
						rrdGraphDef.comment("\\l");
						size++;
				 }
				}
			}
			
			// 시간 범위 선택
			rrdGraphDef.setTimeSpan(graphDefInfo.getStartTime(), graphDefInfo.getEndTime());
			rrdGraphDef.setStep(graphDefInfo.getStep());

			rrdGraphDef.setFilename(resultFilename);
			rrdGraphDef.setWidth(graphDefInfo.getWidth());
			rrdGraphDef.setHeight(graphDefInfo.getHeight());
			rrdGraphDef.setAltYMrtg(false);
			rrdGraphDef.setAltYGrid(true);	

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		return rrdGraphDef;
	}
	
	public RrdGraphDef createSoloLockMultiGraphPerRrd(GraphDefInfo graphDefInfo, String sortItem) throws Exception {

		RrdGraphDef rrdGraphDef = new RrdGraphDef();
		String resultFilename = null;

		try {			
			String strLineWidth = TetradRrdConfig.getTetradRrdConfig("graph_line_width");
			float lineWidth = Float.parseFloat(strLineWidth);
			String[] filters = graphDefInfo.getFilters();
			String[] graphLegend = graphDefInfo.getGraphLegend();
			String dbName = "";
			String graphGubun = graphDefInfo.getAxisTimeUnitDiv();
			String fileName = graphDefInfo.getGubun();
			String gubun = dbLockFileGubun(fileName);
			
			TetradGraphTemplate template = new TetradGraphTemplate(graphGubun);
			template.setFont(rrdGraphDef);
			
			Device device = graphDefInfo.getDevice();
			String datasource = "";
			int size = 0;
				String devCode = device.getIdx()+"_";
				String deviceName = Code.device.getCode().getName(device.getIdx());
				String cdefDataSource = "";
				
				for (int index = 0 ; index < filters.length ; index++){
					dbName = filters[index];
					rrdGraphDef.setTitle(deviceName+"-"+dbName +" DB Lock Time" + " - " +gubun);
					
					datasource = StringUtil.rightSubstring(fileName, 20);
					String rrdPath = CommonUtils.getRrdDbPath(device, dbName, fileName);
					
					File fileExist = new File(rrdPath);

				    // 파일 존재 여부 판단
				 if (fileExist.isFile()) {
						String dataName = devCode+dbName;
						rrdGraphDef.datasource(dataName, rrdPath, datasource, ConsolFun.valueOf(graphDefInfo.getConsolFun()));
						
						cdefDataSource = template.getDatasourceSettingCDef(rrdGraphDef, dataName, "COUNTER", "globalLock_lockTime");
						
						String label = "";
						String formatLabel = "";
						if(sortItem.equals("big")){
							label = deviceName + " " + graphLegend[index] +" - "+ gubun;
							formatLabel = Utility.stringFormat(label, 25, 0);
						}else{
							label = deviceName +" - "+ gubun;
							formatLabel = Utility.stringFormat(label, 12, 0);
						}
	
						rrdGraphDef.line(cdefDataSource, TetradGraphTemplate.getColor(index, size), formatLabel, lineWidth);
						
						rrdGraphDef.comment("Max:");
						rrdGraphDef.gprint(cdefDataSource, ConsolFun.MAX, "%5.2f%S");
						rrdGraphDef.comment("Min:");
						rrdGraphDef.gprint(cdefDataSource, ConsolFun.MIN, "%5.2f%S");
						rrdGraphDef.comment("Avg:");
						rrdGraphDef.gprint(cdefDataSource, ConsolFun.AVERAGE, "%5.2f%S");		
						rrdGraphDef.comment("\\l");
						size++;
				 }
				}
			
			// 시간 범위 선택
			rrdGraphDef.setTimeSpan(graphDefInfo.getStartTime(), graphDefInfo.getEndTime());
			rrdGraphDef.setStep(graphDefInfo.getStep());

			rrdGraphDef.setFilename(resultFilename);
			rrdGraphDef.setWidth(graphDefInfo.getWidth());
			rrdGraphDef.setHeight(graphDefInfo.getHeight());
			rrdGraphDef.setAltYMrtg(false);
			rrdGraphDef.setAltYGrid(true);	

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		return rrdGraphDef;
	}
	
	private Map<String, StatusDatasourceName> convertArrayToMap(List<StatusDatasourceName> tetradServerStatusDatasourceNames) {
		Map<String, StatusDatasourceName> serverMap = new HashMap<String, StatusDatasourceName>();
		for (StatusDatasourceName serverDsInfo : tetradServerStatusDatasourceNames) {
			serverMap.put(serverDsInfo.getDsName(), serverDsInfo);
		}
		return serverMap;
	}
	
}
