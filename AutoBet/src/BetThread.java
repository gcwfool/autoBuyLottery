class BetThread extends Thread{
    
    
	//long betRemainTime = 15*1000;  //离多少秒封盘时进行下注
	
	long almostTime = 25*1000;  //进行最后一次sleep计算的时间
	
	long sleepTime = 10*1000;	//平时水淼时间
	
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
			while(true){
				long CQSSCremainTime = dsnHttp.getCQSSCRemainTime();
				long BJSCremainTime = dsnHttp.getBJSCRemainTime();
				if(CQSSCremainTime > 10*60*1000 || BJSCremainTime > 10*60*1000){//获取时间失败
					while(dsnHttp.loginToDsn() == false); //重新登录
					CQSSCremainTime = dsnHttp.getCQSSCRemainTime();
					BJSCremainTime = dsnHttp.getBJSCRemainTime();
				}
				
				System.out.println("距离重庆时时彩封盘时间为:");
				System.out.println(CQSSCremainTime/1000);
				
				System.out.println("距离北京赛车封盘时间为:");
				System.out.println(BJSCremainTime/1000);
				
				boolean timeTobetCQSSC = CQSSCremainTime <= betRemainTime && CQSSCremainTime >=0;
				boolean timeTobetBJSC = BJSCremainTime <= betRemainTime && BJSCremainTime >=0;

				if((betCQSSC || betOppositeCQSSC)&&timeTobetCQSSC){//最后十五秒秒去下注
					String data = DsnProxyGrab.grabCQSSCdata("LM", "XZ", "");
					if(data != null && data.equals("timeout")){//超时，重新获取数据
						data = DsnProxyGrab.grabCQSSCdata("LM", "XZ", "");
					}
					if(data == null || data.equals("timeout")){//获取数据失败
						while(DsnProxyGrab.doLogin() == false); //重新连接
						data = DsnProxyGrab.grabCQSSCdata("LM", "XZ", "");
					}
					String[] betData = {data};
					System.out.println("下单数据：");
					System.out.println(data);
					if(data != null){
					autoBetSuccess = dsnHttp.doBetCQSSC(betData, betCQSSCPercent, betOppositeCQSSC);
					}
				}
				
				if((betBJSC || betOppositeBJSC)&&timeTobetBJSC){
					//冠亚
					String dataGY = DsnProxyGrab.grabBJSCdata("GY", "XZ", "");		
					if(dataGY != null && dataGY.equals("timeout")){//超时，重新获取数据
						dataGY = DsnProxyGrab.grabBJSCdata("GY", "XZ", "");
					}
					if(dataGY == null || dataGY.equals("timeout")){//获取数据失败
						while(DsnProxyGrab.doLogin() == false); //重新连接
						dataGY = DsnProxyGrab.grabBJSCdata("GY", "XZ", "");
					}
					
					//三四五六
					String dataSSWL = DsnProxyGrab.grabBJSCdata("SSWL", "XZ", "");
					if(dataSSWL != null && dataSSWL.equals("timeout")){//超时，重新获取数据
						dataSSWL = DsnProxyGrab.grabBJSCdata("SSWL", "XZ", "");
					}
					if(dataSSWL == null || dataSSWL.equals("timeout")){//获取数据失败
						while(DsnProxyGrab.doLogin() == false); //重新连接
						dataSSWL = DsnProxyGrab.grabBJSCdata("SSWL", "XZ", "");
					}
					
					//七八九十
					String dataQBJS = DsnProxyGrab.grabBJSCdata("QBJS", "XZ", "");
					if(dataQBJS != null && dataQBJS.equals("timeout")){//超时，重新获取数据
						dataQBJS = DsnProxyGrab.grabBJSCdata("QBJS", "XZ", "");
					}
					if(dataQBJS == null || dataQBJS.equals("timeout")){//获取数据失败
						while(DsnProxyGrab.doLogin() == false); //重新连接
						dataQBJS = DsnProxyGrab.grabBJSCdata("QBJS", "XZ", "");
					}
					
					
					String[] betBJSCData = {dataGY, dataSSWL, dataQBJS};
					
					System.out.println("下单数据：");
					System.out.println(dataGY);
					System.out.println(dataSSWL);
					System.out.println(dataQBJS);
					
					if(dataGY != null && dataSSWL != null && dataQBJS != null){
					autoBetSuccess = dsnHttp.doBetBJSC(betBJSCData, betBJSCPercent, betOppositeBJSC);
					}
				}

				
				//保持代理端连接
				if(CQSSCremainTime >= 0){
					String data = DsnProxyGrab.grabCQSSCdata("LM", "XZ", "");  //请求服务器，保持连接
					if(data == null || data.length() <= 0){//获取数据失败
						while(DsnProxyGrab.doLogin() == false); //重新连接
						data = DsnProxyGrab.grabCQSSCdata("LM", "XZ", "");
					}
				}
				else if(BJSCremainTime >= 0){
					String data = DsnProxyGrab.grabBJSCdata("GY", "XZ", "");		

					if(data == null || data.equals("timeout")){//获取数据失败
						while(DsnProxyGrab.doLogin() == false); //重新连接
						data = DsnProxyGrab.grabBJSCdata("GY", "XZ", "");
					}
				}
					

				
				
				sleepTime = 10*1000;
				
				
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
