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
		if(ab.loginSuccess == true)
			return;
		
		while(DsnProxyGrab.doLogin() == false) {
		}		
		while(dsnHttp.loginToDsn() == false);
		
		ab.loginSuccess = true;
		
		ab.outputMessage.append("µÇÂ¼³É¹¦\n");
	}
}
