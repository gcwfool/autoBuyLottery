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
import org.apache.http.client.protocol.HttpClientContext;

import java.io.*; 
import java.util.Scanner;

import org.apache.http.Header;

public class DsnProxyGrab {
	static CloseableHttpClient httpclient = null;
    static RequestConfig requestConfig = null;
    static HttpClientContext clientContext = null;
    
    //configreader
    static ConfigReader configReader = new ConfigReader();
    
    //static Map<String,String> cookieMap = new HashMap<String, String>(64);
    static String strCookies = "";
    static String cookieuid = "";
    static String cookiedae = "";
    
    static {
         requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
         requestConfig = RequestConfig.copy(requestConfig).setRedirectsEnabled(false).build();//禁止重定向 ， 以便获取cookiedae
         httpclient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
    }

    
    

	public static String setCookie(CloseableHttpResponse httpResponse) {
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
		
		for (String c : cookies)
		{
			if(c.indexOf("path=") != -1 || c.indexOf("expires=") != -1 || c.indexOf("domain=") != -1 ||
			   c.indexOf("Max-Age=") != -1 || c.indexOf("HttpOnly") != -1 || c.indexOf("Expires=") != -1)
				continue;
			strCookies += c;
			strCookies += ";";
		}
		
		int binPos = strCookies.indexOf("LOGINCHK=Y");
		if(binPos != -1)
		{
			strCookies = strCookies.substring(binPos);
		}
		System.out.println("----cookies:" + strCookies);
		System.out.println("----setCookieStore success");

		return strCookies;
	}
    


    public static String doLogin() {
    	if(!configReader.read("grab.config")) {
    		return null;
    	}
        clientContext = HttpClientContext.create();
        clientContext.setRequestConfig(requestConfig);
        
        String loginPage = doGet(configReader.getAddress(), null); //get 登录页面
        
        if(loginPage != null) {
        	cookieuid = strCookies;
        	int posStart = loginPage.indexOf("img src=") + 9;
        	if(posStart >= 0) {
        		int posEnd = loginPage.indexOf('"', posStart);
        		String rmNum = getPicNum("http://3f071b45.dsn.ww311.com/" + loginPage.substring(posStart, posEnd));//get 验证码
        	
        		//发送post
        		List<NameValuePair> params = new ArrayList<NameValuePair>();
        		params.add(new BasicNameValuePair("type", "2"));
        		params.add(new BasicNameValuePair("account", configReader.getAccount()));
        		params.add(new BasicNameValuePair("password", configReader.getPassword()));
        		params.add(new BasicNameValuePair("code", rmNum));
        		String location = doPost(configReader.getAddress(), params, strCookies, "");
        		
        		System.out.println("location: " + location); 

            
        		if(location.indexOf("index?") > 0) {
        			strCookies = "";
        			location = doGet(location, cookieuid);//get cookiedae和重定向url
        			cookiedae = strCookies;
        			strCookies = "";
        			return doGet(location, cookieuid + cookiedae);//get 主页
        		}
            
        	}
        }
   //     int loginPage.
         
        return null;
    }


    /**以utf-8形式读取*/
    public static String doPost(String url, List<NameValuePair> formparams, String cookies, String referUrl) {
        return doPost(url, formparams, cookies, "UTF-8", referUrl);
    }

    public static String doPost(String url,List<NameValuePair> formparams, String cookies, String charset, String referUrl) {  
        HttpPost httppost = new HttpPost(url); 
        httppost.addHeader("Accept-Encoding","Accept-Encoding: gzip, deflate");
        httppost.addHeader("Origin","http://a1.dio168.net");
        httppost.addHeader("Connection","keep-alive");
        httppost.addHeader("Cache-Control","no-cache");
        httppost.addHeader("Referer", referUrl);
        httppost.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36"
        					+ " (KHTML, like Gecko) Chrome/51.0.2704.79 Safari/537.36");
        if(cookies != null){
       	 httppost.addHeader("Cookie", cookies);
        }
        
        System.out.println("executing request " + httppost.getURI()); 

       
        UrlEncodedFormEntity uefEntity;
        try {
            uefEntity = new UrlEncodedFormEntity(formparams, charset);
            httppost.setEntity(uefEntity);
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                // 打印响应状态    
            	setCookie(response);
            	if(response.getStatusLine().toString().indexOf("302 Found") > 0) {
            		String location = response.getFirstHeader("Location").getValue();
            		System.out.println(response.getStatusLine());
            		if(location != null) {
            			return location;
            		}
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
    
    public static String doGet(String url, String cookies) {
         
        try {  
           // 创建httpget.    
           HttpGet httpget = new HttpGet(url);
           
           httpget.addHeader("Accept-Encoding","Accept-Encoding: gzip, deflate, sdch");
           httpget.addHeader("Accept-Language","Accept-Language: zh-CN,zh;q=0.8");
           httpget.addHeader("Cache-Control","no-cache");
           httpget.addHeader("Connection","keep-alive");
           //httpget.addHeader("Content-Type","application/json; charset=UTF-8");
           //httpget.addHeader("Referer","http://www.lashou.com/");
           httpget.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36"
           					+ " (KHTML, like Gecko) Chrome/51.0.2704.79 Safari/537.36");  
           if(cookies != null){
        	   httpget.addHeader("Cookie", cookies);
           }
           System.out.println("executing request " + httpget.getURI()); 
          
           // 执行get请求.    
           CloseableHttpResponse response = httpclient.execute(httpget, clientContext); 
           //CloseableHttpResponse response = httpclient.execute(httpget); 
           
           try {          	           	   
               // 获取响应实体    
        	   setCookie(response);
               HttpEntity entity = response.getEntity(); 
               System.out.println("--------------------------------------"); 
               // 打印响应状态    
               
               String statusLine = response.getStatusLine().toString();
               System.out.println(statusLine); 
               if(statusLine.indexOf("302 Found") > 0) {
            	   return response.getFirstHeader("Location").getValue();
               }
               System.out.println("------------------------------------"); 
               if (entity != null) {
	            	String entityStr = EntityUtils.toString(entity);
	            	System.out.println(entityStr); 
	            	return entityStr;
               }
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
    
    // 获取并输入验证码， 图片下载在D:\\rmnumber.png, 在控制台输入验证码
    public static String getPicNum(String picUri){
    	 HttpGet httpget = new HttpGet(picUri);
         httpget.addHeader("Connection","keep-alive");
         if(strCookies != ""){
        	 httpget.addHeader("Cookie", strCookies);
         }
         httpget.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 "
         					+ "(KHTML, like Gecko) Chrome/51.0.2704.79 Safari/537.36");           
         System.out.println("executing request " + httpget.getURI()); 
        
         // 执行get请求.    
         try {
        	 CloseableHttpResponse response = httpclient.execute(httpget, clientContext); 
        	 try{
        		 setCookie(response);
                 // 打印响应状态    
                 System.out.println(response.getStatusLine()); 
                 System.out.println("------------------------------------");
                 File storeFile = new File("D:\\yzm.png");  
                 FileOutputStream output = new FileOutputStream(storeFile);  
                 //得到网络资源的字节数组,并写入文件  
                 byte [] a = EntityUtils.toByteArray(response.getEntity());
                 output.write(a);  
                 output.close();  
                 System.out.println("请输入验证码：");
                 Scanner scanner = new Scanner(System.in);
                 String rmNum = scanner.next();
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
         
    	return null;
    }
    
  //! @brief 抓取cqssc下单数据
  //! @param game       两面:"LM", 单号:"DH", 前中后三:"QZHS"
  //! @param all        虚注:"XZ" 实占:"SZ" 补货:"BH"
  //! @param range      全部:"", A盘:"A", B盘:"B", C盘:"C", D盘:"D",
  //! @return           success:String fail:null
    public static String grabCQSSCdata(String game, String all, String range){
    	if((game == "LM" || game == "DH" || game == "QZHS") && (range == "" || range == "A" ||
    			range == "B" || range == "C" || range == "D") && (all == "XZ" || all == "SZ" || all == "BH")) {
    		long time =  System.currentTimeMillis();
    		String strTime = Long.toString(time);
    		String data = doGet("http://3f071b45.dsn.ww311.com/agent/control/risk?lottery=CQSSC&games=DX1%2CDS1%2CDX2"
				+ "%2CDS2%2CDX3%2CDS3%2CDX4%2CDS4%2CDX5%2CDS5%2CZDX%2CZDS%2CLH&all=" + all + "&range=" + range 
				+ "&multiple=false&_=" + strTime, cookieuid + cookiedae);
    		if(data != null) {
    			return data;
    		}
    	}
    	return null;
    }
    
}
