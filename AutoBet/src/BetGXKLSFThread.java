import java.util.Vector;
class BetGXKLSFThread extends Thread{
    
    
	//long betRemainTime = 15*1000;  //����������ʱ������ע
	
	long almostTime = 25*1000;  //�������һ��sleep�����ʱ��
	
	long sleepTime = 10*1000;	//ƽʱˮ�ʱ��
	boolean requestTime = true;
	
    static double betGXKLSFPercent = 1.0;
    
    static long betRemainTime = 10 * 1000;
    
    static boolean betGXKLSF = false;
    static boolean betOppositeGXKLSF = false;

    
    
    static boolean clearGXKLSFdetaisData = false;
    
    Client client;
    
    public BetGXKLSFThread(Client client){
    	this.client = client;
    }
    
    
    @Override
    public void run() {
    	try{
	    	boolean autoBetSuccess = false;
	    	
			boolean getGXKLSFOddsData = false;
			
			
			//�������ƴ�ӡ���������������µ����ݲ�ֵ�ı���
			boolean printGXKLSFErrorValue = false;

	    	
			while(true){
				long GXKLSFremainTime = 0;
				
				if(requestTime) {
					GXKLSFremainTime = BetGXKLSFManager.getGXKLSFremainTime();
					
					if(GXKLSFremainTime > 10*60*1000){//��ȡʱ��ʧ��
						

						GXKLSFremainTime = BetGXKLSFManager.getGXKLSFlocalRemainTime();
					}
					
					System.out.println("[��˹���Ա]�����������ʮ�ַ���ʱ��Ϊ:");
					System.out.println(GXKLSFremainTime/1000);			
					
					if((GXKLSFremainTime > 0 && GXKLSFremainTime <= 40*1000)) {//����������̲������󣬻�ȡ����ʱ��
						requestTime = false;
					}
					
				} else {		
					GXKLSFremainTime = BetGXKLSFManager.getGXKLSFlocalRemainTime();
					
					
					System.out.println("[��˹���Ա]�����������ʮ�ַ���ʱ��Ϊ[local]:");
					System.out.println(GXKLSFremainTime/1000);					

					
					if((GXKLSFremainTime <= 0 || GXKLSFremainTime > 40*1000) ) {
						requestTime = true;
					}
				}
				
				if(GXKLSFremainTime < 0) {
					getGXKLSFOddsData = false;
				}
				
				
				
				boolean timeTobetGXKLSF = GXKLSFremainTime <= betRemainTime && GXKLSFremainTime > 1;



				//ÿ����һ����������
				if(!timeTobetGXKLSF && (GXKLSFremainTime <= 90*1000) && GXKLSFremainTime > 0 && getGXKLSFOddsData == false){
					BetGXKLSFManager.getGXKLSFoddsData();
					getGXKLSFOddsData = true;
					
				}
				
				
				

				
				
				
				
				

				
				
				if((betGXKLSF || betOppositeGXKLSF)&&timeTobetGXKLSF&&false){//���ʮ������ȥ��ע
					
					clearGXKLSFdetaisData = false;
					
					
					String[] betGXKLSFData = null;
					
					for(int i = 0; i < 4; i++) {
						//todo
						if((betGXKLSFData = client.getXYNCdata()) == null){
							Thread.currentThread().sleep(1*1000);
						}
						else {
							break;
						}
					}
					
					if(betGXKLSFData == null) {
						System.out.println("[��˹���Ա]�µ�ʧ��,δ��ȡ���µ�����");
					} else if(betGXKLSFData != null &&betGXKLSFData[0].equals(BetGXKLSFManager.getGXKLSFdrawNumber())) {
						
						String[] betsData = {betGXKLSFData[1], betGXKLSFData[2],betGXKLSFData[3],betGXKLSFData[4],betGXKLSFData[5],betGXKLSFData[6],betGXKLSFData[7],betGXKLSFData[8],betGXKLSFData[9]};
						
/*						System.out.println("[��˹���Ա]�µ����ݣ�");
						System.out.println(betGXKLSFData[1]);*/
						
						
						
						autoBetSuccess = BetGXKLSFManager.doBetGXKLSF(betsData, betGXKLSFPercent, betOppositeGXKLSF, betGXKLSFData[10]);
						
						if(autoBetSuccess == true){
							//dsnHttp.setCQSSCBetData(betsData);
						}
						
						printGXKLSFErrorValue = false;
						
					}
					
					

				}
				



				sleepTime = 10*1000;
				
				
				
				if(dsnHttp.isAllLotteryIdle()){
					
					
					
					if(BetGXKLSFManager.getUnCalcProfitBJSCDraw().size() != 0){
						
						Vector<String> calcedGXKLSFDraw = new Vector<String>();
						
						Vector<String> data = BetGXKLSFManager.getUnCalcProfitBJSCDraw();
						for(int i = 0; i < data.size(); i++){
							String drawNumber = data.elementAt(i);
							
							
							
							String[] profitandbishu = dsnHttp.getBetProfit(drawNumber, BetType.GXKLSF);
							if(profitandbishu[0].equals("none") != true){
								BetGXKLSFManager.updateGXKLSFWindowdetailsData(drawNumber, TYPEINDEX.STATC.ordinal(), "0");
								BetGXKLSFManager.updateGXKLSFWindowdetailsData(drawNumber, TYPEINDEX.PROFIT.ordinal(), profitandbishu[0]);
								
								BetGXKLSFManager.updateGXKLSFWindowdetailsData(drawNumber, TYPEINDEX.COUNT.ordinal(), profitandbishu[1]);
								
								calcedGXKLSFDraw.add(drawNumber);
							}
							
						}
						
						if(calcedGXKLSFDraw.size() != 0){
							BetGXKLSFManager.updateUnCalcGXKLSFDraw(calcedGXKLSFDraw);
						}
						
						
					}
					
					
					String balance = dsnHttp.getBalance();
					
					BetGXKLSFManager.updateGXKLSFBalance(balance);

					
				}
				
				if(dsnHttp.isInFreetime(System.currentTimeMillis()) == true && clearGXKLSFdetaisData == false){
					BetGXKLSFManager.clearGXKLSFdetalsData();
					clearGXKLSFdetaisData = true;
				}

				
				

				
				
				
				
				if(GXKLSFremainTime <= almostTime){					

					long time2 = GXKLSFremainTime - betRemainTime;
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
    
    public static void setGXKLSFBetPercent(double percent){
    	betGXKLSFPercent = percent;
    }
    

}
