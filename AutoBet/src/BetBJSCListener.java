import java.awt.*;
import java.awt.event.*;


public class BetBJSCListener implements ActionListener
{
	private autoBet ab;
	
	
	
	public BetBJSCListener(autoBet ab) {
		this.ab = ab;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(ab.loginSuccess == false){			
			return;
		}
		
		String s = ab.textFieldBJSCBetPercent.getText();
		
		if(Common.isNum(s)){
			double percent = Double.parseDouble(s);
			BetThread.betBJSCPercent = percent;
		}
		else{
			//TODO �����Ի�����ʾ�������
		}
		
		ab.setBetTime();
		
		BetThread.betBJSC = true;
		BetThread.betOppositeBJSC = false;
		
		String outputStr = "��ʼ��Ͷ��������,Ͷע������" + BetThread.betBJSCPercent + "\n";
		
		autoBet.outputMessage.append(outputStr);
		
		if(ab.inBet == true){					
			return;
		}
		

		
		BetThread betThread = new BetThread();
								
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

		BetThread.betBJSC = false;
		BetThread.betOppositeBJSC = false;
		
		autoBet.outputMessage.append("ֹͣͶע��������\n");
	}	

}