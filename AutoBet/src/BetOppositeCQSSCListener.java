import java.awt.*;
import java.awt.event.*;


public class BetOppositeCQSSCListener implements ActionListener
{
	private autoBet ab;
	private Client client;
	
	
	
	public BetOppositeCQSSCListener(autoBet ab, Client client) {
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
		
		
		ab.btnBetCQSSC.setBackground(Color.RED);
		ab.btnOppositeBetCQSSC.setBackground(Color.GREEN);
		
		BetThread.betCQSSC = false;
		BetThread.betOppositeCQSSC = true;

		String outputStr = "开始反投重庆时时彩,投注比例：" + BetThread.betCQSSCPercent + "\n";
		
		autoBet.outputGUIMessage(outputStr);
		
		dsnHttp.showCQSSCDeatilsTable();
		
		if(ab.inBet == true){
			return;
		}
		
		BetThread betThread = new BetThread(client);
								
		betThread.start();
		
		ab.inBet = true;
		
		
		
	}	

}