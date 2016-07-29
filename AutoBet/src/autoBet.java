import java.awt.*;
import java.awt.event.*;

enum BetType{
	CQSSC,
	BJSC
}

public class autoBet{
	
	public boolean loginToProxySuccess = false;
	public boolean loginToDSNMemberSuccess = false;
	public boolean inBet = false;

	
	//代理登录界面
	public TextField textFieldProxyAddress;
	public TextField textFieldProxyAccount;
	public TextField textFieldProxyPassword;
	
	public Button btnStartGrabCQSSC;
	public Button btnStopGrabCQSSC;	
	public Button btnStartGrabBJSC;
	public Button btnStopGrabBJSC;
	
	//迪斯尼会员界面
	public TextField textFieldMemberAddress;
	public TextField textFieldMemberAccount;
	public TextField textFieldMemberPassword;
	public TextField textFieldCQSSCBetPercent;
	public TextField textFieldBJSCBetPercent;
	public TextField textFieldBetTime;
	
	public Button btnBetCQSSC;
	public Button btnOppositeBetCQSSC;
	public Button btnStopBetCQSSC;
	public Button btnBetBJSC;
	public Button btnOppositeBJSC;
	public Button btnStopBetBJSC;
	
	
	public static TextArea outputMessage;
	public static Label labelTotalBets;
	public static Label labelSuccessBets;
	public static Label labelFailBets;
	private GrabThread grabThread;
	
	
	public static void main(String[] args) throws Exception {
		

		ConfigReader.read("common.config");
		
		ConfigWriter.open("common.config");
		
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
	
	public void enableGrabBtn(boolean flag){
		btnStartGrabCQSSC.setEnabled(flag);
		btnStopGrabCQSSC.setEnabled(flag);
		btnStartGrabBJSC.setEnabled(flag);
		btnStopGrabBJSC.setEnabled(flag);
	}
	
	
	public void enableDSNMemberBet(boolean flag){
		btnBetCQSSC.setEnabled(false);
		btnOppositeBetCQSSC.setEnabled(flag);
		btnStopBetCQSSC.setEnabled(flag);
		btnBetBJSC.setEnabled(false);
		btnOppositeBJSC.setEnabled(flag);
		btnStopBetBJSC.setEnabled(flag);
	}
	
	public  void launchFrame()
	{

	    Frame mainFrame =new Frame("自动下单器");

		
		Panel panel=new Panel();
		
		panel.setSize(1920,1080);

	    panel.setLocation(0,0);
	    panel.setLayout(null);
	    mainFrame.setLayout(null);

	    mainFrame.add(panel);

	       
		
		
	    //dsn代理界面
	    int DsnProxyX = 50;
	    int DsnProxyY = 50; 
	    

	    
		Label labelDsnProxyLogin = new Label("迪斯尼代理登录:");
		labelDsnProxyLogin.setSize(100, 25);
		labelDsnProxyLogin.setLocation(DsnProxyX, DsnProxyY);
		
		Label labelDsnProxyAddress = new Label("网址:");
		labelDsnProxyAddress.setSize(50, 25);
		labelDsnProxyAddress.setLocation(DsnProxyX, DsnProxyY +30);
		
		
		textFieldProxyAddress = new TextField();
		textFieldProxyAddress.setSize(300,25);
		textFieldProxyAddress.setLocation(DsnProxyX + 50, DsnProxyY +30);
		textFieldProxyAddress.setText(ConfigReader.getProxyAddress());
		
		
		Label labelDsnProxyAccount = new Label("账户:");
		labelDsnProxyAccount.setSize(50, 25);
		labelDsnProxyAccount.setLocation(DsnProxyX, DsnProxyY +60);
		
		textFieldProxyAccount = new TextField();
		textFieldProxyAccount.setSize(300,25);
		textFieldProxyAccount.setLocation(DsnProxyX + 50, DsnProxyY +60);
		textFieldProxyAccount.setText(ConfigReader.getProxyAccount());

		Label labelDsnProxyPassword = new Label("密码:");
		labelDsnProxyPassword.setSize(50, 25);
		labelDsnProxyPassword.setLocation(DsnProxyX, DsnProxyY +90);
		
		textFieldProxyPassword = new TextField();
		textFieldProxyPassword.setSize(300,25);
		textFieldProxyPassword.setLocation(DsnProxyX + 50, DsnProxyY +90);
		textFieldProxyPassword.setText(ConfigReader.getProxyPassword());
		textFieldProxyPassword.setEchoChar('*');
		

		
		
		
		Button btnLogin = new Button("登录");
		btnLogin.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(loginToProxySuccess == true)
					return;
				
				String address = textFieldProxyAddress.getText();
				String account = textFieldProxyAccount.getText();
				String password = textFieldProxyPassword.getText();
				
				DsnProxyGrab.setLoginParams(address, account, password);
				
				if(!DsnProxyGrab.login()) {
					outputMessage.append("登录迪士尼代理失败!\n");
					return;
				}
				
				loginToProxySuccess = true;
				
				ConfigWriter.updateProxyAddress(address);
				ConfigWriter.updateProxyAccount(account);
				ConfigWriter.updateProxyPassword(password);
				
				ConfigWriter.saveTofile("common.config");
				
				grabThread = new GrabThread(new GrabCQSSCwindow(), new GrabBJSCwindow());//启动抓包线程
				grabThread.start();	
				
				enableGrabBtn(true);
				
				if(loginToDSNMemberSuccess&& loginToProxySuccess)
					enableDSNMemberBet(true);
				
				outputMessage.append("登录迪士尼代理成功!\n");
			}
		});
		
		btnLogin.setSize(50, 25);
		btnLogin.setLocation(DsnProxyX, DsnProxyY + 120);
		
		Label labelBetTime = new Label("距封盘还有           秒时进行投注");
		labelBetTime.setSize(200, 25);
		labelBetTime.setLocation(DsnProxyX, DsnProxyY + 150);
		
		textFieldBetTime = new TextField();
		textFieldBetTime.setSize(30, 25);
		textFieldBetTime.setLocation(DsnProxyX + 65, DsnProxyY + 150);
		
		
		
		panel.add(labelDsnProxyLogin);		
		panel.add(labelDsnProxyAddress);
		panel.add(textFieldProxyAddress);
		panel.add(labelDsnProxyAccount);
		panel.add(textFieldProxyAccount);
		panel.add(labelDsnProxyPassword);
		panel.add(textFieldProxyPassword);		
		panel.add(btnLogin);		
		panel.add(textFieldBetTime);		
		panel.add(labelBetTime);
		
		
		//dsn会员界面
		int DsnMemberX = 500;
		int DsnMemberY = 50;		
		
		
		Label labelDsnMemberLogin = new Label("迪斯尼会员登录:");
		labelDsnMemberLogin.setSize(100, 25);
		labelDsnMemberLogin.setLocation(DsnMemberX, DsnMemberY);
		
		Label labelDsnMemberAddress = new Label("网址:");
		labelDsnMemberAddress.setSize(50, 25);
		labelDsnMemberAddress.setLocation(DsnMemberX, DsnMemberY +30);
		
		
		textFieldMemberAddress = new TextField();
		textFieldMemberAddress.setSize(300,25);
		textFieldMemberAddress.setLocation(DsnMemberX + 50, DsnMemberY +30);
		textFieldMemberAddress.setText(ConfigReader.getBetAddress());
		
		Label labelDsnMemberAccount = new Label("账户:");
		labelDsnMemberAccount.setSize(50, 25);
		labelDsnMemberAccount.setLocation(DsnMemberX, DsnMemberY +60);
		
		textFieldMemberAccount = new TextField();
		textFieldMemberAccount.setSize(300,25);
		textFieldMemberAccount.setLocation(DsnMemberX + 50, DsnMemberY +60);
		textFieldMemberAccount.setText(ConfigReader.getBetAccount());	
		
		Label labelDsnMemberPassword = new Label("密码:");
		labelDsnMemberPassword.setSize(50, 25);
		labelDsnMemberPassword.setLocation(DsnMemberX, DsnMemberY +90);
		
		textFieldMemberPassword = new TextField();
		textFieldMemberPassword.setSize(300,25);
		textFieldMemberPassword.setLocation(DsnMemberX + 50, DsnMemberY +90);
		textFieldMemberPassword.setText(ConfigReader.getBetPassword());	
		textFieldMemberPassword.setEchoChar('*');
		
		
		Button btnMemberLogin = new Button("登录");
		btnMemberLogin.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(loginToDSNMemberSuccess == true)
					return;
				
				String address = textFieldMemberAddress.getText();
				String account = textFieldMemberAccount.getText();
				String password = textFieldMemberPassword.getText();
				
				dsnHttp.setLoginParams(address, account, password);

				if(!dsnHttp.login()) {
					outputMessage.append("登录迪士尼会员失败!\n");
					return;
				}
				
				loginToDSNMemberSuccess = true;
				
				ConfigWriter.updateDSNMemberAddress(address);
				ConfigWriter.updateDSNMemberAccount(account);
				ConfigWriter.updateDSNMemberPassword(password);
				
				ConfigWriter.saveTofile("common.config");

				if(loginToDSNMemberSuccess&& loginToProxySuccess)
					enableDSNMemberBet(true);
				
				outputMessage.append("登录迪士尼会员成功!\n");
			}
		});
		
		btnMemberLogin.setSize(50, 25);
		btnMemberLogin.setLocation(DsnMemberX, DsnMemberY + 120);
		

		btnBetCQSSC = new Button("正投重庆时时彩");
		btnBetCQSSC.addActionListener(new BetCQSSCListener(this));
		
		btnBetCQSSC.setSize(90, 25);
		btnBetCQSSC.setLocation(DsnMemberX,DsnMemberY + 150);
		

		Label labelPercent = new Label("投注比例:");
		labelPercent.setSize(60, 25);
		labelPercent.setLocation(DsnMemberX + 100, DsnMemberY + 150);
		
		textFieldCQSSCBetPercent = new TextField();
		textFieldCQSSCBetPercent.setSize(60, 25);
		textFieldCQSSCBetPercent.setLocation(DsnMemberX + 160, DsnMemberY + 150);
		

		btnOppositeBetCQSSC = new Button("反投重庆时时彩");
		btnOppositeBetCQSSC.addActionListener(new BetOppositeCQSSCListener(this));
		
		btnOppositeBetCQSSC.setSize(90, 25);
		btnOppositeBetCQSSC.setLocation(DsnMemberX,DsnMemberY + 180);
		
		btnStopBetCQSSC = new Button("停止投注");
		btnStopBetCQSSC.addActionListener(new StopBetCQSSCListener(this));
		
		btnStopBetCQSSC.setSize(90, 25);
		btnStopBetCQSSC.setLocation(DsnMemberX + 100, DsnMemberY + 180);
		
		
		
		
		btnBetBJSC = new Button("正投北京赛车");
		btnBetBJSC.addActionListener(new BetBJSCListener(this));
		
		btnBetBJSC.setSize(75, 25);
		btnBetBJSC.setLocation(DsnMemberX,DsnMemberY + 210);

		Label BJSClabelPercent = new Label("投注比例:");
		BJSClabelPercent.setSize(60, 25);
		BJSClabelPercent.setLocation(DsnMemberX + 100, DsnMemberY + 210);
		
		textFieldBJSCBetPercent = new TextField();
		textFieldBJSCBetPercent.setSize(60, 25);
		textFieldBJSCBetPercent.setLocation(DsnMemberX + 160, DsnMemberY + 210);

		
		btnOppositeBJSC = new Button("反投北京赛车");
		btnOppositeBJSC.addActionListener(new BetOppositeBJSCListener(this));
		
		btnOppositeBJSC.setSize(75, 25);
		btnOppositeBJSC.setLocation(DsnMemberX,DsnMemberY + 240);
		

		
		btnStopBetBJSC = new Button("停止投注");
		btnStopBetBJSC.addActionListener(new StopBetBJSCListener(this));
		
		btnStopBetBJSC.setSize(90, 25);
		btnStopBetBJSC.setLocation(DsnMemberX + 100, DsnMemberY + 240);
		
		panel.add(labelDsnMemberLogin);
		panel.add(labelDsnMemberAddress);
		panel.add(textFieldMemberAddress);
		panel.add(labelDsnMemberAccount);
		panel.add(textFieldMemberAccount);
		panel.add(labelDsnMemberPassword);
		panel.add(textFieldMemberPassword);		
		panel.add(btnMemberLogin);
		
		panel.add(btnBetCQSSC);
		panel.add(labelPercent);
		panel.add(btnOppositeBetCQSSC);
		panel.add(btnBetBJSC);
		panel.add(btnOppositeBJSC);
		panel.add(BJSClabelPercent);
		panel.add(textFieldCQSSCBetPercent);
		panel.add(textFieldBJSCBetPercent);
		panel.add(btnStopBetCQSSC);
		panel.add(btnStopBetBJSC);
		
		
		enableDSNMemberBet(false);

		
		//!lin 抓取界面使用测试
		btnStartGrabCQSSC = new Button("开始抓取重庆");
		btnStartGrabCQSSC.setSize(75, 25);
		btnStartGrabCQSSC.setLocation(50, 230);
		btnStopGrabCQSSC = new Button("停止抓取重庆");
		btnStopGrabCQSSC.setSize(75, 25);
		btnStopGrabCQSSC.setLocation(150, 230);
		
		btnStartGrabBJSC = new Button("开始抓取北京");
		btnStartGrabBJSC.setSize(75, 25);
		btnStartGrabBJSC.setLocation(50, 260);
		btnStopGrabBJSC = new Button("停止抓取北京");
		btnStopGrabBJSC.setSize(75, 25);
		btnStopGrabBJSC.setLocation(150, 260);
		

		
		btnStartGrabCQSSC.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				grabThread.startGrabCQSSC();
			}
		});
		
		btnStopGrabCQSSC.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				grabThread.stopGrabCQSSC();
			}
		});
		
		btnStartGrabBJSC.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				grabThread.startGrabBJSC();
			}
		});
		
		btnStopGrabBJSC.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				grabThread.stopGrabBJSC();
			}
		});
		
		
		enableGrabBtn(false);
					
		panel.add(btnStartGrabCQSSC);
		panel.add(btnStopGrabCQSSC);
		panel.add(btnStartGrabBJSC);
		panel.add(btnStopGrabBJSC);	
		
		labelTotalBets = new Label();
		labelTotalBets.setSize(300,25);
		labelTotalBets.setLocation(50, 400);
		labelTotalBets.setText("下单次数:0");
		
		labelSuccessBets = new Label();
		labelSuccessBets.setSize(300,25);
		labelSuccessBets.setLocation(50, 430);
		labelSuccessBets.setText("成功次数:0");
		
		labelFailBets = new Label();
		labelFailBets.setSize(300,25);
		labelFailBets.setLocation(50, 460);
		labelFailBets.setText("失败次数:0");
		
		
		panel.add(labelTotalBets);
		panel.add(labelSuccessBets);
		panel.add(labelFailBets);
		
		//!lin end
		
		
		outputMessage = new TextArea();
		outputMessage.setSize(400, 500);
		outputMessage.setLocation(50, 500);

		panel.add(outputMessage);
		

		mainFrame.setSize(1920, 1080);
		
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


