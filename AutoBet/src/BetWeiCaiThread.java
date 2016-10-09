
public class BetWeiCaiThread extends Thread{
    
    
	//long betRemainTime = 15*1000;  //����������ʱ������ע
	
	long almostTime = 25*1000;  //�������һ��sleep�����ʱ��
	
	long sleepTime = 10*1000;	//ƽʱˮ�ʱ��
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

	    	
			while(true){
				long CQSSCremainTime = 0;
				long BJSCremainTime = 0;
				if(requestTime) {
					String getCQSSCmarketDataResult = "";	
					String getBJSCmarketDataResult = "";	
					
					String result = "";
					
					if(WeiCaiHttp.isCQSSCselectionTypeIdListEmpty() == true){
						WeiCaiHttp.getCQSSCselectionTypeIdListData();
					}
					
					if(WeiCaiHttp.isBJSCselectionTypeIdListEmpty() == true){
						result = WeiCaiHttp.getBJSCselectionTypeIdListData();
					}

					if(result.contains("�����µ�¼") == true){
						WeiCaiHttp.login();
					}
					
					
					for(int i = 0; i< 3; i++){
						getCQSSCmarketDataResult = WeiCaiHttp.getCQSSCmarketData();
						if(getCQSSCmarketDataResult.contains("�����µ�¼") == true){
							WeiCaiHttp.login();
						}
						else if(getCQSSCmarketDataResult.contains("true") == true){
							break;
						}
					}
					
					for(int i = 0; i< 3; i++){
						getBJSCmarketDataResult = WeiCaiHttp.getBJSCmarketData();
						if(getBJSCmarketDataResult.contains("�����µ�¼")== true){
							WeiCaiHttp.login();
						}
						else if(getBJSCmarketDataResult.contains("true") == true){
							break;
						}
					}


					if(getCQSSCmarketDataResult.contains("true") == true){
						CQSSCremainTime = WeiCaiHttp.getCQSSCRemainTime();
					}
					else{
						CQSSCremainTime = WeiCaiHttp.getCQSSClocalRemainTime();
					}
						
					if(getBJSCmarketDataResult.contains("true") == true){
						BJSCremainTime = WeiCaiHttp.getBJSCRemainTime();
					}
					else{
						BJSCremainTime = WeiCaiHttp.getBJSClocalRemainTime();
					}
					


					
					System.out.println("[΢��]��������ʱʱ�ʷ���ʱ��Ϊ:");
					System.out.println(CQSSCremainTime/1000);			
					System.out.println("[΢��]���뱱����������ʱ��Ϊ:");
					System.out.println(BJSCremainTime/1000);
					
					if((CQSSCremainTime > 0 && CQSSCremainTime <= 40*1000) || (BJSCremainTime > 0 && BJSCremainTime <= 40*1000)) {//����������̲������󣬻�ȡ����ʱ��
						requestTime = false;
					}
					
				} else {		
					CQSSCremainTime = WeiCaiHttp.getCQSSClocalRemainTime();
					BJSCremainTime = WeiCaiHttp.getBJSClocalRemainTime();
					
					System.out.println("[΢��]��������ʱʱ�ʷ���ʱ��Ϊ[local]:");
					System.out.println(CQSSCremainTime/1000);					
					System.out.println("[΢��]���뱱����������ʱ��Ϊ[local]:");
					System.out.println(BJSCremainTime/1000);
					
					if((CQSSCremainTime <= 0 || CQSSCremainTime > 40*1000) && (BJSCremainTime <= 0 || BJSCremainTime > 40*1000)) {
						requestTime = true;
					}
				}
				
				boolean timeTobetCQSSC = CQSSCremainTime <= betRemainTime && CQSSCremainTime > 1;
				boolean timeTobetBJSC = BJSCremainTime <= betRemainTime && BJSCremainTime > 1;



				
				if((betCQSSC || betOppositeCQSSC)&&timeTobetCQSSC){//���ʮ������ȥ��ע

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
						System.out.println("�µ�ʧ��,δ��ȡ���µ�����");
					} else if(betCQSSCData != null &&betCQSSCData[0].equals(WeiCaiHttp.getCQSSCdrawNumber())) {
						
						String[] betsData = {betCQSSCData[1]};
						
						System.out.println("�µ����ݣ�");
						System.out.println(betCQSSCData[1]);
						
						autoBetSuccess = WeiCaiHttp.doBetCQSSC(betsData, betCQSSCPercent, betOppositeCQSSC, betCQSSCData[2]);
						
						if(autoBetSuccess == true) {
							getCQSSCOddsData = false;
						}
						
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
						System.out.println("δ��ȡ���µ�����");
					} else if(betBJSCData[0].equals(WeiCaiHttp.getBJSCdrawNumber())){
						String[] betsData = {betBJSCData[1], betBJSCData[2], betBJSCData[3]};
						
						System.out.println("�µ����ݣ�");
						System.out.println(betBJSCData[1]);
						System.out.println(betBJSCData[2]);
						System.out.println(betBJSCData[3]);
						autoBetSuccess = WeiCaiHttp.doBetBJSC(betsData, betBJSCPercent, betOppositeBJSC, betBJSCData[4]);
						
						if(autoBetSuccess == true) {
							getBJSCOddsData = false;
						}
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
