package dsn;
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
			//TODO �����Ի�����ʾ�������
		}
		
		ab.setBetTime();
		
		
		ab.btnBetCQSSC.setBackground(Color.RED);
		ab.btnOppositeBetCQSSC.setBackground(Color.GREEN);
		
		BetThread.betCQSSC = false;
		BetThread.betOppositeCQSSC = true;

		String outputStr = "��ʼ��Ͷ����ʱʱ��,Ͷע����" + BetThread.betCQSSCPercent + "\n";
		
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