import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Date;
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
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.protocol.HttpClientContext;

import java.io.*; 

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.Vector;
import java.util.Comparator;
import java.util.Collections;

public class DsnProxyGrab {
	static CloseableHttpClient httpclient = null;
    static RequestConfig requestConfig = null;
    static HttpClientContext clientContext = null;
    
    //static Map<String,String> cookieMap = new HashMap<String, String>(64);
    static String strCookies = "";
    static String cookieuid = "";
    static String cookiedae = "";
    static String [] dataCQSSC = {"", "", ""};
    static String [] dataBJSC = {"", "", "", "", ""};
    static String [] dataXYNC = {"", "", "", "", "", "", "", "", "", "", ""};
    static boolean isCQSSCdataOk = false;
    static boolean isBJSCdataOk = false;
    static boolean isXYNCdataOk = false;
    
    private static String ADDRESS = "";
    private static String ACCOUNT = "";
    private static String PASSWORD = "";
    private static long timeDValue = 0;  //网站时间和电脑时间的差值  网页时间 - 当前时间
    
    static {
         requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
         requestConfig = RequestConfig.copy(requestConfig).setRedirectsEnabled(false).build();//禁止重定向 ， 以便获取cookiedae
         requestConfig = RequestConfig.copy(requestConfig).setConnectTimeout(15*1000).setConnectionRequestTimeout(15*1000).setSocketTimeout(15*1000).build();//设置超时
         httpclient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
    }
    
    
    //优化线路选择
    static Vector<Object[]> lines;
    
    static Vector<Long> lastTenRequestTime = new Vector<Long>();
    static long avgRequestTime = 0;    
    static boolean bcalcRequestTime = true;
    static boolean bneedChangeLine = false;
    
    
    
    

	public static String setCookie(CloseableHttpResponse httpResponse) {
		//System.out.println("----setCookieStore");
		Header headers[] = httpResponse.getHeaders("Set-Cookie");
		if (headers == null || headers.length==0)
		{
			//System.out.println("----there are no cookies");
			return "";
		}
		String cookie = "";
		for (int i = 0; i < headers.length; i++) {
			cookie += headers[i].getValue();
			if(i != headers.length-1)
			{
				cookie += ";";
			}
		}
		//System.out.println("----cookies:" + cookie);
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
		//System.out.println("----cookies:" + strCookies);
		//System.out.println("----setCookieStore success");

		return strCookies;
	}
    
	private static void loginInit() {
		strCookies = "";
        cookieuid = "";
        cookiedae = "";
        clientContext = HttpClientContext.create();
        clientContext.setRequestConfig(requestConfig);
	}

    public static boolean doLogin() { 	
        loginInit();
        String loginPage = doGet(ADDRESS + "/login", ""); //get 登录页面
        
        if(loginPage != "" && loginPage != "timeout") {
        	cookieuid = strCookies;
        	int posStart = loginPage.indexOf("img src=") + 9;
        	if(posStart >= 0) {
        		int posEnd = loginPage.indexOf('"', posStart);
        		String rmNum = getPicNum(ADDRESS + "/" + loginPage.substring(posStart, posEnd));//get 验证码
        		System.out.println("yzm: " + rmNum); 
        		if(!Common.isNum(rmNum)) {
        			return false;
        		}
        	
        		//发送post
        		List<NameValuePair> params = new ArrayList<NameValuePair>();
        		params.add(new BasicNameValuePair("type", "2"));
        		params.add(new BasicNameValuePair("account", ACCOUNT));
        		params.add(new BasicNameValuePair("password", PASSWORD));
        		params.add(new BasicNameValuePair("code", rmNum));
        		String location = doPost(ADDRESS + "/login", params, strCookies, "");
        		
        		System.out.println("location: " + location); 

            
        		if(location.indexOf("index?") > 0) {
        			strCookies = "";
        			if(location.indexOf("http") >= 0) {
        				location = doGet(location, cookieuid);//get cookiedae和重定向url
        			}
        			else {
        				location = doGet(ADDRESS + location, cookieuid);
        			}
        			cookiedae = strCookies;
        			strCookies = "";
        			if(location.indexOf("http") >= 0) {
        				location = doGet(location, cookieuid + cookiedae);
        			}
        			else {
        				location = doGet(ADDRESS + location, cookieuid + cookiedae);
        			}
        			if(location != "" && location != "timeout"){
        				return true;
        			}
        		}
            
        	}
        }
         
        return false;
    }
    
    public static void setLoginParams(String address, String account, String password){
    	ADDRESS = address;
    	ACCOUNT = account;
    	PASSWORD = password;
    }
    
    public static void setLoginAddress(String address){
    	ADDRESS = address;
    }
    
    
    public static void initLines(){
    	String[] addressArray = ConfigReader.getProxyAddressArray();
    	

    	
    	lines = new Vector<Object[]>(addressArray.length);
    	
    	for(int k = 0; k < addressArray.length; k++){
    		lines.add(new Object[2]);
    		lines.elementAt(k)[0] = new String(addressArray[k]);
    		
    		lines.elementAt(k)[1] = new Long(10*1000);
    	}
    }
    
    
    
    @SuppressWarnings("unchecked")
	public static void setLinePriority(){
    	
    	boolean res = false;

    	long timeStart;
    	long timeEnd;
    	
    	long averageTime;
    	
    	
    	setIscalcRequestTime(false);


    	for(int k = 0; k < lines.size(); k++){
    		

    		
    		setLoginAddress((String)lines.elementAt(k)[0]);
    		
    		int i =0;
    		
    		long timePassing = 0;
    		
    		timeStart = System.currentTimeMillis();
    		
    		for( i = 0; i < 4; i++) {
    			
    			
    			
        		if(doLogin()) {
        			res = true;

        			break;
        		}
        		
        		long time1 = System.currentTimeMillis();
        		
        		timePassing += (time1 - timeStart);
        		
        		if(timePassing > 10){
        			break;
        		}
        	}
    		
    		timeEnd = System.currentTimeMillis();
    		
    		long usingTime = timeEnd - timeStart;
    		
    		
    		averageTime = usingTime/(i+1);
    		lines.elementAt(k)[1] = averageTime;
    		
    		
    		
    		res = false;
    		
    		

    	}

    	
    	Comparator ct = new MyCompare();
    	
    	Collections.sort(lines, ct);
    	
    	setIscalcRequestTime(true);
    	

    	System.out.println("【代理】线路快慢排序:");
    	
    	for(int j = 0; j < lines.size(); j++){
    		
    		
    		
    		System.out.println(lines.elementAt(j)[0]);
    		
    		System.out.println(lines.elementAt(j)[1]);
    		
    	}
    }
    
    
    public static void calcRequestAveTime(long requestTime){
        
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
            	
            	
            	System.out.printf("[代理]平均请求时间:%d\n", avgRequestTime);
            	
            	if(avgRequestTime >= 400){
            		setisNeedChangeLine(true);
            	}

        	}

    	}

    		
    	
    }
    
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
    
    
    public static boolean login() {  
    	
    	
    	boolean res = false;

    	setIscalcRequestTime(false);
		
    	for(int i = 0; i < 8; i++) {
    		if(doLogin()) {
    			res = true;
    			break;
    		}
    	}
        	
    	String currentAddress = ADDRESS;

    	if(res == false){
    		
        	String[] addressArray = ConfigReader.getProxyAddressArray();
        	
        	
        	
        	for(int k = 0; k < addressArray.length; k++){
        		
        		
        		if(currentAddress.equals(addressArray[k]))
        				continue;
        		
        		setLoginAddress(addressArray[k]);
        		
        		for(int i = 0; i < 8; i++) {
            		if(doLogin()) {
            			res = true;
            			
            			ConfigWriter.updateProxyAddress(ADDRESS);//更新到现在登得上的网址
            			
            			ConfigWriter.saveTofile("common.config");
            			
            			break;
            		}
            	}
            	
            	if(res == true){
            		break;
            	}
        	}
    	}
    	
    	
    	setIscalcRequestTime(true);

    	    	
    	return res;
    	

    }
    
    public static boolean reLogin() {  
    	
    	boolean res = false;

    	setIscalcRequestTime(false);

    	
    	
    	
    	
    	for(int k = 0; k < lines.size(); k++){

    		
    		setLoginAddress((String)lines.elementAt(k)[0]);
    		
    		for(int i = 0; i < 10; i++) {
        		if(doLogin()) {
        			res = true;

        			
        			break;
        		}
        	}
        	
        	if(res == true){
        		break;
        	}
    	}

    	
    	setIscalcRequestTime(true);
    	

    	    	
    	return res;
    	

    }
    
    
    
    
    
    public static void connFailLogin() {
    	
    	
    	boolean res = false;
    	
    	setIscalcRequestTime(false);

    	while(res == false){
    		
        	
        	
        	
        	
        	for(int k = 0; k < lines.size(); k++){
	
        		setLoginAddress((String)lines.elementAt(k)[0]);
        		
        		for(int i = 0; i < 10; i++) {
            		if(doLogin()) {
            			res = true;
		
            			break;
            		}
            	}
            	
            	if(res == true){
            		break;
            	}
        	}
        	
        	if(res == false){
            	try{
            		Thread.currentThread().sleep(20*1000);
            	}catch(Exception e){
            		
            	}
        	}
        	
        	
    	}
    	
    	setIscalcRequestTime(true);
    	

    }


    /**以utf-8形式读取*/
    public static String doPost(String url, List<NameValuePair> formparams, String cookies, String referUrl) {
        return doPost(url, formparams, cookies, "UTF-8", referUrl);
    }

    public static String doPost(String url,List<NameValuePair> formparams, String cookies, String charset, String referUrl) {  
        
    	try {
    	HttpPost httppost = new HttpPost(url); 
        httppost.addHeader("Accept-Encoding","Accept-Encoding: gzip, deflate");
        httppost.addHeader("Origin","http://a1.dio168.net");
        httppost.addHeader("Connection","keep-alive");
        httppost.addHeader("Cache-Control","no-cache");
        httppost.addHeader("Referer", referUrl);
        httppost.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36"
        					+ " (KHTML, like Gecko) Chrome/51.0.2704.79 Safari/537.36");
        if(cookies != ""){
        	httppost.addHeader("Cookie", cookies);
        }
        
        System.out.println("executing request " + httppost.getURI()); 

       
        UrlEncodedFormEntity uefEntity;
        
            uefEntity = new UrlEncodedFormEntity(formparams, charset);
            httppost.setEntity(uefEntity);
            CloseableHttpResponse response = execute(httppost);
            try {
                // 打印响应状态    
            	setCookie(response);
            	System.out.println(response.getStatusLine());
            	if(response.getStatusLine().toString().indexOf("302 Found") > 0) {
            		String location = response.getFirstHeader("Location").getValue();
            		//System.out.println(response.getStatusLine());
            		if(location != null) {
            			return location;
            		}
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
            if(e.getMessage().indexOf("timed out") > 0) {
            	return "timeout";
            }
        } 
    	catch (Exception e) {  
            e.printStackTrace();
        } 
    	
        return "";
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
    
    
    public static String doGet(String url, String cookies) { 
        try {  
           // 创建httpget.    
           //System.out.println(cookies); 
           HttpGet httpget = new HttpGet(url);
           
           httpget.addHeader("Accept-Encoding","Accept-Encoding: gzip, deflate, sdch");
           httpget.addHeader("Accept-Language","Accept-Language: zh-CN,zh;q=0.8");
           httpget.addHeader("Cache-Control","no-cache");
           httpget.addHeader("Connection","keep-alive");
           //httpget.addHeader("Content-Type","application/json; charset=UTF-8");
           //httpget.addHeader("Referer","http://www.lashou.com/");
           httpget.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36"
           					+ " (KHTML, like Gecko) Chrome/51.0.2704.79 Safari/537.36");  
           if(cookies != ""){
        	   httpget.addHeader("Cookie", cookies);
           }
           
           System.out.println("executing request " + url); 
          
           // 执行get请求.    
           CloseableHttpResponse response = execute(httpget); 
           //CloseableHttpResponse response = httpclient.execute(httpget); 
           
           try {          	           	   
               // 获取响应实体    
        	   setCookie(response);
               HttpEntity entity = response.getEntity(); 
               //System.out.println("--------------------------------------"); 
               
               String statusLine = response.getStatusLine().toString();
               if(statusLine.indexOf("200 OK") == -1) {
            	   System.out.println(statusLine); 
               }
               if(statusLine.indexOf("302 Found") > 0) {
            	   return response.getFirstHeader("Location").getValue();
               }
               //System.out.println("------------------------------------"); 
               if (entity != null) {
	            	String entityStr = EntityUtils.toString(entity);
	            	//System.out.println("entityStr: " + entityStr);
	            	if(entityStr.length() == 0) {
	            		return "";
	            	}
	            	return entityStr;
               }
           } 
           finally {
        	   httpget.releaseConnection();
               response.close(); 
           }  
       } catch (ClientProtocolException e) {  
           e.printStackTrace(); 
       } catch (ParseException e) {  
           e.printStackTrace(); 
       } catch (IOException e) {  
           e.printStackTrace(); 
           if(e.getMessage().indexOf("timed out") > 0) {
        	   return "timeout";
           }
       }
        catch (Exception e) {  
            e.printStackTrace();
        } 
        return "";
    }
    
    public static String getPicNum(String picUri) {
    	try {
    	 HttpGet httpget = new HttpGet(picUri);
         httpget.addHeader("Connection","keep-alive");
         if(strCookies != ""){
        	 httpget.addHeader("Cookie", strCookies);
         }
         httpget.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 "
         					+ "(KHTML, like Gecko) Chrome/51.0.2704.79 Safari/537.36");           
         System.out.println("executing request " + httpget.getURI()); 
        
         // 执行get请求.    
        	 CloseableHttpResponse response = execute(httpget); 
        	 try {
        		 setCookie(response);
                 // 打印响应状态    
                 System.out.println(response.getStatusLine()); 
                 //System.out.println("------------------------------------");
                 if(response.getStatusLine().toString().indexOf("200 OK") < 0) {
                	 return "";
                 }
                 File storeFile = new File("yzm.png");   //图片保存到当前位置
                 FileOutputStream output = new FileOutputStream(storeFile);  
                 //得到网络资源的字节数组,并写入文件  
                 byte [] a = EntityUtils.toByteArray(response.getEntity());
                 output.write(a);  
                 output.close();  
                 
          
                 InputStream ins = null;
         		 String[] cmd = new String[]{ConfigReader.getTessPath() + "\\tesseract", "yzm.png", "result", "-l", "eng"};

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
    	
         
    	return "";
    }
    
  //! @brief 抓取cqssc下单数据
  //! @param game       两面:"LM", 单号:"DH", 前中后三:"QZHS"
  //! @param all        虚注:"XZ" 实占:"SZ" 补货:"BH"
  //! @param range      全部:"", A盘:"A", B盘:"B", C盘:"C", D盘:"D",
  //! @return           成功:String 连接失败:null 超时:"timeout" 
    public static String grabCQSSCdata(String game, String all, String range){
    	if((game == "LM" || game == "DH" || game == "QZHS") && (range == "" || range == "A" ||
    			range == "B" || range == "C" || range == "D") && (all == "XZ" || all == "SZ" || all == "BH")) {
    		switch (game) {
    		    case "LM":
    		      game = "DX1%2CDS1%2CDX2%2CDS2%2CDX3%2CDS3%2CDX4%2CDS4%2CDX5%2CDS5%2CZDX%2CZDS%2CLH";
    		      break;
    		    case "DH":
      		      game = "B1%2CB2%2CB3%2CB4%2CB5";
      		      break;
    		    case "QZHS":
        		  game = "TS1%2CTS2%2CTS3";
        		  break;
    		   default :
    		}
    		long time =  System.currentTimeMillis();
    		String strTime = Long.toString(time);
    		String data = doGet(ADDRESS + "/agent/control/risk?lottery=CQSSC&games=" + game +"&all=" 
    								+ all + "&range=" + range + "&multiple=false&_=" + strTime, cookieuid + cookiedae);
    		if(data != "") {
    			return data;
    		}
    	}
    	return null;
    }
    
    
    //! @brief 抓取cqssc下单数据
    //! @param game       冠亚:"GY", 三四五六:"SSWL", 七八九十:"QBJS"
    //! @param all        虚注:"XZ" 实占:"SZ" 补货:"BH"
    //! @param range      全部:"", A盘:"A", B盘:"B", C盘:"C", D盘:"D",
    //! @return           成功:String 连接失败:null 超时:"timeout" 
      public static String grabBJSCdata(String game, String all, String range){
      	if((game == "GY" || game == "SSWL" || game == "QBJS") && (range == "" || range == "A" ||
      			range == "B" || range == "C" || range == "D") && (all == "XZ" || all == "SZ" || all == "BH")) {
      		switch (game) {
      		    case "GY":
      		      game = "GYH%2CGDX%2CGDS%2CB1%2CDX1%2CDS1%2CLH1%2CB2%2CDX2%2CDS2%2CLH2";
      		      break;
      		    case "SSWL":
        		      game = "B3%2CDX3%2CDS3%2CLH3%2CB4%2CDX4%2CDS4%2CLH4%2CB5%2CDX5%2CDS5%2CLH5%2CB6%2CDX6%2CDS6%2CLH6";
        		      break;
      		    case "QBJS":
          		  game = "B7%2CDX7%2CDS7%2CB8%2CDX8%2CDS8%2CB9%2CDX9%2CDS9%2CB10%2CDX10%2CDS10%2C";
          		  break;
      		   default :
      		}
      		long time =  System.currentTimeMillis();
      		String strTime = Long.toString(time);
      		String data = doGet(ADDRESS + "/agent/control/risk?lottery=BJPK10&games=" + game +"&all=" 
      								+ all + "&range=" + range + "&multiple=false&_=" + strTime, cookieuid + cookiedae);
      		if(data != "") {
      			return data;
      		}
      	}
      	return null;
      }
      
      public static boolean grabXYNCdata() {
    	  String data1 = "";
    	  String data2 = "";
    	  String data3 = "";
    	  String data4 = "";
    	  String data5 = "";
    	  String data6 = "";
    	  String data7 = "";
    	  String data8 = "";
    	  String data9 = "";
    	  
    	  long time =  System.currentTimeMillis();
    	  long timeStart = time;
    	  String strTime = Long.toString(time);
    	  for(int i = 0; i < 3; i++) {
    		  data1 = doGet(ADDRESS + "/agent/control/risk?lottery=XYNC&games=B1%2CDX1%2CDS1%2CWDX1%2CHDS1%2CFW1%2CZFB1%2CLH1"
    	    	  		+ "&all=XZ&range=&multiple=false&_"  + strTime, cookieuid + cookiedae);
    		  if(data1 != "" && data1 != "timeout") {
    			  break;
    		  }  
    	  }
    	  
    	  if(data1 == "" || data1 == "timeout") {
    		  return false;
    	  }
    	  
    	  time =  System.currentTimeMillis();
    	  strTime = Long.toString(time);
    	  for(int i = 0; i < 3; i++) {
    		  data2 = doGet(ADDRESS + "/agent/control/risk?lottery=XYNC&games=B2%2CDX2%2CDS2%2CWDX2%2CHDS2%2CFW2%2CZFB2%2CLH2"
    	    	  		+ "&all=XZ&range=&multiple=false&_"  + strTime, cookieuid + cookiedae);
    		  if(data2 != "" && data2 != "timeout") {
    			  break;
    		  }  
    	  }
    	  
    	  if(data2 == "" || data2 == "timeout") {
    		  return false;
    	  }
    	  
    	  time =  System.currentTimeMillis();
    	  strTime = Long.toString(time);
    	  for(int i = 0; i < 3; i++) {
    		  data3 = doGet(ADDRESS + "/agent/control/risk?lottery=XYNC&games=B3%2CDX3%2CDS3%2CWDX3%2CHDS3%2CFW3%2CZFB3%2CLH3"
    	    	  		+ "&all=XZ&range=&multiple=false&_"  + strTime, cookieuid + cookiedae);
    		  if(data3 != "" && data3 != "timeout") {
    			  break;
    		  }  
    	  }
    	  
    	  if(data3 == "" || data3 == "timeout") {
    		  return false;
    	  }
    	  
    	  time =  System.currentTimeMillis();
    	  strTime = Long.toString(time);
    	  for(int i = 0; i < 3; i++) {
    		  data4 = doGet(ADDRESS + "/agent/control/risk?lottery=XYNC&games=B4%2CDX4%2CDS4%2CWDX4%2CHDS4%2CFW4%2CZFB4%2CLH4"
    	    	  		+ "&all=XZ&range=&multiple=false&_"  + strTime, cookieuid + cookiedae);
    		  if(data4 != "" && data4 != "timeout") {
    			  break;
    		  }  
    	  }
    	  
    	  if(data4 == "" || data4 == "timeout") {
    		  return false;
    	  }
    	  
    	  time =  System.currentTimeMillis();
    	  strTime = Long.toString(time);
    	  for(int i = 0; i < 3; i++) {
    		  data5 = doGet(ADDRESS + "/agent/control/risk?lottery=XYNC&games=B5%2CDX5%2CDS5%2CWDX5%2CHDS5%2CFW5%2CZFB5%2CLH5"
    	    	  		+ "&all=XZ&range=&multiple=false&_"  + strTime, cookieuid + cookiedae);
    		  if(data5 != "" && data5 != "timeout") {
    			  break;
    		  }  
    	  }
    	  
    	  if(data5 == "" || data5 == "timeout") {
    		  return false;
    	  }
    	  
    	  time =  System.currentTimeMillis();
    	  strTime = Long.toString(time);
    	  for(int i = 0; i < 3; i++) {
    		  data6 = doGet(ADDRESS + "/agent/control/risk?lottery=XYNC&games=B6%2CDX6%2CDS6%2CWDX6%2CHDS6%2CFW6%2CZFB6%2CLH6"
    	    	  		+ "&all=XZ&range=&multiple=false&_"  + strTime, cookieuid + cookiedae);
    		  if(data6 != "" && data6 != "timeout") {
    			  break;
    		  }  
    	  }
    	  
    	  if(data6 == "" || data6 == "timeout") {
    		  return false;
    	  }
    	  
    	  time =  System.currentTimeMillis();
    	  strTime = Long.toString(time);
    	  for(int i = 0; i < 3; i++) {
    		  data7 = doGet(ADDRESS + "/agent/control/risk?lottery=XYNC&games=B7%2CDX7%2CDS7%2CWDX7%2CHDS7%2CFW7%2CZFB7%2CLH7"
    	    	  		+ "&all=XZ&range=&multiple=false&_"  + strTime, cookieuid + cookiedae);
    		  if(data7 != "" && data7 != "timeout") {
    			  break;
    		  }  
    	  }
    	  
    	  if(data7 == "" || data7 == "timeout") {
    		  return false;
    	  }
    	  
    	  time =  System.currentTimeMillis();
    	  strTime = Long.toString(time);
    	  for(int i = 0; i < 3; i++) {
    		  data8 = doGet(ADDRESS + "/agent/control/risk?lottery=XYNC&games=B8%2CDX8%2CDS8%2CWDX8%2CHDS8%2CFW8%2CZFB8%2CLH8"
    	    	  		+ "&all=XZ&range=&multiple=false&_"  + strTime, cookieuid + cookiedae);
    		  if(data8 != "" && data8 != "timeout") {
    			  break;
    		  }  
    	  }
    	  
    	  if(data8 == "" || data8 == "timeout") {
    		  return false;
    	  }
    	  
    	  time =  System.currentTimeMillis();
    	  strTime = Long.toString(time);
    	  for(int i = 0; i < 3; i++) {
    		  data9 = doGet(ADDRESS + "/agent/control/risk?lottery=XYNC&games=ZM%2CZDX%2CZDS%2CZWDX"
    	    	  		+ "&all=XZ&range=&multiple=false&_"  + strTime, cookieuid + cookiedae);
    		  if(data9 != "" && data9 != "timeout") {
    			  break;
    		  }  
    	  }
    	  
    	  if(data9 == "" || data9 == "timeout") {
    		  return false;
    	  }
    	  
    	  time =  System.currentTimeMillis();
    	  System.out.println("抓取用时 :"  + (time - timeStart));
    	  
//    	  for(int i = 1; i < 10; i++) {
//    		  System.out.println("农场数据:"  + dataXYNC[i]);
//    	  }
    	  
    	  dataXYNC[1] = data1;
    	  dataXYNC[2] = data2;
    	  dataXYNC[3] = data3;
    	  dataXYNC[4] = data4;
    	  dataXYNC[5] = data5;
    	  dataXYNC[6] = data6;
    	  dataXYNC[7] = data7;
    	  dataXYNC[8] = data8;
    	  dataXYNC[9] = data9;
    	  
    	  return true;
      }
      
      public static void setCQSSCdata(String drawNumber, String data, String remainTime) {
    	  synchronized(DsnProxyGrab.class) {
	    	  dataCQSSC[0] = drawNumber;
	    	  dataCQSSC[1] = data;
	    	  dataCQSSC[2] = remainTime;
	    	  isCQSSCdataOk = true;
	    	  System.out.println("set  CQSSCdata   ok");
    	  }
      }
      
      public static void setBJSCdata(String drawNumber, String [] data, String remainTime) {
    	  synchronized(DsnProxyGrab.class) {
	    	  dataBJSC[0] = drawNumber;
	    	  dataBJSC[1] = data[0];
	    	  dataBJSC[2] = data[1];
	    	  dataBJSC[3] = data[2];
	    	  dataBJSC[4] = remainTime;
	    	  isBJSCdataOk = true;
	    	  System.out.println("set  BJSCdata   ok");
    	  }
      }
      
      public static void setXYNCdata(String drawNumber, String remainTime) {
    	  synchronized(DsnProxyGrab.class) {
	    	  dataXYNC[0] = drawNumber;
	    	  dataXYNC[10] = remainTime;
	    	  isXYNCdataOk = true;
	    	  System.out.println("set  XYNCdata   ok");
    	  }
      }
      
      
      //! @brief    读取cqssc下单数据
      //! @return   数据可用(String[0]:期数， String[1]:data, String[2]:数据时间);  数据不可用(null)
      public static String[] getCQSSCdata() {
	    	  synchronized(DsnProxyGrab.class) {
		    	  if(isCQSSCdataOk) {
		    		  return dataCQSSC;
		    	  }
		    	  return null;
	    	  }
      }
      
      //! @brief     读取取BJSC下单数据
      //! @return    数据可用(String[0]:期数, String[1]:冠亚, String[2]:三四五六, String[3]:七八九十, String[4]:数据时间); 数据不可用(null)
      public static String[] getBJSCdata() {
    	  synchronized(DsnProxyGrab.class) {
	    	  if(isBJSCdataOk) {
	    		  return dataBJSC;
	    	  }
	    	  return null;
    	  }
      }
      
      //! @brief     读取取XYNC下单数据
      //! @return    数据可用(String[0]:期数, String[1~9]:下单数据, String[10]:数据时间); 数据不可用(null)
      public static String[] getXYNCdata() {
    	  synchronized(DsnProxyGrab.class) {
	    	  if(isXYNCdataOk) {
	    		  return dataXYNC;
	    	  }
	    	  return null;
    	  }
      }
      
      public static String [] getXYNCshowData() {
    	  String [] data = {dataXYNC[1], dataXYNC[2], dataXYNC[3], dataXYNC[4], dataXYNC[5], dataXYNC[6], dataXYNC[7], dataXYNC[8], dataXYNC[9]};
    	  return data;
      }
      
      public static void disableCQSSCData() {
    	  synchronized(DsnProxyGrab.class) {
    		  isCQSSCdataOk = false;
    	  }
      }
      
      public static void disableBJSCData() {
    	  synchronized(DsnProxyGrab.class) {
    		  isBJSCdataOk = false;
    	  }
      }
      
      public static void disableXYNCData() {
    	  synchronized(DsnProxyGrab.class) {
    		  isXYNCdataOk = false;
    	  }
      }
      
      public static String [] getCQSSCTime(){
          //get period
    	  String [] time = {"0", "0", "0"};
    	  String response = "";
    	  String host = ADDRESS;
    	  
    	  String getTimeUrl = host + "/time?&_=";
    	  getTimeUrl += Long.toString(System.currentTimeMillis());
				          
    	  response = doGet(getTimeUrl, "");				        
    	  
//    	  if(response == "") {	
//    		  System.out.println("get period failed");
//    		  time[0] = Long.toString(System.currentTimeMillis());
//    		  return time;
//	      }
//    	  
    	  if(response == "timeout") {
        	  response = doGet(getTimeUrl, "");
          }
//    	  
//    	  if(response == "" || response == "timeout") {
//          	  System.out.println("get period failed");
//          	  time[0] = Long.toString(System.currentTimeMillis());
//    		  	return time;
//          }
				          
    	  if(Common.isNum(response)) {
    		  timeDValue = Long.parseLong(response) - System.currentTimeMillis();
    	  }
    	  
    	  String getPeriodUrl = host + "/agent/period?lottery=CQSSC&_=";
    	  getPeriodUrl += Long.toString(System.currentTimeMillis());

          
    	  response = doGet(getPeriodUrl, "");
		          
    	  if(response == "") {	
    		  System.out.println("get period failed");
    		  time[0] = Long.toString(System.currentTimeMillis());
    		  return time;
	      }
    	  
    	  if(response == "timeout") {
        	  response = doGet(getPeriodUrl, "");
          }
          
          if(response == "" || response == "timeout") {
            	System.out.println("get period failed");
            	time[0] = Long.toString(System.currentTimeMillis());
      		  	return time;
           }
	          
    	  System.out.println("preiod:");
    	  System.out.println(response);
				          
    	  long closeTime = 0;
    	  long drawTime = 0;
    	  try{
              JSONObject periodJson = new JSONObject(response);
              closeTime = periodJson.getLong("closeTime");
              time[1] = periodJson.getString("drawNumber");
              drawTime = periodJson.getLong("drawTime");
          }
          catch(Exception e){
        	  System.out.println("获取时间异常");
        	  time[0] = Long.toString(System.currentTimeMillis());
    		  return time;
          }
				          
    	  time[0] = Long.toString(closeTime - (timeDValue + System.currentTimeMillis()));
    	  time[2] = Long.toString(drawTime - (timeDValue + System.currentTimeMillis()));
    	  return time;
      }
      
      public static String [] getBJSCTime(){
          //get period
    	  String [] time = {"0", "0", "0"};
    	  String response = "";
    	  String host = ADDRESS;
    	  
    	  String getTimeUrl = host + "/time?&_=";
          getTimeUrl += Long.toString(System.currentTimeMillis());
          
          response = doGet(getTimeUrl, "");
          
//          if(response == "") {
//        	  System.out.println("get period failed");
//        	  time[0] = Long.toString(System.currentTimeMillis());
//        	  return time;
//          }
//            
          if(response == "timeout") {
        	  response = doGet(getTimeUrl, "");
          }
//            
//          if(response == "" || response == "timeout") {
//          	  System.out.println("get period failed");
//          	  time[0] = Long.toString(System.currentTimeMillis());
//          	  return time;
//          }
          
          if(Common.isNum(response)) {
        	  timeDValue = Long.parseLong(response) - System.currentTimeMillis();
          }

          String getPeriodUrl = host + "/agent/period?lottery=BJPK10&_=";
          getPeriodUrl += Long.toString(System.currentTimeMillis());

          
          response = doGet(getPeriodUrl, "");
          
          if(response == "") {
          	System.out.println("get period failed");
          	time[0] = Long.toString(System.currentTimeMillis());
          	return time;
          }
          
          if(response == "timeout") {
        	  response = doGet(getPeriodUrl, "");
          }
          
          if(response == "" || response == "timeout") {
        	  System.out.println("get period failed");
        	  time[0] = Long.toString(System.currentTimeMillis());
      		  return time;
           }
          
          System.out.println("preiod:");
          System.out.println(response);
          
          long closeTime = 0;
    	  long drawTime = 0;
          try{
              JSONObject periodJson = new JSONObject(response);
              closeTime = periodJson.getLong("closeTime");
              time[1] = periodJson.getString("drawNumber");
              drawTime = periodJson.getLong("drawTime");
          }
          catch(Exception e){
        	  System.out.println("获取时间异常");
        	  time[0] = Long.toString(System.currentTimeMillis());
      		  return time;
          }
          
          time[0] = Long.toString(closeTime - (System.currentTimeMillis() + timeDValue));
    	  time[2] = Long.toString(drawTime - (System.currentTimeMillis() + timeDValue));
    	  return time;
      }    
      
      public static String [] getXYNCTime(){  
    	  String [] time = {"0", "0", "0"};
    	  String response = "";
    	  String host = ADDRESS;
    	  
    	  String getTimeUrl = host + "/time?&_=";
    	  getTimeUrl += Long.toString(System.currentTimeMillis());
				          
    	  response = doGet(getTimeUrl, "");				        
    	   	  
    	  if(response == "timeout") {
        	  response = doGet(getTimeUrl, "");
          }
				          
    	  if(Common.isNum(response)) {
    		  timeDValue = Long.parseLong(response) - System.currentTimeMillis();
    	  }
    	  
    	  String getPeriodUrl = host + "/agent/period?lottery=XYNC&_=";
    	  getPeriodUrl += Long.toString(System.currentTimeMillis());

          
    	  response = doGet(getPeriodUrl, "");
		          
    	  if(response == "") {	
    		  System.out.println("get period failed");
    		  time[0] = Long.toString(System.currentTimeMillis());
    		  return time;
	      }
    	  
    	  if(response == "timeout") {
        	  response = doGet(getPeriodUrl, "");
          }
          
          if(response == "" || response == "timeout") {
            	System.out.println("get period failed");
            	time[0] = Long.toString(System.currentTimeMillis());
      		  	return time;
           }
	          
    	  System.out.println("preiod:");
    	  System.out.println(response);
				          
    	  long closeTime = 0;
    	  long drawTime = 0;
    	  try{
              JSONObject periodJson = new JSONObject(response);
              closeTime = periodJson.getLong("closeTime");
              time[1] = periodJson.getString("drawNumber");
              drawTime = periodJson.getLong("drawTime");
          }
          catch(Exception e){
        	  System.out.println("获取时间异常");
        	  time[0] = Long.toString(System.currentTimeMillis());
    		  return time;
          }
				          
    	  time[0] = Long.toString(closeTime - (timeDValue + System.currentTimeMillis()));
    	  time[2] = Long.toString(drawTime - (timeDValue + System.currentTimeMillis()));
    	  return time;
      }
      
      public static String grabCQSSCdataByCookie(String game, String all, String range, String uid, String dae){
      	if((game == "LM" || game == "DH" || game == "QZHS") && (range == "" || range == "A" ||
      			range == "B" || range == "C" || range == "D") && (all == "XZ" || all == "SZ" || all == "BH")) {
      		switch (game) {
      		    case "LM":
      		      game = "DX1%2CDS1%2CDX2%2CDS2%2CDX3%2CDS3%2CDX4%2CDS4%2CDX5%2CDS5%2CZDX%2CZDS%2CLH";
      		      break;
      		    case "DH":
        		      game = "B1%2CB2%2CB3%2CB4%2CB5";
        		      break;
      		    case "QZHS":
          		  game = "TS1%2CTS2%2CTS3";
          		  break;
      		   default :
      		}
      		long time =  System.currentTimeMillis();
      		String strTime = Long.toString(time);
      		String data = doGet("http://3f071b45.dsn.ww311.com/agent/control/risk?lottery=CQSSC&games=" + game +"&all=" 
      								+ all + "&range=" + range + "&multiple=false&_=" + strTime, uid + dae);
      		if(data != "") {
      			return data;
      		}
      	}
      	return null;
      }
      
      public static boolean isInCQSSCgrabTime() {
    	  long time = System.currentTimeMillis() + timeDValue;
    	  Date date = new Date(time);
          int currentHour = date.getHours();
          int currentMinutes = date.getMinutes();
          int currentSeconds = date.getSeconds();
          
          if((currentHour*60 + currentMinutes < 10*60 +1) && (currentHour * 60 + currentMinutes > 1 * 60 + 55))
              return false;
           
           return true;
      }
      
      public static boolean isInBJSCgrabTime() {
    	  long time = System.currentTimeMillis() + timeDValue;
    	  Date date = new Date(time);
          int currentHour = date.getHours();
          int currentMinutes = date.getMinutes();
          int currentSeconds = date.getSeconds();
          
          if((currentHour *60 + currentMinutes > 9*60 + 1) && (currentHour * 60 + currentMinutes <= 23 * 60 + 57)){
          		return true;
          }
           
           return false;
      }
    
}
