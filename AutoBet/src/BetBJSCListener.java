import java.awt.*;
import java.awt.event.*;


public class BetBJSCListener implements ActionListener
{
	private autoBet ab;
	
	
	
	public BetBJSCListener(autoBet ab) {
		this.ab = ab;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(ab.loginToDSNMemberSuccess == false){			
			return;
		}
		
		ab.grabThread.startGrabBJSC();
		
		String s = ab.textFieldBJSCBetPercent.getText();
		
		if(Common.isNum(s)){
			double percent = Double.parseDouble(s);
			BetThread.betBJSCPercent = percent;
		}
		else{
			//TODO 弹出对话框，提示输入错误
		}
		
		ab.setBetTime();
		
		BetThread.betBJSC = true;
		BetThread.betOppositeBJSC = false;
		
		String outputStr = "开始正投北京赛车,投注比例：" + BetThread.betBJSCPercent + "\n";
		
		autoBet.outputGUIMessage(outputStr);
		
		if(ab.inBet == true){					
			return;
		}
		

		
		BetThread betThread = new BetThread();
								
		betThread.start();
		
		ab.inBet = true;
		
	}	

}




class StopBetBJSCListener implements ActionListener
{
	private autoBet ab;
	
	
	
	public StopBetBJSCListener(autoBet ab) {
		this.ab = ab;
	}
	
	public void actionPerformed(ActionEvent e)
	{

		BetThread.betBJSC = false;
		BetThread.betOppositeBJSC = false;
		
		autoBet.outputGUIMessage("停止投注北京赛车\n");
	}	

}