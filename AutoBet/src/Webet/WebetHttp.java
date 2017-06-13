package Webet;



import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import dsn.BetGD11X5Manager;
import dsn.BetGDKLSFManager;
import dsn.BetGXKLSFManager;
import dsn.BetKL8Manager;
import dsn.BetTJSSCManager;
import dsn.BetXJSSCManager;
import dsn.BetXYNCManager;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebetHttp {
	
	
	
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
	
	
	
	static String changetobjuripart = "";
	
	public static String gameMainUri = "";

	
	
	
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
	
	public static String lineuri = "";
	
	
	
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
		
		setIscalcRequestTime(false);
		
		while(login() == false){
			
			strCookies = "";
			
			try{
				Thread.currentThread().sleep(20*1000);
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
			
		}
		
		setIscalcRequestTime(true);
		
		return true;
	}
	
	
	public static boolean connFailLogin(){
		
		setIscalcRequestTime(false);
		
		while(login() == false){
			
			strCookies = "";
			
			try{
				Thread.currentThread().sleep(20*1000);
				
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
		
		String linePageUri = ADDRESS;
		
		int oldTimeout = defaultTimeout;
		
		try{
			
			String line = "";
			
			String lineurisub = "";
			
			Vector<Object[]> lineWord = new Vector<Object[]>();
			
			String linePage = doGet(linePageUri, "", "");
			
			//System.out.println(linePage);
	    	
	    	if(linePage == null){
	    		linePage = doGet(linePageUri, "", "");
	    	}
	    	
	    	if(linePage != null && linePage.contains("indexs.jsp")){
	    		int ps = linePage.indexOf("\"/indexs.jsp?") + 1;
	    		int pe = linePage.indexOf("\"", ps);
	    		
	    		linePageUri = ADDRESS + linePage.substring(ps, pe);
	    		
	    		linePage = doGet(linePageUri, "", "");
	    		
		    	if(linePage == null){
		    		linePage = doGet(linePageUri, "", "");
		    	}
		    	
		    	if(linePage == null){
		    		return false;
		    	}
		    	
		    	ps = linePage.indexOf("/login_page.jsp?");
		    	pe = linePage.indexOf("\'", ps);
		    	
		    	lineurisub = linePage.substring(ps, pe);
	    				
	    	}
	    	
	    	
	    	int posStart = -1;
	    	int posEnd = -1;
	    	
	    	if(linePage != null){
	    		posStart = linePage.indexOf("urls[");
	    		posStart = linePage.indexOf("\"", posStart) + 1;
	    		
	    		posEnd = linePage.indexOf("\";", posStart);
	    		
	    		long leastTime = 2000;
	    		String chooseLine = "";
	    		
	    		
	    		
	    		while(posStart != -1){
	    			
	    			line = linePage.substring(posStart, posEnd);
	    			
	    			//http://kg688.net/speed.png?1496711567477
	    			
	    			String lineSpeedUri = "http://" + line  + "/speed.png?" + System.currentTimeMillis();
	    			
	    			
	    			
	    			defaultTimeout = 2000;
	    			
	    			long time = System.currentTimeMillis();
	    			
	    			String res = doGet(lineSpeedUri, "", "");
	    			
	    			long usingTime = System.currentTimeMillis() - time;
	    			
	    			if(res != null && usingTime <= leastTime){
	    				leastTime = usingTime;
	    				chooseLine = line;
	    			}
	    			
		    		posStart = linePage.indexOf("urls[", posEnd);
		    		posStart = linePage.indexOf("\"", posStart) + 1;
		    		
		    		posEnd = linePage.indexOf("\";", posStart);
		    		
		    		if(posEnd == -1){
		    			break;
		    		}
	    		}
	    		
	    		
	    		if(chooseLine != ""){	    	        
	    	        lineuri = "http://" + chooseLine;
	    		}
	    		
	    		defaultTimeout = oldTimeout;
	    		
	    		
	    		success = loginToWebet(lineurisub);
	    		
	    		
	    	}
			
		}catch(Exception e){
			defaultTimeout = oldTimeout;
			setIscalcRequestTime(true);
			e.printStackTrace();
		}

		setIscalcRequestTime(true);
		
		return success;
	}
	
	
    public static boolean loginToWebet(String urisub){
    	
    	try{
        	String uri = lineuri + urisub;
        	String res = doGet(uri, "", "");
        	
        	if(res == null){
        		res = doGet(uri, "", "");
        	}
        	
        	if(res != null){
            	int ps = res.indexOf("/entrance.jsp");
            	int pe = res.indexOf("\"", ps);
            	
            	String entrance = res.substring(ps, pe);
            	
            	res = doGet(lineuri + entrance, "", "");
            	
            	if(res == null){
            		res = doGet(lineuri + entrance, "", "");
            	}
            	
            	if(res == null){
            		return false;
            	}
            	
            	
        	}else{
        		return false;
        	}
        	

        	int ps = res.indexOf("login.php");
        	int pe = res.indexOf("\"", ps);
        	
        	String loginphp = res.substring(ps, pe);
        	
        	
        	List<NameValuePair> loginParams = new ArrayList<NameValuePair>();
        	
        	loginParams.add(new BasicNameValuePair("action", "login"));
        	
        	//CQ
        	ps = res.indexOf("CQ", pe);
        	ps = res.indexOf("value", ps);
        	ps = res.indexOf("\"", ps) + 1;
        	pe = res.indexOf("\"", ps);        	
        	String cq = res.substring(ps, pe);
        	loginParams.add(new BasicNameValuePair("CQ", cq));
        	
        	//SS
        	ps = res.indexOf("SS", pe);
        	ps = res.indexOf("value", ps);
        	ps = res.indexOf("\"", ps) + 1;
        	pe = res.indexOf("\"", ps);        	
        	String ss = res.substring(ps, pe);
        	loginParams.add(new BasicNameValuePair("SS", ss));
        	
        	//SR
        	ps = res.indexOf("SR", pe);
        	ps = res.indexOf("value", ps);
        	ps = res.indexOf("\"", ps) + 1;
        	pe = res.indexOf("\"", ps);        	
        	String sr = res.substring(ps, pe);
        	loginParams.add(new BasicNameValuePair("SR", sr));
        	
        	//TS
        	ps = res.indexOf("TS", pe);
        	ps = res.indexOf("value", ps);
        	ps = res.indexOf("\"", ps) + 1;
        	pe = res.indexOf("\"", ps);        	
        	String ts = res.substring(ps, pe);
        	loginParams.add(new BasicNameValuePair("TS", ts));
        	
        	//langx
        	ps = res.indexOf("langx", pe);
        	ps = res.indexOf("value", ps);
        	ps = res.indexOf("\"", ps) + 1;
        	pe = res.indexOf("\"", ps);        	
        	String langx = res.substring(ps, pe);
        	loginParams.add(new BasicNameValuePair("langx", langx));
        	
        	//tpl
        	ps = res.indexOf("tpl", pe);
        	ps = res.indexOf("value", ps);
        	ps = res.indexOf("\"", ps) + 1;
        	pe = res.indexOf("\"", ps);        	
        	String tpl = res.substring(ps, pe);
        	loginParams.add(new BasicNameValuePair("tpl", tpl));


        	loginParams.add(new BasicNameValuePair("LoadingTime", "0.1"));
        	
        	//get_info_key
        	ps = res.indexOf("get_info_key", pe);
        	ps = res.indexOf("value", ps);
        	ps = res.indexOf("\"", ps) + 1;
        	pe = res.indexOf("\"", ps);        	
        	String get_info_key = res.substring(ps, pe);
        	get_info_key = get_info_key.replace(" ", "+");
        	loginParams.add(new BasicNameValuePair("get_info_key", get_info_key));
        	
        	//get_info_key2
        	ps = res.indexOf("get_info_key2", pe);
        	ps = res.indexOf("value", ps);
        	ps = res.indexOf("\"", ps) + 1;
        	pe = res.indexOf("\"", ps);        	
        	String get_info_key2 = res.substring(ps, pe);
        	loginParams.add(new BasicNameValuePair("get_info_key2", get_info_key2));
        	
        	loginParams.add(new BasicNameValuePair("Account", ACCOUNT));
        	loginParams.add(new BasicNameValuePair("PassWD", PWD));


        	
        	
        	//��ȡcookies
        	String loginphpUri = lineuri + "/" + loginphp;

            
            res = doPost(loginphpUri, loginParams, "", "");
            
           // System.out.println(res);
            
            
            
            ps = res.indexOf("agree.jsp?");
            pe = res.indexOf("\'", ps);
            
            
            
            
            String agreeUri = lineuri + "/" + res.substring(ps, pe);
            
            res = doGet(agreeUri, "", uri);
            
            if(res == null){
            	res = doGet(agreeUri, "", uri);
            }

            ps = res.indexOf("agree.jsp?");
            pe = res.indexOf("\"", ps);
            
            
            loginParams.clear();
            loginParams.add(new BasicNameValuePair("LoadingTime", "0.15"));
            loginParams.add(new BasicNameValuePair("LoadingTimeAgree", "0.15"));
            
            res = doPost(lineuri + "/" + res.substring(ps, pe), loginParams, "", "");
            
            
            
            
            res = doGet(lineuri + "/" + res, "", "");
            
           // System.out.println(res);
            
            if(res.contains("登入信息")){
            	System.out.println("登入成功");
            	
            	//String test = "123p=fjdkslajfsdklfjklds;ajflsdjf bj";
        		
                String input = "p=.*BJ";
                
                Pattern p = Pattern.compile(input);
                Matcher m = p.matcher(res);
                
                if(m.find())
                {
                    
                    
                    changetobjuripart = res.substring(m.start(), m.end());
                   // changetobjuripart = changetobjuripart.replace("&25", "%");
                    
                   // changeToBJgame();
                    
                   // System.out.println(changetobjuripart);
                }
            	
            	
            	return true;
            }
    		
    	}catch(Exception e){
    		
    		e.printStackTrace();
    		return false;
    	}

	        

	   return false;	
        
        

    	
    }
    
    
    public static String changeToBJgame(){
    	
    	try{
    		
        	String changeUri = lineuri + "/chang_game.php?" + changetobjuripart;
        	String res = doGet(changeUri, "", "");
        	
        	if(res == null){
        		res = doGet(changeUri, "", "");
        	}
        	
        	int ps = res.indexOf("mainFrameObj.src") + 1;
        	ps = res.indexOf("mainFrameObj.src");
        	ps = res.indexOf("\'", ps) + 1;
        	int pe = res.indexOf("\'", ps);
        	
        	String gameframeurlpart = res.substring(ps, pe);
        	
        	res = doGet(lineuri + gameframeurlpart, "", "");
        	
        	if(res == null){
        		res = doGet(lineuri + gameframeurlpart, "", "");
        	}
        	
        	ps = res.indexOf("game_main.jsp");
        	pe = res.indexOf("\"", ps);
        	
        	String gamemainurlpart = res.substring(ps, pe);
        	
        	//gamemainurlpart = gamemainurlpart.replace("%25", "%");
        	
        	//res =  doGet(lineuri + "/d3m1/bjapp/game/" + gamemainurlpart, "", "");
        	
        	gameMainUri = lineuri + "/d3m1/bjapp/game/" + gamemainurlpart;
        	
/*        	if(res == null){
        		res = doGet(lineuri + "/d3m1/bjapp/game/" + gamemainurlpart, "", "");
        	}*/
        	
        	//System.out.println(res);
        	
        	

        	
        	
        	
        	
        	
        	return res;
    		
    	}catch(Exception e){
    		e.printStackTrace();
    		return null;
    	}
    	

    	
    	
    	
    	
    	
    }
    
    
    
    
    
    
    public static String doGet(String url, String cookies, String referUrl) {
    	
        try {  
            // ����httpget.    
            HttpGet httpget = new HttpGet(url);
            
            if(cookies != "") {
            	httpget.addHeader("Cookie",cookies);
            }
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
           
            //���ó�ʱ
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(defaultTimeout).setConnectTimeout(defaultTimeout).build();
            httpget.setConfig(requestConfig);
            
            // ִ��get����.    
            CloseableHttpResponse response = execute(httpget); 
            
            String statusLine = response.getStatusLine().toString();   
            if(statusLine.indexOf("200 OK") == -1) {
         	   System.out.println(statusLine); 
            }
            
            try{
            	
            	//System.out.println("����cookie:" + strCookies);
            	
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
        httppost.addHeader("x-requested-with","XMLHttpRequest");
        
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
                // ��ӡ��Ӧ״̬    
            	
            	
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
