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
package net.cit.tetrad.monad.impl;

import java.util.Collection;
import java.util.List;

import net.cit.monad.Operations;
import net.cit.tetrad.monad.MonadService;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.CommandResult;
import com.mongodb.DBObject;

/**
 * mongo 이관 클래스
 * @author jaemin
 *
 */
public class MonadServiceImpl implements MonadService {

private Operations operations;
	
	public void setOperations(Operations operations) {
		this.operations = operations;
	}
	
	@Override
	public void listAdd(List<Object> userList,Class<?> className) {
		operations.insert(userList, className);
	}
	
	@Override
	public void add(Object obj,Class<?> className) {
		operations.insert(obj);
	}
	
	@Override
	public void update(Query query, Update update, Class<?> className) {
		operations.updateFirst(query, update, className);
	}
	
	@Override
	public void updateMulti(Query query, Update update, String collectionName) {
		operations.updateMulti(query, update, collectionName,true);
	}
	
	@Override
	public long getCount(Query query,Class<?> className) {
		long cnt=operations.count(query, className);
		return cnt;
	}
	
	@Override
	public long getCount(Query query,String collectionName) {
		long cnt=operations.count(query, collectionName);
		return cnt;
	}
	
	@Override
	public Object getFind(Query query,Class<?> className) {
		Object obj = operations.findOne(query, className);
		return obj;
	}
	
	@Override
	public Object getFindOne(Query query,Class<?> className, String collectionName) {
		Object obj = operations.findOne(query, className, collectionName);
		return obj;
	}
	
	@Override
	public List<Object> getList(Query query,Class<?> className) {
		List<Object> lst= (List<Object>) operations.find(query, className);
		return lst;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Object> getListWithStrCollName(Query query,Class<?> className, String collectionName) {
		List<Object> lst= (List<Object>) operations.find(query, className, collectionName);
		return lst;
	}
	
	@Override
	public void delete(Query query,Class<?> className) {
		operations.remove(query, className);
	}
	
	@Override
	public void delete(Query query, String className) {
		operations.remove(query, className);
	}
	
	public CommandResult command(DBObject cmd) {
		return operations.executeCommand(cmd);
	}
	
	public void insert(Collection<? extends Object> batchToSave, String collectionName) {
		operations.insert(batchToSave, collectionName);
	}
	
	public void dropCollection(String collection) {
		operations.dropCollection(collection);
	}
}
