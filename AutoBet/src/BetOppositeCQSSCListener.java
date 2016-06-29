import java.awt.*;
import java.awt.event.*;


public class BetOppositeCQSSCListener implements ActionListener
{
	private autoBet ab;
	
	
	
	public BetOppositeCQSSCListener(autoBet ab) {
		this.ab = ab;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(ab.loginSuccess == false){			
			return;
		}
		
		String s = ab.textFieldCQSSCBetPercent.getText();
		
		if(Common.isNum(s)){
			double percent = Double.parseDouble(s);
			BetThread.betCQSSCPercent = percent;
		}
		else{
			//TODO �����Ի�����ʾ�������
		}
		
		ab.setBetTime();
		
		BetThread.betCQSSC = false;
		BetThread.betOppositeCQSSC = true;

		String outputStr = "��ʼ��Ͷ����ʱʱ��,Ͷע������" + BetThread.betCQSSCPercent + "\n";
		
		autoBet.outputMessage.append(outputStr);
		
		if(ab.inBet == true){					
			return;
		}
		
		BetThread betThread = new BetThread();
								
		betThread.start();
		
		ab.inBet = true;
		
	}	

}