import java.awt.*;
import java.awt.event.*;

public class autoBet{
	
	public boolean loginSuccess = false;
	
	public static void main(String[] args) throws Exception {
		

		ConfigReader.read("common.config");

		new autoBet().launchFrame();

	}
	
	public  void launchFrame()
	{

	    Frame mainFrame =new Frame("�Զ��µ���");

		
		Panel panel=new Panel();
		
	    panel.setSize(100,50);

	    panel.setLocation(40,40);
	    
	    mainFrame.setLayout(null);

	    mainFrame.add(panel);

	       
		
		
		
		Button btnLogin = new Button("��¼");
		btnLogin.addActionListener(new LoginListener(this));
		
		btnLogin.setSize(50, 50);
		btnLogin.setLocation(50,50);
		
		
		Button btnBetCQSSC = new Button("Ͷ����ʱʱ��");
		btnBetCQSSC.addActionListener(new BetCQSSCListener(this));
		
		btnBetCQSSC.setSize(50, 100);
		btnBetCQSSC.setLocation(150,50);
		
		
		panel.add(btnLogin);
		panel.add(btnBetCQSSC);
		
		mainFrame.setSize(400, 800);
		
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


