import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Date;

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


public class WeiCaiHttp {
	
    static CloseableHttpClient httpclient = null;
    static RequestConfig requestConfig = null;
    static HttpClientContext clientContext = null;
    
    
    static {
        requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
        requestConfig = RequestConfig.copy(requestConfig).setRedirectsEnabled(false).build();//禁止重定向 ， 以便获取cookieb18
        requestConfig = RequestConfig.copy(requestConfig).setConnectTimeout(9*1000).setConnectionRequestTimeout(9*1000).setSocketTimeout(9*1000).build();//设置超时
        httpclient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
   }
    
    
    static String strCookies = "";
    
    
    static String serachKey = "55577";
    
    static String ADDRESS = "";    
    
    static String ACCOUNT = "";
    static String PASSWORD = "";
    
    static String memberUrl = "";
    
    static String CQSSCdrawNumber = "";
    static String previousCQSSCBetNumber = "";
    
    static String BJSCdrawNumber = "";
    static String previousBJSCBetNumber = "";
    
    
    static String host = "http://pxiagme1.lot7777.net";
    
    static ArrayList<String[]> CQSSCselectionTypeIdList = new ArrayList<String[]>();
    
    static ArrayList<String[]> CQSSCoddsList = new ArrayList<String[]>();
    
    static ArrayList<String[]> BJSCselectionTypeIdList = new ArrayList<String[]>();
    
    static ArrayList<String[]> BJSCoddsList = new ArrayList<String[]>();
    
    static String CQSSCeventID = "";
    
    static String BJSCeventID = "";
    
    static int totalAmount = 0;
   
    
    static long CQSSCRemainTime = -1;
    static long CQSSCcloseTime = -1;
    static long timeDValue = 0;

    
    static long BJSCRemainTime = -1;
    static long BJSCcloseTime = -1;
    
    static int successTimes = 0;
    static int failTimes = 0;
    
    static boolean previousCQSSCBetResult = false;
    static boolean previousBJSCBetResult = false;
    
    
    public static boolean login() {    	
    	boolean res = false;
    	for(int i = 0; i < 15; i++) {
    		if(loginToWeiCai()) {
    			res = true;
    			break;
    		}
    	}
    	
    	return res;
    }
    
    
    public static void setLoginParams(String address, String account, String password){
    	ADDRESS = address;
    	ACCOUNT = account;
    	PASSWORD = password;
    }
    
    
    public static String getCQSSCdrawNumber(){
    	return CQSSCdrawNumber;
    }
    
    public static String getBJSCdrawNumber(){
    	return BJSCdrawNumber;
    }
    
    public static boolean loginToWeiCai(){
    	
    	String serachPage = doGet(ADDRESS, "", "");
    	
    	if(serachPage == null)
    		serachPage = doGet(ADDRESS, "", "");
    	
    	serachPage = doGet(ADDRESS + "/se/", "", "");
    	
    	if(serachPage == null)
    		serachPage = doGet(ADDRESS + "/se/", "", "");
    	
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("code", serachKey));

    	String linePage = doPost(ADDRESS + "/se/", params, "");
    	//System.out.println(linePage);
    	
    	if(linePage == null)
    		linePage = doPost(ADDRESS + "/se/", params, "");
    	
    	if(linePage == null)
    		return false;
    	
    	int posStart = linePage.indexOf(host);
    	
    	if(posStart >= 0){
    		
    		int posEnd = linePage.indexOf("\"", posStart);
    		
    		String findLoginUrl = linePage.substring(posStart, posEnd);
    		
    		
    		
    		String location = "";
    		
    		for(int i = 0; i < 10; i++){
    			location = doGet(findLoginUrl, "", ADDRESS + "/se/");
    			if(location != null && location.endsWith("Welcome.action")==true)
    				break;
    		}
    		
    		//System.out.println("网址:");
    		//System.out.println(location);
    		
    		if(location != null){
    			String loginPage = doGet(location, "", "");
    			
    			if(loginPage == null)
    				loginPage = doGet(location, "", "");
    			
    			if(loginPage == null)
    				return false;
    			
    			int linePosEnd = location.indexOf("/member");
    			String host = location.substring(0, linePosEnd);
    			
    			int imgPosStart = loginPage.indexOf("img src=\"");
    			
    			if(imgPosStart >= 0){
    				imgPosStart += 9;
    				int imgPosEnd = loginPage.indexOf("\"", imgPosStart);
    				String imgUrl = loginPage.substring(imgPosStart, imgPosEnd);
    				imgUrl = host + imgUrl;
    				
    				String rmNum = getPicNum(imgUrl);//get 验证码

            		System.out.println("验证码");
            		System.out.println(rmNum);
            		if(!Common.isNum(rmNum)) {
            			return false;
            		}
    				
    				int loginPosEnd = location.indexOf("Welcome");
    				
    				String loginUrl = location.substring(0, loginPosEnd);
    				
    				memberUrl = loginUrl;
    				
    				loginUrl += "/Home/ProceedLogin.action";
    				

    		        List<NameValuePair> loginParams = new ArrayList<NameValuePair>();
    		        loginParams.add(new BasicNameValuePair("userCode", ACCOUNT));
    		        loginParams.add(new BasicNameValuePair("password", PASSWORD));
    		        loginParams.add(new BasicNameValuePair("validationCode", rmNum));

    		    	
    		        String res = doPost(loginUrl, loginParams, "");
    				
    		        if(res.contains("Agree.action") == true)
    		        	return true;
    				
    				//System.out.println(res);
    				
    			}
    			
    			
    			    			
    		}
    		
    	}
    	
    	return false;
    	
    }
    
    
    public static String constructBetsData(String[] data, double percent, BetType betType, boolean opposite){
    	
    	totalAmount = 0;
    	
    	String res = "";
    	
    	
    	try{
    		
    		List<String> parsedGames = new ArrayList<String>();;
    		
	    	JSONArray gamesArray = new JSONArray();
	    	//JSONObject oddsGrabData = null;
	    	
	    	if(betType == BetType.BJSC){
	    		

	    	}
	    	else if(betType == BetType.CQSSC){
	    		

	    	}
	    	
	    	//oddsGrabData = new JSONObject(oddsData);
	    	
	    	for(int i = 0; i < data.length; i++){
    		
    		
            	JSONArray cqsscLMGrabData = new JSONArray(data[i]);        	
            	JSONArray gamesGrabData = cqsscLMGrabData.getJSONArray(0);
            	JSONObject oddsGrabData = cqsscLMGrabData.getJSONObject(1);

        	
	        	for(int j = 0; j < gamesGrabData.length(); j++){
	        		JSONObject gameGrabData = gamesGrabData.getJSONObject(j);
	        		
	    			String game = gameGrabData.getString("k");
	    			String contents = gameGrabData.getString("i");
	    			int amount = gameGrabData.getInt("a");
	    			String oddsKey = game + "_" + contents;
	    			
	    			
	    			
	    			double odds = oddsGrabData.getDouble(oddsKey);
	    			
	    			//剔除北京赛车冠亚军 和 两面
	    			if(game.indexOf("GDX") != -1 || game.indexOf("GDS") != -1)
	    				continue;
	    			
	    			if(parsedGames.contains(game) == true)
	    				continue;
	    			
	    			
	    			
	    			
	    			//计算差值
	        		for(int k = j +1 ; k < gamesGrabData.length(); k++){
	        			JSONObject oppositeGameGrabData = gamesGrabData.getJSONObject(k);
	        			String oppositeGame = oppositeGameGrabData.getString("k");
	        			if(oppositeGame.equals(game)){
	        				int oppositeAmount = oppositeGameGrabData.getInt("a");
	        				if(oppositeAmount > amount){
	        					amount = oppositeAmount - amount;
	        					contents = oppositeGameGrabData.getString("i");
	        					oddsKey = oppositeGame + "_" + contents;
	        					odds = oddsGrabData.getDouble(oddsKey);
	        				}
	        				else{
	        					amount = amount - oppositeAmount;
	        				}
	        				break;
	        			}
	        		}
	        		
	        		parsedGames.add(game);

	    			
	    			
	    			
	    			//只下赔率二以下的
	        		if(odds < 2.5 && amount >0){
	        			amount = (int)(amount*percent);  
	        			if(amount <= 1)
	        				amount = 2; //微彩每注最低2元

	        			totalAmount += amount;
	        			
	        			JSONObject gameObj = new JSONObject();
	        			//gameObj.put("game", game);
	        			

	        			
	        			//处理反投: 大变小，小变大，单变双，双变大，龙变虎，虎变隆
	        			if(opposite){
	        				if(game.indexOf("DX") != -1){//反大小
	        					if(contents.indexOf("D") != -1){
	        						contents = "X";        						
	        					}
	        					else{
	        						contents = "D";
	        					}
	        					oddsKey = game + "_" + contents;
	        					odds = oddsGrabData.getDouble(oddsKey);
	        				}
	        				
	        				
	        				if(game.indexOf("DS") != -1){//反单双
	        					if(contents.indexOf("D") != -1){
	        						contents = "S";        						
	        					}
	        					else{
	        						contents = "D";
	        					}
	        					oddsKey = game + "_" + contents;
	        					odds = oddsGrabData.getDouble(oddsKey);
	        				}
	        				
	        				if(game.indexOf("LH") != -1){//反龙虎
	        					if(contents.indexOf("L") != -1){
	        						contents = "H";        						
	        					}
	        					else{
	        						contents = "L";
	        					}
	        					oddsKey = game + "_" + contents;
	        					odds = oddsGrabData.getDouble(oddsKey);
	
	        				}
	
	        				
	        			}
	        			//反投处理结束
	        				        			

	        			
	        			String selectionTypeId = "";
	        			String betOdds = "";
	        			String selectionTypeName = "";
	        			
	        		
	        	    	if(betType == BetType.CQSSC){//重庆时时彩
        	
	        	        	switch(game){
	        	        	case "DX1":
	        	        		selectionTypeName = "第一球";
	        	        		contents = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS1":
	        	        		selectionTypeName = "第一球";
	        	        		contents = contents.equals("D")?"单":"双";
	        	        		break;
	        	        	case "DX2":
	        	        		selectionTypeName = "第二球";
	        	        		contents = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS2":
	        	        		selectionTypeName = "第二球";
	        	        		contents = contents.equals("D")?"单":"双";
	        	        		break;
	        	        	case "DX3":
	        	        		selectionTypeName = "第三球";
	        	        		contents = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS3":
	        	        		selectionTypeName = "第三球";
	        	        		contents = contents.equals("D")?"单":"双";	
	        	        		break;
	        	        	case "DX4":
	        	        		selectionTypeName = "第四球";
	        	        		contents = contents.equals("D")?"大":"小";
	        	        	case "DS4":
	        	        		selectionTypeName = "第四球";
	        	        		contents = contents.equals("D")?"单":"双";	
	        	        		break;
	        	        	case "DX5":
	        	        		selectionTypeName = "第五球";
	        	        		contents = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS5":
	        	        		selectionTypeName = "第五球";
	        	        		contents = contents.equals("D")?"单":"双";	
	        	        		break;
	        	        	case "ZDX":
	        	        		selectionTypeName = "总和";
	        	        		contents = contents.equals("D")?"总大":"总小";
	        	        		break;
	        	        	case "ZDS":
	        	        		selectionTypeName = "总和";
	        	        		contents = contents.equals("D")?"总单":"总双";		
	        	        		break;
	        	        	case "LH":
	        	        		selectionTypeName = "第一球";
	        	        		contents = contents.equals("L")?"龙":"虎";
	        	        		break;
	        	        	}
	        	        	
	        	        	for(int k = 0; k < CQSSCselectionTypeIdList.size(); k ++){
	        	        		String[] idItem = (String[]) CQSSCselectionTypeIdList.get(k);
	        	        		if(contents.equals(idItem[1])&&idItem[2].contains(selectionTypeName)){
	        	        			selectionTypeName = idItem[2];
	        	        			selectionTypeId = idItem[0];
	        	        			
	        	        			for(int h = 0; h < CQSSCoddsList.size(); h++){
	        	        				String[] oddItem = (String[]) CQSSCoddsList.get(h);
	        	        				if(selectionTypeId.equals(oddItem[0])){
	        	        					betOdds = oddItem[1];
	        	        				}
	        	        			}
	        	        			break;
	        	        		}
	        	        	}
	        	        	
	        	        	
	        	    	}//北京赛车
	        	    	else if(betType == BetType.BJSC){
	        	    		
	        	        	switch(game){
	        	        	case "DX1":
	        	        		selectionTypeName = "冠军";
	        	        		contents = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS1":
	        	        		selectionTypeName = "冠军";
	        	        		contents = contents.equals("D")?"单":"双";
	        	        		break;
	        	        	case "LH1":
	        	        		selectionTypeName = "冠军";
	        	        		contents = contents.equals("L")?"龙":"虎";
	        	        		break;	
	        	        		
	        	        	case "DX2":
	        	        		selectionTypeName = "亚军";
	        	        		contents = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS2":
	        	        		selectionTypeName = "亚军";
	        	        		contents = contents.equals("D")?"单":"双";
	        	        		break;
	        	        	case "LH2":
	        	        		selectionTypeName = "亚军";
	        	        		contents = contents.equals("L")?"龙":"虎";
	        	        		break;		
	        	        	case "DX3":
	        	        		selectionTypeName = "第三名";
	        	        		contents = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS3":
	        	        		selectionTypeName = "第三名";
	        	        		contents = contents.equals("D")?"单":"双";	
	        	        		break;
	        	        	case "LH3":
	        	        		selectionTypeName = "第三名";
	        	        		contents = contents.equals("L")?"龙":"虎";
	        	        		break;		
	        	        	case "DX4":
	        	        		selectionTypeName = "第四名";
	        	        		contents = contents.equals("D")?"大":"小";
	        	        	case "DS4":
	        	        		selectionTypeName = "第四名";
	        	        		contents = contents.equals("D")?"单":"双";	
	        	        		break;
	        	        	case "LH4":
	        	        		selectionTypeName = "第四名";
	        	        		contents = contents.equals("L")?"龙":"虎";
	        	        		break;	
	        	        	case "DX5":
	        	        		selectionTypeName = "第五名";
	        	        		contents = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS5":
	        	        		selectionTypeName = "第五名";
	        	        		contents = contents.equals("D")?"单":"双";	
	        	        		break;
	        	        	case "LH5":
	        	        		selectionTypeName = "第五名";
	        	        		contents = contents.equals("L")?"龙":"虎";
	        	        		break;	
	        	        	case "DX6":
	        	        		selectionTypeName = "第六名";
	        	        		contents = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS6":
	        	        		selectionTypeName = "第六名";
	        	        		contents = contents.equals("D")?"单":"双";	
	        	        		break;
	        	        	case "DX7":
	        	        		selectionTypeName = "第七名";
	        	        		contents = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS7":
	        	        		selectionTypeName = "第七名";
	        	        		contents = contents.equals("D")?"单":"双";	
	        	        		break;
	        	        	case "DX8":
	        	        		selectionTypeName = "第八名";
	        	        		contents = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS8":
	        	        		selectionTypeName = "第八名";
	        	        		contents = contents.equals("D")?"单":"双";	
	        	        		break;
	        	        	case "DX9":
	        	        		selectionTypeName = "第九名";
	        	        		contents = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS9":
	        	        		selectionTypeName = "第九名";
	        	        		contents = contents.equals("D")?"单":"双";	
	        	        		break;
	        	        	case "DX10":
	        	        		selectionTypeName = "第十名";
	        	        		contents = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS10":
	        	        		selectionTypeName = "第十名";
	        	        		contents = contents.equals("D")?"单":"双";	
	        	        		break;

	        	        	}
	        	        	
	        	        	for(int k = 0; k < BJSCselectionTypeIdList.size(); k ++){
	        	        		String[] idItem = (String[]) BJSCselectionTypeIdList.get(k);
	        	        		if(contents.equals(idItem[1])&&idItem[2].contains(selectionTypeName)){
	        	        			selectionTypeName = idItem[2];
	        	        			selectionTypeId = idItem[0];
	        	        			
	        	        			for(int h = 0; h < BJSCoddsList.size(); h++){
	        	        				String[] oddItem = (String[]) BJSCoddsList.get(h);
	        	        				if(selectionTypeId.equals(oddItem[0])){
	        	        					betOdds = oddItem[1];
	        	        				}
	        	        			}
	        	        			break;
	        	        		}
	        	        	}
	        	    	}
	        	    	
	        	    	
	        	    	betOdds = betOdds.replace(" ", "");
	        	    	
	        			gameObj.put("selectionTypeId", Integer.parseInt(selectionTypeId));
	        			gameObj.put("selectionTypeName", selectionTypeName);
	        			gameObj.put("betStake", Integer.toString(amount));
	        			gameObj.put("betOdds", betOdds);
	        	    	
	        			
	        			gamesArray.put(gameObj);
	        		}
	        		
	        	}
	    	}
	    	
	
	    	
	    	
	    	
	    	
	    	
	    	JSONObject betsObj = new JSONObject();
	    	
	    	boolean ignore = false;
	    	betsObj.put("totalBetStake", totalAmount);
	    	betsObj.put("bets", gamesArray);
	    	
	    	if(betType == BetType.CQSSC){
	        	betsObj.put("eventId",CQSSCeventID);
	        	
	        	betsObj.put("isAccessNewOdds", 0);
	    	}
	    	else if(betType == BetType.BJSC){
	        	betsObj.put("eventId",BJSCeventID);
	        	
	        	betsObj.put("isAccessNewOdds", 0);
	
	    	}
	
	    	
	    	res = betsObj.toString();
    	
    	}catch(Exception e){
    		autoBet.outputMessage.append("构造下单数据错误！\n");
    		return "";
    	}
   	
    	
    	
    	return res;
    }
    
    public static long getCQSSCRemainTime(){
    	return CQSSCRemainTime;
    }
    
    public static long getBJSCRemainTime(){
    	return BJSCRemainTime;
    }
    
    
    public static boolean isBJSCselectionTypeIdListEmpty(){
    	return BJSCselectionTypeIdList.size() == 0;
    }
    
    public static boolean isCQSSCselectionTypeIdListEmpty(){
    	return CQSSCselectionTypeIdList.size() == 0;
    }
    
    public static boolean getBJSCselectionTypeIdListData(){
    	String market = "";
   	
    	try{
    		
        	String marketUrl = memberUrl + "BJPKS/Market.action?viewName=two_way_market&gameTypeId=201&marketTypeIds=2012%2C2013%2C2022%2C2023%2C2032%2C2033%2C2042%2C2043%2C2052%2C2053%2C2062%2C2063%2C2072%2C2073%2C2082%2C2083%2C2092%2C2093%2C2102%2C2103%2C2014%2C2024%2C2034%2C2044%2C2054%2C2002%2C2003";
        	
        	//拿selectionTypeID
        	market = doGet(marketUrl, "", "");
        	
        	if(market == null)
        		market = doGet(marketUrl, "", "");
        	
        	int posStart = -1;
        	int posEnd = -1;
        	
        	if(market != null){
            	posStart = market.indexOf("[201201");
            	posEnd = market.indexOf("[200201",  posStart);
            	
            	if(posStart >=0 && posEnd >= posStart){
                	String strCQSSCselectionTypeIdList = market.substring(posStart, posEnd);
                	
                	String[] strArrayCQSSCselectionTypeIdList = strCQSSCselectionTypeIdList.split("\r\n");
                	
                	
                	for(int i = 0; i < strArrayCQSSCselectionTypeIdList.length; i++){
                		
                		
                		strArrayCQSSCselectionTypeIdList[i] = strArrayCQSSCselectionTypeIdList[i].replace("'", "");
                		strArrayCQSSCselectionTypeIdList[i]= strArrayCQSSCselectionTypeIdList[i].replace("[", "");
                		strArrayCQSSCselectionTypeIdList[i] = strArrayCQSSCselectionTypeIdList[i].replace("],", "");
                		
                		String[] selectionTypeId = strArrayCQSSCselectionTypeIdList[i].split(",");
                		
                		
                		
                		BJSCselectionTypeIdList.add(selectionTypeId);

                	}
                	
                	//打印
                	/*for(int k = 0; k < CQSSCselectionTypeIdList.size(); k ++){
                		String[] idItem = (String[]) CQSSCselectionTypeIdList.get(k);
                		for(int h = 0; h < idItem.length; h++){
                			System.out.print(idItem[h]);
                		}
                		System.out.println();
                	}*/
            	}
            	else{
            		System.out.println(market);
            		return false;
            	}
            	


        	}else{
        		System.out.println(market);
        		return false;
        	}
    	
    	}catch(Exception e){
        		
    		System.out.println(market);

    		
    		return false;
        }
    	
    	return true;
    }
    
    
    public static boolean getBJSCmarketData(){
    	
    	

    	String marketRefresh = "";
    	
    	try{
    		
    		
        	int posStart = -1;
        	int posEnd = -1;
    		


        	
        	String oddsUrl = memberUrl + "BJPKS/MarketRefresh.action?viewName=two_way_market_refresh&gameTypeId=201&marketTypeIds=2012%2C2013%2C2022%2C2023%2C2032%2C2033%2C2042%2C2043%2C2052%2C2053%2C2062%2C2063%2C2072%2C2073%2C2082%2C2083%2C2092%2C2093%2C2102%2C2103%2C2014%2C2024%2C2034%2C2044%2C2054%2C2002%2C2003";
        	

        	
        	marketRefresh =  doGet(oddsUrl, "", "");
        	
        	if(marketRefresh == null)
        		marketRefresh =  doGet(oddsUrl, "", "");
        	
        	if(marketRefresh != null){
            	posStart = marketRefresh.indexOf("pItm=[]");
            	posEnd = marketRefresh.indexOf("\r\n", posStart);
            	

            	
        		if(posStart != -1){
                	String strOdds = marketRefresh.substring(posStart, posEnd);

                	
            		posStart = strOdds.indexOf("pItm=[");
            		posStart = strOdds.indexOf("pItm=[", posStart+1);

            		while(posStart != -1){
            			posEnd = strOdds.indexOf(";", posStart);
            			
            			String strOddsItem = strOdds.substring(posStart + 5, posEnd);
            			
            			strOddsItem = strOddsItem.replace("'", "");
            			strOddsItem = strOddsItem.replace("[", "");
            			strOddsItem = strOddsItem.replace("]", "");
            			
            			String[] odds = strOddsItem.split(",");
            			
            			BJSCoddsList.add(odds);
            			
            			posStart = strOdds.indexOf("pItm=[", posEnd);
            			posStart = strOdds.indexOf("pItm=[", posStart+1);
            			
            		}
            		
            		//打印
                	/*for(int k = 0; k < CQSSCoddsList.size(); k ++){
                		String[] idItem = (String[]) CQSSCoddsList.get(k);
                		for(int h = 0; h < idItem.length; h++){
                			System.out.print(idItem[h]);
                			System.out.print("   ");
                		}
                		System.out.println();
                	}*/
        		}
        		else{
        			System.out.println("没有赔率数据\n");
        		}
        		

        		
        		//拿eventID
        		posStart = marketRefresh.indexOf("selectedEventId = ");
        		
        		if(posStart == -1){
        			System.out.println(marketRefresh);
        			return false;
        		}
        		
        		posStart = marketRefresh.indexOf("\"", posStart);
        		posEnd = marketRefresh.indexOf("\"", posStart + 1);
        		
        		BJSCeventID = marketRefresh.substring(posStart+1, posEnd);
        		
        		
        		//拿DrawNumber
        		posStart = marketRefresh.indexOf("selectedEventNo = ");
        		
        		if(posStart == -1){
        			System.out.println(marketRefresh);
        			return false;
        		}
        		
        		posStart = marketRefresh.indexOf("\"", posStart);
        		posEnd = marketRefresh.indexOf("\"", posStart + 1);
        		
        		BJSCdrawNumber = marketRefresh.substring(posStart+1, posEnd);
        		
        		
        		//拿距离开盘的时间
        		posStart = marketRefresh.indexOf("parent.setEventIsPaused(");
        		
        		if(posStart == -1){
        			System.out.println(marketRefresh);
        			return false;
        		}
        		
        		posStart = marketRefresh.indexOf(",'", posStart);
        		posStart = marketRefresh.indexOf(",'", posStart + 1);
        		posStart = marketRefresh.indexOf(",'", posStart + 1);
        		//封盘时间
        		posEnd = marketRefresh.indexOf("'", posStart + 2);
        		
        		BJSCcloseTime = Long.parseLong(marketRefresh.substring(posStart+2, posEnd));
        		
        		//现在时间
        		
        		posStart = marketRefresh.indexOf(",'", posEnd);
        		posEnd = marketRefresh.indexOf("'", posStart + 2);
        		
        		long currentTime = Long.parseLong(marketRefresh.substring(posStart+2, posEnd));
        		
        		timeDValue = currentTime - System.currentTimeMillis();
        		
        		BJSCRemainTime = BJSCcloseTime - currentTime;
        	}
        	else{
        		System.out.println(marketRefresh);
        		return false;
        	}
        	

    		

        	return true;
    		
    	}catch(Exception e){

    		System.out.println(marketRefresh);
    		
    		return false;
    	}
    	

    }
    
    
    
    
    public static boolean getCQSSCselectionTypeIdListData(){
    	String market = "";
       	
    	try{
    		
        	String marketUrl = memberUrl + "CQSSC/Market.action?viewName=consolidated_market&gameTypeId=401&marketTypeIds=4001%2C4002%2C4003%2C4021%2C4022%2C4023%2C4041%2C4042%2C4043%2C4061%2C4062%2C4063%2C4081%2C4082%2C4083%2C4101%2C4102%2C4004%2C4123%2C4122%2C4121";
        	
        	//拿selectionTypeID
        	market = doGet(marketUrl, "", "");
        	
        	if(market == null)
        		market = doGet(marketUrl, "", "");
        	
        	int posStart = -1;
        	int posEnd = -1;
        	
        	if(market != null){
            	posStart = market.indexOf("['400201");
            	posEnd = market.indexOf("['400403'",  posStart);
            	
            	if(posStart >=0 && posEnd >= posStart){
                	String strCQSSCselectionTypeIdList = market.substring(posStart, posEnd);
                	
                	String[] strArrayCQSSCselectionTypeIdList = strCQSSCselectionTypeIdList.split("\r\n");
                	
                	
                	for(int i = 0; i < strArrayCQSSCselectionTypeIdList.length; i++){
                		
                		
                		strArrayCQSSCselectionTypeIdList[i] = strArrayCQSSCselectionTypeIdList[i].replace("'", "");
                		strArrayCQSSCselectionTypeIdList[i]= strArrayCQSSCselectionTypeIdList[i].replace("[", "");
                		strArrayCQSSCselectionTypeIdList[i] = strArrayCQSSCselectionTypeIdList[i].replace("],", "");
                		
                		String[] selectionTypeId = strArrayCQSSCselectionTypeIdList[i].split(",");
                		
                		
                		
                		CQSSCselectionTypeIdList.add(selectionTypeId);

                	}
                	
                	//打印
                	/*for(int k = 0; k < CQSSCselectionTypeIdList.size(); k ++){
                		String[] idItem = (String[]) CQSSCselectionTypeIdList.get(k);
                		for(int h = 0; h < idItem.length; h++){
                			System.out.print(idItem[h]);
                		}
                		System.out.println();
                	}*/
            	}
            	else{
            		System.out.println(market);
            		return false;
            	}
            	


        	}else{
        		System.out.println(market);
        		return false;
        	}
    	
    	}catch(Exception e){
        		
    		System.out.println(market);

    		
    		return false;
        }
    	
    	return true;
    }
    
    
    
    
    public static boolean getCQSSCmarketData(){
    	
    	
    	String market = "";
    	String marketRefresh = "";
    	
    	try{
    		
    		
        	int posStart = -1;
        	int posEnd = -1;
    		


        	
        	String oddsUrl = memberUrl + "CQSSC/MarketRefresh.action?viewName=consolidated_market_refresh&gameTypeId=401&marketTypeIds=4001%2C4002%2C4003%2C4021%2C4022%2C4023%2C4041%2C4042%2C4043%2C4061%2C4062%2C4063%2C4081%2C4082%2C4083%2C4101%2C4102%2C4004%2C4123%2C4122%2C4121";
        	

        	
        	marketRefresh =  doGet(oddsUrl, "", "");
        	
        	if(marketRefresh == null)
        		marketRefresh =  doGet(oddsUrl, "", "");
        	
        	if(marketRefresh != null){
            	posStart = marketRefresh.indexOf("pItm=[]");
            	posEnd = marketRefresh.indexOf("\r\n", posStart);
            	

            	
        		if(posStart != -1){
                	String strOdds = marketRefresh.substring(posStart, posEnd);

                	
            		posStart = strOdds.indexOf("pItm=[");
            		posStart = strOdds.indexOf("pItm=[", posStart+1);

            		while(posStart != -1){
            			posEnd = strOdds.indexOf(";", posStart);
            			
            			String strOddsItem = strOdds.substring(posStart + 5, posEnd);
            			
            			strOddsItem = strOddsItem.replace("'", "");
            			strOddsItem = strOddsItem.replace("[", "");
            			strOddsItem = strOddsItem.replace("]", "");
            			
            			String[] odds = strOddsItem.split(",");
            			
            			CQSSCoddsList.add(odds);
            			
            			posStart = strOdds.indexOf("pItm=[", posEnd);
            			posStart = strOdds.indexOf("pItm=[", posStart+1);
            			
            		}
            		
            		//打印
                	/*for(int k = 0; k < CQSSCoddsList.size(); k ++){
                		String[] idItem = (String[]) CQSSCoddsList.get(k);
                		for(int h = 0; h < idItem.length; h++){
                			System.out.print(idItem[h]);
                			System.out.print("   ");
                		}
                		System.out.println();
                	}*/
        		}
        		else{
        			System.out.println(marketRefresh);
        			System.out.println("没有赔率数据\n");
        		}
        		

        		
        		//拿eventID
        		posStart = marketRefresh.indexOf("selectedEventId = ");
        		
        		if(posStart == -1){
        			System.out.println(marketRefresh);
        			return false;
        		}
        		
        		posStart = marketRefresh.indexOf("\"", posStart);
        		posEnd = marketRefresh.indexOf("\"", posStart + 1);
        		
        		CQSSCeventID = marketRefresh.substring(posStart+1, posEnd);
        		
        		
        		//拿DrawNumber
        		posStart = marketRefresh.indexOf("selectedEventNo = ");
        		
        		if(posStart == -1){
        			System.out.println(marketRefresh);
        			return false;
        		}
        		
        		posStart = marketRefresh.indexOf("\"", posStart);
        		posEnd = marketRefresh.indexOf("\"", posStart + 1);
        		
        		CQSSCdrawNumber = marketRefresh.substring(posStart+1, posEnd);
        		
        		
        		//拿距离开盘的时间
        		posStart = marketRefresh.indexOf("parent.setEventIsPaused(");
        		
        		if(posStart == -1){
        			System.out.println(marketRefresh);
        			return false;
        		}
        		
        		posStart = marketRefresh.indexOf(",'", posStart);
        		posStart = marketRefresh.indexOf(",'", posStart + 1);
        		posStart = marketRefresh.indexOf(",'", posStart + 1);
        		//封盘时间
        		posEnd = marketRefresh.indexOf("'", posStart + 2);
        		
        		CQSSCcloseTime = Long.parseLong(marketRefresh.substring(posStart+2, posEnd));
        		
        		//现在时间
        		
        		posStart = marketRefresh.indexOf(",'", posEnd);
        		posEnd = marketRefresh.indexOf("'", posStart + 2);
        		
        		long currentTime = Long.parseLong(marketRefresh.substring(posStart+2, posEnd));
        		
        		timeDValue = currentTime - System.currentTimeMillis();
        		
            	CQSSCRemainTime = CQSSCcloseTime - currentTime;
        	}
        	else{
        		System.out.println(marketRefresh);
        		return false;
        	}
        	

    		

        	return true;
    		
    	}catch(Exception e){
    		
    		System.out.println(market);
    		System.out.println(marketRefresh);
    		
    		return false;
    	}
    	

    }
    
    
    public static long getCQSSClocalRemainTime() {
    	return CQSSCcloseTime - (System.currentTimeMillis() + timeDValue);
    }
    
    public static long getBJSClocalRemainTime(){
    	return BJSCcloseTime - (System.currentTimeMillis() + timeDValue);
    }
    
    
    public static boolean isEmptyData(String[] data, BetType betType) {
    	try {
	    	if(betType == BetType.CQSSC) {
	    		JSONArray cqsscLMGrabData = new JSONArray(data[0]);        	
	    		JSONArray gamesGrabData = cqsscLMGrabData.getJSONArray(0);
	    		if(gamesGrabData.length() == 0) {
	    			return true;
	    		}
	    		else {
	    			return false;
	    		}
	    	} else {
	    		for(int i = 0; i < 3; i++) {
		    		JSONArray cqsscLMGrabData = new JSONArray(data[i]);        	
		    		JSONArray gamesGrabData = cqsscLMGrabData.getJSONArray(0);
		    		if(gamesGrabData.length() > 0) {
		    			return false;
		    		}
	    		}
	    		return true;
	    	}
	    } catch(Exception e){
	    	autoBet.outputMessage.append("isEmptyData()构造下单数据错误！\n");
	    	return true;
	    }
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


           System.out.println("executing post " + httppost.getURI()); 
           
           StringEntity strEntity;
           try {
        	   strEntity = new StringEntity(jsonData, charset);
               httppost.setEntity(strEntity);
               CloseableHttpResponse response = httpclient.execute(httppost);
               try {
                   // 打印响应状态    
                   //System.out.println(response.getStatusLine());
                    HttpEntity entity = response.getEntity(); 
                    
                    
                    String res = EntityUtils.toString(entity);
                    

                    if (entity != null) {  
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
           } 
           return null;
       }
    
    
    public static boolean doBetCQSSC(String[] betData, double percent, boolean opposite, String remainTime){
    	String host = ADDRESS;
       	
        String jsonParam = "";
        
        if(previousCQSSCBetNumber.equals(CQSSCdrawNumber) && previousCQSSCBetResult == true) //之前如果已经下过单并且成功下单就直接返回
        	return false;
        
        
        //如果未到封盘时间
        if( CQSSCdrawNumber != null){
        	
        	//System.out.printf("下注重庆时时彩第%s期\n",CQSSCdrawNumber);
        	String outputStr = "[微彩]下注重庆时时彩第" + CQSSCdrawNumber + "期\n" + "最新数据时间距收盘" + remainTime + "秒\n";
        	autoBet.outputMessage.append(outputStr);
        	
        	if(isEmptyData(betData, BetType.CQSSC)) {
        		outputStr = "代理无人投注\n\n";
        		autoBet.outputMessage.append(outputStr);
        		return false;
        	}
        	
        	jsonParam = constructBetsData(betData, percent, BetType.CQSSC, opposite);
        
        	//outputBetsDetails(jsonParam, BetType.CQSSC);
        	
        	
        	System.out.println(jsonParam);
        	
        	String response = "";

        	response = bet(memberUrl + "Bet/PlaceBet.action", jsonParam, "UTF-8", "");
        	
        	if(response == null){
        		response = bet(memberUrl + "Bet/PlaceBet.action", jsonParam, "UTF-8", "");
        	}
        	
        	System.out.println("微彩下单结果");
        	System.out.println(response);
        	
        	boolean result = parseBetResult(response);
        	
        	previousCQSSCBetNumber = CQSSCdrawNumber;
        	previousCQSSCBetResult = result;
        	
        	if(result == true) {
				successTimes++;
				autoBet.labelWeiCaiSuccessBets.setText("成功次数:" + successTimes);
			} else {
				failTimes++;
				autoBet.labelWeiCaiFailBets.setText("失败次数:" + failTimes);
			}
			
			autoBet.labelWeiCaiTotalBets.setText("下单次数:" + (successTimes + failTimes));
        	
        	return result;
        
        }
        
        return false;
    }
    
    
    public static boolean doBetBJSC(String[] betData, double percent, boolean opposite, String remainTime){
    	String host = ADDRESS;
       	
        String jsonParam = "";
        
        if(previousBJSCBetNumber.equals(BJSCdrawNumber) && previousBJSCBetResult == true) //之前如果已经下过单并且成功下单就直接返回
        	return false;
        
        
        //如果未到封盘时间
        if( BJSCdrawNumber != null){
        	
        	//System.out.printf("下注重庆时时彩第%s期\n",BJSCdrawNumber);
        	String outputStr = "[微彩]下注北京赛车第" + BJSCdrawNumber + "期\n" + "最新数据时间距收盘" + remainTime + "秒\n";
        	autoBet.outputMessage.append(outputStr);
        	
        	if(isEmptyData(betData, BetType.BJSC)) {
        		outputStr = "代理无人投注\n\n";
        		autoBet.outputMessage.append(outputStr);
        		return false;
        	}
        	
        	jsonParam = constructBetsData(betData, percent, BetType.BJSC, opposite);
        
        	//outputBetsDetails(jsonParam, BetType.BJSC);
        	
        	
        	System.out.println(jsonParam);
        	
        	String response = "";

        	response = bet(memberUrl + "Bet/PlaceBet.action", jsonParam, "UTF-8", "");
        	
        	if(response == null){
        		response = bet(memberUrl + "Bet/PlaceBet.action", jsonParam, "UTF-8", "");
        	}
        	
        	System.out.println("微彩下单结果");
        	System.out.println(response);
        	
        	boolean result = parseBetResult(response);
        	
        	previousBJSCBetNumber = BJSCdrawNumber;
        	previousBJSCBetResult = result;
        	
        	if(result == true) {
				successTimes++;
				autoBet.labelWeiCaiSuccessBets.setText("成功次数:" + successTimes);
			} else {
				failTimes++;
				autoBet.labelWeiCaiFailBets.setText("失败次数:" + failTimes);
			}
			
			autoBet.labelWeiCaiTotalBets.setText("下单次数:" + (successTimes + failTimes));
        	
        	return result;
        
        }
        
        return false;
    }
    
    
    
    
    public static boolean parseBetResult(String betResult){
    	//todo
    	
    	int posStart = -1;
    	
    	int posEnd = -1;
    	
    	int betAmount = 0;
    	
    	if(betResult != null && betResult.length() > 0){
    		
        	posStart = betResult.indexOf("betResultItm = [");
        	
        	posEnd = betResult.indexOf("\r\n", posStart);
    	}

    	
    	autoBet.outputMessage.append("下注详情:\n");
    	
    	if(posStart != -1){
        	while(posStart != -1){
        		String betResultItm = betResult.substring(posStart, posEnd);
        		betResultItm = betResultItm.replace("betResultItm = [", "");
        		betResultItm = betResultItm.replace("\"", "");
        		betResultItm = betResultItm.replace("];", "");
        		String[] betResultItmArr = betResultItm.split(",");
        		
        		//
        		if(betResultItmArr[4].equals("SUCCEEDED")){
        			String outputStr  = String.format("%s : %s\n", betResultItmArr[1], betResultItmArr[3]);
        			autoBet.outputMessage.append(outputStr);
        			betAmount = betAmount + Integer.parseInt(betResultItmArr[3]);
        		}
        		else if(betResultItmArr[4].equals("FAILED")){
        			String outputStr  = String.format("%s : %s %s\n", betResultItmArr[1], betResultItmArr[3], betResultItmArr[5]);
        			autoBet.outputMessage.append(outputStr);
        		}
        		
        		posStart = betResult.indexOf("betResultItm = [", posEnd);
        		posEnd = betResult.indexOf("\r\n", posStart);
        	}
        	
        	if(betAmount >0){
        		String outputStr  = String.format("微彩下注成功! 下单总额: %d\n\n", betAmount);
        		autoBet.outputMessage.append(outputStr);
        	}
        	else{
        		autoBet.outputMessage.append("微彩下注失败\n\n");
        		return false;
        	}
        	
        	
        	
        	
    	}
    	else{
    		autoBet.outputMessage.append("微彩下注失败\n");
    		return false;
    	}

    	
    	return true;
    }
    
	public static String setCookie(CloseableHttpResponse httpResponse)
	{
		strCookies = "";
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
    
    
    
    public static String doGet(String url, String cookies, String referUrl) {
    	
    	
    	
        try {  
            // 创建httpget.    
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
           
            // 执行get请求.    
            CloseableHttpResponse response = httpclient.execute(httpget); 
            //System.out.println(response.getStatusLine());
            
            try{
            	setCookie(response);  	
            	//System.out.println("设置cookie:" + strCookies);
            	
            	String statusLine = response.getStatusLine().toString();
            	
            	if(statusLine.indexOf("302") > 0) {
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

        } 
        
        return null;
    }
    
    
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
            	setCookie(response);
            	//System.out.println("设置cookie:" + strCookies);

            	
            	
            	if(response.getStatusLine().toString().indexOf("200 OK") > 0) {
                    HttpEntity entity = response.getEntity(); 
                    
                    
                    String res = EntityUtils.toString(entity);
                    
                    if(res != "" && res != null){
                    	return res;
                    }

            	}
            	
            	if(response.getStatusLine().toString().indexOf("302") > 0) {
            		return response.getFirstHeader("Location").getValue();

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
     
        } 
        return null;
    }
    
    
    public static String getPicNum(String picUri) {
    	try {
	   	    HttpGet httpget = new HttpGet(picUri);
	        httpget.addHeader("Connection","keep-alive");
	
	        httpget.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 "
	        					+ "(KHTML, like Gecko) Chrome/51.0.2704.79 Safari/537.36");           
	        System.out.println("executing request " + httpget.getURI()); 
       
	        // 执行get请求.    
        
	        CloseableHttpResponse response = httpclient.execute(httpget, clientContext); 
       	 try {
       		    setCookie(response);
                // 打印响应状态    
                System.out.println(response.getStatusLine()); 
                System.out.println("------------------------------------");
                File storeFile = new File("wcyzm.png");   //图片保存到当前位置
                FileOutputStream output = new FileOutputStream(storeFile);  
                //得到网络资源的字节数组,并写入文件  
                byte [] a = EntityUtils.toByteArray(response.getEntity());
                output.write(a);  
                output.close();  
                
         
                InputStream ins = null;
        		 String[] cmd = new String[]{ConfigReader.getTessPath() + "\\tesseract", "wcyzm.png", "result", "-l", "eng"};

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
    
}
