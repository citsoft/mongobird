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
package net.cit.tetrad.resource;

import static net.cit.tetrad.common.ColumnConstent.*;
import static net.cit.tetrad.common.PropertiesNames.*;
import net.cit.tetrad.command.service.TetradCommandService;
import net.cit.tetrad.dao.management.AdminDao;
import net.cit.tetrad.dao.management.IndexDao;
import net.cit.tetrad.dao.management.LoginDao;
import net.cit.tetrad.dao.management.MainDao;
import net.cit.tetrad.dao.management.ManagementDao;
import net.cit.tetrad.dao.management.SubDao;
import net.cit.tetrad.monad.MonadService;
import net.cit.tetrad.rrd.dao.DataAccessObjectForMongo;
import net.cit.tetrad.rrd.service.TetradRrdDbService;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.servlet.ModelAndView;

public class DefaultResource {
	static MonadService monadService;
	String[] MONGOVER_TOTALFILE = IMGTOTALFIELD2_0;
	String mongoVer = "";
	int logRetentionPeriod = 7;
	boolean isMongoVer2_2 = false;
	String[] DSNAME = {"p_fault","global","r_mem","v_mem","conn","op_insert","op_query","op_update","op_delete","in_network","out_network"};
	
	String[] configLocations ;
	AbstractApplicationContext context ;
	String[] trconfigLocations ;
	AbstractApplicationContext trcontext ;
	
	MainDao mainDao;
	LoginDao loginDao;
	AdminDao adminDao;
	ManagementDao managementDao;
	IndexDao indexDao;
	SubDao subDao;
	
	DataAccessObjectForMongo daoForMongo;
	TetradRrdDbService rrdService;
	TetradCommandService comandService;
	
	DefaultResource(){
		configLocations = new String[] { XML_PATH + APPLICATIONCONTEXT_XML };
		context = new ClassPathXmlApplicationContext(configLocations);
		trconfigLocations = new String[] { "applicationContext_rrd.xml" };
		trcontext = new ClassPathXmlApplicationContext(trconfigLocations);
		mainDao = (MainDao) context.getBean("mainDao");
		loginDao = (LoginDao) context.getBean("loginDao");
		adminDao = (AdminDao) context.getBean(BEAN_AFMINDAO);
		managementDao = (ManagementDao) context.getBean(BEAN_MANAGEMENTDAO);
		indexDao = (IndexDao) context.getBean(BEAN_INDEXDAO);
		subDao = (SubDao) context.getBean(BEAN_SUBDAO);
		daoForMongo = (DataAccessObjectForMongo)trcontext.getBean("dataAccessObjectForMongo");
		monadService = (MonadService) context.getBean(BEAN_MONADSERVICEDAO);
		rrdService = (TetradRrdDbService) trcontext.getBean("tetradRrdDbService");
		comandService = (TetradCommandService)trcontext.getBean("commandService");
		
		mongoVer = mainDao.getGlobalMongoVersion();
		isMongoVer2_2 = (mongoVer.equals(MONGOVER2_2));
		logRetentionPeriod = mainDao.getLogRetentionPeriod();
		if(isMongoVer2_2){
			MONGOVER_TOTALFILE = IMGTOTALFIELD2_2;
		}
	}
	
	 ModelAndView commMav(){
		ModelAndView mav = new ModelAndView();
		mav.addObject("tablenm", TABLENAME);
		mav.addObject("mongoVer", mongoVer);
		mav.addObject("mainRefreshPeriodMinute", mainDao.mainRefreshPeriodMinute());
		return mav;
	}
}
