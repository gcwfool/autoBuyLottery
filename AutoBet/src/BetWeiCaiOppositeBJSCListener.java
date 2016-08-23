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
			//TODO 弹出对话框，提示输入错误
		}
		
		ab.setBetTime();
		
		BetWeiCaiThread.betBJSC = false;
		BetWeiCaiThread.betOppositeBJSC = true;
		
		String outputStr = "开始反投微彩北京赛车,投注比例：" + BetWeiCaiThread.betBJSCPercent + "\n";
		
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
		
		autoBet.outputMessage.append("停止投注北京赛车\n");
	}	

}