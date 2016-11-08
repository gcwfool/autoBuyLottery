import java.util.Vector;
class BetKL8Thread extends Thread{
    
    
	//long betRemainTime = 15*1000;  //离多少秒封盘时进行下注
	
	long almostTime = 25*1000;  //进行最后一次sleep计算的时间
	
	long sleepTime = 10*1000;	//平时水淼时间
	boolean requestTime = true;
	
    static double betKL8Percent = 1.0;
    
    static long betRemainTime = 10 * 1000;
    
    static boolean betKL8 = false;
    static boolean betOppositeKL8 = false;

    
    
    static boolean clearKL8detaisData = false;
    
    Client client;
    
    public BetKL8Thread(Client client){
    	this.client = client;
    }
    
    
    @Override
    public void run() {
    	try{
	    	boolean autoBetSuccess = false;
	    	
			boolean getKL8OddsData = false;
			
			
			//用来控制打印封盘数据数据与下单数据差值的变量
			boolean printKL8ErrorValue = false;

	    	
			while(true){
				long KL8remainTime = 0;
				
				if(requestTime) {
					KL8remainTime = BetKL8Manager.getKL8remainTime();
					
					if(KL8remainTime > 10*60*1000){//获取时间失败
						

						KL8remainTime = BetKL8Manager.getKL8localRemainTime();
					}
					
					System.out.println("[迪斯尼会员]距离快乐8封盘时间为:");
					System.out.println(KL8remainTime/1000);			
					
					if((KL8remainTime > 0 && KL8remainTime <= 40*1000)) {//如果将近封盘不发请求，获取本地时间
						requestTime = false;
					}
					
				} else {		
					KL8remainTime = BetKL8Manager.getKL8localRemainTime();
					
					if(autoBetSuccess == false){
						System.out.println("[迪斯尼会员]距离快乐8封盘时间为[local]:");
						System.out.println(KL8remainTime/1000);		
					}
			
					
					if((KL8remainTime <= 0 || KL8remainTime > 40*1000) ) {
						requestTime = true;
					}
				}
				
				if(KL8remainTime < 0) {
					getKL8OddsData = false;
					autoBetSuccess = false;
				}
				
				
				
				boolean timeTobetKL8 = KL8remainTime <= betRemainTime && KL8remainTime > 1;



				//每盘拿一次赔率数据
				if(!timeTobetKL8 && (KL8remainTime <= 90*1000) && KL8remainTime > 0 && getKL8OddsData == false){
					String res = BetKL8Manager.getKL8oddsData();
					if(res != null){
						getKL8OddsData = true;
					}
					
					
				}
				
				
				

				
				
				
				
				

				
				
				if((betKL8 || betOppositeKL8)&&timeTobetKL8){//最后十五秒秒去下注
					
					clearKL8detaisData = false;
					
					
					String[] betKL8Data = null;
					
					for(int i = 0; i < 4; i++) {
						//todo
						if((betKL8Data = client.getBJKL8data()) == null){
							Thread.currentThread().sleep(1*1000);
						}
						else {
							break;
						}
					}
					
					if(betKL8Data == null) {
						System.out.println("[迪斯尼会员]下单失败,未获取到下单数据");
					} else if(betKL8Data != null &&betKL8Data[0].equals(BetKL8Manager.getKL8drawNumber()) && autoBetSuccess == false) {
						
						String[] betsData = {betKL8Data[1]};
						
						System.out.println("[迪斯尼会员]快乐8下单数据：");
						System.out.println(betKL8Data[1]);
						
						
						
						autoBetSuccess = BetKL8Manager.doBetKL8(betsData, betKL8Percent, betOppositeKL8, betKL8Data[2]);
						
						if(autoBetSuccess == true){
							//dsnHttp.setCQSSCBetData(betsData);
						}
						
						printKL8ErrorValue = false;
						
					}else if(!betKL8Data[0].equals(BetKL8Manager.getKL8drawNumber())){
						System.out.println("快乐8下单数据错误\n");
						
						System.out.printf("服务器期数：%ld， 本地期数:%ld\n", betKL8Data[0], BetKL8Manager.getKL8drawNumber());
						
						
					}
					
					

				}
				
				
				



				sleepTime = 10*1000;
				
				
				
				if(dsnHttp.isAllLotteryIdle()){
					
					
					
					if(BetKL8Manager.getUnCalcProfitBJSCDraw().size() != 0){
						
						Vector<String> calcedKL8Draw = new Vector<String>();
						
						Vector<String> data = BetKL8Manager.getUnCalcProfitBJSCDraw();
						for(int i = 0; i < data.size(); i++){
							String drawNumber = data.elementAt(i);
							
							
							
							String[] profitandbishu = dsnHttp.getBetProfit(drawNumber, BetType.KL8);
							if(profitandbishu[0].equals("none") != true){
								BetKL8Manager.updateKL8WindowdetailsData(drawNumber, TYPEINDEX.STATC.ordinal(), "0");
								BetKL8Manager.updateKL8WindowdetailsData(drawNumber, TYPEINDEX.PROFIT.ordinal(), profitandbishu[0]);
								
								BetKL8Manager.updateKL8WindowdetailsData(drawNumber, TYPEINDEX.COUNT.ordinal(), profitandbishu[1]);
								
								calcedKL8Draw.add(drawNumber);
							}
							
						}
						
						if(calcedKL8Draw.size() != 0){
							BetKL8Manager.updateUnCalcKL8Draw(calcedKL8Draw);
						}
						
						
					}
					
					
					String balance = dsnHttp.getBalance();
					
					BetKL8Manager.updateKL8Balance(balance);

					
				}
				
				if(dsnHttp.isInFreetime(System.currentTimeMillis()) == true && clearKL8detaisData == false){
					BetKL8Manager.clearKL8detalsData();
					clearKL8detaisData = true;
				}

				
				

				
				
				
				
				if(KL8remainTime <= almostTime){					

					long time2 = KL8remainTime - betRemainTime;
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
    
    public static void setKL8BetPercent(double percent){
    	betKL8Percent = percent;
    }
    

}
