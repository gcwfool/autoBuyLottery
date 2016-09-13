import java.awt.*;
import java.awt.event.*;


public class BetCQSSCListener implements ActionListener
{
	private autoBet ab;
	
	
	
	public BetCQSSCListener(autoBet ab) {
		this.ab = ab;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(ab.loginToDSNMemberSuccess == false){			
			return;
		}
		
		ab.grabThread.startGrabCQSSC();
		
		String s = ab.textFieldCQSSCBetPercent.getText();
		
		if(Common.isNum(s)){
			double percent = Double.parseDouble(s);
			BetThread.betCQSSCPercent = percent;
		}
		else{
			//TODO �����Ի�����ʾ�������
		}
		
		ab.setBetTime();

		
		BetThread.betCQSSC = true;
		BetThread.betOppositeCQSSC = false;
		
		String outputStr = "��ʼ��Ͷ����ʱʱ��,Ͷע������" + BetThread.betCQSSCPercent + "\n";
		
		autoBet.outputGUIMessage(outputStr);
		
		if(ab.inBet == true){					
			return;
		}
		
		BetThread betThread = new BetThread();
								
		betThread.start();
		
		ab.inBet = true;
		
	}	

}


class StopBetCQSSCListener implements ActionListener
{
	private autoBet ab;
	
	
	
	public StopBetCQSSCListener(autoBet ab) {
		this.ab = ab;
	}
	
	public void actionPerformed(ActionEvent e)
	{

		BetThread.betCQSSC = false;
		BetThread.betOppositeCQSSC = false;
		
		autoBet.outputGUIMessage("ֹͣͶע����ʱʱ��\n");
	}	

}



