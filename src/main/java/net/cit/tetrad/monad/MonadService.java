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
package net.cit.tetrad.monad;

import java.util.Collection;
import java.util.List;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.CommandResult;
import com.mongodb.DBObject;


public interface MonadService {

	public void updateMulti(Query query, Update update, String collectionName);
	
	public void updateMulti(Query query, Update update, String collectionName, boolean upsert);

	public long getCount(Query query, Class<?> className);

	public List<Object> getList(Query query, Class<?> className);

	public List<Object> getListWithStrCollName(Query query,Class<?> className, String collectionName);
	
	public void delete(Query query, Class<?> className);
	
	public void delete(Query query, String className) ;

	public void listAdd(List<Object> userList, Class<?> className);

	public Object getFind(Query query, Class<?> className);

	public void add(Object obj, Class<?> className);

	public void update(Query query, Update update, Class<?> className);

	public Object getFindOne(Query query, Class<?> className, String collectionName);

	public long getCount(Query query, String collectionName);
	
	CommandResult command(DBObject cmd);
	void insert(Collection<? extends Object> batchToSave, String collectionName);
	void dropCollection(String collection);
}
