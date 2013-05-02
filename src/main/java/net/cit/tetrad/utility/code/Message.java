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
