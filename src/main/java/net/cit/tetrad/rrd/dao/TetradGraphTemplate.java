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

import static net.cit.tetrad.common.ColumnConstent.SUBGRAPH_DAY;
import static net.cit.tetrad.common.ColumnConstent.SUBGRAPH_HOUR;
import static net.cit.tetrad.common.ColumnConstent.SUBGRAPH_MINUTE;
import static net.cit.tetrad.common.ColumnConstent.ALL;

import java.awt.Color;
import java.awt.Font;

import net.cit.tetrad.rrd.bean.GraphDefInfo;
import net.cit.tetrad.rrd.utils.TetradRrdConfig;

import org.rrd4j.graph.RrdGraphConstants;
import org.rrd4j.graph.RrdGraphDef;

public class TetradGraphTemplate {

	enum lineColor {
		line1 {
			public Color getColor(){
				return new Color(59, 89, 152);
			}
		},
		line2 {
			public Color getColor(){
				return new Color(90, 184, 0);
			}
		},
		line3 {
			public Color getColor(){
				return new Color(255,69,0);
			}
		},
		line4 {
			public Color getColor(){
				return new Color(128,0,0);
			}
		},
		line5 {
			public Color getColor(){
				return new Color(138,43,226);
			}
		},
		line6 {
			public Color getColor(){
				return new Color(210,105,30);
			}
		},
		line7 {
			public Color getColor(){
				return new Color(255, 0, 255);
			}
		},
		line8 {
			public Color getColor(){
				return new Color(0,100,0);
			}
		},
		line9 {
			public Color getColor(){
				return new Color(0, 0, 255);
			}
		},
		line10 {
			public Color getColor(){
				return new Color(255, 0, 0);
			}
		},
		line11 {
			public Color getColor(){
				return new Color(70,130,180);
			}
		},
		line12 {
			public Color getColor(){
				return new Color(128,128,0);
			}
		},
		line13 {
			public Color getColor(){
				return new Color(0,128,0);
			}
		},
		line14 {
			public Color getColor(){
				return new Color(0,255,0);
			}
		},
		line15 {
			public Color getColor(){
				return new Color(0,128,128);
			}
		},
		line16 {
			public Color getColor(){
				return new Color(0,255,255);
			}
		},
		line17 {
			public Color getColor(){
				return new Color(184,134,11);
			}
		},
		line18 {
			public Color getColor(){
				return new Color(139,69,19);
			}
		},
		line19 {
			public Color getColor(){
				return new Color(165,42,42);
			}
		},
		line20 {
			public Color getColor(){
				return new Color(75,0,130);
			}
		};
			public abstract Color getColor();
	}
	
	
	String graphGubun;
	
	TetradGraphTemplate() {
	}
	
	TetradGraphTemplate(String graphGubun) {
		this.graphGubun = graphGubun;
	}
	
	public void setGraphColor(RrdGraphDef rrdGraphDef){
		rrdGraphDef.setColor("GRID", new Color(231,231,231));
		rrdGraphDef.setColor("MGRID", new Color(254,208,208));
	}
	
//	public void setLimitValue(RrdGraphDef rrdGraphDef, String dsName, GraphDefInfo graphDefInfo) { //그래프에 임계치 선을 그릴경우 
//		CriticalHelper helper = new CriticalHelper(graphDefInfo.getDevice(), null);		
//		helper.setCriticalLevel(dsName);
//		
//		if (helper.existCritical()) {
//			rrdGraphDef.hrule(helper.criticalValue, Color.RED);
//			rrdGraphDef.hrule(helper.warningValue, Color.BLUE);
//		}
//	}
	
	public String setTotalLegend(String legendStr){
		String str = "";
		if(legendStr.equals("totalSystemLocksTimeLockedMicros_r_sum")){
			str = "Global Lock read sum";
		}else if(legendStr.equals("totalSystemLocksTimeLockedMicros_w_sum")){
			str = "Global Lock write sum";
		}else if(legendStr.equals("totalDbLocksTimeLockedMicros_r_sum")){
			str = "DB Locks read sum";
		}else if(legendStr.equals("totalDbLocksTimeLockedMicros_w_sum")){
			str = "DB Locks write sum";
		}
		return str;
	}
	public String getDatasourceSettingCDef(RrdGraphDef rrdGraphDef, String datasource, String serverDsType, String dsName) {
		String newDatasource = datasource;
		if(serverDsType.equals("COUNTER")){
			newDatasource = "final_"+datasource;
			rrdGraphDef.datasource(newDatasource, datasource+",10,*");
		}
		String finalDatasource = newDatasource;
		if(dsName.equals("mem_resident") || dsName.equals("mem_virtual")){
			int op = 1024*1024;
			finalDatasource = "byte_"+newDatasource;
			rrdGraphDef.datasource(finalDatasource, newDatasource+","+op+",*");
			rrdGraphDef.setVerticalLabel("MB");
//		} else if(dsName.equals("extra_info_page_faults")){
//			finalDatasource = "interval_" + newDatasource;
//			rrdGraphDef.datasource(finalDatasource, newDatasource+",1000,/");
		} else if(dsName.equals("globalLock_lockTime")||dsName.equals("locks_timeLockedMicros_W")||dsName.equals("locks_timeLockedMicros_R")){
			finalDatasource = "interval_milli_" + newDatasource;
			rrdGraphDef.datasource(finalDatasource, newDatasource+",1000,/");
			rrdGraphDef.setVerticalLabel("miliseconds");
		} else if( dsName.equals("network_bytesIn") || dsName.equals("network_bytesOut")){
			rrdGraphDef.setVerticalLabel("bytes");
		}	else{
			rrdGraphDef.setVerticalLabel("number");
		}
		return finalDatasource;
	}
	
	public void setdbYlabel(RrdGraphDef rrdGraphDef){
		rrdGraphDef.setVerticalLabel("bytes");
	}
	
	public void setYaxisValue(RrdGraphDef rrdGraphDef, String type) {
		if(type.equals("conn")){
			rrdGraphDef.setAltYMrtg(false);
			rrdGraphDef.setAltYGrid(true);
		} else if(type.equals("r_mem") || type.equals("v_mem")){
			rrdGraphDef.setAltYMrtg(true);
			rrdGraphDef.setAltYGrid(true);
		} else if(type.equals("p_fault")){
			rrdGraphDef.setAltYMrtg(false);
			rrdGraphDef.setAltYGrid(true);
		} else if(type.equals("op_insert") || type.equals("op_query") || type.equals("op_delete") || type.equals("op_update")){
			rrdGraphDef.setAltYMrtg(false);
			rrdGraphDef.setAltYGrid(true);
		} else if(type.equals("global")){
			rrdGraphDef.setAltYMrtg(false);
			rrdGraphDef.setAltYGrid(true);
			rrdGraphDef.setVerticalLabel("milliseconds");
		} else if(type.equals("dataSize") || type.equals("indexSize")) {
			rrdGraphDef.setAltYMrtg(false);
			rrdGraphDef.setAltYGrid(true);
		} else if(type.equals("totdb")) {
			rrdGraphDef.setAltYMrtg(true);
			rrdGraphDef.setAltYGrid(true);
		}else{
			rrdGraphDef.setAltYMrtg(false);
			rrdGraphDef.setAltYGrid(true);
		}
	}
	
	public void setLegendSpace(RrdGraphDef rrdGraphDef, int size) {
		int max = 4;
		for (int i=0; i<max-size; i++) {		
			rrdGraphDef.comment(" \\s");
		}
	}

	private static Font s_oFontNormal=new Font("verdana",Font.PLAIN,10);
	private static Font s_oFontBold=new Font("verdana",Font.BOLD,13);
	
	public void setFont(RrdGraphDef rrdGraphDef){
		rrdGraphDef.setLargeFont(s_oFontBold);
		rrdGraphDef.setSmallFont(s_oFontNormal);
	}
	
	public void setTitle(RrdGraphDef rrdGraphDef, String type) {
		if(type.equals("conn")){
			rrdGraphDef.setTitle("Connections Count");
		} else if(type.equals("r_mem")){
			rrdGraphDef.setTitle("Resident Memory Size");
		} else if(type.equals("v_mem")){
			rrdGraphDef.setTitle("Virtual Memory Size");
		} else if(type.equals("p_fault")){
			rrdGraphDef.setTitle("Page faults");
		} else if(type.equals("op_insert")){
			rrdGraphDef.setTitle("Operations Count - Insert");
		} else if(type.equals("op_delete")){
			rrdGraphDef.setTitle("Operations Count - Delete");
		} else if(type.equals("op_update")){
			rrdGraphDef.setTitle("Operations Count - Update");
		} else if(type.equals("op_query")){
			rrdGraphDef.setTitle("Operations Count - Query");
		} else if(type.equals("global")){
			rrdGraphDef.setTitle("GlobalLock Time");
		} else if(type.equals("w_locks")){
			rrdGraphDef.setTitle("Global Lock - Write");
		} else if(type.equals("r_locks")){
			rrdGraphDef.setTitle("Global Lock - Read");
		} else if(type.equals("in_network")){
			rrdGraphDef.setTitle("Network - In");
		} else if(type.equals("out_network")){
			rrdGraphDef.setTitle("Network - Out");
		} else if(type.equals("totdb")) {
			rrdGraphDef.setTitle("Total DB Size");
		} else if(type.equals("totalDbDataSize")){
			rrdGraphDef.setTitle("Total DB Data Size");
		} else if(type.equals("totalDbIndexSize")){
			rrdGraphDef.setTitle("Total DB Index Size");
		} else if(type.equals("totalGlobalLockTime")) {
			rrdGraphDef.setTitle("Total Global Lock Time");
		} else if(type.equals("dbDataSize")){
			rrdGraphDef.setTitle("DB Data Size Sum");
		} else if(type.equals("dbIndexSize")) {
			rrdGraphDef.setTitle("DB Index Size Sum");
		}
	}
	
	public static Color getColor() {	
		return getColor(0,0);
	}
	
	public static Color getColor(int i, int getSize) {	
		int size = lineColor.values().length;
		int j = i + getSize;
		int idx = (j%size);

		switch (idx){
		case 0:
			return lineColor.line1.getColor();
		case 1:
			return lineColor.line2.getColor();
		case 2:
			return lineColor.line3.getColor();
		case 3:
			return lineColor.line4.getColor();
		case 4:
			return lineColor.line5.getColor();
		case 5:
			return lineColor.line6.getColor();
		case 6:
			return lineColor.line7.getColor();
		case 7:
			return lineColor.line8.getColor();
		case 8:
			return lineColor.line9.getColor();
		case 9:
			return lineColor.line10.getColor();
		case 10:
			return lineColor.line11.getColor();
		case 11:
			return lineColor.line12.getColor();
		case 12:
			return lineColor.line13.getColor();
		case 13:
			return lineColor.line14.getColor();
		case 14:
			return lineColor.line15.getColor();
		case 15:
			return lineColor.line16.getColor();
		case 16:
			return lineColor.line17.getColor();
		case 17:
			return lineColor.line18.getColor();
		case 18:
			return lineColor.line19.getColor();
		case 19:
			return lineColor.line20.getColor();
		default :
			return lineColor.line1.getColor();
		}		
	}
	
}
