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
			//TODO 弹出对话框，提示输入错误
		}
		
		ab.setBetTime();
		
		ab.btnBetGDKLSF.setBackground(Color.RED);
		ab.btnBetOppositeGDKLSF.setBackground(Color.GREEN);
	
		BetGDKLSFThread.betOppositeGDKLSF = true;
		BetGDKLSFThread.betGDKLSF = false;
		
		String outputStr = "开始反投广东快乐十分,投注比例：" + BetGDKLSFThread.betGDKLSFPercent + "\n";
		
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


class BetzhengGDKLSFListener implements ActionListener
{
	private autoBet ab;
	private Client client;
	
	
	public BetzhengGDKLSFListener(autoBet ab, Client client) {
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
			//TODO 弹出对话框，提示输入错误
		}
		
		ab.setBetTime();
		
		ab.btnBetGDKLSF.setBackground(Color.GREEN);
		ab.btnBetOppositeGDKLSF.setBackground(Color.RED);
		
		BetGDKLSFThread.betGDKLSF = true;
		BetGDKLSFThread.betOppositeGDKLSF = false;
		
		String outputStr = "开始正投广东快乐十分,投注比例：" + BetGDKLSFThread.betGDKLSFPercent + "\n";
		
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

