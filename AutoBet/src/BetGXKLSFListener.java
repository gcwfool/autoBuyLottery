import java.awt.*;
import java.awt.event.*;


public class BetGXKLSFListener implements ActionListener
{
	private autoBet ab;
	private Client client;
	
	
	public BetGXKLSFListener(autoBet ab, Client client) {
		this.ab = ab;
		this.client = client;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(ab.loginToDSNMemberSuccess == false){			
			return;
		}
		
		//ab.grabThread.startGrabBJSC();
		
		String s = ab.textFieldGXKLSFBetPercent.getText();
		
		if(Common.isNum(s)){
			double percent = Double.parseDouble(s);
			BetGXKLSFThread.betGXKLSFPercent = percent;
		}
		else{
			//TODO �����Ի�����ʾ�������
		}
		
		ab.setBetTime();
		
	
		BetGXKLSFThread.betOppositeGXKLSF = true;
		
		String outputStr = "��ʼ��Ͷ����ũ��,Ͷע������" + BetGXKLSFThread.betGXKLSFPercent + "\n";
		
		autoBet.outputGUIMessage(outputStr);
		
		BetGXKLSFManager.showGXKLSFDeatilsTable();
		
		if(ab.inBetGXKLSF == true){					
			return;
		}
		

		
		BetGXKLSFThread betThread = new BetGXKLSFThread(client);
								
		betThread.start();
		
		ab.inBetGXKLSF = true;
		
		
		
	}	

}


class BetAmountDetailGXKLSFListener implements ActionListener
{
	private autoBet ab;
	
	
	
	public BetAmountDetailGXKLSFListener(autoBet ab) {
		this.ab = ab;
	}
	
	public void actionPerformed(ActionEvent e)
	{

		BetGXKLSFManager.showGXKLSFBetAmountTable();
	}	

}

