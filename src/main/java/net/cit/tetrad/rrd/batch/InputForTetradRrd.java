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
package net.cit.tetrad.rrd.batch;

import static net.cit.tetrad.common.PropertiesNames.XML_PATH;

import org.apache.log4j.Logger;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class InputForTetradRrd {

	private static Logger logger = Logger.getLogger(InputForTetradRrd.class);
	
	public static void main(String[] args) {
		try {
			logger.debug("InputForTetradRrd Start!!");
			
			// 설정파일로부터 스프링컨테이너 생성
			String[] configLocations = new String[] { XML_PATH + "applicationContext_management.xml", 
													  "applicationContext-mongo.xml", 
													  "applicationContext_rrd.xml" };
			AbstractApplicationContext context = new ClassPathXmlApplicationContext(configLocations);
			
			// TetradRrd에 데이터를 입력하기 위한 인스턴스 생성
			TetradRrdInitializer tetradRrdInitializer = (TetradRrdInitializer)context.getBean("tetradRrdInitializer");
			tetradRrdInitializer.input();
			
			logger.debug("InputForTetradRrd End!!");
		} catch (Exception e) {
			logger.info("----------------------------------------------------------------");
			logger.info(e.getMessage());
			logger.info("----------------------------------------------------------------");
		}
	}

}
