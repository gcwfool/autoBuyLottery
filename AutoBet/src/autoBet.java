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
	BJSC,
	XYNC,
	GXKLSF,
	GDKLSF,
	GD11X5,
	XJSSC,
	TJSSC
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
	public boolean inBetXYNC = false;
	public boolean inBetGXKLSF = false;
	public boolean inBetGDKLSF = false;
	public boolean inBetGD11X5 = false;
	public boolean inBetXJSSC = false;
	public boolean inBetTJSSC = false;
	
	
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
	public Button btnBetAmountWindowXYNC;
	public Button btnBetAmountWindowGXKLSF;
	public Button btnBetAmountWindowGDKLSF;
	public Button btnBetAmountWindowGD11X5;
	public Button btnBetAmountWindowXJSSC;
	public Button btnBetAmountWindowTJSSC;
	
	//迪斯尼会员界面
	public TextField textFieldMemberAddress;
	public TextField textFieldMemberAccount;
	public TextField textFieldMemberPassword;
	public TextField textFieldCQSSCBetPercent;
	public TextField textFieldBJSCBetPercent;
	public TextField textFieldGXKLSFBetPercent;
	public TextField textFieldGDKLSFBetPercent;
	public TextField textFieldGD11X5BetPercent;
	public TextField textFieldXJSSCBetPercent;
	public TextField textFieldTJSSCBetPercent;
	public TextField textFieldBetTime;
	
	public Button btnBetCQSSC;
	public Button btnOppositeBetCQSSC;
	public Button btnStopBetCQSSC;
	public Button btnBetBJSC;
	public Button btnOppositeBJSC;
	public Button btnStopBetBJSC;
	
	public Button btnBetOppositeXYNC;
	public TextField textFieldXYNCBetPercent;

	
	public Button btnBetOppositeGXKLSF;
	public Button btnBetOppositeGDKLSF;
	public Button btnBetOppositeGD11X5;
	public Button btnBetOppositeXJSSC;
	public Button btnBetOppositeTJSSC;
	
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
	
	
	
	public static Button btnLogin;
	
	
	public static Client client;
	
	
	public static TextArea outputMessage;
	public static Label labelTotalBets;
	public static Label labelSuccessBets;
	public static Label labelFailBets;
	public GrabThread grabThread;
	public ReloginThread reloginThread;
	public GrabXYNCthread XYNCthread;
	TianCaiHttp tianCaiHttp = null;
	
	
	static int betTime = 12;
	
	public static int betTimeOut = 8*1000;
	
	public static BetMode betMode = BetMode.MIDDLETIME;
	
	public static void main(String[] args) throws Exception {
		
		
		
		
		if(betMode == BetMode.LESSTIME){
			betTimeOut = 15*1000;
			betTime = 5;
		}else if(betMode == BetMode.MIDDLETIME){
			betTimeOut = 15*1000;
			betTime = 20;
		}
		else{
			betTimeOut = 20*1000;
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
		
		
		client = new Client();
		
		
		//client.start();
		
		

		
		new autoBet().launchFrame();

		


	}
	
	public void setBetTime(){
		String betTime = textFieldBetTime.getText();
		
		if(Common.isNum(betTime)){
			int timeSeconds = Integer.parseInt(betTime);
			BetThread.betRemainTime = timeSeconds*1000;
			BetXYNCThread.betRemainTime =  timeSeconds*1000;
			BetGDKLSFThread.betRemainTime =  timeSeconds*1000;
			BetGXKLSFThread.betRemainTime =  timeSeconds*1000;
			BetGD11X5Thread.betRemainTime =  timeSeconds*1000;
			BetXJSSCThread.betRemainTime =  timeSeconds*1000;
			BetTJSSCThread.betRemainTime =  timeSeconds*1000;
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
		btnBetOppositeXYNC.setEnabled(flag);
		btnBetOppositeGXKLSF.setEnabled(flag);
		btnBetOppositeGDKLSF.setEnabled(flag);
		btnBetOppositeGD11X5.setEnabled(flag);
		btnBetOppositeXJSSC.setEnabled(flag);
		btnBetOppositeTJSSC.setEnabled(flag);
		btnBetAmountWindowXYNC.setEnabled(flag);
		btnBetAmountWindowGXKLSF.setEnabled(flag);
		btnBetAmountWindowGDKLSF.setEnabled(flag);
		btnBetAmountWindowGD11X5.setEnabled(flag);
		btnBetAmountWindowXJSSC.setEnabled(flag);
		btnBetAmountWindowTJSSC.setEnabled(flag);
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
	    

	    
		Label labelDsnProxyLogin = new Label("连接服务器:");
		labelDsnProxyLogin.setSize(100, 25);
		labelDsnProxyLogin.setLocation(DsnProxyX, DsnProxyY);
		
		Label labelDsnProxyAddress = new Label("网址:");
		labelDsnProxyAddress.setSize(50, 25);
		labelDsnProxyAddress.setLocation(DsnProxyX, DsnProxyY +30);
		
		
		textFieldProxyAddress = new TextField();
		textFieldProxyAddress.setSize(300,25);
		textFieldProxyAddress.setLocation(DsnProxyX + 50, DsnProxyY +30);
		textFieldProxyAddress.setText(ConfigReader.getServerAddress());
		
		
		Label labelDsnProxyAccount = new Label("端口:");
		labelDsnProxyAccount.setSize(50, 25);
		labelDsnProxyAccount.setLocation(DsnProxyX, DsnProxyY +60);
		
		textFieldProxyAccount = new TextField();
		textFieldProxyAccount.setSize(300,25);
		textFieldProxyAccount.setLocation(DsnProxyX + 50, DsnProxyY +60);
		textFieldProxyAccount.setText(ConfigReader.getServerPort());

		Label labelDsnProxyPassword = new Label("密码:");
		labelDsnProxyPassword.setSize(50, 25);
		labelDsnProxyPassword.setLocation(DsnProxyX, DsnProxyY +90);
		
		textFieldProxyPassword = new TextField();
		textFieldProxyPassword.setSize(300,25);
		textFieldProxyPassword.setLocation(DsnProxyX + 50, DsnProxyY +90);
		textFieldProxyPassword.setText(ConfigReader.getProxyPassword());
		textFieldProxyPassword.setEchoChar('*');
		

		
		
		
		btnLogin = new Button("连接");
		btnLogin.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(loginToProxySuccess == true)
					return;
				
				String address = textFieldProxyAddress.getText();
				String port = textFieldProxyAccount.getText();
				//String password = textFieldProxyPassword.getText();
				
				//DsnProxyGrab.setLoginParams(address, account, password);
				
				if(!client.connectToSever(address, port)) {
					outputGUIMessage("连接服务器失败!\n");
					return;
				}
				
				loginToProxySuccess = true;
				
				ConfigWriter.updateServerAddress(address);
				ConfigWriter.updateServerPort(port);
				
				ConfigWriter.saveTofile("common.config");
				
				
/*				ConfigWriter.updateProxyAddress(address);
				ConfigWriter.updateProxyAccount(account);
				ConfigWriter.updateProxyPassword(password);
				
				ConfigWriter.saveTofile("common.config");
				
				grabThread = new GrabThread(new GrabCQSSCwindow(), new GrabBJSCwindow());//启动抓包线程
				grabThread.start();	
				
				XYNCthread = new GrabXYNCthread();
				XYNCthread.start();
				
				enableGrabBtn(true);*/
				
				if(loginToDSNMemberSuccess&& loginToProxySuccess)
					enableDSNMemberBet(true);
				
				/*if(loginToWeiCaiMemberSuccess&& loginToProxySuccess)
					enableWeiCaiMemberBet(true);*/
				
				if(loginToTianCaiMemberSuccess&& loginToProxySuccess)
					enableTianCaiMemberBet(true);
				
				client.start();
				
				outputGUIMessage("连接服务器成功!\n");
				
				btnLogin.setEnabled(false);
				//btnLogin.setText("已连接");
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
		//panel.add(labelDsnProxyPassword);
		//panel.add(textFieldProxyPassword);		
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
				
				
				
				reloginThread = new ReloginThread();
				
				reloginThread.start();
				
				
				loginToDSNMemberSuccess = true;
				
				ConfigWriter.updateDSNMemberAddress(address);
				ConfigWriter.updateDSNMemberAccount(account);
				ConfigWriter.updateDSNMemberPassword(password);
				
				ConfigWriter.saveTofile("common.config");

				if(loginToDSNMemberSuccess&&loginToProxySuccess)
					enableDSNMemberBet(true);
				
				outputGUIMessage("登录迪士尼会员成功!\n");
			}
		});
		
		btnMemberLogin.setSize(50, 25);
		btnMemberLogin.setLocation(DsnMemberX, DsnMemberY + 120);
		

		btnBetCQSSC = new Button("正投重庆时时彩");
		btnBetCQSSC.addActionListener(new BetCQSSCListener(this, client));
		
		btnBetCQSSC.setSize(90, 25);
		btnBetCQSSC.setLocation(DsnMemberX,DsnMemberY + 150);
		

		Label labelPercent = new Label("投注比例:");
		labelPercent.setSize(60, 25);
		labelPercent.setLocation(DsnMemberX + 100, DsnMemberY + 150);
		
		textFieldCQSSCBetPercent = new TextField();
		textFieldCQSSCBetPercent.setSize(60, 25);
		textFieldCQSSCBetPercent.setLocation(DsnMemberX + 160, DsnMemberY + 150);
		

		btnOppositeBetCQSSC = new Button("反投重庆时时彩");
		btnOppositeBetCQSSC.addActionListener(new BetOppositeCQSSCListener(this, client));
		
		btnOppositeBetCQSSC.setSize(90, 25);
		btnOppositeBetCQSSC.setLocation(DsnMemberX,DsnMemberY + 150);
		
		btnStopBetCQSSC = new Button("投注金额");
		btnStopBetCQSSC.addActionListener(new StopBetCQSSCListener(this));
		
		btnStopBetCQSSC.setSize(90, 25);
		btnStopBetCQSSC.setLocation(DsnMemberX + 220, DsnMemberY + 150);
		
		
		
		
		btnBetBJSC = new Button("正投北京赛车");
		btnBetBJSC.addActionListener(new BetBJSCListener(this, client));
		
		btnBetBJSC.setSize(75, 25);
		btnBetBJSC.setLocation(DsnMemberX,DsnMemberY + 210);

		Label BJSClabelPercent = new Label("投注比例:");
		BJSClabelPercent.setSize(60, 25);
		BJSClabelPercent.setLocation(DsnMemberX + 100, DsnMemberY + 180);
		
		textFieldBJSCBetPercent = new TextField();
		textFieldBJSCBetPercent.setSize(60, 25);
		textFieldBJSCBetPercent.setLocation(DsnMemberX + 160, DsnMemberY + 180);

		
		btnOppositeBJSC = new Button("反投北京赛车");
		btnOppositeBJSC.addActionListener(new BetOppositeBJSCListener(this, client));
		
		btnOppositeBJSC.setSize(75, 25);
		btnOppositeBJSC.setLocation(DsnMemberX,DsnMemberY + 180);
		

		
		btnStopBetBJSC = new Button("投注金额");
		btnStopBetBJSC.addActionListener(new StopBetBJSCListener(this));
		
		btnStopBetBJSC.setSize(90, 25);
		btnStopBetBJSC.setLocation(DsnMemberX + 220, DsnMemberY + 180);
		
		
		
		//XYNC幸运农场
		btnBetOppositeXYNC = new Button("反投幸运农场");
		btnBetOppositeXYNC.addActionListener(new BetXYNCListener(this, client));
		
		btnBetOppositeXYNC.setSize(75, 25);
		btnBetOppositeXYNC.setLocation(DsnMemberX,DsnMemberY + 210);

		Label XYNClabelPercent = new Label("投注比例:");
		XYNClabelPercent.setSize(60, 25);
		XYNClabelPercent.setLocation(DsnMemberX + 100, DsnMemberY + 210);
		
		textFieldXYNCBetPercent = new TextField();
		textFieldXYNCBetPercent.setSize(60, 25);
		textFieldXYNCBetPercent.setLocation(DsnMemberX + 160, DsnMemberY + 210);
		
		
		btnBetAmountWindowXYNC = new Button("投注金额");
		btnBetAmountWindowXYNC.addActionListener(new BetAmountDetailXYNCListener(this));
		
		btnBetAmountWindowXYNC.setSize(90, 25);
		btnBetAmountWindowXYNC.setLocation(DsnMemberX + 220, DsnMemberY + 210);
		
		
		
		
		//GXKLSF
		btnBetOppositeGXKLSF = new Button("反投广西快乐十分");
		btnBetOppositeGXKLSF.addActionListener(new BetGXKLSFListener(this, client));
		
		btnBetOppositeGXKLSF.setSize(75, 25);
		btnBetOppositeGXKLSF.setLocation(DsnMemberX,DsnMemberY + 240);

		Label GXKLSFlabelPercent = new Label("投注比例:");
		GXKLSFlabelPercent.setSize(60, 25);
		GXKLSFlabelPercent.setLocation(DsnMemberX + 100, DsnMemberY + 240);
		
		textFieldGXKLSFBetPercent = new TextField();
		textFieldGXKLSFBetPercent.setSize(60, 25);
		textFieldGXKLSFBetPercent.setLocation(DsnMemberX + 160, DsnMemberY + 240);
		
		
		btnBetAmountWindowGXKLSF = new Button("投注金额");
		btnBetAmountWindowGXKLSF.addActionListener(new BetAmountDetailGXKLSFListener(this));
		
		btnBetAmountWindowGXKLSF.setSize(90, 25);
		btnBetAmountWindowGXKLSF.setLocation(DsnMemberX + 220, DsnMemberY + 240);
		
		
		//GDKLSF
	
		btnBetOppositeGDKLSF = new Button("反投广东快乐十分");
		btnBetOppositeGDKLSF.addActionListener(new BetGDKLSFListener(this, client));
		
		btnBetOppositeGDKLSF.setSize(75, 25);
		btnBetOppositeGDKLSF.setLocation(DsnMemberX,DsnMemberY + 270);

		Label GDKLSFlabelPercent = new Label("投注比例:");
		GDKLSFlabelPercent.setSize(60, 25);
		GDKLSFlabelPercent.setLocation(DsnMemberX + 100, DsnMemberY + 270);
		
		textFieldGDKLSFBetPercent = new TextField();
		textFieldGDKLSFBetPercent.setSize(60, 25);
		textFieldGDKLSFBetPercent.setLocation(DsnMemberX + 160, DsnMemberY + 270);
		
		
		btnBetAmountWindowGDKLSF = new Button("投注金额");
		btnBetAmountWindowGDKLSF.addActionListener(new BetAmountDetailGXKLSFListener(this));
		
		btnBetAmountWindowGDKLSF.setSize(90, 25);
		btnBetAmountWindowGDKLSF.setLocation(DsnMemberX + 220, DsnMemberY + 270);
		
		
		
		
		//GD11X5
		
		btnBetOppositeGD11X5 = new Button("反投广东11选5");
		btnBetOppositeGD11X5.addActionListener(new BetGD11X5Listener(this, client));
		
		btnBetOppositeGD11X5.setSize(75, 25);
		btnBetOppositeGD11X5.setLocation(DsnMemberX,DsnMemberY + 300);

		Label GD11X5labelPercent = new Label("投注比例:");
		GD11X5labelPercent.setSize(60, 25);
		GD11X5labelPercent.setLocation(DsnMemberX + 100, DsnMemberY + 300);
		
		textFieldGD11X5BetPercent = new TextField();
		textFieldGD11X5BetPercent.setSize(60, 25);
		textFieldGD11X5BetPercent.setLocation(DsnMemberX + 160, DsnMemberY + 300);
		
		
		btnBetAmountWindowGD11X5 = new Button("投注金额");
		btnBetAmountWindowGD11X5.addActionListener(new BetAmountDetailGXKLSFListener(this));
		
		btnBetAmountWindowGD11X5.setSize(90, 25);
		btnBetAmountWindowGD11X5.setLocation(DsnMemberX + 220, DsnMemberY + 300);
		
		
		
		//XJSSC
		
		btnBetOppositeXJSSC = new Button("反投新疆时时彩");
		btnBetOppositeXJSSC.addActionListener(new BetXJSSCListener(this, client));
		
		btnBetOppositeXJSSC.setSize(75, 25);
		btnBetOppositeXJSSC.setLocation(DsnMemberX,DsnMemberY + 330);

		Label XJSSClabelPercent = new Label("投注比例:");
		XJSSClabelPercent.setSize(60, 25);
		XJSSClabelPercent.setLocation(DsnMemberX + 100, DsnMemberY + 330);
		
		textFieldXJSSCBetPercent = new TextField();
		textFieldXJSSCBetPercent.setSize(60, 25);
		textFieldXJSSCBetPercent.setLocation(DsnMemberX + 160, DsnMemberY + 330);
		
		
		btnBetAmountWindowXJSSC = new Button("投注金额");
		btnBetAmountWindowXJSSC.addActionListener(new BetAmountDetailGXKLSFListener(this));
		
		btnBetAmountWindowXJSSC.setSize(90, 25);
		btnBetAmountWindowXJSSC.setLocation(DsnMemberX + 220, DsnMemberY + 330);
		
		
		
		
		//TJSSC
		btnBetOppositeTJSSC = new Button("反投天津时时彩");
		btnBetOppositeTJSSC.addActionListener(new BetTJSSCListener(this, client));
		
		btnBetOppositeTJSSC.setSize(75, 25);
		btnBetOppositeTJSSC.setLocation(DsnMemberX,DsnMemberY + 360);

		Label TJSSClabelPercent = new Label("投注比例:");
		TJSSClabelPercent.setSize(60, 25);
		TJSSClabelPercent.setLocation(DsnMemberX + 100, DsnMemberY + 360);
		
		textFieldTJSSCBetPercent = new TextField();
		textFieldTJSSCBetPercent.setSize(60, 25);
		textFieldTJSSCBetPercent.setLocation(DsnMemberX + 160, DsnMemberY + 360);
		
		
		btnBetAmountWindowTJSSC = new Button("投注金额");
		btnBetAmountWindowTJSSC.addActionListener(new BetAmountDetailGXKLSFListener(this));
		
		btnBetAmountWindowTJSSC.setSize(90, 25);
		btnBetAmountWindowTJSSC.setLocation(DsnMemberX + 220, DsnMemberY + 360);
		
		
		panel.add(labelDsnMemberLogin);
		panel.add(labelDsnMemberAddress);
		panel.add(textFieldMemberAddress);
		panel.add(labelDsnMemberAccount);
		panel.add(textFieldMemberAccount);
		panel.add(labelDsnMemberPassword);
		panel.add(textFieldMemberPassword);		
		panel.add(btnMemberLogin);
		
		//panel.add(btnBetCQSSC);
		panel.add(labelPercent);
		panel.add(btnOppositeBetCQSSC);
		//panel.add(btnBetBJSC);
		panel.add(btnOppositeBJSC);
		panel.add(BJSClabelPercent);
		panel.add(textFieldCQSSCBetPercent);
		panel.add(textFieldBJSCBetPercent);
		panel.add(btnStopBetCQSSC);
		panel.add(btnStopBetBJSC);
		
		//XYNC
		panel.add(btnBetOppositeXYNC);
		panel.add(XYNClabelPercent);
		panel.add(textFieldXYNCBetPercent);
		panel.add(btnBetAmountWindowXYNC);
		
		//GXKLSF
		panel.add(btnBetOppositeGXKLSF);
		panel.add(GXKLSFlabelPercent);
		panel.add(textFieldGXKLSFBetPercent);
		panel.add(btnBetAmountWindowGXKLSF);
		
		
		//GDKLSF
		panel.add(btnBetOppositeGDKLSF);
		panel.add(GDKLSFlabelPercent);
		panel.add(textFieldGDKLSFBetPercent);
		panel.add(btnBetAmountWindowGDKLSF);
		
		
		//GD11X5
		panel.add(btnBetOppositeGD11X5);
		panel.add(GD11X5labelPercent);
		panel.add(textFieldGD11X5BetPercent);
		panel.add(btnBetAmountWindowGD11X5);
		
		//XJSSC
		panel.add(btnBetOppositeXJSSC);
		panel.add(XJSSClabelPercent);
		panel.add(textFieldXJSSCBetPercent);
		panel.add(btnBetAmountWindowXJSSC);
		
		
		//TJSSC
		panel.add(btnBetOppositeTJSSC);
		panel.add(TJSSClabelPercent);
		panel.add(textFieldTJSSCBetPercent);
		panel.add(btnBetAmountWindowTJSSC);
		
		
		enableDSNMemberBet(false);

		


		
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
		btnBetTianCaiBJSC.addActionListener(new BetBJSCListener(this, client));
		
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
/*		btnStartGrabCQSSC = new Button("开抓重庆时彩");
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
		labelFailBets.setText("失败次数:0");*/
		
		
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





