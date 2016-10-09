import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;  
import java.io.PrintStream;
import java.text.SimpleDateFormat;   
import java.util.Date;  
import java.io.File;

import javax.swing.JOptionPane;

enum BetType{
	CQSSC,
	BJSC
}


enum BetMode{
	LESSTIME,
	MIDDLETIME,
	MORETIME
}

public class autoBet{
	
	public boolean loginToProxySuccess = false;
	public boolean loginToDSNMemberSuccess = false;
	public boolean loginToWeiCaiMemberSuccess = false;
	public boolean loginToTianCaiMemberSuccess = false;
	public boolean inBet = false;
	
	public boolean inBetWeiCai = false;
	public boolean inBetTianCai = false;

	
	//代理登录界面
	public TextField textFieldProxyAddress;
	public TextField textFieldProxyAccount;
	public TextField textFieldProxyPassword;
	
	public Button btnStartGrabCQSSC;
	public Button btnStopGrabCQSSC;	
	public Button btnStartGrabBJSC;
	public Button btnStopGrabBJSC;
	public Button btnStartGrabXYNC;
	public Button btnStopGrabXYNC;
	
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
	
	//微彩会员界面
	public TextField textFieldWeiCaiMemberAddress;
	public TextField textFieldWeiCaiMemberAccount;
	public TextField textFieldWeiCaiMemberPassword;
	public TextField textFieldCQSSCBetWeiCaiPercent;
	public TextField textFieldBJSCBetWeiCaiPercent;

	
	public Button btnBetWeiCaiCQSSC;
	public Button btnOppositeBetWeiCaiCQSSC;
	public Button btnStopBetWeiCaiCQSSC;
	public Button btnBetWeiCaiBJSC;
	public Button btnOppositeWeiCaiBJSC;
	public Button btnStopBetWeiCaiBJSC;
	
	public static Label labelWeiCaiTotalBets;
	public static Label labelWeiCaiSuccessBets;
	public static Label labelWeiCaiFailBets;
	
	//添彩会员界面
	public TextField textFieldTianCaiMemberAddress;
	public TextField textFieldTianCaiMemberAccount;
	public TextField textFieldTianCaiMemberPassword;
	public TextField textFieldCQSSCBetTianCaiPercent;
	public TextField textFieldBJSCBetTianCaiPercent;

	
	public Button btnBetTianCaiCQSSC;
	public Button btnOppositeBetTianCaiCQSSC;
	public Button btnStopBetTianCaiCQSSC;
	public Button btnBetTianCaiBJSC;
	public Button btnOppositeTianCaiBJSC;
	public Button btnStopBetTianCaiBJSC;
	
	public static Label labelTianCaiTotalBets;
	public static Label labelTianCaiSuccessBets;
	public static Label labelTianCaiFailBets;
	
	
	
	
	
	
	
	public static TextArea outputMessage;
	public static Label labelTotalBets;
	public static Label labelSuccessBets;
	public static Label labelFailBets;
	public GrabThread grabThread;
	public GrabXYNCthread XYNCthread;
	TianCaiHttp tianCaiHttp = null;
	
	
	static int betTime = 12;
	
	public static int timeOut = 8*1000;
	
	public static BetMode betMode = BetMode.MIDDLETIME;
	
	public static void main(String[] args) throws Exception {
		
		
		
		
		if(betMode == BetMode.LESSTIME){
			timeOut = 6*1000;
			betTime = 5;
		}else if(betMode == BetMode.MIDDLETIME){
			timeOut = 8*1000;
			betTime = 12;
		}
		else{
			timeOut = 10*1000;
			betTime = 24;
		}
		
		
		
		
	    try {  
	    	//生成路径  
	    	File dir = new File("log");  
	        if (dir.exists()) {   
	        } 
	        else {
	        	dir.mkdirs();
	        }
	         
	        //把输出重定向到文件
	    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");//设置日期格式
	    	PrintStream ps=new PrintStream("log/" + df.format(new Date()) + ".txt");  
	    	System.setOut(ps);
	    	System.setErr(ps);
	    } catch (FileNotFoundException e) {  
	    	e.printStackTrace();
		} 
	    
	    ConfigReader.read("common.config");		
		ConfigWriter.open("common.config");
		
		
		dsnHttp.initLines();
		DsnProxyGrab.initLines();
		
		new autoBet().launchFrame();

		


	}
	
	public void setBetTime(){
		String betTime = textFieldBetTime.getText();
		
		if(Common.isNum(betTime)){
			int timeSeconds = Integer.parseInt(betTime);
			BetThread.betRemainTime = timeSeconds*1000;
			BetTianCaiThread.betRemainTime = timeSeconds;
		}
		else{
			//TODO 弹出对话框，提示输入错误
		}
	}
	
	
	public static synchronized void outputGUIMessage(String outstr){
		
		if(outstr == null)
			return;
		
		outputMessage.append(outstr);
	}
	
	public void enableGrabBtn(boolean flag){
		btnStartGrabCQSSC.setEnabled(flag);
		btnStopGrabCQSSC.setEnabled(flag);
		btnStartGrabBJSC.setEnabled(flag);
		btnStopGrabBJSC.setEnabled(flag);
		btnStartGrabXYNC.setEnabled(flag);
		btnStopGrabXYNC.setEnabled(flag);
	}
	
	
	public void enableDSNMemberBet(boolean flag){
		btnBetCQSSC.setEnabled(false);
		btnOppositeBetCQSSC.setEnabled(flag);
		btnStopBetCQSSC.setEnabled(flag);
		btnBetBJSC.setEnabled(false);
		btnOppositeBJSC.setEnabled(flag);
		btnStopBetBJSC.setEnabled(flag);
	}
	
	public void enableWeiCaiMemberBet(boolean flag){
		btnBetWeiCaiCQSSC.setEnabled(false);
		btnOppositeBetWeiCaiCQSSC.setEnabled(flag);
		btnStopBetWeiCaiCQSSC.setEnabled(flag);
		btnBetWeiCaiBJSC.setEnabled(false);
		btnOppositeWeiCaiBJSC.setEnabled(flag);
		btnStopBetWeiCaiBJSC.setEnabled(flag);
	}
	
	public void enableTianCaiMemberBet(boolean flag){
		btnBetTianCaiCQSSC.setEnabled(false);
		btnOppositeBetTianCaiCQSSC.setEnabled(flag);
		btnStopBetTianCaiCQSSC.setEnabled(flag);
		btnBetTianCaiBJSC.setEnabled(false);
		btnOppositeTianCaiBJSC.setEnabled(flag);
		btnStopBetTianCaiBJSC.setEnabled(flag);
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
					outputGUIMessage("登录迪士尼代理失败!\n");
					return;
				}
				
				loginToProxySuccess = true;
				
				ConfigWriter.updateProxyAddress(address);
				ConfigWriter.updateProxyAccount(account);
				ConfigWriter.updateProxyPassword(password);
				
				ConfigWriter.saveTofile("common.config");
				
				grabThread = new GrabThread(new GrabCQSSCwindow(), new GrabBJSCwindow());//启动抓包线程
				grabThread.start();	
				
				XYNCthread = new GrabXYNCthread();
				XYNCthread.start();
				
				enableGrabBtn(true);
				
				if(loginToDSNMemberSuccess&& loginToProxySuccess)
					enableDSNMemberBet(true);
				
				/*if(loginToWeiCaiMemberSuccess&& loginToProxySuccess)
					enableWeiCaiMemberBet(true);*/
				
				if(loginToTianCaiMemberSuccess&& loginToProxySuccess)
					enableTianCaiMemberBet(true);
				
				outputGUIMessage("登录迪士尼代理成功!\n");
			}
		});
		
		btnLogin.setSize(50, 25);
		btnLogin.setLocation(DsnProxyX, DsnProxyY + 120);
		
		Label labelBetTime = new Label("距封盘还有           秒时进行投注");
		labelBetTime.setSize(200, 25);
		labelBetTime.setLocation(DsnProxyX, DsnProxyY + 150);
		
		textFieldBetTime = new TextField();
		textFieldBetTime.setSize(30, 25);
		textFieldBetTime.setText(Integer.toString(betTime));
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
					outputGUIMessage("登录迪士尼会员失败!\n");
					return;
				}
				
				loginToDSNMemberSuccess = true;
				
				ConfigWriter.updateDSNMemberAddress(address);
				ConfigWriter.updateDSNMemberAccount(account);
				ConfigWriter.updateDSNMemberPassword(password);
				
				ConfigWriter.saveTofile("common.config");

				if(loginToDSNMemberSuccess&& loginToProxySuccess)
					enableDSNMemberBet(true);
				
				outputGUIMessage("登录迪士尼会员成功!\n");
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
		
		btnStopBetCQSSC = new Button("投注详情");
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
		

		
		btnStopBetBJSC = new Button("投注详情");
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

		

		//微彩会员界面
		/*int WeiCaiMemberX = 1000;
		int WeiCaiMemberY = 50;		
		
		
		Label labelWeiCaiMemberLogin = new Label("微彩会员登录:");
		labelWeiCaiMemberLogin.setSize(100, 25);
		labelWeiCaiMemberLogin.setLocation(WeiCaiMemberX, WeiCaiMemberY);
		
		Label labelWeiCaiMemberAddress = new Label("网址:");
		labelWeiCaiMemberAddress.setSize(50, 25);
		labelWeiCaiMemberAddress.setLocation(WeiCaiMemberX, WeiCaiMemberY +30);
		
		
		textFieldWeiCaiMemberAddress = new TextField();
		textFieldWeiCaiMemberAddress.setSize(300,25);
		textFieldWeiCaiMemberAddress.setLocation(WeiCaiMemberX + 50, WeiCaiMemberY +30);
		textFieldWeiCaiMemberAddress.setText(ConfigReader.getweicaiBetAddress());
		
		Label labelWeiCaiMemberAccount = new Label("账户:");
		labelWeiCaiMemberAccount.setSize(50, 25);
		labelWeiCaiMemberAccount.setLocation(WeiCaiMemberX, WeiCaiMemberY +60);
		
		textFieldWeiCaiMemberAccount = new TextField();
		textFieldWeiCaiMemberAccount.setSize(300,25);
		textFieldWeiCaiMemberAccount.setLocation(WeiCaiMemberX + 50, WeiCaiMemberY +60);
		textFieldWeiCaiMemberAccount.setText(ConfigReader.getweicaiBetAccount());	
		
		Label labelWeiCaiMemberPassword = new Label("密码:");
		labelWeiCaiMemberPassword.setSize(50, 25);
		labelWeiCaiMemberPassword.setLocation(WeiCaiMemberX, WeiCaiMemberY +90);
		
		textFieldWeiCaiMemberPassword = new TextField();
		textFieldWeiCaiMemberPassword.setSize(300,25);
		textFieldWeiCaiMemberPassword.setLocation(WeiCaiMemberX + 50, WeiCaiMemberY +90);
		textFieldWeiCaiMemberPassword.setText(ConfigReader.getweicaiBetPassword());	
		textFieldWeiCaiMemberPassword.setEchoChar('*');
		
		
		Button btnWeiCaiMemberLogin = new Button("登录");
		btnWeiCaiMemberLogin.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(loginToWeiCaiMemberSuccess == true)
					return;
				
				String address = textFieldWeiCaiMemberAddress.getText();
				String account = textFieldWeiCaiMemberAccount.getText();
				String password = textFieldWeiCaiMemberPassword.getText();
				
				WeiCaiHttp.setLoginParams(address, account, password);

				if(!WeiCaiHttp.login()) {
					outputGUIMessage("登录微彩会员失败!\n");
					return;
				}
				
				loginToWeiCaiMemberSuccess = true;
				
				ConfigWriter.updateWeiCaiMemberAddress(address);
				ConfigWriter.updateWeiCaiMemberAccount(account);
				ConfigWriter.updateWeiCaiMemberPassword(password);
				
				ConfigWriter.saveTofile("common.config");

				if(loginToWeiCaiMemberSuccess&& loginToProxySuccess)
					enableWeiCaiMemberBet(true);
				
				outputGUIMessage("登录微彩会员成功!\n");
			}
		});
		
		btnWeiCaiMemberLogin.setSize(50, 25);
		btnWeiCaiMemberLogin.setLocation(WeiCaiMemberX, WeiCaiMemberY + 120);
		

		btnBetWeiCaiCQSSC = new Button("正投重庆时时彩");
		btnBetWeiCaiCQSSC.addActionListener(new BetWeiCaiOppositeCQSSCListener(this));
		
		btnBetWeiCaiCQSSC.setSize(90, 25);
		btnBetWeiCaiCQSSC.setLocation(WeiCaiMemberX,WeiCaiMemberY + 150);
		

		Label labelWeiCaiPercent = new Label("投注比例:");
		labelWeiCaiPercent.setSize(60, 25);
		labelWeiCaiPercent.setLocation(WeiCaiMemberX + 100, WeiCaiMemberY + 150);
		
		textFieldCQSSCBetWeiCaiPercent = new TextField();
		textFieldCQSSCBetWeiCaiPercent.setSize(60, 25);
		textFieldCQSSCBetWeiCaiPercent.setLocation(WeiCaiMemberX + 160, WeiCaiMemberY + 150);
		

		btnOppositeBetWeiCaiCQSSC = new Button("反投重庆时时彩");
		btnOppositeBetWeiCaiCQSSC.addActionListener(new BetWeiCaiOppositeCQSSCListener(this));
		
		btnOppositeBetWeiCaiCQSSC.setSize(90, 25);
		btnOppositeBetWeiCaiCQSSC.setLocation(WeiCaiMemberX,WeiCaiMemberY + 180);
		
		btnStopBetWeiCaiCQSSC = new Button("投注详情");
		btnStopBetWeiCaiCQSSC.addActionListener(new StopBetWeiCaiCQSSCListener(this));
		
		btnStopBetWeiCaiCQSSC.setSize(90, 25);
		btnStopBetWeiCaiCQSSC.setLocation(WeiCaiMemberX + 100, WeiCaiMemberY + 180);
		
		
		
		
		btnBetWeiCaiBJSC = new Button("正投北京赛车");
		btnBetWeiCaiBJSC.addActionListener(new BetBJSCListener(this));
		
		btnBetWeiCaiBJSC.setSize(75, 25);
		btnBetWeiCaiBJSC.setLocation(WeiCaiMemberX,WeiCaiMemberY + 210);

		Label BJSClabelWeiCaiPercent = new Label("投注比例:");
		BJSClabelWeiCaiPercent.setSize(60, 25);
		BJSClabelWeiCaiPercent.setLocation(WeiCaiMemberX + 100, WeiCaiMemberY + 210);
		
		textFieldBJSCBetWeiCaiPercent = new TextField();
		textFieldBJSCBetWeiCaiPercent.setSize(60, 25);
		textFieldBJSCBetWeiCaiPercent.setLocation(WeiCaiMemberX + 160, WeiCaiMemberY + 210);

		
		btnOppositeWeiCaiBJSC = new Button("反投北京赛车");
		btnOppositeWeiCaiBJSC.addActionListener(new BetWeiCaiOppositeBJSCListener(this));
		
		btnOppositeWeiCaiBJSC.setSize(75, 25);
		btnOppositeWeiCaiBJSC.setLocation(WeiCaiMemberX,WeiCaiMemberY + 240);
		

		
		btnStopBetWeiCaiBJSC = new Button("投注详情");
		btnStopBetWeiCaiBJSC.addActionListener(new StopBetWeiCaiBJSCListener(this));
		
		btnStopBetWeiCaiBJSC.setSize(90, 25);
		btnStopBetWeiCaiBJSC.setLocation(WeiCaiMemberX + 100, WeiCaiMemberY + 240);
		
		
		
		labelWeiCaiTotalBets = new Label();
		labelWeiCaiTotalBets.setSize(300,25);
		labelWeiCaiTotalBets.setLocation(WeiCaiMemberX, 400);
		labelWeiCaiTotalBets.setText("下单次数:0");
		
		labelWeiCaiSuccessBets = new Label();
		labelWeiCaiSuccessBets.setSize(300,25);
		labelWeiCaiSuccessBets.setLocation(WeiCaiMemberX, 430);
		labelWeiCaiSuccessBets.setText("成功次数:0");
		
		labelWeiCaiFailBets = new Label();
		labelWeiCaiFailBets.setSize(300,25);
		labelWeiCaiFailBets.setLocation(WeiCaiMemberX, 460);
		labelWeiCaiFailBets.setText("失败次数:0");
		
		
		panel.add(labelWeiCaiTotalBets);
		panel.add(labelWeiCaiSuccessBets);
		panel.add(labelWeiCaiFailBets);
		
		
		
		panel.add(labelWeiCaiMemberLogin);
		panel.add(labelWeiCaiMemberAddress);
		panel.add(textFieldWeiCaiMemberAddress);
		panel.add(labelWeiCaiMemberAccount);
		panel.add(textFieldWeiCaiMemberAccount);
		panel.add(labelWeiCaiMemberPassword);
		panel.add(textFieldWeiCaiMemberPassword);		
		panel.add(btnWeiCaiMemberLogin);
		
		panel.add(btnBetWeiCaiCQSSC);
		panel.add(labelWeiCaiPercent);
		panel.add(btnOppositeBetWeiCaiCQSSC);
		panel.add(btnBetWeiCaiBJSC);
		panel.add(btnOppositeWeiCaiBJSC);
		panel.add(BJSClabelWeiCaiPercent);
		panel.add(textFieldCQSSCBetWeiCaiPercent);
		panel.add(textFieldBJSCBetWeiCaiPercent);
		panel.add(btnStopBetWeiCaiCQSSC);
		panel.add(btnStopBetWeiCaiBJSC);
		
		
		enableWeiCaiMemberBet(false);*/
		
		//添彩会员界面
		int TianCaiMemberX = 1000;
		int TianCaiMemberY = 50;		
		
		
		Label labelTianCaiMemberLogin = new Label("添彩会员登录:");
		labelTianCaiMemberLogin.setSize(100, 25);
		labelTianCaiMemberLogin.setLocation(TianCaiMemberX, TianCaiMemberY);
		
		Label labelTianCaiMemberAddress = new Label("网址:");
		labelTianCaiMemberAddress.setSize(50, 25);
		labelTianCaiMemberAddress.setLocation(TianCaiMemberX, TianCaiMemberY +30);
		
		
		textFieldTianCaiMemberAddress = new TextField();
		textFieldTianCaiMemberAddress.setSize(300,25);
		textFieldTianCaiMemberAddress.setLocation(TianCaiMemberX + 50, TianCaiMemberY +30);
		textFieldTianCaiMemberAddress.setText(ConfigReader.gettiancaiBetAddress());
		
		Label labelTianCaiMemberAccount = new Label("账户:");
		labelTianCaiMemberAccount.setSize(50, 25);
		labelTianCaiMemberAccount.setLocation(TianCaiMemberX, TianCaiMemberY +60);
		
		textFieldTianCaiMemberAccount = new TextField();
		textFieldTianCaiMemberAccount.setSize(300,25);
		textFieldTianCaiMemberAccount.setLocation(TianCaiMemberX + 50, TianCaiMemberY +60);
		textFieldTianCaiMemberAccount.setText(ConfigReader.gettiancaiBetAccount());	
		
		Label labelTianCaiMemberPassword = new Label("密码:");
		labelTianCaiMemberPassword.setSize(50, 25);
		labelTianCaiMemberPassword.setLocation(TianCaiMemberX, TianCaiMemberY +90);
		
		textFieldTianCaiMemberPassword = new TextField();
		textFieldTianCaiMemberPassword.setSize(300,25);
		textFieldTianCaiMemberPassword.setLocation(TianCaiMemberX + 50, TianCaiMemberY +90);
		textFieldTianCaiMemberPassword.setText(ConfigReader.gettiancaiBetPassword());	
		textFieldTianCaiMemberPassword.setEchoChar('*');
		
		
		Button btnTianCaiMemberLogin = new Button("登录");
		btnTianCaiMemberLogin.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(loginToTianCaiMemberSuccess == true)
					return;
				
				String address = textFieldTianCaiMemberAddress.getText();
				String account = textFieldTianCaiMemberAccount.getText();
				String password = textFieldTianCaiMemberPassword.getText();
				
				tianCaiHttp = new TianCaiHttp();
				
				tianCaiHttp.setLoginParams(address, account, password);

				if(!tianCaiHttp.login()) {
					outputGUIMessage("登录添彩会员失败!\n");
					return;
				}
				
				loginToTianCaiMemberSuccess = true;
				
				ConfigWriter.updateTianCaiMemberAddress(address);
				ConfigWriter.updateTianCaiMemberAccount(account);
				ConfigWriter.updateTianCaiMemberPassword(password);
				
				ConfigWriter.saveTofile("common.config");

				if(loginToTianCaiMemberSuccess&& loginToProxySuccess)
					enableTianCaiMemberBet(true);
				
				outputGUIMessage("登录添彩会员成功!\n");
				
			}
		});
		
		btnTianCaiMemberLogin.setSize(50, 25);
		btnTianCaiMemberLogin.setLocation(TianCaiMemberX, TianCaiMemberY + 120);
		

		btnBetTianCaiCQSSC = new Button("正投重庆时时彩");
		btnBetTianCaiCQSSC.addActionListener(new BetTianCaiOppositeCQSSCListener(this));
		
		btnBetTianCaiCQSSC.setSize(90, 25);
		btnBetTianCaiCQSSC.setLocation(TianCaiMemberX,TianCaiMemberY + 150);
		

		Label labelTianCaiPercent = new Label("投注比例:");
		labelTianCaiPercent.setSize(60, 25);
		labelTianCaiPercent.setLocation(TianCaiMemberX + 100, TianCaiMemberY + 150);
		
		textFieldCQSSCBetTianCaiPercent = new TextField();
		textFieldCQSSCBetTianCaiPercent.setSize(60, 25);
		textFieldCQSSCBetTianCaiPercent.setLocation(TianCaiMemberX + 160, TianCaiMemberY + 150);
		

		btnOppositeBetTianCaiCQSSC = new Button("反投重庆时时彩");
		btnOppositeBetTianCaiCQSSC.addActionListener(new BetTianCaiOppositeCQSSCListener(this));
		
		btnOppositeBetTianCaiCQSSC.setSize(90, 25);
		btnOppositeBetTianCaiCQSSC.setLocation(TianCaiMemberX,TianCaiMemberY + 180);
		
		btnStopBetTianCaiCQSSC = new Button("投注详情");
		btnStopBetTianCaiCQSSC.addActionListener(new StopBetTianCaiCQSSCListener(this));
		
		btnStopBetTianCaiCQSSC.setSize(90, 25);
		btnStopBetTianCaiCQSSC.setLocation(TianCaiMemberX + 100, TianCaiMemberY + 180);
		
		
		
		
		btnBetTianCaiBJSC = new Button("正投北京赛车");
		btnBetTianCaiBJSC.addActionListener(new BetBJSCListener(this));
		
		btnBetTianCaiBJSC.setSize(75, 25);
		btnBetTianCaiBJSC.setLocation(TianCaiMemberX,TianCaiMemberY + 210);

		Label BJSClabelTianCaiPercent = new Label("投注比例:");
		BJSClabelTianCaiPercent.setSize(60, 25);
		BJSClabelTianCaiPercent.setLocation(TianCaiMemberX + 100, TianCaiMemberY + 210);
		
		textFieldBJSCBetTianCaiPercent = new TextField();
		textFieldBJSCBetTianCaiPercent.setSize(60, 25);
		textFieldBJSCBetTianCaiPercent.setLocation(TianCaiMemberX + 160, TianCaiMemberY + 210);

		
		btnOppositeTianCaiBJSC = new Button("反投北京赛车");
		btnOppositeTianCaiBJSC.addActionListener(new BetTianCaiOppositeBJSCListener(this));
		
		btnOppositeTianCaiBJSC.setSize(75, 25);
		btnOppositeTianCaiBJSC.setLocation(TianCaiMemberX,TianCaiMemberY + 240);
		

		
		btnStopBetTianCaiBJSC = new Button("投注详情");
		btnStopBetTianCaiBJSC.addActionListener(new StopBetTianCaiBJSCListener(this));
		
		btnStopBetTianCaiBJSC.setSize(90, 25);
		btnStopBetTianCaiBJSC.setLocation(TianCaiMemberX + 100, TianCaiMemberY + 240);
		
		
		
		labelTianCaiTotalBets = new Label();
		labelTianCaiTotalBets.setSize(300,25);
		labelTianCaiTotalBets.setLocation(TianCaiMemberX, 400);
		labelTianCaiTotalBets.setText("下单次数:0");
		
		labelTianCaiSuccessBets = new Label();
		labelTianCaiSuccessBets.setSize(300,25);
		labelTianCaiSuccessBets.setLocation(TianCaiMemberX, 430);
		labelTianCaiSuccessBets.setText("成功次数:0");
		
		labelTianCaiFailBets = new Label();
		labelTianCaiFailBets.setSize(300,25);
		labelTianCaiFailBets.setLocation(TianCaiMemberX, 460);
		labelTianCaiFailBets.setText("失败次数:0");
		
		
/*		panel.add(labelTianCaiTotalBets);
		panel.add(labelTianCaiSuccessBets);
		panel.add(labelTianCaiFailBets);*/
		
		
		
		panel.add(labelTianCaiMemberLogin);
		panel.add(labelTianCaiMemberAddress);
		panel.add(textFieldTianCaiMemberAddress);
		panel.add(labelTianCaiMemberAccount);
		panel.add(textFieldTianCaiMemberAccount);
		panel.add(labelTianCaiMemberPassword);
		panel.add(textFieldTianCaiMemberPassword);		
		panel.add(btnTianCaiMemberLogin);
		
		panel.add(btnBetTianCaiCQSSC);
		panel.add(labelTianCaiPercent);
		panel.add(btnOppositeBetTianCaiCQSSC);
		panel.add(btnBetTianCaiBJSC);
		panel.add(btnOppositeTianCaiBJSC);
		panel.add(BJSClabelTianCaiPercent);
		panel.add(textFieldCQSSCBetTianCaiPercent);
		panel.add(textFieldBJSCBetTianCaiPercent);
		panel.add(btnStopBetTianCaiCQSSC);
		panel.add(btnStopBetTianCaiBJSC);
		
		
		enableTianCaiMemberBet(false);
		
		
		
		
		
		//!lin 抓取界面使用测试
		btnStartGrabCQSSC = new Button("开抓重庆时彩");
		btnStartGrabCQSSC.setSize(75, 25);
		btnStartGrabCQSSC.setLocation(50, 230);
		btnStopGrabCQSSC = new Button("停抓重庆时彩");
		btnStopGrabCQSSC.setSize(75, 25);
		btnStopGrabCQSSC.setLocation(150, 230);
		
		btnStartGrabBJSC = new Button("开抓北京赛车");
		btnStartGrabBJSC.setSize(75, 25);
		btnStartGrabBJSC.setLocation(50, 260);
		btnStopGrabBJSC = new Button("停抓北京赛车");
		btnStopGrabBJSC.setSize(75, 25);
		btnStopGrabBJSC.setLocation(150, 260);
		
		btnStartGrabXYNC = new Button("开抓幸运农场");
		btnStartGrabXYNC.setSize(75,25);
		btnStartGrabXYNC.setLocation(50, 290);
		
		btnStopGrabXYNC = new Button("停抓幸运农场");
		btnStopGrabXYNC.setSize(75,25);
		btnStopGrabXYNC.setLocation(150, 290);
		
		

		
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
		
		btnStartGrabXYNC.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				XYNCthread.startGrabXYNC();
			}
		});
		
		btnStopGrabXYNC.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				XYNCthread.stopGrabXYNC();
			}
		});
		
		enableGrabBtn(false);
					
		panel.add(btnStartGrabCQSSC);
		panel.add(btnStopGrabCQSSC);
		panel.add(btnStartGrabBJSC);
		panel.add(btnStopGrabBJSC);	
		panel.add(btnStartGrabXYNC);	
		panel.add(btnStopGrabXYNC);
		
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
		
		
/*		panel.add(labelTotalBets);
		panel.add(labelSuccessBets);
		panel.add(labelFailBets);*/
		
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
                    	int n = JOptionPane.showConfirmDialog(null, "确认退出吗?", "退出程序", JOptionPane.YES_NO_OPTION);  
                        if (n == JOptionPane.YES_OPTION) {
                        	System.out.println(outputMessage.getText()); //退出时保存界面输出到log
                        	System.exit(0);  
                        }  
                    }
                }
        );
	}
			
}


