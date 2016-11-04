import java.util.Vector;
class BetGDKLSFThread extends Thread{
    
    
	//long betRemainTime = 15*1000;  //离多少秒封盘时进行下注
	
	long almostTime = 25*1000;  //进行最后一次sleep计算的时间
	
	long sleepTime = 10*1000;	//平时水淼时间
	boolean requestTime = true;
	
    static double betGDKLSFPercent = 1.0;
    
    static long betRemainTime = 10 * 1000;
    
    static boolean betGDKLSF = false;
    static boolean betOppositeGDKLSF = false;

    
    
    static boolean clearGDKLSFdetaisData = false;
    
    Client client;
    
    public BetGDKLSFThread(Client client){
    	this.client = client;
    }
    
    
    @Override
    public void run() {
    	try{
	    	boolean autoBetSuccess = false;
	    	
			boolean getGDKLSFOddsData = false;
			
			
			//用来控制打印封盘数据数据与下单数据差值的变量
			boolean printGDKLSFErrorValue = false;

	    	
			while(true){
				long GDKLSFremainTime = 0;
				
				if(requestTime) {
					GDKLSFremainTime = BetGDKLSFManager.getGDKLSFremainTime();
					
					if(GDKLSFremainTime > 10*60*1000){//获取时间失败
						

						GDKLSFremainTime = BetGDKLSFManager.getGDKLSFlocalRemainTime();
					}
					
					System.out.println("[迪斯尼会员]距离广东快乐十分封盘时间为:");
					System.out.println(GDKLSFremainTime/1000);			
					
					if((GDKLSFremainTime > 0 && GDKLSFremainTime <= 40*1000)) {//如果将近封盘不发请求，获取本地时间
						requestTime = false;
					}
					
				} else {		
					GDKLSFremainTime = BetGDKLSFManager.getGDKLSFlocalRemainTime();
					
					if(autoBetSuccess == false){
						System.out.println("[迪斯尼会员]距离广东快乐十分封盘为[local]:");
						System.out.println(GDKLSFremainTime/1000);	
					}
									

					
					if((GDKLSFremainTime <= 0 || GDKLSFremainTime > 40*1000) ) {
						requestTime = true;
					}
				}
				
				if(GDKLSFremainTime < 0) {
					getGDKLSFOddsData = false;
					
					autoBetSuccess = false;
				}
				
				
				
				boolean timeTobetGDKLSF = GDKLSFremainTime <= betRemainTime && GDKLSFremainTime > 1;



				//每盘拿一次赔率数据
				if(!timeTobetGDKLSF && (GDKLSFremainTime <= 90*1000) && GDKLSFremainTime > 0 && getGDKLSFOddsData == false){
					String res = BetGDKLSFManager.getGDKLSFoddsData();
					if(res != null)
						getGDKLSFOddsData = true;
					
				}
				
				
				

				
				
				
				
				

				
				
				if((betGDKLSF || betOppositeGDKLSF)&&timeTobetGDKLSF){//最后十五秒秒去下注
					
					clearGDKLSFdetaisData = false;
					
					
					String[] betGDKLSFData = null;
					
					for(int i = 0; i < 4; i++) {
						//todo
						if((betGDKLSFData = client.getGDKLdata()) == null){
							Thread.currentThread().sleep(1*1000);
						}
						else {
							break;
						}
					}
					
					if(betGDKLSFData == null) {
						System.out.println("[迪斯尼会员]下单失败,未获取到下单数据");
					} else if(betGDKLSFData != null && betGDKLSFData[0].equals(BetGDKLSFManager.getGDKLSFdrawNumber()) && autoBetSuccess == false) {
						
						String[] betsData = {betGDKLSFData[1], betGDKLSFData[2],betGDKLSFData[3],betGDKLSFData[4],betGDKLSFData[5],betGDKLSFData[6],betGDKLSFData[7],betGDKLSFData[8],betGDKLSFData[9]};
						
						System.out.println("[迪斯尼会员]广东快乐十分下单数据：");
						System.out.println(betGDKLSFData[1]);
						
						
						
						autoBetSuccess = BetGDKLSFManager.doBetGDKLSF(betsData, betGDKLSFPercent, betOppositeGDKLSF, betGDKLSFData[10]);
						
						if(autoBetSuccess == true){
							//dsnHttp.setCQSSCBetData(betsData);
						}
						
						printGDKLSFErrorValue = false;
						
					}
					
					

				}
				



				sleepTime = 10*1000;
				
				
				
				if(dsnHttp.isAllLotteryIdle()){
					
					
					
					if(BetGDKLSFManager.getUnCalcProfitBJSCDraw().size() != 0){
						
						Vector<String> calcedGDKLSFDraw = new Vector<String>();
						
						Vector<String> data = BetGDKLSFManager.getUnCalcProfitBJSCDraw();
						for(int i = 0; i < data.size(); i++){
							String drawNumber = data.elementAt(i);
							
							
							
							String[] profitandbishu = dsnHttp.getBetProfit(drawNumber, BetType.GDKLSF);
							if(profitandbishu[0].equals("none") != true){
								BetGDKLSFManager.updateGDKLSFWindowdetailsData(drawNumber, TYPEINDEX.STATC.ordinal(), "0");
								BetGDKLSFManager.updateGDKLSFWindowdetailsData(drawNumber, TYPEINDEX.PROFIT.ordinal(), profitandbishu[0]);
								
								BetGDKLSFManager.updateGDKLSFWindowdetailsData(drawNumber, TYPEINDEX.COUNT.ordinal(), profitandbishu[1]);
								
								calcedGDKLSFDraw.add(drawNumber);
							}
							
						}
						
						if(calcedGDKLSFDraw.size() != 0){
							BetGDKLSFManager.updateUnCalcGDKLSFDraw(calcedGDKLSFDraw);
						}
						
						
					}
					
					
					String balance = dsnHttp.getBalance();
					
					BetGDKLSFManager.updateGDKLSFBalance(balance);

					
				}
				
				if(dsnHttp.isInFreetime(System.currentTimeMillis()) == true && clearGDKLSFdetaisData == false){
					BetGDKLSFManager.clearGDKLSFdetalsData();
					clearGDKLSFdetaisData = true;
				}

				
				

				
				
				
				
				if(GDKLSFremainTime <= almostTime){					

					long time2 = GDKLSFremainTime - betRemainTime;
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
    
    public static void setGDKLSFBetPercent(double percent){
    	betGDKLSFPercent = percent;
    }
    

}
