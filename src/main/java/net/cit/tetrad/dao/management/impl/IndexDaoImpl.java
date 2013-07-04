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
package net.cit.tetrad.dao.management.impl;

import static net.cit.tetrad.utility.QueryUtils.setCollection;
import net.cit.tetrad.dao.management.IndexDao;
import net.cit.tetrad.model.Index;
import net.cit.tetrad.monad.MonadService;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class IndexDaoImpl implements IndexDao{

	private MonadService monadService;
	
	public void setMonadService(MonadService monadService) {
		this.monadService = monadService;
	}
	
	/**
	 * idx 없을 때 새로 삽입 있을때 +1
	 * @throws Exception 
	 */
	public int createIdx(String tablenm) throws Exception{
		Index idto = new Index();
		Query query=new Query();
		query = setCollection(tablenm);//collecionname 쿼리
		idto = (Index)monadService.getFind(query, Index.class);//collectionname이 Device인 idx값
		int idx = 0;
		if(idto==null){
			//index collection에 값이 아예 없을땐
			idto = new Index();
			idto.setIdx(1);
			idto.setCollectionnm(tablenm);
			monadService.add(idto, Index.class);
			idx = 1;//첫번째 이기때문에 idx가 1로 들어감
		}else{
			//index의 idx값 update
			Update update = new Update();
//			update.inc("idx",1);
			idx = idto.getIdx()+1;
			update.set("idx",idx);
			monadService.update(query, update, Index.class);
		}
		return idx;
	}
}
