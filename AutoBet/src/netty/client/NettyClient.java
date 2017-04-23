package netty.client;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import dsn.Client;


public class NettyClient {    
	String HOST = "127.0.0.1";
    int PORT = 12321;  

    Bootstrap bootstrap = null; 
    EventLoopGroup group = null;
    Channel channel = null;
    NettyClient me = this;
    Client client = null;
    boolean active = false;
    ReadWriteLock lock = new ReentrantReadWriteLock();
    
    public boolean isActive() {
    	lock.readLock().lock();
    	boolean res = active;
    	lock.readLock().unlock();
		return active;
	}

	public void setActive(boolean active) {
		lock.writeLock().lock();
		this.active = active;
		lock.writeLock().unlock();
	}

    /** 
     * 初始化Bootstrap 
     * @return 
     */  
    public NettyClient(Client client){
    	this.client = client;
    }
    
    public void SetAddress(String host, int port) {
    	HOST = host;
    	PORT = port;
    }
    public Bootstrap getBootstrap(){
    	if(group == null) {
    		group = new NioEventLoopGroup();
    	}
        Bootstrap b = new Bootstrap();  
        b.group(group).channel(NioSocketChannel.class);  
        b.handler(new ChannelInitializer<Channel>() {  
            @Override  
            protected void initChannel(Channel ch) throws Exception {  
                ChannelPipeline pipeline = ch.pipeline();  
                pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));  
                pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));  
                pipeline.addLast("decoder", new StringDecoder());  
                pipeline.addLast("encoder", new StringEncoder());  
                pipeline.addLast("handler", new NettyClientHandler(me));  
            }  
        });  
        b.option(ChannelOption.SO_KEEPALIVE, true);  
        return b;  
    }  
    
    public  boolean connect(){
    	bootstrap = getBootstrap();
    	try {  
    		channel = bootstrap.connect(HOST, PORT).sync().channel();
            System.out.println("【client】服务器连接成功");
            active = true;
        } catch (Exception e) {  
        	System.out.println("【client】服务器连接失败！！！！！！" );
        	//e.printStackTrace();
            return false; 
        }  
   
    	return true;
    } 
    
    public void send(String data) {
    	if(channel != null) {
    		channel.writeAndFlush(data);
    	}
    }
    
    public void parseData(String data) {
    	client.parseData(data);
    }
    
    public void sendRegister() {
    	client.sendRegister();
    }
    
} 
