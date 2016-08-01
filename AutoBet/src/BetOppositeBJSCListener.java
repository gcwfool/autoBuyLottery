import java.awt.*;
import java.awt.event.*;


public class BetOppositeBJSCListener implements ActionListener
{
	private autoBet ab;
	
	
	
	public BetOppositeBJSCListener(autoBet ab) {
		this.ab = ab;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(ab.loginToDSNMemberSuccess == false){			
			return;
		}
		
		ab.grabThread.startGrabBJSC();
		
		String s = ab.textFieldBJSCBetPercent.getText();
		
		if(Common.isNum(s)){
			double percent = Double.parseDouble(s);
			BetThread.betBJSCPercent = percent;
		}
		else{
			//TODO �����Ի�����ʾ�������
		}
		
		ab.setBetTime();
		
		BetThread.betBJSC = false;
		BetThread.betOppositeBJSC = true;
		
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