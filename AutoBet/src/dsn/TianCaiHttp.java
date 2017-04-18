package dsn;
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



public class TianCaiHttp {

    CloseableHttpClient httpclient = null;
    RequestConfig requestConfig = null;
    HttpClientContext clientContext = null;
    
    
    TianCaiHttp() {
        requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
        //requestConfig = RequestConfig.copy(requestConfig).setRedirectsEnabled(false).build();//禁止重定向 ， 以便获取cookieb18
        requestConfig = RequestConfig.copy(requestConfig).setConnectTimeout(10*1000).setConnectionRequestTimeout(10*1000).setSocketTimeout(10*1000).build();//设置超时
        httpclient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
    }
    
    
    String strCookies = "";
    String serachKey = "51639";
    
    long time = 0;
    
    String CQSSCdrawNumber = "";
    String BJSCdrawNumber = "";
    String previousCQSSCdrawNumber = "";
    String previousBJSCdrawNumber = "";
    String previousCQSSCBetNumber = "";
    String previousBJSCBetNumber = "";
    
    boolean previousCQSSCBetResult = false;
    boolean previousBJSCBetResult = false;
    
    
    String ADDRESS = "";
    String ACCOUNT = "";
    String PASSWORD = "";
    String HOST = "";
    
    JSONObject CQSSCoddsData = null;
    JSONObject BJSCoddsData = null;
    
    String sessionID = "";
    
    long timeDValue = 0;  //网站时间和电脑时间的差值  网页时间 - 当前时间
    long CQSSCcloseTime = 0;    //封盘时间
    long BJSCcloseTime = 0;
    
    int failTimes = 0;    //下单失败次数
    int successTimes = 0; //下单成功次数
    
    int totalAmount = 0;
    
    
    public String getCQSSCdrawNumber(){
    	return CQSSCdrawNumber;
    }
    
    public String getBJSCdrawNumber(){
    	return BJSCdrawNumber;
    }
    
    public void setLoginParams(String address, String account, String password){
    	ADDRESS = address;
    	ACCOUNT = account;
    	PASSWORD = password;
    }
    
    
    public boolean login() {    	
    	boolean res = false;
    	for(int i = 0; i < 15; i++) {
    		if(loginToTianCai()) {
    			res = true;
    			break;
    		}
    		try {
    			Thread.currentThread().sleep(1000);
    		} catch(InterruptedException e) {
    			//todo
    		}
    	}
    	
    	if(res == false) {
    		autoBet.outputGUIMessage("会员" + ACCOUNT + "连接失败,正在重新登录....\n");
	    	while(!loginToTianCai()) {
	    		try {
	    			Thread.currentThread().sleep(60*1000);
	    		} catch(InterruptedException e) {
	    			//todo
	    		}
	    	}
	    	autoBet.outputGUIMessage("会员" + ACCOUNT + "重新登录成功\n");
    	}
    	
    	return true;
    }
    
    public void connFailLogin() {
    	
    }
    
    
    public boolean loginToTianCai(){
	    String result = doGet(ADDRESS + "/woa/app/welcome", "", "");
		
		if(result == null)
			return false;
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
	    params.add(new BasicNameValuePair("code", serachKey));

	    result = doPost(ADDRESS + "/woa/app/welcome/select", params, "", ADDRESS + "/woa/app/welcome");
	    
	    if(result == "") {
	    	result = doPost(ADDRESS + "/woa/app/welcome/select", params, "", ADDRESS + "/woa/app/welcome");
	    	if(result == "")
	    		return false;
	    }
	    
	    System.out.println(result);
	    
	    int posStart = result.indexOf("test-bar-s1");
	    posStart = result.indexOf("http", posStart);
	    int posEnd = result.indexOf('"', posStart);
	    HOST = result.substring(posStart, posEnd);
	    
	    result =  doGet(HOST + "/index.html?mob=0", "", ADDRESS + "/woa/app/welcome/select");
	    if(result == "") {
	    	result = doGet(HOST + "/index.html?mob=0", "", ADDRESS + "/woa/app/welcome/select");
	    	if(result == "")
	    		return false;
	    }
	    
	    System.out.println(result);
	    result =  getPicNum(HOST + "/app/checkCode/image", HOST + "/index.html?mob=0");
	    if(result == "") {
	    	result =  getPicNum(HOST + "/app/checkCode/image", HOST + "/index.html?mob=0");
	    	if(result == "")
	    		return false;
	    }
	    
	    System.out.println(result);
	    
	    List<NameValuePair> loginParams = new ArrayList<NameValuePair>();
        loginParams.add(new BasicNameValuePair("userName", ACCOUNT));
        loginParams.add(new BasicNameValuePair("password", PASSWORD));
        loginParams.add(new BasicNameValuePair("checkCode", result));

    	
        
        result = doPost(HOST + "/app/loginVerification", loginParams, "", HOST + "/index.html?mob=0");
        System.out.println(result);
        
        JSONObject joResult = null;
        String postAddress = "";
		try{
			joResult = new JSONObject(result);	
		
		
			boolean bRes = joResult.getBoolean("success");
			System.out.println(bRes);
			
			if(!bRes) {
				return false;
			}
			
			postAddress = joResult.getString("message");
			posStart = postAddress.indexOf("code=") + 5;
			posEnd = postAddress.indexOf("&", posStart);
			sessionID = postAddress.substring(posStart, posEnd);
			
			List<NameValuePair> loginParams1 = new ArrayList<NameValuePair>();
	        loginParams1.add(new BasicNameValuePair("code", sessionID));
	        loginParams1.add(new BasicNameValuePair("a", ACCOUNT));
	        loginParams1.add(new BasicNameValuePair("lc", "en"));
	        loginParams1.add(new BasicNameValuePair("site", joResult.getString("site")));
	        loginParams1.add(new BasicNameValuePair("ptn", joResult.getString("ptn")));
	        loginParams1.add(new BasicNameValuePair("mob", "0"));

	        result = doPost(postAddress.substring(0, postAddress.indexOf("?")), loginParams1, "", HOST + "/index.html?mob=0");
		}catch(Exception e) {
			return false;
		}
        
        if(result == "" || result.indexOf("sessionId") == -1) {
        	 return false;
        }
        
        posEnd = postAddress.indexOf('/', 8);
        HOST = postAddress.substring(0, posEnd);
        loginAgree();
	    
	    return true;
    }
    
    
    //开盘时间为9点到24点
    public boolean  isInBJSCBetTime(long time){
        Date date = new Date(time);
        int currentHour = date.getHours();
        int currentMinutes = date.getMinutes();
        int currentSeconds = date.getSeconds();
        
        if(currentHour >=9 && (currentHour * 60 + currentMinutes <= 23 * 60 + 57)){
        	return true;
        }
        
        return false;
    }
  
    //北京赛车开盘时间为10点到01:55
    public boolean isInCQSSCBetTime(long time){
        Date date = new Date(time);
        int currentHour = date.getHours();
        int currentMinutes = date.getMinutes();
        int currentSeconds = date.getSeconds();

        if(currentHour <10 && (currentHour * 60 + currentMinutes > 1 * 60 + 55))
           return false;
        
        return true;
    }

    public void loginAgree() {
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("command", "UPDATE"));
    	params.add(new BasicNameValuePair("sessionId", sessionID));
    	params.add(new BasicNameValuePair("lotteryType", "BJC"));
    	params.add(new BasicNameValuePair("hasPlayerInfo", "true"));
        doPost(HOST + "/lotteryweb/WebClientAgent", params, "", HOST + "/lotteryweb/Login");
    }
    
    public long getCQSSCRemainTime(){
        //get period
    	String response = "";
        
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("command", "UPDATE"));
    	params.add(new BasicNameValuePair("sessionId", sessionID));
    	params.add(new BasicNameValuePair("lotteryType", "SSC"));
    	params.add(new BasicNameValuePair("hasPlayerInfo", "true"));
        response = doPost(HOST + "/lotteryweb/WebClientAgent", params, "", HOST + "/lotteryweb/Login");
        if(response == "") {
        	System.out.println("获取重庆时时彩失败");
        	return -1;
        }
        
        JSONObject joResult = null;
        long remainTime = -9999;
		try{
			joResult = new JSONObject(response.toString());
			
			if(joResult.getInt("returnCode") == 0) {
				JSONObject joGameInfo = joResult.getJSONObject("gameInfo");
				//System.out.println("gameNo:" + joGameInfo.getString("gameNo"));
				System.out.println("gameStatus:" + joGameInfo.getString("gameStatus"));
				System.out.println("closeTime:" + joGameInfo.getLong("closeTime"));
				//System.out.println("gameTime:" + joGameInfo.getLong("gameTime"));
				//System.out.println("odds:" + joResult.getJSONObject("odds"));
				remainTime = joGameInfo.getLong("gameTime") - joGameInfo.getLong("closeTime");
				CQSSCcloseTime = System.currentTimeMillis() + (remainTime * 1000);
				CQSSCoddsData = joResult.getJSONObject("odds");
				if(!CQSSCdrawNumber.equals(joGameInfo.getString("gameNo"))) { //新的一期
					previousCQSSCdrawNumber = CQSSCdrawNumber;
					CQSSCdrawNumber = joGameInfo.getString("gameNo");
					if(previousCQSSCBetNumber != previousCQSSCdrawNumber && previousCQSSCBetNumber != CQSSCdrawNumber && previousCQSSCBetNumber != "") {//判断上一期有没有漏投
						long dNum = 0;
						try {
						    long drawNum = Long.parseLong(CQSSCdrawNumber)%1000;
						    long preBetNum =  Long.parseLong(previousCQSSCBetNumber)%1000;
						    if(drawNum - preBetNum > 0) {
						    	dNum = drawNum - preBetNum - 1;
						    }else if(drawNum - preBetNum  < 0){
						    	dNum = drawNum + 120 - preBetNum - 1;
						    }
						} catch (NumberFormatException e) {
						    e.printStackTrace();
						}
						
						previousCQSSCBetNumber = previousCQSSCdrawNumber;
						failTimes += dNum;
						autoBet.labelFailBets.setText("失败次数:" + failTimes);
						autoBet.labelTotalBets.setText("下单次数:" + (successTimes + failTimes));
						System.out.println("漏投" + dNum + "次, 期数：" + CQSSCdrawNumber + "上次下单期数：" + previousCQSSCBetNumber);
				    }
				}
				
			}
			else {
				System.out.println("message:" + joResult.getString("returnMsg"));
			}
		}catch(Exception e) {
			e.printStackTrace();
			return -9999;
		}
        
        return remainTime;
    }
    
    
    
    public long getBJSCRemainTime(){
        //get period
    	String response = "";
        
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("command", "UPDATE"));
    	params.add(new BasicNameValuePair("sessionId", sessionID));
    	params.add(new BasicNameValuePair("lotteryType", "BJC"));
    	params.add(new BasicNameValuePair("hasPlayerInfo", "true"));
        response = doPost(HOST + "/lotteryweb/WebClientAgent", params, "", HOST + "/lotteryweb/Login");
        if(response == "") {
        	System.out.println("[添彩]北京赛车时间获取失败");
        	return -1;
        }
        
        //System.out.println(response.toString());
        
        JSONObject joResult = null;
        long remainTime = -9999;
		try{
			joResult = new JSONObject(response.toString());
			
			if(joResult.getInt("returnCode") == 0) {
				JSONObject joGameInfo = joResult.getJSONObject("gameInfo");
				//System.out.println("gameNo:" + joGameInfo.getString("gameNo"));
				System.out.println("gameStatus:" + joGameInfo.getString("gameStatus"));
				System.out.println("closeTime:" + joGameInfo.getLong("closeTime"));
				//System.out.println("gameTime:" + joGameInfo.getLong("gameTime"));
				//System.out.println("odds:" + joResult.getJSONObject("odds"));
				remainTime = joGameInfo.getLong("gameTime") - joGameInfo.getLong("closeTime");
				BJSCcloseTime = System.currentTimeMillis() + (remainTime * 1000);
				BJSCoddsData = joResult.getJSONObject("odds");
				if(!BJSCdrawNumber.equals(joGameInfo.getString("gameNo"))) { //新的一期
					previousBJSCdrawNumber = BJSCdrawNumber;
					BJSCdrawNumber = joGameInfo.getString("gameNo");
					if(previousBJSCBetNumber != previousBJSCdrawNumber && previousBJSCBetNumber != BJSCdrawNumber && previousBJSCBetNumber != "") {//判断上一期有没有漏投
						int dNum = 0;
						try {
						    dNum = Integer.parseInt(BJSCdrawNumber) - Integer.parseInt(previousBJSCBetNumber) -1;
						} catch (NumberFormatException e) {
						    e.printStackTrace();
						}
						
						previousBJSCBetNumber = previousBJSCdrawNumber;
						failTimes += dNum;
						autoBet.labelTianCaiFailBets.setText("失败次数:" + failTimes);
						autoBet.labelTianCaiTotalBets.setText("下单次数:" + (successTimes + failTimes));
						System.out.println("漏投" + dNum + "次, 期数：" + BJSCdrawNumber + "上次下单期数：" + previousBJSCBetNumber);
				    }
				}			
				
			}
			else {
				System.out.println("message:" + joResult.getString("returnMsg"));
			}
		}catch(Exception e) {
			e.printStackTrace();
			return -9999;
		}
        
        //{"returnCode":0,"announcement":"尊敬的用户，由於北京PK10官网开奖讯号出现不稳定，故第562372期开奖结果曾经出现问题，系统已重新结算有关期数之受影响的注单，不便之处，敬请原谅。","winLoss":0,"hasPlayerInfo":true,"gameInfo":{"gameType":"SSC","gameNo":"20160826055","lastGameResult":[],"lastGameNo":"20160826054","gameStatus":"BETTING","gameTime":521,"closeTime":90,"history":[],"roadMap":{"BALL_1":{"ODD":[[0,2],[1,1],[0,1],[1,1],[0,1],[1,1],[0,2],[1,1],[0,1],[1,1],[0,1],[1,1],[0,1],[1,2],[0,2],[1,4],[0,1],[1,1],[0,1],[1,4],[0,1]],"BIG":[[0,4],[1,1],[0,1],[1,1],[0,4],[1,1],[0,1],[1,4],[0,4],[1,1],[0,1],[1,3],[0,3],[1,1],[0,1]],"NO_0":[[2,2],[1,1],[2,1],[7,1],[4,1],[5,1],[2,1],[4,1],[1,1],[4,1],[7,1],[2,1],[5,1],[6,1],[9,2],[2,1],[0,1],[3,2],[9,1],[3,1],[8,1],[7,1],[8,1],[1,3],[9,1],[4,1]]},"BALL_2":{"ODD":[[1,6],[0,4],[1,3],[0,2],[1,1],[0,2],[1,2],[0,1],[1,4],[0,1],[1,2],[0,1],[1,2]],"BIG":[[1,4],[0,2],[1,2],[0,2],[1,2],[0,3],[1,2],[0,2],[1,6],[0,1],[1,1],[0,3],[1,1]],"NO_0":[[7,1],[9,2],[7,1],[3,2],[8,2],[0,1],[2,1],[7,2],[3,1],[0,2],[9,1],[8,1],[0,1],[3,1],[5,1],[8,1],[7,1],[5,1],[9,2],[2,1],[9,1],[1,1],[2,1],[3,1],[5,1]]},"BALL_5":{"ODD":[[1,2],[0,1],[1,2],[0,3],[1,1],[0,1],[1,4],[0,1],[1,2],[0,2],[1,1],[0,1],[1,1],[0,2],[1,4],[0,1],[1,1],[0,1]],"BIG":[[1,3],[0,1],[1,2],[0,2],[1,5],[0,5],[1,1],[0,2],[1,1],[0,1],[1,2],[0,1],[1,1],[0,1],[1,3]],"NO_0":[[5,1],[9,1],[8,1],[1,1],[7,1],[8,1],[2,1],[4,1],[9,1],[8,1],[7,1],[5,1],[9,1],[3,1],[0,1],[3,2],[0,1],[6,1],[3,1],[4,1],[5,1],[0,1],[8,1],[9,1],[3,1],[7,1],[3,1],[8,1],[7,1],[6,1]]},"BALL_3":{"ODD":[[1,5],[0,3],[1,1],[0,1],[1,1],[0,1],[1,2],[0,2],[1,5],[0,3],[1,1],[0,1],[1,1],[0,4]],"BIG":[[1,5],[0,1],[1,1],[0,2],[1,4],[0,2],[1,1],[0,1],[1,3],[0,1],[1,1],[0,1],[1,4],[0,1],[1,1],[0,2]],"NO_0":[[5,2],[9,1],[7,1],[9,1],[2,1],[6,1],[2,1],[1,1],[8,1],[5,1],[8,1],[7,1],[1,1],[0,1],[8,1],[1,1],[7,3],[3,1],[6,1],[2,1],[8,1],[7,1],[6,1],[9,1],[2,1],[8,1],[2,2]]},"D_T_T":{"DRAGON":[[1,3],[0,1],[2,1],[1,1],[0,1],[1,4],[0,1],[1,1],[0,5],[1,1],[2,1],[1,1],[0,2],[2,1],[1,1],[0,1],[1,3],[0,1],[1,1]]},"TOTAL":{"ODD":[[1,2],[0,1],[1,1],[0,1],[1,1],[0,2],[1,5],[0,2],[1,1],[0,2],[1,2],[0,3],[1,2],[0,1],[1,3],[0,2]],"BIG":[[0,1],[1,4],[0,1],[1,1],[0,3],[1,3],[0,2],[1,1],[0,2],[1,1],[0,2],[1,1],[0,1],[1,2],[0,1],[1,1],[0,2],[1,1],[0,1]]},"BALL_4":{"ODD":[[0,2],[1,1],[0,3],[1,1],[0,1],[1,1],[0,4],[1,1],[0,2],[1,4],[0,1],[1,1],[0,2],[1,3],[0,2],[1,2]],"BIG":[[0,1],[1,3],[0,2],[1,1],[0,1],[1,1],[0,6],[1,1],[0,2],[1,1],[0,2],[1,1],[0,1],[1,2],[0,4],[1,1],[0,1]],"NO_0":[[2,1],[8,1],[5,1],[6,1],[0,1],[4,1],[5,1],[0,1],[5,1],[0,2],[2,1],[4,1],[1,1],[4,1],[6,1],[1,1],[3,1],[9,1],[3,1],[2,1],[9,1],[4,1],[6,1],[5,1],[3,1],[1,1],[4,1],[2,1],[5,1],[3,1]]}},"twoSideRanking":[{"betOn":"BALL_3","betType":"EVEN","times":4},{"betOn":"BALL_5","betType":"BIG","times":3},{"betOn":"BALL_2","betType":"ODD","times":2},{"betOn":"BALL_3","betType":"SMALL","times":2},{"betOn":"BALL_4","betType":"ODD","times":2},{"betOn":"TOTAL","betType":"EVEN","times":2}],"appearanceList":[{"BALL_1":{"NO_6":1,"NO_1":5,"NO_7":3,"NO_0":1,"NO_2":6,"NO_5":2,"NO_8":2,"NO_9":4,"NO_3":3,"NO_4":4},"BALL_2":{"NO_6":0,"NO_1":1,"NO_7":5,"NO_0":4,"NO_2":3,"NO_5":3,"NO_8":4,"NO_9":6,"NO_3":5,"NO_4":0},"BALL_5":{"NO_6":2,"NO_1":1,"NO_7":4,"NO_0":3,"NO_2":1,"NO_5":3,"NO_8":5,"NO_9":4,"NO_3":6,"NO_4":2},"BALL_3":{"NO_6":3,"NO_1":3,"NO_7":6,"NO_0":1,"NO_2":6,"NO_5":3,"NO_8":5,"NO_9":3,"NO_3":1,"NO_4":0},"BALL_4":{"NO_6":3,"NO_1":3,"NO_7":0,"NO_0":4,"NO_2":4,"NO_5":5,"NO_8":1,"NO_9":2,"NO_3":4,"NO_4":5}},{"BALL_1":{"NO_6":16,"NO_1":2,"NO_7":6,"NO_0":12,"NO_2":13,"NO_5":17,"NO_8":5,"NO_9":1,"NO_3":8,"NO_4":0},"BALL_2":{"NO_6":31,"NO_1":3,"NO_7":9,"NO_0":13,"NO_2":2,"NO_5":0,"NO_8":10,"NO_9":4,"NO_3":1,"NO_4":31},"BALL_5":{"NO_6":0,"NO_1":27,"NO_7":1,"NO_0":8,"NO_2":24,"NO_5":9,"NO_8":2,"NO_9":6,"NO_3":3,"NO_4":10},"BALL_3":{"NO_6":5,"NO_1":14,"NO_7":6,"NO_0":16,"NO_2":0,"NO_5":20,"NO_8":2,"NO_9":4,"NO_3":10,"NO_4":31},"BALL_4":{"NO_6":7,"NO_1":4,"NO_7":31,"NO_0":20,"NO_2":2,"NO_5":1,"NO_8":29,"NO_9":9,"NO_3":0,"NO_4":3}}],"parameters":{"roundTime":"600"}},"odds":{"BALL_1":{"SMALL":1.9838,"NO_0":9.9140,"NO_2":9.9140,"NO_9":9.9140,"NO_3":9.9140,"NO_4":9.9140,"NO_6":9.9140,"EVEN":1.9838,"NO_1":9.9140,"NO_7":9.9140,"ODD":1.9838,"BIG":1.9838,"NO_8":9.9140,"NO_5":9.9140},"BALL_2":{"SMALL":1.9838,"NO_0":9.9140,"NO_2":9.9140,"NO_9":9.9140,"NO_3":9.9140,"NO_4":9.9140,"NO_6":9.9140,"EVEN":1.9838,"NO_1":9.9140,"NO_7":9.9140,"ODD":1.9838,"BIG":1.9838,"NO_8":9.9140,"NO_5":9.9140},"MIDDLE3":{"THREE_CHAOS":2.6000,"THREE_EQUAL":68.0000,"THREE_PAIR":2.8800,"THREE_HALF_STRAIGHT":2.3000,"THREE_STRAIGHT":12.8000},"BALL_5":{"SMALL":1.9838,"NO_0":9.9140,"NO_2":9.9140,"NO_9":9.9140,"NO_3":9.9140,"NO_4":9.9140,"NO_6":9.9140,"EVEN":1.9838,"NO_1":9.9140,"NO_7":9.9140,"ODD":1.9838,"BIG":1.9838,"NO_8":9.9140,"NO_5":9.9140},"FIRST3":{"THREE_CHAOS":2.6000,"THREE_EQUAL":68.0000,"THREE_PAIR":2.8800,"THREE_HALF_STRAIGHT":2.3000,"THREE_STRAIGHT":12.8000},"BALL_3":{"SMALL":1.9838,"NO_0":9.9140,"NO_2":9.9140,"NO_9":9.9140,"NO_3":9.9140,"NO_4":9.9140,"NO_6":9.9140,"EVEN":1.9838,"NO_1":9.9140,"NO_7":9.9140,"ODD":1.9838,"BIG":1.9838,"NO_8":9.9140,"NO_5":9.9140},"LAST3":{"THREE_CHAOS":2.6000,"THREE_EQUAL":68.0000,"THREE_PAIR":2.8800,"THREE_HALF_STRAIGHT":2.3000,"THREE_STRAIGHT":12.8000},"D_T_T":{"DRAGON":1.9838,"TIE":8.8000,"TIGER":1.9838},"TOTAL":{"EVEN":1.9838,"ODD":1.9838,"SMALL":1.9838,"BIG":1.9838},"BALL_4":{"SMALL":1.9838,"NO_0":9.9140,"NO_2":9.9140,"NO_9":9.9140,"NO_3":9.9140,"NO_4":9.9140,"NO_6":9.9140,"EVEN":1.9838,"NO_1":9.9140,"NO_7":9.9140,"ODD":1.9838,"BIG":1.9838,"NO_8":9.9140,"NO_5":9.9140}},"balance":10.00000,"sessionId":"eb212084-bffe-4523-9b61-f5463ba198ac","webServerId":"7d98c50a-2fd1-4edd-b707-834d46ed151f","lastResult":{"gameType":"SSC","gameNo":"20160826054","issueTime":null,"gameResult":[4,5,2,3,6]},"tray":0,"lotteryType":"SSC","command":"UPDATE"}


        return remainTime;
    }
    
    public long getCQSSClocalRemainTime() {
    	return (CQSSCcloseTime - System.currentTimeMillis()) / 1000;
    }
    
    public long getBJSClocalRemainTime() {
    	return (BJSCcloseTime - System.currentTimeMillis()) / 1000;
    }
    
    public static void outputBetsDetails(String jsonData, BetType betType){
    	
    	autoBet.outputGUIMessage("下注详情：\n");
    	try{
        	if(betType == BetType.BJSC){
            	
            	JSONObject betsData = new JSONObject(jsonData);
            	JSONArray gamesData = betsData.getJSONArray("bets");
            	int totalAmout = 0;
            	
            	for(int i = 1; i <= 10 ; i++){
            		String gameDX = "DX" + Integer.toString(i);
            		String gameDS = "DS" + Integer.toString(i);
            		String gameLH = "LH" + Integer.toString(i);
            		JSONObject gameData;
            		int amountDX = 0;
            		String contentsDX = "";
            		int amountDS = 0;
            		String contentsDS = "";
            		int amountLH = 0;
            		String contentsLH = "";
            		for(int j = 0; j < gamesData.length(); j++){
            			
            			gameData = gamesData.getJSONObject(j);
            			
            			String game = gameData.getString("game");
            			if(game.equals(gameDX)){
            				amountDX = gameData.getInt("amount");
            				contentsDX = gameData.getString("contents");
            				contentsDX = contentsDX.equals("D")?"大":"小";
            			}
            			if(game.equals(gameDS)){
            				amountDS = gameData.getInt("amount");  
            				contentsDS = gameData.getString("contents");
            				contentsDS = contentsDS.equals("D")?"单":"双";
            			}
    					if(game.equals(gameLH)){
    						amountLH = gameData.getInt("amount");
    						contentsLH = gameData.getString("contents");
    						contentsLH = contentsLH.equals("L")?"龙":"虎";
    					}

    					

            		}
            		
    				String outputStr = "";
    				if(amountDX != 0 ){
    					if(i == 1){
    						outputStr  = String.format("冠军%s: %d,", contentsDX, amountDX);
    						autoBet.outputGUIMessage(outputStr);
    					}
    					else if(i == 2){
    						outputStr  = String.format("亚军%s: %d,", contentsDX, amountDX);
    						autoBet.outputGUIMessage(outputStr);
    					}
    					else{
    						outputStr  = String.format("第%s名%s: %d,", Integer.toString(i), contentsDX, amountDX);
    						autoBet.outputGUIMessage(outputStr);
    					}
    					
    					totalAmout += amountDX;
    				}
    				
    				if(amountDS != 0 ){
    					if(i == 1){
    						outputStr  = String.format("冠军%s: %d,", contentsDS, amountDS);
    						autoBet.outputGUIMessage(outputStr);
    					}
    					else if(i == 2){
    						outputStr  = String.format("亚军%s: %d,", contentsDS, amountDS);
    						autoBet.outputGUIMessage(outputStr);
    					}
    					else{
    						outputStr  = String.format("第%s名%s: %d,", Integer.toString(i), contentsDS, amountDS);
    						autoBet.outputGUIMessage(outputStr);
    					}
    					totalAmout += amountDS;
    				}
    				
    				if(amountLH != 0 ){
    					if(i == 1){
    						outputStr  = String.format("冠军%s: %d,", contentsLH, amountLH);
    						autoBet.outputGUIMessage(outputStr);
    					}
    					else if(i == 2){
    						outputStr  = String.format("亚军%s: %d,", contentsLH, amountLH);
    						autoBet.outputGUIMessage(outputStr);
    					}
    					else{
    						outputStr  = String.format("第%s名%s: %d,", Integer.toString(i), contentsLH, amountLH);
    						autoBet.outputGUIMessage(outputStr);
    					}
    					totalAmout += amountLH;
    					
    				}
            		
    				autoBet.outputGUIMessage("\n");
            	}
            	autoBet.outputGUIMessage("下单总金额:" + totalAmout + "\n");
        	}
        	
        	
        	if(betType == BetType.CQSSC){
            	
            	JSONObject betsData = new JSONObject(jsonData);
            	JSONArray gamesData = betsData.getJSONArray("bets");
            	int totalAmount = 0;
            	
            	for(int i = 1; i <= 5 ; i++){
            		String gameDX = "DX" + Integer.toString(i);
            		String gameDS = "DS" + Integer.toString(i);        		
            		JSONObject gameData;
            		int amountDX = 0;
            		String contentsDX = "";
            		int amountDS = 0;
            		String contentsDS = "";
            		for(int j = 0; j < gamesData.length(); j++){
            			gameData = gamesData.getJSONObject(j);
            			String game = gameData.getString("game");
            			if(game.equals(gameDX)){
            				amountDX = gameData.getInt("amount");
            				contentsDX = gameData.getString("contents");
            				contentsDX = contentsDX.equals("D")?"大":"小";
            			}
            			if(game.equals(gameDS)){
            				amountDS = gameData.getInt("amount");  
            				contentsDS = gameData.getString("contents");
            				contentsDS = contentsDS.equals("D")?"单":"双";
            			}
            			

            		}
            		
            		
        			String outputStr = "";
        			
    				if(amountDX != 0 ){
    						outputStr  = String.format("第%s球%s: %d,", Integer.toString(i), contentsDX, amountDX);
    						autoBet.outputGUIMessage(outputStr);
    						totalAmount += amountDX;
    				}
    				
    				if(amountDS != 0 ){
    					outputStr  = String.format("第%s球%s: %d,", Integer.toString(i), contentsDS, amountDS);
    					autoBet.outputGUIMessage(outputStr);
    					totalAmount += amountDS;
    				}
    				
    				autoBet.outputGUIMessage("\n");
            		
            	}
            	
            	
            	String gameZDX = "ZDX";
            	String gameZDS = "ZDS";
            	String gameLH = "LH";
            	
            	int amountZDX = 0;
            	int amountZDS = 0;
            	int amountLH = 0;
            	
            	String contentsZDX = "";
            	String contentsZDS = "";
            	String contentsLH = "";
            	
        		for(int j = 0; j < gamesData.length(); j++){
        			JSONObject gameData;
        			gameData = gamesData.getJSONObject(j);
        			String game = gameData.getString("game");
        			
        			if(game.equals(gameZDX)){
        				amountZDX = gameData.getInt("amount");
        				contentsZDX = gameData.getString("contents");
        				contentsZDX = contentsZDX.equals("D")?"大":"小";
        			}
        			if(game.equals(gameZDS)){
        				amountZDS = gameData.getInt("amount");  
        				contentsZDS = gameData.getString("contents");
        				contentsZDS = contentsZDS.equals("D")?"单":"双";
        			}
    				if(game.equals(gameLH)){
    					amountLH = gameData.getInt("amount");
    					contentsLH = gameData.getString("contents");
    					contentsLH = contentsLH.equals("L")?"龙":"虎";
    				}
    	
        		}
        		
    			String outputStr = "";
    			if(amountZDX != 0){
    				outputStr  = String.format("总%s: %d,",  contentsZDX, amountZDX);
    				autoBet.outputGUIMessage(outputStr);
    				totalAmount += amountZDX;
    			}
    			
    			if(amountZDS != 0){
    				outputStr  = String.format("总%s: %d,",  contentsZDS, amountZDS);
    				autoBet.outputGUIMessage(outputStr);
    				totalAmount += amountZDS;
    			}
    			
    			if(amountLH != 0){
    				outputStr  = String.format("%s: %d,",  contentsLH, amountLH);
    				autoBet.outputGUIMessage(outputStr);
    				totalAmount += amountLH;
    			}
    			
    			autoBet.outputGUIMessage("\n");
    			autoBet.outputGUIMessage("下单总金额:" + totalAmount +"\n");
        	}
    	}catch(Exception e){
    		e.printStackTrace();
    	}

    	
    }
    
    public boolean doBetCQSSC(String[] betData, double percent, boolean opposite, String remainTime) {
        String strBet = "";
        
        if(previousCQSSCBetNumber.equals(CQSSCdrawNumber) && previousCQSSCBetResult == true) {//之前如果已经下过单并且成功下单就直接返回
        	return false;
        }
        
        if(isEmptyData(betData, BetType.CQSSC)) {
    		autoBet.outputGUIMessage("代理无人投注\n\n");
    		return false;
    	}
    	
        String outputStr = "[添彩]下注重庆时时彩第" + CQSSCdrawNumber + "期\n" + "最新数据时间距收盘" + remainTime + "秒\n";
    	autoBet.outputGUIMessage(outputStr);
    	strBet = constructBetsData(betData, percent, BetType.CQSSC, opposite);
        
        
        //如果未到封盘时间
        if(getCQSSClocalRemainTime() >= 1) {
        	
        
        	//outputBetsDetails(jsonParam, BetType.CQSSC);
        	
        	boolean result = bet(strBet, BetType.CQSSC);
     	
        	
        	if(!previousCQSSCBetNumber.equals(CQSSCdrawNumber)) {//避免重复计数
	        	if(result == true) {
					successTimes++;
					autoBet.labelTianCaiSuccessBets.setText("成功次数:" + successTimes);
				} else {
					failTimes++;
					autoBet.labelTianCaiFailBets.setText("失败次数:" + failTimes);
				}
				
				autoBet.labelTianCaiTotalBets.setText("下单次数:" + (successTimes + failTimes));
        	}
			
			previousCQSSCBetNumber = CQSSCdrawNumber;
        	previousCQSSCBetResult = result;	
        	
        	return result;
        
        } else {
        	autoBet.outputGUIMessage("下单失败 ,已封盘！\n\n");;
        }
        
        return false;

    }
    
    public boolean doBetBJSC(String[] betData, double percent,boolean opposite, String remainTime) {
    	String strBet = "";
        
        if(previousBJSCBetNumber.equals(BJSCdrawNumber) && previousBJSCBetResult == true) {//之前如果已经下过单并且成功下单就直接返回
        	return false;
        }
        
        if(isEmptyData(betData, BetType.BJSC)) {
    		autoBet.outputGUIMessage("代理无人投注\n\n");
    		return false;
    	}
    	
        String outputStr = "[添彩]下注北京赛车第" + BJSCdrawNumber + "期\n" + "最新数据时间距收盘" + remainTime + "秒\n";
    	autoBet.outputGUIMessage(outputStr);
    	strBet = constructBetsData(betData, percent, BetType.BJSC, opposite);
        
        
        //如果未到封盘时间
        if(getBJSClocalRemainTime() >= 1) {
        
        	//outputBetsDetails(jsonParam, BetType.BJSC);
        	
        	boolean result = bet(strBet, BetType.BJSC);
   
        	
        	if(!previousBJSCBetNumber.equals(BJSCdrawNumber)) {//避免重复计数
	        	if(result == true) {
					successTimes++;
					autoBet.labelTianCaiSuccessBets.setText("成功次数:" + successTimes);
				} else {
					failTimes++;
					autoBet.labelTianCaiFailBets.setText("失败次数:" + failTimes);
				}
				
				autoBet.labelTianCaiTotalBets.setText("下单次数:" + (successTimes + failTimes));
        	}
				
			previousBJSCBetNumber = BJSCdrawNumber;
	        previousBJSCBetResult = result;
			
        	
        	return result;
        
        } else {
        	autoBet.outputGUIMessage("下单失败 ,已封盘！\n\n");;
        }
        
        return false;

    }
     
    
    public boolean parseBetResult(String str){
    	if(str != null) {
    		System.out.println("下单结果：" + str);
    	}
    	
    	if(str != null && str.length()>0){
    		String outputStr = "";
    		JSONObject betResult = null;
    		try{
        		betResult = new JSONObject(str);	
    		}catch(Exception e)
    		{
    			autoBet.outputGUIMessage("迪斯尼下单失败，内部错误\n\n");
    			return false;
    		}
    		int status = betResult.getInt("status");
    		switch(status){
    		case 0:
    			JSONObject account = betResult.getJSONObject("account");
    			double balance = account.getDouble("balance");
    			//int betting = account.getInt("betting");
    			outputStr  = String.format("迪斯尼下单成功！ 账户余额:%f\n\n", balance);
    			autoBet.outputGUIMessage(outputStr);
    			//System.out.printf("下单成功！ 下单金额：%d, 账户余额:%f\n", betting, balance);
    			return true;
    		

    		case 2:
    			//System.out.println("下单失败:已封盘！\n");
    			autoBet.outputGUIMessage("迪斯尼下单失败:已封盘！\n\n");
    			return false;
    		case 3:
    			String message = betResult.getString("message");
    			outputStr  = String.format("迪斯尼下单失败：%s\n\n",message);
    			autoBet.outputGUIMessage(outputStr);
    			return false;
    		
    		}
    	}
    	
    	autoBet.outputGUIMessage("迪斯尼下单失败！\n\n");
    	
    	return false;
    }
    
    public String constructBetsData(String[] data, double percent, BetType betType, boolean opposite){
    	
    	totalAmount = 0;
    	
    	String res = "";
    	JSONObject oddsData = null;
    	
    	
    	try{
    		
    		List<String> parsedGames = new ArrayList<String>();;
    		
	    	JSONArray gamesArray = new JSONArray();
	    	//JSONObject oddsGrabData = null;
	    	
	    	if(betType == BetType.BJSC) {
	    		oddsData = BJSCoddsData;

	    	}
	    	else if(betType == BetType.CQSSC) {
	    		oddsData = CQSSCoddsData;
	    	
	    	}
	    	
	    	if(oddsData == null) {
	    		return "";
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
	        				amount = 2; //添彩每注最低2元

	        			totalAmount += amount;
	        			
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
	        				        			
	        			String selectionTypeName = "";
	        			String outputName = "";
	        			String outputContent = "";
	        			
	        		
	        	    	if(betType == BetType.CQSSC){//重庆时时彩
        	
	        	        	switch(game){
	        	        	case "DX1":
	        	        		selectionTypeName = "BALL_1";
	        	        		contents = contents.equals("D")?"BIG":"SMALL";
	        	        		outputName = "第一球";
	        	        		outputContent = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS1":
	        	        		selectionTypeName = "BALL_1";
	        	        		contents = contents.equals("D")?"ODD":"EVEN";
	        	        		outputName = "第一球";
	        	        		outputContent = contents.equals("D")?"单":"双";
	        	        		break;
	        	        	case "DX2":
	        	        		selectionTypeName = "BALL_2";
	        	        		contents = contents.equals("D")?"BIG":"SMALL";
	        	        		outputName = "第二球";
	        	        		outputContent = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS2":
	        	        		selectionTypeName = "BALL_2";
	        	        		contents = contents.equals("D")?"ODD":"EVEN";
	        	        		outputName = "第二球";
	        	        		outputContent = contents.equals("D")?"单":"双";
	        	        		break;
	        	        	case "DX3":
	        	        		selectionTypeName = "BALL_3";
	        	        		contents = contents.equals("D")?"BIG":"SMALL";
	        	        		outputName = "第三球";
	        	        		outputContent = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS3":
	        	        		selectionTypeName = "BALL_3";
	        	        		contents = contents.equals("D")?"ODD":"EVEN";	
	        	        		outputName = "第三球";
	        	        		outputContent = contents.equals("D")?"单":"双";
	        	        		break;
	        	        	case "DX4":
	        	        		selectionTypeName = "BALL_4";
	        	        		contents = contents.equals("D")?"BIG":"SMALL";
	        	        		outputName = "第四球";
	        	        		outputContent = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS4":
	        	        		selectionTypeName = "BALL_4";
	        	        		contents = contents.equals("D")?"ODD":"EVEN";	
	        	        		outputName = "第四球";
	        	        		outputContent = contents.equals("D")?"单":"双";
	        	        		break;
	        	        	case "DX5":
	        	        		selectionTypeName = "BALL_5";
	        	        		contents = contents.equals("D")?"BIG":"SMALL";
	        	        		outputName = "第五球";
	        	        		outputContent = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS5":
	        	        		selectionTypeName = "BALL_5";
	        	        		contents = contents.equals("D")?"ODD":"EVEN";	
	        	        		outputName = "第五球";
	        	        		outputContent = contents.equals("D")?"单":"双";
	        	        		break;
	        	        	case "ZDX":
	        	        		selectionTypeName = "TOTAL";
	        	        		contents = contents.equals("D")?"BIG":"SMALL";
	        	        		outputName = "总大小";
	        	        		outputContent = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "ZDS":
	        	        		selectionTypeName = "TOTAL";
	        	        		contents = contents.equals("D")?"ODD":"EVEN";	
	        	        		outputName = "总单双";
	        	        		outputContent = contents.equals("D")?"单":"双";
	        	        		break;
	        	        	case "LH":
	        	        		selectionTypeName = "D_T_T";
	        	        		contents = contents.equals("L")?"DRAGON":"TIGER";
	        	        		outputName = "龙虎";
	        	        		outputContent = contents.equals("L")?"龙":"虎";
	        	        		break;
	        	        	}
	        	        	
	        	    	}//北京赛车
	        	    	else if(betType == BetType.BJSC){
	        	    		
	        	        	switch(game){
	        	        	case "DX1":
	        	        		selectionTypeName = "BALL_1";
	        	        		contents = contents.equals("D")?"BIG":"SMALL";
	        	        		outputName = "冠军";
	        	        		outputContent = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS1":
	        	        		selectionTypeName = "BALL_1";
	        	        		contents = contents.equals("D")?"ODD":"EVEN";
	        	        		outputName = "冠军";
	        	        		outputContent = contents.equals("D")?"单":"双";
	        	        		break;
	        	        	case "LH1":
	        	        		selectionTypeName = "BALL_1";
	        	        		contents = contents.equals("L")?"DRAGON":"TIGER";
	        	        		outputName = "冠军";
	        	        		outputContent = contents.equals("L")?"龙":"虎";
	        	        		break;	
	        	        		
	        	        	case "DX2":
	        	        		selectionTypeName = "BALL_2";
	        	        		contents = contents.equals("D")?"BIG":"SMALL";
	        	        		outputName = "亚军";
	        	        		outputContent = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS2":
	        	        		selectionTypeName = "BALL_2";
	        	        		contents = contents.equals("D")?"ODD":"EVEN";
	        	        		outputName = "亚军";
	        	        		outputContent = contents.equals("D")?"单":"双";
	        	        		break;
	        	        	case "LH2":
	        	        		selectionTypeName = "BALL_2";
	        	        		contents = contents.equals("L")?"DRAGON":"TIGER";
	        	        		outputName = "亚军";
	        	        		outputContent = contents.equals("L")?"龙":"虎";
	        	        		break;		
	        	        	case "DX3":
	        	        		selectionTypeName = "BALL_3";
	        	        		contents = contents.equals("D")?"BIG":"SMALL";
	        	        		outputName = "第三名";
	        	        		outputContent = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS3":
	        	        		selectionTypeName = "BALL_3";
	        	        		contents = contents.equals("D")?"ODD":"EVEN";	
	        	        		outputName = "第三名";
	        	        		outputContent = contents.equals("D")?"单":"双";
	        	        		break;
	        	        	case "LH3":
	        	        		selectionTypeName = "BALL_3";
	        	        		contents = contents.equals("L")?"DRAGON":"TIGER";
	        	        		outputName = "第三名";
	        	        		outputContent = contents.equals("L")?"龙":"虎";
	        	        		break;		
	        	        	case "DX4":
	        	        		selectionTypeName = "BALL_4";
	        	        		contents = contents.equals("D")?"BIG":"SMALL";
	        	        		outputName = "第四名";
	        	        		outputContent = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS4":
	        	        		selectionTypeName = "BALL_4";
	        	        		contents = contents.equals("D")?"ODD":"EVEN";
	        	        		outputName = "第四名";
	        	        		outputContent = contents.equals("D")?"单":"双";
	        	        		break;
	        	        	case "LH4":
	        	        		selectionTypeName = "BALL_4";
	        	        		contents = contents.equals("L")?"DRAGON":"TIGER";
	        	        		outputName = "第四名";
	        	        		outputContent = contents.equals("L")?"龙":"虎";
	        	        		break;	
	        	        	case "DX5":
	        	        		selectionTypeName = "BALL_5";
	        	        		contents = contents.equals("D")?"BIG":"SMALL";
	        	        		outputName = "第五名";
	        	        		outputContent = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS5":
	        	        		selectionTypeName = "BALL_5";
	        	        		contents = contents.equals("D")?"ODD":"EVEN";	
	        	        		outputName = "第五名";
	        	        		outputContent = contents.equals("D")?"单":"双";
	        	        		break;
	        	        	case "LH5":
	        	        		selectionTypeName = "BALL_5";
	        	        		contents = contents.equals("L")?"DRAGON":"TIGER";
	        	        		outputName = "第五名";
	        	        		outputContent = contents.equals("L")?"龙":"虎";
	        	        		break;	
	        	        	case "DX6":
	        	        		selectionTypeName = "BALL_6";
	        	        		contents = contents.equals("D")?"BIG":"SMALL";
	        	        		outputName = "第六名";
	        	        		outputContent = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS6":
	        	        		selectionTypeName = "BALL_6";
	        	        		contents = contents.equals("D")?"ODD":"EVEN";	
	        	        		outputName = "第六名";
	        	        		outputContent = contents.equals("D")?"单":"双";
	        	        		break;
	        	        	case "DX7":
	        	        		selectionTypeName = "BALL_7";
	        	        		contents = contents.equals("D")?"BIG":"SMALL";
	        	        		outputName = "第七名";
	        	        		outputContent = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS7":
	        	        		selectionTypeName = "BALL_7";
	        	        		contents = contents.equals("D")?"ODD":"EVEN";	
	        	        		outputName = "第七名";
	        	        		outputContent = contents.equals("D")?"单":"双";
	        	        		break;
	        	        	case "DX8":
	        	        		selectionTypeName = "BALL_8";
	        	        		contents = contents.equals("D")?"BIG":"SMALL";
	        	        		outputName = "第八名";
	        	        		outputContent = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS8":
	        	        		selectionTypeName = "BALL_8";
	        	        		contents = contents.equals("D")?"ODD":"EVEN";
	        	        		outputName = "第八名";
	        	        		outputContent = contents.equals("D")?"单":"双";
	        	        		break;
	        	        	case "DX9":
	        	        		selectionTypeName = "BALL_9";
	        	        		contents = contents.equals("D")?"BIG":"SMALL";
	        	        		outputName = "第九名";
	        	        		outputContent = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS9":
	        	        		selectionTypeName = "BALL_9";
	        	        		contents = contents.equals("D")?"ODD":"EVEN";	
	        	        		outputName = "第九名";
	        	        		outputContent = contents.equals("D")?"单":"双";
	        	        		break;
	        	        	case "DX10":
	        	        		selectionTypeName = "BALL_10";
	        	        		contents = contents.equals("D")?"BIG":"SMALL";
	        	        		outputName = "第十名";
	        	        		outputContent = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS10":
	        	        		selectionTypeName = "BALL_10";
	        	        		contents = contents.equals("D")?"ODD":"EVEN";
	        	        		outputName = "第十名";
	        	        		outputContent = contents.equals("D")?"单":"双";
	        	        		break;

	        	        	}
	        	    	}
   	    	
	        			
	        			res += "[";
	        			res += "\"" + contents + "\",";
	        			res += "\"" + selectionTypeName + "\",";
	        			res += "\"" + amount + "\",";
	        			res += oddsData.getJSONObject(selectionTypeName).getDouble(contents) + "],";
	        			
	        			String out = outputName + "_" + outputContent + ":" + amount + "\n";
	        			autoBet.outputGUIMessage(out);
	        	    	
	        		}
	        		
	        	}
	    	}
	    	
	    	if(res.length() > 1) {
	    		res = "[" + res.substring(0, res.length() - 1) + "]"; 
	    		autoBet.outputGUIMessage("下单总额:" + totalAmount + "\n");
	    	}
	    	
	    	System.out.println("下单数据：------------------------------------" + res);
    	
    	}catch(Exception e){
    		e.printStackTrace();
    		autoBet.outputGUIMessage("构造下单数据错误！\n");
    		return "";
    	}
   	
    	return res;
    }

    
	public String setCookie(CloseableHttpResponse httpResponse)
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
    





    /**以utf-8形式读取*/
    public String doPost(String url,List<NameValuePair> formparams, String cookies, String referUrl) {
        return doPost(url, formparams,"UTF-8", cookies, referUrl);
    }

    public String doPost(String url,List<NameValuePair> formparams,String charset, String cookies, String referUrl) {


     // 创建httppost    
        HttpPost httppost = new HttpPost(url); 
    
        //httppost.addHeader("Cookie", cookies);
        //httppost.addHeader("Accept-Encoding","Accept-Encoding: gzip, deflate, sdch");
        //httppost.addHeader("x-requested-with","XMLHttpRequest");
        httppost.addHeader("Accept-Language","Accept-Language: zh-CN");
        httppost.addHeader("Accept","application/json, text/javascript, */*; q=0.01");
        httppost.addHeader("Accept-Encoding","gzip, deflate");
        httppost.addHeader("Connection","keep-alive");
        if(referUrl != "")
        {
        	httppost.addHeader("Referer", referUrl);
        }
        httppost.addHeader("Cache-Control","max-age=0");
        httppost.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.79 Safari/537.36");    
        
        
        System.out.println("executing request " + httppost.getURI());
        UrlEncodedFormEntity uefEntity;
        try {
            uefEntity = new UrlEncodedFormEntity(formparams, charset);
            httppost.setEntity(uefEntity);
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                // 打印响应状态    
            	setCookie(response);
            	System.out.println(response.getStatusLine().toString());
            	if(response.getStatusLine().toString().indexOf("302 Found") > 0) {
            		String location = response.getFirstHeader("Location").getValue();
            		System.out.println(response.getStatusLine());
            		System.out.println("location:" + location);      		
            		
            		if(location != null && location.length() > 0) {
            			return location;
            		}
            	}else if(response.getStatusLine().toString().indexOf("200 OK") > 0) {
            		HttpEntity entity = response.getEntity(); 
                    String res = EntityUtils.toString(entity);		
            		
            		if(res != null && res.length() > 0) {
            			return res;
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
     
        } 
        return "";
    }
    
    
    public String doGet(String url, String cookies, String referUrl) {
    	
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
            
            String statusLine = response.getStatusLine().toString();   
            if(statusLine.indexOf("200 OK") == -1) {
         	   System.out.println(statusLine); 
            }
            
            try{
            	setCookie(response);  	
            	//System.out.println("设置cookie:" + strCookies);
            	
            	if(response.getStatusLine().toString().indexOf("302 Found") > 0) {
             	   return response.getFirstHeader("Location").getValue();
                }
                HttpEntity entity = response.getEntity(); 
                
                String res = EntityUtils.toString(entity);
                
                if(res != null && res.length() > 0 ){     
                	System.out.println(res);
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
        
        return "";
    }
    
    public boolean isEmptyData(String[] data, BetType betType) {
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
	    	autoBet.outputGUIMessage("isEmptyData()构造下单数据错误！\n");
	    	return true;
	    }
    }

    
    
    public boolean bet(String betData, BetType betType) {

    	String response = "";
        
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("command", "BET"));
    	params.add(new BasicNameValuePair("sessionId", sessionID));
    	params.add(new BasicNameValuePair("bets", betData));
    	if(betType == BetType.CQSSC) {
    		params.add(new BasicNameValuePair("gameType", "SSC"));
    	}
    	else {
    		params.add(new BasicNameValuePair("gameType", "BJC"));
    	}
    	params.add(new BasicNameValuePair("hasPlayerInfo", "true"));
        response = doPost(HOST + "/lotteryweb/WebClientAgent", params, "", HOST + "/lotteryweb/Login");
        if(response == "") {
        	System.out.println("下单失败  response == \"\"");
        	String out = "下单失败！\n\n";	
        	autoBet.outputGUIMessage(out);
        	return false;
        }
        
        System.out.println(response.toString());
        
        JSONObject joResult = null;
		try{
			joResult = new JSONObject(response.toString());
			
			if(joResult.getInt("returnCode") == 0) {
				double balance = joResult.getDouble("balance");
				//System.out.println(" 下单成功   余额:" + balance);
				String out = "下单成功， 账户余额:" + balance + "\n\n";			
				autoBet.outputGUIMessage(out);
				
				return true;
				
			}
			else {
				//System.out.println("下单失败, message:" + joResult.getString("returnMsg"));
				String out = "下单失败" + joResult.getString("returnMsg") + "\n\n";			
				autoBet.outputGUIMessage(out);
				return false;
			}
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
        
    }
    
    
    public String getPicNum(String picUri, String refurl) {
    	try {
	   	    HttpGet httpget = new HttpGet(picUri);
		   	 httpget.addHeader("Accept-Encoding","Accept-Encoding: gzip, deflate, sdch");
	         httpget.addHeader("Accept-Language","Accept-Language: zh-CN,zh;q=0.8");
	         httpget.addHeader("Connection","keep-alive");
	         httpget.addHeader("Upgrade-Insecure-Requests","1");
	         httpget.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
         //
         
         if(refurl != "") {
         	httpget.addHeader("Referer", refurl);
         }
         
         httpget.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.79 Safari/537.36");           
         System.out.println("executing request " + httpget.getURI()); 
       
	        // 执行get请求.    
        
	        CloseableHttpResponse response = httpclient.execute(httpget, clientContext); 
       	 try {
       		    setCookie(response);
                // 打印响应状态    
                System.out.println(response.getStatusLine()); 
                System.out.println("------------------------------------");
                File storeFile = new File("tcyzm.png");   //图片保存到当前位置
                FileOutputStream output = new FileOutputStream(storeFile);  
                //得到网络资源的字节数组,并写入文件  
                byte [] a = EntityUtils.toByteArray(response.getEntity());
                output.write(a);  
                output.close();  
                
         
                InputStream ins = null;
        		 String[] cmd = new String[]{ConfigReader.getTessPath() + "\\tesseract", "tcyzm.png", "result", "-l", "eng"};

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
    
}


