package dsn;
import java.util.Vector;
class BetThread extends Thread{
    
    
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
    
    
    static boolean clearBJSCdetaisData = false;
    
    Client client;
    
    public BetThread(Client client){
    	this.client = client;
    }
    
    
    @Override
    public void run() {
    	try{
	    	boolean autoBetSuccess = false;
	    	
			boolean getCQSSCOddsData = false;
			boolean getBJSCOddsData = false;
			
			
			//�������ƴ�ӡ�������������µ���ݲ�ֵ�ı���
			boolean printBJSCErrorValue = false;
			boolean printCQSSCErrorValue = false;

	    	
			while(true){
				
				if(dsnHttp.isInRelogin == true)
					continue;
				
				long CQSSCremainTime = 0;
				long BJSCremainTime = 0;
				if(requestTime) {
					CQSSCremainTime = dsnHttp.getCQSSCRemainTime();
					BJSCremainTime = dsnHttp.getBJSCRemainTime();
					if(CQSSCremainTime > 10*60*1000){//��ȡʱ��ʧ��					
						CQSSCremainTime = dsnHttp.getCQSSClocalRemainTime();
					}
					
					if(BJSCremainTime > 10*60*1000){//��ȡʱ��ʧ��
						BJSCremainTime = dsnHttp.getBJSClocalRemainTime();
					}
					
					System.out.println("[迪斯尼会员]距离重庆时时彩封盘时间为:");
					System.out.println(CQSSCremainTime/1000);			
					System.out.println("[迪斯尼会员]距离北京赛车封盘时间为:");
					System.out.println(BJSCremainTime/1000);
					
					if((CQSSCremainTime > 0 && CQSSCremainTime <= 40*1000) || (BJSCremainTime > 0 && BJSCremainTime <= 40*1000)) {//������̲������󣬻�ȡ����ʱ��
						requestTime = false;
					}
					
				} else {		
					CQSSCremainTime = dsnHttp.getCQSSClocalRemainTime();
					BJSCremainTime = dsnHttp.getBJSClocalRemainTime();
					
					System.out.println("[迪斯尼会员]距离重庆时时彩封盘时间为:[local]:");
					System.out.println(CQSSCremainTime/1000);					
					System.out.println("[迪斯尼会员]距离北京赛车封盘时间为::");
					System.out.println(BJSCremainTime/1000);
					
					if((CQSSCremainTime <= 0 || CQSSCremainTime > 40*1000) && (BJSCremainTime <= 0 || BJSCremainTime > 40*1000)) {
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


				//ÿ����һ���������
				if(!timeTobetCQSSC && !timeTobetBJSC && (CQSSCremainTime <= 90*1000) && CQSSCremainTime > 0 && getCQSSCOddsData == false){
					String res = dsnHttp.getCQSSCoddsData();
					if(res != null)
						getCQSSCOddsData = true;
					
				}
				
				if(!timeTobetCQSSC && !timeTobetBJSC && (BJSCremainTime <= 90*1000) && BJSCremainTime > 0 && getBJSCOddsData == false){
					String res = dsnHttp.getBJSCoddsData();
					if(res != null)
						getBJSCOddsData = true;
					
				}
				
				
				//���µ�¼

				
				
				//����·
				if(dsnHttp.isAllLotteryIdle()){

					if(dsnHttp.getUnCalcProfitBJSCDraw().size() != 0){
						
						Vector<String> calcedBJSCDraw = new Vector<String>();
						
						Vector<String> data = dsnHttp.getUnCalcProfitBJSCDraw();
						for(int i = 0; i < data.size(); i++){
							String drawNumber = data.elementAt(i);
							
							
							
							String[] profitandbishu = dsnHttp.getBetProfit(drawNumber, BetType.BJSC);
							if(profitandbishu[0].equals("none") != true){
								dsnHttp.updateBJSCWindowdetailsData(drawNumber, TYPEINDEX.STATC.ordinal(), "0");
								dsnHttp.updateBJSCWindowdetailsData(drawNumber, TYPEINDEX.PROFIT.ordinal(), profitandbishu[0]);
								
								dsnHttp.updateBJSCWindowdetailsData(drawNumber, TYPEINDEX.COUNT.ordinal(), profitandbishu[1]);
								
								calcedBJSCDraw.add(drawNumber);
							}
							
						}
						
						if(calcedBJSCDraw.size() != 0){
							dsnHttp.updateUnCalcBJSCDraw(calcedBJSCDraw);
						}
						
						
					}
					
					
					
					if(dsnHttp.getUnCalcProfitCQSSCDraw().size() != 0){
						
						Vector<String> calcedCQSSCDraw = new Vector<String>();
						
						Vector<String> data = dsnHttp.getUnCalcProfitCQSSCDraw();
						for(int i = 0; i < data.size(); i++){
							String drawNumber = data.elementAt(i);
							
							
							
							String[] profitandbishu = dsnHttp.getBetProfit(drawNumber, BetType.CQSSC);
							if(profitandbishu[0].equals("none") != true){
								dsnHttp.updateCQSSCWindowdetailsData(drawNumber, TYPEINDEX.STATC.ordinal(), "0");
								dsnHttp.updateCQSSCWindowdetailsData(drawNumber, TYPEINDEX.PROFIT.ordinal(), profitandbishu[0]);
								
								dsnHttp.updateBJSCWindowdetailsData(drawNumber, TYPEINDEX.COUNT.ordinal(), profitandbishu[1]);
								
								calcedCQSSCDraw.add(drawNumber);
							}
							
						}
						
						if(calcedCQSSCDraw.size() != 0){
							dsnHttp.updateUnCalcCQSSCDraw(calcedCQSSCDraw);
						}
						
						
					}
					
					
					//�����
					
					String balance = dsnHttp.getBalance();
					
					dsnHttp.updateBJSCBalance(balance);
					
					dsnHttp.updateCQSSCBalance(balance);
										
				}
				
				
				if(dsnHttp.isInFreetime(System.currentTimeMillis()) == true && clearBJSCdetaisData == false){
					dsnHttp.clearBJSCdetalsData();
					dsnHttp.clearCQSSCdetalsData();
					clearBJSCdetaisData = true;
				}
				
				
				

				
				
				if((betCQSSC || betOppositeCQSSC)&&timeTobetCQSSC){//���ʮ������ȥ��ע
					
					clearBJSCdetaisData = false;

					
					
					String[] betCQSSCData = null;
					
					for(int i = 0; i < 4; i++) {
						if((betCQSSCData = client.getCQSSCdata()) == null){
							Thread.currentThread().sleep(1*1000);
						}
						else {
							break;
						}
					}
					
					if(betCQSSCData == null) {
						System.out.println("[迪斯尼会员]获取重庆时时彩下单数据失败");
					} else if(betCQSSCData != null &&betCQSSCData[0].equals(dsnHttp.getCQSSCdrawNumber())) {
						
						String[] betsData = {betCQSSCData[1]};
						
						System.out.println("重庆时时彩下单数据：");
						System.out.println(betCQSSCData[1]);
						
						
						
						autoBetSuccess = dsnHttp.doBetCQSSC(betsData, betCQSSCPercent, betOppositeCQSSC, betCQSSCData[2]);
						
						if(autoBetSuccess == true){
							dsnHttp.setCQSSCBetData(betsData);
						}
						
						//todo remove
						autoBetSuccess = true;
						
						printCQSSCErrorValue = false;
						
					}else if(!betCQSSCData[0].equals(dsnHttp.getCQSSCdrawNumber())){
						System.out.println("重庆时时彩下单数据错误\n");
						
						//System.out.printf("服务器期数：%d， 本地期数:%d\n", betCQSSCData[0], dsnHttp.getCQSSCdrawNumber());
						
						
					}
					
					

				}
				
				if((betBJSC || betOppositeBJSC)&&timeTobetBJSC){
					
					
					
					String[] betBJSCData = null;
					
					for(int i = 0; i < 4; i++) {
						if((betBJSCData = client.getBJSCdata()) == null) {
							Thread.currentThread().sleep(1*1000);
						}else {
							break;
						}
					}
					
					if(betBJSCData == null) {
						System.out.println("获取北京赛车下单数据失败");
					} else if(betBJSCData[0].equals(dsnHttp.getBJSCdrawNumber())){
						String[] betsData = {betBJSCData[1], betBJSCData[2], betBJSCData[3]};
						
						System.out.println("北京赛车下单数据");
						System.out.println(betBJSCData[1]);
						System.out.println(betBJSCData[2]);
						System.out.println(betBJSCData[3]);
						
						
						
						autoBetSuccess = dsnHttp.doBetBJSC(betsData, betBJSCPercent, betOppositeBJSC, betBJSCData[4]);
						
						if(autoBetSuccess == true){
							dsnHttp.setBJSCBetData(betsData);
						}
						
						//todo remove
						autoBetSuccess = true;
						
						printBJSCErrorValue = false;
					
					}else if(!betBJSCData[0].equals(dsnHttp.getBJSCdrawNumber())){
						
						System.out.println("重庆时时彩下单数据错误\n");
						
						//System.out.printf("服务器期数：%d， 本地期数:%d\n", betBJSCData[0], dsnHttp.getBJSCdrawNumber());
						
						
					}

				}


				sleepTime = 10*1000;
				
				
				//��ӡ����������µ���ݲ��
				if(BJSCremainTime < -2*1000 && printBJSCErrorValue == false && dsnHttp.previousBJSCBetResult == true && dsnHttp.previousBJSCBetNumber.equals(dsnHttp.BJSCdrawNumber)){
					
					
					
					String data[] = null;

					
					for(int i = 0; i < 4; i++) {
						
						data = client.getBJSCdata();


						if(data == null) {
							data = null;
							Thread.currentThread().sleep(1*1000);
						}else {
							break;
						}
					}
					
					

					
					
					
					if(data[1] == null || data[2] == null || data[3] == null) {
						System.out.println("[��˹���Ա]δ��ȡ���������");
					}else{
						String[] betsData = {data[1], data[2], data[3]};
						
						System.out.println("�����������ݣ�");
						System.out.println(betsData[0]);
						System.out.println(betsData[1]);
						System.out.println(betsData[2]);

						int res = dsnHttp.calcBetDataErrorValue(betsData, BetType.BJSC);

						dsnHttp.updateBJSCWindowdetailsData(dsnHttp.previousBJSCBetNumber, TYPEINDEX.DVALUE.ordinal(), Integer.toString(res));
					}
					
					printBJSCErrorValue = true;
					
				}
				
				
				
				if(CQSSCremainTime < -2*1000 && printCQSSCErrorValue == false && dsnHttp.previousCQSSCBetResult == true && dsnHttp.previousCQSSCBetNumber.equals(dsnHttp.CQSSCdrawNumber)){
					
					String data[] = null;
					
					for(int i = 0; i < 4; i++) {
						
						System.out.println("����client��get����ʼ");
						
						data = client.getCQSSCdata();
						
						System.out.println("����client��get�������");
						
						if( data == null) {
							data = null;
							Thread.currentThread().sleep(1*1000);
						}else {
							
							break;
						}
					}
					
					if(data == null) {
						System.out.println("[��˹���Ա]δ��ȡ���������");
					}else{
		
						System.out.println("����ʱʱ�ʷ�����ݣ�");
						System.out.println(data);

						String[] betData = {data[1]};
						
						int res = dsnHttp.calcBetDataErrorValue(betData, BetType.CQSSC);
						
						dsnHttp.updateCQSSCWindowdetailsData(dsnHttp.previousCQSSCBetNumber, TYPEINDEX.DVALUE.ordinal(), Integer.toString(res));

						
					}
					
					printCQSSCErrorValue = true;
					
				}
				
				
				
				
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
