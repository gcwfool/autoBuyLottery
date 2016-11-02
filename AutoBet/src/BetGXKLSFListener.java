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
			//TODO 弹出对话框，提示输入错误
		}
		
		ab.setBetTime();
		
	
		BetGXKLSFThread.betOppositeGXKLSF = true;
		
		String outputStr = "开始反投广西快乐十分,投注比例：" + BetGXKLSFThread.betGXKLSFPercent + "\n";
		
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

