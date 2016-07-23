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
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.protocol.HttpClientContext;

import java.io.*; 

import org.apache.http.Header;
import org.json.JSONObject;

public class DsnProxyGrab {
	static CloseableHttpClient httpclient = null;
    static RequestConfig requestConfig = null;
    static HttpClientContext clientContext = null;
    
    //static Map<String,String> cookieMap = new HashMap<String, String>(64);
    static String strCookies = "";
    static String cookieuid = "";
    static String cookiedae = "";
    static String [] dataCQSSC = {"", ""};
    static String [] dataBJSC = {"", "", "", ""};
    static boolean isCQSSCdataOk = false;
    static boolean isBJSCdataOk = false;
    
    private static String ADDRESS = "";
    private static String ACCOUNT = "";
    private static String PASSWORD = "";
    
    static {
         requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
         requestConfig = RequestConfig.copy(requestConfig).setRedirectsEnabled(false).build();//��ֹ�ض��� �� �Ա��ȡcookiedae
         requestConfig = RequestConfig.copy(requestConfig).setConnectTimeout(15*1000).setConnectionRequestTimeout(15*1000).setSocketTimeout(15*1000).build();//���ó�ʱ
         httpclient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
    }

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
        String loginPage = doGet(ADDRESS + "/login", ""); //get ��¼ҳ��
        
        if(loginPage != "" && loginPage != "timeout") {
        	cookieuid = strCookies;
        	int posStart = loginPage.indexOf("img src=") + 9;
        	if(posStart >= 0) {
        		int posEnd = loginPage.indexOf('"', posStart);
        		String rmNum = getPicNum(ADDRESS + "/" + loginPage.substring(posStart, posEnd));//get ��֤��
        		if(!Common.isNum(rmNum)) {
        			return false;
        		}
        	
        		//����post
        		List<NameValuePair> params = new ArrayList<NameValuePair>();
        		params.add(new BasicNameValuePair("type", "2"));
        		params.add(new BasicNameValuePair("account", ACCOUNT));
        		params.add(new BasicNameValuePair("password", PASSWORD));
        		params.add(new BasicNameValuePair("code", rmNum));
        		String location = doPost(ADDRESS + "/login", params, strCookies, "");
        		
        		System.out.println("location: " + location); 

            
        		if(location.indexOf("index?") > 0) {
        			strCookies = "";
        			location = doGet(location, cookieuid);//get cookiedae���ض���url
        			cookiedae = strCookies;
        			strCookies = "";
        			location = doGet(location, cookieuid + cookiedae);
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
    
    public static boolean login() {    	
    	boolean res = false;
    	for(int i = 0; i < 15; i++) {
    		if(doLogin()) {
    			res = true;
    			break;
    		}
    	}
    	
    	return res;
    }


    /**��utf-8��ʽ��ȡ*/
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
        if(cookies != ""){
        	httppost.addHeader("Cookie", cookies);
        }
        
        System.out.println("executing request " + httppost.getURI()); 

       
        UrlEncodedFormEntity uefEntity;
        try {
            uefEntity = new UrlEncodedFormEntity(formparams, charset);
            httppost.setEntity(uefEntity);
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                // ��ӡ��Ӧ״̬    
            	setCookie(response);
            	if(response.getStatusLine().toString().indexOf("302 Found") > 0) {
            		String location = response.getFirstHeader("Location").getValue();
            		System.out.println(response.getStatusLine());
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
        return "";
    }
    
    public static String doGet(String url, String cookies) { 
        try {  
           // ����httpget.    
        	System.out.println(cookies); 
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
          
           // ִ��get����.    
           CloseableHttpResponse response = httpclient.execute(httpget, clientContext); 
           //CloseableHttpResponse response = httpclient.execute(httpget); 
           
           try {          	           	   
               // ��ȡ��Ӧʵ��    
        	   setCookie(response);
               HttpEntity entity = response.getEntity(); 
               System.out.println("--------------------------------------"); 
               
               String statusLine = response.getStatusLine().toString();
               System.out.println(statusLine); 
               if(statusLine.indexOf("302 Found") > 0) {
            	   return response.getFirstHeader("Location").getValue();
               }
               System.out.println("------------------------------------"); 
               if (entity != null) {
	            	String entityStr = EntityUtils.toString(entity);
	            	System.out.println("entityStr: " + entityStr);
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
        return "";
    }
    
    public static String getPicNum(String picUri) {
    	 HttpGet httpget = new HttpGet(picUri);
         httpget.addHeader("Connection","keep-alive");
         if(strCookies != ""){
        	 httpget.addHeader("Cookie", strCookies);
         }
         httpget.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 "
         					+ "(KHTML, like Gecko) Chrome/51.0.2704.79 Safari/537.36");           
         System.out.println("executing request " + httpget.getURI()); 
        
         // ִ��get����.    
         try {
        	 CloseableHttpResponse response = httpclient.execute(httpget, clientContext); 
        	 try {
        		 setCookie(response);
                 // ��ӡ��Ӧ״̬    
                 System.out.println(response.getStatusLine()); 
                 System.out.println("------------------------------------");
                 if(response.getStatusLine().toString().indexOf("200 OK") < 0) {
                	 return "";
                 }
                 File storeFile = new File("yzm.png");   //ͼƬ���浽��ǰλ��
                 FileOutputStream output = new FileOutputStream(storeFile);  
                 //�õ�������Դ���ֽ�����,��д���ļ�  
                 byte [] a = EntityUtils.toByteArray(response.getEntity());
                 output.write(a);  
                 output.close();  
                 
          
                 InputStream ins = null;
         		 String[] cmd = new String[]{ConfigReader.getTessPath() + "\\tesseract", "yzm.png", "result", "-l", "eng"};

         		 Process process = Runtime.getRuntime().exec(cmd);
         		 // cmd ����Ϣ
         		 ins = process.getInputStream();
         		 BufferedReader reader = new BufferedReader(new InputStreamReader(ins));

         		 String line = null;
         	  	 while ((line = reader.readLine()) != null) {
         	  		 System.out.println(line);
         		 }
         			
         		 int exitValue = process.waitFor();
         		 System.out.println("����ֵ��" + exitValue);
         		 process.getOutputStream().close();
         		 File file = new File("result.txt");
         		 reader.close();
                 reader = new BufferedReader(new FileReader(file));
                  // һ�ζ���һ�У�ֱ������nullΪ�ļ�����
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
    
  //! @brief ץȡcqssc�µ�����
  //! @param game       ����:"LM", ����:"DH", ǰ�к���:"QZHS"
  //! @param all        ��ע:"XZ" ʵռ:"SZ" ����:"BH"
  //! @param range      ȫ��:"", A��:"A", B��:"B", C��:"C", D��:"D",
  //! @return           �ɹ�:String ����ʧ��:null ��ʱ:"timeout" 
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
    
    
    //! @brief ץȡcqssc�µ�����
    //! @param game       ����:"GY", ��������:"SSWL", �߰˾�ʮ:"QBJS"
    //! @param all        ��ע:"XZ" ʵռ:"SZ" ����:"BH"
    //! @param range      ȫ��:"", A��:"A", B��:"B", C��:"C", D��:"D",
    //! @return           �ɹ�:String ����ʧ��:null ��ʱ:"timeout" 
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
      
      public static void setCQSSCdata(String drawNumber, String data) {
    	  synchronized(DsnProxyGrab.class) {
	    	  dataCQSSC[0] = drawNumber;
	    	  dataCQSSC[1] = data;
	    	  isCQSSCdataOk = true;
	    	  System.out.println("set  CQSSCdata   ok");
    	  }
      }
      
      public static void setBJSCdata(String drawNumber, String [] data) {
    	  synchronized(DsnProxyGrab.class) {
	    	  dataBJSC[0] = drawNumber;
	    	  dataBJSC[1] = data[0];
	    	  dataBJSC[2] = data[1];
	    	  dataBJSC[3] = data[2];
	    	  isBJSCdataOk = true;
	    	  System.out.println("set  BJSCdata   ok");
    	  }
      }
      
      //! @brief    ��ȡcqssc�µ�����
      //! @return   ���ݿ���(String[0]:������ String[1]:data);  ���ݲ�����(null)
      public static String[] getCQSSCdata() {
    	  synchronized(DsnProxyGrab.class) {
	    	  if(isCQSSCdataOk) {
	    		  return dataCQSSC;
	    	  }
	    	  return null;
    	  }
      }
      
      //! @brief     ��ȡȡBJSC�µ�����
      //! @return    ���ݿ���(String[0]:����, String[1]:����, String[2]:��������, String[3]:�߰˾�ʮ); ���ݲ�����(null)
      public static String[] getBJSCdata() {
    	  synchronized(DsnProxyGrab.class) {
	    	  if(isBJSCdataOk) {
	    		  return dataBJSC;
	    	  }
	    	  return null;
    	  }
      }
      
      public static void disableCQSSCData() {
    	  synchronized(DsnProxyGrab.class) {
    		  isCQSSCdataOk = false;
    		  System.out.println("disable  CQSSCdata   ok");
    	  }
      }
      
      public static void disableBJSCData() {
    	  synchronized(DsnProxyGrab.class) {
    		  isBJSCdataOk = false;
    		  System.out.println("disable  BJSCdata   ok");
    	  }
      }
      
      public static String [] getCQSSCTime(){
          //get period
    	  String [] time = {"", "", ""};
    	  String response = "";
    	  String host = ADDRESS;
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
        	  System.out.println("��ȡʱ���쳣");
        	  time[0] = Long.toString(System.currentTimeMillis());
    		  return time;
          }
				          
    	  String getTimeUrl = host + "/time?&_=";
    	  getTimeUrl += Long.toString(System.currentTimeMillis());
				          
    	  response = doGet(getTimeUrl, "");
				        
    	  long time1 = 0;
				          
    	  if(response != null && Common.isNum(response)) {
    		  time1 = Long.parseLong(response);
    	  }
    	  else{
    		  time1 = System.currentTimeMillis();
    	  }
    	  time[0] = Long.toString(closeTime - time1);
    	  time[2] = Long.toString(drawTime - time1);
    	  return time;
      }
      
      public static String [] getBJSCTime(){
          //get period
    	  String [] time = {"", "", ""};
    	  String response = "";
    	  String host = ADDRESS;
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
          
          //JSONObject periodJson = new JSONObject(response);
          long closeTime = 0;
    	  long drawTime = 0;
          try{
              JSONObject periodJson = new JSONObject(response);
              closeTime = periodJson.getLong("closeTime");
              time[1] = periodJson.getString("drawNumber");
              drawTime = periodJson.getLong("drawTime");
          }
          catch(Exception e){
        	  System.out.println("��ȡʱ���쳣");
        	  time[0] = Long.toString(System.currentTimeMillis());
      		  return time;
          }
          
      	
          String getTimeUrl = host + "/time?&_=";
          getTimeUrl += Long.toString(System.currentTimeMillis());
          
          response = doGet(getTimeUrl, "");
          long time1 = 0;
          
          if(response != null && Common.isNum(response))
          {
          	time1 = Long.parseLong(response);
          }
          else{
          	time1 = System.currentTimeMillis();
          }
          
          time[0] = Long.toString(closeTime - time1);
    	  time[2] = Long.toString(drawTime - time1);
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
    	  long time = System.currentTimeMillis();
    	  Date date = new Date(time);
          int currentHour = date.getHours();
          int currentMinutes = date.getMinutes();
          int currentSeconds = date.getSeconds();
          
          if(currentHour <10 && (currentHour * 60 + currentMinutes > 1 * 60 + 55))
              return false;
           
           return true;
      }
      
      public static boolean isInBJSCgrabTime() {
    	  long time = System.currentTimeMillis();
    	  Date date = new Date(time);
          int currentHour = date.getHours();
          int currentMinutes = date.getMinutes();
          int currentSeconds = date.getSeconds();
          
          if(currentHour >=9 && currentHour <= 24){
          		return true;
          }
           
           return false;
      }
    
}
