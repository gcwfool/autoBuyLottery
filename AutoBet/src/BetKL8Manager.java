import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONObject;


public class BetKL8Manager {
	
	static long KL8loseTime = 0;    //封盘时间
	
	static String KL8oddsData = null;
	
	
	static String previousKL8drawNumber = "";
	
    static String KL8drawNumber = "";
    static String previousKL8BetNumber = "";
    
    static boolean previousKL8BetResult = false;
    
    static Vector<String> unCalcProfitKL8Draw = new Vector<String>();    
    
    
    static int KL8betTotalAmount = 0;
    
    static DSNDataDetailsWindow KL8detalsDataWindow = new DSNDataDetailsWindow();   
    
    static Vector<String> unknowStatKL8Draw = new Vector<String>();    
    
    static DSNBetAmountWindow KL8BetAmountWindow = new DSNBetAmountWindow();    
    
    static int KL8bishu = 0;    
    static int KL8oneBetAmount = 0;    
    
    static int KL8zongqishu = 0;
    static int KL8zongshibai = 0;
    static int KL8zongyichang = 0;
    
    static int KL8jinriqishu = 0;
    static int KL8jinrishibai = 0;
    static int KL8jinriyichang = 0;    
    
    
    
    static long KL8remainTime = -1;

	
	public static long getKL8remainTime(){
        //get period
    	String response = "";
    	String host = dsnHttp.ADDRESS;
    		
        String getTimeUrl = host + "/time?&_=";
        getTimeUrl += Long.toString(System.currentTimeMillis());
        
        long startTime = System.currentTimeMillis();
        
        response = dsnHttp.doGet(getTimeUrl, "", dsnHttp.ADDRESS + "/member/load?lottery=KL8&page=lm");
        
        if(response == null){//再拿一次
        	startTime = System.currentTimeMillis();
        	response = dsnHttp.doGet(getTimeUrl, "", dsnHttp.ADDRESS + "/member/load?lottery=KL8&page=lm");
        }
        
        long endTime = System.currentTimeMillis();
        
        long requestTime = endTime - startTime;
        
        requestTime = requestTime /2;
        
        
        if(response != null && Common.isNum(response))
        {
        	dsnHttp.time = Long.parseLong(response);
        	dsnHttp.timeDValue = dsnHttp.time + requestTime - System.currentTimeMillis();
        }
        else{
        	dsnHttp.time = System.currentTimeMillis();
        }
    	
        if(!isInKL8BetTime(dsnHttp.time)){
        	return -1;
        }
        
        
        String getPeriodUrl = host + "/member/period?lottery=KL8&_=";
        getPeriodUrl += Long.toString(System.currentTimeMillis());

        //System.out.println(cookieb18 + "defaultLT=KL8;" + cookieCfduid);
        response = dsnHttp.doGet(getPeriodUrl, "", dsnHttp.ADDRESS + "/member/load?lottery=KL8&page=lm");
        
        if(response == null){
        	dsnHttp.addFailsTimes();
        	response = dsnHttp.doGet(getPeriodUrl, "", dsnHttp.ADDRESS + "/member/load?lottery=KL8&page=lm");
        }
        
        if(response == null)
        {
        	dsnHttp.addFailsTimes();
        	System.out.println("get period failed");
        	return System.currentTimeMillis();
        }
        
        
        
        System.out.println("preiod:");
        System.out.println(response);
        
        
        try{
            JSONObject periodJson = new JSONObject(response);
            KL8loseTime = periodJson.getLong("closeTime");
            if(!KL8drawNumber.equals(periodJson.getString("drawNumber"))) {//新的一期
            	previousKL8drawNumber = KL8drawNumber;
            	KL8drawNumber = periodJson.getString("drawNumber");
            	if(previousKL8BetNumber != previousKL8drawNumber && previousKL8BetNumber != KL8drawNumber && previousKL8BetNumber != "") {//判断上一期有没有漏投
					long dNum = 0;
					try {
					    long drawNum = Long.parseLong(KL8drawNumber)%1000;
					    long preBetNum =  Long.parseLong(previousKL8BetNumber)%1000;
					    if(drawNum - preBetNum > 0) {
					    	dNum = drawNum - preBetNum - 1;
					    }else if(drawNum - preBetNum  < 0){
					    	dNum = drawNum + 120 - preBetNum - 1;
					    }
					} catch (NumberFormatException e) {
					    e.printStackTrace();
					}
					
					
					//failTimes += dNum;
					
					KL8jinrishibai += dNum;					
			        KL8jinriqishu += dNum;
			        
			        KL8detalsDataWindow.updateTextFieldjinriqishu(Integer.toString(KL8jinriqishu));
			        KL8detalsDataWindow.updateTextFieldjinrishibai(Integer.toString(KL8jinrishibai));
			        
			        KL8detalsDataWindow.updateTextFieldzongqishu(Integer.toString(KL8zongqishu + KL8jinriqishu));
			        KL8detalsDataWindow.updateTextFieldzongshibai(Integer.toString(KL8zongshibai + KL8jinrishibai));
					
					
					
/*					autoBet.labelFailBets.setText("失败次数:" + failTimes);
					autoBet.labelTotalBets.setText("下单次数:" + (successTimes + failTimes));*/
					System.out.println("漏投" + dNum + "次, 期数：" + KL8drawNumber + "上次下单期数：" + previousKL8BetNumber);
					
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm");//设置日期格式
					long missDrawNumber = Long.parseLong(previousKL8BetNumber) + 1;
					for(int i = 0; i < dNum; i++){
						String missDrawNmberstr = Long.toString(missDrawNumber + i);						
		    			KL8detalsDataWindow.addData(df.format(new Date()), missDrawNmberstr, 3, "---", "---"); 
					}
					
					
					previousKL8BetNumber = previousKL8drawNumber;
					
					
			    }
            }
        }
        catch(Exception e){
        	autoBet.outputGUIMessage("获取迪斯尼时间错误！");
        	System.out.println("getKL8RemainTime()获取时间异常" + response);
        	return System.currentTimeMillis();
        }
        
        

       
        
        long remainTime = KL8loseTime - (System.currentTimeMillis() + dsnHttp.timeDValue); //用差值计算  防止两次请求期间间隔过长
        
        KL8remainTime = remainTime;
        
    	return remainTime;
	}
	
	
	
	
    public static boolean  isInKL8BetTime(long time){
        Date date = new Date(time);
        int currentHour = date.getHours();
        int currentMinutes = date.getMinutes();
        int currentSeconds = date.getSeconds();
        
        /*if(currentHour >=9 && (currentHour * 60 + currentMinutes <= 23 * 60 + 57)){
        	return true;
        }*/
        
        //两分钟分钟的缓冲
        if((currentHour *60 + currentMinutes > 9*60) && (currentHour * 60 + currentMinutes < 23 * 60 + 55)){
      		return true;
      }
       
      return false;
    }
    
    
    public static long getKL8localRemainTime() {
    	KL8remainTime = KL8loseTime - (System.currentTimeMillis() + dsnHttp.timeDValue);
    	return KL8remainTime;
    }
    
    
    public static String getKL8oddsData(){
    	String url = dsnHttp.ADDRESS + "/member/odds?lottery=KL8&games=ZDX%2CZDS&_=";
    	url += Long.toString(System.currentTimeMillis());
    	
    	KL8oddsData = dsnHttp.doGet(url, "", "");
    	
    	if(KL8oddsData == null){
    		KL8oddsData = dsnHttp.doGet(url, "", "");
    	}
    	
    	return  KL8oddsData;
   	
    }
    
    
    public static boolean doBetKL8(String[] betData, double percent, boolean opposite, String remainTime)
    {

    	String host = dsnHttp.ADDRESS;
       	
        String jsonParam = "";
        
        if(previousKL8BetNumber.equals(KL8drawNumber)) 
        	return false;
        
        
        KL8jinriqishu++;
        
        KL8detalsDataWindow.updateTextFieldjinriqishu(Integer.toString(KL8jinriqishu));
        
        KL8detalsDataWindow.updateTextFieldzongqishu(Integer.toString(KL8zongqishu + KL8jinriqishu));
        
        //如果未到封盘时间
        if( KL8drawNumber != null){
        	
        	//System.out.printf("下注重庆时时彩第%s期\n",KL8drawNumber);
        	String outputStr = "下注快乐8第" + KL8drawNumber + "期\n" + "最新数据时间距收盘" + remainTime + "秒\n";
        	autoBet.outputGUIMessage(outputStr);
        	
/*        	if(isEmptyData(betData, BetType.KL8)) {
        		outputStr = "代理无人投注\n\n";
        		autoBet.outputGUIMessage(outputStr);
        		return false;
        	}*/
        	
        	jsonParam = constructBetsData(betData, percent, BetType.KL8, opposite);
        	
        	//在投注金额窗口显示,只显示百分之一
        	String jsonStr = constructBetsData(betData, 0.01, BetType.KL8, opposite);
        	
        	
        	
        	
        	if(jsonParam == "") {
        		
        		previousKL8BetNumber = KL8drawNumber;
        		
        		outputStr = "代理无人投注\n\n";
        		autoBet.outputGUIMessage(outputStr);

        		return false;
        	}
        
        	addToBetAmountWindow(jsonStr, BetType.KL8);
        	
        	outputBetsDetails(jsonParam);	
        	
        	System.out.println(jsonParam);
        	
        	String response = "";
        	
        	

        	

        	long time1 = System.currentTimeMillis();
        	
        	response = dsnHttp.bet(host + "/member/bet", jsonParam, "UTF-8", "");
        	
        	
        	boolean betRes = isBetSuccess(KL8drawNumber);
        	
        	boolean result = dsnHttp.pureParseBetResult(response);
        	
        	if((betRes == false) && (result == false)){
        		response = dsnHttp.bet(host + "/member/bet", jsonParam, "UTF-8", "");
        	}
        	

        	long time2 = System.currentTimeMillis();
        	
        	long timeD = time2 - time1;
        	
        	double usingTime = timeD;
        	
        	usingTime = usingTime/1000;
        	
        	String strUsingTime  = String.format("下单用时！ :%f 秒\n", usingTime);
        	
        	autoBet.outputGUIMessage(strUsingTime);

        	
        	result = dsnHttp.parseBetResult(response);
        	
        	
        	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm");//设置日期格式
        	
        	if(result == false){
    			KL8detalsDataWindow.addData(df.format(new Date()), KL8drawNumber, 2, Integer.toString(KL8betTotalAmount), Integer.toString(KL8bishu));

    			unknowStatKL8Draw.add(KL8drawNumber);
    			
    			KL8detalsDataWindow.updateTextFieldjinriyichang(Integer.toString(unknowStatKL8Draw.size()));
    			
        	}
        	else{
    			KL8detalsDataWindow.addData(df.format(new Date()), KL8drawNumber, 0, Integer.toString(KL8betTotalAmount), Integer.toString(KL8bishu));        		
        	}
        	
        	unCalcProfitKL8Draw.add(KL8drawNumber);
        	

        	    
        	if(!previousKL8BetNumber.equals(KL8drawNumber)) { //避免重复计数
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
			
			previousKL8BetNumber = KL8drawNumber;
        	previousKL8BetResult = result;
        	
        	return result;
        
        }
        
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


	    		
    		if(KL8oddsData == null){
    			getKL8oddsData();
    		}
	    		
	    	oddsData = KL8oddsData;
	    	
	    	
	    	oddsGrabData = new JSONObject(oddsData);
	    	
	    	for(int i = 0; i < data.length; i++){
    		
    		
            	JSONArray KL8LMGrabData = new JSONArray(data[i]);        	
            	JSONArray gamesGrabData = KL8LMGrabData.getJSONArray(0);

        	
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
	    			/*if(game.indexOf("GDX") != -1 || game.indexOf("GDS") != -1)
	    				continue;*/
	    			
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
	    	

        	betsObj.put("drawNumber",KL8drawNumber);
        	
        	betsObj.put("lottery", "KL8");

	    	
	    	res = betsObj.toString();
    	
    	}catch(Exception e){
    		autoBet.outputGUIMessage("构造下单数据错误！\n");
    		return "";
    	}
   	
    	return res;	
    }
    
    
    
    public static boolean isBetSuccess(String drawNumber){
    	return true;
    }
    
    public static void showKL8DeatilsTable(){
    	KL8detalsDataWindow.setVisible(true);
    }
    
    public static String getKL8drawNumber(){
    	return KL8drawNumber;
    }
    
    
    public static void outputBetsDetails(String jsonData){
    	
    	autoBet.outputGUIMessage("下注详情：\n");
    	try{

            	
        	JSONObject betsData = new JSONObject(jsonData);
        	JSONArray gamesData = betsData.getJSONArray("bets");
        	int totalAmount = 0;
        	

        	
        	
        	String gameZDX = "ZDX";
        	String gameZDS = "ZDS";
        	String gameLH = "LH";
        	
        	int amountZDX = 0;
        	int amountZDS = 0;

        	
        	String contentsZDX = "";
        	String contentsZDS = "";
        	
        	
        	KL8bishu = gamesData.length();

        	
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

			autoBet.outputGUIMessage("\n");
			autoBet.outputGUIMessage("下单总金额:" + totalAmount +"\n");
			
			KL8betTotalAmount = totalAmount;
    			
        	
    	}catch(Exception e){
    		e.printStackTrace();
    	}

    	
    }
    
    
    
    public static void addToBetAmountWindow(String jsonData, BetType betType){
    	
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm");//设置日期格式
    	
    	
    	try{


        	JSONObject betsData = new JSONObject(jsonData);
        	JSONArray gamesData = betsData.getJSONArray("bets");

        	
        	String gameZDX = "ZDX";
        	String gameZDS = "ZDS";

        	
        	int amountZDX = 0;
        	int amountZDS = 0;
        	int amountLH = 0;
        	
        	String contentsZDX = "";
        	String contentsZDS = "";

        	
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

	
    		}
    		
			String outputStr = "";
			if(amountZDX != 0){
				outputStr  = String.format("总%s",  contentsZDX);
				
				
				KL8BetAmountWindow.addData(df.format(new Date()), KL8drawNumber, outputStr, Integer.toString(amountZDX));
				
				
			}
			
			if(amountZDS != 0){
				outputStr  = String.format("总%s",  contentsZDS);
				KL8BetAmountWindow.addData(df.format(new Date()), KL8drawNumber, outputStr, Integer.toString(amountZDS));
				
			}
			

        	
    	}catch(Exception e){
    		e.printStackTrace();
    	}

    	
    }
    
    
    public static Vector<String> getUnCalcProfitBJSCDraw(){
    	return unCalcProfitKL8Draw;
    }
    
    public static void updateUnCalcKL8Draw(Vector<String> calcedDraw){
    	for(int i =0; i < calcedDraw.size(); i++){
    		unCalcProfitKL8Draw.removeElement(calcedDraw.elementAt(i));
    		unknowStatKL8Draw.removeElement(calcedDraw.elementAt(i));
    	}  
    	
    	
    	long currentDraw = Long.parseLong(KL8drawNumber);
    	
    	for(int j =0; j < unCalcProfitKL8Draw.size(); j++){
    		long idrawNumber = Long.parseLong(unCalcProfitKL8Draw.elementAt(j));
    		if((currentDraw - idrawNumber) >= 4){
    			unCalcProfitKL8Draw.removeElement(unCalcProfitKL8Draw.elementAt(j));
    		}
    	}
    	
    	
    	int yichangshu1 = unknowStatKL8Draw.size();
    	
    	for(int j =0; j < unknowStatKL8Draw.size(); j++){
    		long idrawNumber = Long.parseLong(unknowStatKL8Draw.elementAt(j));
    		if((currentDraw - idrawNumber) >= 4){
    			updateKL8WindowdetailsData(unknowStatKL8Draw.elementAt(j), TYPEINDEX.STATC.ordinal(), "1");
    			unknowStatKL8Draw.removeElement(unknowStatKL8Draw.elementAt(j));

    		}
    	}
    	
    	int yichangshu2 = unknowStatKL8Draw.size();
    	
    	KL8jinriyichang = unknowStatKL8Draw.size();
    	
    	KL8jinrishibai += yichangshu1 - yichangshu2;
    	
    	
    	
    	
    	
    	KL8detalsDataWindow.updateTextFieldjinrishibai(Integer.toString(KL8jinrishibai));
    	KL8detalsDataWindow.updateTextFieldjinriyichang(Integer.toString(KL8jinriyichang));
    	
    	
    	
    	KL8detalsDataWindow.updateTextFieldzongqishu(Integer.toString(KL8zongqishu + KL8jinriqishu));
    	
    	KL8detalsDataWindow.updateTextFieldzongshibai(Integer.toString(KL8zongshibai + KL8jinrishibai));
    	
    	
    }
    
    public static void updateKL8WindowdetailsData(String drawNumber, int index, String value){
    	KL8detalsDataWindow.updateRowItem(drawNumber, index, value);
    }
    
    public static void showKL8BetAmountTable(){
    	KL8BetAmountWindow.setVisible(true);
    }
    
    public static void updateKL8Balance(String str){
    	KL8detalsDataWindow.updateTextFieldyue(str);
    }
    
    
    public static boolean isKL8idle(){
    	boolean isIdle = false;
    	if(KL8remainTime < 0 || KL8remainTime > 25*1000 ){
    		isIdle = true;
    	}
    	
    	return isIdle;
    }
    
    
    public static void clearKL8detalsData(){
    	if(unCalcProfitKL8Draw.size() != 0){
    		unCalcProfitKL8Draw.clear();
    	}
    	
    	if(unknowStatKL8Draw.size() != 0){
    		KL8jinrishibai += unknowStatKL8Draw.size();
    		unknowStatKL8Draw.clear();
    	}

    	KL8zongqishu += KL8jinriqishu;
    	KL8zongshibai += KL8jinrishibai;
    	

    	
    	KL8jinriqishu = 0;
    	KL8jinrishibai = 0;
    	KL8jinriyichang = 0;
    	
    	KL8remainTime = -1;

    }
    
    
}
