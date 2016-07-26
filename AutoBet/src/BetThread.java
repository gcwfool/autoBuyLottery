class BetThread extends Thread{
    
    
	//long betRemainTime = 15*1000;  //����������ʱ������ע
	
	long almostTime = 25*1000;  //�������һ��sleep�����ʱ��
	
	long sleepTime = 10*1000;	//ƽʱˮ�ʱ��
	
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
	    	
			while(true){
				long CQSSCremainTime = dsnHttp.getCQSSCRemainTime();
				long BJSCremainTime = dsnHttp.getBJSCRemainTime();
				if(CQSSCremainTime > 10*60*1000 || BJSCremainTime > 10*60*1000){//��ȡʱ��ʧ��
					if(dsnHttp.login() == false)
						;//todo
					CQSSCremainTime = dsnHttp.getCQSSCRemainTime();
					BJSCremainTime = dsnHttp.getBJSCRemainTime();
				}
				
				System.out.println("��������ʱʱ�ʷ���ʱ��Ϊ:");
				System.out.println(CQSSCremainTime/1000);
				
				System.out.println("���뱱����������ʱ��Ϊ:");
				System.out.println(BJSCremainTime/1000);
				
				boolean timeTobetCQSSC = CQSSCremainTime <= betRemainTime && CQSSCremainTime >=0;
				boolean timeTobetBJSC = BJSCremainTime <= betRemainTime && BJSCremainTime >=0;


				//ÿ����һ����������
				if(!timeTobetCQSSC && !timeTobetBJSC && (CQSSCremainTime <= 90*1000) && getCQSSCOddsData == false){
					
					dsnHttp.getCQSSCoddsData();
					getCQSSCOddsData = true;
					
				}
				
				if(!timeTobetCQSSC && !timeTobetBJSC && (BJSCremainTime <= 90*1000) && getBJSCOddsData == false){
					
					dsnHttp.getBJSCoddsData();
					getBJSCOddsData = true;
					
				}
				
				if((betCQSSC || betOppositeCQSSC)&&timeTobetCQSSC){//���ʮ������ȥ��ע

					String[] betCQSSCData = null;
					
					while((betCQSSCData = DsnProxyGrab.getCQSSCdata()) == null){
						
					}					

					
					if(betCQSSCData[0].equals(dsnHttp.getCQSSCdrawNumber())){
						
						String[] betsData = {betCQSSCData[1]};
						
						autoBetSuccess = dsnHttp.doBetCQSSC(betsData, betCQSSCPercent, betOppositeCQSSC);
						
						if(autoBetSuccess == true)
							getCQSSCOddsData = false;
						
						System.out.println("�µ����ݣ�");
						System.out.println(betCQSSCData[1]);
					}
					
					

				}
				
				if((betBJSC || betOppositeBJSC)&&timeTobetBJSC){
					
					String[] betBJSCData;
					
					while((betBJSCData = DsnProxyGrab.getBJSCdata()) == null){
						
					}	

					if(betBJSCData[0].equals(dsnHttp.getBJSCdrawNumber())){
						String[] betsData = {betBJSCData[1], betBJSCData[2], betBJSCData[3]};
						autoBetSuccess = dsnHttp.doBetBJSC(betsData, betBJSCPercent, betOppositeBJSC);
						
						if(autoBetSuccess == true)
							getBJSCOddsData = false;
						
						System.out.println("�µ����ݣ�");
						System.out.println(betBJSCData[1]);
						System.out.println(betBJSCData[2]);
						System.out.println(betBJSCData[3]);
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
