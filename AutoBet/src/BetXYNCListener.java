import java.awt.*;
import java.awt.event.*;


public class BetXYNCListener implements ActionListener
{
	private autoBet ab;
	private Client client;
	
	
	public BetXYNCListener(autoBet ab, Client client) {
		this.ab = ab;
		this.client = client;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(ab.loginToDSNMemberSuccess == false){			
			return;
		}
		
		//ab.grabThread.startGrabBJSC();
		
		String s = ab.textFieldXYNCBetPercent.getText();
		
		if(Common.isNum(s)){
			double percent = Double.parseDouble(s);
			BetXYNCThread.betXYNCPercent = percent;
		}
		else{
			//TODO 弹出对话框，提示输入错误
		}
		
		ab.setBetTime();
		
	
		BetXYNCThread.betOppositeXYNC = true;
		
		String outputStr = "开始反投幸运农场,投注比例：" + BetXYNCThread.betXYNCPercent + "\n";
		
		autoBet.outputGUIMessage(outputStr);
		
		BetXYNCManager.showXYNCDeatilsTable();
		
		if(ab.inBetXYNC == true){					
			return;
		}
		

		
		BetXYNCThread betThread = new BetXYNCThread(client);
								
		betThread.start();
		
		ab.inBetXYNC = true;
		
		
		
	}	

}


class BetAmountDetailXYNCListener implements ActionListener
{
	private autoBet ab;
	
	
	
	public BetAmountDetailXYNCListener(autoBet ab) {
		this.ab = ab;
	}
	
	public void actionPerformed(ActionEvent e)
	{

		BetXYNCManager.showXYNCBetAmountTable();
	}	

}

