import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.ArrayList;

import org.apache.http.message.BasicNameValuePair;


import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


import org.apache.http.HeaderIterator;
import org.apache.http.client.CookieStore;


import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.client.protocol.HttpClientContext;



import java.util.HashMap;
import java.util.Map;

import java.io.File;   
import java.io.FileOutputStream;   
import java.io.*; 
import java.util.Scanner;



import org.apache.http.Header;

public class zzHttp {
	static CloseableHttpClient httpclient = null;
    static RequestConfig requestConfig = null;
    static HttpClientContext clientContext = null;
    static Map<String,String> cookieMap = new HashMap<String, String>(64);
    
    static {
         requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
         httpclient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
    }

    
    

	public static String setCookie(CloseableHttpResponse httpResponse)
	{
		System.out.println("----setCookieStore");
		Header headers[] = httpResponse.getHeaders("Set-Cookie");
		if (headers == null || headers.length==0)
		{
			System.out.println("----there are no cookies");
			return null;
		}
		String cookie = "";
		for (int i = 0; i < headers.length; i++) {
			cookie += headers[i].getValue();
			if(i != headers.length-1)
			{
				cookie += ";";
			}
		}
		System.out.println("----cookies:" + cookie);
		String cookies[] = cookie.split(";");
		
		String strcookies = "";
		
		for (String c : cookies)
		{
			if(c.indexOf("path=") != -1 || c.indexOf("expires=") != -1 || c.indexOf("domain=") != -1)
				continue;
			strcookies += c;
			strcookies += ";";
		}
		System.out.println("----cookies:" + strcookies);
		System.out.println("----setCookieStore success");

		return strcookies;
	}
    


    public static String doGet(String url) {

        clientContext = HttpClientContext.create();
        clientContext.setRequestConfig(requestConfig);
         
        try {  
           // ����httpget.    
           HttpGet httpget = new HttpGet(url);
           //httpget.addHeader("Cookie", "ThinkID=cvohise5omfqpj4f82igimdp20;");
           httpget.addHeader("Accept-Encoding","Accept-Encoding: gzip, deflate, sdch");
           httpget.addHeader("Accept-Language","Accept-Language: zh-CN,zh;q=0.8");
           httpget.addHeader("Cache-Control","no-cache");
           httpget.addHeader("Connection","keep-alive");
           //httpget.addHeader("Content-Type","application/json; charset=UTF-8");
           httpget.addHeader("Referer","http://www.lashou.com/");
           httpget.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.79 Safari/537.36");           
           System.out.println("executing request " + httpget.getURI()); 
          
           // ִ��get����.    
           CloseableHttpResponse response = httpclient.execute(httpget, clientContext); 

           
           try {          	           	   
               // ��ȡ��Ӧʵ��    
               HttpEntity entity = response.getEntity(); 
               System.out.println("--------------------------------------"); 
               // ��ӡ��Ӧ״̬    
               System.out.println(response.getStatusLine()); 
               if (entity != null) {  
	            	   
	            	String entityStr = EntityUtils.toString(entity);
	            	
	            	System.out.println(entityStr);
	            	
	            	if(entityStr.indexOf("location.href = '") > 0){//��Ҫ��ת
	            		int posStart = entityStr.indexOf("location.href = '") + 17;	
	            		int posEnd   = entityStr.indexOf('\'', posStart);
	            		String jumpUrl = entityStr.substring(posStart, posEnd);
	            		doGet(jumpUrl);
	            	}
	            	else if(entityStr.indexOf("location.href = http_type_t+") > 0){//������ת
	            		int posStart = entityStr.indexOf("index", entityStr.indexOf("location.href = http_type_t+"));
	            		int posEnd = entityStr.indexOf('\'', posStart);
	            		String jumpUrl = "http://a1.dio168.net/" + entityStr.substring(posStart, posEnd);
	            		doGet(jumpUrl);
	            	}
	            	else if(entityStr.indexOf("main_frame.php?p=") > 0){//��ȡmain_frame��php
	            		int posStart = entityStr.indexOf("main_frame.php?p=");
	            		int posEnd = entityStr.indexOf('"', posStart);
	            		String jumpUrl = "http://a1.dio168.net/" + entityStr.substring(posStart, posEnd);
	            		doGet(jumpUrl);
	            	}
	            	else if(entityStr.indexOf("Fingerprint2()") > 0){//����main_frame.php �����֤�벢��¼
	            		
	            		// �ҵ�POST��url
	            		int posStart = entityStr.indexOf("login.php?p=");
	            		int posEnd = entityStr.indexOf('"', posStart);
	            		String postUrl = "http://a1.dio168.net/" + entityStr.substring(posStart, posEnd);
	            		
	            		// ����form������д����
	            		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
	            		formparams.add(new BasicNameValuePair("CQ", ""));
	            		
	            		String tmpStr;
	            		posStart = entityStr.indexOf("name=\"SS\" value=\"", posEnd) + 17;
	            		posEnd = entityStr.indexOf('"', posStart);
	            		tmpStr = entityStr.substring(posStart, posEnd);
	            		formparams.add(new BasicNameValuePair("SS", tmpStr));
	            		
	            		posStart = entityStr.indexOf("name=\"SR\" value=\"", posEnd) + 17;
	            		posEnd = entityStr.indexOf('"', posStart);
	            		tmpStr = entityStr.substring(posStart, posEnd);
	            		formparams.add(new BasicNameValuePair("SR", tmpStr));
	            		String picUri = "http://a1.dio168.net/macpic.php?SR=" + tmpStr;
	            		
	            		posStart = entityStr.indexOf("name=\"TS\" value=\"", posEnd) + 17;
	            		posEnd = entityStr.indexOf('"', posStart);
	            		tmpStr = entityStr.substring(posStart, posEnd);
	            		formparams.add(new BasicNameValuePair("TS", tmpStr));
	            		
	            		posStart = entityStr.indexOf("name=\"tpl\" value=\"", posEnd) + 18;
	            		posEnd = entityStr.indexOf('"', posStart);
	            		tmpStr = entityStr.substring(posStart, posEnd);
	            		formparams.add(new BasicNameValuePair("tpl", tmpStr));
	            		
	            		formparams.add(new BasicNameValuePair("langx", "cn"));
	            		formparams.add(new BasicNameValuePair("LoadingTime", "0.265"));
	            		formparams.add(new BasicNameValuePair("get_info_key", "b073c15969a4c2f3bc7b302296b75de8"));
	            		formparams.add(new BasicNameValuePair("get_info_key2", ""));
	            		formparams.add(new BasicNameValuePair("Account", "hgg999"));
	            		formparams.add(new BasicNameValuePair("PassWD", "abcd123456"));
	            		
	            		String rmNum = getPicNum(picUri); //��ȡ��֤��
	            		formparams.add(new BasicNameValuePair("rmNum", rmNum));
	            		
	            		doPost(postUrl, formparams, null, url);//��¼
	            	}
                return "";
               }  
               System.out.println("------------------------------------"); 
           } 
           finally {  
               response.close(); 
           }  
       } catch (ClientProtocolException e) {  
           e.printStackTrace(); 
       } catch (ParseException e) {  
           e.printStackTrace(); 
       } catch (IOException e) {  
           e.printStackTrace(); 
       } 
        return null;
    }


    /**��utf-8��ʽ��ȡ*/
    public static String doPost(String url,List<NameValuePair> formparams, String cookies, String referUrl) {
        return doPost(url, formparams,"UTF-8", cookies, referUrl);
    }

    public static String doPost(String url,List<NameValuePair> formparams,String charset, String cookies, String referUrl) {
        // ����httppost    
        HttpPost httppost = new HttpPost(url); 
        httppost.addHeader("Accept-Encoding","Accept-Encoding: gzip, deflate, sdch");
        httppost.addHeader("Origin","http://a1.dio168.net");
        httppost.addHeader("Connection","keep-alive");
        httppost.addHeader("Cache-Control","no-cache");
        httppost.addHeader("Referer", referUrl);
        httppost.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.79 Safari/537.36");  
        
        System.out.println("executing request " + httppost.getURI()); 

       
        UrlEncodedFormEntity uefEntity;
        try {
            uefEntity = new UrlEncodedFormEntity(formparams, charset);
            httppost.setEntity(uefEntity);
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                // ��ӡ��Ӧ״̬    
                System.out.println(response.getStatusLine());
                 HttpEntity entity = response.getEntity(); 
                 if (entity != null) {  
                	String entityStr = EntityUtils.toString(entity);
                	System.out.println(entityStr);
                    return entityStr;
                 }  
            } finally {  
                response.close(); 
            }  
        } catch (ClientProtocolException e) {  
            e.printStackTrace(); 
        } catch (UnsupportedEncodingException e1) {  
            e1.printStackTrace(); 
        } catch (IOException e) {  
            e.printStackTrace(); 
        } 
        return null;
    }
    
    // ��ȡ��������֤�룬 ͼƬ������D:\\rmnumber.png, �ڿ���̨������֤��
    public static String getPicNum(String picUri){
    	 HttpGet httpget = new HttpGet(picUri);
         httpget.addHeader("Connection","keep-alive");
         httpget.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.79 Safari/537.36");           
         System.out.println("executing request " + httpget.getURI()); 
        
         // ִ��get����.    
         try {
        	 CloseableHttpResponse response = httpclient.execute(httpget, clientContext); 
        	 try{
                 System.out.println("--------------------------------------"); 
                 // ��ӡ��Ӧ״̬    
                 System.out.println(response.getStatusLine()); 
                 File storeFile = new File("D:\\rmnumber.png");  
                 FileOutputStream output = new FileOutputStream(storeFile);  
                 //�õ�������Դ���ֽ�����,��д���ļ�  
                 byte [] a = EntityUtils.toByteArray(response.getEntity());
                 output.write(a);  
                 output.close();  
                 System.out.println("��������֤�룺");
                 Scanner scanner = new Scanner(System.in);
                 String rmNum = scanner.next();
                 System.out.println(rmNum);
                 return rmNum;
        	 }
        	 finally{
        		 response.close(); 
        	 }
         } catch (ClientProtocolException e) {  
             e.printStackTrace(); 
         } catch (UnsupportedEncodingException e1) {  
             e1.printStackTrace(); 
         } catch (IOException e) {  
             e.printStackTrace(); 
         } 
         
    	return "";
    }
}
