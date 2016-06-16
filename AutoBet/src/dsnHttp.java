import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.http.Header;
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
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.http.entity.StringEntity;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.*;



public class dsnHttp {

    static CloseableHttpClient httpclient = null;
    static RequestConfig requestConfig = null;
    static HttpClientContext clientContext = null;
    static ConfigReader configReader = new ConfigReader();
    
    
    //这个变量用来存储
    static String cookieCfduid = "";
    static String cookie2ae = "";
    static String cookiedefaultLT = "";
    static String cookiesb18 = "";
    static String location = "";
    static long   queryParam = 0;
    
    static long time = 0;
    
    static String drawNumber = "";
    
    
    public static boolean loginToDsn(){
    	
    	boolean readRes = configReader.read("bet.config");
    	if(readRes != true){
    		System.out.println("bet.config 文件读取错误，请检查!");
    		return false;
    	}
    		
    	
    	String loginURI = "";
    	loginURI = "/login";
    	
    	loginURI = configReader.getAddress() + loginURI;
    	
    	doGetLoginPage(loginURI);
    	
	    //手动输入验证码:
        System.out.println("请登录迪斯尼输入验证码：");
        Scanner scanner = new Scanner(System.in);
        String code = scanner.next();
        System.out.println(code);
        
        String type = "1";  //remove hardcode later
        String account = configReader.getAccount();
        String password = configReader.getPassword();
        
        
        
        //String cookies = cookieCfduid + cookie2ae;
        
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("type", type));
        params.add(new BasicNameValuePair("account", account));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("code", code));
    	
        doPost(loginURI, params, "");
        
        //For get cookies
        doGet(location,cookieCfduid, "");
        

        

        
        //get cqssc page
        String host = configReader.getAddress();
        String res = doGet(host + "/member/load?lottery=CQSSC&page=lm", "", host + "/member/index");
        if(res == null){
        	System.out.println("get cqssc page failed");
        	return false;
        }

      
        /*String response = "";
        //get time
        String getTimeUrl = //"http://835b1195.dsn.ww311.com/time?_=";
        getTimeUrl += System.currentTimeMillis();
        response = doGetCQSSCparam(getTimeUrl, cookieCfduid, "http://835b1195.dsn.ww311.com/member/load?lottery=CQSSC&page=lm");
        if(response == null)
        {
        	
        	System.out.println("get time failed");
        	return false;
        }
        System.out.println("time:");
        System.out.println(response);
        time = Long.parseLong(response);*/
        

        
        
        
    	return true;
    }
    
    public static boolean doBet(String betData)
    {
        //get period
    	String response = "";
    	String host = configReader.getAddress();
        String getPeriodUrl = host + "/member/period?lottery=CQSSC&_=";
        getPeriodUrl += Long.toString(System.currentTimeMillis());
        response = doGetCQSSCparam(getPeriodUrl, "", configReader.getAddress() + "/member/load?lottery=CQSSC&page=lm");
        
        if(response == null)
        {
        	
        	System.out.println("get period failed");
        	return false;
        }
        
        System.out.println("preiod:");
        System.out.println(response);
        
        JSONObject periodJson = new JSONObject(response);
        long closeTime = periodJson.getLong("closeTime");
        drawNumber = periodJson.getString("drawNumber");
       	
        String jsonParam = "";
        
        time = System.currentTimeMillis();
        
        //如果未到封盘时间
        if(time < closeTime && drawNumber != null){
        	jsonParam = constructBetsData(betData);
        	
        	System.out.println(jsonParam);
        	
        	
        	
        	response = bet("http://835b1195.dsn.ww311.com/member/bet", jsonParam, "UTF-8", "");
        	
        	System.out.println("bet result:");
        	System.out.println(response);
        	
        	
        
        }
        
        return false;
    }
    
    public static String constructBetsData(String betData)
    {
    	JSONObject game1Obj = new JSONObject();
    	
    	//ball 1
    	game1Obj.put("game", "DX1");
    	game1Obj.put("contents", "D");
    	game1Obj.put("amount", 1);
    	game1Obj.put("odds", 1.983);

    	//ball 2
    	JSONObject game2Obj = new JSONObject();
    	game2Obj.put("game", "DX2");
    	game2Obj.put("contents", "D");
    	game2Obj.put("amount", 1);
    	game2Obj.put("odds", 1.983);
    	
    	//ball 3
    	JSONObject game3Obj = new JSONObject();
    	game3Obj.put("game", "DX3");
    	game3Obj.put("contents", "D");
    	game3Obj.put("amount", 1);
    	game3Obj.put("odds", 1.983);
    	
    	//ball 4
    	JSONObject game4Obj = new JSONObject();
    	game4Obj.put("game", "DX4");
    	game4Obj.put("contents", "D");
    	game4Obj.put("amount", 1);
    	game4Obj.put("odds", 1.983);

    	//ball 5
    	JSONObject game5Obj = new JSONObject();
    	game5Obj.put("game", "DX5");
    	game5Obj.put("contents", "D");
    	game5Obj.put("amount", 1);
    	game5Obj.put("odds", 1.983);
    	
    	JSONArray gamesArray = new JSONArray();
    	gamesArray.put(game1Obj);
    	gamesArray.put(game2Obj);
    	gamesArray.put(game3Obj);
    	gamesArray.put(game4Obj);
    	gamesArray.put(game5Obj);
    	
    	//JSONObject gamesObj = new JSONObject();
    	//gamesObj.append("bets", gamesArray);


    	JSONObject betsObj = new JSONObject();
    	
    	boolean ignore = false;
    	betsObj.put("ignore", ignore);
    	betsObj.put("bets", gamesArray);
    	betsObj.put("drawNumber",drawNumber);
    	
    	betsObj.put("lottery", "CQSSC");
    	
    	return betsObj.toString();
    	
    }

    /*public static void setQueryParams(String str){
    	int posStar = str.indexOf("=");
    	int len = str.length();
    	String queryStringParam = str.substring(posStar+1);
    	queryParam = Long.parseLong(queryStringParam);
    	
    	//加十秒
    	queryParam += 99949;
    	System.out.println("set query param successfully");
    	System.out.println(queryParam);
    }
    
    public static String getQueryStringparam(){
    	String queryString = Long.toString(queryParam);
    	queryParam++;
    	return queryString;
    }*/
    
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
		String cookies[] = cookie.split(";");
		
		String strcookies = "";
		
		for (String c : cookies)
		{
			if(c.indexOf("path=") != -1 || c.indexOf("expires=") != -1 || c.indexOf("domain=") != -1 || c.indexOf("HttpOnly") != -1)
				continue;
			strcookies += c;
			strcookies += ";";
		}
		System.out.println("----setCookieStore success");

		return strcookies;
	}
    


    public static String doGetLoginPage(String url) {


    	requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
        httpclient =HttpClients.custom().setDefaultRequestConfig(requestConfig).build(); //HttpClients.createDefault();;//

                                
        try {  
           // 创建httpget.    
           HttpGet httpget = new HttpGet(url);
           httpget.addHeader("Accept-Encoding","Accept-Encoding: gzip, deflate, sdch");
           httpget.addHeader("Accept-Language","Accept-Language: zh-CN,zh;q=0.8");
           httpget.addHeader("Connection","keep-alive");
           httpget.addHeader("Upgrade-Insecure-Requests","1");
           httpget.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
           //httpget.addHeader("Referer","http://www.lashou.com/");
           httpget.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.79 Safari/537.36");           
           System.out.println("executing request " + httpget.getURI()); 
          
           // 执行get请求.    
           CloseableHttpResponse response = httpclient.execute(httpget); 
           
           cookieCfduid = setCookie(response);
           
           
           
           try {          	           	   
        	            	            	          	   
               // 获取响应实体    
               HttpEntity entity = response.getEntity(); 
               System.out.println("--------------------------------------"); 
               // 打印响应状态    
               System.out.println(response.getStatusLine()); 
               if (entity != null) {  
            	   
            	String entityStr = EntityUtils.toString(entity);
            	
            	//getyzmUrl
            	String strs1[] = entityStr.split("img src=\"");
            	String strs2[] = strs1[1].split("\" alt=\"none\"");
            	String yzmURL = strs2[0];    
            	
            	
            	//System.out.println(entityStr); 
            	
            	//response.close();
            	
            	//setQueryParams(yzmURL);
            	
            	//get yzm
            	String hostUrl = "http://835b1195.dsn.ww311.com/";
            	yzmURL = hostUrl + yzmURL;
 
            	httpget.releaseConnection();
            	
            	
            	httpget = new HttpGet(yzmURL);
                httpget.addHeader("Accept-Encoding","Accept-Encoding: gzip, deflate, sdch");
                httpget.addHeader("Accept-Language","Accept-Language: zh-CN,zh;q=0.8");
                httpget.addHeader("Connection","keep-alive");
                httpget.addHeader("Referer","http://835b1195.dsn.ww311.com/login");
                httpget.addHeader("Accept","image/webp,image/*,*/*;q=0.8");
                //httpget.addHeader("Referer","http://www.lashou.com/");
                httpget.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.79 Safari/537.36");           
                System.out.println("executing request " + httpget.getURI()); 
               
                // 执行get请求.   
                
                String destfilename = "D:\\yzm.jpg";
                
        	    //httpget = new HttpGet(url);
        	    File file = new File(destfilename);
        	    if (file.exists()) {
        	        file.delete();
        	    }
                
                response = httpclient.execute(httpget); 
            	
                
                entity = response.getEntity();
                
                
                cookie2ae = setCookie(response);
                
                
                InputStream in = entity.getContent();
                
        	    try {
        	        FileOutputStream fout = new FileOutputStream(file);
        	        int l = -1;
        	        byte[] tmp = new byte[2048];
        	        while ((l = in.read(tmp)) != -1) {
        	            fout.write(tmp);
        	        }
        	        fout.close();
        	    } finally {
        	        in.close();
        	    }
        	    

            	httpget.releaseConnection();
                

            	
                return "hahaha";
               }  
               System.out.println("------------------------------------"); 
           } finally {  
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


    /**以utf-8形式读取*/
    public static String doPost(String url,List<NameValuePair> formparams, String cookies) {
        return doPost(url, formparams,"UTF-8", cookies);
    }

    public static String doPost(String url,List<NameValuePair> formparams,String charset, String cookies) {


     // 创建httppost    
        HttpPost httppost = new HttpPost(url); 
        //httppost.addHeader("Cookie", cookies);
        //httppost.addHeader("Accept-Encoding","Accept-Encoding: gzip, deflate, sdch");
        //httppost.addHeader("x-requested-with","XMLHttpRequest");
        httppost.addHeader("Accept-Language","Accept-Language: zh-CN");
        httppost.addHeader("Accept","application/json, text/javascript, */*; q=0.01");
        httppost.addHeader("Accept-Encoding","gzip, deflate");
        httppost.addHeader("Connection","keep-alive");
        httppost.addHeader("Cache-Control","max-age=0");
        httppost.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.79 Safari/537.36");    


        UrlEncodedFormEntity uefEntity;
        try {
            uefEntity = new UrlEncodedFormEntity(formparams, charset);
            httppost.setEntity(uefEntity);
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                // 打印响应状态    
                System.out.println(response.getStatusLine());
                 HttpEntity entity = response.getEntity(); 
                 
                 //get location
                 Header headers[] = response.getHeaders("Location");
                 location = headers[0].getValue();
                 
                                 
                 httppost.releaseConnection();
                 
                 System.out.println(cookiesb18);
                 
                 if (entity != null) {  
                    return EntityUtils.toString(entity);
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
    
    
    public static String doGet(String url, String cookies, String referUrl) {
    	
        try {  
            // 创建httpget.    
            HttpGet httpget = new HttpGet(url);
            
            //httpget.addHeader("Cookie",cookies);
            httpget.addHeader("Accept-Encoding","Accept-Encoding: gzip, deflate, sdch");
            httpget.addHeader("Accept-Language","Accept-Language: zh-CN,zh;q=0.8");
            httpget.addHeader("Connection","keep-alive");
            httpget.addHeader("Upgrade-Insecure-Requests","1");
            httpget.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            //
            
            if(referUrl != "")
            {
            	httpget.addHeader("Referer",referUrl);
            	
            }
            
            httpget.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.79 Safari/537.36");           
            System.out.println("executing request " + httpget.getURI()); 
           
            // 执行get请求.    
            CloseableHttpResponse response = httpclient.execute(httpget); 
            System.out.println(response.getStatusLine());
            

            
            HttpEntity entity = response.getEntity(); 
            
            String res = EntityUtils.toString(entity);

            httpget.releaseConnection();
            response.close();
            
            if(entity != null){
                return res;
            }
            
            
            
            //System.out.println(EntityUtils.toString(entity)); 


        } catch (ClientProtocolException e) {  
            e.printStackTrace(); 
        } catch (ParseException e) {  
            e.printStackTrace(); 
        } catch (IOException e) {  
            e.printStackTrace(); 
        } 
        
        return null;
    }
    
    
    public static String doGetCQSSCparam(String url, String cookies, String referUrl) {
    	
        try {  
            // 创建httpget.    
            HttpGet httpget = new HttpGet(url);
            
            httpget.addHeader("Cookie",cookies);
            httpget.addHeader("Accept-Encoding","Accept-Encoding: gzip, deflate, sdch");
            httpget.addHeader("Accept-Language","Accept-Language: zh-CN,zh;q=0.8");
            httpget.addHeader("Connection","keep-alive");
            //httpget.addHeader("Upgrade-Insecure-Requests","1");
            httpget.addHeader("Accept","*/*");
            httpget.addHeader("X-Requested-With","XMLHttpRequest");
            
            if(referUrl != "")
            {
            	httpget.addHeader("Referer",referUrl);
            	
            }
            
            httpget.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.79 Safari/537.36");           
            System.out.println("executing request " + httpget.getURI()); 
           
            // 执行get请求.    
            CloseableHttpResponse response = httpclient.execute(httpget); 
            System.out.println(response.getStatusLine());
       
            HttpEntity entity = response.getEntity(); 
            
            String res = EntityUtils.toString(entity);
            
            httpget.releaseConnection();
            response.close();
            
            if(entity != null){
                return res;
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
    
    
    public static String bet(String url,String jsonData, String charset, String cookies) {


        // 创建httppost    
           HttpPost httppost = new HttpPost(url); 
           //httppost.addHeader("Cookie", cookies);
           httppost.addHeader("Content-Type","application/json");
           httppost.addHeader("x-requested-with","XMLHttpRequest");
           httppost.addHeader("Accept-Language","zh-CN,zh;q=0.8");
           httppost.addHeader("Accept","*/*");
           httppost.addHeader("Accept-Encoding","gzip, deflate");
           httppost.addHeader("Connection","keep-alive");
           httppost.addHeader("Referer","http://835b1195.dsn.ww311.com/member/index");
           httppost.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.79 Safari/537.36");    


           StringEntity strEntity;
           try {
        	   strEntity = new StringEntity(jsonData, charset);
               httppost.setEntity(strEntity);
               CloseableHttpResponse response = httpclient.execute(httppost);
               try {
                   // 打印响应状态    
                   System.out.println(response.getStatusLine());
                    HttpEntity entity = response.getEntity(); 
                    
                    
                    String res = EntityUtils.toString(entity);
                    
                    

                    httppost.releaseConnection();
                    
                    
                    if (entity != null) {  
                       return res;
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
    
    
}


