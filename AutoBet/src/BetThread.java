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
			while(true){
				long CQSSCremainTime = dsnHttp.getCQSSCRemainTime();
				long BJSCremainTime = dsnHttp.getBJSCRemainTime();
				if(CQSSCremainTime > 10*60*1000 || BJSCremainTime > 10*60*1000){//��ȡʱ��ʧ��
					while(dsnHttp.loginToDsn() == false); //���µ�¼
					CQSSCremainTime = dsnHttp.getCQSSCRemainTime();
					BJSCremainTime = dsnHttp.getBJSCRemainTime();
				}
				
				System.out.println("��������ʱʱ�ʷ���ʱ��Ϊ:");
				System.out.println(CQSSCremainTime/1000);
				
				System.out.println("���뱱����������ʱ��Ϊ:");
				System.out.println(BJSCremainTime/1000);
				
				boolean timeTobetCQSSC = CQSSCremainTime <= betRemainTime && CQSSCremainTime >=0;
				boolean timeTobetBJSC = BJSCremainTime <= betRemainTime && BJSCremainTime >=0;

				if((betCQSSC || betOppositeCQSSC)&&timeTobetCQSSC){//���ʮ������ȥ��ע
					String data = DsnProxyGrab.grabCQSSCdata("LM", "XZ", "");
					if(data != null && data.equals("timeout")){//��ʱ�����»�ȡ����
						data = DsnProxyGrab.grabCQSSCdata("LM", "XZ", "");
					}
					if(data == null || data.equals("timeout")){//��ȡ����ʧ��
						while(DsnProxyGrab.doLogin() == false); //��������
						data = DsnProxyGrab.grabCQSSCdata("LM", "XZ", "");
					}
					String[] betData = {data};
					System.out.println("�µ����ݣ�");
					System.out.println(data);
					if(data != null){
					autoBetSuccess = dsnHttp.doBetCQSSC(betData, betCQSSCPercent, betOppositeCQSSC);
					}
				}
				
				if((betBJSC || betOppositeBJSC)&&timeTobetBJSC){
					//����
					String dataGY = DsnProxyGrab.grabBJSCdata("GY", "XZ", "");		
					if(dataGY != null && dataGY.equals("timeout")){//��ʱ�����»�ȡ����
						dataGY = DsnProxyGrab.grabBJSCdata("GY", "XZ", "");
					}
					if(dataGY == null || dataGY.equals("timeout")){//��ȡ����ʧ��
						while(DsnProxyGrab.doLogin() == false); //��������
						dataGY = DsnProxyGrab.grabBJSCdata("GY", "XZ", "");
					}
					
					//��������
					String dataSSWL = DsnProxyGrab.grabBJSCdata("SSWL", "XZ", "");
					if(dataSSWL != null && dataSSWL.equals("timeout")){//��ʱ�����»�ȡ����
						dataSSWL = DsnProxyGrab.grabBJSCdata("SSWL", "XZ", "");
					}
					if(dataSSWL == null || dataSSWL.equals("timeout")){//��ȡ����ʧ��
						while(DsnProxyGrab.doLogin() == false); //��������
						dataSSWL = DsnProxyGrab.grabBJSCdata("SSWL", "XZ", "");
					}
					
					//�߰˾�ʮ
					String dataQBJS = DsnProxyGrab.grabBJSCdata("QBJS", "XZ", "");
					if(dataQBJS != null && dataQBJS.equals("timeout")){//��ʱ�����»�ȡ����
						dataQBJS = DsnProxyGrab.grabBJSCdata("QBJS", "XZ", "");
					}
					if(dataQBJS == null || dataQBJS.equals("timeout")){//��ȡ����ʧ��
						while(DsnProxyGrab.doLogin() == false); //��������
						dataQBJS = DsnProxyGrab.grabBJSCdata("QBJS", "XZ", "");
					}
					
					
					String[] betBJSCData = {dataGY, dataSSWL, dataQBJS};
					
					System.out.println("�µ����ݣ�");
					System.out.println(dataGY);
					System.out.println(dataSSWL);
					System.out.println(dataQBJS);
					
					if(dataGY != null && dataSSWL != null && dataQBJS != null){
					autoBetSuccess = dsnHttp.doBetBJSC(betBJSCData, betBJSCPercent, betOppositeBJSC);
					}
				}


					
				String data = DsnProxyGrab.grabCQSSCdata("LM", "XZ", "");  //�������������������
				if(data == null || data.length() <= 0){//��ȡ����ʧ��
					while(DsnProxyGrab.doLogin() == false); //��������
					data = DsnProxyGrab.grabCQSSCdata("LM", "XZ", "");
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
