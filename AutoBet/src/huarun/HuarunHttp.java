package huarun;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

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
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import dsn.ConfigWriter;


public class HuarunHttp {
    static CloseableHttpClient httpclient = null;
    static RequestConfig requestConfig = null;
    static HttpClientContext clientContext = null;
    
    
    static {
        requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
        requestConfig = RequestConfig.copy(requestConfig).setRedirectsEnabled(false).build();//��ֹ�ض��� �� �Ա��ȡcookieb18
        //requestConfig = RequestConfig.copy(requestConfig).setConnectTimeout(autoBet.timeOut).setConnectionRequestTimeout(autoBet.timeOut).setSocketTimeout(autoBet.timeOut).build();//���ó�ʱ
        httpclient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
   }
	
	
	
	static String strCookies = "";
    static Vector<Long> lastTenRequestTime = new Vector<Long>();
    static long avgRequestTime = 0;    
    static boolean bcalcRequestTime = true;
    static boolean bneedChangeLine = false;
    
    
    static long lastChangeLineTime = 0;
    
    
    
    static int requestFailTimes = 0;
    static long lastFailtime = 0;
    
    static boolean isNeedRelogin = false;
    
    static public boolean isInRelogin = false;
    
	
	
	static String ADDRESS = "";
	static String ACCOUNT = "";
	static String PWD = "";
	
	static int defaultTimeout = 3*1000;
	
	static String line = "";
	//static String host = "ok1688.info/";
	public static String jeuValidate = "";
	
	
	
    public static void setIscalcRequestTime(boolean flag){
    	bcalcRequestTime = flag;
    }
    
    public static void setisNeedChangeLine(boolean flag){
    	bneedChangeLine = flag;
    }
    
    public static boolean getIsisNeedChangeLine(){
    	return bneedChangeLine;
    }
    
    public static void clearAvgRequest(){
    	
    	if(lastTenRequestTime.size() >0){
    		lastTenRequestTime.clear();
    	}
    	avgRequestTime = 0;

    }
	
	public static void setLoginParams(String address, String account, String pwd){
		ADDRESS = address;
		ACCOUNT = account;
		PWD = pwd;
	}
	
	
	public static boolean reLogin(){
		return connFailLogin();
	}	
	
	public static boolean connFailLogin(){
		
		setIscalcRequestTime(false);
		
		while(login() == false){
			
			strCookies = "";
			
			try{
				Thread.currentThread().sleep(5*1000);
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		
		setIscalcRequestTime(true);
		
		return true;
	}

	public static boolean login(){
		
		boolean success = false;
		
		setIscalcRequestTime(false);
		
		String linePageUri = "http://www.qch56.com";
		
		int oldTimeout = defaultTimeout;
		defaultTimeout = 6 * 1000;
		
		String html = null;
		
		try{
								
			Vector<Object[]> lines = new Vector<Object[]>();
			
			String linePage = doGet(linePageUri, "", "");
	    	
	    	if(linePage == null){
	    		linePageUri = "http://qzqzl.com";
	    		linePage = doGet(linePageUri, "", "");
	    	}	    	
	    	
	    	List<NameValuePair> params = new ArrayList<NameValuePair>();
	    	params.add(new BasicNameValuePair("code", "123999"));
	    	params.add(new BasicNameValuePair("Submit", "%E7%99%BB+%E9%8C%84"));
	    	linePage = doPost(linePageUri + "/Web/SearchLine.aspx", params, "", "");
	    	
	    	if(linePage == null) {
	    		linePage = doPost(linePageUri + "/Web/SearchLine.aspx", params, "", "");
	    	}
	    	
	    	if(linePage == null) {
	    		return false;
	    	}
	    	
	    	int posStart = 0;
	    	int posEnd = 0;
	    	posStart = linePage.indexOf("div class=\"top1\"");
	    	if(posStart == -1)  {
	    		return false;
	    	}
	    	
	    	posEnd = linePage.indexOf("</ul>", posStart);
	    	
	    	linePage = linePage.substring(posStart, posEnd);
	    	
	    	System.out.println(linePage);
	    	posEnd = 0;
	    	long time = 100000;
	    	while(true) {    		
	    		posStart = linePage.indexOf("http", posEnd);
	    		if (posStart < 0) {
					break;
				}
	    		posEnd = linePage.indexOf("\"", posStart);
	    		if (posEnd < 0) {
					break;
				}
	    		String line = linePage.substring(posStart, posEnd);
	    		long time1 = System.currentTimeMillis();
	    		String res = doGet(line, "", "http://www.qch56.com/Web/SearchLine.aspx");
	    		//System.out.println(res);
	    		if(res != null && res.contains("<input data-type=")) {
	    			time1 = System.currentTimeMillis() - time1;
	    			if(time1 < time) {
	    				time = time1;
	    				System.out.println("time:"+ time + "  line:" + line);
	    				ADDRESS = line;
	    				html = res;
	    			}
	    		}
	    	}
	    	
	    	success = loginTohuarun();
	    	if(success) {
	    		ConfigWriter.updateHuarunMemberAddress(ADDRESS);
				ConfigWriter.updateHuarunMemberAccount(ACCOUNT);
				ConfigWriter.updateHuarunMemberPassword(PWD);
				ConfigWriter.saveTofile("common.config");
	    	}
	    	
		}catch(Exception e){
			defaultTimeout = oldTimeout;
			setIscalcRequestTime(true);
			e.printStackTrace();
		}

		defaultTimeout = oldTimeout;
		setIscalcRequestTime(true);
		
		return success;
	}
	
	
    public static boolean loginTohuarun(){
    	
    	try{
    		
    		String res = doGet(ADDRESS, "", "http://www.qch56.com/Web/SearchLine.aspx");
        	List<NameValuePair> params = new ArrayList<NameValuePair>();
	    	params.add(new BasicNameValuePair("loginName", ACCOUNT));
	    	params.add(new BasicNameValuePair("loginPwd", PWD));
	    	params.add(new BasicNameValuePair("ValidateCode", ""));
        	res = doPost(ADDRESS + "/Handler/LoginHandler.ashx?action=user_login", params, "", ADDRESS);
        	if(res == null) {
        		return false;
        	}
        	res = doGet(ADDRESS + "/LoginValidate.aspx", "", ADDRESS);
        	if(res == null) {
        		return false;
        	}
        	res = doGet(ADDRESS + "/index.aspx", "", ADDRESS + "/LoginValidate.aspx");
        	if(res == null) {
        		return false;
        	}
        	res = doGet(ADDRESS + "/L_PK10/index.aspx?lid=2&path=L_PK10", "", ADDRESS + "/index.aspx");
        	if(res == null) {
        		return false;
        	}
        	//System.out.println(res);
        	if(res.contains("var JeuValidate = ")) {
        		int start = res.indexOf("var JeuValidate") + 19;
        		jeuValidate = res.substring(start, res.indexOf("'", start));
        	} else {
				return false;
			}
        	
        	
        	/*List<NameValuePair> params1 = new ArrayList<NameValuePair>();
	    	params1.add(new BasicNameValuePair("action", "get_oddsinfo"));
	    	params1.add(new BasicNameValuePair("playid", "2,3,4,6,7,8,10,11,12,14,15,16,18,19,20,22,23,25,26,28,29,31,32,34,35,37,38"));
	    	params1.add(new BasicNameValuePair("playpage", "pk10_lmp"));
	    	
        	res = doPost(ADDRESS + "/L_PK10/Handler/Handler.ashx", params1, "", ADDRESS + "/L_PK10/index.aspx?lid=2&path=L_PK10");
        	//System.out.print(res);
        	
        	JSONObject gameInfo = new JSONObject(res);        	
        	gameInfo = new JSONObject(gameInfo.getJSONObject("data").toString());
        	
        	System.out.println(gameInfo.getString("nn"));
        	System.out.println(gameInfo.getString("stop_time"));
        	System.out.println(gameInfo.getString("credit"));
        	System.out.println(gameInfo.getString("drawopen_time"));*/

        	if(res != null) {
        		return true;
        	} 		
    	}catch(Exception e){
    		e.printStackTrace();
    	}

    	return false;
    }
    
    
    
    
    public static String doGet(String url, String cookies, String referUrl) {
    	
        try {  
            //
            HttpGet httpget = new HttpGet(url);
            
            if(cookies != "") {
            	httpget.addHeader("Cookie",cookies);
            }
            httpget.addHeader("Accept-Encoding","gzip, deflate, sdch");
            httpget.addHeader("Accept-Language","zh-CN,zh;q=0.8");
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
           
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(defaultTimeout).setConnectTimeout(defaultTimeout).build();
            httpget.setConfig(requestConfig);
    
            CloseableHttpResponse response = execute(httpget); 
            
            String statusLine = response.getStatusLine().toString();   
            if(statusLine.indexOf("200 OK") == -1) {
         	   System.out.println(statusLine); 
            }
            
            try{
      	
            	if(response.getStatusLine().toString().indexOf("302 Found") > 0) {
             	   return response.getFirstHeader("Location").getValue();
                }
                HttpEntity entity = response.getEntity(); 
                
                String res = EntityUtils.toString(entity);
                
                if(res != null && res.length() > 0 ){     
                	//System.out.println(res);
                    return res;
                }
            }finally{
                httpget.releaseConnection();
                response.close();
            }

        } catch (ClientProtocolException e) {  
            e.printStackTrace(); 
        } catch (ParseException e) {  
            e.printStackTrace(); 
        } catch (IOException e) {  
            e.printStackTrace(); 
        } catch (Exception e){
        	e.printStackTrace();
        }
        
        return null;
    }
    
    public static CloseableHttpResponse  execute(HttpUriRequest request) throws IOException, ClientProtocolException{
    	
    	long time1 = System.currentTimeMillis();
    	long time2 = System.currentTimeMillis();
    	
    	CloseableHttpResponse response;
    	
    	try{
    		response = httpclient.execute(request);    		
    		time2 = System.currentTimeMillis();    		
    		calcRequestAveTime(time2 - time1);
    		
    	}catch(Exception e){
    		
    		time2 = System.currentTimeMillis();
    		calcRequestAveTime(time2 - time1);
    		
    		throw e;
    	}
    	

    	
    	return response;
    	
    }
    
    
    /**��utf-8��ʽ��ȡ*/
    public static String doPost(String url,List<NameValuePair> formparams, String cookies, String refer) {
        return doPost(url, formparams,"UTF-8", cookies, refer);
    }

    public static String doPost(String url,List<NameValuePair> formparams,String charset, String cookies, String refer) {


     // ����httppost   
    	
    	try {
    	
        HttpPost httppost = new HttpPost(url); 
        //httppost.addHeader("Cookie", cookies);
        //httppost.addHeader("Accept-Encoding","Accept-Encoding: gzip, deflate, sdch");
        //httppost.addHeader("x-requested-with","XMLHttpRequest");
        
        if(cookies != "") {
        	httppost.addHeader("Cookie",cookies);
        }
        
        
        httppost.addHeader("Accept-Language","Accept-Language: zh-CN");
        httppost.addHeader("Accept","application/json, text/javascript, */*; q=0.01");
        httppost.addHeader("Accept-Encoding","gzip, deflate");
        httppost.addHeader("Connection","keep-alive");
        httppost.addHeader("Cache-Control","max-age=0");
        httppost.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.79 Safari/537.36");    
        
        if(refer != "")
        {
        	httppost.addHeader("Referer",refer);
        	
        }
        

        UrlEncodedFormEntity uefEntity;
        
            uefEntity = new UrlEncodedFormEntity(formparams, charset);
            httppost.setEntity(uefEntity);
            
            System.out.println("executing request " + httppost.getURI()); 
            
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(defaultTimeout).setConnectTimeout(defaultTimeout).build();
            httppost.setConfig(requestConfig);
            
            CloseableHttpResponse response = execute(httppost);
            try {
            	
            	
            	//to do remove hard code
            	if(url.contains("_index/c.php"))
            		setCookie(response);

            	
            	//System.out.println("����cookie:" + strCookies);
            	if(response.getStatusLine().toString().indexOf("302 Found") > 0) {
            		String location = response.getFirstHeader("Location").getValue();
            		System.out.println(response.getStatusLine());      		
            		if(location != null) {
            			return location;
            		}
            	}
            	
            	
                HttpEntity entity = response.getEntity(); 
                
                String res = EntityUtils.toString(entity);
                
                if(res != null && res.length() > 0 ){     
                	//System.out.println(res);
                    return res;
                }
            } finally {  
            	httppost.releaseConnection();
                response.close(); 
            }  
        } catch (ClientProtocolException e) {  
            e.printStackTrace(); 
        } catch (UnsupportedEncodingException e1) {  
            e1.printStackTrace(); 
        } catch (IOException e) {  
            e.printStackTrace(); 
     
        } catch(Exception e){
     	   e.printStackTrace();
        } 
        return null;
    }
    
	public static String setCookie(CloseableHttpResponse httpResponse)
	{
		
		//System.out.println("----setCookieStore");
		Header headers[] = httpResponse.getHeaders("Set-Cookie");
		if (headers == null || headers.length==0)
		{
			//System.out.println("----there are no cookies");
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
		
		for (String c : cookies)
		{
			if(c.indexOf("path=") != -1 || c.indexOf("expires=") != -1 || c.indexOf("domain=") != -1 || c.indexOf("HttpOnly") != -1)
				continue;
			strCookies += c;
			strCookies += ";";
		}
		//System.out.println("----setCookieStore success");

		return strCookies;
	}
    
	//todo
	public static boolean isInFreetime(){
		return false;
	}
	
	
    public static boolean isAllLotteryIdle(){
    	boolean isIdle = false;
    	
    	if(BetBJSCManager.isBJSCidle()){
    		isIdle = true;
    	}
    	
    	return isIdle;
    }
    
    
    public static boolean  isInFreetime(long time){
        Date date = new Date(time);
        int currentHour = date.getHours();
        int currentMinutes = date.getMinutes();
        int currentSeconds = date.getSeconds();
        
        /*if(currentHour >=9 && (currentHour * 60 + currentMinutes <= 23 * 60 + 57)){
        	return true;
        }*/
        
        //两分钟分钟的缓冲
        if((currentHour *60 + currentMinutes > 3*60) && (currentHour * 60 + currentMinutes <= 3 * 60 + 5)){
        	return true;
        }
        
        
        return false;
    }
    
    
    public synchronized static void addFailsTimes(){
    	long currentTime = System.currentTimeMillis();
    	
    	if(((currentTime - lastFailtime) < 40*1000) || (lastFailtime == 0)){
    		requestFailTimes++;
    		
    		lastFailtime = currentTime;
    		
    		if(requestFailTimes >= 4){
    			setIsNeedRelogin(true);
    			requestFailTimes = 0;
    		}
    		
    	}
    	else{
    		requestFailTimes = 1;
    		lastFailtime = currentTime;
    	}
    }
    
    
    public synchronized static void setIsNeedRelogin(boolean flag){
    	isNeedRelogin = flag;
    }
    
    public synchronized static boolean getIsNeedRelogin(){
    	return isNeedRelogin;
    }
    
    
    
    public synchronized static void calcRequestAveTime(long requestTime){
        
    	if(bcalcRequestTime == true){
    		
        	//requestCount++;
        	
    		long totalReqeustTime = 0;
    		
        	lastTenRequestTime.add(requestTime);
        	
        	while(lastTenRequestTime.size() >10){
        		lastTenRequestTime.remove(0);
        	}
        	
        	
        	if(lastTenRequestTime.size() == 10){
            	for(int i = 0; i < lastTenRequestTime.size(); i++){
            		totalReqeustTime += lastTenRequestTime.elementAt(i);
            	}
            	avgRequestTime = totalReqeustTime/lastTenRequestTime.size();
            	
            	
            	//System.out.printf("[迪斯尼会员]平均请求时间:%d\n", avgRequestTime);
            	
            	
            	long currentTime = System.currentTimeMillis();
            	
            	long passTime = currentTime - lastChangeLineTime;
            	
            	if(avgRequestTime >= 500 && passTime >= 90*1000){
            		setisNeedChangeLine(true);
            		lastChangeLineTime = currentTime;
            	}

        	}

    	}    	
    }
    
}
