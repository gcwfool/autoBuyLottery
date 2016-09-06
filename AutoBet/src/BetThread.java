class BetThread extends Thread{
    
    
	//long betRemainTime = 15*1000;  //离多少秒封盘时进行下注
	
	long almostTime = 25*1000;  //进行最后一次sleep计算的时间
	
	long sleepTime = 10*1000;	//平时水淼时间
	boolean requestTime = true;
	
    static double betCQSSCPercent = 1.0;
    static double betBJSCPercent = 1.0;
    
    static long betRemainTime = 10 * 1000;
    
    static boolean betCQSSC = false;
    static boolean betOppositeCQSSC = false;
    static boolean betBJSC = false;
    static boolean betOppositeBJSC = false;
    
    @Override
    public void run() {
    	try{
	    	boolean autoBetSuccess = false;
	    	
			boolean getCQSSCOddsData = false;
			boolean getBJSCOddsData = false;
			
			
			//用来控制打印封盘数据数据与下单数据差值的变量
			boolean printBJSCErrorValue = false;
			boolean printCQSSCErrorValue = false;

	    	
			while(true){
				long CQSSCremainTime = 0;
				long BJSCremainTime = 0;
				if(requestTime) {
					CQSSCremainTime = dsnHttp.getCQSSCRemainTime();
					BJSCremainTime = dsnHttp.getBJSCRemainTime();
					while(CQSSCremainTime > 10*60*1000 || BJSCremainTime > 10*60*1000){//获取时间失败
						if(dsnHttp.connFailLogin() == false) {
							//todo
							return;
						}
						CQSSCremainTime = dsnHttp.getCQSSCRemainTime();
						BJSCremainTime = dsnHttp.getBJSCRemainTime();
					}
					
					System.out.println("距离重庆时时彩封盘时间为:");
					System.out.println(CQSSCremainTime/1000);			
					System.out.println("距离北京赛车封盘时间为:");
					System.out.println(BJSCremainTime/1000);
					
					if((CQSSCremainTime > 0 && CQSSCremainTime <= 40*1000) || (BJSCremainTime > 0 && BJSCremainTime <= 40*1000)) {//如果将近封盘不发请求，获取本地时间
						requestTime = false;
					}
					
				} else {		
					CQSSCremainTime = dsnHttp.getCQSSClocalRemainTime();
					BJSCremainTime = dsnHttp.getBJSClocalRemainTime();
					
					System.out.println("距离重庆时时彩封盘时间为[local]:");
					System.out.println(CQSSCremainTime/1000);					
					System.out.println("距离北京赛车封盘时间为[local]:");
					System.out.println(BJSCremainTime/1000);
					
					if((CQSSCremainTime <= 0 || CQSSCremainTime > 40*1000) && (BJSCremainTime <= 0 || BJSCremainTime > 40*1000)) {
						requestTime = true;
					}
				}
				
				if(CQSSCremainTime < 0) {
					getCQSSCOddsData = false;
				}
				
				if(BJSCremainTime < 0) {
					getBJSCOddsData = false;
				}
				
				boolean timeTobetCQSSC = CQSSCremainTime <= betRemainTime && CQSSCremainTime > 1;
				boolean timeTobetBJSC = BJSCremainTime <= betRemainTime && BJSCremainTime > 1;


				//每盘拿一次赔率数据
				if(!timeTobetCQSSC && !timeTobetBJSC && (CQSSCremainTime <= 90*1000) && CQSSCremainTime > 0 && getCQSSCOddsData == false){
					dsnHttp.getCQSSCoddsData();
					getCQSSCOddsData = true;
					
				}
				
				if(!timeTobetCQSSC && !timeTobetBJSC && (BJSCremainTime <= 90*1000) && BJSCremainTime > 0 && getBJSCOddsData == false){
					dsnHttp.getBJSCoddsData();
					getBJSCOddsData = true;
					
				}
				
				if((betCQSSC || betOppositeCQSSC)&&timeTobetCQSSC){//最后十五秒秒去下注

					dsnHttp.prepareBetTime1 = System.currentTimeMillis();
					
					String[] betCQSSCData = null;
					
					for(int i = 0; i < 4; i++) {
						if((betCQSSCData = DsnProxyGrab.getCQSSCdata()) == null){
							Thread.currentThread().sleep(1*1000);
						}
						else {
							break;
						}
					}
					
					if(betCQSSCData == null) {
						System.out.println("下单失败,未获取到下单数据");
					} else if(betCQSSCData != null &&betCQSSCData[0].equals(dsnHttp.getCQSSCdrawNumber())) {
						
						String[] betsData = {betCQSSCData[1]};
						
						System.out.println("下单数据：");
						System.out.println(betCQSSCData[1]);
						
						
						
						autoBetSuccess = dsnHttp.doBetCQSSC(betsData, betCQSSCPercent, betOppositeCQSSC, betCQSSCData[2]);
						
						if(autoBetSuccess == true){
							dsnHttp.setCQSSCBetData(betsData);
						}
						
						printCQSSCErrorValue = false;
						
					}
					
					

				}
				
				if((betBJSC || betOppositeBJSC)&&timeTobetBJSC){
					
					dsnHttp.prepareBetTime1 = System.currentTimeMillis();
					
					String[] betBJSCData = null;
					
					for(int i = 0; i < 4; i++) {
						if((betBJSCData = DsnProxyGrab.getBJSCdata()) == null) {
							Thread.currentThread().sleep(1*1000);
						}else {
							break;
						}
					}
					
					if(betBJSCData == null) {
						System.out.println("未获取到下单数据");
					} else if(betBJSCData[0].equals(dsnHttp.getBJSCdrawNumber())){
						String[] betsData = {betBJSCData[1], betBJSCData[2], betBJSCData[3]};
						
						System.out.println("下单数据：");
						System.out.println(betBJSCData[1]);
						System.out.println(betBJSCData[2]);
						System.out.println(betBJSCData[3]);
						
						
						
						autoBetSuccess = dsnHttp.doBetBJSC(betsData, betBJSCPercent, betOppositeBJSC, betBJSCData[4]);
						
						if(autoBetSuccess == true){
							dsnHttp.setBJSCBetData(betsData);
						}
						
						printBJSCErrorValue = false;
					
					}

				}


				sleepTime = 10*1000;
				
				
				//打印封盘数据与下单数据差额
				if(BJSCremainTime < -2*1000 && printBJSCErrorValue == false && dsnHttp.previousBJSCBetResult == true){
					
					
					
					String dataGY = null;
					String dataSSWL = null;
					String dataQBJS = null;
					
					for(int i = 0; i < 4; i++) {
						
						dataGY = DsnProxyGrab.grabBJSCdata("GY", "XZ", "");


						if(dataGY == null || dataGY.equals("timeout")) {
							dataGY = null;
							Thread.currentThread().sleep(1*1000);
						}else {
							break;
						}
					}
					
					
					for(int i = 0; i < 4; i++) {
						
						dataSSWL = DsnProxyGrab.grabBJSCdata("SSWL", "XZ", "");


						if(dataSSWL == null || dataSSWL.equals("timeout")) {
							dataSSWL = null;
							Thread.currentThread().sleep(1*1000);
						}else {
							break;
						}
					}
					
					
					for(int i = 0; i < 4; i++) {
						
						dataQBJS = DsnProxyGrab.grabBJSCdata("QBJS", "XZ", "");


						if(dataQBJS == null || dataQBJS.equals("timeout")) {
							dataQBJS = null;
							Thread.currentThread().sleep(1*1000);
						}else {
							break;
						}
					}
					
					
					
					if(dataGY == null || dataSSWL == null || dataQBJS == null) {
						System.out.println("未获取到封盘数据");
					}else{
						String[] betsData = {dataGY, dataSSWL, dataQBJS};
						
						System.out.println("北京赛车封盘数据：");
						System.out.println(betsData[0]);
						System.out.println(betsData[1]);
						System.out.println(betsData[2]);

						dsnHttp.calcBetDataErrorValue(betsData, BetType.BJSC);

						
					}
					
					printBJSCErrorValue = true;
					
				}
				
				
				
				if(CQSSCremainTime < -2*1000 && printCQSSCErrorValue == false && dsnHttp.previousCQSSCBetResult == true){
					
					String data = null;
					
					for(int i = 0; i < 4; i++) {
						
						data = DsnProxyGrab.grabCQSSCdata("LM", "XZ", "");
						
						if( data == null || data.equals("timeout")) {
							data = null;
							Thread.currentThread().sleep(1*1000);
						}else {
							
							break;
						}
					}
					
					if(data == null) {
						System.out.println("未获取到封盘数据");
					}else{
		
						System.out.println("重庆时时彩封盘数据：");
						System.out.println(data);

						String[] betData = {data};
						
						dsnHttp.calcBetDataErrorValue(betData, BetType.CQSSC);

						
					}
					
					printCQSSCErrorValue = true;
					
				}
				
				
				
				
				if(BJSCremainTime <= almostTime || CQSSCremainTime <= almostTime){					
					long time1 = BJSCremainTime - betRemainTime;
					long time2 = CQSSCremainTime - betRemainTime;
					long littleTime = time1<time2?time1:time2;
					if(time1 <0)
						littleTime = time2;
					if(time2 < 0)
						littleTime = time1;
					if(littleTime > 0 && littleTime <= (almostTime - betRemainTime))
						sleepTime = littleTime;
				}

				Thread.currentThread().sleep(sleepTime);

				
	
			}//while
		
    	}catch (InterruptedException e) {
            // TODO: handle exception
        }
	}//run
    
    public static void setCQSSCBetPercent(double percent){
    	betCQSSCPercent = percent;
    }
    
    public static void setBJSCBetPercent(double percent){
    	betBJSCPercent = percent;
    }    
}
