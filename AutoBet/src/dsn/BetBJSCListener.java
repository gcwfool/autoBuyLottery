package dsn;
import java.awt.*;
import java.awt.event.*;


public class BetBJSCListener implements ActionListener
{
	private autoBet ab;
	private Client client;
	
	
	public BetBJSCListener(autoBet ab, Client client) {
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
			//TODO �����Ի�����ʾ�������
		}
		
		ab.setBetTime();
		
		ab.btnBetBJSC.setBackground(Color.GREEN);
		ab.btnOppositeBJSC.setBackground(Color.RED);
		
		BetThread.betBJSC = true;
		BetThread.betOppositeBJSC = false;
		
		String outputStr = "��ʼ��Ͷ������,Ͷע����" + BetThread.betBJSCPercent + "\n";
		
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




class StopBetBJSCListener implements ActionListener
{
	private autoBet ab;
	
	
	
	public StopBetBJSCListener(autoBet ab) {
		this.ab = ab;
	}
	
	public void actionPerformed(ActionEvent e)
	{

		dsnHttp.showBJSCBetAmountTable();

	}	

}