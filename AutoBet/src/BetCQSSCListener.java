import java.awt.*;
import java.awt.event.*;


class BetThread extends Thread{
    @Override
    public void run() {
    	try{
	    	boolean autoBetSuccess = false;
			while(true){
				long remainTime = dsnHttp.getRemainTime();
				
				if(remainTime > 10*60*1000){//获取时间失败
					while(dsnHttp.loginToDsn() == false); //重新登录
					remainTime = dsnHttp.getRemainTime();
				}
				
				System.out.println("距离封盘时间为:");
				System.out.println(remainTime/1000);
					
				if(remainTime <= 0){//已经封盘,等四分钟后再次查询
					Thread.sleep(4*60*1000);
				}
				else{
					if(remainTime < 3*1000){//最后三秒秒去下注
						String data = DsnProxyGrab.grabCQSSCdata("LM", "XZ", "");
						if(data == null || data.length() <= 0){//获取数据失败
							while(DsnProxyGrab.doLogin() == false); //重新连接
							data = DsnProxyGrab.grabCQSSCdata("LM", "XZ", "");
						}
						System.out.println("下单数据：");
						System.out.println(data);
						if(data != null){
						autoBetSuccess = dsnHttp.doBetCQSSC(data);
						Thread.sleep(4*1000);
						}
					}
					else{
						//long sleepTime = remainTime - 3*1000;
						
						String data = DsnProxyGrab.grabCQSSCdata("LM", "XZ", "");
						if(data == null || data.length() <= 0){//获取数据失败
							while(DsnProxyGrab.doLogin() == false); //重新连接
							data = DsnProxyGrab.grabCQSSCdata("LM", "XZ", "");
						}
						System.out.println("下单数据：");
						System.out.println(data);
						
						long sleepTime = 10*1000;
						
						if(remainTime <= 10*1000){
							sleepTime = remainTime - 3*1000;
						}
						Thread.sleep(sleepTime);
					}
				}
	
			}//while
		
    	}catch (InterruptedException e) {
            // TODO: handle exception
        }
	}//run
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
		betThread.start();
		
		inBet = true;
		
	}	

}