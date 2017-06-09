package yabo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

import dsn.ConfigReader;
import dsn.ConfigWriter;


public class YaboHttp {
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
	
	static int defaultTimeout = 10*1000;
	
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
		
		String linePageUri = "http://99.333666.co";
		
		int oldTimeout = defaultTimeout;
		defaultTimeout = 10 * 1000;
		
		String html = null;
		
		try{
								
			Vector<Object[]> lines = new Vector<Object[]>();
			
			String linePage = doGet(linePageUri, "", "");
	    	
	    	if(linePage == null){
	    		return false;
	    	}	    	
	    	
	    	List<NameValuePair> params = new ArrayList<NameValuePair>();
	    	params.add(new BasicNameValuePair("co_txt", "8899"));
	    	params.add(new BasicNameValuePair("submit_bt", " 確 定 "));
	    	linePage = doPost(linePageUri + "/main.jsp", params, "", "");
	    	
	    	if(linePage == null) {
	    		linePage = doPost(linePageUri + "/main.jsp", params, "", "");
	    	}
	    	
	    	if(linePage == null) {
	    		return false;
	    	}
	    	
	    	int posStart;
	    	String lnkk_0 = null, lnkk_1 = null, lnkk_2 = null, lnkk_3 = null;
	    	posStart = linePage.indexOf("lnkk_0");
	    	if(posStart > 0) {
	    		posStart = linePage.indexOf("http", posStart);
	    		lnkk_0 = linePage.substring(posStart, linePage.indexOf("\"", posStart) - 1);
	    	}
	    	
	    	posStart = linePage.indexOf("lnkk_1");
	    	if(posStart > 0) {
	    		posStart = linePage.indexOf("http", posStart);
	    		lnkk_1 = linePage.substring(posStart, linePage.indexOf("\"", posStart) - 1);
	    	}
	    	
	    	posStart = linePage.indexOf("lnkk_2");
	    	if(posStart > 0) {
	    		posStart = linePage.indexOf("http", posStart);
	    		lnkk_2 = linePage.substring(posStart, linePage.indexOf("\"", posStart) - 1);
	    	}
	    	
	    	posStart = linePage.indexOf("lnkk_3");
	    	if(posStart > 0) {
	    		posStart = linePage.indexOf("http", posStart);
	    		lnkk_3 = linePage.substring(posStart, linePage.indexOf("\"", posStart) - 1);
	    	}
	    	
	    	long time = 0, fast = 999999;
	    	
	    	time = System.currentTimeMillis();
	    	linePage = doGet(lnkk_0, "", "");
	    	time = System.currentTimeMillis() - time;
	    	System.out.println(time);
	    	if(linePage != null) {
	    		fast = time;
	    		ADDRESS = lnkk_0;
	    	}
	    	
	    	time = System.currentTimeMillis();
	    	linePage = doGet(lnkk_1, "", "");
	    	time = System.currentTimeMillis() - time;
	    	System.out.println(time);
	    	if(linePage != null && time < fast) {
	    		fast = time;
	    		ADDRESS = lnkk_1;
	    	}
	    	
	    	time = System.currentTimeMillis();
	    	linePage = doGet(lnkk_2, "", "");
	    	time = System.currentTimeMillis() - time;
	    	System.out.println(time);
	    	if(linePage != null && time < fast) {
	    		fast = time;
	    		ADDRESS = lnkk_2;
	    	}
	    	
	    	time = System.currentTimeMillis();
	    	linePage = doGet(lnkk_3, "", "");
	    	time = System.currentTimeMillis() - time;
	    	System.out.println(time);
	    	if(linePage != null && time < fast) {
	    		fast = time;
	    		ADDRESS = lnkk_3;
	    	}
	    	   	
	    	success = loginToYabo();
	    	if(success) {
	    		ConfigWriter.updateYaboMemberAddress(ADDRESS);
				ConfigWriter.updateYaboMemberAccount(ACCOUNT);
				ConfigWriter.updateYaboMemberPassword(PWD);
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
	
	
    public static boolean loginToYabo(){
    	
    	try{	
    		String res = doGet(ADDRESS, "", "");
    		String code = getPicNum(ADDRESS + "/validateCodeServlet");
        	List<NameValuePair> params = new ArrayList<NameValuePair>();
	    	params.add(new BasicNameValuePair("account", ACCOUNT));
	    	params.add(new BasicNameValuePair("password", PWD));
	    	params.add(new BasicNameValuePair("checkCode", code));
        	res = doPost(ADDRESS + "/logon.do", params, "", "");
        	if(res == null) {
        		return false;
        	}
        	
        	res = doGet(ADDRESS + "/agreement.do", "", "");
        	if(res == null) {
        		return false;
        	}
        	
        	System.out.print(res);
        	res = doGet(ADDRESS + "/logoned.do", "", "");
        	if(res == null) {
        		return false;
        	}
        	
        	res = doGet(ADDRESS + "/CI_3.do?T=21&UVID=0", "", "");
        	if(res == null) {
        		return false;
        	}
        	
        	System.out.print(res);
        	if(res.contains("name=\"JeuValidate\"")) {
        		int start = res.indexOf("name=\"JeuValidate\"") - 28;
        		start = res.indexOf("value", start);
        		jeuValidate = res.substring(start + 7, res.indexOf("\"", start + 7));
        	} else {
        		System.out.println(res);
				return false;
			}
        	
        	
        
        	System.out.println(jeuValidate);

        	if(res != null) {
        		return true;
        	} 		
    	}catch(Exception e){
    		e.printStackTrace();
    	}

    	return false;
    }
    
    
    public static String getPicNum(String picUri) {
    	try {
    		HttpGet httpget = new HttpGet(picUri);
            
           
            httpget.addHeader("Accept-Encoding","gzip, deflate, sdch");
            httpget.addHeader("Accept-Language","zh-CN,zh;q=0.8");
            httpget.addHeader("Connection","keep-alive");
            httpget.addHeader("Upgrade-Insecure-Requests","1");
            httpget.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            //
            
            httpget.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.79 Safari/537.36");           
            System.out.println("executing request " + httpget.getURI()); 
    
            CloseableHttpResponse response = execute(httpget); 
            
            String statusLine = response.getStatusLine().toString();
           
       	 try {
       		    setCookie(response);
                // 打印响应状态    
                System.out.println(response.getStatusLine()); 
                System.out.println("------------------------------------");
                File storeFile = new File("ybyzm.png");   //图片保存到当前位置
                FileOutputStream output = new FileOutputStream(storeFile);  
                //得到网络资源的字节数组,并写入文件  
                byte [] a = EntityUtils.toByteArray(response.getEntity());
                output.write(a);  
                output.close();  
                
                InputStream ins = null;
        		 String[] cmd = new String[]{ConfigReader.getTessPath() + "\\tesseract", "ybyzm.png", "result", "-psm", "7", "digits"};

        		 Process process = Runtime.getRuntime().exec(cmd);
        		 // cmd 的信息
        		 ins = process.getInputStream();
        		 BufferedReader reader = new BufferedReader(new InputStreamReader(ins));

        		 String line = null;
        	  	 while ((line = reader.readLine()) != null) {
        	  		 System.out.println(line);
        		 }
        			
        		 int exitValue = process.waitFor();
        		 System.out.println("返回值：" + exitValue);
        		 process.getOutputStream().close();
        		 File file = new File("result.txt");
        		 reader.close();
                reader = new BufferedReader(new FileReader(file));
                 // 一次读入一行，直到读入null为文件结束
                String rmNum;
                rmNum = reader.readLine();
                reader.close();
                
                return rmNum;
       	 }
       	 finally{
       		httpget.releaseConnection();
       		 response.close(); 
       	 }
        } catch (ClientProtocolException e) {  
            e.printStackTrace(); 
        } catch (UnsupportedEncodingException e1) {  
            e1.printStackTrace(); 
        } catch (IOException e) {  
            e.printStackTrace(); 
        } catch (Exception e) {
				e.printStackTrace();
		 }
        
   	return null;
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
            
            
            try{
      	
            	if(response.getStatusLine().toString().indexOf("302 Found") > 0) {
             	   return response.getFirstHeader("Location").getValue();
                }
            	
            	if(statusLine.indexOf("200 OK") == -1) {
              	   System.out.println(statusLine);
              	   return null;
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
