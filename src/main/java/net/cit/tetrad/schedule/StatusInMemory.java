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
package net.cit.tetrad.schedule;

public class StatusInMemory {
	public static boolean deleteLogState = false;
	public static boolean deleteLogServerSt = false;
	public static boolean deleteLogDbSt = false;
	public static String startDate = null;
	public static String endDate = null;
	public static double totalCnt = 0;
	public static double serverCnt = 0;
	public static double dbCnt = 0;
	public static double processPer = 0;
	
	public static boolean isDeleteLogState() {
		return deleteLogState;
	}
	public static synchronized void setDeleteLogState() {
		if(deleteLogServerSt || deleteLogDbSt){
			deleteLogState = true;
		}else{
			deleteLogState = false;
		}
	}
	public static boolean isDeleteLogServerSt() {
		return deleteLogServerSt;
	}
	public static void setDeleteLogServerSt(boolean deleteLogServerSt) {
		StatusInMemory.deleteLogServerSt = deleteLogServerSt;
		setDeleteLogState();
	}
	public static boolean isDeleteLogDbSt() {
		return deleteLogDbSt;
	}
	public static void setDeleteLogDbSt(boolean deleteLogDbSt) {
		StatusInMemory.deleteLogDbSt = deleteLogDbSt;
		setDeleteLogState();
	}
	public static String getStartDate() {
		return startDate;
	}

	public static String getEndDate() {
		return endDate;
	}

	public static double getTotalCnt() {
		return totalCnt;
	}
	public static void setTotalCnt(int servercnt, int dbcnt) {
		StatusInMemory.totalCnt = servercnt + dbcnt;
	}
	public static double getServerCnt() {
		return serverCnt;
	}
	public static void setServerCnt(int serverCnt) {
		StatusInMemory.serverCnt = serverCnt;
		setProcessPer();
	}
	public static double getDbCnt() {
		return dbCnt;
	}
	public static void setDbCnt(int dbCnt) {
		StatusInMemory.dbCnt = dbCnt;
		setProcessPer();
	}
	
	public static void setDate(String startStr, String endStr){
		StatusInMemory.startDate = startStr;
		StatusInMemory.endDate = endStr;
	}
	
	public static double getProcessPer() {
		return processPer;
	}
	
	public static synchronized void setProcessPer() {
		double per = (totalCnt - serverCnt - dbCnt)/totalCnt * 100;
		StatusInMemory.processPer = per;
	}
	
	public static void resetState(){
		deleteLogServerSt = false;
		deleteLogDbSt = false;
		deleteLogState = false;
	}
}
