import java.awt.*;
import java.awt.event.*;


public class BetCQSSCListener implements ActionListener
{
	private autoBet ab;
	
	
	
	public BetCQSSCListener(autoBet ab) {
		this.ab = ab;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(ab.loginToDSNMemberSuccess == false){			
			return;
		}
		
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
		
		autoBet.outputMessage.append(outputStr);
		
		if(ab.inBet == true){					
			return;
		}
		
		BetThread betThread = new BetThread();
								
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

		BetThread.betCQSSC = false;
		BetThread.betOppositeCQSSC = false;
		
		autoBet.outputMessage.append("停止投注重庆时时彩\n");
	}	

}



