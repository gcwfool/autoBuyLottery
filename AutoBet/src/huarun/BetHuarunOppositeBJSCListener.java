package huarun;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import dsn.*;

public class BetHuarunOppositeBJSCListener implements ActionListener {
	private autoBet ab;
	private Client client;
	
	public BetHuarunOppositeBJSCListener(autoBet ab, Client client) {
		this.ab = ab;
		this.client = client;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(ab.loginToHuarunMemberSuccess == false){			
			return;
		}
		
		//ab.grabThread.startGrabBJSC();
		
		String s = ab.textFieldBJSCBetHuarunPercent.getText();
		
		if(Common.isNum(s)){
			double percent = Double.parseDouble(s);
			BetHuarunBJSCThread.betBJSCPercent = percent;
		}
		else{
			//TODO �����Ի�����ʾ�������
		}
		
		ab.setBetTime();
		
		BetHuarunBJSCThread.betBJSC = false;
		BetHuarunBJSCThread.betOppositeBJSC = true;
		
		String outputStr = "开始反投华润北京赛车" + BetHuarunBJSCThread.betBJSCPercent + "\n";
		
		autoBet.outputGUIMessage(outputStr);
		
		BetBJSCManager.init();
		BetBJSCManager.showBJSCDeatilsTable();
		
		
		if(ab.inBetHuarunBJSC == true){					
			return;
		}
		

		
		new BetHuarunBJSCThread(client).start();
								
		
		ab.inBetHuarunBJSC = true;
	}

}


