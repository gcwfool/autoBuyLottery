package yabo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import dsn.*;

public class BetYaboOppositeBJSCListener implements ActionListener {
	private autoBet ab;
	private Client client;
	
	public BetYaboOppositeBJSCListener(autoBet ab, Client client) {
		this.ab = ab;
		this.client = client;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(ab.loginToYaboMemberSuccess == false){			
			return;
		}
		
		//ab.grabThread.startGrabBJSC();
		
		String s = ab.textFieldBJSCBetYaboPercent.getText();
		
		if(Common.isNum(s)){
			double percent = Double.parseDouble(s);
			BetYaboBJSCThread.betBJSCPercent = percent;
		}
		else{
		}
		
		ab.setBetTime();
		
		BetYaboBJSCThread.betBJSC = false;
		BetYaboBJSCThread.betOppositeBJSC = true;
		
		String outputStr = "开始反投亚博北京赛车" + BetYaboBJSCThread.betBJSCPercent + "\n";
		
		autoBet.outputGUIMessage(outputStr);
		
		BetBJSCManager.init();
		BetBJSCManager.showBJSCDeatilsTable();
		
		
		if(ab.inBetYaboBJSC == true){					
			return;
		}
		

		
		new BetYaboBJSCThread(client).start();
								
		
		ab.inBetYaboBJSC = true;
	}

}


