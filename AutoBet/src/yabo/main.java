package yabo;

import java.io.IOException;

import dsn.ConfigReader;
import dsn.ConfigWriter;

public class main {
	public static void main(String [] args) throws IOException{
		
		ConfigReader.read("common.config");
		ConfigWriter.open("common.config");
		YaboHttp.setLoginParams(ConfigReader.getyaboBetAddress(), ConfigReader.getyaboBetAccount(), ConfigReader.getyaboBetPassword());
		YaboHttp.login();
		while(true) {
			BetBJSCManager.grabGameInfo();
			try {
				Thread.sleep(10000);
			} catch(Exception e) {
				
			}
		}
    }

}
