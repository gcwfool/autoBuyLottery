package Webet;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import dsn.*;

public class BetWebetOppositeBJSCListener implements ActionListener {
	private autoBet ab;
	private Client client;
	
	public BetWebetOppositeBJSCListener(autoBet ab, Client client) {
		this.ab = ab;
		this.client = client;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(ab.loginToWebetMemberSuccess == false){			
			return;
		}
		
		//ab.grabThread.startGrabBJSC();
		
		String s = ab.textFieldBJSCBetWebetPercent.getText();
		
		if(Common.isNum(s)){
			double percent = Double.parseDouble(s);
			BetWebetBJSCThread.betBJSCPercent = percent;
		}
		else{
			//TODO �����Ի�����ʾ�������
		}
		
		ab.setBetTime();
		
		BetWebetBJSCThread.betBJSC = false;
		BetWebetBJSCThread.betOppositeBJSC = true;
		
		String outputStr = "开始反投惟博北京赛车" + BetWebetBJSCThread.betBJSCPercent + "\n";
		
		autoBet.outputGUIMessage(outputStr);
		
		BetBJSCManager.init();
		BetBJSCManager.showBJSCDeatilsTable();
		
		
		if(ab.inBetWebetBJSC == true){					
			return;
		}
		

		
		BetWebetBJSCThread BetWebetThread = new BetWebetBJSCThread(client);
								
		BetWebetThread.start();
		
		ab.inBetWebetBJSC = true;
	}

}


