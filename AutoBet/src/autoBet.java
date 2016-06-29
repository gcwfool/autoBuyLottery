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
			//TODO �����Ի�����ʾ�������
		}
	}
	
	public  void launchFrame()
	{

	    Frame mainFrame =new Frame("�Զ��µ���");

		
		Panel panel=new Panel();
		
	    panel.setSize(500,800);

	    panel.setLocation(0,0);
	    panel.setLayout(null);
	    mainFrame.setLayout(null);

	    mainFrame.add(panel);

	       
		
		
		
		Button btnLogin = new Button("��¼");
		btnLogin.addActionListener(new LoginListener(this));
		
		btnLogin.setSize(50, 25);
		btnLogin.setLocation(50,50);
		
		
		Label labelBetTime = new Label("����̻���           ��ʱ����Ͷע");
		labelBetTime.setSize(200, 25);
		labelBetTime.setLocation(60, 80);
		
		textFieldBetTime = new TextField();
		textFieldBetTime.setSize(30, 25);
		textFieldBetTime.setLocation(125, 80);
		
		Label labelPercent = new Label("Ͷע����:");
		labelPercent.setSize(60, 25);
		labelPercent.setLocation(150, 110);
		
		textFieldCQSSCBetPercent = new TextField();
		textFieldCQSSCBetPercent.setSize(60, 25);
		textFieldCQSSCBetPercent.setLocation(210, 110);
		
		
		Label BJSClabelPercent = new Label("Ͷע����:");
		BJSClabelPercent.setSize(60, 25);
		BJSClabelPercent.setLocation(150, 170);
		
		textFieldBJSCBetPercent = new TextField();
		textFieldBJSCBetPercent.setSize(60, 25);
		textFieldBJSCBetPercent.setLocation(210, 170);
		
		
		Button btnBetCQSSC = new Button("��Ͷ����ʱʱ��");
		btnBetCQSSC.addActionListener(new BetCQSSCListener(this));
		
		btnBetCQSSC.setSize(90, 25);
		btnBetCQSSC.setLocation(50,110);
		
		Button btnOppositeBetCQSSC = new Button("��Ͷ����ʱʱ��");
		btnOppositeBetCQSSC.addActionListener(new BetOppositeCQSSCListener(this));
		
		btnOppositeBetCQSSC.setSize(90, 25);
		btnOppositeBetCQSSC.setLocation(50,140);
		
		
		Button btnStopBetCQSSC = new Button("ֹͣͶע");
		btnStopBetCQSSC.addActionListener(new StopBetCQSSCListener(this));
		
		btnStopBetCQSSC.setSize(90, 25);
		btnStopBetCQSSC.setLocation(150,140);
		
		
		Button btnBetBJSC = new Button("��Ͷ��������");
		btnBetBJSC.addActionListener(new BetBJSCListener(this));
		
		btnBetBJSC.setSize(75, 25);
		btnBetBJSC.setLocation(50,170);
		
		
		Button btnOppositeBJSC = new Button("��Ͷ��������");
		btnOppositeBJSC.addActionListener(new BetOppositeBJSCListener(this));
		
		btnOppositeBJSC.setSize(75, 25);
		btnOppositeBJSC.setLocation(50,200);
		
		
		Button btnStopBetBJSC = new Button("ֹͣͶע");
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


