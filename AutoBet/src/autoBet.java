import java.awt.*;
import java.awt.event.*;

enum BetType{
	CQSSC,
	BJSC
}

public class autoBet{
	
	public boolean loginSuccess = false;
	public boolean inBet = false;
	public TextField textFieldCQSSCBetPercent;
	public TextField textFieldBJSCBetPercent;
	public TextField textFieldBetTime;
	public static TextArea outputMessage;
	
	
	public static void main(String[] args) throws Exception {
		

		ConfigReader.read("common.config");

		new autoBet().launchFrame();

	}
	
	public void setBetTime(){
		String betTime = textFieldBetTime.getText();
		
		if(Common.isNum(betTime)){
			int timeSeconds = Integer.parseInt(betTime);
			BetThread.betRemainTime = timeSeconds*1000;
		}
		else{
			//TODO 弹出对话框，提示输入错误
		}
	}
	
	public  void launchFrame()
	{

	    Frame mainFrame =new Frame("自动下单器");

		
		Panel panel=new Panel();
		
	    panel.setSize(500,800);

	    panel.setLocation(0,0);
	    panel.setLayout(null);
	    mainFrame.setLayout(null);

	    mainFrame.add(panel);

	       
		
		
		
		Button btnLogin = new Button("登录");
		btnLogin.addActionListener(new LoginListener(this));
		
		btnLogin.setSize(50, 25);
		btnLogin.setLocation(50,50);
		
		
		Label labelBetTime = new Label("距封盘还有           秒时进行投注");
		labelBetTime.setSize(200, 25);
		labelBetTime.setLocation(60, 80);
		
		textFieldBetTime = new TextField();
		textFieldBetTime.setSize(30, 25);
		textFieldBetTime.setLocation(125, 80);
		
		Label labelPercent = new Label("投注比例:");
		labelPercent.setSize(60, 25);
		labelPercent.setLocation(150, 110);
		
		textFieldCQSSCBetPercent = new TextField();
		textFieldCQSSCBetPercent.setSize(60, 25);
		textFieldCQSSCBetPercent.setLocation(210, 110);
		
		
		Label BJSClabelPercent = new Label("投注比例:");
		BJSClabelPercent.setSize(60, 25);
		BJSClabelPercent.setLocation(150, 170);
		
		textFieldBJSCBetPercent = new TextField();
		textFieldBJSCBetPercent.setSize(60, 25);
		textFieldBJSCBetPercent.setLocation(210, 170);
		
		
		Button btnBetCQSSC = new Button("正投重庆时时彩");
		btnBetCQSSC.addActionListener(new BetCQSSCListener(this));
		
		btnBetCQSSC.setSize(90, 25);
		btnBetCQSSC.setLocation(50,110);
		
		Button btnOppositeBetCQSSC = new Button("反投重庆时时彩");
		btnOppositeBetCQSSC.addActionListener(new BetOppositeCQSSCListener(this));
		
		btnOppositeBetCQSSC.setSize(90, 25);
		btnOppositeBetCQSSC.setLocation(50,140);
		
		
		Button btnStopBetCQSSC = new Button("停止投注");
		btnStopBetCQSSC.addActionListener(new StopBetCQSSCListener(this));
		
		btnStopBetCQSSC.setSize(90, 25);
		btnStopBetCQSSC.setLocation(150,140);
		
		
		Button btnBetBJSC = new Button("正投北京赛车");
		btnBetBJSC.addActionListener(new BetBJSCListener(this));
		
		btnBetBJSC.setSize(75, 25);
		btnBetBJSC.setLocation(50,170);
		
		
		Button btnOppositeBJSC = new Button("反投北京赛车");
		btnOppositeBJSC.addActionListener(new BetOppositeBJSCListener(this));
		
		btnOppositeBJSC.setSize(75, 25);
		btnOppositeBJSC.setLocation(50,200);
		
		
		Button btnStopBetBJSC = new Button("停止投注");
		btnStopBetBJSC.addActionListener(new StopBetBJSCListener(this));
		
		btnStopBetBJSC.setSize(90, 25);
		btnStopBetBJSC.setLocation(150,200);
		

		
		outputMessage = new TextArea();
		outputMessage.setSize(400, 500);
		outputMessage.setLocation(25, 300);
		
		
		panel.add(btnLogin);
		panel.add(btnBetCQSSC);
		panel.add(labelPercent);
		panel.add(btnOppositeBetCQSSC);
		panel.add(btnBetBJSC);
		panel.add(btnOppositeBJSC);
		panel.add(BJSClabelPercent);
		panel.add(textFieldCQSSCBetPercent);
		panel.add(textFieldBJSCBetPercent);
		panel.add(outputMessage);
		
		panel.add(btnStopBetCQSSC);
		panel.add(btnStopBetBJSC);
		
		panel.add(textFieldBetTime);

		
		panel.add(labelBetTime);
		

		
		
		mainFrame.setSize(500, 800);
		
		mainFrame.setVisible(true);
		
		mainFrame.addWindowListener
        (
                new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        System.exit(0);
                    }
                }
        );
	}
			
}


