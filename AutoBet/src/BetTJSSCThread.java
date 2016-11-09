import java.util.Vector;
class BetTJSSCThread extends Thread{
    
    
	//long betRemainTime = 15*1000;  //离多少秒封盘时进行下注
	
	long almostTime = 25*1000;  //进行最后一次sleep计算的时间
	
	long sleepTime = 10*1000;	//平时水淼时间
	boolean requestTime = true;
	
    static double betTJSSCPercent = 1.0;
    
    static long betRemainTime = 10 * 1000;
    
    static boolean betTJSSC = false;
    static boolean betOppositeTJSSC = false;

    
    
    static boolean clearTJSSCdetaisData = false;
    
    Client client;
    
    public BetTJSSCThread(Client client){
    	this.client = client;
    }
    
    
    @Override
    public void run() {
    	try{
	    	boolean autoBetSuccess = false;
	    	
			boolean getTJSSCOddsData = false;
			
			
			//用来控制打印封盘数据数据与下单数据差值的变量
			boolean printTJSSCErrorValue = false;

	    	
			while(true){
				long TJSSCremainTime = 0;
				
				if(requestTime) {
					TJSSCremainTime = BetTJSSCManager.getTJSSCremainTime();
					
					if(TJSSCremainTime > 10*60*1000){//获取时间失败
						

						TJSSCremainTime = BetTJSSCManager.getTJSSClocalRemainTime();
					}
					
					System.out.println("[迪斯尼会员]距离天津时时彩封盘时间为:");
					System.out.println(TJSSCremainTime/1000);			
					
					if((TJSSCremainTime > 0 && TJSSCremainTime <= 40*1000)) {//如果将近封盘不发请求，获取本地时间
						requestTime = false;
					}
					
				} else {		
					TJSSCremainTime = BetTJSSCManager.getTJSSClocalRemainTime();
					
					if(autoBetSuccess == false){
						System.out.println("[迪斯尼会员]距离天津时时彩封盘时间为[local]:");
						System.out.println(TJSSCremainTime/1000);		
					}
			
					
					if((TJSSCremainTime <= 0 || TJSSCremainTime > 40*1000) ) {
						requestTime = true;
					}
				}
				
				if(TJSSCremainTime < 0) {
					getTJSSCOddsData = false;
					autoBetSuccess = false;
				}
				
				
				
				boolean timeTobetTJSSC = TJSSCremainTime <= betRemainTime && TJSSCremainTime > 1;



				//每盘拿一次赔率数据
				if(!timeTobetTJSSC && (TJSSCremainTime <= 90*1000) && TJSSCremainTime > 0 && getTJSSCOddsData == false){
					String res = BetTJSSCManager.getTJSSCoddsData();
					if(res != null){
						getTJSSCOddsData = true;
					}
					
					
				}
				
				
				

				
				
				
				
				

				
				
				if((betTJSSC || betOppositeTJSSC)&&timeTobetTJSSC){//最后十五秒秒去下注
					
					clearTJSSCdetaisData = false;
					
					
					String[] betTJSSCData = null;
					
					for(int i = 0; i < 4; i++) {
						//todo
						if((betTJSSCData = client.getTJSSCdata()) == null){
							Thread.currentThread().sleep(1*1000);
						}
						else {
							break;
						}
					}
					
					if(betTJSSCData == null) {
						System.out.println("[迪斯尼会员]下单失败,未获取到下单数据");
					} else if(betTJSSCData != null &&betTJSSCData[0].equals(BetTJSSCManager.getTJSSCdrawNumber()) && autoBetSuccess == false) {
						
						String[] betsData = {betTJSSCData[1]};
						
						System.out.println("[迪斯尼会员]天津时时彩下单数据：");
						System.out.println(betTJSSCData[1]);
						
						
						
						autoBetSuccess = BetTJSSCManager.doBetTJSSC(betsData, betTJSSCPercent, betOppositeTJSSC, betTJSSCData[2]);
						
						if(autoBetSuccess == true){
							//dsnHttp.setCQSSCBetData(betsData);
						}
						
						printTJSSCErrorValue = false;
						
					}else if(!betTJSSCData[0].equals(BetTJSSCManager.getTJSSCdrawNumber())){
						System.out.println("天津时时彩下单数据错误\n");
						
						System.out.printf("服务器期数：%d， 本地期数:%d\n", betTJSSCData[0], BetTJSSCManager.getTJSSCdrawNumber());
						
						
					}
					
					

				}
				



				sleepTime = 10*1000;
				
				
				
				if(dsnHttp.isAllLotteryIdle()){
					
					
					
					if(BetTJSSCManager.getUnCalcProfitBJSCDraw().size() != 0){
						
						Vector<String> calcedTJSSCDraw = new Vector<String>();
						
						Vector<String> data = BetTJSSCManager.getUnCalcProfitBJSCDraw();
						for(int i = 0; i < data.size(); i++){
							String drawNumber = data.elementAt(i);
							
							
							
							String[] profitandbishu = dsnHttp.getBetProfit(drawNumber, BetType.TJSSC);
							if(profitandbishu[0].equals("none") != true){
								BetTJSSCManager.updateTJSSCWindowdetailsData(drawNumber, TYPEINDEX.STATC.ordinal(), "0");
								BetTJSSCManager.updateTJSSCWindowdetailsData(drawNumber, TYPEINDEX.PROFIT.ordinal(), profitandbishu[0]);
								
								BetTJSSCManager.updateTJSSCWindowdetailsData(drawNumber, TYPEINDEX.COUNT.ordinal(), profitandbishu[1]);
								
								calcedTJSSCDraw.add(drawNumber);
							}
							
						}
						
						if(calcedTJSSCDraw.size() != 0){
							BetTJSSCManager.updateUnCalcTJSSCDraw(calcedTJSSCDraw);
						}
						
						
					}
					
					
					String balance = dsnHttp.getBalance();
					
					BetTJSSCManager.updateTJSSCBalance(balance);

					
				}
				
				if(dsnHttp.isInFreetime(System.currentTimeMillis()) == true && clearTJSSCdetaisData == false){
					BetTJSSCManager.clearTJSSCdetalsData();
					clearTJSSCdetaisData = true;
				}

				
				

				
				
				
				
				if(TJSSCremainTime <= almostTime){					

					long time2 = TJSSCremainTime - betRemainTime;
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
    
    public static void setTJSSCBetPercent(double percent){
    	betTJSSCPercent = percent;
    }
    

}
