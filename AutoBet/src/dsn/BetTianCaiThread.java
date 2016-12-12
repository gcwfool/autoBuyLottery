package dsn;
public class BetTianCaiThread extends Thread{
    
    
	//long betRemainTime = 15*1000;  //����������ʱ������ע
	
	long almostTime = 25;  //�������һ��sleep�����ʱ��
	
	long sleepTime = 10;	//ƽʱ˯��ʱ��
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
					while(CQSSCremainTime == -9999 || BJSCremainTime == -9999){//��ȡʱ��ʧ��
						if(tianCaiHttp.login() == false) {
							//todo
							return;
						}
						CQSSCremainTime = tianCaiHttp.getCQSSCRemainTime();
						BJSCremainTime = tianCaiHttp.getBJSCRemainTime();
					}
					
					System.out.println("[���]��������ʱʱ�ʷ���ʱ��Ϊ:");
					System.out.println(CQSSCremainTime);			
					System.out.println("[���]���뱱�������ʱ��Ϊ:");
					System.out.println(BJSCremainTime);
					
					if((CQSSCremainTime > 0 && CQSSCremainTime <= 40) || (BJSCremainTime > 0 && BJSCremainTime <= 40)) {//������̲������󣬻�ȡ����ʱ��
						requestTime = false;
					}
					
				} else {		
					CQSSCremainTime = tianCaiHttp.getCQSSClocalRemainTime();
					BJSCremainTime = tianCaiHttp.getBJSClocalRemainTime();
					
					System.out.println("[���]��������ʱʱ�ʷ���ʱ��Ϊ[local]:");
					System.out.println(CQSSCremainTime);					
					System.out.println("[���]���뱱�������ʱ��Ϊ[local]:");
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

				
				if((betCQSSC || betOppositeCQSSC)&&timeTobetCQSSC){//���ʮ������ȥ��ע

					String[] betCQSSCData = null;
					
					for(int i = 0; i < 4; i++) {
						
					}
					
					if(betCQSSCData == null) {
						System.out.println("�µ�ʧ��,δ��ȡ���µ����");
					} else if(betCQSSCData != null &&betCQSSCData[0].equals(tianCaiHttp.getCQSSCdrawNumber())) {
						
						String[] betsData = {betCQSSCData[1]};
						
						System.out.println("�µ���ݣ�");
						System.out.println(betCQSSCData[1]);
						
						autoBetSuccess = tianCaiHttp.doBetCQSSC(betsData, betCQSSCPercent, betOppositeCQSSC, betCQSSCData[2]);
						
					}
					
					

				}
				
				if((betBJSC || betOppositeBJSC)&&timeTobetBJSC){
					
					String[] betBJSCData = null;
					
					for(int i = 0; i < 4; i++) {
						
					}
					
					if(betBJSCData == null) {
						System.out.println("δ��ȡ���µ����");
					} else if(betBJSCData[0].equals(tianCaiHttp.getBJSCdrawNumber())){
						String[] betsData = {betBJSCData[1], betBJSCData[2], betBJSCData[3]};
						
						System.out.println("�µ���ݣ�");
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
    	
	}//run
    
    public void setCQSSCBetPercent(double percent){
    	betCQSSCPercent = percent;
    }
    
    public void setBJSCBetPercent(double percent){
    	betBJSCPercent = percent;
    }
}
