import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Date;
import java.util.Stack;

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
import org.apache.http.entity.StringEntity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

import org.json.*;

import java.util.Vector;
import java.util.Comparator;




enum INDEXTYPE{
	Date,
	DrawNumber
	
}




public class dsnHttp {

    static CloseableHttpClient httpclient = null;
    static RequestConfig requestConfig = null;
    static HttpClientContext clientContext = null;
    
    
    static {
        requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
        requestConfig = RequestConfig.copy(requestConfig).setRedirectsEnabled(false).build();//禁止重定向 ， 以便获取cookieb18
        //requestConfig = RequestConfig.copy(requestConfig).setConnectTimeout(autoBet.timeOut).setConnectionRequestTimeout(autoBet.timeOut).setSocketTimeout(autoBet.timeOut).build();//设置超时
        httpclient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
   }
    
    
    //北京赛车投注详情窗口
    static Vector<String> unknowStatBJSCDraw = new Vector<String>();    
    static Vector<String> unCalcProfitBJSCDraw = new Vector<String>();    

    static DSNDataDetailsWindow BJSCdetalsDataWindow = new DSNDataDetailsWindow();    
    
    
    static int defaultTimeout = 3000;
    
    
    static int BJSCbishu = 0;    
    static int BJSConeBetAmount = 0;    

    
    static int BJSCzongqishu = 0;
    static int BJSCzongshibai = 0;
    static int BJSCzongyichang = 0;
    
    static int BJSCjinriqishu = 0;
    static int BJSCjinrishibai = 0;
    static int BJSCjinriyichang = 0;
    
    
    //重庆时时彩投注详情窗口
    static Vector<String> unknowStatCQSSCDraw = new Vector<String>();    
    static Vector<String> unCalcProfitCQSSCDraw = new Vector<String>();    

    static DSNDataDetailsWindow CQSSCdetalsDataWindow = new DSNDataDetailsWindow();    
    static int CQSSCbishu = 0;    
    static int CQSSConeBetAmount = 0;    

    
    static int CQSSCzongqishu = 0;
    static int CQSSCzongshibai = 0;
    static int CQSSCzongyichang = 0;
    
    static int CQSSCjinriqishu = 0;
    static int CQSSCjinrishibai = 0;
    static int CQSSCjinriyichang = 0;    

    
    
    //static 
    
    //优化线路选择
    static Vector<Object[]> lines;
    
    static Vector<Long> lastTenRequestTime = new Vector<Long>();
    static long avgRequestTime = 0;    
    static boolean bcalcRequestTime = true;
    static boolean bneedChangeLine = false;
    
    
    static long lastChangeLineTime = 0;
    
    
    //这个变量用来存储
    static String strCookies = "";
    static String cookieCfduid = "";
    static String cookieb18 = "";
    static String cookie2ae = "";
    static String cookiedefaultLT = "";
    static String cookiesb18 = "";
    static String location = "";
    static long   queryParam = 0;
    
    static long time = 0;
      
    static String previousCQSSCdrawNumber = "";
    static String previousBJSCdrawNumber = "";
    static String CQSSCdrawNumber = "";
    static String BJSCdrawNumber = "";
    static String previousCQSSCBetNumber = "";
    static String previousBJSCBetNumber = "";
    
    static boolean previousCQSSCBetResult = false;
    static boolean previousBJSCBetResult = false;
    
    
    static String ADDRESS = "";
    static String ACCOUNT = "";
    static String PASSWORD = "";
    
    static String CQSSCoddsData = null;
    static String BJSCoddsData = null;
    
    static long timeDValue = 0;  //网站时间和电脑时间的差值  网页时间 - 当前时间
    static long CQSSCcloseTime = 0;    //封盘时间
    static long BJSCcloseTime = 0;
    
    static int failTimes = 0;    //下单失败次数
    static int successTimes = 0; //下单成功次数
    
    
    static String[] BJSCBetData = null;
    static String[] CQSSCBetData = null;
    
    static int BJSCBetDataErrorValue = 0;
    static int CQSSCBetDataErrorValue = 0;
    
    static int totalBJSCBetDataErrorValue = 0;
    static int totalCQSSCBetDataErrorValue = 0;
    
    
    //计算封盘与实际下注差值
    static int CQSSCbetTotalAmount = 0;
    static int BJSCbetTotalAmount = 0;
    
    
    public dsnHttp(){

    }
    
    
    public static void clearBJSCdetalsData(){
    	if(unCalcProfitBJSCDraw.size() != 0){
    		unCalcProfitBJSCDraw.clear();
    	}
    	
    	if(unknowStatBJSCDraw.size() != 0){
    		BJSCjinrishibai += unknowStatBJSCDraw.size();
    		unknowStatBJSCDraw.clear();
    	}

    	BJSCzongqishu += BJSCjinriqishu;
    	BJSCzongshibai += BJSCjinrishibai;
    	
    	totalBJSCBetDataErrorValue += BJSCBetDataErrorValue;
    	
    	BJSCBetDataErrorValue = 0;
    	
    	BJSCjinriqishu = 0;
    	BJSCjinrishibai = 0;
    	BJSCjinriyichang = 0;

    }
    
    
    public static void clearCQSSCdetalsData(){
    	if(unCalcProfitCQSSCDraw.size() != 0){
    		unCalcProfitCQSSCDraw.clear();
    	}
    	
    	if(unknowStatCQSSCDraw.size() != 0){
    		CQSSCjinrishibai += unknowStatCQSSCDraw.size();
    		unknowStatCQSSCDraw.clear();
    	}

    	CQSSCzongqishu += CQSSCjinriqishu;
    	CQSSCzongshibai += CQSSCjinrishibai;
    	
    	totalCQSSCBetDataErrorValue += CQSSCBetDataErrorValue;
    	
    	CQSSCBetDataErrorValue = 0;
    	
    	CQSSCjinriqishu = 0;
    	CQSSCjinrishibai = 0;
    	CQSSCjinriyichang = 0;

    }
    
    
    
    
    public static void updateBJSCBalance(String str){
    	BJSCdetalsDataWindow.updateTextFieldyue(str);
    }
    
    public static void updateCQSSCBalance(String str){
    	CQSSCdetalsDataWindow.updateTextFieldyue(str);
    }
    
    
    public static String getBalance(){
    	
    	String balanceStr = "---";
    	
    	try{
        	String balanceURI = ADDRESS + "/member/index";
        	
        	String res = doGet(balanceURI, "", "");
        	
        	
        	
        	if(res == null){
        		res = doGet(balanceURI, "", "");
        	}
        	
        	
        	if(res != null){
        		int posStart = res.indexOf("balance");
        		int posEnd = res.indexOf("<", posStart);
        		
        		if(posEnd > posStart && posStart >=0){
        			balanceStr = res.substring(posStart + 9, posEnd);
        		}

        		
        		
        		if(Common.isNum(balanceStr)){    			    			    			
        			balanceStr = String.format("%.1f", Double.parseDouble(balanceStr));
        		}
        	}
    		
    	}catch(Exception e){
    		e.printStackTrace();
    		return balanceStr;
    	}

    	
    	
    	return balanceStr;
    	
    }
    
    
    
    
    public static Vector<String> getUnknowStatBJSCDraw(){
    	return unknowStatBJSCDraw;
    }
    
    public static Vector<String> getUnCalcProfitBJSCDraw(){
    	return unCalcProfitBJSCDraw;
    }
    
    public static void updateBJSCWindowdetailsData(String drawNumber, int index, String value){
    	BJSCdetalsDataWindow.updateRowItem(drawNumber, index, value);
    }
    
    public static void updateUnCalcBJSCDraw(Vector<String> calcedDraw){
    	for(int i =0; i < calcedDraw.size(); i++){
    		unCalcProfitBJSCDraw.removeElement(calcedDraw.elementAt(i));
    		unknowStatBJSCDraw.removeElement(calcedDraw.elementAt(i));
    	}  
    	
    	
    	int currentDraw = Integer.parseInt(BJSCdrawNumber);
    	
    	for(int j =0; j < unCalcProfitBJSCDraw.size(); j++){
    		int idrawNumber = Integer.parseInt(unCalcProfitBJSCDraw.elementAt(j));
    		if((currentDraw - idrawNumber) >= 4){
    			unCalcProfitBJSCDraw.removeElement(unCalcProfitBJSCDraw.elementAt(j));
    		}
    	}
    	
    	
    	int yichangshu1 = unknowStatBJSCDraw.size();
    	
    	for(int j =0; j < unknowStatBJSCDraw.size(); j++){
    		int idrawNumber = Integer.parseInt(unknowStatBJSCDraw.elementAt(j));
    		if((currentDraw - idrawNumber) >= 4){
    			updateBJSCWindowdetailsData(unknowStatBJSCDraw.elementAt(j), TYPEINDEX.STATC.ordinal(), "1");
    			unknowStatBJSCDraw.removeElement(unknowStatBJSCDraw.elementAt(j));
    			
    			
    		}
    	}
    	
    	int yichangshu2 = unknowStatBJSCDraw.size();
    	
    	BJSCjinriyichang = unknowStatBJSCDraw.size();
    	
    	BJSCjinrishibai += yichangshu1 - yichangshu2;
    	
    	
    	
    	
    	
    	BJSCdetalsDataWindow.updateTextFieldjinrishibai(Integer.toString(BJSCjinrishibai));
    	BJSCdetalsDataWindow.updateTextFieldjinriyichang(Integer.toString(BJSCjinriyichang));
    	
    	
    	
    	BJSCdetalsDataWindow.updateTextFieldzongqishu(Integer.toString(BJSCzongqishu + BJSCjinriqishu));
    	
    	BJSCdetalsDataWindow.updateTextFieldzongshibai(Integer.toString(BJSCzongshibai + BJSCjinrishibai));
    	
    	
    	
    	for(int j =0; j < unCalcProfitBJSCDraw.size(); j++){
    		int idrawNumber = Integer.parseInt(unCalcProfitBJSCDraw.elementAt(j));
    		System.out.printf("未获得盈利期数：%d", idrawNumber);
    	}
    	
    	
    }
    
    
    public static void showBJSCDeatilsTable(){
    	BJSCdetalsDataWindow.setVisible(true);
    }
    
    
    
    
    public static Vector<String> getUnknowStatCQSSCDraw(){
    	return unknowStatCQSSCDraw;
    }
    
    public static Vector<String> getUnCalcProfitCQSSCDraw(){
    	return unCalcProfitCQSSCDraw;
    }
    
    public static void updateCQSSCWindowdetailsData(String drawNumber, int index, String value){
    	CQSSCdetalsDataWindow.updateRowItem(drawNumber, index, value);
    }
    
    public static void updateUnCalcCQSSCDraw(Vector<String> calcedDraw){
    	for(int i =0; i < calcedDraw.size(); i++){
    		unCalcProfitCQSSCDraw.removeElement(calcedDraw.elementAt(i));
    		unknowStatCQSSCDraw.removeElement(calcedDraw.elementAt(i));
    	}  
    	
    	
    	long currentDraw = Long.parseLong(CQSSCdrawNumber);
    	
    	for(int j =0; j < unCalcProfitCQSSCDraw.size(); j++){
    		long idrawNumber = Long.parseLong(unCalcProfitCQSSCDraw.elementAt(j));
    		if((currentDraw - idrawNumber) >= 4){
    			unCalcProfitCQSSCDraw.removeElement(unCalcProfitCQSSCDraw.elementAt(j));
    		}
    	}
    	
    	
    	int yichangshu1 = unknowStatCQSSCDraw.size();
    	
    	for(int j =0; j < unknowStatCQSSCDraw.size(); j++){
    		long idrawNumber = Long.parseLong(unknowStatCQSSCDraw.elementAt(j));
    		if((currentDraw - idrawNumber) >= 4){
    			updateCQSSCWindowdetailsData(unknowStatCQSSCDraw.elementAt(j), TYPEINDEX.STATC.ordinal(), "1");
    			unknowStatCQSSCDraw.removeElement(unknowStatCQSSCDraw.elementAt(j));
    			
    			
    		}
    	}
    	
    	int yichangshu2 = unknowStatCQSSCDraw.size();
    	
    	CQSSCjinriyichang = unknowStatCQSSCDraw.size();
    	
    	CQSSCjinrishibai += yichangshu1 - yichangshu2;
    	
    	
    	
    	
    	
    	CQSSCdetalsDataWindow.updateTextFieldjinrishibai(Integer.toString(CQSSCjinrishibai));
    	CQSSCdetalsDataWindow.updateTextFieldjinriyichang(Integer.toString(CQSSCjinriyichang));
    	
    	
    	
    	CQSSCdetalsDataWindow.updateTextFieldzongqishu(Integer.toString(CQSSCzongqishu + CQSSCjinriqishu));
    	
    	CQSSCdetalsDataWindow.updateTextFieldzongshibai(Integer.toString(CQSSCzongshibai + CQSSCjinrishibai));
    	
    	
    }
    
    
    public static void showCQSSCDeatilsTable(){
    	CQSSCdetalsDataWindow.setVisible(true);
    }
    
    
    
    
    
    
    
    public static void setBJSCBetData(String[] betData){
    	BJSCBetData = betData;
    }
    
    public static void setCQSSCBetData(String[] betData){
    	CQSSCBetData = betData;
    }
    
    
    public static boolean isBetSuccess(String drawNumber){
    	boolean betSuccessfully = false;
    	
    	String lastBetsURI = ADDRESS + "/member/bets";
    	
    	String res = doGet(lastBetsURI, "", "");
    	
    	if(res == null){
    		res = doGet(lastBetsURI, "", "");
    	}
    	
    	
    	
    	//System.out.println(res);
    	
    	try{
        	if(res != null && res.contains(drawNumber))
        		betSuccessfully = true;
    	}catch(Exception e){
    		
    	}
    	
    	if(res == null){
    		System.out.println("获取未结明细出现超时情况！");
    	}
    	
    	
    	

    	
    	return betSuccessfully;
    }
    
    public static String[] getBetProfit(String drawNumber){
    	
    	
    	String[] result = {"none", "0"};
    	
    	try{
        	boolean hasBetprofit = false;
        	
        	String lastBetsURI = ADDRESS + "/member/bets?settled=true";
        	
        	String res = doGet(lastBetsURI, "", "");
        	
        	double totalProfit = 0;
        	
        	int bishu = 0;
        	
        	
        	
        	if(res == null){
        		res = doGet(lastBetsURI, "", "");
        	}
        	
        	if(res != null){
        		
        		if(res.contains("暂无数据")){
        			return result;
        		}
        		
        		
        		int posStart = res.indexOf("page_count"); 
        		
        		posStart = res.indexOf(" ", posStart); 
        		
        		int posEnd = res.indexOf(" ", posStart + 1);
        		
        		String pageCountstr = res.substring(posStart + 1, posEnd);
        		
        		int pageCount = 1;
        		
        		if(Common.isNum(pageCountstr)){
        			
        			pageCount = Integer.parseInt(pageCountstr);
        		}
        		
        		pageCount = pageCount >10?10:pageCount;//只查看前十页
        		
        		String number = "";
        		
        		for(int i = 0; i< pageCount; i++){
        			
        			if(res == null)
        				continue;
        			
        			posStart = res.indexOf("draw_number");
            		
            		while(posStart != -1){
            			
            			
            			
            			posStart = res.indexOf(" ", posStart);
            			posEnd = res.indexOf(" ", posStart+1);
            			
            			number = res.substring(posStart + 1, posEnd);
            			
            			if(drawNumber.equals(number)){
            				
            				posStart = res.indexOf("result color\">", posEnd);
            				posEnd = res.indexOf("<", posStart);
            				
            				String profitStr = res.substring(posStart + 14, posEnd);
            				
            				if(Common.isNum(profitStr)){
            					bishu++;
            					totalProfit += Double.parseDouble(profitStr);
            				}
            				
            				hasBetprofit = true;
            			}
            			
            			if(!drawNumber.equals(number) && totalProfit != 0){
            				break;
            			}
            			
            			posStart = res.indexOf("draw_number", posEnd);
            		}
            		
            		
            		if(!drawNumber.equals(number) && hasBetprofit == true){
        				break;
            		}
            		
            		
            		lastBetsURI = ADDRESS + "/member/bets?settled=true";
            		
            		lastBetsURI += "&page=" + Integer.toString(i+2);
                	
                	res = doGet(lastBetsURI, "", "");
                	
                	
                	if(res == null){
                		res = doGet(lastBetsURI, "", "");
                	}
            		
            		
        		}
        		

        	}
        	
        	
        	
        	if(hasBetprofit == true){
        		//return Double.toString(totalProfit);
        		result[0] = String.format("%.1f", totalProfit);
        		result[1] = Integer.toString(bishu);
        	}
    		
    		
    	}catch(Exception e){
    		e.printStackTrace();
    		return result;
    	}
    	
    	

    	
    	return result;

    }
    
    
    
    
    
    
    public static int calcBetDataErrorValue(String[] closedData, BetType betType){
    	
    	
    	int totalAmount = 0;
    	
    	String res = "";
    	
    	String oddsData = "";
    	
    	String[] betData = null;
    	
    	
    	
    	try{
    		
    		List<String> parsedGames = new ArrayList<String>();
    		
	    	JSONArray gamesArray = new JSONArray();
	    	JSONObject oddsGrabData = null;
	    	
	    	if(betType == BetType.BJSC){
	    		
	    		betData = BJSCBetData;
	    		
	    		if(BJSCoddsData == null){
	    			getBJSCoddsData();
	    		}
	    		
	    		oddsData = BJSCoddsData;
	    	}
	    	else if(betType == BetType.CQSSC){
	    		
	    		betData = CQSSCBetData;
	    		
	    		if(CQSSCoddsData == null){
	    			getCQSSCoddsData();
	    		}
	    		
	    		oddsData = CQSSCoddsData;
	    	}
	    	
	    	oddsGrabData = new JSONObject(oddsData);
	    	
	    	for(int i = 0; i < closedData.length; i++){
    		
    		
            	JSONArray colsedLMData = new JSONArray(closedData[i]);        	
            	JSONArray closedGamesData = colsedLMData.getJSONArray(0);
            	
            	
            	JSONArray betLMData = new JSONArray(betData[i]);        	
            	JSONArray betGamesData = betLMData.getJSONArray(0);
            	
            	

        	
	        	for(int j = 0; j < closedGamesData.length(); j++){
	        		JSONObject closedGameData = closedGamesData.getJSONObject(j);
	        		
	    			String game = closedGameData.getString("k");
	    			
	    			String contents = closedGameData.getString("i");
	    			int closedAmount = closedGameData.getInt("a");
	    			String oddsKey = game + "_" + contents;
	    			
	    			if(oddsData.contains(oddsKey) == false)
	    				continue;
	    			
	    			double odds = oddsGrabData.getDouble(oddsKey);
	    			
	    			if(odds > 2.0)//只计算投注的差值
	    				continue;
	    			
	    			//剔除北京赛车冠亚军 和 两面
	    			if(game.indexOf("GDX") != -1 || game.indexOf("GDS") != -1)
	    				continue;
	    			
	    			
	    			boolean findSameBet = false;
	    			
	    			for(int k = 0; k < betGamesData.length(); k++){
	    				
	    				JSONObject betGameData = betGamesData.getJSONObject(k);
	    				String betGame = betGameData.getString("k");
	    				
	    				String betContents = betGameData.getString("i");
	    				
	    				if(betGame.equals(game)&&betContents.equals(contents)){
	    					int betAmount = betGameData.getInt("a");
	    					
	    					int errorValue = closedAmount - betAmount;
	    					
	    					
	    					//String outstr = String.format("%s:%d\n", oddsKey, errorValue);
	    					
	    					//System.out.println(outstr);
	    					
	    					totalAmount += errorValue;
	    					findSameBet = true;
	    					break;
	    					
	    				}
	    				
	    			}
	    			
	    			if(findSameBet == false){
	    				totalAmount += closedAmount;
	    			}
	    			
	    			findSameBet = false;
	    			
	        	}
	    	
	    	}
    	
    	}catch(Exception e){
    		
    		
    		
    		System.out.println("ccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccc");
    		
    	}
    	
    	
    	if(betType == BetType.BJSC){
    		
    		totalAmount -= BJSCbetTotalAmount;
    		
    		BJSCBetDataErrorValue += totalAmount;
    		
			String outputStr  = String.format("北京赛车第%s期,封盘数据与实际下单数据差值为:%d\n总差值为:%d\n\n",BJSCdrawNumber, totalAmount, BJSCBetDataErrorValue);
			
			
			
			autoBet.outputGUIMessage(outputStr);
			
			BJSCdetalsDataWindow.updateTextFieldjinrichazhi(Integer.toString(BJSCBetDataErrorValue));
			BJSCdetalsDataWindow.updateTextFieldzongchazhi(Integer.toString(totalBJSCBetDataErrorValue + BJSCBetDataErrorValue));
			
    		
    	}
    	
    	if(betType == BetType.CQSSC){
    		
    		totalAmount -= CQSSCbetTotalAmount;
    		
    		CQSSCBetDataErrorValue += totalAmount;
    		
			String outputStr  = String.format("重庆时时彩第%s期,封盘数据与实际下单数据差值为:%d\n总差值为:%d\n\n",CQSSCdrawNumber, totalAmount, CQSSCBetDataErrorValue);
			autoBet.outputGUIMessage(outputStr);
			
			CQSSCdetalsDataWindow.updateTextFieldjinrichazhi(Integer.toString(CQSSCBetDataErrorValue));
			CQSSCdetalsDataWindow.updateTextFieldzongchazhi(Integer.toString(totalCQSSCBetDataErrorValue + CQSSCBetDataErrorValue));
			
    	}
    	
    	return totalAmount;
    	
    }
    
    
    public static String getCQSSCoddsData(){
    	String url = ADDRESS + "/member/odds?lottery=CQSSC&games=DX1%2CDX2%2CDX3%2CDX4%2CDX5%2CDS1%2CDS2%2CDS3%2CDS4%2CDS5%2CZDX%2CZDS%2CLH%2CTS1%2CTS2%2CTS3%2CB1%2CB2%2CB3%2CB4%2CB5&_=";
    	url += Long.toString(System.currentTimeMillis());
    	
    	CQSSCoddsData = doGet(url, "", "");
    	
    	if(CQSSCoddsData == null){
    		CQSSCoddsData = doGet(url, "", "");
    	}
    	
    	return  CQSSCoddsData; 	
   	
    }
    
    public static String getBJSCoddsData(){
    	String url = ADDRESS + "/member/odds?lottery=BJPK10&games=DX1%2CDX2%2CDX3%2CDX4%2CDX5%2CDX6%2CDX7%2CDX8%2CDX9%2CDX10%2CDS1%2CDS2%2CDS3%2CDS4%2CDS5%2CDS6%2CDS7%2CDS8%2CDS9%2CDS10%2CGDX%2CGDS%2CLH1%2CLH2%2CLH3%2CLH4%2CLH5&_=";    	
    	url += Long.toString(System.currentTimeMillis());
    	
    	BJSCoddsData = doGet(url, "", "");
    	
    	if(BJSCoddsData == null){
    		BJSCoddsData = doGet(url, "", "");
    	}
 
    	return BJSCoddsData;
    }
    
    public static String getCQSSCdrawNumber(){
    	return CQSSCdrawNumber;
    }
    
    public static String getBJSCdrawNumber(){
    	return BJSCdrawNumber;
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
    	
    	
    	BJSCdetalsDataWindow.setTitle("投注北京赛车详情");
    	CQSSCdetalsDataWindow.setTitle("投注重庆时时彩详情");
    	
    	String[] addressArray = ConfigReader.getBetAddressArray();
    	

    	
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
    			
    			
    			
        		if(loginToDsn()) {
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
    	

    	System.out.println("【迪斯尼会员】线路快慢排序:");
    	
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
            	
            	
            	System.out.printf("[迪斯尼会员]平均请求时间:%d\n", avgRequestTime);
            	
            	
            	long currentTime = System.currentTimeMillis();
            	
            	long passTime = currentTime - lastChangeLineTime;
            	
            	if(avgRequestTime >= 500 && passTime >= 90*1000){
            		setisNeedChangeLine(true);
            		lastChangeLineTime = currentTime;
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
    		if(loginToDsn()) {
    			res = true;
    			break;
    		}
    	}
        	
    	String currentAddress = ADDRESS;

    	if(res == false){
    		
        	String[] addressArray = ConfigReader.getBetAddressArray();
        	
        	
        	
        	for(int k = 0; k < addressArray.length; k++){
        		
        		
        		if(currentAddress.equals(addressArray[k]))
        				continue;
        		
        		setLoginAddress(addressArray[k]);
        		
        		for(int i = 0; i < 8; i++) {
            		if(loginToDsn()) {
            			res = true;
            			
            			ConfigWriter.updateDSNMemberAddress(ADDRESS);//更新到现在登得上的网址
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
        		if(loginToDsn()) {
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
    
    
    
    public static boolean connFailLogin() {
    	
    	boolean res = false;
    	
    	setIscalcRequestTime(false);
    	
    	autoBet.outputGUIMessage("会员" + ACCOUNT + "连接失败,正在重新登录....\n");

    	while(res == false){
    		

        	for(int k = 0; k < lines.size(); k++){

        		
        		setLoginAddress((String)lines.elementAt(k)[0]);
        		
        		for(int i = 0; i < 10; i++) {
            		if(loginToDsn()) {
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

    	autoBet.outputGUIMessage("会员" + ACCOUNT + "重新登录成功\n");
    	
    	return res;
    	
    }
    
    
    public static boolean loginToDsn(){
  	
    	String loginURI = "";
    	loginURI = "/login?dcwpf0dc4=" + System.currentTimeMillis();
    	
    	loginURI = ADDRESS + loginURI;
    	
    	//String code = doGetLoginPage(loginURI);
    	String loginPage = doGet(loginURI, cookieCfduid, ADDRESS+"/login");
    	
    	//System.out.println(loginPage);
    	
        if(loginPage != null) {
        	if(strCookies.indexOf("__cfduid") != -1) {
        		cookieCfduid = strCookies;
        	}
        	int posStart = loginPage.indexOf("img src=") + 9;
        	if(posStart >= 0) {
        		int posEnd = loginPage.indexOf('"', posStart);
        		String rmNum = getPicNum(ADDRESS + "/" + loginPage.substring(posStart, posEnd));//get 验证码
        		
        		/*for(int k = 0; k < 10; k++){
        			if(Common.isNum(rmNum))
        				break;
        			rmNum = getPicNum(ADDRESS + "/" + loginPage.substring(posStart, posEnd));//get 验证码
        		}*/
        		
        		System.out.println("验证码");
        		System.out.println(rmNum);
        		if(!Common.isNum(rmNum)) {
        			return false;
        		}
    	

		        String type = "1";  //remove hardcode later
		        String account = ACCOUNT;
		        String password = PASSWORD;
		        
		
		        
		        List<NameValuePair> params = new ArrayList<NameValuePair>();
		        params.add(new BasicNameValuePair("type", type));
		        params.add(new BasicNameValuePair("account", account));
		        params.add(new BasicNameValuePair("password", password));
		        params.add(new BasicNameValuePair("code", rmNum));
		    	
		        String location = doPost(loginURI, params, "");
		        
		        System.out.println("location: " + location); 
		        
				if(location != null && location.indexOf("agreement?_") > 0) {
					
					
        			if(location.indexOf("http:") < 0) {
        				location = ADDRESS + "/" + location;
        			}
        			

					if(doGet(location, cookieCfduid, "") != null){
						if(strCookies.indexOf("b1845c0da5f1") != -1) {
							cookieb18 = strCookies; 
							System.out.println("cookies: " + cookieCfduid + cookieb18);
			        	}
						return true;
					}
				}
        	}
        
        }

    	return false;
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
    
    
    //开盘时间为9点到24点
    public static boolean  isInBJSCBetTime(long time){
        Date date = new Date(time);
        int currentHour = date.getHours();
        int currentMinutes = date.getMinutes();
        int currentSeconds = date.getSeconds();
        
        /*if(currentHour >=9 && (currentHour * 60 + currentMinutes <= 23 * 60 + 57)){
        	return true;
        }*/
        
        //两分钟分钟的缓冲
        if((currentHour *60 + currentMinutes > 9*60 + 1) && (currentHour * 60 + currentMinutes <= 23 * 60 + 57)){
        	return true;
        }
        
        
        return false;
    }
    
    

    
    
  
    //北京赛车开盘时间为10点到01:55
    public static boolean isInCQSSCBetTime(long time){
        Date date = new Date(time);
        int currentHour = date.getHours();
        int currentMinutes = date.getMinutes();
        int currentSeconds = date.getSeconds();

        //两分钟缓冲
        if( (currentHour*60 + currentMinutes < 10*60 +1) && (currentHour * 60 + currentMinutes > 1 * 60 + 55))
           return false;
        
        return true;
    }

    
    
    public static long getCQSSCRemainTime(){
        //get period
    	String response = "";
    	String host = ADDRESS;
    		
        String getTimeUrl = host + "/time?&_=";
        getTimeUrl += Long.toString(System.currentTimeMillis());
        
        long startTime = System.currentTimeMillis();
        
        response = doGet(getTimeUrl, "", ADDRESS + "/member/load?lottery=CQSSC&page=lm");
        
        if(response == null){//再拿一次
        	startTime = System.currentTimeMillis();
        	response = doGet(getTimeUrl, "", ADDRESS + "/member/load?lottery=CQSSC&page=lm");
        }
        
        long endTime = System.currentTimeMillis();
        
        long requestTime = endTime - startTime;
        
        requestTime = requestTime /2;
        
        
        if(response != null && Common.isNum(response))
        {
        	time = Long.parseLong(response);
        	timeDValue = time + requestTime - System.currentTimeMillis();
        }
        else{
        	time = System.currentTimeMillis();
        }
    	
        if(!isInCQSSCBetTime(time)){
        	return -1;
        }
        
        
        String getPeriodUrl = host + "/member/period?lottery=CQSSC&_=";
        getPeriodUrl += Long.toString(System.currentTimeMillis());

        //System.out.println(cookieb18 + "defaultLT=CQSSC;" + cookieCfduid);
        response = doGet(getPeriodUrl, "", ADDRESS + "/member/load?lottery=CQSSC&page=lm");
        
        if(response == null){
        	response = doGet(getPeriodUrl, "", ADDRESS + "/member/load?lottery=CQSSC&page=lm");
        }
        
        if(response == null)
        {
        	
        	System.out.println("get period failed");
        	return System.currentTimeMillis();
        }
        
        
        
        System.out.println("preiod:");
        System.out.println(response);
        
        try{
            JSONObject periodJson = new JSONObject(response);
            CQSSCcloseTime = periodJson.getLong("closeTime");
            if(!CQSSCdrawNumber.equals(periodJson.getString("drawNumber"))) {//新的一期
            	previousCQSSCdrawNumber = CQSSCdrawNumber;
            	CQSSCdrawNumber = periodJson.getString("drawNumber");
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
					
					
					failTimes += dNum;
					
					CQSSCjinrishibai += dNum;					
			        CQSSCjinriqishu += dNum;
			        
			        CQSSCdetalsDataWindow.updateTextFieldjinriqishu(Integer.toString(CQSSCjinriqishu));
			        CQSSCdetalsDataWindow.updateTextFieldjinrishibai(Integer.toString(CQSSCjinrishibai));
			        
			        CQSSCdetalsDataWindow.updateTextFieldzongqishu(Integer.toString(CQSSCzongqishu + CQSSCjinriqishu));
			        CQSSCdetalsDataWindow.updateTextFieldzongshibai(Integer.toString(CQSSCzongshibai + CQSSCjinrishibai));
					
					
					
/*					autoBet.labelFailBets.setText("失败次数:" + failTimes);
					autoBet.labelTotalBets.setText("下单次数:" + (successTimes + failTimes));*/
					System.out.println("漏投" + dNum + "次, 期数：" + CQSSCdrawNumber + "上次下单期数：" + previousCQSSCBetNumber);
					
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm");//设置日期格式
					long missDrawNumber = Long.parseLong(previousCQSSCBetNumber) + 1;
					for(int i = 0; i < dNum; i++){
						String missDrawNmberstr = Long.toString(missDrawNumber + i);						
		    			CQSSCdetalsDataWindow.addData(df.format(new Date()), missDrawNmberstr, 3, "---", "---"); 
					}
					
					
					previousCQSSCBetNumber = previousCQSSCdrawNumber;
					
					
			    }
            }
        }
        catch(Exception e){
        	autoBet.outputGUIMessage("获取迪斯尼时间错误！");
        	System.out.println("getCQSSCRemainTime()获取时间异常" + response);
        	return System.currentTimeMillis();
        }
       
        
        long remainTime = CQSSCcloseTime - (System.currentTimeMillis() + timeDValue); //用差值计算  防止两次请求期间间隔过长
        
    	return remainTime;
    }
    
    
    
    public static long getBJSCRemainTime(){
        //get period
    	String response = "";
    	String host = ADDRESS;
    	
    	
        String getTimeUrl = host + "/time?&_=";
        getTimeUrl += Long.toString(System.currentTimeMillis());
        
        long startTime = System.currentTimeMillis();
        
        response = doGet(getTimeUrl, "", ADDRESS + "/member/load?lottery=BJPK10&page=lm");
        
        if(response == null){
        	startTime = System.currentTimeMillis();
        	 response = doGet(getTimeUrl, "", ADDRESS + "/member/load?lottery=BJPK10&page=lm");
        }
        
        
        long endTime = System.currentTimeMillis();
        
        long requestTime = endTime - startTime;
        
        requestTime = requestTime /2;
        
        
        if(response != null && Common.isNum(response))
        {
        	time = Long.parseLong(response);
        	timeDValue = time + requestTime - System.currentTimeMillis();
        }
        else{
        	time = System.currentTimeMillis();
        }
        
        if(!isInBJSCBetTime(time)){
        	return -1;
        }
    	
        String getPeriodUrl = host + "/member/period?lottery=BJPK10&_=";
        getPeriodUrl += Long.toString(System.currentTimeMillis());

        
        response = doGet(getPeriodUrl, "", ADDRESS + "/member/load?lottery=BJPK10&page=lm");
        
        if(response == null){
        	response = doGet(getPeriodUrl, "", ADDRESS + "/member/load?lottery=BJPK10&page=lm");
        }
        
        if(response == null)
        {
        	
        	System.out.println("get period failed");
        	return System.currentTimeMillis();
        }
        
        System.out.println("preiod:");
        System.out.println(response);       
        
        try{
            JSONObject periodJson = new JSONObject(response);
            BJSCcloseTime = periodJson.getLong("closeTime");
            if(!BJSCdrawNumber.equals(periodJson.getString("drawNumber"))) {//新的一期
            	previousBJSCdrawNumber = BJSCdrawNumber;
            	BJSCdrawNumber = periodJson.getString("drawNumber");
            	if(previousBJSCBetNumber != previousBJSCdrawNumber && previousBJSCBetNumber != BJSCdrawNumber && previousBJSCBetNumber != "") {//判断上一期有没有漏投
					int dNum = 0;
					try {
					    dNum = Integer.parseInt(BJSCdrawNumber) - Integer.parseInt(previousBJSCBetNumber) -1;
					} catch (NumberFormatException e) {
					    e.printStackTrace();
					}
					
					
					failTimes += dNum;
					
					BJSCjinrishibai += dNum;					
			        BJSCjinriqishu += dNum;
			        
			        BJSCdetalsDataWindow.updateTextFieldjinriqishu(Integer.toString(BJSCjinriqishu));
			        BJSCdetalsDataWindow.updateTextFieldjinrishibai(Integer.toString(BJSCjinrishibai));
			        
			        BJSCdetalsDataWindow.updateTextFieldzongqishu(Integer.toString(BJSCzongqishu + BJSCjinriqishu));
			        BJSCdetalsDataWindow.updateTextFieldzongshibai(Integer.toString(BJSCzongshibai + BJSCjinrishibai));
			        
					
/*					autoBet.labelFailBets.setText("失败次数:" + failTimes);
					autoBet.labelTotalBets.setText("下单次数:" + (successTimes + failTimes));*/
					System.out.println("漏投" + dNum + "次, 期数：" + BJSCdrawNumber + "上次下单期数：" + previousBJSCBetNumber);
					
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm");//设置日期格式
					int missDrawNumber = Integer.parseInt(previousBJSCBetNumber) + 1;
					for(int i = 0; i < dNum; i++){
						String missDrawNmberstr = Integer.toString(missDrawNumber + i);						
		    			BJSCdetalsDataWindow.addData(df.format(new Date()), missDrawNmberstr, 3, "---", "---"); 
					}
					
					
					previousBJSCBetNumber = previousBJSCdrawNumber;
					
					
					
			    }
            }
        }
        catch(Exception e){
        	autoBet.outputGUIMessage("获取迪斯尼时间错误！");
        	System.out.println("getBJSCRemainTime()获取时间异常" + response);
        	return System.currentTimeMillis();
        }
 
        long remainTime = BJSCcloseTime - (System.currentTimeMillis() + timeDValue);
        
    	return remainTime;
    }
    
    public static long getCQSSClocalRemainTime() {
    	return CQSSCcloseTime - (System.currentTimeMillis() + timeDValue);
    }
    
    public static long getBJSClocalRemainTime() {
    	return BJSCcloseTime - (System.currentTimeMillis() + timeDValue);
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
            		
            		BJSCbishu = gamesData.length();
            		
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
            	BJSCbetTotalAmount = totalAmout;
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
            		
            		CQSSCbishu = gamesData.length();
            		
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
    			
    			CQSSCbetTotalAmount = totalAmount;
    			
        	}
    	}catch(Exception e){
    		e.printStackTrace();
    	}

    	
    }
    
    public static boolean doBetCQSSC(String[] betData, double percent, boolean opposite, String remainTime)
    {

    	String host = ADDRESS;
       	
        String jsonParam = "";
        
        if(previousCQSSCBetNumber.equals(CQSSCdrawNumber)) 
        	return false;
        
        
        CQSSCjinriqishu++;
        
        CQSSCdetalsDataWindow.updateTextFieldjinriqishu(Integer.toString(CQSSCjinriqishu));
        
        CQSSCdetalsDataWindow.updateTextFieldzongqishu(Integer.toString(CQSSCzongqishu + CQSSCjinriqishu));
        
        //如果未到封盘时间
        if( CQSSCdrawNumber != null){
        	
        	//System.out.printf("下注重庆时时彩第%s期\n",CQSSCdrawNumber);
        	String outputStr = "下注重庆时时彩第" + CQSSCdrawNumber + "期\n" + "最新数据时间距收盘" + remainTime + "秒\n";
        	autoBet.outputGUIMessage(outputStr);
        	
        	if(isEmptyData(betData, BetType.CQSSC)) {
        		outputStr = "代理无人投注\n\n";
        		autoBet.outputGUIMessage(outputStr);
        		return false;
        	}
        	
        	jsonParam = constructBetsData(betData, percent, BetType.CQSSC, opposite);
        	
        	if(jsonParam == "") {
        		outputStr = "代理无人投注\n\n";
        		autoBet.outputGUIMessage(outputStr);
        		return false;
        	}
        
        	outputBetsDetails(jsonParam, BetType.CQSSC);	
        	
        	System.out.println(jsonParam);
        	
        	String response = "";
        	
        	

        	

        	long time1 = System.currentTimeMillis();
        	
        	response = bet(host + "/member/bet", jsonParam, "UTF-8", "");
        	
        	
        	boolean betRes = isBetSuccess(CQSSCdrawNumber);
        	
        	if((betRes == false) && (response == null || response.contains("balance") == false || response.contains("�ڲ�����") == true)){
        		response = bet(host + "/member/bet", jsonParam, "UTF-8", "");
        	}
        	

        	long time2 = System.currentTimeMillis();
        	
        	long timeD = time2 - time1;
        	
        	double usingTime = timeD;
        	
        	usingTime = usingTime/1000;
        	
        	String strUsingTime  = String.format("下单用时！ :%f 秒\n", usingTime);
        	
        	autoBet.outputGUIMessage(strUsingTime);

        	
        	boolean result = parseBetResult(response);
        	
        	
        	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm");//设置日期格式
        	
        	if(result == false){
    			CQSSCdetalsDataWindow.addData(df.format(new Date()), CQSSCdrawNumber, 2, Integer.toString(CQSSCbetTotalAmount), Integer.toString(CQSSCbishu));

    			unknowStatCQSSCDraw.add(CQSSCdrawNumber);
    			
    			CQSSCdetalsDataWindow.updateTextFieldjinriyichang(Integer.toString(unknowStatCQSSCDraw.size()));
    			
        	}
        	else{
    			CQSSCdetalsDataWindow.addData(df.format(new Date()), CQSSCdrawNumber, 0, Integer.toString(CQSSCbetTotalAmount), Integer.toString(CQSSCbishu));        		
        	}
        	
        	unCalcProfitCQSSCDraw.add(CQSSCdrawNumber);
        	

        	    
        	if(!previousCQSSCBetNumber.equals(CQSSCdrawNumber)) { //避免重复计数
/*        		if(result == true) {
					successTimes++;
					autoBet.labelSuccessBets.setText("成功次数:" + successTimes);
				} else {
					failTimes++;
					autoBet.labelFailBets.setText("失败次数:" + failTimes);
				}
				
				autoBet.labelTotalBets.setText("下单次数:" + (successTimes + failTimes));*/
        	} else if(result) {
/*        		successTimes++;
    			autoBet.labelSuccessBets.setText("成功次数:" + successTimes);
    			failTimes--;
				autoBet.labelFailBets.setText("失败次数:" + failTimes);*/
        	}
			
			previousCQSSCBetNumber = CQSSCdrawNumber;
        	previousCQSSCBetResult = result;
        	
        	return result;
        
        }
        
        return false;
    }
    
    public static boolean doBetBJSC(String[] betData, double percent,boolean opposite, String remainTime)
    {

    	String host = ADDRESS;
       	
        String jsonParam = "";
        
        if(previousBJSCBetNumber.equals(BJSCdrawNumber)) 
        	return false;
        
        
        BJSCjinriqishu++;
        
        BJSCdetalsDataWindow.updateTextFieldjinriqishu(Integer.toString(BJSCjinriqishu));
        
        BJSCdetalsDataWindow.updateTextFieldzongqishu(Integer.toString(BJSCzongqishu + BJSCjinriqishu));
        
        //如果未到封盘时间
        if( BJSCdrawNumber != null){
        	
        	//System.out.printf("下注北京赛车第%s期\n",BJSCdrawNumber);
        	String outputStr = "下注北京赛车第" + BJSCdrawNumber + "期\n"  + "最新数据时间距收盘" + remainTime + "秒\n";
        	autoBet.outputGUIMessage(outputStr);
        	if(isEmptyData(betData, BetType.BJSC)) {
        		outputStr = "代理无人投注\n\n";
        		autoBet.outputGUIMessage(outputStr);
        		return false;
        	}
        	
        	jsonParam = constructBetsData(betData, percent, BetType.BJSC, opposite);
        	
        	if(jsonParam == "") {
        		outputStr = "代理无人投注\n\n";
        		autoBet.outputGUIMessage(outputStr);
        		return false;
        	}
        	        	
        	outputBetsDetails(jsonParam, BetType.BJSC);
        	
        	System.out.println(jsonParam);
        	
        	String response = "";  	
        	
        	
        	
        	
        	
        	long time1 = System.currentTimeMillis();
        	
        	response = bet(host + "/member/bet", jsonParam, "UTF-8", "");
        	
        	boolean betRes = isBetSuccess(BJSCdrawNumber);
        	
        	if((betRes == false)&&(response == null || response.contains("balance") == false || response.contains("内部错误") == true)){
        		response = bet(host + "/member/bet", jsonParam, "UTF-8", "");
        	}
        	

        	long time2 = System.currentTimeMillis();
        	
        	long timeD = time2 - time1;
        	
        	double usingTime = timeD;
        	
        	usingTime = usingTime/1000;
        	
        	String strUsingTime  = String.format("下单用时！ :%f 秒\n", usingTime);
        	
        	autoBet.outputGUIMessage(strUsingTime);

        	
        	boolean result = parseBetResult(response);
        	
        	
        	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm");//设置日期格式

        	
        	if(result == false){
    			BJSCdetalsDataWindow.addData(df.format(new Date()), BJSCdrawNumber, 2, Integer.toString(BJSCbetTotalAmount), Integer.toString(BJSCbishu));

    			unknowStatBJSCDraw.add(BJSCdrawNumber);
    			
    			BJSCdetalsDataWindow.updateTextFieldjinriyichang(Integer.toString(unknowStatBJSCDraw.size()));
    			
        	}
        	else{
    			BJSCdetalsDataWindow.addData(df.format(new Date()), BJSCdrawNumber, 0, Integer.toString(BJSCbetTotalAmount), Integer.toString(BJSCbishu));        		
        	}
        	
        	unCalcProfitBJSCDraw.add(BJSCdrawNumber);

        	
        	if(!previousBJSCBetNumber.equals(BJSCdrawNumber)) { //避免重复计数
        	
/*	        	if(result == true) {
					successTimes++;
					autoBet.labelSuccessBets.setText("成功次数:" + successTimes);
				} else {
					failTimes++;
					autoBet.labelFailBets.setText("失败次数:" + failTimes);
				}
				
				autoBet.labelTotalBets.setText("下单次数:" + (successTimes + failTimes));*/
        	} else if(result) { //第二次进入下单成功 的话更改失败为成功
/*        			successTimes++;
        			autoBet.labelSuccessBets.setText("成功次数:" + successTimes);
        			failTimes--;
					autoBet.labelFailBets.setText("失败次数:" + failTimes);*/
        	}
			
			previousBJSCBetNumber = BJSCdrawNumber;
        	previousBJSCBetResult = result;
        	
        	return result;
        }
        
        return false;
    }
    
    
    public static boolean parseBetResult(String str){
    	
    	


    	
    	
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
    			autoBet.outputGUIMessage("迪斯尼下单情况未知\n\n");
    			
    			
    			
    			return false;
    		}
    		int status = betResult.getInt("status");
    		switch(status){
    		case 0:
    			JSONObject account = betResult.getJSONObject("account");
    			double balance = account.getDouble("balance");
    			//int betting = account.getInt("betting");
    			outputStr  = String.format("迪斯尼下单成功！ 账户余额:%f\n", balance);
    			autoBet.outputGUIMessage(outputStr);
    			//System.out.printf("下单成功！ 下单金额：%d, 账户余额:%f\n", betting, balance);
    			
    			
    			return true;
    		

    		case 2:
    			//System.out.println("下单失败:已封盘！\n");
    			autoBet.outputGUIMessage("迪斯尼下单情况未知\n\n\n");
    			
    			
    			
    			return false;
    		case 3:
    			String message = betResult.getString("message");
    			outputStr  = String.format("迪斯尼下单情况未知\n\n：%s\n",message);
    			autoBet.outputGUIMessage(outputStr);
    			
    			
    			
    			return false;
    		
    		}
    	}
    	
    	
    	
    	autoBet.outputGUIMessage("迪斯尼下单情况未知\n\n！\n\n");
    	
    	return false;
    }
    
    public static String constructBetsData(String[] data, double percent, BetType betType, boolean opposite)
    {
    	
    	//data = "[[{\"k\":\"DX2\",\"i\":\"X\",\"c\":2,\"a\":55,\"r\":109.989,\"cm\":0},{\"k\":\"DX3\",\"i\":\"D\",\"c\":3,\"a\":130,\"r\":258.294,\"cm\":0},{\"k\":\"DX4\",\"i\":\"D\",\"c\":4,\"a\":660,\"r\":1319.868,\"cm\":0},{\"k\":\"DS1\",\"i\":\"D\",\"c\":1,\"a\":10,\"r\":19.998,\"cm\":0},{\"k\":\"DX2\",\"i\":\"D\",\"c\":3,\"a\":20,\"r\":39.996,\"cm\":0},{\"k\":\"DS4\",\"i\":\"D\",\"c\":3,\"a\":20,\"r\":39.996,\"cm\":0},{\"k\":\"DX3\",\"i\":\"X\",\"c\":1,\"a\":5,\"r\":9.999,\"cm\":0},{\"k\":\"DX5\",\"i\":\"D\",\"c\":2,\"a\":40,\"r\":79.992,\"cm\":0},{\"k\":\"DX1\",\"i\":\"X\",\"c\":2,\"a\":55,\"r\":109.989,\"cm\":0},{\"k\":\"DX4\",\"i\":\"X\",\"c\":1,\"a\":40,\"r\":79.992,\"cm\":0},{\"k\":\"ZDX\",\"i\":\"D\",\"c\":2,\"a\":15,\"r\":29.997,\"cm\":0},{\"k\":\"DS1\",\"i\":\"S\",\"c\":2,\"a\":15,\"r\":29.997,\"cm\":0},{\"k\":\"DS2\",\"i\":\"D\",\"c\":3,\"a\":100,\"r\":199.98,\"cm\":0},{\"k\":\"DS3\",\"i\":\"D\",\"c\":2,\"a\":55,\"r\":109.989,\"cm\":0},{\"k\":\"DS3\",\"i\":\"S\",\"c\":3,\"a\":20,\"r\":39.996,\"cm\":0},{\"k\":\"DS4\",\"i\":\"S\",\"c\":2,\"a\":45,\"r\":89.991,\"cm\":0},{\"k\":\"DS5\",\"i\":\"D\",\"c\":3,\"a\":50,\"r\":99.99,\"cm\":0},{\"k\":\"DX1\",\"i\":\"D\",\"c\":3,\"a\":50,\"r\":99.99,\"cm\":0},{\"k\":\"DX5\",\"i\":\"X\",\"c\":3,\"a\":40,\"r\":79.992,\"cm\":0},{\"k\":\"ZDS\",\"i\":\"S\",\"c\":2,\"a\":15,\"r\":29.997,\"cm\":0},{\"k\":\"DS2\",\"i\":\"S\",\"c\":2,\"a\":40,\"r\":79.992,\"cm\":0},{\"k\":\"DS5\",\"i\":\"S\",\"c\":2,\"a\":55,\"r\":109.989,\"cm\":0}],{\"DS1_S\":1.983,\"DS1_D\":1.983,\"DS2_S\":1.983,\"DS2_D\":1.983,\"DS3_S\":1.983,\"DS3_D\":1.983,\"DS4_S\":1.983,\"DS4_D\":1.983,\"DS5_S\":1.983,\"DS5_D\":1.983,\"DX1_X\":1.983,\"DX1_D\":1.983,\"DX2_X\":1.983,\"DX2_D\":1.983,\"DX3_X\":1.983,\"DX3_D\":1.983,\"DX4_X\":1.983,\"DX4_D\":1.983,\"DX5_X\":1.983,\"DX5_D\":1.983,\"LH_T\":9.28,\"LH_H\":1.983,\"LH_L\":1.983,\"ZDS_S\":1.983,\"ZDS_D\":1.983,\"ZDX_X\":1.983,\"ZDX_D\":1.983},{\"B1\":64,\"B4\":142,\"LM\":1535,\"B3\":64,\"B5\":334,\"B2\":64}]";
    	int totalAmount = 0;
    	
    	String res = "";
    	
    	String oddsData = "";
    	
    	
    	try{
    		
    		List<String> parsedGames = new ArrayList<String>();;
    		
	    	JSONArray gamesArray = new JSONArray();
	    	JSONObject oddsGrabData = null;
	    	
	    	if(betType == BetType.BJSC){
	    		
	    		if(BJSCoddsData == null){
	    			getBJSCoddsData();
	    		}
	    		
	    		oddsData = BJSCoddsData;
	    	}
	    	else if(betType == BetType.CQSSC){
	    		
	    		if(CQSSCoddsData == null){
	    			getCQSSCoddsData();
	    		}
	    		
	    		oddsData = CQSSCoddsData;
	    	}
	    	
	    	oddsGrabData = new JSONObject(oddsData);
	    	
	    	for(int i = 0; i < data.length; i++){
    		
    		
            	JSONArray cqsscLMGrabData = new JSONArray(data[i]);        	
            	JSONArray gamesGrabData = cqsscLMGrabData.getJSONArray(0);

        	
	        	for(int j = 0; j < gamesGrabData.length(); j++){
	        		JSONObject gameGrabData = gamesGrabData.getJSONObject(j);
	        		
	    			String game = gameGrabData.getString("k");
	    			String contents = gameGrabData.getString("i");
	    			int amount = gameGrabData.getInt("a");
	    			String oddsKey = game + "_" + contents;
	    			
	    			if(oddsData.contains(oddsKey) == false)
	    				continue;
	    			
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
	        			if(amount == 0)
	        				amount = 1;
	        			totalAmount += amount;
	        			
	        			JSONObject gameObj = new JSONObject();
	        			gameObj.put("game", game);
	        			
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
	        				        			
	        				        			
	        			gameObj.put("contents", contents);
	        			gameObj.put("amount", amount);
	        			gameObj.put("odds", odds);
	        			
	        			
	        			//输出显示
	        	    	if(betType == BetType.CQSSC){
	        	        	
	        	    	}
	        	    	else if(betType == BetType.BJSC){
	        	
	        	    	}
	        			
	        			gamesArray.put(gameObj);
	        		}
	        		
	        	}
	    	}
	    	
	
	    	
	    	if(gamesArray.length() == 0) {
	    		return "";
	    	}
	    	
	    	JSONObject betsObj = new JSONObject();
	    	
	    	boolean ignore = false;
	    	betsObj.put("ignore", ignore);
	    	betsObj.put("bets", gamesArray);
	    	
	    	if(betType == BetType.CQSSC){
	        	betsObj.put("drawNumber",CQSSCdrawNumber);
	        	
	        	betsObj.put("lottery", "CQSSC");
	    	}
	    	else if(betType == BetType.BJSC){
	        	betsObj.put("drawNumber",BJSCdrawNumber);
	        	
	        	betsObj.put("lottery", "BJPK10");
	
	    	}
	    	
	    	res = betsObj.toString();
    	
    	}catch(Exception e){
    		autoBet.outputGUIMessage("构造下单数据错误！\n");
    		return "";
    	}
   	
    	return res;	
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
    





    /**以utf-8形式读取*/
    public static String doPost(String url,List<NameValuePair> formparams, String cookies) {
        return doPost(url, formparams,"UTF-8", cookies);
    }

    public static String doPost(String url,List<NameValuePair> formparams,String charset, String cookies) {


     // 创建httppost   
    	
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
            
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(defaultTimeout).setConnectTimeout(defaultTimeout).build();
            httppost.setConfig(requestConfig);
            
            CloseableHttpResponse response = execute(httppost);
            try {
                // 打印响应状态    
            	setCookie(response);
            	//System.out.println("设置cookie:" + strCookies);
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
     
        } catch(Exception e){
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
           
            //设置超时
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(defaultTimeout).setConnectTimeout(defaultTimeout).build();
            httpget.setConfig(requestConfig);
            
            // 执行get请求.    
            CloseableHttpResponse response = execute(httpget); 
            
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
	    	autoBet.outputGUIMessage("isEmptyData()构造下单数据错误！\n");
	    	return true;
	    }
    }

    
    
    public static String bet(String url,String jsonData, String charset, String cookies){


        // 创建httppost  
    	try {
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
           
        	   strEntity = new StringEntity(jsonData, charset);
               httppost.setEntity(strEntity);
               
               RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(autoBet.betTimeOut).setConnectTimeout(autoBet.betTimeOut).build();
               httppost.setConfig(requestConfig);
               
               
               CloseableHttpResponse response = execute(httppost);
               try {
                   // 打印响应状态    
                   System.out.println(response.getStatusLine());
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
           }catch(Exception e){
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
	        
	        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(defaultTimeout).setConnectTimeout(defaultTimeout).build();
            httpget.setConfig(requestConfig);
	        
        
	        CloseableHttpResponse response = execute(httpget); 
       	 try {
       		    setCookie(response);
                // 打印响应状态    
                System.out.println(response.getStatusLine()); 
                System.out.println("------------------------------------");
                File storeFile = new File("hyyzm.png");   //图片保存到当前位置
                FileOutputStream output = new FileOutputStream(storeFile);  
                //得到网络资源的字节数组,并写入文件  
                byte [] a = EntityUtils.toByteArray(response.getEntity());
                output.write(a);  
                output.close();  
                
                InputStream ins = null;
        		 String[] cmd = new String[]{ConfigReader.getTessPath() + "\\tesseract", "hyyzm.png", "result", "-l", "eng"};

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


