import java.awt.*;
import java.awt.event.*;


class BetThread extends Thread{
    
    
    double betCQSSCPercent = 1.0;
    @Override
    public void run() {
    	try{
	    	boolean autoBetSuccess = false;
			while(true){
				long CQSSCremainTime = dsnHttp.getCQSSCRemainTime();
				long BJSCremainTime = dsnHttp.getBJSCRemainTime();
				if(CQSSCremainTime > 10*60*1000 || BJSCremainTime > 10*60*1000){//获取时间失败
					while(dsnHttp.loginToDsn() == false); //重新登录
					CQSSCremainTime = dsnHttp.getCQSSCRemainTime();
					BJSCremainTime = dsnHttp.getBJSCRemainTime();
				}
				
				System.out.println("距离重庆时时彩封盘时间为:");
				System.out.println(CQSSCremainTime/1000);
				
				System.out.println("距离北京赛车封盘时间为:");
				System.out.println(BJSCremainTime/1000);
				
				boolean timeTobetCQSSC = CQSSCremainTime <= 15*1000 && CQSSCremainTime >=0;
				boolean timeTobetBJSC = BJSCremainTime <= 15*1000 && BJSCremainTime >=0;

				if(timeTobetCQSSC){//最后十五秒秒去下注
					String data = DsnProxyGrab.grabCQSSCdata("LM", "XZ", "");
					if(data == null ){//获取数据失败
						while(DsnProxyGrab.doLogin() == false); //重新连接
						data = DsnProxyGrab.grabCQSSCdata("LM", "XZ", "");
					}
					String[] betData = {data};
					System.out.println("下单数据：");
					System.out.println(data);
					if(data != null){
					autoBetSuccess = dsnHttp.doBetCQSSC(betData, betCQSSCPercent);
					}
				}
				
				if(timeTobetBJSC){
					String dataGY = DsnProxyGrab.grabBJSCdata("GY", "XZ", "");					
					if(dataGY == null ){//获取数据失败
						while(DsnProxyGrab.doLogin() == false); //重新连接
						dataGY = DsnProxyGrab.grabBJSCdata("GY", "XZ", "");
					}
					
					String dataSSWL = DsnProxyGrab.grabBJSCdata("SSWL", "XZ", "");
					String dataQBJS = DsnProxyGrab.grabBJSCdata("QBJS", "XZ", "");
					
					String[] betBJSCData = {dataGY, dataSSWL, dataQBJS};
					
					System.out.println("下单数据：");
					System.out.println(dataGY);
					System.out.println(dataSSWL);
					System.out.println(dataQBJS);
					
					if(dataGY != null && dataSSWL != null && dataQBJS != null){
					autoBetSuccess = dsnHttp.doBetBJSC(betBJSCData, betCQSSCPercent);
					}
				}


					
				String data = DsnProxyGrab.grabCQSSCdata("LM", "XZ", "");  //请求服务器，保持连接
				if(data == null || data.length() <= 0){//获取数据失败
					while(DsnProxyGrab.doLogin() == false); //重新连接
					data = DsnProxyGrab.grabCQSSCdata("LM", "XZ", "");
				}
				
				long sleepTime = 10*1000;
				
				if(BJSCremainTime <= 25*1000 || CQSSCremainTime <= 25 * 1000){					
					long time1 = BJSCremainTime - 15*1000;
					long time2 = CQSSCremainTime - 15*1000;
					long littleTime = time1<time2?time1:time2;
					if(time1 <0)
						littleTime = time2;
					if(time2 < 0)
						littleTime = time1;
					if(littleTime > 0 && littleTime <= 10*1000)
						sleepTime = littleTime;
				}
				Thread.sleep(sleepTime);

				
	
			}//while
		
    	}catch (InterruptedException e) {
            // TODO: handle exception
        }
	}//run
    
    public void setCQSSCBetPercent(double percent){
    	betCQSSCPercent = percent;
    }
}


public class BetCQSSCListener implements ActionListener
{
	private autoBet ab;
	
	public boolean inBet = false;
	
	public BetCQSSCListener(autoBet ab) {
		this.ab = ab;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(ab.loginSuccess == false || inBet == true){
			return;
		}
		
		BetThread betThread = new BetThread();
		
		String s = ab.textFieldCQSSCBetPercent.getText();
		
		if(Common.isNum(s)){
			double percent = Double.parseDouble(s);
			betThread.setCQSSCBetPercent(percent);
		}
		else{
			//TODO 弹出对话框，提示输入错误
		}
			
				
		betThread.start();
		
		inBet = true;
		
	}	

}