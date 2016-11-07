import java.util.Vector;
class BetXJSSCThread extends Thread{
    
    
	//long betRemainTime = 15*1000;  //����������ʱ������ע
	
	long almostTime = 25*1000;  //�������һ��sleep�����ʱ��
	
	long sleepTime = 10*1000;	//ƽʱˮ�ʱ��
	boolean requestTime = true;
	
    static double betXJSSCPercent = 1.0;
    
    static long betRemainTime = 10 * 1000;
    
    static boolean betXJSSC = false;
    static boolean betOppositeXJSSC = false;

    
    
    static boolean clearXJSSCdetaisData = false;
    
    Client client;
    
    public BetXJSSCThread(Client client){
    	this.client = client;
    }
    
    
    @Override
    public void run() {
    	try{
	    	boolean autoBetSuccess = false;
	    	
			boolean getXJSSCOddsData = false;
			
			
			//�������ƴ�ӡ���������������µ����ݲ�ֵ�ı���
			boolean printXJSSCErrorValue = false;

	    	
			while(true){
				long XJSSCremainTime = 0;
				
				if(requestTime) {
					XJSSCremainTime = BetXJSSCManager.getXJSSCremainTime();
					
					if(XJSSCremainTime > 10*60*1000){//��ȡʱ��ʧ��
						

						XJSSCremainTime = BetXJSSCManager.getXJSSClocalRemainTime();
					}
					
					System.out.println("[��˹���Ա]�����������ʮ�ַ���ʱ��Ϊ:");
					System.out.println(XJSSCremainTime/1000);			
					
					if((XJSSCremainTime > 0 && XJSSCremainTime <= 40*1000)) {//����������̲������󣬻�ȡ����ʱ��
						requestTime = false;
					}
					
				} else {		
					XJSSCremainTime = BetXJSSCManager.getXJSSClocalRemainTime();
					
					if(autoBetSuccess == false){
						System.out.println("[��˹���Ա]�����������ʮ�ַ���ʱ��Ϊ[local]:");
						System.out.println(XJSSCremainTime/1000);		
					}
			
					
					if((XJSSCremainTime <= 0 || XJSSCremainTime > 40*1000) ) {
						requestTime = true;
					}
				}
				
				if(XJSSCremainTime < 0) {
					getXJSSCOddsData = false;
					autoBetSuccess = false;
				}
				
				
				
				boolean timeTobetXJSSC = XJSSCremainTime <= betRemainTime && XJSSCremainTime > 1;



				//ÿ����һ����������
				if(!timeTobetXJSSC && (XJSSCremainTime <= 90*1000) && XJSSCremainTime > 0 && getXJSSCOddsData == false){
					String res = BetXJSSCManager.getXJSSCoddsData();
					if(res != null){
						getXJSSCOddsData = true;
					}
					
					
				}
				
				
				

				
				
				
				
				

				
				
				if((betXJSSC || betOppositeXJSSC)&&timeTobetXJSSC){//���ʮ������ȥ��ע
					
					clearXJSSCdetaisData = false;
					
					
					String[] betXJSSCData = null;
					
					for(int i = 0; i < 4; i++) {
						//todo
						if((betXJSSCData = client.getGXKLdata()) == null){
							Thread.currentThread().sleep(1*1000);
						}
						else {
							break;
						}
					}
					
					if(betXJSSCData == null) {
						System.out.println("[��˹���Ա]�µ�ʧ��,δ��ȡ���µ�����");
					} else if(betXJSSCData != null &&betXJSSCData[0].equals(BetXJSSCManager.getXJSSCdrawNumber()) && autoBetSuccess == false) {
						
						String[] betsData = {betXJSSCData[1]};
						
						System.out.println("[��˹���Ա]��������ʮ���µ����ݣ�");
						System.out.println(betXJSSCData[1]);
						
						
						
						autoBetSuccess = BetXJSSCManager.doBetXJSSC(betsData, betXJSSCPercent, betOppositeXJSSC, betXJSSCData[2]);
						
						if(autoBetSuccess == true){
							//dsnHttp.setCQSSCBetData(betsData);
						}
						
						printXJSSCErrorValue = false;
						
					}
					
					

				}
				



				sleepTime = 10*1000;
				
				
				
				if(dsnHttp.isAllLotteryIdle()){
					
					
					
					if(BetXJSSCManager.getUnCalcProfitBJSCDraw().size() != 0){
						
						Vector<String> calcedXJSSCDraw = new Vector<String>();
						
						Vector<String> data = BetXJSSCManager.getUnCalcProfitBJSCDraw();
						for(int i = 0; i < data.size(); i++){
							String drawNumber = data.elementAt(i);
							
							
							
							String[] profitandbishu = dsnHttp.getBetProfit(drawNumber, BetType.XJSSC);
							if(profitandbishu[0].equals("none") != true){
								BetXJSSCManager.updateXJSSCWindowdetailsData(drawNumber, TYPEINDEX.STATC.ordinal(), "0");
								BetXJSSCManager.updateXJSSCWindowdetailsData(drawNumber, TYPEINDEX.PROFIT.ordinal(), profitandbishu[0]);
								
								BetXJSSCManager.updateXJSSCWindowdetailsData(drawNumber, TYPEINDEX.COUNT.ordinal(), profitandbishu[1]);
								
								calcedXJSSCDraw.add(drawNumber);
							}
							
						}
						
						if(calcedXJSSCDraw.size() != 0){
							BetXJSSCManager.updateUnCalcXJSSCDraw(calcedXJSSCDraw);
						}
						
						
					}
					
					
					String balance = dsnHttp.getBalance();
					
					BetXJSSCManager.updateXJSSCBalance(balance);

					
				}
				
				if(dsnHttp.isInFreetime(System.currentTimeMillis()) == true && clearXJSSCdetaisData == false){
					BetXJSSCManager.clearXJSSCdetalsData();
					clearXJSSCdetaisData = true;
				}

				
				

				
				
				
				
				if(XJSSCremainTime <= almostTime){					

					long time2 = XJSSCremainTime - betRemainTime;
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
    
    public static void setXJSSCBetPercent(double percent){
    	betXJSSCPercent = percent;
    }
    

}
