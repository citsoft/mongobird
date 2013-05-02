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
