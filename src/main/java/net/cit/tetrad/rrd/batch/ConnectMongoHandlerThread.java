package net.cit.tetrad.rrd.batch;

import java.util.concurrent.ConcurrentLinkedQueue;

import net.cit.tetrad.rrd.utils.MongoWrapper;

public class ConnectMongoHandlerThread implements Runnable {

	@Override
	public void run() {
		for ( ; ; ) {
			ConcurrentLinkedQueue<MongoWrapper> toReconnectingMongos = MongoInMemory.getToReconnectingMongos();
			MongoWrapper reconnectMongo = toReconnectingMongos.poll();
			if (reconnectMongo == null) {
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				reconnectMongo.reconnect();
			}
			
			System.out.println("toReconnectingMongos : " + toReconnectingMongos);
		}
	}
}
