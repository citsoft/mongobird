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
package net.cit.tetrad.resource;

import static net.cit.tetrad.common.ColumnConstent.*;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.cit.tetrad.common.Config;
import net.cit.tetrad.model.CommonDto;
import net.cit.tetrad.model.Global;
import net.cit.tetrad.model.User;
import net.cit.tetrad.utility.StringUtils;

import org.apache.log4j.Logger;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginResource extends DefaultResource{

	private Logger log = Logger.getLogger(this.getClass());
	
	@RequestMapping("/loginView.do")
	public ModelAndView loginView()throws Exception{
		ModelAndView mav = new ModelAndView();
		mav.setViewName("login");
		return mav;
	}
	
	@RequestMapping("/popup_login.do")
	public ModelAndView popup_login()throws Exception{
		ModelAndView mav = new ModelAndView();
		mav.setViewName("popup_login");
		return mav;
	}
	
	@RequestMapping("/login.do")
	public ModelAndView login(HttpServletRequest request,CommonDto dto) throws Exception{
		log.debug("login start");
		
		ModelAndView mav = new ModelAndView();
		Query query=new Query();
		try{
			int count = (int) monadService.getCount(query, User.class);
			if(count==0){
				insertSuperUser();
				mav.setViewName("login");
			}else{
				User user = doLogin(dto, mav);
				HttpSession session = request.getSession();
				session.setAttribute("loginUserCode", user.getIdx());
				session.setAttribute("loginAuth", user.getAuthority());
				session.setMaxInactiveInterval(1800);
			}
		}catch(Exception e){
			dto.setMessage("오류가 발생하였습니다.");
		}
		mav.addObject("comm", dto);
		log.debug("login end");
		return mav;
	}
	
	private void insertSuperUser(){
		CommonDto insertDto = new CommonDto();
		String tablenm = MAV_USER;
		try{
			int idx = indexDao.createIdx(tablenm);
			insertDto.setIdx(idx);
			insertDto.setUid(ADMIN);
			insertDto.setPasswd(ADMIN);
			insertDto.setAuthority(2);
			Object obj = managementDao.setDto(tablenm, insertDto);//각각 dto에 commondto에서 받은 값을 set
			monadService.add(obj, User.class);
		}catch(Exception e){
			
		}
	}
	
	private User doLogin(CommonDto dto, ModelAndView mav) throws Exception {
		User user = null;
		if(!StringUtils.isNull(dto.getUid()) && !StringUtils.isNull(dto.getPasswd()))user = existUser(dto.getUid(), dto.getPasswd());
		if(user == null){
			dto.setMessage("아이디가 없거나 비밀번호가 맞지 않습니다.");
			mav.setViewName("login");
		}else{
			mav.setViewName("redirect:/mainView.do");//메인페이지를 부르는 .do를 호출
		}
		return user;
	}
	
	private User existUser(String id, String password) throws Exception{
		Query query=new Query();
		String hashPasswd = managementDao.makePasswd(password);
		query = loginDao.findUidPasswd(id, hashPasswd);
		return (User) monadService.getFind(query, User.class);
	}
}
