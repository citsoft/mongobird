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
package net.cit.tetrad.rrd.bean;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

public class ServerStatus extends Object {
	
	@Id
	private String id;
	
	@Indexed
	private String regtime;
	private int groupCode;
	private int deviceCode;
	private int idx;
	private String type;
	private String ip;
	private String port;
	private String host;	
	private String version;
	private String process;
	private double uptime;
	private double uptimeEstimate;
	private String localTime;
	private double globalLock_totalTime;
	private double globalLock_lockTime;
	private double diff_globalLock_lockTime;
	private double globalLock_ratio;
	private double globalLock_currentQueue_total;
	private double globalLock_currentQueue_readers;
	private double globalLock_currentQueue_writers;
	private double globalLock_activeClients_total;
	private double globalLock_activeClients_readers;
	private double globalLock_activeClients_writers;
	private double mem_bits;
	private double mem_resident;
	private double mem_virtual;
	private String mem_supported;
	private double mem_mapped;
	private double mem_mappedWithJournal;
	private double connections_current;
	private double connections_available;
	private double extra_info_heap_usage_bytes;
	private double extra_info_page_faults;
	private double diff_extra_info_page_faults;
	private double indexCounters_btree_accesses;
	private double indexCounters_btree_hits;
	private double indexCounters_btree_misses;
	private double indexCounters_btree_resets;
	private double indexCounters_btree_missRatio;
	private double backgroundFlushing_flushes;
	private double backgroundFlushing_total_ms;
	private double backgroundFlushing_average_ms;
	private double backgroundFlushing_last_ms;
	private String backgroundFlushing_last_finished;
	private double cursors_totalOpen;
	private double cursors_clientCursors_size;
	private double cursors_timedOut;
	private double network_bytesIn;
	private double network_bytesOut;
	private double network_numRequests;
	private String repl_setName;
	private String repl_ismaster = "true";    //기본값 설정 (mongo 2.2부터 repl_ismaster가 standalone에 없음)
	private String repl_secondary;
	private String repl_hosts;
	private double opcounters_insert;
	private double opcounters_query;
	private double opcounters_update;
	private double opcounters_delete;
	private double diff_opcounters_insert;
	private double diff_opcounters_query;
	private double diff_opcounters_update;
	private double diff_opcounters_delete;
	private double opcounters_getmore;
	private double opcounters_command;
	private double asserts_regular;
	private double asserts_warning;
	private double asserts_msg;
	private double asserts_user;
	private double asserts_rollovers;
	private String writeBacksQueued;
	private double dur_commits;
	private double dur_journaledMB;
	private double dur_writeToDataFilesMB;
	private double dur_commitslnWriteLock;
	private double dur_earlyCommits;
	private double dur_timeMs_dt;
	private double dur_timeMs_prepLogBuffer;
	private double dur_timeMs_writeToJournal;
	private double dur_timeMs_writeToDataFiles;
	private double dur_timeMs_remapPrivateView;
	private double dur_compression;
	private double dbObjects;
	private double dbAvgObjSize;
	private double dbDataSize;
	private double dbStorageSize;
	private double dbNumExtents;
	private double dbCount;
	private double dbIndexes;
	private double dbIndexSize;
	private double diff_dbDataSize;
	private double diff_dbIndexSize;
	private double dbFileSize;
	private double dbNsSizeMB;
	private double locks_timeLockedMicros_R;
	private double locks_timeLockedMicros_W;
	private double diff_locks_timeLockedMicros_R;
	private double diff_locks_timeLockedMicros_W;
	private double locks_timeAcquiringMicros_R;
	private double locks_timeAcquiringMicros_W;
	
	private double db_sum_locks_timeLockedMicros_r;
	private double db_sum_locks_timeLockedMicros_w;
	private double diff_db_sum_locks_timeLockedMicros_r;
	private double diff_db_sum_locks_timeLockedMicros_w;
	
	private double recordStats_accessesNotInMemory;
	private double recordStats_pageFaultExceptionsThrown;
		
	private double ok;
	private List<DbStatus> dbInfos;
	private int error;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRegtime() {
		return regtime;
	}
	public void setRegtime(String regtime) {
		this.regtime = regtime;
	}
	public int getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(int groupCode) {
		this.groupCode = groupCode;
	}
//	public String getUid() {
//		return uid;
//	}
//	public void setUid(String uid) {
//		this.uid = uid;
//	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getProcess() {
		return process;
	}
	public void setProcess(String process) {
		this.process = process;
	}
	public double getUptime() {
		return uptime;
	}
	public void setUptime(double uptime) {
		this.uptime = uptime;
	}
	public double getUptimeEstimate() {
		return uptimeEstimate;
	}
	public void setUptimeEstimate(double uptimeEstimate) {
		this.uptimeEstimate = uptimeEstimate;
	}
	public String getLocalTime() {
		return localTime;
	}
	public void setLocalTime(String localTime) {
		this.localTime = localTime;
	}
	public double getGlobalLock_totalTime() {
		return globalLock_totalTime;
	}
	public void setGlobalLock_totalTime(double globalLock_totalTime) {
		this.globalLock_totalTime = globalLock_totalTime;
	}
	public double getGlobalLock_lockTime() {
		return globalLock_lockTime;
	}
	public void setGlobalLock_lockTime(double globalLock_lockTime) {
		this.globalLock_lockTime = globalLock_lockTime;
	}
	public double getGlobalLock_ratio() {
		return globalLock_ratio;
	}
	public void setGlobalLock_ratio(double globalLock_ratio) {
		this.globalLock_ratio = globalLock_ratio;
	}
	public double getGlobalLock_currentQueue_total() {
		return globalLock_currentQueue_total;
	}
	public void setGlobalLock_currentQueue_total(
			double globalLock_currentQueue_total) {
		this.globalLock_currentQueue_total = globalLock_currentQueue_total;
	}
	public double getGlobalLock_currentQueue_readers() {
		return globalLock_currentQueue_readers;
	}
	public void setGlobalLock_currentQueue_readers(
			double globalLock_currentQueue_readers) {
		this.globalLock_currentQueue_readers = globalLock_currentQueue_readers;
	}
	public double getGlobalLock_currentQueue_writers() {
		return globalLock_currentQueue_writers;
	}
	public void setGlobalLock_currentQueue_writers(
			double globalLock_currentQueue_writers) {
		this.globalLock_currentQueue_writers = globalLock_currentQueue_writers;
	}
	public double getGlobalLock_activeClients_total() {
		return globalLock_activeClients_total;
	}
	public void setGlobalLock_activeClients_total(
			double globalLock_activeClients_total) {
		this.globalLock_activeClients_total = globalLock_activeClients_total;
	}
	public double getGlobalLock_activeClients_readers() {
		return globalLock_activeClients_readers;
	}
	public void setGlobalLock_activeClients_readers(
			double globalLock_activeClients_readers) {
		this.globalLock_activeClients_readers = globalLock_activeClients_readers;
	}
	public double getGlobalLock_activeClients_writers() {
		return globalLock_activeClients_writers;
	}
	public void setGlobalLock_activeClients_writers(
			double globalLock_activeClients_writers) {
		this.globalLock_activeClients_writers = globalLock_activeClients_writers;
	}
	public double getMem_bits() {
		return mem_bits;
	}
	public void setMem_bits(double mem_bits) {
		this.mem_bits = mem_bits;
	}
	public double getMem_resident() {
		return mem_resident;
	}
	public void setMem_resident(double mem_resident) {
		this.mem_resident = mem_resident;
	}
	public double getMem_virtual() {
		return mem_virtual;
	}
	public void setMem_virtual(double mem_virtual) {
		this.mem_virtual = mem_virtual;
	}
	public String getMem_supported() {
		return mem_supported;
	}
	public void setMem_supported(String mem_supported) {
		this.mem_supported = mem_supported;
	}
	public double getMem_mapped() {
		return mem_mapped;
	}
	public void setMem_mapped(double mem_mapped) {
		this.mem_mapped = mem_mapped;
	}
	public double getMem_mappedWithJournal() {
		return mem_mappedWithJournal;
	}
	public void setMem_mappedWithJournal(double mem_mappedWithJournal) {
		this.mem_mappedWithJournal = mem_mappedWithJournal;
	}
	public double getConnections_current() {
		return connections_current;
	}
	public void setConnections_current(double connections_current) {
		this.connections_current = connections_current;
	}
	public double getConnections_available() {
		return connections_available;
	}
	public void setConnections_available(double connections_available) {
		this.connections_available = connections_available;
	}
	public double getExtra_info_heap_usage_bytes() {
		return extra_info_heap_usage_bytes;
	}
	public void setExtra_info_heap_usage_bytes(double extra_info_heap_usage_bytes) {
		this.extra_info_heap_usage_bytes = extra_info_heap_usage_bytes;
	}
	public double getExtra_info_page_faults() {
		return extra_info_page_faults;
	}
	public void setExtra_info_page_faults(double extra_info_page_faults) {
		this.extra_info_page_faults = extra_info_page_faults;
	}
	public double getIndexCounters_btree_accesses() {
		return indexCounters_btree_accesses;
	}
	public void setIndexCounters_btree_accesses(double indexCounters_btree_accesses) {
		this.indexCounters_btree_accesses = indexCounters_btree_accesses;
	}
	public double getIndexCounters_btree_hits() {
		return indexCounters_btree_hits;
	}
	public void setIndexCounters_btree_hits(double indexCounters_btree_hits) {
		this.indexCounters_btree_hits = indexCounters_btree_hits;
	}
	public double getIndexCounters_btree_misses() {
		return indexCounters_btree_misses;
	}
	public void setIndexCounters_btree_misses(double indexCounters_btree_misses) {
		this.indexCounters_btree_misses = indexCounters_btree_misses;
	}
	public double getIndexCounters_btree_resets() {
		return indexCounters_btree_resets;
	}
	public void setIndexCounters_btree_resets(double indexCounters_btree_resets) {
		this.indexCounters_btree_resets = indexCounters_btree_resets;
	}
	public double getIndexCounters_btree_missRatio() {
		return indexCounters_btree_missRatio;
	}
	public void setIndexCounters_btree_missRatio(
			double indexCounters_btree_missRatio) {
		this.indexCounters_btree_missRatio = indexCounters_btree_missRatio;
	}
	public double getBackgroundFlushing_flushes() {
		return backgroundFlushing_flushes;
	}
	public void setBackgroundFlushing_flushes(double backgroundFlushing_flushes) {
		this.backgroundFlushing_flushes = backgroundFlushing_flushes;
	}
	public double getBackgroundFlushing_total_ms() {
		return backgroundFlushing_total_ms;
	}
	public void setBackgroundFlushing_total_ms(double backgroundFlushing_total_ms) {
		this.backgroundFlushing_total_ms = backgroundFlushing_total_ms;
	}
	public double getBackgroundFlushing_average_ms() {
		return backgroundFlushing_average_ms;
	}
	public void setBackgroundFlushing_average_ms(
			double backgroundFlushing_average_ms) {
		this.backgroundFlushing_average_ms = backgroundFlushing_average_ms;
	}
	public double getBackgroundFlushing_last_ms() {
		return backgroundFlushing_last_ms;
	}
	public void setBackgroundFlushing_last_ms(double backgroundFlushing_last_ms) {
		this.backgroundFlushing_last_ms = backgroundFlushing_last_ms;
	}
	public String getBackgroundFlushing_last_finished() {
		return backgroundFlushing_last_finished;
	}
	public void setBackgroundFlushing_last_finished(
			String backgroundFlushing_last_finished) {
		this.backgroundFlushing_last_finished = backgroundFlushing_last_finished;
	}
	public double getCursors_totalOpen() {
		return cursors_totalOpen;
	}
	public void setCursors_totalOpen(double cursors_totalOpen) {
		this.cursors_totalOpen = cursors_totalOpen;
	}
	public double getCursors_clientCursors_size() {
		return cursors_clientCursors_size;
	}
	public void setCursors_clientCursors_size(double cursors_clientCursors_size) {
		this.cursors_clientCursors_size = cursors_clientCursors_size;
	}
	public double getCursors_timedOut() {
		return cursors_timedOut;
	}
	public void setCursors_timedOut(double cursors_timedOut) {
		this.cursors_timedOut = cursors_timedOut;
	}
	public double getNetwork_bytesIn() {
		return network_bytesIn;
	}
	public void setNetwork_bytesIn(double network_bytesIn) {
		this.network_bytesIn = network_bytesIn;
	}
	public double getNetwork_bytesOut() {
		return network_bytesOut;
	}
	public void setNetwork_bytesOut(double network_bytesOut) {
		this.network_bytesOut = network_bytesOut;
	}
	public double getNetwork_numRequests() {
		return network_numRequests;
	}
	public void setNetwork_numRequests(double network_numRequests) {
		this.network_numRequests = network_numRequests;
	}
	public String getRepl_setName() {
		return repl_setName;
	}
	public void setRepl_setName(String repl_setName) {
		this.repl_setName = repl_setName;
	}
	public String getRepl_ismaster() {
		return repl_ismaster;
	}
	public void setRepl_ismaster(String repl_ismaster) {
		this.repl_ismaster = repl_ismaster;
	}
	public String getRepl_secondary() {
		return repl_secondary;
	}
	public void setRepl_secondary(String repl_secondary) {
		this.repl_secondary = repl_secondary;
	}
	public String getRepl_hosts() {
		return repl_hosts;
	}
	public void setRepl_hosts(String repl_hosts) {
		this.repl_hosts = repl_hosts;
	}
	public double getOpcounters_insert() {
		return opcounters_insert;
	}
	public void setOpcounters_insert(double opcounters_insert) {
		this.opcounters_insert = opcounters_insert;
	}
	public double getOpcounters_query() {
		return opcounters_query;
	}
	public void setOpcounters_query(double opcounters_query) {
		this.opcounters_query = opcounters_query;
	}
	public double getOpcounters_update() {
		return opcounters_update;
	}
	public void setOpcounters_update(double opcounters_update) {
		this.opcounters_update = opcounters_update;
	}
	public double getOpcounters_delete() {
		return opcounters_delete;
	}
	public void setOpcounters_delete(double opcounters_delete) {
		this.opcounters_delete = opcounters_delete;
	}
	public double getOpcounters_getmore() {
		return opcounters_getmore;
	}
	public void setOpcounters_getmore(double opcounters_getmore) {
		this.opcounters_getmore = opcounters_getmore;
	}
	public double getOpcounters_command() {
		return opcounters_command;
	}
	public void setOpcounters_command(double opcounters_command) {
		this.opcounters_command = opcounters_command;
	}
	public double getAsserts_regular() {
		return asserts_regular;
	}
	public void setAsserts_regular(double asserts_regular) {
		this.asserts_regular = asserts_regular;
	}
	public double getAsserts_warning() {
		return asserts_warning;
	}
	public void setAsserts_warning(double asserts_warning) {
		this.asserts_warning = asserts_warning;
	}
	public double getAsserts_msg() {
		return asserts_msg;
	}
	public void setAsserts_msg(double asserts_msg) {
		this.asserts_msg = asserts_msg;
	}
	public double getAsserts_user() {
		return asserts_user;
	}
	public void setAsserts_user(double asserts_user) {
		this.asserts_user = asserts_user;
	}
	public double getAsserts_rollovers() {
		return asserts_rollovers;
	}
	public void setAsserts_rollovers(double asserts_rollovers) {
		this.asserts_rollovers = asserts_rollovers;
	}
	public String getWriteBacksQueued() {
		return writeBacksQueued;
	}
	public void setWriteBacksQueued(String writeBacksQueued) {
		this.writeBacksQueued = writeBacksQueued;
	}
	public double getDur_commits() {
		return dur_commits;
	}
	public void setDur_commits(double dur_commits) {
		this.dur_commits = dur_commits;
	}
	public double getDur_journaledMB() {
		return dur_journaledMB;
	}
	public void setDur_journaledMB(double dur_journaledMB) {
		this.dur_journaledMB = dur_journaledMB;
	}
	public double getDur_writeToDataFilesMB() {
		return dur_writeToDataFilesMB;
	}
	public void setDur_writeToDataFilesMB(double dur_writeToDataFilesMB) {
		this.dur_writeToDataFilesMB = dur_writeToDataFilesMB;
	}
	public double getDur_commitslnWriteLock() {
		return dur_commitslnWriteLock;
	}
	public void setDur_commitslnWriteLock(double dur_commitslnWriteLock) {
		this.dur_commitslnWriteLock = dur_commitslnWriteLock;
	}
	public double getDur_earlyCommits() {
		return dur_earlyCommits;
	}
	public void setDur_earlyCommits(double dur_earlyCommits) {
		this.dur_earlyCommits = dur_earlyCommits;
	}
	public double getDur_timeMs_dt() {
		return dur_timeMs_dt;
	}
	public void setDur_timeMs_dt(double dur_timeMs_dt) {
		this.dur_timeMs_dt = dur_timeMs_dt;
	}
	public double getDur_timeMs_prepLogBuffer() {
		return dur_timeMs_prepLogBuffer;
	}
	public void setDur_timeMs_prepLogBuffer(double dur_timeMs_prepLogBuffer) {
		this.dur_timeMs_prepLogBuffer = dur_timeMs_prepLogBuffer;
	}
	public double getDur_timeMs_writeToJournal() {
		return dur_timeMs_writeToJournal;
	}
	public void setDur_timeMs_writeToJournal(double dur_timeMs_writeToJournal) {
		this.dur_timeMs_writeToJournal = dur_timeMs_writeToJournal;
	}
	public double getDur_timeMs_writeToDataFiles() {
		return dur_timeMs_writeToDataFiles;
	}
	public void setDur_timeMs_writeToDataFiles(double dur_timeMs_writeToDataFiles) {
		this.dur_timeMs_writeToDataFiles = dur_timeMs_writeToDataFiles;
	}
	public double getDur_timeMs_remapPrivateView() {
		return dur_timeMs_remapPrivateView;
	}
	public void setDur_timeMs_remapPrivateView(double dur_timeMs_remapPrivateView) {
		this.dur_timeMs_remapPrivateView = dur_timeMs_remapPrivateView;
	}
	public double getDur_compression() {
		return dur_compression;
	}
	public void setDur_compression(double dur_compression) {
		this.dur_compression = dur_compression;
	}
	public double getDbObjects() {
		return dbObjects;
	}
	public void setDbObjects(double dbObjects) {
		this.dbObjects = dbObjects;
	}
	public double getDbAvgObjSize() {
		return dbAvgObjSize;
	}
	public void setDbAvgObjSize(double dbAvgObjSize) {
		this.dbAvgObjSize = dbAvgObjSize;
	}
	public double getDbDataSize() {
		return dbDataSize;
	}
	public void setDbDataSize(double dbDataSize) {
		this.dbDataSize = dbDataSize;
	}
	public double getDbStorageSize() {
		return dbStorageSize;
	}
	public void setDbStorageSize(double dbStorageSize) {
		this.dbStorageSize = dbStorageSize;
	}
	public double getDbNumExtents() {
		return dbNumExtents;
	}
	public void setDbNumExtents(double dbNumExtents) {
		this.dbNumExtents = dbNumExtents;
	}
	public double getDbCount() {
		return dbCount;
	}
	public void setDbCount(double dbCount) {
		this.dbCount = dbCount;
	}
	public double getDbIndexes() {
		return dbIndexes;
	}
	public void setDbIndexes(double dbIndexes) {
		this.dbIndexes = dbIndexes;
	}
	public double getDbIndexSize() {
		return dbIndexSize;
	}
	public void setDbIndexSize(double dbIndexSize) {
		this.dbIndexSize = dbIndexSize;
	}
	public double getDbFileSize() {
		return dbFileSize;
	}
	public void setDbFileSize(double dbFileSize) {
		this.dbFileSize = dbFileSize;
	}
	public double getDbNsSizeMB() {
		return dbNsSizeMB;
	}
	public void setDbNsSizeMB(double dbNsSizeMB) {
		this.dbNsSizeMB = dbNsSizeMB;
	}
	public double getOk() {
		return ok;
	}
	public void setOk(double ok) {
		this.ok = ok;
	}
	public List<DbStatus> getDbInfos() {
		return dbInfos;
	}
	public void setDbInfos(List<DbStatus> dbInfos) {
		this.dbInfos = dbInfos;
	}
	public int getError() {
		return error;
	}
	public void setError(int error) {
		this.error = error;
	}
	public int getDeviceCode() {
		return deviceCode;
	}
	public void setDeviceCode(int deviceCode) {
		this.deviceCode = deviceCode;
	}
	public int getIdx() {
		return idx;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}
	public double getDiff_globalLock_lockTime() {
		return diff_globalLock_lockTime;
	}
	public void setDiff_globalLock_lockTime(double diff_globalLock_lockTime) {
		this.diff_globalLock_lockTime = diff_globalLock_lockTime;
	}
	public double getDiff_extra_info_page_faults() {
		return diff_extra_info_page_faults;
	}
	public void setDiff_extra_info_page_faults(double diff_extra_info_page_faults) {
		this.diff_extra_info_page_faults = diff_extra_info_page_faults;
	}
	public double getDiff_opcounters_insert() {
		return diff_opcounters_insert;
	}
	public void setDiff_opcounters_insert(double diff_opcounters_insert) {
		this.diff_opcounters_insert = diff_opcounters_insert;
	}
	public double getDiff_opcounters_query() {
		return diff_opcounters_query;
	}
	public void setDiff_opcounters_query(double diff_opcounters_query) {
		this.diff_opcounters_query = diff_opcounters_query;
	}
	public double getDiff_opcounters_update() {
		return diff_opcounters_update;
	}
	public void setDiff_opcounters_update(double diff_opcounters_update) {
		this.diff_opcounters_update = diff_opcounters_update;
	}
	public double getDiff_opcounters_delete() {
		return diff_opcounters_delete;
	}
	public void setDiff_opcounters_delete(double diff_opcounters_delete) {
		this.diff_opcounters_delete = diff_opcounters_delete;
	}
	public double getLocks_timeLockedMicros_R() {
		return locks_timeLockedMicros_R;
	}
	public void setLocks_timeLockedMicros_R(double locks_timeLockedMicros_R) {
		this.locks_timeLockedMicros_R = locks_timeLockedMicros_R;
	}
	public double getLocks_timeLockedMicros_W() {
		return locks_timeLockedMicros_W;
	}
	public void setLocks_timeLockedMicros_W(double locks_timeLockedMicros_W) {
		this.locks_timeLockedMicros_W = locks_timeLockedMicros_W;
	}
	public double getDb_sum_locks_timeLockedMicros_r() {
		return db_sum_locks_timeLockedMicros_r;
	}
	public void setDb_sum_locks_timeLockedMicros_r(
			double db_sum_locks_timeLockedMicros_r) {
		this.db_sum_locks_timeLockedMicros_r = db_sum_locks_timeLockedMicros_r;
	}
	public double getDb_sum_locks_timeLockedMicros_w() {
		return db_sum_locks_timeLockedMicros_w;
	}
	public void setDb_sum_locks_timeLockedMicros_w(
			double db_sum_locks_timeLockedMicros_w) {
		this.db_sum_locks_timeLockedMicros_w = db_sum_locks_timeLockedMicros_w;
	}
	public double getLocks_timeAcquiringMicros_R() {
		return locks_timeAcquiringMicros_R;
	}
	public void setLocks_timeAcquiringMicros_R(double locks_timeAcquiringMicros_R) {
		this.locks_timeAcquiringMicros_R = locks_timeAcquiringMicros_R;
	}
	public double getLocks_timeAcquiringMicros_W() {
		return locks_timeAcquiringMicros_W;
	}
	public void setLocks_timeAcquiringMicros_W(double locks_timeAcquiringMicros_W) {
		this.locks_timeAcquiringMicros_W = locks_timeAcquiringMicros_W;
	}
	public double getRecordStats_accessesNotInMemory() {
		return recordStats_accessesNotInMemory;
	}
	public void setRecordStats_accessesNotInMemory(
			double recordStats_accessesNotInMemory) {
		this.recordStats_accessesNotInMemory = recordStats_accessesNotInMemory;
	}
	public double getRecordStats_pageFaultExceptionsThrown() {
		return recordStats_pageFaultExceptionsThrown;
	}
	public void setRecordStats_pageFaultExceptionsThrown(
			double recordStats_pageFaultExceptionsThrown) {
		this.recordStats_pageFaultExceptionsThrown = recordStats_pageFaultExceptionsThrown;
	}
	public double getDiff_locks_timeLockedMicros_R() {
		return diff_locks_timeLockedMicros_R;
	}
	public void setDiff_locks_timeLockedMicros_R(
			double diff_locks_timeLockedMicros_R) {
		this.diff_locks_timeLockedMicros_R = diff_locks_timeLockedMicros_R;
	}
	public double getDiff_locks_timeLockedMicros_W() {
		return diff_locks_timeLockedMicros_W;
	}
	public void setDiff_locks_timeLockedMicros_W(
			double diff_locks_timeLockedMicros_W) {
		this.diff_locks_timeLockedMicros_W = diff_locks_timeLockedMicros_W;
	}
	public double getDiff_db_sum_locks_timeLockedMicros_r() {
		return diff_db_sum_locks_timeLockedMicros_r;
	}
	public void setDiff_db_sum_locks_timeLockedMicros_r(
			double diff_db_sum_locks_timeLockedMicros_r) {
		this.diff_db_sum_locks_timeLockedMicros_r = diff_db_sum_locks_timeLockedMicros_r;
	}
	public double getDiff_db_sum_locks_timeLockedMicros_w() {
		return diff_db_sum_locks_timeLockedMicros_w;
	}
	public void setDiff_db_sum_locks_timeLockedMicros_w(
			double diff_db_sum_locks_timeLockedMicros_w) {
		this.diff_db_sum_locks_timeLockedMicros_w = diff_db_sum_locks_timeLockedMicros_w;
	}
	public double getDiff_dbDataSize() {
		return diff_dbDataSize;
	}
	public void setDiff_dbDataSize(double diff_dbDataSize) {
		this.diff_dbDataSize = diff_dbDataSize;
	}
	public double getDiff_dbIndexSize() {
		return diff_dbIndexSize;
	}
	public void setDiff_dbIndexSize(double diff_dbIndexSize) {
		this.diff_dbIndexSize = diff_dbIndexSize;
	}	
	
}
