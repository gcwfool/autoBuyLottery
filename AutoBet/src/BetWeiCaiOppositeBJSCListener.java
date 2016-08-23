import java.awt.*;
import java.awt.event.*;


public class BetWeiCaiOppositeBJSCListener implements ActionListener
{
	private autoBet ab;
	
	
	
	public BetWeiCaiOppositeBJSCListener(autoBet ab) {
		this.ab = ab;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(ab.loginToWeiCaiMemberSuccess == false){			
			return;
		}
		
		ab.grabThread.startGrabBJSC();
		
		String s = ab.textFieldBJSCBetWeiCaiPercent.getText();
		
		if(Common.isNum(s)){
			double percent = Double.parseDouble(s);
			BetWeiCaiThread.betBJSCPercent = percent;
		}
		else{
			//TODO �����Ի�����ʾ�������
		}
		
		ab.setBetTime();
		
		BetWeiCaiThread.betBJSC = false;
		BetWeiCaiThread.betOppositeBJSC = true;
		
		String outputStr = "��ʼ��Ͷ΢�ʱ�������,Ͷע������" + BetWeiCaiThread.betBJSCPercent + "\n";
		
		autoBet.outputMessage.append(outputStr);
		
		if(ab.inBetWeiCai == true){					
			return;
		}
		

		
		BetWeiCaiThread BetWeiCaiThread = new BetWeiCaiThread();
								
		BetWeiCaiThread.start();
		
		ab.inBetWeiCai = true;
		
	}	

}



class StopBetWeiCaiBJSCListener implements ActionListener
{
	private autoBet ab;
	
	
	
	public StopBetWeiCaiBJSCListener(autoBet ab) {
		this.ab = ab;
	}
	
	public void actionPerformed(ActionEvent e)
	{

		BetWeiCaiThread.betBJSC = false;
		BetWeiCaiThread.betOppositeBJSC = false;
		
		autoBet.outputMessage.append("ֹͣͶע��������\n");
	}	

}