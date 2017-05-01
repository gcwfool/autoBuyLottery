package huarun;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

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
    
    static String phaseId = "";
    static String usableCredit = "";
    
    static boolean openning = false;
    
    
    static Vector<String> unknowStatBJSCDraw = new Vector<String>();    
    static Vector<String> unCalcProfitBJSCDraw = new Vector<String>();    

    static DSNDataDetailsWindow BJSCdetalsDataWindow = new DSNDataDetailsWindow();    
    
    static DSNBetAmountWindow BJSCBetAmountWindow = new DSNBetAmountWindow();    
	
	static long remainTime = -1;
	
	static Map<String, String> oddsPairs = new HashMap<String, String>();

	public static long webTime = -1;  //seconds
	
	public static long closeTime = -1; //seconds
	
	public static long timeDvalue = -1; //sceonds
	
	
	public static void init(){
		BJSCdetalsDataWindow.setTitle("[华润]投注北京赛车详情");
		BJSCBetAmountWindow.setTitle("[华润]北京赛车下注金额");
	}
	
	public static boolean grabGameInfo() {
		String res = null;
		try{      	
        	List<NameValuePair> params1 = new ArrayList<NameValuePair>();
	    	params1.add(new BasicNameValuePair("action", "get_oddsinfo"));
	    	params1.add(new BasicNameValuePair("playid", "2,3,4,6,7,8,10,11,12,14,15,16,18,19,20,22,23,25,26,28,29,31,32,34,35,37,38"));
	    	params1.add(new BasicNameValuePair("playpage", "pk10_lmp"));
	    	
	    	long time1 = 0, time2 = 0;
	    	time1 = System.currentTimeMillis();
        	res = HuarunHttp.doPost(HuarunHttp.ADDRESS + "/L_PK10/Handler/Handler.ashx", params1, "", HuarunHttp.ADDRESS + "/L_PK10/index.aspx?lid=2&path=L_PK10");
        	time2 = System.currentTimeMillis();
        	
        	JSONObject gameInfo = new JSONObject(res);        	
        	gameInfo = new JSONObject(gameInfo.getJSONObject("data").toString());
        	
        	if(gameInfo.has("stop_time")) {
        		String stop_time = gameInfo.getString("stop_time");
        		if(stop_time.equals("")) {
        			return true;
        		}
        	
	        	String[] timeArray = stop_time.split(":");
	    		
	    		int hour = Integer.parseInt(timeArray[0]);
	    		int min = Integer.parseInt(timeArray[1]);
	    		int sec = Integer.parseInt(timeArray[2]);
	    		
	    		openning = gameInfo.getString("openning").equals("y") ? true : false;
	    		if(openning && time2 - time1 < 2500) {
			        closeTime = hour * 3600 + min * 60 + sec;
			        closeTime = System.currentTimeMillis()/1000 + closeTime;
        		}
	        	
	    		BJSCdrawNumber = gameInfo.getString("nn");
	    		phaseId = gameInfo.getString("p_id");
	    		      	
	    		JSONObject oddsInfo = gameInfo.getJSONObject("play_odds");
	    		usableCredit = gameInfo.getString("usable_credit");
	    		putOddsInfo(oddsInfo);
	    		updateBJSCBalance(usableCredit);	
	    		
	    		return true;
        	}		
    	}catch(Exception e){
    		e.printStackTrace();
    		System.out.println(res);
    	}
		
		return false;
	}
	
	public static void putOddsInfo(JSONObject oddsInfo) {
		try {
			JSONObject tmp = oddsInfo.getJSONObject("2_11");
			oddsPairs.put("11", tmp.getString("pl"));
			
			tmp = oddsInfo.getJSONObject("2_12");
			oddsPairs.put("12", tmp.getString("pl"));
			
			tmp = oddsInfo.getJSONObject("3_13");
			oddsPairs.put("13", tmp.getString("pl"));
			
			tmp = oddsInfo.getJSONObject("3_14");
			oddsPairs.put("14", tmp.getString("pl"));
			
			tmp = oddsInfo.getJSONObject("4_15");
			oddsPairs.put("15", tmp.getString("pl"));
			
			tmp = oddsInfo.getJSONObject("4_16");
			oddsPairs.put("16", tmp.getString("pl"));
			
			tmp = oddsInfo.getJSONObject("6_27");
			oddsPairs.put("27", tmp.getString("pl"));
	
			tmp = oddsInfo.getJSONObject("6_28");
			oddsPairs.put("28", tmp.getString("pl"));
			
			tmp = oddsInfo.getJSONObject("7_29");
			oddsPairs.put("29", tmp.getString("pl"));
			
			tmp = oddsInfo.getJSONObject("7_30");
			oddsPairs.put("30", tmp.getString("pl"));
			
			tmp = oddsInfo.getJSONObject("8_31");
			oddsPairs.put("31", tmp.getString("pl"));
			
			tmp = oddsInfo.getJSONObject("8_32");
			oddsPairs.put("32", tmp.getString("pl"));
			
			tmp = oddsInfo.getJSONObject("10_43");
			oddsPairs.put("43", tmp.getString("pl"));
	
			tmp = oddsInfo.getJSONObject("10_44");
			oddsPairs.put("44", tmp.getString("pl"));
			
			tmp = oddsInfo.getJSONObject("11_45");
			oddsPairs.put("45", tmp.getString("pl"));
			
			tmp = oddsInfo.getJSONObject("11_46");
			oddsPairs.put("46", tmp.getString("pl"));
	
			tmp = oddsInfo.getJSONObject("12_47");
			oddsPairs.put("47", tmp.getString("pl"));
			
			tmp = oddsInfo.getJSONObject("12_48");
			oddsPairs.put("48", tmp.getString("pl"));
			
			tmp = oddsInfo.getJSONObject("14_59");
			oddsPairs.put("59", tmp.getString("pl"));
	
			tmp = oddsInfo.getJSONObject("14_60");
			oddsPairs.put("60", tmp.getString("pl"));
			
			tmp = oddsInfo.getJSONObject("15_61");
			oddsPairs.put("61", tmp.getString("pl"));
			
			tmp = oddsInfo.getJSONObject("15_62");
			oddsPairs.put("62", tmp.getString("pl"));
	
			tmp = oddsInfo.getJSONObject("16_63");
			oddsPairs.put("63", tmp.getString("pl"));
			
			tmp = oddsInfo.getJSONObject("16_64");
			oddsPairs.put("64", tmp.getString("pl"));
			
			tmp = oddsInfo.getJSONObject("18_75");
			oddsPairs.put("75", tmp.getString("pl"));
	
			tmp = oddsInfo.getJSONObject("18_76");
			oddsPairs.put("76", tmp.getString("pl"));
			
			tmp = oddsInfo.getJSONObject("19_77");
			oddsPairs.put("77", tmp.getString("pl"));
			
			tmp = oddsInfo.getJSONObject("19_78");
			oddsPairs.put("78", tmp.getString("pl"));
	
			tmp = oddsInfo.getJSONObject("20_79");
			oddsPairs.put("79", tmp.getString("pl"));
			
			tmp = oddsInfo.getJSONObject("20_80");
			oddsPairs.put("80", tmp.getString("pl"));
			
			tmp = oddsInfo.getJSONObject("22_91");
			oddsPairs.put("91", tmp.getString("pl"));
	
			tmp = oddsInfo.getJSONObject("22_92");
			oddsPairs.put("92", tmp.getString("pl"));
			
			tmp = oddsInfo.getJSONObject("23_93");
			oddsPairs.put("93", tmp.getString("pl"));
			
			tmp = oddsInfo.getJSONObject("23_94");
			oddsPairs.put("94", tmp.getString("pl"));
			
			tmp = oddsInfo.getJSONObject("25_105");
			oddsPairs.put("105", tmp.getString("pl"));
			
			tmp = oddsInfo.getJSONObject("25_106");
			oddsPairs.put("106", tmp.getString("pl"));
	
			tmp = oddsInfo.getJSONObject("26_107");
			oddsPairs.put("107", tmp.getString("pl"));
			
			tmp = oddsInfo.getJSONObject("26_108");
			oddsPairs.put("108", tmp.getString("pl"));
	
			tmp = oddsInfo.getJSONObject("28_119");
			oddsPairs.put("119", tmp.getString("pl"));
			
			tmp = oddsInfo.getJSONObject("28_120");
			oddsPairs.put("120", tmp.getString("pl"));
	
			tmp = oddsInfo.getJSONObject("29_121");
			oddsPairs.put("121", tmp.getString("pl"));
			
			tmp = oddsInfo.getJSONObject("29_122");
			oddsPairs.put("122", tmp.getString("pl"));
	
			tmp = oddsInfo.getJSONObject("31_133");
			oddsPairs.put("133", tmp.getString("pl"));
			
			tmp = oddsInfo.getJSONObject("31_134");
			oddsPairs.put("134", tmp.getString("pl"));
	
			tmp = oddsInfo.getJSONObject("32_135");
			oddsPairs.put("135", tmp.getString("pl"));
			
			tmp = oddsInfo.getJSONObject("32_136");
			oddsPairs.put("136", tmp.getString("pl"));
	
			tmp = oddsInfo.getJSONObject("34_147");
			oddsPairs.put("147", tmp.getString("pl"));
			
			tmp = oddsInfo.getJSONObject("34_148");
			oddsPairs.put("148", tmp.getString("pl"));
	
			tmp = oddsInfo.getJSONObject("35_149");
			oddsPairs.put("149", tmp.getString("pl"));
			
			tmp = oddsInfo.getJSONObject("35_150");
			oddsPairs.put("150", tmp.getString("pl"));
		
		}catch(Exception e){
			e.printStackTrace();
		}

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
    
    public static boolean  isInLastTime(){
    	
    	long time = System.currentTimeMillis();
        Date date = new Date(time);
        int currentHour = date.getHours();
        int currentMinutes = date.getMinutes();
        int currentSeconds = date.getSeconds();
        
        /*if(currentHour >=9 && (currentHour * 60 + currentMinutes <= 23 * 60 + 57)){
        	return true;
        }*/
        
        //两分钟分钟的缓冲
        if((currentHour *60 + currentMinutes > 23*60 + 57) && (currentHour * 60 + currentMinutes <= 24 * 60)){
        	return true;
        } 
        
        
        return false;
    }
	
	
	public static long getRemainTime(){
        return closeTime - (System.currentTimeMillis()/1000);
	}

    
    public static boolean doBetBJSC(String[] betData, double percent,boolean opposite, String remainTime)
    {

    	String host = HuarunHttp.ADDRESS;
       	
        String betStr = "";
        
        if(previousBJSCBetNumber.equals(BJSCdrawNumber)) {
        	System.out.println("previousBJSCBetNumber == BJSCdrawNumber");
        	return false;
        }
        	
        
        
        BJSCjinriqishu++;
        
        BJSCdetalsDataWindow.updateTextFieldjinriqishu(Integer.toString(BJSCjinriqishu));
        
        BJSCdetalsDataWindow.updateTextFieldzongqishu(Integer.toString(BJSCzongqishu + BJSCjinriqishu));
        
        //如果未到封盘时间
        if( getRemainTime() > 0){
        	
        	//System.out.printf("下注北京赛车第%s期\n",BJSCdrawNumber);
        	String outputStr = "华润下注北京赛车第" + BJSCdrawNumber + "期\n"  + "最新数据时间距收盘" + remainTime + "秒\n";
        	autoBet.outputGUIMessage(outputStr);
        	
        	long time1 = System.currentTimeMillis();
        	Vector<String> saves = new Vector<String>();
        	List<NameValuePair> params = constructBetsData(betData, percent,  opposite, saves);

        	
        	if(params == null) {
        		outputStr = "代理无人投注\n\n";
        		autoBet.outputGUIMessage(outputStr);
        		return false;
        	}
        	
        	
        	
        	String outputBetData = constructoutputData(betData, percent,  opposite);
 	
        	
        	addToBetAmountWindow(outputBetData);
        	
        	
        	        	
        	outputBetsDetails(outputBetData);
        	
        	//System.out.println(betStr);
        	
        	String response = "";       	
   	
	        int oldTimeout = HuarunHttp.defaultTimeout;
	        
	        HuarunHttp.defaultTimeout = 10*1000;
	        System.out.println(getRemainTime());
	        
        	
        	response = HuarunHttp.doPost(HuarunHttp.ADDRESS + "/L_PK10/Handler/Handler.ashx", params, "", HuarunHttp.ADDRESS + "/L_PK10/index.aspx?lid=2&path=L_PK10");
        	
        	System.out.printf("下单结果:%s\n", response);
        	
        	boolean failed = isBetFailed(response);
        	boolean betRes = isBetSuccess(response);
        	
        	if((failed == true) && getRemainTime() > 0){
        		if(!response.contains("newpl")) {
        			params = constructBetsData(betData, percent,  opposite, saves);
        		} else {
        			try{
        			   JSONObject newpl = new JSONObject(response);
         			   JSONObject data = newpl.getJSONObject("data");
         			   JSONArray arr = data.getJSONArray("index");
         			   JSONArray arr1 = data.getJSONArray("newpl");
         			   int size = arr.length();
         			   int [] indexs = new int[size];
         			   String [] pls = new String[size];
         			   for(int i = 0; i < size; i++) {
         				   indexs[i] = arr.getInt(i);
         				   pls[i] = arr1.getString(i);
         			   } 
         			   
         			   params = reConstructBetsData(betData, percent,  opposite, saves, indexs, pls, size);
        	    		
        	    	}catch(Exception e){
        	    		e.printStackTrace();
        	    	}			   
        		}
        		response = HuarunHttp.doPost(HuarunHttp.ADDRESS + "/L_PK10/Handler/Handler.ashx", params, "", HuarunHttp.ADDRESS + "/L_PK10/index.aspx?lid=2&path=L_PK10");	  
        		betRes = isBetSuccess(response);
        		failed = isBetFailed(response);
        		System.out.printf("再次下单结果:%s\n", response);
        	}
        	
        	HuarunHttp.defaultTimeout = oldTimeout;

        	long time2 = System.currentTimeMillis();
        	
        	long timeD = time2 - time1;
        	
        	double usingTime = timeD;
        	
        	usingTime = usingTime/1000;
        	
        	String strUsingTime  = String.format("下单用时！ :%f 秒\n", usingTime);
        	
        	autoBet.outputGUIMessage(strUsingTime);     	
     	
        	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm");//设置日期格式

        	
        	if(betRes == false){
    			BJSCdetalsDataWindow.addData(df.format(new Date()), BJSCdrawNumber, 2, Integer.toString(BJSCbetTotalAmount), Integer.toString(BJSCbishu));

    			unknowStatBJSCDraw.add(BJSCdrawNumber);
    			
    			BJSCdetalsDataWindow.updateTextFieldjinriyichang(Integer.toString(unknowStatBJSCDraw.size()));
        	}
        	else{
    			BJSCdetalsDataWindow.addData(df.format(new Date()), BJSCdrawNumber, 0, Integer.toString(BJSCbetTotalAmount), Integer.toString(BJSCbishu));        		
        	}
        	
        	unCalcProfitBJSCDraw.add(BJSCdrawNumber);		
			previousBJSCBetNumber = BJSCdrawNumber;
        	previousBJSCBetResult = betRes;
        	
        	return betRes;
        }
        
        return false;
    }
    
    
    
    
    
    public static List<NameValuePair> constructBetsData(String[] data, double percent, boolean opposite, Vector<String> saves) {
    	
    	long totalAmount = 0;
    	
    	String res = "";
    	
    	Map<String, String> betPairs = new HashMap<String, String>();
    	
    	try{
    		
    		List<String> parsedGames = new ArrayList<String>();
	    	
	    	for(int i = 0; i < data.length; i++){
	    		
            	JSONArray cqsscLMGrabData = new JSONArray(data[i]);        	
            	JSONArray gamesGrabData = cqsscLMGrabData.getJSONArray(0);
            	JSONObject oddsGrabData = cqsscLMGrabData.getJSONObject(1);
	
	        	for(int j = 0; j < gamesGrabData.length(); j++) {
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
	        			if(amount < 2)
	        				continue; //添彩每注最低2元

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
   	
	        	    		
        	        	switch(game){
	        	        	case "DX1":
	        	        		selectionTypeName = "1";
	        	        		contents = contents.equals("D")?"11":"12";
	        	        		outputName = "冠军";
	        	        		outputContent = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS1":
	        	        		selectionTypeName = "1";
	        	        		contents = contents.equals("D")?"13":"14";
	        	        		outputName = "冠军";
	        	        		outputContent = contents.equals("D")?"单":"双";
	        	        		break;
	        	        	case "LH1":
	        	        		selectionTypeName = "1";
	        	        		contents = contents.equals("L")?"15":"16";
	        	        		outputName = "冠军";
	        	        		outputContent = contents.equals("L")?"龙":"虎";
	        	        		break;	
	        	        		
	        	        	case "DX2":
	        	        		selectionTypeName = "2";
	        	        		contents = contents.equals("D")?"27":"28";
	        	        		outputName = "亚军";
	        	        		outputContent = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS2":
	        	        		selectionTypeName = "2";
	        	        		contents = contents.equals("D")?"29":"30";
	        	        		outputName = "亚军";
	        	        		outputContent = contents.equals("D")?"单":"双";
	        	        		break;
	        	        	case "LH2":
	        	        		selectionTypeName = "2";
	        	        		contents = contents.equals("L")?"31":"32";
	        	        		outputName = "亚军";
	        	        		outputContent = contents.equals("L")?"龙":"虎";
	        	        		break;		
	        	        	case "DX3":
	        	        		selectionTypeName = "3";
	        	        		contents = contents.equals("D")?"43":"44";
	        	        		outputName = "第三名";
	        	        		outputContent = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS3":
	        	        		selectionTypeName = "3";
	        	        		contents = contents.equals("D")?"45":"46";	
	        	        		outputName = "第三名";
	        	        		outputContent = contents.equals("D")?"单":"双";
	        	        		break;
	        	        	case "LH3":
	        	        		selectionTypeName = "3";
	        	        		contents = contents.equals("L")?"47":"48";
	        	        		outputName = "第三名";
	        	        		outputContent = contents.equals("L")?"龙":"虎";
	        	        		break;		
	        	        	case "DX4":
	        	        		selectionTypeName = "4";
	        	        		contents = contents.equals("D")?"59":"60";
	        	        		outputName = "第四名";
	        	        		outputContent = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS4":
	        	        		selectionTypeName = "4";
	        	        		contents = contents.equals("D")?"61":"62";
	        	        		outputName = "第四名";
	        	        		outputContent = contents.equals("D")?"单":"双";
	        	        		break;
	        	        	case "LH4":
	        	        		selectionTypeName = "4";
	        	        		contents = contents.equals("L")?"63":"64";
	        	        		outputName = "第四名";
	        	        		outputContent = contents.equals("L")?"龙":"虎";
	        	        		break;	
	        	        	case "DX5":
	        	        		selectionTypeName = "5";
	        	        		contents = contents.equals("D")?"75":"76";
	        	        		outputName = "第五名";
	        	        		outputContent = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS5":
	        	        		selectionTypeName = "5";
	        	        		contents = contents.equals("D")?"77":"78";	
	        	        		outputName = "第五名";
	        	        		outputContent = contents.equals("D")?"单":"双";
	        	        		break;
	        	        	case "LH5":
	        	        		selectionTypeName = "5";
	        	        		contents = contents.equals("L")?"79":"80";
	        	        		outputName = "第五名";
	        	        		outputContent = contents.equals("L")?"龙":"虎";
	        	        		break;	
	        	        	case "DX6":
	        	        		selectionTypeName = "6";
	        	        		contents = contents.equals("D")?"91":"92";
	        	        		outputName = "第六名";
	        	        		outputContent = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS6":
	        	        		selectionTypeName = "6";
	        	        		contents = contents.equals("D")?"93":"94";	
	        	        		outputName = "第六名";
	        	        		outputContent = contents.equals("D")?"单":"双";
	        	        		break;
	        	        	case "DX7":
	        	        		selectionTypeName = "7";
	        	        		contents = contents.equals("D")?"105":"106";
	        	        		outputName = "第七名";
	        	        		outputContent = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS7":
	        	        		selectionTypeName = "7";
	        	        		contents = contents.equals("D")?"107":"108";	
	        	        		outputName = "第七名";
	        	        		outputContent = contents.equals("D")?"单":"双";
	        	        		break;
	        	        	case "DX8":
	        	        		selectionTypeName = "8";
	        	        		contents = contents.equals("D")?"119":"120";
	        	        		outputName = "第八名";
	        	        		outputContent = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS8":
	        	        		selectionTypeName = "8";
	        	        		contents = contents.equals("D")?"121":"122";
	        	        		outputName = "第八名";
	        	        		outputContent = contents.equals("D")?"单":"双";
	        	        		break;
	        	        	case "DX9":
	        	        		selectionTypeName = "9";
	        	        		contents = contents.equals("D")?"133":"134";
	        	        		outputName = "第九名";
	        	        		outputContent = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS9":
	        	        		selectionTypeName = "9";
	        	        		contents = contents.equals("D")?"135":"136";	
	        	        		outputName = "第九名";
	        	        		outputContent = contents.equals("D")?"单":"双";
	        	        		break;
	        	        	case "DX10":
	        	        		selectionTypeName = "10";
	        	        		contents = contents.equals("D")?"147":"148";
	        	        		outputName = "第十名";
	        	        		outputContent = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS10":
	        	        		selectionTypeName = "10";
	        	        		contents = contents.equals("D")?"149":"150";
	        	        		outputName = "第十名";
	        	        		outputContent = contents.equals("D")?"单":"双";
	        	        		break;
        	        	}    	        	

        	        	betPairs.put(contents, String.valueOf(amount));
	        			String out = outputName + "_" + outputContent + ":" + amount;
	        			String save = selectionTypeName + "_" + outputContent + "_" + amount;
	        			saves.add(save);
	        			System.out.println(out);	        	    	
	        		}		
	        	}
	    	}
	    	
	    	if(res.length() > 1) {
	    		res = "[" + res.substring(0, res.length() - 1) + "]"; 
	    		autoBet.outputGUIMessage("下单总额:" + totalAmount + "\n");
	    	}
	    	
	    	List<Map.Entry<String, String>> mapList;
	    	mapList=new ArrayList<Map.Entry<String, String>>(betPairs.entrySet()); 
	    	Collections.sort(mapList, new Comparator<Map.Entry<String,String>>() {
				@Override
				public int compare(Map.Entry<String,String> firstMapEntry, 
								   Map.Entry<String,String> secondMapEntry) {
						if(Integer.parseInt(firstMapEntry.getKey()) > Integer.parseInt(secondMapEntry.getKey())) {
							return 1;
						}else if(Integer.parseInt(firstMapEntry.getKey()) < Integer.parseInt(secondMapEntry.getKey())){
							return  -1;
						}
						else {
							return 0;
						}
				}
			});
	    	
	    	String oddsid = "";
	    	String uPI_M = "";
	    	String uPI_P = "";
	    	String i_index = "";
	    	int i = 0;
	    	for(Map.Entry<String,String> entry : mapList){
	    		oddsid += entry.getKey() + ",";
	    		uPI_M += entry.getValue() + ",";
	    		uPI_P += oddsPairs.get(entry.getKey()) + ",";
	    		i_index += i + ",";
	    		i++;
	            System.out.println(entry.getKey()+":"+entry.getValue());             
	        }  
	    	
	    	oddsid = oddsid.substring(0, oddsid.length() - 1);
	    	uPI_M = uPI_M.substring(0, uPI_M.length() - 1);
	    	uPI_P = uPI_P.substring(0, uPI_P.length() - 1);
	    	i_index = i_index.substring(0, i_index.length() - 1);
	    	System.out.println("下单数据：------------------------------------");
	    	List<NameValuePair> params = new ArrayList<NameValuePair>();
	    	params.add(new BasicNameValuePair("action", "put_money"));
	    	params.add(new BasicNameValuePair("phaseid", phaseId));
	    	params.add(new BasicNameValuePair("oddsid", oddsid));
	    	params.add(new BasicNameValuePair("uPI_P", uPI_P));
	    	params.add(new BasicNameValuePair("uPI_M", uPI_M));
	    	params.add(new BasicNameValuePair("i_index", i_index));
	    	params.add(new BasicNameValuePair("JeuValidate", HuarunHttp.jeuValidate));
	    	params.add(new BasicNameValuePair("playpage", "pk10_lmp"));

	        System.out.println(phaseId);
	        System.out.println(oddsid);
	        System.out.println(uPI_P);
	        System.out.println(uPI_M);
	        System.out.println(i_index);
	        System.out.println(HuarunHttp.jeuValidate);
	        
	        return params;
	        
    	
    	}catch(Exception e){
    		e.printStackTrace();
    		autoBet.outputGUIMessage("构造下单数据错误！\n");
    		return null;
    	}
   	
    }
    
public static List<NameValuePair> reConstructBetsData(String[] data, double percent, boolean opposite, Vector<String> saves, int[] indexs, String[]  pls, int size) {
    	
    	long totalAmount = 0;
    	
    	String res = "";
    	
    	Map<String, String> betPairs = new HashMap<String, String>();
    	
    	try{
    		
    		List<String> parsedGames = new ArrayList<String>();
	    	
	    	for(int i = 0; i < data.length; i++){
	    		
            	JSONArray cqsscLMGrabData = new JSONArray(data[i]);        	
            	JSONArray gamesGrabData = cqsscLMGrabData.getJSONArray(0);
            	JSONObject oddsGrabData = cqsscLMGrabData.getJSONObject(1);
	
	        	for(int j = 0; j < gamesGrabData.length(); j++) {
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
	        			if(amount < 2)
	        				continue; //添彩每注最低2元

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
   	
	        	    		
        	        	switch(game){
	        	        	case "DX1":
	        	        		selectionTypeName = "1";
	        	        		contents = contents.equals("D")?"11":"12";
	        	        		outputName = "冠军";
	        	        		outputContent = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS1":
	        	        		selectionTypeName = "1";
	        	        		contents = contents.equals("D")?"13":"14";
	        	        		outputName = "冠军";
	        	        		outputContent = contents.equals("D")?"单":"双";
	        	        		break;
	        	        	case "LH1":
	        	        		selectionTypeName = "1";
	        	        		contents = contents.equals("L")?"15":"16";
	        	        		outputName = "冠军";
	        	        		outputContent = contents.equals("L")?"龙":"虎";
	        	        		break;	
	        	        		
	        	        	case "DX2":
	        	        		selectionTypeName = "2";
	        	        		contents = contents.equals("D")?"27":"28";
	        	        		outputName = "亚军";
	        	        		outputContent = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS2":
	        	        		selectionTypeName = "2";
	        	        		contents = contents.equals("D")?"29":"30";
	        	        		outputName = "亚军";
	        	        		outputContent = contents.equals("D")?"单":"双";
	        	        		break;
	        	        	case "LH2":
	        	        		selectionTypeName = "2";
	        	        		contents = contents.equals("L")?"31":"32";
	        	        		outputName = "亚军";
	        	        		outputContent = contents.equals("L")?"龙":"虎";
	        	        		break;		
	        	        	case "DX3":
	        	        		selectionTypeName = "3";
	        	        		contents = contents.equals("D")?"43":"44";
	        	        		outputName = "第三名";
	        	        		outputContent = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS3":
	        	        		selectionTypeName = "3";
	        	        		contents = contents.equals("D")?"45":"46";	
	        	        		outputName = "第三名";
	        	        		outputContent = contents.equals("D")?"单":"双";
	        	        		break;
	        	        	case "LH3":
	        	        		selectionTypeName = "3";
	        	        		contents = contents.equals("L")?"47":"48";
	        	        		outputName = "第三名";
	        	        		outputContent = contents.equals("L")?"龙":"虎";
	        	        		break;		
	        	        	case "DX4":
	        	        		selectionTypeName = "4";
	        	        		contents = contents.equals("D")?"59":"60";
	        	        		outputName = "第四名";
	        	        		outputContent = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS4":
	        	        		selectionTypeName = "4";
	        	        		contents = contents.equals("D")?"61":"62";
	        	        		outputName = "第四名";
	        	        		outputContent = contents.equals("D")?"单":"双";
	        	        		break;
	        	        	case "LH4":
	        	        		selectionTypeName = "4";
	        	        		contents = contents.equals("L")?"63":"64";
	        	        		outputName = "第四名";
	        	        		outputContent = contents.equals("L")?"龙":"虎";
	        	        		break;	
	        	        	case "DX5":
	        	        		selectionTypeName = "5";
	        	        		contents = contents.equals("D")?"75":"76";
	        	        		outputName = "第五名";
	        	        		outputContent = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS5":
	        	        		selectionTypeName = "5";
	        	        		contents = contents.equals("D")?"77":"78";	
	        	        		outputName = "第五名";
	        	        		outputContent = contents.equals("D")?"单":"双";
	        	        		break;
	        	        	case "LH5":
	        	        		selectionTypeName = "5";
	        	        		contents = contents.equals("L")?"79":"80";
	        	        		outputName = "第五名";
	        	        		outputContent = contents.equals("L")?"龙":"虎";
	        	        		break;	
	        	        	case "DX6":
	        	        		selectionTypeName = "6";
	        	        		contents = contents.equals("D")?"91":"92";
	        	        		outputName = "第六名";
	        	        		outputContent = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS6":
	        	        		selectionTypeName = "6";
	        	        		contents = contents.equals("D")?"93":"94";	
	        	        		outputName = "第六名";
	        	        		outputContent = contents.equals("D")?"单":"双";
	        	        		break;
	        	        	case "DX7":
	        	        		selectionTypeName = "7";
	        	        		contents = contents.equals("D")?"105":"106";
	        	        		outputName = "第七名";
	        	        		outputContent = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS7":
	        	        		selectionTypeName = "7";
	        	        		contents = contents.equals("D")?"107":"108";	
	        	        		outputName = "第七名";
	        	        		outputContent = contents.equals("D")?"单":"双";
	        	        		break;
	        	        	case "DX8":
	        	        		selectionTypeName = "8";
	        	        		contents = contents.equals("D")?"119":"120";
	        	        		outputName = "第八名";
	        	        		outputContent = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS8":
	        	        		selectionTypeName = "8";
	        	        		contents = contents.equals("D")?"121":"122";
	        	        		outputName = "第八名";
	        	        		outputContent = contents.equals("D")?"单":"双";
	        	        		break;
	        	        	case "DX9":
	        	        		selectionTypeName = "9";
	        	        		contents = contents.equals("D")?"133":"134";
	        	        		outputName = "第九名";
	        	        		outputContent = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS9":
	        	        		selectionTypeName = "9";
	        	        		contents = contents.equals("D")?"135":"136";	
	        	        		outputName = "第九名";
	        	        		outputContent = contents.equals("D")?"单":"双";
	        	        		break;
	        	        	case "DX10":
	        	        		selectionTypeName = "10";
	        	        		contents = contents.equals("D")?"147":"148";
	        	        		outputName = "第十名";
	        	        		outputContent = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS10":
	        	        		selectionTypeName = "10";
	        	        		contents = contents.equals("D")?"149":"150";
	        	        		outputName = "第十名";
	        	        		outputContent = contents.equals("D")?"单":"双";
	        	        		break;
        	        	}    	        	

        	        	betPairs.put(contents, String.valueOf(amount));
	        			String out = outputName + "_" + outputContent + ":" + amount;
	        			String save = selectionTypeName + "_" + outputContent + "_" + amount;
	        			saves.add(save);
	        			System.out.println(out);	        	    	
	        		}		
	        	}
	    	}
	    	
	    	if(res.length() > 1) {
	    		res = "[" + res.substring(0, res.length() - 1) + "]"; 
	    		autoBet.outputGUIMessage("下单总额:" + totalAmount + "\n");
	    	}
	    	
	    	List<Map.Entry<String, String>> mapList;
	    	mapList=new ArrayList<Map.Entry<String, String>>(betPairs.entrySet()); 
	    	Collections.sort(mapList, new Comparator<Map.Entry<String,String>>() {
				@Override
				public int compare(Map.Entry<String,String> firstMapEntry, 
								   Map.Entry<String,String> secondMapEntry) {
						if(Integer.parseInt(firstMapEntry.getKey()) > Integer.parseInt(secondMapEntry.getKey())) {
							return 1;
						}else if(Integer.parseInt(firstMapEntry.getKey()) < Integer.parseInt(secondMapEntry.getKey())){
							return  -1;
						}
						else {
							return 0;
						}
				}
			});
	    	
	    	String oddsid = "";
	    	String uPI_M = "";
	    	String uPI_P = "";
	    	String i_index = "";
	    	int i = 0;
	    	for(Map.Entry<String,String> entry : mapList){
	    		oddsid += entry.getKey() + ",";
	    		uPI_M += entry.getValue() + ",";
	    		boolean change = false;
	    		for(int j = 0; j < size; j++) {
	    			if(indexs[j] == i) {
	    				uPI_P += pls[j] + ",";
	    				change = true;
	    			}
	    		}
	    		
	    		if(!change) {
	    			uPI_P += oddsPairs.get(entry.getKey()) + ",";
	    		}
	    		i_index += i + ",";
	    		i++;
	            //System.out.println(entry.getKey()+":"+entry.getValue());             
	        }  
	    	
	    	oddsid = oddsid.substring(0, oddsid.length() - 1);
	    	uPI_M = uPI_M.substring(0, uPI_M.length() - 1);
	    	uPI_P = uPI_P.substring(0, uPI_P.length() - 1);
	    	i_index = i_index.substring(0, i_index.length() - 1);
	    	System.out.println("下单数据：------------------------------------");
	    	List<NameValuePair> params = new ArrayList<NameValuePair>();
	    	params.add(new BasicNameValuePair("action", "put_money"));
	    	params.add(new BasicNameValuePair("phaseid", phaseId));
	    	params.add(new BasicNameValuePair("oddsid", oddsid));
	    	params.add(new BasicNameValuePair("uPI_P", uPI_P));
	    	params.add(new BasicNameValuePair("uPI_M", uPI_M));
	    	params.add(new BasicNameValuePair("i_index", i_index));
	    	params.add(new BasicNameValuePair("JeuValidate", HuarunHttp.jeuValidate));
	    	params.add(new BasicNameValuePair("playpage", "pk10_lmp"));

	        System.out.println(phaseId);
	        System.out.println(oddsid);
	        System.out.println(uPI_P);
	        System.out.println(uPI_M);
	        System.out.println(i_index);
	        System.out.println(HuarunHttp.jeuValidate);
	        
	        return params;
	        
    	
    	}catch(Exception e){
    		e.printStackTrace();
    		autoBet.outputGUIMessage("构造下单数据错误！\n");
    		return null;
    	}
   	
    }
    
    public static boolean isBetSuccess(String res){
    	
    	try{
    		JSONObject betRes = new JSONObject(res);
    		
    		int errorCode = betRes.getInt("success");
    		String tipinfo = betRes.getString("tipinfo");
    		
    		if(errorCode == 200 && tipinfo.contains("下注成功")){
    			JSONObject data = betRes.getJSONObject("data");
    			HuarunHttp.jeuValidate = data.getString("JeuValidate");
    			return true;
    		} 
    		
    	}catch(Exception e){
    		e.printStackTrace();
    	}	
    	
    	return false;
    }
    
    public static boolean isBetFailed(String res){
    	
    	try{
    		JSONObject betRes = new JSONObject(res);
    		
    		int errorCode = betRes.getInt("success");
    		
    		if(errorCode != 200){
    			JSONObject data = betRes.getJSONObject("data");
    			HuarunHttp.jeuValidate = data.getString("JeuValidate");
    			System.out.println(res);
    			return true;
    		}
    		
    	}catch(Exception e){
    		e.printStackTrace();
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
	        			//amount = 10;
	        			if(amount < 2)
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
    
    public static String getProfit(String number) { 
    	int dNum = Integer.parseInt(BJSCdrawNumber) - Integer.parseInt(number);
    	if(dNum > 3) {
    		return "---";
    	}
    	
    	String res = null;
    	String res1 = null;
    	double profit = 0;
    	boolean hasResult = false;
		try{			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date currentTime = new Date();
			String dateString = formatter.format(currentTime);
			
			if(dNum == 1 && getRemainTime() < 245) {
				int pos = 0;
				res = HuarunHttp.doGet(HuarunHttp.ADDRESS + "/ReportBill/Report_kc.aspx?findDate=" + dateString, "", "");
				res1 = HuarunHttp.doGet(HuarunHttp.ADDRESS + "/ReportBill/Report_kc.aspx?page=2&findDate=" + dateString + "&lid=&op=&isback=", "", "");
				if(res!= null && res1 != null && res.contains(">" + number + "<")) {
					pos = res.indexOf(">" + number + "<", pos);	
					if(pos > 0) {
						hasResult = true;
					}
					
					while(pos > 0) {
						pos = res.indexOf("</tr>", pos) - 40;
						pos = res.indexOf("right\">", pos) + 7;
						profit += Double.valueOf(res.substring(pos, res.indexOf("&nbsp", pos)));
						pos = res.indexOf(">" + number + "<", pos);
					}
					
					if(res1 != null) {
						pos = res1.indexOf(">" + number + "<", pos);	
						while(pos > 0) {
							pos = res1.indexOf("</tr>", pos) - 40;
							pos = res1.indexOf("right\">", pos) + 7;
							profit += Double.valueOf(res1.substring(pos, res1.indexOf("&nbsp", pos)));
							pos = res1.indexOf(">" + number + "<", pos);
						}
					}
					
					if(hasResult) {
						return String.valueOf((int)profit);
					}
				}	
			} else if(dNum == 2) {
				int pos = 0;
				res = HuarunHttp.doGet(HuarunHttp.ADDRESS + "/ReportBill/Report_kc.aspx?findDate=" + dateString, "", "");
				res1 = HuarunHttp.doGet(HuarunHttp.ADDRESS + "/ReportBill/Report_kc.aspx?page=2&findDate=" + dateString + "&lid=&op=&isback=", "", "");
				String res2 = HuarunHttp.doGet(HuarunHttp.ADDRESS + "/ReportBill/Report_kc.aspx?page=3&findDate=" + dateString + "&lid=&op=&isback=", "", "");
				
				if(res!= null && res1 != null && res2 != null) {
					pos = res.indexOf(">" + number + "<", 0);
					if(pos > 0) {
						hasResult = true;
					}
					
					while(pos > 0) {
						pos = res.indexOf("</tr>", pos) - 40;
						pos = res.indexOf("right\">", pos) + 7;
						profit += Double.valueOf(res.substring(pos, res.indexOf("&nbsp", pos)));
						pos = res.indexOf(">" + number + "<", pos);
					}
					
					if(res1 != null) {
						pos = res1.indexOf(">" + number + "<", 0);	
						if(pos > 0) {
							hasResult = true;
						}
						
						while(pos > 0) {
							pos = res1.indexOf("</tr>", pos) - 40;
							pos = res1.indexOf("right\">", pos) + 7;
							profit += Double.valueOf(res1.substring(pos, res1.indexOf("&nbsp", pos)));
							pos = res1.indexOf(">" + number + "<", pos);
						}
					}
					
					if(res2 != null) {
						pos = res2.indexOf(">" + number + "<", 0);	
						if(pos > 0) {
							hasResult = true;
						}
						
						while(pos > 0) {
							pos = res2.indexOf("</tr>", pos) - 40;
							pos = res2.indexOf("right\">", pos) + 7;
							profit += Double.valueOf(res2.substring(pos, res2.indexOf("&nbsp", pos)));
							pos = res2.indexOf(">" + number + "<", pos);
						}
					}
					
					if(hasResult) {
						return String.valueOf((int)profit);
					}
				}	
			} else if(dNum == 3) {
				int pos = 0;
				res = HuarunHttp.doGet(HuarunHttp.ADDRESS + "/ReportBill/Report_kc.aspx?page=2&findDate=" + dateString + "&lid=&op=&isback=", "", "");
				res1 = HuarunHttp.doGet(HuarunHttp.ADDRESS + "/ReportBill/Report_kc.aspx?page=3&findDate=" + dateString + "&lid=&op=&isback=", "", "");
				String res2 = HuarunHttp.doGet(HuarunHttp.ADDRESS + "/ReportBill/Report_kc.aspx?page=4&findDate=" + dateString + "&lid=&op=&isback=", "", "");
				if(res != null && res2 != null && res1 != null) {
					pos = res.indexOf(">" + number + "<", 0);	
					if(pos > 0) {
						hasResult = true;
					}
					
					while(pos > 0) {
						pos = res.indexOf("</tr>", pos) - 40;
						pos = res.indexOf("right\">", pos) + 7;
						profit += Double.valueOf(res.substring(pos, res.indexOf("&nbsp", pos)));
						pos = res.indexOf(">" + number + "<", pos);
					}
					
					if(res1 != null) {
						pos = res1.indexOf(">" + number + "<", 0);	
						if(pos > 0) {
							hasResult = true;
						}
						
						while(pos > 0) {
							pos = res1.indexOf("</tr>", pos) - 40;
							pos = res1.indexOf("right\">", pos) + 7;
							profit += Double.valueOf(res1.substring(pos, res1.indexOf("&nbsp", pos)));
							pos = res1.indexOf(">" + number + "<", pos);
						}
					}
					
					if(res2 != null) {
						pos = res2.indexOf(">" + number + "<", 0);	
						if(pos > 0) {
							hasResult = true;
						}
						
						while(pos > 0) {
							pos = res2.indexOf("</tr>", pos) - 40;
							pos = res2.indexOf("right\">", pos) + 7;
							profit += Double.valueOf(res2.substring(pos, res2.indexOf("&nbsp", pos)));
							pos = res2.indexOf(">" + number + "<", pos);
						}
					}
					
					if(hasResult) {
						return String.valueOf((int)profit);
					}
				}	
			} else if(dNum == 0 && isInLastTime()) {
				int pos = 0;
				res = HuarunHttp.doGet(HuarunHttp.ADDRESS + "/ReportBill/Report_kc.aspx?findDate=" + dateString, "", "");
				res1 = HuarunHttp.doGet(HuarunHttp.ADDRESS + "/ReportBill/Report_kc.aspx?page=2&findDate=" + dateString + "&lid=&op=&isback=", "", "");
				if(res!= null && res1 != null && res.contains(">" + number + "<")) {
					pos = res.indexOf(">" + number + "<", pos);	
					if(pos > 0) {
						hasResult = true;
					}
					
					while(pos > 0) {
						pos = res.indexOf("</tr>", pos) - 40;
						pos = res.indexOf("right\">", pos) + 7;
						profit += Double.valueOf(res.substring(pos, res.indexOf("&nbsp", pos)));
						pos = res.indexOf(">" + number + "<", pos);
					}
					
					if(res1 != null) {
						pos = res1.indexOf(">" + number + "<", pos);	
						while(pos > 0) {
							pos = res1.indexOf("</tr>", pos) - 40;
							pos = res1.indexOf("right\">", pos) + 7;
							profit += Double.valueOf(res1.substring(pos, res1.indexOf("&nbsp", pos)));
							pos = res1.indexOf(">" + number + "<", pos);
						}
					}
					
					if(hasResult) {
						return String.valueOf((int)profit);
					}
				}	
			}
    	}catch(Exception e){
    		e.printStackTrace();
    		System.out.println(res);
    	}
    	return "---";
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
    		if((currentDraw - idrawNumber) >= 3){
    			unCalcProfitBJSCDraw.removeElement(unCalcProfitBJSCDraw.elementAt(j));
    		}
    	}
    	
    	
    	int yichangshu1 = unknowStatBJSCDraw.size();
    	
    	for(int j =0; j < unknowStatBJSCDraw.size(); j++){
    		int idrawNumber = Integer.parseInt(unknowStatBJSCDraw.elementAt(j));
    		if((currentDraw - idrawNumber) >= 3){
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
    
  
    
    public static boolean isBJSCidle(){
    	boolean isIdle = false;
    	long time = getRemainTime();
    	
    	if(time < 0 || time > 53 ){
    		isIdle = true;
    	}
    	
    	return isIdle;
    }
    
}
