import java.awt.*;
import java.awt.event.*;


public class LoginListener implements ActionListener
{
	private autoBet ab;
	
	public LoginListener(autoBet ab) {
		this.ab = ab;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		while(DsnProxyGrab.doLogin() == false) {
		}		
		while(dsnHttp.loginToDsn() == false);
		
		ab.loginSuccess = true;
	}
}
