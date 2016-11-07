import java.awt.*;
import java.awt.event.*;


public class BetTJSSCListener implements ActionListener
{
	private autoBet ab;
	private Client client;
	
	
	public BetTJSSCListener(autoBet ab, Client client) {
		this.ab = ab;
		this.client = client;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(ab.loginToDSNMemberSuccess == false){			
			return;
		}
		
		//ab.grabThread.startGrabBJSC();
		
		String s = ab.textFieldTJSSCBetPercent.getText();
		
		if(Common.isNum(s)){
			double percent = Double.parseDouble(s);
			BetTJSSCThread.betTJSSCPercent = percent;
		}
		else{
			//TODO 弹出对话框，提示输入错误
		}
		
		ab.setBetTime();
		
	
		BetTJSSCThread.betOppositeTJSSC = true;
		
		String outputStr = "开始反投广西快乐十分,投注比例：" + BetTJSSCThread.betTJSSCPercent + "\n";
		
		autoBet.outputGUIMessage(outputStr);
		
		BetTJSSCManager.showTJSSCDeatilsTable();
		
		if(ab.inBetTJSSC == true){					
			return;
		}
		

		
		BetTJSSCThread betThread = new BetTJSSCThread(client);
								
		betThread.start();
		
		ab.inBetTJSSC = true;
		
		
		
	}	

}


class BetAmountDetailTJSSCListener implements ActionListener
{
	private autoBet ab;
	
	
	
	public BetAmountDetailTJSSCListener(autoBet ab) {
		this.ab = ab;
	}
	
	public void actionPerformed(ActionEvent e)
	{

		BetTJSSCManager.showTJSSCBetAmountTable();
	}	

}

