package dsn;
import java.util.Vector;
class BetXJSSCThread extends Thread{
    
    
	//long betRemainTime = 15*1000;  //离多少秒封盘时进行下注
	
	long almostTime = 25*1000;  //进行最后一次sleep计算的时间
	
	long sleepTime = 10*1000;	//平时水淼时间
	boolean requestTime = true;
	
    static double betXJSSCPercent = 1.0;
    
    static long betRemainTime = 10 * 1000;
    
    static boolean betXJSSC = false;
    static boolean betOppositeXJSSC = false;

    
    
    static boolean clearXJSSCdetaisData = false;
    
    Client client;
    
    public BetXJSSCThread(Client client){
    	this.client = client;
    }
    
    
    @Override
    public void run() {
    	try{
	    	boolean autoBetSuccess = false;
	    	
			boolean getXJSSCOddsData = false;
			
			
			//用来控制打印封盘数据数据与下单数据差值的变量
			boolean printXJSSCErrorValue = false;

	    	
			while(true){
				
				if(dsnHttp.isInFreetime(System.currentTimeMillis()) == true && clearXJSSCdetaisData == false){
					BetXJSSCManager.clearXJSSCdetalsData();
					clearXJSSCdetaisData = true;
				}
				
				long localTime = dsnHttp.getlocalTime();
				
				if(!BetXJSSCManager.isInXJSSCBetTime(localTime) || dsnHttp.isInRelogin == true)
					continue;
				
				long XJSSCremainTime = 0;
				
				if(requestTime) {
					XJSSCremainTime = BetXJSSCManager.getXJSSCremainTime();
					
					if(XJSSCremainTime > 10*60*1000){//获取时间失败
						

						XJSSCremainTime = BetXJSSCManager.getXJSSClocalRemainTime();
					}
					
					System.out.println("[迪斯尼会员]距离新疆时时彩封盘时间为:");
					System.out.println(XJSSCremainTime/1000);			
					
					if((XJSSCremainTime > 0 && XJSSCremainTime <= 40*1000)) {//如果将近封盘不发请求，获取本地时间
						requestTime = false;
					}
					
				} else {		
					XJSSCremainTime = BetXJSSCManager.getXJSSClocalRemainTime();
					
					if(autoBetSuccess == false){
						System.out.println("[迪斯尼会员]距离新疆时时彩封盘时间为[local]:");
						System.out.println(XJSSCremainTime/1000);		
					}
			
					
					if((XJSSCremainTime <= 0 || XJSSCremainTime > 40*1000) ) {
						requestTime = true;
					}
				}
				
				if(XJSSCremainTime < 0) {
					getXJSSCOddsData = false;
					autoBetSuccess = false;
				}
				
				
				
				boolean timeTobetXJSSC = XJSSCremainTime <= betRemainTime && XJSSCremainTime > 1;



				//每盘拿一次赔率数据
				if(!timeTobetXJSSC && (XJSSCremainTime <= 90*1000) && XJSSCremainTime > 0 && getXJSSCOddsData == false){
					String res = BetXJSSCManager.getXJSSCoddsData();
					if(res != null){
						getXJSSCOddsData = true;
					}
					
					
				}
				
				
				

				
				
				
				
				

				
				
				if((betXJSSC || betOppositeXJSSC)&&timeTobetXJSSC){//最后十五秒秒去下注
					
					clearXJSSCdetaisData = false;
					
					
					String[] betXJSSCData = null;
					
					for(int i = 0; i < 4; i++) {
						//todo
						if((betXJSSCData = client.getXJSSCdata()) == null){
							Thread.currentThread().sleep(1*1000);
						}
						else {
							break;
						}
					}
					
					if(betXJSSCData == null) {
						System.out.println("[迪斯尼会员]下单失败,未获取到下单数据");
					} else if(betXJSSCData != null &&betXJSSCData[0].equals(BetXJSSCManager.getXJSSCdrawNumber()) && autoBetSuccess == false) {
						
						String[] betsData = {betXJSSCData[1]};
						
						System.out.println("[迪斯尼会员]新疆时时彩下单数据：");
						System.out.println(betXJSSCData[1]);
						
						
						
						autoBetSuccess = BetXJSSCManager.doBetXJSSC(betsData, betXJSSCPercent, betOppositeXJSSC, betXJSSCData[2]);
						
						if(autoBetSuccess == true){
							//dsnHttp.setCQSSCBetData(betsData);
						}
						
						//todo remove
						autoBetSuccess = true;
						
						printXJSSCErrorValue = false;
						
					}else if(!betXJSSCData[0].equals(BetXJSSCManager.getXJSSCdrawNumber())){
						System.out.println("新疆时时彩下单数据错误\n");
						
						//System.out.printf("服务器期数：%d， 本地期数:%d\n", betXJSSCData[0], BetXJSSCManager.getXJSSCdrawNumber());
						
						
					}
					
					

				}
				



				sleepTime = 10*1000;
				
				
				
				if(dsnHttp.isAllLotteryIdle()){
					
					
					
					if(BetXJSSCManager.getUnCalcProfitBJSCDraw().size() != 0){
						
						Vector<String> calcedXJSSCDraw = new Vector<String>();
						
						Vector<String> data = BetXJSSCManager.getUnCalcProfitBJSCDraw();
						for(int i = 0; i < data.size(); i++){
							String drawNumber = data.elementAt(i);
							
							
							
							String[] profitandbishu = dsnHttp.getBetProfit(drawNumber, BetType.XJSSC);
							if(profitandbishu[0].equals("none") != true){
								BetXJSSCManager.updateXJSSCWindowdetailsData(drawNumber, TYPEINDEX.STATC.ordinal(), "0");
								BetXJSSCManager.updateXJSSCWindowdetailsData(drawNumber, TYPEINDEX.PROFIT.ordinal(), profitandbishu[0]);
								
								BetXJSSCManager.updateXJSSCWindowdetailsData(drawNumber, TYPEINDEX.COUNT.ordinal(), profitandbishu[1]);
								
								calcedXJSSCDraw.add(drawNumber);
							}
							
						}
						
						if(calcedXJSSCDraw.size() != 0){
							BetXJSSCManager.updateUnCalcXJSSCDraw(calcedXJSSCDraw);
						}
						
						
					}
					
					
					String balance = dsnHttp.getBalance();
					
					BetXJSSCManager.updateXJSSCBalance(balance);

					
				}
				


				
				

				
				
				
				
				if(XJSSCremainTime <= almostTime){					

					long time2 = XJSSCremainTime - betRemainTime;
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
    
    public static void setXJSSCBetPercent(double percent){
    	betXJSSCPercent = percent;
    }
    

}
