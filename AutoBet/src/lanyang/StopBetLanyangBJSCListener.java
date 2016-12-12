package lanyang;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import dsn.autoBet;

public class StopBetLanyangBJSCListener implements ActionListener
{
	private autoBet ab;
	
	
	
	public StopBetLanyangBJSCListener(autoBet ab) {
		this.ab = ab;
	}
	
	public void actionPerformed(ActionEvent e)
	{

		BetLanyangBJSCThread.betBJSC = false;
		BetLanyangBJSCThread.betOppositeBJSC = false;
		
		autoBet.outputGUIMessage("ֹͣ停止投注蓝洋北京赛车\n");
	}	

}
