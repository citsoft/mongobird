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
package net.cit.tetrad.dao.management;

import net.cit.tetrad.model.CommonDto;
import org.springframework.data.mongodb.core.query.Update;


public interface ManagementDao {
	
	public Class<?> getDtoClassNm(String dtoClassNm);

	public Object setDto(String dtoClassNm, CommonDto dto);

	public Update setUpdate(String dtoClassNm, CommonDto dto);

	public String makePasswd(String passwd);
	
	public String mongoExistCheck(String ip, int port, String userStr, String passwdStr, String isExistMongo);

	public String timeStampToString(String timeStampStr);
}
