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
