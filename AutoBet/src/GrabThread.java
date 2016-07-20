class GrabThread extends Thread{
	static long almostTime = 40*1000;  //进入加速时间
	long sleepTime = 7*1000;	//平时睡眠时间
    
    static boolean grabCQSSC = false;
    static boolean grabBJSC = false;
    static boolean isCQSSCclose = false;
    static boolean isBJSCclose = false;
    GrabCQSSCwindow gwCQSSC;
    GrabBJSCwindow gwBJSC;
    public GrabThread(GrabCQSSCwindow gwCQSSC, GrabBJSCwindow gwBJSC) {
		this.gwCQSSC = gwCQSSC;
		this.gwBJSC = gwBJSC;
	}
    
    @Override
    public void run() {
    	try {
    		if(!DsnProxyGrab.login()) {
    			//todo
    			return;
    		}
    		
			while(true){
				String[] CQSSCTime = {"", "", ""};
				String[] BJSCTime = {"", "", ""};
				long CQSSCremainTime = 0;
				long BJSCremainTime = 0;
				if(grabCQSSC) {
					CQSSCTime= DsnProxyGrab.getCQSSCTime();
					CQSSCremainTime = Long.parseLong(CQSSCTime[0]);
					while(CQSSCremainTime > 10*60*1000) {//获取时间失败
						if(!DsnProxyGrab.login()) {
							//todo
							return;
						}
						CQSSCTime = DsnProxyGrab.getCQSSCTime();
						CQSSCremainTime = Long.parseLong(CQSSCTime[0]);
					}
					
					if(CQSSCremainTime > 0) {
						if(isCQSSCclose) {
							gwCQSSC.setCloseText(false);
							gwCQSSC.resetData();
							isCQSSCclose = false;
						}
						System.out.println("距离重庆时时彩封盘时间为:");
						System.out.println(CQSSCremainTime/1000);
						gwCQSSC.setRemainTime(CQSSCremainTime);
						if(CQSSCremainTime < almostTime) {
							sleepTime = 3*1000;
						}
					}else {
						if(!isCQSSCclose) {
							gwCQSSC.setCloseText(true);
							isCQSSCclose = true;
							sleepTime = 7*1000;
							DsnProxyGrab.disableCQSSCData();
						}
						System.out.println("距离重庆时时彩开盘:");
						System.out.println(Long.parseLong(CQSSCTime[2])/1000);
						gwCQSSC.setRemainTime(Long.parseLong(CQSSCTime[2]));
					}
					
					gwCQSSC.setDrawNumber(CQSSCTime[1]);
				}
				
				if(grabBJSC) {
					BJSCTime= DsnProxyGrab.getBJSCTime();
					BJSCremainTime = Long.parseLong(BJSCTime[0]);
					while(BJSCremainTime > 10*60*1000) {//获取时间失败
						if(!DsnProxyGrab.login()) {
							//todo
							return;
						}
						BJSCTime = DsnProxyGrab.getBJSCTime();
						BJSCremainTime = Long.parseLong(BJSCTime[0]);
					}
					
					if(BJSCremainTime > 0) {
						if(isBJSCclose) {
							gwBJSC.setCloseText(false);
							gwBJSC.resetData();
							isBJSCclose = false;
						}
						System.out.println("距离北京赛车彩封盘时间为:");
						System.out.println(BJSCremainTime/1000);
						gwBJSC.setRemainTime(BJSCremainTime);
						if(BJSCremainTime < almostTime) {
							sleepTime = 3*1000;
						}
					}else {
						if(!isBJSCclose) {
							gwBJSC.setCloseText(true);
							isBJSCclose = true;
							sleepTime = 7*1000;
							DsnProxyGrab.disableBJSCData();
						}
						System.out.println("距离北京赛车开盘:");
						System.out.println(Long.parseLong(BJSCTime[2])/1000);
						gwBJSC.setRemainTime(Long.parseLong(BJSCTime[2]));
					}
					
					gwBJSC.setDrawNumber(BJSCTime[1]);
				}			
				
				if(grabCQSSC && CQSSCremainTime > 3000 && !((CQSSCremainTime > almostTime) && grabBJSC && (BJSCremainTime < almostTime) && (BJSCremainTime > 0))) {
					String data = DsnProxyGrab.grabCQSSCdata("LM", "XZ", "");
					if(data == "timeout" || data == null) {
						continue;
					}
					
					if(CQSSCremainTime < almostTime) {
						DsnProxyGrab.setCQSSCdata(CQSSCTime[1], data);
					}
					String [] datas = {data};
					gwCQSSC.setData(datas);
					
				}
			    
				if(grabBJSC && BJSCremainTime > 3000 && !((BJSCremainTime > almostTime) && grabCQSSC && (CQSSCremainTime < almostTime) && (CQSSCremainTime > 0))) {
					String dataGY = DsnProxyGrab.grabBJSCdata("GY", "XZ", "");
					if(dataGY == "timeout" || dataGY == null) {
						continue;
					}
					
					String dataSSWL = DsnProxyGrab.grabBJSCdata("SSWL", "XZ", "");
					if(dataSSWL == "timeout" || dataGY == null) {
						continue;
					}
					
					String dataQBJS = DsnProxyGrab.grabBJSCdata("QBJS", "XZ", "");
					if(dataQBJS == "timeout" || dataQBJS == null) {
						continue;
					}
					
					String [] data = {dataGY, dataSSWL, dataQBJS};
					if(BJSCremainTime < almostTime) {
						DsnProxyGrab.setBJSCdata(BJSCTime[1], data);
					}
					gwBJSC.setData(data);
				}
				
				if(DsnProxyGrab.getCQSSCdata() != null) {
					gwCQSSC.setDataOk(true);
				}
				else {
					gwCQSSC.setDataOk(false);
				}
				
				if(DsnProxyGrab.getBJSCdata() != null) {
					gwBJSC.setDataOk(true);
				}
				else {
					gwBJSC.setDataOk(false);
				}
					
				Thread.currentThread().sleep(sleepTime);

			}//while
		
    	}catch (InterruptedException e) {
            // TODO: handle exception
        }
	}//run
    
    public  void startGrabCQSSC() {
    	grabCQSSC = true;
    	gwCQSSC.setVisible(true);
    }
    
    public  void startGrabBJSC() {
    	grabBJSC = true;
    	gwBJSC.setVisible(true);
    }
    
    public  void stopGrabCQSSC() {
    	grabCQSSC = false;
    	gwCQSSC.setVisible(false);
    }
    
    public  void stopGrabBJSC() {
    	grabBJSC = false;
    	gwBJSC.setVisible(false);
    }
    
}
