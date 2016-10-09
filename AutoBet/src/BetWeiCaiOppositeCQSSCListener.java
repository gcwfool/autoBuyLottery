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
			//TODO 弹出对话框，提示输入错误
		}
		
		ab.setBetTime();

		
		BetWeiCaiThread.betCQSSC = false;
		BetWeiCaiThread.betOppositeCQSSC = true;
		
		String outputStr = "开始反投微彩重庆时时彩,投注比例：" + BetWeiCaiThread.betCQSSCPercent + "\n";
		
		autoBet.outputGUIMessage(outputStr);
		
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
		
		autoBet.outputGUIMessage("停止投注微彩重庆时时彩\n");
	}	

}