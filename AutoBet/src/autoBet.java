

public class autoBet {
	public static void main(String[] args) throws Exception {
		
		//String content = zzLogin.doGet("http://a1.dio168.net/indexs2.php?p=QWERTYUNzIzOTc2NDY0MT14ZWRuaV84ODFvaWQmbmM9WEdOQUwmODgxb2lkPWxwdCZGMiVtb2MuODg4Nzc3YUYyJUYyJUEzJXB0dGg9aHI%253D");
		//dsnHttp.loginToDsn();   
		//测试  5秒读一次数据\
		ConfigReader.read("common.config");
		//if(DsnProxyGrab.doLogin() == false) { //登录代理
			//return ;
		//}
		
		boolean res = false;
		while(DsnProxyGrab.doLogin() == false) {
		}
		

		while(dsnHttp.loginToDsn() == false);
		
		boolean autoBetSuccess = false;
		
		while(true){
			long remainTime = dsnHttp.getRemainTime();
			
			if(remainTime > 10*60*1000){//获取时间失败
				while(dsnHttp.loginToDsn() == false); //重新登录
				remainTime = dsnHttp.getRemainTime();
			}
			
			System.out.println("距离封盘时间为:");
			System.out.println(remainTime/1000);
				
			if(remainTime <= 0){//已经封盘,等四分钟后再次查询
				Thread.sleep(4*60*1000);
			}
			else{
				if(remainTime < 3*1000){//最后三秒秒去下注
					String data = DsnProxyGrab.grabCQSSCdata("LM", "XZ", "");
					if(data == null || data.length() <= 0){//获取数据失败
						while(DsnProxyGrab.doLogin() == false); //重新连接
						data = DsnProxyGrab.grabCQSSCdata("LM", "XZ", "");
					}
					System.out.println("下单数据：");
					System.out.println(data);
					if(data != null){
					autoBetSuccess = dsnHttp.doBetCQSSC(data);
					Thread.sleep(4*1000);
					}
				}
				else{
					//long sleepTime = remainTime - 3*1000;
					
					String data = DsnProxyGrab.grabCQSSCdata("LM", "XZ", "");
					if(data == null || data.length() <= 0){//获取数据失败
						while(DsnProxyGrab.doLogin() == false); //重新连接
						data = DsnProxyGrab.grabCQSSCdata("LM", "XZ", "");
					}
					System.out.println("下单数据：");
					System.out.println(data);
					
					long sleepTime = 10*1000;
					
					if(remainTime <= 10*1000){
						sleepTime = remainTime - 3*1000;
					}
					Thread.sleep(sleepTime);
				}
			}

		}
		
	}
}
