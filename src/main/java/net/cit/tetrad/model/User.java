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
