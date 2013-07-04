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
package net.cit.tetrad.data.convert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.cit.monad.Operations;
import net.cit.tetrad.model.Device;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DataConvert {
	private Operations operations;	
	
	public void setOperations(Operations operations) {
		this.operations = operations;
	}

	@RequestMapping("/convert.do")
	public ModelAndView mainView() throws Exception{
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/data/convert/convert");
		boolean canConvert = true;
		Device device = operations.findOne(new Query(), Device.class);
		if (device != null) {
			canConvert = false;
		}
		mav.addObject("canConvert", canConvert);
		
		return mav;
	}
	
	@RequestMapping("/dataconvert.do")
	public ModelAndView convert(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String host = request.getParameter("host");
		String port = request.getParameter("port");
		String database = request.getParameter("database");
		
		String startDate = request.getParameter("startdt");
		DataConvertThread convertTread = new DataConvertThread(host, port, database, startDate);
		DataConvertThread.CONVERT_STATUS = 1;
		convertTread.start();
		ModelAndView mav = new ModelAndView();
		mav.addObject("convert_status", DataConvertThread.CONVERT_STATUS);
		mav.addObject("convert_db_tot", DataConvertThread.CONVERT_DBDATA_TOT);
		mav.addObject("convert_db_cnt", DataConvertThread.CONVERT_DBDATA_CNT);
		mav.addObject("convert_server_tot", DataConvertThread.CONVERT_SERVERDATA_TOT);
		mav.addObject("convert_server_cnt", DataConvertThread.CONVERT_SERVERDATA_CNT);
		mav.setViewName("/data/convert/convertStatus");
		return mav;
	}
	
	@RequestMapping("/convertStatus.do")
	public ModelAndView convertStatus(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("convert_status", DataConvertThread.CONVERT_STATUS);
		mav.addObject("convert_db_tot", DataConvertThread.CONVERT_DBDATA_TOT);
		mav.addObject("convert_db_cnt", DataConvertThread.CONVERT_DBDATA_CNT);
		mav.addObject("convert_server_tot", DataConvertThread.CONVERT_SERVERDATA_TOT);
		mav.addObject("convert_server_cnt", DataConvertThread.CONVERT_SERVERDATA_CNT);
		
		mav.setViewName("/data/convert/convertStatus");	
		return mav;
	}
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
//		String[] configLocations = new String[]{"applicationContext_convert.xml"};
//		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(configLocations);
		
//		DataConvert convert = (DataConvert)context.getBean("dataConvert");
//		convert.initial("20110701");
//		DataConvert convert = new DataConvert();
//		convert.test("localhost", "27017", "tetrad", "20110701");
//		convert.input();
	}

}
