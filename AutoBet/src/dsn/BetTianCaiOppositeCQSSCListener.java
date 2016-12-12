package dsn;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class BetTianCaiOppositeCQSSCListener implements ActionListener {
	
	private autoBet ab;	

	public BetTianCaiOppositeCQSSCListener(autoBet ab) {
		this.ab = ab;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(ab.loginToTianCaiMemberSuccess == false){			
			return;
		}
		
		//ab.grabThread.startGrabCQSSC();
		
		String s = ab.textFieldCQSSCBetTianCaiPercent.getText();
		
		if(Common.isNum(s)){
			double percent = Double.parseDouble(s);
			BetTianCaiThread.betCQSSCPercent = percent;
		}
		else{
			//TODO �����Ի�����ʾ�������
		}
		
		ab.setBetTime();

		
		BetTianCaiThread.betCQSSC = false;
		BetTianCaiThread.betOppositeCQSSC = true;
		
		String outputStr = "��ʼ��Ͷ�������ʱʱ��,Ͷע����" + BetTianCaiThread.betCQSSCPercent + "\n";
		
		autoBet.outputGUIMessage(outputStr);
		
		if(ab.inBetTianCai == true){					
			return;
		}
		
		BetTianCaiThread betTianCaiThread = new BetTianCaiThread(ab.tianCaiHttp);
								
		betTianCaiThread.start();
		
		ab.inBetTianCai = true;
		
	}	
}

class StopBetTianCaiCQSSCListener implements ActionListener
{
	private autoBet ab;
	
	
	
	public StopBetTianCaiCQSSCListener(autoBet ab) {
		this.ab = ab;
	}
	
	public void actionPerformed(ActionEvent e)
	{

		BetTianCaiThread.betCQSSC = false;
		BetTianCaiThread.betOppositeCQSSC = false;
		
		autoBet.outputGUIMessage("ֹͣͶע�������ʱʱ��\n");
	}	

}
