import java.awt.*;
import java.awt.event.*;


public class BetGDKLSFListener implements ActionListener
{
	private autoBet ab;
	private Client client;
	
	
	public BetGDKLSFListener(autoBet ab, Client client) {
		this.ab = ab;
		this.client = client;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(ab.loginToDSNMemberSuccess == false){			
			return;
		}
		
		//ab.grabThread.startGrabBJSC();
		
		String s = ab.textFieldGDKLSFBetPercent.getText();
		
		if(Common.isNum(s)){
			double percent = Double.parseDouble(s);
			BetGDKLSFThread.betGDKLSFPercent = percent;
		}
		else{
			//TODO �����Ի�����ʾ�������
		}
		
		ab.setBetTime();
		
	
		BetGDKLSFThread.betOppositeGDKLSF = true;
		
		String outputStr = "��ʼ��Ͷ�㶫����ʮ��,Ͷע������" + BetGDKLSFThread.betGDKLSFPercent + "\n";
		
		autoBet.outputGUIMessage(outputStr);
		
		BetGDKLSFManager.showGDKLSFDeatilsTable();
		
		if(ab.inBetGDKLSF == true){					
			return;
		}
		

		
		BetGDKLSFThread betThread = new BetGDKLSFThread(client);
								
		betThread.start();
		
		ab.inBetGDKLSF = true;
		
		
		
	}	

}


class BetAmountDetailGDKLSFListener implements ActionListener
{
	private autoBet ab;
	
	
	
	public BetAmountDetailGDKLSFListener(autoBet ab) {
		this.ab = ab;
	}
	
	public void actionPerformed(ActionEvent e)
	{

		BetGDKLSFManager.showGDKLSFBetAmountTable();
	}	

}

