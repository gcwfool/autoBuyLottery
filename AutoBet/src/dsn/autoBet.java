package dsn;
import huarun.BetHuarunOppositeBJSCListener;
import huarun.HuarunHttp;

import java.awt.Button;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

import Webet.BetBJSCManager;
import Webet.BetWebetOppositeBJSCListener;
import Webet.WebetHttp;
import Webet.WebetReloginThread;
import lanyang.BetLanyangBJSCAmountListener;
import lanyang.BetLanyangOppositeBJSCListener;
import lanyang.LanyangHttp;
import lanyang.LanyangReloginThread;
import lanyang.StopBetLanyangBJSCListener;
import yabo.BetYaboOppositeBJSCListener;
import yabo.YaboHttp;

enum BetType {
	CQSSC, BJSC, XYNC, GXKLSF, GDKLSF, GD11X5, XJSSC, TJSSC, KL8
}

enum BetMode {
	LESSTIME, MIDDLETIME, MORETIME
}

public class autoBet {

	public boolean loginToProxySuccess = false;
	public boolean loginToDSNMemberSuccess = false;
	public boolean loginToWeiCaiMemberSuccess = false;
	public boolean loginToWebetMemberSuccess = false;
	public boolean loginToLanyangMemberSuccess = false;
	public boolean loginToHuarunMemberSuccess = false;
	public boolean loginToYaboMemberSuccess = false;
	public boolean inBet = false;
	public boolean inBetXYNC = false;
	public boolean inBetGXKLSF = false;
	public boolean inBetGDKLSF = false;
	public boolean inBetGD11X5 = false;
	public boolean inBetXJSSC = false;
	public boolean inBetTJSSC = false;
	public boolean inBetKL8 = false;
	
	public boolean inBetLanyangBJSC = false;
	public boolean inBetHuarunBJSC = false;
	public boolean inBetYaboBJSC = false;

	public boolean inBetWeiCai = false;
	public boolean inBetWebetBJSC = false;
	

	// 代理登录界面
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
	public Button btnBetAmountWindowKL8;

	// 迪斯尼会员界面
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
	public TextField textFieldKL8BetPercent;
	public TextField textFieldBetTime;

	public Button btnBetCQSSC;
	public Button btnBetBJSC;
	public Button btnBetXYNC;
	public Button btnBetGXKLSF;
	public Button btnBetGDKLSF;
	public Button btnBetGD11X5;
	public Button btnBetXJSSC;
	public Button btnBetTJSSC;
	public Button btnBetKL8;
	
	
	public Button btnOppositeBetCQSSC;
	public Button btnStopBetCQSSC;
	
	public Button btnOppositeBJSC;
	public Button btnStopBetBJSC;

	public Button btnBetOppositeXYNC;
	public TextField textFieldXYNCBetPercent;

	public Button btnBetOppositeGXKLSF;
	public Button btnBetOppositeGDKLSF;
	public Button btnBetOppositeGD11X5;
	public Button btnBetOppositeXJSSC;
	public Button btnBetOppositeTJSSC;
	public Button btnBetOppositeKL8;

	// 微彩会员界面
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

	// 惟博会员界面
	public TextField textFieldWebetMemberAddress;
	public TextField textFieldWebetMemberAccount;
	public TextField textFieldWebetMemberPassword;
	public TextField textFieldCQSSCBetWebetPercent;
	public TextField textFieldBJSCBetWebetPercent;

	public Button btnBetWebetCQSSC;
	public Button btnOppositeBetWebetCQSSC;
	public Button btnStopBetWebetCQSSC;
	public Button btnBetWebetBJSC;
	public Button btnOppositeWebetBJSC;
	public Button btnStopBetWebetBJSC;

	public static Label labelWebetTotalBets;
	public static Label labelWebetSuccessBets;
	public static Label labelWebetFailBets;

	public static Button btnLogin;

	public static Client client;
	
	
	
	// 蓝洋会员界面
	public TextField textFieldLanyangMemberAddress;
	public TextField textFieldLanyangMemberAccount;
	public TextField textFieldLanyangMemberPassword;
	public TextField textFieldCQSSCBetLanyangPercent;
	public TextField textFieldBJSCBetLanyangPercent;

	public Button btnBetLanyangCQSSC;
	public Button btnOppositeBetLanyangCQSSC;
	public Button btnStopBetLanyangCQSSC;
	public Button btnBetLanyangBJSC;
	public Button btnBetLanyangBJSCAmount;
	public Button btnOppositeLanyangBJSC;
	public Button btnStopBetLanyangBJSC;

	public static Label labelLanyangTotalBets;
	public static Label labelLanyangSuccessBets;
	public static Label labelLanyangFailBets;
	
	//华润会员界面
	public TextField textFieldHuarunMemberAddress;
	public TextField textFieldHuarunMemberAccount;
	public TextField textFieldHuarunMemberPassword;
	public TextField textFieldCQSSCBetHuarunPercent;
	public TextField textFieldBJSCBetHuarunPercent;

	public Button btnBetHuarunCQSSC;
	public Button btnOppositeBetHuarunCQSSC;
	public Button btnStopBetHuarunCQSSC;
	public Button btnBetHuarunBJSC;
	public Button btnBetHuarunBJSCAmount;
	public Button btnOppositeHuarunBJSC;
	public Button btnStopBetHuarunBJSC;

	public static Label labelHuarunTotalBets;
	public static Label labelHuarunSuccessBets;
	public static Label labelHuarunFailBets;
	//华润会员界面end
	
	//亚博会员界面
	public TextField textFieldYaboMemberAddress;
	public TextField textFieldYaboMemberAccount;
	public TextField textFieldYaboMemberPassword;
	public TextField textFieldCQSSCBetYaboPercent;
	public TextField textFieldBJSCBetYaboPercent;

	public Button btnBetYaboCQSSC;
	public Button btnOppositeBetYaboCQSSC;
	public Button btnStopBetYaboCQSSC;
	public Button btnBetYaboBJSC;
	public Button btnBetYaboBJSCAmount;
	public Button btnOppositeYaboBJSC;
	public Button btnStopBetYaboBJSC;

	public static Label labelYaboTotalBets;
	public static Label labelYaboSuccessBets;
	public static Label labelYaboFailBets;
	//亚博会员界面end
	
	

	public static TextArea outputMessage;
	public static Label labelTotalBets;
	public static Label labelSuccessBets;
	public static Label labelFailBets;

	public ReloginThread reloginThread;
	
	public LanyangReloginThread lanyangReloginThread;
	public WebetReloginThread webetReloginThread;

	//WebetHttp WebetHttp = null;

	static int betTime = 12;

	public static int betTimeOut = 8 * 1000;

	public static BetMode betMode = BetMode.MIDDLETIME;

	public static void main(String[] args) throws Exception {

		if (betMode == BetMode.LESSTIME) {
			betTimeOut = 15 * 1000;
			betTime = 5;
		} else if (betMode == BetMode.MIDDLETIME) {
			betTimeOut = 15 * 1000;
			betTime = 10;
		} else {
			betTimeOut = 20 * 1000;
			betTime = 24;
		}

		
/*		LanyangHttp.loginToLanyang();
		BetBJSCManager.grabGameInfo();
		BetBJSCManager.grabOddsData();*/
		

		try {
			// 生成路径
			File dir = new File("log");
			if (dir.exists()) {
			} else {
				dir.mkdirs();
			}

			// 把输出重定向到文件
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");// 设置日期格式
			PrintStream ps = new PrintStream("log/" + df.format(new Date())
					+ ".txt");
			System.setOut(ps);
			System.setErr(ps);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		ConfigReader.read("common.config");
		ConfigWriter.open("common.config");

		dsnHttp.initLines();

		//LanyangHttp.loginToLanyang();
		

		client = new Client();

		// client.start();

		new autoBet().launchFrame();
		
		
/*		WebetHttp.setLoginParams("http://kg688.net", "gcwfool", "gcwfool");
		
		WebetHttp.login();
		
		BetBJSCManager.changeToBJgame();
		
		BetBJSCManager.grabGameInfo();*/

	}

	public void setBetTime() {
		String betTime = textFieldBetTime.getText();

		if (Common.isNum(betTime)) {
			int timeSeconds = Integer.parseInt(betTime);
			BetThread.betRemainTime = timeSeconds * 1000;
			BetXYNCThread.betRemainTime = timeSeconds * 1000;
			BetGDKLSFThread.betRemainTime = timeSeconds * 1000;
			BetGXKLSFThread.betRemainTime = timeSeconds * 1000;
			BetGD11X5Thread.betRemainTime = timeSeconds * 1000;
			BetXJSSCThread.betRemainTime = timeSeconds * 1000;
			BetTJSSCThread.betRemainTime = timeSeconds * 1000;
			BetKL8Thread.betRemainTime = timeSeconds * 1000;
			
		} else {
			// TODO 弹出对话框，提示输入错误
		}
	}

	public static synchronized void outputGUIMessage(String outstr) {

		if (outstr == null)
			return;

		outputMessage.append(outstr);
	}

	public void enableGrabBtn(boolean flag) {
		btnStartGrabCQSSC.setEnabled(flag);
		btnStopGrabCQSSC.setEnabled(flag);
		btnStartGrabBJSC.setEnabled(flag);
		btnStopGrabBJSC.setEnabled(flag);
		btnStartGrabXYNC.setEnabled(flag);
		btnStopGrabXYNC.setEnabled(flag);
	}

	public void enableDSNMemberBet(boolean flag) {
		btnBetCQSSC.setEnabled(flag);
		
		btnBetXYNC.setEnabled(flag);
		btnBetGXKLSF.setEnabled(flag);
		btnBetGDKLSF.setEnabled(flag);
		btnBetGD11X5.setEnabled(flag);
		btnBetXJSSC.setEnabled(flag);
		btnBetTJSSC.setEnabled(flag);
		btnBetKL8.setEnabled(flag);
		
		btnOppositeBetCQSSC.setEnabled(flag);
		btnStopBetCQSSC.setEnabled(flag);
		btnBetBJSC.setEnabled(flag);
		btnOppositeBJSC.setEnabled(flag);
		btnStopBetBJSC.setEnabled(flag);
		btnBetOppositeXYNC.setEnabled(flag);
		btnBetOppositeGXKLSF.setEnabled(flag);
		btnBetOppositeGDKLSF.setEnabled(flag);
		btnBetOppositeGD11X5.setEnabled(flag);
		btnBetOppositeXJSSC.setEnabled(flag);
		btnBetOppositeTJSSC.setEnabled(flag);
		btnBetOppositeKL8.setEnabled(flag);
		btnBetAmountWindowXYNC.setEnabled(flag);
		btnBetAmountWindowGXKLSF.setEnabled(flag);
		btnBetAmountWindowGDKLSF.setEnabled(flag);
		btnBetAmountWindowGD11X5.setEnabled(flag);
		btnBetAmountWindowXJSSC.setEnabled(flag);
		btnBetAmountWindowTJSSC.setEnabled(flag);
		btnBetAmountWindowKL8.setEnabled(flag);
	}

	public void enableWeiCaiMemberBet(boolean flag) {
		btnBetWeiCaiCQSSC.setEnabled(false);
		btnOppositeBetWeiCaiCQSSC.setEnabled(flag);
		btnStopBetWeiCaiCQSSC.setEnabled(flag);
		btnBetWeiCaiBJSC.setEnabled(false);
		btnOppositeWeiCaiBJSC.setEnabled(flag);
		btnStopBetWeiCaiBJSC.setEnabled(flag);
	}

	public void enableWebetMemberBet(boolean flag) {
		btnBetWebetCQSSC.setEnabled(false);
		btnOppositeBetWebetCQSSC.setEnabled(flag);
		btnStopBetWebetCQSSC.setEnabled(flag);
		btnBetWebetBJSC.setEnabled(false);
		btnOppositeWebetBJSC.setEnabled(flag);
		btnStopBetWebetBJSC.setEnabled(flag);
	}
	
	
	public void enableLanyangMemberBet(boolean flag) {
		btnBetLanyangCQSSC.setEnabled(false);
		btnOppositeBetLanyangCQSSC.setEnabled(false);
		btnStopBetLanyangCQSSC.setEnabled(false);
		btnBetLanyangBJSC.setEnabled(false);
		btnOppositeLanyangBJSC.setEnabled(flag);
		btnStopBetLanyangBJSC.setEnabled(flag);
		btnBetLanyangBJSCAmount.setEnabled(flag);
	}
	
	public void enableHuarunMemberBet(boolean flag) {
		btnBetHuarunCQSSC.setEnabled(false);
		btnOppositeBetHuarunCQSSC.setEnabled(false);
		btnStopBetHuarunCQSSC.setEnabled(false);
		btnBetHuarunBJSC.setEnabled(false);
		btnOppositeHuarunBJSC.setEnabled(flag);
		btnStopBetHuarunBJSC.setEnabled(flag);
		btnBetHuarunBJSCAmount.setEnabled(flag);
	}
	
	public void enableYaboMemberBet(boolean flag) {
		btnBetYaboCQSSC.setEnabled(false);
		btnOppositeBetYaboCQSSC.setEnabled(false);
		btnStopBetYaboCQSSC.setEnabled(false);
		btnBetYaboBJSC.setEnabled(false);
		btnOppositeYaboBJSC.setEnabled(flag);
		btnStopBetYaboBJSC.setEnabled(flag);
		btnBetYaboBJSCAmount.setEnabled(flag);
	}

	public void launchFrame() {

		Frame mainFrame = new Frame("自动下单器");

		Panel panel = new Panel();

		panel.setSize(1920, 1080);

		panel.setLocation(0, 0);
		panel.setLayout(null);
		mainFrame.setLayout(null);

		mainFrame.add(panel);

		// dsn代理界面
		int DsnProxyX = 50;
		int DsnProxyY = 50;

		Label labelDsnProxyLogin = new Label("连接服务器:");
		labelDsnProxyLogin.setSize(100, 25);
		labelDsnProxyLogin.setLocation(DsnProxyX, DsnProxyY);

		Label labelDsnProxyAddress = new Label("网址:");
		labelDsnProxyAddress.setSize(50, 25);
		labelDsnProxyAddress.setLocation(DsnProxyX, DsnProxyY + 30);

		textFieldProxyAddress = new TextField();
		textFieldProxyAddress.setSize(300, 25);
		textFieldProxyAddress.setLocation(DsnProxyX + 50, DsnProxyY + 30);
		textFieldProxyAddress.setText(ConfigReader.getServerAddress());

		Label labelDsnProxyAccount = new Label("端口:");
		labelDsnProxyAccount.setSize(50, 25);
		labelDsnProxyAccount.setLocation(DsnProxyX, DsnProxyY + 60);

		textFieldProxyAccount = new TextField();
		textFieldProxyAccount.setSize(300, 25);
		textFieldProxyAccount.setLocation(DsnProxyX + 50, DsnProxyY + 60);
		textFieldProxyAccount.setText(ConfigReader.getServerPort());

		Label labelDsnProxyPassword = new Label("密码:");
		labelDsnProxyPassword.setSize(50, 25);
		labelDsnProxyPassword.setLocation(DsnProxyX, DsnProxyY + 90);

		textFieldProxyPassword = new TextField();
		textFieldProxyPassword.setSize(300, 25);
		textFieldProxyPassword.setLocation(DsnProxyX + 50, DsnProxyY + 90);
		textFieldProxyPassword.setText(ConfigReader.getProxyPassword());
		textFieldProxyPassword.setEchoChar('*');

		btnLogin = new Button("连接");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (loginToProxySuccess == true)
					return;

				String address = textFieldProxyAddress.getText();
				String port = textFieldProxyAccount.getText();
				// String password = textFieldProxyPassword.getText();

				// DsnProxyGrab.setLoginParams(address, account, password);

				if (!client.connectToSever(address, port)) {
					outputGUIMessage("连接服务器失败!\n");
					return;
				}

				loginToProxySuccess = true;

				ConfigWriter.updateServerAddress(address);
				ConfigWriter.updateServerPort(port);

				ConfigWriter.saveTofile("common.config");

				/*
				 * ConfigWriter.updateProxyAddress(address);
				 * ConfigWriter.updateProxyAccount(account);
				 * ConfigWriter.updateProxyPassword(password);
				 * 
				 * ConfigWriter.saveTofile("common.config");
				 * 
				 * grabThread = new GrabThread(new GrabCQSSCwindow(), new
				 * GrabBJSCwindow());//启动抓包线程 grabThread.start();
				 * 
				 * XYNCthread = new GrabXYNCthread(); XYNCthread.start();
				 * 
				 * enableGrabBtn(true);
				 */

				if (loginToDSNMemberSuccess && loginToProxySuccess)
					enableDSNMemberBet(true);

				/*
				 * if(loginToWeiCaiMemberSuccess&& loginToProxySuccess)
				 * enableWeiCaiMemberBet(true);
				 */

				if (loginToWebetMemberSuccess && loginToProxySuccess)
					enableWebetMemberBet(true);

				client.start();

				outputGUIMessage("连接服务器成功!\n");

				btnLogin.setEnabled(false);
				// btnLogin.setText("已连接");
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
		// panel.add(labelDsnProxyPassword);
		// panel.add(textFieldProxyPassword);
		panel.add(btnLogin);
		panel.add(textFieldBetTime);
		panel.add(labelBetTime);

		// dsn会员界面
		int DsnMemberX = 500;
		int DsnMemberY = 50;

		Label labelDsnMemberLogin = new Label("迪斯尼会员登录");
		labelDsnMemberLogin.setSize(100, 25);
		labelDsnMemberLogin.setLocation(DsnMemberX, DsnMemberY);

		Label labelDsnMemberAddress = new Label("网址:");
		labelDsnMemberAddress.setSize(50, 25);
		labelDsnMemberAddress.setLocation(DsnMemberX, DsnMemberY + 30);

		textFieldMemberAddress = new TextField();
		textFieldMemberAddress.setSize(300, 25);
		textFieldMemberAddress.setLocation(DsnMemberX + 50, DsnMemberY + 30);
		textFieldMemberAddress.setText(ConfigReader.getBetAddress());

		Label labelDsnMemberAccount = new Label("账户:");
		labelDsnMemberAccount.setSize(50, 25);
		labelDsnMemberAccount.setLocation(DsnMemberX, DsnMemberY + 60);

		textFieldMemberAccount = new TextField();
		textFieldMemberAccount.setSize(300, 25);
		textFieldMemberAccount.setLocation(DsnMemberX + 50, DsnMemberY + 60);
		textFieldMemberAccount.setText(ConfigReader.getBetAccount());

		Label labelDsnMemberPassword = new Label("密码:");
		labelDsnMemberPassword.setSize(50, 25);
		labelDsnMemberPassword.setLocation(DsnMemberX, DsnMemberY + 90);

		textFieldMemberPassword = new TextField();
		textFieldMemberPassword.setSize(300, 25);
		textFieldMemberPassword.setLocation(DsnMemberX + 50, DsnMemberY + 90);
		textFieldMemberPassword.setText(ConfigReader.getBetPassword());
		textFieldMemberPassword.setEchoChar('*');

		Button btnMemberLogin = new Button("登录");
		btnMemberLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (loginToDSNMemberSuccess == true)
					return;

				String address = textFieldMemberAddress.getText();
				String account = textFieldMemberAccount.getText();
				String password = textFieldMemberPassword.getText();

				dsnHttp.setLoginParams(address, account, password);

				if (!dsnHttp.login()) {
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

				if (loginToDSNMemberSuccess && loginToProxySuccess)
					enableDSNMemberBet(true);

				outputGUIMessage("登录迪士尼会员成功!\n");
			}
		});

		btnMemberLogin.setSize(50, 25);
		btnMemberLogin.setLocation(DsnMemberX, DsnMemberY + 120);

		btnBetCQSSC = new Button("正投重庆时时彩");
		btnBetCQSSC.addActionListener(new BetCQSSCListener(this, client));

		btnBetCQSSC.setSize(90, 25);
		btnBetCQSSC.setLocation(DsnMemberX, DsnMemberY + 150);
		
		
		btnBetBJSC = new Button("正投北京赛车");
		btnBetBJSC.addActionListener(new BetBJSCListener(this, client));

		btnBetBJSC.setSize(90, 25);
		btnBetBJSC.setLocation(DsnMemberX, DsnMemberY + 180);
		
		btnBetXYNC = new Button("正投幸运农场");
		btnBetXYNC.addActionListener(new BetzhengXYNCListener(this, client));

		btnBetXYNC.setSize(90, 25);
		btnBetXYNC.setLocation(DsnMemberX, DsnMemberY + 210);
		
		
		
		// GXKLSF
		btnBetGXKLSF = new Button("正投广西快乐十分");
		btnBetGXKLSF.addActionListener(new BetzhengGXKLSFListener(this,
				client));

		btnBetGXKLSF.setSize(90, 25);
		btnBetGXKLSF.setLocation(DsnMemberX, DsnMemberY + 240);
		
		
		// GDKLSF

		btnBetGDKLSF = new Button("正投广东快乐十分");
		btnBetGDKLSF.addActionListener(new BetzhengGDKLSFListener(this,
				client));

		btnBetGDKLSF.setSize(90, 25);
		btnBetGDKLSF.setLocation(DsnMemberX, DsnMemberY + 270);
		
		// GD11X5

		btnBetGD11X5 = new Button("正投广东11选5");
		btnBetGD11X5.addActionListener(new BetzhengGD11X5Listener(this,
				client));

		btnBetGD11X5.setSize(90, 25);
		btnBetGD11X5.setLocation(DsnMemberX, DsnMemberY + 300);
		
		
		// XJSSC

		btnBetXJSSC = new Button("正投新疆时时彩");
		btnBetXJSSC
				.addActionListener(new BetzhengXJSSCListener(this, client));

		btnBetXJSSC.setSize(90, 25);
		btnBetXJSSC.setLocation(DsnMemberX, DsnMemberY + 330);
		
		
		// TJSSC
		btnBetTJSSC = new Button("正投天津时时彩");
		btnBetTJSSC
				.addActionListener(new BetzhengTJSSCListener(this, client));

		btnBetTJSSC.setSize(90, 25);
		btnBetTJSSC.setLocation(DsnMemberX, DsnMemberY + 360);
		
		//kl8
		btnBetKL8 = new Button("正投北京快乐8");
		btnBetKL8.addActionListener(new BetzhengKL8Listener(this, client));

		btnBetKL8.setSize(90, 25);
		btnBetKL8.setLocation(DsnMemberX, DsnMemberY + 390);
		
		int DsnMemberOldX =  DsnMemberX + 100;
		

		Label labelPercent = new Label("投注比例:");
		labelPercent.setSize(60, 25);
		labelPercent.setLocation(DsnMemberOldX + 100, DsnMemberY + 150);

		textFieldCQSSCBetPercent = new TextField();
		textFieldCQSSCBetPercent.setSize(60, 25);
		textFieldCQSSCBetPercent
				.setLocation(DsnMemberOldX + 160, DsnMemberY + 150);

		btnOppositeBetCQSSC = new Button("反投重庆时时彩");
		btnOppositeBetCQSSC.addActionListener(new BetOppositeCQSSCListener(
				this, client));

		btnOppositeBetCQSSC.setSize(90, 25);
		btnOppositeBetCQSSC.setLocation(DsnMemberOldX, DsnMemberY + 150);

		btnStopBetCQSSC = new Button("投注金额");
		btnStopBetCQSSC.addActionListener(new StopBetCQSSCListener(this));

		btnStopBetCQSSC.setSize(90, 25);
		btnStopBetCQSSC.setLocation(DsnMemberOldX + 220, DsnMemberY + 150);



		Label BJSClabelPercent = new Label("投注比例:");
		BJSClabelPercent.setSize(60, 25);
		BJSClabelPercent.setLocation(DsnMemberOldX + 100, DsnMemberY + 180);

		textFieldBJSCBetPercent = new TextField();
		textFieldBJSCBetPercent.setSize(60, 25);
		textFieldBJSCBetPercent.setLocation(DsnMemberOldX + 160, DsnMemberY + 180);

		btnOppositeBJSC = new Button("反投北京赛车");
		btnOppositeBJSC.addActionListener(new BetOppositeBJSCListener(this,
				client));

		btnOppositeBJSC.setSize(90, 25);
		btnOppositeBJSC.setLocation(DsnMemberOldX, DsnMemberY + 180);

		btnStopBetBJSC = new Button("投注金额");
		btnStopBetBJSC.addActionListener(new StopBetBJSCListener(this));

		btnStopBetBJSC.setSize(90, 25);
		btnStopBetBJSC.setLocation(DsnMemberOldX + 220, DsnMemberY + 180);

		// XYNC幸运农场
		btnBetOppositeXYNC = new Button("反投幸运农场");
		btnBetOppositeXYNC.addActionListener(new BetXYNCListener(this, client));

		btnBetOppositeXYNC.setSize(90, 25);
		btnBetOppositeXYNC.setLocation(DsnMemberOldX, DsnMemberY + 210);

		Label XYNClabelPercent = new Label("投注比例:");
		XYNClabelPercent.setSize(60, 25);
		XYNClabelPercent.setLocation(DsnMemberOldX + 100, DsnMemberY + 210);

		textFieldXYNCBetPercent = new TextField();
		textFieldXYNCBetPercent.setSize(60, 25);
		textFieldXYNCBetPercent.setLocation(DsnMemberOldX + 160, DsnMemberY + 210);

		btnBetAmountWindowXYNC = new Button("投注金额");
		btnBetAmountWindowXYNC
				.addActionListener(new BetAmountDetailXYNCListener(this));

		btnBetAmountWindowXYNC.setSize(90, 25);
		btnBetAmountWindowXYNC.setLocation(DsnMemberOldX + 220, DsnMemberY + 210);

		// GXKLSF
		btnBetOppositeGXKLSF = new Button("反投广西快乐十分");
		btnBetOppositeGXKLSF.addActionListener(new BetGXKLSFListener(this,
				client));

		btnBetOppositeGXKLSF.setSize(90, 25);
		btnBetOppositeGXKLSF.setLocation(DsnMemberOldX, DsnMemberY + 240);

		Label GXKLSFlabelPercent = new Label("投注比例:");
		GXKLSFlabelPercent.setSize(60, 25);
		GXKLSFlabelPercent.setLocation(DsnMemberOldX + 100, DsnMemberY + 240);

		textFieldGXKLSFBetPercent = new TextField();
		textFieldGXKLSFBetPercent.setSize(60, 25);
		textFieldGXKLSFBetPercent.setLocation(DsnMemberOldX + 160,
				DsnMemberY + 240);

		btnBetAmountWindowGXKLSF = new Button("投注金额");
		btnBetAmountWindowGXKLSF
				.addActionListener(new BetAmountDetailGXKLSFListener(this));

		btnBetAmountWindowGXKLSF.setSize(90, 25);
		btnBetAmountWindowGXKLSF
				.setLocation(DsnMemberOldX + 220, DsnMemberY + 240);

		// GDKLSF

		btnBetOppositeGDKLSF = new Button("反投广东快乐十分");
		btnBetOppositeGDKLSF.addActionListener(new BetGDKLSFListener(this,
				client));

		btnBetOppositeGDKLSF.setSize(90, 25);
		btnBetOppositeGDKLSF.setLocation(DsnMemberOldX, DsnMemberY + 270);

		Label GDKLSFlabelPercent = new Label("投注比例:");
		GDKLSFlabelPercent.setSize(60, 25);
		GDKLSFlabelPercent.setLocation(DsnMemberOldX + 100, DsnMemberY + 270);

		textFieldGDKLSFBetPercent = new TextField();
		textFieldGDKLSFBetPercent.setSize(60, 25);
		textFieldGDKLSFBetPercent.setLocation(DsnMemberOldX + 160,
				DsnMemberY + 270);

		btnBetAmountWindowGDKLSF = new Button("投注金额");
		btnBetAmountWindowGDKLSF
				.addActionListener(new BetAmountDetailGDKLSFListener(this));

		btnBetAmountWindowGDKLSF.setSize(90, 25);
		btnBetAmountWindowGDKLSF
				.setLocation(DsnMemberOldX + 220, DsnMemberY + 270);

		// GD11X5

		btnBetOppositeGD11X5 = new Button("反投广东11选5");
		btnBetOppositeGD11X5.addActionListener(new BetGD11X5Listener(this,
				client));

		btnBetOppositeGD11X5.setSize(90, 25);
		btnBetOppositeGD11X5.setLocation(DsnMemberOldX, DsnMemberY + 300);

		Label GD11X5labelPercent = new Label("投注比例:");
		GD11X5labelPercent.setSize(60, 25);
		GD11X5labelPercent.setLocation(DsnMemberOldX + 100, DsnMemberY + 300);

		textFieldGD11X5BetPercent = new TextField();
		textFieldGD11X5BetPercent.setSize(60, 25);
		textFieldGD11X5BetPercent.setLocation(DsnMemberOldX + 160,
				DsnMemberY + 300);

		btnBetAmountWindowGD11X5 = new Button("投注金额");
		btnBetAmountWindowGD11X5
				.addActionListener(new BetAmountDetailGD11X5Listener(this));

		btnBetAmountWindowGD11X5.setSize(90, 25);
		btnBetAmountWindowGD11X5
				.setLocation(DsnMemberOldX + 220, DsnMemberY + 300);

		// XJSSC

		btnBetOppositeXJSSC = new Button("反投新疆时时彩");
		btnBetOppositeXJSSC
				.addActionListener(new BetXJSSCListener(this, client));

		btnBetOppositeXJSSC.setSize(90, 25);
		btnBetOppositeXJSSC.setLocation(DsnMemberOldX, DsnMemberY + 330);

		Label XJSSClabelPercent = new Label("投注比例:");
		XJSSClabelPercent.setSize(60, 25);
		XJSSClabelPercent.setLocation(DsnMemberOldX + 100, DsnMemberY + 330);

		textFieldXJSSCBetPercent = new TextField();
		textFieldXJSSCBetPercent.setSize(60, 25);
		textFieldXJSSCBetPercent
				.setLocation(DsnMemberOldX + 160, DsnMemberY + 330);

		btnBetAmountWindowXJSSC = new Button("投注金额");
		btnBetAmountWindowXJSSC
				.addActionListener(new BetAmountDetailXJSSCListener(this));

		btnBetAmountWindowXJSSC.setSize(90, 25);
		btnBetAmountWindowXJSSC.setLocation(DsnMemberOldX + 220, DsnMemberY + 330);

		// TJSSC
		btnBetOppositeTJSSC = new Button("反投天津时时彩");
		btnBetOppositeTJSSC
				.addActionListener(new BetTJSSCListener(this, client));

		btnBetOppositeTJSSC.setSize(90, 25);
		btnBetOppositeTJSSC.setLocation(DsnMemberOldX, DsnMemberY + 360);

		Label TJSSClabelPercent = new Label("投注比例:");
		TJSSClabelPercent.setSize(60, 25);
		TJSSClabelPercent.setLocation(DsnMemberOldX + 100, DsnMemberY + 360);

		textFieldTJSSCBetPercent = new TextField();
		textFieldTJSSCBetPercent.setSize(60, 25);
		textFieldTJSSCBetPercent
				.setLocation(DsnMemberOldX + 160, DsnMemberY + 360);

		btnBetAmountWindowTJSSC = new Button("投注金额");
		btnBetAmountWindowTJSSC
				.addActionListener(new BetAmountDetailTJSSCListener(this));

		btnBetAmountWindowTJSSC.setSize(90, 25);
		btnBetAmountWindowTJSSC.setLocation(DsnMemberOldX + 220, DsnMemberY + 360);

		// KL8
		btnBetOppositeKL8 = new Button("反投北京快乐8");
		btnBetOppositeKL8.addActionListener(new BetKL8Listener(this, client));

		btnBetOppositeKL8.setSize(90, 25);
		btnBetOppositeKL8.setLocation(DsnMemberOldX, DsnMemberY + 390);

		Label KL8labelPercent = new Label("投注比例:");
		KL8labelPercent.setSize(60, 25);
		KL8labelPercent.setLocation(DsnMemberOldX + 100, DsnMemberY + 390);

		textFieldKL8BetPercent = new TextField();
		textFieldKL8BetPercent.setSize(60, 25);
		textFieldKL8BetPercent.setLocation(DsnMemberOldX + 160, DsnMemberY + 390);

		btnBetAmountWindowKL8 = new Button("投注金额");
		btnBetAmountWindowKL8
				.addActionListener(new BetAmountDetailKL8Listener(this));

		btnBetAmountWindowKL8.setSize(90, 25);
		btnBetAmountWindowKL8.setLocation(DsnMemberOldX + 220, DsnMemberY + 390);

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
		
		
		panel.add(btnBetXYNC);
		panel.add(btnBetGXKLSF);
		panel.add(btnBetGDKLSF);
		panel.add(btnBetGD11X5);
		panel.add(btnBetXJSSC);
		panel.add(btnBetTJSSC);
		panel.add(btnBetKL8);
		
		
		
		
		
		

		// XYNC
		panel.add(btnBetOppositeXYNC);
		panel.add(XYNClabelPercent);
		panel.add(textFieldXYNCBetPercent);
		panel.add(btnBetAmountWindowXYNC);

		// GXKLSF
		panel.add(btnBetOppositeGXKLSF);
		panel.add(GXKLSFlabelPercent);
		panel.add(textFieldGXKLSFBetPercent);
		panel.add(btnBetAmountWindowGXKLSF);

		// GDKLSF
		panel.add(btnBetOppositeGDKLSF);
		panel.add(GDKLSFlabelPercent);
		panel.add(textFieldGDKLSFBetPercent);
		panel.add(btnBetAmountWindowGDKLSF);

		// GD11X5
		panel.add(btnBetOppositeGD11X5);
		panel.add(GD11X5labelPercent);
		panel.add(textFieldGD11X5BetPercent);
		panel.add(btnBetAmountWindowGD11X5);

		// XJSSC
		panel.add(btnBetOppositeXJSSC);
		panel.add(XJSSClabelPercent);
		panel.add(textFieldXJSSCBetPercent);
		panel.add(btnBetAmountWindowXJSSC);

		// TJSSC
		panel.add(btnBetOppositeTJSSC);
		panel.add(TJSSClabelPercent);
		panel.add(textFieldTJSSCBetPercent);
		panel.add(btnBetAmountWindowTJSSC);

		// KL8
		panel.add(btnBetOppositeKL8);
		panel.add(KL8labelPercent);
		panel.add(textFieldKL8BetPercent);
		panel.add(btnBetAmountWindowKL8);

		enableDSNMemberBet(false);

		// 惟博会员界面
		int WebetMemberX = 1550;
		int WebetMemberY = 50;

		Label labelWebetMemberLogin = new Label("惟博会员登录:");
		labelWebetMemberLogin.setSize(100, 25);
		labelWebetMemberLogin.setLocation(WebetMemberX, WebetMemberY);

		Label labelWebetMemberAddress = new Label("网址:");
		labelWebetMemberAddress.setSize(50, 25);
		labelWebetMemberAddress.setLocation(WebetMemberX,
				WebetMemberY + 30);

		textFieldWebetMemberAddress = new TextField();
		textFieldWebetMemberAddress.setSize(300, 25);
		textFieldWebetMemberAddress.setLocation(WebetMemberX + 50,
				WebetMemberY + 30);
		textFieldWebetMemberAddress.setText(ConfigReader
				.getWebetBetAddress());

		Label labelWebetMemberAccount = new Label("账户:");
		labelWebetMemberAccount.setSize(50, 25);
		labelWebetMemberAccount.setLocation(WebetMemberX,
				WebetMemberY + 60);

		textFieldWebetMemberAccount = new TextField();
		textFieldWebetMemberAccount.setSize(300, 25);
		textFieldWebetMemberAccount.setLocation(WebetMemberX + 50,
				WebetMemberY + 60);
		textFieldWebetMemberAccount.setText(ConfigReader
				.getWebetBetAccount());

		Label labelWebetMemberPassword = new Label("密码:");
		labelWebetMemberPassword.setSize(50, 25);
		labelWebetMemberPassword.setLocation(WebetMemberX,
				WebetMemberY + 90);

		textFieldWebetMemberPassword = new TextField();
		textFieldWebetMemberPassword.setSize(300, 25);
		textFieldWebetMemberPassword.setLocation(WebetMemberX + 50,
				WebetMemberY + 90);
		textFieldWebetMemberPassword.setText(ConfigReader
				.getWebetBetPassword());
		textFieldWebetMemberPassword.setEchoChar('*');

		Button btnWebetMemberLogin = new Button("登录");
		btnWebetMemberLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (loginToWebetMemberSuccess == true)
					return;

				String address = textFieldWebetMemberAddress.getText();
				String account = textFieldWebetMemberAccount.getText();
				String password = textFieldWebetMemberPassword.getText();

				//WebetHttp = new WebetHttp();

				WebetHttp.setLoginParams(address, account, password);

				if (!WebetHttp.login()) {
					
					outputGUIMessage("登录惟博会员失败!\n");
					return;
				}
				
				boolean res = BetBJSCManager.changeToBJgame();
				
				if(res == false){
					BetBJSCManager.changeToBJgame();
				}
				
				if(res == false){
					outputGUIMessage("登录惟博会员失败!\n");
					return;
				}
				

				loginToWebetMemberSuccess = true;
				


				ConfigWriter.updateWebetMemberAddress(address);
				ConfigWriter.updateWebetMemberAccount(account);
				ConfigWriter.updateWebetMemberPassword(password);

				ConfigWriter.saveTofile("common.config");

				if (loginToWebetMemberSuccess && loginToProxySuccess)
					enableWebetMemberBet(true);

				outputGUIMessage("登录惟博会员成功!\n");
				
				webetReloginThread = new WebetReloginThread();
				webetReloginThread.start();

			}
		});

		btnWebetMemberLogin.setSize(50, 25);
		btnWebetMemberLogin.setLocation(WebetMemberX, WebetMemberY + 120);

		btnBetWebetCQSSC = new Button("正投重庆时时彩");
		/*btnBetWebetCQSSC
				.addActionListener(new BetWebetOppositeCQSSCListener(this));*/

		btnBetWebetCQSSC.setSize(90, 25);
		btnBetWebetCQSSC.setLocation(WebetMemberX, WebetMemberY + 150);

		Label labelWebetPercent = new Label("投注比例:");
		labelWebetPercent.setSize(60, 25);
		labelWebetPercent.setLocation(WebetMemberX + 100,
				WebetMemberY + 150);

		textFieldCQSSCBetWebetPercent = new TextField();
		textFieldCQSSCBetWebetPercent.setSize(60, 25);
		textFieldCQSSCBetWebetPercent.setLocation(WebetMemberX + 160,
				WebetMemberY + 150);

		btnOppositeBetWebetCQSSC = new Button("反投重庆时时彩");
/*		btnOppositeBetWebetCQSSC
				.addActionListener(new BetWebetOppositeCQSSCListener(this));*/

		btnOppositeBetWebetCQSSC.setSize(90, 25);
		btnOppositeBetWebetCQSSC.setLocation(WebetMemberX,
				WebetMemberY + 180);

		btnStopBetWebetCQSSC = new Button("投注详情");
/*		btnStopBetWebetCQSSC
				.addActionListener(new StopBetWebetCQSSCListener(this));*/

		btnStopBetWebetCQSSC.setSize(90, 25);
		btnStopBetWebetCQSSC.setLocation(WebetMemberX + 100,
				WebetMemberY + 180);

		btnBetWebetBJSC = new Button("正投北京赛车");
		btnBetWebetBJSC.addActionListener(new BetBJSCListener(this, client));

		btnBetWebetBJSC.setSize(90, 25);
		btnBetWebetBJSC.setLocation(WebetMemberX, WebetMemberY + 210);

		Label BJSClabelWebetPercent = new Label("投注比例:");
		BJSClabelWebetPercent.setSize(60, 25);
		BJSClabelWebetPercent.setLocation(WebetMemberX + 100,
				WebetMemberY + 210);

		textFieldBJSCBetWebetPercent = new TextField();
		textFieldBJSCBetWebetPercent.setSize(60, 25);
		textFieldBJSCBetWebetPercent.setLocation(WebetMemberX + 160,
				WebetMemberY + 210);

		btnOppositeWebetBJSC = new Button("反投北京赛车");
		btnOppositeWebetBJSC
				.addActionListener(new BetWebetOppositeBJSCListener(this, client));

		btnOppositeWebetBJSC.setSize(90, 25);
		btnOppositeWebetBJSC
				.setLocation(WebetMemberX, WebetMemberY + 240);

		btnStopBetWebetBJSC = new Button("投注详情");
/*		btnStopBetWebetBJSC.addActionListener(new StopBetWebetBJSCListener(
				this));*/

		btnStopBetWebetBJSC.setSize(90, 25);
		btnStopBetWebetBJSC.setLocation(WebetMemberX + 100,
				WebetMemberY + 240);

		labelWebetTotalBets = new Label();
		labelWebetTotalBets.setSize(300, 25);
		labelWebetTotalBets.setLocation(WebetMemberX, 400);
		labelWebetTotalBets.setText("下单次数:0");

		labelWebetSuccessBets = new Label();
		labelWebetSuccessBets.setSize(300, 25);
		labelWebetSuccessBets.setLocation(WebetMemberX, 430);
		labelWebetSuccessBets.setText("成功次数:0");

		labelWebetFailBets = new Label();
		labelWebetFailBets.setSize(300, 25);
		labelWebetFailBets.setLocation(WebetMemberX, 460);
		labelWebetFailBets.setText("失败次数:0");

		/*
		 * panel.add(labelWebetTotalBets); panel.add(labelWebetSuccessBets);
		 * panel.add(labelWebetFailBets);
		 */

		panel.add(labelWebetMemberLogin);
		panel.add(labelWebetMemberAddress);
		panel.add(textFieldWebetMemberAddress);
		panel.add(labelWebetMemberAccount);
		panel.add(textFieldWebetMemberAccount);
		panel.add(labelWebetMemberPassword);
		panel.add(textFieldWebetMemberPassword);
		panel.add(btnWebetMemberLogin);

		panel.add(btnBetWebetCQSSC);
		panel.add(labelWebetPercent);
		panel.add(btnOppositeBetWebetCQSSC);
		panel.add(btnBetWebetBJSC);
		panel.add(btnOppositeWebetBJSC);
		panel.add(BJSClabelWebetPercent);
		panel.add(textFieldCQSSCBetWebetPercent);
		panel.add(textFieldBJSCBetWebetPercent);
		panel.add(btnStopBetWebetCQSSC);
		panel.add(btnStopBetWebetBJSC);

		enableWebetMemberBet(false);

		
		
		
		
		
		
		
		
		
		// 蓝洋会员界面
		int LanyangMemberX = 1050;
		int LanyangMemberY = 50;

		Label labelLanyangMemberLogin = new Label("蓝洋会员登录:");
		labelLanyangMemberLogin.setSize(100, 25);
		labelLanyangMemberLogin.setLocation(LanyangMemberX, LanyangMemberY);

		Label labelLanyangMemberAddress = new Label("网址:");
		labelLanyangMemberAddress.setSize(50, 25);
		labelLanyangMemberAddress.setLocation(LanyangMemberX,
				LanyangMemberY + 30);

		textFieldLanyangMemberAddress = new TextField();
		textFieldLanyangMemberAddress.setSize(300, 25);
		textFieldLanyangMemberAddress.setLocation(LanyangMemberX + 50,
				LanyangMemberY + 30);
		textFieldLanyangMemberAddress.setText(ConfigReader
				.getlanyangBetAddress());

		Label labelLanyangMemberAccount = new Label("账户:");
		labelLanyangMemberAccount.setSize(50, 25);
		labelLanyangMemberAccount.setLocation(LanyangMemberX,
				LanyangMemberY + 60);

		textFieldLanyangMemberAccount = new TextField();
		textFieldLanyangMemberAccount.setSize(300, 25);
		textFieldLanyangMemberAccount.setLocation(LanyangMemberX + 50,
				LanyangMemberY + 60);
		textFieldLanyangMemberAccount.setText(ConfigReader
				.getlanyangBetAccount());

		Label labelLanyangMemberPassword = new Label("密码:");
		labelLanyangMemberPassword.setSize(50, 25);
		labelLanyangMemberPassword.setLocation(LanyangMemberX,
				LanyangMemberY + 90);

		textFieldLanyangMemberPassword = new TextField();
		textFieldLanyangMemberPassword.setSize(300, 25);
		textFieldLanyangMemberPassword.setLocation(LanyangMemberX + 50,
				LanyangMemberY + 90);
		textFieldLanyangMemberPassword.setText(ConfigReader
				.getlanyangBetPassword());
		textFieldLanyangMemberPassword.setEchoChar('*');

		Button btnLanyangMemberLogin = new Button("登录");
		btnLanyangMemberLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (loginToLanyangMemberSuccess == true)
					return;

				String address = textFieldLanyangMemberAddress.getText();
				String account = textFieldLanyangMemberAccount.getText();
				String password = textFieldLanyangMemberPassword.getText();

				//LanyangHttp = new LanyangHttp();

				LanyangHttp.setLoginParams(address, account, password);

				if (!LanyangHttp.login()) {
					outputGUIMessage("登录蓝洋会员失败!\n");
					return;
				}
				
				
				lanyangReloginThread = new LanyangReloginThread();
				lanyangReloginThread.start();

				loginToLanyangMemberSuccess = true;

				ConfigWriter.updateLanyangMemberAddress(address);
				ConfigWriter.updateLanyangMemberAccount(account);
				ConfigWriter.updateLanyangMemberPassword(password);

				ConfigWriter.saveTofile("common.config");

				if (loginToLanyangMemberSuccess && loginToProxySuccess)
					enableLanyangMemberBet(true);

				outputGUIMessage("登录蓝洋会员成功!\n");

			}
		});

		btnLanyangMemberLogin.setSize(50, 25);
		btnLanyangMemberLogin.setLocation(LanyangMemberX, LanyangMemberY + 120);

		btnBetLanyangCQSSC = new Button("正投重庆时时彩");
		/*btnBetLanyangCQSSC
				.addActionListener(new BetLanyangOppositeCQSSCListener(this));*/

		btnBetLanyangCQSSC.setSize(90, 25);
		btnBetLanyangCQSSC.setLocation(LanyangMemberX, LanyangMemberY + 150);

		Label labelLanyangPercent = new Label("投注比例:");
		labelLanyangPercent.setSize(60, 25);
		labelLanyangPercent.setLocation(LanyangMemberX + 100,
				LanyangMemberY + 150);

		textFieldCQSSCBetLanyangPercent = new TextField();
		textFieldCQSSCBetLanyangPercent.setSize(60, 25);
		textFieldCQSSCBetLanyangPercent.setLocation(LanyangMemberX + 160,
				LanyangMemberY + 150);

		btnOppositeBetLanyangCQSSC = new Button("反投重庆时时彩");
/*		btnOppositeBetLanyangCQSSC
				.addActionListener(new BetLanyangOppositeCQSSCListener(this));*/

		btnOppositeBetLanyangCQSSC.setSize(90, 25);
		btnOppositeBetLanyangCQSSC.setLocation(LanyangMemberX,
				LanyangMemberY + 180);

		btnStopBetLanyangCQSSC = new Button("停止投注");
/*		btnStopBetLanyangCQSSC
				.addActionListener(new StopBetLanyangCQSSCListener(this));*/

		btnStopBetLanyangCQSSC.setSize(90, 25);
		btnStopBetLanyangCQSSC.setLocation(LanyangMemberX + 100,
				LanyangMemberY + 180);

		btnBetLanyangBJSC = new Button("正投北京赛车");
		btnBetLanyangBJSC.addActionListener(new BetBJSCListener(this, client));

		btnBetLanyangBJSC.setSize(90, 25);
		btnBetLanyangBJSC.setLocation(LanyangMemberX, LanyangMemberY + 210);

		Label BJSClabelLanyangPercent = new Label("投注比例:");
		BJSClabelLanyangPercent.setSize(60, 25);
		BJSClabelLanyangPercent.setLocation(LanyangMemberX + 100,
				LanyangMemberY + 210);

		textFieldBJSCBetLanyangPercent = new TextField();
		textFieldBJSCBetLanyangPercent.setSize(60, 25);
		textFieldBJSCBetLanyangPercent.setLocation(LanyangMemberX + 160,
				LanyangMemberY + 210);

		btnOppositeLanyangBJSC = new Button("反投北京赛车");
		btnOppositeLanyangBJSC
				.addActionListener(new BetLanyangOppositeBJSCListener(this, client));

		btnOppositeLanyangBJSC.setSize(90, 25);
		btnOppositeLanyangBJSC
				.setLocation(LanyangMemberX, LanyangMemberY + 240);
		
		
		
		btnBetLanyangBJSCAmount = new Button("投注金额");
		btnBetLanyangBJSCAmount.addActionListener(new BetLanyangBJSCAmountListener());

		btnBetLanyangBJSCAmount.setSize(90, 25);
		btnBetLanyangBJSCAmount.setLocation(LanyangMemberX + 100,
				LanyangMemberY + 240);
		
		

		btnStopBetLanyangBJSC = new Button("停止投注");
		btnStopBetLanyangBJSC.addActionListener(new StopBetLanyangBJSCListener(
				this));

		btnStopBetLanyangBJSC.setSize(90, 25);
		btnStopBetLanyangBJSC.setLocation(LanyangMemberX + 200,
				LanyangMemberY + 240);

		labelLanyangTotalBets = new Label();
		labelLanyangTotalBets.setSize(300, 25);
		labelLanyangTotalBets.setLocation(LanyangMemberX, 400);
		labelLanyangTotalBets.setText("下单次数:0");

		labelLanyangSuccessBets = new Label();
		labelLanyangSuccessBets.setSize(300, 25);
		labelLanyangSuccessBets.setLocation(LanyangMemberX, 430);
		labelLanyangSuccessBets.setText("成功次数:0");

		labelLanyangFailBets = new Label();
		labelLanyangFailBets.setSize(300, 25);
		labelLanyangFailBets.setLocation(LanyangMemberX, 460);
		labelLanyangFailBets.setText("失败次数:0");

		/*
		 * panel.add(labelLanyangTotalBets); panel.add(labelLanyangSuccessBets);
		 * panel.add(labelLanyangFailBets);
		 */

		panel.add(labelLanyangMemberLogin);
		panel.add(labelLanyangMemberAddress);
		panel.add(textFieldLanyangMemberAddress);
		panel.add(labelLanyangMemberAccount);
		panel.add(textFieldLanyangMemberAccount);
		panel.add(labelLanyangMemberPassword);
		panel.add(textFieldLanyangMemberPassword);
		panel.add(btnLanyangMemberLogin);

		panel.add(btnBetLanyangCQSSC);
		panel.add(labelLanyangPercent);
		panel.add(btnOppositeBetLanyangCQSSC);
		panel.add(btnBetLanyangBJSC);
		panel.add(btnOppositeLanyangBJSC);
		panel.add(BJSClabelLanyangPercent);
		panel.add(textFieldCQSSCBetLanyangPercent);
		panel.add(textFieldBJSCBetLanyangPercent);
		panel.add(btnStopBetLanyangCQSSC);
		panel.add(btnStopBetLanyangBJSC);
		panel.add(btnBetLanyangBJSCAmount);

		enableLanyangMemberBet(false);
		
		
		// 华润会员界面
		int HuarunMemberX = 1050;
		int HuarunMemberY = 350;

		Label labelHuarunMemberLogin = new Label("华润会员登录:");
		labelHuarunMemberLogin.setSize(100, 25);
		labelHuarunMemberLogin.setLocation(HuarunMemberX, HuarunMemberY);

		Label labelHuarunMemberAddress = new Label("网址:");
		labelHuarunMemberAddress.setSize(50, 25);
		labelHuarunMemberAddress.setLocation(HuarunMemberX,
				HuarunMemberY + 30);

		textFieldHuarunMemberAddress = new TextField();
		textFieldHuarunMemberAddress.setSize(300, 25);
		textFieldHuarunMemberAddress.setLocation(HuarunMemberX + 50,
				HuarunMemberY + 30);
		textFieldHuarunMemberAddress.setText(ConfigReader
				.gethuarunBetAddress());

		Label labelHuarunMemberAccount = new Label("账户:");
		labelHuarunMemberAccount.setSize(50, 25);
		labelHuarunMemberAccount.setLocation(HuarunMemberX,
				HuarunMemberY + 60);

		textFieldHuarunMemberAccount = new TextField();
		textFieldHuarunMemberAccount.setSize(300, 25);
		textFieldHuarunMemberAccount.setLocation(HuarunMemberX + 50,
				HuarunMemberY + 60);
		textFieldHuarunMemberAccount.setText(ConfigReader
				.gethuarunBetAccount());

		Label labelHuarunMemberPassword = new Label("密码:");
		labelHuarunMemberPassword.setSize(50, 25);
		labelHuarunMemberPassword.setLocation(HuarunMemberX,
				HuarunMemberY + 90);

		textFieldHuarunMemberPassword = new TextField();
		textFieldHuarunMemberPassword.setSize(300, 25);
		textFieldHuarunMemberPassword.setLocation(HuarunMemberX + 50,
				HuarunMemberY + 90);
		textFieldHuarunMemberPassword.setText(ConfigReader
				.gethuarunBetPassword());
		textFieldHuarunMemberPassword.setEchoChar('*');

		Button btnHuarunMemberLogin = new Button("登录");
		btnHuarunMemberLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (loginToHuarunMemberSuccess == true || loginToProxySuccess ==false)
					return;
				
				if(loginToProxySuccess ==false) {
					outputGUIMessage("请先连接服务器!\n");
					return;
				}

				String address = textFieldHuarunMemberAddress.getText();
				String account = textFieldHuarunMemberAccount.getText();
				String password = textFieldHuarunMemberPassword.getText();

				//HuarunHttp = new HuarunHttp();

				HuarunHttp.setLoginParams(address, account, password);

				if (!HuarunHttp.loginTohuarun() && !HuarunHttp.login()) {
					outputGUIMessage("登录华润会员失败!\n");
					return;
				}
				
				//new BetHuarunBJSCThread(client).start();
				loginToHuarunMemberSuccess = true;
				
				if (loginToHuarunMemberSuccess && loginToProxySuccess)
					enableHuarunMemberBet(true);

				outputGUIMessage("登录华润会员成功!\n");
				
				/*HuarunReloginThread = new HuarunReloginThread();
				HuarunReloginThread.start();

				loginToHuarunMemberSuccess = true;

				ConfigWriter.updateHuarunMemberAddress(address);
				ConfigWriter.updateHuarunMemberAccount(account);
				ConfigWriter.updateHuarunMemberPassword(password);

				ConfigWriter.saveTofile("common.config");

				if (loginToHuarunMemberSuccess && loginToProxySuccess)
					enableHuarunMemberBet(true);

				outputGUIMessage("登录华润会员成功!\n");*/

			}
		});

		btnHuarunMemberLogin.setSize(50, 25);
		btnHuarunMemberLogin.setLocation(HuarunMemberX, HuarunMemberY + 120);

		btnBetHuarunCQSSC = new Button("正投重庆时时彩");
		/*btnBetHuarunCQSSC
				.addActionListener(new BetHuarunOppositeCQSSCListener(this));*/

		btnBetHuarunCQSSC.setSize(90, 25);
		btnBetHuarunCQSSC.setLocation(HuarunMemberX, HuarunMemberY + 150);

		Label labelHuarunPercent = new Label("投注比例:");
		labelHuarunPercent.setSize(60, 25);
		labelHuarunPercent.setLocation(HuarunMemberX + 100,
				HuarunMemberY + 150);

		textFieldCQSSCBetHuarunPercent = new TextField();
		textFieldCQSSCBetHuarunPercent.setSize(60, 25);
		textFieldCQSSCBetHuarunPercent.setLocation(HuarunMemberX + 160,
				HuarunMemberY + 150);

		btnOppositeBetHuarunCQSSC = new Button("反投重庆时时彩");
/*		btnOppositeBetHuarunCQSSC
				.addActionListener(new BetHuarunOppositeCQSSCListener(this));*/

		btnOppositeBetHuarunCQSSC.setSize(90, 25);
		btnOppositeBetHuarunCQSSC.setLocation(HuarunMemberX,
				HuarunMemberY + 180);

		btnStopBetHuarunCQSSC = new Button("停止投注");
/*		btnStopBetHuarunCQSSC
				.addActionListener(new StopBetHuarunCQSSCListener(this));*/

		btnStopBetHuarunCQSSC.setSize(90, 25);
		btnStopBetHuarunCQSSC.setLocation(HuarunMemberX + 100,
				HuarunMemberY + 180);

		btnBetHuarunBJSC = new Button("正投北京赛车");
		btnBetHuarunBJSC.addActionListener(new BetBJSCListener(this, client));

		btnBetHuarunBJSC.setSize(90, 25);
		btnBetHuarunBJSC.setLocation(HuarunMemberX, HuarunMemberY + 210);

		Label BJSClabelHuarunPercent = new Label("投注比例:");
		BJSClabelHuarunPercent.setSize(60, 25);
		BJSClabelHuarunPercent.setLocation(HuarunMemberX + 100,
				HuarunMemberY + 210);

		textFieldBJSCBetHuarunPercent = new TextField();
		textFieldBJSCBetHuarunPercent.setSize(60, 25);
		textFieldBJSCBetHuarunPercent.setLocation(HuarunMemberX + 160,
				HuarunMemberY + 210);

		btnOppositeHuarunBJSC = new Button("反投北京赛车");
		btnOppositeHuarunBJSC
				.addActionListener(new BetHuarunOppositeBJSCListener(this, client));

		btnOppositeHuarunBJSC.setSize(90, 25);
		btnOppositeHuarunBJSC
				.setLocation(HuarunMemberX, HuarunMemberY + 240);
		
		
		
		btnBetHuarunBJSCAmount = new Button("投注金额");
		/*btnBetHuarunBJSCAmount.addActionListener(new BetHuarunBJSCAmountListener());
*/
		btnBetHuarunBJSCAmount.setSize(90, 25);
		btnBetHuarunBJSCAmount.setLocation(HuarunMemberX + 100,
				HuarunMemberY + 240);
		
		

		btnStopBetHuarunBJSC = new Button("停止投注");
		/*btnStopBetHuarunBJSC.addActionListener(new StopBetHuarunBJSCListener(
				this));*/

		btnStopBetHuarunBJSC.setSize(90, 25);
		btnStopBetHuarunBJSC.setLocation(HuarunMemberX + 200,
				HuarunMemberY + 240);

		labelHuarunTotalBets = new Label();
		labelHuarunTotalBets.setSize(300, 25);
		labelHuarunTotalBets.setLocation(HuarunMemberX, 400);
		labelHuarunTotalBets.setText("下单次数:0");

		labelHuarunSuccessBets = new Label();
		labelHuarunSuccessBets.setSize(300, 25);
		labelHuarunSuccessBets.setLocation(HuarunMemberX, 430);
		labelHuarunSuccessBets.setText("成功次数:0");

		labelHuarunFailBets = new Label();
		labelHuarunFailBets.setSize(300, 25);
		labelHuarunFailBets.setLocation(HuarunMemberX, 460);
		labelHuarunFailBets.setText("失败次数:0");

		/*
		 * panel.add(labelHuarunTotalBets); panel.add(labelHuarunSuccessBets);
		 * panel.add(labelHuarunFailBets);
		 */

		panel.add(labelHuarunMemberLogin);
		panel.add(labelHuarunMemberAddress);
		panel.add(textFieldHuarunMemberAddress);
		panel.add(labelHuarunMemberAccount);
		panel.add(textFieldHuarunMemberAccount);
		panel.add(labelHuarunMemberPassword);
		panel.add(textFieldHuarunMemberPassword);
		panel.add(btnHuarunMemberLogin);

		panel.add(btnBetHuarunCQSSC);
		panel.add(labelHuarunPercent);
		panel.add(btnOppositeBetHuarunCQSSC);
		panel.add(btnBetHuarunBJSC);
		panel.add(btnOppositeHuarunBJSC);
		panel.add(BJSClabelHuarunPercent);
		panel.add(textFieldCQSSCBetHuarunPercent);
		panel.add(textFieldBJSCBetHuarunPercent);
		panel.add(btnStopBetHuarunCQSSC);
		panel.add(btnStopBetHuarunBJSC);
		panel.add(btnBetHuarunBJSCAmount);

		enableHuarunMemberBet(false);
		
		
		
		// 亚博会员界面
		int YaboMemberX = 1550;
		int YaboMemberY = 350;

		Label labelYaboMemberLogin = new Label("亚博会员登录:");
		labelYaboMemberLogin.setSize(100, 25);
		labelYaboMemberLogin.setLocation(YaboMemberX, YaboMemberY);

		Label labelYaboMemberAddress = new Label("网址:");
		labelYaboMemberAddress.setSize(50, 25);
		labelYaboMemberAddress.setLocation(YaboMemberX,
				YaboMemberY + 30);

		textFieldYaboMemberAddress = new TextField();
		textFieldYaboMemberAddress.setSize(300, 25);
		textFieldYaboMemberAddress.setLocation(YaboMemberX + 50,
				YaboMemberY + 30);
		textFieldYaboMemberAddress.setText(ConfigReader
				.getyaboBetAddress());

		Label labelYaboMemberAccount = new Label("账户:");
		labelYaboMemberAccount.setSize(50, 25);
		labelYaboMemberAccount.setLocation(YaboMemberX,
				YaboMemberY + 60);

		textFieldYaboMemberAccount = new TextField();
		textFieldYaboMemberAccount.setSize(300, 25);
		textFieldYaboMemberAccount.setLocation(YaboMemberX + 50,
				YaboMemberY + 60);
		textFieldYaboMemberAccount.setText(ConfigReader
				.getyaboBetAccount());

		Label labelYaboMemberPassword = new Label("密码:");
		labelYaboMemberPassword.setSize(50, 25);
		labelYaboMemberPassword.setLocation(YaboMemberX,
				YaboMemberY + 90);

		textFieldYaboMemberPassword = new TextField();
		textFieldYaboMemberPassword.setSize(300, 25);
		textFieldYaboMemberPassword.setLocation(YaboMemberX + 50,
				YaboMemberY + 90);
		textFieldYaboMemberPassword.setText(ConfigReader
				.getyaboBetPassword());
		textFieldYaboMemberPassword.setEchoChar('*');

		Button btnYaboMemberLogin = new Button("登录");
		btnYaboMemberLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (loginToYaboMemberSuccess == true || loginToProxySuccess ==false)
					return;
				
				if(loginToProxySuccess ==false) {
					outputGUIMessage("请先连接服务器!\n");
					return;
				}

				String address = textFieldYaboMemberAddress.getText();
				String account = textFieldYaboMemberAccount.getText();
				String password = textFieldYaboMemberPassword.getText();

				//YaboHttp = new YaboHttp();

				YaboHttp.setLoginParams(address, account, password);

				if (!YaboHttp.login() && !YaboHttp.loginToYabo()) {
					outputGUIMessage("登录亚博会员失败!\n");
					return;
				}
				
				//new BetYaboBJSCThread(client).start();
				loginToYaboMemberSuccess = true;
				
				if (loginToYaboMemberSuccess && loginToProxySuccess)
					enableYaboMemberBet(true);

				outputGUIMessage("登录亚博会员成功!\n");
				
				/*YaboReloginThread = new YaboReloginThread();
				YaboReloginThread.start();

				loginToYaboMemberSuccess = true;

				ConfigWriter.updateYaboMemberAddress(address);
				ConfigWriter.updateYaboMemberAccount(account);
				ConfigWriter.updateYaboMemberPassword(password);

				ConfigWriter.saveTofile("common.config");

				if (loginToYaboMemberSuccess && loginToProxySuccess)
					enableYaboMemberBet(true);

				outputGUIMessage("登录华润会员成功!\n");*/

			}
		});

		btnYaboMemberLogin.setSize(50, 25);
		btnYaboMemberLogin.setLocation(YaboMemberX, YaboMemberY + 120);

		btnBetYaboCQSSC = new Button("正投重庆时时彩");
		/*btnBetYaboCQSSC
				.addActionListener(new BetYaboOppositeCQSSCListener(this));*/

		btnBetYaboCQSSC.setSize(90, 25);
		btnBetYaboCQSSC.setLocation(YaboMemberX, YaboMemberY + 150);

		Label labelYaboPercent = new Label("投注比例:");
		labelYaboPercent.setSize(60, 25);
		labelYaboPercent.setLocation(YaboMemberX + 100,
				YaboMemberY + 150);

		textFieldCQSSCBetYaboPercent = new TextField();
		textFieldCQSSCBetYaboPercent.setSize(60, 25);
		textFieldCQSSCBetYaboPercent.setLocation(YaboMemberX + 160,
				YaboMemberY + 150);

		btnOppositeBetYaboCQSSC = new Button("反投重庆时时彩");
/*		btnOppositeBetYaboCQSSC
				.addActionListener(new BetYaboOppositeCQSSCListener(this));*/

		btnOppositeBetYaboCQSSC.setSize(90, 25);
		btnOppositeBetYaboCQSSC.setLocation(YaboMemberX,
				YaboMemberY + 180);

		btnStopBetYaboCQSSC = new Button("停止投注");
/*		btnStopBetYaboCQSSC
				.addActionListener(new StopBetYaboCQSSCListener(this));*/

		btnStopBetYaboCQSSC.setSize(90, 25);
		btnStopBetYaboCQSSC.setLocation(YaboMemberX + 100,
				YaboMemberY + 180);

		btnBetYaboBJSC = new Button("正投北京赛车");
		btnBetYaboBJSC.addActionListener(new BetBJSCListener(this, client));

		btnBetYaboBJSC.setSize(90, 25);
		btnBetYaboBJSC.setLocation(YaboMemberX, YaboMemberY + 210);

		Label BJSClabelYaboPercent = new Label("投注比例:");
		BJSClabelYaboPercent.setSize(60, 25);
		BJSClabelYaboPercent.setLocation(YaboMemberX + 100,
				YaboMemberY + 210);

		textFieldBJSCBetYaboPercent = new TextField();
		textFieldBJSCBetYaboPercent.setSize(60, 25);
		textFieldBJSCBetYaboPercent.setLocation(YaboMemberX + 160,
				YaboMemberY + 210);

		btnOppositeYaboBJSC = new Button("反投北京赛车");
		btnOppositeYaboBJSC
				.addActionListener(new BetYaboOppositeBJSCListener(this, client));

		btnOppositeYaboBJSC.setSize(90, 25);
		btnOppositeYaboBJSC
				.setLocation(YaboMemberX, YaboMemberY + 240);
		
		
		
		btnBetYaboBJSCAmount = new Button("投注金额");
		/*btnBetYaboBJSCAmount.addActionListener(new BetYaboBJSCAmountListener());
*/
		btnBetYaboBJSCAmount.setSize(90, 25);
		btnBetYaboBJSCAmount.setLocation(YaboMemberX + 100,
				YaboMemberY + 240);
		
		

		btnStopBetYaboBJSC = new Button("停止投注");
		/*btnStopBetYaboBJSC.addActionListener(new StopBetYaboBJSCListener(
				this));*/

		btnStopBetYaboBJSC.setSize(90, 25);
		btnStopBetYaboBJSC.setLocation(YaboMemberX + 200,
				YaboMemberY + 240);

		labelYaboTotalBets = new Label();
		labelYaboTotalBets.setSize(300, 25);
		labelYaboTotalBets.setLocation(YaboMemberX, 400);
		labelYaboTotalBets.setText("下单次数:0");

		labelYaboSuccessBets = new Label();
		labelYaboSuccessBets.setSize(300, 25);
		labelYaboSuccessBets.setLocation(YaboMemberX, 430);
		labelYaboSuccessBets.setText("成功次数:0");

		labelYaboFailBets = new Label();
		labelYaboFailBets.setSize(300, 25);
		labelYaboFailBets.setLocation(YaboMemberX, 460);
		labelYaboFailBets.setText("失败次数:0");

		/*
		 * panel.add(labelYaboTotalBets); panel.add(labelYaboSuccessBets);
		 * panel.add(labelYaboFailBets);
		 */

		panel.add(labelYaboMemberLogin);
		panel.add(labelYaboMemberAddress);
		panel.add(textFieldYaboMemberAddress);
		panel.add(labelYaboMemberAccount);
		panel.add(textFieldYaboMemberAccount);
		panel.add(labelYaboMemberPassword);
		panel.add(textFieldYaboMemberPassword);
		panel.add(btnYaboMemberLogin);

		panel.add(btnBetYaboCQSSC);
		panel.add(labelYaboPercent);
		panel.add(btnOppositeBetYaboCQSSC);
		panel.add(btnBetYaboBJSC);
		panel.add(btnOppositeYaboBJSC);
		panel.add(BJSClabelYaboPercent);
		panel.add(textFieldCQSSCBetYaboPercent);
		panel.add(textFieldBJSCBetYaboPercent);
		panel.add(btnStopBetYaboCQSSC);
		panel.add(btnStopBetYaboBJSC);
		panel.add(btnBetYaboBJSCAmount);

		enableYaboMemberBet(false);
		
		
		
		
		
		
		
		
		
		
		// !lin 抓取界面使用测试
		/*
		 * btnStartGrabCQSSC = new Button("开抓重庆时彩");
		 * btnStartGrabCQSSC.setSize(90, 25); btnStartGrabCQSSC.setLocation(50,
		 * 230); btnStopGrabCQSSC = new Button("停抓重庆时彩");
		 * btnStopGrabCQSSC.setSize(90, 25); btnStopGrabCQSSC.setLocation(150,
		 * 230);
		 * 
		 * btnStartGrabBJSC = new Button("开抓北京赛车"); btnStartGrabBJSC.setSize(90,
		 * 25); btnStartGrabBJSC.setLocation(50, 260); btnStopGrabBJSC = new
		 * Button("停抓北京赛车"); btnStopGrabBJSC.setSize(90, 25);
		 * btnStopGrabBJSC.setLocation(150, 260);
		 * 
		 * btnStartGrabXYNC = new Button("开抓幸运农场");
		 * btnStartGrabXYNC.setSize(90,25); btnStartGrabXYNC.setLocation(50,
		 * 290);
		 * 
		 * btnStopGrabXYNC = new Button("停抓幸运农场");
		 * btnStopGrabXYNC.setSize(90,25); btnStopGrabXYNC.setLocation(150,
		 * 290);
		 * 
		 * 
		 * 
		 * 
		 * btnStartGrabCQSSC.addActionListener(new ActionListener(){ public void
		 * actionPerformed(ActionEvent e) { grabThread.startGrabCQSSC(); } });
		 * 
		 * btnStopGrabCQSSC.addActionListener(new ActionListener(){ public void
		 * actionPerformed(ActionEvent e) { grabThread.stopGrabCQSSC(); } });
		 * 
		 * btnStartGrabBJSC.addActionListener(new ActionListener(){ public void
		 * actionPerformed(ActionEvent e) { grabThread.startGrabBJSC(); } });
		 * 
		 * btnStopGrabBJSC.addActionListener(new ActionListener(){ public void
		 * actionPerformed(ActionEvent e) { grabThread.stopGrabBJSC(); } });
		 * 
		 * btnStartGrabXYNC.addActionListener(new ActionListener(){ public void
		 * actionPerformed(ActionEvent e) { XYNCthread.startGrabXYNC(); } });
		 * 
		 * btnStopGrabXYNC.addActionListener(new ActionListener(){ public void
		 * actionPerformed(ActionEvent e) { XYNCthread.stopGrabXYNC(); } });
		 * 
		 * enableGrabBtn(false);
		 * 
		 * panel.add(btnStartGrabCQSSC); panel.add(btnStopGrabCQSSC);
		 * panel.add(btnStartGrabBJSC); panel.add(btnStopGrabBJSC);
		 * panel.add(btnStartGrabXYNC); panel.add(btnStopGrabXYNC);
		 * 
		 * labelTotalBets = new Label(); labelTotalBets.setSize(300,25);
		 * labelTotalBets.setLocation(50, 400);
		 * labelTotalBets.setText("下单次数:0");
		 * 
		 * labelSuccessBets = new Label(); labelSuccessBets.setSize(300,25);
		 * labelSuccessBets.setLocation(50, 430);
		 * labelSuccessBets.setText("成功次数:0");
		 * 
		 * labelFailBets = new Label(); labelFailBets.setSize(300,25);
		 * labelFailBets.setLocation(50, 460); labelFailBets.setText("失败次数:0");
		 */

		/*
		 * panel.add(labelTotalBets); panel.add(labelSuccessBets);
		 * panel.add(labelFailBets);
		 */

		// !lin end

		outputMessage = new TextArea();
		outputMessage.setSize(400, 500);
		outputMessage.setLocation(50, 500);

		panel.add(outputMessage);

		mainFrame.setSize(1920, 1080);

		mainFrame.setVisible(true);

		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int n = JOptionPane.showConfirmDialog(null, "确认退出吗?", "退出程序",
						JOptionPane.YES_NO_OPTION);
				if (n == JOptionPane.YES_OPTION) {
					System.out.println(outputMessage.getText()); // 退出时保存界面输出到log
					System.exit(0);
				}
			}
		});
	}

}
