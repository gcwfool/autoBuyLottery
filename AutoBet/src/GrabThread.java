class GrabThread extends Thread{
	static long almostTime = 30*1000;  //进行最后一次sleep计算的时间	
	long sleepTime = 10*1000;	//平时睡眠时间
    
    static long betRemainTime = 10 * 1000;
    static long grabStartTime = 25 * 1000;
    
    static boolean grabCQSSC = false;
    static boolean grabBJSC = false;
    autoBet ab;
    public GrabThread(autoBet ab) {
		this.ab = ab;
	}
    
    @Override
    public void run() {
    	try {
    		if(!DsnProxyGrab.login()) {
    			//todo
    			return;
    		}
    		
			while(true){
				long CQSSCremainTime = DsnProxyGrab.getCQSSCRemainTime();
				long BJSCremainTime = DsnProxyGrab.getBJSCRemainTime();
				if(CQSSCremainTime > 10*60*1000 || BJSCremainTime > 10*60*1000) {//获取时间失败
					if(!DsnProxyGrab.login()) {
						//todo
						return;
					}
					CQSSCremainTime = DsnProxyGrab.getCQSSCRemainTime();
					BJSCremainTime = DsnProxyGrab.getBJSCRemainTime();
				}
				
				System.out.println("距离重庆时时彩封盘时间为:");
				System.out.println(CQSSCremainTime/1000);
				
				System.out.println("距离北京赛车封盘时间为:");
				System.out.println(BJSCremainTime/1000);
				
				boolean timeToGrabCQSSC = CQSSCremainTime <= grabStartTime && CQSSCremainTime >= betRemainTime;
				boolean timeToGrabBJSC = BJSCremainTime <= grabStartTime && BJSCremainTime >= betRemainTime;
				
				DsnProxyGrab.resetData();

				while((timeToGrabCQSSC && grabCQSSC) || (timeToGrabBJSC && grabBJSC)) {//最后十五秒秒去下注
					String data = null;
					if(timeToGrabCQSSC) {
						data = DsnProxyGrab.grabCQSSCdata("LM", "XZ", "");
						if(data == null) {
							if(!DsnProxyGrab.login()) {
				    			//todo
				    			return;
				    		}
						}
						else if(data == "timeout") {
							continue;
						}
						else {
							DsnProxyGrab.setCQSSCdata(data);
						}
					}
					
					if(timeToGrabBJSC) {
						String dataGY = DsnProxyGrab.grabBJSCdata("GY", "XZ", "");
						if(dataGY == null) {
							if(!DsnProxyGrab.login()) {
				    			//todo
				    			return;
				    		}
						}
						else if(dataGY == "timeout") {
							continue;
						}
						else {
							DsnProxyGrab.setBJSC_GY(dataGY);
						}
						String dataSSWL = DsnProxyGrab.grabBJSCdata("SSWL", "XZ", "");
						if(dataSSWL == null) {
							if(!DsnProxyGrab.login()) {
				    			//todo
				    			return;
				    		}
						}
						else if(dataSSWL == "timeout") {
							continue;
						}
						else {
							DsnProxyGrab.setBJSC_SSWL(dataSSWL);
						}
						String dataQBJS = DsnProxyGrab.grabBJSCdata("QBJS", "XZ", "");
						if(dataQBJS == null) {
							if(!DsnProxyGrab.login()) {
				    			//todo
				    			return;
				    		}
						}
						else if(dataQBJS == "timeout") {
							continue;
						}
						else {
							DsnProxyGrab.setBJSC_QBJS(dataQBJS);
						}
					}
					
					sleep(1*1000);
				
					//
					CQSSCremainTime = DsnProxyGrab.getCQSSCRemainTime();
					BJSCremainTime = DsnProxyGrab.getBJSCRemainTime();
					timeToGrabCQSSC = CQSSCremainTime <= grabStartTime && CQSSCremainTime >=betRemainTime;
					timeToGrabBJSC = BJSCremainTime <= grabStartTime && BJSCremainTime >=betRemainTime;
				}
				
				
				sleepTime = 10*1000;	
				
				if(BJSCremainTime <= almostTime || CQSSCremainTime <= almostTime){					
					long time1 = BJSCremainTime - grabStartTime;
					long time2 = CQSSCremainTime - grabStartTime;
					long littleTime = time1<time2?time1:time2;
					if(time1 <0)
						littleTime = time2;
					if(time2 < 0)
						littleTime = time1;
					if(littleTime > 0 && littleTime <= (almostTime - grabStartTime))
						sleepTime = littleTime;
				}
				
				Thread.currentThread().sleep(sleepTime);

			}//while
		
    	}catch (InterruptedException e) {
            // TODO: handle exception
        }
	}//run
    
    public static void setBetRemainTime(long time) {
    	betRemainTime = time;
    	grabStartTime = time + (15*1000);
    	almostTime = time + (20*1000);
    }
    
}
