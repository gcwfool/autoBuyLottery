import java.awt.*;
import java.awt.event.*;


public class BetGD11X5Listener implements ActionListener
{
	private autoBet ab;
	private Client client;
	
	
	public BetGD11X5Listener(autoBet ab, Client client) {
		this.ab = ab;
		this.client = client;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(ab.loginToDSNMemberSuccess == false){			
			return;
		}
		
		//ab.grabThread.startGrabBJSC();
		
		String s = ab.textFieldGD11X5BetPercent.getText();
		
		if(Common.isNum(s)){
			double percent = Double.parseDouble(s);
			BetGD11X5Thread.betGD11X5Percent = percent;
		}
		else{
			//TODO 弹出对话框，提示输入错误
		}
		
		ab.setBetTime();
		
	
		BetGD11X5Thread.betOppositeGD11X5 = true;
		
		String outputStr = "开始反投广东11选5,投注比例：" + BetGD11X5Thread.betGD11X5Percent + "\n";
		
		autoBet.outputGUIMessage(outputStr);
		
		BetGD11X5Manager.showGD11X5DeatilsTable();
		
		if(ab.inBetGD11X5 == true){					
			return;
		}
		

		
		BetGD11X5Thread betThread = new BetGD11X5Thread(client);
								
		betThread.start();
		
		ab.inBetGD11X5 = true;
		
		
		
	}	

}


class BetAmountDetailGD11X5Listener implements ActionListener
{
	private autoBet ab;
	
	
	
	public BetAmountDetailGD11X5Listener(autoBet ab) {
		this.ab = ab;
	}
	
	public void actionPerformed(ActionEvent e)
	{

		BetGD11X5Manager.showGD11X5BetAmountTable();
	}	

}

