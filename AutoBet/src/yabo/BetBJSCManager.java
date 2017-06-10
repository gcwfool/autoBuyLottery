package yabo;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.events.Attribute;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

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
		BJSCdetalsDataWindow.setTitle("[亚博]投注北京赛车详情");
		BJSCBetAmountWindow.setTitle("[亚博]北京赛车下注金额");
	}
	
	public static boolean grabGameInfo() {
		String res = null;
		try{      	
	    	
	    	long time1 = 0, time2 = 0;
	    	time1 = System.currentTimeMillis();
        	res = YaboHttp.doGet(YaboHttp.ADDRESS + "/bjsc/read_multiple.do?LT=3&T=21&GT=30,31,32,34,35,36,38,39,40,42,43,44,46,47,48,50,51,53,54,56,57,59,60,62,63,64,65",  "", "");
        	time2 = System.currentTimeMillis();
        	
        	//System.out.println(res);
        	
        	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();           
    		
			DocumentBuilder db = dbf.newDocumentBuilder();
	        Document document = db.parse(new ByteArrayInputStream(res.getBytes("utf-8")));
	        
	        NodeList list = document.getElementsByTagName("ROOT"); 
	        Element element = (Element)list.item(0);
	        
	        String stop_time = element.getElementsByTagName("Stoptime").item(0).getFirstChild().getNodeValue();
	        System.out.println(stop_time);
    		if(stop_time.equals("")) {
    			return true;
    		}
    	
        	String[] timeArray = stop_time.split(":");
    		
    		int hour = Integer.parseInt(timeArray[0]);
    		int min = Integer.parseInt(timeArray[1]);
    		int sec = Integer.parseInt(timeArray[2]);
    		
    		if(element.getElementsByTagName("k_stat").item(0).getFirstChild().getNodeValue().equals("y") && time2 - time1 < 2500) {
		        closeTime = hour * 3600 + min * 60 + sec;
		        closeTime = System.currentTimeMillis()/1000 + closeTime;
    		}
    		
    		//期数
    		String number = element.getElementsByTagName("k_qs").item(0).getFirstChild().getNodeValue();
    		 if(!BJSCdrawNumber.equals(number)) {//新的一期
             	previousBJSCdrawNumber = BJSCdrawNumber;
             	BJSCdrawNumber = number;
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
    		 
    		BJSCdrawNumber = number;
    		System.out.println(BJSCdrawNumber);
	       
    		//赔率
	        list = element.getElementsByTagName("Multiple_Info").item(0).getChildNodes();
	        for (int k = 0; k < list.getLength(); k++) {   
	        	//System.out.println(list.item(k).getNodeName() + ":" +  list.item(k).getTextContent());
	        	oddsPairs.put(list.item(k).getNodeName(), list.item(k).getTextContent());
	        }
	        
	        //余额
	        usableCredit = element.getElementsByTagName("Money_KY").item(0).getFirstChild().getNodeValue();
    	    updateBJSCBalance(usableCredit);	
    	    return true;
 
    	}catch(Exception e){
    		e.printStackTrace();
    		System.out.println(res);
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

    	String host = YaboHttp.ADDRESS;
       	
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
        	String outputStr = "亚博下注北京赛车第" + BJSCdrawNumber + "期\n"  + "最新数据时间距收盘" + remainTime + "秒\n";
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
        	////////////////////////
//        	if(true) {
//        		return true;
//        	}
        	///////////////////////////
        	//System.out.println(betStr);
        	
        	String response = "";       	
   	
	        int oldTimeout = YaboHttp.defaultTimeout;
	        
	        YaboHttp.defaultTimeout = 10*1000;
	        System.out.println(getRemainTime());
	        
        	
        	response = YaboHttp.doPost(YaboHttp.ADDRESS + "/bjsc/order.do", params, "", "");
        	
        	System.out.printf("下单结果:%s\n", response);
        	//boolean failed = isBetFailed(response);
        	boolean betRes = isBetSuccess(response);
        	if(betRes) {
        		try {
	        		int posStart = response.indexOf("location") + 10;
	        		String uri = response.substring(posStart, response.indexOf("'", posStart));
	        		response = YaboHttp.doGet(YaboHttp.ADDRESS + uri, "", "");
	        		if(!response.contains("Update_JV(")) {
	        			response = YaboHttp.doGet(YaboHttp.ADDRESS + uri, "", "");
	        		}
	        		if(response.contains("Update_JV(")) {
	        			posStart = response.indexOf("Update_JV(") + 11;
	        			YaboHttp.jeuValidate = response.substring(posStart, response.indexOf("\"", posStart));
	        		}
	        		System.out.println(response);
        		} catch(Exception e) {
        		}
        	}
//        	
//        	if((failed == true) && getRemainTime() > 0){
//        		if(!response.contains("newpl")) {
//        			params = constructBetsData(betData, percent,  opposite, saves);
//        		} else {
//        			try{
//        			   JSONObject newpl = new JSONObject(response);
//         			   JSONObject data = newpl.getJSONObject("data");
//         			   JSONArray arr = data.getJSONArray("index");
//         			   JSONArray arr1 = data.getJSONArray("newpl");
//         			   int size = arr.length();
//         			   int [] indexs = new int[size];
//         			   String [] pls = new String[size];
//         			   for(int i = 0; i < size; i++) {
//         				   indexs[i] = arr.getInt(i);
//         				   pls[i] = arr1.getString(i);
//         			   } 
//         			   
//         			   params = reConstructBetsData(betData, percent,  opposite, saves, indexs, pls, size);
//        	    		
//        	    	}catch(Exception e){
//        	    		e.printStackTrace();
//        	    	}			   
//        		}
//        		response = YaboHttp.doPost(YaboHttp.ADDRESS + "/L_PK10/Handler/Handler.ashx", params, "", YaboHttp.ADDRESS + "/L_PK10/index.aspx?lid=2&path=L_PK10");	  
//        		betRes = isBetSuccess(response);
//        		failed = isBetFailed(response);
//        		System.out.printf("再次下单结果:%s\n", response);
//        	}
        	
        	YaboHttp.defaultTimeout = oldTimeout;

        	long time2 = System.currentTimeMillis();
        	
        	long timeD = time2 - time1;
        	
        	double usingTime = timeD;
        	
        	usingTime = usingTime/1000;
        	
        	String strUsingTime  = String.format("下单用时！ :%f 秒\n\n", usingTime);
        	
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
	        			if(amount < 5)
	        				continue; //添彩每注最低2元
	        			if(amount > 100000) {
	        				amount = 100000;
	        			}

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
	        	        		contents = contents.equals("D")?"m_30_548":"m_30_549";
	        	        		outputName = "冠军";
	        	        		outputContent = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS1":
	        	        		selectionTypeName = "1";
	        	        		contents = contents.equals("D")?"m_31_550":"m_31_551";
	        	        		outputName = "冠军";
	        	        		outputContent = contents.equals("D")?"单":"双";
	        	        		break;
	        	        	case "LH1":
	        	        		selectionTypeName = "1";
	        	        		contents = contents.equals("L")?"m_32_552":"m_32_553";
	        	        		outputName = "冠军";
	        	        		outputContent = contents.equals("L")?"龙":"虎";
	        	        		break;	
	        	        		
	        	        	case "DX2":
	        	        		selectionTypeName = "2";
	        	        		contents = contents.equals("D")?"m_34_554":"m_34_555";
	        	        		outputName = "亚军";
	        	        		outputContent = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS2":
	        	        		selectionTypeName = "2";
	        	        		contents = contents.equals("D")?"m_35_556":"m_35_557";
	        	        		outputName = "亚军";
	        	        		outputContent = contents.equals("D")?"单":"双";
	        	        		break;
	        	        	case "LH2":
	        	        		selectionTypeName = "2";
	        	        		contents = contents.equals("L")?"m_36_558":"m_36_559";
	        	        		outputName = "亚军";
	        	        		outputContent = contents.equals("L")?"龙":"虎";
	        	        		break;		
	        	        	case "DX3":
	        	        		selectionTypeName = "3";
	        	        		contents = contents.equals("D")?"m_38_560":"m_38_561";
	        	        		outputName = "第三名";
	        	        		outputContent = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS3":
	        	        		selectionTypeName = "3";
	        	        		contents = contents.equals("D")?"m_39_562":"m_39_563";	
	        	        		outputName = "第三名";
	        	        		outputContent = contents.equals("D")?"单":"双";
	        	        		break;
	        	        	case "LH3":
	        	        		selectionTypeName = "3";
	        	        		contents = contents.equals("L")?"m_40_564":"m_40_565";
	        	        		outputName = "第三名";
	        	        		outputContent = contents.equals("L")?"龙":"虎";
	        	        		break;		
	        	        	case "DX4":
	        	        		selectionTypeName = "4";
	        	        		contents = contents.equals("D")?"m_42_566":"m_42_567";
	        	        		outputName = "第四名";
	        	        		outputContent = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS4":
	        	        		selectionTypeName = "4";
	        	        		contents = contents.equals("D")?"m_43_568":"m_43_569";
	        	        		outputName = "第四名";
	        	        		outputContent = contents.equals("D")?"单":"双";
	        	        		break;
	        	        	case "LH4":
	        	        		selectionTypeName = "4";
	        	        		contents = contents.equals("L")?"m_44_570":"m_44_571";
	        	        		outputName = "第四名";
	        	        		outputContent = contents.equals("L")?"龙":"虎";
	        	        		break;	
	        	        	case "DX5":
	        	        		selectionTypeName = "5";
	        	        		contents = contents.equals("D")?"m_46_572":"m_46_573";
	        	        		outputName = "第五名";
	        	        		outputContent = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS5":
	        	        		selectionTypeName = "5";
	        	        		contents = contents.equals("D")?"m_47_574":"m_47_575";	
	        	        		outputName = "第五名";
	        	        		outputContent = contents.equals("D")?"单":"双";
	        	        		break;
	        	        	case "LH5":
	        	        		selectionTypeName = "5";
	        	        		contents = contents.equals("L")?"m_48_576":"m_48_577";
	        	        		outputName = "第五名";
	        	        		outputContent = contents.equals("L")?"龙":"虎";
	        	        		break;	
	        	        	case "DX6":
	        	        		selectionTypeName = "6";
	        	        		contents = contents.equals("D")?"m_50_578":"m_50_579";
	        	        		outputName = "第六名";
	        	        		outputContent = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS6":
	        	        		selectionTypeName = "6";
	        	        		contents = contents.equals("D")?"m_51_580":"m_51_581";	
	        	        		outputName = "第六名";
	        	        		outputContent = contents.equals("D")?"单":"双";
	        	        		break;
	        	        	case "DX7":
	        	        		selectionTypeName = "7";
	        	        		contents = contents.equals("D")?"m_53_582":"m_53_583";
	        	        		outputName = "第七名";
	        	        		outputContent = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS7":
	        	        		selectionTypeName = "7";
	        	        		contents = contents.equals("D")?"m_54_584":"m_54_585";	
	        	        		outputName = "第七名";
	        	        		outputContent = contents.equals("D")?"单":"双";
	        	        		break;
	        	        	case "DX8":
	        	        		selectionTypeName = "8";
	        	        		contents = contents.equals("D")?"m_56_586":"m_56_587";
	        	        		outputName = "第八名";
	        	        		outputContent = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS8":
	        	        		selectionTypeName = "8";
	        	        		contents = contents.equals("D")?"m_57_588":"m_57_589";
	        	        		outputName = "第八名";
	        	        		outputContent = contents.equals("D")?"单":"双";
	        	        		break;
	        	        	case "DX9":
	        	        		selectionTypeName = "9";
	        	        		contents = contents.equals("D")?"m_59_590":"m_59_591";
	        	        		outputName = "第九名";
	        	        		outputContent = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS9":
	        	        		selectionTypeName = "9";
	        	        		contents = contents.equals("D")?"m_60_592":"m_60_593";	
	        	        		outputName = "第九名";
	        	        		outputContent = contents.equals("D")?"单":"双";
	        	        		break;
	        	        	case "DX10":
	        	        		selectionTypeName = "10";
	        	        		contents = contents.equals("D")?"m_62_594":"m_62_595";
	        	        		outputName = "第十名";
	        	        		outputContent = contents.equals("D")?"大":"小";
	        	        		break;
	        	        	case "DS10":
	        	        		selectionTypeName = "10";
	        	        		contents = contents.equals("D")?"m_63_596":"m_63_597";
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
	    	
	    	String oddsid = "";
	    	String uPI_ID = "";
	    	String uPI_P = "";
	    	String uPI_M = "";
	    	String i_index = "";
	    
	    	System.out.println("下单数据：------------------------------------");
	    	List<NameValuePair> params = new ArrayList<NameValuePair>();
	    	if(betPairs.containsKey("m_30_548")) {
	    		uPI_ID += "," + "548";
	    		uPI_P += "," + oddsPairs.get("m_30_548");
	    		uPI_M += "," + betPairs.get("m_30_548");
	    		params.add(new BasicNameValuePair("jeuM_30_548", betPairs.get("m_30_548")));
	    	}else {
	    		params.add(new BasicNameValuePair("jeuM_30_548", ""));
	    	}
	    	
	    	if(betPairs.containsKey("m_30_549")) {
	    		uPI_ID += "," + "549";
	    		uPI_P += "," +  oddsPairs.get("m_30_549");
	    		uPI_M += "," +  betPairs.get("m_30_549");
	    		params.add(new BasicNameValuePair("jeuM_30_549", betPairs.get("m_30_549")));
	    	}else {
	    		params.add(new BasicNameValuePair("jeuM_30_549", ""));
	    	}
	    	
	    	if(betPairs.containsKey("m_31_550")) {
	    		uPI_ID += "," + "550";
	    		uPI_P += "," +  oddsPairs.get("m_31_550");
	    		uPI_M += "," +  betPairs.get("m_31_550");
	    		params.add(new BasicNameValuePair("jeuM_31_550", betPairs.get("m_31_550")));
	    	}else {
	    		params.add(new BasicNameValuePair("jeuM_31_550", ""));
	    	}
	    	
	    	if(betPairs.containsKey("m_31_551")) {
	    		uPI_ID += "," + "551";
	    		uPI_P += "," +  oddsPairs.get("m_31_551");
	    		uPI_M += "," +  betPairs.get("m_31_551");
	    		params.add(new BasicNameValuePair("jeuM_31_551", betPairs.get("m_31_551")));
	    	}else {
	    		params.add(new BasicNameValuePair("jeuM_31_551", ""));
	    	}
	    	
	    	if(betPairs.containsKey("m_32_552")) {
	    		uPI_ID += "," + "552";
	    		uPI_P += "," +  oddsPairs.get("m_32_552");
	    		uPI_M += "," +  betPairs.get("m_32_552");
	    		params.add(new BasicNameValuePair("jeuM_32_552", betPairs.get("m_32_552")));
	    	}else {
	    		params.add(new BasicNameValuePair("jeuM_32_552", ""));
	    	}
	    	
	    	if(betPairs.containsKey("m_32_553")) {
	    		uPI_ID += "," + "553";
	    		uPI_P += "," +  oddsPairs.get("m_32_553");
	    		uPI_M += "," +  betPairs.get("m_32_553");
	    		params.add(new BasicNameValuePair("jeuM_32_553", betPairs.get("m_32_553")));
	    	}else {
	    		params.add(new BasicNameValuePair("jeuM_32_553", ""));
	    	}
	    	
	    	if(betPairs.containsKey("m_34_554")) {
	    		uPI_ID += "," + "554";
	    		uPI_P += "," +  oddsPairs.get("m_34_554");
	    		uPI_M += "," +  betPairs.get("m_34_554");
	    		params.add(new BasicNameValuePair("jeuM_34_554", betPairs.get("m_34_554")));
	    	}else {
	    		params.add(new BasicNameValuePair("jeuM_34_554", ""));
	    	}
	    	
	    	if(betPairs.containsKey("m_34_555")) {
	    		uPI_ID += "," + "555";
	    		uPI_P += "," +  oddsPairs.get("m_34_555");
	    		uPI_M += "," +  betPairs.get("m_34_555");
	    		params.add(new BasicNameValuePair("jeuM_34_555", betPairs.get("m_34_555")));
	    	}else {
	    		params.add(new BasicNameValuePair("jeuM_34_555", ""));
	    	}
	    	
	    	if(betPairs.containsKey("m_35_556")) {
	    		uPI_ID += "," + "556";
	    		uPI_P += "," +  oddsPairs.get("m_35_556");
	    		uPI_M += "," +  betPairs.get("m_35_556");
	    		params.add(new BasicNameValuePair("jeuM_35_556", betPairs.get("m_35_556")));
	    	}else {
	    		params.add(new BasicNameValuePair("jeuM_35_556", ""));
	    	}
	    	
	    	if(betPairs.containsKey("m_35_557")) {
	    		uPI_ID += "," + "557";
	    		uPI_P += "," +  oddsPairs.get("m_35_557");
	    		uPI_M += "," +  betPairs.get("m_35_557");
	    		params.add(new BasicNameValuePair("jeuM_35_557", betPairs.get("m_35_557")));
	    	}else {
	    		params.add(new BasicNameValuePair("jeuM_35_557", ""));
	    	}
	    	
	    	if(betPairs.containsKey("m_36_558")) {
	    		uPI_ID += "," + "558";
	    		uPI_P += "," +  oddsPairs.get("m_36_558");
	    		uPI_M += "," +  betPairs.get("m_36_558");
	    		params.add(new BasicNameValuePair("jeuM_36_558", betPairs.get("m_36_558")));
	    	}else {
	    		params.add(new BasicNameValuePair("jeuM_36_558", ""));
	    	}
	    	
	    	if(betPairs.containsKey("m_36_559")) {
	    		uPI_ID += "," + "559";
	    		uPI_P += "," +  oddsPairs.get("m_36_559");
	    		uPI_M += "," +  betPairs.get("m_36_559");
	    		params.add(new BasicNameValuePair("jeuM_36_559", betPairs.get("m_36_559")));
	    	}else {
	    		params.add(new BasicNameValuePair("jeuM_36_559", ""));
	    	}
	    	
	    	if(betPairs.containsKey("m_38_560")) {
	    		uPI_ID += "," + "560";
	    		uPI_P += "," +  oddsPairs.get("m_38_560");
	    		uPI_M += "," +  betPairs.get("m_38_560");
	    		params.add(new BasicNameValuePair("jeuM_38_560", betPairs.get("m_38_560")));
	    	}else {
	    		params.add(new BasicNameValuePair("jeuM_38_560", ""));
	    	}
	    
	    	if(betPairs.containsKey("m_38_561")) {
	    		uPI_ID += "," + "561";
	    		uPI_P += "," +  oddsPairs.get("m_38_561");
	    		uPI_M += "," +  betPairs.get("m_38_561");
	    		params.add(new BasicNameValuePair("jeuM_38_561", betPairs.get("m_38_561")));
	    	}else {
	    		params.add(new BasicNameValuePair("jeuM_38_561", ""));
	    	}
	    	
	    	if(betPairs.containsKey("m_39_562")) {
	    		uPI_ID += "," + "562";
	    		uPI_P += "," +  oddsPairs.get("m_39_562");
	    		uPI_M += "," +  betPairs.get("m_39_562");
	    		params.add(new BasicNameValuePair("jeuM_39_562", betPairs.get("m_39_562")));
	    	}else {
	    		params.add(new BasicNameValuePair("jeuM_39_562", ""));
	    	}
	    	
	    	if(betPairs.containsKey("m_39_563")) {
	    		uPI_ID += "," + "563";
	    		uPI_P += "," +  oddsPairs.get("m_39_563");
	    		uPI_M += "," +  betPairs.get("m_39_563");
	    		params.add(new BasicNameValuePair("jeuM_39_563", betPairs.get("m_39_563")));
	    	}else {
	    		params.add(new BasicNameValuePair("jeuM_39_563", ""));
	    	}
	    	
	    	if(betPairs.containsKey("m_40_564")) {
	    		uPI_ID += "," + "564";
	    		uPI_P += "," +  oddsPairs.get("m_40_564");
	    		uPI_M += "," +  betPairs.get("m_40_564");
	    		params.add(new BasicNameValuePair("jeuM_40_564", betPairs.get("m_40_564")));
	    	}else {
	    		params.add(new BasicNameValuePair("jeuM_40_564", ""));
	    	}
	    	
	    	if(betPairs.containsKey("m_40_565")) {
	    		uPI_ID += "," + "565";
	    		uPI_P += "," +  oddsPairs.get("m_40_565");
	    		uPI_M += "," +  betPairs.get("m_40_565");
	    		params.add(new BasicNameValuePair("jeuM_40_565", betPairs.get("m_40_565")));
	    	}else {
	    		params.add(new BasicNameValuePair("jeuM_40_565", ""));
	    	}
	    	
	    	if(betPairs.containsKey("m_42_566")) {
	    		uPI_ID += "," + "566";
	    		uPI_P += "," +  oddsPairs.get("m_42_566");
	    		uPI_M += "," +  betPairs.get("m_42_566");
	    		params.add(new BasicNameValuePair("jeuM_42_566", betPairs.get("m_42_566")));
	    	}else {
	    		params.add(new BasicNameValuePair("jeuM_42_566", ""));
	    	}
	    	
	    	if(betPairs.containsKey("m_42_567")) {
	    		uPI_ID += "," + "567";
	    		uPI_P += "," +  oddsPairs.get("m_42_567");
	    		uPI_M += "," +  betPairs.get("m_42_567");
	    		params.add(new BasicNameValuePair("jeuM_42_567", betPairs.get("m_42_567")));
	    	}else {
	    		params.add(new BasicNameValuePair("jeuM_42_567", ""));
	    	}
	    	
	    	if(betPairs.containsKey("m_43_568")) {
	    		uPI_ID += "," + "568";
	    		uPI_P += "," +  oddsPairs.get("m_43_568");
	    		uPI_M += "," +  betPairs.get("m_43_568");
	    		params.add(new BasicNameValuePair("jeuM_43_568", betPairs.get("m_43_568")));
	    	}else {
	    		params.add(new BasicNameValuePair("jeuM_43_568", ""));
	    	}
	    	
	    	if(betPairs.containsKey("m_43_569")) {
	    		uPI_ID += "," + "569";
	    		uPI_P += "," +  oddsPairs.get("m_43_569");
	    		uPI_M += "," +  betPairs.get("m_43_569");
	    		params.add(new BasicNameValuePair("jeuM_43_569", betPairs.get("m_43_569")));
	    	}else {
	    		params.add(new BasicNameValuePair("jeuM_43_569", ""));
	    	}
	    	
	    	if(betPairs.containsKey("m_44_570")) {
	    		uPI_ID += "," + "570";
	    		uPI_P += "," +  oddsPairs.get("m_44_570");
	    		uPI_M += "," +  betPairs.get("m_44_570");
	    		params.add(new BasicNameValuePair("jeuM_44_570", betPairs.get("m_44_570")));
	    	}else {
	    		params.add(new BasicNameValuePair("jeuM_44_570", ""));
	    	}
	    	
	    	if(betPairs.containsKey("m_44_571")) {
	    		uPI_ID += "," + "571";
	    		uPI_P += "," +  oddsPairs.get("m_44_571");
	    		uPI_M += "," +  betPairs.get("m_44_571");
	    		params.add(new BasicNameValuePair("jeuM_44_571", betPairs.get("m_44_571")));
	    	}else {
	    		params.add(new BasicNameValuePair("jeuM_44_571", ""));
	    	}
	    	
	    	if(betPairs.containsKey("m_46_572")) {
	    		uPI_ID += "," + "572";
	    		uPI_P += "," +  oddsPairs.get("m_46_572");
	    		uPI_M += "," +  betPairs.get("m_46_572");
	    		params.add(new BasicNameValuePair("jeuM_46_572", betPairs.get("m_46_572")));
	    	}else {
	    		params.add(new BasicNameValuePair("jeuM_46_572", ""));
	    	}
	    	
	    	if(betPairs.containsKey("m_46_573")) {
	    		uPI_ID += "," + "573";
	    		uPI_P += "," +  oddsPairs.get("m_46_573");
	    		uPI_M += "," +  betPairs.get("m_46_573");
	    		params.add(new BasicNameValuePair("jeuM_46_573", betPairs.get("m_46_573")));
	    	}else {
	    		params.add(new BasicNameValuePair("jeuM_46_573", ""));
	    	}
	    	
	    	if(betPairs.containsKey("m_47_574")) {
	    		uPI_ID += "," + "574";
	    		uPI_P += "," +  oddsPairs.get("m_47_574");
	    		uPI_M += "," +  betPairs.get("m_47_574");
	    		params.add(new BasicNameValuePair("jeuM_47_574", betPairs.get("m_47_574")));
	    	}else {
	    		params.add(new BasicNameValuePair("jeuM_47_574", ""));
	    	}
	    	
	    	if(betPairs.containsKey("m_47_575")) {
	    		uPI_ID += "," + "575";
	    		uPI_P += "," +  oddsPairs.get("m_47_575");
	    		uPI_M += "," +  betPairs.get("m_47_575");
	    		params.add(new BasicNameValuePair("jeuM_47_575", betPairs.get("m_47_575")));
	    	}else {
	    		params.add(new BasicNameValuePair("jeuM_47_575", ""));
	    	}
	    	
	    	if(betPairs.containsKey("m_48_576")) {
	    		uPI_ID += "," + "576";
	    		uPI_P += "," +  oddsPairs.get("m_48_576");
	    		uPI_M += "," +  betPairs.get("m_48_576");
	    		params.add(new BasicNameValuePair("jeuM_48_576", betPairs.get("m_48_576")));
	    	}else {
	    		params.add(new BasicNameValuePair("jeuM_48_576", ""));
	    	}
	    	
	    	if(betPairs.containsKey("m_48_577")) {
	    		uPI_ID += "," + "577";
	    		uPI_P += "," +  oddsPairs.get("m_48_577");
	    		uPI_M += "," +  betPairs.get("m_48_577");
	    		params.add(new BasicNameValuePair("jeuM_48_577", betPairs.get("m_48_577")));
	    	}else {
	    		params.add(new BasicNameValuePair("jeuM_48_577", ""));
	    	}
	    	
	    	if(betPairs.containsKey("m_50_578")) {
	    		uPI_ID += "," + "578";
	    		uPI_P += "," +  oddsPairs.get("m_50_578");
	    		uPI_M += "," +  betPairs.get("m_50_578");
	    		params.add(new BasicNameValuePair("jeuM_50_578", betPairs.get("m_50_578")));
	    	}else {
	    		params.add(new BasicNameValuePair("jeuM_50_578", ""));
	    	}
	    	
	    	if(betPairs.containsKey("m_50_579")) {
	    		uPI_ID += "," + "579";
	    		uPI_P += "," +  oddsPairs.get("m_50_579");
	    		uPI_M += "," +  betPairs.get("m_50_579");
	    		params.add(new BasicNameValuePair("jeuM_50_579", betPairs.get("m_50_579")));
	    	}else {
	    		params.add(new BasicNameValuePair("jeuM_50_579", ""));
	    	}
	    	
	    	if(betPairs.containsKey("m_51_580")) {
	    		uPI_ID += "," + "580";
	    		uPI_P += "," +  oddsPairs.get("m_51_580");
	    		uPI_M += "," +  betPairs.get("m_51_580");
	    		params.add(new BasicNameValuePair("jeuM_51_580", betPairs.get("m_51_580")));
	    	}else {
	    		params.add(new BasicNameValuePair("jeuM_51_580", ""));
	    	}
	    	
	    	if(betPairs.containsKey("m_51_581")) {
	    		uPI_ID += "," + "581";
	    		uPI_P += "," +  oddsPairs.get("m_51_581");
	    		uPI_M += "," +  betPairs.get("m_51_581");
	    		params.add(new BasicNameValuePair("jeuM_51_581", betPairs.get("m_51_581")));
	    	}else {
	    		params.add(new BasicNameValuePair("jeuM_51_581", ""));
	    	}
	    	
	    	if(betPairs.containsKey("m_53_582")) {
	    		uPI_ID += "," + "582";
	    		uPI_P += "," +  oddsPairs.get("m_53_582");
	    		uPI_M += "," +  betPairs.get("m_53_582");
	    		params.add(new BasicNameValuePair("jeuM_53_582", betPairs.get("m_53_582")));
	    	}else {
	    		params.add(new BasicNameValuePair("jeuM_53_582", ""));
	    	}
	    	
	    	if(betPairs.containsKey("m_53_583")) {
	    		uPI_ID += "," + "583";
	    		uPI_P += "," +  oddsPairs.get("m_53_583");
	    		uPI_M += "," +  betPairs.get("m_53_583");
	    		params.add(new BasicNameValuePair("jeuM_53_583", betPairs.get("m_53_583")));
	    	}else {
	    		params.add(new BasicNameValuePair("jeuM_53_583", ""));
	    	}
	    	
	    	if(betPairs.containsKey("m_54_584")) {
	    		uPI_ID += "," + "584";
	    		uPI_P += "," +  oddsPairs.get("m_54_584");
	    		uPI_M += "," +  betPairs.get("m_54_584");
	    		params.add(new BasicNameValuePair("jeuM_54_584", betPairs.get("m_54_584")));
	    	}else {
	    		params.add(new BasicNameValuePair("jeuM_54_584", ""));
	    	}
	    	
	    	if(betPairs.containsKey("m_54_585")) {
	    		uPI_ID += "," + "585";
	    		uPI_P += "," +  oddsPairs.get("m_54_585");
	    		uPI_M += "," +  betPairs.get("m_54_585");
	    		params.add(new BasicNameValuePair("jeuM_54_585", betPairs.get("m_54_585")));
	    	}else {
	    		params.add(new BasicNameValuePair("jeuM_54_585", ""));
	    	}
	    	
	    	if(betPairs.containsKey("m_56_586")) {
	    		uPI_ID += "," + "586";
	    		uPI_P += "," +  oddsPairs.get("m_56_586");
	    		uPI_M += "," +  betPairs.get("m_56_586");
	    		params.add(new BasicNameValuePair("jeuM_56_586", betPairs.get("m_56_586")));
	    	}else {
	    		params.add(new BasicNameValuePair("jeuM_56_586", ""));
	    	}
	    	
	    	if(betPairs.containsKey("m_56_587")) {
	    		uPI_ID += "," + "587";
	    		uPI_P += "," +  oddsPairs.get("m_56_587");
	    		uPI_M += "," +  betPairs.get("m_56_587");
	    		params.add(new BasicNameValuePair("jeuM_56_587", betPairs.get("m_56_587")));
	    	}else {
	    		params.add(new BasicNameValuePair("jeuM_56_587", ""));
	    	}
	    	
	    	if(betPairs.containsKey("m_57_588")) {
	    		uPI_ID += "," + "588";
	    		uPI_P += "," +  oddsPairs.get("m_57_588");
	    		uPI_M += "," +  betPairs.get("m_57_588");
	    		params.add(new BasicNameValuePair("jeuM_57_588", betPairs.get("m_57_588")));
	    	}else {
	    		params.add(new BasicNameValuePair("jeuM_57_588", ""));
	    	}
	    	
	    	if(betPairs.containsKey("m_57_589")) {
	    		uPI_ID += "," + "589";
	    		uPI_P += "," +  oddsPairs.get("m_57_589");
	    		uPI_M += "," +  betPairs.get("m_57_589");
	    		params.add(new BasicNameValuePair("jeuM_57_589", betPairs.get("m_57_589")));
	    	}else {
	    		params.add(new BasicNameValuePair("jeuM_57_589", ""));
	    	}
	    	
	    	if(betPairs.containsKey("m_59_590")) {
	    		uPI_ID += "," + "590";
	    		uPI_P += "," +  oddsPairs.get("m_59_590");
	    		uPI_M += "," +  betPairs.get("m_59_590");
	    		params.add(new BasicNameValuePair("jeuM_59_590", betPairs.get("m_59_590")));
	    	}else {
	    		params.add(new BasicNameValuePair("jeuM_59_590", ""));
	    	}
	    	
	    	if(betPairs.containsKey("m_59_591")) {
	    		uPI_ID += "," + "591";
	    		uPI_P += "," +  oddsPairs.get("m_59_591");
	    		uPI_M += "," +  betPairs.get("m_59_591");
	    		params.add(new BasicNameValuePair("jeuM_59_591", betPairs.get("m_59_591")));
	    	}else {
	    		params.add(new BasicNameValuePair("jeuM_59_591", ""));
	    	}
	    	
	    	if(betPairs.containsKey("m_60_592")) {
	    		uPI_ID += "," + "592";
	    		uPI_P += "," +  oddsPairs.get("m_60_592");
	    		uPI_M += "," +  betPairs.get("m_60_592");
	    		params.add(new BasicNameValuePair("jeuM_60_592", betPairs.get("m_60_592")));
	    	}else {
	    		params.add(new BasicNameValuePair("jeuM_60_592", ""));
	    	}
	    	
	    	if(betPairs.containsKey("m_60_593")) {
	    		uPI_ID += "," + "593";
	    		uPI_P += "," +  oddsPairs.get("m_60_593");
	    		uPI_M += "," +  betPairs.get("m_60_593");
	    		params.add(new BasicNameValuePair("jeuM_60_593", betPairs.get("m_60_593")));
	    	}else {
	    		params.add(new BasicNameValuePair("jeuM_60_593", ""));
	    	}
	    	
	    	if(betPairs.containsKey("m_62_594")) {
	    		uPI_ID += "," + "594";
	    		uPI_P += "," +  oddsPairs.get("m_62_594");
	    		uPI_M += "," +  betPairs.get("m_62_594");
	    		params.add(new BasicNameValuePair("jeuM_62_594", betPairs.get("m_62_594")));
	    	}else {
	    		params.add(new BasicNameValuePair("jeuM_62_594", ""));
	    	}
	    	
	    	if(betPairs.containsKey("m_62_595")) {
	    		uPI_ID += "," + "595";
	    		uPI_P += "," +  oddsPairs.get("m_62_595");
	    		uPI_M += "," +  betPairs.get("m_62_595");
	    		params.add(new BasicNameValuePair("jeuM_62_595", betPairs.get("m_62_595")));
	    	}else {
	    		params.add(new BasicNameValuePair("jeuM_62_595", ""));
	    	}
	    	
	    	if(betPairs.containsKey("m_63_596")) {
	    		uPI_ID += "," + "596";
	    		uPI_P += "," +  oddsPairs.get("m_63_596");
	    		uPI_M += "," +  betPairs.get("m_63_596");
	    		params.add(new BasicNameValuePair("jeuM_63_596", betPairs.get("m_63_596")));
	    	}else {
	    		params.add(new BasicNameValuePair("jeuM_63_596", ""));
	    	}
	    	
	    	if(betPairs.containsKey("m_63_597")) {
	    		uPI_ID += "," + "597";
	    		uPI_P += "," +  oddsPairs.get("m_63_597");
	    		uPI_M += "," +  betPairs.get("m_63_597");
	    		params.add(new BasicNameValuePair("jeuM_63_597", betPairs.get("m_63_597")));
	    	}else {
	    		params.add(new BasicNameValuePair("jeuM_63_597", ""));
	    	}
	    	
	    	if(!uPI_ID.equals("")) {
	    		uPI_ID = uPI_ID.substring(1);
	    	}
	    	
	    	if(!uPI_P.equals("")) {
	    		uPI_P = uPI_P.substring(1);
	    	}
	    	
	    	if(!uPI_M.equals("")) {
	    		uPI_M = uPI_M.substring(1);
	    	}
	    	

	    	params.add(new BasicNameValuePair("jeuM_64_598", ""));
	    	params.add(new BasicNameValuePair("jeuM_64_599", ""));
	    	params.add(new BasicNameValuePair("jeuM_65_600", ""));
	    	params.add(new BasicNameValuePair("jeuM_65_601", ""));
	    	
	    	params.add(new BasicNameValuePair("JeuValidate", YaboHttp.jeuValidate));
	    	params.add(new BasicNameValuePair("uPI_ID", uPI_ID));
	    	params.add(new BasicNameValuePair("uPI_P", uPI_P));
	    	params.add(new BasicNameValuePair("uPI_M", uPI_M));

	        System.out.println(uPI_ID);
	        System.out.println(uPI_P);
	        System.out.println(uPI_M);
	        System.out.println(YaboHttp.jeuValidate);
	        
	        return params;
	        
    	
    	}catch(Exception e){
    		e.printStackTrace();
    		autoBet.outputGUIMessage("构造下单数据错误！\n");
    		return null;
    	}
   	
    }
    
    public static String addParam(String name, List<NameValuePair> params) {
    	return "";
    }
    
    public static boolean isBetSuccess(String res){
    	if(res == null) {
    		return false;
    	}
    	if(res.contains("下注成功")) {
    		return true;
    	}
    	
    	return false;
    }
    
    public static boolean isBetFailed(String res){
    	
    	try{
    		JSONObject betRes = new JSONObject(res);
    		
    		int errorCode = betRes.getInt("success");
    		
    		if(errorCode != 200){
    			JSONObject data = betRes.getJSONObject("data");
    			YaboHttp.jeuValidate = data.getString("JeuValidate");
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
				res = YaboHttp.doGet(YaboHttp.ADDRESS + "/hisorder/queryHistory.do?cdate=" + dateString + "&rowNumPerPage=15", "", "");
				res1 = YaboHttp.doGet(YaboHttp.ADDRESS + "/hisorder/queryHistory.do?pageNo=2&cdate=" + dateString, "", "");
				if(res!= null && res1 != null && res.contains(number + "期")) {
					pos = res.indexOf(number + "期", pos);	
					if(pos > 0) {
						hasResult = true;
					}
					
					while(pos > 0) {
						pos = res.indexOf("td class='f_right'", pos + 1);
						pos = res.indexOf("td class='f_right'", pos + 1);
						pos = res.indexOf("td class='f_right'", pos + 1);
						profit += Double.valueOf(res.substring(pos + 19, res.indexOf("</td>", pos)));
						pos = res.indexOf(number + "期", pos);
					}
					
					if(res1 != null) {
						pos = res1.indexOf(number + "期", 0);	
						while(pos > 0) {
							pos = res1.indexOf("td class='f_right'", pos + 1);
							pos = res1.indexOf("td class='f_right'", pos + 1);
							pos = res1.indexOf("td class='f_right'", pos + 1);
							profit += Double.valueOf(res1.substring(pos + 19, res1.indexOf("</td>", pos)));
							pos = res1.indexOf(number + "期", pos);
						}
					}
					
					if(hasResult) {
						return String.valueOf((int)profit);
					}
				}	
			} else if(dNum == 2) {
				int pos = 0;
				res = YaboHttp.doGet(YaboHttp.ADDRESS + "/hisorder/queryHistory.do?cdate=" + dateString + "&rowNumPerPage=15", "", "");
				res1 = YaboHttp.doGet(YaboHttp.ADDRESS + "/hisorder/queryHistory.do?pageNo=2&cdate=" + dateString, "", "");
				String res2 = YaboHttp.doGet(YaboHttp.ADDRESS + "/hisorder/queryHistory.do?pageNo=3&cdate=" + dateString, "", "");
				
				if(res!= null && res1 != null && res2 != null) {
					pos = res.indexOf(number + "期", 0);
					if(pos > 0) {
						hasResult = true;
					}
					
					while(pos > 0) {
						pos = res.indexOf("td class='f_right'", pos + 1);
						pos = res.indexOf("td class='f_right'", pos + 1);
						pos = res.indexOf("td class='f_right'", pos + 1);
						profit += Double.valueOf(res.substring(pos + 19, res.indexOf("</td>", pos)));
						pos = res.indexOf(number + "期", pos);
					}
					
					if(res1 != null) {
						pos = res1.indexOf(number + "期", 0);	
						if(pos > 0) {
							hasResult = true;
						}
						
						while(pos > 0) {
							pos = res1.indexOf("td class='f_right'", pos + 1);
							pos = res1.indexOf("td class='f_right'", pos + 1);
							pos = res1.indexOf("td class='f_right'", pos + 1);
							profit += Double.valueOf(res1.substring(pos + 19, res1.indexOf("</td>", pos)));
							pos = res1.indexOf(number + "期", pos);
						}
					}
					
					if(res2 != null) {
						pos = res2.indexOf(number + "期", 0);	
						if(pos > 0) {
							hasResult = true;
						}
						
						while(pos > 0) {
							pos = res2.indexOf("td class='f_right'", pos + 1);
							pos = res2.indexOf("td class='f_right'", pos + 1);
							pos = res2.indexOf("td class='f_right'", pos + 1);
							profit += Double.valueOf(res2.substring(pos + 19, res2.indexOf("</td>", pos)));
							pos = res2.indexOf(number + "期", pos);
						}
					}
					
					if(hasResult) {
						return String.valueOf((int)profit);
					}
				}	
			} else if(dNum == 3) {
				int pos = 0;
				res = YaboHttp.doGet(YaboHttp.ADDRESS + "/hisorder/queryHistory.do?pageNo=2&cdate=" + dateString, "", "");
				res1 = YaboHttp.doGet(YaboHttp.ADDRESS + "/hisorder/queryHistory.do?pageNo=3&cdate=" + dateString, "", "");
				String res2 = YaboHttp.doGet(YaboHttp.ADDRESS + "/hisorder/queryHistory.do?pageNo=4&cdate=" + dateString, "", "");
				if(res != null && res2 != null && res1 != null) {
					pos = res.indexOf(number + "期", 0);	
					if(pos > 0) {
						hasResult = true;
					}
					
					while(pos > 0) {
						pos = res.indexOf("td class='f_right'", pos + 1);
						pos = res.indexOf("td class='f_right'", pos + 1);
						pos = res.indexOf("td class='f_right'", pos + 1);
						profit += Double.valueOf(res.substring(pos + 19, res.indexOf("</td>", pos)));
						pos = res.indexOf(number + "期", pos);
					}
					
					if(res1 != null) {
						pos = res1.indexOf(number + "期", 0);	
						if(pos > 0) {
							hasResult = true;
						}
						
						while(pos > 0) {
							pos = res1.indexOf("td class='f_right'", pos + 1);
							pos = res1.indexOf("td class='f_right'", pos + 1);
							pos = res1.indexOf("td class='f_right'", pos + 1);
							profit += Double.valueOf(res1.substring(pos + 19, res1.indexOf("</td>", pos)));
							pos = res1.indexOf(number + "期", pos);
						}
					}
					
					if(res2 != null) {
						pos = res2.indexOf(number + "期", 0);	
						if(pos > 0) {
							hasResult = true;
						}
						
						while(pos > 0) {
							pos = res2.indexOf("td class='f_right'", pos + 1);
							pos = res2.indexOf("td class='f_right'", pos + 1);
							pos = res2.indexOf("td class='f_right'", pos + 1);
							profit += Double.valueOf(res2.substring(pos + 19, res2.indexOf("</td>", pos)));
							pos = res2.indexOf(number + "期", pos);
						}
					}
					
					if(hasResult) {
						return String.valueOf((int)profit);
					}
				}	
			} else if(dNum == 0 && isInLastTime()) {
				int pos = 0;
				res = YaboHttp.doGet(YaboHttp.ADDRESS + "/hisorder/queryHistory.do?cdate=" + dateString + "&rowNumPerPage=15", "", "");
				res1 = YaboHttp.doGet(YaboHttp.ADDRESS + "/hisorder/queryHistory.do?pageNo=2&cdate=" + dateString, "", "");
				if(res!= null && res1 != null && res.contains(number + "期")) {
					pos = res.indexOf(number + "期", 0);		
					if(pos > 0) {
						hasResult = true;
					}
					
					while(pos > 0) {
						pos = res.indexOf("td class='f_right'", pos + 1);
						pos = res.indexOf("td class='f_right'", pos + 1);
						pos = res.indexOf("td class='f_right'", pos + 1);
						profit += Double.valueOf(res.substring(pos + 19, res.indexOf("</td>", pos)));
						pos = res.indexOf(number + "期", pos);
					}
					
					if(res1 != null) {
						pos = res1.indexOf(number + "期", 0);	
						while(pos > 0) {
							pos = res1.indexOf("td class='f_right'", pos + 1);
							pos = res1.indexOf("td class='f_right'", pos + 1);
							pos = res1.indexOf("td class='f_right'", pos + 1);
							profit += Double.valueOf(res1.substring(pos + 19, res1.indexOf("</td>", pos)));
							pos = res1.indexOf(number + "期", pos);
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
