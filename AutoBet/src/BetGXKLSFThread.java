import java.util.Vector;
class BetGXKLSFThread extends Thread{
    
    
	//long betRemainTime = 15*1000;  //离多少秒封盘时进行下注
	
	long almostTime = 25*1000;  //进行最后一次sleep计算的时间
	
	long sleepTime = 10*1000;	//平时水淼时间
	boolean requestTime = true;
	
    static double betGXKLSFPercent = 1.0;
    
    static long betRemainTime = 10 * 1000;
    
    static boolean betGXKLSF = false;
    static boolean betOppositeGXKLSF = false;

    
    
    static boolean clearGXKLSFdetaisData = false;
    
    Client client;
    
    public BetGXKLSFThread(Client client){
    	this.client = client;
    }
    
    
    @Override
    public void run() {
    	try{
	    	boolean autoBetSuccess = false;
	    	
			boolean getGXKLSFOddsData = false;
			
			
			//用来控制打印封盘数据数据与下单数据差值的变量
			boolean printGXKLSFErrorValue = false;

	    	
			while(true){
				
				if(dsnHttp.isInFreetime(System.currentTimeMillis()) == true && clearGXKLSFdetaisData == false){
					BetGXKLSFManager.clearGXKLSFdetalsData();
					clearGXKLSFdetaisData = true;
				}
				
				long localTime = dsnHttp.getlocalTime();
				
				if(!BetGXKLSFManager.isInGXKLSFBetTime(localTime) || dsnHttp.isInRelogin == true)
					continue;
				
				long GXKLSFremainTime = 0;
				
				if(requestTime) {
					GXKLSFremainTime = BetGXKLSFManager.getGXKLSFremainTime();
					
					if(GXKLSFremainTime > 10*60*1000){//获取时间失败
						

						GXKLSFremainTime = BetGXKLSFManager.getGXKLSFlocalRemainTime();
					}
					
					System.out.println("[迪斯尼会员]距离广西快乐十分封盘时间为:");
					System.out.println(GXKLSFremainTime/1000);			
					
					if((GXKLSFremainTime > 0 && GXKLSFremainTime <= 40*1000)) {//如果将近封盘不发请求，获取本地时间
						requestTime = false;
					}
					
				} else {		
					GXKLSFremainTime = BetGXKLSFManager.getGXKLSFlocalRemainTime();
					
					if(autoBetSuccess == false){
						System.out.println("[迪斯尼会员]距离广西快乐十分封盘时间为[local]:");
						System.out.println(GXKLSFremainTime/1000);		
					}
			
					
					if((GXKLSFremainTime <= 0 || GXKLSFremainTime > 40*1000) ) {
						requestTime = true;
					}
				}
				
				if(GXKLSFremainTime < 0) {
					getGXKLSFOddsData = false;
					autoBetSuccess = false;
				}
				
				
				
				boolean timeTobetGXKLSF = GXKLSFremainTime <= betRemainTime && GXKLSFremainTime > 1;



				//每盘拿一次赔率数据
				if(!timeTobetGXKLSF && (GXKLSFremainTime <= 90*1000) && GXKLSFremainTime > 0 && getGXKLSFOddsData == false){
					String res = BetGXKLSFManager.getGXKLSFoddsData();
					if(res != null){
						getGXKLSFOddsData = true;
					}
					
					
				}
				
				
				

				
				
				
				
				

				
				
				if((betGXKLSF || betOppositeGXKLSF)&&timeTobetGXKLSF){//最后十五秒秒去下注
					
					clearGXKLSFdetaisData = false;
					
					
					String[] betGXKLSFData = null;
					
					for(int i = 0; i < 4; i++) {
						//todo
						if((betGXKLSFData = client.getGXKLdata()) == null){
							Thread.currentThread().sleep(1*1000);
						}
						else {
							break;
						}
					}
					
					if(betGXKLSFData == null) {
						System.out.println("[迪斯尼会员]下单失败,未获取到下单数据");
					} else if(betGXKLSFData != null &&betGXKLSFData[0].equals(BetGXKLSFManager.getGXKLSFdrawNumber()) && autoBetSuccess == false) {
						
						String[] betsData = {betGXKLSFData[1]};
						
						System.out.println("[迪斯尼会员]广西快乐十分下单数据：");
						System.out.println(betGXKLSFData[1]);
						
						
						
						autoBetSuccess = BetGXKLSFManager.doBetGXKLSF(betsData, betGXKLSFPercent, betOppositeGXKLSF, betGXKLSFData[2]);
						
						if(autoBetSuccess == true){
							//dsnHttp.setCQSSCBetData(betsData);
						}
						
						//todo remove
						autoBetSuccess = true;
						
						printGXKLSFErrorValue = false;
						
					}else if(!betGXKLSFData[0].equals(BetGXKLSFManager.getGXKLSFdrawNumber())){
						System.out.println("广西快乐十分下单数据错误\n");
						
						System.out.printf("服务器期数：%d， 本地期数:%d\n", betGXKLSFData[0], BetGXKLSFManager.getGXKLSFdrawNumber());
						
						
					}
					
					

				}
				



				sleepTime = 10*1000;
				
				
				
				if(dsnHttp.isAllLotteryIdle()){
					
					
					
					if(BetGXKLSFManager.getUnCalcProfitBJSCDraw().size() != 0){
						
						Vector<String> calcedGXKLSFDraw = new Vector<String>();
						
						Vector<String> data = BetGXKLSFManager.getUnCalcProfitBJSCDraw();
						for(int i = 0; i < data.size(); i++){
							String drawNumber = data.elementAt(i);
							
							
							
							String[] profitandbishu = dsnHttp.getBetProfit(drawNumber, BetType.GXKLSF);
							if(profitandbishu[0].equals("none") != true){
								BetGXKLSFManager.updateGXKLSFWindowdetailsData(drawNumber, TYPEINDEX.STATC.ordinal(), "0");
								BetGXKLSFManager.updateGXKLSFWindowdetailsData(drawNumber, TYPEINDEX.PROFIT.ordinal(), profitandbishu[0]);
								
								BetGXKLSFManager.updateGXKLSFWindowdetailsData(drawNumber, TYPEINDEX.COUNT.ordinal(), profitandbishu[1]);
								
								calcedGXKLSFDraw.add(drawNumber);
							}
							
						}
						
						if(calcedGXKLSFDraw.size() != 0){
							BetGXKLSFManager.updateUnCalcGXKLSFDraw(calcedGXKLSFDraw);
						}
						
						
					}
					
					
					String balance = dsnHttp.getBalance();
					
					BetGXKLSFManager.updateGXKLSFBalance(balance);

					
				}
				


				
				

				
				
				
				
				if(GXKLSFremainTime <= almostTime){					

					long time2 = GXKLSFremainTime - betRemainTime;
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
    
    public static void setGXKLSFBetPercent(double percent){
    	betGXKLSFPercent = percent;
    }
    

}
