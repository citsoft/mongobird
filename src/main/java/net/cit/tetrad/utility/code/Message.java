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

import java.util.Locale;

import org.springframework.context.support.MessageSourceAccessor;

public class Message {
	private static MessageSourceAccessor msAcc = null;
    public void setMessageSourceAccessor(MessageSourceAccessor msAcc) {
       this.msAcc = msAcc;
   }

   /**
    * KEY에 해당하는 메세지 반환
    * 
    * @param  key
    * @return
    */
   public static String getMessage(String key, Locale locale) {
       return msAcc.getMessage(key, locale);
   }

   /**
    * KEY에 해당하는 메세지 반환
    * 
    * @param  key
    * @return
    */
   public static String getMessage(String key, Object[] objs, Locale locale) {
     return msAcc.getMessage(key, objs, locale);
   }
}
