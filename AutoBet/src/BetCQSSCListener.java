import java.awt.*;
import java.awt.event.*;


public class BetCQSSCListener implements ActionListener
{
	private autoBet ab;
	
	private Client client;
	
	public BetCQSSCListener(autoBet ab, Client client) {
		this.ab = ab;
		this.client = client;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(ab.loginToDSNMemberSuccess == false){			
			return;
		}
		
		//ab.grabThread.startGrabCQSSC();
		
		String s = ab.textFieldCQSSCBetPercent.getText();
		
		if(Common.isNum(s)){
			double percent = Double.parseDouble(s);
			BetThread.betCQSSCPercent = percent;
		}
		else{
			//TODO 弹出对话框，提示输入错误
		}
		
		ab.setBetTime();

		
		BetThread.betCQSSC = true;
		BetThread.betOppositeCQSSC = false;
		
		String outputStr = "开始正投重庆时时彩,投注比例：" + BetThread.betCQSSCPercent + "\n";
		
		autoBet.outputGUIMessage(outputStr);
		
		if(ab.inBet == true){					
			return;
		}
		
		BetThread betThread = new BetThread(client);
								
		betThread.start();
		
		ab.inBet = true;
		
	}	

}


class StopBetCQSSCListener implements ActionListener
{
	private autoBet ab;
	
	
	
	public StopBetCQSSCListener(autoBet ab) {
		this.ab = ab;
	}
	
	public void actionPerformed(ActionEvent e)
	{

		dsnHttp.showCQSSCBetAmountTable();
	}	

}



