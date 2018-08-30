package com.jscredit.zxypt.utils;

import java.net.URLDecoder;
import java.net.URLEncoder;

public class EncryptUtils {
	private static char[] codeKey = { 0, 1, 0, 1 };
	private static int codeKeyLen = 0;
	private final static String security_key="wuhanwri_hx";
	static{
 		    char[] arrayOfChar = security_key.toCharArray();
		    codeKey = arrayOfChar;
		    codeKeyLen = arrayOfChar.length;		
	}

	public static String decode(String paramString){
	    String str = "";
	    try{
            str= URLDecoder.decode(paramString, "utf-8");
	    }catch(Exception e){
	        e.printStackTrace();
	    }
	    return str;
	    //return xor(paramString);
	}

	public static String encode(String paramString){
	    String str = "";
        try{
            str= URLEncoder.encode(paramString, "utf-8");
        }catch(Exception e){
            e.printStackTrace();
        }
        return str;
	    //return xor(paramString);
	}
	
	 
	
   

	  private static String xor(String paramString){
		 String str="";
	     if (paramString != null){
	    	 char[] arrayOfChar1 = paramString.toCharArray();
		   	 char[] arrayOfChar2 = new char[arrayOfChar1.length];
		   	 if (codeKeyLen >= arrayOfChar1.length){
	    	   for (int j = 0; j < arrayOfChar1.length; j++){
	    		   arrayOfChar2[j] = (char)(arrayOfChar1[j] ^ codeKey[j]);
	    	   }    
		     }else{
	    	   for (int i = 0; i < arrayOfChar1.length; i++){
	    		   arrayOfChar2[i] = (char)(arrayOfChar1[i] ^ codeKey[(i % codeKeyLen)]);
	    	   }
	    	 }
		   	 str = new String(arrayOfChar2);
	    	 
	     }
	     return str;
	 }
	  
	  
	 public static void main(String[] args) {
		 String str="系统测试112121";
		  str=encode(str);
		  System.out.println(str);
		  str=decode(str);
		  System.out.println(str);
	}

 
}
