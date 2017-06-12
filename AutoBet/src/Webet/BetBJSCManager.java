package Webet;

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
	
	
	//static JSONObject gameOdds = new JSONObject();
	
	
	public static long webTime = -1;  //seconds
	
	public static long closeTime = -1; //seconds
	
	public static long timeDvalue = -1; //sceonds
	
	
	
	static String loadbjdatauripart = "";
	static String loaddatauripart = "";
	
	static String linenow = "";
	static String gametype = "";
	static String varH = "157";
	static String varS = "741";
	static String varU = "793";
	static String varM = "";
	static String D3HallSwitch = "";
	static String kang = "";
	static String gid = "";
	
	
	static JSONObject gameOdds = null;
	static JSONObject oddDvalue = null;
	
	
	
	public static void init(){
		BJSCdetalsDataWindow.setTitle("[惟博]投注北京赛车详情");
		BJSCBetAmountWindow.setTitle("[惟博]北京赛车下注金额");
	}
	
	
	public static boolean changeToBJgame(){
		
		try{
			
			String res =  WebetHttp.changeToBJgame();
			
			if(null != res && res.contains("game_main.jsp")){
				return true;
			}else{
				return false;
			}
			
			

	    	

			
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		

		
	}
	
	
	public static String getProfit(String drawNumber){
		
		String profit = "---";
		
		String maxPage = "1";
		
		try{
			
			String profitUri = WebetHttp.lineuri + "z/mem2-betAcountY";
			
			String res = WebetHttp.doGet(profitUri, WebetHttp.strCookies, "");
			
			int posStart = -1;
			int posEnd = -1;
			
			if(res != null){
				posStart = res.indexOf("endpage");
				posStart = res.indexOf(":", posStart);
				posEnd = res.indexOf(",",posStart);
				maxPage = res.substring(posStart+1, posEnd);
			}
			
			profitUri = WebetHttp.lineuri + "z/mem2-betAcountY" + "?lottery=ALL&ptype=2&page=" + maxPage;
			
			res = WebetHttp.doGet(profitUri, WebetHttp.strCookies, "");
			
			
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
			
			
        	String res =  WebetHttp.doGet(WebetHttp.gameMainUri, "", "");
        	
        	
        	
        	if(res == null){
        		res =  WebetHttp.doGet(WebetHttp.gameMainUri, "", "");
        	}
        	
        	if(null == res || res.contains("找不到登入记录")){
        		WebetHttp.setIsNeedRelogin(true);
        		return false;
        	}
			
	    	int ps = res.indexOf("URL");
	    	ps = res.indexOf("p=", ps);
	    	int pe = res.indexOf("\'", ps);
	    	
	    	loaddatauripart = res.substring(ps, pe);
	    	
	    	ps = res.indexOf("Line_Now", pe);
	    	ps = res.indexOf("\'", ps) + 1;
	    	pe = res.indexOf("\'", ps);
	    	
	    	linenow = res.substring(ps, pe);
	    	
	    	ps = res.indexOf("var H", pe);
	    	ps = res.indexOf("\'", ps) + 1;
	    	pe = res.indexOf("\'", ps);
	    	varH = res.substring(ps, pe);
	    	
	    	ps = res.indexOf("var S", pe);
	    	ps = res.indexOf("\'", ps) + 1;
	    	pe = res.indexOf("\'", ps);
	    	varS = res.substring(ps, pe);
	    	
	    	ps = res.indexOf("var U", pe);
	    	ps = res.indexOf("\'", ps) + 1;
	    	pe = res.indexOf("\'", ps);
	    	varU = res.substring(ps, pe);
	    	
	    	ps = res.indexOf("D3HallSwitch", pe);
	    	ps = res.indexOf("\"", ps) + 1;
	    	pe = res.indexOf("\"", ps);
	    	D3HallSwitch = res.substring(ps, pe);
	    	
	    	ps = res.indexOf("var M", pe);
	    	ps = res.indexOf("\"", ps) + 1;
	    	pe = res.indexOf("\"", ps);
	    	varM = res.substring(ps, pe);
	    	
	    	
	    	ps = res.indexOf("kang", pe);
	    	ps = res.indexOf("=", ps);
	    	ps = res.indexOf("\"", ps) + 1;
	    	pe = res.indexOf("\"", ps);
	    	kang = res.substring(ps, pe);
			
			
			
			
			
			
			
			
			
			String load_data_cr = WebetHttp.lineuri + "/d3m1/app/game/load_data_cr.php?" + loaddatauripart  + "&ComTag=Y&Line=" +  linenow + "&gametype=BJ&showtype=all&crtype=Y&H=" + varH + "&S=" + varS + "&M=" + varM;
			
			res = WebetHttp.doGet(load_data_cr, "", "");
			
			if(res == null){
				res = WebetHttp.doGet(load_data_cr, "", "");
			}
			
			ps = res.indexOf("gid");
			ps = res.indexOf(":\"", ps) + 2;
			pe = res.indexOf("\"", ps);
			
			gid = res.substring(ps, pe);
			
			ps = res.indexOf("gnum");
			ps = res.indexOf(":\"", ps) + 2;
			pe = res.indexOf("\"", ps);
			
			BJSCdrawNumber = res.substring(ps, pe);
			
			System.out.println(res);
			
			
			String loadgamedata = WebetHttp.lineuri + "/d3m1/bjapp/ajaxdata/load_data.jsp?" +loaddatauripart + "&_=" + 
											Long.toString(System.currentTimeMillis()) + "&play_code=DB&H=" + varH +"&S=" + varS + "&U=" + varU + "&GameNum=" + "0" +
											"&Line_Now=" + linenow + "&game_id=" + "0" + "&mode=normal&D3HallSwitch=" + D3HallSwitch;
	        
	        
			res = WebetHttp.doGet(loadgamedata, "", "");
			
			if(res == null){
				res = WebetHttp.doGet(loadgamedata, "", "");
				
				
			}
			
			System.out.println(res);
			
			
			
	        if(res != null){
	        	
	        	//System.out.println(res);
	        	
	        	ps = res.indexOf("GameID");
	        	ps = res.indexOf(">", ps) + 1;
	        	pe = res.indexOf("<", ps);
	        	gid = res.substring(ps, pe);
	        	
	        	ps = res.indexOf("GameNum");
	        	ps = res.indexOf(">", ps) + 1;
	        	pe = res.indexOf("<", ps);
	        	BJSCdrawNumber = res.substring(ps, pe);
	        	
	        	
	        	ps = res.indexOf("StopTimeSec");
	        	ps = res.indexOf(">", ps) + 1;
	        	pe = res.indexOf("<", ps);
	        	
	        	remainTime = Integer.parseInt(res.substring(ps, pe));
	        	
	        	closeTime = System.currentTimeMillis() + remainTime* 1000;
	        	
	        	ps = res.indexOf("{", pe);
	        	pe = res.indexOf(";", ps);
	        	
	        	String oddsStr = res.substring(ps, pe);
	        	
	        	gameOdds = new JSONObject(oddsStr);
	        	
	        	JSONObject tmpobj = gameOdds.getJSONObject("BS_" + gid + "_CNB01");
	        	
	        	ps = res.indexOf("[", pe) + 1;
	        	pe = res.indexOf("]", ps);
	        	
	        	String oddsDvalueStr = res.substring(ps, pe);
	        	
	        	oddDvalue = new JSONObject(oddsDvalueStr);
	        	
	        	
	        	
	        	
	        	//double bsDvalue = Double.parseDouble(oddDvalue.getString("Line_BS"));
	        	
	        	
	        	
	        	//System.out.println(oddsDvalueStr);

	        	
	        	return true;
	        	
	        }
			
		}catch(Exception e){
			e.printStackTrace();
			WebetHttp.addFailsTimes();
			return false;
		}
		
		WebetHttp.addFailsTimes();
		
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
/*		long time = System.currentTimeMillis();
        Date date = new Date(time);
        int currentHour = date.getHours();
        int currentMinutes = date.getMinutes();
        int currentSeconds = date.getSeconds();
        
        
        long currentTime = currentHour*60*60 + currentMinutes*60 + currentSeconds;*/
        
        remainTime = (closeTime  - System.currentTimeMillis())/1000;
        
        return remainTime;
	}
	
	
    public static boolean grabOddsData(){
    	
    	String res = "";
    	
    	

        try{
        	String url = WebetHttp.lineuri + "z/gPKT-pkt7?gi=11&bt=4";
        	
        	//System.out.println(WebetHttp.strCookies);
        	
        	
        	res = WebetHttp.doGet(url, WebetHttp.strCookies, "");
        	
        	
        	
        	if(res == null){
        		res = WebetHttp.doGet(url, WebetHttp.strCookies, "");
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
            		//System.out.println(gameOdds.toString());
            		
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
    	
    	
        WebetHttp.addFailsTimes();
    	
    	
    	
    	return  false;
   	
    }
    
    
    public static boolean doBetBJSC(String[] betData, double percent,boolean opposite, String remainTime)
    {

    	String host = WebetHttp.lineuri;
       	
        String betStr = "";
        
        if(previousBJSCBetNumber.equals(BJSCdrawNumber)) {
        	System.out.println("previousBJSCBetNumber == BJSCdrawNumber");
        	return false;
        }
        	
        
        
        BJSCjinriqishu++;
        
        BJSCdetalsDataWindow.updateTextFieldjinriqishu(Integer.toString(BJSCjinriqishu));
        
        BJSCdetalsDataWindow.updateTextFieldzongqishu(Integer.toString(BJSCzongqishu + BJSCjinriqishu));
        
        //如果未到封盘时间
        if( BJSCdrawNumber != null){
        	
        	//System.out.printf("下注北京赛车第%s期\n",BJSCdrawNumber);
        	String outputStr = "惟博下注北京赛车第" + BJSCdrawNumber + "期\n"  + "最新数据时间距收盘" + remainTime + "秒\n";
        	autoBet.outputGUIMessage(outputStr);
        	
        	
        	String betMode = "";
        	
        	if(opposite == false){
        		betMode = "正投";
        	}else{
        		betMode = "反投";
        	}
        	
        	outputStr = String.format("%s, 下注比例：%f\n", betMode, percent);
        	autoBet.outputGUIMessage(outputStr);
        	
        	
        	betStr = constructBetsData(betData, percent,  opposite);
        	
        	if(!betStr.contains("@")) {
        		outputStr = "代理无人投注\n\n";
        		autoBet.outputGUIMessage(outputStr);
        		return false;
        	}
        	
        	
        	
        	String outputBetData = constructoutputData(betData, percent,  opposite);
        	
        	
        	
        	
        	
        	addToBetAmountWindow(outputBetData);
        	
        	
        	        	
        	outputBetsDetails(outputBetData);
        	
        	//System.out.println(betStr);
        	
        	String response = "";  	
        	
        	
        	
        	
        	
        	long time1 = System.currentTimeMillis();
        	
	        List<NameValuePair> params = new ArrayList<NameValuePair>();
	        params.add(new BasicNameValuePair("kang", kang));
	        params.add(new BasicNameValuePair("play_code", "DB"));
	        params.add(new BasicNameValuePair("game_id", gid));
	        params.add(new BasicNameValuePair("Line", linenow));
	        params.add(new BasicNameValuePair("order_num", betStr));
	        params.add(new BasicNameValuePair("trace_order_num", ""));
	        
        	
	        int oldTimeout = WebetHttp.defaultTimeout;
	        
	        WebetHttp.defaultTimeout = 10*1000;
	        
        	
        	response = WebetHttp.doPost(WebetHttp.lineuri + "/d3m1/bjapp/ajaxdata/order_action.jsp?" + loaddatauripart, params, "", WebetHttp.gameMainUri);
        	
        	System.out.printf("下单结果:%s\n", response);
        	
        	boolean betRes = isBetSuccess(response);
        	
        	if((betRes == false)){
        		response = WebetHttp.doPost(WebetHttp.lineuri + "/d3m1/bjapp/ajaxdata/order_action.jsp?" + loaddatauripart, params, "", WebetHttp.gameMainUri);
        		
        		System.out.printf("再次下单结果:%s\n", response);
        	}
        	
        	
        	
        	
        	WebetHttp.defaultTimeout = oldTimeout;

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
    	
    	String res = "";
    	
    	//res =res + "," + BJSCdrawNumber;
    	
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
	        			//amount = 10;
	        			if(amount < 2){
	        				continue;
	        			}
	        				
	        			totalAmount += amount;	        				        				        			
	        			//处理反投: 大变小，小变大，单变双，双变大，龙变虎，虎变隆
	        				
        				//autoBet.outputGUIMessage(game + contents + ":" + Integer.toString(amount));
        				
        				if(game.indexOf("DX") != -1){//反大小
/*        					if(contents.indexOf("D") != -1 && opposite){
        						contents = "2";        						
        					}
        					else{
        						contents = "1";
        					}*/
        					
        					if(contents.indexOf("D") != -1){
        						
        						if(opposite == false){
        							contents = "B";
        						}
        						else{
        							contents = "S";
        						}
        						
        					}else{
        						if(opposite == false){
        							contents = "S";
        						}
        						else{
        							contents = "B";
        						}
        						
        					}
        					
        					
        					
        					
        					
        					
        					int index = Integer.parseInt(game.replace("DX", ""));
        					
        					String indexstr = String.format("%02d", index);
        					
        					String oddKey = "BS_" + gid + "_CN" + contents + indexstr;
        					
        					double odd = Double.parseDouble(gameOdds.getJSONObject(oddKey).getString("io"));
        					double lineDavlue = Double.parseDouble(oddDvalue.getString("Line_BS"));
        					
        					odd = odd + lineDavlue;
        					
        					
        					
        					if(res.equals("")){
        						res = "BS@CN" + contents + indexstr + "@" + Double.toString(odd) + "@" + Integer.toString(amount);
        					}else{
        						res = res + "," + "BS@CN" + contents + indexstr + "@" + Double.toString(odd)+ "@" + Integer.toString(amount);
        					}
        					
        					
        				}
        				
        				
        				if(game.indexOf("DS") != -1){//反单双
/*        					if(contents.indexOf("D") != -1 && opposite){
        						contents = "2";        						
        					}
        					else{
        						contents = "1";
        					}*/
        					
        					if(contents.indexOf("D") != -1){
        						
        						if(opposite == false){
        							contents = "E";
        						}
        						else{
        							contents = "D";
        						}
        						
        					}else{
        						if(opposite == false){
        							contents = "D";
        						}
        						else{
        							contents = "E";
        						}
        						
        					}
        					
        					
        					int index = Integer.parseInt(game.replace("DS", ""));
        					
        					String indexstr = String.format("%02d", index);
        					
        					String oddKey = "ED_" + gid + "_CN" + contents + indexstr;
        					
        					double odd = Double.parseDouble(gameOdds.getJSONObject(oddKey).getString("io"));
        					double lineDavlue = Double.parseDouble(oddDvalue.getString("Line_ED"));
        					
        					odd = odd + lineDavlue;
        					
        					
        					
        					if(res.equals("")){
        						res = "ED@CN" + contents + indexstr + "@" + Double.toString(odd)+ "@" + Integer.toString(amount);
        					}else{
        						res = res + "," + "ED@CN" + contents + indexstr + "@" + Double.toString(odd)+ "@" + Integer.toString(amount);
        					}

        				}
        				
        				if(game.indexOf("LH") != -1){//反龙虎
/*        					if(contents.indexOf("L") != -1 && opposite){
        						contents = "2";        						
        					}
        					else{
        						contents = "1";
        					}*/
        					
        					if(contents.indexOf("L") != -1){
        						
        						if(opposite == false){
        							contents = "TOR";
        						}
        						else{
        							contents = "TIG";
        						}
        						
        					}else{
        						if(opposite == false){
        							contents = "TIG";
        						}
        						else{
        							contents = "TOR";
        						}
        						
        					}
        					
        					int index = Integer.parseInt(game.replace("LH", ""));
        					int indexOther = 11 - index;
        					
        					String indexstr = String.format("%02d", index);
        					String indexOtherstr = String.format("%02d", indexOther);
        					
        					String oddKey = "TT_" + gid + "_" + contents + indexstr + indexOtherstr;
        					
        					double odd = Double.parseDouble(gameOdds.getJSONObject(oddKey).getString("io"));
        					double lineDavlue = Double.parseDouble(oddDvalue.getString("Line_TT"));
        					
        					odd = odd + lineDavlue;
        					
        					
        					
        					if(res.equals("")){
        						res = "TT@" + contents + indexstr + indexOtherstr + "@" + Double.toString(odd)+ "@" + Integer.toString(amount);
        					}else{
        						res = res + "," + "TT@" + contents + indexstr + indexOtherstr + "@" + Double.toString(odd)+ "@" + Integer.toString(amount);
        					}
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
    		
    		String errorCode = betRes.getString("error");
    		
    		if(errorCode.contains("false")){
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
    		
    		String errorCode = betRes.getString("error");
    		

    		
    		if(errorCode.contains("false")){
    			autoBet.outputGUIMessage("下单成功\n");
    			return true;
    		}
    		else{
    			autoBet.outputGUIMessage("下单未知");
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
    		
    		List<String> parsedGames = new ArrayList<String>();
    		
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
	        			if(amount <2 ){
	        				continue;
	        			}
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
        	String balanceURI = WebetHttp.lineuri + "z/user-info";
        	
        	String res = WebetHttp.doGet(balanceURI, WebetHttp.strCookies, "");
        	
        	
        	
        	if(res == null){
        		res = WebetHttp.doGet(balanceURI, WebetHttp.strCookies, "");
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
