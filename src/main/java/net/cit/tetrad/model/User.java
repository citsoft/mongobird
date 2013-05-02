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
package net.cit.tetrad.model;

import org.springframework.data.mongodb.core.index.Indexed;

public class User{

	@Indexed(unique=true)
	private String uid;
	private int idx=0;
	private String reg_date;
	private String up_date;
	private String passwd;
	private String username;
	private String email;
	private String mobileFirst;
	private String mobileSecond;
	private String mobileThird;
	private int authority=0;
	
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public int getIdx() {
		return idx;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public int getAuthority() {
		return authority;
	}
	public void setAuthority(int authority) {
		this.authority = authority;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getReg_date() {
		return reg_date;
	}
	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}
	public String getUp_date() {
		return up_date;
	}
	public void setUp_date(String up_date) {
		this.up_date = up_date;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobileFirst() {
		return mobileFirst;
	}
	public void setMobileFirst(String mobileFirst) {
		this.mobileFirst = mobileFirst;
	}
	public String getMobileSecond() {
		return mobileSecond;
	}
	public void setMobileSecond(String mobileSecond) {
		this.mobileSecond = mobileSecond;
	}
	public String getMobileThird() {
		return mobileThird;
	}
	public void setMobileThird(String mobileThird) {
		this.mobileThird = mobileThird;
	}
}
