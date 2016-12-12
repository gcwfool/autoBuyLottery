package dsn;
import java.awt.*;
import java.awt.event.*;


public class BetOppositeBJSCListener implements ActionListener
{
	private autoBet ab;
	private Client client;
	
	
	public BetOppositeBJSCListener(autoBet ab, Client client) {
		this.ab = ab;
		this.client = client;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(ab.loginToDSNMemberSuccess == false){			
			return;
		}
		
		//ab.grabThread.startGrabBJSC();
		
		String s = ab.textFieldBJSCBetPercent.getText();
		
		if(Common.isNum(s)){
			double percent = Double.parseDouble(s);
			BetThread.betBJSCPercent = percent;
		}
		else{
			//TODO 弹出对话框，提示输入错误
		}
		
		ab.setBetTime();
		
		ab.btnBetBJSC.setBackground(Color.RED);
		ab.btnOppositeBJSC.setBackground(Color.GREEN);
		
		BetThread.betBJSC = false;
		BetThread.betOppositeBJSC = true;
		
		String outputStr = "开始反投北京赛车,投注比例：" + BetThread.betBJSCPercent + "\n";
		
		autoBet.outputGUIMessage(outputStr);
		
		dsnHttp.showBJSCDeatilsTable();
		
		if(ab.inBet == true){					
			return;
		}
		

		
		BetThread betThread = new BetThread(client);
								
		betThread.start();
		
		ab.inBet = true;
		
		
		
	}	

}