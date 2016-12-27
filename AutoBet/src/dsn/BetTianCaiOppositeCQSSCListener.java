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
			//TODO 弹出对话框，提示输入错误
		}
		
		ab.setBetTime();

		
		BetTianCaiThread.betCQSSC = false;
		BetTianCaiThread.betOppositeCQSSC = true;
		
		String outputStr = "开始反投添彩重庆时时彩,投注比例：" + BetTianCaiThread.betCQSSCPercent + "\n";
		
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
		
		autoBet.outputGUIMessage("停止投注添彩重庆时时彩\n");
	}	

}
