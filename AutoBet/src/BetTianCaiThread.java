public class BetTianCaiThread extends Thread{
    
    
	//long betRemainTime = 15*1000;  //离多少秒封盘时进行下注
	
	long almostTime = 25;  //进行最后一次sleep计算的时间
	
	long sleepTime = 10;	//平时睡眠时间
	boolean requestTime = true;
	
    static double betCQSSCPercent = 1.0;
    static double betBJSCPercent = 1.0;
    
    static long betRemainTime = 30;
    
    static boolean betCQSSC = false;
    static boolean betOppositeCQSSC = false;
    static boolean betBJSC = false;
    static boolean betOppositeBJSC = false;
    
    TianCaiHttp tianCaiHttp = null;
    
    
    BetTianCaiThread(TianCaiHttp tch) {
    	tianCaiHttp = tch;
    }
    
    @Override
    public void run() {
    	try{
	    	boolean autoBetSuccess = false;
	    	
			boolean getCQSSCOddsData = false;
			boolean getBJSCOddsData = false;

	    	
			while(true){
				long CQSSCremainTime = 0;
				long BJSCremainTime = 0;
				if(requestTime) {
					CQSSCremainTime = tianCaiHttp.getCQSSCRemainTime();
					BJSCremainTime = tianCaiHttp.getBJSCRemainTime();
					while(CQSSCremainTime == -9999 || BJSCremainTime == -9999){//获取时间失败
						if(tianCaiHttp.login() == false) {
							//todo
							return;
						}
						CQSSCremainTime = tianCaiHttp.getCQSSCRemainTime();
						BJSCremainTime = tianCaiHttp.getBJSCRemainTime();
					}
					
					System.out.println("[添彩]距离重庆时时彩封盘时间为:");
					System.out.println(CQSSCremainTime);			
					System.out.println("[添彩]距离北京赛车封盘时间为:");
					System.out.println(BJSCremainTime);
					
					if((CQSSCremainTime > 0 && CQSSCremainTime <= 40) || (BJSCremainTime > 0 && BJSCremainTime <= 40)) {//如果将近封盘不发请求，获取本地时间
						requestTime = false;
					}
					
				} else {		
					CQSSCremainTime = tianCaiHttp.getCQSSClocalRemainTime();
					BJSCremainTime = tianCaiHttp.getBJSClocalRemainTime();
					
					System.out.println("[添彩]距离重庆时时彩封盘时间为[local]:");
					System.out.println(CQSSCremainTime);					
					System.out.println("[添彩]距离北京赛车封盘时间为[local]:");
					System.out.println(BJSCremainTime);
					
					if((CQSSCremainTime <= 0 || CQSSCremainTime > 40) && (BJSCremainTime <= 0 || BJSCremainTime > 40)) {
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

				
				if((betCQSSC || betOppositeCQSSC)&&timeTobetCQSSC){//最后十五秒秒去下注

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
					} else if(betCQSSCData != null &&betCQSSCData[0].equals(tianCaiHttp.getCQSSCdrawNumber())) {
						
						String[] betsData = {betCQSSCData[1]};
						
						System.out.println("下单数据：");
						System.out.println(betCQSSCData[1]);
						
						autoBetSuccess = tianCaiHttp.doBetCQSSC(betsData, betCQSSCPercent, betOppositeCQSSC, betCQSSCData[2]);
						
					}
					
					

				}
				
				if((betBJSC || betOppositeBJSC)&&timeTobetBJSC){
					
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
					} else if(betBJSCData[0].equals(tianCaiHttp.getBJSCdrawNumber())){
						String[] betsData = {betBJSCData[1], betBJSCData[2], betBJSCData[3]};
						
						System.out.println("下单数据：");
						System.out.println(betBJSCData[1]);
						System.out.println(betBJSCData[2]);
						System.out.println(betBJSCData[3]);
						autoBetSuccess = tianCaiHttp.doBetBJSC(betsData, betBJSCPercent, betOppositeBJSC, betBJSCData[4]);
					
					}

				}


				sleepTime = 10;
				
				
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

				Thread.currentThread().sleep(sleepTime * 1000);

				
	
			}//while
		
    	}catch (InterruptedException e) {
            // TODO: handle exception
        }
//        TianCaiHttp tianCaiHttp = new TianCaiHttp();
//    	while(true) {
//    		try {
//    			
//    			if(betOppositeCQSSC || betCQSSC) {
//    				long cqsscRemainTime = tianCaiHttp.getCQSSCRemainTime();
//    				System.out.println("【添彩】距离重庆时时彩收盘： " + cqsscRemainTime);
//    				
//    			}
//    			
//    			//String [] betCQSSCData = null;
//    	//		String data = "[[{\"k\":\"DX2\",\"i\":\"X\",\"c\":14,\"a\":1986,\"r\":3970.7292,\"cm\":0.0148},{\"k\":\"DX3\",\"i\":\"D\",\"c\":14,\"a\":1008,\"r\":2015.4624,\"cm\":0},{\"k\":\"DX4\",\"i\":\"D\",\"c\":4,\"a\":141,\"r\":280.9974,\"cm\":0.4872},{\"k\":\"LH\",\"i\":\"H\",\"c\":1,\"a\":8000,\"r\":15544,\"cm\":0},{\"k\":\"DS1\",\"i\":\"D\",\"c\":5,\"a\":314,\"r\":627.8868,\"cm\":0.0252},{\"k\":\"DX2\",\"i\":\"D\",\"c\":6,\"a\":204,\"r\":407.3544,\"cm\":0.3024},{\"k\":\"DS4\",\"i\":\"D\",\"c\":3,\"a\":21,\"r\":41.8278,\"cm\":0.084},{\"k\":\"DX3\",\"i\":\"X\",\"c\":3,\"a\":40,\"r\":79.6728,\"cm\":0.1596},{\"k\":\"DX5\",\"i\":\"D\",\"c\":8,\"a\":462,\"r\":923.7396,\"cm\":0},{\"k\":\"ZDS\",\"i\":\"D\",\"c\":8,\"a\":462,\"r\":923.7396,\"cm\":0},{\"k\":\"DX1\",\"i\":\"X\",\"c\":13,\"a\":8502,\"r\":16547.0596,\"cm\":0.074},{\"k\":\"DX4\",\"i\":\"X\",\"c\":13,\"a\":2966,\"r\":5930.7348,\"cm\":0},{\"k\":\"LH\",\"i\":\"L\",\"c\":2,\"a\":240,\"r\":477.264,\"cm\":1.344},{\"k\":\"ZDX\",\"i\":\"D\",\"c\":15,\"a\":2008,\"r\":3998.4624,\"cm\":0},{\"k\":\"ZDX\",\"i\":\"X\",\"c\":4,\"a\":129,\"r\":256.3278,\"cm\":0.3192},{\"k\":\"DS1\",\"i\":\"S\",\"c\":9,\"a\":474,\"r\":947.7372,\"cm\":0},{\"k\":\"DS2\",\"i\":\"D\",\"c\":8,\"a\":462,\"r\":923.7396,\"cm\":0},{\"k\":\"DS3\",\"i\":\"D\",\"c\":17,\"a\":7008,\"r\":13963.8624,\"cm\":0},{\"k\":\"DS3\",\"i\":\"S\",\"c\":3,\"a\":220,\"r\":437.6208,\"cm\":1.1676},{\"k\":\"DS4\",\"i\":\"S\",\"c\":9,\"a\":532,\"r\":1063.7256,\"cm\":0},{\"k\":\"DS5\",\"i\":\"D\",\"c\":14,\"a\":1008,\"r\":2015.4624,\"cm\":0},{\"k\":\"DX1\",\"i\":\"D\",\"c\":3,\"a\":259,\"r\":515.109,\"cm\":1.4196},{\"k\":\"DX5\",\"i\":\"X\",\"c\":3,\"a\":19,\"r\":37.845,\"cm\":0.0756},{\"k\":\"ZDS\",\"i\":\"S\",\"c\":5,\"a\":310,\"r\":619.8876,\"cm\":0.0252},{\"k\":\"DS2\",\"i\":\"S\",\"c\":5,\"a\":106,\"r\":211.8612,\"cm\":0.0588},{\"k\":\"DS5\",\"i\":\"S\",\"c\":3,\"a\":40,\"r\":79.6728,\"cm\":0.1596}],{\"DS1_S\":1.983,\"DS1_D\":1.983,\"DS2_S\":1.983,\"DS2_D\":1.983,\"DS3_S\":1.983,\"DS3_D\":1.983,\"DS4_S\":1.983,\"DS4_D\":1.983,\"DS5_S\":1.983,\"DS5_D\":1.983,\"DX1_X\":1.983,\"DX1_D\":1.983,\"DX2_X\":1.983,\"DX2_D\":1.983,\"DX3_X\":1.983,\"DX3_D\":1.983,\"DX4_X\":1.983,\"DX4_D\":1.983,\"DX5_X\":1.983,\"DX5_D\":1.983,\"LH_T\":9.28,\"LH_H\":1.983,\"LH_L\":1.983,\"ZDS_S\":1.983,\"ZDS_D\":1.983,\"ZDX_X\":1.983,\"ZDX_D\":1.983},{\"B1\":50,\"B4\":50,\"LM\":36921,\"B3\":20050,\"B5\":50,\"B2\":100}]";
////    			for(int i = 0; i < 4; i++) {
////					if((betCQSSCData = DsnProxyGrab.getCQSSCdata()) == null){
////						Thread.currentThread().sleep(1*1000);
////					}
////					else {
////						break;
////					}
////				}
//    			
//    			//if(betCQSSCData != null) {
//		    		String[] betsData = {data};
//		    		tianCaiHttp.doBetCQSSC(betsData, betCQSSCPercent, true, "10");
//    		//	}
//	    		Thread.currentThread().sleep(10*1000);
//	    	}catch (InterruptedException e) {
//	            // TODO: handle exception
//	        }
//    	}
	}//run
    
    public void setCQSSCBetPercent(double percent){
    	betCQSSCPercent = percent;
    }
    
    public void setBJSCBetPercent(double percent){
    	betBJSCPercent = percent;
    }
}
