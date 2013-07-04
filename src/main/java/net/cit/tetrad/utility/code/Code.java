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
package net.cit.tetrad.utility.code;

public enum Code {

	group{
		public CodeInterface getCode(){
			return new GroupCode();
		}
	},
	device{
		public CodeInterface getCode(){
			return new DeviceCode();
		}
	},
	user{
		public CodeInterface getCode(){
			return new UserCode();
		}
	},
	unit{
		public CodeInterface getCode(){
			return new UnitCode();
		}
	},
	event{
		public CodeInterface getCode(){
			return new EventCode();
		}
	},
	type{
		public CodeInterface getCode(){
			return new TypeCode();
		}
	},
	mongosEvent{
		public CodeInterface getCode(){
			return new MongosEventCode();
		}
	},
	mongodEvent{
		public CodeInterface getCode(){
			return new MongodEventCode();
		}
	},
	mongodEvent2_2{
		public CodeInterface getCode(){
			return new MongodEventCode2_2();
		}
	};

	public abstract CodeInterface getCode();
	
}
