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
//			String res = YaboHttp.doGet(YaboHttp.ADDRESS + "/hisorder/queryHistory.do?cdate=2017-06-09&rowNumPerPage=15", "", "");
//			
//			int posStart = 0;
//			
//			posStart = res.indexOf("622528期", posStart);
//			posStart = res.indexOf("td class='f_right'", posStart + 1);
//			posStart = res.indexOf("td class='f_right'", posStart + 1);
//			posStart = res.indexOf("td class='f_right'", posStart + 1);
//			System.out.println(res);
//			
//			
//			System.out.println(res.substring(posStart + 19, res.indexOf("</td>", posStart)));
			
			System.out.println(BetBJSCManager.getProfit("622528"));
			try {
				Thread.sleep(10000);
			} catch(Exception e) {
				
			}
		}
    }

}
