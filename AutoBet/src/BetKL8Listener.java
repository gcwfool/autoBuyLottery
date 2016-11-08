import java.awt.*;
import java.awt.event.*;


public class BetKL8Listener implements ActionListener
{
	private autoBet ab;
	private Client client;
	
	
	public BetKL8Listener(autoBet ab, Client client) {
		this.ab = ab;
		this.client = client;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(ab.loginToDSNMemberSuccess == false){			
			return;
		}
		
		//ab.grabThread.startGrabBJSC();
		
		String s = ab.textFieldKL8BetPercent.getText();
		
		if(Common.isNum(s)){
			double percent = Double.parseDouble(s);
			BetKL8Thread.betKL8Percent = percent;
		}
		else{
			//TODO 弹出对话框，提示输入错误
		}
		
		ab.setBetTime();
		
	
		BetKL8Thread.betOppositeKL8 = true;
		
		String outputStr = "开始反投新疆时时彩,投注比例：" + BetKL8Thread.betKL8Percent + "\n";
		
		autoBet.outputGUIMessage(outputStr);
		
		BetKL8Manager.showKL8DeatilsTable();
		
		if(ab.inBetKL8 == true){					
			return;
		}
		

		
		BetKL8Thread betThread = new BetKL8Thread(client);
								
		betThread.start();
		
		ab.inBetKL8 = true;
		
		
		
	}	

}


class BetAmountDetailKL8Listener implements ActionListener
{
	private autoBet ab;
	
	
	
	public BetAmountDetailKL8Listener(autoBet ab) {
		this.ab = ab;
	}
	
	public void actionPerformed(ActionEvent e)
	{

		BetKL8Manager.showKL8BetAmountTable();
	}	

}

