

public class autoBet {
	public static void main(String[] args) throws Exception {
		
		//String content = zzLogin.doGet("http://a1.dio168.net/indexs2.php?p=QWERTYUNzIzOTc2NDY0MT14ZWRuaV84ODFvaWQmbmM9WEdOQUwmODgxb2lkPWxwdCZGMiVtb2MuODg4Nzc3YUYyJUYyJUEzJXB0dGg9aHI%253D");
		//dsnHttp.loginToDsn();   
		//测试  5秒读一次数据
		String data = DsnProxyGrab.doLogin();
		if(data != null) {
			for(int i = 0; i < 3; i++) {
					//data = DsnProxyGrab.grabCQSSCdata("LM", "XZ", "");
					//data = DsnProxyGrab.grabCQSSCdata("DH", "XZ", "");
					//data = DsnProxyGrab.grabCQSSCdata("QZHS", "XZ", "");
				Thread.sleep(10000);
			}
		}
	}
}
