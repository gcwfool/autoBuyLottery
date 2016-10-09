import java.net.*;
import java.nio.*;
import java.nio.channels.*;

import org.json.*;

import java.util.HashMap;  
import java.util.Map;
import java.io.IOException;
import java.util.concurrent.locks.ReadWriteLock;  
import java.util.concurrent.locks.ReentrantReadWriteLock; 

public class Client extends Thread{
	String [] dataCQSSC = {"", "", ""};
    String [] dataBJSC = {"", "", "", "", ""};
    boolean grabBJSC = true;
    boolean grabCQSSC = true;
    ReadWriteLock lock = new ReentrantReadWriteLock();
    
    public void run() {
    	try {
    		while(true) {
    			SocketChannel client = null;
    			try {   
    			        client = SocketChannel.open();
    			        if(!client.connect(new InetSocketAddress("123.207.90.66",12321))){
    			            continue;
    			        }
    			
    			    client.socket().setSoTimeout(2000);
    			    //发送注册信息
    			    boolean reg = false;
		            {
		            	Map<String, String> map = new HashMap<String, String>();  
		                map.put("request", "register");
		                map.put("account", "account");
		                map.put("website", "dsn");
		                //将json转化为String类型    
		                JSONObject json = new JSONObject(map);  
		                String request = json.toString();
			            ByteBuffer buffer = ByteBuffer.allocate(1024);
			            ByteBuffer buffer1 = ByteBuffer.allocate(20480);
			            buffer.put(request.getBytes());
			            buffer.flip();
				        client.write(buffer);
				        System.out.println("发送数据: " + new String(buffer.array()));
				        String content = "";
				        if(client.read(buffer1) == -1) {
				        	continue;
				        }
				        content += new String(buffer1.array());
				        System.out.println("接收数据: " + buffer1.position());
				        buffer1.clear();
				           
				        try {
				            json = new JSONObject(content);
				            
				            if(json.getString("result").equals("true")) {
				            	System.out.println("注册成功");
				            	reg = true;
				            } else {
				            	System.out.println("注册失败");
				            }
				            	
				        } catch (JSONException e) {
				    		System.out.println("注册数据包错误");
				    	}
				        if(!reg) {
				        	continue;
				        }
	            	}
		            while(true) {
		            	if(grabBJSC){
			            	Map<String, String> map = new HashMap<String, String>();  
			                map.put("request", "data");
			                map.put("lottery", "BJSC");
			                //将json转化为String类型    
			                JSONObject json = new JSONObject(map);  
			                String request = json.toString();
				            ByteBuffer buffer = ByteBuffer.allocate(1024);
				            ByteBuffer buffer1 = ByteBuffer.allocate(20480);
				            buffer.put(request.getBytes());
				            buffer.flip();
					        client.write(buffer);
					        System.out.println("发送数据: " + new String(buffer.array()));
					        String content = "";
					        if(client.read(buffer1) == -1) {
					        	break;
					        }
					        content += new String(buffer1.array());
					        System.out.println("接收数据: " + buffer1.position());
					        buffer1.clear();
					       
					           
					        try {
					            json = new JSONObject(content);
					            
					            if(json.getString("result").equals("true")) {
					            	lock.writeLock().lock();
					            	dataBJSC[0] = json.getString("drawNumber");
					            	dataBJSC[4] = json.getString("remainTime");
					            	dataBJSC[1] = json.getString("dataGY");
					            	dataBJSC[2] = json.getString("dataSSWL");
					            	dataBJSC[3] = json.getString("dataQBJS");
					            	lock.writeLock().unlock();
					            	System.out.println("drawNumber:" + dataBJSC[0]);
					            	System.out.println("remainTime:" + dataBJSC[4]);
					            	
					            } else {
					            	System.out.println("获取数据失败");
					            }
					            	
					        } catch (JSONException e) {
					    		System.out.println("数据包错误");
					    	}
		            	}
		            	
		            	if(grabCQSSC){
			            	Map<String, String> map = new HashMap<String, String>();  
			                map.put("request", "data");
			                map.put("lottery", "CQSSC");
			                //将json转化为String类型    
			                JSONObject json = new JSONObject(map);  
			                String request = json.toString();
				            ByteBuffer buffer = ByteBuffer.allocate(1024);
				            ByteBuffer buffer1 = ByteBuffer.allocate(20480);
				            buffer.put(request.getBytes());
				            buffer.flip();
					        client.write(buffer);
					        System.out.println("发送数据: " + new String(buffer.array()));
					        String content = "";
					        if(client.read(buffer1) == -1) {
					        	break;
					        }
					        content += new String(buffer1.array());
					        System.out.println("接收数据: " + buffer1.position());
					        buffer1.clear();
					           
					        try {
					            json = new JSONObject(content);
					            if(json.getString("result").equals("true")) {
					            	lock.writeLock().lock();
					            	dataCQSSC[0] = json.getString("drawNumber");
					            	dataCQSSC[2] = json.getString("remainTime");
					            	dataCQSSC[1] = json.getString("data");
					            	lock.writeLock().unlock();
					            	System.out.println("drawNumber:" + dataCQSSC[0]);
					            	System.out.println("remainTime:" + dataCQSSC[2]);
					            } else {
					            	System.out.println("获取数据失败");
					            }
					            	
					        } catch (JSONException e) {
					    		System.out.println("数据包错误");
					    	}
		            	}

			            
			            System.out.println("sleep");
			            
			            Thread.sleep(2000);
						
			            System.out.println("end sleep");
				            
		            
		            }//while
    			} catch(IOException e) {
    				System.out.println("重新建立连接");
    			}
    		}//while
    	}catch (InterruptedException e) {
	        // TODO: handle exception
			System.out.println("end sleep1");
	    }
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
    
}