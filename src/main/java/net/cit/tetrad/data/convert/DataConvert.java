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
