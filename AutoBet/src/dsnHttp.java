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
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.*;



public class dsnHttp {

    static CloseableHttpClient httpclient = null;
    static RequestConfig requestConfig = null;
    static HttpClientContext clientContext = null;
    
    
    //������������洢
    static String cookieCfduid = "";
    static String cookie2ae = "";
    static String cookiedefaultLT = "";
    static String cookiesb18 = "";
    static String location = "";
    static long   queryParam = 0;
    
    static long time = 0;
    
    static String drawNumber = "";
    static String previousBetNumber = "";
    
    
    public static boolean loginToDsn(){
  	
    	String loginURI = "";
    	loginURI = "/login";
    	
    	loginURI = ConfigReader.getBetAddress() + loginURI;
    	
    	String code = doGetLoginPage(loginURI);
    	

        String type = "1";  //remove hardcode later
        String account = ConfigReader.getBetAccount();
        String password = ConfigReader.getBetPassword();
        

        
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("type", type));
        params.add(new BasicNameValuePair("account", account));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("code", code));
    	
        String location = doPost(loginURI, params, "");
        
        System.out.println("location: " + location); 
        
		if(location.indexOf("agreement?_") > 0) {

			if(doGet(location, cookieCfduid, "") != null){
				return true;
			}
		}
        

    	return false;
    }
    public static long timeToBet(){
        //get period
    	String response = "";
    	String host = ConfigReader.getBetAddress();
        String getPeriodUrl = host + "/member/period?lottery=CQSSC&_=";
        getPeriodUrl += Long.toString(System.currentTimeMillis());

        
        response = doGet(getPeriodUrl, "", ConfigReader.getBetAddress() + "/member/load?lottery=CQSSC&page=lm");
        
        if(response == null)
        {
        	
        	System.out.println("get period failed");
        	return System.currentTimeMillis();
        }
        
        System.out.println("preiod:");
        System.out.println(response);
        
        JSONObject periodJson = new JSONObject(response);
        long closeTime = periodJson.getLong("closeTime");
        drawNumber = periodJson.getString("drawNumber");
        
        time = System.currentTimeMillis();
        
        long remainTime = closeTime - time;
        
    	return remainTime;
    }
    public static boolean doBetCQSSC(String betData)
    {

    	String host = ConfigReader.getBetAddress();
       	
        String jsonParam = "";
        
        if(previousBetNumber.equals(drawNumber)) //֮ǰ����Ѿ��¹�����ֱ�ӷ���
        	return false;
        
        
        //���δ������ʱ��
        if( drawNumber != null){
        	jsonParam = constructBetsData(betData);
        	
        	System.out.println(jsonParam);
        	
        	String response = "";
        	
        	previousBetNumber = drawNumber;
        	
        	response = betCQSSC(host + "/member/bet", jsonParam, "UTF-8", "");
        	
        	System.out.println("bet result:");
        	System.out.println(response);
        	
        	return true;
        
        }
        
        return false;
    }
    
    public static String constructBetsData(String data)
    {
    	
    	//data = "[[{\"k\":\"DX2\",\"i\":\"X\",\"c\":2,\"a\":55,\"r\":109.989,\"cm\":0},{\"k\":\"DX3\",\"i\":\"D\",\"c\":3,\"a\":130,\"r\":258.294,\"cm\":0},{\"k\":\"DX4\",\"i\":\"D\",\"c\":4,\"a\":660,\"r\":1319.868,\"cm\":0},{\"k\":\"DS1\",\"i\":\"D\",\"c\":1,\"a\":10,\"r\":19.998,\"cm\":0},{\"k\":\"DX2\",\"i\":\"D\",\"c\":3,\"a\":20,\"r\":39.996,\"cm\":0},{\"k\":\"DS4\",\"i\":\"D\",\"c\":3,\"a\":20,\"r\":39.996,\"cm\":0},{\"k\":\"DX3\",\"i\":\"X\",\"c\":1,\"a\":5,\"r\":9.999,\"cm\":0},{\"k\":\"DX5\",\"i\":\"D\",\"c\":2,\"a\":40,\"r\":79.992,\"cm\":0},{\"k\":\"DX1\",\"i\":\"X\",\"c\":2,\"a\":55,\"r\":109.989,\"cm\":0},{\"k\":\"DX4\",\"i\":\"X\",\"c\":1,\"a\":40,\"r\":79.992,\"cm\":0},{\"k\":\"ZDX\",\"i\":\"D\",\"c\":2,\"a\":15,\"r\":29.997,\"cm\":0},{\"k\":\"DS1\",\"i\":\"S\",\"c\":2,\"a\":15,\"r\":29.997,\"cm\":0},{\"k\":\"DS2\",\"i\":\"D\",\"c\":3,\"a\":100,\"r\":199.98,\"cm\":0},{\"k\":\"DS3\",\"i\":\"D\",\"c\":2,\"a\":55,\"r\":109.989,\"cm\":0},{\"k\":\"DS3\",\"i\":\"S\",\"c\":3,\"a\":20,\"r\":39.996,\"cm\":0},{\"k\":\"DS4\",\"i\":\"S\",\"c\":2,\"a\":45,\"r\":89.991,\"cm\":0},{\"k\":\"DS5\",\"i\":\"D\",\"c\":3,\"a\":50,\"r\":99.99,\"cm\":0},{\"k\":\"DX1\",\"i\":\"D\",\"c\":3,\"a\":50,\"r\":99.99,\"cm\":0},{\"k\":\"DX5\",\"i\":\"X\",\"c\":3,\"a\":40,\"r\":79.992,\"cm\":0},{\"k\":\"ZDS\",\"i\":\"S\",\"c\":2,\"a\":15,\"r\":29.997,\"cm\":0},{\"k\":\"DS2\",\"i\":\"S\",\"c\":2,\"a\":40,\"r\":79.992,\"cm\":0},{\"k\":\"DS5\",\"i\":\"S\",\"c\":2,\"a\":55,\"r\":109.989,\"cm\":0}],{\"DS1_S\":1.983,\"DS1_D\":1.983,\"DS2_S\":1.983,\"DS2_D\":1.983,\"DS3_S\":1.983,\"DS3_D\":1.983,\"DS4_S\":1.983,\"DS4_D\":1.983,\"DS5_S\":1.983,\"DS5_D\":1.983,\"DX1_X\":1.983,\"DX1_D\":1.983,\"DX2_X\":1.983,\"DX2_D\":1.983,\"DX3_X\":1.983,\"DX3_D\":1.983,\"DX4_X\":1.983,\"DX4_D\":1.983,\"DX5_X\":1.983,\"DX5_D\":1.983,\"LH_T\":9.28,\"LH_H\":1.983,\"LH_L\":1.983,\"ZDS_S\":1.983,\"ZDS_D\":1.983,\"ZDX_X\":1.983,\"ZDX_D\":1.983},{\"B1\":64,\"B4\":142,\"LM\":1535,\"B3\":64,\"B5\":334,\"B2\":64}]";

    
    	
    	JSONArray cqsscLMGrabData = new JSONArray(data);
    	
    	JSONArray gamesGrabData = cqsscLMGrabData.getJSONArray(0);
    	
    	JSONObject oddsGrabData = cqsscLMGrabData.getJSONObject(1);
    	
    	JSONArray gamesArray = new JSONArray();
    	
    	for(int i = 0; i < gamesGrabData.length(); i++){
    		JSONObject gameGrabData = gamesGrabData.getJSONObject(i);
    		
    		
    		
			String game = gameGrabData.getString("k");
			String contents = gameGrabData.getString("i");
			String oddsKey = game + "_" + contents;
			double odds = oddsGrabData.getDouble(oddsKey);
			int amount = gameGrabData.getInt("a");
			//ֻ�����ʶ����µ�
    		if(odds < 2 && amount >0){
    			amount = amount/50;  //hard code ��ʱֻ����ʮ��֮һ����
    			if(amount == 0)
    				amount = 1;
    			if(amount > 10)  //�����ʱ����
    				amount = 10;
    			JSONObject gameObj = new JSONObject();
    			gameObj.put("game", game);
    			gameObj.put("contents", contents);
    			gameObj.put("amount", amount);
    			gameObj.put("odds", odds);
    			
    			gamesArray.put(gameObj);
    		}
    		
    	}
    	
    	
    	JSONObject betsObj = new JSONObject();
    	
    	boolean ignore = false;
    	betsObj.put("ignore", ignore);
    	betsObj.put("bets", gamesArray);
    	betsObj.put("drawNumber",drawNumber);
    	
    	betsObj.put("lottery", "CQSSC");
    	
    	String res = betsObj.toString();
    	
    	System.out.println(res);
    	
    	return res;
    	
    	

    	
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
           // ����httpget.    
           HttpGet httpget = new HttpGet(url);
           httpget.addHeader("Accept-Encoding","Accept-Encoding: gzip, deflate, sdch");
           httpget.addHeader("Accept-Language","Accept-Language: zh-CN,zh;q=0.8");
           httpget.addHeader("Connection","keep-alive");
           httpget.addHeader("Upgrade-Insecure-Requests","1");
           httpget.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
           //httpget.addHeader("Referer","http://www.lashou.com/");
           httpget.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.79 Safari/537.36");           
           System.out.println("executing request " + httpget.getURI()); 
          
           // ִ��get����.    
           CloseableHttpResponse response = httpclient.execute(httpget); 
           
           cookieCfduid = setCookie(response);
           
           
           
           try {          	           	   
        	            	            	          	   
               // ��ȡ��Ӧʵ��    
               HttpEntity entity = response.getEntity(); 
               System.out.println("--------------------------------------"); 
               // ��ӡ��Ӧ״̬    
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
            	
            	
            	String code = getPicNum(yzmURL);
            	

                System.out.println(code);

            	
                return code;
               }  
               
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


    /**��utf-8��ʽ��ȡ*/
    public static String doPost(String url,List<NameValuePair> formparams, String cookies) {
        return doPost(url, formparams,"UTF-8", cookies);
    }

    public static String doPost(String url,List<NameValuePair> formparams,String charset, String cookies) {


     // ����httppost    
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
                // ��ӡ��Ӧ״̬    
            	
            	if(response.getStatusLine().toString().indexOf("302 Found") > 0) {
            		String location = response.getFirstHeader("Location").getValue();
            		System.out.println(response.getStatusLine());
            		
            		httppost.releaseConnection();
            		
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
    
    
    public static String doGet(String url, String cookies, String referUrl) {
    	
        try {  
            // ����httpget.    
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
           
            // ִ��get����.    
            CloseableHttpResponse response = httpclient.execute(httpget); 
            System.out.println(response.getStatusLine());
            

            
            HttpEntity entity = response.getEntity(); 
            
            String res = EntityUtils.toString(entity);

            httpget.releaseConnection();
            response.close();
            
            if(res != null){
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
    
    

    
    
    public static String betCQSSC(String url,String jsonData, String charset, String cookies) {


        // ����httppost    
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
                   // ��ӡ��Ӧ״̬    
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
    
    
    public static String getPicNum(String picUri) {
   	 HttpGet httpget = new HttpGet(picUri);
        httpget.addHeader("Connection","keep-alive");

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
                File storeFile = new File("hyyzm.png");   //ͼƬ���浽��ǰλ��
                FileOutputStream output = new FileOutputStream(storeFile);  
                //�õ�������Դ���ֽ�����,��д���ļ�  
                byte [] a = EntityUtils.toByteArray(response.getEntity());
                output.write(a);  
                output.close();  
                
         
                InputStream ins = null;
        		 String[] cmd = new String[]{ConfigReader.getTessPath() + "\\tesseract", "hyyzm.png", "result", "-l", "eng"};

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
    
}


