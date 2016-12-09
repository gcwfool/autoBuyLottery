package lanyang;



import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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

public class LanyangHttp {
	
	
	
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
	
	
	
	static String ADDRESS = "";
	static int defaultTimeout = 5*1000;
	
	static String line = "mm.";
	static String host = "ok1688.info/";
	
	public static String lineuri = "http://" + line + host;
	
	
    public static boolean loginToLanyang(){
      	
/*    	String hostURI = "http://www.ok1688.info/";

    	String linePage = doGet(hostURI, "", ADDRESS+"/login");

    	
    	
    	System.out.print(linePage);
    	
    	

    	String[] lines = {"cd.", "cf.",  "as.", "ct.", "hh.", "kk.", "mm.", "oo.", "pp.", "qq."};
    	
    	
    	for(int i = 0; i < lines.length; i++){
    		String line = "http://" + lines[i]  + "ok1688.info/" + "_index/speed.jpg?" + System.currentTimeMillis();
    		doGet(line, "", hostURI);
    	}
    	
    	String lineuri = "http://mm.ok1688.info/";*/
    	
    	
    	
    	
        if(true) {
        	
        	
        	
	        List<NameValuePair> params = new ArrayList<NameValuePair>();
	        params.add(new BasicNameValuePair("fline", "mm"));

        	
        	//��¼����
        	String uri = lineuri + "index2.php";
        	String res = doGet(uri, "", "");
        	
        	System.out.print(res);
        	
        	//��¼���淵�ص�token
        	int posStart = res.indexOf("__RequestVerificationToken");
        	String token = "";
        	if(posStart != -1){
            	posStart = res.indexOf("value=\"", posStart);
            	posStart = posStart + 7;
            	
            	int posEnd = res.indexOf("\"", posStart);
            	
            	
            	token = res.substring(posStart, posEnd);
            	
            	System.out.println(token);
        	}

        	
        	
        	//��ȡcookies
        	String captchaUri = lineuri + "_index/captcha.php";
        	doGet(captchaUri, "", uri);
        	
        	
        	String loginUri = lineuri + "_index/c.php";
        	
	        List<NameValuePair> loginParams = new ArrayList<NameValuePair>();
	        loginParams.add(new BasicNameValuePair("user", "ly8899"));
	        loginParams.add(new BasicNameValuePair("pwd", "aaa111"));
	        loginParams.add(new BasicNameValuePair("captcha", ""));
	        loginParams.add(new BasicNameValuePair("style", "2"));
	        loginParams.add(new BasicNameValuePair("__RequestVerificationToken", token));
        	
	        
	        res = doPost(loginUri, loginParams, "");
	        
	        System.out.println(res);
	        
	        
	        String agreeUri = lineuri + "_index/agree.php?s=2";
	        
	        res = doGet(agreeUri, "", uri);
	        
	        String agreeConfirmUri = lineuri + "z/";
	        
	        
	        res = doGet(agreeConfirmUri, strCookies, agreeUri);
	        
	        
	        
	        System.out.println(res);
        
        }

    	return false;
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
    		//calcRequestAveTime(time2 - time1);
    		
    	}catch(Exception e){
    		
    		time2 = System.currentTimeMillis();
    		//calcRequestAveTime(time2 - time1);
    		
    		throw e;
    	}
    	

    	
    	return response;
    	
    }
    
    
    /**��utf-8��ʽ��ȡ*/
    public static String doPost(String url,List<NameValuePair> formparams, String cookies) {
        return doPost(url, formparams,"UTF-8", cookies);
    }

    public static String doPost(String url,List<NameValuePair> formparams,String charset, String cookies) {


     // ����httppost   
    	
    	try {
    	
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
            	
            	System.out.println("cookies");
            	System.out.println(strCookies);
            	
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
    
    
    
}
