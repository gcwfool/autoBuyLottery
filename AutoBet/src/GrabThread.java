class GrabThread extends Thread{
	long almostTime = 45*1000;  //进入加速时间
	long sleepTime = 9*1000;	//平时睡眠时间
    
    boolean grabCQSSC = false;
    boolean grabBJSC = false;
    boolean isCQSSCclose = false;
    boolean isBJSCclose = false;
    boolean isNeedLogin = false;
    boolean requestTime = true;
    boolean inCQSSCgrabTime = true;
    boolean inBJSCgrabTime = true;
    GrabCQSSCwindow gwCQSSC;
    GrabBJSCwindow gwBJSC;
    public GrabThread(GrabCQSSCwindow gwCQSSC, GrabBJSCwindow gwBJSC) {
		this.gwCQSSC = gwCQSSC;
		this.gwBJSC = gwBJSC;
	}
    
    @Override
    public void run() {
    	try {
    		String[] CQSSCTime = {"0", "0", "0"};
			String[] BJSCTime = {"0", "0", "0"};
			while(true){
				if(isNeedLogin) {
					if(!DsnProxyGrab.login()) {
						//todo
						return;
					}
					isNeedLogin = false;
				}
				long CQSSCremainTime = 0;
				long BJSCremainTime = 0;
				if(DsnProxyGrab.isInCQSSCgrabTime()) {
						inCQSSCgrabTime = true;
				} else {
					if(inCQSSCgrabTime) {
						CQSSCremainTime = -1;
						isCQSSCclose = true;
						gwCQSSC.resetData();
						gwCQSSC.setRemainTime(0);
						DsnProxyGrab.disableCQSSCData();
						inCQSSCgrabTime = false;
					}
				}
				
				if(grabCQSSC && inCQSSCgrabTime) {
					CQSSCremainTime = gwCQSSC.getRemainTime();
					if(requestTime) {
						CQSSCTime= DsnProxyGrab.getCQSSCTime();
						CQSSCremainTime = Long.parseLong(CQSSCTime[0]);
						if(CQSSCremainTime > 0) {
							System.out.println("距离重庆时时彩封盘:" + CQSSCremainTime/1000);
							gwCQSSC.setRemainTime(CQSSCremainTime);
						}
						else {
							System.out.println("距离重庆时时彩开盘:" + Long.parseLong(CQSSCTime[2])/1000);
							gwCQSSC.setRemainTime(Long.parseLong(CQSSCTime[2]));
						}
						
					}
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
						if(!requestTime) {
							System.out.println("[距离重庆时时彩封盘时间为]:" + CQSSCremainTime/1000);
						}
						if(CQSSCremainTime < almostTime) {
							sleepTime = 4*1000;
							requestTime = false;
						}
						
					} else if(CQSSCremainTime <= 0){
						if(!isCQSSCclose) {
							gwCQSSC.setCloseText(true);
							isCQSSCclose = true;
							requestTime = true;
							sleepTime = 9*1000;
							DsnProxyGrab.disableCQSSCData();
							continue;
						}
					}
					
					gwCQSSC.setDrawNumber(CQSSCTime[1]);
				}
				
				if(DsnProxyGrab.isInBJSCgrabTime()) {
					inBJSCgrabTime = true;
				} else {
					if(inBJSCgrabTime) {
						BJSCremainTime = -1;
						isBJSCclose = true;
						gwBJSC.resetData();
						gwBJSC.setRemainTime(0);
						DsnProxyGrab.disableBJSCData();
						inBJSCgrabTime = false;
					}
				}
				
				if(grabBJSC && inBJSCgrabTime) {
					BJSCremainTime = gwBJSC.getRemainTime();
					if(requestTime) {
						BJSCTime= DsnProxyGrab.getBJSCTime();
						BJSCremainTime = Long.parseLong(BJSCTime[0]);
						if(BJSCremainTime > 0) {
							System.out.println("距离北京赛车封盘:" + BJSCremainTime/1000);
							gwBJSC.setRemainTime(BJSCremainTime);
						}
						else {
							System.out.println("距离北京赛车开盘:" + Long.parseLong(BJSCTime[2])/1000);
							gwBJSC.setRemainTime(Long.parseLong(BJSCTime[2]));
						}
					}
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
						
						if(!requestTime) {
							System.out.println("[距离北京赛车彩封盘时间为]:" + BJSCremainTime/1000);
						}
						
						if(BJSCremainTime < almostTime) {
							sleepTime = 3*1000;
							requestTime = false;
						}
					}else if(BJSCremainTime <= 0){
						if(!isBJSCclose) {
							gwBJSC.setCloseText(true);
							isBJSCclose = true;
							requestTime = true;
							sleepTime = 9*1000;
							DsnProxyGrab.disableBJSCData();
							continue;
						}
					}
					
					gwBJSC.setDrawNumber(BJSCTime[1]);
				}			
				
				if(grabCQSSC && inCQSSCgrabTime && CQSSCremainTime > 3000 && !((CQSSCremainTime > almostTime) && grabBJSC && (BJSCremainTime < almostTime) && (BJSCremainTime > 0))) {
					String data = DsnProxyGrab.grabCQSSCdata("LM", "XZ", "");
					if(data == "timeout") {
						continue;
					}else if(data == null) {
						isNeedLogin = true;
						continue;
					}
					
					if(CQSSCremainTime < almostTime) {
						DsnProxyGrab.setCQSSCdata(CQSSCTime[1], data, Long.toString(gwCQSSC.getRemainTime()/1000));
					}
					String [] datas = {data};
					gwCQSSC.setData(datas);
					
				}
			    
				if(grabBJSC && inBJSCgrabTime && BJSCremainTime > 3000 && !((BJSCremainTime > almostTime) && grabCQSSC && (CQSSCremainTime < almostTime) && (CQSSCremainTime > 0))) {
					String dataGY = DsnProxyGrab.grabBJSCdata("GY", "XZ", "");
					if(dataGY == "timeout") {
						continue;
					}else if(dataGY == null) {
						isNeedLogin = true;
						continue;
					}
					
					String dataSSWL = DsnProxyGrab.grabBJSCdata("SSWL", "XZ", "");
					if(dataSSWL == "timeout") {
						continue;
					}else if(dataSSWL == null) {
						isNeedLogin = true;
						continue;
					}
					
					String dataQBJS = DsnProxyGrab.grabBJSCdata("QBJS", "XZ", "");
					if(dataQBJS == "timeout") {
						continue;
					}else if(dataQBJS == null) {
						isNeedLogin = true;
						continue;
					}
					
					String [] data = {dataGY, dataSSWL, dataQBJS};
					if(BJSCremainTime < almostTime) {
						DsnProxyGrab.setBJSCdata(BJSCTime[1], data, Long.toString(gwBJSC.getRemainTime()/1000));
					}
					gwBJSC.setData(data);
				}
				
				if(grabCQSSC && inCQSSCgrabTime) {
					if(DsnProxyGrab.getCQSSCdata() != null) {
						gwCQSSC.setDataOk(true);
					}
					else {
						gwCQSSC.setDataOk(false);
					}
				}
				
				if(grabBJSC && inBJSCgrabTime) {
					if(DsnProxyGrab.getBJSCdata() != null) {
						gwBJSC.setDataOk(true);
					}
					else {
						gwBJSC.setDataOk(false);
					}
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
    	gwCQSSC.resetData();
    	gwCQSSC.setVisible(false);
    }
    
    public  void stopGrabBJSC() {
    	grabBJSC = false;
    	gwBJSC.resetData();
    	gwBJSC.setVisible(false);
    }
    
}
