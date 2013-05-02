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
package net.cit.tetrad.dao.management.impl;

import static net.cit.tetrad.common.PropertiesNames.TYPE;

import java.util.ArrayList;
import java.util.List;

import net.cit.tetrad.dao.management.AdminDao;

public class AdminDaoImpl implements AdminDao {

	public List<Object> typeList(){
		List<Object> typeLst = new ArrayList<Object>();
		for(int i=0;i<TYPE.length;i++){
			typeLst.add(TYPE[i]);
		}
		return typeLst;
	}
}
