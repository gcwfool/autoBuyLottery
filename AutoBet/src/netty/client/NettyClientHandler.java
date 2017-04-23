package netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {  
	
	NettyClient nettyClient = null;
	
	public NettyClientHandler(NettyClient nettyClient) {
		this.nettyClient = nettyClient;
	}

    @Override  
    public void channelRead(ChannelHandlerContext ctx, Object msg)  
            throws Exception {     
    	nettyClient.parseData((String)msg);  	
    }  
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("【client】--- Server is active ---");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("【client】--- Server is inactive ---"); 
        nettyClient.setActive(false);
    }
}  
