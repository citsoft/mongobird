package net.cit.tetrad.rrd.utils;

import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class MongoWrapper {
	private Logger logger = Logger.getLogger(this.getClass());
	private String host;
	private int port;
	private Mongo mongo;
	
	public void connect(String host, int port) {
		try {
			this.host = host;
			this.port= port;
			mongo = new Mongo(host, port);
		} catch (Exception e) {
			logger.error(e, e);
		}
	}
	
	 public Mongo getMongo() {
		 return mongo;
	 }
	 
	 public boolean isMongoConnected() {
		 return mongo != null;
	 }
	 
	 public void reconnect() {
		 try {
			mongo = new Mongo(host, port);
		} catch (UnknownHostException e) {
			logger.error(e, e);
		} catch (MongoException e) {
			logger.error(e, e);
		}
	 }
}
