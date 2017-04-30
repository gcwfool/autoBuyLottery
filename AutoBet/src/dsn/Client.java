package dsn;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import netty.client.NettyClient;

import org.json.JSONException;
import org.json.JSONObject;

public class Client extends Thread{
	String [] dataCQSSC = {"", "", ""};
    String [] dataBJSC = {"", "", "", "", "", "", ""};
    String [] dataXYNC = {"", "", "", "", "","", "", "", "", "", ""};
    String [] dataGDKL = {"", "", "", "", "","", "", "", "", "", ""};
    String [] dataGXKL = {"", "", ""};
    String [] dataXJSSC = {"", "", ""};
    String [] dataTJSSC = {"", "", ""};
    String [] dataGD115 = {"", "", ""};
    String [] dataBJKL8 = {"", "", ""};
    boolean grabBJSC = true;
    boolean grabCQSSC = false;
    boolean grabXYNC = false;
    boolean grabGDKL = false;
    boolean grabGXKL = false;
    boolean grabXJSSC = false;
    boolean grabTJSSC = false;
    boolean grabGD115 = false;
    boolean grabBJKL8 = false;
    boolean betBJSCopen = false;
    ReadWriteLock lock = new ReentrantReadWriteLock();

    NettyClient client = null;
    
    
    public boolean connectToSever(String taddress, String tport){
    	client = new NettyClient(this);
    	int port = 0;
    	
    	if(Common.isNum(tport)){
    		port = Integer.parseInt(tport);
    	} else {
    		return false;
    	}
    	
    	client.SetAddress(taddress, port);
    	boolean res = client.connect();
    	
    	return res;
    }
    
    public void run() {
    	try {
    		while(true) {
    			try { 
    				if(!client.isActive()) {
    					client.connect();
    				}
    				
    				sendRegister();
		            
		            while(true) {
		            	if(!client.isActive()) {
		            		break;
		            	}
		            	boolean brokenBag = false;
		            	if(grabBJSC){
			            	Map<String, String> map = new HashMap<String, String>();
			                map.put("request", "data");
			                map.put("lottery", "BJSC");   
			                JSONObject json = new JSONObject(map);  
			                String request = json.toString();
				            
			                client.send(request);				           
		            	}
		            	
		            	/*if(grabCQSSC){
			            	Map<String, String> map = new HashMap<String, String>();  
			                map.put("request", "data");
			                map.put("lottery", "CQSSC");  
			                JSONObject json = new JSONObject(map);  
			                String request = json.toString();
				            ByteBuffer buffer = ByteBuffer.allocate(1024);
				            ByteBuffer buffer1 = ByteBuffer.allocate(30960);
				            buffer.put(request.getBytes());
				            buffer.flip();
					        client.write(buffer);
					        String content = "";
					        if(client.read(buffer1) == -1) {
					        	break;
					        }
					        content += new String(buffer1.array());
					        buffer1.clear();
					           
					        try {
					            json = new JSONObject(content);
					            if(json.getString("result").equals("true")) {
					            	
					            	String str1 = json.getString("drawNumber");
					            	String str2 = json.getString("remainTime");
					            	String str3 = json.getString("data");
					            	
					            	lock.writeLock().lock();
					            	dataCQSSC[0] = str1;
					            	dataCQSSC[2] = str2;
					            	dataCQSSC[1] = str3;
					            	lock.writeLock().unlock();
					            	//System.out.println("drawNumber:" + dataCQSSC[0]);
					            	//System.out.println("remainTime:" + dataCQSSC[2]);
					            } else {
					            	System.out.println("【client】获取数据失败");
					            }
					            	
					        } catch (JSONException e) {
					    		System.out.println("【client】数据包错误");
					    		e.printStackTrace();
					    		try {
					    			client.read(buffer1);
					    		} catch(IOException io) {
					    			io.printStackTrace();
					    		}
					    		brokenBag = true;
					    	}
		            	}
		            	
		            	if(grabXYNC){
			            	Map<String, String> map = new HashMap<String, String>();  
			                map.put("request", "data");
			                map.put("lottery", "XYNC");  
			                JSONObject json = new JSONObject(map);  
			                String request = json.toString();
				            ByteBuffer buffer = ByteBuffer.allocate(1024);
				            ByteBuffer buffer1 = ByteBuffer.allocate(30960);
				            buffer.put(request.getBytes());
				            buffer.flip();
					        client.write(buffer);
					        String content = "";
					        if(client.read(buffer1) == -1) {
					        	break;
					        }
					        content += new String(buffer1.array());
					        buffer1.clear();
					           
					        try {
					            json = new JSONObject(content);
					            if(json.getString("result").equals("true")) {
					            	
					            	String drawNumber = json.getString("drawNumber");
					            	String remainTime = json.getString("remainTime");
					            	String data1 = json.getString("data1");
					            	String data2 = json.getString("data2");
					            	String data3 = json.getString("data3");
					            	String data4 = json.getString("data4");
					            	String data5 = json.getString("data5");
					            	String data6 = json.getString("data6");
					            	String data7 = json.getString("data7");
					            	String data8 = json.getString("data8");
					            	String data9 = json.getString("data9");
					            	
					            	
					            	lock.writeLock().lock();
					            	dataXYNC[0] = drawNumber;
					            	dataXYNC[10] = remainTime;
					            	dataXYNC[1] = data1;
					            	dataXYNC[2] = data2;
					            	dataXYNC[3] = data3;
					            	dataXYNC[4] = data4;
					            	dataXYNC[5] = data5;
					            	dataXYNC[6] = data6;
					            	dataXYNC[7] = data7;
					            	dataXYNC[8] = data8;
					            	dataXYNC[9] = data9;
					            	lock.writeLock().unlock();
					            	//System.out.println("drawNumber:" + dataXYNC[0]);
					            	//System.out.println("remainTime:" + dataXYNC[2]);
					            } else {
					            	System.out.println("【client】获取数据失败");
					            }
					            	
					        } catch (JSONException e) {
					    		System.out.println("【client】数据包错误");
					    		e.printStackTrace();
					    		try {
					    			client.read(buffer1);
					    		} catch(IOException io) {
					    			io.printStackTrace();
					    		}
					    		brokenBag = true;
					    	}
		            	}
		            	
		            	if(grabGDKL){
			            	Map<String, String> map = new HashMap<String, String>();  
			                map.put("request", "data");
			                map.put("lottery", "GDKLSF");  
			                JSONObject json = new JSONObject(map);  
			                String request = json.toString();
				            ByteBuffer buffer = ByteBuffer.allocate(1024);
				            ByteBuffer buffer1 = ByteBuffer.allocate(30960);
				            buffer.put(request.getBytes());
				            buffer.flip();
					        client.write(buffer);
					        String content = "";
					        if(client.read(buffer1) == -1) {
					        	break;
					        }
					        content += new String(buffer1.array());
					        buffer1.clear();
					           
					        try {
					            json = new JSONObject(content);
					            if(json.getString("result").equals("true")) {
					            	
					            	String drawNumber = json.getString("drawNumber");
					            	String remainTime = json.getString("remainTime");
					            	String data1 = json.getString("data1");
					            	String data2 = json.getString("data2");
					            	String data3 = json.getString("data3");
					            	String data4 = json.getString("data4");
					            	String data5 = json.getString("data5");
					            	String data6 = json.getString("data6");
					            	String data7 = json.getString("data7");
					            	String data8 = json.getString("data8");
					            	String data9 = json.getString("data9");
					            	
					            	
					            	lock.writeLock().lock();
					            	dataGDKL[0] = drawNumber;
					            	dataGDKL[10] = remainTime;
					            	dataGDKL[1] = data1;
					            	dataGDKL[2] = data2;
					            	dataGDKL[3] = data3;
					            	dataGDKL[4] = data4;
					            	dataGDKL[5] = data5;
					            	dataGDKL[6] = data6;
					            	dataGDKL[7] = data7;
					            	dataGDKL[8] = data8;
					            	dataGDKL[9] = data9;
					            	lock.writeLock().unlock();
					            	//System.out.println("drawNumber:" + dataGDKL[0]);
					            	//System.out.println("remainTime:" + dataGDKL[2]);
					            } else {
					            	System.out.println("【client】获取数据失败");
					            }
					            	
					        } catch (JSONException e) {
					    		System.out.println("【client】数据包错误");
					    		e.printStackTrace();
					    		try {
					    			client.read(buffer1);
					    		} catch(IOException io) {
					    			io.printStackTrace();
					    		}
					    		brokenBag = true;
					    	}
		            	}
		            	
		            	if(grabGXKL){
			            	Map<String, String> map = new HashMap<String, String>();  
			                map.put("request", "data");
			                map.put("lottery", "GXKLSF");  
			                JSONObject json = new JSONObject(map);  
			                String request = json.toString();
				            ByteBuffer buffer = ByteBuffer.allocate(1024);
				            ByteBuffer buffer1 = ByteBuffer.allocate(30960);
				            buffer.put(request.getBytes());
				            buffer.flip();
					        client.write(buffer);
					        String content = "";
					        if(client.read(buffer1) == -1) {
					        	break;
					        }
					        content += new String(buffer1.array());
					        buffer1.clear();
					           
					        try {
					            json = new JSONObject(content);
					            if(json.getString("result").equals("true")) {
					            	
					            	String str1 = json.getString("drawNumber");
					            	String str2 = json.getString("remainTime");
					            	String str3 = json.getString("data");
					            	
					            	lock.writeLock().lock();
					            	dataGXKL[0] = str1;
					            	dataGXKL[2] = str2;
					            	dataGXKL[1] = str3;
					            	lock.writeLock().unlock();
					            	//System.out.println("drawNumber:" + dataGXKL[0]);
					            	//System.out.println("remainTime:" + dataGXKL[2]);
					            } else {
					            	System.out.println("【client】获取数据失败");
					            }
					            	
					        } catch (JSONException e) {
					    		System.out.println("【client】数据包错误");
					    		e.printStackTrace();
					    		try {
					    			client.read(buffer1);
					    		} catch(IOException io) {
					    			io.printStackTrace();
					    		}
					    		brokenBag = true;
					    	}
		            	}
		            	
		            	if(grabXJSSC){
			            	Map<String, String> map = new HashMap<String, String>();  
			                map.put("request", "data");
			                map.put("lottery", "XJSSC");  
			                JSONObject json = new JSONObject(map);  
			                String request = json.toString();
				            ByteBuffer buffer = ByteBuffer.allocate(1024);
				            ByteBuffer buffer1 = ByteBuffer.allocate(30960);
				            buffer.put(request.getBytes());
				            buffer.flip();
					        client.write(buffer);
					        String content = "";
					        if(client.read(buffer1) == -1) {
					        	break;
					        }
					        content += new String(buffer1.array());
					        buffer1.clear();
					           
					        try {
					            json = new JSONObject(content);
					            if(json.getString("result").equals("true")) {
					            	
					            	String str1 = json.getString("drawNumber");
					            	String str2 = json.getString("remainTime");
					            	String str3 = json.getString("data");
					            	
					            	lock.writeLock().lock();
					            	dataXJSSC[0] = str1;
					            	dataXJSSC[2] = str2;
					            	dataXJSSC[1] = str3;
					            	lock.writeLock().unlock();
					            } else {
					            	System.out.println("【client】获取数据失败");
					            }
					            	
					        } catch (JSONException e) {
					    		System.out.println("【client】数据包错误");
					    		e.printStackTrace();
					    		try {
					    			client.read(buffer1);
					    		} catch(IOException io) {
					    			io.printStackTrace();
					    		}
					    		brokenBag = true;
					    	}
		            	}
		            	
		            	if(grabTJSSC){
			            	Map<String, String> map = new HashMap<String, String>();  
			                map.put("request", "data");
			                map.put("lottery", "TJSSC");  
			                JSONObject json = new JSONObject(map);  
			                String request = json.toString();
				            ByteBuffer buffer = ByteBuffer.allocate(1024);
				            ByteBuffer buffer1 = ByteBuffer.allocate(30960);
				            buffer.put(request.getBytes());
				            buffer.flip();
					        client.write(buffer);
					        String content = "";
					        if(client.read(buffer1) == -1) {
					        	break;
					        }
					        content += new String(buffer1.array());
					        buffer1.clear();
					           
					        try {
					            json = new JSONObject(content);
					            if(json.getString("result").equals("true")) {
					            	
					            	String str1 = json.getString("drawNumber");
					            	String str2 = json.getString("remainTime");
					            	String str3 = json.getString("data");
					            	
					            	lock.writeLock().lock();
					            	dataTJSSC[0] = str1;
					            	dataTJSSC[2] = str2;
					            	dataTJSSC[1] = str3;
					            	lock.writeLock().unlock();
					            } else {
					            	System.out.println("【client】获取数据失败");
					            }
					            	
					        } catch (JSONException e) {
					    		System.out.println("【client】数据包错误");
					    		e.printStackTrace();
					    		try {
					    			client.read(buffer1);
					    		} catch(IOException io) {
					    			io.printStackTrace();
					    		}
					    		brokenBag = true;
					    	}
		            	}
		            	
		            	if(grabGD115){
			            	Map<String, String> map = new HashMap<String, String>();  
			                map.put("request", "data");
			                map.put("lottery", "GD11X5");  
			                JSONObject json = new JSONObject(map);  
			                String request = json.toString();
				            ByteBuffer buffer = ByteBuffer.allocate(1024);
				            ByteBuffer buffer1 = ByteBuffer.allocate(30960);
				            buffer.put(request.getBytes());
				            buffer.flip();
					        client.write(buffer);
					        String content = "";
					        if(client.read(buffer1) == -1) {
					        	break;
					        }
					        content += new String(buffer1.array());
					        buffer1.clear();
					           
					        try {
					            json = new JSONObject(content);
					            if(json.getString("result").equals("true")) {
					            	
					            	String str1 = json.getString("drawNumber");
					            	String str2 = json.getString("remainTime");
					            	String str3 = json.getString("data");
					            	
					            	lock.writeLock().lock();
					            	dataGD115[0] = str1;
					            	dataGD115[2] = str2;
					            	dataGD115[1] = str3;
					            	lock.writeLock().unlock();
					            } else {
					            	System.out.println("【client】获取数据失败");
					            }
					            	
					        } catch (JSONException e) {
					    		System.out.println("【client】数据包错误");
					    		e.printStackTrace();
					    		try {
					    			client.read(buffer1);
					    		} catch(IOException io) {
					    			io.printStackTrace();
					    		}
					    		brokenBag = true;
					    	}
		            	}
		            	
		            	if(grabBJKL8){
			            	Map<String, String> map = new HashMap<String, String>();  
			                map.put("request", "data");
			                map.put("lottery", "BJKL8");  
			                JSONObject json = new JSONObject(map);  
			                String request = json.toString();
				            ByteBuffer buffer = ByteBuffer.allocate(1024);
				            ByteBuffer buffer1 = ByteBuffer.allocate(30960);
				            buffer.put(request.getBytes());
				            buffer.flip();
					        client.write(buffer);
					        String content = "";
					        if(client.read(buffer1) == -1) {
					        	break;
					        }
					        content += new String(buffer1.array());
					        buffer1.clear();
					           
					        try {
					            json = new JSONObject(content);
					            if(json.getString("result").equals("true")) {
					            	
					            	String str1 = json.getString("drawNumber");
					            	String str2 = json.getString("remainTime");
					            	String str3 = json.getString("data");
					            	
					            	lock.writeLock().lock();
					            	dataBJKL8[0] = str1;
					            	dataBJKL8[2] = str2;
					            	dataBJKL8[1] = str3;
					            	lock.writeLock().unlock();
					            } else {
					            	System.out.println("【client】获取数据失败");
					            }
					            	
					        } catch (JSONException e) {
					    		System.out.println("【client】数据包错误");
					    		e.printStackTrace();
					    		try {
					    			client.read(buffer1);
					    		} catch(IOException io) {
					    			io.printStackTrace();
					    		}
					    		brokenBag = true;
					    	}
		            	}*/

			            
			            //System.out.println("sleep");
			            
		            	if(!brokenBag) {
			            	Thread.sleep(2000);
		            	}
			            else {
			            	Thread.sleep(800);
			            }
						
			            //System.out.println("end sleep");
				            
		            
		            }//while
    			} catch(Exception e) {
    				System.out.println("【client】重新建立连接");
    			}
    			Thread.sleep(5000);
    		}//while
    	}catch (Exception e) {
	        // TODO: handle exception
			//System.out.println("end sleep1");
	    }
    }
    
    public void sendRegister() {
    	Map<String, String> map = new HashMap<String, String>();  
        map.put("request", "register");
        map.put("account", "account");
        map.put("website", "dsn");
        
        JSONObject json = new JSONObject(map);  
        String request = json.toString();
        client.send(request); 
    }
    
    public void parseData(String data) {
    	try {
    		JSONObject json = new JSONObject(data);
            
            if(json.getString("result").equals("true") && json.has("lottery")) {
            	switch (json.getString("lottery")) {
            		case "BJSC":
                    	String str1 = json.getString("drawNumber");
                    	String str2 = json.getString("remainTime");
                    	String str3 = json.getString("dataGY");
                    	String str4 = json.getString("dataSSWL");
                    	String str5 = json.getString("dataQBJS");				            	
                    	String str6 = json.getString("percent");
                    	String str7 = json.getString("positive");
                    	
                    	String open = json.getString("open");
                    	
                    	
                    	lock.writeLock().lock();
                    	if(open.equals("y")) {
                    		betBJSCopen = true;
                    	} else {
                    		betBJSCopen = false;
                    	}
                    	dataBJSC[0] = str1;
                    	dataBJSC[4] = str2;
                    	dataBJSC[1] = str3;
                    	dataBJSC[2] = str4;
                    	dataBJSC[3] = str5;
                    	dataBJSC[5] = str6;
                    	dataBJSC[6] = str7;				            	
                    	lock.writeLock().unlock();	
                    break;
            	}            	
            } else {
            	System.out.println("【client】获取数据失败");
            }   	
        } catch (JSONException e) {
    		System.out.println("【client】数据包错误");
    		e.printStackTrace();
    	}
    }
    
    public boolean betBJSCopen() {
    	lock.readLock().lock();
    	boolean open =betBJSCopen;
    	lock.readLock().unlock();
    	return open;
    }
    
    public String [] getCQSSCdata() {
    	lock.readLock().lock();
    	String [] data = (String [])dataCQSSC.clone();
    	lock.readLock().unlock();
    	return data;
    }
    
    public String [] getBJSCdata() {
    	lock.readLock().lock();
    	String [] data = (String [])dataBJSC.clone();
    	lock.readLock().unlock();
    	return data;
    }
    
    public String [] getXYNCdata() {
    	lock.readLock().lock();
    	String [] data = (String [])dataXYNC.clone();
    	lock.readLock().unlock();
    	return data;
    }
    
    public String [] getGXKLdata() {
    	lock.readLock().lock();
    	String [] data = (String [])dataGXKL.clone();
    	lock.readLock().unlock();
    	return data;
    }
    
    public String [] getGDKLdata() {
    	lock.readLock().lock();
    	String [] data = (String [])dataGDKL.clone();
    	lock.readLock().unlock();
    	return data;
    }
    
    public String [] getGD115data() {
    	lock.readLock().lock();
    	String [] data = (String [])dataGD115.clone();
    	lock.readLock().unlock();
    	return data;
    }
    
    public String [] getBJKL8data() {
    	lock.readLock().lock();
    	String [] data = (String [])dataBJKL8.clone();
    	lock.readLock().unlock();
    	return data;
    }
    
    public String [] getXJSSCdata() {
    	lock.readLock().lock();
    	String [] data = (String [])dataXJSSC.clone();
    	lock.readLock().unlock();
    	return data;
    }
    
    public String [] getTJSSCdata() {
    	lock.readLock().lock();
    	String [] data = (String [])dataTJSSC.clone();
    	lock.readLock().unlock();
    	return data;
    }
    
    public void setRequestCQSSC(boolean req) {
    	if(req) {
    		grabCQSSC = true;
    	} else {
    		grabCQSSC = false;
    	}
    }
    
    public void setRequestBJSC(boolean req) {
    	if(req) {
    		grabBJSC = true;
    	} else {
    		grabBJSC = false;
    	}
    }
    
    public void setRequestXYNC(boolean req) {
    	if(req) {
    		grabXYNC = true;
    	} else {
    		grabXYNC = false;
    	}
    }
    
    public void setRequestGXKL(boolean req) {
    	if(req) {
    		grabGXKL = true;
    	} else {
    		grabGXKL = false;
    	}
    }
    
    public void setRequestGDKL(boolean req) {
    	if(req) {
    		grabGDKL = true;
    	} else {
    		grabGDKL = false;
    	}
    }
    
    public void setRequestTJSSC(boolean req) {
    	if(req) {
    		grabTJSSC = true;
    	} else {
    		grabTJSSC = false;
    	}
    }
    
    public void setRequestXJSSC(boolean req) {
    	if(req) {
    		grabXJSSC = true;
    	} else {
    		grabXJSSC = false;
    	}
    }
    
    public void setRequestGD115(boolean req) {
    	if(req) {
    		grabGD115 = true;
    	} else {
    		grabGD115 = false;
    	}
    }
    
    public void setRequestBJKL8(boolean req) {
    	if(req) {
    		grabBJKL8 = true;
    	} else {
    		grabBJKL8 = false;
    	}
    }
    
}