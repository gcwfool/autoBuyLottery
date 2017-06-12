package dsn;
import java.io.File;  

import javax.xml.parsers.DocumentBuilder;  
import javax.xml.parsers.DocumentBuilderFactory;  

import javax.xml.transform.Transformer;  
import javax.xml.transform.TransformerFactory;  
import javax.xml.transform.dom.DOMSource;  
import javax.xml.transform.stream.StreamResult;  
  

import org.w3c.dom.Document;  
import org.w3c.dom.Element;  
import org.w3c.dom.NodeList; 

public class ConfigWriter {
	
		
	static DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	static DocumentBuilder db;
	static Document document;
	
	
	 public static boolean saveTofile(String filename)   
	 {   
	      boolean flag = true;   
	      try   
	       {   
	            /** ��document�е�����д���ļ���   */   
	             TransformerFactory tFactory = TransformerFactory.newInstance();      
	             Transformer transformer = tFactory.newTransformer();    
	            /** ���� */   
	            //transformer.setOutputProperty(OutputKeys.ENCODING, "GB2312");   
	             DOMSource source = new DOMSource(document);    
	             StreamResult result = new StreamResult(new File(filename));      
	             transformer.transform(source, result);    
	         }catch(Exception ex)   
	         {   
	             flag = false;   
	             ex.printStackTrace();   
	         }   
	        return flag;         
	 }  
	
	
	public static boolean open(String filename){
		dbf = DocumentBuilderFactory.newInstance();           
		try {
			db = dbf.newDocumentBuilder();
	        document = db.parse(new File(filename));  

	        return true;
		} catch(Exception e) {   
			e.printStackTrace();	   
		}
			  
        return false;	
	}
	
	
	public static boolean updateServerAddress(String address){
		
		try{
	        NodeList list = document.getElementsByTagName("SERVER");        
	        Element element = (Element)list.item(0);  
	        
	        element.getElementsByTagName("ADDRESS").item(0).getFirstChild().setNodeValue(address);     
	        return true;
		}catch(Exception e) {   
			e.printStackTrace();	   
		}

		return false;
	}
	
	
	public static boolean updateServerPort(String address){
		
		try{
	        NodeList list = document.getElementsByTagName("SERVER");        
	        Element element = (Element)list.item(0);  
	        
	        element.getElementsByTagName("PORT").item(0).getFirstChild().setNodeValue(address);     
	        return true;
		}catch(Exception e) {   
			e.printStackTrace();	   
		}

		return false;
	}
	
	
	
	public static boolean updateProxyAddress(String address){
		
		try{
	        NodeList list = document.getElementsByTagName("PROXYLOGIN");        
	        Element element = (Element)list.item(0);  
	        
	        element.getElementsByTagName("ADDRESS").item(0).getFirstChild().setNodeValue(address);     
	        return true;
		}catch(Exception e) {   
			e.printStackTrace();	   
		}

		return false;
	}
	
	public static boolean updateProxyAccount(String account){
		
		try{
	        NodeList list = document.getElementsByTagName("PROXYLOGIN");        
	        Element element = (Element)list.item(0);  
	        
	        element.getElementsByTagName("ACCOUNT").item(0).getFirstChild().setNodeValue(account);     
	        return true;
		}catch(Exception e) {   
			e.printStackTrace();	   
		}

		return false;
	}
	
	public static boolean updateProxyPassword(String password){
		
		try{
	        NodeList list = document.getElementsByTagName("PROXYLOGIN");        
	        Element element = (Element)list.item(0);  
	        
	        element.getElementsByTagName("PASSWORD").item(0).getFirstChild().setNodeValue(password);     
	        return true; 
		}catch(Exception e) {   
			e.printStackTrace();	   
		}

		return false;
	}
	
	public static boolean updateDSNMemberAddress(String address){
		
		try{
	        NodeList list = document.getElementsByTagName("BETLOGIN");        
	        Element element = (Element)list.item(0);  
	        
	        element.getElementsByTagName("ADDRESS").item(0).getFirstChild().setNodeValue(address);     
	        return true;

		}catch(Exception e) {   
			e.printStackTrace();	   
		}

		return false;
	}
	
	public static boolean updateDSNMemberAccount(String account){
		
		try{
	        NodeList list = document.getElementsByTagName("BETLOGIN");        
	        Element element = (Element)list.item(0);  
	        
	        element.getElementsByTagName("ACCOUNT").item(0).getFirstChild().setNodeValue(account);     
	        return true;

		}catch(Exception e) {   
			e.printStackTrace();	   
		}

		return false;
	}
	
	
	public static boolean updateDSNMemberPassword(String password){
		
		try{
	        NodeList list = document.getElementsByTagName("BETLOGIN");        
	        Element element = (Element)list.item(0);  
	        
	        element.getElementsByTagName("PASSWORD").item(0).getFirstChild().setNodeValue(password);     
	        return true;

		}catch(Exception e) {   
			e.printStackTrace();	   
		}

		return false;
	}
	
	
	
	
	public static boolean updateWeiCaiMemberAddress(String address){
		
		try{
	        NodeList list = document.getElementsByTagName("WEICAIBETLOGIN");        
	        Element element = (Element)list.item(0);  
	        
	        element.getElementsByTagName("ADDRESS").item(0).getFirstChild().setNodeValue(address);     
	        return true;

		}catch(Exception e) {   
			e.printStackTrace();	   
		}

		return false;
	}
	
	public static boolean updateWeiCaiMemberAccount(String account){
		
		try{
	        NodeList list = document.getElementsByTagName("WEICAIBETLOGIN");        
	        Element element = (Element)list.item(0);  
	        
	        element.getElementsByTagName("ACCOUNT").item(0).getFirstChild().setNodeValue(account);     
	        return true;

		}catch(Exception e) {   
			e.printStackTrace();	   
		}

		return false;
	}
	
	
	public static boolean updateWeiCaiMemberPassword(String password){
		
		try{
	        NodeList list = document.getElementsByTagName("WEICAIBETLOGIN");        
	        Element element = (Element)list.item(0);  
	        
	        element.getElementsByTagName("PASSWORD").item(0).getFirstChild().setNodeValue(password);     
	        return true;

		}catch(Exception e) {   
			e.printStackTrace();	   
		}

		return false;
	}
	
	public static boolean updateTianCaiMemberAddress(String address){
		
		try{
	        NodeList list = document.getElementsByTagName("TIANCAIBETLOGIN");        
	        Element element = (Element)list.item(0);  
	        
	        element.getElementsByTagName("ADDRESS").item(0).getFirstChild().setNodeValue(address);     
	        return true;

		}catch(Exception e) {   
			e.printStackTrace();	   
		}

		return false;
	}
	
	public static boolean updateTianCaiMemberAccount(String account){
		
		try{
	        NodeList list = document.getElementsByTagName("TIANCAIBETLOGIN");        
	        Element element = (Element)list.item(0);  
	        
	        element.getElementsByTagName("ACCOUNT").item(0).getFirstChild().setNodeValue(account);     
	        return true;

		}catch(Exception e) {   
			e.printStackTrace();	   
		}

		return false;
	}
	
	
	public static boolean updateTianCaiMemberPassword(String password){
		
		try{
	        NodeList list = document.getElementsByTagName("TIANCAIBETLOGIN");        
	        Element element = (Element)list.item(0);  
	        
	        element.getElementsByTagName("PASSWORD").item(0).getFirstChild().setNodeValue(password);     
	        return true;

		}catch(Exception e) {   
			e.printStackTrace();	   
		}

		return false;
	}
	
	
	
	
	
	
	public static boolean updateLanyangMemberAddress(String address){
		
		try{
	        NodeList list = document.getElementsByTagName("LANYANGBETLOGIN");        
	        Element element = (Element)list.item(0);  
	        
	        element.getElementsByTagName("ADDRESS").item(0).getFirstChild().setNodeValue(address);     
	        return true;

		}catch(Exception e) {   
			e.printStackTrace();	   
		}

		return false;
	}
	
	public static boolean updateLanyangMemberAccount(String account){
		
		try{
	        NodeList list = document.getElementsByTagName("LANYANGBETLOGIN");        
	        Element element = (Element)list.item(0);  
	        
	        element.getElementsByTagName("ACCOUNT").item(0).getFirstChild().setNodeValue(account);     
	        return true;

		}catch(Exception e) {   
			e.printStackTrace();	   
		}

		return false;
	}
	
	
	public static boolean updateLanyangMemberPassword(String password){
		
		try{
	        NodeList list = document.getElementsByTagName("LANYANGBETLOGIN");        
	        Element element = (Element)list.item(0);  
	        
	        element.getElementsByTagName("PASSWORD").item(0).getFirstChild().setNodeValue(password);     
	        return true;

		}catch(Exception e) {   
			e.printStackTrace();	   
		}

		return false;
	}	
	
	public static boolean updateHuarunMemberAddress(String address){
		
		try{
	        NodeList list = document.getElementsByTagName("HUARUNBETLOGIN");        
	        Element element = (Element)list.item(0);  
	        
	        element.getElementsByTagName("ADDRESS").item(0).getFirstChild().setNodeValue(address);     
	        return true;

		}catch(Exception e) {   
			e.printStackTrace();	   
		}

		return false;
	}
	
	public static boolean updateHuarunMemberAccount(String account){
		
		try{
	        NodeList list = document.getElementsByTagName("HUARUNBETLOGIN");        
	        Element element = (Element)list.item(0);  
	        
	        element.getElementsByTagName("ACCOUNT").item(0).getFirstChild().setNodeValue(account);     
	        return true;

		}catch(Exception e) {   
			e.printStackTrace();	   
		}

		return false;
	}
	
	
	public static boolean updateHuarunMemberPassword(String password){
		
		try{
	        NodeList list = document.getElementsByTagName("HUARUNBETLOGIN");        
	        Element element = (Element)list.item(0);  
	        
	        element.getElementsByTagName("PASSWORD").item(0).getFirstChild().setNodeValue(password);     
	        return true;

		}catch(Exception e) {   
			e.printStackTrace();	   
		}

		return false;
	}	
	
	public static boolean updateYaboMemberAddress(String address){
		
		try{
	        NodeList list = document.getElementsByTagName("YABOBETLOGIN");        
	        Element element = (Element)list.item(0);  
	        
	        element.getElementsByTagName("ADDRESS").item(0).getFirstChild().setNodeValue(address);     
	        return true;

		}catch(Exception e) {   
			e.printStackTrace();	   
		}

		return false;
	}
	
	public static boolean updateYaboMemberAccount(String account){
		
		try{
	        NodeList list = document.getElementsByTagName("YABOBETLOGIN");        
	        Element element = (Element)list.item(0);  
	        
	        element.getElementsByTagName("ACCOUNT").item(0).getFirstChild().setNodeValue(account);     
	        return true;

		}catch(Exception e) {   
			e.printStackTrace();	   
		}

		return false;
	}
	
	
	public static boolean updateYaboMemberPassword(String password){
		
		try{
	        NodeList list = document.getElementsByTagName("YABOBETLOGIN");        
	        Element element = (Element)list.item(0);  
	        
	        element.getElementsByTagName("PASSWORD").item(0).getFirstChild().setNodeValue(password);     
	        return true;

		}catch(Exception e) {   
			e.printStackTrace();	   
		}

		return false;
	}	
	
	
	
	public static boolean updateWebetMemberAddress(String address){
		
		try{
	        NodeList list = document.getElementsByTagName("WEBETLOGIN");        
	        Element element = (Element)list.item(0);  
	        
	        element.getElementsByTagName("ADDRESS").item(0).getFirstChild().setNodeValue(address);     
	        return true;

		}catch(Exception e) {   
			e.printStackTrace();	   
		}

		return false;
	}
	
	public static boolean updateWebetMemberAccount(String account){
		
		try{
	        NodeList list = document.getElementsByTagName("WEBETLOGIN");        
	        Element element = (Element)list.item(0);  
	        
	        element.getElementsByTagName("ACCOUNT").item(0).getFirstChild().setNodeValue(account);     
	        return true;

		}catch(Exception e) {   
			e.printStackTrace();	   
		}

		return false;
	}
	
	
	public static boolean updateWebetMemberPassword(String password){
		
		try{
	        NodeList list = document.getElementsByTagName("WEBETLOGIN");        
	        Element element = (Element)list.item(0);  
	        
	        element.getElementsByTagName("PASSWORD").item(0).getFirstChild().setNodeValue(password);     
	        return true;

		}catch(Exception e) {   
			e.printStackTrace();	   
		}

		return false;
	}	
	
	
}

