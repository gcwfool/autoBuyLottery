import java.io.File;  

import javax.xml.parsers.DocumentBuilder;  
import javax.xml.parsers.DocumentBuilderFactory;  
  
import org.w3c.dom.Document;  
import org.w3c.dom.Element;  
import org.w3c.dom.NodeList; 

public class ConfigReader {
	
	static String proxyAddress = "";
	static String proxyAccount = "";
	static String proxyPassword = "";
	static String betAddress = "";
	static String betAccount = "";
	static String betPassword = "";
	static String weicaibetAddress = "";
	static String weicaibetAccount = "";
	static String weicaibetPassword = "";
	static String tessPath = "";
	
	public static boolean read(String filename) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();           
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
	        Document document = db.parse(new File(filename));  
	         
	        //read proxy login msg
	        NodeList list = document.getElementsByTagName("PROXYLOGIN");        
	        Element element = (Element)list.item(0);  
            
	        proxyAddress = element.getElementsByTagName("ADDRESS").item(0).getFirstChild().getNodeValue();               
	        System.out.println("proxyaddress:" + proxyAddress);  
	              
	        proxyAccount = element.getElementsByTagName("ACCOUNT").item(0).getFirstChild().getNodeValue();    
	        System.out.println("proxyaccount:" + proxyAccount);  
	              
	        proxyPassword = element.getElementsByTagName("PASSWORD").item(0).getFirstChild().getNodeValue();  
	        System.out.println("proxypassword:" + proxyPassword);
	        
	        //read bet login msg
	        list = document.getElementsByTagName("BETLOGIN");        
	        element = (Element)list.item(0);  
            
	        betAddress = element.getElementsByTagName("ADDRESS").item(0).getFirstChild().getNodeValue();               
	        System.out.println("betaddress:" + betAddress);  
	              
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
	
	
	public static String getTessPath() {
		return tessPath;
	}
	
	
}
