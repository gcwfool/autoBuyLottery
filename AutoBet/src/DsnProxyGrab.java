import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
         requestConfig = RequestConfig.copy(requestConfig).setRedirectsEnabled(false).build();//��ֹ�ض��� �� �Ա��ȡcookiedae
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
        
        String loginPage = doGet(configReader.getAddress(), null); //get ��¼ҳ��
        
        if(loginPage != null) {
        	cookieuid = strCookies;
        	int posStart = loginPage.indexOf("img src=") + 9;
        	if(posStart >= 0) {
        		int posEnd = loginPage.indexOf('"', posStart);
        		String rmNum = getPicNum("http://3f071b45.dsn.ww311.com/" + loginPage.substring(posStart, posEnd));//get ��֤��
        	
        		//����post
        		List<NameValuePair> params = new ArrayList<NameValuePair>();
        		params.add(new BasicNameValuePair("type", "2"));
        		params.add(new BasicNameValuePair("account", configReader.getAccount()));
        		params.add(new BasicNameValuePair("password", configReader.getPassword()));
        		params.add(new BasicNameValuePair("code", rmNum));
        		String location = doPost(configReader.getAddress(), params, strCookies, "");
        		
        		System.out.println("location: " + location); 

            
        		if(location.indexOf("index?") > 0) {
        			strCookies = "";
        			location = doGet(location, cookieuid);//get cookiedae���ض���url
        			cookiedae = strCookies;
        			strCookies = "";
        			return doGet(location, cookieuid + cookiedae);//get ��ҳ
        		}
            
        	}
        }
   //     int loginPage.
         
        return null;
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
           // ����httpget.    
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
          
           // ִ��get����.    
           CloseableHttpResponse response = httpclient.execute(httpget, clientContext); 
           //CloseableHttpResponse response = httpclient.execute(httpget); 
           
           try {          	           	   
               // ��ȡ��Ӧʵ��    
        	   setCookie(response);
               HttpEntity entity = response.getEntity(); 
               System.out.println("--------------------------------------"); 
               // ��ӡ��Ӧ״̬    
               
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
                 File storeFile = new File("yzm.png");   //ͼƬ���浽��ǰλ��
                 FileOutputStream output = new FileOutputStream(storeFile);  
                 //�õ�������Դ���ֽ�����,��д���ļ�  
                 byte [] a = EntityUtils.toByteArray(response.getEntity());
                 output.write(a);  
                 output.close();  
                 
                 
                
                 
                 InputStream ins = null;
         		 String[] cmd = new String[]{configReader.getTessPath() + "\\tesseract", "yzm.png", "result", "-l", "eng"};

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
    
  //! @brief ץȡcqssc�µ�����
  //! @param game       ����:"LM", ����:"DH", ǰ�к���:"QZHS"
  //! @param all        ��ע:"XZ" ʵռ:"SZ" ����:"BH"
  //! @param range      ȫ��:"", A��:"A", B��:"B", C��:"C", D��:"D",
  //! @return           success:String fail:null
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
    		String data = doGet("http://3f071b45.dsn.ww311.com/agent/control/risk?lottery=CQSSC&games=" + game +"&all=" 
    								+ all + "&range=" + range + "&multiple=false&_=" + strTime, cookieuid + cookiedae);
    		if(data != null) {
    			return data;
    		}
    	}
    	return null;
    }
    
}
