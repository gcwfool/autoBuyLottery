import java.awt.*;
import java.awt.event.*;


public class BetOppositeCQSSCListener implements ActionListener
{
	private autoBet ab;
	
	
	
	public BetOppositeCQSSCListener(autoBet ab) {
		this.ab = ab;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(ab.loginSuccess == false){			
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
		
		BetThread.betCQSSC = false;
		BetThread.betOppositeCQSSC = true;

		String outputStr = "开始反投重庆时时彩,投注比例：" + BetThread.betCQSSCPercent + "\n";
		
		autoBet.outputMessage.append(outputStr);
		
		if(ab.inBet == true){					
			return;
		}
		
		BetThread betThread = new BetThread();
								
		betThread.start();
		
		ab.inBet = true;
		
	}	

}