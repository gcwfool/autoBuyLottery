import java.io.File;  

import javax.xml.parsers.DocumentBuilder;  
import javax.xml.parsers.DocumentBuilderFactory;  
  
import org.w3c.dom.Document;  
import org.w3c.dom.Element;  
import org.w3c.dom.NodeList; 

public class ConfigReader {
	
	String address = "";
	String account = "";
	String password = "";
	String tessPath = "";
	
	public boolean read(String filename) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
        
//      System.out.println("class name: " + dbf.getClass().getName());  
          
        // step 2:获得具体的dom解析器  
		try {
			DocumentBuilder db = dbf.newDocumentBuilder(); 

	        Document document = db.parse(new File(filename));  
	          
	        NodeList list = document.getElementsByTagName("LOGIN");  
	          
	        Element element = (Element)list.item(0);  
	              
	        address = element.getElementsByTagName("ADDRESS").item(0).getFirstChild().getNodeValue();               
	        System.out.println("address:" + address);  
	              
	        account = element.getElementsByTagName("ACCOUNT").item(0).getFirstChild().getNodeValue();    
	        System.out.println("account:" + account);  
	              
	        password = element.getElementsByTagName("PASSWORD").item(0).getFirstChild().getNodeValue();  
	        System.out.println("password:" + password);
	        
	        tessPath = element.getElementsByTagName("PATH").item(0).getFirstChild().getNodeValue();  
	        System.out.println("password:" + password);
	        
	        return true;
		}catch(Exception e){   
			e.printStackTrace();	   
		}
			  
        return false;
		
	}
	
	public  String getAddress() {
		return address;
		
	}
	
	public  String getAccount() {
		return account;
		
	}

	public  String getPassword() {
		return password;
	
	}
	
	public  String getTessPath() {
		return tessPath;
	
	}

}
