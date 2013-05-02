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
package net.cit.tetrad.common;

import java.util.Comparator;

/**
 * 공통함수
 */
@SuppressWarnings("restriction")
public class Utility {

	/**
     * NULL이면 공백으로 리턴
     * @param str
     * @return ""
     */
    public static String isNull(String str) {
        return str == null ? "" : str; 
    }
    
    /**
    * NULL이면 format으로 리턴
    * @param str
    * @param nullFormat
    * @return ""
    */
   public static String isNull(String str, String nullFormat) {
       return str == null ? nullFormat : str; 
   }
    
    /**
     * NULL이면 0으로 리턴
     * @param str
     * @return "0"
     */
    public static String isNullNumber(String str) {
        return str == null || str.equals("") ? "0" : str; 
    }
    
    /**
	 * 숫자를 두자릿 수 문자열로 변환한다.
	 * @param i
	 * @return
	 */
	public static String int2String(int i){
		String str = "";
		
		if(i < 10){
			str = "0" + Integer.toString(i);
		}else{
			str = Integer.toString(i);
		}
		
		return str;
	}
	
	/**
	 * 10보다 작은 숫자 앞에 0 붙이기
	 * @param val
	 * @return
	 */
	public static String intFormat(int val){
		String reval=Integer.toString(val);
		if(val<10) reval="0"+Integer.toString(val);
		return reval;
	}
	
	/**
	 * 문자열 길이 지정, 공백 처리
	 * @param str
	 * @param length
	 * @param al 2:center / 1:right / 0:left
	 * @return
	 */
	public static String stringFormat(String str, int length, int al) {    //포맷 메서드
		int diff = length - str.length();
		if(diff<0)return str.substring(0, length);
		
	     char[] source = str.toCharArray();   //str문자열로받은 매개변수를 source라는 배열에 할당
	     char[] result = new char[length]; 
	     
         for(int i=0; i < result.length; i++)   //출력
        	 result[i] = ' ';
         switch(al){
         	case 2 :
         		System.arraycopy(source, 0, result, diff/2, source.length);
         		break;
         	case 1 :
         		System.arraycopy(source, 0, result, diff, source.length);
         		break;
         	case 0 :
            default : 
            	System.arraycopy(source, 0, result, 0, source.length);
         } 
         return new String(result);
	}
		
	public static  Comparator codeKeySort = new Comparator() {
		public int compare (Object s1,  Object s2){
			Comparable ss1 =  (Comparable)s1;
			Comparable ss2 =  (Comparable)s2;
			return (-1) * ss2.compareTo(ss1);
		}
	};
}
