package dsn;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class BetTianCaiOppositeBJSCListener implements ActionListener {
	private autoBet ab;
	
	public BetTianCaiOppositeBJSCListener(autoBet ab) {
		this.ab = ab;
	}
	
	public void actionPerformed(ActionEvent e)
	{
/*		if(ab.loginToTianCaiMemberSuccess == false){			
			return;
		}
		
		//ab.grabThread.startGrabBJSC();
		
		String s = ab.textFieldBJSCBetTianCaiPercent.getText();
		
		if(Common.isNum(s)){
			double percent = Double.parseDouble(s);
			BetTianCaiThread.betBJSCPercent = percent;
		}
		else{
			//TODO 弹出对话框，提示输入错误
		}
		
		ab.setBetTime();
		
		BetTianCaiThread.betBJSC = false;
		BetTianCaiThread.betOppositeBJSC = true;
		
		String outputStr = "开始反投添彩北京赛车,投注比例：" + BetTianCaiThread.betBJSCPercent + "\n";
		
		autoBet.outputGUIMessage(outputStr);
		
		if(ab.inBetTianCai == true){					
			return;
		}
		

		
		BetTianCaiThread BetTianCaiThread = new BetTianCaiThread(ab.tianCaiHttp);
								
		BetTianCaiThread.start();
		
		ab.inBetTianCai = true;*/
	}

}

class StopBetTianCaiBJSCListener implements ActionListener
{
	private autoBet ab;
	
	
	
	public StopBetTianCaiBJSCListener(autoBet ab) {
		this.ab = ab;
	}
	
	public void actionPerformed(ActionEvent e)
	{

		BetTianCaiThread.betBJSC = false;
		BetTianCaiThread.betOppositeBJSC = false;
		
		autoBet.outputGUIMessage("停止投注添彩北京赛车\n");
	}	

}
