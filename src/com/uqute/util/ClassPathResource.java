package com.uqute.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClassPathResource {
	 	/**
	 	 * 手机号码的验证，严格验证
	 	 * @param mobiles 要验证的手机号码
	 	 * @return
	 	 */
	    public static boolean isMobileNO(String mobiles){     
	        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");     
	        Matcher m = p.matcher(mobiles);            
	        return m.matches();     
	    } 
	   
	    /**
	     * E_mail的验证
	     * @param email 要验证的email
	     * @return
	     */
	    public static boolean isEmail(String email){     
	     String str="^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
	        Pattern p = Pattern.compile(str);     
	        Matcher m = p.matcher(email);            
	        return m.matches();     
	    } 
	     
}
