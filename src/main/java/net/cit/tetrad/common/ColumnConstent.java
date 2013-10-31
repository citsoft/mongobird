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
package net.cit.tetrad.common;



public class ColumnConstent {	
	public static final String APPLICATIONCONTEXT_XML = "applicationContext_management.xml";
	public static final String MONGOVER2_2 = "2.2"; 
	public static final String MONGOVER2_0 = "2"; 
	
	/** 경로 지정을 위한 상수 */
	public static final String PATH_MANAGEMEMT = "management/";
	public static final String PATH_GRAPH = "graph/";
	public static final String PATH_LOCKGRAPH = "lockGraph/";
	public static final String PATH_DBGRAPH = "dbGraph/";
	
	/** ModelAndView 속성 지정을 위한 상수 */
	public static final String MAV_DEVICE = "device";
	public static final String MAV_CRITICAL = "critical";
	public static final String MAV_GROUP = "group";
	public static final String MAV_GLOBAL = "global";
	public static final String MAV_USER = "user";
	public static final String MAV_TYPE = "type";
	public static final String MAV_COMM = "comm";
	
	/** 임계값 지정을 위한 상수*/
	public static final String UNIT_PER = "percent";
	public static final String UNIT_EX = "ex";
	public static final String UNIT_SECONDS = "seconds";
	
	/** admin 지정을 위한 상수*/
	public static final String ADMIN = "admin";
	
	/** 컬럼명 지정을 위한 상수 */
	public static final String COL_GROUPCODE = "groupCode";
	public static final String COL_DEVICECODE = "deviceCode";
	
	/** Request 속성 지정을 위한 상수 */
	public static final String REQ_DIVAL = "dival";
	public static final String REQ_SECHO = "sEcho";
	
	/** Response 속성 지정을 위한 상수 */
	public static final String RES_CONTENTTYPE = "ContentType";
	
	/** BeanName 지정을 위한 상수 */
	public static final String BEAN_MANAGEMENTDAO = "managementDao";
	public static final String BEAN_INDEXDAO = "indexDao";
	public static final String BEAN_QUERYTDAO = "queryDao";
	public static final String BEAN_AFMINDAO = "adminDao";
	public static final String BEAN_SUBDAO = "subDao";
	public static final String BEAN_MONADSERVICEDAO = "monadService";
	
	
	public final static String PROCESS_MONGOD = "mongod";
	public final static String PROCESS_MONGOS = "mongos";

	public final static String[] RRD_SUFFIXPATH = {"", "_MINUTES"};
	public final static String RRD_SUFFIXPATH_HOURS = "_HOURS";
	public final static String RRD_SUFFIXPATH_DAYS = "_DAYS";
	public final static String[] IMGTOTALFIELD2_0 = {"totalDbDataSize", "totalDbIndexSize", "totalGlobalLockTime"};
	public final static String[] IMGTOTALFIELD2_2 = {"totalDbDataSize", "totalDbIndexSize"};
	public static final String[] TOTALFILEARR = {"totalSystemLocksTimeLockedMicros_w_sum","totalDbLocksTimeLockedMicros_w_sum"};
	public static final String[] DBLOCKFILEARR = {"locks_timeLockedMicros_w","locks_timeLockedMicros_r"};
	
	public final static String GRAPH_SUFFIX_DAY = "_DAY";
	public final static String GRAPH_SUFFIX_WEEK = "_WEEK";
	public final static String GRAPH_SUFFIX_MONTH = "_MONTH";
	
	public final static String IMG_NODATA_PATH = "nodata/tmp_graph_01.gif";
	
	public final static int MAIN_GRAPH_WIDTH = 267;
	public final static int MAIN_GRAPH_HEIGHT = 133;
	public final static int SUB_GRAPH_WIDTH = 880;
	public final static int SUB_GRAPH_HEIGHT = 300;
	public final static int SUB_SMALL_GRAPH_WIDTH = 390;
	public final static int SUB_SMALL_GRAPH_HEIGHT = 141;

	public final static String MYSTATE = "replSetGetStatus";
	public final static String COLL_DASHBOARD = "dashboard";
	public final static String COLL_TOTALMONGODINFO= "totalInfo";
	public final static String COLL_ALARM= "alarm";
	public final static String COLL_SERVERSTATUS= "serverStatus";
	public final static String COLL_DBSTATUS= "dbStatus";
	
	public final static String DBSTATUS_DBNAME = "db";
	public final static String DBSTATUS_DIFF_DATASIZE = "diff_dataSize";
	public final static String DBSTATUS_DIFF_INDEXSIZE = "diff_indexSize";
	
	public final static String SERVERSTATUS_ID = "id";
	public final static String SERVERSTATUS_IP = "ip";
	public final static String SERVERSTATUS_PORT = "port";
	public final static String SERVERSTATUS_DBOBJECTS = "dbObjects";
	public final static String SERVERSTATUS_DBAVGOBJSIZE = "dbAvgObjSize";
	public final static String SERVERSTATUS_DBDATASIZE = "dbDataSize";
	public final static String SERVERSTATUS_DBSTORAGESIZE = "dbStorageSize";
	public final static String SERVERSTATUS_DBNUMEXTENTS = "dbNumExtents";
	public final static String SERVERSTATUS_DBCOUNT = "dbCount";
	public final static String SERVERSTATUS_DBINDEXES = "dbIndexes";
	public final static String SERVERSTATUS_DBINDEXSIZE = "dbIndexSize";
	public final static String SERVERSTATUS_DBFILESIZE = "dbFileSize";
	public final static String SERVERSTATUS_DBNSSIZEMB = "dbNsSizeMB";
	public final static String SERVERSTATUS_DIFF_DBDATASIZE = "diff_dbDataSize";
	public final static String SERVERSTATUS_DIFF_DBINDEXSIZE = "diff_dbIndexSize";
	public final static String SERVERSTATUS_GLOBALLLOCKTIME = "globalLock_lockTime";
	public final static String SERVERSTATUS_PAGEFAULTS = "extra_info_page_faults";
	public final static String SERVERSTATUS_OPINSERT = "opcounters_insert";
	public final static String SERVERSTATUS_OPQUERY = "opcounters_query";
	public final static String SERVERSTATUS_OPUPDATE = "opcounters_update";
	public final static String SERVERSTATUS_OPDELETE = "opcounters_delete";
	public final static String SERVERSTATUS_REGTIME = "regtime";
	public final static String SERVERSTATUS_TYPE = "type";
	public final static String SERVERSTATUS_OK = "ok";
	public final static String SERVERSTATUS_ERROR = "error";
	public final static String SERVERSTATUS_DIFF_GLOBALLLOCKTIME = "diff_globalLock_lockTime";
	public final static String SERVERSTATUS_DIFF_PAGEFAULTS = "diff_extra_info_page_faults";
	public final static String SERVERSTATUS_DIFF_OPINSERT = "diff_opcounters_insert";
	public final static String SERVERSTATUS_DIFF_OPQUERY = "diff_opcounters_query";
	public final static String SERVERSTATUS_DIFF_OPUPDATE = "diff_opcounters_update";
	public final static String SERVERSTATUS_DIFF_OPDELETE = "diff_opcounters_delete";
	public final static String SERVERSTATUS_ISMASTER = "repl_ismaster";
	public final static String SERVERSTATUS_TRUE = "true";
	public final static String SERVERSTATUS_LOCKSTIMELOCKEDMICROS_R = "locks_timeLockedMicros_R";
	public final static String SERVERSTATUS_LOCKSTIMELOCKEDMICROS_W = "locks_timeLockedMicros_W";
	public final static String SERVERSTATUS_DIFF_LOCKSTIMELOCKEDMICROS_R = "diff_locks_timeLockedMicros_R";
	public final static String SERVERSTATUS_DIFF_LOCKSTIMELOCKEDMICROS_W = "diff_locks_timeLockedMicros_W";
	public final static String SERVERSTATUS_DBSUMLOCKSLOCKED_R = "db_sum_locks_timeLockedMicros_r";
	public final static String SERVERSTATUS_DBSUMLOCKSLOCKED_W = "db_sum_locks_timeLockedMicros_w";
	public final static String SERVERSTATUS_DIFF_DBSUMLOCKSLOCKED_R = "diff_db_sum_locks_timeLockedMicros_r";
	public final static String SERVERSTATUS_DIFF_DBSUMLOCKSLOCKED_W = "diff_db_sum_locks_timeLockedMicros_w";
	
	
	public final static String DEVICECODE = "deviceCode";
	
	public final static String DEVICE_GROUPCODE = "groupCode";
	public final static String DEVICE_UID = "uid";
	public final static String DEVICE_TYPE = "type";
	public final static String IDX = "idx";
	
	public final static int ALRAM_RISK = 0;
	public final static int ALRAM_CRITICAL = 1;
	public final static int ALRAM_WARNING = 2;
	public final static int ALRAM_INFO = 3;
	
	public final static String SUBGRAPH_MINUTE = "1";
	public final static String SUBGRAPH_HOUR = "2";
	public final static String SUBGRAPH_DAY = "3";
	public final static String ALL = "4";
	
	public final static String ALARM_UP_DATE = "up_date";
	public final static String ALARM_CONFIRM = "confirm";
	
	public final static String ALARM_DEVICECODE = "deviceCode";
	public final static String ALARM_TYPE = "type";
	public final static String ALARM_GROUPCODE = "groupCode";
	public final static String ALARM_IP = "ip";
	public final static String ALARM_PORT = "port";
	public final static String ALARM_CRI_TYPE = "cri_type";
	public final static String ALARM_FIGURE = "figure";
	public final static String ALARM_CRI_VALUE = "cri_value";
	public final static String ALARM_REAL_FIGURE = "real_figure";
	public final static String ALARM_REAL_CRI_VALUE = "real_cri_value";
	public final static String ALARM_REG_DATE = "reg_date";
	public final static String ALARM_REG_TIME = "reg_time";
	public final static String ALARM_UP_TIME = "up_time";
	public final static String ALARM_ALARM = "alarm";
	public final static String ALARM_COUNT = "count";
	
	public final static String USER_EMAIL = "email";
	
	public final static String CLRITICAL_TYPE = "type";
	public final static String SEARCHALL = "searchAll";
	
	public final static String GRAPH_ACCUM = "ACC";
	public final static String GRAPH_CURENT = "CUR";
	
	public final static int RRD_STEP_SECOND = 1;
	public final static int RRD_STEP_MINUTE = 6;
	public final static int RRD_STEP_5MINUTE = 30;
	public final static int RRD_STEP_30MINUTE = 180;
	public final static int RRD_STEP_HOUR = 360;
	
	// 값은 분
	public final static int DHTML_SEARCH_RANGE_1MIN = 30;
	public final static int DHTML_SEARCH_RANGE_5MIN = 150;
	public final static int DHTML_SEARCH_RANGE_30MIN = 900;
	public final static int DHTML_SEARCH_RANGE_60MIN = 60*30;

	// 값은 시간 
	public final static int DHTML_TOTAL_RANGE_1MIN = 2;
	public final static int DHTML_TOTAL_RANGE_5MIN = 10;
	public final static int DHTML_TOTAL_RANGE_30MIN = 60;
	public final static int DHTML_TOTAL_RANGE_60MIN = 120;
	
	public final static int DHTML_XUNIT_CRITERION_NUM = 5;
	
	public final static String TOTAL_TOTALDBDATASIZE = "totalDbDataSize";
	public final static String TOTAL_TTOTALDBINDEXSIZE = "totalDbIndexSize";
	public final static String TOTAL_DIFF_TOTALDBDATASIZE = "diff_totalDbDataSize";
	public final static String TOTAL_DIFF_TOTALDBINDEXSIZE = "diff_totalDbIndexSize";
	public final static String TOTAL_TOTALGLOBALLOCKTIME = "totalGlobalLockTime";
	public final static String TOTAL_MAXIMUMPAGEFAULTS = "maximumPageFaults";
	public final static String TOTAL_TOTALSYSTEMLOCKSTIMELOCKEDMICROS_R_SUM = "totalSystemLocksTimeLockedMicros_r_sum";
	public final static String TOTAL_TOTALSYSTEMLOCKSTIMELOCKEDMICROS_W_SUM = "totalSystemLocksTimeLockedMicros_w_sum";
	public final static String TOTAL_TOTALDBLOCKSTIMELOCKEDMICROS_R_SUM = "totalDbLocksTimeLockedMicros_r_sum";
	public final static String TOTAL_TOTALDBLOCKSTIMELOCKEDMICROS_W_SUM = "totalDbLocksTimeLockedMicros_w_sum";
	
	public final static String RRD_SYNC_PERIOD = "rrdSyncPeriod";
	
	/** License 관련 상수 */
	public final static String LICENSE_FILE = ".mongobird";
	
	public final static int REGIST_SUCCESS = 10000;
	public final static int REGIST_FAIL_INVALID = 40000;
	public final static int REGIST_FAIL_CANNOTCONNECT = 50000;
	
	public final static String LICENSE_PATTERN = "([A-Z0-9]{4})-([A-Z0-9]{4})-([A-Z0-9]{4})-([A-Z0-9]{4})-([A-Z0-9]{4})";
}	
