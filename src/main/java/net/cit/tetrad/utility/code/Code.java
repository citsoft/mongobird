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
