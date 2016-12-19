package lanyang;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

















import dsn.Common;
import dsn.DSNBetAmountWindow;
import dsn.DSNDataDetailsWindow;
import dsn.TYPEINDEX;
import dsn.autoBet;




public class BetBJSCManager {
	
	
	static int dsStartIndex = 4011;
	static int dxStartIndex = 4021;
	static int lhStartIndex = 4031;
	static String BJSCdrawNumber = "";
	static String previousBJSCBetNumber = "";
	static boolean previousBJSCBetResult = false;
	
	static String previousBJSCdrawNumber = "";
	
	static int BJSCbishu = 0;
	
	static int BJSCbetTotalAmount = 0;
	
    static int BJSCzongqishu = 0;
    static int BJSCzongshibai = 0;
    static int BJSCzongyichang = 0;
    
    static int BJSCjinriqishu = 0;
    static int BJSCjinrishibai = 0;
    static int BJSCjinriyichang = 0;
    
    
    static Vector<String> unknowStatBJSCDraw = new Vector<String>();    
    static Vector<String> unCalcProfitBJSCDraw = new Vector<String>();    

    static DSNDataDetailsWindow BJSCdetalsDataWindow = new DSNDataDetailsWindow();    
    
    static DSNBetAmountWindow BJSCBetAmountWindow = new DSNBetAmountWindow();    
	
	static long remainTime = -1;
	
	
	static JSONObject gameOdds = new JSONObject();
	
	
	public static long webTime = -1;  //seconds
	
	public static long closeTime = -1; //seconds
	
	public static long timeDvalue = -1; //sceonds
	
	
	public static void init(){
		BJSCdetalsDataWindow.setTitle("[蓝洋]投注北京赛车详情");
		BJSCBetAmountWindow.setTitle("[蓝洋]北京赛车下注金额");
	}
	
	
	public static String getProfit(String drawNumber){
		
		String profit = "---";
		
		String maxPage = "1";
		
		try{
			
			String profitUri = LanyangHttp.lineuri + "z/mem2-betAcountY";
			
			String res = LanyangHttp.doGet(profitUri, LanyangHttp.strCookies, "");
			
			int posStart = -1;
			int posEnd = -1;
			
			if(res != null){
				posStart = res.indexOf("endpage");
				posStart = res.indexOf(":", posStart);
				posEnd = res.indexOf(",",posStart);
				maxPage = res.substring(posStart+1, posEnd);
			}
			
			profitUri = LanyangHttp.lineuri + "z/mem2-betAcountY" + "?lottery=ALL&ptype=2&page=" + maxPage;
			
			res = LanyangHttp.doGet(profitUri, LanyangHttp.strCookies, "");
			
			
			if(res != null && res.contains(drawNumber)){
				posStart = res.indexOf(drawNumber);
				
				posEnd = res.indexOf("]", posStart);
				
				String subStr = res.substring(posStart, posEnd);
				
				String[] infoArray = subStr.split(",");
				
				profit = infoArray[7];

			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		

		if(Common.isNum(profit)){
			return profit;
		}else{
			profit = "---";
		}
		
		return profit;
	}
	
	public static boolean grabGameInfo(){
		
		try{
			String checkGameUri = LanyangHttp.lineuri + "z/user-CheckGameAjax";
			
	        List<NameValuePair> params = new ArrayList<NameValuePair>();
	        params.add(new BasicNameValuePair("cn", "11"));
	        params.add(new BasicNameValuePair("ty", "0"));
	        
	        String cookies = "onlyone=0;";
	        cookies = LanyangHttp.strCookies + cookies;

	        String res = LanyangHttp.doPost(checkGameUri, params, cookies, LanyangHttp.lineuri + "z/");
	        
	        System.out.println(res);
	        
	        if(res != null && res.contains("index2.php")){
	        	LanyangHttp.setIsNeedRelogin(true);
	        	return false;
	        }
	        
	        if(res != null){
	        	
	        	//System.out.println(res);
	        	
	        	if(!res.contains("name"))
	        		return false;
	        	
	        	JSONObject gameInfo = new JSONObject(res);
	        	

	        	
	        	gameInfo = new JSONObject(gameInfo.getJSONObject("data").toString());
	        	
	        	//BJSCdrawNumber = gameInfo.getString("name");
	        	


                if(!BJSCdrawNumber.equals(gameInfo.getString("name"))) {//新的一期
                	previousBJSCdrawNumber = BJSCdrawNumber;
                	BJSCdrawNumber = gameInfo.getString("name");
                	if(previousBJSCBetNumber != previousBJSCdrawNumber && previousBJSCBetNumber != BJSCdrawNumber && previousBJSCBetNumber != "") {//判断上一期有没有漏投
    					int dNum = 0;
    					try {
    					    dNum = Integer.parseInt(BJSCdrawNumber) - Integer.parseInt(previousBJSCBetNumber) -1;
    					} catch (NumberFormatException e) {
    					    e.printStackTrace();
    					}
    					
    					
    					
    					
    					BJSCjinrishibai += dNum;					
    			        BJSCjinriqishu += dNum;
    			        
    			        BJSCdetalsDataWindow.updateTextFieldjinriqishu(Integer.toString(BJSCjinriqishu));
    			        BJSCdetalsDataWindow.updateTextFieldjinrishibai(Integer.toString(BJSCjinrishibai));
    			        
    			        BJSCdetalsDataWindow.updateTextFieldzongqishu(Integer.toString(BJSCzongqishu + BJSCjinriqishu));
    			        BJSCdetalsDataWindow.updateTextFieldzongshibai(Integer.toString(BJSCzongshibai + BJSCjinrishibai));
    			        

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
	            
	          
	        	
	        	
	        	
	        	
	        	
	        	
	        	String closeTimeStr = gameInfo.getString("et");
	        	String[] closeTimeArray = closeTimeStr.split(":");
	        	
	        	
        		int hour = Integer.parseInt(closeTimeArray[0]);
        		int min = Integer.parseInt(closeTimeArray[1]);
        		int sec = Integer.parseInt(closeTimeArray[2]);
        		
        		closeTime = hour*60*60 + min*60 + sec;
	        	
	        	
	        	System.out.println(gameInfo.toString());
	        	
	        	return true;
	        	
	        }
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return false;
             	
		
		
	}
	
	public static String getDrawnumber(){
		return BJSCdrawNumber;
	}
	
	
    public static boolean  isInBJSCBetTime(){
    	
    	long time = System.currentTimeMillis();
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
	
	
	public static long getReaminTime(){
		long time = System.currentTimeMillis();
        Date date = new Date(time);
        int currentHour = date.getHours();
        int currentMinutes = date.getMinutes();
        int currentSeconds = date.getSeconds();
        
        
        long currentTime = currentHour*60*60 + currentMinutes*60 + currentSeconds;
        
        remainTime = closeTime + timeDvalue - currentTime;
        
        return remainTime;
	}
	
	
    public static boolean grabOddsData(){
    	
    	String res = "";
    	
    	

        try{
        	String url = LanyangHttp.lineuri + "z/gPKT-pkt7?gi=11&bt=1";
        	
        	//System.out.println(LanyangHttp.strCookies);
        	
        	
        	res = LanyangHttp.doGet(url, LanyangHttp.strCookies, "");
        	
        	
        	
        	if(res == null){
        		res = LanyangHttp.doGet(url, LanyangHttp.strCookies, "");
        	}
        	
        	if(res != null){
            	//System.out.println(res);
            	
            	//获取赔率数据
            	int dsIndex = dsStartIndex;
            	int dxIndex = dxStartIndex;
            	int lhIndex = lhStartIndex;
            	
            	int posStart = -1;
            	int posEnd = -1;
            	
            	double odd = 0;
            	
            	for(int i = 0; i < 10; i++){
            		dsIndex = dsStartIndex + i;
            		String findStr = Integer.toString(dsIndex) + "[0]";
            		
            		posStart = res.indexOf(findStr);
            		if(posStart != -1){
            			posStart = res.indexOf("=", posStart);
            			posStart += 1;
            			posEnd = res.indexOf(";", posStart);
            			
            			String oddStr = res.substring(posStart, posEnd);
            			oddStr.replace(" ", "");
            			
            			odd = Double.parseDouble(oddStr);
            			
            			gameOdds.put(Integer.toString(dsIndex), odd);
            			
            		}
            		
            		
            		
            	}
            	
            	
            	
            	for(int i = 0; i < 10; i++){
            		dxIndex = dxStartIndex + i;
            		String findStr = Integer.toString(dxIndex) + "[0]";
            		
            		posStart = res.indexOf(findStr);
            		if(posStart != -1){
            			posStart = res.indexOf("=", posStart);
            			posStart += 1;
            			posEnd = res.indexOf(";", posStart);
            			
            			String oddStr = res.substring(posStart, posEnd);
            			oddStr.replace(" ", "");
            			
            			odd = Double.parseDouble(oddStr);
            			
            			gameOdds.put(Integer.toString(dxIndex), odd);
            			
            		}
            		
            		
            		
            	}
            	
            	for(int i = 0; i < 5; i++){
            		lhIndex = lhStartIndex + i;
            		String findStr = Integer.toString(lhIndex) + "[0]";
            		
            		posStart = res.indexOf(findStr);
            		if(posStart != -1){
            			posStart = res.indexOf("=", posStart);
            			posStart += 1;
            			posEnd = res.indexOf(";", posStart);
            			
            			String oddStr = res.substring(posStart, posEnd);
            			oddStr.replace(" ", "");
            			
            			odd = Double.parseDouble(oddStr);
            			
            			gameOdds.put(Integer.toString(lhIndex), odd);
            			
            		}
            		
            		
            		
            	}
            	
            	
            	
            	if(gameOdds.length() >0){
            		//System.out.println(gameOdds.length());
            		System.out.println(gameOdds.toString());
            		
            	}
            	
            	
            	
            	//获取网站时间
            	posStart = res.indexOf("nowDate=\"");
            	if(posStart != -1){
            		posStart = res.indexOf(" " , posStart);
            		posStart += 1;
            		
            		posEnd = res.indexOf("\"", posStart);
            		
            		String timeStr = res.substring(posStart, posEnd);
            		
            		String[] timeArray = timeStr.split(":");
            		
            		int hour = Integer.parseInt(timeArray[0]);
            		int min = Integer.parseInt(timeArray[1]);
            		int sec = Integer.parseInt(timeArray[2]);
            		
            		webTime = hour*60*60 + min*60 + sec;
            		
            		long currentTime = System.currentTimeMillis();
            		Date date = new Date(currentTime);
                    int currentHour = date.getHours();
                    int currentMinutes = date.getMinutes();
                    int currentSeconds = date.getSeconds();
                    
                    currentTime = currentHour*60*60 + currentMinutes*60 + currentSeconds;
                    
                    timeDvalue = currentTime - webTime;
            		
            		//System.out.println(timeDvalue);
            		
            		return true;
            		
            	}
        		
        	}
        	

            
        	
        	
        	
        }catch(Exception e){
        	e.printStackTrace();
        }
    	
    	
        LanyangHttp.addFailsTimes();
    	
    	
    	
    	return  false;
   	
    }
    
    
    public static boolean doBetBJSC(String[] betData, double percent,boolean opposite, String remainTime)
    {

    	String host = LanyangHttp.lineuri;
       	
        String betStr = "";
        
        if(previousBJSCBetNumber.equals(BJSCdrawNumber)) 
        	return false;
        
        
        BJSCjinriqishu++;
        
        BJSCdetalsDataWindow.updateTextFieldjinriqishu(Integer.toString(BJSCjinriqishu));
        
        BJSCdetalsDataWindow.updateTextFieldzongqishu(Integer.toString(BJSCzongqishu + BJSCjinriqishu));
        
        //如果未到封盘时间
        if( BJSCdrawNumber != null){
        	
        	//System.out.printf("下注北京赛车第%s期\n",BJSCdrawNumber);
        	String outputStr = "蓝洋下注北京赛车第" + BJSCdrawNumber + "期\n"  + "最新数据时间距收盘" + remainTime + "秒\n";
        	autoBet.outputGUIMessage(outputStr);
        	
        	
        	betStr = constructBetsData(betData, percent,  opposite);
        	
        	if(!betStr.contains("|")) {
        		outputStr = "代理无人投注\n\n";
        		autoBet.outputGUIMessage(outputStr);
        		return false;
        	}
        	
        	
        	
        	String outputBetData = constructoutputData(betData, percent,  opposite);
        	
        	
        	
        	
        	
        	addToBetAmountWindow(outputBetData);
        	
        	
        	        	
        	outputBetsDetails(outputBetData);
        	
        	System.out.println(betStr);
        	
        	String response = "";  	
        	
        	
        	
        	
        	
        	long time1 = System.currentTimeMillis();
        	
	        List<NameValuePair> params = new ArrayList<NameValuePair>();
	        params.add(new BasicNameValuePair("b", betStr));
	        
        	
	        int oldTimeout = LanyangHttp.defaultTimeout;
	        
	        LanyangHttp.defaultTimeout = 10*1000;
	        
        	
        	response = LanyangHttp.doPost(host + "z/o-trans", params, LanyangHttp.strCookies, "");
        	
        	System.out.printf("下单结果:%s\n", response);
        	
        	boolean betRes = isBetSuccess(response);
        	
        	if((betRes == false)){
        		response = LanyangHttp.doPost(host + "z/o-trans", params, LanyangHttp.strCookies, "");
        		
        		System.out.printf("再次下单结果:%s\n", response);
        	}
        	
        	
        	
        	
        	LanyangHttp.defaultTimeout = oldTimeout;

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
    
    
    
    
    
    public static String constructBetsData(String[] data, double percent,  boolean opposite)
    {
    	
    	//data = "[[{\"k\":\"DX2\",\"i\":\"X\",\"c\":2,\"a\":55,\"r\":109.989,\"cm\":0},{\"k\":\"DX3\",\"i\":\"D\",\"c\":3,\"a\":130,\"r\":258.294,\"cm\":0},{\"k\":\"DX4\",\"i\":\"D\",\"c\":4,\"a\":660,\"r\":1319.868,\"cm\":0},{\"k\":\"DS1\",\"i\":\"D\",\"c\":1,\"a\":10,\"r\":19.998,\"cm\":0},{\"k\":\"DX2\",\"i\":\"D\",\"c\":3,\"a\":20,\"r\":39.996,\"cm\":0},{\"k\":\"DS4\",\"i\":\"D\",\"c\":3,\"a\":20,\"r\":39.996,\"cm\":0},{\"k\":\"DX3\",\"i\":\"X\",\"c\":1,\"a\":5,\"r\":9.999,\"cm\":0},{\"k\":\"DX5\",\"i\":\"D\",\"c\":2,\"a\":40,\"r\":79.992,\"cm\":0},{\"k\":\"DX1\",\"i\":\"X\",\"c\":2,\"a\":55,\"r\":109.989,\"cm\":0},{\"k\":\"DX4\",\"i\":\"X\",\"c\":1,\"a\":40,\"r\":79.992,\"cm\":0},{\"k\":\"ZDX\",\"i\":\"D\",\"c\":2,\"a\":15,\"r\":29.997,\"cm\":0},{\"k\":\"DS1\",\"i\":\"S\",\"c\":2,\"a\":15,\"r\":29.997,\"cm\":0},{\"k\":\"DS2\",\"i\":\"D\",\"c\":3,\"a\":100,\"r\":199.98,\"cm\":0},{\"k\":\"DS3\",\"i\":\"D\",\"c\":2,\"a\":55,\"r\":109.989,\"cm\":0},{\"k\":\"DS3\",\"i\":\"S\",\"c\":3,\"a\":20,\"r\":39.996,\"cm\":0},{\"k\":\"DS4\",\"i\":\"S\",\"c\":2,\"a\":45,\"r\":89.991,\"cm\":0},{\"k\":\"DS5\",\"i\":\"D\",\"c\":3,\"a\":50,\"r\":99.99,\"cm\":0},{\"k\":\"DX1\",\"i\":\"D\",\"c\":3,\"a\":50,\"r\":99.99,\"cm\":0},{\"k\":\"DX5\",\"i\":\"X\",\"c\":3,\"a\":40,\"r\":79.992,\"cm\":0},{\"k\":\"ZDS\",\"i\":\"S\",\"c\":2,\"a\":15,\"r\":29.997,\"cm\":0},{\"k\":\"DS2\",\"i\":\"S\",\"c\":2,\"a\":40,\"r\":79.992,\"cm\":0},{\"k\":\"DS5\",\"i\":\"S\",\"c\":2,\"a\":55,\"r\":109.989,\"cm\":0}],{\"DS1_S\":1.983,\"DS1_D\":1.983,\"DS2_S\":1.983,\"DS2_D\":1.983,\"DS3_S\":1.983,\"DS3_D\":1.983,\"DS4_S\":1.983,\"DS4_D\":1.983,\"DS5_S\":1.983,\"DS5_D\":1.983,\"DX1_X\":1.983,\"DX1_D\":1.983,\"DX2_X\":1.983,\"DX2_D\":1.983,\"DX3_X\":1.983,\"DX3_D\":1.983,\"DX4_X\":1.983,\"DX4_D\":1.983,\"DX5_X\":1.983,\"DX5_D\":1.983,\"LH_T\":9.28,\"LH_H\":1.983,\"LH_L\":1.983,\"ZDS_S\":1.983,\"ZDS_D\":1.983,\"ZDX_X\":1.983,\"ZDX_D\":1.983},{\"B1\":64,\"B4\":142,\"LM\":1535,\"B3\":64,\"B5\":334,\"B2\":64}]";
    	int totalAmount = 0;
    	
    	String res = "11,1,0";
    	
    	res =res + "," + BJSCdrawNumber;
    	
    	String oddsData = "";
    	
    	
    	try{
    		
    		List<String> parsedGames = new ArrayList<String>();;
    		
	    	JSONArray gamesArray = new JSONArray();
	    	JSONObject oddsGrabData = null;
	    	

	    		
	    	
	    	
	    	for(int i = 0; i < data.length; i++){
    		
    		
            	JSONArray cqsscLMGrabData = new JSONArray(data[i]);        	
            	JSONArray gamesGrabData = cqsscLMGrabData.getJSONArray(0);
            	
            	oddsGrabData = cqsscLMGrabData.getJSONObject(1);

        	
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
	        		if(odds < 2.0 && amount >0){
	        			amount = (int)(amount*percent);  
	        			if(amount <= 10)
	        				continue;
	        			totalAmount += amount;	        				        				        			
	        			//处理反投: 大变小，小变大，单变双，双变大，龙变虎，虎变隆
	        				
        				//autoBet.outputGUIMessage(game + contents + ":" + Integer.toString(amount));
        				
        				if(game.indexOf("DX") != -1){//反大小
        					if(contents.indexOf("D") != -1 && opposite){
        						contents = "2";        						
        					}
        					else{
        						contents = "1";
        					}
        					
        					
        					
        					
        					int index = Integer.parseInt(game.replace("DX", ""));
        					
        					index = dxStartIndex + index -1;
        					
        					double odd = gameOdds.getDouble(Integer.toString(index));
        					
        					res = res + "," +  Integer.toString(index) + "|" + Double.toString(odd) + "|" + contents + "|" + Integer.toString(amount) + "|" + "0";
        				}
        				
        				
        				if(game.indexOf("DS") != -1){//反单双
        					if(contents.indexOf("D") != -1 && opposite){
        						contents = "2";        						
        					}
        					else{
        						contents = "1";
        					}
        					
        					int index = Integer.parseInt(game.replace("DS", ""));
        					
        					index = dsStartIndex + index -1;
        					
        					double odd = gameOdds.getDouble(Integer.toString(index));
        					
        					res = res + "," +  Integer.toString(index) + "|" + Double.toString(odd) + "|" + contents + "|" + Integer.toString(amount) + "|" + "0";

        				}
        				
        				if(game.indexOf("LH") != -1){//反龙虎
        					if(contents.indexOf("L") != -1 && opposite){
        						contents = "2";        						
        					}
        					else{
        						contents = "1";
        					}
        					int index = Integer.parseInt(game.replace("LH", ""));
        					
        					index = lhStartIndex + index -1;
        					
        					double odd = gameOdds.getDouble(Integer.toString(index));
        					
        					res = res + "," +  Integer.toString(index) + "|" + Double.toString(odd) + "|" + contents + "|" + Integer.toString(amount) + "|" + "0";
        				}


	        		
	        		}
	        		
	        	}
	    	}
	    	

	    	
	    	
    	
    	}catch(Exception e){
    		e.printStackTrace();
    		autoBet.outputGUIMessage("构造下单数据错误！\n");
    		return "";
    	}
   	
    	return res;	
    }
    
    
    public static boolean isBetSuccess(String res){
    	
    	try{
    		JSONObject betRes = new JSONObject(res);
    		
    		String errorCode = betRes.getString("c");
    		
    		if(errorCode.contains("100")){
    			return true;
    		}
    		
    	}catch(Exception e){
    		
    	}
    	
    	
    	
    	
    	
    	return false;
    }
    
    
    public static boolean parseBetResult(String res){
    	
    	try{
    		JSONObject betRes = new JSONObject(res);
    		
    		String errorCode = betRes.getString("c");
    		
    		if(errorCode.contains("100")){
    			autoBet.outputGUIMessage("下单成功\n");
    			return true;
    		}
    		else if(errorCode.contains("330")){
    			autoBet.outputGUIMessage("下单失败，已封盘\n");
    		}
    		else if(errorCode.contains("140")){
    			autoBet.outputGUIMessage("下单失败，下单数据错误\n");
    		}
    		else if(errorCode.contains("005")){
    			autoBet.outputGUIMessage("下单失败，额度不足\n");
    		}
    		
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	
    	

    	return false;

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
    	

    	
    	BJSCjinriqishu = 0;
    	BJSCjinrishibai = 0;
    	BJSCjinriyichang = 0;
    	
    	
    	remainTime = -1;

    }
    
    
    public static String constructoutputData(String[] data, double percent,  boolean opposite)
    {
    	
    	//data = "[[{\"k\":\"DX2\",\"i\":\"X\",\"c\":2,\"a\":55,\"r\":109.989,\"cm\":0},{\"k\":\"DX3\",\"i\":\"D\",\"c\":3,\"a\":130,\"r\":258.294,\"cm\":0},{\"k\":\"DX4\",\"i\":\"D\",\"c\":4,\"a\":660,\"r\":1319.868,\"cm\":0},{\"k\":\"DS1\",\"i\":\"D\",\"c\":1,\"a\":10,\"r\":19.998,\"cm\":0},{\"k\":\"DX2\",\"i\":\"D\",\"c\":3,\"a\":20,\"r\":39.996,\"cm\":0},{\"k\":\"DS4\",\"i\":\"D\",\"c\":3,\"a\":20,\"r\":39.996,\"cm\":0},{\"k\":\"DX3\",\"i\":\"X\",\"c\":1,\"a\":5,\"r\":9.999,\"cm\":0},{\"k\":\"DX5\",\"i\":\"D\",\"c\":2,\"a\":40,\"r\":79.992,\"cm\":0},{\"k\":\"DX1\",\"i\":\"X\",\"c\":2,\"a\":55,\"r\":109.989,\"cm\":0},{\"k\":\"DX4\",\"i\":\"X\",\"c\":1,\"a\":40,\"r\":79.992,\"cm\":0},{\"k\":\"ZDX\",\"i\":\"D\",\"c\":2,\"a\":15,\"r\":29.997,\"cm\":0},{\"k\":\"DS1\",\"i\":\"S\",\"c\":2,\"a\":15,\"r\":29.997,\"cm\":0},{\"k\":\"DS2\",\"i\":\"D\",\"c\":3,\"a\":100,\"r\":199.98,\"cm\":0},{\"k\":\"DS3\",\"i\":\"D\",\"c\":2,\"a\":55,\"r\":109.989,\"cm\":0},{\"k\":\"DS3\",\"i\":\"S\",\"c\":3,\"a\":20,\"r\":39.996,\"cm\":0},{\"k\":\"DS4\",\"i\":\"S\",\"c\":2,\"a\":45,\"r\":89.991,\"cm\":0},{\"k\":\"DS5\",\"i\":\"D\",\"c\":3,\"a\":50,\"r\":99.99,\"cm\":0},{\"k\":\"DX1\",\"i\":\"D\",\"c\":3,\"a\":50,\"r\":99.99,\"cm\":0},{\"k\":\"DX5\",\"i\":\"X\",\"c\":3,\"a\":40,\"r\":79.992,\"cm\":0},{\"k\":\"ZDS\",\"i\":\"S\",\"c\":2,\"a\":15,\"r\":29.997,\"cm\":0},{\"k\":\"DS2\",\"i\":\"S\",\"c\":2,\"a\":40,\"r\":79.992,\"cm\":0},{\"k\":\"DS5\",\"i\":\"S\",\"c\":2,\"a\":55,\"r\":109.989,\"cm\":0}],{\"DS1_S\":1.983,\"DS1_D\":1.983,\"DS2_S\":1.983,\"DS2_D\":1.983,\"DS3_S\":1.983,\"DS3_D\":1.983,\"DS4_S\":1.983,\"DS4_D\":1.983,\"DS5_S\":1.983,\"DS5_D\":1.983,\"DX1_X\":1.983,\"DX1_D\":1.983,\"DX2_X\":1.983,\"DX2_D\":1.983,\"DX3_X\":1.983,\"DX3_D\":1.983,\"DX4_X\":1.983,\"DX4_D\":1.983,\"DX5_X\":1.983,\"DX5_D\":1.983,\"LH_T\":9.28,\"LH_H\":1.983,\"LH_L\":1.983,\"ZDS_S\":1.983,\"ZDS_D\":1.983,\"ZDX_X\":1.983,\"ZDX_D\":1.983},{\"B1\":64,\"B4\":142,\"LM\":1535,\"B3\":64,\"B5\":334,\"B2\":64}]";
    	int totalAmount = 0;
    	
    	String res = "";
    	
    	
    	
    	
    	try{
    		
    		List<String> parsedGames = new ArrayList<String>();;
    		
	    	JSONArray gamesArray = new JSONArray();
	    	JSONObject oddsGrabData = null;
	    	

	    	
	    	
	    	for(int i = 0; i < data.length; i++){
    		
    		
            	JSONArray cqsscLMGrabData = new JSONArray(data[i]);        	
            	JSONArray gamesGrabData = cqsscLMGrabData.getJSONArray(0);
            	
            	oddsGrabData = cqsscLMGrabData.getJSONObject(1);
        	
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
	        		if(odds < 2.0 && amount >0){
	        			amount = (int)(amount*percent);  
	        			if(amount < 10)
	        				continue;
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
	    	
	    	
	    	
	        betsObj.put("drawNumber",BJSCdrawNumber);
	        	
	        betsObj.put("lottery", "BJPK10");
	
	    	
	    	
	    	res = betsObj.toString();
    	
    	}catch(Exception e){
    		e.printStackTrace();
    		autoBet.outputGUIMessage("构造下单数据错误！\n");
    		return "";
    	}
   	
    	return res;	
    }    
    
    
    
    public static void outputBetsDetails(String jsonData){
    	
    	autoBet.outputGUIMessage("下注详情：\n");
    	try{
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
        	catch(Exception e){
        		e.printStackTrace();
        	}
        	
    	

    	
    }
    
    
    
    public static void showBJSCDeatilsTable(){
    	BJSCdetalsDataWindow.setVisible(true);
    }
    
    
    
    public static void showBJSCBetAmountTable(){
    	BJSCBetAmountWindow.setVisible(true);
    }
    
    
    public static void addToBetAmountWindow(String jsonData){
    	
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm");//设置日期格式
    	
    	
    	try{

            	
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
						outputStr  = String.format("冠军  %s,", contentsDX);

						BJSCBetAmountWindow.addData(df.format(new Date()), BJSCdrawNumber, outputStr, Integer.toString(amountDX));
					}
					else if(i == 2){
						outputStr  = String.format("亚军  %s", contentsDX);

						BJSCBetAmountWindow.addData(df.format(new Date()), BJSCdrawNumber, outputStr, Integer.toString(amountDX));
					}
					else{
						outputStr  = String.format("第%s名  %s", Integer.toString(i), contentsDX);

						BJSCBetAmountWindow.addData(df.format(new Date()), BJSCdrawNumber, outputStr, Integer.toString(amountDX));
					}
					
				}
				
				if(amountDS != 0 ){
					if(i == 1){
						outputStr  = String.format("冠军  %s", contentsDS);

						BJSCBetAmountWindow.addData(df.format(new Date()), BJSCdrawNumber, outputStr, Integer.toString(amountDS));
					}
					else if(i == 2){
						outputStr  = String.format("亚军  %s", contentsDS);

						BJSCBetAmountWindow.addData(df.format(new Date()), BJSCdrawNumber, outputStr, Integer.toString(amountDS));
					}
					else{
						outputStr  = String.format("第%s名  %s", Integer.toString(i), contentsDS);

						BJSCBetAmountWindow.addData(df.format(new Date()), BJSCdrawNumber, outputStr, Integer.toString(amountDS));
					}
				}
				
				if(amountLH != 0 ){
					if(i == 1){
						outputStr  = String.format("冠军  %s", contentsLH);

						BJSCBetAmountWindow.addData(df.format(new Date()), BJSCdrawNumber, outputStr, Integer.toString(amountLH));
					}
					else if(i == 2){
						outputStr  = String.format("亚军  %s", contentsLH);

						BJSCBetAmountWindow.addData(df.format(new Date()), BJSCdrawNumber, outputStr, Integer.toString(amountLH));
					}
					else{
						outputStr  = String.format("第%s名  %s", Integer.toString(i), contentsLH);

						
						BJSCBetAmountWindow.addData(df.format(new Date()), BJSCdrawNumber, outputStr, Integer.toString(amountLH));
						
					}
					
				}
        		
        	}

        	
        
        	

    	}catch(Exception e){
    		e.printStackTrace();
    	}

    	
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
    
    
    public static void updateBJSCBalance(String str){
    	BJSCdetalsDataWindow.updateTextFieldyue(str);
    }
    
    
    public static String getBalance(){
    	
    	String balanceStr = "---";
    	
    	try{
        	String balanceURI = LanyangHttp.lineuri + "z/user-info";
        	
        	String res = LanyangHttp.doGet(balanceURI, LanyangHttp.strCookies, "");
        	
        	
        	
        	if(res == null){
        		res = LanyangHttp.doGet(balanceURI, LanyangHttp.strCookies, "");
        	}
        	
        	
        	if(res != null){
        		int posStart = res.indexOf("N_Credits");
        		posStart = res.indexOf(">", posStart);
        		int posEnd = res.indexOf("<", posStart);
        		
        		if(posEnd > posStart && posStart >=0){
        			balanceStr = res.substring(posStart+1, posEnd);
        		}
        		
        		balanceStr = balanceStr.trim();
        		
        		
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
    
    
    
    public static boolean isBJSCidle(){
    	boolean isIdle = false;
    	if(remainTime < 0 || remainTime > 30 ){
    		isIdle = true;
    	}
    	
    	return isIdle;
    }
    
}
