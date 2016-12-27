package dsn;
import java.util.Vector;
class BetGD11X5Thread extends Thread{
    
    
	//long betRemainTime = 15*1000;  //离多少秒封盘时进行下注
	
	long almostTime = 25*1000;  //进行最后一次sleep计算的时间
	
	long sleepTime = 10*1000;	//平时水淼时间
	boolean requestTime = true;
	
    static double betGD11X5Percent = 1.0;
    
    static long betRemainTime = 10 * 1000;
    
    static boolean betGD11X5 = false;
    static boolean betOppositeGD11X5 = false;

    
    
    static boolean clearGD11X5detaisData = false;
    
    Client client;
    
    public BetGD11X5Thread(Client client){
    	this.client = client;
    }
    
    
    @Override
    public void run() {
    	try{
	    	boolean autoBetSuccess = false;
	    	
			boolean getGD11X5OddsData = false;
			
			
			//用来控制打印封盘数据数据与下单数据差值的变量
			boolean printGD11X5ErrorValue = false;

	    	
			while(true){
				
				if(dsnHttp.isInFreetime(System.currentTimeMillis()) == true && clearGD11X5detaisData == false){
					BetGD11X5Manager.clearGD11X5detalsData();
					clearGD11X5detaisData = true;
				}
				
				long localTime = dsnHttp.getlocalTime();
				
				if(!BetGD11X5Manager.isInGD11X5BetTime(localTime) || dsnHttp.isInRelogin == true)
					continue;
				
				
				long GD11X5remainTime = 0;
				
				if(requestTime) {
					GD11X5remainTime = BetGD11X5Manager.getGD11X5remainTime();
					
					if(GD11X5remainTime > 10*60*1000){//获取时间失败
						

						GD11X5remainTime = BetGD11X5Manager.getGD11X5localRemainTime();
					}
					
					System.out.println("[迪斯尼会员]距离广东11选5封盘时间为:");
					System.out.println(GD11X5remainTime/1000);			
					
					if((GD11X5remainTime > 0 && GD11X5remainTime <= 40*1000)) {//如果将近封盘不发请求，获取本地时间
						requestTime = false;
					}
					
				} else {		
					GD11X5remainTime = BetGD11X5Manager.getGD11X5localRemainTime();
					
					if(autoBetSuccess == false){
						System.out.println("[迪斯尼会员]距离广东11选5封盘为[local]:");
						System.out.println(GD11X5remainTime/1000);		
					}
			

					
					if((GD11X5remainTime <= 0 || GD11X5remainTime > 40*1000) ) {
						requestTime = true;
					}
				}
				
				if(GD11X5remainTime < 0) {
					getGD11X5OddsData = false;
					autoBetSuccess = false;
				}
				
				
				
				boolean timeTobetGD11X5 = GD11X5remainTime <= betRemainTime && GD11X5remainTime > 1;



				//每盘拿一次赔率数据
				if(!timeTobetGD11X5 && (GD11X5remainTime <= 90*1000) && GD11X5remainTime > 0 && getGD11X5OddsData == false){
					String res = BetGD11X5Manager.getGD11X5oddsData();
					if(res != null)
						getGD11X5OddsData = true;
					
				}
				
				
				

				
				
				
				
				

				
				
				if((betGD11X5 || betOppositeGD11X5)&&timeTobetGD11X5){//最后十五秒秒去下注
					
					clearGD11X5detaisData = false;
					
					
					String[] betGD11X5Data = null;
					
					for(int i = 0; i < 4; i++) {
						//todo
						if((betGD11X5Data = client.getGD115data()) == null){
							Thread.currentThread().sleep(1*1000);
						}
						else {
							break;
						}
					}
					
					if(betGD11X5Data == null) {
						System.out.println("[迪斯尼会员]下单失败,未获取到下单数据");
					} else if(betGD11X5Data != null &&betGD11X5Data[0].equals(BetGD11X5Manager.getGD11X5drawNumber())&&autoBetSuccess == false) {
						
						String[] betsData = {betGD11X5Data[1]};
						
						System.out.println("[迪斯尼会员]广东11选5下单数据：");
						System.out.println(betGD11X5Data[1]);
						
						
						
						autoBetSuccess = BetGD11X5Manager.doBetGD11X5(betsData, betGD11X5Percent, betOppositeGD11X5, betGD11X5Data[2]);
						
						//todo remove
						autoBetSuccess = true;
						
						if(autoBetSuccess == true){
							//dsnHttp.setCQSSCBetData(betsData);
						}
						
						printGD11X5ErrorValue = false;
						
					}else if(!betGD11X5Data[0].equals(BetGD11X5Manager.getGD11X5drawNumber())){
						System.out.println("广西快乐十分下单数据错误\n");
						
						//System.out.printf("服务器期数：%d， 本地期数:%d\n", betGD11X5Data[0], BetGD11X5Manager.getGD11X5drawNumber());
						
						
					}
					
					

				}
				



				sleepTime = 10*1000;
				
				
				
				if(dsnHttp.isAllLotteryIdle()){
					
					
					
					if(BetGD11X5Manager.getUnCalcProfitBJSCDraw().size() != 0){
						
						Vector<String> calcedGD11X5Draw = new Vector<String>();
						
						Vector<String> data = BetGD11X5Manager.getUnCalcProfitBJSCDraw();
						for(int i = 0; i < data.size(); i++){
							String drawNumber = data.elementAt(i);
							
							
							
							String[] profitandbishu = dsnHttp.getBetProfit(drawNumber, BetType.GD11X5);
							if(profitandbishu[0].equals("none") != true){
								BetGD11X5Manager.updateGD11X5WindowdetailsData(drawNumber, TYPEINDEX.STATC.ordinal(), "0");
								BetGD11X5Manager.updateGD11X5WindowdetailsData(drawNumber, TYPEINDEX.PROFIT.ordinal(), profitandbishu[0]);
								
								BetGD11X5Manager.updateGD11X5WindowdetailsData(drawNumber, TYPEINDEX.COUNT.ordinal(), profitandbishu[1]);
								
								calcedGD11X5Draw.add(drawNumber);
							}
							
						}
						
						if(calcedGD11X5Draw.size() != 0){
							BetGD11X5Manager.updateUnCalcGD11X5Draw(calcedGD11X5Draw);
						}
						
						
					}
					
					
					String balance = dsnHttp.getBalance();
					
					BetGD11X5Manager.updateGD11X5Balance(balance);

					
				}
				


				
				

				
				
				
				
				if(GD11X5remainTime <= almostTime){					

					long time2 = GD11X5remainTime - betRemainTime;
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
    
    public static void setGD11X5BetPercent(double percent){
    	betGD11X5Percent = percent;
    }
    

}
