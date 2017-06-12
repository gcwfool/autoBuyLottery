package dsn;
import java.io.File;  

import javax.xml.parsers.DocumentBuilder;  
import javax.xml.parsers.DocumentBuilderFactory;  
  
import org.w3c.dom.Document;  
import org.w3c.dom.Element;  
import org.w3c.dom.NodeList; 

public class ConfigReader {
	
	static String serverAddress = "";
	static String serverPort = "";
	
	
	static String proxyAddress = "";
	static String proxyAccount = "";
	static String proxyPassword = "";
	static String betAddress = "";
	static String betAccount = "";
	static String betPassword = "";
	static String weicaibetAddress = "";
	static String weicaibetAccount = "";
	static String weicaibetPassword = "";
	static String tiancaibetAddress = "";
	static String tiancaibetAccount = "";
	static String tiancaibetPassword = "";
	static String tessPath = "";
	
	
	
	static String lanyangbetAddress = "";
	static String lanyangbetAccount = "";
	static String lanyangbetPassword = "";
	
	static String huarunbetAddress = "";
	static String huarunbetAccount = "";
	static String huarunbetPassword = "";
	
	static String yabobetAddress = "";
	static String yabobetAccount = "";
	static String yabobetPassword = "";
	
	static String webetAddress = "";
	static String webetAccount = "";
	static String webetPassword = "";
	
	
	static String proxyAddress1 = "";
	static String proxyAddress2 = "";
	static String proxyAddress3 = "";
	
	static String betAddress1 = "";
	static String betAddress2 = "";
	static String betAddress3 = "";
	static String betAddress4 = "";
	
	
	public static boolean read(String filename) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();           
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
	        Document document = db.parse(new File(filename));  
	         
	        
	        
	        NodeList list = document.getElementsByTagName("SERVER");        
	        Element element = (Element)list.item(0);  
	        
	        serverAddress = element.getElementsByTagName("ADDRESS").item(0).getFirstChild().getNodeValue();               
	        System.out.println("serverAddress:" + serverAddress);  
	        
	        serverPort = element.getElementsByTagName("PORT").item(0).getFirstChild().getNodeValue();               
	        System.out.println("serverPort:" + serverPort);  
	        
	        
	        
	        //read proxy login msg
	        list = document.getElementsByTagName("PROXYLOGIN");        
	        element = (Element)list.item(0);  
            
	        proxyAddress = element.getElementsByTagName("ADDRESS").item(0).getFirstChild().getNodeValue();               
	        System.out.println("proxyaddress:" + proxyAddress);  
	        
	        //���е�˹�������ַ
	        proxyAddress1 = element.getElementsByTagName("ADDRESS1").item(0).getFirstChild().getNodeValue();               
	        System.out.println("proxyaddress1:" + proxyAddress1);  
	        
	        proxyAddress2 = element.getElementsByTagName("ADDRESS2").item(0).getFirstChild().getNodeValue();               
	        System.out.println("proxyaddress2:" + proxyAddress2);  
	        
	        proxyAddress3 = element.getElementsByTagName("ADDRESS3").item(0).getFirstChild().getNodeValue();               
	        System.out.println("proxyaddress3:" + proxyAddress3);  
	              
	        proxyAccount = element.getElementsByTagName("ACCOUNT").item(0).getFirstChild().getNodeValue();    
	        System.out.println("proxyaccount:" + proxyAccount);  
	              
	        proxyPassword = element.getElementsByTagName("PASSWORD").item(0).getFirstChild().getNodeValue();  
	        System.out.println("proxypassword:" + proxyPassword);
	        
	        //read bet login msg
	        list = document.getElementsByTagName("BETLOGIN");        
	        element = (Element)list.item(0);  
            
	        betAddress = element.getElementsByTagName("ADDRESS").item(0).getFirstChild().getNodeValue();               
	        System.out.println("betaddress:" + betAddress);  
	        
	        //���е�˹���Ա��ַ
	        betAddress1 = element.getElementsByTagName("ADDRESS1").item(0).getFirstChild().getNodeValue();               
	        System.out.println("betaddress1:" + betAddress1);  
	        
	        betAddress2 = element.getElementsByTagName("ADDRESS2").item(0).getFirstChild().getNodeValue();               
	        System.out.println("betaddress2:" + betAddress2);  
	        
	        betAddress3 = element.getElementsByTagName("ADDRESS3").item(0).getFirstChild().getNodeValue();               
	        System.out.println("betaddress3:" + betAddress3);  
	        
	        
	        betAddress4 = element.getElementsByTagName("ADDRESS4").item(0).getFirstChild().getNodeValue();               
	        System.out.println("betaddress4:" + betAddress4);  
	        
	              
	        betAccount = element.getElementsByTagName("ACCOUNT").item(0).getFirstChild().getNodeValue();    
	        System.out.println("betaccount:" + betAccount);  
	              
	        betPassword = element.getElementsByTagName("PASSWORD").item(0).getFirstChild().getNodeValue();  
	        System.out.println("betpassword:" + betPassword);
	        
	        
	        
	        //read weicai bet login msg
	        list = document.getElementsByTagName("WEICAIBETLOGIN");        
	        element = (Element)list.item(0);  
            
	        weicaibetAddress = element.getElementsByTagName("ADDRESS").item(0).getFirstChild().getNodeValue();               
	        System.out.println("weicaibetaddress:" + weicaibetAddress);  
	              
	        weicaibetAccount = element.getElementsByTagName("ACCOUNT").item(0).getFirstChild().getNodeValue();    
	        System.out.println("weicaibetaccount:" + weicaibetAccount);  
	              
	        weicaibetPassword = element.getElementsByTagName("PASSWORD").item(0).getFirstChild().getNodeValue();  
	        System.out.println("weicaibetpassword:" + weicaibetPassword);
	        
	        //read tiancai bet login msg
	        list = document.getElementsByTagName("TIANCAIBETLOGIN");        
	        element = (Element)list.item(0);  
            
	        tiancaibetAddress = element.getElementsByTagName("ADDRESS").item(0).getFirstChild().getNodeValue();               
	        System.out.println("tiancaibetaddress:" + tiancaibetAddress);  
	              
	        tiancaibetAccount = element.getElementsByTagName("ACCOUNT").item(0).getFirstChild().getNodeValue();    
	        System.out.println("tiancaibetaccount:" + tiancaibetAccount);  
	              
	        tiancaibetPassword = element.getElementsByTagName("PASSWORD").item(0).getFirstChild().getNodeValue();  
	        System.out.println("tiancaibetpassword:" + tiancaibetPassword);


	        //read lanyang bet login msg
	        list = document.getElementsByTagName("LANYANGBETLOGIN");        
	        element = (Element)list.item(0);  
            
	        lanyangbetAddress = element.getElementsByTagName("ADDRESS").item(0).getFirstChild().getNodeValue();               
	        System.out.println("lanyangbetaddress:" + lanyangbetAddress);  
	              
	        lanyangbetAccount = element.getElementsByTagName("ACCOUNT").item(0).getFirstChild().getNodeValue();    
	        System.out.println("lanyangbetaccount:" + lanyangbetAccount);  
	              
	        lanyangbetPassword = element.getElementsByTagName("PASSWORD").item(0).getFirstChild().getNodeValue();  
	        System.out.println("lanyangbetpassword:" + lanyangbetPassword);	  
	        
	        //read huarun bet login msg
	        list = document.getElementsByTagName("HUARUNBETLOGIN");        
	        element = (Element)list.item(0);  
            
	        huarunbetAddress = element.getElementsByTagName("ADDRESS").item(0).getFirstChild().getNodeValue();               
	        System.out.println("huarunbetaddress:" + huarunbetAddress);  
	              
	        huarunbetAccount = element.getElementsByTagName("ACCOUNT").item(0).getFirstChild().getNodeValue();    
	        System.out.println("huarunbetaccount:" + huarunbetAccount);  
	              
	        huarunbetPassword = element.getElementsByTagName("PASSWORD").item(0).getFirstChild().getNodeValue();  
	        System.out.println("huarunbetpassword:" + huarunbetPassword);	
	        
	        //read yabo bet login msg
	        list = document.getElementsByTagName("YABOBETLOGIN");        
	        element = (Element)list.item(0);  
            
	        yabobetAddress = element.getElementsByTagName("ADDRESS").item(0).getFirstChild().getNodeValue();               
	        System.out.println("huarunbetaddress:" + yabobetAddress);  
	              
	        yabobetAccount = element.getElementsByTagName("ACCOUNT").item(0).getFirstChild().getNodeValue();    
	        System.out.println("huarunbetaccount:" + yabobetAccount);  
	              
	        yabobetPassword = element.getElementsByTagName("PASSWORD").item(0).getFirstChild().getNodeValue();  
	        System.out.println("huarunbetpassword:" + yabobetPassword);	
	        
	        
	        //read webet bet login msg
	        list = document.getElementsByTagName("WEBETLOGIN");        
	        element = (Element)list.item(0);  
            
	        webetAddress = element.getElementsByTagName("ADDRESS").item(0).getFirstChild().getNodeValue();               
	        System.out.println("webet address:" + webetAddress);  
	              
	        webetAccount = element.getElementsByTagName("ACCOUNT").item(0).getFirstChild().getNodeValue();    
	        System.out.println("webet account:" + webetAccount);  
	              
	        webetPassword = element.getElementsByTagName("PASSWORD").item(0).getFirstChild().getNodeValue();  
	        System.out.println("webet password:" + webetPassword);	
	        
	        
	        
	        
	        //read tesseract-ocr path
	        list = document.getElementsByTagName("PATH");   
	        element = (Element)list.item(0);  
            
	        tessPath = element.getElementsByTagName("TESSPATH").item(0).getFirstChild().getNodeValue();               
	        System.out.println("tesspath:" + tessPath);  

	        return true;
		} catch(Exception e) {   
			e.printStackTrace();	   
		}
			  
        return false;	
	}
	
	
	public static String getServerAddress() {
		return serverAddress;
	}
	
	public static String getServerPort() {
		return serverPort;
	}
	
	
	public static String getProxyAddress() {
		return proxyAddress;
	}
	
	public static String getProxyAccount() {
		return proxyAccount;
	}
	
	public static String getProxyPassword() {
		return proxyPassword;
	}
	
	public static String getBetAddress() {
		return betAddress;
	}
	
	public static String getBetAccount() {
		return betAccount;
	}
	
	public static String getBetPassword() {
		return betPassword;
	}
	
	
	public static String getweicaiBetAddress() {
		return weicaibetAddress;
	}
	
	public static String getweicaiBetAccount() {
		return weicaibetAccount;
	}
	
	public static String getweicaiBetPassword() {
		return weicaibetPassword;
	}
	
	public static String gettiancaiBetAddress() {
		return tiancaibetAddress;
	}
	
	public static String gettiancaiBetAccount() {
		return tiancaibetAccount;
	}
	
	public static String gettiancaiBetPassword() {
		return tiancaibetPassword;
	}
	
	
	public static String getlanyangBetAddress() {
		return lanyangbetAddress;
	}
	
	public static String getlanyangBetAccount() {
		return lanyangbetAccount;
	}
	
	public static String getlanyangBetPassword() {
		return lanyangbetPassword;
	}
	
	public static String gethuarunBetAddress() {
		return huarunbetAddress;
	}
	
	public static String gethuarunBetAccount() {
		return huarunbetAccount;
	}
	
	public static String gethuarunBetPassword() {
		return huarunbetPassword;
	}
	
	public static String getyaboBetAddress() {
		return yabobetAddress;
	}
	
	public static String getyaboBetAccount() {
		return yabobetAccount;
	}
	
	public static String getyaboBetPassword() {
		return yabobetPassword;
	}
	
	public static String getWebetBetAddress() {
		return webetAddress;
	}
	
	public static String getWebetBetAccount() {
		return webetAccount;
	}
	
	public static String getWebetBetPassword() {
		return webetPassword;
	}
	
	
	public static String getTessPath() {
		return tessPath;
	}
	
	public static String[] getProxyAddressArray(){
		
		String[] addressArray = {proxyAddress1, proxyAddress2, proxyAddress3};
		
		return addressArray;
	}
	
	
	public static String[] getBetAddressArray(){
		
		String[] addressArray = {betAddress1, betAddress2, betAddress3, betAddress4};
		
		return addressArray;
	}

	
}
