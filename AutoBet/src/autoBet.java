import java.awt.*;
import java.awt.event.*;

public class autoBet{
	
	public boolean loginSuccess = false;
	public TextField textFieldCQSSCBetPercent;
	
	public static void main(String[] args) throws Exception {
		

		ConfigReader.read("common.config");

		new autoBet().launchFrame();

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
		
		
		Button btnBetCQSSC = new Button("投重庆时时彩");
		btnBetCQSSC.addActionListener(new BetCQSSCListener(this));
		
		btnBetCQSSC.setSize(80, 25);
		btnBetCQSSC.setLocation(50,75);
		
		Label labelPercent = new Label("百分比:");
		labelPercent.setSize(60, 25);
		labelPercent.setLocation(140, 75);
		
		textFieldCQSSCBetPercent = new TextField();
		textFieldCQSSCBetPercent.setSize(60, 25);
		textFieldCQSSCBetPercent.setLocation(200, 75);
		
		panel.add(btnLogin);
		panel.add(btnBetCQSSC);
		panel.add(labelPercent);
		panel.add(textFieldCQSSCBetPercent);
		
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


