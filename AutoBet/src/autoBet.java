

public class autoBet {
	public static void main(String[] args) throws Exception {
		
		//String content = zzLogin.doGet("http://a1.dio168.net/indexs2.php?p=QWERTYUNzIzOTc2NDY0MT14ZWRuaV84ODFvaWQmbmM9WEdOQUwmODgxb2lkPWxwdCZGMiVtb2MuODg4Nzc3YUYyJUYyJUEzJXB0dGg9aHI%253D");
		//dsnHttp.loginToDsn();   
		//测试  5秒读一次数据
		String data = DsnProxyGrab.doLogin(); //登录代理
		
		boolean res = dsnHttp.loginToDsn();	//登录会员
		
		boolean autoBetSuccess = false;
		
		if (res == true){
			while(true){
				long remainTime = dsnHttp.timeToBet();
				
					System.out.println("距离封盘时间为:");
					System.out.println(remainTime/1000);
				
					if(remainTime <= 0){//已经封盘,等四分钟后再次查询
						Thread.sleep(4*60*1000);//等四分钟
					}
					else{
						if(remainTime < 3*1000){//最后三秒秒去下注
							data = DsnProxyGrab.grabCQSSCdata("LM", "XZ", "");
							System.out.println("下单数据：");
							System.out.println(data);
							if(data != null){
								autoBetSuccess = dsnHttp.doBetCQSSC(data);
								Thread.sleep(4*1000);
							}
						}
						else{
							long sleepTime = remainTime - 3*1000;
							Thread.sleep(sleepTime);
						}
					}

		}
		}
		
		/*if(data != null) {
			for(int i = 0; i < 10; i++) {
				data = DsnProxyGrab.grabCQSSCdata("LM", "XZ", "");
				Thread.sleep(5000);
			}
		}*/

	}
}
