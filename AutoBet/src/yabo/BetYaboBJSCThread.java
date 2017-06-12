package yabo;

import java.util.Vector;

import dsn.Client;
import dsn.TYPEINDEX;
import dsn.autoBet;

public class BetYaboBJSCThread extends Thread{
    
	
	long almostTime = 25;  //进行最后一次sleep计算的时间
	
	long sleepTime = 10;	//平时睡眠时间
	boolean requestTime = true;
	
    static double betBJSCPercent = 1.0;
    
    static long betRemainTime = 15;
    
    static boolean betBJSC = false;
    static boolean betOppositeBJSC = false;
    
    static boolean isNeedLogin = false;

    
    
    static boolean clearBJSCdetaisData = false;
    
    Client client;
    
    public BetYaboBJSCThread(Client client){
    	this.client = client;
    }
    
    boolean printonce = false;
    
    
    @Override
    public void run() {
    	boolean autoBetSuccess = false;	

			while(true){
				try{
					if(YaboHttp.isInFreetime() == true && clearBJSCdetaisData == false){
						//BetBJSCManager.clearBJSCdetalsData();
						clearBJSCdetaisData = true;
					}			
					
					if(YaboHttp.isInRelogin == true) {
						System.out.println("reLogin !!");
						continue;	
					}
		
					long BJSCremainTime = 0;
					
					if(BetBJSCManager.isInBJSCBetTime()) {
					
						if(requestTime) {	
							if(!BetBJSCManager.grabGameInfo() && !BetBJSCManager.grabGameInfo()) {
								System.out.println("reLogin !!");
								if(!YaboHttp.loginToYabo()) {
									YaboHttp.reLogin();
								}
							}
							
							BJSCremainTime = BetBJSCManager.getRemainTime();
							System.out.println("[亚博]第" + BetBJSCManager.getDrawnumber() + "期距离北京赛车封盘时间为:");
							System.out.println(BJSCremainTime);
	
							if((BJSCremainTime > 0 && BJSCremainTime <= 40)) {//如果将近封盘不发请求，获取本地时间
								requestTime = false;
							}
							
						} else {
							BJSCremainTime = BetBJSCManager.getRemainTime();
							System.out.println("[亚博]第" + BetBJSCManager.getDrawnumber() + "期距离北京赛车封盘时间为local:");
							System.out.println(BJSCremainTime);
							
							if((BJSCremainTime < 0 || BJSCremainTime >= 40)) {//如果将近封盘不发请求，获取本地时间
								requestTime = true;}
							
						}
						
						if(BJSCremainTime < 0) {
							autoBetSuccess = false;
						}
			
						boolean timeTobetBJSC = BJSCremainTime <= 15 && BJSCremainTime > 0;
			
						if((betBJSC || betOppositeBJSC)&&timeTobetBJSC){//最后十五秒秒去下注
							
							clearBJSCdetaisData = false;
							
							
							String[] betBJSCData = null;
							
							for(int i = 0; i < 4; i++) {
								if((betBJSCData = client.getBJSCdata()) == null){
									Thread.currentThread().sleep(500);
								}
								else {
									break;
								}
							}
							
							if(betBJSCData == null) {
								System.out.println("[亚博]下单失败,未获取到下单数据");
							} else if(betBJSCData != null &&betBJSCData[0].equals(BetBJSCManager.getDrawnumber()) && autoBetSuccess == false) {
							//} else if(betBJSCData != null &&betBJSCData[0].equals(BetBJSCManager.getDrawnumber())) {
								
								String[] betsData = {betBJSCData[1], betBJSCData[2], betBJSCData[3]};
								
								System.out.println("[亚博]北京赛车下单数据：");
								System.out.println(betBJSCData[0]);
								System.out.println(betBJSCData[1]);							
								
								double times = Double.parseDouble(betBJSCData[5]);
								
								//if(client.betBJSCopen()) {
									//autoBetSuccess = BetBJSCManager.doBetBJSC(betsData, betBJSCPercent*times, !Boolean.parseBoolean(betBJSCData[6]), betBJSCData[4]);
								//} else {
									//autoBet.outputGUIMessage("亚博北京赛车" + BetBJSCManager.getDrawnumber() + "期  暂停投注！ \n");
								//}
								autoBetSuccess = BetBJSCManager.doBetBJSC(betsData, betBJSCPercent*times, !Boolean.parseBoolean(betBJSCData[6]), betBJSCData[4]);
								if(!autoBetSuccess && !YaboHttp.loginToYabo()) {
									YaboHttp.reLogin();
								}
								//todo remove
								autoBetSuccess = true;					
								
							}else if(!betBJSCData[0].equals(BetBJSCManager.getDrawnumber())) {
								System.out.println("亚博北京赛车下单数据错误\n");			
								System.out.printf("服务器期数：%s， 本地期数:%s\n", betBJSCData[0], BetBJSCManager.getDrawnumber());				
							}
						}
					}

					sleepTime = 10;
				
					if(YaboHttp.isAllLotteryIdle()){
						if(BetBJSCManager.getUnCalcProfitBJSCDraw().size() != 0) {						
							Vector<String> calcedBJSCDraw = new Vector<String>();					
							Vector<String> data = BetBJSCManager.getUnCalcProfitBJSCDraw();
							
							for(int i = 0; i < data.size(); i++) {						
								String drawNumber = data.elementAt(i);						
								String profit = BetBJSCManager.getProfit(drawNumber);
								
								if(profit.equals("---") != true){
									BetBJSCManager.updateBJSCWindowdetailsData(drawNumber, TYPEINDEX.STATC.ordinal(), "0");
									BetBJSCManager.updateBJSCWindowdetailsData(drawNumber, TYPEINDEX.PROFIT.ordinal(), profit);								
									BetBJSCManager.updateBJSCWindowdetailsData(drawNumber, TYPEINDEX.COUNT.ordinal(), "0");
									
									calcedBJSCDraw.add(drawNumber);
								}		
							}
							
							if(calcedBJSCDraw.size() != 0){
								BetBJSCManager.updateUnCalcBJSCDraw(calcedBJSCDraw);
							}
							
							
						}
								
					}
	
					if(BJSCremainTime <= almostTime && BJSCremainTime > 0){					
						long time2 = BJSCremainTime - 15;
						long littleTime = time2;
						
						if(time2 < 0)
							littleTime = 1;
						if(littleTime > 0 && littleTime <= (almostTime - 15))
							sleepTime = littleTime;
					}
					
					Thread.currentThread().sleep(sleepTime * 1000);	
				}catch (InterruptedException e) {
					e.printStackTrace();
			    }
			}//while
	   	
	}//run
    

}

