package dsn;
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
			//TODO �����Ի�����ʾ�������
		}
		
		ab.setBetTime();
		
		ab.btnBetXYNC.setBackground(Color.RED);
		ab.btnBetOppositeXYNC.setBackground(Color.GREEN);
		
		BetXYNCThread.betOppositeXYNC = true;
		BetXYNCThread.betXYNC = false;
		
		String outputStr = "��ʼ��Ͷ����ũ��,Ͷע����" + BetXYNCThread.betXYNCPercent + "\n";
		
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



class BetzhengXYNCListener implements ActionListener
{
	private autoBet ab;
	private Client client;
	
	
	public BetzhengXYNCListener(autoBet ab, Client client) {
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
			//TODO �����Ի�����ʾ�������
		}
		
		ab.setBetTime();
		
		ab.btnBetXYNC.setBackground(Color.GREEN);
		ab.btnBetOppositeXYNC.setBackground(Color.RED);
	
		BetXYNCThread.betXYNC = true;
		BetXYNCThread.betOppositeXYNC = false;
		
		String outputStr = "��ʼ��Ͷ����ũ��,Ͷע����" + BetXYNCThread.betXYNCPercent + "\n";
		
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

