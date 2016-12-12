package lanyang;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import dsn.*;

public class BetLanyangOppositeBJSCListener implements ActionListener {
	private autoBet ab;
	private Client client;
	
	public BetLanyangOppositeBJSCListener(autoBet ab, Client client) {
		this.ab = ab;
		this.client = client;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(ab.loginToLanyangMemberSuccess == false){			
			return;
		}
		
		//ab.grabThread.startGrabBJSC();
		
		String s = ab.textFieldBJSCBetLanyangPercent.getText();
		
		if(Common.isNum(s)){
			double percent = Double.parseDouble(s);
			BetLanyangBJSCThread.betBJSCPercent = percent;
		}
		else{
			//TODO �����Ի�����ʾ�������
		}
		
		ab.setBetTime();
		
		BetLanyangBJSCThread.betBJSC = false;
		BetLanyangBJSCThread.betOppositeBJSC = true;
		
		String outputStr = "开始反投蓝洋北京赛车" + BetLanyangBJSCThread.betBJSCPercent + "\n";
		
		autoBet.outputGUIMessage(outputStr);
		
		BetBJSCManager.init();
		BetBJSCManager.showBJSCDeatilsTable();
		
		
		if(ab.inBetLanyangBJSC == true){					
			return;
		}
		

		
		BetLanyangBJSCThread BetLanyangThread = new BetLanyangBJSCThread(client);
								
		BetLanyangThread.start();
		
		ab.inBetLanyangBJSC = true;
	}

}


