package Webet;

import dsn.TYPEINDEX;

import java.util.Vector;

import dsn.Client;
import dsn.autoBet;
import dsn.dsnHttp;
class BetWebetBJSCThread extends Thread{
    
    
	//long betRemainTime = 15*1000;  //离多少秒封盘时进行下注
	
	long almostTime = 25;  //进行最后一次sleep计算的时间
	
	long sleepTime = 10;	//平时水淼时间
	boolean requestTime = true;
	
    static double betBJSCPercent = 0.001;
    
    static long betRemainTime = 20;
    
    static boolean betBJSC = false;
    static boolean betOppositeBJSC = false;

    
    
    static boolean clearBJSCdetaisData = false;
    
    Client client;
    
    public BetWebetBJSCThread(Client client){
    	this.client = client;
    }
    
    boolean printonce = false;
    
    
    @Override
    public void run() {
    	try{
	    	boolean autoBetSuccess = false;
	    	
			boolean getBJSCOddsData = false;
			
			
			//用来控制打印封盘数据数据与下单数据差值的变量
			boolean printBJSCErrorValue = false;

	    	
			while(true){
				
				
				if(WebetHttp.isInFreetime() == true && clearBJSCdetaisData == false){
					BetBJSCManager.clearBJSCdetalsData();
					clearBJSCdetaisData = true;
				}
				
				
				if(WebetHttp.isInRelogin == true)
					continue;
				
				
				
				
				long BJSCremainTime = 0;
				
				if(requestTime) {
					
					//BetBJSCManager.grabGameInfo();
					
					boolean res = BetBJSCManager.grabGameInfo();
					
					if(res == false){
						BetBJSCManager.grabGameInfo();
					}
					
					if(res == false){
						Thread.currentThread().sleep(3000);
						continue;
					}
					
					
					BJSCremainTime = BetBJSCManager.getReaminTime();
					
					
					
					System.out.println("[惟博]距离北京赛车封盘时间为:");
					System.out.println(BJSCremainTime);
					
					printonce = false;
					
					if((BJSCremainTime > 0 && BJSCremainTime <= 40)) {//如果将近封盘不发请求，获取本地时间
						requestTime = false;
					}
					
				} else {
					BJSCremainTime = BetBJSCManager.getReaminTime();
					if(printonce == false)
					{
						System.out.println("[惟博]距离北京赛车封盘时间为local:");
						System.out.println(BJSCremainTime);
						printonce = true;
					}

					
					if((BJSCremainTime < 0 || BJSCremainTime >= 40)) {//如果将近封盘不发请求，获取本地时间
						requestTime = true;}
					
				}
				
				if(BJSCremainTime < 0) {
					getBJSCOddsData = false;
					autoBetSuccess = false;
				}
				
				
				
				boolean timeTobetBJSC = BJSCremainTime <= betRemainTime && BJSCremainTime > 0;




				
				
				if((betBJSC || betOppositeBJSC)&&timeTobetBJSC){//最后十五秒秒去下注
					
					clearBJSCdetaisData = false;
					
					
					String[] betBJSCData = null;
					
					for(int i = 0; i < 4; i++) {
						//todo
						if((betBJSCData = client.getBJSCdata()) == null){
							Thread.currentThread().sleep(1*1000);
						}
						else {
							break;
						}
					}
					
					if(betBJSCData == null) {
						System.out.println("[惟博]下单失败,未获取到下单数据");
					} else if(betBJSCData != null &&betBJSCData[0].equals(BetBJSCManager.getDrawnumber()) && autoBetSuccess == false) {
						
						String[] betsData = {betBJSCData[1], betBJSCData[2], betBJSCData[3]};
						
/*						System.out.println("[惟博]北京赛车下单数据：");
						System.out.println(betBJSCData[1]);
						
						
						
						autoBetSuccess = BetBJSCManager.doBetBJSC(betsData, betBJSCPercent, betOppositeBJSC, betBJSCData[4]);*/
						
						
						System.out.println("[惟博]北京赛车下单数据：");
						System.out.println(betBJSCData[0]);
						System.out.println(betBJSCData[1]);
						//System.out.println(betBJSCData[2]);
						//System.out.println(betBJSCData[3]);
						
						
						double times = Double.parseDouble(betBJSCData[5]);
						
						
						
						
						//策略
					//	autoBetSuccess = BetBJSCManager.doBetBJSC(betsData, betBJSCPercent*times, !Boolean.parseBoolean(betBJSCData[6]), betBJSCData[4]);
						
						//if(client.betBJSCopen()) {
						if(true) {
							autoBetSuccess = BetBJSCManager.doBetBJSC(betsData, betBJSCPercent*times, !Boolean.parseBoolean(betBJSCData[6]), betBJSCData[4]);
						} else {
							autoBet.outputGUIMessage("惟博北京赛车" + dsnHttp.getBJSCdrawNumber() + "期  暂停投注！ \n");
						}

						
						if(autoBetSuccess == true){
							//dsnHttp.setCQSSCBetData(betsData);
						}
						
						//todo remove
						autoBetSuccess = true;
						
						printBJSCErrorValue = false;
						
					}else if(!betBJSCData[0].equals(BetBJSCManager.getDrawnumber())){
						System.out.println("惟博北京赛车下单数据错误\n");
						
						System.out.printf("服务器期数：%s， 本地期数:%s\n", betBJSCData[0], BetBJSCManager.getDrawnumber());
						
						
					}
					
					

				}
				



				sleepTime = 10*1000;
				
				
				
				if(WebetHttp.isAllLotteryIdle()){
					
					

					if(BetBJSCManager.getUnCalcProfitBJSCDraw().size() != 0){
						
						Vector<String> calcedBJSCDraw = new Vector<String>();
						
						Vector<String> data = BetBJSCManager.getUnCalcProfitBJSCDraw();
						for(int i = 0; i < data.size(); i++){
							String drawNumber = data.elementAt(i);
							
							
							
							String profit = BetBJSCManager.getProfit(drawNumber);
							if(profit.equals("---") != true){
								BetBJSCManager.updateBJSCWindowdetailsData(drawNumber, TYPEINDEX.STATC.ordinal(), "0");
								BetBJSCManager.updateBJSCWindowdetailsData(drawNumber, TYPEINDEX.PROFIT.ordinal(), profit);
								
								BetBJSCManager.updateBJSCWindowdetailsData(drawNumber, TYPEINDEX.COUNT.ordinal(), profit);
								
								calcedBJSCDraw.add(drawNumber);
							}
							
						}
						
						if(calcedBJSCDraw.size() != 0){
							BetBJSCManager.updateUnCalcBJSCDraw(calcedBJSCDraw);
						}
						
						
					}
					
					
					String balance = BetBJSCManager.getBalance();
					
					BetBJSCManager.updateBJSCBalance(balance);

					
				}
				


				
				

				
				
				
				
				if(BJSCremainTime <= almostTime && BJSCremainTime >0){					

					long time2 = BJSCremainTime - betRemainTime;
					long littleTime = time2;
					
					if(time2 < 0)
						littleTime = 1;
					if(littleTime > 0 && littleTime <= (almostTime - betRemainTime))
						sleepTime = littleTime;
				}

				Thread.currentThread().sleep(sleepTime);

				
	
			}//while
		
    	}catch (InterruptedException e) {
            // TODO: handle exception
        }
	}//run
    
    public static void setBJSCBetPercent(double percent){
    	betBJSCPercent = percent;
    }
    

}
