import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONObject;


public class BetGDKLSFManager {
	
	static long GDKLSFloseTime = 0;    //封盘时间
	
	static String GDKLSFoddsData = null;
	
	
	static String previousGDKLSFdrawNumber = "";
	
    static String GDKLSFdrawNumber = "";
    static String previousGDKLSFBetNumber = "";
    
    static boolean previousGDKLSFBetResult = false;
    
    static Vector<String> unCalcProfitGDKLSFDraw = new Vector<String>();    
    
    
    static int GDKLSFbetTotalAmount = 0;
    
    static DSNDataDetailsWindow GDKLSFdetalsDataWindow = new DSNDataDetailsWindow();   
    
    static Vector<String> unknowStatGDKLSFDraw = new Vector<String>();    
    
    static DSNBetAmountWindow GDKLSFBetAmountWindow = new DSNBetAmountWindow();    
    
    static int GDKLSFbishu = 0;    
    static int GDKLSFoneBetAmount = 0;    
    
    static int GDKLSFzongqishu = 0;
    static int GDKLSFzongshibai = 0;
    static int GDKLSFzongyichang = 0;
    
    static int GDKLSFjinriqishu = 0;
    static int GDKLSFjinrishibai = 0;
    static int GDKLSFjinriyichang = 0;    
    
    
    
    static long GDKLSFremainTime = -1;

	
	public static long getGDKLSFremainTime(){
        //get period
    	String response = "";
    	String host = dsnHttp.ADDRESS;
    		
        String getTimeUrl = host + "/time?&_=";
        getTimeUrl += Long.toString(System.currentTimeMillis());
        
        long startTime = System.currentTimeMillis();
        
        response = dsnHttp.doGet(getTimeUrl, "", dsnHttp.ADDRESS + "/member/load?lottery=GDKLSF&page=lm");
        
        if(response == null){//再拿一次
        	startTime = System.currentTimeMillis();
        	response = dsnHttp.doGet(getTimeUrl, "", dsnHttp.ADDRESS + "/member/load?lottery=GDKLSF&page=lm");
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
    	
        if(!isInGDKLSFBetTime(dsnHttp.time)){
        	return -1;
        }
        
        
        String getPeriodUrl = host + "/member/period?lottery=GDKLSF&_=";
        getPeriodUrl += Long.toString(System.currentTimeMillis());

        //System.out.println(cookieb18 + "defaultLT=GDKLSF;" + cookieCfduid);
        response = dsnHttp.doGet(getPeriodUrl, "", dsnHttp.ADDRESS + "/member/load?lottery=GDKLSF&page=lm");
        
        if(response == null){
        	dsnHttp.addFailsTimes();
        	response = dsnHttp.doGet(getPeriodUrl, "", dsnHttp.ADDRESS + "/member/load?lottery=GDKLSF&page=lm");
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
            GDKLSFloseTime = periodJson.getLong("closeTime");
            if(!GDKLSFdrawNumber.equals(periodJson.getString("drawNumber"))) {//新的一期
            	previousGDKLSFdrawNumber = GDKLSFdrawNumber;
            	GDKLSFdrawNumber = periodJson.getString("drawNumber");
            	if(previousGDKLSFBetNumber != previousGDKLSFdrawNumber && previousGDKLSFBetNumber != GDKLSFdrawNumber && previousGDKLSFBetNumber != "") {//判断上一期有没有漏投
					long dNum = 0;
					try {
					    long drawNum = Long.parseLong(GDKLSFdrawNumber)%1000;
					    long preBetNum =  Long.parseLong(previousGDKLSFBetNumber)%1000;
					    if(drawNum - preBetNum > 0) {
					    	dNum = drawNum - preBetNum - 1;
					    }else if(drawNum - preBetNum  < 0){
					    	dNum = drawNum + 120 - preBetNum - 1;
					    }
					} catch (NumberFormatException e) {
					    e.printStackTrace();
					}
					
					
					//failTimes += dNum;
					
					GDKLSFjinrishibai += dNum;					
			        GDKLSFjinriqishu += dNum;
			        
			        GDKLSFdetalsDataWindow.updateTextFieldjinriqishu(Integer.toString(GDKLSFjinriqishu));
			        GDKLSFdetalsDataWindow.updateTextFieldjinrishibai(Integer.toString(GDKLSFjinrishibai));
			        
			        GDKLSFdetalsDataWindow.updateTextFieldzongqishu(Integer.toString(GDKLSFzongqishu + GDKLSFjinriqishu));
			        GDKLSFdetalsDataWindow.updateTextFieldzongshibai(Integer.toString(GDKLSFzongshibai + GDKLSFjinrishibai));
					
					
					
/*					autoBet.labelFailBets.setText("失败次数:" + failTimes);
					autoBet.labelTotalBets.setText("下单次数:" + (successTimes + failTimes));*/
					System.out.println("漏投" + dNum + "次, 期数：" + GDKLSFdrawNumber + "上次下单期数：" + previousGDKLSFBetNumber);
					
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm");//设置日期格式
					long missDrawNumber = Long.parseLong(previousGDKLSFBetNumber) + 1;
					for(int i = 0; i < dNum; i++){
						String missDrawNmberstr = Long.toString(missDrawNumber + i);						
		    			GDKLSFdetalsDataWindow.addData(df.format(new Date()), missDrawNmberstr, 3, "---", "---"); 
					}
					
					
					previousGDKLSFBetNumber = previousGDKLSFdrawNumber;
					
					
			    }
            }
        }
        catch(Exception e){
        	autoBet.outputGUIMessage("获取迪斯尼时间错误！");
        	System.out.println("getGDKLSFRemainTime()获取时间异常" + response);
        	return System.currentTimeMillis();
        }
        
        

       
        
        long remainTime = GDKLSFloseTime - (System.currentTimeMillis() + dsnHttp.timeDValue); //用差值计算  防止两次请求期间间隔过长
        
        GDKLSFremainTime = remainTime;
        
    	return remainTime;
	}
	
	
	
	
    public static boolean  isInGDKLSFBetTime(long time){
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
    
    
    public static long getGDKLSFlocalRemainTime() {
    	GDKLSFremainTime = GDKLSFloseTime - (System.currentTimeMillis() + dsnHttp.timeDValue);
    	return GDKLSFremainTime;
    }
    
    
    public static String getGDKLSFoddsData(){
    	String url = dsnHttp.ADDRESS + "/member/odds?lottery=GDKLSF&games=DX1%2CDX2%2CDX3%2CDX4%2CDX5%2CDX6%2CDX7%2CDX8%2CWDX1%2CWDX2%2CWDX3%2CWDX4%2CWDX5%2CWDX6%2CWDX7%2CWDX8%2CDS1%2CDS2%2CDS3%2CDS4%2CDS5%2CDS6%2CDS7%2CDS8%2CHDS1%2CHDS2%2CHDS3%2CHDS4%2CHDS5%2CHDS6%2CHDS7%2CHDS8%2CZDX%2CZDS%2CZWDX%2CLH1%2CLH2%2CLH3%2CLH4&_=";
    	url += Long.toString(System.currentTimeMillis());
    	
    	GDKLSFoddsData = dsnHttp.doGet(url, "", "");
    	
    	if(GDKLSFoddsData == null){
    		GDKLSFoddsData = dsnHttp.doGet(url, "", "");
    	}
    	
    	return  GDKLSFoddsData;
   	
    }
    
    
    public static boolean doBetGDKLSF(String[] betData, double percent, boolean opposite, String remainTime)
    {

    	String host = dsnHttp.ADDRESS;
       	
        String jsonParam = "";
        
        if(previousGDKLSFBetNumber.equals(GDKLSFdrawNumber)) 
        	return false;
        
        
        GDKLSFjinriqishu++;
        
        GDKLSFdetalsDataWindow.updateTextFieldjinriqishu(Integer.toString(GDKLSFjinriqishu));
        
        GDKLSFdetalsDataWindow.updateTextFieldzongqishu(Integer.toString(GDKLSFzongqishu + GDKLSFjinriqishu));
        
        //如果未到封盘时间
        if( GDKLSFdrawNumber != null){
        	
        	//System.out.printf("下注重庆时时彩第%s期\n",GDKLSFdrawNumber);
        	String outputStr = "下注广东快乐十分第" + GDKLSFdrawNumber + "期\n" + "最新数据时间距收盘" + remainTime + "秒\n";
        	autoBet.outputGUIMessage(outputStr);
        	
/*        	if(isEmptyData(betData, BetType.GDKLSF)) {
        		outputStr = "代理无人投注\n\n";
        		autoBet.outputGUIMessage(outputStr);
        		return false;
        	}*/
        	
        	jsonParam = constructBetsData(betData, percent, BetType.GDKLSF, opposite);
        	
        	//在投注金额窗口显示,只显示百分之一
        	String jsonStr = constructBetsData(betData, 0.01, BetType.GDKLSF, opposite);
        	
        	
        	
        	
        	if(jsonParam == "") {
        		
        		previousGDKLSFBetNumber = GDKLSFdrawNumber;
        		
        		outputStr = "代理无人投注\n\n";
        		autoBet.outputGUIMessage(outputStr);
        		
        		
        		
        		return false;
        	}
        
        	
        	addToBetAmountWindow(jsonStr, BetType.GDKLSF);
        	
        	
        	outputBetsDetails(jsonParam);	
        	
        	System.out.println(jsonParam);
        	
        	String response = "";
        	
        	

        	

        	long time1 = System.currentTimeMillis();
        	
        	response = dsnHttp.bet(host + "/member/bet", jsonParam, "UTF-8", "");
        	
        	
        	boolean betRes = isBetSuccess(GDKLSFdrawNumber);
        	
        	if((betRes == false) && (response == null || response.contains("balance") == false || response.contains("�ڲ�����") == true)){
        		response = dsnHttp.bet(host + "/member/bet", jsonParam, "UTF-8", "");
        	}
        	

        	long time2 = System.currentTimeMillis();
        	
        	long timeD = time2 - time1;
        	
        	double usingTime = timeD;
        	
        	usingTime = usingTime/1000;
        	
        	String strUsingTime  = String.format("下单用时！ :%f 秒\n", usingTime);
        	
        	autoBet.outputGUIMessage(strUsingTime);

        	
        	boolean result = dsnHttp.parseBetResult(response);
        	
        	
        	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm");//设置日期格式
        	
        	if(result == false){
    			GDKLSFdetalsDataWindow.addData(df.format(new Date()), GDKLSFdrawNumber, 2, Integer.toString(GDKLSFbetTotalAmount), Integer.toString(GDKLSFbishu));

    			unknowStatGDKLSFDraw.add(GDKLSFdrawNumber);
    			
    			GDKLSFdetalsDataWindow.updateTextFieldjinriyichang(Integer.toString(unknowStatGDKLSFDraw.size()));
    			
        	}
        	else{
    			GDKLSFdetalsDataWindow.addData(df.format(new Date()), GDKLSFdrawNumber, 0, Integer.toString(GDKLSFbetTotalAmount), Integer.toString(GDKLSFbishu));        		
        	}
        	
        	unCalcProfitGDKLSFDraw.add(GDKLSFdrawNumber);
        	

        	    
        	if(!previousGDKLSFBetNumber.equals(GDKLSFdrawNumber)) { //避免重复计数
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
			
			previousGDKLSFBetNumber = GDKLSFdrawNumber;
        	previousGDKLSFBetResult = result;
        	
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


	    		
    		if(GDKLSFoddsData == null){
    			getGDKLSFoddsData();
    		}
	    		
	    	oddsData = GDKLSFoddsData;
	    	
	    	
	    	oddsGrabData = new JSONObject(oddsData);
	    	
	    	for(int i = 0; i < data.length; i++){
    		
    		
            	JSONArray GDKLSFLMGrabData = new JSONArray(data[i]);        	
            	JSONArray gamesGrabData = GDKLSFLMGrabData.getJSONArray(0);

        	
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
	    	

        	betsObj.put("drawNumber",GDKLSFdrawNumber);
        	
        	betsObj.put("lottery", "GDKLSF");

	    	
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
    
    public static void showGDKLSFDeatilsTable(){
    	GDKLSFdetalsDataWindow.setVisible(true);
    }
    
    public static String getGDKLSFdrawNumber(){
    	return GDKLSFdrawNumber;
    }
    
    
    public static void outputBetsDetails(String jsonData){
    	
    	autoBet.outputGUIMessage("下注详情：\n");
    	try{

            	
        	JSONObject betsData = new JSONObject(jsonData);
        	JSONArray gamesData = betsData.getJSONArray("bets");
        	int totalAmount = 0;
        	
        	GDKLSFbishu = gamesData.length();
        	
        	for(int i = 1; i <= 8 ; i++){
        		String gameDX = "DX" + Integer.toString(i);
        		String gameDS = "DS" + Integer.toString(i);
        		
        		String gameWDX = "WDX" + Integer.toString(i);
        		String gameHDS = "HDS" + Integer.toString(i);
        		
        		String gameLH = "LH" + Integer.toString(i);
        		
        		JSONObject gameData;
        		
        		int amountDX = 0;
        		String contentsDX = "";
        		
        		int amountDS = 0;
        		String contentsDS = "";
        		
        		int amountWDX = 0;
        		String contentsWDX = "";
        		
        		int amountHDS = 0;
        		String contentsHDS = "";
        		
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
        			
        			if(game.equals(gameLH)){
        				amountLH = gameData.getInt("amount");
        				contentsLH = gameData.getString("contents");
        				contentsLH = contentsLH.equals("D")?"龙":"虎";
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
				
				if(amountLH != 0 ){
					outputStr  = String.format("第%s球   %s: %d,", Integer.toString(i), contentsLH, amountLH);
					autoBet.outputGUIMessage(outputStr);
					totalAmount += amountLH;
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
			
			GDKLSFbetTotalAmount = totalAmount;
    			
        	
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
        	
        	GDKLSFbishu = gamesData.length();
        	
        	for(int i = 1; i <= 8 ; i++){
        		String gameDX = "DX" + Integer.toString(i);
        		String gameDS = "DS" + Integer.toString(i);
        		
        		String gameWDX = "WDX" + Integer.toString(i);
        		String gameHDS = "HDS" + Integer.toString(i);
        		
        		String gameLH = "LH" + Integer.toString(i);
        		
        		JSONObject gameData;
        		
        		int amountDX = 0;
        		String contentsDX = "";
        		
        		int amountDS = 0;
        		String contentsDS = "";
        		
        		int amountWDX = 0;
        		String contentsWDX = "";
        		
        		int amountHDS = 0;
        		String contentsHDS = "";
        		
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
        			
        			if(game.equals(gameLH)){
        				amountLH = gameData.getInt("amount");
        				contentsLH = gameData.getString("contents");
        				contentsLH = contentsLH.equals("D")?"龙":"虎";
        			}

        		}
        		
        		
    			String outputStr = "";
    			
				if(amountDX != 0 ){
						outputStr  = String.format("第%s球   %s", Integer.toString(i), contentsDX);

						GDKLSFBetAmountWindow.addData(df.format(new Date()), GDKLSFdrawNumber, outputStr, Integer.toString(amountDX));
				}
				
				if(amountDS != 0 ){
					outputStr  = String.format("第%s球   %s", Integer.toString(i), contentsDS);
					GDKLSFBetAmountWindow.addData(df.format(new Date()), GDKLSFdrawNumber, outputStr, Integer.toString(amountDS));
				}
				
				if(amountWDX != 0 ){
					outputStr  = String.format("第%s球   %s", Integer.toString(i), contentsWDX);
					GDKLSFBetAmountWindow.addData(df.format(new Date()), GDKLSFdrawNumber, outputStr, Integer.toString(amountWDX));
				}
				
				if(amountHDS != 0 ){
					outputStr  = String.format("第%s球   %s", Integer.toString(i), contentsHDS);
					GDKLSFBetAmountWindow.addData(df.format(new Date()), GDKLSFdrawNumber, outputStr, Integer.toString(amountHDS));
				}
				
				if(amountLH != 0 ){
					outputStr  = String.format("第%s球   %s", Integer.toString(i), contentsLH);
					GDKLSFBetAmountWindow.addData(df.format(new Date()), GDKLSFdrawNumber, outputStr, Integer.toString(amountLH));
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
				GDKLSFBetAmountWindow.addData(df.format(new Date()), GDKLSFdrawNumber, outputStr, Integer.toString(amountZDX));
			}
			
			if(amountZDS != 0){
				outputStr  = String.format("总%s",  contentsZDS);
				GDKLSFBetAmountWindow.addData(df.format(new Date()), GDKLSFdrawNumber, outputStr, Integer.toString(amountZDS));
			}
			
			if(amountZWDX != 0){
				outputStr  = String.format("总%s",  contentsZWDX);
				GDKLSFBetAmountWindow.addData(df.format(new Date()), GDKLSFdrawNumber, outputStr, Integer.toString(amountZWDX));
			}
			


    			
        	
    	}catch(Exception e){
    		e.printStackTrace();
    	}

    	
    }
    
    
    public static Vector<String> getUnCalcProfitBJSCDraw(){
    	return unCalcProfitGDKLSFDraw;
    }
    
    public static void updateUnCalcGDKLSFDraw(Vector<String> calcedDraw){
    	for(int i =0; i < calcedDraw.size(); i++){
    		unCalcProfitGDKLSFDraw.removeElement(calcedDraw.elementAt(i));
    		unknowStatGDKLSFDraw.removeElement(calcedDraw.elementAt(i));
    	}  
    	
    	
    	long currentDraw = Long.parseLong(GDKLSFdrawNumber);
    	
    	for(int j =0; j < unCalcProfitGDKLSFDraw.size(); j++){
    		long idrawNumber = Long.parseLong(unCalcProfitGDKLSFDraw.elementAt(j));
    		if((currentDraw - idrawNumber) >= 4){
    			unCalcProfitGDKLSFDraw.removeElement(unCalcProfitGDKLSFDraw.elementAt(j));
    		}
    	}
    	
    	
    	int yichangshu1 = unknowStatGDKLSFDraw.size();
    	
    	for(int j =0; j < unknowStatGDKLSFDraw.size(); j++){
    		long idrawNumber = Long.parseLong(unknowStatGDKLSFDraw.elementAt(j));
    		if((currentDraw - idrawNumber) >= 4){
    			updateGDKLSFWindowdetailsData(unknowStatGDKLSFDraw.elementAt(j), TYPEINDEX.STATC.ordinal(), "1");
    			unknowStatGDKLSFDraw.removeElement(unknowStatGDKLSFDraw.elementAt(j));

    		}
    	}
    	
    	int yichangshu2 = unknowStatGDKLSFDraw.size();
    	
    	GDKLSFjinriyichang = unknowStatGDKLSFDraw.size();
    	
    	GDKLSFjinrishibai += yichangshu1 - yichangshu2;
    	
    	
    	
    	
    	
    	GDKLSFdetalsDataWindow.updateTextFieldjinrishibai(Integer.toString(GDKLSFjinrishibai));
    	GDKLSFdetalsDataWindow.updateTextFieldjinriyichang(Integer.toString(GDKLSFjinriyichang));
    	
    	
    	
    	GDKLSFdetalsDataWindow.updateTextFieldzongqishu(Integer.toString(GDKLSFzongqishu + GDKLSFjinriqishu));
    	
    	GDKLSFdetalsDataWindow.updateTextFieldzongshibai(Integer.toString(GDKLSFzongshibai + GDKLSFjinrishibai));
    	
    	
    }
    
    public static void updateGDKLSFWindowdetailsData(String drawNumber, int index, String value){
    	GDKLSFdetalsDataWindow.updateRowItem(drawNumber, index, value);
    }
    
    public static void showGDKLSFBetAmountTable(){
    	GDKLSFBetAmountWindow.setVisible(true);
    }
    
    public static void updateGDKLSFBalance(String str){
    	GDKLSFdetalsDataWindow.updateTextFieldyue(str);
    }
    
    
    public static boolean isGDKLSFidle(){
    	boolean isIdle = false;
    	if(GDKLSFremainTime < 0 || GDKLSFremainTime > 25*1000 ){
    		isIdle = true;
    	}
    	
    	return isIdle;
    }
    
    
    public static void clearGDKLSFdetalsData(){
    	if(unCalcProfitGDKLSFDraw.size() != 0){
    		unCalcProfitGDKLSFDraw.clear();
    	}
    	
    	if(unknowStatGDKLSFDraw.size() != 0){
    		GDKLSFjinrishibai += unknowStatGDKLSFDraw.size();
    		unknowStatGDKLSFDraw.clear();
    	}

    	GDKLSFzongqishu += GDKLSFjinriqishu;
    	GDKLSFzongshibai += GDKLSFjinrishibai;
    	

    	
    	GDKLSFjinriqishu = 0;
    	GDKLSFjinrishibai = 0;
    	GDKLSFjinriyichang = 0;
    	
    	GDKLSFremainTime = -1;

    }
    
    
}
