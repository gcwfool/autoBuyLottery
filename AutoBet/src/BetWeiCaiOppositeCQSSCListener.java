import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class BetWeiCaiOppositeCQSSCListener implements ActionListener
{
	private autoBet ab;
	
	
	
	public BetWeiCaiOppositeCQSSCListener(autoBet ab) {
		this.ab = ab;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(ab.loginToWeiCaiMemberSuccess == false){			
			return;
		}
		
		ab.grabThread.startGrabCQSSC();
		
		String s = ab.textFieldCQSSCBetWeiCaiPercent.getText();
		
		if(Common.isNum(s)){
			double percent = Double.parseDouble(s);
			BetWeiCaiThread.betCQSSCPercent = percent;
		}
		else{
			//TODO �����Ի�����ʾ�������
		}
		
		ab.setBetTime();

		
		BetWeiCaiThread.betCQSSC = false;
		BetWeiCaiThread.betOppositeCQSSC = true;
		
		String outputStr = "��ʼ��Ͷ΢������ʱʱ��,Ͷע������" + BetWeiCaiThread.betCQSSCPercent + "\n";
		
		autoBet.outputMessage.append(outputStr);
		
		if(ab.inBetWeiCai == true){					
			return;
		}
		
		BetWeiCaiThread betWeiCaiThread = new BetWeiCaiThread();
								
		betWeiCaiThread.start();
		
		ab.inBetWeiCai = true;
		
	}	

}


class StopBetWeiCaiCQSSCListener implements ActionListener
{
	private autoBet ab;
	
	
	
	public StopBetWeiCaiCQSSCListener(autoBet ab) {
		this.ab = ab;
	}
	
	public void actionPerformed(ActionEvent e)
	{

		BetWeiCaiThread.betCQSSC = false;
		BetWeiCaiThread.betOppositeCQSSC = false;
		
		autoBet.outputMessage.append("ֹͣͶע΢������ʱʱ��\n");
	}	

}