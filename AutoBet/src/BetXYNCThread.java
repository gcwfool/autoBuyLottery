import java.util.Vector;
class BetXYNCThread extends Thread{
    
    
	//long betRemainTime = 15*1000;  //����������ʱ������ע
	
	long almostTime = 25*1000;  //�������һ��sleep�����ʱ��
	
	long sleepTime = 10*1000;	//ƽʱˮ�ʱ��
	boolean requestTime = true;
	
    static double betXYNCPercent = 1.0;
    
    static long betRemainTime = 10 * 1000;
    
    static boolean betXYNC = false;
    static boolean betOppositeXYNC = false;

    
    
    static boolean clearXYNCdetaisData = false;
    
    Client client;
    
    public BetXYNCThread(Client client){
    	this.client = client;
    }
    
    
    @Override
    public void run() {
    	try{
	    	boolean autoBetSuccess = false;
	    	
			boolean getXYNCOddsData = false;
			
			
			//�������ƴ�ӡ���������������µ����ݲ�ֵ�ı���
			boolean printXYNCErrorValue = false;

	    	
			while(true){
				long XYNCremainTime = 0;
				
				if(requestTime) {
					XYNCremainTime = BetXYNCManager.getXYNCremainTime();
					
					if(XYNCremainTime > 10*60*1000){//��ȡʱ��ʧ��
						

						XYNCremainTime = BetXYNCManager.getXYNClocalRemainTime();
					}
					
					System.out.println("[��˹���Ա]��������ũ������ʱ��Ϊ:");
					System.out.println(XYNCremainTime/1000);			
					
					if((XYNCremainTime > 0 && XYNCremainTime <= 40*1000)) {//����������̲������󣬻�ȡ����ʱ��
						requestTime = false;
					}
					
				} else {		
					XYNCremainTime = BetXYNCManager.getXYNClocalRemainTime();
					
					if(autoBetSuccess == false){
						System.out.println("[��˹���Ա]��������ũ������ʱ��Ϊ[local]:");
						System.out.println(XYNCremainTime/1000);	
					}
				

					
					if((XYNCremainTime <= 0 || XYNCremainTime > 40*1000) ) {
						requestTime = true;
					}
				}
				
				if(XYNCremainTime < 0) {
					getXYNCOddsData = false;
					autoBetSuccess = false;
				}
				
				
				
				boolean timeTobetXYNC = XYNCremainTime <= betRemainTime && XYNCremainTime > 1;



				//ÿ����һ����������
				if(!timeTobetXYNC && (XYNCremainTime <= 90*1000) && XYNCremainTime > 0 && getXYNCOddsData == false){
					String res = BetXYNCManager.getXYNCoddsData();
					if(res != null)
						getXYNCOddsData = true;
					
				}
				
				
				

				
				
				
				
				

				
				
				if((betXYNC || betOppositeXYNC)&&timeTobetXYNC){//���ʮ������ȥ��ע
					
					clearXYNCdetaisData = false;
					
					
					String[] betXYNCData = null;
					
					for(int i = 0; i < 4; i++) {
						if((betXYNCData = client.getXYNCdata()) == null){
							Thread.currentThread().sleep(1*1000);
						}
						else {
							break;
						}
					}
					
					if(betXYNCData == null) {
						System.out.println("[��˹���Ա]�µ�ʧ��,δ��ȡ���µ�����");
					} else if(betXYNCData != null &&betXYNCData[0].equals(BetXYNCManager.getXYNCdrawNumber()) && autoBetSuccess == false) {
						
						String[] betsData = {betXYNCData[1], betXYNCData[2],betXYNCData[3],betXYNCData[4],betXYNCData[5],betXYNCData[6],betXYNCData[7],betXYNCData[8],betXYNCData[9]};
						
						System.out.println("[��˹���Ա]����ũ���µ����ݣ�");
						System.out.println(betXYNCData[1]);
						
						
						
						autoBetSuccess = BetXYNCManager.doBetXYNC(betsData, betXYNCPercent, betOppositeXYNC, betXYNCData[10]);
						
						if(autoBetSuccess == true){
							//dsnHttp.setCQSSCBetData(betsData);
						}
						
						printXYNCErrorValue = false;
						
					}
					
					

				}
				



				sleepTime = 10*1000;
				
				
				
				if(dsnHttp.isAllLotteryIdle()){
					
					
					
					if(BetXYNCManager.getUnCalcProfitBJSCDraw().size() != 0){
						
						Vector<String> calcedXYNCDraw = new Vector<String>();
						
						Vector<String> data = BetXYNCManager.getUnCalcProfitBJSCDraw();
						for(int i = 0; i < data.size(); i++){
							String drawNumber = data.elementAt(i);
							
							
							
							String[] profitandbishu = dsnHttp.getBetProfit(drawNumber, BetType.XYNC);
							if(profitandbishu[0].equals("none") != true){
								BetXYNCManager.updateXYNCWindowdetailsData(drawNumber, TYPEINDEX.STATC.ordinal(), "0");
								BetXYNCManager.updateXYNCWindowdetailsData(drawNumber, TYPEINDEX.PROFIT.ordinal(), profitandbishu[0]);
								
								BetXYNCManager.updateXYNCWindowdetailsData(drawNumber, TYPEINDEX.COUNT.ordinal(), profitandbishu[1]);
								
								calcedXYNCDraw.add(drawNumber);
							}
							
						}
						
						if(calcedXYNCDraw.size() != 0){
							BetXYNCManager.updateUnCalcXYNCDraw(calcedXYNCDraw);
						}
						
						
					}
					
					
					String balance = dsnHttp.getBalance();
					
					BetXYNCManager.updateXYNCBalance(balance);

					
				}
				
				if(dsnHttp.isInFreetime(System.currentTimeMillis()) == true && clearXYNCdetaisData == false){
					BetXYNCManager.clearXYNCdetalsData();
					clearXYNCdetaisData = true;
				}

				
				

				
				
				
				
				if(XYNCremainTime <= almostTime){					

					long time2 = XYNCremainTime - betRemainTime;
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
    
    public static void setXYNCBetPercent(double percent){
    	betXYNCPercent = percent;
    }
    

}
