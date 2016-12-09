package dsn;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONObject;


public class BetGD11X5Manager {
	
	static long GD11X5loseTime = 0;    //封盘时间
	
	static String GD11X5oddsData = null;
	
	
	static String previousGD11X5drawNumber = "";
	
    static String GD11X5drawNumber = "";
    static String previousGD11X5BetNumber = "";
    
    static boolean previousGD11X5BetResult = false;
    
    static Vector<String> unCalcProfitGD11X5Draw = new Vector<String>();    
    
    
    static int GD11X5betTotalAmount = 0;
    
    static DSNDataDetailsWindow GD11X5detalsDataWindow = new DSNDataDetailsWindow();   
    
    static Vector<String> unknowStatGD11X5Draw = new Vector<String>();    
    
    static DSNBetAmountWindow GD11X5BetAmountWindow = new DSNBetAmountWindow();    
    
    static int GD11X5bishu = 0;    
    static int GD11X5oneBetAmount = 0;    
    
    static int GD11X5zongqishu = 0;
    static int GD11X5zongshibai = 0;
    static int GD11X5zongyichang = 0;
    
    static int GD11X5jinriqishu = 0;
    static int GD11X5jinrishibai = 0;
    static int GD11X5jinriyichang = 0;    
    
    
    
    static long GD11X5remainTime = -1;

	
	public static long getGD11X5remainTime(){
        //get period
    	String response = "";
    	String host = dsnHttp.ADDRESS;
    		
        String getTimeUrl = host + "/time?&_=";
        getTimeUrl += Long.toString(System.currentTimeMillis());
        
        long startTime = System.currentTimeMillis();
        
        response = dsnHttp.doGet(getTimeUrl, "", dsnHttp.ADDRESS + "/member/load?lottery=GD11X5&page=lm");
        
        if(response == null){//再拿一次
        	startTime = System.currentTimeMillis();
        	response = dsnHttp.doGet(getTimeUrl, "", dsnHttp.ADDRESS + "/member/load?lottery=GD11X5&page=lm");
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
    	
        if(!isInGD11X5BetTime(dsnHttp.time)){
        	return -1;
        }
        
        
        String getPeriodUrl = host + "/member/period?lottery=GD11X5&_=";
        getPeriodUrl += Long.toString(System.currentTimeMillis());

        //System.out.println(cookieb18 + "defaultLT=GD11X5;" + cookieCfduid);
        response = dsnHttp.doGet(getPeriodUrl, "", dsnHttp.ADDRESS + "/member/load?lottery=GD11X5&page=lm");
        
        if(response == null){
        	dsnHttp.addFailsTimes();
        	response = dsnHttp.doGet(getPeriodUrl, "", dsnHttp.ADDRESS + "/member/load?lottery=GD11X5&page=lm");
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
            GD11X5loseTime = periodJson.getLong("closeTime");
            if(!GD11X5drawNumber.equals(periodJson.getString("drawNumber"))) {//新的一期
            	previousGD11X5drawNumber = GD11X5drawNumber;
            	GD11X5drawNumber = periodJson.getString("drawNumber");
            	if(previousGD11X5BetNumber != previousGD11X5drawNumber && previousGD11X5BetNumber != GD11X5drawNumber && previousGD11X5BetNumber != "") {//判断上一期有没有漏投
					long dNum = 0;
					try {
					    long drawNum = Long.parseLong(GD11X5drawNumber)%1000;
					    long preBetNum =  Long.parseLong(previousGD11X5BetNumber)%1000;
					    if(drawNum - preBetNum > 0) {
					    	dNum = drawNum - preBetNum - 1;
					    }else if(drawNum - preBetNum  < 0){
					    	dNum = drawNum + 120 - preBetNum - 1;
					    }
					} catch (NumberFormatException e) {
					    e.printStackTrace();
					}
					
					
					//failTimes += dNum;
					
					GD11X5jinrishibai += dNum;					
			        GD11X5jinriqishu += dNum;
			        
			        GD11X5detalsDataWindow.updateTextFieldjinriqishu(Integer.toString(GD11X5jinriqishu));
			        GD11X5detalsDataWindow.updateTextFieldjinrishibai(Integer.toString(GD11X5jinrishibai));
			        
			        GD11X5detalsDataWindow.updateTextFieldzongqishu(Integer.toString(GD11X5zongqishu + GD11X5jinriqishu));
			        GD11X5detalsDataWindow.updateTextFieldzongshibai(Integer.toString(GD11X5zongshibai + GD11X5jinrishibai));
					
					
					
/*					autoBet.labelFailBets.setText("失败次数:" + failTimes);
					autoBet.labelTotalBets.setText("下单次数:" + (successTimes + failTimes));*/
					System.out.println("漏投" + dNum + "次, 期数：" + GD11X5drawNumber + "上次下单期数：" + previousGD11X5BetNumber);
					
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm");//设置日期格式
					long missDrawNumber = Long.parseLong(previousGD11X5BetNumber) + 1;
					for(int i = 0; i < dNum; i++){
						String missDrawNmberstr = Long.toString(missDrawNumber + i);						
		    			GD11X5detalsDataWindow.addData(df.format(new Date()), missDrawNmberstr, 3, "---", "---"); 
					}
					
					
					previousGD11X5BetNumber = previousGD11X5drawNumber;
					
					
			    }
            }
        }
        catch(Exception e){
        	autoBet.outputGUIMessage("获取迪斯尼时间错误！");
        	System.out.println("getGD11X5RemainTime()获取时间异常" + response);
        	return System.currentTimeMillis();
        }
        
        

       
        
        long remainTime = GD11X5loseTime - (System.currentTimeMillis() + dsnHttp.timeDValue); //用差值计算  防止两次请求期间间隔过长
        
        GD11X5remainTime = remainTime;
        
    	return remainTime;
	}
	
	
	
	
    public static boolean  isInGD11X5BetTime(long time){
        Date date = new Date(time);
        int currentHour = date.getHours();
        int currentMinutes = date.getMinutes();
        int currentSeconds = date.getSeconds();
        
        /*if(currentHour >=9 && (currentHour * 60 + currentMinutes <= 23 * 60 + 57)){
        	return true;
        }*/
        
        //两分钟分钟的缓冲
        if((currentHour *60 + currentMinutes > 9*60 + 1) && (currentHour * 60 + currentMinutes <= 23 * 60)){
        	return true;
        }
        
        
        return false;
    }
    
    
    public static long getGD11X5localRemainTime() {
    	GD11X5remainTime = GD11X5loseTime - (System.currentTimeMillis() + dsnHttp.timeDValue);
    	return GD11X5remainTime;
    }
    
    
    public static String getGD11X5oddsData(){
    	String url = dsnHttp.ADDRESS + "/member/odds?lottery=GD11X5&games=DX1%2CDX2%2CDX3%2CDX4%2CDX5%2CZDX%2CZWDX%2CZDS%2CDS1%2CDS2%2CDS3%2CDS4%2CDS5%2CLH&_=";
    	url += Long.toString(System.currentTimeMillis());
    	
    	GD11X5oddsData = dsnHttp.doGet(url, "", "");
    	
    	if(GD11X5oddsData == null){
    		GD11X5oddsData = dsnHttp.doGet(url, "", "");
    	}
    	
    	return  GD11X5oddsData;
   	
    }
    
    
    public static boolean doBetGD11X5(String[] betData, double percent, boolean opposite, String remainTime)
    {

    	String host = dsnHttp.ADDRESS;
       	
        String jsonParam = "";
        
        if(previousGD11X5BetNumber.equals(GD11X5drawNumber)) 
        	return false;
        
        
        GD11X5jinriqishu++;
        
        GD11X5detalsDataWindow.updateTextFieldjinriqishu(Integer.toString(GD11X5jinriqishu));
        
        GD11X5detalsDataWindow.updateTextFieldzongqishu(Integer.toString(GD11X5zongqishu + GD11X5jinriqishu));
        
        //如果未到封盘时间
        if( GD11X5drawNumber != null){
        	
        	//System.out.printf("下注重庆时时彩第%s期\n",GD11X5drawNumber);
        	String outputStr = "下注重庆幸运农场第" + GD11X5drawNumber + "期\n" + "最新数据时间距收盘" + remainTime + "秒\n";
        	autoBet.outputGUIMessage(outputStr);
        	
/*        	if(isEmptyData(betData, BetType.GD11X5)) {
        		outputStr = "代理无人投注\n\n";
        		autoBet.outputGUIMessage(outputStr);
        		return false;
        	}*/
        	
        	jsonParam = constructBetsData(betData, percent, BetType.GD11X5, opposite);
        	
        	//在投注金额窗口显示,只显示百分之一
        	String jsonStr = constructBetsData(betData, 0.01, BetType.GD11X5, opposite);
        	
        	
        	
        	
        	if(jsonParam == "") {
        		
        		previousGD11X5BetNumber = GD11X5drawNumber;
        		
        		outputStr = "代理无人投注\n\n";
        		autoBet.outputGUIMessage(outputStr);
        		
        		
        		
        		return false;
        	}
        
        	
        	addToBetAmountWindow(jsonStr, BetType.GD11X5);
        	
        	outputBetsDetails(jsonParam);	
        	
        	System.out.println(jsonParam);
        	
        	String response = "";
        	
        	

        	

        	long time1 = System.currentTimeMillis();
        	
        	response = dsnHttp.bet(host + "/member/bet", jsonParam, "UTF-8", "");
        	
        	
        	boolean betRes = isBetSuccess(GD11X5drawNumber);
        	
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
    			GD11X5detalsDataWindow.addData(df.format(new Date()), GD11X5drawNumber, 2, Integer.toString(GD11X5betTotalAmount), Integer.toString(GD11X5bishu));

    			unknowStatGD11X5Draw.add(GD11X5drawNumber);
    			
    			GD11X5detalsDataWindow.updateTextFieldjinriyichang(Integer.toString(unknowStatGD11X5Draw.size()));
    			
        	}
        	else{
    			GD11X5detalsDataWindow.addData(df.format(new Date()), GD11X5drawNumber, 0, Integer.toString(GD11X5betTotalAmount), Integer.toString(GD11X5bishu));        		
        	}
        	
        	unCalcProfitGD11X5Draw.add(GD11X5drawNumber);
        	

        	    
        	if(!previousGD11X5BetNumber.equals(GD11X5drawNumber)) { //避免重复计数
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
			
			previousGD11X5BetNumber = GD11X5drawNumber;
        	previousGD11X5BetResult = result;
        	
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


	    		
    		if(GD11X5oddsData == null){
    			getGD11X5oddsData();
    		}
	    		
	    	oddsData = GD11X5oddsData;
	    	
	    	
	    	oddsGrabData = new JSONObject(oddsData);
	    	
	    	for(int i = 0; i < data.length; i++){
    		
    		
            	JSONArray GD11X5LMGrabData = new JSONArray(data[i]);        	
            	JSONArray gamesGrabData = GD11X5LMGrabData.getJSONArray(0);

        	
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
	    	

        	betsObj.put("drawNumber",GD11X5drawNumber);
        	
        	betsObj.put("lottery", "GD11X5");

	    	
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
    
    public static void showGD11X5DeatilsTable(){
    	GD11X5detalsDataWindow.setVisible(true);
    }
    
    public static String getGD11X5drawNumber(){
    	return GD11X5drawNumber;
    }
    
    
    public static void outputBetsDetails(String jsonData){
    	
    	autoBet.outputGUIMessage("下注详情：\n");
    	try{

            	
        	JSONObject betsData = new JSONObject(jsonData);
        	JSONArray gamesData = betsData.getJSONArray("bets");
        	int totalAmount = 0;
        	
        	GD11X5bishu = gamesData.length();
        	
        	for(int i = 1; i <= 8 ; i++){
        		String gameDX = "DX" + Integer.toString(i);
        		String gameDS = "DS" + Integer.toString(i);
        		
        		String gameWDX = "WDX" + Integer.toString(i);
        		String gameHDS = "HDS" + Integer.toString(i);

        		
        		JSONObject gameData;
        		
        		int amountDX = 0;
        		String contentsDX = "";
        		
        		int amountDS = 0;
        		String contentsDS = "";
        		
        		int amountWDX = 0;
        		String contentsWDX = "";
        		
        		int amountHDS = 0;
        		String contentsHDS = "";
        		

        		
        		
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
        			
        			if(game.equals(gameWDX)){
        				amountWDX = gameData.getInt("amount");  
        				contentsWDX = gameData.getString("contents");
        				contentsWDX = contentsWDX.equals("D")?"尾大":"尾小";
        			}
        			
        			if(game.equals(gameHDS)){
        				amountHDS = gameData.getInt("amount");  
        				contentsHDS = gameData.getString("contents");
        				contentsHDS = contentsHDS.equals("D")?"合单":"合双";
        			}
        			


        		}
        		
        		
    			String outputStr = "";
    			
				if(amountDX != 0 ){
						outputStr  = String.format("第%s球   %s: %d,", Integer.toString(i), contentsDX, amountDX);
						autoBet.outputGUIMessage(outputStr);
						totalAmount += amountDX;
				}
				
				if(amountDS != 0 ){
					outputStr  = String.format("第%s球   %s: %d,", Integer.toString(i), contentsDS, amountDS);
					autoBet.outputGUIMessage(outputStr);
					totalAmount += amountDS;
				}
				
				if(amountWDX != 0 ){
					outputStr  = String.format("第%s球   %s: %d,", Integer.toString(i), contentsWDX, amountWDX);
					autoBet.outputGUIMessage(outputStr);
					totalAmount += amountWDX;
				}
				
				if(amountHDS != 0 ){
					outputStr  = String.format("第%s球   %s: %d,", Integer.toString(i), contentsHDS, amountHDS);
					autoBet.outputGUIMessage(outputStr);
					totalAmount += amountHDS;
				}
				

				
				autoBet.outputGUIMessage("\n");
        		
        	}
        	
        	String gameZDX = "ZDX";
        	String gameZDS = "ZDS";
        	String gameZWDX = "ZWDX";
        	
        	
        	int amountZDX = 0;
        	int amountZDS = 0;
        	int amountZWDX = 0;
        	
        	String contentsZDX = "";
        	String contentsZDS = "";
        	String contentsZWDX = "";
        	
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
    			if(game.equals(gameZWDX)){
    				amountZWDX = gameData.getInt("amount");  
    				contentsZWDX = gameData.getString("contents");
    				contentsZWDX = contentsZWDX.equals("D")?"尾大":"尾小";
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
			
			if(amountZWDX != 0){
				outputStr  = String.format("总%s: %d,",  contentsZWDX, amountZWDX);
				autoBet.outputGUIMessage(outputStr);
				totalAmount += amountZWDX;
			}
			
			autoBet.outputGUIMessage("\n");
			autoBet.outputGUIMessage("下单总金额:" + totalAmount +"\n");
			
			GD11X5betTotalAmount = totalAmount;
    			
        	
    	}catch(Exception e){
    		e.printStackTrace();
    	}

    	
    }
    
    
    
    public static void addToBetAmountWindow(String jsonData, BetType betType){
    	
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm");//设置日期格式
    	
    	
    	try{


        	JSONObject betsData = new JSONObject(jsonData);
        	JSONArray gamesData = betsData.getJSONArray("bets");
        	int totalAmount = 0;
        	
        	GD11X5bishu = gamesData.length();
        	
        	for(int i = 1; i <= 5 ; i++){
        		String gameDX = "DX" + Integer.toString(i);
        		String gameDS = "DS" + Integer.toString(i);
        		
        		String gameWDX = "WDX" + Integer.toString(i);
        		String gameHDS = "HDS" + Integer.toString(i);

        		
        		JSONObject gameData;
        		
        		int amountDX = 0;
        		String contentsDX = "";
        		
        		int amountDS = 0;
        		String contentsDS = "";
        		
        		int amountWDX = 0;
        		String contentsWDX = "";
        		
        		int amountHDS = 0;
        		String contentsHDS = "";
        		

        		
        		
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
        			
        			if(game.equals(gameWDX)){
        				amountWDX = gameData.getInt("amount");  
        				contentsWDX = gameData.getString("contents");
        				contentsWDX = contentsWDX.equals("D")?"尾大":"尾小";
        			}
        			
        			if(game.equals(gameHDS)){
        				amountHDS = gameData.getInt("amount");  
        				contentsHDS = gameData.getString("contents");
        				contentsHDS = contentsHDS.equals("D")?"合单":"合双";
        			}
        			


        		}
        		
        		
    			String outputStr = "";
    			
				if(amountDX != 0 ){
						outputStr  = String.format("第%s球   %s", Integer.toString(i), contentsDX);

						GD11X5BetAmountWindow.addData(df.format(new Date()), GD11X5drawNumber, outputStr, Integer.toString(amountDX));
				}
				
				if(amountDS != 0 ){
					outputStr  = String.format("第%s球   %s", Integer.toString(i), contentsDS);
					GD11X5BetAmountWindow.addData(df.format(new Date()), GD11X5drawNumber, outputStr, Integer.toString(amountDS));
				}
				
				if(amountWDX != 0 ){
					outputStr  = String.format("第%s球   %s", Integer.toString(i), contentsWDX);
					GD11X5BetAmountWindow.addData(df.format(new Date()), GD11X5drawNumber, outputStr, Integer.toString(amountWDX));
				}
				
				if(amountHDS != 0 ){
					outputStr  = String.format("第%s球   %s", Integer.toString(i), contentsHDS);
					GD11X5BetAmountWindow.addData(df.format(new Date()), GD11X5drawNumber, outputStr, Integer.toString(amountHDS));
				}
				

				
				
        		
        	}
        	
        	String gameZDX = "ZDX";
        	String gameZDS = "ZDS";
        	String gameZWDX = "ZWDX";
        	
        	
        	int amountZDX = 0;
        	int amountZDS = 0;
        	int amountZWDX = 0;
        	
        	String contentsZDX = "";
        	String contentsZDS = "";
        	String contentsZWDX = "";
        	
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
    			if(game.equals(gameZWDX)){
    				amountZWDX = gameData.getInt("amount");  
    				contentsZWDX = gameData.getString("contents");
    				contentsZWDX = contentsZWDX.equals("D")?"尾大":"尾小";
    			}
	
    		}
    		
			String outputStr = "";
			if(amountZDX != 0){
				outputStr  = String.format("总%s",  contentsZDX);
				GD11X5BetAmountWindow.addData(df.format(new Date()), GD11X5drawNumber, outputStr, Integer.toString(amountZDX));
			}
			
			if(amountZDS != 0){
				outputStr  = String.format("总%s",  contentsZDS);
				GD11X5BetAmountWindow.addData(df.format(new Date()), GD11X5drawNumber, outputStr, Integer.toString(amountZDS));
			}
			
			if(amountZWDX != 0){
				outputStr  = String.format("总%s",  contentsZWDX);
				GD11X5BetAmountWindow.addData(df.format(new Date()), GD11X5drawNumber, outputStr, Integer.toString(amountZWDX));
			}
			
			autoBet.outputGUIMessage("\n");
			autoBet.outputGUIMessage("下单总金额:" + totalAmount +"\n");
			
			GD11X5betTotalAmount = totalAmount;

    			
        	
    	}catch(Exception e){
    		e.printStackTrace();
    	}

    	
    }
    
    
    public static Vector<String> getUnCalcProfitBJSCDraw(){
    	return unCalcProfitGD11X5Draw;
    }
    
    public static void updateUnCalcGD11X5Draw(Vector<String> calcedDraw){
    	for(int i =0; i < calcedDraw.size(); i++){
    		unCalcProfitGD11X5Draw.removeElement(calcedDraw.elementAt(i));
    		unknowStatGD11X5Draw.removeElement(calcedDraw.elementAt(i));
    	}  
    	
    	
    	long currentDraw = Long.parseLong(GD11X5drawNumber);
    	
    	for(int j =0; j < unCalcProfitGD11X5Draw.size(); j++){
    		long idrawNumber = Long.parseLong(unCalcProfitGD11X5Draw.elementAt(j));
    		if((currentDraw - idrawNumber) >= 4){
    			unCalcProfitGD11X5Draw.removeElement(unCalcProfitGD11X5Draw.elementAt(j));
    		}
    	}
    	
    	
    	int yichangshu1 = unknowStatGD11X5Draw.size();
    	
    	for(int j =0; j < unknowStatGD11X5Draw.size(); j++){
    		long idrawNumber = Long.parseLong(unknowStatGD11X5Draw.elementAt(j));
    		if((currentDraw - idrawNumber) >= 4){
    			updateGD11X5WindowdetailsData(unknowStatGD11X5Draw.elementAt(j), TYPEINDEX.STATC.ordinal(), "1");
    			unknowStatGD11X5Draw.removeElement(unknowStatGD11X5Draw.elementAt(j));

    		}
    	}
    	
    	int yichangshu2 = unknowStatGD11X5Draw.size();
    	
    	GD11X5jinriyichang = unknowStatGD11X5Draw.size();
    	
    	GD11X5jinrishibai += yichangshu1 - yichangshu2;
    	
    	
    	
    	
    	
    	GD11X5detalsDataWindow.updateTextFieldjinrishibai(Integer.toString(GD11X5jinrishibai));
    	GD11X5detalsDataWindow.updateTextFieldjinriyichang(Integer.toString(GD11X5jinriyichang));
    	
    	
    	
    	GD11X5detalsDataWindow.updateTextFieldzongqishu(Integer.toString(GD11X5zongqishu + GD11X5jinriqishu));
    	
    	GD11X5detalsDataWindow.updateTextFieldzongshibai(Integer.toString(GD11X5zongshibai + GD11X5jinrishibai));
    	
    	
    }
    
    public static void updateGD11X5WindowdetailsData(String drawNumber, int index, String value){
    	GD11X5detalsDataWindow.updateRowItem(drawNumber, index, value);
    }
    
    public static void showGD11X5BetAmountTable(){
    	GD11X5BetAmountWindow.setVisible(true);
    }
    
    public static void updateGD11X5Balance(String str){
    	GD11X5detalsDataWindow.updateTextFieldyue(str);
    }
    
    
    public static boolean isGD11X5idle(){
    	boolean isIdle = false;
    	if(GD11X5remainTime < 0 || GD11X5remainTime > 40*1000 ){
    		isIdle = true;
    	}
    	
    	return isIdle;
    }
    
    
    public static void clearGD11X5detalsData(){
    	if(unCalcProfitGD11X5Draw.size() != 0){
    		unCalcProfitGD11X5Draw.clear();
    	}
    	
    	if(unknowStatGD11X5Draw.size() != 0){
    		GD11X5jinrishibai += unknowStatGD11X5Draw.size();
    		unknowStatGD11X5Draw.clear();
    	}

    	GD11X5zongqishu += GD11X5jinriqishu;
    	GD11X5zongshibai += GD11X5jinrishibai;
    	

    	
    	GD11X5jinriqishu = 0;
    	GD11X5jinrishibai = 0;
    	GD11X5jinriyichang = 0;
    	
    	GD11X5remainTime = -1;

    }
    
    
}
